package common.perf;

public class FmsActionItem {

    /** �׼� �̸� */
    public String displayName = "";

    /** �׼� ī���� ���ڿ� */
    public String counter = "";

    /** �׼� ��� */
    public String command = "";

    /** �׼� ���� */
    public String desc = "";

    /** ���� �� �Ķ��Ÿ �Է� ���� ���� (0-�Է� �Ұ�, 1-�Է� ����) */
    public int useParam = 1;

    /** �⺻ �Ķ��Ÿ */
    public String defaultValue = "";

    /** ��� �ð� (��) */
    public int waitTime = 3;

    public FmsActionItem() {
    	
    }
    
    @Override
	public boolean equals(Object obj) {
    	try {
	    	FmsActionItem action = (FmsActionItem)obj;
	    	
	    	return this.displayName.equals(action.displayName)
	    			&& this.command.equals(action.command)
	    			&& this.useParam == action.useParam;
    	}catch(Exception e) {
    		return false;
    	}
	}

	public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("displayName=").append(displayName);
        str.append(",  ");
        str.append("command=").append(command);
        str.append(",  ");
        str.append("desc=").append(desc);
        return str.toString();
    }
}