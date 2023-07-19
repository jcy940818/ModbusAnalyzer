package src_en.exception;

public class PacketFormatException extends Exception {
	public PacketFormatException(){
		super("Invalid Modbus Type\n\nPlese Check Modbus Type ( TCP / RTU )");
	}		
}