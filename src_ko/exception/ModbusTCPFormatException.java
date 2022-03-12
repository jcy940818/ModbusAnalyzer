package src_ko.exception;

public class ModbusTCPFormatException extends Exception {
	public ModbusTCPFormatException(){
		super("Modbus TCP 헤더 정보가 잘못되었습니다\n\nModbus Type을 확인해주세요(TCP/RTU)");
	}		
}