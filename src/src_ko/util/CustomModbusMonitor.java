package src_ko.util;

/**
 * @author Moon
 * 
 * ����� ���� ������ �����
 * ������ ��ſ� ���� ������ Ÿ��, ��� �ڵ�, ��û ���� �������� �ּ�, �������� ��û ����, ������ Ÿ��, ����Ʈ ���� ������ ���� ���� �� �� �ִ�
 */

public class CustomModbusMonitor{
	
}

/*
public class CustomModbusMonitor extends MultiDropMonitor {
	
	// Modbus Type
	public static final String MODBUS_TCP = "TCP";
	public static final String MODBUS_RTU = "RTU";

	// Modbus FunctionCode
	public static final int FC01 = 0x01; // Read Coil Status
	public static final int FC02 = 0x02; // Read Input Status
	public static final int FC03 = 0x03; // Read Holding Registers
	public static final int FC04 = 0x04; // Read Input Registers
	public static final int FC05 = 0x05; // Force Single Coil
	public static final int FC06 = 0x06; // Preset Single Register
	public static final int FC15 = 0x0F; // Force Multiple Coils
	public static final int FC16 = 0x10; // Preset Multiple Registers
	
	// Modbus Exception Description
	public static final String[] MODBUS_EXCEPTION_DESCRIPTION = {
		"Illegal Function(0x01)",
		"Illegal Data Address(0x02)",
		"Illegal Data Value(0x03)",
		"Slave Device Failure(0x04)",
		"Acknowledge(0x05)",
		"Slave Device Busy(0x06)",
		"Negative Acknoowledge(0x07)",
		"Memory Parity Error(0x08)",
		"No definition (Unknown Exception)",
		"Gateway Path Unavailable(0x0A)",
		"Gateway Target Device Failed to Respond(0x0B)"
	};
	
	// Control Type
	public static final String USE_PARAM = "Y"; // �Ķ���͸� ����ϴ� ����
	public static final String NOT_USE_PARAM = "N"; // �Ķ���͸� ������� �ʴ� ����
	public static final String WORD_BIT_CONTROL = "BIT"; // ������� ��Ʈ ����
	
	// Byte Order
	public static final String A_B_C_D = "ABCD";
	public static final String D_C_B_A = "DCBA";
	public static final String B_A_D_C = "BADC";
	public static final String C_D_A_B = "CDAB";
	
	// DataType Byte Size
	public static final int REGISTER_SIZE_S = 2;
	public static final int REGISTER_SIZE_US = 2;
	public static final int REGISTER_SIZE_I = 4;
	public static final int REGISTER_SIZE_UI = 4;
	public static final int REGISTER_SIZE_F = 4;
	public static final int REGISTER_SIZE_L = 8;
	public static final int REGISTER_SIZE_UL = 8;
	public static final int REGISTER_SIZE_D = 8;

	// Modbuc TCP Header
	protected short tid = 0;
	protected static final short PROTOCOL_IDENTIFIER = 0;
	
	// Default Device uid, type
	protected int unitId = 1;
	protected String type = MODBUS_RTU;
	
	// RX Data Check Enabled (Default : Not Use)
	protected boolean functionCheckEnabled = false;
	protected boolean lengthCheckEnabled = false;
	protected boolean retryCrcEnabled = false;
	
    protected int currentCommand;
    protected int maxRetryCrcCount = 3;
    protected final List<String> commandList = new ArrayList<>();
    protected final HashMap<Integer, Integer> crcErrCntMap = new HashMap<>();
    
    // ���� ���� �����
    protected HashMap<String, Integer> currentPerfMap = new HashMap();
    
    protected void init(ResultSet rs) throws SQLException {
        super.init(rs);
        String protocolData = rs.getString("PROTOCOL_DATA").trim();
        
        try {
        	if(protocolData.contains(",")) {
        		this.unitId = Integer.parseInt(protocolData.split(",")[0].trim());
    	        this.setType(protocolData.split(",")[1].trim().toUpperCase());
        	}else {
        		this.unitId = Integer.parseInt(protocolData);
        		this.setType(this.type);
        	}
        }catch(NumberFormatException e) {
        	this.unitId = 1;
        	this.setType(this.type);
        }catch(NullPointerException e) {
        	this.unitId = 1;
        	this.setType(this.type);
        }
    }
    
    public int getBaseUnitID() {
		return 1;
	}
            
    protected final void setType(String type) {
    	// ��� ��� ���� �Ķ���Ͱ� �߸��Ǿ��ٸ� �⺻ ��� ������� �����Ѵ� 
    	if(!(type.equalsIgnoreCase(MODBUS_TCP) || type.equalsIgnoreCase(MODBUS_RTU)))
    		type = this.type;
    		    	
		this.type = type;
	}
    
    protected final void setDefaultType(String type) {
		this.setType(type);
	}
    
    protected final void setFunctionCheckEnable(boolean enabled) {
    	this.functionCheckEnabled = enabled;
    }
    
    protected final void setLengthCheckEnable(boolean enabled) {
    	this.lengthCheckEnabled = enabled;
    }
    
    protected final void setRetryCrcEnable(boolean enabled) {
    	this.retryCrcEnabled = enabled;
    }
    
    protected final void setMaxRetryCrcCount(int maxRetrycount) {
    	this.maxRetryCrcCount = maxRetrycount;
    }
    
	public int parseCommand(String command, boolean forControl) throws IllegalArgumentException {
		int parsedCommand = commandList.indexOf(command);
		
		if (parsedCommand < 0) {
			commandList.add(command);
			parsedCommand = commandList.size() - 1;
		}
		
		// ���� Ŀ�ǵ��� ���
		if (forControl) parsedCommand *= -1;
		
		crcErrCntMap.put(parsedCommand, 0);
		return parsedCommand;
	}

	protected void sendCommand(PacketOutputStream out, int command, String[] args) throws IOException {		
		
		if(type.equalsIgnoreCase(MODBUS_TCP)) sendModbusTcpHeader(out);
		
		if(command >= 0) {
			// ���� ��û			
			sendCollectRequest(out, command);
		}else {
			// ���� ��û
			sendControlRequest(out, command, args);						
		}
			
		currentCommand = command;
	}

	protected void parseResponsePacket(PacketInputStream in) throws IOException {

		if(this.type.equalsIgnoreCase(MODBUS_TCP)) skipModbusTcpHeader(in);
				
		checkUnitID(in);

		int function = in.read();

		// Compare TX, RX FunctionCode (Exception ���� ��Ŷ�� �Ʒ��� ������ �������� �ʴ´�)
		if(function <= FC16 && functionCheckEnabled)
			if(functionCheckEnabled) checkFunctionCode(commandList.get(currentCommand), function);
		
		if(function == FC05 || function == FC06 || function == FC15 || function == FC16) {
			// ����
			in.readShort();
			in.readShort();
			if (type.equalsIgnoreCase(MODBUS_RTU)) in.skipCRC16();
			processControlResult(currentCommand, "SUCCESS");

		}else if (function == FC01 || function == FC02) {
			
			int FollowByte = in.read();
			if(lengthCheckEnabled) checkLength(commandList.get(currentCommand), FollowByte);
			
			int i, j, temp;
			double[] ret = new double[FollowByte * 8];

			for (i = 0; i < FollowByte; i++) {
				temp = in.read();
				for (j = 0; j < 8; j++) {
					if (i * 8 + j >= (FollowByte * 8))
						break;
					ret[i * 8 + j] = (temp >> j & 1);
				}
			}

			if (type.equalsIgnoreCase(MODBUS_RTU)) {
				try {
					in.skipCRC16();
				}catch(IOException e) {
					if(retryCrcEnabled) {
						if(processCrcRetry(currentCommand, e)) return;	
					}else
						throw e;
				}
			}
			
			// ���� ��Ŷ�� CRC ���뿡 ������ ���ٸ� ���� Ŀ�ǵ�(��û)�� CRC ���� ī��Ʈ�� �ʱ�ȭ
            crcErrCntMap.put(currentCommand, 0);
			updateWatchPoints(currentCommand, ret);

		}else if (function == FC03 || function == FC04) {
			CustomPacketInputStream cin = new CustomPacketInputStream(in);
			
			String dataType = commandList.get(currentCommand).split("_")[3];
			int byteCount = in.read();
			if(lengthCheckEnabled) checkLength(commandList.get(currentCommand), byteCount);
			
			double[] ret = new double[byteCount / getRegisterSize(dataType)];

			for(int i = 0; i < ret.length; i++) {
				ret[i] = cin.processData(dataType);

				// ������ Ÿ�� ũ�Ⱑ Word�� ���("S", "US") ���� ���� ��Ʈ ��� ���Ͽ� ������ ���� �� ����
				if(dataType.equalsIgnoreCase("S") || dataType.equalsIgnoreCase("US")) {
					String perfCounter = commandList.get(currentCommand); // 3_0_10_S
					int currentPerfSlot = i + 1; // WatchPoint.getCheckSlot(); // {1}					
					perfCounter = getOriginPerfCounter(perfCounter, currentPerfSlot); // 3_0_10_S\{1}					
					currentPerfMap.put(perfCounter, (int) ret[i]);
				}
			}
			
			if (type.equalsIgnoreCase(MODBUS_RTU)) {
				try {
					in.skipCRC16();
				}catch(IOException e) {
					if(retryCrcEnabled) {
						if(processCrcRetry(currentCommand, e)) return;	
					}else
						throw e;
				}
			}
			
			// ���� ��Ŷ�� CRC ���뿡 ������ ���ٸ� ���� Ŀ�ǵ�(��û)�� CRC ���� ī��Ʈ�� �ʱ�ȭ
            crcErrCntMap.put(currentCommand, 0);
			updateWatchPoints(currentCommand, ret);
			
		}else if (function >= 0x81 && function <= 0x91) {
			// ���� ���� ó��			
			int exceptionCode = in.read();
			if (type.equalsIgnoreCase(MODBUS_RTU)) in.skipCRC16();
			throw new IOException(String.format("Recive Modbus Exception [ Function Code : 0x%02x / Exception Code : 0x%02x / Exception Desc : %s ]", 
					(function & 0xff),
					(exceptionCode & 0xff),
					MODBUS_EXCEPTION_DESCRIPTION[exceptionCode - 1]));			
		}else throw new IOException(String.format("Read Incorrect Modbus Function Code : 0x%02x", function & 0xff));
				
	}
	
		
	// ���� ��Ŷ ����
	protected void sendCollectRequest(PacketOutputStream out, int command) throws IOException {
		int[] tokens = new int[3];
		String[] perfCount = commandList.get(command).split("_");

		for (int i = 0; i < tokens.length; i++) {
			if (perfCount[i].contains("0x") || perfCount[i].contains("0X")) {
				perfCount[i] = perfCount[i].replace("0x", "").replace("0X", "");
				perfCount[i] = String.valueOf(Integer.parseInt(perfCount[i], 16));
			}

			// tokens[0] : Function Code
			// tokens[1] : Start Address
			// tokens[2] : Request Count
			tokens[i] = Integer.parseInt(perfCount[i]);
		}

		out.write(getUnitID()); // Unit ID (Slave Address)
		out.write(tokens[0]); // Function Code
		out.writeShort(tokens[1]); // Start Address
		out.writeShort(tokens[2]); // Request Count

		if (type.equalsIgnoreCase(MODBUS_RTU)) out.writeCRC16(0);
	}
	
	
	// ���� ��Ŷ ����
	protected void sendControlRequest(PacketOutputStream out, int command, String[] args) throws IOException {
		int ActualCommand = command * -1;
		
		String[] controlInfo = commandList.get(ActualCommand).split("_");
		
		// ���� �� �ּҰ� 16������ ǥ��Ǿ� �ִٸ� 10������ �������ش�
		if (controlInfo[1].contains("0x") || controlInfo[1].contains("0X")) {
			controlInfo[1] = controlInfo[1].replace("0x", "").replace("0X", "");
			controlInfo[1] = String.valueOf(Integer.parseInt(controlInfo[1], 16));
		}
		
		int functionCode = Integer.parseInt(controlInfo[0]);
		int controlAddress = Integer.parseInt(controlInfo[1]);
		
		if (functionCode == FC05) {
			int controlStatus = 0x0000;
			
			out.write(getUnitID());
			out.write(FC05);
			out.writeShort(controlAddress);
			
			// FC05 : �⺻������ ����� �Ķ���͸� ������� ���� (���� ���ุ���� ON, OFF ���� �����ϵ��� ����)
			// 5_0_1, 5_0_ON : FC01 0x0000 register 1(ON) 
			// 5_8_0, 5_8_OFF : FC01 0x0008 register 0(OFF)
			if(controlInfo[2].equalsIgnoreCase("1") || controlInfo[2].equalsIgnoreCase("ON")) {
				controlStatus = 0xFF00; // ON
			}else if(controlInfo[2].equalsIgnoreCase("0") || controlInfo[2].equalsIgnoreCase("OFF")) {
				controlStatus = 0x0000; // OFF
			}else {
				throw new IOException("["+this.getName()+"] Wrong Control Format (FC05) : " + commandList.get(ActualCommand));
			}
			
			out.writeShort(controlStatus);
			
		}else {
			out.write(getUnitID());
			out.write(FC06);
			out.writeShort(controlAddress);			
			
			// ���� �Ķ���� ��� ���� : Y / N
			// ���� ���� ��Ʈ ���� : BIT 0~15
			String controlType = commandList.get(ActualCommand).split("_")[2].trim();

			if (controlType.startsWith(USE_PARAM)) {
				// "Y" : ��� �Ķ���͸� ����� ���
				// 6_0_Y_10 : FC06 / register : 0x0000 / useParam : true / scale : 10
				// 6_10_Y_100 : FC06 / register : 0x000A / useParam : true / scale : 100
				Double originValue = Double.parseDouble(args[0]);
				int scale = Integer.parseInt(commandList.get(ActualCommand).split("_")[3]);
				int writeValue = (int) (originValue * scale);
				out.writeShort(writeValue);
				
			} else if (controlType.startsWith(NOT_USE_PARAM)) {
				// "N" : ��� �Ķ���͸� ������� ���� ���
				// 6_5_N_1 : FC06 / register : 0x0005 / useParam : false / controlValue : 1
				// 6_11_N_25 : FC06 / register : 0x000B / useParam : false / controlValue : 25
				out.writeShort(Integer.parseInt(commandList.get(ActualCommand).split("_")[3]));
				
			} else if (controlType.startsWith(WORD_BIT_CONTROL)) {
				// "BIT" : ���� ���� ��Ʈ ����
				// ControlCommand : "6_0_BIT3_ON;3_0_10_S\{1}"
				// ���� : FC06 ����ڵ带 ����Ͽ� 0x0000���� 3�� ��Ʈ�� 1(ON)���� ����
				// ���� ���� ��Ʈ ���� ���� �� ���� 3_0_10_S\{1}�� WatchPoint�� ������ ���� ��
				String controlCommand = commandList.get(ActualCommand);
				int writeBitStatus = 0;
				
				String write = controlInfo[3].split(";")[0];
				if(write.equalsIgnoreCase("1") || write.equalsIgnoreCase("ON")) {
					writeBitStatus = 1; // ON
				}else if(write.equalsIgnoreCase("0") || write.equalsIgnoreCase("OFF")) {
					writeBitStatus = 0; // OFF
				}else {
					throw new IOException("["+this.getName()+"] Wrong Control Format (FC06) : " + commandList.get(ActualCommand));
				}
				
				int nowValue = currentPerfMap.get(controlCommand.split(";")[1]);
				int BitNum = Integer.parseInt(controlType.replace(WORD_BIT_CONTROL, ""));
				int wordControlValue = getWordControlParam(nowValue, BitNum, writeBitStatus);
				out.writeShort(wordControlValue);
				
			}else {
				throw new IOException("["+this.getName()+"] Wrong Control Format (FC06) : " + commandList.get(ActualCommand));				
			}
		}
		
		if (type.equalsIgnoreCase(MODBUS_RTU)) out.writeCRC16(0);
	}
	
	
	// ���� ���� ��Ʈ ���� ����
	private int getWordControlParam(int nowValue, int bitNum, int writeBitStatus) {
		String bitString = String.format("%16s", Integer.toBinaryString(nowValue)).replace(' ', '0');
		return replaceBit(bitString, bitNum, writeBitStatus);
	}

	// ���� ���� ��Ʈ ���� ����
	private int replaceBit(String orig, int index, int value) {
		char[] origChars = orig.toCharArray();
		String charVal = String.valueOf(value);
		origChars[orig.length() - 1 - index] = charVal.charAt(0);
		String ret = String.valueOf(origChars);
		return Integer.parseInt(ret, 2);
	}
	
	// ���ڷ� ���� ������ Ÿ���� �������� ũ��(Byte Size) ����
	protected int getRegisterSize(String dataType) {		
		if(dataType.startsWith("S")) {
			return REGISTER_SIZE_S;
		}else if(dataType.startsWith("US")) {
			return REGISTER_SIZE_US;
		}else if(dataType.startsWith("I")) {
			return REGISTER_SIZE_I;
		}else if(dataType.startsWith("UI")) {
			return REGISTER_SIZE_UI;
		}else if(dataType.startsWith("F")) {
			return REGISTER_SIZE_F;
		}else if(dataType.startsWith("L")) {
			return REGISTER_SIZE_L;
		}else if(dataType.startsWith("UL")) {
			return REGISTER_SIZE_UL;
		}else if(dataType.startsWith("D")) {
			return REGISTER_SIZE_D;
		}else {
			return REGISTER_SIZE_S;
		}		
	}	
	
	
	// ������ ������ ���� ī���� ���ڿ� ����
	private String getOriginPerfCounter(String perfCounter, int perfSlot) {
		return new StringBuilder(perfCounter).append("\\{").append(perfSlot).append("}").toString();
	}
	
	
	// ���� ī������ ������ ���� Ÿ������ ����
	private int getPerfSlot(String perfCounter) {
		return Integer.parseInt(perfCounter.split("\\\\")[1].replace("{", "").replace("}", ""));
	}
	
	
	// Send Modbus TCP Header
	protected void sendModbusTcpHeader(PacketOutputStream out) throws IOException {
		if (++tid >= Short.MAX_VALUE) tid = 1;
        out.writeShort(tid);
        out.writeShort(PROTOCOL_IDENTIFIER);
        out.writeShort(6);
	}
	
	
	// Skip Modbus TCP Header
	protected void skipModbusTcpHeader(PacketInputStream in) throws IOException {
		readTid(in); // Check Transaction ID
		readProtocolID(in); //  Check Protocol ID
		in.readShort();	 // Pass Length
	}
	
	
	// Check Transaction ID
	protected void readTid(PacketInputStream in) throws IOException {
		int received_tid = in.readShort();
		if(tid != received_tid)
			throw new IOException("["+this.getName()+"] Tid Mismatch! [ Expected : " + String.format("0x%04x", tid & 0xffff) + "] [ Actual : " + String.format("0x%04x", received_tid & 0xffff) + " ]");
	}
	
	
	// Check Protocol ID
	protected void readProtocolID(PacketInputStream in) throws IOException {
		int received_protocolId = in.readShort();
		if(PROTOCOL_IDENTIFIER != received_protocolId)
			throw new IOException("["+this.getName()+"] Wrong protocol ID! [ Expected : " + String.format("0x%04x", PROTOCOL_IDENTIFIER & 0xffff) + "] [ Actual : " + String.format("0x%04x", received_protocolId & 0xffff) + " ]");
	}
	
	// Check Unit ID
	protected void checkUnitID(PacketInputStream in) throws IOException {
		int received_unitId = in.read();
		if(getUnitID() != received_unitId)
			throw new IOException("["+this.getName()+"] Unit ID Mismatch! [ Expected : " + String.format("0x%02x", getUnitID() & 0xff) + " ] [ Actual : " + String.format("0x%02x", received_unitId & 0xff) + " ]");
	}
	
	// Compare TX, RX FunctionCode
	protected void checkFunctionCode(String command, int functionCode) throws IOException {
		int expectedFunction = Integer.parseInt(command.split("_")[0]);
		if(expectedFunction != functionCode)
			throw new IOException(String.format("[%s] Recive Wrong FunctionCode! [ Expected FC : 0x%02x ] [ Actual FC : 0x%02x ]", this.getName(), expectedFunction, functionCode));
	}
	
	// Check Response Data Length
	protected void checkLength(String command, int actualLength) throws IOException {
		String[] requestInfo = command.split("_");

		int functionCode = Integer.parseInt(requestInfo[0]);
		int requestCount = Integer.parseInt(requestInfo[2]);
		int expectedLength = 0;

		if (functionCode >= FC01 && functionCode <= FC02) {
			expectedLength = (requestCount % 8 == 0) ? (requestCount / 8) : (requestCount / 8) + 1;
		} else if (functionCode >= FC03 && functionCode <= FC04) {
			expectedLength = requestCount * 2;
		}

		if (expectedLength != actualLength)
			throw new IOException(String.format("[%s] Recive Wrong RX Data Length(Read Byte Count)! [ Expected : 0x%02x ] [ Actual : 0x%02x ]", this.getName(), expectedLength, actualLength));
	}
	
	
	// CRC �˻� ����� ������ �ִٸ� maxRetryCount��ŭ ���� ��û(currentCommand)�� ��õ�
	protected boolean processCrcRetry(int currentCommand,IOException e) throws IOException{
		int crcErrCnt = crcErrCntMap.get(currentCommand);

		if(e.getMessage().contains("Incorrect CRC") && (crcErrCnt < maxRetryCrcCount)) {
			String msg = MessageFormat.format("Read Incorrect CRC [ Command : {0} ] [ Continuous CRC-Error Count : {1} => Current Request Retry ]", currentCommand, crcErrCnt);
			IOException crcErrException = new IOException(msg);
			writePacketLog(msg);
			crcErrCntMap.put(currentCommand, ++crcErrCnt);
			handleCommError(currentCommand, crcErrException);
			postCommand(currentCommand, null);
			return true;
		}else if (e.getMessage().contains("Incorrect CRC")) {    				
			String msg = MessageFormat.format("Read Incorrect CRC [ Command : {0} ] [ Continuous CRC-Error Count : {1} ]", currentCommand, crcErrCnt);
			IOException crcErrException = new IOException(msg);    				
			crcErrCntMap.put(currentCommand, ++crcErrCnt);      				
			throw crcErrException;
		}else{
			// Not a CRC-Error
			throw e;
		}
	}
	
	
	protected void writePacketLog(String msg) {
		if (packetLogEnable && packetLogger != null) {
			packetLogger.error(msg);
		}
	}
	
	protected int hexStringToInteger(String hexString) {		
		if (hexString.contains("0x") || hexString.contains("0X")) {
			hexString = hexString.replace("0x", "").replace("0X", "");
		}
		
		return Integer.parseInt(hexString, 16);
	}
	
}
*/