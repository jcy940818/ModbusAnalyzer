package common.agent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import common.perf.Perf;

public class PerfData implements Comparable{
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	int index;
	Object value;
	long time;
	String timeString;
	
	public int getIndex() {
		return index;
	}
	public Object getValue() {
		return value;
	}
	public long getTime() {
		return time;
	}
	public String getTimeString() {
		return timeString;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public void setValue(Object value) {
		if(value.toString().equals("NaN")) {
			this.value = "-";
		}else {
			this.value = value;	
		}
	}
	public void setTime(long time) {
		this.time = time;
		if(time != -1) {
			this.timeString = sdf.format(new Date(time));
		}else {
			this.timeString = "-";
		}
	}
		
	public static PerfData MIN(Perf perf, ArrayList<PerfData> rowDataList) {
		PerfData minValuePerf = null;
		double min = 0.0;
		
		 if(perf.getDataFormat() == 3) {
			 try {
				 for(int i = 0; i < rowDataList.size(); i++) {
					 PerfData perfData = rowDataList.get(i);
					 double doubleValue = Double.parseDouble(perfData.getValue().toString());
					 if(min > doubleValue) {
						 min = doubleValue;
						 minValuePerf = perfData;
					 }
				 }
			 }catch(Exception e) {
				 e.printStackTrace();
				 return null;
			 }
			 
		 }else {
			 return null;
		 }
		 
		 return minValuePerf;
	}
	
	public static PerfData MAX(Perf perf, ArrayList<PerfData> rowDataList) {
		PerfData maxValuePerf = null;
		double max = 0.0;
		
		 if(perf.getDataFormat() == 3) {
			 try {
				 for(int i = 0; i < rowDataList.size(); i++) {
					 PerfData perfData = rowDataList.get(i);
					 double doubleValue = Double.parseDouble(perfData.getValue().toString());
					 if(max < doubleValue) {
						 max = doubleValue;
						 maxValuePerf = perfData;
					 }
				 }
			 }catch(Exception e) {
				 e.printStackTrace();
				 return null;
			 }
			 
		 }else {
			 return null;
		 }
		 
		 return maxValuePerf;
	}
	
	public static Double AVG(Perf perf, ArrayList<PerfData> rowDataList) {		
		double avg = 0.0;
		
		 if(perf.getDataFormat() == 3) {
			 try {
				 for(int i = 0; i < rowDataList.size(); i++) {
					 PerfData perfData = rowDataList.get(i);
					 avg += Double.parseDouble(perfData.getValue().toString());
				 }
			 }catch(Exception e) {
				 e.printStackTrace();
				 return null;
			 }
			 
		 }else {
			 return null;
		 }
		 
		 return (avg / rowDataList.size());
	}
	
		
	@Override
	/**
	 * 정렬 기준 : 가장 최근에 수집된 데이터부터
	 */
	public int compareTo(Object obj) {
		PerfData perfData = (PerfData)obj;
		
		if(this.time > perfData.time) {
			return -1;
		}else if(this.time == perfData.time) {
			return 0;
		}else {
			return 1;
		}
	}
	
}
