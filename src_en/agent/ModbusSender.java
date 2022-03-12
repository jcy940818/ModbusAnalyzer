package src_en.agent;

import java.io.IOException;
import java.net.Socket;

import src_en.info.TX_Info;
import src_en.util.PacketInputStream;
import src_en.util.PacketOutputStream;

public class ModbusSender {
			
	// 수집 패킷 전송 
	public static TX_Info sendRTU(Socket client, TX_Info tx) throws IOException {
		byte[] buff = new byte[8192];
		PacketInputStream packetReader = new PacketInputStream(buff, client.getInputStream());
		PacketOutputStream packetWriter = new PacketOutputStream(client.getOutputStream());				
		
		packetWriter.write(tx.getUnitId());
		packetWriter.write(tx.getFunctionCode());
		packetWriter.writeShort(tx.getStartAddress());		
		if(tx.isRead()) {
			packetWriter.writeShort(tx.getRequestCount());	
		}else {
			packetWriter.writeShort(tx.getControlValue());
		}		
		packetWriter.writeShort(tx.getCrc());
		packetWriter.flush();
		
		return tx;	
	}
	
	public static TX_Info sendTCP(Socket client, TX_Info tx) throws IOException {
		byte[] buff = new byte[8192];
		PacketInputStream packetReader = new PacketInputStream(buff, client.getInputStream());
		PacketOutputStream packetWriter = new PacketOutputStream(client.getOutputStream());	
		
		packetWriter.writeShort(tx.getTransactionId());
		packetWriter.writeShort(tx.getProtocolId());
		packetWriter.writeShort(tx.getLength());
		packetWriter.write(tx.getUnitId());
		packetWriter.write(tx.getFunctionCode());	
		packetWriter.writeShort(tx.getStartAddress());
		if(tx.isRead()) {
			packetWriter.writeShort(tx.getRequestCount());	
		}else {
			packetWriter.writeShort(tx.getControlValue());
		}		
		packetWriter.flush();
	
		return tx;
	}
	

	// ModbusAnalyzer CommandLine Version 코드 (참고용으로 남겨둠)	
//	
//	public static TX_Info getRtuTx() throws IOException {
//		PacketOutputStream out = new PacketOutputStream(System.out);
//		TX_Info tx = new TX_Info();
//		tx.setRTU(true);
//		int unitId = 0;
//		int functionCode = 0;
//		int startAddress = 0;
//		int requestCount = 0;
//		int modbusAddress = 0;
//		int crc = 0;
//
//		System.out.print("1. Unit ID(장비번호) : ");
//		unitId = TX_Generator.insertInfo("Unit ID");
//
//		System.out.print("2. Function Code(기능코드) : ");
//		functionCode = TX_Generator.insertInfo("Function Code");
//			
//		System.out.print("3. StartAddress(요청 시작주소) : ");
//		startAddress = TX_Generator.insertInfo("Start Address");
//
//		// 사용자가 모드버스 기준 주소를 입력했을때
//		if(startAddress >= 40001) {
//			functionCode = 0x03;
//			startAddress %= 10000;
//			startAddress -= 1;
//		}else if(startAddress >= 30001) {
//			functionCode = 0x04;
//			startAddress %= 10000;
//			startAddress -= 1;
//		}
//		
//		System.out.print("4. Request Count(요청 개수) : ");
//		requestCount = TX_Generator.insertInfo("Request Count");
//
//		out.write(unitId);
//		out.write(functionCode);
//		out.writeShort(startAddress);
//		out.writeShort(requestCount);
//		crc = out.getCRC16(0);
//		
//		tx.setRTU(true);
//		tx.setUnitId(unitId);
//		tx.setFunctionCode(functionCode);
//		tx.setStartAddress(startAddress);
//		tx.setRequestCount(requestCount);		
//		tx.setEndAddress(startAddress + (requestCount-1));					
//		tx.setCrc(crc);
//		
//		
//		byte[] txInfo = out.toByteArray();
//		StringBuffer TX = new StringBuffer();
//
//		for (int i = 0; i < txInfo.length; i++) {
//			TX.append(String.format("%02x", txInfo[i]));
//		}
//
//		tx.setContent(TX.toString());
//		return tx;
//	}
//	
//	
//	public static TX_Info getTcpTx() throws IOException {
//		PacketOutputStream out = new PacketOutputStream(System.out);
//		TX_Info tx = new TX_Info();
//		tx.setRTU(false);
//		int transactionId = 0;
//		short protocolId = 0x0000;
//		short length = 0x0006; 
//		int unitId = 0;
//		int functionCode = 0;
//		int startAddress = 0;
//		int requestCount = 0;
//		int modbusAddress = 0; 
//		System.out.println();
//		
//		System.out.print("1. Transaction ID : ");
//		transactionId = TX_Generator.insertInfo("Transaction ID");
//		
//		System.out.print("2. Unit ID(장비번호) : ");
//		unitId = TX_Generator.insertInfo("Unit ID");
//		
//		System.out.print("3. FunctionCode(기능코드) : ");
//		functionCode = TX_Generator.insertInfo("Function Code");		
//		
//		System.out.print("4. StartAddress(요청 시작주소) : ");
//		startAddress = TX_Generator.insertInfo("Start Address");
//		
//		// 사용자가 모드버스 기준 주소를 입력했을때
//		if(startAddress >= 40001) {
//			functionCode = 0x03;
//			startAddress %= 10000;
//			startAddress -= 1;
//		}else if(startAddress >= 30001) {
//			functionCode = 0x04;
//			startAddress %= 10000;
//			startAddress -= 1;
//		}
//				
//		System.out.print("5. Request Count(요청 개수) : ");
//		requestCount = TX_Generator.insertInfo("Request Count");
//		
//		out.writeShort(transactionId);
//		out.writeShort(protocolId);
//		out.writeShort(length);
//		out.write(unitId);
//		out.write(functionCode);
//		out.writeShort(startAddress);
//		out.writeShort(requestCount);								      		
//		
//		tx.setRTU(false);
//		tx.setTransactionId(transactionId);
//		tx.setProtocolId(protocolId);
//		tx.setLength(length);
//		tx.setUnitId(unitId);
//		tx.setFunctionCode(functionCode);
//		tx.setStartAddress(startAddress);
//		tx.setRequestCount(requestCount);
//		tx.setEndAddress(startAddress + (requestCount-1));
//	
//		
//		byte[] txInfo = out.toByteArray();		
//		StringBuffer TX = new StringBuffer();
//				
//		
//		for(int i = 0; i < txInfo.length; i++) {
//			TX.append(String.format("%02x",txInfo[i]));
//		}
//							
//		tx.setContent(TX.toString());
//		return tx;
//	}
	
}
