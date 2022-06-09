package common.modbus;

import javax.swing.JOptionPane;

import common.agent.PerfData;
import common.perf.FmsPerfItem;
import common.perf.Perf;
import common.perf.PerfLabelStatusBean;
import src_ko.util.Util;

public class ModbusWatchPoint extends FmsPerfItem implements Comparable {
	
	private int functionCode = 3;
	private int registerAddr;
	private int modbusAddr;
	private String dataType;
	private PerfData data;
	
	private String decCounter;
	private String hexCounter;
	
	// MK119 V10 : Liz 전용 필드
	private int deviceID;
	private int pointID;
	
	public ModbusWatchPoint() {
		
	}
	
	public ModbusWatchPoint(Perf perf) {
		this.displayName = perf.getDisplayName();
		this.counter = perf.getCounter();
		this.interval = perf.getInterval();
		this.measure = perf.getMeasure();
		this.scaleFunc = perf.getScaleFunction();
		this.dataFormat = perf.getDataFormat();
		this.binLabel = perf.getBinLabel();
		this.labels = perf.getStatusLabels();
		this.evt = perf.getFmsEventInfo();
		this.enable = perf.getEnable();
	}
	
	@Override
	public int compareTo(Object obj) {
		ModbusWatchPoint modbusWp = (ModbusWatchPoint)obj;
		
		int A_functionCode = this.getFunctionCode();
		int B_functionCode = modbusWp.getFunctionCode();
		
		int A_address = this.getRegisterAddr();
		int B_address = modbusWp.getRegisterAddr();
		
		String A_scaleFunction = this.getScaleFunction();
		String B_scaleFunction = modbusWp.getScaleFunction();
		
		if(A_functionCode < B_functionCode) {
			return -1;
		}else if(A_functionCode == B_functionCode) {
			// 기능코드가 동일한 경우
			
			if(A_address < B_address) {
				return -1;
			}else if(A_address == B_address) {
				// 기능코드와 주소까지 동일한 경우
				if(A_scaleFunction.length() < B_scaleFunction.length()) {
					return -1;
				}else if(A_scaleFunction.length() == B_scaleFunction.length()) {					
					try {
						if ((A_scaleFunction.contains(">>") && (B_scaleFunction.contains(">>")))) {
							int A_bit = Integer.parseInt(A_scaleFunction.split(">>")[1].split(")")[0]);
							int B_bit = Integer.parseInt(B_scaleFunction.split(">>")[1].split(")")[0]);
							
							if(A_bit < B_bit) {
								return -1;
							}else if(A_bit == B_bit) {
								return 0;
							}else {
								return 1;
							}
						}else {
							return A_scaleFunction.compareToIgnoreCase(B_scaleFunction);
						}
					}catch(Exception e) {
						return A_scaleFunction.compareToIgnoreCase(B_scaleFunction);
					}
				}else {
					return 1;
				}			
			}else {
				return 1;
			}		
		}else {
			return 1;
		}
	}
		
	public int getDeviceID() {
		return deviceID;
	}

	public int getPointID() {
		return pointID;
	}

	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}

	public void setPointID(int pointID) {
		this.pointID = pointID;
	}

	public int getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(int functionCode) {
		this.functionCode = functionCode;
	}
	public int getRegisterAddr() {
		return registerAddr & 0xffff;
	}
	public void setRegisterAddr(int registerAddr) {
		this.registerAddr = registerAddr;
	}
	public int getModbusAddr() {
		return modbusAddr;
	}
	public void setModbusAddr(int modbusAddr) {
		this.modbusAddr = modbusAddr;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public PerfData getData() {
		return data;
	}
	public void setData(PerfData data) {
		this.data = data;
	}
	public String getDecCounter() {
		return decCounter;
	}

	public String getHexCounter() {
		return hexCounter;
	}

	public void setDecCounter(String decCounter) {
		this.decCounter = decCounter;
	}

	public void setHexCounter(String hexCounter) {
		this.hexCounter = hexCounter;
	}

	public Object getDataContent() {
		return PerfData.getPerfLastContent(this, this.data);
	}
	
	public void init() throws ModbusWatchPointInitException{
		
		try {
			
			// 모드버스 관제점 드라이버 코드 유효성 검사
			if(!this.getCounter().contains("_")) {
				throw new ModbusWatchPointInitException();
				
			}else if(this.getCounter().split("_").length != 3){
				throw new ModbusWatchPointInitException();
			}
			
			int functionCode;
			int registerAddr;
			int modbusAddr;
			String dataType;
			
			String[] counterToken = this.getCounter().split("\\\\")[0].split("_");
			
			functionCode = Integer.parseInt(counterToken[0]);
			
			// 기능 코드 : FC01 ~ FC04
			if(functionCode < 1 || functionCode > 4) {
				throw new ModbusWatchPointInitException();
			}
			
			if (counterToken[1].startsWith("0x") || counterToken[1].startsWith("0X")) {
				registerAddr = Integer.parseInt(counterToken[1].replace("0x", "").replace("0X", ""), 16);
			} else {
				registerAddr = Integer.parseInt(counterToken[1]);
				registerAddr %= 10000;
				registerAddr--;
			}
	
			dataType = counterToken[2];
			
			this.setFunctionCode(functionCode);
			this.setRegisterAddr(registerAddr);
			this.setModbusAddr(Integer.parseInt(this.getModbusAddrString()));
			this.setDataType(dataType);
			this.setDecCounter(functionCode + "_" + (this.getRegisterAddr() + 1) + "_" + dataType);
			this.setHexCounter(functionCode + "_" + (this.getRegisterAddrHexString()) + "_" + dataType);
			
		}catch(Exception e) {
			throw new ModbusWatchPointInitException(this.getDisplayName());
			
		}
	}
	
	public ModbusWatchPoint getClone() {
		try {
			ModbusWatchPoint clone = new ModbusWatchPoint();
			clone.displayName = this.displayName;
			clone.counter = this.counter;
			clone.scaleFunc = this.scaleFunc;
			clone.interval = this.interval;
			clone.measure = this.measure;
			clone.dataFormat = this.dataFormat;
			clone.binLabel = this.binLabel.clone();
			if(this.labels != null) {
				clone.labels = this.labels.clone();	
			}
			clone.init();
			return clone;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getRegisterAddrHexString() {
		return String.format("0x%04X", this.getRegisterAddr());
	}
	
	public String getModbusAddrString() {
		String modbus = "";
		
		switch(this.getFunctionCode()) {
			case 1: modbus = "0"; break;
			case 2: modbus = "1"; break;
			case 3: modbus = "4"; break;
			case 4: modbus = "3"; break;
			case 5: modbus = "0"; break;
			case 6: modbus = "4"; break;
		}
		
		return String.format("%s%04d", modbus, this.getRegisterAddr() + 1);
	}
	
	public static void showInfo(ModbusWatchPoint wp) {
		if(wp != null) {			
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s%s%s\n", Util.colorGreen("Modbus Point Information"), Util.separator, Util.separator));
			
			sb.append(String.format("%s : %s", Util.colorBlue("1. Name"), wp.getDisplayName()));
			sb.append(Util.separator + Util.separator + "\n\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("2. Counter"), wp.getCounter()));
			sb.append(Util.separator + Util.separator + "\n");
			
			
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(String.format("%s", Util.colorBlue("│")));
			sb.append(Util.separator + Util.separator + "\n");
			
			
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(String.format("%s : %s", Util.colorBlue("├&nbsp;&nbsp;Function Code"), wp.getFunctionCode()));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(String.format("%s : %s", Util.colorBlue("├&nbsp;&nbsp;Modbus Address ( DEC )"), wp.getModbusAddrString()));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(String.format("%s : %d", Util.colorBlue("├&nbsp;&nbsp;Register Address ( DEC )"), wp.getRegisterAddr()));
			sb.append(Util.separator + Util.separator + "\n");
			
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(String.format("%s : %s", Util.colorBlue("├&nbsp;&nbsp;Register Address ( HEX )"), wp.getRegisterAddrHexString()));
			sb.append(Util.separator + Util.separator + "\n");
			
			
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(String.format("%s : %s", Util.colorBlue("└&nbsp;&nbsp;Data Type"), wp.getDataType()));
			sb.append(Util.separator + Util.separator + "\n\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("3. Measure"), wp.getMeasure()));
			sb.append(Util.separator + Util.separator + "\n\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("4. Scale Formula"), wp.getScaleFunction()));
			sb.append(Util.separator + Util.separator + "\n\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("5. Data Format"), wp.getDataFormat()));
			sb.append(Util.separator + Util.separator + "\n");
			
			if(wp.getDataFormat() == 1) {
				String[] binLabel = wp.getBinLabel();
				
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append(String.format("%s%s : %s&nbsp;&nbsp;%s&nbsp;&nbsp;%s : %s",
						Util.colorBlue("├&nbsp;&nbsp;"),
						Util.colorBlue("Value"),
						"0",
						Util.colorRed("/"),
						Util.colorBlue("Label"),
						binLabel[0]));
				sb.append(Util.separator + Util.separator + "\n");
				
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append(String.format("%s%s : %s&nbsp;&nbsp;%s&nbsp;&nbsp;%s : %s",
						Util.colorBlue("└&nbsp;&nbsp;"),
						Util.colorBlue("Value"),
						"1",
						Util.colorRed("/"),
						Util.colorBlue("Label"),
						binLabel[1]));
				sb.append(Util.separator + Util.separator + "\n");
				
			}else if(wp.getDataFormat() == 2) {
				PerfLabelStatusBean[] labels = wp.getStatusLabels();
				
				for(int i = 0; i < labels.length; i++) {
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					sb.append(String.format("%s%s : %s&nbsp;&nbsp;%s&nbsp;&nbsp;%s : %s",
							Util.colorBlue((i != (labels.length - 1)) ? "├&nbsp;&nbsp;" : "└&nbsp;&nbsp;"),
							Util.colorBlue("Value"),
							labels[i].value,
							Util.colorRed("/"),
							Util.colorBlue("Label"),
							labels[i].label));
					sb.append(Util.separator + Util.separator + "\n");
				}
			}
			
			Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
}