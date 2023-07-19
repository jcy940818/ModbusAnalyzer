package src_ko.info;

import java.io.IOException;
import java.util.ArrayList;

import src_ko.util.TX_Generator;

public class TX_Info{
	
	private String agentType;
	
	// ��ɾ� ����
	private String commandType;		
	private String functionContent;	
	
	// Modbus Type, Cotent
	private boolean isRTU = false;
	private boolean isRead = true; // �������� ��������
	private String content = "";
	
	// Modbus TCP Header
	private int transactionId; 
	private int protocolId = 0x0000;
	private int length = 0x0006;
	
	// Modbus Common Info
	private int unitId; 
	private int functionCode; 
	private int startAddress;	
	private int requestCount;
	private int crc;
	
	// �ΰ� ����
	private int endAddress;
	private String modbusAddress;
	
	// ���� ���� ����
	private int controlValue; // �������� ���� ���� ��
	private String controlStatus; // ON, OFF ���� ���� ��

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	/**
	 * ���޹��� TX ������ ���� �ּҺ��� �������͸� 1���� ��û�ϴ� TX_Info �迭 ���·� ����
	 * @return
	 */
	public static ArrayList<TX_Info> getTxList(TX_Info tx, boolean autoTidIncrement) throws IOException{
		ArrayList<TX_Info> txList = new ArrayList();
		
		boolean isRTU = tx.isRTU();
		int transactionId = 0;
		int unitId = tx.getUnitId();
		int functionCode = tx.getFunctionCode();
		int startAddress = tx.getStartAddress();
		int requestCount = tx.getRequestCount();
		int step = 0;
		
		TX_Info item = null;
		
		if(isRTU) {
			for(int i = 0; i < 1000; i++) {
				item = new TX_Generator().generateReadRTU(unitId, functionCode, startAddress + step, requestCount);
				step += requestCount;
				txList.add(item);
			}
		}else {
			transactionId = tx.getTransactionId();
						
			for(int i = 0; i < 1000; i++) {
				if((transactionId + i) >= Short.MAX_VALUE) transactionId = transactionId / Short.MAX_VALUE;
				
				if(autoTidIncrement) {
					item = new TX_Generator().generateReadTCP(transactionId + i, 0x0000, 0x0006, unitId, functionCode, startAddress + step, requestCount);	
				}else {
					item = new TX_Generator().generateReadTCP(transactionId, 0x0000, 0x0006, unitId, functionCode, startAddress + step, requestCount);	
				}				
				
				step += requestCount;
				txList.add(item);
			}
		}
		
		return txList;
	}
	
	public boolean isRTU() {
		return isRTU;
	}
	
	public void setRTU(boolean isRTU) {
		this.isRTU = isRTU;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
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
			case 1 : commandType = "���� ����(ON/OFF)"; functionContent="Read Coil Status"; isRead=true; break;
			case 2 : commandType = "�������� ����(ON/FF)"; functionContent="Read Input Status"; isRead=true; break;
			case 3 : commandType = "��������(����)"; functionContent="Read Holding Registers"; isRead=true; break;
			case 4 : commandType = "��������(����)"; functionContent="Read Input Registers"; isRead=true; break;
			case 5 : commandType = "���� ����(ON/FF) ����"; functionContent="Force Single Coil"; isRead=false; break;
			case 6 : commandType = "��������(����) �� ����"; functionContent="Preset Single Register"; isRead=false; break;
			default : commandType = "��������(����)"; functionContent="Force Multiple Coils"; isRead=true; break;
		}
		
		switch(functionCode) {
			case 1: modbusAddress = "0"; break;
			case 2: modbusAddress = "1"; break;
			case 3: modbusAddress = "4"; break;
			case 4: modbusAddress = "3"; break;
			case 5: modbusAddress = "0"; break;
			case 6: modbusAddress = "4"; break;					
	}
		
	}

	public int getStartAddress() {
		return startAddress & 0xffff;
	}

	public void setStartAddress(int startAddress) {
		this.startAddress = startAddress & 0xffff;
	}
	
	public String getRegisterAddrHexString() {
		return String.format("0x%04X", getStartAddress());
	}
	
	public String getModbusAddrString() {
		String modbusAddress = "";
		switch(getFunctionCode()) {
			case 1: modbusAddress = "0"; break;
			case 2: modbusAddress = "1"; break;
			case 3: modbusAddress = "4"; break;
			case 4: modbusAddress = "3"; break;
		}
		return String.format("%s%04d", modbusAddress, (getStartAddress() & 0xffff) + 1);
	}

	public int getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}

	public int getCrc() {
		return crc & 0xffff;
	}

	public void setCrc(int crc) {
		this.crc = crc & 0xffff;
	}

	public int getEndAddress() {
		return endAddress & 0xffff;
	}

	public void setEndAddress(int endAddress) {
		this.endAddress = endAddress & 0xffff;
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

	public String getCommandType() {
		return commandType;
	}

	public String getFunctionContent() {
		return functionContent;
	}

	public boolean isRead() {
		return isRead;
	}

	public String getModbusAddress() {
		return modbusAddress;
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		if(this.isRTU) sb.append("TID : " + this.getTransactionId() + " / ");
		sb.append("UnidID : " + this.getUnitId() + " / ");
		sb.append("FC : " + this.getFunctionCode() + " / ");
		sb.append("startAddress : " + this.getStartAddress() + " / ");
		sb.append("requestCount : " + this.getRequestCount());
		
		return sb.toString();
	}
	
}
