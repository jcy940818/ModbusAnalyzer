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
		
		StringBuilder sb = new StringBuilder("<font color='red'>��û ��Ŷ�� ���� ��Ŷ�� ������ ����ġ</font>&nbsp;&nbsp;&nbsp;&nbsp;\n");
		sb.append("TX�� RX�� ");
			
		sb.append("<font color='blue'>");
		
		if((tx.isRTU()==rx.isRTU())&&!rx.isRTU()) {
			// Modbus TCP
			if(tx.getTransactionId()!=rx.getTransactionId()) {				
				isSame = false;
				isSameTransactionId = false;
				sb.append("Ʈ�����ID, ");
			}
		}
		
		if(tx.getUnitId()!= rx.getUnitId()) {			
			isSame = false;
			isSameUnitId = false;
			sb.append("����ȣ, ");
		}
		
		if(tx.getFunctionCode()!=rx.getFunctionCode()) {			
			isSame = false;
			isSameFunctionCode = false;
			sb.append("����ڵ�, ");
		}
			
		sb.append("</font>");
		
		if(!isSame) {			
			int lastSeparator = sb.lastIndexOf(",");
			sb.replace(lastSeparator, lastSeparator+1 , "");
			
			sb.append(" ������ ��ġ���� �ʽ��ϴ�"+ Util.longSeparator +"\n");
						
			int index = 1;
			
			if(!isSameTransactionId) sb.append("\n" + String.format("%d. Ʈ�����ID - TX : 0x%04x  /  RX : 0x%04x", index++, (tx.getTransactionId()&0xffff),(rx.getTransactionId()&0xffff)));
			if(!isSameUnitId) sb.append("\n" + String.format("%d. ����ȣ - TX : %d�� ���  /  RX : %d�� ���", index++, tx.getUnitId(), rx.getUnitId()));
			if(!isSameFunctionCode) sb.append("\n" + String.format("%d. ����ڵ� - TX : FC%02x  /  RX : FC%02x", index++, tx.getFunctionCode(), rx.getFunctionCode()));
			
			String msg = "RCU �ϳ��� ���� ��� ������ ��������� Ȯ�����ּ���";		
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
		
		// ����ڵ� 3, 4 �� �϶��� �����Ѵ�
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
			// �ùٸ� ���� ���� �� ���
			return null;
		}else {
			// ������ ����ڵ� 3, 4�� �� ��쿡
			// ���� ��Ŷ�� Length ������ TX�� ��û���� * 2 �̴� (�������� �ϳ��� 2Byte �̱� ������)
			
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
		
		// ����ڵ� 3, 4 �� �϶��� �����Ѵ�
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
			// �ùٸ� ���� ���� �� ���
			return;
		}else {
			// ������ ����ڵ� 3, 4�� �� ��쿡
			// ���� ��Ŷ�� Length ������ TX�� ��û���� * 2 �̴� (�������� �ϳ��� 2Byte �̱� ������) 
			
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s%s\n", Util.colorRed("RX Length is Not Correct"), Util.longSeparator));
			sb.append(String.format("%s%s%s\n\n",Util.colorRed("������ RX(���� ��Ŷ)�� Length ������ �������� �ʽ��ϴ�") ,Util.longSeparator, Util.longSeparator));
			
			sb.append("�ùٸ� ������ ����ڵ� 3, 4���� ���� ���� �� ���\n\n");
			sb.append("RX(���� ��Ŷ)�� " + Util.colorBlue("Length") + "(���� Byte ����) ������\n");
			sb.append("TX(��û ��Ŷ)�� " + Util.colorBlue("Request Count x 2") + "(�������� ��û ���� x 2) ��(��) �Ǿ�� �մϴ�\n\n\n");
			
			sb.append("�׷��Ƿ� ���� �����Ͻ� ��û�� ���� �ùٸ� �����̶��\n\n");
			sb.append(String.format("TX(��û ��Ŷ)���� �������� ������ %s�� ��û�Ͽ�����\n", Util.colorBlue(String.valueOf(requestCount))));
			sb.append(String.format("RX(���� ��Ŷ)�� ���� Byte ���� ������ %s ��(��) �Ǿ�� �մϴ�\n\n\n", 
					Util.colorBlue(String.valueOf(requestCount * 2)) +  Util.colorBlue(" ( " + requestCount + " x 2 )")));
			
			sb.append("������ ���� ������ ������ �Ʒ��� ���� ������ �ֽ��ϴ�\n\n");			
			sb.append(String.format("TX(��û ��Ŷ)�� %s : %s\n", Util.colorBlue("Request Count"), Util.colorBlue(String.valueOf(requestCount))));
			sb.append(String.format("RX(���� ��Ŷ)�� %s : %s ( %s is Not %s )\n", Util.colorRed("Length"), 
					Util.colorRed(String.valueOf(readByteCount)) , 
					Util.colorBlue(String.valueOf(requestCount) + " x 2"),
					Util.colorRed(String.valueOf(readByteCount))));
			sb.append(String.format("RX�� Length(���� Byte ����) ���� %s�� ������ �������� ���� %s���� ��û�� TX�� ���� �����Դϴ�%s\n\n\n",					
					Util.colorRed(String.valueOf(readByteCount)),					
					Util.colorRed(String.valueOf(readByteCount / 2)),
					Util.longSeparator
					));
			
			sb.append("�̷��� ������ ���(Server)���� �������� TX(��û ��Ŷ)�� ó�� �ϴ� ��\n");
			sb.append("���� ����ڰ� ������ ��û�� ���� ������ �ƴ� �ٸ� ��û�� ���� ������ �־��� ��� �߻� �� �� ������\n");
			sb.append(Util.colorRed("��� �߸� ������ ���� ��Ŷ�� ���������� ���� ��Ŷó�� ó���ϰ� �Ǹ� ���� �������� ���� ƥ �� �ֽ��ϴ�") + Util.longSeparator + "\n\n");
			sb.append("���� 1 : ��񿡰� " + Util.colorBlue("�µ�( �� )") + " �� �� ��û�Ͽ����� ��񿡼� " + Util.colorRed("����( % )") + " �� �� ���� �� ���\n");
			sb.append("���� 2 : ��񿡰� " + Util.colorBlue("����( V )") + " �� �� ��û�Ͽ����� ��񿡼� " + Util.colorRed("����( A )") +" �� �� ���� �� ���\n");
			
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
			case 0x01 : msg = "��񿡼� �������� �ʴ� ����ڵ带 ��û�Ͽ� �߻��� ����, ��û�Ͻ� ����ڵ� ������ Ȯ�����ּ���"; break;									
			// Illegal Data Address
			case 0x02 : msg = "��񿡼� �������� �ʴ� �������� �ּҸ� ��û�Ͽ� �߻��� ����, ��û�Ͻ� �ּ� ������ Ȯ�����ּ���"; break;									
			// Illegal Data Value
			case 0x03 : msg = "��񿡼� ������  �ʴ� �����Ͱ� ���ԵǾ� �߻��� ����, ��û�Ͻ� �ּ��� ������ ������ Ȯ�����ּ���"; break;									
			// Slave Device Failure
			case 0x04 : msg = "��񿡼� ��û�� ó���ϴ� �� ���� �� �� ���� ���� �߻�"; break;
			// Acknowledge
			case 0x05 : msg = "��񿡼� ��û�� �����Ͽ� ó�� ��������, ó���� ���Ͽ� ����� �ð��� �ʿ��մϴ� (�ַ� Ŭ���̾�Ʈ ���� Ÿ�Ӿƿ��� �����ϱ� ���� �߻��� ����)"; break;
			// Slave Device Busy
			case 0x06 : msg = "��� �������� ��û(TX)�� �ް�, �׿� ���� ������ �����ϴ� ���� ���û(TX)�� ���� �� ��� �߻��ϴ� ����"; break;
			// Negative Acknowledge
			case 0x07 : msg = "��û���� ������ ��񿡼� ó�� �� �� �����ϴ�"; break;
			// Memory Parity Error
			case 0x08 : msg = "��񿡼� ��û ��Ŷ ó���� ���Ͽ� �ؼ��� �޸𸮿��� �и�Ƽ ������ �����Ͽ����ϴ�"; break;
			// Gateway Path Unavailable
			case 0x0a :	msg = "Gateway ������ ���� ����, RCU ������ ��� ������ Ȯ�����ּ���"; break;
			// Gateway Target Device Failed to Respond
			case 0x0b : msg = "Gateway ������ ���� ����, RCU ������ ��� ������ Ȯ�����ּ���"; break;
			
			default : msg = null; break;
		}
		
		return msg;
	}
}
