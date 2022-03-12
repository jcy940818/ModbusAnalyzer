package src_en.exception;

public class ReadControlPacketException extends Exception {
	public ReadControlPacketException(){
		super("Multiple Register controls are not supported");
	}		
}