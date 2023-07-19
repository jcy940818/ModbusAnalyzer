package src_en.util;

public class PacketGenerator {
	public static byte[] getOriginPacket(String packetStream) {

		byte[] origin = null;

		try {
			char[] chars = packetStream.toCharArray();

			// 0x00, 0x01 ...
			// 16������ '01' �� 1�� �ν��ϱ� ������  "/2" �� ���ش�.
			origin = new byte[chars.length / 2];
			int index = 0;

			for (int i = 0; i < origin.length; i++) {
				if (index >= chars.length)
					break;
				StringBuffer temp = new StringBuffer();
				temp.append(chars[index++]);
				temp.append(chars[index++]);
				origin[i] = (byte) Integer.parseInt(temp.toString(), 16);
			}
		} catch (Exception e) {
			e.printStackTrace();			
			System.out.println("PacketGenerator.java ���� ���� �߻�");
		}
		return origin;
	}

}
