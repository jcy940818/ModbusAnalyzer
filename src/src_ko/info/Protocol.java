package src_ko.info;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import src_ko.database.DbUtil;
import src_ko.util.FileUtil;
import src_ko.util.ProtocolDownloader;

public class Protocol implements Comparable {
	
	// 모든 시설물에 포함되는 프로토콜
	public static final int PROTOCOL_MODBUS_RTU = 997;
	public static final int PROTOCOL_MODBUS_TCP = 998;
	public static final int PROTOCOL_CONTACT = 999;
	public static final int PROTOCOL_VIRTUAL = 1000;
	public static final int PROTOCOL_BACNET = 1002;
	
	public static final int PROTOCOL = 0;
	public static final int SNMP = 1;
	
	private int protocolType; // common or snmp
	private int facCode; // 시설물 코드
	private String facType; // 시설물 종류
	private int number; // 프로토콜 번호
	private String name; // 프로토콜 이름	
	private String xml; // 성능 XML
	private String controlXml; // 제어 XML
	
	private String enName; // 프로토콜 영문 이름
	private String enumKey; // 프로토콜 Enum Key
	
	public Protocol() {
		
	}

	public Protocol(int protocolType, int facCode, int protocolNumber, String name_ko, String name_en, String xml, String controlXml) {
		this.setProtocolType(protocolType);					
		this.setFacCode(facCode);
		this.setNumber(protocolNumber);
		this.setName(name_ko);
		this.setEnName(name_en);
		this.setXml(xml);
		this.setControlXml(controlXml);
	}
	
	public int getProtocolType() {
		return protocolType;
	}
	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}
	public int getFacCode() {
		return facCode;
	}
	public void setFacCode(int facCode) {
		this.facCode = facCode;
		this.facType = DbUtil.getFacilityType(facCode);
	}
	public String getFacType() {
		return facType;
	}
	public void setFacType(String facType) {
		this.facType = facType;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public String getControlXml() {
		return controlXml;
	}
	public void setControlXml(String controlXml) {
		this.controlXml = controlXml;
	}
	
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public String getEnumKey() {
		return enumKey;
	}
	public void setEnumKey(String enumKey) {
		this.enumKey = enumKey;
	}
	
	
	@Override
	public int compareTo(Object obj) {
		Protocol p = (Protocol) obj;
		
		if(this.getProtocolType() < p.getProtocolType()) {
			return -1;
		}else if(this.getProtocolType() == p.getProtocolType()) {
			// 프로토콜 타입이 동일 할 경우
			
			if(this.getFacCode() < p.getFacCode()) {
				return -1;
			}else if(this.getFacCode() == p.getFacCode()) {
				// 시설물 종류가 동일 할 경우
				
				if(this.getFacType().compareTo(p.getFacType()) < 0) {
					return -1;
				}else if(this.getFacType().compareTo(p.getFacType()) == 0) {
					// 시설물 타입이 동일 할 경우
					
					if(this.getNumber() < p.getNumber()) {
						return -1;
					}else if(this.getNumber() == p.getNumber()) {
						// 프로토콜 번호가 동일 할 경우
						return 0;
					}else {
						return 1;
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

	
	public String toString() {
		return "ProtocolType : " + (this.getProtocolType() == Protocol.PROTOCOL ? "PROTOCOL" : "SNMP")
				+" | FacCode : " + this.getFacCode()
				+" | FacType : " + this.getFacType()
				+" | ProtocolNum : " + this.getNumber()
				+" | Name : " + this.getName()
				+" | EnName : " + this.getEnName()
				+" | XML : " + this.getXml()
				+" | ControlXML : " + this.getControlXml();
	}
	
	
	
	// VersionInfo Map 파싱
	public static HashMap<String, String> getVersionInfoMap(String parsedVersionInfo) throws IOException{
		HashMap<String, String> map = new HashMap<String, String>();
		String separator = "String";
		Scanner sc = new Scanner(parsedVersionInfo);
		
		while(sc.hasNextLine()) {
			try {
				String line = sc.nextLine();	
				String key = null;
				String value = null;
				
				if(line.contains("public static final String")) {
					key = line.split(" = ")[0].split("String")[1].trim();
					value = line.split(" = ")[1].replace("\"", "").replace(";", "").trim();
					map.put(key, value);
				}else {
					continue;
				}
				
			}catch(Exception e) {
				e.printStackTrace();
				continue;
			}
		}// while
		
		return map;
	}
	
	
	// 4.2 SystemConfig 파싱
	public static String parseSystemConfig(String systemConfig) throws IOException{
		return systemConfig.split("private static final FmsProtocolInfo")[1]
					.split("private static final TpmMibInfo printerMibs")[0]
					.replace(", new FmsProtocolInfo", "\nnew FmsProtocolInfo")
					.replace(", new FmsMibInfo", "\nnew FmsMibInfo")
					.replace("        ", "");
	}
	
	
	// 4.2 SystemConfig : Protocol List 파싱
	public static ArrayList<Protocol> getProtocolList(String protocolConfig){
		ArrayList<Protocol> list = new ArrayList<Protocol>();
		HashMap<Integer, CommonProtocol> cpCheckMap = new HashMap<Integer, CommonProtocol>();
		
		Scanner sc = new Scanner(protocolConfig);
		
		while(sc.hasNextLine()) {			
			try {
				Protocol p = null;				
				String line = sc.nextLine();
				
				if(line.contains("new FmsProtocol") || line.contains("new FmsProtocolInfo")) {
					p = new Protocol(); 
					p.setProtocolType(Protocol.PROTOCOL);
				}else if(line.contains("new FmsMib") || line.contains("new FmsMibInfo")) {
					p = new Protocol();
					p.setProtocolType(Protocol.SNMP);
				}else{
					continue;
				}
				
				String[] token = line.split(",");
				String[] token2 = line.split(", \"");
				
				int facCode = Integer.parseInt(token[0].split("\\(")[1].trim());
				int number = Integer.parseInt(token[1].trim());
				String name = token2[1].replace("\"", "");		
								
				String xml = null;
				try {
					xml = token2[3].split("/")[1].split(".xml")[0].trim() + ".xml"; 				
				}catch(Exception e) {
					xml = "-";
				}
				
				String controlXml = null;
				try {
					if(token2[3].contains("\", null")) {
						controlXml = "-";
					} else {
						controlXml = token2[4].split("/")[1].split(".xml")[0].trim() + ".xml";
					}
				}catch(Exception e) {
					controlXml = "-";
				}
				
				p.setFacCode(facCode);
				p.setNumber(number);
				p.setName(name);
				p.setXml(xml);
				p.setControlXml(controlXml);
				list.add(p);
				
				// Common Protocol
				if( p.getProtocolType() == Protocol.PROTOCOL &&
					(number == PROTOCOL_MODBUS_RTU || number == PROTOCOL_MODBUS_TCP || number == PROTOCOL_CONTACT || number == PROTOCOL_VIRTUAL || number == PROTOCOL_BACNET) ){					
					if(cpCheckMap.containsKey(p.getFacCode())) {
						CommonProtocol cp = cpCheckMap.get(p.getFacCode());
						cp.haveProtocol(number);
					}else {
						CommonProtocol cp = new CommonProtocol();
						cp.setFacType(p.getFacCode());
						cp.haveProtocol(number);
						cpCheckMap.put(p.getFacCode(), cp);
					}
				}
				
			}catch(Exception e) {
				e.printStackTrace();
				continue;
			}
		}// While
		
		return CommonProtocol.generateCommonProtocol(list, cpCheckMap);
	}
	
	
	// 4.5 FmsProtocol : Protocol List 파싱
	public static ArrayList<Protocol> getFmsProtocolList(String fmsProtocol, String enumKo, String enumEn) throws IOException{
		ArrayList<Protocol> list = new ArrayList<Protocol>();
		HashMap<Integer, CommonProtocol> cpCheckMap = new HashMap<Integer, CommonProtocol>();
		
		Scanner sc = new Scanner(fmsProtocol);
		
		while(sc.hasNextLine()) {
			try {
				Protocol p = null;
				String line = sc.nextLine();
								
				if(line.contains("= new FmsProtocol(")) {
					p = new Protocol(); 
					p.setProtocolType(Protocol.PROTOCOL);
				}else {
					continue;
				}
				
				String[] token = line.split(",");
								
				String enumKey = line.split("= new FmsProtocol")[0].trim();
				String facType = token[2].trim();
				int number = Integer.parseInt(token[3].trim());
				String name = getEnumValue(enumKo, "enum.FmsProtocol." + enumKey);
				String enName = getEnumValue(enumEn, "enum.FmsProtocol." + enumKey);
				
				int xmlIndex = (token.length >= 10) ? (token.length - 3) : 6;
				int controlXmlIndex = (token.length >= 10) ? (token.length - 2) : 7;
				
				String xml = null;
				try {
					xml = token[xmlIndex].split("/")[1].split(".xml")[0].trim() + ".xml";
				}catch(Exception e) {
					xml = "-";
				}
								
				String controlXml = null;
				try {
					if(token[controlXmlIndex].contains("\", null")) {
						controlXml = "-";
					} else {
						controlXml = token[controlXmlIndex].split("/")[1].split(".xml")[0].trim() + ".xml";
					}
				}catch(Exception e) {
					controlXml = "-";
				}
				
				p.setEnumKey(enumKey);
				p.setFacCode(DbUtil.getFacilityCode(facType));
				// p.setFacType(facType);
				p.setNumber(number);
				p.setName(name);
				p.setEnName(enName);
				p.setXml(xml);
				p.setControlXml(controlXml);
				list.add(p);
				 
				// Common Protocol
				if( p.getProtocolType() == Protocol.PROTOCOL &&
					(number == PROTOCOL_MODBUS_RTU || number == PROTOCOL_MODBUS_TCP || number == PROTOCOL_CONTACT || number == PROTOCOL_VIRTUAL || number == PROTOCOL_BACNET) ){					
					if(cpCheckMap.containsKey(p.getFacCode())) {
						CommonProtocol cp = cpCheckMap.get(p.getFacCode());
						cp.haveProtocol(number);
					}else {
						CommonProtocol cp = new CommonProtocol();
						cp.setFacType(p.getFacCode());
						cp.haveProtocol(number);
						cpCheckMap.put(p.getFacCode(), cp);
					}
				}
				
			}catch(Exception e) {
				e.printStackTrace();
				continue;
			}
		}// while
		
		return CommonProtocol.generateCommonProtocol(list, cpCheckMap);
	}
	
	
	// 4.5 FmsMib : SNMP Protocol List 파싱
	public static ArrayList<Protocol> getFmsMibList(String fmsMib, String enumKo, String enumEn) throws IOException{
		ArrayList<Protocol> list = new ArrayList<Protocol>();
		
		Scanner sc = new Scanner(fmsMib);
		
		while(sc.hasNextLine()) {
			try {
				Protocol p = null;
				String line = sc.nextLine();
								
				if(line.contains("= new FmsMib(")) {
					p = new Protocol(); 
					p.setProtocolType(Protocol.SNMP);
				}else {
					continue;
				}
				
				String[] token = line.split(",");
								
				String enumKey = line.split("= new FmsMib")[0].trim(); 
				String facType = token[2].trim(); 
				int number = Integer.parseInt(token[3].trim());
				String name = getEnumValue(enumKo, "enum.FmsMib." + enumKey);
				String enName = getEnumValue(enumEn, "enum.FmsMib." + enumKey);
				
				String xml = null;
				try {
					xml = token[6].split("/")[1].split(".xml")[0].trim() + ".xml"; 				
				}catch(Exception e) {
					xml = "-";
				}									
								
				String controlXml = null;
				try {
					if(token[7].contains("\", null")) {
						controlXml = "-";
					} else {
						controlXml = token[7].split("/")[1].split(".xml")[0].trim() + ".xml";
					}
				}catch(Exception e) {
					controlXml = "-";
				}
				
				p.setEnumKey(enumKey);
				p.setFacCode(DbUtil.getFacilityCode(facType));
//				p.setFacType(facType);
				p.setNumber(number);
				p.setName(name);
				p.setEnName(enName);
				p.setXml(xml);
				p.setControlXml(controlXml);
				list.add(p);
								
			}catch(Exception e) {
				e.printStackTrace();
				continue;
			}
		}// while
				
		return list;
	}
		
	
	public static String getEnumValue(String properties, String enumKey) {
		Scanner sc = new Scanner(properties);		
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			if(line.contains(enumKey)) {				
				String actualKey = line.split("=")[0].trim();				
				if(enumKey.equalsIgnoreCase(actualKey)) {
					return line.split("=")[1].trim();	
				}else {
					continue;
				}				
			}else {
				continue;
			}
		}
		return null;
	}
	
	// 4.2 Protocol List Table
	public static JTable getProtocolTable(ArrayList<Protocol> protocolList) {
		JTable table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] {
				"프로토콜 타입",
				"시설물 코드",
				"시설물 종류",
				"프로토콜 번호",
				"프로토콜 이름",
				"성능 XML",
				"제어 XML"
		}));
		
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			for(int i = 0; i < protocolList.size(); i++) {
				Protocol p = protocolList.get(i);				
				record = new Vector();	
				
				/* column[0] */ record.add((p.getProtocolType() == Protocol.PROTOCOL ? "PROTOCOL" : "SNMP")); // 프로토콜 타입
				/* column[1] */ record.add(p.getFacCode()); // 시설물 코드
				/* column[2] */ record.add(p.getFacType());  // 시설물 종류
				/* column[3] */ record.add(p.getNumber());  // 프로토콜 번호
				/* column[4] */ record.add(p.getName()); // 프로토콜 이름
				/* column[5] */ record.add(p.getXml()); // 성능 XML
				/* column[6] */ record.add(p.getControlXml()); // 제어 XML
				
				model.addRow(record);					
			}
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();
		}
		
		return table;
	}
	
	
	// 4.5 Protocol List Table
	public static JTable getProtocolTable(ArrayList<Protocol> protocolList, ArrayList<Protocol> mibList) {
		
		for(int i = 0; i < mibList.size(); i++) {
			protocolList.add(mibList.get(i));
		}
		
		JTable table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] {
				"프로토콜 타입",
				"시설물 코드",
				"시설물 종류",
				"프로토콜 번호",
				"프로토콜 이름 (한글)",
				"프로토콜 이름 (영문)",
				"성능 XML",
				"제어 XML"
		}));
		
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			for(int i = 0; i < protocolList.size(); i++) {
				Protocol p = protocolList.get(i);
				record = new Vector();
				
				/* column[0] */ record.add((p.getProtocolType() == Protocol.PROTOCOL ? "PROTOCOL" : "SNMP")); // 프로토콜 타입
				/* column[1] */ record.add(p.getFacCode()); // 시설물 코드
				/* column[2] */ record.add(p.getFacType());  // 시설물 종류
				/* column[3] */ record.add(p.getNumber());  // 프로토콜 번호
				/* column[4] */ record.add(p.getName()); // 프로토콜 이름 (한글)
				/* column[5] */ record.add(p.getEnName()); // 프로토콜 이름 (영문)
				/* column[6] */ record.add(p.getXml()); // 성능 XML
				/* column[7] */ record.add(p.getControlXml()); // 제어 XML
				
				model.addRow(record);					
			}
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();
		}
		
		return table;
	}
	
	
	// versionInfo 버전의 systemConfig 프로토콜 리스트 다운로드
	public static void download_SystemConfig(File versionInfoJava, File systemConfigJava, String path) {
		try {
			
			// VersionInfo 파싱
			HashMap<String, String> map = getVersionInfoMap(FileUtil.getFileContent(versionInfoJava, "euc-kr"));			
			String versionInfo  = map.get(ONION_Info.PRODUCT_VERSION_FULL) + map.get(ONION_Info.BUILD_DATE) + " 프로토콜";
			versionInfo = versionInfo.replace("build", "Build");
			
			// SystemConfig 파싱
			ArrayList<Protocol> protocolList = getProtocolList(parseSystemConfig(FileUtil.getFileContent(systemConfigJava, "euc-kr")));
			Collections.sort(protocolList);
			JTable table = getProtocolTable(protocolList);
										
			ProtocolDownloader loder = new ProtocolDownloader(versionInfo, table, path, versionInfo, false);
			loder.start();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	// versionInfo 버전의 fmsProtocol, fmsMib 프로토콜 리스트 다운로드
	public static void download_FmsProtocol(File versionInfoJava, File fmsProtocolJava, File fmsMibJava, File enumKoProperty, File enumEnProperty, String path) {
		try {			
			
			// VersionInfo 파싱
			HashMap<String, String> map = getVersionInfoMap(FileUtil.getFileContent(versionInfoJava, "euc-kr"));			
			String versionInfo  = map.get(ONION_Info.PRODUCT_VERSION_FULL) + map.get(ONION_Info.BUILD_DATE) + " 프로토콜";				
			versionInfo = versionInfo.replace("build", "Build");
			
			String fmsProtocol = FileUtil.getFileContent(fmsProtocolJava, "euc-kr");
			String fmsMib = FileUtil.getFileContent(fmsMibJava, "euc-kr");
			String enumKo = FileUtil.convertString(FileUtil.getFileContent(enumKoProperty, "euc-kr"));
			String enumEn = FileUtil.convertString(FileUtil.getFileContent(enumEnProperty, "euc-kr"));

			// FmsProtocol 파싱
			ArrayList<Protocol> fmsProtocolList = getFmsProtocolList(fmsProtocol, enumKo, enumEn);
			
			// FmsMib 파싱
			ArrayList<Protocol> fmsMibList = getFmsMibList(fmsMib, enumKo, enumEn);
			
			Collections.sort(fmsProtocolList);
			Collections.sort(fmsMibList);
			JTable table = getProtocolTable(fmsProtocolList, fmsMibList);						
			
			ProtocolDownloader loder = new ProtocolDownloader(versionInfo, table, path, versionInfo, true);
			loder.start();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// MK119 4.2 이하 프로토콜 리스트 리턴
	public static ArrayList<Protocol> getProtocolList_42(File systemConfigJava) {
		try {			
			// SystemConfig 파싱
			ArrayList<Protocol> protocolList = getProtocolList(parseSystemConfig(FileUtil.getFileContent(systemConfigJava, "euc-kr")));
			Collections.sort(protocolList);
			return protocolList;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	
	// MK119 4.5 이상 프로토콜 리스트 리턴
	public static ArrayList<Protocol> getProtocolList_45(File fmsProtocolJava, File fmsMibJava, File enumKoProperty, File enumEnProperty) {
		try {						
			String fmsProtocol = FileUtil.getFileContent(fmsProtocolJava, "euc-kr");
			String fmsMib = FileUtil.getFileContent(fmsMibJava, "euc-kr");
			String enumKo = FileUtil.convertString(FileUtil.getFileContent(enumKoProperty, "euc-kr"));
			String enumEn = FileUtil.convertString(FileUtil.getFileContent(enumEnProperty, "euc-kr"));

			// FmsProtocol 파싱
			ArrayList<Protocol> fmsProtocolList = getFmsProtocolList(fmsProtocol, enumKo, enumEn);
			
			// FmsMib 파싱
			ArrayList<Protocol> fmsMibList = getFmsMibList(fmsMib, enumKo, enumEn);

			Collections.sort(fmsProtocolList);
			Collections.sort(fmsMibList);
			
			for(int i = 0; i < fmsMibList.size(); i++) {
				fmsProtocolList.add(fmsMibList.get(i));
			}
			
			return fmsProtocolList;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

//*** [ 공통 프로토콜 검사 클래스 ] *************************************************************************************************
class CommonProtocol{
	int facType;
	boolean modbus_RTU = false;
	boolean modbus_TCP = false;
	boolean contact = false;
	boolean virtual = false;
	boolean bacnet = false;
	
	public int getFacType() { return facType; }
	public void setFacType(int facType) { this.facType = facType; }
	public boolean hasModbus_RTU() { return modbus_RTU; }
	public boolean hasModbus_TCP() { return modbus_TCP; }
	public boolean hasContact() { return contact; }
	public boolean hasVirtual() { return virtual; }
	public boolean hasBacnet() { return bacnet; }
	
	public void haveProtocol(int protocolNum) {
		switch(protocolNum) {
			case Protocol.PROTOCOL_MODBUS_RTU :
				this.modbus_RTU = true;
				break;
			case Protocol.PROTOCOL_MODBUS_TCP :
				this.modbus_TCP = true;
				break;
			case Protocol.PROTOCOL_CONTACT :
				this.contact = true;
				break;
			case Protocol.PROTOCOL_VIRTUAL :
				this.virtual = true;
				break;
			case Protocol.PROTOCOL_BACNET :
				this.bacnet = true;
				break;
		}
	}
	
	public static ArrayList<Protocol> generateCommonProtocol(ArrayList<Protocol> list, HashMap<Integer, CommonProtocol> map){
		ArrayList<Integer> facCodeList = DbUtil.getAllFacilityCodeList();
		
		for(int i = 0; i < facCodeList.size(); i++) {
			try {
				
			int facCode = facCodeList.get(i);
			CommonProtocol cp = map.get(facCode);
			
			if(cp == null) {
				// 프로토콜 정보 클래스에 존재하지 않아서 추가하지 못한 시설물
				Protocol p = null;
				
				p = new Protocol(Protocol.PROTOCOL, facCode, Protocol.PROTOCOL_MODBUS_RTU, "Modbus-RTU 연결 프로토콜", "Modbus-RTU Connection Protocol", "-", "-");
				list.add(p);
				
				p = new Protocol(Protocol.PROTOCOL, facCode, Protocol.PROTOCOL_MODBUS_TCP, "Modbus-TCP 연결 프로토콜", "Modbus-TCP Connection Protocol", "-", "-");
				list.add(p);
				
				p = new Protocol(Protocol.PROTOCOL, facCode, Protocol.PROTOCOL_CONTACT, "접점 연결 프로토콜", "Contact Point Connection Protocol", "point.xml", "-");
				list.add(p);
				
				p = new Protocol(Protocol.PROTOCOL, facCode, Protocol.PROTOCOL_VIRTUAL, "가상 연결 프로토콜", "Virtual Connection Protocol", "point.xml", "-");
				list.add(p);
				
				p = new Protocol(Protocol.PROTOCOL, facCode, Protocol.PROTOCOL_BACNET, "BACnet 연결 프로토콜", "BACnet Connection Protocol", "-", "-");
				list.add(p);
			}else {
				// 프로토콜 정보 클래스에 존재하는 시설물 일 경우
				if(!cp.hasModbus_RTU()) {
					Protocol p = new Protocol(Protocol.PROTOCOL, cp.getFacType(), Protocol.PROTOCOL_MODBUS_RTU, "Modbus-RTU 연결 프로토콜", "Modbus-RTU Connection Protocol", "-", "-");
					list.add(p);
				}
				if(!cp.hasModbus_TCP()) {
					Protocol p = new Protocol(Protocol.PROTOCOL, cp.getFacType(), Protocol.PROTOCOL_MODBUS_TCP, "Modbus-TCP 연결 프로토콜", "Modbus-TCP Connection Protocol", "-", "-");
					list.add(p);
				}
				if(!cp.hasContact()) {
					Protocol p = new Protocol(Protocol.PROTOCOL, cp.getFacType(), Protocol.PROTOCOL_CONTACT, "접점 연결 프로토콜", "Contact Point Connection Protocol", "point.xml", "-");
					list.add(p);
				}
				if(!cp.hasVirtual()) {
					Protocol p = new Protocol(Protocol.PROTOCOL, cp.getFacType(), Protocol.PROTOCOL_VIRTUAL, "가상 연결 프로토콜", "Virtual Connection Protocol", "point.xml", "-");
					list.add(p);
				}
				if(!cp.hasBacnet()) {
					Protocol p = new Protocol(Protocol.PROTOCOL, cp.getFacType(), Protocol.PROTOCOL_BACNET, "BACnet 연결 프로토콜", "BACnet Connection Protocol", "-", "-");
					list.add(p);
				}
			}				
			
			}catch(Exception e) {
				e.printStackTrace();
				continue;
			}
		}// End for-Loop
		
		return list;
	}
	
}
// ****************************************************************************************************
