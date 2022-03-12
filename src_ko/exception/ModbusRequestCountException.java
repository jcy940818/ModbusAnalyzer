package src_ko.exception;

public class ModbusRequestCountException extends Exception {
	public ModbusRequestCountException() {
		super("Modbus TX는 한번의 요청으로 최대 127개의 성능을 요청할 수 있습니다");
	}
}