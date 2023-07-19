package common.perf;

import java.util.Arrays;

public class SnmpPerfItem extends Perf {

	/** 표시 이름 */
	public String displayName;

	/** OID */
	public String oid;

	/** 검사 간격 (초) */
	public int interval;

	/** 수집 단위 */
	public String units;

	/** 변환식 */
	public String expression;

	/** 항목 설명 */
	public String description;

	/** 데이터 형식 */
	public int dataFormat = PerfConf.DATA_FORMAT_MEASURE; // 데이터 형식 (1:이진 데이터, 2:상태 데이터, 3:성능 데이터)

	/** 이진 데이터 상태를 저장할 스트링 배열 */
	public String binLabel[] = new String[] { "", "" };

	/** 다중 상태 레이블 */
	public PerfLabelStatusBean labels[];

	/** 성능 항목 자동 등록 여부 */
	public boolean autoReg = false;

	/** 자동 등록할 이벤트 목록 */
	public EventInfo evt[];
	
	/** 사용 중지 */
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
