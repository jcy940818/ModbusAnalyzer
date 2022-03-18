package common.perf;

public class PerfItem {

    public String commandCode;  // 명령 코드
    public String platform;     // 적용 플랫폼(UNIX, NT, 또는 null)
    public String displayName;  // 표시 이름
    public int checkInterval;   // 검사 간격 (초)
    public String measure;      // 측정 단위
    public String counter;      // 성능 카운터 문자열

    @Override
	public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("commandCode=").append(this.commandCode).append(", ");
        str.append("platform=").append(this.platform).append(", ");
        str.append("displayName=").append(this.displayName).append(", ");
        str.append("checkInterval=").append(this.checkInterval).append(", ");
        str.append("measure=").append(this.measure).append(", ");
        str.append("counter=").append(this.counter).append(", ");
        return str.toString();
    }
}
