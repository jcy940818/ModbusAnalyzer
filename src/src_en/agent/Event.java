package src_en.agent;

public class Event {		
		
	// �ɰ���
	// 10 : Normal
	// 20 : Warning
	// 30 : Minor
	// 40 : Critical
	// 50 : Fatal
	
	// ������
	// < : �̸�
	// <= : ����
	// = : ����
	// <> : �ٸ�
	// >= : �̻�
	// > �ʰ�
	// <-> ���� ����� ��
	
	// �߻����
	// 1 : ���� �ð����� �Ӱ谪 ������
	// 3 : ���� �ð����� �Ӱ谪 �߻� Ƚ�� ���� ��
	// 4 : �Ӱ谪�� ������ �� ����
	// 5 : �Ӱ谪 ���� �� �ѹ���
	
	public static boolean useAutoEvent = false; // �̺�Ʈ �ڵ� ��� ��� ���  ����
	
	// ���� ������ �̺�Ʈ �̸�, �̺�Ʈ �޽���
	private String perfEventName = "";
	private String perfEventMessage = "";	
	
	// �̺�Ʈ ���� ����
	public static String name = "Event"; // �̺�Ʈ ��
	public static String message = ""; // �̺�Ʈ �޽���
	public static String enable = "0"; // �̺�Ʈ ��� ���� : 0 / 1
	public static String severity = "10"; // �ɰ��� : Normal
	public static String threshold = "1"; // �Ӱ谪
	public static String op = ">="; // ������
	public static String mode = "5"; // �߻� ��� : �Ӱ谪 ���� �� �ѹ���
	public static String duration = "10"; // ���� �ð�
	public static String count = "10"; // �߻� Ƚ��
	public static String notify = "0"; // �뺸 ��� ����: 0 / 1
	public static String autoReg = "TRUE"; // �ڵ� ��� : TRUE	
	public static String autoClose = "TRUE"; // �̺�Ʈ �ڵ� ���� ��� ���� : TRUE
	public static String seqCount = "3"; // �뺸 Ƚ�� : ���� MK119 ������ ���� �߰��� �뺸 Ƚ���� �������� ����
	
	public String getPerfEventName() {
		return perfEventName;
	}
	
	public void setPerfEventName(String perfEventName) {
		this.perfEventName = perfEventName;
	}
	
	public String getPerfEventMessage() {
		return perfEventMessage;
	}
	
	public void setPerfEventMessage(String perfEventMessage) {
		this.perfEventMessage = perfEventMessage;
	}
	
}
