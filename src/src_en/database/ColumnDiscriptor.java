package src_en.database;

public class ColumnDiscriptor {
			
//	SELECT A.TABLE_NAME + '_' + (A.COLUMN_NAME)
//	FROM INFORMATION_SCHEMA.COLUMNS A
//	WHERE A.TABLE_NAME = 'SERVERINFO'
	
	public static final String SERVERINFO = "SERVERINFO";
	public static final String SERVERINFO_FACILITY = "SERVERINFO_FACILITY";
	public static final String SERVERINFO_RTU = "SERVERINFO_RTU";
	public static final String SERVER_PERF = "SERVER_PERF";
	public static final String ALARM = "ALARM";
	public static final String EVENTS = "EVENTS";
	
	public static String getDiscription(Column column) {	
		switch(column.getTable_ColumnName()) {
		// SERVERINFO 테이블							
		case "SERVERINFO_nServerIndex" : return SERVERINFO_nServerIndex;			
		case "SERVERINFO_strServerName" : return SERVERINFO_strServerName;			
		case "SERVERINFO_strHostName" : return SERVERINFO_strHostName;			
		case "SERVERINFO_strServerIP" : return SERVERINFO_strServerIP;			
		case "SERVERINFO_nTotalProcess" : return SERVERINFO_nTotalProcess;			
		case "SERVERINFO_nServerAlive" : return SERVERINFO_nServerAlive;			
		case "SERVERINFO_nServerStatus" : return SERVERINFO_nServerStatus;			
		case "SERVERINFO_nAgentType" : return SERVERINFO_nAgentType;			
		case "SERVERINFO_strServerDescription" : return SERVERINFO_strServerDescription;			
		case "SERVERINFO_strMemo" : return SERVERINFO_strMemo;			
		case "SERVERINFO_nPerfCheckInterval" : return SERVERINFO_nPerfCheckInterval;			
		case "SERVERINFO_nLogCheckInterval" : return SERVERINFO_nLogCheckInterval;			
		case "SERVERINFO_nProcessCheckInterval" : return SERVERINFO_nProcessCheckInterval;			
		case "SERVERINFO_nDaemonCheckInterval" : return SERVERINFO_nDaemonCheckInterval;			
		case "SERVERINFO_nDiskCheckInterval" : return SERVERINFO_nDiskCheckInterval;			
		case "SERVERINFO_nPerfCheckEnable" : return SERVERINFO_nPerfCheckEnable;			
		case "SERVERINFO_nLogCheckEnable" : return SERVERINFO_nLogCheckEnable;			
		case "SERVERINFO_nProcessCheckEnable" : return SERVERINFO_nProcessCheckEnable;			
		case "SERVERINFO_nDaemonCheckEnable" : return SERVERINFO_nDaemonCheckEnable;			
		case "SERVERINFO_nDaemonControlCheckEnable" : return SERVERINFO_nDaemonControlCheckEnable;			
		case "SERVERINFO_nControlCheckEnable" : return SERVERINFO_nControlCheckEnable;			
		case "SERVERINFO_nDiskCheckEnable" : return SERVERINFO_nDiskCheckEnable;			
		case "SERVERINFO_nEnable" : return SERVERINFO_nEnable;			
		case "SERVERINFO_strOSName" : return SERVERINFO_strOSName;			
		case "SERVERINFO_strVersion" : return SERVERINFO_strVersion;			
		case "SERVERINFO_strProcessorName" : return SERVERINFO_strProcessorName;			
		case "SERVERINFO_strAgentVersion" : return SERVERINFO_strAgentVersion;			
		case "SERVERINFO_strServerKind" : return SERVERINFO_strServerKind;			
		case "SERVERINFO_strGetCommunity" : return SERVERINFO_strGetCommunity;			
		case "SERVERINFO_strSetCommunity" : return SERVERINFO_strSetCommunity;			
		case "SERVERINFO_USE_SNMP" : return SERVERINFO_USE_SNMP;			
		case "SERVERINFO_SNMP_VERSION" : return SERVERINFO_SNMP_VERSION;			
		case "SERVERINFO_CHECK_ALIVE_PING" : return SERVERINFO_CHECK_ALIVE_PING;			
		case "SERVERINFO_CHECK_ALIVE_SNMP" : return SERVERINFO_CHECK_ALIVE_SNMP;			
		case "SERVERINFO_SNMP_COLLECT_METHOD" : return SERVERINFO_SNMP_COLLECT_METHOD;			
		case "SERVERINFO_NW_VENDOR" : return SERVERINFO_NW_VENDOR;			
		case "SERVERINFO_NW_PRODUCT" : return SERVERINFO_NW_PRODUCT;			
		case "SERVERINFO_SERVER_CONDITION" : return SERVERINFO_SERVER_CONDITION;			
		case "SERVERINFO_SERVER_CONDITION2" : return SERVERINFO_SERVER_CONDITION2;			
		case "SERVERINFO_IDC_INDEX" : return SERVERINFO_IDC_INDEX;			
		case "SERVERINFO_AUX_SERVER_IP" : return SERVERINFO_AUX_SERVER_IP;			
									
		// SERVERINFO_FACILITY 테이블							
		case "SERVERINFO_FACILITY_NODE_INDEX" : return SERVERINFO_FACILITY_NODE_INDEX;			
		case "SERVERINFO_FACILITY_FACILITY_TYPE" : return SERVERINFO_FACILITY_FACILITY_TYPE;			
		case "SERVERINFO_FACILITY_CONN_METHOD" : return SERVERINFO_FACILITY_CONN_METHOD;			
		case "SERVERINFO_FACILITY_RTU_INDEX" : return SERVERINFO_FACILITY_RTU_INDEX;			
		case "SERVERINFO_FACILITY_RTU_PORT_NUM" : return SERVERINFO_FACILITY_RTU_PORT_NUM;			
		case "SERVERINFO_FACILITY_SNMP_MIB" : return SERVERINFO_FACILITY_SNMP_MIB;			
		case "SERVERINFO_FACILITY_COMM_PROTOCOL" : return SERVERINFO_FACILITY_COMM_PROTOCOL;			
		case "SERVERINFO_FACILITY_PROTOCOL_DATA" : return SERVERINFO_FACILITY_PROTOCOL_DATA;			
		case "SERVERINFO_FACILITY_packetLogEnable" : return SERVERINFO_FACILITY_packetLogEnable;			
		case "SERVERINFO_FACILITY_packetLogFileName" : return SERVERINFO_FACILITY_packetLogFileName;			
		case "SERVERINFO_FACILITY_packetLogMaxFileSize" : return SERVERINFO_FACILITY_packetLogMaxFileSize;			
		case "SERVERINFO_FACILITY_packetLogMaxBackupIndex" : return SERVERINFO_FACILITY_packetLogMaxBackupIndex;			
		case "SERVERINFO_FACILITY_AUX_PORT_NUM" : return SERVERINFO_FACILITY_AUX_PORT_NUM;			
		case "SERVERINFO_FACILITY_PHASE_TYPE" : return SERVERINFO_FACILITY_PHASE_TYPE;			
		case "SERVERINFO_FACILITY_deviceLocation" : return SERVERINFO_FACILITY_deviceLocation;			
		case "SERVERINFO_FACILITY_offeredITSM" : return SERVERINFO_FACILITY_offeredITSM;			
		case "SERVERINFO_FACILITY_operationDepartment" : return SERVERINFO_FACILITY_operationDepartment;			
		case "SERVERINFO_FACILITY_serviceName" : return SERVERINFO_FACILITY_serviceName;			
		case "SERVERINFO_FACILITY_RESPONSE_TIMEOUT" : return SERVERINFO_FACILITY_RESPONSE_TIMEOUT;			
		case "SERVERINFO_FACILITY_USE_SO_TIMEOUT" : return SERVERINFO_FACILITY_USE_SO_TIMEOUT;			
									
		// SERVERINFO_RTU 테이블							
		case "SERVERINFO_RTU_NODE_INDEX" : return SERVERINFO_RTU_NODE_INDEX;			
		case "SERVERINFO_RTU_RTU_TYPE" : return SERVERINFO_RTU_RTU_TYPE;			
		case "SERVERINFO_RTU_NUM_AI_CHANNEL" : return SERVERINFO_RTU_NUM_AI_CHANNEL;			
		case "SERVERINFO_RTU_NUM_DI_CHANNEL" : return SERVERINFO_RTU_NUM_DI_CHANNEL;			
		case "SERVERINFO_RTU_NUM_AO_CHANNEL" : return SERVERINFO_RTU_NUM_AO_CHANNEL;			
		case "SERVERINFO_RTU_NUM_DO_CHANNEL" : return SERVERINFO_RTU_NUM_DO_CHANNEL;			
		case "SERVERINFO_RTU_NUM_SERIAL_PORT" : return SERVERINFO_RTU_NUM_SERIAL_PORT;			
		case "SERVERINFO_RTU_USE_PSTN" : return SERVERINFO_RTU_USE_PSTN;			
		case "SERVERINFO_RTU_PSTN_COM_PORT" : return SERVERINFO_RTU_PSTN_COM_PORT;			
		case "SERVERINFO_RTU_PSTN_PHONE_NUMBER" : return SERVERINFO_RTU_PSTN_PHONE_NUMBER;			
		case "SERVERINFO_RTU_PASSIVE_TCP_SERVER_PORT" : return SERVERINFO_RTU_PASSIVE_TCP_SERVER_PORT;			
		case "SERVERINFO_RTU_rcu_id" : return SERVERINFO_RTU_rcu_id;			
		case "SERVERINFO_RTU_AUX_TCP_PORT" : return SERVERINFO_RTU_AUX_TCP_PORT;			
									
		// SERVER_PERF 테이블							
		case "SERVER_PERF_nPerfIndex" : return SERVER_PERF_nPerfIndex;			
		case "SERVER_PERF_nServerIndex" : return SERVER_PERF_nServerIndex;			
		case "SERVER_PERF_nPerfType" : return SERVER_PERF_nPerfType;			
		case "SERVER_PERF_strDisplayName" : return SERVER_PERF_strDisplayName;			
		case "SERVER_PERF_strPerfCounter" : return SERVER_PERF_strPerfCounter;			
		case "SERVER_PERF_nPerfInterval" : return SERVER_PERF_nPerfInterval;			
		case "SERVER_PERF_strMeasure" : return SERVER_PERF_strMeasure;			
		case "SERVER_PERF_nEnable" : return SERVER_PERF_nEnable;			
		case "SERVER_PERF_dblLastUsage" : return SERVER_PERF_dblLastUsage;			
		case "SERVER_PERF_strLastUsageTime" : return SERVER_PERF_strLastUsageTime;			
		case "SERVER_PERF_strOperation" : return SERVER_PERF_strOperation;			
		case "SERVER_PERF_strCommandCode" : return SERVER_PERF_strCommandCode;			
		case "SERVER_PERF_nPortNumber" : return SERVER_PERF_nPortNumber;			
		case "SERVER_PERF_DATA_FORMAT" : return SERVER_PERF_DATA_FORMAT;			
		case "SERVER_PERF_DELTA_COLLECTION" : return SERVER_PERF_DELTA_COLLECTION;			
		case "SERVER_PERF_SAVE_DATA" : return SERVER_PERF_SAVE_DATA;			
		case "SERVER_PERF_DB_SAVE_METHOD" : return SERVER_PERF_DB_SAVE_METHOD;			
		case "SERVER_PERF_FAIR_LIMIT_UPPER" : return SERVER_PERF_FAIR_LIMIT_UPPER;			
		case "SERVER_PERF_FAIR_LIMIT_LOWER" : return SERVER_PERF_FAIR_LIMIT_LOWER;			
		case "SERVER_PERF_MODIFY_OPERATION" : return SERVER_PERF_MODIFY_OPERATION;			
		case "SERVER_PERF_VALID_RANGE_ENABLE" : return SERVER_PERF_VALID_RANGE_ENABLE;			
		case "SERVER_PERF_VALID_RANGE_UPPER" : return SERVER_PERF_VALID_RANGE_UPPER;			
		case "SERVER_PERF_VALID_RANGE_LOWER" : return SERVER_PERF_VALID_RANGE_LOWER;			
		case "SERVER_PERF_perf_property" : return SERVER_PERF_perf_property;			
									
		// ALARM 테이블							
		case "ALARM_nAlarmIndex" : return ALARM_nAlarmIndex;			
		case "ALARM_strAlarmName" : return ALARM_strAlarmName;			
		case "ALARM_nEventSeverity" : return ALARM_nEventSeverity;			
		case "ALARM_nAlarmKind" : return ALARM_nAlarmKind;			
		case "ALARM_nServerIndex" : return ALARM_nServerIndex;			
		case "ALARM_nEnable" : return ALARM_nEnable;			
		case "ALARM_nNotify" : return ALARM_nNotify;			
		case "ALARM_nWarnStatus" : return ALARM_nWarnStatus;			
		case "ALARM_dblErrorValue" : return ALARM_dblErrorValue;			
		case "ALARM_strMeasure" : return ALARM_strMeasure;			
		case "ALARM_nAlarmCheckMode" : return ALARM_nAlarmCheckMode;			
		case "ALARM_nAlarmSequence" : return ALARM_nAlarmSequence;			
		case "ALARM_nAlarmInterval" : return ALARM_nAlarmInterval;			
		case "ALARM_nHitCount" : return ALARM_nHitCount;			
		case "ALARM_nAlarmMedia" : return ALARM_nAlarmMedia;			
		case "ALARM_nActionIndex" : return ALARM_nActionIndex;			
		case "ALARM_nActionServerIndex" : return ALARM_nActionServerIndex;			
		case "ALARM_strActionCommand" : return ALARM_strActionCommand;			
		case "ALARM_strActionParameter" : return ALARM_strActionParameter;			
		case "ALARM_nActionIndex2" : return ALARM_nActionIndex2;			
		case "ALARM_strActionCommand2" : return ALARM_strActionCommand2;			
		case "ALARM_strActionParameter2" : return ALARM_strActionParameter2;			
		case "ALARM_strClassName" : return ALARM_strClassName;			
		case "ALARM_strCustomMessage" : return ALARM_strCustomMessage;			
		case "ALARM_nSchedule" : return ALARM_nSchedule;			
		case "ALARM_AUTO_CLOSE" : return ALARM_AUTO_CLOSE;			
		case "ALARM_ACTION_MODE" : return ALARM_ACTION_MODE;			
		case "ALARM_EVENT_GUIDE" : return ALARM_EVENT_GUIDE;			
		case "ALARM_SUSPEND_LIMIT" : return ALARM_SUSPEND_LIMIT;			
		case "ALARM_SUSPEND_START_TIME" : return ALARM_SUSPEND_START_TIME;			
		case "ALARM_SUSPEND_END_TIME" : return ALARM_SUSPEND_END_TIME;			
		case "ALARM_SUSPEND_ALARM" : return ALARM_SUSPEND_ALARM;			
		case "ALARM_nSaveEnable" : return ALARM_nSaveEnable;			
		case "ALARM_CAMERA_PRESET2" : return ALARM_CAMERA_PRESET2;			
									
		// EVENTS 테이블							
		case "EVENTS_nIndex" : return EVENTS_nIndex;			
		case "EVENTS_strEventName" : return EVENTS_strEventName;			
		case "EVENTS_strEventContent" : return EVENTS_strEventContent;			
		case "EVENTS_strEventDate" : return EVENTS_strEventDate;			
		case "EVENTS_strSystemName" : return EVENTS_strSystemName;			
		case "EVENTS_nSystemIndex" : return EVENTS_nSystemIndex;			
		case "EVENTS_nServerIndex" : return EVENTS_nServerIndex;			
		case "EVENTS_strHostName" : return EVENTS_strHostName;			
		case "EVENTS_strHostIP" : return EVENTS_strHostIP;			
		case "EVENTS_nSeverity" : return EVENTS_nSeverity;			
		case "EVENTS_strProcessUser" : return EVENTS_strProcessUser;			
		case "EVENTS_strProcessDate" : return EVENTS_strProcessDate;			
		case "EVENTS_strProcessContent" : return EVENTS_strProcessContent;			
		case "EVENTS_strCompleteDate" : return EVENTS_strCompleteDate;			
		case "EVENTS_nStatus" : return EVENTS_nStatus;			
		case "EVENTS_strSessionID" : return EVENTS_strSessionID;			
		case "EVENTS_nAlarmIndex" : return EVENTS_nAlarmIndex;			
		case "EVENTS_nRepeatCount" : return EVENTS_nRepeatCount;			
		case "EVENTS_strLastOccurTime" : return EVENTS_strLastOccurTime;			
		case "EVENTS_AUTO_CLOSE" : return EVENTS_AUTO_CLOSE;			
		case "EVENTS_EVENT_PROCESSING_TYPE" : return EVENTS_EVENT_PROCESSING_TYPE;			
		case "EVENTS_strConfirmedUserName" : return EVENTS_strConfirmedUserName;			
		case "EVENTS_strFaultCause" : return EVENTS_strFaultCause;
		
		// 내용이 작성되지 않은 컬럼일 경우
		default : return String.format(
				"테이블 명 : %s\r\n" + 
				"컬럼 명 : %s\r\n" + 
				"\r\n" + 
				"( Column Discription 내용이 없습니다 )\r\n" +
				"\r\n" + 
				"( Moon 에게 문의해주세요 )"
				, column.getTableName(), column.getColumnName());				
		}// end switch
	}
	
	/**
	 * SERVERINFO 테이블
	 */
	// 01. nServerIndex
	public static final String SERVERINFO_nServerIndex = 
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nServerIndex\r\n" + 
			"\r\n" + 
			"SERVERINFO 테이블의 기본키(primary key) 입니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 해당 컬럼은 null 값이나 중복 값을 가질 수 없으며\r\n" + 
			"장비의 고유한 인덱스를 의미합니다\r\n" + 
			"\r\n" + 
			"이때 장비란 시설물을 포함한 \r\n" + 
			"서버, RCU, 카메라, 리포트매니저 등\r\n" + 
			"MK119 AdminConsole에서 등록 가능한 관리 대상을 의미합니다";
	
	// 02. strServerName
	public static final String SERVERINFO_strServerName = 
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : strServerName\r\n" + 
			"\r\n" + 
			"장비명을 의미합니다";
	
	// 03. strHostName
	public static final String SERVERINFO_strHostName = 
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : strHostName\r\n" + 
			"\r\n" + 
			"장비에 등록된 IP의 호스트 이름입니다\r\n" + 
			"\r\n" + 
			"IP : 127.0.0.1\r\n" + 
			"Host Name : localhost";
		
	// 04. strServerIP
	public static final String SERVERINFO_strServerIP = 
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : strServerIP\r\n" + 
			"\r\n" + 
			"장비의 IP 주소 입니다";
	
	// 05. nTotalProcess
	public static final String SERVERINFO_nTotalProcess =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nTotalProcess\r\n" + 
			"\r\n" + 
			"전체 프로세스 개수를 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
		
	// 06. nServerAlive
	public static final String SERVERINFO_nServerAlive =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nServerAlive\r\n" + 
			"\r\n" + 
			"장비의 동작 상태를 의미합니다\r\n" + 
			"\r\n" + 
			"-2 : 에이전트 인증 실패\r\n" + 
			"-1 : 이상\r\n" + 
			" 0 : 무응답\r\n" + 
			" 1 : 동작중";
	
	// 07. nServerStatus
	public static final String SERVERINFO_nServerStatus =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nServerStatus\r\n" + 
			"\r\n" + 
			"서버 상태를 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	// 08. nAgentType
	public static final String SERVERINFO_nAgentType =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nAgentType\r\n" + 
			"\r\n" + 
			"midknight 서버와 Agent 간의 데이터 전송 방법을 의미합니다\r\n" + 
			"\r\n" + 
			" 1 : 서버\r\n" + 
			" 2 : 네트워크(SNMP)\r\n" + 
			" 4 : Oracle DB\r\n" + 
			" 8 : RTU\r\n" + 
			" 16 : 시설물\r\n" + 
			" 64 : 프린터\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 09. strServerDescription
	public static final String SERVERINFO_strServerDescription =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : strServerDescription\r\n" + 
			"\r\n" + 
			"서버 설명 정보를 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	// 10. strMemo
	public static final String SERVERINFO_strMemo =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : strMemo\r\n" + 
			"\r\n" + 
			"장비 등록 시 \"설명\" 폼에 입력 된 내용이며\r\n" + 
			"\r\n" + 
			"장비 등록 정보의 \"설명\" 폼의 내용과 동일합니다";
	
	
	// 11. nPerfCheckInterval
	public static final String SERVERINFO_nPerfCheckInterval =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nPerfCheckInterval\r\n" + 
			"\r\n" + 
			"성능 모니터링 검사 간격을 의미합니다 ( 단위 : 초 )\r\n" + 
			"\r\n" + 
			"( 성능의 수집 주기와 다른 내용입니다 )";
	
	
	// 12. nLogCheckInterval
	public static final String SERVERINFO_nLogCheckInterval =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nLogCheckInterval\r\n" + 
			"\r\n" + 
			"로그 검사 간격을 의미합니다 ( 단위 : 초 )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"지정된 시간마다 서버의 로그를 검사하여\r\n" + 
			"\r\n" + 
			"_LOG 및 _LOG_ 테이블에 데이터를 추가합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	// 13. nProcessCheckInterval
	public static final String SERVERINFO_nProcessCheckInterval =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nProcessCheckInterval\r\n" + 
			"\r\n" + 
			"프로세스 개수 검사 간격을 의미합니다 ( 단위 : 초 )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"지정된 시간마다 프로세스의 총 개수를 검사하여\r\n" + 
			"\r\n" + 
			"서버의 _PROCESS 테이블을 갱신합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	// 14. nDaemonCheckInterval
	public static final String SERVERINFO_nDaemonCheckInterval =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nDaemonCheckInterval\r\n" + 
			"\r\n" + 
			"데몬 프로세스 검사 간격을 의미합니다 ( 단위 : 초 )\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	// 15. nDiskCheckInterval
	public static final String SERVERINFO_nDiskCheckInterval =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nDiskCheckInterval\r\n" + 
			"\r\n" + 
			"디스크 사용량 검사 간격을 의미합니다 ( 단위 : 초 )\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	// 16. nPerfCheckEnable
	public static final String SERVERINFO_nPerfCheckEnable =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nPerfCheckEnable\r\n" + 
			"\r\n" + 
			"성능 모니터링 검사 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 검사 안함\r\n" + 
			" 1 : 검사\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	
	// 17. nLogCheckEnable
	public static final String SERVERINFO_nLogCheckEnable =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nLogCheckEnable\r\n" + 
			"\r\n" + 
			"로그 검사 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 검사 안함\r\n" + 
			" 1 : 검사\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	// 18. nProcessCheckEnable
	public static final String SERVERINFO_nProcessCheckEnable =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nProcessCheckEnable\r\n" + 
			"\r\n" + 
			"프로세스 검사 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 검사 안함\r\n" + 
			" 1 : 검사\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	// 19. nDaemonCheckEnable
	public static final String SERVERINFO_nDaemonCheckEnable =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nDaemonCheckEnable\r\n" + 
			"\r\n" + 
			"데몬 프로세스 검사 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 검사 안함\r\n" + 
			" 1 : 검사\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	// 20. nDaemonControlCheckEnable
	public static final String SERVERINFO_nDaemonControlCheckEnable =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nDaemonControlCheckEnable\r\n" + 
			"\r\n" + 
			"데몬 제어 명령 사용 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 데몬 제어 불가능\r\n" + 
			" 1 : 데몬 제어 가능\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	// 21. nControlCheckEnable
	public static final String SERVERINFO_nControlCheckEnable =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nControlCheckEnable\r\n" + 
			"\r\n" + 
			"제어 명령 실행 가능 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 제어 명령 실행 중지\r\n" + 
			" 1 : 제어 명령 실행 가능\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 22. nDiskCheckEnable
	public static final String SERVERINFO_nDiskCheckEnable =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nDiskCheckEnable\r\n" + 
			"\r\n" + 
			"디스크 사용량 검사 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 검사 안함\r\n" + 
			" 1 : 검사\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 23. nEnable
	public static final String SERVERINFO_nEnable =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : nEnable\r\n" + 
			"\r\n" + 
			"장비의 사용 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 중지\r\n" + 
			" 1 : 사용";
	
	
	// 24. strOSName
	public static final String SERVERINFO_strOSName =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : strOSName\r\n" + 
			"\r\n" + 
			"운영 체제 이름을 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	// 25. strVersion
	public static final String SERVERINFO_strVersion =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : strVersion\r\n" + 
			"\r\n" + 
			"운영 체제 버전을 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	// 26. strProcessorName
	public static final String SERVERINFO_strProcessorName =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : strProcessorName\r\n" + 
			"\r\n" + 
			"프로세서 이름을 의미합니다\r\n" + 
			"\r\n" + 
			"( Alpha, x86, ... )\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 27. strAgentVersion
	public static final String SERVERINFO_strAgentVersion =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : strAgentVersion\r\n" + 
			"\r\n" + 
			"midKnight Agent 버전을 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 28. strServerKind
	public static final String SERVERINFO_strServerKind =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : strServerKind\r\n" + 
			"\r\n" + 
			"시스템 종류를 의미합니다\r\n" + 
			"\r\n" + 
			"( 관리 대상이 서버, 네트워크, 프린터인 경우에만 사용합니다 )\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 29. strGetCommunity
	public static final String SERVERINFO_strGetCommunity =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : strGetCommunity\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 30. strSetCommunity
	public static final String SERVERINFO_strSetCommunity =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : strSetCommunity\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 31. USE_SNMP
	public static final String SERVERINFO_USE_SNMP =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : USE_SNMP\r\n" + 
			"\r\n" + 
			"SNMP 지원 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 미지원\r\n" + 
			" 1 : 지원\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 32. SNMP_VERSION
	public static final String SERVERINFO_SNMP_VERSION =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : SNMP_VERSION\r\n" + 
			"\r\n" + 
			"SNMP 버전을 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 33. CHECK_ALIVE_PING
	public static final String SERVERINFO_CHECK_ALIVE_PING =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : CHECK_ALIVE_PING\r\n" + 
			"\r\n" + 
			"Ping 정상 여부 검사 옵션을 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : Ping 정상 여부 검사 안함\r\n" + 
			" 1 : Ping 정상 여부 검사 사용\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 34. CHECK_ALIVE_SNMP
	public static final String SERVERINFO_CHECK_ALIVE_SNMP =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : CHECK_ALIVE_SNMP\r\n" + 
			"\r\n" + 
			"SNMP 정상 여부 검사 옵션을 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : SNMP 정상 여부 검사 안함\r\n" + 
			" 1 : SNMP 정상 여부 검사 사용\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 35. SNMP_COLLECT_METHOD
	public static final String SERVERINFO_SNMP_COLLECT_METHOD =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : SNMP_COLLECT_METHOD\r\n" + 
			"\r\n" + 
			"SNMP 수집 방법을 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : async (비동기식)\r\n" + 
			" 1 : sync (동기식)\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	// 36. NW_VENDOR
	public static final String SERVERINFO_NW_VENDOR =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : NW_VENDOR\r\n" + 
			"\r\n" + 
			"네트워크 장비 제조사 이름을 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 37. NW_PRODUCT
	public static final String SERVERINFO_NW_PRODUCT =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : NW_PRODUCT\r\n" + 
			"\r\n" + 
			"네트워크 장비 제품 이름을 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 38. SERVER_CONDITION
	public static final String SERVERINFO_SERVER_CONDITION =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : SERVER_CONDITION\r\n" + 
			"\r\n" + 
			"장비의 현재 상태를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 접속 전\r\n" + 
			" 1 : 접속 전\r\n" + 
			" 2 : 접속 중\r\n" + 
			" 3 : 접속 성공\r\n" + 
			" 4 : 통신 중\r\n" + 
			" 5 : 통신 오류\r\n" + 
			" 6 : 접속 끊김\r\n" + 
			" 7 : 접속 오류\r\n" + 
			" 8 : Unknown\r\n" + 
			" 9 : Ping 실패";
	
	// 39. SERVER_CONDITION2
	public static final String SERVERINFO_SERVER_CONDITION2 =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : SERVER_CONDITION2\r\n" + 
			"\r\n" + 
			"SERVER_CONDITION 컬럼과 동일한 내용으로 추정\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 40. IDC_INDEX
	public static final String SERVERINFO_IDC_INDEX =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : IDC_INDEX\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 41. AUX_SERVER_IP
	public static final String SERVERINFO_AUX_SERVER_IP =
			"테이블 명 : SERVERINFO\r\n" + 
			"컬럼 명 : AUX_SERVER_IP\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	
	/**
	 * SERVERINFO_FACILITY 테이블
	 */
	// 01. NODE_INDEX
	public static final String SERVERINFO_FACILITY_NODE_INDEX = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : NODE_INDEX\r\n" + 
			"\r\n" + 
			"SERVERINFO_FACILITY 테이블의 기본키(primary key) 입니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 해당 컬럼은 null 값이나 중복 값을 가질 수 없으며\r\n" + 
			"시설물의 고유한 인덱스를 의미합니다\r\n" + 
			"\r\n" + 
			"또한 해당 컬럼은 SERVERINFO 테이블의 \r\n" + 
			"nServerIndex 컬럼과 동일한 내용을 공유합니다\r\n" + 
			"\r\n" + 
			"즉, SERVERINFO 테이블의\r\n" + 
			"nServerIndex = 18인 서버(장비)의 시설물 정보는\r\n" + 
			"\r\n" + 
			"SERVERINFO_FACILITY 테이블의\r\n" + 
			"NODE_INDEX = 18인 행(레코드) 입니다";
	
	
	// 02. FACILITY_TYPE
	public static final String SERVERINFO_FACILITY_FACILITY_TYPE = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : FACILITY_TYPE\r\n" + 
			"\r\n" + 
			"시설물 종류를 의미합니다\r\n" + 
			"\r\n" + 
			" 1 : UPS\r\n" + 
			" 2 : CRAC ( 항온항습기 )\r\n" + 
			" 3 : 하론소화기\r\n" + 
			" 4 : 정류기\r\n" + 
			" 5 : 인버터\r\n" + 
			" 6 : AVC\r\n" + 
			" 7 : 분전반\r\n" + 
			" 8 : 누수감지기\r\n" + 
			" 9 : 카메라 ( 현재 사용 안함 )\r\n" + 
			" 10 : VESDA\r\n" + 
			" 11 : STS\r\n" + 
			" 12 : 계전기\r\n" + 
			" 13 : BMS\r\n" + 
			" 14 : 온습도계\r\n" + 
			" 15 : 화재 수신기\r\n" + 
			" 16 : 선형 탐지기\r\n" + 
			" 17 : 카메라 컨트롤러\r\n" + 
			" 18 : 랙\r\n" + 
			" 19 : 디지털 미터\r\n" + 
			" 20 : 지문 인식기\r\n" + 
			" 21 : 발전기\r\n" + 
			" 22 : 풍량계\r\n" + 
			" 23 : 가습기\r\n" + 
			" 24 : 모터 감시장치\r\n" + 
			" 25 : 풍속계\r\n" + 
			" 26 : PDU\r\n" + 
			" 27 : 공조 설비\r\n" + 
			" 28 : 냉동기\r\n" + 
			" 29 : XD (집중 쿨링)\r\n" + 
			" 98 : AI-Net 다중 센서\r\n" + 
			" 99 : 센서류\r\n" + 
			" 102 : Access Floor\r\n" + 
			" 200 : IBS 설비";
	
	
	// 03. CONN_METHOD
	public static final String SERVERINFO_FACILITY_CONN_METHOD = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : CONN_METHOD\r\n" + 
			"\r\n" + 
			"시설물 장비의 연결 방법을 의미합니다\r\n" + 
			"\r\n" + 
			"( 시설물 종류마다 지원하는 연결 방법이 상이합니다 )\r\n" + 
			"\r\n" + 
			" 1 : 접점 연결\r\n" + 
			" 2 : 시리얼 포트 연결\r\n" + 
			" 4 : SNMP 연결\r\n" + 
			" 8 : PSTN ( 현재 사용 안함 )\r\n" + 
			" 16 : TCP/IP 연결\r\n" + 
			" 32 : ZigBee 연결\r\n" + 
			" 64 : UDP/IP 연결\r\n" + 
			" 128 : BACnet 연결\r\n" + 
			" 256 :  File 접근\r\n" + 
			" 512 : PSM 연결\r\n" + 
			" 1024 : DB 접근\r\n" + 
			" 2048  : Modbus 연결\r\n" + 
			" 4096 : iLon 연결\r\n" + 
			" 8192 : LNS DDE 연결\r\n" + 
			" 32768 : PLC 연결\r\n" + 
			" 12288 : 가상 연결\r\n" + 
			" 65536 : IPMI 연결\r\n" + 
			" 131072 : SNMP(MANAGER) 연결\r\n" + 
			" 196608 : MUX 연결\r\n" + 
			" 262144 : UDP RECV 연결\r\n" + 
			" 327680 : UDP/IP 연결(Bindless)\r\n" + 
			" 393218 : Midas 연결\r\n" + 
			" 458752 : Rackguard 연결\r\n" + 
			" 524288 : BACnet REST Agent 연결\r\n" + 
			" 589824 : REST API 연결";
	
	
	// 04. RTU_INDEX
	public static final String SERVERINFO_FACILITY_RTU_INDEX = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : RTU_INDEX\r\n" + 
			"\r\n" + 
			"해당 시설물이 RCU를 통하여 통신하는 경우\r\n" + 
			"( 시리얼 포트, PLC 연결 방식 등 )\r\n" + 
			"\r\n" + 
			"RCU 장비의 인덱스를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, SERVERINFO 테이블 내용 중\r\n" + 
			"RCU 장비의 nServerIndex 컬럼 값입니다\r\n" + 
			"\r\n" + 
			"( RCU를 통하여 통신하지 않는다면 기본 값으로 0을 가집니다 )";
	
	
	// 05. RTU_PORT_NUM
	public static final String SERVERINFO_FACILITY_RTU_PORT_NUM = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : RTU_PORT_NUM\r\n" + 
			"\r\n" + 
			"해당 시설물에 등록된 포트 번호를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"만약 해당 시설물이 RCU를 통하여 통신하는 경우에는\r\n" + 
			"RCU 장비의 포트 번호를 의미합니다\r\n" + 
			"( 시리얼 포트, PLC 연결 방식 등 )\r\n" + 
			"\r\n" + 
			"이는 1470 같은 직접적인 RCU의 포트 번호가 아닌\r\n" + 
			"시리얼 포트 연결 방식으로 시설물 장비를 등록 시\r\n" + 
			"통신 할 RCU 선택 후,\r\n" + 
			"바로 다음으로 선택하게 되는 '사용 포트' 폼의 값 입니다\r\n" + 
			"\r\n" + 
			"즉, 연동 할 RCU가 여러개의 포트를 지원 할 경우\r\n" + 
			"지원하는 포트 중 몇 번째 포트인지를 의미합니다\r\n" + 
			"\r\n" + 
			"대부분의 RCU는\r\n" + 
			"하나의 포트를 사용하기 때문에\r\n" + 
			"해당 컬럼 값으로 1 을 사용하지만\r\n" + 
			"\r\n" + 
			"RCU 종류 중\r\n" + 
			"TCP/IP Multiport RCU 같은 경우\r\n" + 
			"RCU 하나에 16개의 포트를 지원하기 때문에\r\n" + 
			"\r\n" + 
			"컬럼 값 확인 시 참고해주시기 바랍니다";
	
	
	// 06. SNMP_MIB
	public static final String SERVERINFO_FACILITY_SNMP_MIB = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : SNMP_MIB\r\n" + 
			"\r\n" + 
			"해당 시설물이 SNMP 연결 방식으로 통신 할 경우\r\n" + 
			"\r\n" + 
			"등록 된 SNMP 프로토콜의 번호를 의미합니다\r\n" + 
			"\r\n" + 
			"( 시설물 종류마다 SNMP 프로토콜 번호가 상이합니다 )";
	
	
	// 07. COMM_PROTOCOL
	public static final String SERVERINFO_FACILITY_COMM_PROTOCOL = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : COMM_PROTOCOL\r\n" + 
			"\r\n" + 
			"해당 시설물에 등록 된 프로토콜 번호를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"프로토콜 명이 동일하여도\r\n" + 
			"시설물 종류마다 프로토콜 번호가 다를 수 있습니다\r\n" + 
			"\r\n" + 
			"( UPS - PLC 연동 프로토콜 번호 : 58 )\r\n" + 
			"( CRAC - PLC 연동 프로토콜 번호 : 69 )\r\n" + 
			"( 분전반 - PLC 연동 프로토콜 번호 : 42 )";
	
	// 08. PROTOCOL_DATA
	public static final String SERVERINFO_FACILITY_PROTOCOL_DATA = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : PROTOCOL_DATA\r\n" + 
			"\r\n" + 
			"해당 시설물이 실제 장비와 통신하기 위하여\r\n" + 
			"필요한 정보를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"해당 컬럼은 프로토콜 개발자가 프로토콜 개발 작업 중\r\n" + 
			"통신에 필요한 정보가 있다고 판단 될 시 정의하며\r\n" + 
			"\r\n" + 
			"Modbus 프로토콜을 예로 들면\r\n" + 
			"요청 패킷(TX) 내용에 \r\n" + 
			"몇 번 장비에 대한 요청인지 장비 번호 정보가 필요합니다\r\n" + 
			"\r\n" + 
			"이 때 장비 번호에 대한 정보는\r\n" + 
			"등록하는 사용자의 설정에 따라 변경 될 수 있기 때문에\r\n" + 
			"장비 등록시 Unit ID 라는 폼으로 입력받게 됩니다\r\n" + 
			"\r\n" + 
			"이렇게 사용자로부터 Unit ID 폼에 입력받은 값이\r\n" + 
			"SERVERINFO_FACILITY 테이블의 \r\n" + 
			"PROTOCOL_DATA 컬럼의 내용으로 저장됩니다\r\n" + 
			"\r\n" + 
			"( 입력 폼의 Unit ID 라는 문자열은 개발자가 정의한 것이며\r\n" + 
			"개발자의 판단에 따라 다른 문자열이 될 수도 있습니다 )";
	
	
	// 09. packetLogEnable
	public static final String SERVERINFO_FACILITY_packetLogEnable = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : packetLogEnable\r\n" + 
			"\r\n" + 
			"해당 시설물의 패킷 로그 사용 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 패킷 로그 사용 안함\r\n" + 
			" 1 : 패킷 로그 사용";
	
	// 10. packetLogFileName
	public static final String SERVERINFO_FACILITY_packetLogFileName = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : packetLogFileName\r\n" + 
			"\r\n" + 
			"해당 시설물의 패킷 로그 이름을 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"컬럼 내용에 패킷 로그 파일이 저장된 경로를 포함하며\r\n" + 
			"\r\n" + 
			"해당 시설물의 등록 정보에서 확인 할 수 있습니다";
	
	// 11. packetLogMaxFileSize
	public static final String SERVERINFO_FACILITY_packetLogMaxFileSize = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : packetLogMaxFileSize\r\n" + 
			"\r\n" + 
			"해당 시설물의 패킷로그 파일 1개당 \r\n" + 
			"\r\n" + 
			"최대 크기( 단위 : MB )를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"시설물의 등록 정보에서 확인 할 수 있습니다";
	
	// 12. packetLogMaxBackupIndex
	public static final String SERVERINFO_FACILITY_packetLogMaxBackupIndex = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : packetLogMaxBackupIndex\r\n" + 
			"\r\n" + 
			"해당 시설물의 패킷 로그 파일 개수 제한 값을 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"시설물은 설정된 패킷 로그의 최대 크기만큼\r\n" + 
			"\r\n" + 
			"패킷 내용이 기록되면 새로운 패킷 로그 파일을 생성하는데\r\n" + 
			"\r\n" + 
			"이 때 생성 할 수 있는 최대 패킷 로그 파일의 개수를 의미합니다\r\n" + 
			"\r\n" + 
			"시설물의 등록 정보에서 확인 할 수 있습니다";
	
	// 13. AUX_PORT_NUM
	public static final String SERVERINFO_FACILITY_AUX_PORT_NUM = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : AUX_PORT_NUM\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 14. PHASE_TYPE
	public static final String SERVERINFO_FACILITY_PHASE_TYPE = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : PHASE_TYPE\r\n" + 
			"\r\n" + 
			"시설물 종류 랙 등록 시\r\n" + 
			"\r\n" + 
			"연결 방법으로 PSM 연결 방식을 지정하면\r\n" + 
			"\r\n" + 
			"선택 할 수 있는 분기 정보를 의미합니다\r\n" + 
			"\r\n" + 
			" single : 단상\r\n" + 
			" three : 3 상\r\n" + 
			" combi : 혼합";
	
	// 15. deviceLocation
	public static final String SERVERINFO_FACILITY_deviceLocation = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : deviceLocation\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 16. offeredITSM
	public static final String SERVERINFO_FACILITY_offeredITSM = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : offeredITSM\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 17. operationDepartment
	public static final String SERVERINFO_FACILITY_operationDepartment = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : operationDepartment\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 18. serviceName
	public static final String SERVERINFO_FACILITY_serviceName = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : serviceName\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 19. RESPONSE_TIMEOUT
	public static final String SERVERINFO_FACILITY_RESPONSE_TIMEOUT = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : RESPONSE_TIMEOUT\r\n" + 
			"\r\n" + 
			"해당 시설물의 응답 타임아웃을 의미합니다 \r\n" + 
			"\r\n" + 
			"기본 값 : 4000 ( 단위 : millisecond )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"시리얼 포트 연결 방식으로\r\n" + 
			"하나의 RCU 아래에 여러 장비가 물려 통신하는 경우\r\n" + 
			"\r\n" + 
			"그 중 하나의 장비에서 응답을 주지 않거나,\r\n" + 
			"응답을 늦게 주는 경우\r\n" + 
			"\r\n" + 
			"해당 장비는 설정 된 타임아웃 시간동안 응답을 대기합니다\r\n" + 
			"\r\n" + 
			"이 때 동일한 RCU에 물려있는 다른 장비들은\r\n" + 
			"위의 타임아웃 시간동안 대기하게 되며\r\n" + 
			"성능 수집 주기에 영향을 줍니다\r\n" + 
			"\r\n" + 
			"이러한 경우 응답이 느린 장비의 타임아웃을 짧게 설정한다면\r\n" + 
			"타임아웃 내에 오지 않는 응답에 대해서는 패스하고\r\n" + 
			"\r\n" + 
			"다음 장비의 요청을 전송하여\r\n" + 
			"\r\n" + 
			"성능 수집 주기가 밀리는 현상을 해소 할 수 있습니다";
	
	
	// 20. USE_SO_TIMEOUT
	public static final String SERVERINFO_FACILITY_USE_SO_TIMEOUT = 
			"테이블 명 : SERVERINFO_FACILITY\r\n" + 
			"컬럼 명 : USE_SO_TIMEOUT\r\n" + 
			"\r\n" + 
			"시설물의 소켓 타임아웃 사용 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 소켓 타임아웃 사용 안함\r\n" + 
			" 1 : 소켓 타임아웃 사용\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"TCP/IP 연결 방식으로 등록 된 장비에서 설정 할 수 있으며\r\n" + 
			"\r\n" + 
			"장비와의 재접속으로 통신 오류가 복구되는 경우는\r\n" + 
			"해당 옵션 적용으로 통신 오류 이벤트 발생 없이 복구됩니다\r\n" + 
			"\r\n" + 
			"( 시설물의 응답 타임아웃 내용과는 다른 옵션입니다 )\r\n" + 
			"( SERVERINFO_FACILITY 테이블의 \r\n" + 
			" RESPONSE_TIMEOUT 컬럼과는 다른 내용 )";
	
	
	
	/**
	 * SERVERINFO_RTU 테이블
	 */
	// 01. NODE_INDEX
	public static final String SERVERINFO_RTU_NODE_INDEX = 
			"테이블 명 : SERVERINFO_RTU\r\n" + 
			"컬럼 명 : NODE_INDEX\r\n" + 
			"\r\n" + 
			"SERVERINFO_RTU 테이블의 기본키(primary key) 입니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 해당 컬럼은 null 값이나 중복 값을 가질 수 없으며\r\n" + 
			"RCU의 고유한 인덱스를 의미합니다\r\n" + 
			"\r\n" + 
			"또한 해당 컬럼은 SERVERINFO 테이블의 \r\n" + 
			"nServerIndex 컬럼과 동일한 내용을 공유합니다\r\n" + 
			"\r\n" + 
			"즉, SERVERINFO 테이블의\r\n" + 
			"nServerIndex = 18인 서버(RCU)의 RCU 관련 정보는\r\n" + 
			"\r\n" + 
			"SERVERINFO_RTU 테이블의\r\n" + 
			"NODE_INDEX = 18인 행(레코드) 입니다";
	
	// 02. RTU_TYPE
	public static final String SERVERINFO_RTU_RTU_TYPE = 
			"테이블 명 : SERVERINFO_RTU\r\n" + 
			"컬럼 명 : RTU_TYPE\r\n" + 
			"\r\n" + 
			"RCU 종류를 의미합니다\r\n" + 
			"\r\n" + 
			" 3 : MK RCU V1.0\r\n" + 
			" 5 : TCP/IP RCU\r\n" + 
			" 6 : MK119 - REM 2408\r\n" + 
			" 9 : MK119 - REM 1204\r\n" + 
			" 11 : MK119 - REM 1204 v1.0.3\r\n" + 
			" 12 : Passive TCP/IP Server\r\n" + 
			" 13 : LSIS XGT PLC\r\n" + 
			" 14 : PoscoICT HVAC SI\r\n" + 
			" 15 : CIMON PLC\r\n" + 
			" 16 : LSIS GLOFA PLC\r\n" + 
			" 17 : MK Active RCU\r\n" + 
			" 18 : TCP/IP Multiport RCU\r\n" + 
			" 19 : Modbus Gateway\r\n" + 
			" 20 : TCP/IP 이중화 RCU";
	
	
	// 03. NUM_AI_CHANNEL
	public static final String SERVERINFO_RTU_NUM_AI_CHANNEL = 
			"테이블 명 : SERVERINFO_RTU\r\n" + 
			"컬럼 명 : NUM_AI_CHANNEL\r\n" + 
			"\r\n" + 
			"아날로그 입력 채널 개수를 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 04. NUM_DI_CHANNEL
	public static final String SERVERINFO_RTU_NUM_DI_CHANNEL = 
			"테이블 명 : SERVERINFO_RTU\r\n" + 
			"컬럼 명 : NUM_DI_CHANNEL\r\n" + 
			"\r\n" + 
			"디지털 입력 채널 개수를 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 05. NUM_AO_CHANNEL
	public static final String SERVERINFO_RTU_NUM_AO_CHANNEL = 
			"테이블 명 : SERVERINFO_RTU\r\n" + 
			"컬럼 명 : NUM_AO_CHANNEL\r\n" + 
			"\r\n" + 
			"아날로그 출력 채널 개수를 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 06. NUM_DO_CHANNEL
	public static final String SERVERINFO_RTU_NUM_DO_CHANNEL = 
			"테이블 명 : SERVERINFO_RTU\r\n" + 
			"컬럼 명 : NUM_DO_CHANNEL\r\n" + 
			"\r\n" + 
			"디지털 출력 채널 개수를 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 07. NUM_SERIAL_PORT
	public static final String SERVERINFO_RTU_NUM_SERIAL_PORT = 
			"테이블 명 : SERVERINFO_RTU\r\n" + 
			"컬럼 명 : NUM_SERIAL_PORT\r\n" + 
			"\r\n" + 
			"RCU 에서 지원하는 포트 개수를 의미합니다\r\n" + 
			"\r\n" + 
			"( 일반적인 RCU의 경우 1개의 포트를 지원하지만\r\n" + 
			"\r\n" + 
			" TCP/IP Multiport RCU의 경우 최대 16개의 포트를 지원합니다 )";
	
	
	// 08. USE_PSTN
	public static final String SERVERINFO_RTU_USE_PSTN = 
			"테이블 명 : SERVERINFO_RTU\r\n" + 
			"컬럼 명 : USE_PSTN\r\n" + 
			"\r\n" + 
			"PSTN 접속 사용 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : PSTN 접속 사용 안함\r\n" + 
			" 1 : PSTN 접속 사용\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"( PSTN : Public Switched Telephone Network )\r\n" + 
			"\r\n" + 
			"PSTN이란 전국에 설치 된 전화 통신망을 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 09. PSTN_COM_PORT
	public static final String SERVERINFO_RTU_PSTN_COM_PORT = 
			"테이블 명 : SERVERINFO_RTU\r\n" + 
			"컬럼 명 : PSTN_COM_PORT\r\n" + 
			"\r\n" + 
			"PSTN 접속 시 사용 할 시리얼 포트 번호를 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 10. PSTN_PHONE_NUMBER
	public static final String SERVERINFO_RTU_PSTN_PHONE_NUMBER = 
			"테이블 명 : SERVERINFO_RTU\r\n" + 
			"컬럼 명 : PSTN_PHONE_NUMBER\r\n" + 
			"\r\n" + 
			"PSTN 접속 전화 번호를 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 11. PASSIVE_TCP_SERVER_PORT
	public static final String SERVERINFO_RTU_PASSIVE_TCP_SERVER_PORT = 
			"테이블 명 : SERVERINFO_RTU\r\n" + 
			"컬럼 명 : PASSIVE_TCP_SERVER_PORT\r\n" + 
			"\r\n" + 
			"해당 RCU의 포트 번호를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"TCP/IP RCU의 경우 주로 1470번 을 사용합니다\r\n" + 
			"\r\n" + 
			"( TCP/IP Multiport RCU의 경우\r\n" + 
			" 최대 16개의 포트를 지원하기 때문에 별도로 포트 정보를 관리하며\r\n" + 
			"\r\n" + 
			" SERVERINFO_RTU 테이블의 \r\n" + 
			" PASSIVE_TCP_SERVER_POR 컬럼에는 0 값을 저장하고\r\n" + 
			"\r\n" + 
			" SERVERINFO_RTU_MULTIPORT 테이블에서\r\n" + 
			" 16개 포트에 대하여 포트 번호 정보를 관리합니다 )";
	
	
	// 12. rcu_id
	public static final String SERVERINFO_RTU_rcu_id = 
			"테이블 명 : SERVERINFO_RTU\r\n" + 
			"컬럼 명 : rcu_id\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 13. AUX_TCP_PORT
	public static final String SERVERINFO_RTU_AUX_TCP_PORT = 
			"테이블 명 : SERVERINFO_RTU\r\n" + 
			"컬럼 명 : AUX_TCP_PORT\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	/**
	 * SERVER_PERF 테이블
	 */
	// 01. nPerfIndex
	public static final String SERVER_PERF_nPerfIndex = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : nPerfIndex\r\n" + 
			"\r\n" + 
			"SERVER_PERF 테이블의 기본키(primary key) 입니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 해당 컬럼은 null 값이나 중복 값을 가질 수 없으며\r\n" + 
			"\r\n" + 
			"성능의 고유한 인덱스를 의미합니다";
	
	
	// 02. nServerIndex
	public static final String SERVER_PERF_nServerIndex = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : nServerIndex\r\n" + 
			"\r\n" + 
			"해당 성능을 보유한 장비의 인덱스를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 해당 성능을 가지고있는 장비 인덱스를 의미하는\r\n" + 
			"\r\n" + 
			"SERVERINFO 테이블의\r\n" + 
			"nServerIndex 컬럼과 동일한 내용을 공유합니다";
	
	// 03. nPerfType
	public static final String SERVER_PERF_nPerfType = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : nPerfType\r\n" + 
			"\r\n" + 
			"성능 검사 방법을 의미합니다\r\n" + 
			"\r\n" + 
			" 1 : 에이전트\r\n" + 
			" 2 : SNMP\r\n" + 
			" 3 : 포트 응답\r\n" + 
			" 4 : 오라클 DB\r\n" + 
			" 5 : RCU 접점\r\n" + 
			" 6 : RCU 시리얼 연결\r\n" + 
			" 8 : TCP 시리얼 연결\r\n" + 
			" 9 : ZigBee 코디네이터 연결\r\n" + 
			" 10 : UDP 연결\r\n" + 
			" 11 : BACnet 연결\r\n" + 
			" 12 : File 접근\r\n" + 
			" 13 : PSM 연결\r\n" + 
			" 14 : DB 접근\r\n" + 
			" 15 : Modbus 연결\r\n" + 
			" 16 : iLON 연결\r\n" + 
			" 17 : LNS DDE 연결\r\n" + 
			" 18 : PLC 연결\r\n" + 
			" 19 : 가상성능\r\n" + 
			" 20 : IPMI 연결\r\n" + 
			" 22 : 가상(누적)\r\n" + 
			" 23 : 가상(일전력량)\r\n" + 
			" 24 : 가상(월전력량) \r\n" + 
			" 25 : 가상(SQL)\r\n" + 
			" 26 : 리포트성능\r\n" + 
			" 27 : MUX 연동\r\n" + 
			" 28 : UDP RECV 연동\r\n" + 
			" 29 : REST\r\n" + 
			" 30 : MidasCon\r\n" + 
			" 31 : MidasAp\r\n" + 
			" 32 : 가상(리셋 카운터) \r\n" + 
			" 33 : 랙가드 연결\r\n" + 
			" 34 : 일가동시간\r\n" + 
			" 35 : 가상(초기화)\r\n" + 
			" 36 : 가상(측정시간)\r\n" + 
			" 37 : REST API\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 04. strDisplayName
	public static final String SERVER_PERF_strDisplayName = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : strDisplayName\r\n" + 
			"\r\n" + 
			"성능명을 의미합니다";
	
	// 05. strPerfCounter
	public static final String SERVER_PERF_strPerfCounter = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : strPerfCounter\r\n" + 
			"\r\n" + 
			"성능의 카운터(PerfCounter)를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"카운터는 프로토콜 개발자가 정의하며\r\n" + 
			"요청 패킷을 생성하기 위한 정보로 주로 사용됩니다\r\n" + 
			"\r\n" + 
			"즉, 카운터는 하나의 요청을 식별 할 수 있는 정보입니다\r\n" +
			"\r\n" +
			"장비마다 통신 방법, 패킷 구조가 상이하기 때문에\r\n" + 
			"프로토콜마다 성능의 카운터가 상이 할 수 있습니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"─────────────────────────\r\n" + 
			"\r\n" + 
			"strPerfCounter 컬럼의 경우 \r\n" + 
			"\r\n" + 
			"중요한 내용이기 때문에 자세하게 설명하겠습니다\r\n" + 
			"\r\n" + 
			"동일한 카운터를 가진 성능들은 \r\n" + 
			"동일한 하나의 요청을 통하여 응답을 받아 처리됩니다\r\n" + 
			"\r\n" + 
			"또한 여러 성능이 동일한 카운터를 가지고있어도\r\n" + 
			"모두 동일한 성능이라고는 할 수 없습니다\r\n" + 
			"\r\n" + 
			"실제 장비로부터 응답 받은 데이터를\r\n" + 
			"성능 카운터 마지막에 붙는 슬롯 ( {?} )으로 구분하거나\r\n" + 
			"비트 연산을 통하여 Bit ON/OFF 상태로 구분하여\r\n" + 
			"\r\n" + 
			"각각 다른 성능 값으로 처리 할 수 있기 때문입니다\r\n" + 
			"\r\n" + 
			"A : 3_0_10\\{1}\r\n" + 
			"B : 3_0_10\\{2}\r\n" + 
			"C : 3_0_10\\{3}\r\n" + 
			"\r\n" + 
			"위에 A, B, C 성능이 있습니다\r\n" + 
			"이들 성능의 카운터 양식은 3_0_10\\{?} 으로\r\n" + 
			"\r\n" + 
			"카운터는 '\\' 문자를 기준으로 정보가 나뉘게 됩니다\r\n" + 
			"( 3_0_10 과 {1},{2},{3}(슬롯) )\r\n" + 
			"\r\n" + 
			"앞의 3_0_10은 요청 패킷을 만드는데 사용되는 정보이며\r\n" + 
			"그 뒤에 붙는 슬롯 {1}, {2}, {3}은 \r\n" + 
			"\r\n" + 
			"장비로부터 응답받은 패킷을 처리하여 얻은 데이터를\r\n" + 
			"순서대로 A{1}, B{2}, C{3} 에 성능 값으로\r\n" + 
			"업데이트 하겠다는 의미입니다\r\n" + 
			"\r\n" + 
			"그렇기 때문에 3_0_10 이라는 정보로 만들어진 요청패킷으로\r\n" + 
			"장비에게 요청하여 응답받은 응답 패킷을 처리하여\r\n" + 
			"A, B, C 성능에 값을 줄 수 있는 것 입니다.\r\n" + 
			"\r\n" + 
			"즉 A, B, C는 각각 다른 성능이지만\r\n" + 
			"동일한 한번의 요청을 통하여 응답패킷을 받아\r\n" + 
			"성능 값이 결정되는 것 입니다";
	
	
	// 06. nPerfInterval
	public static final String SERVER_PERF_nPerfInterval = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : nPerfInterval\r\n" + 
			"\r\n" + 
			"성능의 수집주기를 의미합니다 ( 단위 : 초 )";
	
	// 07. strMeasure
	public static final String SERVER_PERF_strMeasure = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : strMeasure\r\n" + 
			"\r\n" + 
			"성능의 단위를 의미합니다\r\n" + 
			"\r\n" + 
			" 온도 : ℃\r\n" + 
			" 습도 : %\r\n" + 
			" 전압 : V\r\n" + 
			" 전류 : A";
	
	// 08. nEnable
	public static final String SERVER_PERF_nEnable = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : nEnable\r\n" + 
			"\r\n" + 
			"성능 사용 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 중지\r\n" + 
			" 1 : 사용";
	
	// 09. dblLastUsage
	public static final String SERVER_PERF_dblLastUsage = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : dblLastUsage\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 10. strLastUsageTime
	public static final String SERVER_PERF_strLastUsageTime = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : strLastUsageTime\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 11. strOperation
	public static final String SERVER_PERF_strOperation = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : strOperation\r\n" + 
			"\r\n" + 
			"성능의 보정식을 의미합니다\r\n" + 
			"\r\n" + 
			"( 원본 값 : 365 )\r\n" + 
			"( 보정식 : x/10 )\r\n" + 
			"( 성능 값 : 36.5 )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"───────────────────────\r\n" + 
			"\r\n" + 
			"( 원본 값 : 14 [0x1110] )\r\n" + 
			"( 보정식 : (x>>0)&1 )\r\n" + 
			"( 성능 값 : 0 )\r\n" + 
			"\r\n" + 
			"내용 : 원본 값(14 [0x1110])의 비트 구조에서\r\n" + 
			"        오른쪽 비트연산(>>)으로 0번 이동하고(제자리)\r\n" + 
			"        1 [0x0001] 과 &(AND) 연산을 함\r\n" + 
			"\r\n" + 
			"AND 연산이란\r\n" + 
			"연산자와 피연산자의 비트 자리가 모두 1(참)일 때 \r\n" + 
			"결과 값이 1 이고\r\n" + 
			"연산자와 피연산자 중 하나라도 0이면\r\n" + 
			"결과 값이 0 입니다\r\n" + 
			"\r\n" + 
			"즉,\r\n" + 
			"0x1110 ( 원본 값 : 14 )\r\n" + 
			"0x0001 ( AND연산 피연산자 : 1 )\r\n" + 
			"\r\n" + 
			"위의 비트 구조를 보면 AND 연산 시\r\n" + 
			"연산자 피연산자 모두 1이 되는 비트 자리가 없기 때문에\r\n" + 
			"보정식 적용 결과 0이라는 값이 나온 것 입니다\r\n" + 
			"\r\n" + 
			"───────────────────────\r\n" + 
			"\r\n" + 
			"( 원본 값 : 15 [0x1111] )\r\n" + 
			"( 보정식 : (x>>3)&1 )\r\n" + 
			"( 성능 값 : 1 )\r\n" + 
			"\r\n" + 
			"내용 : 원본 값(15 [0x1111])의 비트 구조에서\r\n" + 
			"        >>(오른쪽 비트연산)으로 3번 이동하고\r\n" + 
			"        1[0x0001] 과 &(AND) 연산을 함\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"0x1111 ( 원본 값 : 15 ) 에서\r\n" + 
			"오른쪽 비트연산(>>) 을 수행하면 0x0001 입니다\r\n" + 
			"(오른쪽으로 비트 세칸만큼 이동하고 빈공간은 0으로 채워짐)\r\n" + 
			"\r\n" + 
			"그 후\r\n" + 
			"1 [0x0001] 값과 AND 연산을 합니다\r\n" + 
			"\r\n" + 
			"0x0001 ( 원본 값에서 오른쪽 비트연산 3번 수행 결과 : 1 [0x0001] )\r\n" + 
			"0x0001 ( AND연산 피연산자 : 1 [0x0001] )\r\n" + 
			"\r\n" + 
			"위의 비트 구조를 보면 AND 연산 시\r\n" + 
			"0번 비트의 자리에서\r\n" + 
			"연산자와 피연산자의 비트가 모두 1이기 때문에\r\n" + 
			"결과 값으로 1이 나온 것 입니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"만약 위의 식에서\r\n" + 
			"원본 값 ( 15 [0x1111] )에서 비트연산을 하지않고\r\n" + 
			"피연산자가 ( 1 [0x0001] )이 아니라 ( 9 [0x1001] )이었다면\r\n" + 
			"\r\n" + 
			"0x1111 ( 원본 값 : 15 )\r\n" + 
			"0x1001 ( AND연산 피연산자 : 9 )\r\n" + 
			"\r\n" + 
			"연산자( 15 )와 피연산자( 9 )의\r\n" + 
			"0번 비트와\r\n" + 
			"3번 비트가\r\n" + 
			"모두 1 이기 때문에 \r\n" + 
			"\r\n" + 
			"결과 값이 9 [0x1001] 입니다\r\n" + 
			"\r\n" + 
			"위와 같은 방법으로 같은 레지스터에서 값을 읽었더라도\r\n" + 
			"비트 연산을 통하여 DI 성능을 표시 할 수 있습니다\r\n" + 
			"\r\n" + 
			"0 : OFF\r\n" + 
			"1 : ON \r\n" + 
			"\r\n" + 
			"또는 \r\n" + 
			"\r\n" + 
			"0 : Normal\r\n" + 
			"1 : Alarm";
	
	// 12. strCommandCode
	public static final String SERVER_PERF_strCommandCode = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : strCommandCode\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 13. nPortNumber
	public static final String SERVER_PERF_nPortNumber = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : nPortNumber\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 14. DATA_FORMAT
	public static final String SERVER_PERF_DATA_FORMAT = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : DATA_FORMAT\r\n" + 
			"\r\n" + 
			"성능의 데이터 형식을 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 이진 상태 ( DI )\r\n" + 
			" 1 : 다중 상태 ( 멀티 값 )\r\n" + 
			" 2 : 성능 데이터 ( 아날로그 성능 )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"이진 상태( DI )의 레이블 정보는\r\n" + 
			"( 0, 1 일 때 표시해 줄 문자열 정보 )\r\n" + 
			"PERF_LABEL_BOOLEAN 테이블에서 관리합니다\r\n" + 
			"\r\n" + 
			"다중 상태( 멀티 값 )의 레이블 정보는\r\n" + 
			"( 성능 값과 매핑해줄 문자열 정보 )\r\n" + 
			"PERF_LABEL_STATUS 테이블에서 관리합니다";
	
	// 15. DELTA_COLLECTION
	public static final String SERVER_PERF_DELTA_COLLECTION = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : DELTA_COLLECTION\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 16. SAVE_DATA
	public static final String SERVER_PERF_SAVE_DATA = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : SAVE_DATA\r\n" + 
			"\r\n" + 
			"성능의 데이터 저장 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 데이터 저장 안함\r\n" + 
			" 1 : 데이터 저장 함\r\n" + 
			"\r\n" + 
			"0 ( 데이터 저장 안함 )을 적용 시 FileDB를 기록하지 않습니다";
	
	// 17. DB_SAVE_METHOD
	public static final String SERVER_PERF_DB_SAVE_METHOD = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : DB_SAVE_METHOD\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 18. FAIR_LIMIT_UPPER
	public static final String SERVER_PERF_FAIR_LIMIT_UPPER = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : FAIR_LIMIT_UPPER\r\n" + 
			"\r\n" + 
			"성능 적정 범위 지정 상위 값 ( 단위 : % )";
	
	// 19. FAIR_LIMIT_LOWER
	public static final String SERVER_PERF_FAIR_LIMIT_LOWER = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : FAIR_LIMIT_LOWER\r\n" + 
			"\r\n" + 
			"성능 적정 범위 지정 하위 값 ( 단위 : % )";
	
	// 20. MODIFY_OPERATION
	public static final String SERVER_PERF_MODIFY_OPERATION = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : MODIFY_OPERATION\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 21. VALID_RANGE_ENABLE
	public static final String SERVER_PERF_VALID_RANGE_ENABLE = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : VALID_RANGE_ENABLE\r\n" + 
			"\r\n" + 
			"수집 유효 범위 옵션 사용 여부\r\n" + 
			"\r\n" + 
			" 0 : 사용 안함\r\n" + 
			" 1 : 사용";
	
	// 22. VALID_RANGE_UPPER
	public static final String SERVER_PERF_VALID_RANGE_UPPER = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : VALID_RANGE_UPPER\r\n" + 
			"\r\n" + 
			"성능 수집 유효 범위 상위 값";
	
	// 23. VALID_RANGE_LOWER
	public static final String SERVER_PERF_VALID_RANGE_LOWER = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : VALID_RANGE_LOWER\r\n" + 
			"\r\n" + 
			"성능 수집 유효 범위 하위 값";
	
	// 24. perf_property
	public static final String SERVER_PERF_perf_property = 
			"테이블 명 : SERVER_PERF\r\n" + 
			"컬럼 명 : perf_property\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	

	
	/**
	 * ALARM 테이블 정보
	 */
	// 01. nAlarmIndex
	public static final String ALARM_nAlarmIndex = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nAlarmIndex\r\n" + 
			"\r\n" + 
			"ALARM 테이블의 기본키(primary key) 입니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 해당 컬럼은 null 값이나 중복 값을 가질 수 없으며\r\n" + 
			"\r\n" + 
			"이벤트의 고유한 인덱스를 의미합니다";
	
	
	// 02. strAlarmName
	public static final String ALARM_strAlarmName = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : strAlarmName\r\n" + 
			"\r\n" + 
			"이벤트의 이름을 의미합니다";
	
	// 03. nEventSeverity
	public static final String ALARM_nEventSeverity = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nEventSeverity\r\n" + 
			"\r\n" + 
			"이벤트의 심각도를 의미합니다\r\n" + 
			"\r\n" + 
			" 10 : Normal\r\n" + 
			" 20 : Warning\r\n" + 
			" 30 : Minor\r\n" + 
			" 40 : Critical\r\n" + 
			" 50 : Fatal";
	
	// 04. nAlarmKind
	public static final String ALARM_nAlarmKind = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nAlarmKind\r\n" + 
			"\r\n" + 
			"이벤트의 종류를 의미합니다\r\n" + 
			"\r\n" + 
			" 1 : 시스템\r\n" + 
			" 2 : 성능\r\n" + 
			" 3 : 로그\r\n" + 
			" 4 : 액션\r\n" + 
			" 5 : MK119 로그\r\n" + 
			" 6 : 관리콘솔 로그\r\n" + 
			" 7 : 모바일 로그\r\n" + 
			" 8 : 서비스 / 프로세스";
	
	// 05. nServerIndex
	public static final String ALARM_nServerIndex = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nServerIndex\r\n" + 
			"\r\n" + 
			"해당 이벤트를 보유한 장비의 인덱스를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 해당 이벤트를 가지고있는 장비 인덱스를 의미하는\r\n" + 
			"\r\n" + 
			"SERVERINFO 테이블의\r\n" + 
			"nServerIndex 컬럼과 동일한 내용을 공유합니다";
	
	// 06. nEnable
	public static final String ALARM_nEnable = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nEnable\r\n" + 
			"\r\n" + 
			"이벤트의 발생 사용 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 중지\r\n" + 
			" 1 : 사용";
	
	// 07. nNotify
	public static final String ALARM_nNotify = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nNotify\r\n" + 
			"\r\n" + 
			"이벤트의 통보 사용 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 중지\r\n" + 
			" 1 : 사용";
	
	// 08. nWarnStatus
	public static final String ALARM_nWarnStatus = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nWarnStatus\r\n" + 
			"\r\n" + 
			"이벤트의 이상 유무를 의미합니다\r\n" + 
			"\r\n" + 
			"-1 : 알 수 없음\r\n" + 
			" 0 : 정상\r\n" + 
			" 1 : 이상\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 09. dblErrorValue
	public static final String ALARM_dblErrorValue = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : dblErrorValue\r\n" + 
			"\r\n" + 
			"이벤트의 이상 유무 판단을 위한 필드입니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 10. strMeasure
	public static final String ALARM_strMeasure = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : strMeasure\r\n" + 
			"\r\n" + 
			"dblErrorValue 컬럼의 단위를 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 11. nAlarmCheckMode
	public static final String ALARM_nAlarmCheckMode = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nAlarmCheckMode\r\n" + 
			"\r\n" + 
			"이벤트 발생 모드를 의미합니다\r\n" + 
			"\r\n" + 
			" 1 : 지정된 시간동안 상태가 지속 될 경우\r\n" + 
			" 2 : 지정된 시간동안 지정된 횟수만큼 조건이 발생 할 경우\r\n" + 
			" 3 : 조건 발생시 한번만\r\n" + 
			" 4 : 조건이 발생 할 때마다\r\n" + 
			" 6 : 지정된 횟수만큼 조건이 연속으로 발생 할 경우";
	
	// 12. nAlarmSequence
	public static final String ALARM_nAlarmSequence = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nAlarmSequence\r\n" + 
			"\r\n" + 
			"이벤트의 통보 횟수를 의미합니다";
	
	// 13. nAlarmInterval
	public static final String ALARM_nAlarmInterval = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nAlarmInterval\r\n" + 
			"\r\n" + 
			"이벤트의 지속 시간을 의미합니다 ( 단위 : 분 )";
	
	// 14. nHitCount
	public static final String ALARM_nHitCount = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nHitCount\r\n" + 
			"\r\n" + 
			"이벤트의 이상 발생 횟수를 의미합니다";
	
	// 15. nAlarmMedia
	public static final String ALARM_nAlarmMedia = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nAlarmMedia\r\n" + 
			"\r\n" + 
			"이벤트 통보 방법을 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 사용 안함\r\n" + 
			" 1 : SMS 사용\r\n" + 
			" 2 : Mail 사용\r\n" + 
			" 3 : SMS, Mail 사용\r\n" + 
			" 4 : Action 사용\r\n" + 
			" 5 : SMS, Action 사용\r\n" + 
			" 6 : Mail, Action 사용\r\n" + 
			" 7 : SMS, Mail, Action 사용 ( 모두 사용 )";
	
	// 16. nActionIndex
	public static final String ALARM_nActionIndex = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nActionIndex\r\n" + 
			"\r\n" + 
			"이벤트 통보 방법 중\r\n" + 
			"Action( 제어 명령 )을 선택 하였을 경우\r\n" + 
			"\r\n" + 
			"이벤트 발생 시\r\n" + 
			"수행하게 될 Action( 제어 명령)의 인덱스를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 이벤트 발생 시 수행하게 될 액션( 제어 ) 정보인\r\n" + 
			"\r\n" + 
			"SERVER_CONTROL 테이블의\r\n" + 
			"nControlIndex 컬럼 값을 의미합니다";
	
	// 17. nActionServerIndex
	public static final String ALARM_nActionServerIndex = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nActionServerIndex\r\n" + 
			"\r\n" + 
			"이벤트 통보 방법 중\r\n" + 
			"Action( 제어 명령 )을 선택 하였을 경우\r\n" + 
			"\r\n" + 
			"이벤트 발생 시\r\n" + 
			"수행하게 될 Action( 제어 명령 )을 가지고있는 \r\n" + 
			"\r\n" + 
			"장비의 인덱스를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, SERVER_CONTROL ( 제어 정보 )테이블의\r\n" + 
			"nServerIndex 컬럼 내용을 의미합니다";
	
	// 18. strActionCommand
	public static final String ALARM_strActionCommand = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : strActionCommand\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 19. strActionParameter
	public static final String ALARM_strActionParameter = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : strActionParameter\r\n" + 
			"\r\n" + 
			"이벤트 통보 방법 중\r\n" + 
			"Action( 제어 명령 )을 선택 하였을 경우\r\n" + 
			"\r\n" + 
			"이벤트 발생 시\r\n" + 
			"수행하게 될 Action( 제어 명령 )의 파라미터 값 입니다\r\n" + 
			"( 이벤트 등록 정보의 시작 파라미터 필드 )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 해당 이벤트가 발생하였을 때\r\n" + 
			"설정온도 설정 제어 명령어가 수행되도록 지정하였을 때\r\n" + 
			"설정온도 값을 몇으로 제어 할 지에 대한 값 입니다";
	
	// 20. nActionIndex2
	public static final String ALARM_nActionIndex2 = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nActionIndex2\r\n" + 
			"\r\n" + 
			"이벤트 통보 방법 중\r\n" + 
			"Action( 제어 명령 )을 선택 하였을 경우\r\n" + 
			"\r\n" + 
			"이벤트 발생 후 자동 복구( 정상화 ) 시\r\n" + 
			"수행하게 될 Action( 제어 명령)의 인덱스를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 이벤트 발생 후 자동 복구( 정상화 ) 시\r\n" + 
			"수행하게 될 액션 정보\r\n" + 
			"\r\n" + 
			"SERVER_CONTROL 테이블의\r\n" + 
			"nControlIndex 컬럼 값을 의미합니다";
	
	// 21. strActionCommand2
	public static final String ALARM_strActionCommand2 = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : strActionCommand2\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 22. strActionParameter2
	public static final String ALARM_strActionParameter2 = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : strActionParameter2\r\n" + 
			"\r\n" + 
			"이벤트 통보 방법 중\r\n" + 
			"Action( 제어 명령 )을 선택 하였을 경우\r\n" + 
			"\r\n" + 
			"이벤트 발생 후 자동 복구( 정상화 ) 시\r\n" + 
			"수행하게 될 Action( 제어 명령 )의 파라미터 값 입니다\r\n" + 
			"( 이벤트 등록 정보의 종료 파라미터 필드 )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 해당 이벤트가 발생 후 자동 복구( 정상화 ) 시\r\n" + 
			"설정온도 설정 제어 명령어가 수행되도록 지정하였을 때\r\n" + 
			"설정온도 값을 몇으로 제어 할 지에 대한 값 입니다";
	
	// 23. strClassName
	public static final String ALARM_strClassName = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : strClassName\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 24. strCustomMessage
	public static final String ALARM_strCustomMessage = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : strCustomMessage\r\n" + 
			"\r\n" + 
			"이벤트의 알림 메시지 설정 내용을 의미합니다";
	
	// 25. nSchedule
	public static final String ALARM_nSchedule = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nSchedule\r\n" + 
			"\r\n" + 
			"이벤트의 스케줄 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 스케줄 없음\r\n" + 
			" 1 : 스케줄링\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 26. AUTO_CLOSE
	public static final String ALARM_AUTO_CLOSE = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : AUTO_CLOSE\r\n" + 
			"\r\n" + 
			"이벤트의 자동 종료 옵션 사용 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 이벤트 자동 종료 사용 안함\r\n" + 
			" 1 : 이벤트 자동 종료 사용";
	
	// 27. ACTION_MODE
	public static final String ALARM_ACTION_MODE = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : ACTION_MODE\r\n" + 
			"\r\n" + 
			"이벤트 통보 방법 중\r\n" + 
			"Action( 제어 명령 )을 선택 하였을 경우\r\n" + 
			"\r\n" + 
			"이벤트 발생 시\r\n" + 
			"수행하게 될 Action( 제어 명령 )의 \r\n" + 
			"수행( 통보 ) 방법을 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 통보시 한번만 수행\r\n" + 
			" 1 : 통보시 매번 수행";
	
	// 28. EVENT_GUIDE
	public static final String ALARM_EVENT_GUIDE = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : EVENT_GUIDE\r\n" + 
			"\r\n" + 
			"이벤트의 장애 처리 지침 내용을 의미합니다";
	
	
	// 29. SUSPEND_LIMIT
	public static final String ALARM_SUSPEND_LIMIT = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : SUSPEND_LIMIT\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 30. SUSPEND_START_TIME
	public static final String ALARM_SUSPEND_START_TIME = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : SUSPEND_START_TIME\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 31. SUSPEND_END_TIME
	public static final String ALARM_SUSPEND_END_TIME = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : SUSPEND_END_TIME\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 32. SUSPEND_ALARM
	public static final String ALARM_SUSPEND_ALARM = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : SUSPEND_ALARM\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 33. nSaveEnable
	public static final String ALARM_nSaveEnable = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : nSaveEnable\r\n" + 
			"\r\n" + 
			"이벤트 저장 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 이벤트 저장 안함\r\n" + 
			" 1 : 이벤트 저장 함";
	
	// 34. CAMERA_PRESET2
	public static final String ALARM_CAMERA_PRESET2 = 
			"테이블 명 : ALARM\r\n" + 
			"컬럼 명 : CAMERA_PRESET2\r\n" + 
			"\r\n" + 
			"이벤트 카메라 연동 관련 필드입니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	
	
	
	/**
	 * EVENTS 테이블 정보
	 */
	// 01. nIndex
	public static final String EVENTS_nIndex = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : nIndex\r\n" + 
			"\r\n" + 
			"EVENTS 테이블의 기본키(primary key) 입니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 해당 컬럼은 null 값이나 중복 값을 가질 수 없으며\r\n" + 
			"\r\n" + 
			"이벤트 내역의 고유한 인덱스를 의미합니다";
	
	// 02. strEventName
	public static final String EVENTS_strEventName = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strEventName\r\n" + 
			"\r\n" + 
			"이벤트 이름을 의미합니다";
	
	// 03. strEventContent
	public static final String EVENTS_strEventContent = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strEventContent\r\n" + 
			"\r\n" + 
			"발생한 이벤트의\r\n" + 
			"\r\n" + 
			"이벤트 내용을 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"예) 현재값: 36.5℃ 현재 온도의 값이 30℃ 이상입니다.\r\n" + 
			"예) 현재값: 55.0℃ 현재 습도의 값이 50% 이상입니다.";
	
	// 04. strEventDate
	public static final String EVENTS_strEventDate = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strEventDate\r\n" + 
			"\r\n" + 
			"이벤트 발생 시각을 의미합니다";
	
	// 05. strSystemName
	public static final String EVENTS_strSystemName = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strSystemName\r\n" + 
			"\r\n" + 
			"기본 값 : Midknight\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 06. nSystemIndex
	public static final String EVENTS_nSystemIndex = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : nSystemIndex\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 07. nServerIndex
	public static final String EVENTS_nServerIndex = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : nServerIndex\r\n" + 
			"\r\n" + 
			"해당 이벤트가 등록 된 장비의 인덱스를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 해당 이벤트가 등록 된 장비 인덱스를 의미하는\r\n" + 
			"\r\n" + 
			"SERVERINFO 테이블의\r\n" + 
			"nServerIndex 컬럼과 동일한 내용을 공유합니다";
	
	
	// 08. strHostName
	public static final String EVENTS_strHostName = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strHostName\r\n" + 
			"\r\n" + 
			"해당 이벤트가 발생한 장비의\r\n" + 
			"\r\n" + 
			"장비명을 의미합니다";
	
	// 09. strHostIP
	public static final String EVENTS_strHostIP = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strHostIP\r\n" + 
			"\r\n" + 
			"이벤트가 발생한 장비에 등록 된 IP를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"만약 장비가 시리얼 포트 연결 방식과 같은\r\n" + 
			"\r\n" + 
			"RCU를 이용한 통신 중 이라면\r\n" + 
			"\r\n" + 
			"RCU에 등록 된 IP를 의미합니다";
	
	// 10. nSeverity
	public static final String EVENTS_nSeverity = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : nSeverity\r\n" + 
			"\r\n" + 
			"이벤트의 심각도를 의미합니다\r\n" + 
			"\r\n" + 
			" 10 : Normal\r\n" + 
			" 20 : Warning\r\n" + 
			" 30 : Minor\r\n" + 
			" 40 : Critical\r\n" + 
			" 50 : Fatal";
		
	// 11. strProcessUser
	public static final String EVENTS_strProcessUser = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strProcessUser\r\n" + 
			"\r\n" + 
			"발생하여 이벤트 내역 페이지에 등록 된\r\n" + 
			"\r\n" + 
			"이벤트의 상태를 종료로 변경 시\r\n" + 
			"\r\n" + 
			"이벤트를 종료 처리한 처리자를 의미합니다";
	
	// 12. strProcessDate
	public static final String EVENTS_strProcessDate = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strProcessDate\r\n" + 
			"\r\n" + 
			"발생하여 이벤트 내역 페이지에 등록 된\r\n" + 
			"\r\n" + 
			"이벤트의 상태를\r\n" + 
			"\r\n" + 
			"처리자가 인지 또는 종료로 변경 시\r\n" + 
			"\r\n" + 
			"처리자가 설정 한 이벤트의 인지 시각을 의미합니다";
	
	// 13. strProcessContent
	public static final String EVENTS_strProcessContent = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strProcessContent\r\n" + 
			"\r\n" + 
			"발생하여 이벤트 내역 페이지에 등록 된\r\n" + 
			"\r\n" + 
			"이벤트의 상태를\r\n" + 
			"\r\n" + 
			"처리자가 인지 또는 종료로 변경 시\r\n" + 
			"\r\n" + 
			"처리자가 입력 한 이벤트의 처리 내용을 의미합니다";
	
	// 14. strCompleteDate
	public static final String EVENTS_strCompleteDate = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strCompleteDate\r\n" + 
			"\r\n" + 
			"발생하여 이벤트 내역 페이지에 등록 된\r\n" + 
			"\r\n" + 
			"이벤트의 상태를\r\n" + 
			"\r\n" + 
			"처리자가 종료로 변경 시\r\n" + 
			"\r\n" + 
			"처리자가 설정 한 이벤트의 종료 시각을 의미합니다";
	
	// 15. nStatus
	public static final String EVENTS_nStatus = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : nStatus\r\n" + 
			"\r\n" + 
			"이벤트 내역에 등록 된\r\n" + 
			"\r\n" + 
			"이벤트의 상태를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 발생\r\n" + 
			" 1 : 인지\r\n" + 
			" 2 : 종료\r\n" + 
			" 3 : 자동 종료( 자동 복구 )";
	
	// 16. strSessionID
	public static final String EVENTS_strSessionID = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strSessionID\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";

	// 17. nAlarmIndex
	public static final String EVENTS_nAlarmIndex = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : nAlarmIndex\r\n" + 
			"\r\n" + 
			"이벤트 내역에 등록 된\r\n" + 
			"\r\n" + 
			"이벤트( 알람 )의 인덱스를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 이벤트 정보를 관리하는\r\n" + 
			"\r\n" + 
			"ALARM 테이블의\r\n" + 
			"nAlarmIndex 컬럼 내용을 의미합니다";
	
	// 18. nRepeatCount
	public static final String EVENTS_nRepeatCount = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : nRepeatCount\r\n" + 
			"\r\n" + 
			"해당 이벤트가 발생, 인지 상태에서 발생한 회수를 의미합니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"즉, 종료되지 않은 이벤트의 반복 발생 회수를 의미합니다\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 19. strLastOccurTime
	public static final String EVENTS_strLastOccurTime = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strLastOccurTime\r\n" + 
			"\r\n" + 
			"해당 이벤트가 마지막으로 발생한 시각을 의미합니다";
	
	// 20. AUTO_CLOSE
	public static final String EVENTS_AUTO_CLOSE = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : AUTO_CLOSE\r\n" + 
			"\r\n" + 
			"이벤트의 자동 종료 옵션 사용 여부를 의미합니다\r\n" + 
			"\r\n" + 
			" 0 : 이벤트 자동 종료 사용 안함\r\n" + 
			" 1 : 이벤트 자동 종료 사용";
	
	// 21. EVENT_PROCESSING_TYPE
	public static final String EVENTS_EVENT_PROCESSING_TYPE = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : EVENT_PROCESSING_TYPE\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 22. strConfirmedUserName
	public static final String EVENTS_strConfirmedUserName = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strConfirmedUserName\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
	
	// 23. strFaultCause
	public static final String EVENTS_strFaultCause = 
			"테이블 명 : EVENTS\r\n" + 
			"컬럼 명 : strFaultCause\r\n" + 
			"\r\n" + 
			"( 자세한 컬럼 사용 정보 없음 )";
		
	
	
	/**
	 * New Column
	 */
	// 01.
	// 02.
	// 03.
	// 04. 
	// 05. 
	// 06. 
	// 07. 
	// 08.
	// 09.
	// 10.
	// 11.
	// 12.
	// 13.
	// 14.
	// 15.
	// 16.
	// 17.
	// 18.
	// 19.
	// 20.
	// 21.
	// 22.
	// 23.
	// 24.
	// 25.
	// 26.
	// 27.
	// 28.
	// 29.
	// 30.	
	// 31.
	// 32.
	// 33.
	// 34. 
	// 35. 
	// 36. 
	// 37. 
	// 38. 
	// 39. 
	// 40. 	
}
