package src_ko.exception;

public class ModbusRequestCountException extends Exception {
	public ModbusRequestCountException() {
		super("Modbus TX�� �ѹ��� ��û���� �ִ� 127���� ������ ��û�� �� �ֽ��ϴ�");
	}
}