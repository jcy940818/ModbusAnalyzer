package common.perf;

public class PerfItem {

    public String commandCode;  // ��� �ڵ�
    public String platform;     // ���� �÷���(UNIX, NT, �Ǵ� null)
    public String displayName;  // ǥ�� �̸�
    public int checkInterval;   // �˻� ���� (��)
    public String measure;      // ���� ����
    public String counter;      // ���� ī���� ���ڿ�

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
