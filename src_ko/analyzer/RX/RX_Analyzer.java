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
		
		// Perf 인스턴스에 넘겨줄 정보
		int startAddress = 0;
		
		//Perf_Info 클래스에 
		//static TX_Info 필드가 선언되어 있으므로
		//Perf_Info 인스턴스는 동일한 TX_Info 인스턴스를 공유한다.	
		Perf_Info[] perf = null;
						 
		//RX_Info가 요청정보(TX_Info)를 가지고 있으면
	    //Perf_Info에게 요청정보(TX_Info)를 알려준다.
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
					Util.showMessage("다중 값 제어는 지원하지 않습니다", JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				// 예외코드를 수신하면 내용  출력
				if(rx.isException()) {			
					return new ExceptionReader(in).read_RTU_Exception(rx);
				}
				
				// Status 읽기 및 제어 관련 RX 처리
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
		
		// rx 정보에 각 레지스터의 값 정보를 가지고 있는 Perf_Info 배열을 저장한다.
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
		
		// Perf 인스턴스에 넘겨줄 정보
		int startAddress = 0;
		int startModbusAddress = 0;
		
		//Perf_Info 클래스에 
		//static TX_Info 필드가 선언되어 있으므로
		//Perf_Info 인스턴스는 동일한 TX_Info 인스턴스를 공유한다.	
		Perf_Info[] perf = null;
		
		//RX_Info가 요청정보(TX_Info)를 가지고 있으면
	    //Perf_Info에게 요청정보(TX_Info)를 알려준다.
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
				
				// 다중 제어 요청일 경우 패스
				if((rx.getFunctionCode() == 15)||(rx.getFunctionCode() == 16)){
					Util.showMessage("다중 값 제어는 지원하지 않습니다", JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				// 예외코드를 수신하면 내용  출력
				if(rx.isException()) {			
					return new ExceptionReader(in).read_TCP_Exception(rx);
				}
				
				// Status 읽기 및 제어 관련 RX 처리
				if ((functionCode == 0x01) || (functionCode == 0x02)) {
					return new RX_Status_Analyzer(in).readTCP_Status(rx);
				} else if ((functionCode == 0x05) || (functionCode == 0x06)) {
					return new Control_RX_Analyzer(in).readTCP_ControlResult(rx);
				}
										
				readByteCount = in.read();
				registerCount = readByteCount / 2;
				
				try {
					if((protocolId != 0x0000)) {
						// Modbus TCP RX Header length값은 요청의 길이에 따라 결정되므로 static하게 조건으로 설정할 수 없다
						// Modbus TCP Header : protocolID는 0x0000 으로 고정되어 있다
						// Modbus TCP Header : Length는 0x0006 으로 고정되어 있다
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
		String state = "Unknown (알 수 없음)";
		
		if(fc >= 1 && fc <= 6) {
			state = "Normal (정상 응답 패킷)";
		}else if(fc >= 15 && fc <= 16) {
			state = "Normal (정상 응답 패킷)";
		}else if(fc >= 0x80 && fc <= 0x89) {
			state = "Exception (예외 응답 패킷)";
		}
		
		return state;
	}
}