package common.modbus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.perf.FmsPerfItem.EventInfo;
import common.perf.PerfConf;
import common.perf.PerfLabelStatusBean;

public class ModbusWatchPointLoader {
	
	public static void main(String[] args) {
		try {
			File file = new File("C:\\Users\\Moon\\Desktop\\moon file.xlsx");
			ModbusWatchPoint[] items = loadModbusWatchPointXlsx(file);
			
			for (int i = 0; i < items.length; i++) {

				System.out.println(i + ". " + items[i].getDisplayName() + " === >" + items[i].getDecCounter() + " ==> " + items[i].getModbusAddr());

			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
        
    public static ModbusWatchPoint[] loadModbusWatchPointXlsx(File xlsxFile) throws IOException, ModbusWatchPointInitException{
		FileInputStream inputStream = new FileInputStream(xlsxFile);
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet sheet = workbook.getSheetAt(0);
		int numberOfRows = sheet.getPhysicalNumberOfRows();
		ModbusWatchPoint[] modbusWps = new ModbusWatchPoint[numberOfRows - 2];

		for (int i = 2; i < numberOfRows; i++) {
			Row row = sheet.getRow(i);
			
			if(row == null) break;
			
			modbusWps[i - 2] = new ModbusWatchPoint();
			if (row.getCell(1) == null)
				throw new IOException(Integer.toString(i+1) + "번째 행의 성능 이름이 없습니다");
			
			modbusWps[i - 2].displayName = row.getCell(1).getStringCellValue();
			
			if (row.getCell(2) == null)
				throw new IOException(Integer.toString(i+1) + "번째 행 (" + modbusWps[i - 2].displayName + ")의 성능 카운터가 없습니다");
			
			String counter = "";
			counter += Integer.toString((int) row.getCell(2).getNumericCellValue()) + "_";
			Cell addr = row.getCell(3);
			counter += addr.getCellType() == CellType.STRING ? addr.getStringCellValue() + "_" : (int) addr.getNumericCellValue() + "_";
			counter += row.getCell(4).getStringCellValue();
			
			modbusWps[i - 2].counter = counter;
			modbusWps[i - 2].interval = (row.getCell(5) == null || row.getCell(5).getCellType()!= CellType.NUMERIC) ? 60 : (int) row.getCell(5).getNumericCellValue();
			modbusWps[i - 2].measure = row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "";
			modbusWps[i - 2].scaleFunc = row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "x";

			if (row.getCell(10) != null) {
				modbusWps[i - 2].dataFormat = PerfConf.DATA_FORMAT_STATUS;
				String[] keys = row.getCell(10).getStringCellValue().split(";");
				PerfLabelStatusBean[] statusLabels = new PerfLabelStatusBean[keys.length / 2];
				int j = 0;
				try {
					for (int k = 0; k < keys.length; k += 2) {
						statusLabels[j] = new PerfLabelStatusBean();
						statusLabels[j].value = Integer.parseInt(keys[k].trim());
						statusLabels[j].label = keys[k + 1].trim();
						j++;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					throw new IOException(Integer.toString(i+1) + "번째 행 (" + modbusWps[i - 2].displayName + ")의 다중 성능 상태 라벨이 잘못되었습니다");
				}
				modbusWps[i - 2].labels = statusLabels;
			} else if (row.getCell(8) != null && row.getCell(9) != null) {
				modbusWps[i - 2].dataFormat = PerfConf.DATA_FORMAT_DIGITAL;
				modbusWps[i - 2].binLabel = new String[] { row.getCell(8).getStringCellValue(),
						row.getCell(9).getStringCellValue() };
			} else {
				modbusWps[i - 2].dataFormat = PerfConf.DATA_FORMAT_MEASURE;
			}

			if (row.getCell(11) != null) {
				try {
					EventInfo evt = new EventInfo();
					evt.severity = (int) row.getCell(11).getNumericCellValue();
					evt.threshold = (int) row.getCell(12).getNumericCellValue();
					evt.op = row.getCell(13).getStringCellValue();
					evt.mode = (int) row.getCell(14).getNumericCellValue();
					evt.duration = (int) row.getCell(15).getNumericCellValue();
					evt.count = (int) row.getCell(16).getNumericCellValue();
					evt.seqCount = (int) row.getCell(17).getNumericCellValue();
					evt.autoReg = row.getCell(18).getBooleanCellValue();
					evt.name = row.getCell(19).getStringCellValue();
					evt.msg = row.getCell(20).getStringCellValue();
					evt.enable = (int) row.getCell(21).getNumericCellValue();
					evt.autoClose = row.getCell(22).getBooleanCellValue();
					modbusWps[i - 2].evt = new EventInfo[] { evt };
				} catch (NullPointerException e) {
					throw new IOException(Integer.toString(i+1) + "번째 행 (" + modbusWps[i - 2].displayName + ")의 이벤트 정보가 잘못되었습니다");
				}
			}
		}
		
		modbusWps = trimWatchPointArray(modbusWps); 
		
		// 모드버스 정보 초기화
		for(ModbusWatchPoint modbusWp : modbusWps) {
			modbusWp.init();
		}
		
		return modbusWps;
    }
   
    
    private static ModbusWatchPoint[] trimWatchPointArray(ModbusWatchPoint[] ModbusWps){
    	int nullCounter = 0;
    	for(int i=0;i<ModbusWps.length;i++){
    		if(checkIsEmptyCell(ModbusWps[i])){
    			nullCounter = i;
    			break;
    		}
    	}
    	
    	if(nullCounter!=0){
    		ModbusWatchPoint[] newArray = new ModbusWatchPoint[nullCounter];
    		for(int i=0;i<newArray.length;i++){
    			newArray[i] = new ModbusWatchPoint();
    			newArray[i].displayName = ModbusWps[i].displayName;
    			newArray[i].counter = ModbusWps[i].counter;
    			newArray[i].measure = ModbusWps[i].measure;
    			newArray[i].scaleFunc = ModbusWps[i].scaleFunc;
    		}
    		return newArray;
    	} else {
    		return ModbusWps;
    	}
    }
    
    private static boolean checkIsEmptyCell(ModbusWatchPoint item){
    	return item.displayName.equals("") || item.counter.equals("0_0_\\{1}") || item.scaleFunc.equals("")|| !item.scaleFunc.contains("x");
    }
}
