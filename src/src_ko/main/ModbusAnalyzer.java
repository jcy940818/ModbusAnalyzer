package src_ko.main;

import javax.swing.JOptionPane;

import src_ko.util.Util;

public class ModbusAnalyzer {
	
	public static void show() {
		StringBuilder sb = new StringBuilder();				
		sb.append("<font color='red'>Result Unknown MK119 Tasks are Complete</font>\n");
		sb.append(String.format("MK119 성능 추가 작업 수행 시간이 5분을 초과하였습니다%s\n\n", Util.separator));
		sb.append(String.format("성능 추가 작업의 완료 여부를 알 수 없습니다%s\n\n", Util.separator));
		sb.append(String.format("( %s )%s\n\n", Util.colorBlue("해당 메시지가 성능 추가 작업의 실패를 의미하지는 않습니다") ,Util.separator));
		sb.append(String.format("Exception Message : %s%s\n","" ,Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	public static void show1() {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='green'>Successfully MK119 Add WatchPoints!</font>\n");
		sb.append(String.format("%s 장비에 성공적으로 성능을 추가하였습니다%s\n\n", Util.colorBlue("UPS-A"), Util.separator));
		sb.append(String.format("총 %d개 성능 항목 추가 완료%s\n", 30, Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
		return;
	}

	public static void show2() {
		// 성능 추가 실패 로직 : 세션 끊김
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Failed to MK119 Add WatchPoints!</font>\n");
		sb.append(String.format("%s AdminConsole [ ID : %s ] 세션이 종료되었습니다%s\n\n",
				Util.colorBlue("192.168.1.92" + ":" + "8080"), Util.colorBlue("admin"), Util.separator));
		sb.append(String.format("마지막 로그인 정보를 이용하여 세션을 재생성하고%s\n\n", Util.separator));
		sb.append(String.format("%s 장비에 성능 추가 작업을 이어서 하시겠습니까?%s\n", Util.colorBlue("Modbus UPS-A"), Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
		return;
	}

	public static void show3() {
		StringBuilder sb = new StringBuilder();
		sb = new StringBuilder();
		sb.append(String.format("<font color='red'>Failed to MK119 Create New Session</font>%s\n", Util.separator));
		sb.append(String.format("MK119 AdminConsole 세션 재생성에 실패하여 작업을 종료합니다%s\n", Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}

	public static void show4() {
		StringBuilder sb = new StringBuilder();
		sb = new StringBuilder();
		sb.append(String.format("<font color='red'>Cancel MK119 Add WatchPoints</font>%s\n", Util.separator));
		sb.append(String.format("MK119 성능 추가 작업을 취소하였습니다%s\n", Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}

	public static void show5() {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Failed to MK119 Add WatchPoints!</font>\n");
		sb.append(String.format("알 수 없는 이유로 성능 추가에 실패하였습니다%s\n", Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}

	public static void show6() {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Failed to MK119 Add WatchPoints!</font>\n");
		sb.append(String.format("처리 할 수 없는 예외 발생으로 인하여 성능 추가에 실패하였습니다%s\n\n", Util.separator));
		sb.append(String.format("Exception Message : %s%s\n", "JSON Parsing Exception", Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		return;
	}
}