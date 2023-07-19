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
			
			// Workbook ����
			// new HSSFWorkbook(); // Excel 2007 ���� ����
			// new XSSFWorkbook(); // Excel 2007 ���� �̻�
			// new SXSSFWorkbook(); // Excel 2007 �̻�, ��뷮 Excel ó�� ���� (���� ����)
			
			Workbook xlsx = new SXSSFWorkbook(); // Excel 2007 ���� �̻�							
			
			// Sheet ���� -----------------------------------------
			Sheet sheet = xlsx.createSheet(buildVersion);
			
			// Row, Cell ���� -------------------------------------
			Row row = null;
			Cell cell = null;
			
			
			// ���� ���� Cell ���� -----------------------------------
			Cell firstFilterCell = null;
			Cell lastFilterCell = null;
			
			
			// �� ���� ---------------------------------------------
			// �� �ʺ� ���� ------------------------------------------
			int length = 0;
			int headerLength = 0;
			int contentLength = 0;
			
			sheet.setDefaultRowHeight((short)400);
			
			// ��� ��Ÿ�� ���� ------------------------------------------
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
			headerFont.setFontName("���� ���");
			headerFont.setFontHeight((short)240);
			headerFont.setBold(true);
			headerStyle.setFont(headerFont);
			
			
			// �Ϲ� �� ��Ÿ�� ���� -----------------------------------------------
			CellStyle cellStyle = xlsx.createCellStyle();		
			cellStyle.setAlignment(HorizontalAlignment.LEFT);		
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setBorderTop(BorderStyle.THIN);
			cellStyle.setBorderBottom(BorderStyle.THIN);
			cellStyle.setBorderLeft(BorderStyle.THIN);
			cellStyle.setBorderRight(BorderStyle.THIN);		
			
			Font cellFont = xlsx.createFont();
			cellFont.setFontName("���� ���");
			cellFont.setFontHeight((short)220); // ��Ʈ ũ�� : 11
			cellStyle.setFont(cellFont);
			
			// ù ��° �� ��Ÿ�� ------------------------------------------------
			CellStyle centerStyle = xlsx.createCellStyle();
			centerStyle.setAlignment(HorizontalAlignment.CENTER);
			centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			centerStyle.setBorderTop(BorderStyle.THIN);
			centerStyle.setBorderBottom(BorderStyle.THIN);
			centerStyle.setBorderLeft(BorderStyle.THIN);
			centerStyle.setBorderRight(BorderStyle.THIN);		
			
			Font centerFont = xlsx.createFont();
			centerFont.setFontHeight((short)220); // ��Ʈ ũ�� : 11
			centerFont.setFontName("���� ���");
			centerStyle.setFont(centerFont);
			
			// ���� �� ��Ÿ�� ���� (ù ��° �� Ŭ�� ) -----------------------------------
			CellStyle yellowCell = xlsx.createCellStyle();
			yellowCell.cloneStyleFrom(centerStyle);
			yellowCell.setFillForegroundColor(HSSFColorPredefined.LIGHT_YELLOW.getIndex());
			yellowCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);			
			
			CellStyle orangeCell = xlsx.createCellStyle();
			orangeCell.cloneStyleFrom(yellowCell);
			orangeCell.setFillForegroundColor(HSSFColorPredefined.LIGHT_ORANGE.getIndex());
			orangeCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);			
			
			// �� �ٲ� ���� -------------------------------------------------------
			headerStyle.setWrapText(true);
			
			
			// �÷�(���) �ʱ�ȭ ---------------------------------------------------
			row = sheet.createRow(0);
			for (int i = 0; i < columnCount; i++) {
				cell = row.createCell(i);
				cell.setCellValue(columnModel.getColumn(i).getHeaderValue().toString());
				cell.setCellStyle(headerStyle);
				
				if(i == 0) {
					// ���� ��� ���� �뵵
					firstFilterCell = cell;
				}
			}
			
			
			// �� ���� �ʱ�ȭ --------------------------------------------------------
			for (int i = 1; i <= rowCount; i++) {
				row = sheet.createRow(i);
				for (int j = 0; j < columnCount; j++) {
					cell = row.createCell(j);
					
					if(i == rowCount && (j == columnCount - 1)) {
						// ���� ��� ���� �뵵
						lastFilterCell = cell;
					}
					
					try {
						
						String value = table.getValueAt(i - 1, j).toString();
						cell.setCellValue((value != null) ? value : "");
						cell.setCellStyle(centerStyle); // �� ���� ��� ����
						
						// ù ��° ���� ������ �˻� �� ���ǿ� �ش�Ǹ� ������ �ִ� �� ��Ÿ�� ����
						if(j == 0 && value.contains("PROTOCOL")) {
							cell.setCellStyle(yellowCell);
						}else if(j == 0 && value.contains("SNMP")) {
							cell.setCellStyle(orangeCell);
						}
							
					}catch(NullPointerException e) {
						
						cell.setCellValue("");
						cell.setCellStyle(cellStyle); // �� ���� ���� ����
						
						System.out.println("Null Content Cell [Row : " + (i-1) + " / Column : " + j + "]");						
						e.printStackTrace();
						continue;
					}
					
					// �ټ� �� ° ������
					if (j >= 4) {
						// �������� �̸�
						// ���� XML
						// ���� XML
						cell.setCellStyle(cellStyle); // �� ���� ���� ����
					}
				}
			}
		
			int defaultExcelWidh = 300;
			sheet.setColumnWidth(0, defaultExcelWidh * 13); // �������� Ÿ��
			sheet.setColumnWidth(1, defaultExcelWidh * 12); // �ü��� �ڵ�
			sheet.setColumnWidth(2, defaultExcelWidh * 18); // �ü��� ����
			sheet.setColumnWidth(3, defaultExcelWidh * 18); // �������� ��ȣ
			sheet.setColumnWidth(4, defaultExcelWidh * 38); // �������� �̸� (KO)
			
			if(isFmsProtocol) {
				sheet.setColumnWidth(5, defaultExcelWidh * 38); // �������� �̸� (EN)
				sheet.setColumnWidth(6, defaultExcelWidh * 28); // ���� XML
				sheet.setColumnWidth(7, defaultExcelWidh * 28); // ���� XML
			}else {
				sheet.setColumnWidth(5, defaultExcelWidh * 28); // ���� XML
				sheet.setColumnWidth(6, defaultExcelWidh * 28); // ���� XML
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
