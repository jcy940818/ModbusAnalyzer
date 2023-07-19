package src_ko.analyzer.TX;

import java.io.IOException;

import javax.swing.JOptionPane;

import src_ko.exception.CRCException;
import src_ko.exception.ModbusFormatException;
import src_ko.exception.ModbusTCPFormatException;
import src_ko.info.TX_Info;
import src_ko.util.PacketInputStream;
import src_ko.util.Util;


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
				startAddress = in.readShort();// ���� �� �ּ�
				tx.setStartAddress(startAddress); // ���� Register �ּ�

				controlStatus = in.readShort(); // 0xFF00 : ON / 0x0000 : OFF

				if ((controlStatus & 0xffff) == 0xff00) {
					tx.setControlValue(0xff00);
					tx.setControlStatus("ON");
				} else if ((controlStatus & 0xffff) == 0x0000) {
					tx.setControlValue(0x0000);
					tx.setControlStatus("OFF");
				} else {
					// ���� ���� 0xFF00(ON) / 0x0000(OFF)�� �� �� �ִ�.
					throw new ModbusFormatException();
				}
			} catch (Exception e) {
				throw new ModbusFormatException();
			}
		} catch (ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("TX Initialize Exception\n" + "����ڵ� 0x05 : ���� ���� ���� �����δ� 0xFF00(ON) / 0x0000(OFF)�� ��� �� �� �ֽ��ϴ�", JOptionPane.ERROR_MESSAGE);
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
		int startAddress = 0; // ���� �� �ּ�
		int controlValue = 0; // ���� �� �ּҿ� �� ��
		int crc = 0;

		try {
			try {
				startAddress = in.readShort(); // ���� �� �ּ�
				tx.setStartAddress(startAddress); // ���� Register �ּ�

				controlValue = in.readShort(); // ���� ���� ��
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
				startAddress = in.readShort();// ���� �� �ּ�
				tx.setStartAddress(startAddress); // ���� Register �ּ�

				controlStatus = in.readShort(); // 0xFF00 : ON / 0x0000 : OFF

				if ((controlStatus & 0xffff) == 0xff00) {
					tx.setControlValue(0xff00);
					tx.setControlStatus("ON");
				} else if ((controlStatus & 0xffff) == 0x0000) {
					tx.setControlValue(0x0000);
					tx.setControlStatus("OFF");
				} else {
					// ���� ���� 0xFF00(ON) / 0x0000(OFF)�� �� �� �ִ�.
					throw new Exception();
				}

			} catch (Exception e) {
				throw new ModbusFormatException();
			}
		} catch (ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("TX Initialize Exception\n" + "����ڵ� 0x05 : ���� ���� ���� �����δ� 0xFF00(ON) / 0x0000(OFF)�� ��� �� �� �ֽ��ϴ�", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		try {
			// Modbus TCP Header Inspect
			if ((tx.getProtocolId() != 0x0000) || (tx.getLength() != 0x0006)) {
				// Modbus TCP Header : protocolID�� 0x0000 ���� �����Ǿ� �ִ�.
				// Modbus TCP Header : Length�� 0x0006 ���� �����Ǿ� �ִ�.
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
		int startAddress = 0; // ���� �� �ּ�
		int controlValue = 0; // ���� �� �ּҿ� �� ��

		try {
			try {
				startAddress = in.readShort(); // ���� �� �ּ�
				tx.setStartAddress(startAddress); // ���� Register �ּ�

				controlValue = in.readShort(); // ���� ���� ��
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
				// Modbus TCP Header : protocolID�� 0x0000 ���� �����Ǿ� �ִ�.
				// Modbus TCP Header : Length�� 0x0006 ���� �����Ǿ� �ִ�.
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
