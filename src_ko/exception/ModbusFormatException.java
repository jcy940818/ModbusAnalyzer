package src_ko.exception;

import src_ko.util.Util;

public class ModbusFormatException extends Exception {
	public ModbusFormatException() {
		super("Modbus 통신의 패킷 구조가 아닙니다\n\nModbus Type을 확인해주세요(TCP/RTU)" + Util.separator + Util.separator +"\n");
	}					
}