package src_en.analyzer.TX;

import java.io.IOException;

import javax.swing.JOptionPane;

import src_en.exception.CRCException;
import src_en.exception.ModbusFormatException;
import src_en.exception.ModbusTCPFormatException;
import src_en.info.TX_Info;
import src_en.util.PacketInputStream;
import src_en.util.Util;


public class Control_TX_Analyzer {
		
	PacketInputStream in = null;

	public Control_TX_Analyzer(PacketInputStream in) {
		this.in = in;
	}

	// RTU FunctionCode : 05
	public TX_Info readRTU_StatusControl(TX_Info tx) {
		int startAddress = 0;
		int controlStatus = 0;
		int crc = 0;

		try {
			try {
				startAddress = in.readShort();// 제어 할 주소
				tx.setStartAddress(startAddress); // 제어 Register 주소

				controlStatus = in.readShort(); // 0xFF00 : ON / 0x0000 : OFF

				if ((controlStatus & 0xffff) == 0xff00) {
					tx.setControlValue(0xff00);
					tx.setControlStatus("ON");
				} else if ((controlStatus & 0xffff) == 0x0000) {
					tx.setControlValue(0x0000);
					tx.setControlStatus("OFF");
				} else {
					// 제어 값은 0xFF00(ON) / 0x0000(OFF)만 쓸 수 있다.
					throw new ModbusFormatException();
				}
			} catch (Exception e) {
				throw new ModbusFormatException();
			}
		} catch (ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("<font color='red'>TX Initialize Exception</font>"
					+ "\n"
					+ "Function Code 0x05 : Can Write value only 0xFF00(ON) / 0x0000(OFF)"
					+ Util.separator
					+ Util.separator
					+ "\n", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		try {
			try {
				crc = in.readCRC16();
				crc = (crc & 0xffff);
			} catch (IOException e) {
				throw new CRCException();
			}
		} catch (CRCException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("TX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			return null;
		}

		tx.setCrc(crc);

		return tx;
	}
		
		
		
	// RTU FunctionCode : 06
	public TX_Info readRTU_RegisterControl(TX_Info tx) {
		int startAddress = 0; // 제어 할 주소
		int controlValue = 0; // 제어 할 주소에 쓸 값
		int crc = 0;

		try {
			try {
				startAddress = in.readShort(); // 제어 할 주소
				tx.setStartAddress(startAddress); // 제어 Register 주소

				controlValue = in.readShort(); // 제어 설정 값
				tx.setControlValue(controlValue);

			} catch (Exception e) {
				throw new ModbusFormatException();
			}
		} catch (ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("TX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			return null;
		}

		try {
			try {
				crc = in.readCRC16();
				crc = (crc & 0xffff);
			} catch (IOException e) {
				throw new CRCException();
			}
		} catch (CRCException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("TX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			return null;
		}

		tx.setCrc(crc);

		return tx;
	}
		
		
		
		
	// TCP FunctionCode : 05
	public TX_Info readTCP_StatusControl(TX_Info tx) {
		int startAddress = 0;
		int controlStatus = 0;

		try {
			try {
				startAddress = in.readShort();// 제어 할 주소
				tx.setStartAddress(startAddress); // 제어 Register 주소

				controlStatus = in.readShort(); // 0xFF00 : ON / 0x0000 : OFF

				if ((controlStatus & 0xffff) == 0xff00) {
					tx.setControlValue(0xff00);
					tx.setControlStatus("ON");
				} else if ((controlStatus & 0xffff) == 0x0000) {
					tx.setControlValue(0x0000);
					tx.setControlStatus("OFF");
				} else {
					// 제어 값은 0xFF00(ON) / 0x0000(OFF)만 쓸 수 있다.
					throw new Exception();
				}

			} catch (Exception e) {
				throw new ModbusFormatException();
			}
		} catch (ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("<font color='red'>TX Initialize Exception</font>"
					+ "\n"
					+ "Function Code 0x05 : Can Write value only 0xFF00(ON) / 0x0000(OFF)"
					+ Util.separator
					+ Util.separator
					+ "\n", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		try {
			// Modbus TCP Header Inspect
			if ((tx.getProtocolId() != 0x0000) || (tx.getLength() != 0x0006)) {
				// Modbus TCP Header : protocolID는 0x0000 으로 고정되어 있다.
				// Modbus TCP Header : Length는 0x0006 으로 고정되어 있다.
				throw new ModbusTCPFormatException();
			}
		} catch (ModbusTCPFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("TX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return tx;
	}

		
		
		// TCP FunctionCode : 06
	public TX_Info readTCP_RegisterControl(TX_Info tx) {
		int startAddress = 0; // 제어 할 주소
		int controlValue = 0; // 제어 할 주소에 쓸 값

		try {
			try {
				startAddress = in.readShort(); // 제어 할 주소
				tx.setStartAddress(startAddress); // 제어 Register 주소

				controlValue = in.readShort(); // 제어 설정 값
				tx.setControlValue(controlValue);

			} catch (Exception e) {
				throw new ModbusFormatException();
			}
		} catch (ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("TX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			return null;
		}

		try {
			if ((tx.getProtocolId() != 0x0000) || (tx.getLength() != 0x0006)) {
				// Modbus TCP Header : protocolID는 0x0000 으로 고정되어 있다.
				// Modbus TCP Header : Length는 0x0006 으로 고정되어 있다.
				throw new ModbusTCPFormatException();
			}
		} catch (ModbusTCPFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("TX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return tx;
	}
	
}
