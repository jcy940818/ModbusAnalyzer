package common.perf;

public class FmsActionItem {

    /** 액션 이름 */
    public String displayName = "";

    /** 액션 카운터 문자열 */
    public String counter = "";

    /** 액션 명령 */
    public String command = "";

    /** 액션 설명 */
    public String desc = "";

    /** 실행 시 파라메타 입력 가능 여부 (0-입력 불가, 1-입력 가능) */
    public int useParam = 1;

    /** 기본 파라메타 */
    public String defaultValue = "";

    /** 대기 시간 (초) */
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