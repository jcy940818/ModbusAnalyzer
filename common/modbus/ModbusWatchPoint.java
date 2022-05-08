package common.modbus;

import common.agent.PerfData;
import common.perf.FmsPerfItem;

public class ModbusWatchPoint extends FmsPerfItem implements Comparable {
	
	private int unitId;
	private int functionCode;
	private int registerAddr;
	private int modbusAddr;
	private String dataType;
	private PerfData data;
	
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
	
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public int getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(int functionCode) {
		this.functionCode = functionCode;
	}
	public int getRegisterAddr() {
		return registerAddr;
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
	public Object getContent() {
		return PerfData.getPerfLastContent(this, this.data);
	}
	
}