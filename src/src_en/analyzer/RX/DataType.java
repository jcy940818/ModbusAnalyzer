package src_en.analyzer.RX;

import java.util.HashMap;

import src_en.info.RX_Info;

public class DataType {	
	
	public static HashMap<String, String> typeMap = new HashMap(); 
	public static HashMap<String, String> customTypeMap = new HashMap();
	
	public static void initTypeMap() {
		// Key : ModbusAanlyzer Modbus DataType
		// Value : MK119 Modbus DataType
		DataType.typeMap.put("BINARY", "BINARY");
		DataType.typeMap.put("TWO BYTE INT SIGNED", "TWO BYTE INT SIGNED");
		DataType.typeMap.put("TWO BYTE INT UNSIGNED", "TWO BYTE INT UNSIGNED");
		DataType.typeMap.put("FOUR BYTE INT SIGNED (A B C D)", "FOUR BYTE INT SIGNED");
		DataType.typeMap.put("FOUR BYTE INT SIGNED (C D A B)", "FOUR BYTE INT SIGNED SWAPPED");
		DataType.typeMap.put("FOUR BYTE INT UNSIGNED (A B C D)", "FOUR BYTE INT UNSIGNED");
		DataType.typeMap.put("FOUR BYTE INT UNSIGNED (C D A B)", "FOUR BYTE INT UNSIGNED SWAPPED");
		DataType.typeMap.put("FOUR BYTE FLOAT (A B C D)", "FOUR BYTE FLOAT");
		DataType.typeMap.put("FOUR BYTE FLOAT (C D A B)", "FOUR BYTE FLOAT SWAPPED");
		DataType.typeMap.put("EIGHT BYTE INT SIGNED (A B C D)", "EIGHT BYTE INT SIGNED");
		DataType.typeMap.put("EIGHT BYTE DOUBLE (A B C D)", "EIGHT BYTE FLOAT");
	}
	
	public static void initCustomTypeMap() {
		// Key : ModbusAanlyzer Modbus DataType
		// Value : Custom Modbus DataType
		
		DataType.customTypeMap.put("BINARY","B");

		DataType.customTypeMap.put("TWO BYTE INT SIGNED","S");
		DataType.customTypeMap.put("TWO BYTE INT UNSIGNED","US");

		DataType.customTypeMap.put("FOUR BYTE INT SIGNED (A B C D)","I-ABCD");
		DataType.customTypeMap.put("FOUR BYTE INT SIGNED (D C B A)","I-DCBA");
		DataType.customTypeMap.put("FOUR BYTE INT SIGNED (B A D C)","I-BADC");
		DataType.customTypeMap.put("FOUR BYTE INT SIGNED (C D A B)","I-CDAB");

		DataType.customTypeMap.put("FOUR BYTE INT UNSIGNED (A B C D)","UI-ABCD");
		DataType.customTypeMap.put("FOUR BYTE INT UNSIGNED (D C B A)","UI-DCBA");
		DataType.customTypeMap.put("FOUR BYTE INT UNSIGNED (B A D C)","UI-BADC");
		DataType.customTypeMap.put("FOUR BYTE INT UNSIGNED (C D A B)","UI-CDAB");

		DataType.customTypeMap.put("FOUR BYTE FLOAT (A B C D)","F-ABCD");
		DataType.customTypeMap.put("FOUR BYTE FLOAT (D C B A)","F-DCBA");
		DataType.customTypeMap.put("FOUR BYTE FLOAT (B A D C)","F-BADC");
		DataType.customTypeMap.put("FOUR BYTE FLOAT (C D A B)","F-CDAB");

		DataType.customTypeMap.put("EIGHT BYTE INT SIGNED (A B C D)","L-ABCD");
		DataType.customTypeMap.put("EIGHT BYTE INT SIGNED (D C B A)","L-DCBA");
		DataType.customTypeMap.put("EIGHT BYTE INT SIGNED (B A D C)","L-BADC");
		DataType.customTypeMap.put("EIGHT BYTE INT SIGNED (C D A B)","L-CDAB");

		DataType.customTypeMap.put("EIGHT BYTE INT UNSIGNED (A B C D)","UL-ABCD");
		DataType.customTypeMap.put("EIGHT BYTE INT UNSIGNED (D C B A)","UL-DCBA");
		DataType.customTypeMap.put("EIGHT BYTE INT UNSIGNED (B A D C)","UL-BADC");
		DataType.customTypeMap.put("EIGHT BYTE INT UNSIGNED (C D A B)","UL-CDAB");

		DataType.customTypeMap.put("EIGHT BYTE DOUBLE (A B C D)","D-ABCD");
		DataType.customTypeMap.put("EIGHT BYTE DOUBLE (D C B A)","D-DCBA");
		DataType.customTypeMap.put("EIGHT BYTE DOUBLE (B A D C)","D-BADC");
		DataType.customTypeMap.put("EIGHT BYTE DOUBLE (C D A B)","D-CDAB");
	}
	
	public static String[] dataTypes = {
			"ASCII CODE",
			"UNI CODE",

			"BINARY",
			"HEX",

			"TWO BYTE INT SIGNED",
			"TWO BYTE INT UNSIGNED",

			"FOUR BYTE INT SIGNED (A B C D)",
			"FOUR BYTE INT SIGNED (D C B A)",
			"FOUR BYTE INT SIGNED (B A D C)",
			"FOUR BYTE INT SIGNED (C D A B)",

			"FOUR BYTE INT UNSIGNED (A B C D)",
			"FOUR BYTE INT UNSIGNED (D C B A)",
			"FOUR BYTE INT UNSIGNED (B A D C)",
			"FOUR BYTE INT UNSIGNED (C D A B)",

			"FOUR BYTE FLOAT (A B C D)",
			"FOUR BYTE FLOAT (D C B A)",
			"FOUR BYTE FLOAT (B A D C)",
			"FOUR BYTE FLOAT (C D A B)",

			"EIGHT BYTE INT SIGNED (A B C D)",
			"EIGHT BYTE INT SIGNED (D C B A)",
			"EIGHT BYTE INT SIGNED (B A D C)",
			"EIGHT BYTE INT SIGNED (C D A B)",

			"EIGHT BYTE INT UNSIGNED (A B C D)",
			"EIGHT BYTE INT UNSIGNED (D C B A)",
			"EIGHT BYTE INT UNSIGNED (B A D C)",
			"EIGHT BYTE INT UNSIGNED (C D A B)",

			"EIGHT BYTE DOUBLE (A B C D)",
			"EIGHT BYTE DOUBLE (D C B A)",
			"EIGHT BYTE DOUBLE (B A D C)",
			"EIGHT BYTE DOUBLE (C D A B)"
	};
	
	public static String[] mk119dataTypes = {
			"BINARY",
			
			"TWO BYTE INT SIGNED",
			"TWO BYTE INT UNSIGNED",
			
			"FOUR BYTE INT SIGNED",
			"FOUR BYTE INT SWAPPED",
			"FOUR BYTE INT UNSIGNED",
			"FOUR BYTE INT UNSIGNED SWAPPED",
			
			"FOUR BYTE FLOAT",
			"FOUR BYTE FLOAT SWAPPED",
			
			"EIGHT BYTE INT SIGNED",
			"EIGHT BYTE FLOAT"
	};
		
	
	// Data Size : 4 Byte ------------------------------------------------
	/**	 
	 * @return FOUR BYTE INT (ABCD)
	 */
	public static long ABCD_4BYTE(String high, String low) {		
        int A = Integer.parseInt(high.substring(0, 2), 16) & 0xff;
        int B = Integer.parseInt(high.substring(2, 4), 16) & 0xff;
        int C = Integer.parseInt(low.substring(0, 2), 16) & 0xff;
        int D = Integer.parseInt(low.substring(2, 4), 16) & 0xff;
        return ((A << 24) + (B << 16) + (C << 8) + (D << 0));
    }
	
	/**	 
	 * @return FOUR BYTE INT (DCBA)
	 */
	public static long DCBA_4BYTE(String high, String low) {		
        int A = Integer.parseInt(high.substring(0, 2), 16) & 0xff;
        int B = Integer.parseInt(high.substring(2, 4), 16) & 0xff;
        int C = Integer.parseInt(low.substring(0, 2), 16) & 0xff;
        int D = Integer.parseInt(low.substring(2, 4), 16) & 0xff;
        return ((D << 24) + (C << 16) + (B << 8) + (A << 0));
    }
	
	/**	 
	 * @return FOUR BYTE INT (BADC)
	 */
	public static long BADC_4BYTE(String high, String low) {		
        int A = Integer.parseInt(high.substring(0, 2), 16) & 0xff;
        int B = Integer.parseInt(high.substring(2, 4), 16) & 0xff;
        int C = Integer.parseInt(low.substring(0, 2), 16) & 0xff;
        int D = Integer.parseInt(low.substring(2, 4), 16) & 0xff;
        return ((B << 24) + (A << 16) + (D << 8) + (C << 0));
    }
	
	/**	 
	 * @return FOUR BYTE INT (CDAB)
	 */
	public static long CDAB_4BYTE(String high, String low) {		
        int A = Integer.parseInt(high.substring(0, 2), 16) & 0xff;
        int B = Integer.parseInt(high.substring(2, 4), 16) & 0xff;
        int C = Integer.parseInt(low.substring(0, 2), 16) & 0xff;
        int D = Integer.parseInt(low.substring(2, 4), 16) & 0xff;
        return ((C << 24) + (D << 16) + (A << 8) + (B << 0));
    }
	
	
	// Data Size : 8 Byte ------------------------------------------------
	/**
	 * @return EIGHT BYTE INT (ABCD)
	 */
	public static long ABCD_8BYTE(String A, String B, String C, String D) {
		long A1 = Integer.parseInt(A.substring(0, 2), 16) & 0xff;
		long A2 = Integer.parseInt(A.substring(2, 4), 16) & 0xff;
		
		long B1 = Integer.parseInt(B.substring(0, 2), 16) & 0xff;
		long B2 = Integer.parseInt(B.substring(2, 4), 16) & 0xff;
		
		long C1 = Integer.parseInt(C.substring(0, 2), 16) & 0xff;
		long C2 = Integer.parseInt(C.substring(2, 4), 16) & 0xff;
		
		long D1 = Integer.parseInt(D.substring(0, 2), 16) & 0xff;
		long D2 = Integer.parseInt(D.substring(2, 4), 16) & 0xff;
		
		return ((A1 << 56) + (A2 << 48) + 
				(B1 << 40) + (B2 << 32) + 
				(C1 << 24) + (C2 << 16) + 
				(D1 << 8 ) + (D2 <<  0));
	}
	
	/**
	 * @return EIGHT BYTE INT (DCBA)
	 */
	public static long DCBA_8BYTE(String A, String B, String C, String D) {
		long A1 = Integer.parseInt(A.substring(0, 2), 16) & 0xff;
		long A2 = Integer.parseInt(A.substring(2, 4), 16) & 0xff;
		
		long B1 = Integer.parseInt(B.substring(0, 2), 16) & 0xff;
		long B2 = Integer.parseInt(B.substring(2, 4), 16) & 0xff;
		
		long C1 = Integer.parseInt(C.substring(0, 2), 16) & 0xff;
		long C2 = Integer.parseInt(C.substring(2, 4), 16) & 0xff;
		
		long D1 = Integer.parseInt(D.substring(0, 2), 16) & 0xff;
		long D2 = Integer.parseInt(D.substring(2, 4), 16) & 0xff;
				
		return ((D2 << 56) + (D1 << 48) + 
				(C2 << 40) + (C1 << 32) + 
				(B2 << 24) + (B1 << 16) + 
				(A2 << 8 ) + (A1 <<  0));
	}
	
	/**
	 * @return EIGHT BYTE INT (BADC)
	 */
	public static long BADC_8BYTE(String A, String B, String C, String D) {
		long A1 = Integer.parseInt(A.substring(0, 2), 16) & 0xff;
		long A2 = Integer.parseInt(A.substring(2, 4), 16) & 0xff;
		
		long B1 = Integer.parseInt(B.substring(0, 2), 16) & 0xff;
		long B2 = Integer.parseInt(B.substring(2, 4), 16) & 0xff;
		
		long C1 = Integer.parseInt(C.substring(0, 2), 16) & 0xff;
		long C2 = Integer.parseInt(C.substring(2, 4), 16) & 0xff;
		
		long D1 = Integer.parseInt(D.substring(0, 2), 16) & 0xff;
		long D2 = Integer.parseInt(D.substring(2, 4), 16) & 0xff;
				
		return ((A2 << 56) + (A1 << 48) + 
				(B2 << 40) + (B1 << 32) + 
				(C2 << 24) + (C1 << 16) + 
				(D2 << 8 ) + (D1 <<  0));
	}
	
	/**
	 * @return EIGHT BYTE INT (CDAB)
	 */
	public static long CDAB_8BYTE(String A, String B, String C, String D) {
		long A1 = Integer.parseInt(A.substring(0, 2), 16) & 0xff;
		long A2 = Integer.parseInt(A.substring(2, 4), 16) & 0xff;
		
		long B1 = Integer.parseInt(B.substring(0, 2), 16) & 0xff;
		long B2 = Integer.parseInt(B.substring(2, 4), 16) & 0xff;
		
		long C1 = Integer.parseInt(C.substring(0, 2), 16) & 0xff;
		long C2 = Integer.parseInt(C.substring(2, 4), 16) & 0xff;
		
		long D1 = Integer.parseInt(D.substring(0, 2), 16) & 0xff;
		long D2 = Integer.parseInt(D.substring(2, 4), 16) & 0xff;
		
		return ((D1 << 56) + (D2 << 48) + 
				(C1 << 40) + (C2 << 32) + 
				(B1 << 24) + (B2 << 16) + 
				(A1 << 8 ) + (A2 <<  0));
	}
	
	
	
	// STATUS
	public static Object[] init_STATUS(RX_Info rx) {
		Object[] value = new Object[rx.getPerfInfo().length];
		
			for (int i = 0; i < rx.getPerfInfo().length; i++) {
			value[i] = rx.getPerfInfo()[i].getBitStatus();
		}
		return value;
	}
	
	// BINARY
	public static Object[] init_BINARY(RX_Info rx) {
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			value[i] = rx.getPerfInfo()[i].getBinaryValue();
		}
		return value;
	}
		
	// HEX
	public static Object[] init_HEX(RX_Info rx){
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			value[i] = String.format("0x%04X", rx.getPerfInfo()[i].getIntHexValue());
		}
		return value;
	}
	
	
	// ASCII CODE
	public static Object[] init_ASCII_CODE(RX_Info rx) {
		Object[] value = new Object[rx.getPerfInfo().length];
		
		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			String ASCII = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());
			
			byte A = (byte) (Integer.parseInt(ASCII.substring(0, 2), 16) & 0xff);
			byte B = (byte) (Integer.parseInt(ASCII.substring(2, 4), 16) & 0xff);
			
			value[i] = String.format("%c%c", (char)A, (char)B);					
		}
		
		return value;		
	}
	
	
	// UNI CODE
	public static Object[] init_UNI_CODE(RX_Info rx) {
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {			
			value[i] = (char)rx.getPerfInfo()[i].getIntHexValue();
		}

		return value;
	}
	
	
	// TWO BYTE INT SIGNED
	public static Object[] init_TWO_BYTE_INT_SIGNED(RX_Info rx){
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			value[i] = String.format("%d", rx.getPerfInfo()[i].getIntValue());
		}
		return value;
	}
	
	// TWO BYTE INT UNSIGNED
	public static Object[] init_TWO_BYTE_INT_UNSIGNED(RX_Info rx){
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			value[i] = String.format("%d", rx.getPerfInfo()[i].getIntValue() & 0xffff );
		}
		return value;
	}
	
	
	
	// FOUR BYTE INT SIGNED (big endian) : A B C D
	public static Object[] init_FOUR_BYTE_INT_SIGNED_ABCD(RX_Info rx){		
			Object[] value = new Object[rx.getPerfInfo().length];

			for (int i = 0; i < rx.getPerfInfo().length; i++) {
				if (i % 2 == 0) {
					String high = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());
					String low = null;
					
					if((i + 1) == rx.getPerfInfo().length) {
						low = "0000";
					}else {
						low = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					}					
					value[i] = ABCD_4BYTE(high, low);					
				} else {
					value[i] = "---";
				}
			}
			return value;
	}
		
	// FOUR BYTE INT SIGNED (little endian) : D C B A
	public static Object[] init_FOUR_BYTE_INT_SIGNED_DCBA(RX_Info rx){
			Object[] value = new Object[rx.getPerfInfo().length];
	
			for (int i = 0; i < rx.getPerfInfo().length; i++) {
				if (i % 2 == 0) {
					String high = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());
					String low = null;
					
					if((i + 1) == rx.getPerfInfo().length) {
						low = "0000";
					}else {
						low = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					}					
					value[i] = DCBA_4BYTE(high, low);					
				} else {
					value[i] = "---";
				}
			}
			return value;
	}

	// FOUR BYTE INT SIGNED (big endian byte swap) : B A D C
	public static Object[] init_FOUR_BYTE_INT_SIGNED_BADC(RX_Info rx){
			Object[] value = new Object[rx.getPerfInfo().length];
	
			for (int i = 0; i < rx.getPerfInfo().length; i++) {
				if (i % 2 == 0) {
					String high = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());
					String low = null;
					
					if((i + 1) == rx.getPerfInfo().length) {
						low = "0000";
					}else {
						low = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					}					
					value[i] = BADC_4BYTE(high, low);					
				} else {
					value[i] = "---";
				}
			}
			return value;		
	}
	
	// FOUR BYTE INT SIGNED (little endian byte swap) : C D A B
	public static Object[] init_FOUR_BYTE_INT_SIGNED_CDAB(RX_Info rx){
			Object[] value = new Object[rx.getPerfInfo().length];
			
			for (int i = 0; i < rx.getPerfInfo().length; i++) {
				if (i % 2 == 0) {
					String high = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());
					String low = null;
					
					if((i + 1) == rx.getPerfInfo().length) {
						low = "0000";
					}else {
						low = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					}					
					value[i] = CDAB_4BYTE(high, low);					
				} else {
					value[i] = "---";
				}
			}
			return value;				
	}
	
	
	
	// FOUR BYTE INT UNSIGNED (big endian) : A B C D
	public static Object[] init_FOUR_BYTE_INT_UNSIGNED_ABCD(RX_Info rx){
			Object[] value = new Object[rx.getPerfInfo().length];
			
			for (int i = 0; i < rx.getPerfInfo().length; i++) {
				if (i % 2 == 0) {
					String high = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());
					String low = null;
					
					if((i + 1) == rx.getPerfInfo().length) {
						low = "0000";
					}else {
						low = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					}					
					value[i] = ABCD_4BYTE(high, low) & 0xffffffffL;
				} else {
					value[i] = "---";
				}
			}
			return value;				
	}

	// FOUR BYTE INT UNSIGNED (little endian) : D C B A
	public static Object[] init_FOUR_BYTE_INT_UNSIGNED_DCBA(RX_Info rx){
			Object[] value = new Object[rx.getPerfInfo().length];
			
			for (int i = 0; i < rx.getPerfInfo().length; i++) {
				if (i % 2 == 0) {
					String high = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());
					String low = null;
					
					if((i + 1) == rx.getPerfInfo().length) {
						low = "0000";
					}else {
						low = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					}					
					value[i] = DCBA_4BYTE(high, low) & 0xffffffffL;
				} else {
					value[i] = "---";
				}
			}
			return value;						
	}

	// FOUR BYTE INT UNSIGNED (big endian byte swap) : B A D C
	public static Object[] init_FOUR_BYTE_INT_UNSIGNED_BADC(RX_Info rx){
			Object[] value = new Object[rx.getPerfInfo().length];
			
			for (int i = 0; i < rx.getPerfInfo().length; i++) {
				if (i % 2 == 0) {
					String high = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());
					String low = null;
					
					if((i + 1) == rx.getPerfInfo().length) {
						low = "0000";
					}else {
						low = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					}					
					value[i] = BADC_4BYTE(high, low) & 0xffffffffL;
				} else {
					value[i] = "---";
				}
			}
			return value;						
	}
	
	// FOUR BYTE INT UNSIGNED (little endian byte swap) : C D A B
	public static Object[] init_FOUR_BYTE_INT_UNSIGNED_CDAB(RX_Info rx){
			Object[] value = new Object[rx.getPerfInfo().length];
			
			for (int i = 0; i < rx.getPerfInfo().length; i++) {
				if (i % 2 == 0) {
					String high = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());
					String low = null;
					
					if((i + 1) == rx.getPerfInfo().length) {
						low = "0000";
					}else {
						low = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					}					
					value[i] = CDAB_4BYTE(high, low) & 0xffffffffL;
				} else {
					value[i] = "---";
				}
			}
			return value;				
	}
	
	
	
	// FOUR BYTE FLOAT (big endian) : A B C D
	public static Object[] init_FOUR_BYTE_FLOAT_ABCD(RX_Info rx){
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 2 == 0) {
				String high = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());
				String low = null;
				
				if((i + 1) == rx.getPerfInfo().length) {
					low = "0000";
				}else {
					low = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
				}					
				
				value[i] = Float.intBitsToFloat((int)( ABCD_4BYTE(high, low) & 0xffffffffffffffffL ));
			} else {
				value[i] = "---";
			}
		}
		return value;
	}
	
	// FOUR BYTE FLOAT (little endian) : D C B A
	public static Object[] init_FOUR_BYTE_FLOAT_DCBA(RX_Info rx){
			Object[] value = new Object[rx.getPerfInfo().length];
	
			for (int i = 0; i < rx.getPerfInfo().length; i++) {
				if (i % 2 == 0) {
					String high = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());
					String low = null;
					
					if((i + 1) == rx.getPerfInfo().length) {
						low = "0000";
					}else {
						low = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					}					
					
					value[i] = Float.intBitsToFloat((int)( DCBA_4BYTE(high, low) & 0xffffffffffffffffL ));
				} else {
					value[i] = "---";
				}
			}
			return value;		
	}
	
	// FOUR BYTE FLOAT (big endian byte swap) : B A D C
	public static Object[] init_FOUR_BYTE_FLOAT_BADC(RX_Info rx){
			Object[] value = new Object[rx.getPerfInfo().length];
			
			for (int i = 0; i < rx.getPerfInfo().length; i++) {
				if (i % 2 == 0) {
					String high = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());
					String low = null;
					
					if((i + 1) == rx.getPerfInfo().length) {
						low = "0000";
					}else {
						low = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					}					
					
					value[i] = Float.intBitsToFloat((int)( BADC_4BYTE(high, low) & 0xffffffffffffffffL ));
				} else {
					value[i] = "---";
				}
			}
			return value;				
	}
	
	// FOUR BYTE FLOAT (little endian byte swap) : C D A B
	public static Object[] init_FOUR_BYTE_FLOAT_CDAB(RX_Info rx){
		Object[] value = new Object[rx.getPerfInfo().length];
		
		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 2 == 0) {
				String high = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());
				String low = null;
				
				if((i + 1) == rx.getPerfInfo().length) {
					low = "0000";
				}else {
					low = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
				}					
				
				value[i] = Float.intBitsToFloat((int)( CDAB_4BYTE(high, low) & 0xffffffffffffffffL ));
			} else {
				value[i] = "---";
			}
		}
		return value;			
	}
	
	
	
	/**
	 * 2021. 6. 19 : 8Byte 데이터 타입 추가 
	 */
	// EIGHT BYTE INT SIGNED : A B C D
	public static Object[] init_EIGHT_BYTE_INT_SIGNED_ABCD(RX_Info rx) {
		String A, B, C, D;
		
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 4 == 0) {
				A = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());				

				if ((i + 1) == rx.getPerfInfo().length) {
					B = "0000";
					C = "0000";
					D = "0000";
				} else if((i + 2) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = "0000"; 
					D = "0000"; 
				} else if((i + 3) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = "0000"; 
				} else {
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = String.format("%04x", rx.getPerfInfo()[i + 3].getIntHexValue());
				}
				
				value[i] = ABCD_8BYTE(A, B, C, D);					
			} else {
				value[i] = "---";
			}
		}
		return value;
	}
	
	
	// EIGHT BYTE INT SIGNED : D C B A
	public static Object[] init_EIGHT_BYTE_INT_SIGNED_DCBA(RX_Info rx) {
		String A, B, C, D;
		
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 4 == 0) {
				A = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());				

				if ((i + 1) == rx.getPerfInfo().length) {
					B = "0000";
					C = "0000";
					D = "0000";
				} else if((i + 2) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = "0000"; 
					D = "0000"; 
				} else if((i + 3) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = "0000"; 
				} else {
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = String.format("%04x", rx.getPerfInfo()[i + 3].getIntHexValue());
				}
				
				value[i] = DCBA_8BYTE(A, B, C, D);					
			} else {
				value[i] = "---";
			}
		}
		return value;
	}
	
	// EIGHT BYTE INT SIGNED : B A D C
	public static Object[] init_EIGHT_BYTE_INT_SIGNED_BADC(RX_Info rx) {
		String A, B, C, D;
		
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 4 == 0) {
				A = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());				

				if ((i + 1) == rx.getPerfInfo().length) {
					B = "0000";
					C = "0000";
					D = "0000";
				} else if((i + 2) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = "0000"; 
					D = "0000"; 
				} else if((i + 3) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = "0000"; 
				} else {
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = String.format("%04x", rx.getPerfInfo()[i + 3].getIntHexValue());
				}
				
				value[i] = BADC_8BYTE(A, B, C, D);					
			} else {
				value[i] = "---";
			}
		}
		return value;
	}
	
	// EIGHT BYTE INT SIGNED : C D A B
	public static Object[] init_EIGHT_BYTE_INT_SIGNED_CDAB(RX_Info rx) {
		String A, B, C, D;
		
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 4 == 0) {
				A = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());				

				if ((i + 1) == rx.getPerfInfo().length) {
					B = "0000";
					C = "0000";
					D = "0000";
				} else if((i + 2) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = "0000"; 
					D = "0000"; 
				} else if((i + 3) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = "0000"; 
				} else {
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = String.format("%04x", rx.getPerfInfo()[i + 3].getIntHexValue());
				}
				
				value[i] = CDAB_8BYTE(A, B, C, D);					
			} else {
				value[i] = "---";
			}
		}
		return value;	
	}
	
	
	
	// EIGHT BYTE INT UNSIGNED : A B C D
	public static Object[] init_EIGHT_BYTE_INT_UNSIGNED_ABCD(RX_Info rx) {
		String A, B, C, D;
		
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 4 == 0) {
				A = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());				

				if ((i + 1) == rx.getPerfInfo().length) {
					B = "0000";
					C = "0000";
					D = "0000";
				} else if((i + 2) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = "0000"; 
					D = "0000"; 
				} else if((i + 3) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = "0000"; 
				} else {
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = String.format("%04x", rx.getPerfInfo()[i + 3].getIntHexValue());
				}				
				
				value[i] = Long.toUnsignedString(ABCD_8BYTE(A, B, C, D));  					
			} else {
				value[i] = "---";
			}
		}
		return value;	
	}
	
	// EIGHT BYTE INT UNSIGNED : D C B A
	public static Object[] init_EIGHT_BYTE_INT_UNSIGNED_DCBA(RX_Info rx) {
		String A, B, C, D;
		
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 4 == 0) {
				A = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());				

				if ((i + 1) == rx.getPerfInfo().length) {
					B = "0000";
					C = "0000";
					D = "0000";
				} else if((i + 2) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = "0000"; 
					D = "0000"; 
				} else if((i + 3) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = "0000"; 
				} else {
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = String.format("%04x", rx.getPerfInfo()[i + 3].getIntHexValue());
				}				
				
				value[i] = Long.toUnsignedString(DCBA_8BYTE(A, B, C, D));  					
			} else {
				value[i] = "---";
			}
		}
		return value;	
	}
	
	// EIGHT BYTE INT UNSIGNED : B A D C
	public static Object[] init_EIGHT_BYTE_INT_UNSIGNED_BADC(RX_Info rx) {
		String A, B, C, D;
		
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 4 == 0) {
				A = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());				

				if ((i + 1) == rx.getPerfInfo().length) {
					B = "0000";
					C = "0000";
					D = "0000";
				} else if((i + 2) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = "0000"; 
					D = "0000"; 
				} else if((i + 3) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = "0000"; 
				} else {
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = String.format("%04x", rx.getPerfInfo()[i + 3].getIntHexValue());
				}				
				
				value[i] = Long.toUnsignedString(BADC_8BYTE(A, B, C, D));  					
			} else {
				value[i] = "---";
			}
		}
		return value;				
	}
	
	// EIGHT BYTE INT UNSIGNED : C D A B
	public static Object[] init_EIGHT_BYTE_INT_UNSIGNED_CDAB(RX_Info rx) {
		String A, B, C, D;
		
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 4 == 0) {
				A = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());				

				if ((i + 1) == rx.getPerfInfo().length) {
					B = "0000";
					C = "0000";
					D = "0000";
				} else if((i + 2) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = "0000"; 
					D = "0000"; 
				} else if((i + 3) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = "0000"; 
				} else {
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = String.format("%04x", rx.getPerfInfo()[i + 3].getIntHexValue());
				}				
				
				value[i] = Long.toUnsignedString(CDAB_8BYTE(A, B, C, D));  					
			} else {
				value[i] = "---";
			}
		}
		return value;
	}	
		
	
	// EIGHT BYTE DOUBLE : A B C D
	public static Object[] init_EIGHT_BYTE_DOUBLE_ABCD(RX_Info rx) {
		String A, B, C, D;
		
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 4 == 0) {
				A = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());				

				if ((i + 1) == rx.getPerfInfo().length) {
					B = "0000";
					C = "0000";
					D = "0000";
				} else if((i + 2) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = "0000"; 
					D = "0000"; 
				} else if((i + 3) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = "0000"; 
				} else {
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = String.format("%04x", rx.getPerfInfo()[i + 3].getIntHexValue());
				}				
				
				value[i] = Double.longBitsToDouble(ABCD_8BYTE(A, B, C, D) & 0xffffffffffffffffL);  					
			} else {
				value[i] = "---";
			}
		}
		return value;
	}

	
	// EIGHT BYTE DOUBLE : D C B A
	public static Object[] init_EIGHT_BYTE_DOUBLE_DCBA(RX_Info rx) {
		String A, B, C, D;
		
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 4 == 0) {
				A = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());				

				if ((i + 1) == rx.getPerfInfo().length) {
					B = "0000";
					C = "0000";
					D = "0000";
				} else if((i + 2) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = "0000"; 
					D = "0000"; 
				} else if((i + 3) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = "0000"; 
				} else {
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = String.format("%04x", rx.getPerfInfo()[i + 3].getIntHexValue());
				}				
				
				value[i] = Double.longBitsToDouble(DCBA_8BYTE(A, B, C, D) & 0xffffffffffffffffL);  					
			} else {
				value[i] = "---";
			}
		}
		return value;
	}
	
	// EIGHT BYTE DOUBLE : B A D C
	public static Object[] init_EIGHT_BYTE_DOUBLE_BADC(RX_Info rx) {
		String A, B, C, D;
		
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 4 == 0) {
				A = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());				

				if ((i + 1) == rx.getPerfInfo().length) {
					B = "0000";
					C = "0000";
					D = "0000";
				} else if((i + 2) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = "0000"; 
					D = "0000"; 
				} else if((i + 3) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = "0000"; 
				} else {
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = String.format("%04x", rx.getPerfInfo()[i + 3].getIntHexValue());
				}				
				
				value[i] = Double.longBitsToDouble(BADC_8BYTE(A, B, C, D) & 0xffffffffffffffffL);  					
			} else {
				value[i] = "---";
			}
		}
		return value;
	}
	
	// EIGHT BYTE DOUBLE : C D A B
	public static Object[] init_EIGHT_BYTE_DOUBLE_CDAB(RX_Info rx) {
		String A, B, C, D;
		
		Object[] value = new Object[rx.getPerfInfo().length];

		for (int i = 0; i < rx.getPerfInfo().length; i++) {
			if (i % 4 == 0) {
				A = String.format("%04x", rx.getPerfInfo()[i].getIntHexValue());				

				if ((i + 1) == rx.getPerfInfo().length) {
					B = "0000";
					C = "0000";
					D = "0000";
				} else if((i + 2) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = "0000"; 
					D = "0000"; 
				} else if((i + 3) == rx.getPerfInfo().length){
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = "0000"; 
				} else {
					B = String.format("%04x", rx.getPerfInfo()[i + 1].getIntHexValue());
					C = String.format("%04x", rx.getPerfInfo()[i + 2].getIntHexValue());
					D = String.format("%04x", rx.getPerfInfo()[i + 3].getIntHexValue());
				}				
				
				value[i] = Double.longBitsToDouble(CDAB_8BYTE(A, B, C, D) & 0xffffffffffffffffL);  					
			} else {
				value[i] = "---";
			}
		}
		return value;
	}
}
