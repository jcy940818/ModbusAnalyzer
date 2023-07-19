package common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import src_ko.main.MoonInspector;
import src_ko.swing.MainFrame;

public class ProtocolTableGenerator {

	private static String initTable = 
			"IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.tables WITH(NOLOCK) WHERE TABLE_SCHEMA = 'dbo' AND TABLE_NAME = 'PROTOCOL')\n" + 
			"BEGIN\n" + 
			"	exec('DELETE FROM PROTOCOL')\n" + 
			"END\n" + 
			"ELSE\n" + 
			"BEGIN\n" + 
			"	exec('CREATE TABLE PROTOCOL (\n" + 
			"		strProtocolType nvarchar(50),		-- 프로토콜 타입\n" + 
			"		nFacType INT,						-- 시설물 코드\n" + 
			"		strFacType nvarchar(50),			-- 시설물 종류\n" + 
			"		nProtocolID INT,					-- 프로토콜 번호\n" + 
			"		strProtocolName varchar(100),		-- 프로토콜 이름(한글)\n" + 
			"		strProtocolName_En varchar(100),	-- 프로토콜 이름(영문)\n" + 
			"		strPerfXml varchar(100),			-- 성능 XML\n" + 
			"		strControlXml varchar(100),			-- 제어 XML\n" + 
			"		PRIMARY KEY (strProtocolType, nFacType, nProtocolID)\n" + 
			"	);');\n" + 
			"END\n\n";
	
	private static String baseQuery = 
			"INSERT INTO PROTOCOL ("
			+ "strProtocolType, "
			+ "nFacType, "
			+ "strFacType, "
			+ "nProtocolID, "
			+ "strProtocolName, "
			+ "strProtocolName_En, "
			+ "strPerfXml, "
			+ "strControlXml"
			+ ") VALUES ("
			+ "'=0=', "
			+ "=1=, "
			+ "'=2=', "
			+ "=3=, "
			+ "'=4=', "
			+ "'=5=', "
			+ "'=6=', "
			+ "'=7='"
			+ ");\n";
	
	public static void generate() throws Exception{
		
		if(!MoonInspector.isMoon()) return;
		
		String path = MainFrame.getCurrentPath();
		File currentDir = new File(path);
		
		for(File file : currentDir.listFiles()) {			
			if(file.getAbsolutePath().endsWith(".xlsx")) {
				try {
					double mk119Version = getMK119Version(file);
					String insertQuery = generateQuery(file, (mk119Version >= 4.5));
					
					if(insertQuery != null && !insertQuery.equals("")) {
						
						StringBuilder sb = new StringBuilder();
						sb.append("/*** " + file.getName().replace(".xlsx", "") + " ***/\n\n");
						sb.append(initTable);
						sb.append(insertQuery);
						
						File queryFile = new File(file.getParent() + "\\" + file.getName().replace(".xlsx", "") + ".txt");
						OutputStream out = new FileOutputStream(queryFile);
						out.write(sb.toString().getBytes());
						out.flush();
						out.close();
						
						System.out.println("Generate Successful : " + queryFile.getAbsolutePath());
					}
					
				}catch(Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		
	}
	
	public static String generateQuery(File file, boolean hasEngName) throws IOException{		
		StringBuilder query = new StringBuilder();

    	FileInputStream inputStream = null;
    	Cell cell = null;
    	
    	try {
			inputStream = new FileInputStream(file);
			Workbook workbook = new XSSFWorkbook(inputStream);
			
			Sheet sheet = workbook.getSheetAt(0);			
	
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
					
					int colNum = 0;
					
					String strProtocolType = ExcelUtil.getStringValue(row.getCell(colNum++));
					String nFacType = ExcelUtil.getStringValue(row.getCell(colNum++));
					String strFacType = ExcelUtil.getStringValue(row.getCell(colNum++));
					String nProtocolID = ExcelUtil.getStringValue(row.getCell(colNum++));
					String strProtocolName = ExcelUtil.getStringValue(row.getCell(colNum++));
					String strProtocolName_En = hasEngName ? ExcelUtil.getStringValue(row.getCell(colNum++)) : "NULL";
					String strPerfXml = ExcelUtil.getStringValue(row.getCell(colNum++));
					String strControlXml = ExcelUtil.getStringValue(row.getCell(colNum++));
					
					strProtocolType = strProtocolType.equals("-") ? "NULL" : strProtocolType;
					nFacType = nFacType.equals("-") ? "NULL" : nFacType;
					strFacType = strFacType.equals("-") ? "NULL" : strFacType;
					nProtocolID = nProtocolID.equals("-") ? "NULL" : nProtocolID;
					strProtocolName = strProtocolName.equals("-") ? "NULL" : strProtocolName;
					strProtocolName_En = strProtocolName_En.equals("-") ? "NULL" : strProtocolName_En;
					strPerfXml = strPerfXml.equals("-") ? "NULL" : strPerfXml;
					strControlXml = strControlXml.equals("-") ? "NULL" : strControlXml;
					
					String appendQuery = baseQuery
							.replace("=0=", strProtocolType)
							.replace("=1=", nFacType)
							.replace("=2=", strFacType)
							.replace("=3=", nProtocolID)
							.replace("=4=", strProtocolName)
							.replace("=5=", strProtocolName_En)
							.replace("=6=", strPerfXml)
							.replace("=7=", strControlXml)
							.replace("'NULL'", "NULL");
					
					query.append(appendQuery);
					
				}catch(Exception e) {
					e.printStackTrace();
					return null;
				}
				
			}// end while-loop
			
			return query.toString();
		
    	}finally {
    		if(inputStream != null) inputStream.close();
    		inputStream = null;
    	}
    }
	
	public static double getMK119Version(File xlsxFile) {
		return Double.parseDouble(xlsxFile.getName().split(" ")[1]);
	}

}
