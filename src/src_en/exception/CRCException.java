package src_en.exception;

import src_en.util.Util;

public class CRCException extends Exception {
	public CRCException(){
		super("The packet has an invalid CRC value" + Util.separator + Util.separator + "\n");
	}		
}