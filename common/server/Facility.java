package common.server;

import java.util.ArrayList;
import java.util.HashMap;

import common.perf.Perf;
import src_ko.info.Protocol;

public class Facility extends Server implements Comparable{
	
	public static final String GET_FACILITY = 
			"WITH tree_query AS \r\n" + 
			"( SELECT nGroupIndex , nParentIndex , strGroupName \r\n" + 
			", convert(varchar(255), nGroupIndex) sort \r\n" + 
			", convert(varchar(255), strGroupName) depth_fullname \r\n" + 
			"FROM SERVERGROUP WHERE nParentIndex = -1\r\n" + 
			"UNION ALL SELECT B.nGroupIndex , B.nParentIndex , B.strGroupName \r\n" + 
			", convert(varchar(255), convert(nvarchar,C.sort) + ' > ' + convert(varchar(255), B.nGroupIndex)) sort\r\n" + 
			", convert(varchar(255), convert(nvarchar,C.depth_fullname) + ' > ' + convert(varchar(255), B.strGroupName)) depth_fullname \r\n" + 
			"FROM SERVERGROUP B, tree_query C \r\n" + 
			"WHERE B.nParentIndex = C.nGroupIndex) \r\n" + 
			"\r\n" + 
			"select \r\n" + 
			"	DISTINCT\r\n" + 
			"	replace(c.depth_fullname,'<ROOT>','장비 관리 ( 그룹 없음 )') as 'groupInfo',	\r\n" + 
			"	a.strServerIP as 'ip',\r\n" + 
			"	f.RTU_PORT_NUM as 'port',\r\n" + 
			"	f.RTU_INDEX as 'rtuIndex',\r\n" + 
			"	a.nAgentType AS 'agentType',\r\n" + 
			"	a.nServerIndex as 'index',\r\n" + 
			"	f.FACILITY_TYPE as 'facType',\r\n" + 
			"	a.strServerName as 'name',\r\n" + 
			"	f.CONN_METHOD as 'connMethod',\r\n" + 
			"	f.COMM_PROTOCOL as 'commProtocol',\r\n" + 
			"	f.SNMP_MIB as 'snmpProtocol',\r\n" + 
			"	a.SERVER_CONDITION as 'condition'\r\n" + 
			" \r\n" + 
			"from SERVERINFO a inner join SERVERGROUPMAP b on a.nServerIndex=b.nServerIndex\r\n" + 
			"	inner join tree_query c on b.nGroupIndex = c.ngroupIndex\r\n" + 
			"	inner join SERVERINFO_FACILITY f ON a.nServerIndex = f.NODE_INDEX\r\n" + 
			" order by a.nServerIndex";
	
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
