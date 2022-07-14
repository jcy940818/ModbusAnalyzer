package common.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelUtil {
	
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
