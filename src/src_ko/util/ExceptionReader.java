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
				// ���� ��Ŷ�̸鼭 CRC���� �߸��� ���
//				Util.showMessage("RX Initialize Exception\n" + "CRC�� ��ġ���� �ʴ� ��Ŷ�Դϴ� " + Util.separator + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
				rx.setCRCError(isCrcError);
			}
		}catch(NumberFormatException e) {
			// �߸��� CRC�� int Ÿ������ ��ȯ �� �߻��� Exception ó�� (�ַ� ���ڿ��� ���������� ��ȯ�Ϸ��ٰ� ���� �߻�)
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
