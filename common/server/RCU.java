package common.server;

import java.util.ArrayList;

public class RCU extends Server{

	public static final String GET_RTU = "WITH tree_query AS \r\n" + 
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
			"	a.strServerIP as 'ip',\r\n" + 
			"	rtu.PASSIVE_TCP_SERVER_PORT as 'port',	\r\n" + 
			"	a.nAgentType AS 'agentType',\r\n" + 
			"	a.nServerIndex as 'index',\r\n" + 
			"	rtu.RTU_TYPE as 'rtuType',\r\n" + 
			"	a.strServerName as 'name',\r\n" + 
			"	a.SERVER_CONDITION as 'condition'\r\n" + 
			" \r\n" + 
			"from SERVERINFO a inner join SERVERGROUPMAP b on a.nServerIndex=b.nServerIndex\r\n" + 
			"	inner join tree_query c on b.nGroupIndex = c.ngroupIndex\r\n" + 
			"	inner join SERVERINFO_RTU rtu ON a.nServerIndex = rtu.NODE_INDEX\r\n" + 
			" order by a.nServerIndex";	
	
	private boolean isMultiPort = false;
	private int port;
	private String rcuTypeDetail;
	private ArrayList<Server> facList = new ArrayList<Server>();
	private ArrayList<MultiPortMap> multiPortMapList = new ArrayList<MultiPortMap>();
	
	public boolean isMultiPort() {
		return isMultiPort;
	}
	public void setMultiPort(boolean isMultiPort) {
		this.isMultiPort = isMultiPort;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public ArrayList<Server> getFacList() {
		return facList;
	}
	public String getRcuTypeDetail() {
		return rcuTypeDetail;
	}
	public void setRcuTypeDetail(String rcuTypeDetail) {
		this.rcuTypeDetail = rcuTypeDetail;
	}
	public void setFacList(ArrayList<Server> facList) {
		this.facList = facList;
	}
	public ArrayList<MultiPortMap> getMultiPortMapList() {
		return multiPortMapList;
	}
	public void setMultiPortMapList(ArrayList<MultiPortMap> multiPortMapList) {
		this.multiPortMapList = multiPortMapList;
	}
	
}


