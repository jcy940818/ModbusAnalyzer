package common.perf;

import java.util.ArrayList;
import java.util.Arrays;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import common.util.Operator;

public class FmsPerfItem extends Perf {

	/** 성능 이름 */
	public String displayName;

	/** 성능 카운터 문자열 */
	public String counter;

	/** 성능 검사 주기 (초) */
	public int interval;

	/** 측정 단위 */
	public String measure;

	/** 연산식 */
	public String scaleFunc;

	/** 데이터 형식 */
	public int dataFormat; // 데이터 형식 (1:이진 데이터, 2:상태 데이터, 3:성능 데이터)

	/** 이진 데이터 상태를 저장할 스트링 배열 */
	public String binLabel[] = new String[] { "", "" };

	/** 다중 상태 레이블 */
	public PerfLabelStatusBean labels[];	
	public ArrayList<PerfLabelStatusBean> labelList = new ArrayList<PerfLabelStatusBean>();
	
	/** 성능 항목 자동 등록 여부 */
	public boolean autoReg = false;

	/** 자동 등록할 이벤트 목록 */
	public EventInfo evt[];

	/** 사용 중지 */
	public int enable;

	public FmsPerfItem() {
	}

	public FmsPerfItem(String name, int j, int i, String msr, String cntStr, String scale) {
		displayName = name;
		enable = j;
		interval = i;
		measure = msr;
		counter = cntStr;
		scaleFunc = scale;
	}

	/**
	 * FmsPerfItem Deep Copy
	 */
	public FmsPerfItem(FmsPerfItem orig) {
		this.displayName = orig.displayName;
		this.counter = orig.counter;
		this.interval = orig.interval;
		this.measure = orig.measure;
		this.scaleFunc = orig.scaleFunc;
		this.dataFormat = orig.dataFormat;
		this.binLabel = orig.binLabel;
		this.labels = orig.labels;
		this.autoReg = orig.autoReg;
		this.evt = orig.evt;
		this.enable = orig.enable;
	}

	public boolean equals(Object obj) {
		if (obj instanceof FmsPerfItem == false)
			return false;

		FmsPerfItem target = (FmsPerfItem) obj;

		if (this.counter == null || target.counter == null)
			return false;

		if (this.scaleFunc == null || target.scaleFunc == null)
			return false;

		return this.counter.equals(target.counter) && this.scaleFunc.equals(target.scaleFunc);
	}

	public String toString() {
		return displayName;
	}

	public String toJSONString() throws JSONException {
		JSONObject perfObj = new JSONObject();
		JSONObject eventObj = new JSONObject();
		JSONArray perfLabelsArray = new JSONArray();

		if (evt != null && evt[0] != null) {
			eventObj.put("name", evt[0].name);
			eventObj.put("severity", evt[0].severity);
			eventObj.put("threshold", evt[0].threshold);
			eventObj.put("op", evt[0].op);
			eventObj.put("mode", evt[0].mode);
			eventObj.put("duration", evt[0].duration);
			eventObj.put("count", evt[0].count);
			eventObj.put("autoReg", evt[0].autoReg);
			eventObj.put("message", evt[0].msg);
			eventObj.put("enable", evt[0].enable);
			eventObj.put("threshold2", evt[0].threshold2);
			eventObj.put("autoClose", evt[0].autoClose);
		}

		if (this.dataFormat == PerfConf.DATA_FORMAT_STATUS) {
			for (int i = 0; i < this.labels.length; i++) {
				JSONObject labelObj = new JSONObject();
				labelObj.put("value", labels[i].value);
				labelObj.put("label", labels[i].label);
				perfLabelsArray.put(labelObj);
			}
		}

		perfObj.put("displayName", this.displayName);
		perfObj.put("enable", this.enable);
		perfObj.put("counter", this.counter);
		perfObj.put("interval", this.interval);
		perfObj.put("measure", this.measure);
		perfObj.put("scaleFunc", this.scaleFunc);
		perfObj.put("dataFormat", this.dataFormat);
		perfObj.put("label0", this.binLabel[0]);
		perfObj.put("label1", this.binLabel[1]);
		perfObj.put("perfLabels", perfLabelsArray);
		perfObj.put("event", eventObj);
		return perfObj.toString().replaceAll("\"", "'");
	}

	public static class EventInfo {
		/** 이벤트 이름 */
		public String name;

		/** 이벤트 중요도 */
		public int severity;

		/** 임계값 */
		public double threshold;

		/** 비교 연산자 */
		public String op;

		/** 발생 모드 */
		public int mode;

		/** 지속 시간 (초) */
		public int duration;

		/** 발생 횟수 */
		public int count;

		/** 통보 횟수 */
		public int seqCount;

		/** 자동 종료 여부 */
		public boolean autoClose;

		/** 통보 메시지 */
		public String msg;

		/** 자동 등록 여부 */
		public boolean autoReg = false;

		/** 이벤트 자동등록시 사용여부 */
		public int enable;

		/** 임계값2 */
		public double threshold2;

		public EventInfo() {
		}

		public EventInfo(String n, int s, double t, String o, int m, int d, int c, boolean a, int seqCount, int e,
				int t2) {
			name = n;
			severity = s;
			threshold = t;
			op = o;
			mode = m;
			duration = d;
			count = c;
			autoClose = a;
			this.seqCount = seqCount;
			msg = "";
			enable = e;
			threshold2 = t;
		}
	}

	public String getDisplayName() {
		return this.displayName;
	};

	public void setCounter(String counter) {
		this.counter = counter;
	}
	
	public String getCounter() {
		return this.counter;
	}

	public int getInterval() {
		return this.interval;
	}

	public String getMeasure() {
		return this.measure;
	}

	public String getScaleFunction() {
		return this.scaleFunc;
	}
	
	public String getReverseScale() {
		char[] scaleCharArray = this.scaleFunc.toCharArray();
		
		for(int i = 0; i < scaleCharArray.length; i++) {
			scaleCharArray[i] = Operator.getReverseOperator(scaleCharArray[i]);
		}
		
		return new String(scaleCharArray);
	}

	public int getDataFormat() {
		return this.dataFormat;
	}

	public String[] getBinLabel() {
		return this.binLabel;
	}

	public void setStatusLabels() {
		if(this.labelList.size() == 0) {
			this.labels = null;
			return;
		}
		
		this.labels = new PerfLabelStatusBean[this.labelList.size()];
		for(int i = 0; i < this.labelList.size(); i++) {
			labels[i] = this.labelList.get(i);
		}
	}
	
	public PerfLabelStatusBean[] getStatusLabels() {
		if(this.labels == null) return null;
		
		Arrays.sort(this.labels);
		return this.labels;
	}
	
	public EventInfo[] getFmsEventInfo() {
		return this.evt;
	}
	
	public common.perf.SnmpPerfItem.EventInfo[] getSnmpEventInfo() {
		try {
			if(this.evt != null && this.evt.length > 0) {
				SnmpPerfItem.EventInfo[] snmpEvent = new SnmpPerfItem.EventInfo[this.evt.length];
				
				for(int i = 0; i < this.evt.length; i++) {
					snmpEvent[i] = new SnmpPerfItem.EventInfo();
					snmpEvent[i].severity = evt[i].severity;
					snmpEvent[i].threshold = evt[i].threshold;
					snmpEvent[i].threshold2 = evt[i].threshold2;
					snmpEvent[i].op = evt[i].op;
					snmpEvent[i].mode = evt[i].mode;
					snmpEvent[i].duration = evt[i].duration;
					snmpEvent[i].count = evt[i].count;
					snmpEvent[i].seqCount = evt[i].seqCount;
					snmpEvent[i].autoReg = evt[i].autoReg;
					snmpEvent[i].name = evt[i].name;
					snmpEvent[i].msg = evt[i].msg;
					snmpEvent[i].enable = evt[i].enable;
					snmpEvent[i].autoClose = evt[i].autoClose;
				}

				return snmpEvent;
				
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getEnable() {
		return this.enable;
	}
}
