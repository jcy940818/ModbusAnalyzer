package common.modbus;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.base.KeyedModbusLocator;
import com.serotonin.modbus4j.base.ReadFunctionGroup;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.FunctionCode;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.ip.xa.XaMessageRequest;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.locator.BinaryLocator;
import com.serotonin.modbus4j.locator.NumericLocator;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.serial.rtu.RtuMessageRequest;

import common.agent.PerfData;
import src_ko.util.PacketInputStream;
import src_ko.util.PacketOutputStream;

public class ModbusMonitor{

	public static boolean isRunning = false;
	
	public static final int TYPE_RTU = 997;
	public static final int TYPE_TCP = 998;

	private List locators = new ArrayList();
	private List commands = new ArrayList();
	private List<ModbusWatchPoint> points = new ArrayList<ModbusWatchPoint>();
	
	private int type;
	private boolean partitioned = false;
	private int transactionId = 0;
	private int unitID = 1;
	
	ModbusMaster master;
	private BatchRead batchRead = new BatchRead();
	
	public int getType() {
		return type;
	}
	
	public final void setType(int type) {
		this.type = type;
	}
	
	public int getUnitID() {
        return unitID;
    }
	
	public void setUnitID(int unitID) {
		this.unitID = unitID;
	}
	
	public int getTransactionID() {
		return transactionId;
	}
	
	public void setTransactionID(int transactionId) {
		this.transactionId = transactionId;
	}
	
	public void init(int type, String ip, int port){
		this.type = type;
		IpParameters params = new IpParameters();
		params.setHost(ip);
		params.setPort(port);
		
		master = new ModbusFactory().createTcpMaster(params, false);
		try {
			master.init();
		} catch (ModbusInitException e) {
			e.printStackTrace();
		}
	}

	public synchronized int parseCommand(ModbusWatchPoint point) {
		String command = point.getHexCounter();
		String[] strs = command.split("_");
		int address;
		int func = Integer.parseInt(strs[0]);

		if (strs[1].startsWith("0x") || strs[1].startsWith("0X")) {
			address = Integer.parseInt(strs[1].substring(2), 16);
			command = command.replaceAll("0x", "").replaceAll("0X", "");
		} else {
			address = Integer.parseInt(strs[1]);
			address %= 10000;
			address--;
		}

		int dataType = DataType.TWO_BYTE_INT_SIGNED;
		if (strs.length > 2) {
			dataType = getDataType(strs[2]);
		}

		BaseLocator locator = null;
		if (func == 1 || func == 2) {
			locator = new BinaryLocator(getUnitID(), func, address);
		} else {
			locator = new NumericLocator(getUnitID(), func, address, dataType);
		}

		if (partitioned) {
			// 이미 partitioning 되어있으면 batchRead를 새로 생성해야 한다
			batchRead = new BatchRead();

			for (int i = 0; i < commands.size(); i++) {
				batchRead.addLocator(commands.get(i), (BaseLocator) locators.get(i));
			}

			partitioned = false;
		}

		batchRead.addLocator(command, locator);
		commands.add(command);
		locators.add(locator);
		points.add(point);

		return locators.size() - 1;
	}

	public synchronized String sendCommand(ReadFunctionGroup functionGroup, Socket clientSocket) throws IOException {
		byte[] buff = new byte[8192];
		PacketInputStream in = new PacketInputStream(buff, clientSocket.getInputStream());
		PacketOutputStream out = new PacketOutputStream(clientSocket.getOutputStream());
		
		ModbusRequest request = null;
		
		int slaveId = this.getUnitID();
		int startOffset = functionGroup.getStartOffset();

		try {
			if (functionGroup.getFunctionCode() == FunctionCode.READ_COILS) {
				request = new ReadCoilsRequest(slaveId, startOffset, functionGroup.getLength());
				
			} else if (functionGroup.getFunctionCode() == FunctionCode.READ_DISCRETE_INPUTS) {
				request = new ReadDiscreteInputsRequest(slaveId, startOffset, functionGroup.getLength());
				
			} else if (functionGroup.getFunctionCode() == FunctionCode.READ_HOLDING_REGISTERS) {
				request = new ReadHoldingRegistersRequest(slaveId, startOffset, functionGroup.getLength());
				
			} else if (functionGroup.getFunctionCode() == FunctionCode.READ_INPUT_REGISTERS) {
				request = new ReadInputRegistersRequest(slaveId, startOffset, functionGroup.getLength());
				
			} else {
				throw new IOException("Unsupported Function");
			}
		} catch (ModbusTransportException e) {
			throw new IOException("ModbusTransportException Function " + e.toString());	
		}
		
		out.write(getRequestPacket(request));
		byte[] packet = out.toByteArray();
		out.flush();
		
		return getPacketString(packet, 0, packet.length);
	}

	public synchronized String parseResponsePacket(ReadFunctionGroup functionGroup, Socket clientSocket) throws IOException {		
		byte[] buff = new byte[8192];		
		PacketInputStream in = new PacketInputStream(buff, clientSocket.getInputStream());
		PacketOutputStream out = new PacketOutputStream(clientSocket.getOutputStream());
		
		int readUnitID = 0;
		
		switch(type) {
			case TYPE_RTU :
				readUnitID = in.readByte();
				break;
				
			case TYPE_TCP :
				int transactionID = in.readShort();
				int protocolID = in.readShort();
				int length = in.readShort();
				readUnitID = in.readByte();
				break;
		}
		
		int funcCode = functionGroup.getFunctionCode();
		int func = in.read();
		
		if (func != funcCode) {
			if( func > 0x80 && func < 0x91){
				byte[] packet = in.debug_getBuffer();
				String rxPacket = getPacketString(packet, 0, packet.length);
				System.out.println("RX : " + rxPacket);
				return rxPacket;
			}
		}
		
		int startOffset = functionGroup.getStartOffset();
		int byteCount = getByteCount(in);
		
		byte[] data = new byte[byteCount];
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte)in.read();
		}

		if (type == TYPE_RTU) {
//			in.skipCRC16();
			in.readShort();
		}

		byte[] packet = in.debug_getBuffer();
		String rxPacket = getPacketString(packet, 0, packet.length);
		System.out.println("RX : " + rxPacket);
		
		List locators = functionGroup.getLocators();
		
		for (int i = 0; i < locators.size(); i++) {
			KeyedModbusLocator keyLocator = (KeyedModbusLocator) locators.get(i);
			BaseLocator locator = (BaseLocator) keyLocator.getLocator();
			int cmd = this.locators.indexOf(locator);
			Object valueObj = keyLocator.bytesToValue(data, startOffset);
			
			double value = Double.NaN;
			
			// 수집값 처리 단위 : double
			if (valueObj instanceof Number) {
				value = ((Number) valueObj).doubleValue();
			}
			else if (valueObj instanceof Boolean) {
				value = ((Boolean) valueObj).booleanValue() ? 1 : 0;
			}
			
			ModbusWatchPoint point = points.get(cmd);
			PerfData perfData = new PerfData();
			perfData.setPureValue(value);
			perfData.setValue(point.getComputedValue(value));
			perfData.setTime(System.currentTimeMillis());
			point.setData(perfData);
			
			String measrue = null;
			if(point.getMeasure() != null && !point.getMeasure().trim().equals("") && point.getMeasure().trim().length() >= 1) {
				measrue = point.getMeasure().trim();
			}
			
			System.out.printf("[ %s ] = " + PerfData.getPerfPureValue(perfData) + "\n", point.getDecCounter());
		}
		
		return rxPacket;
	}
	
	public final void setMaxReadBitCount(int maxReadBitCount) {
		this.master.setMaxReadBitCount(maxReadBitCount);
	}
	
	public final void setMaxReadRegisterCount(int maxReadRegisterCount) {
		this.master.setMaxReadRegisterCount(maxReadRegisterCount);
	}

	public int getByteCount(PacketInputStream in) throws IOException {
		return in.read();
	}

	private synchronized ReadFunctionGroup getFuntionGroup(BaseLocator locator) throws IOException {
		// 이 함수가 호출된 뒤에는 batchRead.addLocator()를 호출해도 소용이 없다.
		List functionGroups = batchRead.getReadFunctionGroups(master);
		partitioned = true;

		for (int i = 0; i < functionGroups.size(); i++) {
			ReadFunctionGroup functionGroup = (ReadFunctionGroup) functionGroups.get(i);

			List locators = functionGroup.getLocators();
			for (int j = 0; j < locators.size(); j++) {
				KeyedModbusLocator keyLocator = (KeyedModbusLocator) locators.get(j);

				if (keyLocator.getLocator().equals(locator)) {
					return functionGroup;
				}
			}
		}

		throw new IOException();
	}
	
	public synchronized List<ReadFunctionGroup> getFuntionGroupList(){
		return (List<ReadFunctionGroup>) batchRead.getReadFunctionGroups(master);
	}

	private synchronized byte[] getRequestPacket(ModbusRequest request) {
		switch (type) {
			case TYPE_RTU:
				return new RtuMessageRequest(request).getMessageData();
			case TYPE_TCP:
				return new XaMessageRequest(request, getTransactionID()).getMessageData();
			default:
				System.out.println("Unsupported Modbus Type");
				return null;
		}
	}
	
	private int getDataType(String dataTypeStr) {
		int dataType = DataType.TWO_BYTE_INT_SIGNED;
		
		if (dataTypeStr.equalsIgnoreCase("BINARY")) {
			dataType = DataType.BINARY;
		}
		else if (dataTypeStr.equalsIgnoreCase("TWO BYTE INT SIGNED")){
			dataType = DataType.TWO_BYTE_INT_SIGNED;
		}
		else if (dataTypeStr.equalsIgnoreCase("TWO BYTE INT UNSIGNED")) {
			dataType = DataType.TWO_BYTE_INT_UNSIGNED;
		}
		else if (dataTypeStr.equalsIgnoreCase("FOUR BYTE INT SIGNED")){
			dataType = DataType.FOUR_BYTE_INT_SIGNED;
		}
		else if (dataTypeStr.equalsIgnoreCase("FOUR BYTE INT UNSIGNED")){
			dataType = DataType.FOUR_BYTE_INT_UNSIGNED;
		}
		else if (dataTypeStr.equalsIgnoreCase("FOUR BYTE INT SWAPPED")) {
			dataType = DataType.FOUR_BYTE_INT_SIGNED_SWAPPED;
		}
		else if (dataTypeStr.equalsIgnoreCase("FOUR BYTE INT SIGNED SWAPPED")) {
			dataType = DataType.FOUR_BYTE_INT_SIGNED_SWAPPED;
		}
		else if (dataTypeStr.equalsIgnoreCase("FOUR BYTE INT UNSIGNED SWAPPED")) {
			dataType = DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED;
		}		
		else if (dataTypeStr.equalsIgnoreCase("FOUR BYTE FLOAT")) {
			dataType = DataType.FOUR_BYTE_FLOAT;
		}
		else if (dataTypeStr.equalsIgnoreCase("FOUR BYTE FLOAT SWAPPED")) {
			dataType = DataType.FOUR_BYTE_FLOAT_SWAPPED;
		}		
		else if (dataTypeStr.equalsIgnoreCase("EIGHT BYTE INT SIGNED")) {
			dataType = DataType.EIGHT_BYTE_INT_SIGNED;
		}
		else if (dataTypeStr.equalsIgnoreCase("EIGHT BYTE FLOAT")) {
			dataType = DataType.EIGHT_BYTE_FLOAT;
		}

		return dataType;
	}
	
	private String getDataTypeString(int dataTypeInt) {
		String dataType = "TWO BYTE INT SIGNED";
		
		if (dataTypeInt == DataType.BINARY) {
			return "BINARY";
		}
		else if (dataTypeInt == DataType.TWO_BYTE_INT_SIGNED){
			return "TWO BYTE INT SIGNED";
		}
		else if (dataTypeInt == DataType.TWO_BYTE_INT_UNSIGNED) {
			return "TWO BYTE INT UNSIGNED";
		}
		else if (dataTypeInt == DataType.FOUR_BYTE_INT_SIGNED){
			return "FOUR BYTE INT SIGNED";
		}
		else if (dataTypeInt == DataType.FOUR_BYTE_INT_UNSIGNED){
			return "FOUR BYTE INT UNSIGNED";
		}
		else if (dataTypeInt == DataType.FOUR_BYTE_INT_SIGNED_SWAPPED) {
			return "FOUR BYTE INT SWAPPED";
		}
		else if (dataTypeInt == DataType.FOUR_BYTE_INT_SIGNED_SWAPPED) {
			return "FOUR BYTE INT SIGNED SWAPPED";
		}
		else if (dataTypeInt == DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED) {
			return "FOUR BYTE INT UNSIGNED SWAPPED";
		}		
		else if (dataTypeInt == DataType.FOUR_BYTE_FLOAT) {
			return "FOUR BYTE FLOAT";
		}
		else if (dataTypeInt == DataType.FOUR_BYTE_FLOAT_SWAPPED) {
			return "FOUR BYTE FLOAT SWAPPED";
		}		
		else if (dataTypeInt == DataType.EIGHT_BYTE_INT_SIGNED) {
			return "EIGHT BYTE INT SIGNED";
		}
		else if (dataTypeInt == DataType.EIGHT_BYTE_FLOAT) {
			return "EIGHT BYTE FLOAT";
		}

		return dataType;
	}
	
	/** 디버그용으로 쓰이는 패킷의 값을 return 한다. */
    public static String getPacketString(byte[] packet, int offset, int len) {
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
	
}