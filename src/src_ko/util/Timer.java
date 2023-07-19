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
			case 1 : days = "일요일"; break;
			case 2 : days = "월요일"; break;
			case 3 : days = "화요일"; break;
			case 4 : days = "수요일"; break;
			case 5 : days = "목요일"; break;
			case 6 : days = "금요일"; break;
			case 7 : days = "토요일"; break;		
		}
		
		
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		
		time.append(year + "년 ");
		time.append(String.format("%02d", ++month) + "월 ");
		time.append(String.format("%02d", day) + "일");
		time.append("(" + days + ") : ");
		
		time.append(String.format("%02d", hour) + "시 ");
		time.append(String.format("%02d", minute) + "분 ");
		time.append(String.format("%02d", second) + "초");
		
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
