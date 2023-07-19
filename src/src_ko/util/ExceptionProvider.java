package src_ko.util;

import javax.swing.JOptionPane;

import src_ko.exception.ReciveExceptionRX_H01;
import src_ko.exception.ReciveExceptionRX_H02;
import src_ko.exception.ReciveExceptionRX_H03;
import src_ko.exception.ReciveExceptionRX_H04;
import src_ko.exception.ReciveExceptionRX_H05;
import src_ko.exception.ReciveExceptionRX_H06;
import src_ko.exception.ReciveExceptionRX_H07;
import src_ko.exception.ReciveExceptionRX_H08;
import src_ko.exception.ReciveExceptionRX_H09;
import src_ko.exception.ReciveExceptionRX_H0A;
import src_ko.exception.ReciveExceptionRX_H0B;
import src_ko.info.RX_Info;
import src_ko.info.TX_Info;

public class ExceptionProvider {
	
	public static StringBuilder CompareTxRx(TX_Info tx, RX_Info rx) {
		boolean isSame = true;
		boolean isSameTransactionId = true;
		boolean isSameUnitId = true;
		boolean isSameFunctionCode = true;
		
		if(rx.getFunctionCode() >= 0x80) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder("<font color='red'>요청 패킷과 응답 패킷의 데이터 불일치</font>&nbsp;&nbsp;&nbsp;&nbsp;\n");
		sb.append("TX와 RX의 ");
			
		sb.append("<font color='blue'>");
		
		if((tx.isRTU()==rx.isRTU())&&!rx.isRTU()) {
			// Modbus TCP
			if(tx.getTransactionId()!=rx.getTransactionId()) {				
				isSame = false;
				isSameTransactionId = false;
				sb.append("트랜잭션ID, ");
			}
		}
		
		if(tx.getUnitId()!= rx.getUnitId()) {			
			isSame = false;
			isSameUnitId = false;
			sb.append("장비번호, ");
		}
		
		if(tx.getFunctionCode()!=rx.getFunctionCode()) {			
			isSame = false;
			isSameFunctionCode = false;
			sb.append("기능코드, ");
		}
			
		sb.append("</font>");
		
		if(!isSame) {			
			int lastSeparator = sb.lastIndexOf(",");
			sb.replace(lastSeparator, lastSeparator+1 , "");
			
			sb.append(" 정보가 일치하지 않습니다"+ Util.longSeparator +"\n");
						
			int index = 1;
			
			if(!isSameTransactionId) sb.append("\n" + String.format("%d. 트랜잭션ID - TX : 0x%04x  /  RX : 0x%04x", index++, (tx.getTransactionId()&0xffff),(rx.getTransactionId()&0xffff)));
			if(!isSameUnitId) sb.append("\n" + String.format("%d. 장비번호 - TX : %d번 장비  /  RX : %d번 장비", index++, tx.getUnitId(), rx.getUnitId()));
			if(!isSameFunctionCode) sb.append("\n" + String.format("%d. 기능코드 - TX : FC%02x  /  RX : FC%02x", index++, tx.getFunctionCode(), rx.getFunctionCode()));
			
			String msg = "RCU 하나에 여러 장비가 물려서 통신중인지 확인해주세요";		
			sb.append("\n\n" + msg);
			
			return sb;
		}else {
			return null;
		}		
	}
	
	public static String getCompareTxRxString(TX_Info tx, RX_Info rx) {
		boolean isSame = true;
		boolean isSameTransactionId = true;
		boolean isSameUnitId = true;
		boolean isSameFunctionCode = true;
		
		if(rx.getFunctionCode() >= 0x80) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder("[  Warning!  TX & RX ");
		
		if((tx.isRTU()==rx.isRTU())&&!rx.isRTU()) {
			// Modbus TCP
			if(tx.getTransactionId()!=rx.getTransactionId()) {				
				isSame = false;
				isSameTransactionId = false;
				sb.append("\"Transaction ID\", ");
			}
		}
		
		if(tx.getUnitId()!= rx.getUnitId()) {			
			isSame = false;
			isSameUnitId = false;
			sb.append("\"Unit ID\", ");
		}
		
		if(tx.getFunctionCode()!=rx.getFunctionCode()) {			
			isSame = false;
			isSameFunctionCode = false;
			sb.append("\"Function Code\", ");
		}
		
		if(!isSame) {			
			int lastSeparator = sb.lastIndexOf(",");
			sb.replace(lastSeparator, lastSeparator+1 , "");
			
			sb.append("Data Mismatch!");
			sb.append("  ]");
			
//			int index = 1;
//			if(!isSameTransactionId) sb.append("\n" + String.format("%d. Transaction ID => TX:0x%04x != RX:0x%04x", index++, (tx.getTransactionId()&0xffff),(rx.getTransactionId()&0xffff)));
//			if(!isSameUnitId) sb.append("\n" + String.format("%d. Unit ID => TX:%d != RX:%d", index++, tx.getUnitId(), rx.getUnitId()));
//			if(!isSameFunctionCode) sb.append("\n" + String.format("%d. Function Code => TX:FC%02x != RX:FC%02x", index++, tx.getFunctionCode(), rx.getFunctionCode()));
//			sb.append("\n");
			
			return sb.toString();
		}else {
			return null;
		}
	}
	
	public static String getRxLengthCheckResult(TX_Info tx, RX_Info rx) {
		
		// 기능코드 3, 4 번 일때만 수행한다
		if(!(tx.getFunctionCode() >= 0x03 && tx.getFunctionCode() <= 0x04)
			|| !(rx.getFunctionCode() >= 0x03 && rx.getFunctionCode() <= 0x04)
			|| rx == null
			|| rx.isException()
			|| rx.isCRCError()) {
			return null;
		}
		
		int requestCount = tx.getRequestCount();
		int readByteCount = rx.getReadByteCount();
		
		if((requestCount * 2) == readByteCount) {
			// 올바른 응답 내용 일 경우
			return null;
		}else {
			// 모드버스 기능코드 3, 4번 일 경우에
			// 응답 패킷의 Length 정보는 TX의 요청개수 * 2 이다 (레지스터 하나에 2Byte 이기 때문에)
			
			StringBuilder sb = new StringBuilder("[  Warning!  Wrong RX Data Length : TX Request Count( ");
			sb.append(requestCount);
			sb.append(" ) * 2 is not ");
			sb.append("RX Data Length( ");
			sb.append(readByteCount);
			sb.append(" )  ]");
			
			return sb.toString();
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
			sb.append(String.format("%s%s%s\n\n",Util.colorRed("수신한 RX(응답 패킷)의 Length 내용이 적절하지 않습니다") ,Util.longSeparator, Util.longSeparator));
			
			sb.append("올바른 모드버스 기능코드 3, 4번에 대한 응답 일 경우\n\n");
			sb.append("RX(응답 패킷)의 " + Util.colorBlue("Length") + "(읽은 Byte 길이) 정보는\n");
			sb.append("TX(요청 패킷)의 " + Util.colorBlue("Request Count x 2") + "(레지스터 요청 개수 x 2) 이(가) 되어야 합니다\n\n\n");
			
			sb.append("그러므로 현재 전송하신 요청에 대한 올바른 응답이라면\n\n");
			sb.append(String.format("TX(요청 패킷)으로 레지스터 개수를 %s개 요청하였으니\n", Util.colorBlue(String.valueOf(requestCount))));
			sb.append(String.format("RX(응답 패킷)의 읽은 Byte 길이 내용은 %s 이(가) 되어야 합니다\n\n\n", 
					Util.colorBlue(String.valueOf(requestCount * 2)) +  Util.colorBlue(" ( " + requestCount + " x 2 )")));
			
			sb.append("하지만 현재 수신한 내용은 아래와 같이 문제가 있습니다\n\n");			
			sb.append(String.format("TX(요청 패킷)의 %s : %s\n", Util.colorBlue("Request Count"), Util.colorBlue(String.valueOf(requestCount))));
			sb.append(String.format("RX(응답 패킷)의 %s : %s ( %s is Not %s )\n", Util.colorRed("Length"), 
					Util.colorRed(String.valueOf(readByteCount)) , 
					Util.colorBlue(String.valueOf(requestCount) + " x 2"),
					Util.colorRed(String.valueOf(readByteCount))));
			sb.append(String.format("RX의 Length(읽은 Byte 길이) 값이 %s인 응답은 레지스터 개수 %s개를 요청한 TX에 대한 응답입니다%s\n\n\n",					
					Util.colorRed(String.valueOf(readByteCount)),					
					Util.colorRed(String.valueOf(readByteCount / 2)),
					Util.longSeparator
					));
			
			sb.append("이러한 현상은 장비(Server)에서 여러개의 TX(요청 패킷)을 처리 하던 중\n");
			sb.append("현재 사용자가 전송한 요청에 대한 응답이 아닌 다른 요청에 대한 응답을 주었을 경우 발생 할 수 있으며\n");
			sb.append(Util.colorRed("장비가 잘못 전송한 응답 패킷을 수신측에서 정상 패킷처럼 처리하게 되면 현재 수집중인 값이 튈 수 있습니다") + Util.longSeparator + "\n\n");
			sb.append("예시 1 : 장비에게 " + Util.colorBlue("온도( ℃ )") + " 값 을 요청하였으나 장비에서 " + Util.colorRed("습도( % )") + " 값 을 응답 할 경우\n");
			sb.append("예시 2 : 장비에게 " + Util.colorBlue("전압( V )") + " 값 을 요청하였으나 장비에서 " + Util.colorRed("전류( A )") +" 값 을 응답 할 경우\n");
			
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
	
	public static String getExceptionContent(RX_Info rx) {
		String msg =  null;
		
		switch(rx.getExceptionCode()) {
			// Illegal Function
			case 0x01 : msg = "장비에서 지원하지 않는 기능코드를 요청하여 발생한 예외, 요청하신 기능코드 내용을 확인해주세요"; break;									
			// Illegal Data Address
			case 0x02 : msg = "장비에서 지원하지 않는 레지스터 주소를 요청하여 발생한 예외, 요청하신 주소 내용을 확인해주세요"; break;									
			// Illegal Data Value
			case 0x03 : msg = "장비에서 허용되지  않는 데이터가 포함되어 발생한 예외, 요청하신 주소의 데이터 내용을 확인해주세요"; break;									
			// Slave Device Failure
			case 0x04 : msg = "장비에서 요청을 처리하던 중 복구 할 수 없는 오류 발생"; break;
			// Acknowledge
			case 0x05 : msg = "장비에서 요청을 수신하여 처리 중이지만, 처리를 위하여 충분한 시간이 필요합니다 (주로 클라이언트 측의 타임아웃을 방지하기 위해 발생한 예외)"; break;
			// Slave Device Busy
			case 0x06 : msg = "장비가 지속적인 요청(TX)을 받고, 그에 대한 응답을 수행하는 도중 재요청(TX)이 전송 된 경우 발생하는 예외"; break;
			// Negative Acknowledge
			case 0x07 : msg = "요청받은 내용을 장비에서 처리 할 수 없습니다"; break;
			// Memory Parity Error
			case 0x08 : msg = "장비에서 요청 패킷 처리를 위하여 해석중 메모리에서 패리티 오류를 감지하였습니다"; break;
			// Gateway Path Unavailable
			case 0x0a :	msg = "Gateway 문제로 인한 예외, RCU 컨버터 통신 세팅을 확인해주세요"; break;
			// Gateway Target Device Failed to Respond
			case 0x0b : msg = "Gateway 문제로 인한 예외, RCU 컨버터 통신 세팅을 확인해주세요"; break;
			
			default : msg = null; break;
		}
		
		return msg;
	}
}
