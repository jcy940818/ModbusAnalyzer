package src_en.exception;

import src_en.util.Util;

public class ModbusFormatException extends Exception {
	public ModbusFormatException() {
		super("Invalid Modbus Packet structure\n\nPlese Check Modbus Type ( TCP / RTU )" + Util.separator + Util.separator +"\n");
	}					
}