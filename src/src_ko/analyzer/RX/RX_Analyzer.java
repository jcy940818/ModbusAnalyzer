package src_ko.analyzer.RX;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;

import javax.swing.JOptionPane;

import src_ko.exception.CRCException;
import src_ko.exception.ModbusFormatException;
import src_ko.exception.ModbusTCPFormatException;
import src_ko.exception.RegisterLengthException;
import src_ko.info.Perf_Info;
import src_ko.info.RX_Info;
import src_ko.util.ExceptionReader;
import src_ko.util.PacketGenerator;
import src_ko.util.PacketInputStream;
import src_ko.util.Util;

public class RX_Analyzer {
				
	/**
	 * @return Modbus RTU RX - Information
	 */
	public RX_Info rtuAnalysis(RX_Info rxContent) throws IOException {			
		RX_Info rx = new RX_Info();
		rx.setRTU(true);
		
		String RX = "";
		
		RX = rxContent.getContent();
		RX = RX.replaceAll(" ", "");
		rx.setContent(RX);
		if(rxContent.isHasTxInfo()) rx.setTxInfo(rxContent.getTxInfo()); 
		
				
		PacketInputStream in = null;
		byte[] packet;
		byte[] buff = new byte[8192];
		int unitId = 0;
		int functionCode = 0;
		int readByteCount = 0;
		int registerCount = 0;
		int crc = 0;
		
		// Perf �ν��Ͻ��� �Ѱ��� ����
		int startAddress = 0;
		
		//Perf_Info Ŭ������ 
		//static TX_Info �ʵ尡 ����Ǿ� �����Ƿ�
		//Perf_Info �ν��Ͻ��� ������ TX_Info �ν��Ͻ��� �����Ѵ�.	
		Perf_Info[] perf = null;
						 
		//RX_Info�� ��û����(TX_Info)�� ������ ������
	    //Perf_Info���� ��û����(TX_Info)�� �˷��ش�.
		if(rx.isHasTxInfo()) {
			rx.setHasTxInfo(true);
			Perf_Info.setTxInfo(rx.getTxInfo());
			Perf_Info.setHasTxInfo(true);
			startAddress = rx.getTxInfo().getStartAddress();
		}
				
		try {
			try {
				packet = PacketGenerator.getOriginPacket(RX);
				in = new PacketInputStream(buff, new ByteArrayInputStream(packet));
				
				unitId = in.read();
				rx.setUnitId(unitId);
				
				functionCode = in.read();
				rx.setFunctionCode(functionCode);														
				
				if((rx.getFunctionCode() == 15)||(rx.getFunctionCode() == 16)){									
					Util.showMessage("���� �� ����� �������� �ʽ��ϴ�", JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				// �����ڵ带 �����ϸ� ����  ���
				if(rx.isException()) {			
					return new ExceptionReader(in).read_RTU_Exception(rx);
				}
				
				// Status �б� �� ���� ���� RX ó��
				if ((functionCode == 0x01) || (functionCode == 0x02)) {
					return new RX_Status_Analyzer(in).readRTU_Status(rx);
				} else if ((functionCode == 0x05) || (functionCode == 0x06)) {
					return new Control_RX_Analyzer(in).readRTU_ControlResult(rx);
				}
				
				readByteCount = in.read();
				registerCount = readByteCount / 2;
				
			} catch (Exception e) {
				throw new ModbusFormatException();
			}
		}catch(ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());			
			Util.showMessage("RX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			return null;			
		}
		
		perf = new Perf_Info[registerCount];
		rx.setReadByteCount(readByteCount);
		rx.setRequestCount(registerCount);
		
		try {
			try {
				for (int i = 0; i < registerCount; i++) {
					
					int registerValue = in.readShort();
				
					perf[i] = new Perf_Info();
					perf[i].setIntValue(registerValue);
					perf[i].setBinaryValue(registerValue);										
					
					if (rx.isHasTxInfo()) {
						perf[i].setRegisterAddress(rx.getTxInfo().getStartAddress() + i);
					}				
				}
			} catch (EOFException e) {
				throw new RegisterLengthException();				
			}
		}catch(RegisterLengthException e) {
			e.printStackTrace();
			return null;
		}
		
		
		boolean isCrcError = false;
		String errorCRC = "";
				
		try {
			try {
				rx.setExpectedCrc(in.getCRC16());
				crc = in.readCRC16();
			} catch (IOException e) {				
				isCrcError = true;
				errorCRC = e.getMessage();
				rx.setCrc(Integer.parseInt(errorCRC) & 0xffff);
			}
		}catch(NumberFormatException e) {
			// �߸��� CRC�� int Ÿ������ ��ȯ �� �߻��� Exception ó�� (�ַ� ���ڿ��� ���������� ��ȯ�Ϸ��ٰ� ���� �߻�)			
			Util.showMessage("NumberFormatException\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
		}
		
		if(!isCrcError) {
			rx.setCrc(crc & 0xffff);
		}		
		
		try {
			if(isCrcError) {				
				rx.setCRCError(true);
				throw new CRCException();
			}
		}catch(CRCException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return rx;
		}
		
		// rx ������ �� ���������� �� ������ ������ �ִ� Perf_Info �迭�� �����Ѵ�.
		rx.setPerfInfo(perf);

		return rx;
	}
		
		
	
	
	/**
	 * @return Modbus TCP RX - Information
	 */
	public RX_Info tcpAnalysis(RX_Info rxContent) throws IOException {		
		RX_Info rx = new RX_Info();		
		rx.setRTU(false);
		
		String RX = "";
		
		RX = rxContent.getContent();
		RX = RX.replaceAll(" ", "");		
		rx.setContent(RX);
		if(rxContent.isHasTxInfo()) rx.setTxInfo(rxContent.getTxInfo()); 
		
		PacketInputStream in = null;
		byte[] packet;
		byte[] buff = new byte[8192];
		int transactionId = 0;
		int protocolId = 0;
		int length = 0;
		int unitId = 0;
		int functionCode = 0;
		int readByteCount = 0;
		int registerCount = 0;
		
		// Perf �ν��Ͻ��� �Ѱ��� ����
		int startAddress = 0;
		int startModbusAddress = 0;
		
		//Perf_Info Ŭ������ 
		//static TX_Info �ʵ尡 ����Ǿ� �����Ƿ�
		//Perf_Info �ν��Ͻ��� ������ TX_Info �ν��Ͻ��� �����Ѵ�.	
		Perf_Info[] perf = null;
		
		//RX_Info�� ��û����(TX_Info)�� ������ ������
	    //Perf_Info���� ��û����(TX_Info)�� �˷��ش�.
		if(rx.isHasTxInfo()) {
			Perf_Info.setTxInfo(rx.getTxInfo());
			Perf_Info.setHasTxInfo(true);
			startAddress = rx.getTxInfo().getStartAddress();
		}
				
		try {
			try {
				packet = PacketGenerator.getOriginPacket(RX);
				in = new PacketInputStream(buff, new ByteArrayInputStream(packet));	
							
				transactionId = in.readUnsignedShort();
				rx.setTransactionId(transactionId);
				
				protocolId = in.readShort();
				rx.setProtocolId(protocolId);
				
				length = in.readShort();
				rx.setLength(length);
				
				unitId = in.read();
				rx.setUnitId(unitId);
				
				functionCode = in.read();
				rx.setFunctionCode(functionCode);
				
				// ���� ���� ��û�� ��� �н�
				if((rx.getFunctionCode() == 15)||(rx.getFunctionCode() == 16)){
					Util.showMessage("���� �� ����� �������� �ʽ��ϴ�", JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				// �����ڵ带 �����ϸ� ����  ���
				if(rx.isException()) {			
					return new ExceptionReader(in).read_TCP_Exception(rx);
				}
				
				// Status �б� �� ���� ���� RX ó��
				if ((functionCode == 0x01) || (functionCode == 0x02)) {
					return new RX_Status_Analyzer(in).readTCP_Status(rx);
				} else if ((functionCode == 0x05) || (functionCode == 0x06)) {
					return new Control_RX_Analyzer(in).readTCP_ControlResult(rx);
				}
										
				readByteCount = in.read();
				registerCount = readByteCount / 2;
				
				try {
					if((protocolId != 0x0000)) {
						// Modbus TCP RX Header length���� ��û�� ���̿� ���� �����ǹǷ� static�ϰ� �������� ������ �� ����
						// Modbus TCP Header : protocolID�� 0x0000 ���� �����Ǿ� �ִ�
						// Modbus TCP Header : Length�� 0x0006 ���� �����Ǿ� �ִ�
						throw new ModbusTCPFormatException();					
					}
				}catch(ModbusTCPFormatException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
					Util.showMessage("RX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);					
				}
				
			} catch (Exception e) {
				throw new ModbusFormatException();				
			}	
		}catch(ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("RX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		perf = new Perf_Info[registerCount];
		rx.setRTU(false);
		rx.setTransactionId(transactionId);
		rx.setContent(RX);
		rx.setUnitId(unitId);
		rx.setFunctionCode(functionCode);
		rx.setReadByteCount(readByteCount);
		rx.setRequestCount(registerCount);
		
		try {
			try {
				for (int i = 0; i < registerCount; i++) {								
					
					int registerValue = in.readShort();
					perf[i] = new Perf_Info();
					perf[i].setIntValue(registerValue);
					perf[i].setBinaryValue(registerValue);
					
					if (rx.isHasTxInfo()) {
						perf[i].setRegisterAddress(rx.getTxInfo().getStartAddress() + i);
					}		
				}
			} catch (EOFException e) {
				throw new RegisterLengthException();			
			}
		}catch(RegisterLengthException e) {
			e.printStackTrace();
			return null;
		}

		rx.setPerfInfo(perf);

		return rx;
	}		
	
	public static String getPacketState(int fc) {
		String state = "Unknown (�� �� ����)";
		
		if(fc >= 1 && fc <= 6) {
			state = "Normal (���� ���� ��Ŷ)";
		}else if(fc >= 15 && fc <= 16) {
			state = "Normal (���� ���� ��Ŷ)";
		}else if(fc >= 0x80 && fc <= 0x89) {
			state = "Exception (���� ���� ��Ŷ)";
		}
		
		return state;
	}
}