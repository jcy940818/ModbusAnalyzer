package src_en.util;

import java.io.IOException;

import src_en.analyzer.TX.TX_Analyzer;
import src_en.info.TX_Info;

public class TX_Generator {
	
	/** 수집 패킷 생성 ******************************************************************************************************************/	 
	public static TX_Info generateReadRTU(int unitId, int functionCode, int startAddress, int requestCount) throws IOException {
		PacketOutputStream out = new PacketOutputStream(System.out);
		
		out.write(unitId);
		out.write(functionCode);
		out.writeShort(startAddress);
		out.writeShort(requestCount);
		out.writeCRC16(0);
		
		byte[] txInfo = out.toByteArray();		
		StringBuffer TX = new StringBuffer();
						
		for(int i = 0; i < txInfo.length; i++) {
			TX.append(String.format("%02x",txInfo[i]));
		}
		
		TX_Info tx  = new TX_Info();
		tx.setContent(TX.toString());				
		tx = new TX_Analyzer().rtuAnalysis(tx);
								
		return tx;
	}
			
	public static TX_Info generateReadTCP(int transactionId,int protocolId, int length, int unitId, int functionCode, int startAddress, int requestCount) throws IOException {
		PacketOutputStream out = new PacketOutputStream(System.out);
	
		out.writeShort(transactionId);
		out.writeShort(protocolId);
		out.writeShort(length);
		out.write(unitId);
		out.write(functionCode);
		out.writeShort(startAddress);
		out.writeShort(requestCount);		

		byte[] txInfo = out.toByteArray();
		StringBuffer TX = new StringBuffer();

		for(int i = 0; i < txInfo.length; i++) {
			TX.append(String.format("%02x",txInfo[i]));
		}
								
		TX_Info tx = new TX_Info();
		tx.setContent(TX.toString());		
		tx = new TX_Analyzer().tcpAnalysis(tx);
			
		return tx;
	}
	
	
	/** 제어 패킷 생성 ******************************************************************************************************************/
	public static TX_Info generateWriteRTU(int unitId, int functionCode, int startAddress, int controlValue) throws IOException {
		PacketOutputStream out = new PacketOutputStream(System.out);
		
		out.write(unitId);
		out.write(functionCode);
		out.writeShort(startAddress);
		out.writeShort(controlValue);
		out.writeCRC16(0);
		
		byte[] txInfo = out.toByteArray();		
		StringBuffer TX = new StringBuffer();
						
		for(int i = 0; i < txInfo.length; i++) {
			TX.append(String.format("%02x",txInfo[i]));
		}
		
		TX_Info tx  = new TX_Info();
		tx.setContent(TX.toString());				
		tx = new TX_Analyzer().rtuAnalysis(tx);
								
		return tx;
	}
	
	
	public static TX_Info generateWriteTCP(int transactionId,int protocolId, int length, int unitId, int functionCode, int startAddress, int controlValue) throws IOException {
		PacketOutputStream out = new PacketOutputStream(System.out);
	
		out.writeShort(transactionId);
		out.writeShort(protocolId);
		out.writeShort(length);
		out.write(unitId);
		out.write(functionCode);
		out.writeShort(startAddress);
		out.writeShort(controlValue);

		byte[] txInfo = out.toByteArray();
		StringBuffer TX = new StringBuffer();

		for(int i = 0; i < txInfo.length; i++) {
			TX.append(String.format("%02x",txInfo[i]));
		}
								
		TX_Info tx = new TX_Info();
		tx.setContent(TX.toString());		
		tx = new TX_Analyzer().tcpAnalysis(tx);
			
		return tx;
	}
	
	
}
