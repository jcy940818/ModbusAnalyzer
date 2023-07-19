package common;

import common.web.AdminConsole_Info;
import moon.Moon;

public class Test {
	
	public static void main(String[] args) {
		
		Moon.currentLanguage = Moon.KO;
		
		test();
		
	}
	
	public static void test() {
		AdminConsole_Info adminConsole = new AdminConsole_Info("localhost", "8080", "admin", "1", null);
		adminConsole.setVersion("4.5");
		adminConsole.refreshSession(adminConsole);
		
	}
	
}
