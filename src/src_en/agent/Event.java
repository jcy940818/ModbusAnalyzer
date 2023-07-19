package src_en.agent;

public class Event {		
		
	// 심각도
	// 10 : Normal
	// 20 : Warning
	// 30 : Minor
	// 40 : Critical
	// 50 : Fatal
	
	// 연산자
	// < : 미만
	// <= : 이하
	// = : 같음
	// <> : 다름
	// >= : 이상
	// > 초과
	// <-> 값이 변경될 때
	
	// 발생모드
	// 1 : 지속 시간동안 임계값 만족시
	// 3 : 지속 시간동안 임계값 발생 횟수 만족 시
	// 4 : 임계값을 만족할 때 마다
	// 5 : 임계값 만족 시 한번만
	
	public static boolean useAutoEvent = false; // 이벤트 자동 등록 기능 사용  여부
	
	// 성능 고유의 이벤트 이름, 이벤트 메시지
	private String perfEventName = "";
	private String perfEventMessage = "";	
	
	// 이벤트 공통 정보
	public static String name = "Event"; // 이벤트 명
	public static String message = ""; // 이벤트 메시지
	public static String enable = "0"; // 이벤트 사용 여부 : 0 / 1
	public static String severity = "10"; // 심각도 : Normal
	public static String threshold = "1"; // 임계값
	public static String op = ">="; // 연산자
	public static String mode = "5"; // 발생 모드 : 임계값 만족 시 한번만
	public static String duration = "10"; // 지속 시간
	public static String count = "10"; // 발생 횟수
	public static String notify = "0"; // 통보 사용 여부: 0 / 1
	public static String autoReg = "TRUE"; // 자동 등록 : TRUE	
	public static String autoClose = "TRUE"; // 이벤트 자동 종료 사용 여부 : TRUE
	public static String seqCount = "3"; // 통보 횟수 : 현재 MK119 모드버스 성능 추가시 통보 횟수는 설정하지 못함
	
	public String getPerfEventName() {
		return perfEventName;
	}
	
	public void setPerfEventName(String perfEventName) {
		this.perfEventName = perfEventName;
	}
	
	public String getPerfEventMessage() {
		return perfEventMessage;
	}
	
	public void setPerfEventMessage(String perfEventMessage) {
		this.perfEventMessage = perfEventMessage;
	}
	
}
