package src_ko.agent;

public class ControlAction {
	
	// Moon Control Type
	// 1. "Y" : 파라미터를 사용하는 제어
	// 2. "N" : 파라미터를 사용하지 않는 제어
	// 3. "BIT 0~15" : 워드 단위 비트 제어
	
	private String controlType;
	private String controlName;
	private String controlCounter;
	private String command;
	private String scale;
	private String desc;
	private int useParam = 1;
	private int waitTime = 1;
	
	
	public static ControlAction createCustomControl(String controlInfo) {
		ControlAction control = new ControlAction();
		
		// 기본 값
		control.setControlName("");
		control.setControlCounter("CONTROL");
		control.setCommand("");
		control.setDesc("");
		control.setUseParam(1);
		control.setWaitTime(1);
		
		String[] controlElements = controlInfo.split(",");		
		
		for(String controlElement : controlElements) {
			
			try {								
				String key = null;
				String value = null;
				
				if(!controlElement.contains("=")) {
					control.setControlName(controlElement.trim());
					continue;
				}else {
					key = controlElement.split("=")[0].trim().toLowerCase();
					value = controlElement.split("=")[1].trim();	
				}
				
				if (key.equalsIgnoreCase("controlname") || key.contains("name")) {
					control.setControlName((value != null) ? value : ""); // 제어 이름
					
				} else if (key.equalsIgnoreCase("controlcounter") || key.equalsIgnoreCase("counter") || key.contains("counter")) {
					control.setControlCounter((value != null) ? value : "CONTROL"); // 제어 카운터 ( DB에는 저장되지만 기능은 없는 컬럼 )
					
				} else if (key.equalsIgnoreCase("command") || key.equalsIgnoreCase("cmd") || key.contains("command")) {
					control.setCommand((value != null) ? value : ""); // 제어 명령어
					
				} else if (key.equalsIgnoreCase("description") || key.equalsIgnoreCase("desc") || key.contains("desc") || key.contains("content")) {
					control.setDesc((value != null) ? value : ""); // 제어 명령어
					
				}else if (key.equalsIgnoreCase("useparam") || key.contains("use") || key.contains("param")) {
					try {
						control.setUseParam((value != null) ? Integer.parseInt(value) : 1); // slot
					}catch(NumberFormatException e) {
						control.setUseParam(1);
					}
				}else if (key.equalsIgnoreCase("waitTime") || key.contains("wait") || key.contains("time") || key.contains("timeout")) {
					try {
						control.setWaitTime((value != null) ? Integer.parseInt(value) : 1); // slot
					}catch(NumberFormatException e) {
						control.setWaitTime(1);
					}
				}
			
			}catch(Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
		return control;
	}
	
	
	public String getControlType() {
		return controlType;
	}
	public void setControlType(String controlType) {
		this.controlType = controlType;
	}
	public String getControlName() {
		return controlName;
	}
	public void setControlName(String controlName) {
		this.controlName = controlName;
	}
	public String getControlCounter() {
		return controlCounter;
	}
	public void setControlCounter(String controlCounter) {
		this.controlCounter = controlCounter;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getUseParam() {
		return useParam;
	}
	public void setUseParam(int useParam) {
		this.useParam = useParam;
	}
	public int getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}
	
}
