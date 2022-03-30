package common.server;

public class MultiPortMap implements Comparable{
	
	public static final String GET_MULTI_PORT_MAP = "SELECT \r\n" + 
			"	NODE_INDEX AS 'index',\r\n" + 
			"	PORT_NUMBER AS 'ch',\r\n" + 
			"	TCP_SERIAL_PORT AS 'port'\r\n" + 
			"FROM \r\n" + 
			"	SERVERINFO_RTU_MULTIPORT \r\n" + 
			"	ORDER BY NODE_INDEX, PORT_NUMBER ASC";
	
	private int rtuIndex;
	private int ch;
	private int port;
	
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
