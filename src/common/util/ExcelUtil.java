package common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.perf.FmsActionItem;
import common.perf.FmsPerfItem;
import common.perf.FmsPerfItem.EventInfo;
import common.perf.Perf;
import common.perf.PerfConf;
import common.perf.PerfLabelStatusBean;
import moon.Moon;
import src_ko.swing.MainFrame;
import src_ko.util.FileUtil;
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
						sb.append(String.format("%s : %s%s%s\n", Util.colorBlue("�� ��ȣ"), info[0], Util.separator, Util.separator));
						sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("���� �ʵ�"), info[1], Util.separator, Util.separator));	
					}else {
						sb.append(String.format("%s : %s%s%s\n", Util.colorBlue("Row Number"), info[0], Util.separator, Util.separator));
						sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("Error Field"), info[1], Util.separator, Util.separator));
					}
					
					if(hasPointName) {
						if(Moon.isKorean()) {
							sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("����Ʈ �̸�"), info[2], Util.separator, Util.separator));	
						}else {
							sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("Point Name"), info[2], Util.separator, Util.separator));
						}
					}
					
					if(Moon.isKorean()) {
						sb.append(String.format("%s�� ���� %s �ʵ� �Ľ� �������� ������ �߻��Ͽ����ϴ�%s%s\n",
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
	
	public static ArrayList<FmsActionItem> loadControlItem(File file) {
		ArrayList<FmsActionItem> controlList  = null;
		
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
						sb.append(String.format("%s : %s%s%s\n", Util.colorBlue("�� ��ȣ"), info[0], Util.separator, Util.separator));
						sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("���� �ʵ�"), info[1], Util.separator, Util.separator));	
					}else {
						sb.append(String.format("%s : %s%s%s\n", Util.colorBlue("Row Number"), info[0], Util.separator, Util.separator));
						sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("Error Field"), info[1], Util.separator, Util.separator));
					}
					
					if(hasPointName) {
						if(Moon.isKorean()) {
							sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("���� �̸�"), info[2], Util.separator, Util.separator));	
						}else {
							sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("Control Name"), info[2], Util.separator, Util.separator));
						}
					}
					
					if(Moon.isKorean()) {
						sb.append(String.format("%s�� ���� %s �ʵ� �Ľ� �������� ������ �߻��Ͽ����ϴ�%s%s\n",
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
					
					item = (Moon.isKorean()) ? "���ɸ�" : "Point Name";
					cell = row.getCell(0);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					fmsPerfItem.displayName = ExcelUtil.getStringValue(cell);
					
					if(type.equalsIgnoreCase("common")) {
						item = (Moon.isKorean()) ? "ī����" : "Counter";	
					}else {
						item = (Moon.isKorean()) ? "OID" : "OID";
					}
					cell = row.getCell(1);
					if (ExcelUtil.isNull(cell)) throw new IOException();
					String counter = ExcelUtil.getStringValue(cell);
					if(counter.endsWith(".0")) counter = counter.replace(".0", "");
					
					if(type.equalsIgnoreCase("common")) {
						item = (Moon.isKorean()) ? "����" : "Slot";
						cell = row.getCell(2);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						int slot = ExcelUtil.getIntValue(cell);
						
						String perfCounter = String.format("%s\\{%d}", counter, slot);
						fmsPerfItem.counter = perfCounter;
						
					}else {
						
						fmsPerfItem.counter = counter;
					}
					
					
					item = (Moon.isKorean()) ? "�����ֱ�" : "Interval";
					cell = row.getCell(3);
					fmsPerfItem.interval = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getIntValue(cell) : 60;

					
					item = (Moon.isKorean()) ? "����" : "Measure";
					cell = row.getCell(4);
					fmsPerfItem.measure = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getStringValue(cell) : "";
					
					
					item = (Moon.isKorean()) ? "������" : "Scale Formula";
					cell = row.getCell(5);
					fmsPerfItem.scaleFunc = !(ExcelUtil.isNull(cell)) ? ExcelUtil.getStringValue(cell) : "x";


					boolean dataFormat1 = !ExcelUtil.isNull(row.getCell(6)) && !ExcelUtil.isNull(row.getCell(7));
					boolean dataForamt2 = !ExcelUtil.isNull(row.getCell(8));
					
					if(!ExcelUtil.isNull(row.getCell(6)) && ExcelUtil.isNull(row.getCell(7))
						|| ExcelUtil.isNull(row.getCell(6)) && !ExcelUtil.isNull(row.getCell(7))) {						
						item = (Moon.isKorean()) ? "���� ���� ���̺�" : "Binary State Label";
						throw new IOException();
					}
					if(dataFormat1 && dataForamt2) {
						// ������ ���� : ���� �����̸鼭 ���ÿ� ���� ���� �� ���� ����
						item = (Moon.isKorean()) ? "���� ���� ���̺� & ���� ���� ���̺�" : "Binary State Label & Multi-State Label";
						throw new IOException();
					}
					
					if (dataForamt2) {
						item = (Moon.isKorean()) ? "���� ���� ���̺�" : "Multi-State Label";
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
						item = (Moon.isKorean()) ? "���� ���� ���̺�" : "Binary State Label";
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
						
						item = (Moon.isKorean()) ? "�̺�Ʈ �ɰ���" : "Event Severity";
						cell = row.getCell(9);
						evt.severity = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "�̺�Ʈ �Ӱ谪" : "Event Threshold";
						cell = row.getCell(10);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.threshold = ExcelUtil.getDoubleValue(cell);
						
						item = (Moon.isKorean()) ? "�̺�Ʈ ������" : "Event Operator";
						cell = row.getCell(11);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.op = ExcelUtil.getStringValue(cell);
						
						item = (Moon.isKorean()) ? "�̺�Ʈ �߻� ���" : "Event Mode";
						cell = row.getCell(12);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.mode = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "�̺�Ʈ ���� �ð�" : "Event Duration";
						cell = row.getCell(13);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.duration = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "�̺�Ʈ �߻� Ƚ��" : "Event Count";
						cell = row.getCell(14);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.count = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "�̺�Ʈ �뺸 Ƚ��" : "Event SeqCount";
						cell = row.getCell(15);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.seqCount = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "�̺�Ʈ �ڵ� ��� ��뿩��" : "Event AutoReg";
						cell = row.getCell(16);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.autoReg = ExcelUtil.getBooleanValue(cell);
						
						item = (Moon.isKorean()) ? "�̺�Ʈ �̸�" : "Event Name";
						cell = row.getCell(17);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.name = ExcelUtil.getStringValue(cell);
						
						item = (Moon.isKorean()) ? "�̺�Ʈ �޽���" : "Event Message";
						cell = row.getCell(18);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.msg = ExcelUtil.getStringValue(cell);
						
						item = (Moon.isKorean()) ? "�̺�Ʈ ��뿩��" : "Event Enable";
						cell = row.getCell(19);
						if (ExcelUtil.isNull(cell)) throw new IOException();
						evt.enable = ExcelUtil.getIntValue(cell);
						
						item = (Moon.isKorean()) ? "�̺�Ʈ �ڵ� ���� ��뿩��" : "Event AutoClose";
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
	
	public static ArrayList<FmsActionItem> loadControlExcel(File xlsxFile) throws IOException {
    	
    	FileInputStream inputStream = null;
    	String item = "";
		Cell cell = null;
		FmsActionItem actionItem = null;
    	
    	try {
			inputStream = new FileInputStream(xlsxFile);
			Workbook workbook = new XSSFWorkbook(inputStream);
			
			Sheet sheet = workbook.getSheetAt(0);
			ArrayList<FmsActionItem> controlList = new ArrayList<FmsActionItem>();
	
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
					
					actionItem = new FmsActionItem();
					actionItem.counter = "CONTROL";
					controlList.add(actionItem);
					
					item = (Moon.isKorean()) ? "���� �̸�" : "Control Name";
					cell = row.getCell(0);
					if(ExcelUtil.isNull(cell)) throw new IOException();
					actionItem.displayName = ExcelUtil.getStringValue(cell);
					
					
					item = (Moon.isKorean()) ? "���� ��ɾ�" : "Control Command";
					cell = row.getCell(1);
					if(ExcelUtil.isNull(cell)) throw new IOException();
					String command = ExcelUtil.getStringValue(cell);
					
					if(command.endsWith(".0")) command = command.replace(".0", "");
					actionItem.command = command;
					
					
					item = (Moon.isKorean()) ? "���� ���� ����" : "Control Description";
					cell = row.getCell(2);
					actionItem.desc = ExcelUtil.getStringValue(cell);
					
					
					item = (Moon.isKorean()) ? "�Ķ���� ��� ����" : "Use Parameter";
					cell = row.getCell(3);
					if(ExcelUtil.isNull(cell)) throw new IOException();
					actionItem.useParam = ExcelUtil.getIntValue(cell);

					
					item = (Moon.isKorean()) ? "���� Ÿ�Ӿƿ�" : "Control Timeout";
					cell = row.getCell(4);
					if(ExcelUtil.isNull(cell)) throw new IOException();
					actionItem.waitTime = ExcelUtil.getIntValue(cell);

					
				}catch(Exception e) {
					throw new IOException(Integer.toString(rowNum + 1) + "," + item + "," + actionItem.displayName);
					
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
	
	public static void downloadPerf(String perfType, ArrayList<Perf> perfs) {
		new Thread(()->{
			try {
				FileInputStream in = null;
				FileOutputStream out = null;
				
				String template = String.format("%s\\%s\\V4\\%s\\%s.xlsx",
							MainFrame.getCurrentPath(),
							"template",
							Moon.currentLanguage,
							perfType
						);
				
				File templateFile = new File(template);
				if(!templateFile.exists()) {
					if(Moon.isKorean()) {
						StringBuilder sb = new StringBuilder();
						sb.append(Util.colorRed("Template File that does not Exist") + Util.separator + "\n");
						sb.append("�Ʒ��� ��ο� ���ø� ������ �������� �ʽ��ϴ�" + Util.separator + Util.separator + "\n\n");
						sb.append(Util.colorRed("Path") + " : " + templateFile.getAbsolutePath().replace("\\", Util.colorRed("\\")) + Util.separator + Util.separator + "\n");
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						return;
					}else {
						StringBuilder sb = new StringBuilder();
						sb.append(Util.colorRed("Template File that does not Exist") + Util.separator + "\n");
						sb.append("Template file does not exist in the path below" + Util.separator + Util.separator + "\n\n");
						sb.append(Util.colorRed("Path") + " : " + templateFile.getAbsolutePath().replace("\\", Util.colorRed("\\")) + Util.separator + Util.separator + "\n");
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				String formPath = templateFile.getParent() + "\\form.xlsx";
				File formFile = new File(formPath);
				FileUtil.copyFile(templateFile, formFile);
				
				if(formFile.exists()) {
					downloadPerfExel(formFile, perfType, perfs);
				}
					
			}catch(Exception e) {
				e.printStackTrace();
				return;
			}
		}).start(); // end Thread
	}
	
	public static void downloadControl(String controlType, ArrayList<FmsActionItem> actions) {
		new Thread(()->{
			try {
				FileInputStream in = null;
				FileOutputStream out = null;
				
				String template = String.format("%s\\%s\\V4\\%s\\%s.xlsx",
							MainFrame.getCurrentPath(),
							"template",
							Moon.currentLanguage,
							controlType
						);
				
				File templateFile = new File(template);
				if(!templateFile.exists()) {
					if(Moon.isKorean()) {
						StringBuilder sb = new StringBuilder();
						sb.append(Util.colorRed("Template File that does not Exist") + Util.separator + "\n");
						sb.append("�Ʒ��� ��ο� ���ø� ������ �������� �ʽ��ϴ�" + Util.separator + Util.separator + "\n\n");
						sb.append(Util.colorRed("Path") + " : " + templateFile.getAbsolutePath().replace("\\", Util.colorRed("\\")) + Util.separator + Util.separator + "\n");
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						return;
					}else {
						StringBuilder sb = new StringBuilder();
						sb.append(Util.colorRed("Template File that does not Exist") + Util.separator + "\n");
						sb.append("Template file does not exist in the path below" + Util.separator + Util.separator + "\n\n");
						sb.append(Util.colorRed("Path") + " : " + templateFile.getAbsolutePath().replace("\\", Util.colorRed("\\")) + Util.separator + Util.separator + "\n");
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				String formPath = templateFile.getParent() + "\\form.xlsx";
				File formFile = new File(formPath);
				FileUtil.copyFile(templateFile, formFile);
				
				if(formFile.exists()) {
					downloadControlExel(formFile, controlType, actions);
				}
					
			}catch(Exception e) {
				e.printStackTrace();
				return;
			}
		}).start(); // end Thread
	}
	
	public static void downloadPerfExel(File file, String perfType, ArrayList<Perf> perfList) throws IOException{		
		FileInputStream in = null;
		FileOutputStream out = null;
		
		try {
			in = new FileInputStream(file);
			
			XSSFWorkbook workbook = new XSSFWorkbook(in);
	
			XSSFCellStyle leftAlign = workbook.createCellStyle();
			leftAlign.setAlignment(HorizontalAlignment.LEFT);
//			leftAlign.setBorderTop(BorderStyle.THIN);
//			leftAlign.setBorderBottom(BorderStyle.THIN);
//			leftAlign.setBorderLeft(BorderStyle.THIN);
//			leftAlign.setBorderRight(BorderStyle.THIN);
			
			XSSFCellStyle centerAlign = workbook.createCellStyle();
			centerAlign.setAlignment(HorizontalAlignment.CENTER);
//			centerAlign.setBorderTop(BorderStyle.THIN);
//			centerAlign.setBorderBottom(BorderStyle.THIN);
//			centerAlign.setBorderLeft(BorderStyle.THIN);
//			centerAlign.setBorderRight(BorderStyle.THIN);
	
			XSSFSheet pointSheet = workbook.getSheetAt(0);
			
			int header = ExcelUtil.getHeaderRowNum(pointSheet, 1);
			
			for(Perf perf : perfList) {
				Row row = pointSheet.createRow(++header);
				
				for(int i = 0; i <= 20; i++) {
					
					if(i == 6 || i == 7) {
						if(perf.getDataFormat() == PerfConf.DATA_FORMAT_DIGITAL) row.createCell(i);
						
					}else if(i == 8){
						if(perf.getDataFormat() == PerfConf.DATA_FORMAT_STATUS) row.createCell(i);
						
					}else {
						row.createCell(i);
					}
					
					if(i == 0 || i == 8 || i == 17 || i == 18) {
						Cell cell = row.getCell(i);
						if(cell != null) cell.setCellStyle(leftAlign);
						
					}else {
						Cell cell = row.getCell(i);
						if(cell != null) cell.setCellStyle(centerAlign);
						
					}
				}
				
				String counter = perf.getCounter();
				String slot = null;
				
				if(counter.contains("\\") && counter.contains("{") && counter.contains("}")) {
					slot = counter.split("\\\\")[1].replace("{", "").replace("}", "");
					counter = counter.split("\\\\")[0];
				}
								
				row.getCell(0).setCellValue(perf.getDisplayName());
				row.getCell(1).setCellValue(counter);
				
				try {
					int slotNum = Integer.parseInt(slot);
					row.getCell(2).setCellValue(slotNum);
					
				}catch(NumberFormatException e) {
					row.getCell(2).setCellValue((slot != null) ? slot : "");
					
				}
				
				row.getCell(3).setCellValue(perf.getInterval());
				row.getCell(4).setCellValue(perf.getMeasure());
				row.getCell(5).setCellValue(perf.getScaleFunction());
				
				switch(perf.getDataFormat()) {
					case PerfConf.DATA_FORMAT_DIGITAL :
						String[] binaryLabel = perf.getBinLabel();
						if(binaryLabel != null) {
							row.getCell(6).setCellValue(binaryLabel[0]);
							row.getCell(7).setCellValue(binaryLabel[1]);
						}
						break;
						
					case PerfConf.DATA_FORMAT_STATUS :
						PerfLabelStatusBean[] labels =  perf.getStatusLabels();
						if(labels != null) {
							String multiLabel = "";
							if(labels != null) {
								for(PerfLabelStatusBean label : labels) {
									multiLabel += label.value + "; " + label.label + "; ";
								}
							}
							row.getCell(8).setCellValue(multiLabel.trim());
						}
						break;
						
					case PerfConf.DATA_FORMAT_MEASURE :
						break;
						
					default :
						break;
				}
				
				common.perf.FmsPerfItem.EventInfo[] fmsEvents = perf.getFmsEventInfo();
				common.perf.SnmpPerfItem.EventInfo[] snmpEvents = perf.getSnmpEventInfo();
				
				common.perf.FmsPerfItem.EventInfo fmsEvent = null;
				common.perf.SnmpPerfItem.EventInfo snmpEvent = null;
				
				if(perfType.equalsIgnoreCase("Common") && fmsEvents != null && fmsEvents.length > 0 && fmsEvents[0] != null) {
					
					fmsEvent = fmsEvents[0];
					row.getCell(9).setCellValue(fmsEvent.severity);
					row.getCell(10).setCellValue(fmsEvent.threshold);
					row.getCell(11).setCellValue(fmsEvent.op);
					row.getCell(12).setCellValue(fmsEvent.mode);
					row.getCell(13).setCellValue(fmsEvent.duration);
					row.getCell(14).setCellValue(fmsEvent.count);
					row.getCell(15).setCellValue(fmsEvent.seqCount);
					row.getCell(16).setCellValue(fmsEvent.autoReg);
					row.getCell(17).setCellValue(fmsEvent.name);
					row.getCell(18).setCellValue(fmsEvent.msg);
					row.getCell(19).setCellValue(fmsEvent.enable);
					row.getCell(20).setCellValue(fmsEvent.autoClose);
					
				}else if(perfType.equalsIgnoreCase("SNMP") && snmpEvents != null && snmpEvents.length > 0 && snmpEvents[0] != null){
					
					snmpEvent = snmpEvents[0];
					row.getCell(9).setCellValue(snmpEvent.severity);
					row.getCell(10).setCellValue(snmpEvent.threshold);
					row.getCell(11).setCellValue(snmpEvent.op);
					row.getCell(12).setCellValue(snmpEvent.mode);
					row.getCell(13).setCellValue(snmpEvent.duration);
					row.getCell(14).setCellValue(snmpEvent.count);
					row.getCell(15).setCellValue(snmpEvent.seqCount);
					row.getCell(16).setCellValue(snmpEvent.autoReg);
					row.getCell(17).setCellValue(snmpEvent.name);
					row.getCell(18).setCellValue(snmpEvent.msg);
					row.getCell(19).setCellValue(snmpEvent.enable);
					row.getCell(20).setCellValue(snmpEvent.autoClose);
				}
			}

			out = new FileOutputStream(file);
			workbook.write(out);
			out.flush();
			
			downloadFile(file);
			
		}finally {
			
			if(in != null) in.close();
			if(out != null) out.close();
			if(file.exists()) FileUtil.deleteFile(file);
		}
	}
	
	public static void downloadControlExel(File file, String perfType, ArrayList<FmsActionItem> actionList) throws IOException{		
		FileInputStream in = null;
		FileOutputStream out = null;
		
		try {
			in = new FileInputStream(file);
			
			XSSFWorkbook workbook = new XSSFWorkbook(in);
	
			XSSFCellStyle leftAlign = workbook.createCellStyle();
			leftAlign.setAlignment(HorizontalAlignment.LEFT);
//			leftAlign.setBorderTop(BorderStyle.THIN);
//			leftAlign.setBorderBottom(BorderStyle.THIN);
//			leftAlign.setBorderLeft(BorderStyle.THIN);
//			leftAlign.setBorderRight(BorderStyle.THIN);
			
			XSSFCellStyle centerAlign = workbook.createCellStyle();
			centerAlign.setAlignment(HorizontalAlignment.CENTER);
//			centerAlign.setBorderTop(BorderStyle.THIN);
//			centerAlign.setBorderBottom(BorderStyle.THIN);
//			centerAlign.setBorderLeft(BorderStyle.THIN);
//			centerAlign.setBorderRight(BorderStyle.THIN);
	
			XSSFSheet pointSheet = workbook.getSheetAt(0);
			
			int header = ExcelUtil.getHeaderRowNum(pointSheet, 0);
			
			for(FmsActionItem action : actionList) {
				Row row = pointSheet.createRow(++header);
				
				for(int i = 0; i <= 4; i++) {
					
					row.createCell(i);
					
					if(i == 0 || i == 2) {
						Cell cell = row.getCell(i);
						if(cell != null) cell.setCellStyle(leftAlign);
						
					}else {
						Cell cell = row.getCell(i);
						if(cell != null) cell.setCellStyle(centerAlign);
						
					}
				}
								
				row.getCell(0).setCellValue(action.displayName);
				row.getCell(1).setCellValue(action.command);
				row.getCell(2).setCellValue(action.desc);
				row.getCell(3).setCellValue(action.useParam);
				row.getCell(4).setCellValue(action.waitTime);

			}
			
			out = new FileOutputStream(file);
			workbook.write(out);
			out.flush();
			
			downloadFile(file);
			
		}finally {
			
			if(in != null) in.close();
			if(out != null) out.close();
			if(file.exists()) FileUtil.deleteFile(file);
		}
	}
	
	public static void downloadFile(File file) {
		String downloadPath = Util.getFilePath();
		
		if(downloadPath != null) {
			downloadPath += ".xlsx";
			File downloadFile = new File(downloadPath);
			FileUtil.copyFile(file, downloadFile);
			if(downloadFile.exists()) {
				
				if(Moon.isKorean()) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorGreen("File Download Successful") + Util.separator + "\n");
					sb.append("�Ʒ��� ��ο� ������ �ٿ�ε� �Ϸ��Ͽ����ϴ�" + Util.separator + Util.separator + "\n\n");
					sb.append(Util.colorBlue("Path") + " : " + downloadFile.getAbsolutePath().replace("\\", Util.colorBlue("\\")) + Util.separator + Util.separator + "\n");
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
					return;	
				}else {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorGreen("File Download Successful") + Util.separator + "\n");
					sb.append("Downloaded the file to the path below" + Util.separator + Util.separator + "\n\n");
					sb.append(Util.colorBlue("Path") + " : " + downloadFile.getAbsolutePath().replace("\\", Util.colorBlue("\\")) + Util.separator + Util.separator + "\n");
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
			}
		}else {
			return;
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
