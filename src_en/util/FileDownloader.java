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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class FileDownloader extends Thread{
	
	public static final int MAX_WIDTH = 20000;
	
	public static boolean running = false;
	public static String currentDownloadFile = null;
	
	private JTable table;	
	private String path;
	
	public FileDownloader(JTable table, String path) {
		this.table = table;		
		this.path = path;
	}
	
	@Override
	public void run() {
		try {		
			FileDownloader.running = true;
			FileDownloader.currentDownloadFile = path + ".xlsx";
			
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
			Sheet sheet = xlsx.createSheet("ModbusAnalyzer");
			
			// Row, Cell 생성 -------------------------------------
			Row row = null;
			Cell cell = null;
				
			// 행 높이 ---------------------------------------------
			// 열 너비 설정 ------------------------------------------
			int length = 0;
			int headerLength = 0;
			int contentLength = 0;
			
			sheet.setDefaultRowHeight((short)400);
			
			for(int i = 0; i < rowCount; i++) {			
				for(int j = 0; j < columnCount; j++) {
					headerLength = columnModel.getColumn(j).getHeaderValue().toString().length();
					contentLength = table.getValueAt(i, j).toString().length();				
					length = Math.max(headerLength, contentLength) * 520;
					length = (length > MAX_WIDTH) ? MAX_WIDTH : length;
					
					if(j == 0) {
						// 순서 헤더
						if(i == 0) 
						sheet.setColumnWidth(j, 3000);
						
					}else {
						sheet.setColumnWidth(j, length);	
					}
				}
			}
			
			
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
			headerFont.setFontHeight((short)260);
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
			
			// 줄 바꿈 적용 -------------------------------------------------------
			headerStyle.setWrapText(true);
			
			
			// 컬럼(헤더) 초기화 ---------------------------------------------------
			row = sheet.createRow(0);		
			for(int i = 0; i < columnCount; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columnModel.getColumn(i).getHeaderValue().toString());
				cell.setCellStyle(headerStyle);			
			}
			
			
			// 셀 내용 초기화 --------------------------------------------------------
			for(int i = 1; i <= rowCount; i++) {
				row = sheet.createRow(i);
				for(int j = 0; j < columnCount; j++) {				
					cell = row.createCell(j);
					cell.setCellValue(table.getValueAt(i-1, j).toString());
					cell.setCellStyle(cellStyle);
					if(j == 0) cell.setCellStyle(centerStyle);
				}
			}
			
			
		
		File xlsxFile = new File(path + ".xlsx");
		FileOutputStream fileOut = new FileOutputStream(xlsxFile);
		xlsx.write(fileOut);
		fileOut.close();					
		
		String msg = String.format("<font color='blue'>File download completed</font>"
				+ "\n"
				+ "The file was successfully downloaded to the path"
				+ Util.separator + Util.separator
				+"\n\n"
				+ "Path : %s" 
				+ Util.separator + Util.separator
				, xlsxFile.getParent());
		
		msg = msg + "\n\nFile : " + Util.colorBlue(xlsxFile.getName()) + Util.separator + Util.separator + "\n";
		Util.showMessage(msg, JOptionPane.INFORMATION_MESSAGE);
		
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			String msg = String.format("<font color='red'>File storage path error</font>"
					+ "\n"
					+ "The file cannot be downloaded to the path you specified"
					+ Util.separator + Util.separator
					+ "\n\n"
					+ "Please specify a different download path"
					+ "\n\n%s"
					+ Util.separator + Util.separator
					+ "\n"
					, e.getMessage());
			
			Util.showMessage(msg, JOptionPane.ERROR_MESSAGE);		
		}catch(Exception e) {
			e.printStackTrace();
			String msg = String.format("<font color='red'>Unknown error</font>"
					+ "\n"
					+ "An unknown error occurred while downloading the file"
					+ Util.separator + Util.separator
					+ "\n\n%s"
					+ Util.separator + Util.separator
					+ "\n", e.getMessage());
			
			Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
		}finally {
			FileDownloader.running = false;
			FileDownloader.currentDownloadFile = null;
		}		
	}
	
}
