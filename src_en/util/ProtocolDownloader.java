package src_en.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ProtocolDownloader extends Thread{
	
	public static boolean running = false;
	private String downloadFile = null;
	
	private String buildVersion;
	private JTable table;	
	private String path;
	private String fileName;
	private boolean isFmsProtocol;
	
	public ProtocolDownloader(String buildVersion,JTable table, String path, String fileName, boolean isFmsProtocol) {
		this.buildVersion = buildVersion;
		this.table = table;		
		this.path = path;
		this.fileName = fileName;
		this.isFmsProtocol = isFmsProtocol;
	}
	
	@Override
	public void run() {
		try {		
			ProtocolDownloader.running = true;
			
			fileName = fileName
					.replace("\\", "")
					.replace("/", "")
					.replace(":", "")
					.replace("*", "")
					.replace("?", "")
					.replace("\"", "")
					.replace("<", "")
					.replace(">", "")
					.replace("|", "");
			
			downloadFile = path + "\\" + fileName +".xlsx";
			
			TableModel model = table.getModel();
			TableColumnModel columnModel = table.getColumnModel();		
			
			int columnCount = table.getColumnCount();
			int rowCount = table.getRowCount();			
			
			// Workbook 생성
			// new HSSFWorkbook(); // Excel 2007 버전 이하
			// new XSSFWorkbook(); // Excel 2007 버전 이상
			// new SXSSFWorkbook(); // Excel 2007 이상, 대용량 Excel 처리 적합 (쓰기 전용)
			
			Workbook xlsx = new SXSSFWorkbook(); // Excel 2007 버전 이상							
			
			// Sheet 생성 -----------------------------------------
			Sheet sheet = xlsx.createSheet(buildVersion);
			
			// Row, Cell 생성 -------------------------------------
			Row row = null;
			Cell cell = null;
			
			
			// 필터 전용 Cell 생성 -----------------------------------
			Cell firstFilterCell = null;
			Cell lastFilterCell = null;
			
			
			// 행 높이 ---------------------------------------------
			// 열 너비 설정 ------------------------------------------
			int length = 0;
			int headerLength = 0;
			int contentLength = 0;
			
			sheet.setDefaultRowHeight((short)400);
			
			// 헤더 스타일 설정 ------------------------------------------
			CellStyle headerStyle = xlsx.createCellStyle();
			headerStyle.setAlignment(HorizontalAlignment.CENTER);		
			headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			headerStyle.setBorderTop(BorderStyle.MEDIUM);
			headerStyle.setBorderBottom(BorderStyle.MEDIUM);
			headerStyle.setBorderLeft(BorderStyle.MEDIUM);
			headerStyle.setBorderRight(BorderStyle.MEDIUM);
			headerStyle.setFillForegroundColor(HSSFColorPredefined.YELLOW.getIndex());
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			
			
			Font headerFont = xlsx.createFont();
			headerFont.setFontName("맑은 고딕");
			headerFont.setFontHeight((short)240);
			headerFont.setBold(true);
			headerStyle.setFont(headerFont);
			
			
			// 일반 셀 스타일 설정 -----------------------------------------------
			CellStyle cellStyle = xlsx.createCellStyle();		
			cellStyle.setAlignment(HorizontalAlignment.LEFT);		
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setBorderTop(BorderStyle.THIN);
			cellStyle.setBorderBottom(BorderStyle.THIN);
			cellStyle.setBorderLeft(BorderStyle.THIN);
			cellStyle.setBorderRight(BorderStyle.THIN);		
			
			Font cellFont = xlsx.createFont();
			cellFont.setFontName("맑은 고딕");
			cellFont.setFontHeight((short)220); // 폰트 크기 : 11
			cellStyle.setFont(cellFont);
			
			// 첫 번째 열 스타일 ------------------------------------------------
			CellStyle centerStyle = xlsx.createCellStyle();
			centerStyle.setAlignment(HorizontalAlignment.CENTER);
			centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			centerStyle.setBorderTop(BorderStyle.THIN);
			centerStyle.setBorderBottom(BorderStyle.THIN);
			centerStyle.setBorderLeft(BorderStyle.THIN);
			centerStyle.setBorderRight(BorderStyle.THIN);		
			
			Font centerFont = xlsx.createFont();
			centerFont.setFontHeight((short)220); // 폰트 크기 : 11
			centerFont.setFontName("맑은 고딕");
			centerStyle.setFont(centerFont);
			
			// 배경색 셀 스타일 설정 (첫 번째 열 클론 ) -----------------------------------
			CellStyle yellowCell = xlsx.createCellStyle();
			yellowCell.cloneStyleFrom(centerStyle);
			yellowCell.setFillForegroundColor(HSSFColorPredefined.LIGHT_YELLOW.getIndex());
			yellowCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);			
			
			CellStyle orangeCell = xlsx.createCellStyle();
			orangeCell.cloneStyleFrom(yellowCell);
			orangeCell.setFillForegroundColor(HSSFColorPredefined.LIGHT_ORANGE.getIndex());
			orangeCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);			
			
			// 줄 바꿈 적용 -------------------------------------------------------
			headerStyle.setWrapText(true);
			
			
			// 컬럼(헤더) 초기화 ---------------------------------------------------
			row = sheet.createRow(0);
			for (int i = 0; i < columnCount; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columnModel.getColumn(i).getHeaderValue().toString());
				cell.setCellStyle(headerStyle);
				
				if(i == 0) {
					// 필터 기능 설정 용도
					firstFilterCell = cell;
				}
			}
			
			
			// 셀 내용 초기화 --------------------------------------------------------
			for (int i = 1; i <= rowCount; i++) {
				row = sheet.createRow(i);
				for (int j = 0; j < columnCount; j++) {
					cell = row.createCell(j);
					
					if(i == rowCount && (j == columnCount - 1)) {
						// 필터 기능 설정 용도
						lastFilterCell = cell;
					}
					
					try {
						
						String value = table.getValueAt(i - 1, j).toString();
						cell.setCellValue((value != null) ? value : "");
						cell.setCellStyle(centerStyle); // 셀 내용 가운데 정렬
						
						// 첫 번째 열의 내용을 검사 후 조건에 해당되면 배경색이 있는 셀 스타일 지정
						if(j == 0 && value.contains("PROTOCOL")) {
							cell.setCellStyle(yellowCell);
						}else if(j == 0 && value.contains("SNMP")) {
							cell.setCellStyle(orangeCell);
						}
							
					}catch(NullPointerException e) {
						
						cell.setCellValue("");
						cell.setCellStyle(cellStyle); // 셀 내용 왼쪽 정렬
						
						System.out.println("Null Content Cell [Row : " + (i-1) + " / Column : " + j + "]");						
						e.printStackTrace();
						continue;
					}
					
					// 다섯 번 째 열부터
					if (j >= 4) {
						// 프로토콜 이름
						// 성능 XML
						// 제어 XML
						cell.setCellStyle(cellStyle); // 셀 내용 왼쪽 정렬
					}
				}
			}
		
			int defaultExcelWidh = 300;
			sheet.setColumnWidth(0, defaultExcelWidh * 13); // 프로토콜 타입
			sheet.setColumnWidth(1, defaultExcelWidh * 12); // 시설물 코드
			sheet.setColumnWidth(2, defaultExcelWidh * 18); // 시설물 종류
			sheet.setColumnWidth(3, defaultExcelWidh * 18); // 프로토콜 번호
			sheet.setColumnWidth(4, defaultExcelWidh * 38); // 프로토콜 이름 (KO)
			
			if(isFmsProtocol) {
				sheet.setColumnWidth(5, defaultExcelWidh * 38); // 프로토콜 이름 (EN)
				sheet.setColumnWidth(6, defaultExcelWidh * 28); // 성능 XML
				sheet.setColumnWidth(7, defaultExcelWidh * 28); // 제어 XML
			}else {
				sheet.setColumnWidth(5, defaultExcelWidh * 28); // 성능 XML
				sheet.setColumnWidth(6, defaultExcelWidh * 28); // 제어 XML
			}
		
		sheet.setAutoFilter(new CellRangeAddress(firstFilterCell.getRowIndex(), lastFilterCell.getRowIndex(), firstFilterCell.getColumnIndex(), lastFilterCell.getColumnIndex()));		
		
		File xlsxFile = new File(downloadFile);
		FileOutputStream fileOut = new FileOutputStream(xlsxFile);
		xlsx.write(fileOut);
		fileOut.close();
		
		String msg = String.format("%s\nSuccessfully downloaded the file to the path below" + Util.separator +"\n\nPath : %s" + Util.separator,Util.colorGreen("Successful download of Protocol list")  ,xlsxFile.getParent());
		msg = msg + "\n\nFile : " + Util.colorBlue(xlsxFile.getName()) + Util.separator + Util.separator + "\n";
		Util.showMessage(msg, JOptionPane.INFORMATION_MESSAGE);
		
		System.out.println("Protocol Download Done : " + xlsxFile.getName() + " [ " + xlsxFile.getAbsolutePath() + " ]");
		
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			String msg = String.format("<font color='red'>File saving path error</font>"
					+ "\n"
					+ "The file cannot be downloaded to the path you selected"
					+ Util.separator
					+ "\n\n"
					+ "Please choose a different path"
					+ "\n\n"
					+ "%s"
					+ Util.separator
					+ "\n\n", e.getMessage());
			
			Util.showMessage(msg, JOptionPane.ERROR_MESSAGE);		
		}catch(Exception e) {
			e.printStackTrace();
			String msg = String.format("<font color='red'>Unknown error</font>"
					+ "\n"
					+ "An unknown error occurred while downloading the file"
					+ Util.separator
					+ "\n\n"
					+ "If you need any help, please contact Moon"
					+ "\n\n"
					+ "%s"
					+ Util.separator 
					+ "\n\n", e.getMessage());
			
			Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
		}finally {
			ProtocolDownloader.running = false;
		}
	}
	
}
