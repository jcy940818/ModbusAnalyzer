package src_ko.main;

import src_ko.info.ONION_Info;

public class MoonInspector{
	
	public static boolean isMoon() {
		return ONION_Info.userName.contains("��â��")
				&& ONION_Info.userFullName.contains("Moon") 
				&& ONION_Info.userFullName.contains("��â��"); 				
	}
	
}