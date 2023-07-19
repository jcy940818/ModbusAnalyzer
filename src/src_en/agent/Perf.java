package src_en.agent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import src_en.analyzer.RX.DataType;
import src_en.info.TX_Info;
import src_en.swing.ModbusAgent_Panel;
import src_en.util.Util;

public class Perf implements Comparable{
	
	// MK119 : ModbusMonitor Perf bean
	
	public Perf() {
		
	}
	
	public Perf(String functionCode, String registerAddress, String modbusAddress, String lastValue) {
		this.functionCode = functionCode;
		this.registerAddress = registerAddress;
		this.modbusAddress = modbusAddress;
		this.lastValue = lastValue;
	}
	
	private int index;
	private String MK119_DataType;
	private String perfCounter;
	private String jsonString;
	
	// XML Field ------------------------------
	private String displayName = "";
	private String counter = "";
	private int slot = 1;
	private String interval = "60";
	private String measure = "";
	private String scaleFunction = "x";
	private String dataFormat = "3";	
	private HashMap binaryMap = new HashMap();	
	private HashMap multiStatusMap = new HashMap();
	private Event event = new Event();
	
	// Control : 제어
	private ControlAction control;
	
	// Modbus Field ---------------------------
	private String unitId;
	private String functionCode;
	private String registerAddress;
	private String modbusAddress;
	private String dataType;
	private Object lastValue;
	
	// SNMP Field
	private String oid;
	
	// Custom Modbus Field -----------------------
	private TX_Info tx;
	private String customPerfCounter_HEX;
	private String customPerfCounter_DEC;
	private String customDataType;
	private int customSlot;
	
	
	@Override
	public int compareTo(Object obj) {		
		Perf perf = (Perf)obj;
		
		int A_functionCode = Integer.parseInt(this.getFunctionCode());
		int B_functionCode = Integer.parseInt(perf.getFunctionCode());
		
		int A_address = Integer.parseInt(this.getRegisterAddress().toLowerCase().replace("0x", ""), 16);
		int B_address = Integer.parseInt(perf.getRegisterAddress().toLowerCase().replace("0x", ""), 16);
		
		String A_scaleFunction = this.getScaleFunction();
		String B_scaleFunction = perf.getScaleFunction();
		
		if(A_functionCode < B_functionCode) {
			return -1;
		}else if(A_functionCode == B_functionCode) {
			// 기능코드가 동일한 경우
			
			if(A_address < B_address) {
				return -1;				
			}else if(A_address == B_address) {
				// 기능코드와 주소까지 동일한 경우				
				if(A_scaleFunction.length() < B_scaleFunction.length()) {
					return -1;
				}else if(A_scaleFunction.length() == B_scaleFunction.length()) {					
					try {
						if ((A_scaleFunction.contains(">>") && (B_scaleFunction.contains(">>")))) {
							int A_bit = Integer.parseInt(A_scaleFunction.split(">>")[1].split("\\)")[0]);
							int B_bit = Integer.parseInt(B_scaleFunction.split(">>")[1].split("\\)")[0]);
							
							if(A_bit < B_bit) {
								return -1;
							}else if(A_bit == B_bit) {
								return 0;
							}else {
								return 1;
							}							
						}else {
							return A_scaleFunction.compareToIgnoreCase(B_scaleFunction); 
						}						
					}catch(Exception e) {
						return A_scaleFunction.compareToIgnoreCase(B_scaleFunction);	
					}
				}else {
					return 1;
				}				
			}else {
				return 1;
			}			
		}else {
			return 1;
		}				
	}
	
	public static void initPerfCounter(boolean isModbusAddress, Perf... perfs) {
		if(isModbusAddress) {
			// 주소 표기 방식 : 모드버스 주소
			for(int i = 0; i < perfs.length; i++) {
				StringBuilder sb = new StringBuilder();
				sb.append(perfs[i].getFunctionCode());
				sb.append("_");
				sb.append(String.valueOf((Integer.parseInt(perfs[i].getModbusAddress()) % 10000)));
				sb.append("_");
				sb.append(perfs[i].getMK119_DataType());
				sb.append("\\\\{1}");
				perfs[i].setPerfCounter(sb.toString());
			}
		}else {
			// 주소 표기 방식 : 레지스터 주소
			for(int i = 0; i < perfs.length; i++) {
				StringBuilder sb = new StringBuilder();
				sb.append(perfs[i].getFunctionCode());
				sb.append("_");
				sb.append(perfs[i].getRegisterAddress());
				sb.append("_");
				sb.append(perfs[i].getMK119_DataType());
				sb.append("\\\\{1}");
				perfs[i].setPerfCounter(sb.toString());
			}
		}		
	}
	
	public static void convertCustomPerfCounter(boolean isModbusAddress, Perf... perfs) {
		if(isModbusAddress) {
			// 주소 표기 방식 : 모드버스 주소
			for(int i = 0; i < perfs.length; i++) {				
				perfs[i].setPerfCounter(perfs[i].getCustomPerfCounter_DEC());
			}
		}else {
			// 주소 표기 방식 : 레지스터 주소
			for(int i = 0; i < perfs.length; i++) {
				perfs[i].setPerfCounter(perfs[i].getCustomPerfCounter_HEX());
			}
		}		
	}
	
	
	public static String getTotalJSON(Perf...perfs) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i =0; i < perfs.length; i++) {
			sb.append(perfs[i].getJsonString() + ",");
		}
		sb.append("]");
		return sb.toString().replace("},]", "}]").replace("'","\"");
	}
	
	public static void parseJSON(boolean useAutoEvent, Perf...perfs) {
		for(int i = 0; i < perfs.length; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append("{");
			sb.append(String.format("'displayName':'%s',", perfs[i].getDisplayName()));
			sb.append(String.format("'enable':0,", null));
			sb.append(String.format("'counter':'%s',", perfs[i].getPerfCounter()));
			sb.append(String.format("'interval':%s,", perfs[i].getInterval()));
			sb.append(String.format("'measure':'%s',", perfs[i].getMeasure()));
			sb.append(String.format("'scaleFunc':'%s',", perfs[i].getScaleFunction()));
			sb.append(String.format("'dataFormat':%s,", perfs[i].getDataFormat()));
			
			if(perfs[i].getDataFormat().equals("1")) {
				sb.append(String.format("'label0':'%s',", perfs[i].getBinaryMap().get("0")));
				sb.append(String.format("'label1':'%s',", perfs[i].getBinaryMap().get("1")));
				sb.append(String.format("'perfLabels':[]", null));
			}else if(perfs[i].getDataFormat().equals("2")) {
				sb.append("'label0':'',");
				sb.append("'label1':'',");
				sb.append(String.format("'perfLabels':%s", Perf.parseMultiStatusJSON(perfs[i].getMultiStatusMap())));
			}else if(perfs[i].getDataFormat().equals("3")) {
				sb.append("'label0':'',");
				sb.append("'label1':'',");
				sb.append(String.format("'perfLabels':[]", null));
			}
			
			if(useAutoEvent) {
				sb.append(",'event':{");				
				sb.append(String.format("'name':'%s',", perfs[i].event.getPerfEventName()));
				sb.append(String.format("'severity':%s,", Event.severity));
				sb.append(String.format("'threshold':%s,", Event.threshold));				
				sb.append(String.format("'op':'%s',", Event.op));				
				sb.append(String.format("'mode':%s,", Event.mode));				
				sb.append(String.format("'duration':%s,", Event.duration));				
				sb.append(String.format("'count':%s,", Event.count));				
				sb.append(String.format("'autoReg':%s,", Event.autoReg.toLowerCase()));				
				sb.append(String.format("'message':'%s',", perfs[i].event.getPerfEventMessage()));				
				sb.append(String.format("'enable':%s,", Event.enable));				
				sb.append("'threshold2':0,");
				sb.append(String.format("'autoClose':%s", Event.autoClose.toLowerCase()));				
				sb.append("}");
			}
			sb.append("}");			
			
			perfs[i].setJsonString(sb.toString());
		}
	}
	
	public static void initPerfEvent(Perf...perfs) {
		for(int i = 0; i < perfs.length; i++) {
			perfs[i].event.setPerfEventName(perfs[i].getDisplayName() + " " + Event.name);
			perfs[i].event.setPerfEventMessage(Event.message.replace("\n", "\\r\\n"));
		}
	}
	
	public static Perf[] getModbusPerfs(JTable table, int[] index) {			
		if((table.getValueAt(0, 0) == null)
			||(table.getRowCount() <= 0)
			||(index.length < 0)) {			
			return null;
		}
		 		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		Vector vector = model.getDataVector();
		
		Perf[] perfs = new Perf[index.length];		
		
		for(int i = 0; i < index.length; i++) {
			Vector data = (Vector)vector.get(index[i]);								
			perfs[i] = new Perf();
			perfs[i].setFunctionCode(String.valueOf(ModbusAgent.lastFunctionCode)); // 기능 코드
			perfs[i].setRegisterAddress(String.valueOf(data.get(1))); // 레지스터 주소
			perfs[i].setModbusAddress(String.valueOf(data.get(2))); // 모드버스 주소
			perfs[i].setLastValue(String.valueOf(data.get(3))); // 마지막 수집 값
			
			// 데이터 타입 : 기능 코드 1,2 라면 무조건 BINARY ( ON/OFF 상태만 표시하기 때문에 )
			if(ModbusAgent.lastFunctionCode == 1 || ModbusAgent.lastFunctionCode == 2) {
				perfs[i].setDataType("BINARY");
				perfs[i].setDataFormat("1"); // 데이터 형식 : 이진 데이터
				perfs[i].binaryMap.put("0", "OFF");
				perfs[i].binaryMap.put("1", "ON");
				perfs[i].setLastValue(String.valueOf(data.get(3).equals("ON") ? "1" : "0"));
			}else {
				String dataType = ModbusAgent_Panel.dataTypeComboBox.getSelectedItem().toString();
				
				if(dataType.equalsIgnoreCase("BINARY")) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Modbus Validation Exception</font>\n");
					sb.append(String.format("<font color='blue'>BINARY</font> Data type represents ON/OFF status%s%s\n\n", Util.separator, Util.separator));					
					sb.append(String.format("Therefore, you can only use items that use function codes 1 and 2%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("The currently selected Data type : <font color='red'>%s</font>%s\n",dataType ,Util.separator));
					sb.append(String.format("The currently selected Function Code : <font color='red'>%s</font>%s\n",perfs[i].getFunctionCode() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);										
					return null;
				}else if(!DataType.typeMap.containsKey(dataType)) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Modbus Collection Exception</font>\n");
					sb.append(String.format("The data type you selected is not supported by that function%s%s\n\n", Util.separator, Util.separator));					
					
					sb.append(String.format("<font color='blue'>Data type items supported by the function</font>%s\n", Util.separator));					
					sb.append(String.format("1. %s%s%s\n","BINARY" , Util.separator, Util.separator));				
					sb.append(String.format("2. %s%s%s\n","TWO BYTE INT SIGNED" , Util.separator, Util.separator));
					sb.append(String.format("3. %s%s%s\n","TWO BYTE INT UNSIGNED" , Util.separator, Util.separator));
					sb.append(String.format("4. %s%s%s\n","FOUR BYTE INT SIGNED (A B C D)" , Util.separator, Util.separator));
					sb.append(String.format("5. %s%s%s\n","FOUR BYTE INT SIGNED (C D A B)" , Util.separator, Util.separator));
					sb.append(String.format("6. %s%s%s\n","FOUR BYTE INT UNSIGNED (A B C D)" , Util.separator, Util.separator));
					sb.append(String.format("7. %s%s%s\n","FOUR BYTE INT UNSIGNED (C D A B)" , Util.separator, Util.separator));
					sb.append(String.format("8. %s%s%s\n","FOUR BYTE FLOAT (A B C D)" , Util.separator, Util.separator));
					sb.append(String.format("9. %s%s%s\n","FOUR BYTE FLOAT (C D A B)" , Util.separator, Util.separator));
					sb.append(String.format("10. %s%s%s\n","EIGHT BYTE INT SIGNED (A B C D)" , Util.separator, Util.separator));
					sb.append(String.format("11. %s%s%s\n","EIGHT BYTE DOUBLE (A B C D)" , Util.separator, Util.separator));
					
					sb.append(String.format("\nThe current data type you chose : <font color='red'>%s</font>%s\n",dataType ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);					
					return null;
				}
				
				perfs[i].setDataType(dataType); // 데이터 타입
				perfs[i].setDataFormat("3"); // 데이터 형식 : 성능 데이터
			}
			
			perfs[i].setInterval("60"); // 수집주기 ( 기본 : 60초 )
			perfs[i].setMeasure(""); // 단 위
			perfs[i].setScaleFunction("x"); // 보정식 ( 기본 : x )			
		}
		
		// Modbus RTU 타입의 경우 CRC 정보 행을 제외하고 리턴
		Vector data = null;
		try {
			data = (Vector)vector.get(index[index.length - 1]);
		}catch(ArrayIndexOutOfBoundsException e) {
			StringBuilder sb = new StringBuilder();
			sb.append("<font color='red'>Modbus Client Exception</font>\n");
			sb.append(String.format("Please select what you want to add as performance"
					+ Util.separator + Util.separator 
					+ "\n\nin the Modbus-Client result table%s\n", Util.separator));			
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);						
			return null;
		}
		
		if(data.get(0).toString().contains("CRC"))
			perfs = Arrays.copyOfRange(perfs, 0, perfs.length - 1);
				
		// 유효하지 않은 값을 가진 레지스터 정보는 버린다
		int invalidPerfCount = 0;
		for(int i = 0; i < perfs.length; i++) {
			if(perfs[i].getLastValue().toString().equalsIgnoreCase("---")) invalidPerfCount++;
		}
		
		Perf[] lastPerfs;
		
		if(invalidPerfCount >= 1) {
			lastPerfs = new Perf[perfs.length - invalidPerfCount];
			
			int j = 0;
			for(int i = 0; i < perfs.length; i++) {
				if(perfs[i].getLastValue().toString().equalsIgnoreCase("---")) continue;
				lastPerfs[j++] = perfs[i];				
			}
			
			return lastPerfs;
		}
		
		return perfs;
	}
	
	
	public static Perf[] getCustomModbusPerfs(JTable table, int[] index) {
		
		if((table.getValueAt(0, 0) == null)
			||(table.getRowCount() <= 0)
			||(index.length < 0)) {			
			return null;
		}
		 		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		Vector vector = model.getDataVector();
		
		Perf[] perfs = new Perf[index.length];		
		
		for(int i = 0; i < index.length; i++) {
			Vector data = (Vector)vector.get(index[i]);								
			perfs[i] = new Perf();
			perfs[i].setTx(ModbusAgent_Panel.global_tx);
			
			perfs[i].setFunctionCode(String.valueOf(ModbusAgent.lastFunctionCode)); // 기능 코드
			perfs[i].setRegisterAddress(String.valueOf(data.get(1))); // 레지스터 주소
			perfs[i].setModbusAddress(String.valueOf(data.get(2))); // 모드버스 주소
			perfs[i].setLastValue(String.valueOf(data.get(3))); // 마지막 수집 값
			
			// 데이터 타입 : 기능 코드 1,2 라면 무조건 BINARY ( ON/OFF 상태만 표시하기 때문에 )
			if(ModbusAgent.lastFunctionCode == 1 || ModbusAgent.lastFunctionCode == 2) {
				perfs[i].setDataType("BINARY");
				perfs[i].setCustomDataType("B");
				perfs[i].setDataFormat("1"); // 데이터 형식 : 이진 데이터
				perfs[i].binaryMap.put("0", "OFF");
				perfs[i].binaryMap.put("1", "ON");
				perfs[i].setLastValue(String.valueOf(data.get(3).equals("ON") ? "1" : "0"));
			}else {
				String dataType = ModbusAgent_Panel.dataTypeComboBox.getSelectedItem().toString();
				
				if(dataType.equalsIgnoreCase("BINARY")) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Custom Modbus DataType Exception</font>\n");
					sb.append(String.format("<font color='blue'>BINARY</font> Data type represents ON/OFF status%s%s\n\n", Util.separator, Util.separator));					
					sb.append(String.format("Therefore, you can only use items that use function codes 1 and 2%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("The currently selected Data type : <font color='red'>%s</font>%s\n",dataType ,Util.separator));
					sb.append(String.format("The currently selected Function Code : <font color='red'>%s</font>%s\n",perfs[i].getFunctionCode() ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);					
					return null;
				}else if(!DataType.customTypeMap.containsKey(dataType)) {
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Custom Modbus DataType Exception</font>\n");
					sb.append(String.format("The %s you selected is not supported by Custom Modbus XML Generator function%s%s\n\n", Util.colorBlue("Data Type"),Util.separator, Util.separator));										
					sb.append(String.format("The currently selected Data type : <font color='red'>%s</font>%s\n",dataType ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);					
					return null;
				}
				
				perfs[i].setDataType(dataType); // 데이터 타입
				perfs[i].setCustomDataType(DataType.customTypeMap.get(dataType));
				perfs[i].setDataFormat("3"); // 데이터 형식 : 성능 데이터
			}
			
			perfs[i].setInterval("60"); // 수집주기 ( 기본 : 60초 )
			perfs[i].setMeasure(""); // 단 위
			perfs[i].setScaleFunction("x"); // 보정식 ( 기본 : x )									
			
			perfs[i].setCustomPerfCounter_HEX(
					String.format("%s_%s_%s_%s\\{%d}", 
							perfs[i].getFunctionCode(),
							String.format("0x%04X", perfs[i].getTx().getStartAddress()),
							perfs[i].getTx().getRequestCount(),
							perfs[i].getCustomDataType(),
							ModbusAgent_Panel.slotMap.get(perfs[i].getRegisterAddress())							
							)
					);
			
			perfs[i].setCustomPerfCounter_DEC(
					String.format("%s_%s_%s_%s\\{%d}", 
							perfs[i].getFunctionCode(),
							String.format("%d", perfs[i].getTx().getStartAddress()),
							perfs[i].getTx().getRequestCount(),
							perfs[i].getCustomDataType(),
							ModbusAgent_Panel.slotMap.get(perfs[i].getRegisterAddress())							
							)
					);
		}
		
		// Modbus RTU 타입의 경우 CRC 정보 행을 제외하고 리턴
		Vector data = null;
		try {
			data = (Vector)vector.get(index[index.length - 1]);
		}catch(ArrayIndexOutOfBoundsException e) {
			StringBuilder sb = new StringBuilder();
			sb.append("<font color='red'>Custom Modbus XML Exception</font>\n");
			sb.append(String.format("Please select what you want to add as performance"
					+ Util.separator + Util.separator
					+ "\n\nin the Modbus-Agent result table%s\n", Util.separator));			
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if(data.get(0).toString().contains("CRC"))
			perfs = Arrays.copyOfRange(perfs, 0, perfs.length - 1);
				
		// 유효하지 않은 값을 가진 레지스터 정보는 버린다
		int invalidPerfCount = 0;
		for(int i = 0; i < perfs.length; i++) {
			if(perfs[i].getLastValue().toString().equalsIgnoreCase("---")) invalidPerfCount++;
		}
		
		Perf[] lastPerfs;
		
		if(invalidPerfCount >= 1) {
			lastPerfs = new Perf[perfs.length - invalidPerfCount];
			
			int j = 0;
			for(int i = 0; i < perfs.length; i++) {
				if(perfs[i].getLastValue().toString().equalsIgnoreCase("---")) continue;
				lastPerfs[j++] = perfs[i];				
			}
			
			return lastPerfs;
		}
		
		return perfs;
	}
	
	
	public static Perf createSnmpPerf(String oid) {		
		Perf perf = new Perf();
		perf.setDisplayName("");
		
		if(oid != null) {
			perf.setOid(oid);	
		}else {
			perf.setOid("");
		}
				
		perf.setInterval("60"); // 수집주기 ( 기본 : 60초 )
		perf.setMeasure(""); // 단 위
		perf.setScaleFunction("x"); // 보정식 ( 기본 : x )
		
		return perf;
	}
	
	
	public static Perf createCustomPerf(String perfInfo) {
		Perf perf = new Perf();
		
		// 기본 값
		perf.setDisplayName("");
		perf.setPerfCounter("");
		perf.setOid("");
		perf.setSlot(1);
		perf.setScaleFunction("x");
		perf.setMeasure("");
		perf.setInterval("60");
		
		String[] perfElements = perfInfo.split(",");
		
		
		for(String perfElement : perfElements) {
			
			try {
								
				String key = null; 
				String value = null; 
				
				if(!perfElement.contains("=")) {
					perf.setDisplayName(perfElement.trim());
					continue;
				}else {
					key = perfElement.split("=")[0].trim().toLowerCase();
					value = perfElement.split("=")[1].trim();	
				}
				
				if (key.equalsIgnoreCase("displayname") || key.contains("name")) {
					perf.setDisplayName((value != null) ? value : ""); // 성능명
					
				} else if (key.equalsIgnoreCase("counter") || key.equalsIgnoreCase("perfcounter") || key.contains("counter")) {
					perf.setPerfCounter((value != null) ? value : ""); // 성능 카운터
					
				} else if (key.equalsIgnoreCase("oid") || key.contains("oid")) {
					perf.setOid((value != null) ? value : ""); // SNMP OID
					
				} else if (key.equalsIgnoreCase("slot") || key.contains("slot")) {
					try {
						perf.setSlot((value != null) ? Integer.parseInt(value) : 1); // slot
					}catch(NumberFormatException e) {
						perf.setSlot(1); // slot
					}
				} else if (key.equalsIgnoreCase("interval") || key.contains("interval")) {
					perf.setInterval((value != null) ? value : "60"); // 수집주기
					
				} else if (key.equalsIgnoreCase("measure") || key.equalsIgnoreCase("units")) {
					perf.setMeasure((value != null) ? value : ""); // 성능 단위
					
				} else if (key.equalsIgnoreCase("scalefunction") || key.equalsIgnoreCase("expression") || key.contains("scale")) {
					perf.setScaleFunction((value != null) ? value : "x"); // 보정식
					
				} else if (key.equalsIgnoreCase("label0") || key.contains("0")) {
					perf.getBinaryMap().put("0", (value != null) ? value : ""); // 이진 상태 : 0
					perf.setDataFormat("1");
					
				} else if (key.equalsIgnoreCase("label1") || key.contains("1")) {
					perf.getBinaryMap().put("1", (value != null) ? value : ""); // 이진 상태 : 1
					perf.setDataFormat("1");
					
				} else if (key.equalsIgnoreCase("multistatus") || key.contains("multi") || key.contains("status")) {
					HashMap multiStatusMap = parseMultiStatusMap(value);				
					if(multiStatusMap != null) {
						perf.setMultiStatusMap(multiStatusMap);	
						perf.setDataFormat("2");
					}else {
						perf.setDataFormat("3");
					}				
				}		
			
			}catch(Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
		return perf;
	}
	
	
	public static Perf[] getSimpleModbusPerfs(JTable table, int[] index, String selectedDataType) {			
		if((table.getValueAt(0, 0) == null)
			||(table.getRowCount() <= 0)
			||(index.length < 0)) {			
			return null;
		}
		 		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		Vector vector = model.getDataVector();
		
		Perf[] perfs = new Perf[index.length];		
		
		for(int i = 0; i < index.length; i++) {			
			Vector data = (Vector)vector.get(index[i]);								
			perfs[i] = new Perf();
			perfs[i].setFunctionCode(String.valueOf(ModbusAgent.lastFunctionCode)); // 기능 코드
			perfs[i].setRegisterAddress(String.valueOf(data.get(1))); // 레지스터 주소
			perfs[i].setModbusAddress(String.valueOf(data.get(2))); // 모드버스 주소
			perfs[i].setLastValue(String.valueOf(data.get(3))); // 마지막 수집 값
			
			// 데이터 타입 : 기능 코드 1,2 라면 무조건 BINARY ( ON/OFF 상태만 표시하기 때문에 )
			if(ModbusAgent.lastFunctionCode == 1 || ModbusAgent.lastFunctionCode == 2) {
				perfs[i].setDataType("BINARY");
				perfs[i].setDataFormat("1"); // 데이터 형식 : 이진 데이터
				perfs[i].binaryMap.put("0", "OFF");
				perfs[i].binaryMap.put("1", "ON");
				perfs[i].setLastValue(String.valueOf(data.get(3).equals("ON") ? "1" : "0"));
			} else {
//				String dataType = ModbusAgent_Panel.dataTypeComboBox.getSelectedItem().toString();
				
				String dataType = selectedDataType;
				
				if(!dataType.contains("HEX") && !dataType.contains("TWO BYTE INT") && !dataType.contains("FOUR BYTE INT")){
					StringBuilder sb = new StringBuilder();
					sb.append("<font color='red'>Unsupported Data Type</font>\n");
					sb.append(String.format("The %s you selected is not supported by <font color='blue'>Bit Structure</font> function%s\n", Util.colorBlue("Data Type"), Util.separator));																				
					sb.append(String.format("\nThe currently selected Data type : <font color='red'>%s</font>%s\n",dataType ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				perfs[i].setDataType(dataType); // 데이터 타입
				perfs[i].setDataFormat("3"); // 데이터 형식 : 성능 데이터
			}
			
			perfs[i].setInterval("60"); // 수집주기 ( 기본 : 60초 )
			perfs[i].setMeasure(""); // 단 위
			perfs[i].setScaleFunction("x"); // 보정식 ( 기본 : x )			
		}
		
		// Modbus RTU 타입의 경우 CRC 정보 행을 제외하고 리턴
		Vector data = null;
		try {
			data = (Vector)vector.get(index[index.length - 1]);
		}catch(ArrayIndexOutOfBoundsException e) {
					
			return null;
		}
		
		if(data.get(0).toString().contains("CRC"))
			perfs = Arrays.copyOfRange(perfs, 0, perfs.length - 1);
				
		// 유효하지 않은 값을 가진 레지스터 정보는 버린다
		int invalidPerfCount = 0;
		for(int i = 0; i < perfs.length; i++) {
			if(perfs[i].getLastValue().toString().equalsIgnoreCase("---")) invalidPerfCount++;
		}
		
		Perf[] lastPerfs;
		
		if(invalidPerfCount >= 1) {
			lastPerfs = new Perf[perfs.length - invalidPerfCount];
			
			int j = 0;
			for(int i = 0; i < perfs.length; i++) {
				if(perfs[i].getLastValue().toString().equalsIgnoreCase("---")) continue;
				lastPerfs[j++] = perfs[i];				
			}
			
			return lastPerfs;
		}
		
		return perfs;
	}
	
	
	/**
	 * 전달받은 ModbusPerf 인스턴스의 비트 구조를 출력한다
	 */
	public static void showBitStatus(JTable table, int[] index, String selectedDataType) {
		Perf perf = null;
		
		try {
			perf = Perf.getSimpleModbusPerfs(table, index, selectedDataType)[0];
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("ModbusPerf : showBitStatus() Exception [ " + e.getMessage() + " ]");
			return;		
		}
				
		if(perf == null) {
			System.out.println("ModbusPerf : showBitStatus() -> ModbusPerf is null");
			return;		
		}else {
			int functionCode = Integer.parseInt(perf.getFunctionCode());
			String currentFunctionCode = null;
			
			switch(functionCode) {
				case 01 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC01); break;
				case 02 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC02); break;
				case 03 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC03); break;
				case 04 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC04); break;
				case 05 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC05); break;
				case 06 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC06); break;
				case 15 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC15); break;
				case 16 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC16); break;
			}
			
			if (!(functionCode == 3 || functionCode == 4)) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Unsupported Function Code</font>\n");
				sb.append(String.format("The %s you selected is not supported by <font color='blue'>Bit Structure</font> function%s\n", Util.colorBlue("Function Code"), Util.separator));																				
				sb.append(String.format("\nThe currently selected Function Code : <font color='red'>%s</font>%s\n",currentFunctionCode ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return;
			}
			
		}
		
		long lastValue = (perf.getDataType().equalsIgnoreCase("HEX")) ? 
				Integer.parseInt(perf.getLastValue().toString().replace("0x", ""),16) 
				:
				Long.parseLong(perf.getLastValue().toString());
		
		int bitCount = 16; // Default : TWO BYTE (16Bit)
		
		if (perf.getDataType().startsWith("TWO")) {
			bitCount = 16;
		} else if (perf.getDataType().startsWith("FOUR")) {
			bitCount = 32;
		} else {
			bitCount = 16;
		}
		 
		String dataType = perf.getDataType();
		
		StringBuilder bitBuilder = new StringBuilder();
		
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='green'>Bit Structure</font>\n");
		sb.append(String.format(" <font color='blue'>Data Type</font> : %s%s%s\n\n",dataType ,Util.separator, Util.separator));
		
		sb.append(String.format(" <font color='blue'>Modbus Address (DEC)</font> : %s%s%s\n",perf.getModbusAddress() ,Util.separator, Util.separator));
		sb.append(String.format(" <font color='blue'>Register Address (DEC)</font> : %d%s%s\n", Integer.parseInt(perf.getRegisterAddress().replace("0x", ""), 16)  ,Util.separator, Util.separator));
		sb.append(String.format(" <font color='blue'>Register Address (HEX)</font> : %s%s%s\n\n",perf.getRegisterAddress() ,Util.separator, Util.separator));
		
		sb.append(String.format(" <font color='blue'>Decimal</font> : %d%s%s\n",lastValue ,Util.separator, Util.separator));
		sb.append(String.format(" <font color='blue'>Hexadecimal</font> : 0x%04X%s%s\n",lastValue ,Util.separator, Util.separator));
		sb.append(String.format(" <font color='blue'>Binary</font> : "));
		for(int i = 0; i < bitCount; i++) {
			long bitStatus = (lastValue >> i) & 1;
			String content = (bitStatus == 1) ? "1" : "0";
			
			if (i % 4 == 0) {
				bitBuilder.append(String.format(" %s", content));
			}else{
				bitBuilder.append(String.format("%s", content));
			}						
		}						
		sb.append(String.format("%s%s%s\n\n", bitBuilder.reverse().toString().replaceAll("1", Util.colorRed("1")), Util.separator, Util.separator));
		
		for(int i = 0; i < bitCount; i++) {
			long bitStatus = (lastValue >> i) & 1;
			String content = (bitStatus == 1) ? "1 ( ON )" : "0 ( OFF )";
			
			if(content.contains("ON")) {
				sb.append(String.format("<font color='red'>BIT %d : %s%s%s</font>\n", i , content , Util.separator, Util.separator));	
			}else {
				sb.append(String.format("BIT %d : %s%s%s\n", i , content , Util.separator, Util.separator));
			}						
		}
		
		Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	public static String parseMultiStatusSring(HashMap map) {
		if(map == null || map.size() == 0) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		
		Set keys = map.keySet();
		Iterator it = keys.iterator();
		
		while(it.hasNext()) {
			String key = (String)it.next();
			sb.append(String.format("%s; ", key));
			sb.append(String.format("%s; ", map.get(key)));
		}
		
		return sb.toString();
	}
	
	
	public static String parseMultiStatusJSON(HashMap map) {
		if(map == null || map.size() == 0) {
			return "[]";
		}
		
		StringBuilder sb = new StringBuilder();
		
		Set keys = map.keySet();
		Iterator it = keys.iterator();
		
		sb.append("[");
		
		while(it.hasNext()) {
			String key = (String)it.next();
			sb.append("{");
			sb.append(String.format("'value':%s,", key));
			sb.append(String.format("'label':'%s'", map.get(key)));
			sb.append("},");
		}
		
		sb.append("]");
		
		return sb.toString().replace("},]", "}]");
	}
	
	
	/**
	 * 성능명을 인자로 받아서 성능명에 적절한 단위(Measure)를 리턴해준다
	 */
	public static String createMeasure(String perfName) {		
		String measure = "";
		
		try {
			
			// 전력 관련
			if(perfName.toLowerCase().contains("percent") || perfName.toLowerCase().contains("율") || perfName.toLowerCase().contains("률")
			|| perfName.toLowerCase().endsWith("율") || perfName.toLowerCase().endsWith("률")) {
					
					measure = "%"; // 백분율
					return measure;
					
			}else if(perfName.toLowerCase().contains("balance") || perfName.toLowerCase().contains("균형")  || perfName.toLowerCase().contains("밸런스") || perfName.toLowerCase().contains("벨런스")) {
				
				measure = "%"; // 균형율
				return measure;
				
			}else if(perfName.contains("THD") || perfName.toLowerCase().contains("왜곡율") || perfName.toLowerCase().contains("왜곡률")) {
				
				measure = "%"; // 고조파 왜곡율
				return measure;
				
			}else if(perfName.toLowerCase().endsWith("load")) {
				
				measure = "%"; // 부하율
				return measure;
				
			}else if(perfName.toLowerCase().contains("capacity") || perfName.toLowerCase().contains("용량")) {
				
				measure = "%"; // 용량
				return measure;
				
			}else if(perfName.toLowerCase().contains("resistance") || perfName.toLowerCase().contains("저항")) {
				
				measure = "Ω"; // 저항
				return measure;
				
			}else if(perfName.toLowerCase().contains("angle") || perfName.toLowerCase().contains("위상")) {
				
				measure = "°"; // 각도
				return measure;
				
			}else if(perfName.toLowerCase().contains("voltage") || perfName.toLowerCase().contains("전압")
//				|| perfName.toLowerCase().endsWith("va") || perfName.toLowerCase().endsWith("vb") || perfName.toLowerCase().endsWith("vc")
				|| perfName.toLowerCase().endsWith("vab") || perfName.toLowerCase().endsWith("vbc") || perfName.toLowerCase().endsWith("vca")
				|| perfName.toLowerCase().endsWith("vr")  || perfName.toLowerCase().endsWith("vs")  || perfName.toLowerCase().endsWith("vt")
				|| perfName.toLowerCase().endsWith("vrs") || perfName.toLowerCase().endsWith("vst") || perfName.toLowerCase().endsWith("vtr")
				|| perfName.toLowerCase().endsWith("v1")  || perfName.toLowerCase().endsWith("v2")  || perfName.toLowerCase().endsWith("v3")
				|| perfName.toLowerCase().endsWith("v12") || perfName.toLowerCase().endsWith("v23") || perfName.toLowerCase().endsWith("v31")) {
				
				measure = "V"; // 전압
				return measure;
				
			}else if(perfName.toLowerCase().contains("current") || perfName.toLowerCase().contains("amp") || perfName.toLowerCase().contains("전류")
				|| perfName.toLowerCase().endsWith("ia")  || perfName.toLowerCase().endsWith("ib")  || perfName.toLowerCase().endsWith("ic")
				|| perfName.toLowerCase().endsWith("iab") || perfName.toLowerCase().endsWith("ibc") || perfName.toLowerCase().endsWith("ica")
				|| perfName.toLowerCase().endsWith("ir")  || perfName.toLowerCase().endsWith("is")  || perfName.toLowerCase().endsWith("it")
				|| perfName.toLowerCase().endsWith("irs") || perfName.toLowerCase().endsWith("ist") || perfName.toLowerCase().endsWith("itr")
				|| perfName.toLowerCase().endsWith("i1")  || perfName.toLowerCase().endsWith("i2")  || perfName.toLowerCase().endsWith("i3")
				|| perfName.toLowerCase().endsWith("i12") || perfName.toLowerCase().endsWith("i23") || perfName.toLowerCase().endsWith("i31")) {
								
				measure = "A"; // 전류
				return measure;
				
			}else if(perfName.toLowerCase().contains("frequency") || perfName.toLowerCase().contains("주파수")) {
				
				measure = "Hz"; // 주파수
				return measure;
				
			}else if(perfName.toLowerCase().contains("reactive") || perfName.toLowerCase().contains("var") || perfName.toLowerCase().contains("무효")) {
				
				if(perfName.toLowerCase().contains("power") || perfName.toLowerCase().contains("kvar") || perfName.toLowerCase().contains("전력") || perfName.toLowerCase().endsWith("var"))
					measure = "kVAR"; // 무효 전력			
				if(perfName.toLowerCase().contains("energy") || perfName.toLowerCase().contains("kvarh") || perfName.toLowerCase().contains("전력량") || perfName.toLowerCase().endsWith("varh"))
					measure = "kVARh"; // 무효 전력량
				
				return measure;
				
			}else if(perfName.toLowerCase().contains("active") || perfName.toLowerCase().contains("kw") || perfName.toLowerCase().contains("유효")) {
				
				if(perfName.toLowerCase().contains("power") || perfName.toLowerCase().contains("kw")  || perfName.toLowerCase().contains("전력") || perfName.toLowerCase().endsWith("kw"))
					measure = "kW"; // 유효 전력
				if(perfName.toLowerCase().contains("energy") || perfName.toLowerCase().contains("kwh")  || perfName.toLowerCase().contains("전력량") || perfName.toLowerCase().endsWith("kwh"))
					measure = "kWh"; // 유효 전력량
				
				return measure;
				
			}else if(perfName.toLowerCase().contains("apparent") || perfName.toLowerCase().contains("va") || perfName.toLowerCase().contains("피상")) {
				
				if(perfName.toLowerCase().contains("power") || perfName.toLowerCase().contains("kva")  || perfName.toLowerCase().contains("전력") || perfName.toLowerCase().endsWith("va"))
					measure = "kVA"; // 피상 전력			
				if(perfName.toLowerCase().contains("energy") || perfName.toLowerCase().contains("kvah")  || perfName.toLowerCase().contains("전력량") || perfName.toLowerCase().endsWith("vah"))
					measure = "kVAh"; // 피상 전력량
				
				return measure;
				
			}else if(perfName.toLowerCase().contains("kwh")|| perfName.toLowerCase().endsWith("kwh") || perfName.toLowerCase().contains("전력량")) {
				
				measure = "kWh"; // 전력량
				return measure;
				
			}else if(perfName.toLowerCase().contains(" w") || perfName.toLowerCase().contains(" kw") || perfName.toLowerCase().contains("전력")) {
				
				measure = "kW"; // 전력
				return measure;
				
			}else if(perfName.toLowerCase().contains("power factor") || perfName.toLowerCase().contains("powerfactor") || perfName.toLowerCase().contains("pf") || perfName.toLowerCase().contains("factor") || perfName.toLowerCase().contains("역률")) {
				
				measure = "%"; // 역률
				return measure;
				
			}
			
			
			// 온습도 관련
			if(perfName.toLowerCase().contains("temp") || perfName.toLowerCase().contains("temperature") || perfName.toLowerCase().contains("온도")) {
				
				measure = "℃"; // 온도
				return measure;
				
			}else if(perfName.toLowerCase().contains("humi") || perfName.toLowerCase().contains("humidity") || perfName.toLowerCase().contains("습도")) {
				
				measure = "%"; // 습도
				return measure;
				
			}
			
		}catch(Exception e) {
			return "";
		}
		
		return measure;
	}
	
	
	public static HashMap parseMultiStatusMap(String pattern) {
		if(pattern == null || pattern.length() == 0 || pattern.equalsIgnoreCase("")) {
			return null;
		}
		
		HashMap multiStatusMap = new HashMap();		
		String[] tokens = pattern.replace("; ",";").split(";");
		
		if(tokens.length % 2 != 0) {
			// 다중 상태의 key, value 매핑이 정상적으로 되지 않음
			return null;
		}else {
			for(int i = 0; i < tokens.length; i+=2) {
				
				// inspect NumberFormatException : 다중 상태는 값-문자 형식으로 매핑되어야 한다
				Integer.parseInt(tokens[i].trim()); 
				
				multiStatusMap.put(tokens[i].trim(), tokens[i+1].trim());
			}
			return multiStatusMap;
		}
	}
	

	@Override
	public String toString() {
		return String.format("FunctionCode : %s  /  RegisterAddress : %s  /  ModbusAddress : %s  /  LastValue :  %s", functionCode, registerAddress, modbusAddress, lastValue);		
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public String getScaleFunction() {
		return scaleFunction;
	}

	public void setScaleFunction(String scaleFunction) {
		this.scaleFunction = scaleFunction;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public HashMap getBinaryMap() {
		return binaryMap;
	}

	public void setBinaryMap(HashMap binaryMap) {
		this.binaryMap = binaryMap;
	}

	public HashMap getMultiStatusMap() {
		return multiStatusMap;
	}

	public void setMultiStatusMap(HashMap multiStatusMap) {
		this.multiStatusMap = multiStatusMap;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	public String getRegisterAddress() {
		return registerAddress;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}

	public String getModbusAddress() {
		return modbusAddress;
	}

	public void setModbusAddress(String modbusAddress) {
		this.modbusAddress = modbusAddress;
	}

	public String getDataType() {
		return dataType;
	}

	public String getPerfCounter() {
		return perfCounter;
	}

	public void setPerfCounter(String perfCounter) {
		this.perfCounter = perfCounter;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getMK119_DataType() {
		return MK119_DataType;
	}

	public void setMK119_DataType(String mK119_DataType) {
		MK119_DataType = mK119_DataType;
	}

	public Object getLastValue() {
		return lastValue;
	}

	public void setLastValue(Object lastValue) {
		this.lastValue = lastValue;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public TX_Info getTx() {
		return tx;
	}

	public void setTx(TX_Info tx) {
		this.tx = tx;
	}
	
	public String getCustomDataType() {
		return customDataType;
	}

	public void setCustomDataType(String customDataType) {
		this.customDataType = customDataType;
	}

	public int getCustomSlot() {
		return customSlot;
	}

	public void setCustomSlot(int customSlot) {
		this.customSlot = customSlot;
	}

	public String getCustomPerfCounter_HEX() {
		return customPerfCounter_HEX;
	}

	public void setCustomPerfCounter_HEX(String customPerfCounter_HEX) {
		this.customPerfCounter_HEX = customPerfCounter_HEX;
	}

	public String getCustomPerfCounter_DEC() {
		return customPerfCounter_DEC;
	}

	public void setCustomPerfCounter_DEC(String customPerfCounter_DEC) {
		this.customPerfCounter_DEC = customPerfCounter_DEC;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public ControlAction getControl() {
		return control;
	}

	public void setControl(ControlAction control) {
		this.control = control;
	}	
	
	
}
