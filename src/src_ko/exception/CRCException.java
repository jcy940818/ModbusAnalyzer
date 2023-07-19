package src_ko.exception;

import src_ko.util.Util;

public class CRCException extends Exception {
	public CRCException(){
		super("CRC 내용이 잘못된 패킷입니다" + Util.separator + Util.separator + "\n");
	}		
}