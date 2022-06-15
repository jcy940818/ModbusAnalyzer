package common.agent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import common.perf.Perf;
import common.perf.PerfLabelStatusBean;

public class PerfData implements Comparable{
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	int index;
	Object value;
	Object pureValue;
	long time;
	String timeString;
	
	public int getIndex() {
		return index;
	}
	public Object getValue() {
		return value;
	}
	public Object getPureValue() {
		return pureValue;
	}
	public long getTime() {
		return time;
	}
	public String getTimeString() {
		if(timeString != null) {
			return timeString;
		}else {
			return "-";
		}
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
	public void setPureValue(Object pureValue) {
		if(pureValue.toString().equals("NaN")) {
			this.pureValue = "-";
		}else {
			this.pureValue = pureValue;	
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
		
	
	/**
	 * 정렬 기준 : 가장 최근에 수집된 데이터부터
	 */
	@Override
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
	
	public static Object getPerfPureValue(PerfData data) {
		Object pureValue = "-";
		
		try {
			double doubleValue = Double.parseDouble(data.getPureValue().toString());
			pureValue = (Math.round(doubleValue*1000)/1000.0);
		}catch(Exception e) {
			pureValue = "-";
		}
		
		// 정수 데이터는 소숫점을 표시하지 않도록 하기 위해서 아래의 코드를 추가
		Object temp = null;
		try {
			temp = pureValue.toString();
			if (pureValue.toString().endsWith(".0")) {
				pureValue = pureValue.toString().replace(".0", "");
			}
		} catch (Exception e) {
			pureValue = temp;
		}

		return pureValue;
	}
	
	public static Object getPerfContent(Perf perf, PerfData data) {
		
		Object content = "-";
		boolean labelMapping = false;
		
		switch(perf.getDataFormat()) {
			
			case 1 : // 이진 성능
				try {
					String[] binLabel = perf.getBinLabel();
					double doubleValue = Double.parseDouble(data.getValue().toString());
					if(doubleValue == 0) {
						content = binLabel[0];
						labelMapping = true;
					}else if(doubleValue == 1) {
						content = binLabel[1];
						labelMapping = true;
					}else {
						content = (Math.round(doubleValue*1000)/1000.0);
					}
				}catch(Exception e) {
					content = "-";
				}
				break;
				
				
			case 2 : // 다중 성능
				try {
					double doubleValue = Double.parseDouble(data.getValue().toString());
					PerfLabelStatusBean[] labels = perf.getStatusLabels();
					content = (Math.round(doubleValue*1000)/1000.0);
					// 다중 상태 레이블을 검사 후 일치하는 값이 있다면 내용에 적용 후 반복문 종료
					for(int k = 0; k < labels.length; k++) {					
						if(doubleValue == labels[k].value) {
							content = labels[k].label;
							labelMapping = true;
							break;
						}
					}
				}catch(Exception e) {
					content = "-";
				}
				break;
				
				
			case 3 : // 아날로그 성능
				try {
					double doubleValue = Double.parseDouble(data.getValue().toString());
					if((perf.getMeasure() != null) && (perf.getMeasure().length() > 0)) {
						content = (Math.round(doubleValue*1000)/1000.0) + " " + perf.getMeasure();	
					}else {
						content = (Math.round(doubleValue*1000)/1000.0);
					}
				}catch(Exception e) {
					content = "-";
				}
				break;
				
				
			default :  
				content = "-"; 
				break;
				
		}// end switch
		
		// 정수 데이터는 소숫점을 표시하지 않도록 하기 위해서 아래의 코드를 추가
		Object temp = null;
		try {
			temp = content.toString();
			if(!labelMapping) {
				if(content.toString().contains(" ") && content.toString().split(" ")[0].endsWith(".0")) {
					content = content.toString().replace(".0", "");
				}else if(content.toString().endsWith(".0")) {
					content = content.toString().replace(".0", "");
				}
			}
		}catch(Exception e) {
			content = temp;
		}
		
		return content;
	}
	
}
