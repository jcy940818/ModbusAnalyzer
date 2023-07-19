package src_en.exception;

import src_en.util.Util;

public class RegisterLengthException extends Exception {
	public RegisterLengthException(){
		super("It's not a response packet with the correct Modbus structure" + Util.separator + Util.separator + "\n\n" + "The data length of the response packet is not correct\n");		
	}		
}	