package common.perf;

public class SnmpPerfItem {

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
//        StringBuffer str = new StringBuffer();
//        str.append("displayName=").append(displayName).append(", ");
//        str.append("oid=").append(oid).append(", ");
//        str.append("interval=").append(interval).append(", ");
//        str.append("units=").append(units).append(", ");
//        str.append("expression=").append(expression).append(", ");
//        str.append("data format=").append(dataFormat).append(", ");
//        if (dataFormat == PerfConf.DATA_FORMAT_DIGITAL) {
//            str.append("label0=").append(binLabel[0]).append(", ");
//            str.append("label1=").append(binLabel[1]).append(", ");
//        }
//        str.append("auto reg=").append(autoReg).append(", ");
//        for (int i = 0; evt != null && i < evt.length; i++) {
//            str.append("event [name=").append(evt[i].name).append(", ");
//            str.append("severity=").append(evt[i].severity).append(", ");
//            str.append("threshold=").append(evt[i].threshold).append(", ");
//            str.append("op=").append(evt[i].op).append(", ");
//            str.append("mode=").append(evt[i].mode).append(", ");
//            str.append("duration=").append(evt[i].duration).append(", ");
//            str.append("count=").append(evt[i].count).append(", ");
//            str.append("auto close=").append(evt[i].autoClose).append("]");
//            str.append("enable=").append(evt[i].enable).append(", ");
//        }
//        return str.toString();       
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

        /** 통보 메시지*/
        public String msg;

        /** 자동 등록 여부*/
        public boolean autoReg=false;
    	
    	/**이벤트 자동등록시 사용여부*/
    	public int enable;

        public EventInfo() {}

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
}
