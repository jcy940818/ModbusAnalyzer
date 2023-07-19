package common.modbus;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.perf.FmsPerfItem;
import common.perf.FmsPerfItem.EventInfo;
import common.perf.PerfConf;
import common.perf.PerfLabelStatusBean;
import common.util.ExcelUtil;
import common.util.FontManager;
import common.util.XmlGenerator;
import moon.Moon;
import src_ko.agent.Event;
import src_ko.swing.MainFrame;
import src_ko.util.FileUtil;
import src_ko.util.Util;

public class ModbusPointExporter {

	public static boolean isRunning = false;
	
	public static void exportExcel(int mkVersion, String addrFormat, boolean useAutoEvent , boolean usePerfEvent, ArrayList<ModbusWatchPoint> pointList) {
		new Thread(()->{
			try {
				String fileName = "Modbus";
				
				if(mkVersion >= 10) {
					StringBuilder msg = new StringBuilder();
					msg.append("<font color='Green'>MK119 V10 Template Type</font>\n");				
					
					if(Moon.isKorean()) {
						msg.append("MK119 V10 템플릿의 종류를 선택해주세요" + Util.separator + Util.separator +"\n");
					}else {
						msg.append("Please select the type of MK119 V10 Excel template" + Util.separator + Util.separator +"\n");
					}
	
					int menu = Util.showOption(msg.toString(), new String[] { "  Modbus  ", "  PLC  "}, JOptionPane.QUESTION_MESSAGE);
					switch (menu) {
						case 0: // 첫 번째 버튼 : Modbus
							fileName = "Modbus";
							break;
						case 1: // 두 번째 버튼 : PLC
							fileName = "PLC";
							break;
						default :
							// 템플릿 다운로드 취소
							return;
					}
				}
				
				String filePath = String.format("%s\\%s\\V%s\\%s\\%s.xlsx", 
						MainFrame.getCurrentPath(),
						"template",
						mkVersion,
						Moon.currentLanguage,
						fileName
						);
				
				File file = new File(filePath);
				if(!file.exists()) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Template File that does not Exist") + Util.separator + "\n");
					sb.append("아래의 경로에 템플릿 파일이 존재하지 않습니다" + Util.separator + Util.separator + "\n\n");
					sb.append(Util.colorRed("Path") + " : " + file.getAbsolutePath().replace("\\", Util.colorRed("\\")) + Util.separator + Util.separator + "\n");
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String formPath = file.getParent() + "\\form.xlsx";
				File formFile = new File(formPath);
				FileUtil.copyFile(file, formFile);
				
				if(formFile.exists()) {
					if(mkVersion >= 10) {
						// MK119 V10
						if(fileName.equals("Modbus")) {
							exportExcelV10_Modbus(formFile, addrFormat, pointList);
						}else {
							exportExcelV10_PLC(formFile, addrFormat, pointList);
						}
					}else {
						// MK119 V4
						exportExcelV4(formFile, addrFormat, useAutoEvent, usePerfEvent, pointList);
					}
				}
			}catch(Exception exception) {
				StringBuilder sb = new StringBuilder();
				sb.append(Util.colorRed("Failed to Task") + Util.separator + "\n");
				sb.append("처리 할 수 없는 예외가 발생하여 작업에 실패하였습니다" + Util.separator + Util.separator + "\n\n");
				sb.append(Util.colorRed("Exception Message") + " : " + exception.getMessage() + Util.separator + Util.separator + "\n");
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return;
			}
		
		}).start(); // end Thread
	}
	
	public static void exportXML(String addrFormat, boolean useAutoEvent, ArrayList<ModbusWatchPoint> pointList) {
		if(pointList != null && pointList.size() > 0) {
			String encoding = "euc-kr";
	    	
	    	StringBuilder msg = new StringBuilder();
			msg.append("<font color='Green'>XML File Encoding</font>\n");		
			
			if(Moon.isKorean()) {
				msg.append("XML 파일의 인코딩 방식을 선택해주세요" + Util.separator + Util.separator +"\n\n");
				msg.append(String.format("%s : EUC-KR%s%s\n", Util.colorBlue("MK119 4.2 Version 이하"), Util.separator, Util.separator));
				msg.append(String.format("%s : UTF-8%s%s\n", Util.colorBlue("MK119 4.5 Version 이상"), Util.separator, Util.separator));
			}else {
				msg.append("Select the encoding method of the XML file" + Util.separator + Util.separator +"\n\n");
				msg.append(String.format("It's the same as or lower than MK119 %s Version : %s%s%s\n", Util.colorGreen("4.2") ,Util.colorBlue("EUC-KR"), Util.separator, Util.separator));
				msg.append(String.format("It's the same as or higher than MK119 %s Version : %s%s%s\n", Util.colorGreen("4.5"), Util.colorBlue("UTF-8"), Util.separator, Util.separator));
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
					return;
			}
			
			ArrayList<FmsPerfItem> fmsPerfItemList = new ArrayList<FmsPerfItem>();
			
			for(int i = 0; i < pointList.size(); i++) {
				ModbusWatchPoint point = pointList.get(i);
				
				if(addrFormat.contains("HEX")) {
					point.setCounter(point.getHexCounter() + "\\{1}");
				}else {
					point.setCounter(point.getDecCounter() + "\\{1}");
				}
				
				fmsPerfItemList.add(point);
			}
		
			XmlGenerator.generateXML(fmsPerfItemList, useAutoEvent, encoding, "modbus");
		}
	}
	
	public static void exportExcelV4(File file, String addrFormat, boolean useAutoEvent, boolean usePerfEvent, ArrayList<ModbusWatchPoint> pointList) throws IOException{
		ModbusPointExporter.isRunning = true;
		FileInputStream in = null;
		FileOutputStream out = null;
		boolean hexType = addrFormat.contains("HEX");
		
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
			
			int ID = 0;
			int header = ExcelUtil.getHeaderRowNum(pointSheet, 1);
			int columnCount = (useAutoEvent || usePerfEvent) ? 22 : 10;
			
			for(ModbusWatchPoint point : pointList) {
				ID++;
				Row row = pointSheet.createRow(++header);
				
				for(int i = 0; i <= columnCount; i++) {
					
					if(i == 8 || i == 9) {
						if(point.getDataFormat() == PerfConf.DATA_FORMAT_DIGITAL) row.createCell(i);
					}else if(i == 10){
						if(point.getDataFormat() == PerfConf.DATA_FORMAT_STATUS) row.createCell(i);
					}else {
						row.createCell(i);
					}
					
					if(i == 1 || i == 10 || i == 19 || i == 20) {
						Cell cell = row.getCell(i);
						if(cell != null) cell.setCellStyle(leftAlign);
					}else {
						Cell cell = row.getCell(i);
						if(cell != null) cell.setCellStyle(centerAlign);						
					}
				}
				
				row.getCell(0).setCellValue(ID);
				row.getCell(1).setCellValue(point.getDisplayName());
				row.getCell(2).setCellValue(point.getFunctionCode());
				
				if(usePerfEvent) {
					hexType = (point.getRegisterAddr() >= 9999 || point.getCounter().toLowerCase().contains("0x"));
				}
				
				if(hexType) {
					row.getCell(3).setCellValue(point.getRegisterAddrHexString());
				}else {
					row.getCell(3).setCellValue(point.getRegisterAddr() + 1);
				}
				row.getCell(4).setCellValue(point.getDataType());
				row.getCell(5).setCellValue(60);
				row.getCell(6).setCellValue(point.getMeasure());
				row.getCell(7).setCellValue(point.getScaleFunction());
				
				switch(point.getDataFormat()) {
					case PerfConf.DATA_FORMAT_DIGITAL :
						String[] binaryLabel = point.getBinLabel();
						if(binaryLabel != null) {
							row.getCell(8).setCellValue(binaryLabel[0]);
							row.getCell(9).setCellValue(binaryLabel[1]);
						}
						break;
						
					case PerfConf.DATA_FORMAT_STATUS :
						PerfLabelStatusBean[] labels =  point.getStatusLabels();
						if(labels != null) {
							String multiLabel = "";
							if(labels != null) {
								for(PerfLabelStatusBean label : labels) {
									multiLabel += label.value + "; " + label.label + "; ";
								}
							}
							row.getCell(10).setCellValue(multiLabel.trim());
						}
						break;
						
					case PerfConf.DATA_FORMAT_MEASURE :
						break;
						
					default :
						break;
				}
				
				if(useAutoEvent) {
					row.getCell(11).setCellValue(Integer.parseInt(Event.severity));
					row.getCell(12).setCellValue(Double.parseDouble(Event.threshold));
					row.getCell(13).setCellValue(Event.op);
					row.getCell(14).setCellValue(Integer.parseInt(Event.mode));
					row.getCell(15).setCellValue(Integer.parseInt(Event.duration));
					row.getCell(16).setCellValue(Integer.parseInt(Event.count));
					row.getCell(17).setCellValue(Integer.parseInt(Event.seqCount));
					row.getCell(18).setCellValue(Boolean.parseBoolean(Event.autoReg));
					row.getCell(19).setCellValue(point.getDisplayName() + " " + Event.name);
					row.getCell(20).setCellValue(Event.message);
					row.getCell(21).setCellValue(Integer.parseInt(Event.enable));
					row.getCell(22).setCellValue(Boolean.parseBoolean(Event.autoClose));
					
				}else if(usePerfEvent && point.evt != null && point.evt.length > 0 && point.evt[0] != null){
					
					EventInfo event = point.evt[0];
					row.getCell(11).setCellValue(event.severity);
					row.getCell(12).setCellValue(event.threshold);
					row.getCell(13).setCellValue(event.op);
					row.getCell(14).setCellValue(event.mode);
					row.getCell(15).setCellValue(event.duration);
					row.getCell(16).setCellValue(event.count);
					row.getCell(17).setCellValue(event.seqCount);
					row.getCell(18).setCellValue(event.autoReg);
					row.getCell(19).setCellValue(event.name);
					row.getCell(20).setCellValue(event.msg);
					row.getCell(21).setCellValue(event.enable);
					row.getCell(22).setCellValue(event.autoClose);
				}
			}

			out = new FileOutputStream(file);
			workbook.write(out);
			out.flush();
			
			downloadFile(file);
			
		}finally {
			ModbusPointExporter.isRunning = false;
			if(in != null) in.close();
			if(out != null) out.close();
			if(file.exists()) FileUtil.deleteFile(file);
		}
	}
	
	public static void exportExcelV10(File file, String addrFormat, ArrayList<ModbusWatchPoint> pointList) throws IOException{
		
	}
	
	public static void exportExcelV10_Modbus(File file, String addrFormat, ArrayList<ModbusWatchPoint> pointList) throws IOException{
		ModbusPointExporter.isRunning = true;
		FileInputStream in = null;
		FileOutputStream out = null;
		
		try {
			in = new FileInputStream(file);
			
			XSSFWorkbook workbook = new XSSFWorkbook(in);
	
			XSSFCellStyle leftAlign = workbook.createCellStyle();
			leftAlign.setAlignment(HorizontalAlignment.LEFT);
			
			XSSFCellStyle centerAlign = workbook.createCellStyle();
			centerAlign.setAlignment(HorizontalAlignment.CENTER);
	
			XSSFSheet pointSheet = workbook.getSheetAt(1);
			XSSFSheet labelSheet = workbook.getSheetAt(2);
			
			int header = ExcelUtil.getHeaderRowNum(pointSheet, 0);
			int labelHeader = ExcelUtil.getHeaderRowNum(labelSheet, 0);
			
			for(ModbusWatchPoint point : pointList) {
				Row row = pointSheet.createRow(++header);
				
				for(int i = 0; i <= 13; i++) {
					row.createCell(i);
					
					if(i == 0) {
						row.getCell(i).setCellStyle(leftAlign);
					}else {
						row.getCell(i).setCellStyle(centerAlign);
					}
				}
				
				row.getCell(0).setCellValue(point.getDisplayName());
				row.getCell(2).setCellValue(point.getMeasure());
				row.getCell(3).setCellValue(point.getFunctionCode());
				if(addrFormat.contains("HEX")) {
					row.getCell(4).setCellValue(point.getRegisterAddrHexString());
				}else {
					row.getCell(4).setCellValue(point.getRegisterAddr() + 1);
				}
				row.getCell(5).setCellValue(point.getDataType());
				row.getCell(6).setCellValue(point.getScaleFunction());
				row.getCell(7).setCellValue(60);
				row.getCell(8).setCellValue(60);
				
				String dataFormat = "";
				switch(point.getDataFormat()) {
					case PerfConf.DATA_FORMAT_DIGITAL :
						dataFormat = (Moon.isKorean()) ? "이진값" : "Boolean";
						String[] binaryLabel = point.getBinLabel();
						if(binaryLabel != null) {
							row.getCell(12).setCellValue(binaryLabel[0]);
							row.getCell(13).setCellValue(binaryLabel[1]);
						}
						break;
						
					case PerfConf.DATA_FORMAT_STATUS :
						dataFormat = (Moon.isKorean()) ? "다중값" : "Multi-State";
						PerfLabelStatusBean[] labels = point.getStatusLabels();
						if(labels != null) {
							for(PerfLabelStatusBean label : labels) {
								Row labelRow = labelSheet.createRow(++labelHeader);
								for(int i = 0; i <= 2; i++) {
									labelRow.createCell(i);
									if(i == 1) {
										labelRow.getCell(i).setCellStyle(centerAlign);
									}else {
										labelRow.getCell(i).setCellStyle(leftAlign);
									}
								}
								labelRow.getCell(0).setCellValue(point.getDisplayName());
								labelRow.getCell(1).setCellValue(label.value);
								labelRow.getCell(2).setCellValue(label.label);
							}
						}
						break;
						
					case PerfConf.DATA_FORMAT_MEASURE :
						dataFormat = (Moon.isKorean()) ? "실수값" : "Real Number";
						break;
						
					default :
						dataFormat = (Moon.isKorean()) ? "실수값" : "Real Number";
						break;
				}
				row.getCell(9).setCellValue(dataFormat);
			}

			out = new FileOutputStream(file);
			workbook.write(out);
			out.flush();
			
			downloadFile(file);
			
		}finally {
			ModbusPointExporter.isRunning = false;
			if(in != null) in.close();
			if(out != null) out.close();
			if(file.exists()) FileUtil.deleteFile(file);
		}
	}
	
	public static void exportExcelV10_PLC(File file, String addrFormat, ArrayList<ModbusWatchPoint> pointList) throws IOException{
		
		String plcID = "";
		String deviceAlias = null;
		boolean useMemoryBit = false;
		boolean hasMemoryBit = ModbusWatchPoint.checkHasMemoryBit(pointList);
		
		String[] plcInfo = getPlcInfo(hasMemoryBit);
		
		if(plcInfo != null) {
			plcID = (plcInfo[0] != null && !plcInfo[0].isEmpty() ) ? plcInfo[0] : "";
			deviceAlias = (plcInfo[1] != null && !plcInfo[1].isEmpty() ) ? plcInfo[1] : null;
			useMemoryBit = (plcInfo[2] != null && !plcInfo[2].isEmpty() ) ? Boolean.parseBoolean(plcInfo[2]) : false;
			
			if(deviceAlias != null) {
				ArrayList<ModbusWatchPoint> clonePointList = new ArrayList<ModbusWatchPoint>();
				
				for(String alias : deviceAlias.split(",")) {
					for(ModbusWatchPoint point : pointList) {
						ModbusWatchPoint clonePoint = new ModbusWatchPoint();
						clonePoint.copy(point);
						clonePoint.setDeviceAlias(alias.trim());
						clonePointList.add(clonePoint);
					}
				}
				
				pointList = clonePointList;
			}
		}else {
			return;
		}
		
		ModbusPointExporter.isRunning = true;
		FileInputStream in = null;
		FileOutputStream out = null;
		
		try {
			in = new FileInputStream(file);
			
			XSSFWorkbook workbook = new XSSFWorkbook(in);
	
			XSSFCellStyle leftAlign = workbook.createCellStyle();
			leftAlign.setAlignment(HorizontalAlignment.LEFT);
			leftAlign.setBorderTop(BorderStyle.THIN);
			leftAlign.setBorderBottom(BorderStyle.THIN);
			leftAlign.setBorderLeft(BorderStyle.THIN);
			leftAlign.setBorderRight(BorderStyle.THIN);
			
			XSSFCellStyle centerAlign = workbook.createCellStyle();
			centerAlign.setAlignment(HorizontalAlignment.CENTER);
			centerAlign.setBorderTop(BorderStyle.THIN);
			centerAlign.setBorderBottom(BorderStyle.THIN);
			centerAlign.setBorderLeft(BorderStyle.THIN);
			centerAlign.setBorderRight(BorderStyle.THIN);
	
			XSSFSheet pointSheet = workbook.getSheetAt(1);
			XSSFSheet labelSheet = workbook.getSheetAt(2);
			
			int ID = 0;
			int header = ExcelUtil.getHeaderRowNum(pointSheet, 2);
			int labelHeader = ExcelUtil.getHeaderRowNum(labelSheet, 1);
						
			Cell plcID_Cell = pointSheet.getRow(1).getCell(1);
			plcID_Cell.setCellValue(plcID);
			
			for(ModbusWatchPoint point : pointList) {
				ID++;
				Row row = pointSheet.createRow(++header);
				
				for(int i = 0; i <= 18; i++) {
					row.createCell(i);
					
					if(i == 3) {
						row.getCell(i).setCellStyle(leftAlign);
					}else {
						row.getCell(i).setCellStyle(centerAlign);
					}
				}
				
				row.getCell(0).setCellValue(ID);
				row.getCell(1).setCellValue(point.getDeviceAlias());
				row.getCell(2).setCellValue(ID);
				row.getCell(3).setCellValue(point.getDisplayName());
				row.getCell(5).setCellValue(point.getMeasure());
				row.getCell(6).setCellValue(point.getFunctionCode());
				if(addrFormat.contains("HEX")) {
					row.getCell(7).setCellValue(point.getRegisterAddrHexString());
				}else {
					row.getCell(7).setCellValue(point.getRegisterAddr() + 1);
				}
				
				if(useMemoryBit) {
					if(point.getMemoryBit() != null) {
						try {
							int memoryBit = Integer.parseInt(point.getMemoryBit());
							row.getCell(8).setCellValue(memoryBit);
							row.getCell(10).setCellValue("x");
						}catch(Exception e) {							
							row.getCell(10).setCellValue(point.getScaleFunction());
						}
					}else {
						row.getCell(10).setCellValue(point.getScaleFunction());
					}
				}else {
					row.getCell(10).setCellValue(point.getScaleFunction());
				}
				
				row.getCell(9).setCellValue(point.getDataType());				
				row.getCell(11).setCellValue(60);
				row.getCell(12).setCellValue(60);								
				row.getCell(13).setCellValue(point.getDataFormat());
				row.getCell(18).setCellValue(0);
				
				switch(point.getDataFormat()) {
					case PerfConf.DATA_FORMAT_DIGITAL :
						String[] binaryLabel = point.getBinLabel();
						if(binaryLabel != null) {
							row.getCell(16).setCellValue(binaryLabel[0]);
							row.getCell(17).setCellValue(binaryLabel[1]);
						}
						break;
						
					case PerfConf.DATA_FORMAT_STATUS :						
						PerfLabelStatusBean[] labels = point.getStatusLabels();
						if(labels != null) {
							for(PerfLabelStatusBean label : labels) {
								Row labelRow = labelSheet.createRow(++labelHeader);
								for(int i = 0; i <= 3; i++) {
									labelRow.createCell(i);
									if(i == 3) {
										labelRow.getCell(i).setCellStyle(leftAlign);
									}else {
										labelRow.getCell(i).setCellStyle(centerAlign);
									}
								}
								labelRow.getCell(0).setCellValue(ID);
								labelRow.getCell(1).setCellValue(ID);
								labelRow.getCell(2).setCellValue(label.value);
								labelRow.getCell(3).setCellValue(label.label);
							}
						}
						break;
						
					case PerfConf.DATA_FORMAT_MEASURE :
						break;
						
					default :
						break;
				}
			}

			out = new FileOutputStream(file);
			workbook.write(out);
			out.flush();
			
			downloadFile(file);
			
		}finally {
			ModbusPointExporter.isRunning = false;
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
				StringBuilder sb = new StringBuilder();
				sb.append(Util.colorGreen("Template File Download Successful") + Util.separator + "\n");
				sb.append("아래의 경로에 템플릿 파일을 다운로드 완료하였습니다" + Util.separator + Util.separator + "\n\n");
				sb.append(Util.colorBlue("Path") + " : " + downloadFile.getAbsolutePath().replace("\\", Util.colorBlue("\\")) + Util.separator + Util.separator + "\n");
				Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}else {
			return;
		}
	}
	
	public static String[] getPlcInfo(boolean hasMemoryBit) {
		
		Font font = FontManager.getFont(Font.BOLD, 15);
		
		JLabel insert = new JLabel("<html><font color='green'>MK119 V10 PLC Template</font>" + Util.longSeparator + Util.longSeparator +  Util.longSeparator +"<br><br></html>");
		insert.setFont(font);
		
		JLabel plcID_label = new JLabel("<html><font color='blue'>PLC ID</font><br></html>");
		plcID_label.setFont(font);
		
		JLabel deviceAlias_label = new JLabel("<html><font color='blue'>Device Alias</font><br></html>");
		deviceAlias_label.setFont(font);
		
		JLabel memoryBit_label = new JLabel("<html><font color='blue'>Memory Bit</font><br></html>");
		memoryBit_label.setFont(font);
		
		font = FontManager.getFont(Font.PLAIN, 15);
		
		JTextField plcID = new JTextField();		
		plcID.setForeground(Color.BLACK);
		plcID.setFont(font);
		
		JTextField deviceAlias = new JTextField();
		deviceAlias.setForeground(Color.BLACK);
		deviceAlias.setFont(font);
		
		font = FontManager.getFont(Font.BOLD, 15);
		
		JCheckBox useMemoryBit = new JCheckBox(" Memory Bit Disable");
		useMemoryBit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(useMemoryBit.isSelected()) {
					useMemoryBit.setText(" Memory Bit Enable");
				}else {
					useMemoryBit.setText(" Memory Bit Disable");
				}
			}
		});
		useMemoryBit.setSelected(false);
		useMemoryBit.setForeground(Color.BLACK);
		useMemoryBit.setFont(font);
		useMemoryBit.setFocusPainted(false);
		
		int option = -1;
		
		if(hasMemoryBit) {
			
			Object[] message = {
					   insert,
					   plcID_label, plcID,
					   new JLabel("<html><br></html>"),
				       deviceAlias_label, deviceAlias,
				       new JLabel("<html><br></html>"),
				       memoryBit_label, useMemoryBit
				};
			
			option = JOptionPane.showConfirmDialog(null, message, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			
		}else {
			
			Object[] message = {
					   insert,
					   plcID_label, plcID,
					   new JLabel("<html><br></html>"),
				       deviceAlias_label, deviceAlias
				};
			
			option = JOptionPane.showConfirmDialog(null, message, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		}
		
		if(option == JOptionPane.OK_OPTION) {
			return new String[] { 
					plcID.getText().trim(), 
					deviceAlias.getText().trim(),
					String.valueOf(useMemoryBit.isSelected())
			};
			
		} else {
			return null;
			
		}
	}
	
}
