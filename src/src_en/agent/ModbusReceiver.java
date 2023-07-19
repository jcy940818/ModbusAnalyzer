package src_en.agent;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;

import src_en.analyzer.RX.Control_RX_Analyzer;
import src_en.analyzer.RX.RX_Analyzer;
import src_en.analyzer.RX.RX_Status_Analyzer;
import src_en.exception.CRCException;
import src_en.exception.RegisterLengthException;
import src_en.info.Perf_Info;
import src_en.info.RX_Info;
import src_en.info.TX_Info;
import src_en.util.ExceptionReader;
import src_en.util.PacketInputStream;
import src_en.util.PacketOutputStream;
import src_en.util.Util;


public class ModbusReceiver {
	
	// Modbus RTU
	public static RX_Info reciveRTU(Socket client, TX_Info tx) throws IOException, SocketTimeoutException {
	
		RX_Info rx = new RX_Info();
		rx.setTxInfo(tx);
		rx.setRTU(true);
				
		byte[] buff = new byte[8192];
		PacketInputStream in = new PacketInputStream(buff, client.getInputStream());
		PacketOutputStream out = new PacketOutputStream(client.getOutputStream());									
		ModbusAgent.cleaerTempRxPacket();
		
		int unitId = in.read();
		ModbusAgent.setTempRxPacket(in.debug_getBuffer());
		rx.setUnitId(unitId);
		
		int functionCode = in.read();
		rx.setFunctionCode(functionCode);		
		
		// �����ڵ带 �����ϸ� ����  ���
		if((functionCode >= 0x80)&&(functionCode <= 0x8F)) {				
			return new ExceptionReader(in).read_RTU_Exception(rx);
		}
						
		// Status �б� �� ���� ���� RX ó��
		if ((functionCode == 0x01) || (functionCode == 0x02)) {
			return new RX_Status_Analyzer(in).readRTU_Status(rx);
		} else if ((functionCode == 0x05) || (functionCode == 0x06)) {
			return new Control_RX_Analyzer(in).readRTU_ControlResult(rx);
		}
				
		int readByteCount = in.read();
		rx.setReadByteCount(readByteCount);
		
		int registerCount = readByteCount / 2;
		int crc = 0;
						
		// Perf �ν��Ͻ��� �Ѱ��� ����
		int startAddress = rx.getTxInfo().getStartAddress();		
		
		//Perf_Info Ŭ������ 
		//static TX_Info �ʵ尡 ����Ǿ� �����Ƿ�
		//Perf_Info �ν��Ͻ��� ������ TX_Info �ν��Ͻ��� �����Ѵ�.	
		Perf_Info[] perf = null;		
		perf = new Perf_Info[registerCount];	
		
		rx.setStartAddress(rx.getTxInfo().getStartAddress());		
		
		//RX_Info�� ��û����(TX_Info)�� ������ ������
	    //Perf_Info���� ��û����(TX_Info)�� �˷��ش�.
		if(rx.isHasTxInfo()) {			
			Perf_Info.setTxInfo(rx.getTxInfo());
			Perf_Info.setHasTxInfo(true);
			startAddress = rx.getTxInfo().getStartAddress();								
		}
		
		// ���� �� �ʱ�ȭ
		try {
			try {
				for (int i = 0; i < registerCount; i++) {
					
					int registerValue = in.readShort();
	
					// �������Ϳ��� ���� ���� ���� �ν��Ͻ��� �����Ѵ�.
					// ���߿� �� �κ��� ������Ÿ�Կ� ���� �����ϰ� ��������.
					perf[i] = new Perf_Info();
					perf[i].setIntValue(registerValue);
					perf[i].setBinaryValue(registerValue & 0xffff);
					
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
		
		StringBuffer RX = new StringBuffer();
		RX.append(String.format("%02x",rx.getUnitId()));
		RX.append(String.format("%02x",rx.getFunctionCode()));
		RX.append(String.format("%02x",rx.getReadByteCount()));
		for(int i = 0; i < perf.length; i++) { RX.append(String.format("%04x", (perf[i].getIntHexValue()))); }
		RX.append(String.format("%04x", (rx.getCrc() & 0xffff)));		
		rx.setContent(RX.toString());
		
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
							
		rx = new RX_Analyzer().rtuAnalysis(rx);
		rx.setActualPacket(in.debug_getBuffer()); // ���� ��Ŷ ���� ����
		
		rx.setScanResult("Good");
		return rx;
	}
	
	
			
	
	// Modbus TCP
	public static RX_Info reciveTCP(Socket client, TX_Info tx) throws IOException, SocketTimeoutException {
	
		RX_Info rx = new RX_Info();
		rx.setTxInfo(tx);
		rx.setRTU(false);		
		
		byte[] buff = new byte[8192];
		PacketInputStream in = new PacketInputStream(buff, client.getInputStream());
		PacketOutputStream out = new PacketOutputStream(client.getOutputStream());		
		ModbusAgent.cleaerTempRxPacket();		
		
		int transactionId = in.readShort();
		ModbusAgent.setTempRxPacket(in.debug_getBuffer());		
		rx.setTransactionId(transactionId);
		
		int protocolId = in.readShort();
		rx.setProtocolId(protocolId);
		
		int length = in.readShort();
		rx.setLength(length);
		
		int unitId = in.read();
		rx.setUnitId(unitId);
		
		int functionCode = in.read();
		rx.setFunctionCode(functionCode);				
		
		// �����ڵ带 �����ϸ� ����  ���
		if((functionCode >= 0x80)&&(functionCode <= 0x8F)) {		
			return new ExceptionReader(in).read_TCP_Exception(rx);
		}
		
		// Status �б� �� ���� ���� RX ó��
		if ((functionCode == 0x01) || (functionCode == 0x02)) {
			return new RX_Status_Analyzer(in).readTCP_Status(rx);
		} else if ((functionCode == 0x05) || (functionCode == 0x06)) {
			return new Control_RX_Analyzer(in).readTCP_ControlResult(rx);
		}

		int readByteCount = in.read();
		int registerCount = readByteCount / 2;
		
		// Perf �ν��Ͻ��� �Ѱ��� ����
		int startAddress = rx.getTxInfo().getStartAddress();
		
		//Perf_Info Ŭ������
		//static TX_Info �ʵ尡 ����Ǿ� �����Ƿ�
		//Perf_Info �ν��Ͻ��� ������ TX_Info �ν��Ͻ��� �����Ѵ�.
		Perf_Info[] perf = null;
		
		perf = new Perf_Info[registerCount];
		
		rx.setReadByteCount(readByteCount);
		rx.setStartAddress(rx.getTxInfo().getStartAddress());		
		
		//RX_Info�� ��û����(TX_Info)�� ������ ������
	    //Perf_Info���� ��û����(TX_Info)�� �˷��ش�.
		if(rx.isHasTxInfo()) {			
			Perf_Info.setTxInfo(rx.getTxInfo());
			Perf_Info.setHasTxInfo(true);
			startAddress = rx.getTxInfo().getStartAddress();
		}
		
		// ���� �� �ʱ�ȭ
		try {
			try {
				for (int i = 0; i < registerCount; i++) {
					
					int registerValue = in.readShort();
	
					// �������Ϳ��� ���� ���� ���� �ν��Ͻ��� �����Ѵ�.
					// ���߿� �� �κ��� ������Ÿ�Կ� ���� �����ϰ� ��������.
					perf[i] = new Perf_Info();
					perf[i].setIntValue(registerValue);
					perf[i].setBinaryValue(registerValue & 0xffff);
					
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
		
				
		StringBuffer RX = new StringBuffer();
		RX.append(String.format("%04x", rx.getTransactionId()));
		RX.append(String.format("%04x", rx.getProtocolId()));
		RX.append(String.format("%04x", rx.getLength()));
		RX.append(String.format("%02x",rx.getUnitId()));
		RX.append(String.format("%02x",rx.getFunctionCode()));
		RX.append(String.format("%02x",rx.getReadByteCount()));
		for(int i = 0; i < perf.length; i++) { RX.append(String.format("%04x", (perf[i].getIntHexValue()))); }				
		rx.setContent(RX.toString());
			
		rx = new RX_Analyzer().tcpAnalysis(rx);
		rx.setActualPacket(in.debug_getBuffer()); // ���� ��Ŷ ���� ����
				
		rx.setScanResult("Good");
		return rx;
	}

}

