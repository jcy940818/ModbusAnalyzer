package src_en.exception;

public class WriteControlPacketException extends Exception {
	// GUI 버전에서 구현 할 것
	public WriteControlPacketException(){
		super("don't support generating control request packets");
	}		
}