package src_ko.exception;

public class PacketFormatException extends Exception {
	public PacketFormatException(){
		super("Modbus Type�� �߸��Ǿ����ϴ�\n\nModbus Type�� Ȯ�����ּ���(TCP/RTU)");
	}		
}