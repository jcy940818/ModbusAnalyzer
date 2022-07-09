package src_ko.agent;

import src_ko.database.DbUtil;

public class ModbusFacility {
	private int nServerIndex;
	private String strServerName;
	private int FACILITY_TYPE;
	private String FACILITY_TYPE_String;	

	private int CONN_METHOD;
	private int COMM_PROTOCOL;

	public ModbusFacility(){
		
	}
	
	public ModbusFacility(int nServerIndex, String strServerName, int FACILITY_TYPE, String FACILITY_TYPE_String,int CONN_METHOD, int COMM_PROTOCOL) {
		this.nServerIndex = nServerIndex;
		this.strServerName = strServerName;
		this.FACILITY_TYPE = FACILITY_TYPE;
		this.FACILITY_TYPE_String = FACILITY_TYPE_String;
		this.CONN_METHOD = CONN_METHOD;
		this.COMM_PROTOCOL = COMM_PROTOCOL;		
	}
	
	public int getnServerIndex() {
		return nServerIndex;
	}

	public void setnServerIndex(int nServerIndex) {
		this.nServerIndex = nServerIndex;
	}

	public String getStrServerName() {
		return strServerName;
	}

	public void setStrServerName(String strServerName) {
		this.strServerName = strServerName;
	}

	public int getCONN_METHOD() {
		return CONN_METHOD;
	}

	public void setCONN_METHOD(int cONN_METHOD) {
		CONN_METHOD = cONN_METHOD;
	}

	public int getCOMM_PROTOCOL() {
		return COMM_PROTOCOL;
	}

	public void setCOMM_PROTOCOL(int cOMM_PROTOCOL) {
		COMM_PROTOCOL = cOMM_PROTOCOL;
	}
	
	public String getFACILITY_TYPE_String() {
		return FACILITY_TYPE_String;
	}
	
	public void setFacilityType(int facType) {
		this.FACILITY_TYPE = facType;
		this.FACILITY_TYPE_String = DbUtil.getFacilityType(facType);
	}

}
