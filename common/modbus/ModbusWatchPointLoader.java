package common.modbus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import common.perf.FmsPerfConf;
import common.perf.FmsPerfItem.EventInfo;
import common.perf.Perf;
import common.perf.PerfConf;
import common.perf.PerfLabelStatusBean;

public class ModbusWatchPointLoader {
	
	public static void main(String[] args) {
		try {
			File file = new File("C:\\OnionSoftware\\midknight\\conf\\ko\\fms\\AR56_COMP.xml");
			ModbusWatchPoint[] items = loadModbusWatchPointXML(file, "euc-kr");
			
			for (int i = 0; i < items.length; i++) {

				System.out.println(i + ". " + items[i].getDisplayName() + "  === >  " + items[i].getHexCounter() + "  ==>  " + items[i].getModbusAddr());

			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
        
    public static ModbusWatchPoint[] loadModbusWatchPointXlsx(File xlsxFile) throws IOException, ModbusWatchPointInitException{
    	
    	FileInputStream inputStream = null;
    	
    	try {
			inputStream = new FileInputStream(xlsxFile);
			Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			int numberOfRows = sheet.getPhysicalNumberOfRows();
			ModbusWatchPoint[] modbusWps = new ModbusWatchPoint[numberOfRows - 2];
	
			for (int i = 2; i < numberOfRows; i++) {
				Row row = sheet.getRow(i);
				
				if(row == null) break;
				
				modbusWps[i - 2] = new ModbusWatchPoint();
				
				if (row.getCell(1) == null) throw new IOException(Integer.toString(i+1) + "번째 행의 성능 이름이 없습니다");
				modbusWps[i - 2].displayName = CellUtil.getStringValue(row.getCell(1));
				
				if (row.getCell(2) == null) throw new IOException(Integer.toString(i+1) + "번째 행 (" + modbusWps[i - 2].displayName + ")의 성능 카운터가 없습니다");
				
				String counter = "";
				counter += CellUtil.getIntValue(row.getCell(2)) + "_";
				counter += CellUtil.getStringValue(row.getCell(3)).toLowerCase().contains("0x") ? CellUtil.getStringValue(row.getCell(3)) : CellUtil.getIntValue(row.getCell(3));
				counter += "_";
				counter += CellUtil.getStringValue(row.getCell(4));
				
				modbusWps[i - 2].counter = counter;
				modbusWps[i - 2].interval = row.getCell(5) != null ? CellUtil.getIntValue(row.getCell(5)) : 60;
				modbusWps[i - 2].measure = row.getCell(6) != null ? CellUtil.getStringValue(row.getCell(6)) : "";
				modbusWps[i - 2].scaleFunc = row.getCell(7) != null ? CellUtil.getStringValue(row.getCell(7)) : "x";
	
				if (row.getCell(10) != null) {
					modbusWps[i - 2].dataFormat = PerfConf.DATA_FORMAT_STATUS;
					String[] keys = CellUtil.getStringValue(row.getCell(10)).split(";");
					PerfLabelStatusBean[] statusLabels = new PerfLabelStatusBean[keys.length / 2];
					int j = 0;
					try {
						for (int k = 0; k < keys.length; k += 2) {
							statusLabels[j] = new PerfLabelStatusBean();
							statusLabels[j].value = Integer.parseInt(keys[k].trim());
							statusLabels[j].label = keys[k + 1].trim();
							j++;
						}
					} catch (Exception e) {
						throw new IOException(Integer.toString(i+1) + "번째 행 (" + modbusWps[i - 2].displayName + ")의 다중 성능 상태 라벨이 잘못되었습니다");
					}
					modbusWps[i - 2].labels = statusLabels;
				} else if (row.getCell(8) != null && row.getCell(9) != null) {
					modbusWps[i - 2].dataFormat = PerfConf.DATA_FORMAT_DIGITAL;
					modbusWps[i - 2].binLabel = new String[] { 
							CellUtil.getStringValue(row.getCell(8)),
							CellUtil.getStringValue(row.getCell(9)) };
				} else {
					modbusWps[i - 2].dataFormat = PerfConf.DATA_FORMAT_MEASURE;
				}
	
				if (row.getCell(11) != null && !CellUtil.getStringValue(row.getCell(11)).equalsIgnoreCase("")) {
					String item = "";
					Cell cell = null;
					try {
						EventInfo evt = new EventInfo();
						
						item = "severity";
						cell = row.getCell(11);
						evt.severity = CellUtil.getIntValue(cell);
						
						item = "threshold";
						cell = row.getCell(12);
						evt.threshold = CellUtil.getDoubleValue(cell);
						
						item = "op";
						cell = row.getCell(13);
						evt.op = CellUtil.getStringValue(cell);
						
						item = "mode";
						cell = row.getCell(14);
						evt.mode = CellUtil.getIntValue(cell);
						
						item = "duration";
						cell = row.getCell(15);
						evt.duration = CellUtil.getIntValue(cell);
						
						item = "count";
						cell = row.getCell(16);
						evt.count = CellUtil.getIntValue(cell);
						
						item = "seqCount";
						cell = row.getCell(17);
						evt.seqCount = CellUtil.getIntValue(cell);
						
						item = "autoReg";
						cell = row.getCell(18);
						evt.autoReg = CellUtil.getBooleanValue(cell);
						
						item = "name";
						cell = row.getCell(19);
						evt.name = CellUtil.getStringValue(cell);
						
						item = "msg";
						cell = row.getCell(20);
						evt.msg = CellUtil.getStringValue(cell);
						
						item = "enable";
						cell = row.getCell(21);
						evt.enable = CellUtil.getIntValue(cell);
						
						item = "autoClose";
						cell = row.getCell(22);
						evt.autoClose = CellUtil.getBooleanValue(cell);
						
						modbusWps[i - 2].evt = new EventInfo[] { evt };
					} catch (Exception e) {
						throw new IOException(Integer.toString(i+1) + "번째 행 (" + modbusWps[i - 2].displayName + ")의 이벤트 정보( " + item +" )가 잘못되었습니다");
					}
				}
			}
			
			modbusWps = trimWatchPointArray(modbusWps);
			
			// 모드버스 정보 초기화
			for(ModbusWatchPoint modbusWp : modbusWps) {
				modbusWp.init();
			}
			
			return modbusWps;
			
    	}finally {
    		if(inputStream != null) inputStream.close();
    		inputStream = null;
    		
    	}
    }
    
    
    
    public static ModbusWatchPoint[] loadModbusWatchPointXML(File xmlFile, String encoding) throws IOException, ModbusWatchPointInitException{
    	ArrayList<Perf> perfs = FmsPerfConf.getFmsPerfList(xmlFile, encoding);
    	ModbusWatchPoint[] modbusWps = new ModbusWatchPoint[perfs.size()];
    	
    	for(int i = 0; i < perfs.size(); i++) {
    		modbusWps[i] = new ModbusWatchPoint(perfs.get(i));
    		modbusWps[i].init();
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

class CellUtil {
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
