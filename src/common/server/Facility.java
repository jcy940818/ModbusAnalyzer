package common.server;

import java.util.ArrayList;
import java.util.HashMap;

import common.perf.Perf;
import src_ko.info.Protocol;

public class Facility extends Server implements Comparable{
	
	// 실제 데이터베이스의 시설물 개수와 차이가 나는 이유는
	// 그룹에 속하지 않은 데이터베이스에만 존재하는 장비가 존재하기 때문이다
	// 아래의 쿼리는 실제 그룹과 매핑되어 AdminConsole UI에서 확인이 가능한 시설물만 조회한다
	public static final String GET_FACILITY = 
			"select distinct\r\n" + 
			"	si.strServerIP as 'ip',\r\n" + 
			"	fac.RTU_PORT_NUM as 'port',\r\n" + 
			"	fac.RTU_INDEX as 'rtuIndex',\r\n" + 
			"	si.nAgentType AS 'agentType',\r\n" + 
			"	si.nServerIndex as 'index',\r\n" + 
			"	fac.FACILITY_TYPE as 'facType',\r\n" + 
			"	si.strServerName as 'name',\r\n" + 
			"	fac.CONN_METHOD as 'connMethod',\r\n" + 
			"	fac.COMM_PROTOCOL as 'commProtocol',\r\n" + 
			"	fac.SNMP_MIB as 'snmpProtocol',\r\n" + 
			"	si.SERVER_CONDITION as 'condition'\r\n" + 
			" \r\n" + 
			"from SERVERINFO si\r\n" + 
			"	inner join SERVERGROUPMAP map on si.nServerIndex = map.nServerIndex\r\n" + 
			"	inner join SERVERINFO_FACILITY fac ON si.nServerIndex = fac.NODE_INDEX\r\n" + 
			"\r\n" + 
			"order by si.nServerIndex";
	
	ArrayList<Perf> perfList = new ArrayList<Perf>();
	HashMap<Integer, Perf> perfMap = new HashMap<Integer, Perf>();
	
	private boolean connRCU = false; 
	private RCU rcu; // 시리얼 포트 연결 방식일 경우 바라보는 RCU
	private int port; // 시리얼 포트 연결 방식일 경우 RCU의 포트 채널 번호
	private int rtuIndex;
	private int rcuPortCh;
	
	private int connCode; // 연결 방식
	private String connMethod;
	
	private boolean isProtocol; // 프로토콜 정보
	private int commProtocol;
	private int snmpProtocol;		

	public boolean isConnRCU() {
		return connRCU;
	}

	public void setConnRCU(boolean connRCU) {
		this.connRCU = connRCU;
	}

	public RCU getRcu() {
		return rcu;
	}

	public void setRcu(RCU rcu) {
		this.rcu = rcu;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getRtuIndex() {
		return rtuIndex;
	}

	public void setRtuIndex(int rtuIndex) {
		this.rtuIndex = rtuIndex;
	}

	public int getRcuPortCh() {
		return rcuPortCh;
	}

	public void setRcuPortCh(int rcuPortCh) {
		this.rcuPortCh = rcuPortCh;
	}

	public int getConnCode() {
		return connCode;
	}

	public void setConnCode(int connCode) {
		this.connCode = connCode;
	}

	public String getConnMethod() {
		return connMethod;
	}

	public void setConnMethod(String connMethod) {
		this.connMethod = connMethod;
	}

	public boolean isProtocol() {
		return isProtocol;
	}

	public void setIsProtocol(boolean isProtocol) {
		this.isProtocol = isProtocol;
	}
	
	public int getCommProtocol() {
		return commProtocol;
	}

	public void setCommProtocol(int commProtocol) {
		this.commProtocol = commProtocol;
	}

	public int getSnmpProtocol() {
		return snmpProtocol;
	}

	public void setSnmpProtocol(int snmpProtocol) {
		this.snmpProtocol = snmpProtocol;
	}

	public ArrayList<Perf> getPerfList() {
		return perfList;
	}

	public void setPerfList(ArrayList<Perf> perfList) {
		this.perfList = perfList;
	}

	public HashMap<Integer, Perf> getPerfMap() {
		return perfMap;
	}

	public void setPerfMap(HashMap<Integer, Perf> perfMap) {
		this.perfMap = perfMap;
	}

	public String getProtocolKey() {
		int protocolType = this.isProtocol() ? Protocol.PROTOCOL : Protocol.SNMP;
		int facCode = this.getType();
		int protocolNumber = this.isProtocol() ? this.getCommProtocol() : this.getSnmpProtocol();
		return String.format("%d-%d-%d", protocolType, facCode, protocolNumber);
	}

}
