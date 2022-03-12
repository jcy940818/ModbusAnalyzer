package src_ko.exception;

public class ReadControlPacketException extends Exception {
	public ReadControlPacketException(){
		super("다중 값 제어 관련 요청은 지원하지 않습니다");
	}		
}