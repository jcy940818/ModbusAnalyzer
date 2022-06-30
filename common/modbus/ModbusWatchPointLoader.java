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
import moon.Moon;
import src_ko.util.Util;

public class ModbusWatchPointLoader {
	
	public static ModbusWatchPoint[] load(int mkVersion, File file) {
		ModbusWatchPoint[] modbusWps  = null;
		
		try {
			if(file != null && file.exists()) {

				// 인코딩 선택 해야함
				try {
					String encoding = "euc-kr";
					
					if(file.getAbsolutePath().toLowerCase().endsWith(".xml")) {
						StringBuilder msg = new StringBuilder();
						msg.append("<font color='Green'>XML File Encoding</font>\n");
						msg.append(Util.colorBlue("XML File") + " : " + file.getName() + Util.separator + Util.separator +"\n\n");
						msg.append("XML 파일의 인코딩 방식을 선택해주세요" + Util.separator + Util.separator +"\n");

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

						modbusWps = ModbusWatchPointLoader.loadModbusWatchPointXML(file, encoding);
						
					}else {
						if(mkVersion >= 10) {
							modbusWps = ModbusWatchPointLoader.loadModbusWatchPointXlsxV10(file);
						}else {
							modbusWps = ModbusWatchPointLoader.loadModbusWatchPointXlsxV4(file);	
						}
					}
					
				}catch(ModbusWatchPointInitException e) {
					modbusWps = null;
					e.printStackTrace();
					
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s\n", Util.colorRed("Modbus Watch Point Initialization Error")));
					sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("모드버스 포인트"), e.getMessage(), Util.separator, Util.separator));
					sb.append(String.format("위의 모드버스 포인트 정보를 초기화 하는중 오류가 발생하였습니다%s%s\n", Util.separator, Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					
				}catch(IOException e) {
					modbusWps = null;
					e.printStackTrace();
					
					String[] info = e.getMessage().split(",");
					boolean hasPointName = !info[2].equalsIgnoreCase("null");
					
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s\n", Util.colorRed("Modbus Watch Point Initialization Error")));
					sb.append(String.format("%s : %s%s%s\n", Util.colorBlue("행 번호"), info[0], Util.separator, Util.separator));
					sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("에러 필드"), info[1], Util.separator, Util.separator));
					
					if(hasPointName) {
						sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("모드버스 포인트"), info[2], Util.separator, Util.separator));
					}
					
					sb.append(String.format("%s번 행의 %s 필드 파싱 과정에서 에러가 발생하였습니다%s%s\n", 
									Util.colorRed(info[0]),							
									Util.colorRed(info[1]),
									Util.separator,
									Util.separator));
					
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
        
    public static ModbusWatchPoint[] loadModbusWatchPointXlsxV4(File xlsxFile) throws IOException, ModbusWatchPointInitException{
    	
    	FileInputStream inputStream = null;
    	String item = "";
		Cell cell = null;
    	
    	try {
			inputStream = new FileInputStream(xlsxFile);
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			int numberOfRows = sheet.getPhysicalNumberOfRows();
			ModbusWatchPoint[] modbusWps = new ModbusWatchPoint[numberOfRows - 2];
	
			for (int i = 2; i < numberOfRows; i++) {
				Row row = sheet.getRow(i);
				
				try {
					if(row == null) {
						continue;
					}else if(row.getCell(1) == null) {
						// 성능명 내용이 없으면 스킵
						continue;
					}else if(CellUtil.getStringValue(row.getCell(1)).equals("")) {
						// 성능명 내용이 없으면 스킵
						continue;
					}
					
					modbusWps[i - 2] = new ModbusWatchPoint();
					
					item = (Moon.isKorean()) ? "성능명" : "Point Name";
					cell = row.getCell(1);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					modbusWps[i - 2].displayName = CellUtil.getStringValue(cell);
					
					item = (Moon.isKorean()) ? "기능코드" : "Function Code";
					cell = row.getCell(2);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					int functionCode = CellUtil.getIntValue(cell);
					
					item = (Moon.isKorean()) ? "주소" : "Address";
					cell = row.getCell(3);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					String address = CellUtil.getStringValue(cell).toLowerCase().contains("0x") ? CellUtil.getStringValue(cell) : String.valueOf(CellUtil.getIntValue(cell));
					
					item = (Moon.isKorean()) ? "데이터 타입" : "Data Type";
					cell = row.getCell(4);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					String dataType = CellUtil.getStringValue(cell);
					
					String counter = functionCode + "_" + address + "_" + dataType;
					
					modbusWps[i - 2].counter = counter;
					
					item = (Moon.isKorean()) ? "수집 주기" : "Interval";
					cell = row.getCell(5);
					modbusWps[i - 2].interval = !(cell == null || CellUtil.getStringValue(cell).equals("")) ? CellUtil.getIntValue(cell) : 60;
					
					item = (Moon.isKorean()) ? "단위" : "Measure";
					cell = row.getCell(6);
					modbusWps[i - 2].measure = !(cell == null || CellUtil.getStringValue(cell).equals("")) ? CellUtil.getStringValue(cell) : "";
					
					item = (Moon.isKorean()) ? "보정식" : "Scale Function";
					cell = row.getCell(7);
					modbusWps[i - 2].scaleFunc = !(cell == null || CellUtil.getStringValue(cell).equals("")) ? CellUtil.getStringValue(cell) : "x";
		
					
					boolean dataFormat1 = row.getCell(8) != null
										&& !CellUtil.getStringValue(row.getCell(8)).equals("")
										&& !(CellUtil.getStringValue(row.getCell(8)).length() < 1);
					
					dataFormat1 = dataFormat1
										&& row.getCell(9) != null
										&& !CellUtil.getStringValue(row.getCell(9)).equals("")
										&& !(CellUtil.getStringValue(row.getCell(9)).length() < 1);
					
					boolean dataForamt2 = row.getCell(10) != null
							&& !CellUtil.getStringValue(row.getCell(10)).equals("")
							&& !(CellUtil.getStringValue(row.getCell(10)).length() < 1);
					
					if(dataFormat1 && dataForamt2) {
						// 데이터 형식 : 이진 상태이면서 동시에 다중 상태 일 수는 없다
						item = (Moon.isKorean()) ? "이진 상태 레이블 & 다중 상태 레이블" : "Binary Status Label & Multi-Status Label";
						throw new IOException();
					}
					
					if (dataForamt2) {
						item = (Moon.isKorean()) ? "다중 상태 레이블" : "Multi-Status Label";
						cell = row.getCell(10);
						String[] keys = CellUtil.getStringValue(cell).split(";");
						
						modbusWps[i - 2].dataFormat = PerfConf.DATA_FORMAT_STATUS;
						PerfLabelStatusBean[] statusLabels = new PerfLabelStatusBean[keys.length / 2];
						int j = 0;
						
						for (int k = 0; k < keys.length; k += 2) {
							statusLabels[j] = new PerfLabelStatusBean();
							statusLabels[j].value = Integer.parseInt(keys[k].trim());
							statusLabels[j].label = keys[k + 1].trim();
							j++;
						}
						
						modbusWps[i - 2].labels = statusLabels;
						
					} else if (dataFormat1) {
						item = (Moon.isKorean()) ? "이진 상태 레이블" : "Binary Status Label";
						modbusWps[i - 2].dataFormat = PerfConf.DATA_FORMAT_DIGITAL;
						modbusWps[i - 2].binLabel = new String[] { 
								CellUtil.getStringValue(row.getCell(8)),
								CellUtil.getStringValue(row.getCell(9)) };
					} else {
						modbusWps[i - 2].dataFormat = PerfConf.DATA_FORMAT_MEASURE;
					}
					
				}catch(Exception e) {
					throw new IOException(Integer.toString(i+1) + "," + item + "," + modbusWps[i - 2].displayName);			
				}
	
				/*
				if (row.getCell(11) != null && !CellUtil.getStringValue(row.getCell(11)).equalsIgnoreCase("")) {
				
					try {
						EventInfo evt = new EventInfo();
						
						item = (Moon.isKorean()) ? "심각도" : "Severity";
						cell = row.getCell(11);
						evt.severity = CellUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "임계값" : "Threshold";
						cell = row.getCell(12);
						evt.threshold = CellUtil.getDoubleValue(cell);
						
						item = (Moon.isKorean()) ? "연산자" : "Operator";
						cell = row.getCell(13);
						evt.op = CellUtil.getStringValue(cell);
						
						item = (Moon.isKorean()) ? "발생 모드" : "Mode";
						cell = row.getCell(14);
						evt.mode = CellUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "지속 시간" : "Duration";
						cell = row.getCell(15);
						evt.duration = CellUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "발생 횟수" : "Count";
						cell = row.getCell(16);
						evt.count = CellUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "통보 횟수" : "SeqCount";
						cell = row.getCell(17);
						evt.seqCount = CellUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "자동 등록 사용여부" : "AutoReg";
						cell = row.getCell(18);
						evt.autoReg = CellUtil.getBooleanValue(cell);
						
						item = (Moon.isKorean()) ? "이름" : "Name";
						cell = row.getCell(19);
						evt.name = CellUtil.getStringValue(cell);
						
						item = (Moon.isKorean()) ? "메시지" : "Message";
						cell = row.getCell(20);
						evt.msg = CellUtil.getStringValue(cell);
						
						item = (Moon.isKorean()) ? "사용여부" : "Enable";
						cell = row.getCell(21);
						evt.enable = CellUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "자동 복구 사용여부" : "AutoClose";
						cell = row.getCell(22);
						evt.autoClose = CellUtil.getBooleanValue(cell);
						
						modbusWps[i - 2].evt = new EventInfo[] { evt };
					} catch (Exception e) {
						throw new IOException("event" + "," + Integer.toString(i+1) + "," + item + "," + modbusWps[i - 2].displayName);			
					}	
				}
				*/
			}
			
			modbusWps = trimWatchPointArray(modbusWps);
			
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
    
    
    public static ModbusWatchPoint[] loadModbusWatchPointXlsxV10(File xlsxFile) throws IOException, ModbusWatchPointInitException{
    	
    	FileInputStream inputStream = null;
    	String item = "";
		Cell cell = null;
    	
    	try {
			inputStream = new FileInputStream(xlsxFile);
			Workbook workbook = new XSSFWorkbook(inputStream);
			
			Sheet mappingSheet = workbook.getSheetAt(2);
			int mappingNumberOfRows = mappingSheet.getPhysicalNumberOfRows();
			HashMap<String, String> mappingMap = new HashMap<String, String>();
			String content =  "Point Value Code Definition";
			
			for(int i = 2; i < mappingNumberOfRows; i++) {
				int rowNum = i - 2;
				Row row = mappingSheet.getRow(i);
				
				try {
					if(row == null) {
						continue;
					}else if(row.getCell(2) == null) {
						// Point Value Code Definition 시트의 Data Code 내용이 없으면 스킵
						continue;
					}else if(CellUtil.getStringValue(row.getCell(2)).equals("")) {
						// Point Value Code Definition 시트의 Data Code 내용이 없으면 스킵
						continue;
					}
					
					item = content + " ( Device ID )";
					cell = row.getCell(0);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					int deviceID = CellUtil.getIntValue(cell);
					
					item = content + " ( Point ID )";
					cell = row.getCell(1);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					int pointID = CellUtil.getIntValue(cell);
					
					item = content + " ( Data Code )";
					cell = row.getCell(2);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					int dataCode = CellUtil.getIntValue(cell);
					
					item = content + " ( Point Value )";
					cell = row.getCell(3);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					String pointValue = CellUtil.getStringValue(cell);
					
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
					throw new IOException(Integer.toString(i+1) + "," + item + "," + null);
				}
			}
			
			
			Sheet sheet = workbook.getSheetAt(1);
			int numberOfRows = sheet.getPhysicalNumberOfRows();
			ModbusWatchPoint[] modbusWps = new ModbusWatchPoint[numberOfRows - 4];
			for (int i = 4; i < numberOfRows; i++) {
				int rowNum = i - 4;				
				Row row = sheet.getRow(i);
				
				try {
					if(row == null) {
						continue;
					}else if(row.getCell(3) == null) {
						// Point Definition 시트의 Point Name 내용이 없으면 스킵
						continue;
					}else if(CellUtil.getStringValue(row.getCell(3)).equals("")) {
						// Point Definition 시트의 Point Name 내용이 없으면 스킵
						continue;
					}
					
					modbusWps[rowNum] = new ModbusWatchPoint();
					
					item = (Moon.isKorean()) ? "Device ID" : "Device ID";
					cell = row.getCell(0);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					modbusWps[rowNum].setDeviceID(CellUtil.getIntValue(cell));
					
					
					// Device Alias Pass
					// cell = row.getCell(1);
					
					
					item = (Moon.isKorean()) ? "Point ID" : "Point ID";
					cell = row.getCell(2);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					modbusWps[rowNum].setPointID(CellUtil.getIntValue(cell));
					
					
					item = (Moon.isKorean()) ? "Point Name" : "Point Name";
					cell = row.getCell(3);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					modbusWps[rowNum].displayName = CellUtil.getStringValue(cell);
					
					
					// Point Type Pass
					// cell = row.getCell(4);
					
					
					item = (Moon.isKorean()) ? "Measure" : "Measure";
					cell = row.getCell(5);
					modbusWps[rowNum].measure = !(cell == null || CellUtil.getStringValue(cell).equals("")) ? CellUtil.getStringValue(cell) : "";
					
					
					item = (Moon.isKorean()) ? "Function Code" : "Function Code";
					cell = row.getCell(6);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					int functionCode = CellUtil.getIntValue(cell);
					
					
					item = (Moon.isKorean()) ? "Address" : "Address";
					cell = row.getCell(7);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					String address = CellUtil.getStringValue(cell).toLowerCase().contains("0x") ? CellUtil.getStringValue(cell) : String.valueOf(CellUtil.getIntValue(cell));
					
					
					item = (Moon.isKorean()) ? "Data Type" : "Data Type";
					cell = row.getCell(8);
					if (cell == null || CellUtil.getStringValue(cell).equals("")) throw new IOException();
					String dataType = CellUtil.getStringValue(cell);
					
					String counter = functionCode + "_" + address + "_" + dataType;
					
					modbusWps[rowNum].counter = counter;
					
					
					item = (Moon.isKorean()) ? "Calibration Formula" : "Calibration Formula";
					cell = row.getCell(9);
					modbusWps[rowNum].scaleFunc = !(cell == null || CellUtil.getStringValue(cell).equals("")) ? CellUtil.getStringValue(cell) : "x";
					
					
					item = (Moon.isKorean()) ? "Check Interval" : "Check Interval";
					cell = row.getCell(10);
					modbusWps[rowNum].interval = !(cell == null || CellUtil.getStringValue(cell).equals("")) ? CellUtil.getIntValue(cell) : 60;
					
					
					item = (Moon.isKorean()) ? "Data Format" : "Data Format";
					cell = row.getCell(12);
					modbusWps[rowNum].dataFormat = !(cell == null || CellUtil.getStringValue(cell).equals("")) ? CellUtil.getIntValue(cell) : 3;
					
					
					if (modbusWps[rowNum].dataFormat == PerfConf.DATA_FORMAT_DIGITAL) {
						item = (Moon.isKorean()) ? "Label of 0, 1" : "Label of 0, 1";						
						modbusWps[rowNum].binLabel = new String[] { 
								CellUtil.getStringValue(row.getCell(15)),
								CellUtil.getStringValue(row.getCell(16)) };
						
					}else if (modbusWps[rowNum].dataFormat == PerfConf.DATA_FORMAT_STATUS) {
						item = (Moon.isKorean()) ? "Point Value Code Definition" : "Point Value Code Definition";
						String key = modbusWps[rowNum].getDeviceID() + "-" + modbusWps[rowNum].getPointID();
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
						
						modbusWps[rowNum].labels = statusLabels;						
					}
					
				}catch(Exception e) {
					throw new IOException(Integer.toString(i+1) + "," + item + "," + modbusWps[rowNum].displayName);
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
    
    
    
    public static ModbusWatchPoint[] loadModbusWatchPointXML(File xmlFile, String encoding) throws IOException, ModbusWatchPointInitException{
    	ArrayList<Perf> perfs = FmsPerfConf.getFmsPerfList(xmlFile, encoding);
    	ModbusWatchPoint[] modbusWps = new ModbusWatchPoint[perfs.size()];
    	
    	for(int i = 0; i < perfs.size(); i++) {
    		modbusWps[i] = new ModbusWatchPoint(perfs.get(i));
    		modbusWps[i].init();
    	}
    	
    	return modbusWps;
    }
   
    
    private static ModbusWatchPoint[] trimWatchPointArray(ModbusWatchPoint[] ModbusWps){
    	int nullCounter = 0;
    	for(int i=0;i<ModbusWps.length;i++){
    		if(checkIsEmptyCell(ModbusWps[i])){
    			nullCounter = i;
    			break;
    		}
    	}
    	
    	if(nullCounter!=0){
    		ModbusWatchPoint[] newArray = new ModbusWatchPoint[nullCounter];
    		for(int i=0;i<newArray.length;i++){
    			newArray[i] = new ModbusWatchPoint();
    			newArray[i].displayName = ModbusWps[i].displayName;
    			newArray[i].counter = ModbusWps[i].counter;
    			newArray[i].measure = ModbusWps[i].measure;
    			newArray[i].scaleFunc = ModbusWps[i].scaleFunc;
    		}
    		return newArray;
    	} else {
    		return ModbusWps;
    	}
    }
    
    private static boolean checkIsEmptyCell(ModbusWatchPoint item){
    	return item.displayName.equals("") || item.counter.equals("0_0_\\{1}") || item.scaleFunc.equals("")|| !item.scaleFunc.contains("x");
    }
    
}

class CellUtil {
	public static boolean isNull(Cell cell) {
		return cell.toString().trim().length() < 1;
	}
	
	public static String getStringValue(Cell cell) {
		return cell.toString().trim();
	}

	public static boolean getBooleanValue(Cell cell) {
		return Boolean.parseBoolean(cell.toString().trim());
	}
	
	public static int getIntValue(Cell cell) {
		return (int) Double.parseDouble(cell.toString().trim());
	}

	public static double getDoubleValue(Cell cell) {
		return Double.parseDouble(cell.toString().trim());
	}
}
