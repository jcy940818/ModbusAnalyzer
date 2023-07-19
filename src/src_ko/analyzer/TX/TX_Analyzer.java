package src_ko.analyzer.TX;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import src_ko.exception.CRCException;
import src_ko.exception.ModbusFormatException;
import src_ko.exception.ModbusTCPFormatException;
import src_ko.info.TX_Info;
import src_ko.util.PacketGenerator;
import src_ko.util.PacketInputStream;
import src_ko.util.Util;

public class TX_Analyzer {
					
	/**
	 * @return Modbus RTU TX - Information
	 */
	public TX_Info rtuAnalysis(TX_Info txContent) throws IOException {
		TX_Info tx = new TX_Info();
		tx.setRTU(true);
		
		String TX = "";

		TX = txContent.getContent();
		TX = TX.replaceAll(" ", "");
		tx.setContent(TX);
		
		PacketInputStream in = null;
		byte[] packet;
		byte[] buff = new byte[8192];
		int unitId = 0;
		int functionCode = 0;
		int startAddress = 0;
		int requestCount = 0;
		int crc = 0;
				
		try {
			try {
				packet = PacketGenerator.getOriginPacket(TX);
				in = new PacketInputStream(buff, new ByteArrayInputStream(packet));						
				
				unitId = in.read();
				tx.setUnitId(unitId);
							
				functionCode = in.read();
				tx.setFunctionCode(functionCode);
				
				if((tx.getFunctionCode() == 15)||(tx.getFunctionCode() == 16)){
					Util.showMessage("다중 값 제어는 지원하지 않습니다" + Util.longSeparator, JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				// 제어 처리
				if (functionCode == 0x05) {
					return new Control_TX_Analyzer(in).readRTU_StatusControl(tx);
				} else if (functionCode == 0x06) {				
					return new Control_TX_Analyzer(in).readRTU_RegisterControl(tx);
				}
							
				startAddress = in.readShort();
				tx.setStartAddress(startAddress);
								
				requestCount = in.readUnsignedShort();
				tx.setEndAddress(startAddress + (requestCount-1));
				
				tx.setRequestCount(requestCount);						
			}catch(Exception e) {			
				throw new ModbusFormatException();
			}
		} catch (ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("TX Initialize Exception\n" + e.getMessage() + Util.longSeparator, JOptionPane.ERROR_MESSAGE);
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
			Util.showMessage("TX Initialize Exception\n" + e.getMessage() + Util.longSeparator, JOptionPane.ERROR_MESSAGE);
			return null;
		}

		tx.setCrc(crc & 0xffff);

		return tx;
	}
	

		
	
	/**
	 * @return Modbus TCP TX - Information
	 */
	public TX_Info tcpAnalysis(TX_Info txContent) throws IOException {
		TX_Info tx = new TX_Info();
		tx.setRTU(false);	
		
		String TX = "";
		
		TX = txContent.getContent();
		TX = TX.replaceAll(" ", "");
		tx.setContent(TX);
				
		PacketInputStream in = null;
		byte[] packet = null; 
		byte[] buff = new byte[8192];
		
		// Modbus TCP : Header
		int transactionId = 0;
		int protocolId = 0;
		int length = 0;
		
		int unitId = 0;
		int functionCode = 0;
		int startAddress = 0;
		int requestCount = 0;
		
		try {
			try {
				packet = PacketGenerator.getOriginPacket(TX);
				in = new PacketInputStream(buff, new ByteArrayInputStream(packet));
				
				transactionId = in.readUnsignedShort();
				tx.setTransactionId(transactionId);
				
				protocolId = in.readShort();
				tx.setProtocolId(protocolId);
				
				length = in.readShort();
				tx.setLength(length);
				
				unitId = in.read();
				tx.setUnitId(unitId);
				
				functionCode = in.read();
				tx.setFunctionCode(functionCode);
				
				if((tx.getFunctionCode() == 15)||(tx.getFunctionCode() == 16)){
					Util.showMessage("다중 값 제어는 지원하지 않습니다" + Util.longSeparator, JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				// 제어 처리
				if (functionCode == 0x05) {
					return new Control_TX_Analyzer(in).readTCP_StatusControl(tx);
				} else if (functionCode == 0x06) {
					return new Control_TX_Analyzer(in).readTCP_RegisterControl(tx);
				}
							
				startAddress = in.readShort();
				tx.setStartAddress(startAddress);
				
				
				requestCount = in.readUnsignedShort();
				tx.setRequestCount(requestCount);
				tx.setEndAddress(startAddress + (requestCount - 1));
				
			} catch(Exception e) {
				throw new ModbusFormatException();
			}	
		} catch (ModbusFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("TX Initialize Exception\n" + e.getMessage() + Util.longSeparator, JOptionPane.ERROR_MESSAGE);
			return null;
		}

		
		// Modbus TCP Header Inspect
		try {
			if ((tx.getProtocolId() != 0x0000) || (tx.getLength() != 0x0006)) {
				// Modbus TCP Header : protocolID는 0x0000 으로 고정되어 있다.
				// Modbus TCP Header : Length는 0x0006 으로 고정되어 있다.
				throw new ModbusTCPFormatException();
			}
		} catch (ModbusTCPFormatException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Util.showMessage("TX Initialize Exception\n" + e.getMessage() + Util.longSeparator, JOptionPane.ERROR_MESSAGE);
			return null;
		}
	
		return tx;
	}
}
