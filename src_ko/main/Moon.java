package src_ko.main;

import javax.swing.JOptionPane;

import src_ko.info.ONION_Info;
import src_ko.util.Util;

public class Moon{
	
	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		 sb.append(Util.colorRed("Procedure Generator Frame Already Exists") + Util.separator + "\n");
		 sb.append("프로시저 생성 프레임이 이미 열려있습니다" + Util.separator + "\n");
		 Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	public static boolean isMoon() {
		return ONION_Info.userName.contains("정창용")
				&& ONION_Info.userFullName.contains("Moon") 
				&& ONION_Info.userFullName.contains("정창용"); 				
	}
	
}