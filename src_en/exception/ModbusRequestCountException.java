package src_en.exception;

public class ModbusRequestCountException extends Exception {
	public ModbusRequestCountException() {
		super("Modbus can request up to 125 Modbus requests at once");
	}
}