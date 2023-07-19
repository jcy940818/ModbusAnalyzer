package src_ko.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class PacketInputStream extends DataInputStream {

    private Buffer buffer;
    private int check_sum_start;

    public PacketInputStream(byte[] buff, int offset, int length) {
        this(new Buffer(buff, offset, length, null));
    }

    public PacketInputStream(byte[] buff, InputStream in) {

        this(new Buffer(buff, 0, 0, in));
    }

    private PacketInputStream(Buffer decoder) {

        super(decoder);
        buffer = decoder;
    }

    public void close() throws IOException {

        super.close();

    }

    public void markChecksumStart(int currentCheckSumLength) {

        check_sum_start = buffer.pos - currentCheckSumLength;
        buffer.mark(check_sum_start);
    }

    public int getDataOffset() {

        return check_sum_start;
    }

    public void resetBuffer() {

        buffer.resetBuffer();
    }

    public int getCRC16() {

        int len = buffer.pos - check_sum_start;
        int crc16 = CRC16.getCRC16(buffer.buf, check_sum_start, len);
        return crc16;
    }

    
    public void skipCRC16() throws IOException {

        int crc16 = getCRC16();
        int len = buffer.pos - check_sum_start;
        int crc = readShort();
        if (crc != crc16) {
            throw new IOException("Incorrect CRC16. read : " + crc + ". expected : " + crc16);
        }
    }
 
    
    public int readCRC16() throws IOException {

        int crc16 = getCRC16();
        int len = buffer.pos - check_sum_start;
        int crc = readShort();
        if (crc != crc16) {
            throw new IOException(String.valueOf(crc));            
        }
        return crc;
    }
    

    public void skipCRC32(int offset, long value) throws IOException {
    	Checksum checksum = new CRC32();
    	checksum.update(buffer.buf, offset, buffer.buf.length-offset);
    	long crc32value = checksum.getValue();
    	
    	if(value != crc32value) {
    		throw new IOException("Invalid CRC32, expected : " + crc32value + " received : " + value);
    	}
    }

    public int getCheckSum() throws IOException {

        int sum = 0;
        for (int i = check_sum_start; i < buffer.pos; i++) {
            sum += buffer.buf[i];
        }
        return sum;
    }
    
    public int getCheckSum(int start) throws IOException {

        int sum = 0;
        for (int i = start; i < buffer.pos; i++) {
            sum += buffer.buf[i];
        }
        return sum;
    }
    
    public int getCheckSum16() throws IOException {

        int sum = 0;
        for (int i = check_sum_start; i < buffer.pos; i++) {
            sum += (buffer.buf[i] & 0xFF);
        }
        return sum;
    }
    
    public void skipCheckSum() throws IOException {

        int checksum = getCheckSum() & 0xff;
        int c = read();
        if (c != checksum) {
            throw new IOException("Incorrect CheckSum");
        }
    }
    public void skipCheckSum16() throws IOException{
    	
        int checksum = getCheckSum16() & 0xFFFF;
        int c = readShort();
        if (c != checksum)
          throw new IOException("Incorrect CheckSum");
      }
    public void skipCheckSum16_LE() throws IOException{
    	
      int checksum = getCheckSum() & 0xFFFF;
      int c = readShort_LE();
      if (c != checksum)
        throw new IOException("Incorrect CheckSum");
    }
    

    public int getModulo95CheckSum() {

        int sum = 0;
        for (int i = check_sum_start; i < buffer.pos; i++) {
            sum += buffer.buf[i] - 32;
            sum %= 95;
        }
        return sum + 32;
    }

    public void skipModulo95CheckSum() throws IOException {

        int checkSum = getModulo95CheckSum();
        int c = read();

        if (checkSum != c) {
            throw new IOException("Incorrect Modulo95 Checksum");
        }
    }

    /**
     * 아스키 코드로 읽어 변환한다.
     * zzz ????
     * eg) [33][32][33] -> 323
     * 
     * @param cntChar
     * @return
     * @throws IOException
     */
    public double readDecimalDigits(int cntChar) throws IOException {

        double v = 0;
        int sign = 1;
        int dot = 1;
        while (--cntChar >= 0) {
            int d = super.read();
            if (d >= '0' && d <= '9') {
                d -= '0';
            } else if (v == 0 && d == '-') { // 음수 처리
                sign = -1;
                continue;
            } else if (v == 0 && d == '+') { // 양수는 통과
                continue;
            } else if (dot == 1 && d == '.') { // 소수점 처리
                for (int i = 0; i < cntChar; i++) {
                    dot *= 10;
                }
                continue;
            } else {
            	// ASCII가 아닌 byte가 걸린 경우
            	// 기존에는 Exception 발생했으나, 그럴 경우 counter 전체가 안올라오게 된다.
            	while (--cntChar >= 0) super.read();
                return Double.NaN;
            }
            v = v * 10 + d;
        }
        return sign * v / dot;
    }

    /**
     * 지정한 문자가 나올때 까지 읽는다.
     * 
     * @param delim
     * @return
     * @throws IOException
     */
    public double readDecimalDigitsDelim(int delim) throws IOException {

        double v = 0;
        int sign = 1;

        int d;
        int len = 0; // 읽은 바이트 수
        int dotPos = -1; // 소수점 위치
        while ((d = super.read()) != delim) {

            if (d >= '0' && d <= '9') {
                d -= '0';
            } else if (len == 0 && d == '-') { // 음수 처리
                sign = -1;
                continue;
            } else if (len == 0 && d == '+') { // 양수는 통과
                continue;
            } else if (dotPos == -1 && d == '.') {
                dotPos = len;
                continue;
            } else {
                throw new IOException("invalid char : " + (char) d);
            }
            v = v * 10 + d;
            len++;
        }

        int dot = 1;
        if (dotPos != -1) { // 소수점 계산
            for (int i = 0; i < len - dotPos; i++) {
                dot *= 10;
            }
        }
        return sign * v / dot;
    }



    public void skipByte(int expectedByte) throws IOException {

        int c = in.read();
        if (c != expectedByte) {
            throw new IOException("unexpected data : 0x" + Integer.toHexString(c));
        }
    }

    /**
     * @return 리틀 엔디안으로 2byte의 정수(-32768 ~ 32767)를 리턴한다.
     * @throws IOException
     */
    public short readShort_LE() throws IOException {

        int ch2 = in.read();
        int ch1 = in.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (short) ((ch1 << 8) + (ch2 << 0));
    }

    /**
     * @return 리틀 엔디안으로 2byte의 양수(0 ~ 65535)를 리턴한다.
     * @throws IOException
     */
    public int readUnsignedShort_LE() throws IOException {

        int ch2 = in.read();
        int ch1 = in.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return ((ch1 << 8) + (ch2 << 0));
    }

    /**
     * @return 리틀 엔디안으로 4byte의 정수(-2147483648 ~ 2147483647)를 리턴한다.
     * @throws IOException
     */
    public int readInt_LE() throws IOException {

        int ch4 = in.read();
        int ch3 = in.read();
        int ch2 = in.read();
        int ch1 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    /**
     * @return UINT 32일 때, 자바에서 unsigned int가 없으므로 long 형으로 리턴함.
     * @throws IOException
     */
    public long readUnsignedInt() throws IOException {

        return readInt() & 0xFFFFFFFFL;
    }

    /**
     * @return 리틀엔디안 UINT 32일 때, 자바에서 unsigned int가 없으므로 long 형으로 리턴함.
     * @throws IOException
     */
    public long readUnsignedInt_LE() throws IOException {

        return readInt_LE() & 0xFFFFFFFFL;
    }

    /**
     * @return 리틀 엔디안으로 8byte의 정수(-9223372036854775808 ~ 9223372036854775807)를 리턴한다.
     * @throws IOException
     */
    public long readLong_LE() throws IOException {

        int low = readInt_LE();
        int high = readInt_LE();
        long v = ((long) high << 32) + (low & 0xFFFFFFFFL);
        return v;
    }
    
    /**
     * 8byte long ABCDEFGH -> GHEFCDAB
     * @return 
     * @throws IOException
     */
    public long readLong_Reversed() throws IOException {
		long low = readIntReversed();
		long high = readIntReversed();
		return (high << 32) + (low & 0xFFFFFFFFL);
	}

    public float readFloat_LE() throws IOException {

        return Float.intBitsToFloat(readInt_LE());
    }

    public double readDouble_LE() throws IOException {

        return Double.longBitsToDouble(readLong_LE());
    }

    // Sentinel II Numerical Format Converter
    public float readSentinelIINumbericalFormat() throws IOException {

        int b1 = read();
        int b2 = read();
        if ((b1 == 0xFF && b2 == 0xFF) || (b1 == 0x7F && b2 == 0xFF)) {
            return Float.NaN;
        }

        int[] uppers = toBinaryArray(b1);
        int[] lowers = toBinaryArray(b2);

        int sign = uppers[7];
        int bias = (uppers[6] + uppers[5] + uppers[4] + uppers[3] == 0) ? 6 : 7;
        int biasedExponent = uppers[6] * 8 + uppers[5] * 4 + uppers[4] * 2 +
            uppers[3];
        int unbiasedExponent = biasedExponent - bias;
        double finalMultiplier = Math.pow(2, unbiasedExponent);

        double fractionalMantissa = uppers[2] * 0.5 + uppers[1] * 0.25 +
            uppers[0] * 0.125 + lowers[7] * 0.0625 + lowers[6] * 0.03125 +
            lowers[5]
            * 0.015625 + lowers[4] * 0.0078125 + lowers[3] * 0.00390625 +
            lowers[2] * 0.001953125 + lowers[1] * 0.0009765625 + lowers[0]
            * 0.00048828125;

        double fullMantissa = (uppers[6] + uppers[5] + uppers[4] + uppers[3] ==
            0) ? fractionalMantissa : fractionalMantissa + 1;

        if (sign == 1) {
            return (float) (-1 * finalMultiplier * fullMantissa);
        }
        else {
            return (float) (finalMultiplier * fullMantissa);
        }
    }
    
    public int readByteBCD() throws IOException {
    	int orig = this.readUnsignedByte();
    	int ret10 = orig >> 4;
        int ret1 = orig & 15;
    	return ret10 * 10 + ret1;
    }
    
    public long readIntReversed() throws IOException {
    	int lowByte = readUnsignedShort();
    	int highByte = readUnsignedShort();
    	return (lowByte + (highByte << 16));
    }
    
    public long readUnsignedIntReversed() throws IOException {
    	int lowByte = readUnsignedShort();
    	int highByte = readUnsignedShort();
    	return (lowByte + (highByte << 16)) & 0xFFFFFFFFL;
    }
    
    public long readUnsignedLongReversed() throws IOException {
    	long short1 = readUnsignedShort();
    	long short2 = readUnsignedShort();
    	long short3 = readUnsignedShort();
    	long short4 = readUnsignedShort();
    	return (short1 + (short2 << 16) + (short3 << 32) + (short4 << 48)) & 0xFFFFFFFFFFFFFFFFL;
    }

    public double readUnsignedInt_ignoreValue(long value) throws IOException {
    	long ret = readInt() & 0xFFFFFFFFL;
        return value == ret ? Double.NaN : (double) ret ;
    }
    
    public double readInt_ignoreValue(long value) throws IOException {
    	long ret = readInt();
        return value == ret ? Double.NaN : (double) ret ;
    }
    
    public double readUnsignedShort_ignoreValue(int value) throws IOException {
    	int ret = readUnsignedShort();
        return value == ret ? Double.NaN : (double) ret ;
    }
    
    public double readShort_ignoreValue(short value) throws IOException {
    	short ret = readShort();
        return value == ret ? Double.NaN : (double) ret ;
    }
    
    public byte[] debug_getBuffer() {

        int len = buffer.count - check_sum_start;
        byte[] newBuff = new byte[len];
        System.arraycopy(buffer.buf, check_sum_start, newBuff, 0, len);

        return newBuff;
    }

    private static int[] toBinaryArray(int a) {

        int[] buf = new int[8];
        int radix = 2;
        int mask = radix - 1;

        for (int i = 0; i < buf.length; i++) {
            buf[i] = a & mask;
            a >>>= 1;
        }

        return buf;
    }
    
    public void clear() throws IOException{
    	while(this.available() > 0) this.read();
    }
    
    public int getBufferLength() {
		return buffer.count - check_sum_start;
	}

    static class Buffer extends InputStream {

        private InputStream in;
        protected byte buf[];
        protected int pos;
        protected int mark = 0;
        protected int count;
        BufferedInputStream ii;

        public Buffer(byte[] buf, int offset, int length, InputStream in) {

            this.buf = buf;
            pos = offset;
            count = Math.min(offset + length, buf.length);
            mark = offset;
            this.in = in;
        }

        private void fill() throws IOException {

            if (pos >= count) {
                int len = buf.length - count;
                if (in == null) {
                    throw new EOFException("in == null" + buf.length + " " + pos + " " + mark + " " + count);
                }
                if (len == 0) {
                    throw new EOFException("len == 0. pos : " + pos + " count : " + count);
                }
                if ((len = in.read(buf, count, len)) <= 0) {
                    throw new EOFException("len : " + len);
                }
                count += len;
            }
        }

        public synchronized int read() throws IOException {

            fill();
            return buf[pos++] & 0xFF;
        }

        public synchronized int read(byte b[], int off, int len) throws IOException {

            if (b == null) {
                throw new NullPointerException();
            } else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            }
            fill();
            if (pos + len > count) {
                len = count - pos;
            }
            if (len <= 0) {
                return 0;
            }
            System.arraycopy(buf, pos, b, off, len);
            pos += len;
            return len;
        }

        public synchronized long skip(long n) {

            if (pos + n > count) {
                n = count - pos;
            }
            if (n < 0) {
                return 0;
            }
            pos += n;
            return n;
        }

        public synchronized final int available() {

            return count - pos;
        }

        public boolean markSupported() {

            return true;
        }

        public void mark(int readAheadLimit) {

            mark = pos;
        }

        public synchronized void reset() throws IOException {

            if (mark < 0) {
                throw new IOException("Resetting to invalid mark");
            }
            pos = mark;
        }

        public void resetBuffer() {

            mark = -1;
            count -= pos;
            if (count > 0) {
                System.arraycopy(buf, pos, buf, 0, count);
            }
            pos = 0;
        }

        public void close() throws IOException {

            if (in != null) {
                in.close();
                in = null;
            }
            mark = -1;
            count = 0;
        }
    }
}
