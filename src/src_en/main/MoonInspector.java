package src_en.main;

import src_en.info.ONION_Info;

public class MoonInspector{
	
	public static boolean isMoon() {
		return ONION_Info.userName.contains("��â��")
				&& ONION_Info.userFullName.contains("Moon") 
				&& ONION_Info.userFullName.contains("��â��"); 				
	}
	
}