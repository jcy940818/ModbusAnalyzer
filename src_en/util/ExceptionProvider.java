package src_en.util;

import javax.swing.JOptionPane;

import src_en.exception.ReciveExceptionRX_H01;
import src_en.exception.ReciveExceptionRX_H02;
import src_en.exception.ReciveExceptionRX_H03;
import src_en.exception.ReciveExceptionRX_H04;
import src_en.exception.ReciveExceptionRX_H05;
import src_en.exception.ReciveExceptionRX_H06;
import src_en.exception.ReciveExceptionRX_H07;
import src_en.exception.ReciveExceptionRX_H08;
import src_en.exception.ReciveExceptionRX_H09;
import src_en.exception.ReciveExceptionRX_H0A;
import src_en.exception.ReciveExceptionRX_H0B;
import src_en.info.RX_Info;
import src_en.info.TX_Info;

public class ExceptionProvider {
	
	public static StringBuilder CompareTxRx(TX_Info tx, RX_Info rx) {
		boolean isSame = true;
		boolean isSameTransactionId = true;
		boolean isSameUnitId = true;
		boolean isSameFunctionCode = true;
		
		if(rx.getFunctionCode() >= 0x80) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder("<font color='red'>TX, RX Data mismatch</font>&nbsp;&nbsp;&nbsp;&nbsp;\n");
		sb.append("TX - RX ");
			
		sb.append("<font color='blue'>");
		
		if((tx.isRTU()==rx.isRTU())&&!rx.isRTU()) {
			// Modbus TCP
			if(tx.getTransactionId()!=rx.getTransactionId()) {				
				isSame = false;
				isSameTransactionId = false;
				sb.append("TransactionID, ");
			}
		}
		
		if(tx.getUnitId()!= rx.getUnitId()) {			
			isSame = false;
			isSameUnitId = false;
			sb.append("UnitID(Slave ID), ");
		}
		
		if(tx.getFunctionCode()!=rx.getFunctionCode()) {			
			isSame = false;
			isSameFunctionCode = false;
			sb.append("FunctionCode, ");
		}
			
		sb.append("</font>");
		
		if(!isSame) {			
			int lastSeparator = sb.lastIndexOf(",");
			sb.replace(lastSeparator, lastSeparator+1 , "");
			
			sb.append(" Data mismatch"+ Util.longSeparator +"\n");
						
			int index = 1;
			
			if(!isSameTransactionId) sb.append("\n" + String.format("%d. Transaction ID  =>   [ TX : 0x%04x ]   ,   [ RX : 0x%04x ]" , index++, (tx.getTransactionId()&0xffff),(rx.getTransactionId()&0xffff)));
			if(!isSameUnitId) sb.append("\n" + String.format("%d. Unit ID(Slave ID)  =>   [ TX : %d ]   ,   [ RX : %d ]", index++, tx.getUnitId(), rx.getUnitId()));
			if(!isSameFunctionCode) sb.append("\n" + String.format("%d. Function Code  =>   [ TX : FC%02x ]   ,   [ RX : FC%02x ]", index++, tx.getFunctionCode(), rx.getFunctionCode()));
			
			String msg = "Please check if multiple devices are communicating with one converter(RCU)";		
			sb.append("\n\n" + msg);
			
			return sb;
		}else {
			return null;
		}		
	}
	
	

	public static void checkRxLength(TX_Info tx, RX_Info rx) {
		
		// 기능코드 3, 4 번 일때만 수행한다
		if(!(tx.getFunctionCode() >= 0x03 && tx.getFunctionCode() <= 0x04)
			|| !(rx.getFunctionCode() >= 0x03 && rx.getFunctionCode() <= 0x04)
			|| rx == null 
			|| rx.isException()
			|| rx.isCRCError()) {
			return;
		}
		
		int requestCount = tx.getRequestCount();
		int readByteCount = rx.getReadByteCount();
		
		if((requestCount * 2) == readByteCount) {
			// 올바른 응답 내용 일 경우
			return;
		}else {
			// 모드버스 기능코드 3, 4번 일 경우에
			// 응답 패킷의 Length 정보는 TX의 요청개수 * 2 이다 (레지스터 하나에 2Byte 이기 때문에) 
			
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s%s\n", Util.colorRed("RX Length is Not Correct"), Util.longSeparator));
			sb.append(String.format("%s%s%s\n\n",Util.colorRed("The length data of the received response packet is not valid") ,Util.longSeparator, Util.longSeparator));
			
			sb.append("If it's a response to the correct Modbus function codes 3 and 4\n\n");
			sb.append("RX(Response Packet) " + Util.colorBlue("Length") + " data is\n");
			sb.append("TX(Request Packet) have to be the " + Util.colorBlue("Request Count x 2") + "\n\n\n");
			
			sb.append("Therefore, if it was the correct response to the request you sent\n\n");
			sb.append(String.format("Since you requested %s registers in the request packet(TX)\n", Util.colorBlue(String.valueOf(requestCount))));
			sb.append(String.format("If it was a correct response packet(RX), the length value of the read byte must be %s\n\n\n", 
					Util.colorBlue(String.valueOf(requestCount * 2)) +  Util.colorBlue(" ( " + requestCount + " x 2 )")));
			
			sb.append("However, the actual data received is not correct as shown below\n\n");			
			sb.append(String.format("%s of TX(Request Packet) : %s\n", Util.colorBlue("Request Count"), Util.colorBlue(String.valueOf(requestCount))));
			sb.append(String.format("%s of RX(Response Packet) : %s ( %s is Not %s )\n", Util.colorRed("Length"), 
					Util.colorRed(String.valueOf(readByteCount)) , 
					Util.colorBlue(String.valueOf(requestCount) + " x 2"),
					Util.colorRed(String.valueOf(readByteCount))));
//			sb.append(String.format("RX의 Length(읽은 Byte 길이) 값이 %s인 응답은 레지스터 개수 %s개를 요청한 TX에 대한 응답입니다%s\n\n\n",
			sb.append(String.format("A response with the Length byte data value of the response packet you received of %s is a response to a packet that requested %s registers"
					+ Util.separator + Util.separator 
					+ "\n\n\n",
					Util.colorRed(String.valueOf(readByteCount)),					
					Util.colorRed(String.valueOf(readByteCount / 2)),
					Util.longSeparator
					));
			
			sb.append("In general, this phenomenon is when the device processes multiple requests at the same time" + Util.separator + Util.separator + "\n");
			sb.append("This phenomenon occurs when you give a response to another request, not a value for the point I requested" + Util.separator + Util.separator + "\n");
			sb.append(Util.colorRed("If a MK119 processes an incorrect response like this, an abnormal value is recorded.") + Util.longSeparator + "\n\n");
			sb.append("Example 1 : The " + Util.colorBlue("Temperature value( ℃ )") +" is requested from the device, but when the device responds to the " + Util.colorRed("Humidity value( % )") + "\n");
			sb.append("Example 2 : The " + Util.colorBlue("Voltage value( V )") +" is requested from the device, but when the device responds to the " + Util.colorRed("Current value( A )") + "\n");
			
			Util.showMessage(sb.toString(), JOptionPane.WARNING_MESSAGE);
		}				
	}
	
		
	public static String InsertTxInformationException(String info) {
		switch (info) {
		case "Transaction ID": return info;
		case "Unit ID": return info;
		case "Function Code": return info;
		case "Start Address": return info;
		case "Request Count": return info;
		default : return "Unkonwn Information";
		}	
	}


	public static void getExceptionContent(int exceptionCode) throws Exception{
		switch(exceptionCode) {
			case 0x01 : throw new ReciveExceptionRX_H01();
			case 0x02 : throw new ReciveExceptionRX_H02();
			case 0x03 : throw new ReciveExceptionRX_H03();
			case 0x04 : throw new ReciveExceptionRX_H04();
			case 0x05 : throw new ReciveExceptionRX_H05();
			case 0x06 : throw new ReciveExceptionRX_H06(); 
			case 0x07 : throw new ReciveExceptionRX_H07();
			case 0x08 : throw new ReciveExceptionRX_H08();
			case 0x09 : throw new ReciveExceptionRX_H09();
			case 0x0a : throw new ReciveExceptionRX_H0A();
			case 0x0b :	throw new ReciveExceptionRX_H0B();
		}
	}
	
}
