package common.server;

import java.util.ArrayList;

import common.perf.Perf;

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
			"	replace(c.depth_fullname,'<ROOT>','장비 관리 ( 그룹 없음 )') as 'groupInfo',	\r\n" + 
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
	
	private int connCode; // 연결 방식
	private String connMethod;
	
	private boolean isCommon; // 프로토콜 정보
	private int commProtocol;
	private int snmpProtocol;
	
	private ArrayList<Perf> perfs;

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

	public boolean isCommon() {
		return isCommon;
	}

	public void setCommon(boolean isCommon) {
		this.isCommon = isCommon;
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
	
	public ArrayList<Perf> getPerfs() {
		return perfs;
	}

	public void setPerfs(ArrayList<Perf> perfs) {
		this.perfs = perfs;
	}

}
