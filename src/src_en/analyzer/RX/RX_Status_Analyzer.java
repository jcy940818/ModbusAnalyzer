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
					temp = in.read(); // RX의 값으로 읽은 1Byte		
					
					RX.append(String.format("%02x",temp));
					
					for(int j = 0; j < 8; j++) {
						if(i * 8 + j >= (readByteCount * 8)) break;
						perf[i * 8 + j] = new Perf_Info();
						int bitValue = (temp >> j & 1);
						perf[i * 8 + j].setIntValue(bitValue);
						perf[i * 8 + j].setBitStatus((bitValue==1)?"ON":"OFF");
						
						// FC01 코일, FC02 레지스터 주소 지정
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
			// 잘못된 CRC를 int 타입으로 변환 중 발생한 Exception 처리 (주로 문자열을 정수형으로 변환하려다가 예외 발생)			
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

		// rx 정보에 각 레지스터의 값 정보를 가지고 있는 Perf_Info 배열을 저장한다.
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
					temp = in.read(); // RX의 값으로 읽은 1Byte			
					RX.append(String.format("%02x",temp));
					for(int j = 0; j < 8; j++) {
						if(i * 8 + j >= (readByteCount * 8)) break;
						perf[i * 8 + j] = new Perf_Info();
						int bitValue = (temp >> j & 1);
						perf[i * 8 + j].setIntValue(bitValue);
						perf[i * 8 + j].setBitStatus((bitValue==1)?"ON":"OFF");
						
						// FC01 코일, FC02 레지스터 주소 지정
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
			// Modbus TCP RX Header length값은 요청의 길이에 따라 결정되므로 static하게 조건으로 설정할 수 없다.
			// Modbus TCP Header : protocolID는 0x0000 으로 고정되어 있다.				
			// Modbus TCP Header : Length는 0x0006 으로 고정되어 있다.
			throw new ModbusTCPFormatException();						
		}
		}catch(ModbusTCPFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("RX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			return null;
		}

		// rx 정보에 각 레지스터의 값 정보를 가지고 있는 Perf_Info 배열을 저장한다.
		rx.setPerfInfo(perf);

		rx.setContent(RX.toString());

		rx.setScanResult("Good");
		return rx;
	}	
	
}
