package src_en.info;

public class Perf_Info {
	
	// XML 정보
	private XML_Info xml;
	
	// 요청정보(TX) 관련 필드
	public static TX_Info tx;
	static boolean hasTxInfo = false;
	
	// Perf_Info Only
	int registerAddress;
	String forModbusAddress;
	int intValue;
	double doubleValue;
	String binaryValue;
	String bitStatus;
	
	public static boolean HasTxInfo() {
		return hasTxInfo;
	}
	
	public static void setHasTxInfo(boolean hasTxInfo) {
		Perf_Info.hasTxInfo = hasTxInfo;
	}
	
	public static TX_Info getTxInfo() {
		return tx;
	}

	public static void setTxInfo(TX_Info tx) {
		Perf_Info.tx = tx;
	}

	public int getRegisterAddress() {
		return (registerAddress & 0xffff);
	}
	
	public void setRegisterAddress(int registerAddress) {
		this.registerAddress = registerAddress;
	}
	
	public int getIntValue() {
		return intValue;
	}
	
	public int getIntHexValue() {
		return (intValue & 0xffff);
	}
	
	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}
	
	public double getDoubleValue() {
		return doubleValue;
	}
	
	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public String getBinaryValue() {
		return binaryValue;
	}

	public void setBinaryValue(int originValue) {		
		originValue &= 0xffff;
		String str = String.format("%16s", Integer.toBinaryString(originValue)).replace(' ', '0');
		String space = " ";
		
		StringBuffer sb = new StringBuffer();
		sb.append(str.substring(0, 4)).append(space);
		sb.append(str.substring(4, 8)).append(space);
		sb.append(str.substring(8, 12)).append(space);
		sb.append(str.substring(12, 16));
		
		binaryValue = sb.toString();
	}

	public String getBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(String bitStatus) {
		this.bitStatus = bitStatus;
	}
	
	public void setModbusAddress(int functionCode) {
		// 모드버스 주소를 만번대로 표시하기 위한 문자열
		switch(functionCode) {
			case 1: forModbusAddress = "0"; break;
			case 2: forModbusAddress = "1"; break;
			case 3: forModbusAddress = "4"; break;
			case 4: forModbusAddress = "3"; break;
			case 5: forModbusAddress = "0"; break;
			case 6: forModbusAddress = "4"; break;					
		}
	}
			
	public String getModbusAddress() {
		return String.format("%s%04d", forModbusAddress, registerAddress+1);
	}

	public XML_Info getXML() {
		return xml;
	}

	public void setXML(XML_Info XML) {
		xml = XML;
	}
		
}
