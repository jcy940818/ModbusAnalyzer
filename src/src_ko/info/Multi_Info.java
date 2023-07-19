package src_ko.info;

public class Multi_Info {
	public TX_Info TX;		
	public RX_Info RX;
	
	public Multi_Info(TX_Info tX, RX_Info rX) {
		super();
		TX = tX;
		RX = rX;
	}
	
	public TX_Info getTX() {
		return TX;
	}
	
	public void setTX(TX_Info tX) {
		TX = tX;
	}
	
	public RX_Info getRX() {
		return RX;
	}
	
	public void setRX(RX_Info rX) {
		RX = rX;
	}
	
}
