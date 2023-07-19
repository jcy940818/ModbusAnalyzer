package src_ko.info;

public class RX_Info {
	
	// 실제 패킷 내용
	private String actualPacket;
	
	// 응답(RX) 종류
	private String commandType;
	private String functionContent;	
	private boolean isCRCError;
	
	// Modbus Type, Cotent
	private boolean isRTU = false;
	private boolean isRead = true; // 수집인지 제어인지
	private boolean isException = false; // 예외 패킷 인지
	private String content = "";
		
	// Modbus TCP Header
	private int transactionId;
	private int protocolId;
	private int length;
	
	// RX를 요청한 요청정보(TX)
	private boolean hasTxInfo = false;
	private TX_Info txInfo = null;
	
	// Modbus Common Info
	private int unitId;
	private int functionCode;
	private int readByteCount;
	private int crc;
	private int expectedCrc;
	
	// Modbus Exception Code
	private int exceptionCode;
	private String exceptionContent;
	
	// RX 제어 관련
	private int controlAddress;
	private int controlValue;
	private String controlStatus;
	
	// RX가 읽은 레지스터(성능) 정보
	private Perf_Info[] perfInfo;		
		
	// ExceptionScan 결과
	private String scanResult;
	
	private int startAddress;
	private int requestCount;
	private int endAddress;
	
	private String modbusAddress;
	
	public boolean isRTU() {
		return isRTU;
	}

	public void setRTU(boolean isRTU) {
		this.isRTU = isRTU;
	}

	public Perf_Info[] getPerfInfo() {
		return perfInfo;
	}

	public void setPerfInfo(Perf_Info[] perfInfo) {
		this.perfInfo = perfInfo;
	}

	public TX_Info getTxInfo() {
		return txInfo;
	}

	public void setTxInfo(TX_Info txInfo) {
		this.txInfo = txInfo;
		this.setHasTxInfo(true);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getTransactionId() {
		return transactionId & 0xffff;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId & 0xffff;
	}

	public int getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(int protocolId) {
		this.protocolId = protocolId;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isHasTxInfo() {
		return hasTxInfo;
	}

	public void setHasTxInfo(boolean hasTxInfo) {
		this.hasTxInfo = hasTxInfo;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public int getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(int functionCode) {
		this.functionCode = functionCode;
				
		switch(functionCode) {
			case 1 : commandType = "코일 상태"; functionContent="Read Coil Status"; isRead=true; break;
			case 2 : commandType = "레지스터 상태"; functionContent="Read Input Status"; isRead=true; break;
			case 3 : commandType = "레지스터 값"; functionContent="Read Holding Registers"; isRead=true; break;
			case 4 : commandType = "레지스터 값"; functionContent="Read Input Registers"; isRead=true; break;
			case 5 : commandType = "코일 상태 제어"; functionContent="Force Single Coil"; isRead=false; break;
			case 6 : commandType = "레지스터 값 제어"; functionContent="Preset Single Register"; isRead=false; break;			
			default : commandType = "Exception Response"; functionContent="Exception Response"; isRead=true; break;
		}
		
		switch(functionCode) {
			case 1: modbusAddress = "0"; break;
			case 2: modbusAddress = "1"; break;
			case 3: modbusAddress = "4"; break;
			case 4: modbusAddress = "3"; break;
			case 5: modbusAddress = "0"; break;
			case 6: modbusAddress = "4"; break;					
	}
		
		if((functionCode >= 0x80)&&(functionCode <= 0x8F)) isException = true;
	}

	public int getReadByteCount() {
		return readByteCount;
	}

	public void setReadByteCount(int readByteCount) {
		this.readByteCount = readByteCount;
	}

	public int getCrc() {
		return crc & 0xffff;
	}

	public void setCrc(int crc) {
		this.crc = crc & 0xffff;
	}
	
	public int getExpectedCrc() {
		return expectedCrc & 0xffff;
	}

	public void setExpectedCrc(int expectedCrc) {
		this.expectedCrc = expectedCrc & 0xffff;
	}

	public int getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public int getControlAddress() {
		return controlAddress & 0xffff;
	}

	public void setControlAddress(int controlAddress) {
		this.controlAddress = controlAddress & 0xffff;
	}

	public int getControlValue() {
		return controlValue;
	}

	public void setControlValue(int controlValue) {
		this.controlValue = controlValue;
	}

	public String getControlStatus() {
		return controlStatus;
	}

	public void setControlStatus(String controlStatus) {
		this.controlStatus = controlStatus;
	}

	public int getStartAddress() {
		return startAddress & 0xffff;
	}

	public void setStartAddress(int startAddress) {
		this.startAddress = startAddress & 0xffff;
	}

	public int getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}

	public int getEndAddress() {
		return endAddress & 0xffff;
	}

	public void setEndAddress(int endAddress) {
		this.endAddress = endAddress & 0xffff;
	}

	public String getCommandType() {
		return commandType;
	}

	public boolean isRead() {
		return isRead;
	}

	public String getExceptionContent() {
		return exceptionContent;
	}

	public void setExceptionContent(String exceptionContent) {
		this.exceptionContent = exceptionContent;
	}

	public String getFunctionContent() {
		return functionContent;
	}

	public String getModbusAddress() {
		return modbusAddress;
	}

	public boolean isCRCError() {
		return isCRCError;
	}

	public void setCRCError(boolean isCRCError) {
		this.isCRCError = isCRCError;
	}
	
	public void setActualPacket(byte[] tokens) {
		StringBuilder packet = new StringBuilder();		
		for(int i = 0; i < tokens.length; i++) {
			packet.append(String.format("%02x", (tokens[i] & 0xff)));
		}		
		actualPacket = packet.toString();
	}
	
	public String getActualPacket() {
		return this.actualPacket;
	}
	
	public boolean isException() {
		return this.isException;
	}

	public String getScanResult() {
		return scanResult;
	}

	public void setScanResult(String scanResult) {
		this.scanResult = scanResult;
	}
	
	public static String getRxHandleContent(RX_Info rx) {
		String content = "";
		
		if ((rx != null) && rx.isRTU() && rx.isCRCError()) {
			content += String.format("[  Warning!  Incorrect CRC : 0x%04x", rx.getCrc() & 0xffff);
			
			if((rx.getExpectedCrc() & 0xffff) != 0x0000) {
				content += String.format("  /  Correct CRC : 0x%04x  ]", rx.getExpectedCrc() & 0xffff);
			}else {
				content += "  ]";
			}
		}

		if (rx.isException()) {
			if ((rx != null) && rx.isRTU() && rx.isCRCError()) {
				content += System.lineSeparator();
			}
			content += String.format("[  Warning!  RX is Exception Response = 0x%02x : %s  ]", rx.getExceptionCode(), rx.getExceptionContent());
		}
		
		return content;
	}
}
