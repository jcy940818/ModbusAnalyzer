package common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class MyCalendar {
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final long SECODNS = 1000;
	public static final long MINUTE = SECODNS * 60;
	public static final long HOUR = MINUTE * 60;
	
	public static void setTimeNow(JComboBox year_c, JComboBox month_c, JComboBox day_c, JComboBox hour_c, JComboBox minute_c, JComboBox second_c) {
		if(year_c == null || month_c == null || day_c == null || hour_c == null || minute_c == null || second_c == null)
			return;
		
		Calendar c = Calendar.getInstance();
		
		int yearNow = c.get(Calendar.YEAR);
		int monthNow = c.get(Calendar.MONTH);
		int dayNow = c.get(Calendar.DAY_OF_MONTH);
		
		int hourNow = c.get(Calendar.HOUR_OF_DAY);
		int minuteNow = c.get(Calendar.MINUTE);
		int secondNow = c.get(Calendar.SECOND);
		
		ArrayList<String> year = new ArrayList<String>();
		for(int i = 2000; i <= yearNow; i++) year.add(String.format("%04d", i));
		
		ArrayList<String> month = new ArrayList<String>();
		for(int i = 1; i <= 12; i++) month.add(String.format("%02d", i));
					
		ArrayList<String> day = new ArrayList<String>();
		for(int i = 1; i <= getMonthLastDay(yearNow, monthNow + 1); i++) day.add(String.format("%02d", i));
		
		ArrayList<String> hour = new ArrayList<String>();
		for(int i = 0; i < 24; i++) hour.add(String.format("%02d", i));
		
		ArrayList<String> minute = new ArrayList<String>();
		for(int i = 0; i < 60; i++) minute.add(String.format("%02d", i));
		
		ArrayList<String> second = new ArrayList<String>();
		for(int i = 0; i < 60; i++) second.add(String.format("%02d", i));
		
		year_c.setModel(new DefaultComboBoxModel(year.toArray()));
		month_c.setModel(new DefaultComboBoxModel(month.toArray()));
		day_c.setModel(new DefaultComboBoxModel(day.toArray()));
		hour_c.setModel(new DefaultComboBoxModel(hour.toArray()));
		minute_c.setModel(new DefaultComboBoxModel(minute.toArray()));
		second_c.setModel(new DefaultComboBoxModel(second.toArray()));
		
		for(int i = 0; i < year_c.getItemCount(); i++) {
			String item = year_c.getItemAt(i).toString();
			if(item.equals(String.format("%04d", yearNow))) {
				year_c.setSelectedIndex(i);
				break;
			}
		}
		for(int i = 0; i < month_c.getItemCount(); i++) {
			String item = month_c.getItemAt(i).toString();
			if(item.equals(String.format("%02d", monthNow + 1))) {
				month_c.setSelectedIndex(i);
				break;
			}
		}
		for(int i = 0; i < day_c.getItemCount(); i++) {
			String item = day_c.getItemAt(i).toString();
			if(item.equals(String.format("%02d", dayNow))) {
				day_c.setSelectedIndex(i);
				break;
			}
		}
		for(int i = 0; i < hour_c.getItemCount(); i++) {
			String item = hour_c.getItemAt(i).toString();
			if(item.equals(String.format("%02d", hourNow))) {
				hour_c.setSelectedIndex(i);
				break;
			}
		}
		for(int i = 0; i < minute_c.getItemCount(); i++) {
			String item = minute_c.getItemAt(i).toString();
			if(item.equals(String.format("%02d", minuteNow))) {
				minute_c.setSelectedIndex(i);
				break;
			}
		}
		for(int i = 0; i < second_c.getItemCount(); i++) {
			String item = second_c.getItemAt(i).toString();
			if(item.equals(String.format("%02d", secondNow))) {
				second_c.setSelectedIndex(i);
				break;
			}
		}
	}
	
	public static long getMilliseconds(JComboBox year_c, JComboBox month_c, JComboBox day_c, JComboBox hour_c, JComboBox minute_c, JComboBox second_c) {
		Calendar c = Calendar.getInstance();
		
		int yaer = Integer.parseInt(year_c.getSelectedItem().toString());
		int month = Integer.parseInt(month_c.getSelectedItem().toString());
		int day = Integer.parseInt(day_c.getSelectedItem().toString());
		
		int hour = Integer.parseInt(hour_c.getSelectedItem().toString());
		int minute = Integer.parseInt(minute_c.getSelectedItem().toString());
		int second = Integer.parseInt(second_c.getSelectedItem().toString());
		
		c.set(yaer, month - 1, day, hour, minute, second);
		return c.getTimeInMillis();
	}
	
	public static long getCalcMilliseconds(long time, int hour) {
		return time += (hour * HOUR); 
	}

	// 년도와 월 정보를 주면 해당 연월의 마지막 날을 리턴한다
	public static int getMonthLastDay(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(year, month - 1, 1);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static void setLastDayComboBox(JComboBox year_comboBox, JComboBox month_comboBox, JComboBox day_comboBox) {
		int selectedYear = Integer.parseInt(year_comboBox.getSelectedItem().toString());
		int selectedMonth = Integer.parseInt(month_comboBox.getSelectedItem().toString());
		int selectedDay = Integer.parseInt(day_comboBox.getSelectedItem().toString());
		
		int lastDay = MyCalendar.getMonthLastDay(selectedYear, selectedMonth);
		
		ArrayList<String> dayList = new ArrayList<String>();
		for(int i = 1; i <= lastDay; i++) {
			dayList.add(String.format("%02d", i));
		}
		
		day_comboBox.setModel(new DefaultComboBoxModel(dayList.toArray()));
		for(int i = 0; i < day_comboBox.getItemCount(); i++) {
			String item = day_comboBox.getItemAt(i).toString();
			if(item.equals(String.format("%02d", selectedDay))) {
				day_comboBox.setSelectedIndex(i);
				break;
			}
		}
	}
		
}
