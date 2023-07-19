package src_ko.util;

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
		
		String msg = String.format("<font color='blue'>파일 다운로드 완료</font>\n아래의 경로에 성공적으로 파일을 저장 하였습니다" + Util.separator +"\n\nPath : %s" + Util.separator, xlsxFile.getParent());
		msg = msg + "\n\nFile : " + Util.colorBlue(xlsxFile.getName()) + Util.separator + Util.separator + "\n";
		Util.showMessage(msg, JOptionPane.INFORMATION_MESSAGE);
		
		}catch(FileNotFoundException e) {
			e.printStackTrace();
			String msg = String.format("<font color='red'>파일 저장 경로 에러</font>\n선택하신 경로에 파일을 저장 할 수 없습니다" + Util.separator + "\n\n다른 경로를 선택해주세요\n\n%s" + Util.separator + "\n", e.getMessage());
			Util.showMessage(msg, JOptionPane.ERROR_MESSAGE);		
		}catch(Exception e) {
			e.printStackTrace();
			String msg = String.format("<font color='red'>알 수 없는 오류</font>\n파일 저장 중 알 수 없는 오류가 발생하였습니다" + Util.separator + "\n\nMoon에게 문의해주세요\n\n%s" + Util.separator + "\n", e.getMessage());
			Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
		}finally {
			FileDownloader.running = false;
			FileDownloader.currentDownloadFile = null;
		}		
	}
	
}
