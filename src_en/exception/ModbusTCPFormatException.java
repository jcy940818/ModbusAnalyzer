package src_en.exception;

public class ModbusTCPFormatException extends Exception {
	public ModbusTCPFormatException(){
		super("Invalid Modbus TCP Header data\n\nPlese Check Modbus Type ( TCP / RTU )");
	}		
}