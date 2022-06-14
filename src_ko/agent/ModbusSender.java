package src_ko.agent;

import java.io.IOException;
import java.net.Socket;

import src_ko.info.TX_Info;
import src_ko.util.PacketInputStream;
import src_ko.util.PacketOutputStream;

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
	
}
