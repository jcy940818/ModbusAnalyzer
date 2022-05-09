package common.modbus;

public class ModbusWatchPointInitException extends Exception {

	public ModbusWatchPointInitException(String modbusPointName) {
		super(modbusPointName);
	}
	
}
