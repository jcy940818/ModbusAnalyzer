package src_ko.analyzer.RX;

import java.io.IOException;

import javax.swing.JOptionPane;

import src_ko.exception.CRCException;
import src_ko.exception.ModbusFormatException;
import src_ko.info.RX_Info;
import src_ko.util.PacketInputStream;
import src_ko.util.Util;


public class Control_RX_Analyzer {

	PacketInputStream in = null;	
	
	public Control_RX_Analyzer(PacketInputStream in) {
		this.in = in;
	}
	
	public RX_Info readRTU_ControlResult(RX_Info rx) {		
		
		int functionCode = rx.getFunctionCode();
		int controlAddress; // 제어 된 주소

		int controlValue; // 제어로 쓰인 값
		String controlStatus; // 제어로 쓰인 상태 (ON/OFF)
		int crc = 0;
		
		try {
			try {
				controlAddress = in.readShort();// 제어 할 주소			
				rx.setStartAddress(controlAddress); // 제어  Register 주소
				
				controlValue = in.readShort(); // 0xFF00 : ON / 0x0000 : OFF
				rx.setControlValue(controlValue);
				
				if (functionCode == 0x05) {
					if ((controlValue & 0xffff) == 0xff00) {
						rx.setControlStatus("ON");
					} else if ((controlValue & 0xffff) == 0x0000) {
						rx.setControlStatus("OFF");
					}				
				}// end functionCode inspect
				
			}catch(Exception e) {
				// RX를 읽는 동안 발생하는 모든 예외를 처리
				throw new ModbusFormatException();				
			}
		}catch(ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("RX Initialize Exception\n" + "기능코드 0x05 : 코일 상태 제어 값으로는 0xFF00(ON) / 0x0000(OFF)만 사용 할 수 있습니다", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		
		try {
			try {
				crc = in.readCRC16();
				crc = (crc & 0xffff);
			} catch (IOException e) {
				throw new CRCException();
			}
		}catch(CRCException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("RX Initialize Exception\n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);			
			return null;
		}
				
		rx.setCrc(crc);

		StringBuffer RX = new StringBuffer();
		RX.append(String.format("%02x",rx.getUnitId() & 0xff));
		RX.append(String.format("%02x",rx.getFunctionCode() & 0xff));
		RX.append(String.format("%04x",rx.getStartAddress() & 0xffff));
		RX.append(String.format("%04x",rx.getControlValue() & 0xffff));
		RX.append(String.format("%04x",rx.getCrc() & 0xffff));		
		rx.setContent(RX.toString());
				
		
		return rx;	
	}// end readRTU_ControlResult()

	
	
		
	public RX_Info readTCP_ControlResult(RX_Info rx) {
		int functionCode = rx.getFunctionCode();
		int controlAddress; // 제어 된 주소
		int controlValue; // 제어로 쓰인 값
		String controlStatus; // 제어로 쓰인 상태 (ON/OFF)		
		
		try {
			try {
				controlAddress = in.readShort();// 제어 할 주소			
				rx.setStartAddress(controlAddress); // 제어  Register 주소
				
				controlValue = in.readShort(); // 0xFF00 : ON / 0x0000 : OFF
				rx.setControlValue(controlValue);
				
				if (functionCode == 0x05) {
					if ((controlValue & 0xffff) == 0xff00) {
						rx.setControlStatus("ON");
					} else if ((controlValue & 0xffff) == 0x0000) {
						rx.setControlStatus("OFF");
					}				
				}// end functionCode inspect
				
			}catch(Exception e) {
				// RX를 읽는 동안 발생하는 모든 예외를 처리
				throw new ModbusFormatException();
			}
		}catch(ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("RX Initialize Exception\n" + "기능코드 0x05 : 코일 상태 제어 값으로는 0xFF00(ON) / 0x0000(OFF)만 사용 할 수 있습니다", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		StringBuffer RX = new StringBuffer();
		RX.append(String.format("%04x",rx.getTransactionId() & 0xffff));
		RX.append(String.format("%04x",rx.getProtocolId() & 0xffff));
		RX.append(String.format("%04x",rx.getLength() & 0xffff));
		RX.append(String.format("%02x",rx.getUnitId() & 0xff));
		RX.append(String.format("%02x",rx.getFunctionCode() & 0xff));
		RX.append(String.format("%04x",rx.getStartAddress() & 0xffff));
		RX.append(String.format("%04x",rx.getControlValue() & 0xffff));		
		rx.setContent(RX.toString());

		return rx;		
	}// end readTCP_ControlResult()
	
}
