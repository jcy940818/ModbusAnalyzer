package src_ko.exception;

public class UnknownFunctionCodeException extends Exception {
	public UnknownFunctionCodeException(){
		super("알 수 없는 Function Code 입니다");
	}		
}