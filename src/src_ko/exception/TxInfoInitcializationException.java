package src_ko.exception;

public class TxInfoInitcializationException extends Exception {
	public TxInfoInitcializationException(){
		super("TX 정보 초기화 중 오류가 발생하였습니다 ");
	}		
}