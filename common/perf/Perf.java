package common.perf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import common.server.Facility;
import common.util.AlphanumComparator;
import src_ko.database.DbUtil;

public abstract class Perf implements Comparable {
	
	public static final String GET_PERFS = 
			"SELECT \r\n" + 
			"	fac.NODE_INDEX AS 'facIndex',\r\n" + 
			"	perf.nPerfIndex AS 'perfIndex',\r\n" + 
			"	perf.nPerfType AS 'perfType',\r\n" + 
			"	perf.strDisplayName AS 'displayName',\r\n" + 
			"	perf.strPerfCounter AS 'perfCounter',\r\n" + 
			"	perf.nPerfInterval AS 'interval',\r\n" + 
			"	perf.strMeasure AS 'unit',\r\n" + 
			"	perf.strOperation AS 'scale',\r\n" + 
			"	perf.DATA_FORMAT AS 'dataFormat'\r\n" + 
			"FROM SERVER_PERF perf \r\n" + 
			"	INNER JOIN SERVERINFO_FACILITY fac ON perf.nServerIndex = fac.NODE_INDEX\r\n" + 
			"	WHERE fac.NODE_INDEX = ?";
	
	public static final String GET_PERF_LABEL_BOOLEAN = 
			"SELECT \r\n" + 
			"DISTINCT \r\n" + 
			"	label.PERF_INDEX AS 'perfIndex',\r\n" + 
			"	label.LABEL0 AS 'value0',\r\n" + 
			"	label.LABEL1 AS 'value1'\r\n" + 
			"FROM PERF_LABEL_BOOLEAN label INNER JOIN SERVER_PERF perf ON label.PERF_INDEX = perf.nPerfIndex\r\n" + 
			"	WHERE perf.nPerfIndex \r\n" + 
			"		IN ( \r\n" + 
			"		SELECT nPerfIndex \r\n" + 
			"			FROM SERVER_PERF perf INNER JOIN  SERVERINFO_FACILITY fac \r\n" + 
			"			ON perf.nServerIndex = fac.NODE_INDEX WHERE fac.NODE_INDEX = ? AND perf.DATA_FORMAT = 1 \r\n" + 
			"		)";
	
	public static final String GET_PERF_LABEL_STATUS = 
			"SELECT \r\n" + 
			"DISTINCT \r\n" + 
			"	label.PERF_INDEX AS 'perfIndex',\r\n" + 
			"	label.VALUE AS 'value',\r\n" + 
			"	label.LABEL AS 'label'\r\n" + 
			"FROM PERF_LABEL_STATUS label INNER JOIN SERVER_PERF perf ON label.PERF_INDEX = perf.nPerfIndex\r\n" + 
			"	WHERE perf.nPerfIndex \r\n" + 
			"		IN ( \r\n" + 
			"		SELECT nPerfIndex \r\n" + 
			"			FROM SERVER_PERF perf INNER JOIN  SERVERINFO_FACILITY fac \r\n" + 
			"			ON perf.nServerIndex = fac.NODE_INDEX WHERE fac.NODE_INDEX = ? AND perf.DATA_FORMAT = 2\r\n" + 
			"		)";
	
	public int index;
	public int perfType;
	public String perfTypeString;
	public Object lastValue;
	public Object lastValueTime;
	
	public int getIndex() { return index; }
	public void setIndex(int index) { this.index = index; }
	public Object getLastValue() { return lastValue; }
	public void setLastValue(Object lastValue) { this.lastValue = lastValue; }
	public Object getLastValueTime() { return lastValueTime; }
	public void setLastValueTime(Object lastValueTime) { this.lastValueTime = lastValueTime; }
	public int getPerfType() { return perfType; }
	public void setPerfType(int perfType) { this.perfType = perfType; }
	public String getPerfTypeString() { return perfTypeString; }	
	public void setPerfTypeString(String perfTypeString) { this.perfTypeString = perfTypeString; }
	public abstract String getDisplayName();
	public abstract String getCounter();
	public abstract int getInterval();
	public abstract String getMeasure();
	public abstract String getScaleFunction();
	public abstract int getDataFormat();
	public abstract String[] getBinLabel();
	public abstract PerfLabelStatusBean[] getStatusLabels();
	public abstract common.perf.FmsPerfItem.EventInfo[] getFmsEventInfo();
	public abstract common.perf.SnmpPerfItem.EventInfo[] getSnmpEventInfo();
	public abstract int getEnable();
	
	public static ArrayList<FmsPerfItem> getFaciltiyPerfList(Connection conn, Facility fac){
		ArrayList<FmsPerfItem> perfList = new ArrayList<FmsPerfItem>();
		HashMap<Integer, FmsPerfItem> perfMap = new HashMap<Integer, FmsPerfItem>();
		
		try {
			
			/* 성능 인스턴스 초기화 */
			PreparedStatement pstmt = conn.prepareStatement(GET_PERFS);
			pstmt.setInt(1, fac.getIndex());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				try {
					FmsPerfItem perf = new FmsPerfItem();
					perf.index = rs.getInt("perfIndex");
					perf.perfType = rs.getInt("perfType");
					perf.perfTypeString = DbUtil.getPerfType(perf.perfType);
					perf.displayName = rs.getString("displayName");
					perf.counter = rs.getString("perfCounter");
					perf.interval = rs.getInt("interval");
					perf.measure = rs.getString("unit");
					perf.scaleFunc = rs.getString("scale");
					perf.dataFormat = rs.getInt("dataFormat");
					
					perfList.add(perf);
					perfMap.put(perf.index, perf);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			/* 이진 상태 초기화 */
			pstmt = conn.prepareStatement(GET_PERF_LABEL_BOOLEAN);
			pstmt.setInt(1, fac.getIndex());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				try {
					int perfIndex = rs.getInt("perfIndex");
					if(perfMap.containsKey(perfIndex)) {
						FmsPerfItem perf =  perfMap.get(perfIndex);
						if(perf.getDataFormat() == 1) {
							perf.binLabel = new String[2];
							perf.binLabel[0] = rs.getString("value0");
							perf.binLabel[1] = rs.getString("value1");
						}
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			/* 다중 상태 초기화 */
			pstmt = conn.prepareStatement(GET_PERF_LABEL_STATUS);
			pstmt.setInt(1, fac.getIndex());
			rs = pstmt.executeQuery();			
						
			while(rs.next()) {								
				try {					
					PerfLabelStatusBean bean = new PerfLabelStatusBean();
					int perfIndex = rs.getInt("perfIndex");				
					bean.value = rs.getInt("value");
					bean.label = rs.getString("label");
					
					if(perfMap.containsKey(perfIndex)) {						
						FmsPerfItem perf = perfMap.get(perfIndex);
						if(perf.getDataFormat() == 2) {
							perf.labelList.add(bean);	
						}
					}					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			for(int i = 0; i < perfList.size(); i++) {
				FmsPerfItem perf = perfList.get(i);
				if(perf.getDataFormat() == 2) {
					perf.setStatusLabels();
				}
			}
			
			Collections.sort(perfList);
			return perfList;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public int compareTo(Object obj) {
		Perf perf = (Perf) obj;
		int compareName = AlphanumComparator.comparator.compare(this.getDisplayName(), perf.getDisplayName());

		if(compareName < 0) {
			return -1;
		}else if(compareName == 0) {
			return 0;
		}else {
			return 1;
		}
	}
	
}
