package common.server;

import java.util.ArrayList;

public class RCU extends Server{

	/* 01 */ public static final int RTU_TYPE_DY_MUX = 3;				// MK_RCU_V1.0
	/* 02 */ public static final int RTU_TYPE_TCPIP = 5;				// TCP/IP_RCU
	/* 03 */ public static final int RTU_TYPE_REM2408 = 6;				// MK119_-_REM_2408
	/* 04 */ public static final int RTU_TYPE_REM1204 = 9;				// MK119_-_REM_1204
	/* 05 */ public static final int RTU_TYPE_REM1204v103 = 11;			// MK119_-_REM_1204_v1.0.3
	/* 06 */ public static final int RTU_TYPE_PASSIVE_TCP_SERVER = 12; 	// Passive_TCP/IP_Server
	/* 07 */ public static final int RTU_TYPE_LSIS_XGT_PLC = 13; 		// LSIS_XGT_PLC
	/* 08 */ public static final int RTU_TYPE_POSCO_NARAH = 14;			// PoscoICT_HVAC_SI
	/* 09 */ public static final int RTU_TYPE_CIMON_PLC = 15;			// CIMON_PLC
	/* 10 */ public static final int RTU_TYPE_LSIS_GLOFA_PLC = 16;		// LSIS_GLOFA_PLC
	/* 11 */ public static final int RTU_TYPE_ACTIVE_RCU = 17;			// MK_Active_RCU
	/* 12 */ public static final int RTU_TYPE_MULTIPORT_TCP = 18;		// TCP/IP_Multiport_RCU
	/* 13 */ public static final int RTU_TYPE_MODBUS_GATEWAY = 19;		// Modbus_Gateway
	/* 14 */ public static final int RTU_TYPE_DUPLEXED_TCP = 20;		// TCP/IP_이중화_RCU
	/* 15 */ public static final int RTU_TYPE_MQTT_BROKER = 21;			// MQTT_Broker
	
	public static final String GET_RTU = 
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
			"	rtu.PASSIVE_TCP_SERVER_PORT as 'port',	\r\n" + 
			"	a.nAgentType AS 'agentType',\r\n" + 
			"	a.nServerIndex as 'index',\r\n" + 
			"	rtu.RTU_TYPE as 'rtuType',\r\n" + 
			"	a.strServerName as 'name',\r\n" + 
			"	a.SERVER_CONDITION as 'condition',\r\n" + 
			"	a.AUX_SERVER_IP as 'auxIP',\r\n" + 
			"	rtu.AUX_TCP_PORT as 'auxPort',\r\n" + 
			"	addInfo.FIELD1_TEXT as 'mqttPort'\r\n" + 
			" \r\n" + 
			"from SERVERINFO a inner join SERVERGROUPMAP b on a.nServerIndex=b.nServerIndex\r\n" + 
			"	inner join tree_query c on b.nGroupIndex = c.ngroupIndex\r\n" + 
			"	inner join SERVERINFO_RTU rtu ON a.nServerIndex = rtu.NODE_INDEX\r\n" + 
			"	left join SERVERINFO_ADDINFO addInfo ON a.nServerIndex = addInfo.EQUIP_ID\r\n" + 
			" order by a.nServerIndex";
	
	public static final int DEFAULT_PORT = 1470;
	
	private boolean isMultiPort = false;
	private boolean isDuplexedPort = false;
	
	private int port; // RCU Port
	
	private String auxIP; // 이중화 IP
	private int auxPort; // 이중화 포트
	
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
	public boolean isDuplexedPort() {
		return isDuplexedPort;
	}
	public void setDuplexedPort(boolean isDuplexedPort) {
		this.isDuplexedPort = isDuplexedPort;
	}
	public String getAuxIP() {
		return auxIP;
	}
	public int getAuxPort() {
		return auxPort;
	}
	public void setAuxIP(String auxIP) {
		this.auxIP = auxIP;
	}
	public void setAuxPort(int auxPort) {
		this.auxPort = auxPort;
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


