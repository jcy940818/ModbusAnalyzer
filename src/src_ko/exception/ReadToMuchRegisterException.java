package src_ko.exception;

public class ReadToMuchRegisterException extends Exception {
	public ReadToMuchRegisterException(){
		super("TX에서 요청한 레지스터 개수보다 더 많은 레지스터를 읽었습니다");
	}		
}