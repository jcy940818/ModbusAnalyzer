package src_en.exception;

public class WriteControlPacketException extends Exception {
	// GUI �������� ���� �� ��
	public WriteControlPacketException(){
		super("don't support generating control request packets");
	}		
}