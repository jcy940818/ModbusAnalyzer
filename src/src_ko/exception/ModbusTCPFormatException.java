package src_ko.exception;

public class ModbusTCPFormatException extends Exception {
	public ModbusTCPFormatException(){
		super("Modbus TCP ��� ������ �߸��Ǿ����ϴ�\n\nModbus Type�� Ȯ�����ּ���(TCP/RTU)");
	}		
}