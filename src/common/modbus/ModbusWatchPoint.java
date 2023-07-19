package common.modbus;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import common.agent.PerfData;
import common.perf.FmsActionItem;
import common.perf.FmsPerfItem;
import common.perf.Perf;
import common.perf.PerfConf;
import common.perf.PerfLabelStatusBean;
import common.util.Calculator;
import moon.Moon;
import src_ko.agent.ModbusAgent;
import src_ko.util.Util;

public class ModbusWatchPoint extends FmsPerfItem implements Comparable {
	
	private int functionCode = 3;
	private int registerAddr;
	private int modbusAddr;
	private String dataType;
	private PerfData data = new PerfData();
	
	private String decCounter;
	private String regCounter;
	private String hexCounter;
	
	// MK119 V10 : Liz 전용 필드
	private String deviceAlias = "";
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
	public boolean equals(Object obj) {
		if (obj instanceof ModbusWatchPoint == false)
			return false;

		ModbusWatchPoint target = (ModbusWatchPoint) obj;

		if (this.displayName == null || target.displayName == null)
			return false;
		
		if (this.counter == null || target.counter == null)
			return false;

		if (this.measure == null || target.measure == null)
			return false;
		
		if (this.scaleFunc == null || target.scaleFunc == null)
			return false;

		if (this.dataFormat != target.dataFormat)
			return false;
		
		return this.displayName.equals(target.displayName)
				&& this.counter.equals(target.counter)
				&& this.measure.equals(target.measure)
				&& this.scaleFunc.equals(target.scaleFunc)
				&& this.dataFormat == target.dataFormat;
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
		
	public String getDeviceAlias() {
		return deviceAlias;
	}

	public void setDeviceAlias(String deviceAlias) {
		this.deviceAlias = deviceAlias;
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
	
	public String getRegCounter() {
		return regCounter;
	}

	public String getHexCounter() {
		return hexCounter;
	}

	public void setDecCounter(String decCounter) {
		this.decCounter = decCounter;
	}
	
	public void setRegCounter(String regCounter) {
		this.regCounter = regCounter;
	}

	public void setHexCounter(String hexCounter) {
		this.hexCounter = hexCounter;
	}

	public Object getDataContent() {
		return PerfData.getPerfContent(this, this.data);
	}
	
	public void init() throws ModbusPointInitException{
		
		try {
			
			// 모드버스 관제점 드라이버 코드 유효성 검사
			if(!this.getCounter().contains("_")) {
				throw new ModbusPointInitException();
				
			}else if(this.getCounter().split("_").length != 3){
				throw new ModbusPointInitException();
			}
			
			int functionCode;
			int registerAddr;
			int modbusAddr;
			String dataType;
			
			String[] counterToken = this.getCounter().split("\\\\")[0].split("_");
			
			functionCode = Integer.parseInt(counterToken[0].trim());
			
			// 기능 코드 : FC01 ~ FC04
			if(functionCode < 1 || functionCode > 4) {
				throw new ModbusPointInitException();
			}
			
			if (counterToken[1].startsWith("0x") || counterToken[1].startsWith("0X")) {
				registerAddr = Integer.parseInt(counterToken[1].replace("0x", "").replace("0X", "").trim(), 16);
			} else {
				registerAddr = Integer.parseInt(counterToken[1].trim());
				registerAddr %= 10000;
				registerAddr--;
			}
	
			dataType = counterToken[2].trim();
			if(ModbusMonitor.getDataType(dataType) == -1) {
				throw new ModbusPointInitException();
			}
			
			this.setFunctionCode(functionCode);
			this.setRegisterAddr(registerAddr);
			this.setModbusAddr(Integer.parseInt(this.getModbusAddrString()));
			this.setDataType(dataType);
			this.setDecCounter(functionCode + "_" + (this.getRegisterAddr() + 1) + "_" + dataType);
			this.setRegCounter(functionCode + "_" + this.getRegisterAddr() + "_" + dataType);
			this.setHexCounter(functionCode + "_" + (this.getRegisterAddrHexString()) + "_" + dataType);
			
		}catch(Exception e) {
			throw new ModbusPointInitException(this.getDisplayName());
			
		}
	}
	
	public void copy(ModbusWatchPoint clone) {
		try {
			this.displayName = clone.displayName;
			this.counter = clone.counter;
			this.scaleFunc = clone.scaleFunc;
			this.interval = clone.interval;
			this.measure = clone.measure;
			this.dataFormat = clone.dataFormat;
			
			if(this.dataFormat == 1) {
				this.binLabel[0] = clone.binLabel[0];
				this.binLabel[1] = clone.binLabel[1];
			}else if(this.dataFormat == 2) {
				PerfLabelStatusBean[] cloneBeans = clone.getStatusLabels();
				if(cloneBeans != null && cloneBeans.length > 0) {
					ArrayList<PerfLabelStatusBean> labelList = new ArrayList<PerfLabelStatusBean>();
					for(PerfLabelStatusBean bean : cloneBeans) {
						PerfLabelStatusBean copyBean = new PerfLabelStatusBean();
						copyBean.value = bean.value;
						copyBean.label = bean.label;
						labelList.add(copyBean);
					}
					this.labelList = labelList;
					this.setStatusLabels();
				}
			}
			
			this.init();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<FmsActionItem> convertToControl(boolean allFcCode, boolean generateDesc, boolean concatMeasure){
		ArrayList<FmsActionItem> actionList = null;
		boolean hexType = (this.getRegisterAddr() >= 9999 || this.getCounter().toLowerCase().contains("0x"));
		String control = Moon.isKorean() ? "제어" : "Control";
		
		if( !(allFcCode || this.functionCode == 0x01 || this.functionCode == 0x03) ) {
			// allFcCode 변수가 true 일 경우는 기능코드 2, 4번에 대해서도 제어를 생성한다
			return null;
		}
		
		try {
			actionList = new ArrayList<FmsActionItem>();
			
			if(this.functionCode < 0x03
					&& !this.binLabel[0].isEmpty()
					&& !this.binLabel[1].isEmpty()) {
				
				FmsActionItem control_ON = new FmsActionItem();
				FmsActionItem control_OFF = new FmsActionItem();
				
				StringBuilder cmd = new StringBuilder();
				cmd.append("5_");
				cmd.append(((hexType) ? getRegisterAddrHexString() : (getRegisterAddr() + 1) ));
				cmd.append("_" + this.dataType);
				
				control_ON.displayName = this.displayName + " " + control + " - " + this.binLabel[1];
				control_ON.counter = "CONTROL";
				control_ON.command = cmd.toString() + "_ON";
				control_ON.useParam = 0;
				control_ON.waitTime = 3;
				
				control_OFF.displayName = this.displayName + " " + control + " - " + this.binLabel[0];
				control_OFF.counter = "CONTROL";
				control_OFF.command = cmd.toString() + "_OFF";
				control_OFF.useParam = 0;
				control_OFF.waitTime = 3;
				
				if(generateDesc) {
					if(Moon.isKorean()) {
						control_ON.desc = this.displayName + " 항목을 [" + this.binLabel[1] + "] 상태로 제어";
						control_OFF.desc = this.displayName + " 항목을 [" + this.binLabel[0] + "] 상태로 제어";
					}else {
						control_ON.desc = "Controls the " + this.displayName + " to the [" + this.binLabel[1] + "] state";
						control_OFF.desc = "Controls the " + this.displayName + " to the [" + this.binLabel[0] + "] state";
					}
				}
				
				actionList.add(control_ON);
				actionList.add(control_OFF);
				
			}else if(this.functionCode >= 0x03) {

				int controlCode = this.dataType.toUpperCase().trim().startsWith("TWO") ? 6 : 16;

				// 워드 단위 이진 상태 제어
				if(this.dataFormat == PerfConf.DATA_FORMAT_DIGITAL
						&& !this.binLabel[0].isEmpty()
						&& !this.binLabel[1].isEmpty()) {
					
					// 워드 단위 비트 상태 제어
					FmsActionItem control_ON = new FmsActionItem();
					FmsActionItem control_OFF = new FmsActionItem();
					 
					String bitPosition = ( getMemoryBit() != null  && this.scaleFunc.replace(" ", "").trim().endsWith("&1") ) ? getMemoryBit() : "0";

					StringBuilder cmd = new StringBuilder();
					cmd.append(controlCode + "_");
					cmd.append(((hexType) ? getRegisterAddrHexString() : (getRegisterAddr() + 1) ));
					cmd.append("_" + this.dataType);
					cmd.append("_" + bitPosition);
					
					control_ON.displayName = this.displayName + " " + control + " - " + this.binLabel[1];
					control_ON.counter = "CONTROL";
					control_ON.command = cmd.toString() + "_ON";
					control_ON.useParam = 0;
					control_ON.waitTime = 3;
					
					control_OFF.displayName = this.displayName + " " + control + " - " + this.binLabel[0];
					control_OFF.counter = "CONTROL";
					control_OFF.command = cmd.toString() + "_OFF";
					control_OFF.useParam = 0;
					control_OFF.waitTime = 3;
					
					if(generateDesc) {
						if(Moon.isKorean()) {
							control_ON.desc = this.displayName + " 항목을 [" + this.binLabel[1] + "] 상태로 제어";
							control_OFF.desc = this.displayName + " 항목을 [" + this.binLabel[0] + "] 상태로 제어";
						}else {
							control_ON.desc = "Controls the " + this.displayName + " to the [" + this.binLabel[1] + "] state";
							control_OFF.desc = "Controls the " + this.displayName + " to the [" + this.binLabel[0] + "] state";
						}
					}
					
					actionList.add(control_ON);
					actionList.add(control_OFF);
					
				}else if(this.dataFormat == PerfConf.DATA_FORMAT_STATUS 
						&& this.getStatusLabels() != null 
						&& this.getStatusLabels().length > 0) {
					
					StringBuilder cmd = new StringBuilder();
					cmd.append(controlCode + "_");
					cmd.append(((hexType) ? getRegisterAddrHexString() : (getRegisterAddr() + 1) ));
					cmd.append("_" + this.dataType);
					
					for(PerfLabelStatusBean status : this.getStatusLabels()) {
						
						FmsActionItem statusControl = new FmsActionItem();
						
						statusControl.displayName = this.displayName + " " + control + " - " + status.label;
						statusControl.counter = "CONTROL";
						statusControl.command = cmd.toString() + "," + status.value;
						statusControl.useParam = 0;
						statusControl.waitTime = 3;

						if(generateDesc) {
							if(Moon.isKorean()) {
								statusControl.desc = this.displayName + " 항목을 [" + status.label + "] 상태로 제어";
							}else {
								statusControl.desc = "Controls the " + this.displayName + " to the [" + status.label + "] state";
							}
						}
						
						actionList.add(statusControl);
					}
					
				}else if(this.dataFormat == PerfConf.DATA_FORMAT_MEASURE){
					
					FmsActionItem controlValue = new FmsActionItem();
					
					StringBuilder cmd = new StringBuilder();
					cmd.append(controlCode + "_");
					cmd.append(((hexType) ? getRegisterAddrHexString() : (getRegisterAddr() + 1) ));
					cmd.append("_" + this.dataType);
					
					controlValue.displayName = this.displayName + " " + control;
					controlValue.counter = "CONTROL";
					controlValue.useParam = 1;
					controlValue.waitTime = 3;
					
					if(this.scaleFunc.trim().toLowerCase().equalsIgnoreCase("x")) {
						controlValue.command = cmd.toString();
					}else {
						controlValue.command = cmd.toString() + "," + getReverseScale();
					}
					
					if(generateDesc) {
						if(Moon.isKorean()) {
							controlValue.desc = this.displayName + " 항목 제어";
						}else {
							controlValue.desc = "Controls " + this.displayName + " item";
						}
						
						if(concatMeasure && !this.measure.isEmpty()) {
							controlValue.desc += " ( " + this.measure + " )";
						}
					}
					
					actionList.add(controlValue);
				}else {
					// 알 수 없는 데이터 형식
					return null;
				}
				
			}else {
				// 알 수 없는 기능코드
				return null;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return actionList;
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
			
			sb.append(String.format("%s : %s", Util.colorBlue("1. Point Name"), wp.getDisplayName()));
			sb.append(Util.separator + Util.separator + "\n\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("2. Counter ( Driver Code )"), wp.getCounter()));
			sb.append(Util.separator + Util.separator + Util.separator + Util.separator + "\n");
			
			
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(String.format("%s", Util.colorBlue("│")));
			sb.append(Util.separator + Util.separator + Util.separator + Util.separator + "\n");
			
			
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(String.format("%s : %s", Util.colorBlue("├&nbsp;&nbsp;Function Code"), wp.getFunctionCode()));
			sb.append(Util.separator + Util.separator + Util.separator + Util.separator + "\n");
			
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(String.format("%s : %s", Util.colorBlue("├&nbsp;&nbsp;Modbus Address ( DEC )"), wp.getModbusAddrString()));
			sb.append(Util.separator + Util.separator + Util.separator + Util.separator + "\n");
			
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(String.format("%s : %d", Util.colorBlue("├&nbsp;&nbsp;Register Address ( DEC )"), wp.getRegisterAddr()));
			sb.append(Util.separator + Util.separator + Util.separator + Util.separator + "\n");
			
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(String.format("%s : %s", Util.colorBlue("├&nbsp;&nbsp;Register Address ( HEX )"), wp.getRegisterAddrHexString()));
			sb.append(Util.separator + Util.separator + Util.separator + Util.separator + "\n");
			
			
			sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(String.format("%s : %s", Util.colorBlue("└&nbsp;&nbsp;Data Type"), wp.getDataType()));
			sb.append(Util.separator + Util.separator + "\n\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("3. Measure"), wp.getMeasure()));
			sb.append(Util.separator + Util.separator + "\n\n");
			
			sb.append(String.format("%s : %s", Util.colorBlue("4. Scale Formula"), wp.getScaleFunction()));
			sb.append(Util.separator + Util.separator + "\n\n");
			
			String dataFormat = null;
			switch(wp.getDataFormat()) {
				case 1 : 
					dataFormat = "1 ( Binary State )";
					break;
				case 2 : 
					dataFormat = "2 ( Multiple State )";
					break;
				case 3 : 
					dataFormat = "3 ( Analog )";
					break;
				default : 
					dataFormat = "3 ( Analog )";
					break;
			}
			
			sb.append(String.format("%s : %s", Util.colorBlue("5. Data Format"), dataFormat));
			sb.append(Util.separator + Util.separator + Util.separator + Util.separator + "\n");
			
			if(wp.getDataFormat() == 1) {
				String[] binLabel = wp.getBinLabel();
				
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append(String.format("%s", Util.colorBlue("│")));
				sb.append(Util.separator + Util.separator + Util.separator + Util.separator + "\n");
				
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append(String.format("%s%s : %s&nbsp;&nbsp;%s&nbsp;&nbsp;%s : %s",
						Util.colorBlue("├&nbsp;&nbsp;"),
						Util.colorBlue("Value"),
						"0",
						Util.colorRed("/"),
						Util.colorBlue("Label"),
						binLabel[0]));
				sb.append(Util.separator + Util.separator + Util.separator + Util.separator + "\n");
								
				
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append(String.format("%s%s : %s&nbsp;&nbsp;%s&nbsp;&nbsp;%s : %s",
						Util.colorBlue("└&nbsp;&nbsp;"),
						Util.colorBlue("Value"),
						"1",
						Util.colorRed("/"),
						Util.colorBlue("Label"),
						binLabel[1]));
				sb.append(Util.separator + Util.separator + Util.separator + Util.separator + "\n");
				
			}else if(wp.getDataFormat() == 2) {
				PerfLabelStatusBean[] labels = wp.getStatusLabels();
				
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				sb.append(String.format("%s", Util.colorBlue("│")));
				sb.append(Util.separator + Util.separator + Util.separator + Util.separator + "\n");
				
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
					sb.append(Util.separator + Util.separator + Util.separator + Util.separator + "\n");
				}
			}
			
			Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	/**
	 * 전달받은 ModbusWatchPoint 인스턴스의 비트 구조를 출력한다
	 */
	public static void showBitStatus(ModbusWatchPoint point, long lastValue) {
		
		String dataType = point.getDataType();
		if(!dataType.contains(" INT ")){
			if(Moon.isKorean()) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Unsupported Data Type</font>\n");
				sb.append(String.format("선택하신 포인트의 데이터 타입은 <font color='blue'>Bit Structure</font> 기능을 지원하지 않습니다%s\n", Util.separator));																				
				sb.append(String.format("\n현재 선택된 포인트의 데이터 타입 : <font color='red'>%s</font>%s\n",dataType ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return;
			}else {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Unsupported Data Type</font>\n");
				sb.append(String.format("The %s you selected is not supported by <font color='blue'>Bit Structure</font> function%s\n", Util.colorBlue("Data Type"), Util.separator));																				
				sb.append(String.format("\nThe currently selected Data type : <font color='red'>%s</font>%s\n",dataType ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		int functionCode = point.getFunctionCode();
		String currentFunctionCode = null;
		
		switch(functionCode) {
			case 01 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC01); break;
			case 02 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC02); break;
			case 03 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC03); break;
			case 04 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC04); break;
			case 05 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC05); break;
			case 06 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC06); break;
			case 15 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC15); break;
			case 16 : currentFunctionCode = String.format("%d (%s)",functionCode ,ModbusAgent.FC16); break;
		}
		
		if (!(functionCode == 3 || functionCode == 4)) {
			if(Moon.isKorean()) {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Unsupported Function Code</font>\n");
				sb.append(String.format("현재 기능코드는 <font color='blue'>Bit Structure</font> 기능을 지원하지 않습니다%s\n", Util.separator));																				
				sb.append(String.format("\n현재 선택된 기능코드 : <font color='red'>%s</font>%s\n",currentFunctionCode ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return;	
			}else {
				StringBuilder sb = new StringBuilder();
				sb.append("<font color='red'>Unsupported Function Code</font>\n");
				sb.append(String.format("The %s you selected is not supported by <font color='blue'>Bit Structure</font> function%s\n", Util.colorBlue("Function Code"), Util.separator));																				
				sb.append(String.format("\nThe currently selected Function Code : <font color='red'>%s</font>%s\n",currentFunctionCode ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		int bitCount = 16; // Default : TWO BYTE (16Bit)
		
		if (point.getDataType().startsWith("TWO")) {
			bitCount = 16;
		}else if (point.getDataType().startsWith("FOUR")) {
			bitCount = 32;
		}else if (point.getDataType().startsWith("EIGHT")) {
			bitCount = 64;
		}else {
			bitCount = 16;
		}
		
		StringBuilder bitBuilder = new StringBuilder();
		
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='green'>Bit Structure</font>\n");
		
		sb.append(String.format(" <font color='blue'>Point Name</font> : %s%s%s\n",point.getDisplayName() ,Util.separator, Util.separator));
		sb.append(String.format(" <font color='blue'>Data Type</font> : %s%s%s\n",dataType ,Util.separator, Util.separator));
		sb.append(String.format(" <font color='blue'>Scale Formula</font> : %s%s%s\n\n",point.getScaleFunction() ,Util.separator, Util.separator));
		
		sb.append(String.format(" <font color='blue'>Modbus Address ( DEC )</font> : %s%s%s\n",point.getModbusAddrString() ,Util.separator, Util.separator));
		sb.append(String.format(" <font color='blue'>Register Address ( DEC )</font> : %d%s%s\n", point.getRegisterAddr()  ,Util.separator, Util.separator));
		sb.append(String.format(" <font color='blue'>Register Address ( HEX )</font> : %s%s%s\n\n",point.getRegisterAddrHexString() ,Util.separator, Util.separator));
		
		sb.append(String.format(" <font color='blue'>Decimal</font> : %d%s%s\n",lastValue ,Util.separator, Util.separator));
		
		String hexString = String.format("%016X", lastValue);
		String A = hexString.substring(0, 4);
		String B = hexString.substring(4, 8);
		String C = hexString.substring(8, 12);
		String D = hexString.substring(12, 16);
		
		String hex = "";
		if(point.getDataType().startsWith("TWO")) {
			hex = (Util.colorGreen("0x") + D);
			
		}else if (point.getDataType().startsWith("FOUR")) {
			hex = (Util.colorGreen("0x") + C) + " ";
			hex += (Util.colorGreen("0x") + D);
			
		}else if (point.getDataType().startsWith("EIGHT")) {
			hex = (Util.colorGreen("0x") + A) + " ";
			hex += (Util.colorGreen("0x") + B) + " ";
			hex += (Util.colorGreen("0x") + C) + " ";
			hex += (Util.colorGreen("0x") + D);
			
		}else {
			hex = (Util.colorGreen("0x") + D);
		}
		sb.append(String.format(" <font color='blue'>Hexadecimal</font> : %s%s%s\n",hex ,Util.separator, Util.separator));
		
		sb.append(String.format(" <font color='blue'>Binary</font> : "));
		for(int i = 0; i < bitCount; i++) {
			long bitStatus = (lastValue >> i) & 1;
			String content = (bitStatus == 1) ? "1" : "0";
			
			if (i % 4 == 0) {
				bitBuilder.append(String.format(" %s", content));
			}else{
				bitBuilder.append(String.format("%s", content));
			}						
		}
		sb.append(String.format("%s%s%s\n\n", bitBuilder.reverse().toString().replaceAll("1", Util.colorRed("1")), Util.separator, Util.separator));
		
		for(int i = 0; i < bitCount; i++) {
			long bitStatus = (lastValue >> i) & 1;
			String content = (bitStatus == 1) ? "1 ( ON )" : "0 ( OFF )";
			
			if(content.contains("ON")) {
				sb.append(String.format("<font color='red'>BIT %d : %s%s%s</font>\n", i , content , Util.separator, Util.separator));	
			}else {
				sb.append(String.format("BIT %d : %s%s%s\n", i , content , Util.separator, Util.separator));
			}
		}
		
		Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	public static void pointDataClear(ArrayList<ModbusWatchPoint> pointList) {
		if(pointList == null || pointList.size() < 1) {
			return;
		}else {
			for(ModbusWatchPoint point : pointList) {
				point.setData(new PerfData());
			}
		}
	}
	
	public String getMemoryBit() {
		try {
			boolean isMemoryBit = this.scaleFunc.contains("x")
								&& this.scaleFunc.contains(">>")
								&& this.scaleFunc.contains("&");
			
			if(isMemoryBit) {
				return this.scaleFunc.trim()
						.split("&")[0]
						.replace("(", "")
						.replace(")", "")
						.replace("x", "")
						.replace(">>", "");
			}else {
				return null;
			}
			
		}catch(Exception e) {
			return null;
		}
	}
	
	public static boolean checkHasMemoryBit(ArrayList<ModbusWatchPoint> pointList) {
		if(pointList == null || pointList.size() < 1) return false;
		
		for(ModbusWatchPoint point : pointList) {
			if(point.getMemoryBit() != null) return true;
		}
		
		return false;
	}
	
	public final double getComputedValue(double value) {
		String fixFormula = this.getScaleFunction(); 
		
        if (value != Double.NaN && !fixFormula.equals("x")) {
            try {
                value = Calculator.calculate(fixFormula.replaceAll("x", Double.toString(value)));
            } catch (Exception e) {
                value = value;
            }
        }
        return value;
    }
	
	public String getText(String addrFormat) {
		switch(addrFormat) {
			case "Modbus (DEC)" : 
				return String.format("%d.  [ %s ]", this.getIndex(), this.getDecCounter());
	
			case "Register (DEC)" :
				return String.format("%d.  [ %s ]", this.getIndex(), this.getRegCounter());
				
			case "Register (HEX)" : 
				return String.format("%d.  [ %s ]", this.getIndex(), this.getHexCounter());
			}
	
		return String.format("%d.  [ %s ]", this.getIndex(), this.getDecCounter());
	}
	
	public static ArrayList<ModbusWatchPoint> convertArrayToList(ModbusWatchPoint[] pointArray){
		ArrayList<ModbusWatchPoint> pointList = new ArrayList<ModbusWatchPoint>();
		for(ModbusWatchPoint point : pointArray) {
			pointList.add(point);
		}
		return pointList;
	}
	
	public static ArrayList<FmsActionItem> getControlList(ArrayList<ModbusWatchPoint> pointList, boolean allFcCode, boolean generateDesc, boolean concatMeasure) {
		ArrayList<FmsActionItem> controlList = new ArrayList<FmsActionItem>();
		
		for(ModbusWatchPoint point : pointList) {
    		ArrayList<FmsActionItem> pointControl = point.convertToControl(allFcCode, generateDesc, concatMeasure);
    		if(pointControl != null && pointControl.size() > 0) {
    			for(FmsActionItem action : pointControl) {
    				controlList.add(action);
	    		}
    		}
    	}
			
		return controlList;
	}
	
	public src_ko.agent.Perf parsePerf_ko() {
			
		src_ko.agent.Perf perf = new src_ko.agent.Perf();
		
		perf.setDisplayName(this.getDisplayName());
		perf.setFunctionCode(String.valueOf(this.getFunctionCode()));
		perf.setRegisterAddress(String.valueOf(this.getRegisterAddr()));
		perf.setModbusAddress(String.valueOf(this.getModbusAddr()));		
		perf.setDataType(this.getDataType());
		perf.setMK119_DataType(this.getDataType());
		perf.setInterval("60");	
		perf.setMeasure(this.getMeasure());
		perf.setScaleFunction(this.getScaleFunction());
		perf.setDataFormat(String.valueOf(this.getDataFormat()));
		
		HashMap binaryMap = perf.getBinaryMap();
		binaryMap.put("0", this.binLabel[0]);
		binaryMap.put("1", this.binLabel[1]);
		perf.setBinaryMap(binaryMap);
		
		if(this.getDataFormat() == 2 && this.getStatusLabels() != null) {
			HashMap multiStatusMap = new HashMap();
			PerfLabelStatusBean[] labelList = this.getStatusLabels();
			for(int i = 0; i < labelList.length; i++) {
				PerfLabelStatusBean bean = labelList[i];
				multiStatusMap.put(
						String.valueOf(bean.value), 
						String.valueOf(bean.label)
				);			
			}
			perf.setMultiStatusMap(multiStatusMap);	
		}
		
		return perf;
	}
	
}