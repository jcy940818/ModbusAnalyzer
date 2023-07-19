package src_en.analyzer.RX;

import java.io.IOException;

import javax.swing.JOptionPane;

import src_en.exception.CRCException;
import src_en.exception.ModbusFormatException;
import src_en.exception.ModbusTCPFormatException;
import src_en.info.Perf_Info;
import src_en.info.RX_Info;
import src_en.util.PacketInputStream;
import src_en.util.Util;


public class RX_Status_Analyzer{
	
	PacketInputStream in = null;	
	
	public RX_Status_Analyzer(PacketInputStream in){
		this.in = in;
	}
	
	// RTU : FC 01, 02 
	public RX_Info readRTU_Status(RX_Info rx) throws IOException {		
		
		StringBuffer RX = new StringBuffer();
		RX.append(String.format("%02x",rx.getUnitId()));
		RX.append(String.format("%02x",rx.getFunctionCode()));
			
		int readByteCount = 0;
		int crc = 0;
		String bitStatus; 
		int temp;
		Perf_Info[] perf = null;				
		
		try {
			try {
				readByteCount = in.read();
				rx.setReadByteCount(readByteCount);
				rx.setRequestCount(readByteCount * 8);
				RX.append(String.format("%02x",rx.getReadByteCount()));
				
				perf = new Perf_Info[readByteCount * 8];
				
				for(int i = 0; i < readByteCount; i++) {				
					temp = in.read(); // RX�� ������ ���� 1Byte		
					
					RX.append(String.format("%02x",temp));
					
					for(int j = 0; j < 8; j++) {
						if(i * 8 + j >= (readByteCount * 8)) break;
						perf[i * 8 + j] = new Perf_Info();
						int bitValue = (temp >> j & 1);
						perf[i * 8 + j].setIntValue(bitValue);
						perf[i * 8 + j].setBitStatus((bitValue==1)?"ON":"OFF");
						
						// FC01 ����, FC02 �������� �ּ� ����
						if (rx.isHasTxInfo()) {
							perf[i * 8 + j].setRegisterAddress(rx.getTxInfo().getStartAddress() + (i*8+j));						
						}// if
					}// for - j
				}// for - i
			}catch(Exception e) {
				throw new ModbusFormatException();				
			}		
		}catch(ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("RX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
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
		
		RX.append(String.format("%04x", (rx.getCrc() & 0xffff)));

		// rx ������ �� ���������� �� ������ ������ �ִ� Perf_Info �迭�� �����Ѵ�.
		rx.setPerfInfo(perf);

		rx.setContent(RX.toString());

		rx.setScanResult("Good");
		return rx;
	}
	

	
	// TCP FC 01, 02
	public RX_Info readTCP_Status(RX_Info rx) throws IOException {
	
		StringBuffer RX = new StringBuffer();
		RX.append(String.format("%04x", rx.getTransactionId()));
		RX.append(String.format("%04x", rx.getProtocolId()));
		RX.append(String.format("%04x", rx.getLength()));
		RX.append(String.format("%02x",rx.getUnitId()));
		RX.append(String.format("%02x",rx.getFunctionCode()));
		
		int readByteCount = 0;		
		String bitStatus; 
		int temp;
		Perf_Info[] perf = null;
			
		try {
			try {
				readByteCount = in.read();			
				rx.setReadByteCount(readByteCount);			
				rx.setRequestCount(readByteCount * 8);
				
				RX.append(String.format("%02x",rx.getReadByteCount()));
				
				perf = new Perf_Info[readByteCount * 8];
				
				for(int i = 0; i < readByteCount; i++) {				
					temp = in.read(); // RX�� ������ ���� 1Byte			
					RX.append(String.format("%02x",temp));
					for(int j = 0; j < 8; j++) {
						if(i * 8 + j >= (readByteCount * 8)) break;
						perf[i * 8 + j] = new Perf_Info();
						int bitValue = (temp >> j & 1);
						perf[i * 8 + j].setIntValue(bitValue);
						perf[i * 8 + j].setBitStatus((bitValue==1)?"ON":"OFF");
						
						// FC01 ����, FC02 �������� �ּ� ����
						if (rx.isHasTxInfo()) {
							perf[i * 8 + j].setRegisterAddress(rx.getTxInfo().getStartAddress() + (i*8+j));						
						}// if					
					}// for - j
				}// for - i
			}catch(Exception e) {
				throw new ModbusFormatException();				
			}						
		}catch(ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("RX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			return null;
		}
					
		try {
		if((rx.getProtocolId() != 0x0000)) {
			// Modbus TCP RX Header length���� ��û�� ���̿� ���� �����ǹǷ� static�ϰ� �������� ������ �� ����.
			// Modbus TCP Header : protocolID�� 0x0000 ���� �����Ǿ� �ִ�.				
			// Modbus TCP Header : Length�� 0x0006 ���� �����Ǿ� �ִ�.
			throw new ModbusTCPFormatException();						
		}
		}catch(ModbusTCPFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("RX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			return null;
		}

		// rx ������ �� ���������� �� ������ ������ �ִ� Perf_Info �迭�� �����Ѵ�.
		rx.setPerfInfo(perf);

		rx.setContent(RX.toString());

		rx.setScanResult("Good");
		return rx;
	}	
	
}
