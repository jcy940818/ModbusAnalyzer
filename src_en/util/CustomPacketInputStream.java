package src_en.util;

import java.io.IOException;

/**
 * @author Moon
 * 
 * 사용자 지정 PacketInputStream
 * 데이터 타입, 부호 사용 여부, 바이트 오더를 직접 지정 할 수 있다
 */
public class CustomPacketInputStream{
	
	// Byte Order 상수
	public static final String A_B_C_D = "ABCD";
	public static final String D_C_B_A = "DCBA";
	public static final String B_A_D_C = "BADC";
	public static final String C_D_A_B = "CDAB";
	
	private PacketInputStream in;
	
	public CustomPacketInputStream(PacketInputStream in) {
		this.in = in;
	}

	public double processData(String dataType) throws IOException {
		
		String byteOrder = null;
		
		if(dataType.contains("-")) {
			byteOrder = dataType.split("-")[1].trim();
			dataType = dataType.split("-")[0].trim();
		}
		
		switch (dataType) {
			case "S" :
				return this.read_2Byte_Short(true);
			case "US" :
				return this.read_2Byte_Short(false);
			case "I" :
				return this.read_4Byte_Int(true, byteOrder);
			case "UI" :
				return this.read_4Byte_Int(false, byteOrder);
			case "F" :
				return this.read_4Byte_Float(byteOrder);
			case "L" :
			case "UL" :
				return this.read_8Byte_Int(byteOrder);
			case "D" :
				return this.read_8Byte_Double(byteOrder);
			default:
				return this.read_2Byte_Short(true);
		}
	}
	
	// 1Byte Data Type **********************************************
	/**
	 * @return 1Byte 정수
	 * @throws IOException
	 */
	public int read_1Byte(boolean isSigned) throws IOException {
		return (isSigned) ? in.readByte() : in.readUnsignedByte();
	}
	
	
	/**
	 * @return 1Byte 문자
	 * @throws IOException
	 */
	public char read_1Byte_ASCII() throws IOException {
		return (char)in.readUnsignedByte();
	}
	
	
	/**
	 * @return length 길이의 문자형 배열
	 * @throws IOException
	 */
	public char[] read_1Byte_ASCII(int length) throws IOException {
		char[] asciiArray = new char[length];
		
		for(int i = 0; i < asciiArray.length; i++) {
			asciiArray[i] = this.read_1Byte_ASCII();
		}
		
		return asciiArray;		
	}
	
	
	// 2Byte Data Type **********************************************
	/**
	 * @return 2Byte 정수
	 * @throws IOException
	 */
	public int read_2Byte_Short(boolean isSigned) throws IOException{		
		return (isSigned) ? in.readShort() : in.readUnsignedShort();
	}
	
	
	// 4Byte Data Type **********************************************
	/**
	 * @return 4Byte 정수 
	 * @throws IOException
	 */
	public long read_4Byte_Int(boolean isSigned, String byteOrder) throws IOException{		
		byteOrder = byteOrder.toUpperCase();
		long value = 0L;
		
		switch(byteOrder) {
			case A_B_C_D :
				value = this.ABCD_4Byte(in);
				return (isSigned) ? value : value & 0xffffffffL;
						
			case D_C_B_A :
				value = this.DCBA_4Byte(in);
				return (isSigned) ? value : value & 0xffffffffL;
				
			case B_A_D_C :
				value = this.BADC_4Byte(in);
				return (isSigned) ? value : value & 0xffffffffL;
				
			case C_D_A_B :
				value = this.CDAB_4Byte(in);
				return (isSigned) ? value : value & 0xffffffffL;
				
			default :
				// Default Byte Order : Big-Endian (A B C D)
				value = this.ABCD_4Byte(in);
				return (isSigned) ? value : value & 0xffffffffL;
		}
	}
	
	
	/**
	 * @return 4Byte 부동 소수점 실수
	 * @throws IOException
	 */
	public float read_4Byte_Float(String byteOrder) throws IOException{
		byteOrder = byteOrder.toUpperCase();
		
		switch(byteOrder) {
			case A_B_C_D :
				return Float.intBitsToFloat((int) (this.ABCD_4Byte(in) & 0xffffffffffffffffL));
						
			case D_C_B_A :
				return Float.intBitsToFloat((int) (this.DCBA_4Byte(in) & 0xffffffffffffffffL));
				
			case B_A_D_C :
				return Float.intBitsToFloat((int) (this.BADC_4Byte(in) & 0xffffffffffffffffL));
				
			case C_D_A_B :
				return Float.intBitsToFloat((int) (this.CDAB_4Byte(in) & 0xffffffffffffffffL));
				
			default :
				// Default Byte Order : Big-Endian (A B C D)
				return Float.intBitsToFloat((int) (this.ABCD_4Byte(in) & 0xffffffffffffffffL));
			}
	}
	
	
	// 8Byte Data Type **********************************************
	/**
	 * @return 8Byte 부호가 있는 정수 
	 * @throws IOException
	 */
	public long read_8Byte_Int(String byteOrder) throws IOException{		
		byteOrder = byteOrder.toUpperCase();			
		
		switch(byteOrder) {
			case A_B_C_D :					
				return this.ABCD_8Byte(in);
						
			case D_C_B_A :
				return this.DCBA_8Byte(in);
				
			case B_A_D_C :
				return this.BADC_8Byte(in);
				
			case C_D_A_B :
				return this.CDAB_8Byte(in);
				
			default :
				// Default Byte Order : Big-Endian (A B C D)
				return this.ABCD_8Byte(in);
		}
	}
		
		
	/**
	 * @return 8Byte 부동 소수점 실수
	 * @throws IOException
	 */
	public double read_8Byte_Double(String byteOrder) throws IOException{
		byteOrder = byteOrder.toUpperCase();
		
		switch(byteOrder) {
			case A_B_C_D :
				return Double.longBitsToDouble(ABCD_8Byte(in) & 0xffffffffffffffffL);
						
			case D_C_B_A :
				return Double.longBitsToDouble(DCBA_8Byte(in) & 0xffffffffffffffffL);
				
			case B_A_D_C :
				return Double.longBitsToDouble(BADC_8Byte(in) & 0xffffffffffffffffL);
				
			case C_D_A_B :
				return Double.longBitsToDouble(CDAB_8Byte(in) & 0xffffffffffffffffL);
				
			default :
				// Default Byte Order : Big-Endian (A B C D)
				return Double.longBitsToDouble(ABCD_8Byte(in) & 0xffffffffffffffffL);
			}
	}
	
	
	// Byte Order : 4Byte -------------------------------------------
	/**
	 * @param PacketInputStream in
	 * @return 4Byte Int : A B C D
	 * @throws IOException
	 */
	private long ABCD_4Byte(PacketInputStream in) throws IOException {
		int A = in.readByte() & 0xff;
		int B = in.readByte() & 0xff;
		int C = in.readByte() & 0xff;
		int D = in.readByte() & 0xff;
		return ((A << 24) + (B << 16) + (C << 8) + (D << 0));
	}
	
	
	/**
	 * @param PacketInputStream in
	 * @return 4Byte Int : D C B A
	 * @throws IOException
	 */
	private long DCBA_4Byte(PacketInputStream in) throws IOException {
		int A = in.readByte() & 0xff;
		int B = in.readByte() & 0xff;
		int C = in.readByte() & 0xff;
		int D = in.readByte() & 0xff;
		return ((D << 24) + (C << 16) + (B << 8) + (A << 0));
	}
	
	
	/**
	 * @param PacketInputStream in
	 * @return 4Byte Int : B A D C
	 * @throws IOException
	 */
	private long BADC_4Byte(PacketInputStream in) throws IOException {
		int A = in.readByte() & 0xff;
		int B = in.readByte() & 0xff;
		int C = in.readByte() & 0xff;
		int D = in.readByte() & 0xff;
		return ((B << 24) + (A << 16) + (D << 8) + (C << 0));
	}
	
	
	/**
	 * @param PacketInputStream in
	 * @return 4Byte Int : C D A B
	 * @throws IOException
	 */
	private long CDAB_4Byte(PacketInputStream in) throws IOException {
		int A = in.readByte() & 0xff;
		int B = in.readByte() & 0xff;
		int C = in.readByte() & 0xff;
		int D = in.readByte() & 0xff;
		return ((C << 24) + (D << 16) + (A << 8) + (B << 0));
	}

	
	// Byte Order : 8Byte -------------------------------------------
	/**
	 * @param PacketInputStream in
	 * @return 8Byte Long : A B C D
	 * @throws IOException
	 */
	private long ABCD_8Byte(PacketInputStream in) throws IOException {
		long A1 = in.readByte() & 0xff;
		long A2 = in.readByte() & 0xff;
		
		long B1 = in.readByte() & 0xff;
		long B2 = in.readByte() & 0xff;
		
		long C1 = in.readByte() & 0xff;
		long C2 = in.readByte() & 0xff;
		
		long D1 = in.readByte() & 0xff;
		long D2 = in.readByte() & 0xff;
		
		return ((A1 << 56) + (A2 << 48) + 
				(B1 << 40) + (B2 << 32) + 
				(C1 << 24) + (C2 << 16) + 
				(D1 << 8 ) + (D2 <<  0));
	}
	
	
	/**
	 * @param PacketInputStream in
	 * @return 8Byte Long : D C B A
	 * @throws IOException
	 */
	private long DCBA_8Byte(PacketInputStream in) throws IOException {
		long A1 = in.readByte() & 0xff;
		long A2 = in.readByte() & 0xff;
		
		long B1 = in.readByte() & 0xff;
		long B2 = in.readByte() & 0xff;
		
		long C1 = in.readByte() & 0xff;
		long C2 = in.readByte() & 0xff;
		
		long D1 = in.readByte() & 0xff;
		long D2 = in.readByte() & 0xff;
		
		return ((D2 << 56) + (D1 << 48) + 
				(C2 << 40) + (C1 << 32) + 
				(B2 << 24) + (B1 << 16) + 
				(A2 << 8 ) + (A1 <<  0));
	}
	
	
	/**
	 * @param PacketInputStream in
	 * @return 8Byte Long : B A D C
	 * @throws IOException
	 */
	private long BADC_8Byte(PacketInputStream in) throws IOException {
		long A1 = in.readByte() & 0xff;
		long A2 = in.readByte() & 0xff;
		
		long B1 = in.readByte() & 0xff;
		long B2 = in.readByte() & 0xff;
		
		long C1 = in.readByte() & 0xff;
		long C2 = in.readByte() & 0xff;
		
		long D1 = in.readByte() & 0xff;
		long D2 = in.readByte() & 0xff;
		
		return ((A2 << 56) + (A1 << 48) + 
				(B2 << 40) + (B1 << 32) + 
				(C2 << 24) + (C1 << 16) + 
				(D2 << 8 ) + (D1 <<  0));
	}
	
	
	/**
	 * @param PacketInputStream in
	 * @return 8Byte Long : C D A B
	 * @throws IOException
	 */
	private long CDAB_8Byte(PacketInputStream in) throws IOException {
		long A1 = in.readByte() & 0xff;
		long A2 = in.readByte() & 0xff;
		
		long B1 = in.readByte() & 0xff;
		long B2 = in.readByte() & 0xff;
		
		long C1 = in.readByte() & 0xff;
		long C2 = in.readByte() & 0xff;
		
		long D1 = in.readByte() & 0xff;
		long D2 = in.readByte() & 0xff;
		
		return ((D1 << 56) + (D2 << 48) + 
				(C1 << 40) + (C2 << 32) + 
				(B1 << 24) + (B2 << 16) + 
				(A1 << 8 ) + (A2 <<  0));
	}
	
	
}