package common.server;

import java.util.ArrayList;
import java.util.HashMap;

import common.perf.Perf;
import src_ko.info.Protocol;

public class Facility extends Server implements Comparable{
	
	// ���� �����ͺ��̽��� �ü��� ������ ���̰� ���� ������
	// �׷쿡 ������ ���� �����ͺ��̽����� �����ϴ� ��� �����ϱ� �����̴�
	// �Ʒ��� ������ ���� �׷�� ���εǾ� AdminConsole UI���� Ȯ���� ������ �ü����� ��ȸ�Ѵ�
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
	private RCU rcu; // �ø��� ��Ʈ ���� ����� ��� �ٶ󺸴� RCU
	private int port; // �ø��� ��Ʈ ���� ����� ��� RCU�� ��Ʈ ä�� ��ȣ
	private int rtuIndex;
	private int rcuPortCh;
	
	private int connCode; // ���� ���
	private String connMethod;
	
	private boolean isProtocol; // �������� ����
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
