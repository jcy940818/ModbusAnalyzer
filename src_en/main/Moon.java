package src_en.main;

import javax.swing.JOptionPane;

import src_en.info.ONION_Info;
import src_en.swing.ModbusXmlGeneratorFrame;
import src_en.util.MessageUtil_en;
import src_en.util.Util;

public class Moon{
	
	public static void main(String[] args) {
		
	}
	
	public static boolean isMoon() {
		return ONION_Info.userName.contains("정창용")
				&& ONION_Info.userFullName.contains("Moon") 
				&& ONION_Info.userFullName.contains("정창용"); 				
	}
	
}