package src_ko.exception;

import src_ko.util.Util;

public class ModbusFormatException extends Exception {
	public ModbusFormatException() {
		super("Modbus ����� ��Ŷ ������ �ƴմϴ�\n\nModbus Type�� Ȯ�����ּ���(TCP/RTU)" + Util.separator + Util.separator +"\n");
	}					
}