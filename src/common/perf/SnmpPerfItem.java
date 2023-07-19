package common.perf;

import java.util.Arrays;

public class SnmpPerfItem extends Perf {

	/** ǥ�� �̸� */
	public String displayName;

	/** OID */
	public String oid;

	/** �˻� ���� (��) */
	public int interval;

	/** ���� ���� */
	public String units;

	/** ��ȯ�� */
	public String expression;

	/** �׸� ���� */
	public String description;

	/** ������ ���� */
	public int dataFormat = PerfConf.DATA_FORMAT_MEASURE; // ������ ���� (1:���� ������, 2:���� ������, 3:���� ������)

	/** ���� ������ ���¸� ������ ��Ʈ�� �迭 */
	public String binLabel[] = new String[] { "", "" };

	/** ���� ���� ���̺� */
	public PerfLabelStatusBean labels[];

	/** ���� �׸� �ڵ� ��� ���� */
	public boolean autoReg = false;

	/** �ڵ� ����� �̺�Ʈ ��� */
	public EventInfo evt[];
	
	/** ��� ���� */
	public int enable;
	
	
	public SnmpPerfItem() {
	}

	public SnmpPerfItem(String _name, String _oid, int _interval, String _units, String _exp, String _desc) {
		displayName = _name;
		oid = _oid;
		interval = _interval;
		units = _units;
		expression = _exp;
		description = _desc;
	}

	public String toString() {    
		return displayName;
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

		public EventInfo(String n, int s, double t, String o, int m, int d, int c, boolean a, int seqCount, int e) {
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
		}
	}

	public String getDisplayName() {
		return this.displayName;
	};

	public String getCounter() {
		return this.oid;
	}

	public int getInterval() {
		return this.interval;
	}

	public String getMeasure() {
		return this.units;
	}

	public String getScaleFunction() {
		return this.expression;
	}

	public int getDataFormat() {
		return this.dataFormat;
	}

	public String[] getBinLabel() {
		return this.binLabel;
	}

	public PerfLabelStatusBean[] getStatusLabels() {
		if(this.labels == null) return null;
		
		Arrays.sort(this.labels);
		return this.labels;
	}
	
	public common.perf.FmsPerfItem.EventInfo[] getFmsEventInfo() {
		try {
			if(this.evt != null && this.evt.length > 0) {
				FmsPerfItem.EventInfo[] fmsEvent = new FmsPerfItem.EventInfo[this.evt.length];
				
				for(int i = 0; i < this.evt.length; i++) {
					fmsEvent[i] = new FmsPerfItem.EventInfo();
					fmsEvent[i].severity = evt[i].severity;
					fmsEvent[i].threshold = evt[i].threshold;
					fmsEvent[i].threshold2 = evt[i].threshold2;
					fmsEvent[i].op = evt[i].op;
					fmsEvent[i].mode = evt[i].mode;
					fmsEvent[i].duration = evt[i].duration;
					fmsEvent[i].count = evt[i].count;
					fmsEvent[i].seqCount = evt[i].seqCount;
					fmsEvent[i].autoReg = evt[i].autoReg;
					fmsEvent[i].name = evt[i].name;
					fmsEvent[i].msg = evt[i].msg;
					fmsEvent[i].enable = evt[i].enable;
					fmsEvent[i].autoClose = evt[i].autoClose;
				}

				return fmsEvent;
				
			}else {
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public EventInfo[] getSnmpEventInfo() {
		return this.evt;
	}

	public int getEnable() {
		return this.enable;
	}	
}
