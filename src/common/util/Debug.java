package common.util;

import java.io.IOException;
import java.io.OutputStream;

public class Debug {

    public static byte[] toByteArray(String src) {
        src = src.trim();
        
        byte[] bytes;

        if (src.matches("[a-fA-F0-9]+")) {
            bytes = new byte[src.length() / 2];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) Integer.parseInt(src.substring(i * 2, i * 2 + 2), 16);
            }
        }
        else {
            String reg = "[^a-fA-F0-9]+";
            src = src.replaceAll(reg, " ");
            src = src.trim();

            String[] strs = src.split(" ");
            bytes = new byte[strs.length];
            for (int i = 0; i < strs.length; i++) {
                bytes[i] = (byte) Integer.parseInt(strs[i], 16);
            }
        }

        return bytes;
    }

    /** 디버그용으로 쓰이는 패킷의 값을 return 한다. */
    public static String toString(byte[] packet) {
        return toString(packet, 0, packet.length);
    }

    /** 디버그용으로 쓰이는 패킷의 값을 return 한다. */
    public static String toString(byte[] packet, int offset, int len) {
        if (packet == null || packet.length - offset < 0) {
            return null;
        }

        StringBuffer buff = new StringBuffer();

        for (int i = offset; i < offset + len; i++) {

            int b = (int) (packet[i] & 0xff);
            buff.append("");
            if (b < 0x10) {
                buff.append("0");
            }
            buff.append(Integer.toString(b, 16)).append("");
        }

        return buff.toString();
    }

    public static class DubugOutStream extends OutputStream {

        public String separator = null;

        public DubugOutStream() {
            this("");
        }

        public DubugOutStream(String separator) {
            this.separator = separator;
        }

        public void write(int b) throws IOException {

            String a = Integer.toString((int) (b & 0xff), 16);
            if (a.length() == 1)
                a = "0" + a;

            if (separator.length() == 2) {
                System.out.print(separator.charAt(0) + a + separator.charAt(1));
            }
            else {
                System.out.print(a + separator);
            }
        }
    }
	
}
