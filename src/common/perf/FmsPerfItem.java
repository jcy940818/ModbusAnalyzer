package common.perf;

import java.util.ArrayList;
import java.util.Arrays;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import common.util.Operator;

public class FmsPerfItem extends Perf {

	/** ���� �̸� */
	public String displayName;

	/** ���� ī���� ���ڿ� */
	public String counter;

	/** ���� �˻� �ֱ� (��) */
	public int interval;

	/** ���� ���� */
	public String measure;

	/** ����� */
	public String scaleFunc;

	/** ������ ���� */
	public int dataFormat; // ������ ���� (1:���� ������, 2:���� ������, 3:���� ������)

	/** ���� ������ ���¸� ������ ��Ʈ�� �迭 */
	public String binLabel[] = new String[] { "", "" };

	/** ���� ���� ���̺� */
	public PerfLabelStatusBean labels[];	
	public ArrayList<PerfLabelStatusBean> labelList = new ArrayList<PerfLabelStatusBean>();
	
	/** ���� �׸� �ڵ� ��� ���� */
	public boolean autoReg = false;

	/** �ڵ� ����� �̺�Ʈ ��� */
	public EventInfo evt[];

	/** ��� ���� */
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
		/** �̺�Ʈ �̸� */
		public String name;

		/** �̺�Ʈ �߿䵵 */
		public int severity;

		/** �Ӱ谪 */
		public double threshold;

		/** �� ������ */
		public String op;

		/** �߻� ��� */
		public int mode;

		/** ���� �ð� (��) */
		public int duration;

		/** �߻� Ƚ�� */
		public int count;

		/** �뺸 Ƚ�� */
		public int seqCount;

		/** �ڵ� ���� ���� */
		public boolean autoClose;

		/** �뺸 �޽��� */
		public String msg;

		/** �ڵ� ��� ���� */
		public boolean autoReg = false;

		/** �̺�Ʈ �ڵ���Ͻ� ��뿩�� */
		public int enable;

		/** �Ӱ谪2 */
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
