package src_ko.main;

import javax.swing.JOptionPane;

import src_ko.util.Util;

public class ModbusAnalyzer {
	
	public static void show() {
		StringBuilder sb = new StringBuilder();				
		sb.append("<font color='red'>Result Unknown MK119 Tasks are Complete</font>\n");
		sb.append(String.format("MK119 ���� �߰� �۾� ���� �ð��� 5���� �ʰ��Ͽ����ϴ�%s\n\n", Util.separator));
		sb.append(String.format("���� �߰� �۾��� �Ϸ� ���θ� �� �� �����ϴ�%s\n\n", Util.separator));
		sb.append(String.format("( %s )%s\n\n", Util.colorBlue("�ش� �޽����� ���� �߰� �۾��� ���и� �ǹ������� �ʽ��ϴ�") ,Util.separator));
		sb.append(String.format("Exception Message : %s%s\n","" ,Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	public static void show1() {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='green'>Successfully MK119 Add WatchPoints!</font>\n");
		sb.append(String.format("%s ��� ���������� ������ �߰��Ͽ����ϴ�%s\n\n", Util.colorBlue("UPS-A"), Util.separator));
		sb.append(String.format("�� %d�� ���� �׸� �߰� �Ϸ�%s\n", 30, Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
		return;
	}

	public static void show2() {
		// ���� �߰� ���� ���� : ���� ����
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Failed to MK119 Add WatchPoints!</font>\n");
		sb.append(String.format("%s AdminConsole [ ID : %s ] ������ ����Ǿ����ϴ�%s\n\n",
				Util.colorBlue("192.168.1.92" + ":" + "8080"), Util.colorBlue("admin"), Util.separator));
		sb.append(String.format("������ �α��� ������ �̿��Ͽ� ������ ������ϰ�%s\n\n", Util.separator));
		sb.append(String.format("%s ��� ���� �߰� �۾��� �̾ �Ͻðڽ��ϱ�?%s\n", Util.colorBlue("Modbus UPS-A"), Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
		return;
	}

	public static void show3() {
		StringBuilder sb = new StringBuilder();
		sb = new StringBuilder();
		sb.append(String.format("<font color='red'>Failed to MK119 Create New Session</font>%s\n", Util.separator));
		sb.append(String.format("MK119 AdminConsole ���� ������� �����Ͽ� �۾��� �����մϴ�%s\n", Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}

	public static void show4() {
		StringBuilder sb = new StringBuilder();
		sb = new StringBuilder();
		sb.append(String.format("<font color='red'>Cancel MK119 Add WatchPoints</font>%s\n", Util.separator));
		sb.append(String.format("MK119 ���� �߰� �۾��� ����Ͽ����ϴ�%s\n", Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}

	public static void show5() {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Failed to MK119 Add WatchPoints!</font>\n");
		sb.append(String.format("�� �� ���� ������ ���� �߰��� �����Ͽ����ϴ�%s\n", Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}

	public static void show6() {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Failed to MK119 Add WatchPoints!</font>\n");
		sb.append(String.format("ó�� �� �� ���� ���� �߻����� ���Ͽ� ���� �߰��� �����Ͽ����ϴ�%s\n\n", Util.separator));
		sb.append(String.format("Exception Message : %s%s\n", "JSON Parsing Exception", Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}
}