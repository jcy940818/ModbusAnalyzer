package src_en.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class PacketOutputStream extends DataOutputStream {

    private Buffer buffer;
  
    private OutputStream out;

    public PacketOutputStream() {
        this(new Buffer());
    }

    public PacketOutputStream(OutputStream out) {
        this(new Buffer());
        this.out = out;
    }

    private PacketOutputStream(Buffer buffer) {
        super(buffer);
        this.buffer = buffer;
    }


    public void flush() throws IOException {
        if (buffer.size() > 0) {
        	if(out!=null)
            out.write(buffer.getBuffer(), 0, buffer.size());
            buffer.reset();
        }
    }

    public int getCheckSum(int start) {
        int sum = 0;
        byte[] buf = buffer.getBuffer();
        int count = buffer.size();
        for (int i = start; i < count; i ++) {
            sum += buf[i];
        }
        return sum;
    }
    public int getCheckSum16(int start) {
        int sum = 0;
        byte[] buf = buffer.getBuffer();
        int count = buffer.size();
        for (int i = start; i < count; i ++) {
            sum += (buf[i] & 0xFF);
        }
        return sum;
    }

    public void writeCRC16(int start) throws IOException {
        int crc16 = CRC16.getCRC16(buffer.getBuffer(), start, buffer.size());
        writeShort(crc16);
    }
    
    public int getCRC16(int start) throws IOException {
    	// CRC16 을 버퍼에 쓰고, CRC 내용 리턴
        int crc16 = CRC16.getCRC16(buffer.getBuffer(), start, buffer.size());
        writeShort(crc16);
        return crc16;
    }
    

    public void writeCRC16_LE(int start) throws IOException {
        int crc16 = CRC16.getCRC16(buffer.getBuffer(), start, buffer.size());
        writeShort_LE(crc16);
    }

    public void writeDecimalDigits(int value, int cntChar) throws IOException {
        int m = 1;
        while (cntChar-- > 1) {
            m *= 10;
        }
        while (m > 0) {
            int d = (value / m) % 10;
            write(d + '0');
            m /= 10;
        }
    }
    
    public void writeDecimalDigits_LE(int value, int cntChar) throws IOException {
        int m = 1;
        
        while (m > 0 && cntChar-- > 0 ) {
            int d = (value / m) % 10;
            write(d + '0');
            m *= 10;
        }
    }    

    public void writeHexaDigits(int value, int cntChar) throws IOException {
        cntChar --;
        for (cntChar *= 4; cntChar >= 0; cntChar -= 4) {
            int d = (value >>> cntChar) & 0xF;
            if (d >= 10) {
                d += 'A' - 10;
            }
            else {
                d += '0';
            }
            write(d);
        }
    }
    
    public void writeHexaDigitsLowerCase(int value, int cntChar) throws IOException {
        cntChar --;
        for (cntChar *= 4; cntChar >= 0; cntChar -= 4) {
            int d = (value >>> cntChar) & 0xF;
            if (d >= 10) {
                d += 'a' - 10;
            }
            else {
                d += '0';
            }
            write(d);
        }
    }
    
   
    public void writeShort_LE(int v) throws IOException {
        write((v >>> 0) & 0xFF);
        write((v >>> 8) & 0xFF);
    }

    public void writeInt_LE(int v) throws IOException {
        write((v >>> 0) & 0xFF);
        write((v >>> 8) & 0xFF);
        write((v >>> 16) & 0xFF);
        write((v >>> 24) & 0xFF);
    }

    public void writeLong_LE(long v) throws IOException {
        this.writeInt_LE((int)v);
        this.writeInt_LE((int)(v >> 32));
    }


    public void writeFloat_LE(float v) throws IOException {
        writeInt_LE(Float.floatToIntBits(v));
    }


    public void writeDouble_LE(double v) throws IOException {
        writeLong_LE(Double.doubleToLongBits(v));
    }
    
    public void writeBCDByte(int i) throws IOException {
        if(i > 99) throw new IOException("Can write 2 digits or less.");
        int digit1 = i / 10;
        int digit0 = i % 10;
        write(digit1 * 16 + digit0);
    }
    
    /**
     * 421 => 0x1A5 => 0x30 0x31 0x41 0x35 와 같은 형식으로 쓴다.
     * @param v 쓸 값
     * @param digit 자릿수
     * @throws IOException
     */
    public void writeHexString(int v, int length) throws IOException{
    	String hexString = Integer.toHexString(v);
    	if(hexString.length() > length) throw new IOException("too long hex string");
		for (int i = length - 1; i < -1; i--) {
			try {
				char digit = hexString.charAt(i);
				write(digit);
			} catch (StringIndexOutOfBoundsException e) {
				write(0x30);
			}
		}
    }
    
    static class Buffer  extends ByteArrayOutputStream {
        final byte[] getBuffer() {
            return super.buf;
        }

        public void reset() {
            super.count = 0;
        }
    }
    
    // 버퍼 내용을 byte array로 리턴
    public byte[] toByteArray() {
    	return this.buffer.toByteArray();
    }
}
