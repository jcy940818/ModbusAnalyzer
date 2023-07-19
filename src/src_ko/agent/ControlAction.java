package src_ko.agent;

public class ControlAction {
	
	// Moon Control Type
	// 1. "Y" : �Ķ���͸� ����ϴ� ����
	// 2. "N" : �Ķ���͸� ������� �ʴ� ����
	// 3. "BIT 0~15" : ���� ���� ��Ʈ ����
	
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
		
		// �⺻ ��
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
					control.setControlName((value != null) ? value : ""); // ���� �̸�
					
				} else if (key.equalsIgnoreCase("controlcounter") || key.equalsIgnoreCase("counter") || key.contains("counter")) {
					control.setControlCounter((value != null) ? value : "CONTROL"); // ���� ī���� ( DB���� ��������� ����� ���� �÷� )
					
				} else if (key.equalsIgnoreCase("command") || key.equalsIgnoreCase("cmd") || key.contains("command")) {
					control.setCommand((value != null) ? value : ""); // ���� ��ɾ�
					
				} else if (key.equalsIgnoreCase("description") || key.equalsIgnoreCase("desc") || key.contains("desc") || key.contains("content")) {
					control.setDesc((value != null) ? value : ""); // ���� ��ɾ�
					
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
