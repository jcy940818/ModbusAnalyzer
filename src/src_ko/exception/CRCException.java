package src_ko.exception;

import src_ko.util.Util;

public class CRCException extends Exception {
	public CRCException(){
		super("CRC ������ �߸��� ��Ŷ�Դϴ�" + Util.separator + Util.separator + "\n");
	}		
}