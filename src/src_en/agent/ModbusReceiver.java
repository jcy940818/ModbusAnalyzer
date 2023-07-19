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
		
		// 예외코드를 수신하면 내용  출력
		if((functionCode >= 0x80)&&(functionCode <= 0x8F)) {				
			return new ExceptionReader(in).read_RTU_Exception(rx);
		}
						
		// Status 읽기 및 제어 관련 RX 처리
		if ((functionCode == 0x01) || (functionCode == 0x02)) {
			return new RX_Status_Analyzer(in).readRTU_Status(rx);
		} else if ((functionCode == 0x05) || (functionCode == 0x06)) {
			return new Control_RX_Analyzer(in).readRTU_ControlResult(rx);
		}
				
		int readByteCount = in.read();
		rx.setReadByteCount(readByteCount);
		
		int registerCount = readByteCount / 2;
		int crc = 0;
						
		// Perf 인스턴스에 넘겨줄 정보
		int startAddress = rx.getTxInfo().getStartAddress();		
		
		//Perf_Info 클래스에 
		//static TX_Info 필드가 선언되어 있으므로
		//Perf_Info 인스턴스는 동일한 TX_Info 인스턴스를 공유한다.	
		Perf_Info[] perf = null;		
		perf = new Perf_Info[registerCount];	
		
		rx.setStartAddress(rx.getTxInfo().getStartAddress());		
		
		//RX_Info가 요청정보(TX_Info)를 가지고 있으면
	    //Perf_Info에게 요청정보(TX_Info)를 알려준다.
		if(rx.isHasTxInfo()) {			
			Perf_Info.setTxInfo(rx.getTxInfo());
			Perf_Info.setHasTxInfo(true);
			startAddress = rx.getTxInfo().getStartAddress();								
		}
		
		// 성능 값 초기화
		try {
			try {
				for (int i = 0; i < registerCount; i++) {
					
					int registerValue = in.readShort();
	
					// 레지스터에서 읽은 값을 성능 인스턴스에 저장한다.
					// 나중에 이 부분을 데이터타입에 따라 적절하게 변경하자.
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
			// 잘못된 CRC를 int 타입으로 변환 중 발생한 Exception 처리 (주로 문자열을 정수형으로 변환하려다가 예외 발생)			
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
		rx.setActualPacket(in.debug_getBuffer()); // 실제 패킷 내용 저장
		
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
		
		// 예외코드를 수신하면 내용  출력
		if((functionCode >= 0x80)&&(functionCode <= 0x8F)) {		
			return new ExceptionReader(in).read_TCP_Exception(rx);
		}
		
		// Status 읽기 및 제어 관련 RX 처리
		if ((functionCode == 0x01) || (functionCode == 0x02)) {
			return new RX_Status_Analyzer(in).readTCP_Status(rx);
		} else if ((functionCode == 0x05) || (functionCode == 0x06)) {
			return new Control_RX_Analyzer(in).readTCP_ControlResult(rx);
		}

		int readByteCount = in.read();
		int registerCount = readByteCount / 2;
		
		// Perf 인스턴스에 넘겨줄 정보
		int startAddress = rx.getTxInfo().getStartAddress();
		
		//Perf_Info 클래스에
		//static TX_Info 필드가 선언되어 있으므로
		//Perf_Info 인스턴스는 동일한 TX_Info 인스턴스를 공유한다.
		Perf_Info[] perf = null;
		
		perf = new Perf_Info[registerCount];
		
		rx.setReadByteCount(readByteCount);
		rx.setStartAddress(rx.getTxInfo().getStartAddress());		
		
		//RX_Info가 요청정보(TX_Info)를 가지고 있으면
	    //Perf_Info에게 요청정보(TX_Info)를 알려준다.
		if(rx.isHasTxInfo()) {			
			Perf_Info.setTxInfo(rx.getTxInfo());
			Perf_Info.setHasTxInfo(true);
			startAddress = rx.getTxInfo().getStartAddress();
		}
		
		// 성능 값 초기화
		try {
			try {
				for (int i = 0; i < registerCount; i++) {
					
					int registerValue = in.readShort();
	
					// 레지스터에서 읽은 값을 성능 인스턴스에 저장한다.
					// 나중에 이 부분을 데이터타입에 따라 적절하게 변경하자.
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
		rx.setActualPacket(in.debug_getBuffer()); // 실제 패킷 내용 저장
				
		rx.setScanResult("Good");
		return rx;
	}

}

