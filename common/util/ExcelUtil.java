package common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.perf.ControlAction;
import common.perf.FmsPerfItem;
import common.perf.FmsPerfItem.EventInfo;
import common.perf.PerfConf;
import common.perf.PerfLabelStatusBean;
import moon.Moon;
import src_ko.util.Util;

public class ExcelUtil {
	
	public static ArrayList<FmsPerfItem> loadPerfItem(File file, String type) {
		ArrayList<FmsPerfItem> perfList  = null;
		
		try {
			if(file != null && file.exists()) {
				
				try {
					perfList = loadPerfExcel(file, type);
					
				}catch(IOException e) {
					perfList = null;
					e.printStackTrace();
					
					String[] info = e.getMessage().split(",");
					boolean hasPointName = !info[2].equalsIgnoreCase("null");
					
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s\n", Util.colorRed("Point Initialization Error")));
					
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
			perfList = null;
			e.printStackTrace();
			
		}finally {
			return perfList;
		}
	}
	
	public static ArrayList<ControlAction> loadControlItem(File file) {
		ArrayList<ControlAction> controlList  = null;
		
		try {
			if(file != null && file.exists()) {
				
				try {
					
					controlList = loadControlExcel(file);
					
				}catch(IOException e) {
					controlList = null;
					e.printStackTrace();
					
					String[] info = e.getMessage().split(",");
					boolean hasPointName = !info[2].equalsIgnoreCase("null");
					
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s\n", Util.colorRed("Control Initialization Error")));
					
					if(Moon.isKorean()) {
						sb.append(String.format("%s : %s%s%s\n", Util.colorBlue("행 번호"), info[0], Util.separator, Util.separator));
						sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("에러 필드"), info[1], Util.separator, Util.separator));	
					}else {
						sb.append(String.format("%s : %s%s%s\n", Util.colorBlue("Row Number"), info[0], Util.separator, Util.separator));
						sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("Error Field"), info[1], Util.separator, Util.separator));
					}
					
					if(hasPointName) {
						if(Moon.isKorean()) {
							sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("제어 이름"), info[2], Util.separator, Util.separator));	
						}else {
							sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("Control Name"), info[2], Util.separator, Util.separator));
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
			controlList = null;
			e.printStackTrace();
			
		}finally {
			return controlList;
		}
	}
	
	public static ArrayList<FmsPerfItem> loadPerfExcel(File xlsxFile, String type) throws IOException {
    	
    	FileInputStream inputStream = null;
    	String item = "";
		Cell cell = null;
		FmsPerfItem fmsPerfItem = null;
    	
    	try {
			inputStream = new FileInputStream(xlsxFile);
			Workbook workbook = new XSSFWorkbook(inputStream);
			
			Sheet sheet = workbook.getSheetAt(0);
			ArrayList<FmsPerfItem> perfList = new ArrayList<FmsPerfItem>();
	
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
					}else if(ExcelUtil.isNull(row.getCell(0))) {
						nullCount++;
						continue;
					}
					
					fmsPerfItem = new FmsPerfItem();
					perfList.add(fmsPerfItem);
					
					
					item = (Moon.isKorean()) ? "성능명" : "Point Name";
					cell = row.getCell(0);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					fmsPerfItem.displayName = ExcelUtil.getStringValue(cell);
					
					if(type.equalsIgnoreCase("common")) {
						item = (Moon.isKorean()) ? "카운터" : "Counter";	
					}else {
						item = (Moon.isKorean()) ? "OID" : "OID";
					}
					cell = row.getCell(1);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					String counter = ExcelUtil.getStringValue(cell);
					if(counter.endsWith(".0")) counter = counter.replace(".0", "");
					
					if(type.equalsIgnoreCase("common")) {
						item = (Moon.isKorean()) ? "슬롯" : "Slot";
						cell = row.getCell(2);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						int slot = ExcelUtil.getIntValue(cell);
						
						String perfCounter = String.format("%s\\{%d}", counter, slot);
						fmsPerfItem.counter = perfCounter;
						
					}else {
						
						fmsPerfItem.counter = counter;
					}
					
					
					item = (Moon.isKorean()) ? "수집주기" : "Interval";
					cell = row.getCell(3);
					fmsPerfItem.interval = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getIntValue(cell) : 60;

					
					item = (Moon.isKorean()) ? "단위" : "Measure";
					cell = row.getCell(4);
					fmsPerfItem.measure = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getStringValue(cell) : "";
					
					
					item = (Moon.isKorean()) ? "보정식" : "Scale Formula";
					cell = row.getCell(5);
					fmsPerfItem.scaleFunc = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getStringValue(cell) : "x";


					boolean dataFormat1 = !ExcelUtil.isNull(row.getCell(6)) && !ExcelUtil.isNull(row.getCell(7));
					boolean dataForamt2 = !ExcelUtil.isNull(row.getCell(8));
					
					if(!ExcelUtil.isNull(row.getCell(6)) && ExcelUtil.isNull(row.getCell(7))
						|| ExcelUtil.isNull(row.getCell(6)) && !ExcelUtil.isNull(row.getCell(7))) {						
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
						cell = row.getCell(8);
						String[] keys = ExcelUtil.getStringValue(cell).split(";");
						
						fmsPerfItem.dataFormat = PerfConf.DATA_FORMAT_STATUS;
						PerfLabelStatusBean[] statusLabels = new PerfLabelStatusBean[keys.length / 2];
						int j = 0;
						
						for (int k = 0; k < keys.length; k += 2) {
							statusLabels[j] = new PerfLabelStatusBean();
							statusLabels[j].value = Integer.parseInt(keys[k].trim());
							statusLabels[j].label = keys[k + 1].trim();
							j++;
						}
						
						fmsPerfItem.labels = statusLabels;
						
					} else if (dataFormat1) {
						item = (Moon.isKorean()) ? "이진 상태 레이블" : "Binary State Label";
						fmsPerfItem.dataFormat = PerfConf.DATA_FORMAT_DIGITAL;
						fmsPerfItem.binLabel = new String[] { 
								ExcelUtil.getStringValue(row.getCell(6)),
								ExcelUtil.getStringValue(row.getCell(7)) };
						
					} else {
						fmsPerfItem.dataFormat = PerfConf.DATA_FORMAT_MEASURE;
						
					}
					
				}catch(Exception e) {
					throw new IOException(Integer.toString(rowNum + 1) + "," + item + "," + fmsPerfItem.displayName);			
				}
				
				if (!ExcelUtil.isNull(row.getCell(9))) {
				
					try {
						EventInfo evt = new EventInfo();
						
						item = (Moon.isKorean()) ? "이벤트 심각도" : "Event Severity";
						cell = row.getCell(9);
						evt.severity = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "이벤트 임계값" : "Event Threshold";
						cell = row.getCell(10);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.threshold = ExcelUtil.getDoubleValue(cell);
						
						item = (Moon.isKorean()) ? "이벤트 연산자" : "Event Operator";
						cell = row.getCell(11);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.op = ExcelUtil.getStringValue(cell);
						
						item = (Moon.isKorean()) ? "이벤트 발생 모드" : "Event Mode";
						cell = row.getCell(12);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.mode = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "이벤트 지속 시간" : "Event Duration";
						cell = row.getCell(13);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.duration = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "이벤트 발생 횟수" : "Event Count";
						cell = row.getCell(14);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.count = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "이벤트 통보 횟수" : "Event SeqCount";
						cell = row.getCell(15);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.seqCount = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "이벤트 자동 등록 사용여부" : "Event AutoReg";
						cell = row.getCell(16);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.autoReg = ExcelUtil.getBooleanValue(cell);
						
						item = (Moon.isKorean()) ? "이벤트 이름" : "Event Name";
						cell = row.getCell(17);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.name = ExcelUtil.getStringValue(cell);
						
						item = (Moon.isKorean()) ? "이벤트 메시지" : "Event Message";
						cell = row.getCell(18);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.msg = ExcelUtil.getStringValue(cell);
						
						item = (Moon.isKorean()) ? "이벤트 사용여부" : "Event Enable";
						cell = row.getCell(19);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.enable = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "이벤트 자동 복구 사용여부" : "Event AutoClose";
						cell = row.getCell(20);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.autoClose = ExcelUtil.getBooleanValue(cell);
						
						fmsPerfItem.evt = new EventInfo[] { evt };
					} catch (Exception e) {
						throw new IOException(Integer.toString(rowNum + 1) + "," + item + "," + fmsPerfItem.displayName);						
					}
				}
				
			}// end while-loop
			
			return perfList;
		
    	}finally {
    		if(inputStream != null) inputStream.close();
    		inputStream = null;
    	}
    }
	
	public static ArrayList<ControlAction> loadControlExcel(File xlsxFile) throws IOException {
    	
    	FileInputStream inputStream = null;
    	String item = "";
		Cell cell = null;
		ControlAction controlAction = null;
    	
    	try {
			inputStream = new FileInputStream(xlsxFile);
			Workbook workbook = new XSSFWorkbook(inputStream);
			
			Sheet sheet = workbook.getSheetAt(0);
			ArrayList<ControlAction> controlList = new ArrayList<ControlAction>();
	
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
					
					controlAction = new ControlAction();
					controlAction.setControlCounter("CONTROL");
					controlList.add(controlAction);
					
					item = (Moon.isKorean()) ? "제어 이름" : "Control Name";
					cell = row.getCell(0);
					if(ExcelUtil.isNull(cell)) throw new IOException();
					controlAction.setControlName(ExcelUtil.getStringValue(cell));
					
					
					item = (Moon.isKorean()) ? "제어 명령어" : "Control Command";
					cell = row.getCell(1);
					if(ExcelUtil.isNull(cell)) throw new IOException();
					String command = ExcelUtil.getStringValue(cell);
					
					if(command.endsWith(".0")) command = command.replace(".0", "");
					controlAction.setCommand(command);
					
					
					item = (Moon.isKorean()) ? "제어 수행 내용" : "Control Description";
					cell = row.getCell(2);
					controlAction.setDesc(ExcelUtil.getStringValue(cell));
					
					
					item = (Moon.isKorean()) ? "파라미터 사용 여부" : "Use Parameter";
					cell = row.getCell(3);
					if(ExcelUtil.isNull(cell)) throw new IOException();
					controlAction.setUseParam(ExcelUtil.getIntValue(cell));

					
					item = (Moon.isKorean()) ? "제어 타임아웃" : "Control Timeout";
					cell = row.getCell(4);
					if(ExcelUtil.isNull(cell)) throw new IOException();
					controlAction.setWaitTime(ExcelUtil.getIntValue(cell));

					
				}catch(Exception e) {
					throw new IOException(Integer.toString(rowNum + 1) + "," + item + "," + controlAction.getControlName());
					
				}
				
			}// end while-loop
				
			return controlList;
			
    	}finally {
    		if(inputStream != null) inputStream.close();
    		inputStream = null;
    	}
    }
	
	
	public static int getHeaderRowNum(Sheet sheet, int offset) {
		
		Cell cell = null;
		int header = 0;
		int nullCount = 0;
		
		while(true) {
			try {
				if(nullCount > 100) return -1;
				
				Row row = sheet.getRow(header);
				
				if(row == null) {
					header++;
					nullCount++;
					continue;
				}

				cell = row.getCell(0);

				if(!ExcelUtil.isNull(cell)) {
					return header + offset;
					
				}else {
					header++;
					nullCount++;
					continue;
				}
				
			}catch(Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
	}
    	
	public static boolean isNull(Cell cell) {
		return cell == null || cell.toString().trim().equals("") || cell.toString().trim().isEmpty();
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
