package common.agent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PerfData implements Comparable{
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	int index;
	Object value;
	long time;
	String timeString;
	
	public int getIndex() {
		return index;
	}
	public Object getValue() {
		return value;
	}
	public long getTime() {
		return time;
	}
	public String getTimeString() {
		return timeString;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public void setValue(Object value) {
		if(value.toString().equals("NaN")) {
			this.value = "-";
		}else {
			this.value = value;	
		}
	}
	public void setTime(long time) {
		this.time = time;
		if(time != -1) {
			this.timeString = sdf.format(new Date(time));
		}else {
			this.timeString = "-";
		}
	}
	
		
	@Override
	/**
	 * 정렬 기준 : 가장 최근에 수집된 데이터부터
	 */
	public int compareTo(Object obj) {
		PerfData perfData = (PerfData)obj;
		
		if(this.time > perfData.time) {
			return -1;
		}else if(this.time == perfData.time) {
			return 0;
		}else {
			return 1;
		}
	}
	
}
