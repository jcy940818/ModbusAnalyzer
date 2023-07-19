package src_ko.exception;

public class WriteControlPacketException extends Exception {
	// GUI 버전에서 구현 할 것
	public WriteControlPacketException(){
		super("제어 요청 패킷 생성은 지원하지 않습니다");
	}		
}