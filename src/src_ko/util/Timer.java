package src_ko.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Timer {
	
	public static String getCurrentTime() {
		StringBuffer time = new StringBuffer();
		Calendar c = Calendar.getInstance();
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int whatDay = c.get(Calendar.DAY_OF_WEEK);
		
		String days = "";
		switch(whatDay) {
			case 1 : days = "�Ͽ���"; break;
			case 2 : days = "������"; break;
			case 3 : days = "ȭ����"; break;
			case 4 : days = "������"; break;
			case 5 : days = "�����"; break;
			case 6 : days = "�ݿ���"; break;
			case 7 : days = "�����"; break;		
		}
		
		
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		
		time.append(year + "�� ");
		time.append(String.format("%02d", ++month) + "�� ");
		time.append(String.format("%02d", day) + "��");
		time.append("(" + days + ") : ");
		
		time.append(String.format("%02d", hour) + "�� ");
		time.append(String.format("%02d", minute) + "�� ");
		time.append(String.format("%02d", second) + "��");
		
		// 20-12-09 15:30:24
		return time.toString();
	}
	
	public static String getServerTime() {
		String currentTime;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		currentTime = sdf.format(date);
		
		return currentTime;		
	}
	
}
