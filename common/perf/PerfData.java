package common.perf;

import java.text.SimpleDateFormat;

public class PerfData {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private int index;
	private Object value;
	private String valueString;
	private long time;
	private String timeString;
	
	public PerfData(int index, Object value, long time) {
		this.index = index;
		this.value = value;
		this.time = time;
	}

	public int getIndex() {
		return index;
	}

	public Object getValue() {
		return value;
	}

	public String getValueString() {
		return valueString;
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
		this.value = value;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}
	
}
