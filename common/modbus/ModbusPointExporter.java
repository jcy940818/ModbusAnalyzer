package common.modbus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.perf.PerfConf;
import common.perf.PerfLabelStatusBean;
import common.util.ExcelUtil;
import moon.Moon;
import src_ko.swing.MainFrame;
import src_ko.util.FileUtil;
import src_ko.util.Util;

public class ModbusPointExporter {

	public static boolean isRunning = false;
	
	public static void export(int mkVersion, String addrFormat, ArrayList<ModbusWatchPoint> pointList) {
		new Thread(()->{
			try {
				String fileName = "Modbus";
				
				if(mkVersion >= 10) {
					StringBuilder msg = new StringBuilder();
					msg.append("<font color='Green'>MK119 V10 Template Type Selection</font>\n");				
					
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
						exportExcelV4(formFile, addrFormat, pointList);
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
	
	public static void exportXML(String addrFormat, ArrayList<ModbusWatchPoint> pointList) {
		
	}
	
	public static void exportExcelV4(File file, String addrFormat, ArrayList<ModbusWatchPoint> pointList) throws IOException{
		
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
					row.getCell(4).setCellValue(Integer.parseInt(point.getModbusAddrString()));
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
						PerfLabelStatusBean[] lables = point.getStatusLabels();
						if(lables != null) {
							for(PerfLabelStatusBean label : lables) {
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
			
			if(in != null) in.close();
			if(out != null) out.close();
			if(file.exists()) FileUtil.deleteFile(file);
			
		}finally {
			ModbusPointExporter.isRunning = false;
		}
	}
	
	public static void exportExcelV10_PLC(File file, String addrFormat, ArrayList<ModbusWatchPoint> pointList) throws IOException{
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
			
			for(ModbusWatchPoint point : pointList) {
				ID++;
				Row row = pointSheet.createRow(++header);
				
				for(int i = 0; i <= 18; i++) {
					row.createCell(i);
					
					if(i == 1 || i == 3) {
						row.getCell(i).setCellStyle(leftAlign);
					}else {
						row.getCell(i).setCellStyle(centerAlign);
					}
				}
				
				row.getCell(0).setCellValue(ID);
				row.getCell(1).setCellValue("");
				row.getCell(2).setCellValue(ID);
				row.getCell(3).setCellValue(point.getDisplayName());
				row.getCell(5).setCellValue(point.getMeasure());
				row.getCell(6).setCellValue(point.getFunctionCode());
				if(addrFormat.contains("HEX")) {
					row.getCell(7).setCellValue(point.getRegisterAddrHexString());
				}else {
					row.getCell(7).setCellValue(Integer.parseInt(point.getModbusAddrString()));
				}
				row.getCell(9).setCellValue(point.getDataType());
				row.getCell(10).setCellValue(point.getScaleFunction());
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
						PerfLabelStatusBean[] lables = point.getStatusLabels();
						if(lables != null) {
							for(PerfLabelStatusBean label : lables) {
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
			
			if(in != null) in.close();
			if(out != null) out.close();
			if(file.exists()) FileUtil.deleteFile(file);
			
		}finally {
			ModbusPointExporter.isRunning = false;
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
	
}
