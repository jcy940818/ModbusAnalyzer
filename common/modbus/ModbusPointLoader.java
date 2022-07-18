package common.modbus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.perf.FmsPerfConf;
import common.perf.Perf;
import common.perf.PerfConf;
import common.perf.PerfLabelStatusBean;
import common.util.ExcelUtil;
import moon.Moon;
import src_ko.util.Util;

public class ModbusPointLoader {
	
	public static ArrayList<ModbusWatchPoint> load(int mkVersion, File file) {
		ArrayList<ModbusWatchPoint> modbusWps  = null;
		
		try {
			if(file != null && file.exists()) {

				// 인코딩 선택 해야함
				try {

					if(file.getAbsolutePath().toLowerCase().endsWith(".xml")) {
						// XML 업로드
						modbusWps = ModbusPointLoader.loadXmlV4(file);
						
					}else {
						// Excel 업로드
						
						if(mkVersion >= 10) {
							// MK119 V10 Excel 업로드
							modbusWps = ModbusPointLoader.loadExcelV10(file);
							
						}else {
							// MK119 V4 Excel 업로드
							modbusWps = ModbusPointLoader.loadExcelV4(file);
						}
					}
					
				}catch(ModbusPointInitException e) {
					modbusWps = null;
					e.printStackTrace();
					
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s\n", Util.colorRed("Modbus Watch Point Initialization Error")));
					
					if(Moon.isKorean()) {
						sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("포인트 이름"), e.getMessage(), Util.separator, Util.separator));
						sb.append(String.format("위의 포인트 정보를 초기화 하는중 오류가 발생하였습니다%s%s\n", Util.separator, Util.separator));	
					}else {
						sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("Point Name"), e.getMessage(), Util.separator, Util.separator));
						sb.append(String.format("An error occurred while initializing the above Modbus Point information%s%s\n", Util.separator, Util.separator));
					}
					
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
				}catch(IOException e) {
					modbusWps = null;
					e.printStackTrace();
					
					String[] info = e.getMessage().split(",");
					boolean hasPointName = !info[2].equalsIgnoreCase("null");
					
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s\n", Util.colorRed("Modbus Watch Point Initialization Error")));
					
					if(Moon.isKorean()) {
						sb.append(String.format("%s : %s%s%s\n", Util.colorBlue("행 번호"), info[0], Util.separator, Util.separator));
						sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("에러 필드"), info[1], Util.separator, Util.separator));	
					}else {
						sb.append(String.format("%s : %s%s%s\n", Util.colorBlue("Row Number"), info[0], Util.separator, Util.separator));
						sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("Error Field"), info[1], Util.separator, Util.separator));
					}
					
					if(hasPointName) {
						if(Moon.isKorean()) {
							sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("포인트 이름"), info[2], Util.separator, Util.separator));	
						}else {
							sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("Point Name"), info[2], Util.separator, Util.separator));
						}
					}
					
					if(Moon.isKorean()) {
						sb.append(String.format("%s번 행의 %s 필드 파싱 과정에서 에러가 발생하였습니다%s%s\n", 
								Util.colorRed(info[0]),							
								Util.colorRed(info[1]),
								Util.separator,
								Util.separator));	
					}else {
						sb.append(String.format("Error occurred during %s field parsing on row number %s%s%s\n", 
								Util.colorRed(info[1]),							
								Util.colorRed(info[0]),
								Util.separator,
								Util.separator));
					}
					
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);						
				}
			}
			
		} catch (Exception e) {
			modbusWps = null;
			e.printStackTrace();
			
		}finally {
			return modbusWps;
		}
	}
	
    
	
    public static ArrayList<ModbusWatchPoint> loadExcelV4(File xlsxFile) throws IOException, ModbusPointInitException{
    	
    	FileInputStream inputStream = null;
    	String item = "";
		Cell cell = null;
		ModbusWatchPoint modbusPoint = null;
    	
    	try {
			inputStream = new FileInputStream(xlsxFile);
			Workbook workbook = new XSSFWorkbook(inputStream);
			
			Sheet sheet = workbook.getSheetAt(0);
			ArrayList<ModbusWatchPoint> modbusWps = new ArrayList<ModbusWatchPoint>();
	
			int header = ExcelUtil.getHeaderRowNum(sheet, 1);
			if(header < 0) return null;
			int nullCount = 0;
			
			while(true) {
				if(nullCount > 100) break;
				
				int rowNum = ++header;
				Row row = sheet.getRow(rowNum);
				
				try {
					if(row == null) {
						nullCount++;
						continue;
					}else if(ExcelUtil.isNull(row.getCell(1))) {
						nullCount++;
						continue;
					}
					
					modbusPoint = new ModbusWatchPoint();
					modbusWps.add(modbusPoint);
					
					item = (Moon.isKorean()) ? "성능명" : "Point Name";
					cell = row.getCell(1);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					modbusPoint.displayName = ExcelUtil.getStringValue(cell);
					
					item = (Moon.isKorean()) ? "기능코드" : "Function Code";
					cell = row.getCell(2);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					int functionCode = ExcelUtil.getIntValue(cell);
					
					item = (Moon.isKorean()) ? "주소" : "Address";
					cell = row.getCell(3);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					String address = ExcelUtil.getStringValue(cell).toLowerCase().contains("0x") ? ExcelUtil.getStringValue(cell) : String.valueOf(ExcelUtil.getIntValue(cell));
					
					item = (Moon.isKorean()) ? "데이터 타입" : "Data Type";
					cell = row.getCell(4);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					String dataType = ExcelUtil.getStringValue(cell);
					
					String counter = functionCode + "_" + address + "_" + dataType;
					
					modbusPoint.counter = counter;
					
//					item = (Moon.isKorean()) ? "수집 주기" : "Interval";
//					cell = row.getCell(5);
//					modbusPoint.interval = !(cell == null || ExcelUtil.getStringValue(cell).equals("")) ? ExcelUtil.getIntValue(cell) : 60;
					
					item = (Moon.isKorean()) ? "단위" : "Measure";
					cell = row.getCell(6);
					modbusPoint.measure = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getStringValue(cell) : "";
					
					item = (Moon.isKorean()) ? "보정식" : "Scale Function";
					cell = row.getCell(7);
					modbusPoint.scaleFunc = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getStringValue(cell) : "x";
		
					
					boolean dataFormat1 = !ExcelUtil.isNull(row.getCell(8)) && !ExcelUtil.isNull(row.getCell(9));
					boolean dataForamt2 = !ExcelUtil.isNull(row.getCell(10));
					
					if(!ExcelUtil.isNull(row.getCell(8)) && ExcelUtil.isNull(row.getCell(9))
						|| ExcelUtil.isNull(row.getCell(8)) && !ExcelUtil.isNull(row.getCell(9))) {						
						item = (Moon.isKorean()) ? "이진 상태 레이블" : "Binary State Label";
						throw new IOException();
					}
					if(dataFormat1 && dataForamt2) {
						// 데이터 형식 : 이진 상태이면서 동시에 다중 상태 일 수는 없다
						item = (Moon.isKorean()) ? "이진 상태 레이블 & 다중 상태 레이블" : "Binary State Label & Multi-State Label";
						throw new IOException();
					}
					
					if (dataForamt2) {
						item = (Moon.isKorean()) ? "다중 상태 레이블" : "Multi-State Label";
						cell = row.getCell(10);
						String[] keys = ExcelUtil.getStringValue(cell).split(";");
						
						modbusPoint.dataFormat = PerfConf.DATA_FORMAT_STATUS;
						PerfLabelStatusBean[] statusLabels = new PerfLabelStatusBean[keys.length / 2];
						int j = 0;
						
						for (int k = 0; k < keys.length; k += 2) {
							statusLabels[j] = new PerfLabelStatusBean();
							statusLabels[j].value = Integer.parseInt(keys[k].trim());
							statusLabels[j].label = keys[k + 1].trim();
							j++;
						}
						
						modbusPoint.labels = statusLabels;
						
					} else if (dataFormat1) {
						item = (Moon.isKorean()) ? "이진 상태 레이블" : "Binary State Label";
						modbusPoint.dataFormat = PerfConf.DATA_FORMAT_DIGITAL;
						modbusPoint.binLabel = new String[] { 
								ExcelUtil.getStringValue(row.getCell(8)),
								ExcelUtil.getStringValue(row.getCell(9)) };
					} else {
						modbusPoint.dataFormat = PerfConf.DATA_FORMAT_MEASURE;
					}
					
				}catch(Exception e) {
					throw new IOException(Integer.toString(rowNum + 1) + "," + item + "," + modbusPoint.displayName);			
				}
	
				/*
				if (!ExcelUtil.isNull(row.getCell(11))) {
				
					try {
						EventInfo evt = new EventInfo();
						
						item = (Moon.isKorean()) ? "심각도" : "Severity";
						cell = row.getCell(11);
						evt.severity = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "임계값" : "Threshold";
						cell = row.getCell(12);
						evt.threshold = ExcelUtil.getDoubleValue(cell);
						
						item = (Moon.isKorean()) ? "연산자" : "Operator";
						cell = row.getCell(13);
						evt.op = ExcelUtil.getStringValue(cell);
						
						item = (Moon.isKorean()) ? "발생 모드" : "Mode";
						cell = row.getCell(14);
						evt.mode = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "지속 시간" : "Duration";
						cell = row.getCell(15);
						evt.duration = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "발생 횟수" : "Count";
						cell = row.getCell(16);
						evt.count = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "통보 횟수" : "SeqCount";
						cell = row.getCell(17);
						evt.seqCount = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "자동 등록 사용여부" : "AutoReg";
						cell = row.getCell(18);
						evt.autoReg = ExcelUtil.getBooleanValue(cell);
						
						item = (Moon.isKorean()) ? "이름" : "Name";
						cell = row.getCell(19);
						evt.name = ExcelUtil.getStringValue(cell);
						
						item = (Moon.isKorean()) ? "메시지" : "Message";
						cell = row.getCell(20);
						evt.msg = ExcelUtil.getStringValue(cell);
						
						item = (Moon.isKorean()) ? "사용여부" : "Enable";
						cell = row.getCell(21);
						evt.enable = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "자동 복구 사용여부" : "AutoClose";
						cell = row.getCell(22);
						evt.autoClose = ExcelUtil.getBooleanValue(cell);
						
						modbusPoint.evt = new EventInfo[] { evt };
					} catch (Exception e) {
						throw new IOException(Integer.toString(rowNum + 1) + "," + item + "," + modbusPoint.displayName);
					}
				}
				*/
				
			}// end while-loop
			
			modbusWps = trimWatchPointList(modbusWps);
			
			// 모드버스 정보 초기화
			for(ModbusWatchPoint modbusWp : modbusWps) {
				modbusWp.init();
			}
			
			return modbusWps;
		
    	}finally {
    		if(inputStream != null) inputStream.close();
    		inputStream = null;
    	}
    }
    
    public static ArrayList<ModbusWatchPoint> loadExcelV10(File xlsxFile) throws IOException, ModbusPointInitException{
    	
    	StringBuilder msg = new StringBuilder();
		msg.append("<font color='Green'>MK119 V10 Template Type Selection</font>\n");
		msg.append(Util.colorBlue("Excel File") + " : " + xlsxFile.getName() + Util.separator + Util.separator +"\n\n");
		
		if(Moon.isKorean()) {
			msg.append("MK119 V10 템플릿의 종류를 선택해주세요" + Util.separator + Util.separator +"\n");
		}else {
			msg.append("Please select the type of MK119 V10 Excel template" + Util.separator + Util.separator +"\n");
		}

		int menu = Util.showOption(msg.toString(), new String[] { "  Modbus  ", "  PLC  "}, JOptionPane.QUESTION_MESSAGE);

		switch (menu) {
			case 0: // 첫 번째 버튼 : Modbus
				return ModbusPointLoader.loadExcelV10_Modbus(xlsxFile);
				
			case 1: // 두 번째 버튼 : PLC
				return ModbusPointLoader.loadExcelV10_PLC(xlsxFile);
				
			default :
				return null;
		}
		
    }
    
    
    public static ArrayList<ModbusWatchPoint> loadExcelV10_Modbus(File xlsxFile) throws IOException, ModbusPointInitException{
    	
    	FileInputStream inputStream = null;
    	String item = "";
		Cell cell = null;
		ModbusWatchPoint modbusPoint = null;
		
    	try {
			inputStream = new FileInputStream(xlsxFile);
			Workbook workbook = new XSSFWorkbook(inputStream);
			
			Sheet sheet = workbook.getSheetAt(1);
			ArrayList<ModbusWatchPoint> modbusWps = new ArrayList<ModbusWatchPoint>();
			HashMap<String, ArrayList<ModbusWatchPoint>> multiStatesPointMap = new HashMap<String, ArrayList<ModbusWatchPoint>>();
			
			int header = ExcelUtil.getHeaderRowNum(sheet, 0);
			if(header < 0) return null;
			int nullCount = 0;
			
			while(true) {
				if(nullCount > 100) break;
				
				int rowNum = ++header;
				Row row = sheet.getRow(rowNum);
				
				try {
					if(row == null) {
						nullCount++;
						continue;
						
					}else if(ExcelUtil.isNull(row.getCell(0))) {
						nullCount++;
						continue;
					}
					
					modbusPoint = new ModbusWatchPoint();
					modbusWps.add(modbusPoint);
					
					item = (Moon.isKorean()) ? "포인트 이름" : "Point Name";
					cell = row.getCell(0);
					if(ExcelUtil.isNull(cell)) throw new IOException();
					modbusPoint.displayName = ExcelUtil.getStringValue(cell);
					
					
					item = (Moon.isKorean()) ? "단위" : "Measure";
					cell = row.getCell(2);
					modbusPoint.measure = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getStringValue(cell) : "";
					
					
					item = (Moon.isKorean()) ? "기능코드" : "Function Code";
					cell = row.getCell(3);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					int functionCode = ExcelUtil.getIntValue(cell);
					
					
					item = (Moon.isKorean()) ? "주소" : "Address";
					cell = row.getCell(4);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					String address = ExcelUtil.getStringValue(cell).toLowerCase().contains("0x") ? ExcelUtil.getStringValue(cell) : String.valueOf(ExcelUtil.getIntValue(cell));
					
					
					item = (Moon.isKorean()) ? "데이터 타입" : "Data Type";
					cell = row.getCell(5);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					String dataType = ExcelUtil.getStringValue(cell);
					
					String counter = functionCode + "_" + address + "_" + dataType;
					
					modbusPoint.counter = counter;
					
					
					item = (Moon.isKorean()) ? "보정식" : "Calibration Formula";
					cell = row.getCell(6);
					modbusPoint.scaleFunc = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getStringValue(cell) : "x";
					
					
//					item = (Moon.isKorean()) ? "수집 주기" : "Check Interval";
//					cell = row.getCell(7);
//					modbusPoint.interval = !(cell == null || ExcelUtil.getStringValue(cell).equals("")) ? ExcelUtil.getIntValue(cell) : 60;
					
					
					item = (Moon.isKorean()) ? "데이터 형식" : "Data Format";
					cell = row.getCell(9);
					String dataForamt = ExcelUtil.getStringValue(cell);
					
					if(dataForamt != null && dataForamt.length() > 0) {
						dataForamt = dataForamt.toLowerCase();
					}
					
					if(dataForamt.equalsIgnoreCase("1") || dataForamt.contains("bool") || dataForamt.contains("이진")) {
						modbusPoint.dataFormat = PerfConf.DATA_FORMAT_DIGITAL;
						
					}else if(dataForamt.equalsIgnoreCase("2") || dataForamt.contains("multi") || dataForamt.contains("다중")) {
						modbusPoint.dataFormat = PerfConf.DATA_FORMAT_STATUS;
						
					}else {						
						modbusPoint.dataFormat = PerfConf.DATA_FORMAT_MEASURE;
						
					}
					
					if (modbusPoint.dataFormat == PerfConf.DATA_FORMAT_DIGITAL) {
						item = (Moon.isKorean()) ? "이진 상태 레이블" : "Binary State Label";						
						modbusPoint.binLabel = new String[] { 
								ExcelUtil.getStringValue(row.getCell(12)),
								ExcelUtil.getStringValue(row.getCell(13)) };
						
					}else if (modbusPoint.dataFormat == PerfConf.DATA_FORMAT_STATUS) {
						if(multiStatesPointMap.containsKey(modbusPoint.displayName)) {
							ArrayList<ModbusWatchPoint> list = multiStatesPointMap.get(modbusPoint.displayName);
							list.add(modbusPoint);
							
						}else {
							ArrayList<ModbusWatchPoint> list = new ArrayList<ModbusWatchPoint>();
							list.add(modbusPoint);
							multiStatesPointMap.put(modbusPoint.displayName, list);
							
						}
					}
					
				}catch(Exception e) {
					throw new IOException(Integer.toString(rowNum + 1) + "," + item + "," + modbusPoint.displayName);
				}
			}// end while-loop
			
			HashMap<String, String> mappingMap = new HashMap<String, String>();
			Sheet labelSheet = workbook.getSheetAt(2);
			
			int labelHeader = ExcelUtil.getHeaderRowNum(labelSheet, 0);
			if(labelHeader < 0) return null;
			nullCount = 0;
			
			while(true) {
				if(nullCount > 100) break;
				
				int rowNum = ++labelHeader;
				Row row = labelSheet.getRow(rowNum);
				
				try {
					if(row == null) {
						nullCount++;
						continue;
					}else if(ExcelUtil.isNull(row.getCell(0))) {
						nullCount++;
						continue;
					}
					
					item = (Moon.isKorean()) ? "포인트 이름" : "Point Name";
					cell = row.getCell(0);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					String pointName = ExcelUtil.getStringValue(cell);
					
					item = (Moon.isKorean()) ? "Data Code" : "Data Code";
					cell = row.getCell(1);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					int dataCode = ExcelUtil.getIntValue(cell);
					
					item = (Moon.isKorean()) ? "Point Value" : "Point Value";
					cell = row.getCell(2);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					String pointValue = ExcelUtil.getStringValue(cell);
					
					String key = pointName;
					String value = dataCode + "; " + pointValue + ";";
					
					if(mappingMap.containsKey(key)) {
						String lastValue = mappingMap.get(key);
						lastValue += ( " " +  value );
						mappingMap.put(key, lastValue);
					}else {
						mappingMap.put(key, value);
					}
				}catch(Exception e) {
					throw new IOException(Integer.toString(rowNum + 1) + "," + item + "," + null);
				}
			}
			
			// 다중 상태 포인트의 레이블 초기화
			if(multiStatesPointMap.size() > 0) {
				item = (Moon.isKorean()) ? "다중 상태 레이블" : "Multi-State Label";
				
				for(String key : multiStatesPointMap.keySet()) {
		            ArrayList<ModbusWatchPoint> multiStatePointList = multiStatesPointMap.get(key);
		            
		            if(!mappingMap.containsKey(key)) {
		            	StringBuilder sb = new StringBuilder();
						sb.append(String.format("%s\n", Util.colorRed("Multi-State Point Label Mapping Error")));
						
						if(Moon.isKorean()) {
							sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("포인트 이름"), key, Util.separator, Util.separator));
							sb.append(String.format("포인트의 다중 상태 레이블 매핑 정보를 초기화 하는중 오류가 발생하였습니다%s%s\n", Util.separator, Util.separator));	
						}else {
							sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("Point Name"), key, Util.separator, Util.separator));
							sb.append(String.format("An error occurred while initializing label mapping information for the above multi-state point%s%s\n", Util.separator, Util.separator));
						}
						
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		            	return null;
		            }
		            
		            String value = mappingMap.get(key);
		            
					String[] keys = value.split(";");
					
					for(ModbusWatchPoint point : multiStatePointList) {
						PerfLabelStatusBean[] statusLabels = new PerfLabelStatusBean[keys.length / 2];
						int j = 0;
						
						for (int k = 0; k < keys.length; k += 2) {
							statusLabels[j] = new PerfLabelStatusBean();
							statusLabels[j].value = Integer.parseInt(keys[k].trim());
							statusLabels[j].label = keys[k + 1].trim();
							j++;
						}
						
						point.labels = statusLabels;	
					}
		            		            
		        }
			}
			
			// 모드버스 정보 초기화
			for(ModbusWatchPoint modbusWp : modbusWps) {
				modbusWp.init();
			}
			
			return modbusWps;
		
    	}finally {
    		if(inputStream != null) inputStream.close();
    		inputStream = null;
    	}
    	
    }
    
    
    
    public static ArrayList<ModbusWatchPoint> loadExcelV10_PLC(File xlsxFile) throws IOException, ModbusPointInitException{
    	
    	FileInputStream inputStream = null;
    	String item = "";
		Cell cell = null;
		ModbusWatchPoint modbusPoint = null;
    	
    	try {
			inputStream = new FileInputStream(xlsxFile);
			Workbook workbook = new XSSFWorkbook(inputStream);
			
			Sheet mappingSheet = workbook.getSheetAt(2);
			HashMap<String, String> mappingMap = new HashMap<String, String>();
			String content =  "Multi-State Label";
			
			int labelHeader = ExcelUtil.getHeaderRowNum(mappingSheet, 1);
			if(labelHeader < 0) return null;
			int nullCount = 0;
			
			while(true) {
				if(nullCount > 100) break;
				
				int rowNum = ++labelHeader;
				Row row = mappingSheet.getRow(rowNum);
				
				try {
					if(row == null) {
						nullCount++;
						continue;
					}else if(ExcelUtil.isNull(row.getCell(0)) && ExcelUtil.isNull(row.getCell(1))) {
						nullCount++;
						continue;
					}
					
					item = content + " ( Device ID )";
					cell = row.getCell(0);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					int deviceID = ExcelUtil.getIntValue(cell);
					
					item = content + " ( Point ID )";
					cell = row.getCell(1);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					int pointID = ExcelUtil.getIntValue(cell);
					
					item = content + " ( Data Code )";
					cell = row.getCell(2);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					int dataCode = ExcelUtil.getIntValue(cell);
					
					item = content + " ( Point Value )";
					cell = row.getCell(3);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					String pointValue = ExcelUtil.getStringValue(cell);
					
					String key = deviceID + "-" + pointID;
					String value = dataCode + "; " + pointValue + ";";
					
					if(mappingMap.containsKey(key)) {
						String lastValue = mappingMap.get(key);
						lastValue += ( " " +  value );
						mappingMap.put(key, lastValue);
					}else {
						mappingMap.put(key, value);
					}					
				}catch(Exception e) {
					throw new IOException(Integer.toString(rowNum + 1) + "," + item + "," + null);
				}
			}
			
			
			Sheet sheet = workbook.getSheetAt(1);
			ArrayList<ModbusWatchPoint> modbusWps = new ArrayList<ModbusWatchPoint>();
			
			int header = ExcelUtil.getHeaderRowNum(sheet, 2);
			if(header < 0) return null;			
			nullCount = 0;
			
			while(true) {
				if(nullCount > 100) break;
				
				int rowNum = ++header;
				Row row = sheet.getRow(rowNum);
				
				try {
					if(row == null) {
						nullCount++;
						continue;
					}else if(ExcelUtil.isNull(row.getCell(3))) {
						nullCount++;
						continue;
					}
					
					modbusPoint = new ModbusWatchPoint();
					modbusWps.add(modbusPoint);
					
					item = (Moon.isKorean()) ? "Device ID" : "Device ID";
					cell = row.getCell(0);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					modbusPoint.setDeviceID(ExcelUtil.getIntValue(cell));
					
					
					// Device Alias Pass
					// cell = row.getCell(1);
					
					
					item = (Moon.isKorean()) ? "Point ID" : "Point ID";
					cell = row.getCell(2);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					modbusPoint.setPointID(ExcelUtil.getIntValue(cell));
					
					
					item = (Moon.isKorean()) ? "Point Name" : "Point Name";
					cell = row.getCell(3);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					modbusPoint.displayName = ExcelUtil.getStringValue(cell);
					
					
					// Point Type Pass
					// cell = row.getCell(4);
					
					
					item = (Moon.isKorean()) ? "Measure" : "Measure";
					cell = row.getCell(5);
					modbusPoint.measure = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getStringValue(cell) : "";
					
					
					item = (Moon.isKorean()) ? "Function Code" : "Function Code";
					cell = row.getCell(6);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					int functionCode = ExcelUtil.getIntValue(cell);
					
					
					item = (Moon.isKorean()) ? "Address" : "Address";
					cell = row.getCell(7);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					String address = ExcelUtil.getStringValue(cell).toLowerCase().contains("0x") ? ExcelUtil.getStringValue(cell) : String.valueOf(ExcelUtil.getIntValue(cell));
					
					
//					item = (Moon.isKorean()) ? "Memory BIT" : "Memory BIT";
//					cell = row.getCell(8);
//					int memoryBit = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getIntValue(cell) : 0;
//					modbusPoint.setMemoryBit(memoryBit);
					
					
					item = (Moon.isKorean()) ? "Data Type" : "Data Type";
					cell = row.getCell(9);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					String dataType = ExcelUtil.getStringValue(cell);
					
					String counter = functionCode + "_" + address + "_" + dataType;
					
					modbusPoint.counter = counter;
					
					
					item = (Moon.isKorean()) ? "Calibration Formula" : "Calibration Formula";
					cell = row.getCell(10);
					modbusPoint.scaleFunc = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getStringValue(cell) : "x";
					
					
//					item = (Moon.isKorean()) ? "Check Interval" : "Check Interval";
//					cell = row.getCell(11);
//					modbusPoint.interval = !(cell == null || ExcelUtil.getStringValue(cell).equals("")) ? ExcelUtil.getIntValue(cell) : 60;
					
					
					item = (Moon.isKorean()) ? "Data Format" : "Data Format";
					cell = row.getCell(13);
					modbusPoint.dataFormat = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getIntValue(cell) : 3;
					
					
					if (modbusPoint.dataFormat == PerfConf.DATA_FORMAT_DIGITAL) {
						item = (Moon.isKorean()) ? "Binary State Label" : "Binary State Label";						
						modbusPoint.binLabel = new String[] { 
								ExcelUtil.getStringValue(row.getCell(16)),
								ExcelUtil.getStringValue(row.getCell(17)) };
						
					}else if (modbusPoint.dataFormat == PerfConf.DATA_FORMAT_STATUS) {
						item = (Moon.isKorean()) ? "Multi-State Label" : "Multi-State Label";
						String key = modbusPoint.getDeviceID() + "-" + modbusPoint.getPointID();
						String value = mappingMap.get(key);
						String[] keys = value.split(";");
						
						PerfLabelStatusBean[] statusLabels = new PerfLabelStatusBean[keys.length / 2];
						int j = 0;
						
						for (int k = 0; k < keys.length; k += 2) {
							statusLabels[j] = new PerfLabelStatusBean();
							statusLabels[j].value = Integer.parseInt(keys[k].trim());
							statusLabels[j].label = keys[k + 1].trim();
							j++;
						}
						
						modbusPoint.labels = statusLabels;						
					}
					
				}catch(Exception e) {
					throw new IOException(Integer.toString(rowNum + 1) + "," + item + "," + modbusPoint.displayName);
				}
			}
			
			// 모드버스 정보 초기화
			for(ModbusWatchPoint modbusWp : modbusWps) {
				modbusWp.init();
			}
			
			return modbusWps;
		
    	}finally {
    		if(inputStream != null) inputStream.close();
    		inputStream = null;
    	}
    }
    
    

    
    public static ArrayList<ModbusWatchPoint> loadXmlV4(File xmlFile) throws IOException, ModbusPointInitException{
    	String encoding = "euc-kr";
    	
    	StringBuilder msg = new StringBuilder();
		msg.append("<font color='Green'>XML File Encoding</font>\n");
		msg.append(Util.colorBlue("XML File") + " : " + xmlFile.getName() + Util.separator + Util.separator +"\n\n");
		
		if(Moon.isKorean()) {
			msg.append("XML 파일의 인코딩 방식을 선택해주세요" + Util.separator + Util.separator +"\n");	
		}else {
			msg.append("Select the encoding method of the XML file" + Util.separator + Util.separator +"\n");
		}						

		int menu = Util.showOption(msg.toString(), new String[] { "EUC-KR", "UTF-8"}, JOptionPane.QUESTION_MESSAGE);

		switch (menu) {
			case 0: // 첫 번째 버튼 : EUC-KR
				encoding = "euc-kr";
				break;
				
			case 1: // 두 번째 버튼
				encoding = "utf-8";
				break;
				
			default :
				return null;
		}
    	
    	ArrayList<Perf> perfs = FmsPerfConf.getFmsPerfList(xmlFile, encoding);
    	ModbusWatchPoint[] modbusWps = new ModbusWatchPoint[perfs.size()];
    	
    	for(int i = 0; i < perfs.size(); i++) {
    		modbusWps[i] = new ModbusWatchPoint(perfs.get(i));
    		modbusWps[i].init();
    	}

    	return ModbusWatchPoint.convertArrayToList(modbusWps);
    }
   
    
    private static ArrayList<ModbusWatchPoint> trimWatchPointList(ArrayList<ModbusWatchPoint> ModbusWps){
    	int nullCounter = 0;
    	for(int i=0;i<ModbusWps.size();i++){
    		if(checkIsEmptyCell(ModbusWps.get(i))){
    			nullCounter = i;
    			break;
    		}
    	}
    	
    	if(nullCounter!=0){
    		ModbusWatchPoint[] newArray = new ModbusWatchPoint[nullCounter];
    		for(int i=0;i<newArray.length;i++){
    			newArray[i] = new ModbusWatchPoint();
    			newArray[i].displayName = ModbusWps.get(i).displayName;
    			newArray[i].counter = ModbusWps.get(i).counter;
    			newArray[i].measure = ModbusWps.get(i).measure;
    			newArray[i].scaleFunc = ModbusWps.get(i).scaleFunc;
    		}
    		return ModbusWatchPoint.convertArrayToList(newArray);
    	} else {
    		return ModbusWps;
    	}
    }
    
    private static boolean checkIsEmptyCell(ModbusWatchPoint item){
    	return item.displayName.equals("") || item.counter.equals("0_0_\\{1}") || item.scaleFunc.equals("")|| !item.scaleFunc.contains("x");
    }
    
}