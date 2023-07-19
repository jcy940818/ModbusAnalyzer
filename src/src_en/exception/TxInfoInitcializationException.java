package src_en.exception;

public class TxInfoInitcializationException extends Exception {
	public TxInfoInitcializationException(){
		super("An error occurred in the process of initializing the information on the request packet");
	}		
}