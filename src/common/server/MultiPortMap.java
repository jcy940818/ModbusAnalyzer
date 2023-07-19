package common.server;

public class MultiPortMap implements Comparable{
	
	public static final String GET_MULTI_PORT_MAP = 
			"SELECT \r\n" + 
			"	DISTINCT\r\n" + 
			"	rtu.NODE_INDEX AS 'rtuIndex',	\r\n" + 
			"	portMap.PORT_NUMBER AS 'ch',\r\n" + 
			"	portMap.TCP_SERIAL_PORT AS 'port',\r\n" + 
			"	fac.NODE_INDEX AS 'facIndex'\r\n" + 
			"FROM \r\n" + 
			"	SERVERINFO_RTU rtu\r\n" + 
			"	\r\n" + 
			"	INNER JOIN SERVERINFO_RTU_MULTIPORT portMap ON portMap.NODE_INDEX = rtu.NODE_INDEX\r\n" + 
			"	LEFT JOIN SERVERINFO_FACILITY fac ON fac.RTU_INDEX = rtu.NODE_INDEX AND fac.RTU_PORT_NUM = portMap.PORT_NUMBER\r\n" + 
			"ORDER BY rtu.NODE_INDEX, portMap.PORT_NUMBER ASC";
	
	private int rtuIndex;
	private int ch;
	private int port;
	private int facIndex;
	
	public int getRtuIndex() {
		return rtuIndex;
	}
	public void setRtuIndex(int rtuIndex) {
		this.rtuIndex = rtuIndex;
	}
	public int getCh() {
		return ch;
	}
	public void setCh(int ch) {
		this.ch = ch;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}	
	public int getFacIndex() {
		return facIndex;
	}
	public void setFacIndex(int facIndex) {
		this.facIndex = facIndex;
	}
	
	@Override
	public int compareTo(Object obj) {
		MultiPortMap map = (MultiPortMap)obj;
		
		if(this.ch < map.ch) {
			return -1;
		}else if(this.ch == map.ch) {
			return 0;
		}else {
			return 1;
		}		
	}
	
	
}
