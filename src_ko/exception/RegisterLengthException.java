package src_ko.exception;

import src_ko.util.Util;

public class RegisterLengthException extends Exception {
	public RegisterLengthException(){
		super("올바른 모드버스 구조의 응답 패킷이 아닙니다" + Util.separator + Util.separator + "\n\n" + "응답 패킷의 데이터 길이가 올바르지 않습니다\n");		
	}		
}	