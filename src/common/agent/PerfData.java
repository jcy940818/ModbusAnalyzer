package common.agent;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import common.perf.Perf;
import common.perf.PerfLabelStatusBean;

public class PerfData implements Comparable{
	
	public static DecimalFormat df = new DecimalFormat("#.###");
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	static {
		df.setRoundingMode(RoundingMode.DOWN);
	}
	
	int index;
	Object value;
	Object pureValue = "none";
	long time;
	String timeString;
	
	public int getIndex() {
		return index;
	}
	public Object getValue() {
		if(value != null) {
			return value;	
		}else {
			return Double.NaN;
		}
	}
	public Object getPureValue() {
		try {
			return PerfData.df.format(pureValue);
		}catch(Exception e) {
			return pureValue;
		}
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
		
		if(data.getPureValue().toString().equals(pureValue) || data.getPureValue().toString().equals("none")) return pureValue;
		
		try {
			double doubleValue = Double.parseDouble(data.getPureValue().toString());
			
			try {
				pureValue = PerfData.df.format(doubleValue);
				
			}catch(Exception e) {
				e.printStackTrace();
				pureValue = pureValue;
			}
			
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
		
		if(data.getValue().toString().equals(content)) return content;
		
		double doubleValue = Double.parseDouble(data.getValue().toString());
		
		try {
			
			content = Double.isNaN(doubleValue) ? content :  PerfData.df.format(doubleValue);
			
		}catch(Exception e) {
			e.printStackTrace();
			content = content;
		}
		
		switch(perf.getDataFormat()) {
			
			case 1 : // 이진 성능
				try {
					String[] binLabel = perf.getBinLabel();
					
					if(doubleValue == 0) {
						content = binLabel[0];
						labelMapping = true;
						
					}else if(doubleValue == 1) {
						content = binLabel[1];
						labelMapping = true;
						
					}
				}catch(Exception e) {
					content = "-";
				}
				break;
				
				
			case 2 : // 다중 성능
				try {
					PerfLabelStatusBean[] labels = perf.getStatusLabels();
					
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
					if((perf.getMeasure() != null) && (!perf.getMeasure().isEmpty()) && !content.equals("-")) {
						content = content + " " + perf.getMeasure();
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
	
	
	public static void resetDecimalPoint() {
		
		PerfData.df.applyPattern("#.###");
		
	}
	
	public static boolean setDecimalPoint(String pattern) {
		try {
			PerfData.df.applyPattern(pattern);
			
			Double.parseDouble(PerfData.df.format(0));
			return true;
			
		}catch(Exception e) {
			e.printStackTrace();
			PerfData.df = new DecimalFormat("#.###");
			return false;
		}
	}
	
	public static boolean setDecimalPoint(double pattern) {
		try {
			
			int num = new DecimalFormat("#.##############################").format(pattern).split("\\.")[1].length();
			
			return setDecimalPoint(num);
			
		}catch(Exception e) {
			e.printStackTrace();
			PerfData.df = new DecimalFormat("#.###");
			return false;
		}
	}
	
	public static boolean setDecimalPoint(int num) {
		try {
			if(num < 0) {
				PerfData.df = new DecimalFormat("#");
				return false;
				
			}else if(num == 0) {
				PerfData.df = new DecimalFormat("#");
				return true;
			}
			
			StringBuilder pattern = new StringBuilder("#.");
			
			for(int i = 0; i < num; i++) {
				pattern.append("#");
			}
			
			PerfData.df.applyPattern(pattern.toString());
			
			Double.parseDouble(PerfData.df.format(0));
			return true;
			
		}catch(Exception e) {
			e.printStackTrace();
			PerfData.df = new DecimalFormat("#.###");
			return false;
		}
	}
	
}
