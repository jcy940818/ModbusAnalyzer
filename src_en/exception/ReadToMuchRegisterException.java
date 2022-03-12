package src_en.exception;

public class ReadToMuchRegisterException extends Exception {
	public ReadToMuchRegisterException(){
		super("Read more registers than I expected");
	}		
}