package src_ko.util;

import java.io.IOException;

import javax.swing.JOptionPane;

import src_ko.info.RX_Info;

public class ExceptionReader {
	PacketInputStream in;
	
	public ExceptionReader(PacketInputStream in) {
		this.in = in;
	}
	
	public RX_Info read_RTU_Exception(RX_Info rx) throws IOException {
		int exceptionCode = in.read();	
		rx.setExceptionCode(exceptionCode);
		
		int crc = 0;
		
		boolean isCrcError = false;
		String errorCRC = "";
		
		try {
			try {
				crc = in.readCRC16();
			} catch (IOException e) {				
				isCrcError = true;
				errorCRC = e.getMessage();
				rx.setCrc(Integer.parseInt(errorCRC) & 0xffff);
				// 예외 패킷이면서 CRC까지 잘못된 경우
//				Util.showMessage("RX Initialize Exception\n" + "CRC가 일치하지 않는 패킷입니다 " + Util.separator + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
				rx.setCRCError(isCrcError);
			}
		}catch(NumberFormatException e) {
			// 잘못된 CRC를 int 타입으로 변환 중 발생한 Exception 처리 (주로 문자열을 정수형으로 변환하려다가 예외 발생)
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("RX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if(!isCrcError) {
			rx.setCrc(crc);
		}
				
		StringBuffer RX = new StringBuffer();
		RX.append(String.format("%02x",rx.getUnitId())); // Unit ID
		RX.append(String.format("%02x",rx.getFunctionCode())); // Exception Function
		RX.append(String.format("%02x",rx.getExceptionCode())); // Exception code
		RX.append(String.format("%04x", (rx.getCrc() & 0xffff))); // CRC
		rx.setContent(RX.toString());						
		
		try {
			ExceptionProvider.getExceptionContent(exceptionCode);
		}catch(Exception e) {
			rx.setExceptionContent(e.getMessage());			
		}
				
		return rx;
	}
	
	
	
	public RX_Info read_TCP_Exception(RX_Info rx) throws IOException {
		int exceptionCode = in.read();	
		rx.setExceptionCode(exceptionCode);								
				
		StringBuffer RX = new StringBuffer();
		RX.append(String.format("%04x", rx.getTransactionId()));
		RX.append(String.format("%04x", rx.getProtocolId()));
		RX.append(String.format("%04x", rx.getLength()));
		RX.append(String.format("%02x",rx.getUnitId())); // Unit ID
		RX.append(String.format("%02x",rx.getFunctionCode())); // Exception Function
		RX.append(String.format("%02x",rx.getExceptionCode())); // Exception code
		rx.setContent(RX.toString());
		
		try {
			ExceptionProvider.getExceptionContent(exceptionCode);
		}catch(Exception e) {
			rx.setExceptionContent(e.getMessage());			
		}

		return rx;
	}
}
