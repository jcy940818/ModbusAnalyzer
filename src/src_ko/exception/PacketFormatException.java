package src_ko.exception;

public class PacketFormatException extends Exception {
	public PacketFormatException(){
		super("Modbus Type이 잘못되었습니다\n\nModbus Type을 확인해주세요(TCP/RTU)");
	}		
}