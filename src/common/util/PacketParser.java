package common.util;

public class PacketParser {
	
	public static String getParsedPacket(boolean containsLength, String tcpDump, int length) {	
		try {
			String[] tokens = tcpDump.split(" ");
			boolean isLengthField = false;
			
			StringBuilder packet = new StringBuilder();
			
			for(String token : tokens) {
				try {
					token = token.trim();
					
					if(!token.replace(" ", "").isEmpty()) {
						if(containsLength && token.toLowerCase().contains("length")) {
							isLengthField = true;
							continue;
							
						}else if(isLengthField) {
							length = Integer.parseInt(token);
							isLengthField = false;
							
						}else {
							Integer.parseInt(token, 16);
							packet.append(token);
							isLengthField = false;
						}
					}else {
						continue;
					}
					
				}catch(Exception e) {
					continue;
				}
			}
						
			packet = new StringBuilder(packet.reverse().substring(0, length * 2)).reverse();
			return packet.toString();
			
		}catch(Exception e) {
			e.printStackTrace();
			return "ERROR";
		}
	}
	
	public static int getParsedPacketLength(String tcpDump) {	
		try {
			String[] tokens = tcpDump.split(" ");
			int length = -1;
			boolean isLengthField = false;
			
			for(String token : tokens) {
				try {
					token = token.trim();
					
					if(!token.replace(" ", "").isEmpty()) {
						if(token.toLowerCase().contains("length")) {
							isLengthField = true;
							continue;
							
						}else if(isLengthField) {
							length = Integer.parseInt(token);							
							isLengthField = false;
						}else {
							isLengthField = false;
							continue;
						}
					}else {
						continue;
					}
					
				}catch(Exception e) {
					continue;
				}
			}
			
			return length;
			
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}
