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
		// SERVERINFO ���̺�							
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
									
		// SERVERINFO_FACILITY ���̺�							
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
									
		// SERVERINFO_RTU ���̺�							
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
									
		// SERVER_PERF ���̺�							
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
									
		// ALARM ���̺�							
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
									
		// EVENTS ���̺�							
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
		
		// ������ �ۼ����� ���� �÷��� ���
		default : return String.format(
				"���̺� �� : %s\r\n" + 
				"�÷� �� : %s\r\n" + 
				"\r\n" + 
				"( Column Discription ������ �����ϴ� )\r\n" +
				"\r\n" + 
				"( Moon ���� �������ּ��� )"
				, column.getTableName(), column.getColumnName());				
		}// end switch
	}
	
	/**
	 * SERVERINFO ���̺�
	 */
	// 01. nServerIndex
	public static final String SERVERINFO_nServerIndex = 
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nServerIndex\r\n" + 
			"\r\n" + 
			"SERVERINFO ���̺��� �⺻Ű(primary key) �Դϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �ش� �÷��� null ���̳� �ߺ� ���� ���� �� ������\r\n" + 
			"����� ������ �ε����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"�̶� ���� �ü����� ������ \r\n" + 
			"����, RCU, ī�޶�, ����Ʈ�Ŵ��� ��\r\n" + 
			"MK119 AdminConsole���� ��� ������ ���� ����� �ǹ��մϴ�";
	
	// 02. strServerName
	public static final String SERVERINFO_strServerName = 
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : strServerName\r\n" + 
			"\r\n" + 
			"������ �ǹ��մϴ�";
	
	// 03. strHostName
	public static final String SERVERINFO_strHostName = 
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : strHostName\r\n" + 
			"\r\n" + 
			"��� ��ϵ� IP�� ȣ��Ʈ �̸��Դϴ�\r\n" + 
			"\r\n" + 
			"IP : 127.0.0.1\r\n" + 
			"Host Name : localhost";
		
	// 04. strServerIP
	public static final String SERVERINFO_strServerIP = 
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : strServerIP\r\n" + 
			"\r\n" + 
			"����� IP �ּ� �Դϴ�";
	
	// 05. nTotalProcess
	public static final String SERVERINFO_nTotalProcess =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nTotalProcess\r\n" + 
			"\r\n" + 
			"��ü ���μ��� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
		
	// 06. nServerAlive
	public static final String SERVERINFO_nServerAlive =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nServerAlive\r\n" + 
			"\r\n" + 
			"����� ���� ���¸� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"-2 : ������Ʈ ���� ����\r\n" + 
			"-1 : �̻�\r\n" + 
			" 0 : ������\r\n" + 
			" 1 : ������";
	
	// 07. nServerStatus
	public static final String SERVERINFO_nServerStatus =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nServerStatus\r\n" + 
			"\r\n" + 
			"���� ���¸� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	// 08. nAgentType
	public static final String SERVERINFO_nAgentType =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nAgentType\r\n" + 
			"\r\n" + 
			"midknight ������ Agent ���� ������ ���� ����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 1 : ����\r\n" + 
			" 2 : ��Ʈ��ũ(SNMP)\r\n" + 
			" 4 : Oracle DB\r\n" + 
			" 8 : RTU\r\n" + 
			" 16 : �ü���\r\n" + 
			" 64 : ������\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 09. strServerDescription
	public static final String SERVERINFO_strServerDescription =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : strServerDescription\r\n" + 
			"\r\n" + 
			"���� ���� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	// 10. strMemo
	public static final String SERVERINFO_strMemo =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : strMemo\r\n" + 
			"\r\n" + 
			"��� ��� �� \"����\" ���� �Է� �� �����̸�\r\n" + 
			"\r\n" + 
			"��� ��� ������ \"����\" ���� ����� �����մϴ�";
	
	
	// 11. nPerfCheckInterval
	public static final String SERVERINFO_nPerfCheckInterval =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nPerfCheckInterval\r\n" + 
			"\r\n" + 
			"���� ����͸� �˻� ������ �ǹ��մϴ� ( ���� : �� )\r\n" + 
			"\r\n" + 
			"( ������ ���� �ֱ�� �ٸ� �����Դϴ� )";
	
	
	// 12. nLogCheckInterval
	public static final String SERVERINFO_nLogCheckInterval =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nLogCheckInterval\r\n" + 
			"\r\n" + 
			"�α� �˻� ������ �ǹ��մϴ� ( ���� : �� )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"������ �ð����� ������ �α׸� �˻��Ͽ�\r\n" + 
			"\r\n" + 
			"_LOG �� _LOG_ ���̺� �����͸� �߰��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	// 13. nProcessCheckInterval
	public static final String SERVERINFO_nProcessCheckInterval =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nProcessCheckInterval\r\n" + 
			"\r\n" + 
			"���μ��� ���� �˻� ������ �ǹ��մϴ� ( ���� : �� )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"������ �ð����� ���μ����� �� ������ �˻��Ͽ�\r\n" + 
			"\r\n" + 
			"������ _PROCESS ���̺��� �����մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	// 14. nDaemonCheckInterval
	public static final String SERVERINFO_nDaemonCheckInterval =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nDaemonCheckInterval\r\n" + 
			"\r\n" + 
			"���� ���μ��� �˻� ������ �ǹ��մϴ� ( ���� : �� )\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	// 15. nDiskCheckInterval
	public static final String SERVERINFO_nDiskCheckInterval =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nDiskCheckInterval\r\n" + 
			"\r\n" + 
			"��ũ ��뷮 �˻� ������ �ǹ��մϴ� ( ���� : �� )\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	// 16. nPerfCheckEnable
	public static final String SERVERINFO_nPerfCheckEnable =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nPerfCheckEnable\r\n" + 
			"\r\n" + 
			"���� ����͸� �˻� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : �˻� ����\r\n" + 
			" 1 : �˻�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	
	// 17. nLogCheckEnable
	public static final String SERVERINFO_nLogCheckEnable =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nLogCheckEnable\r\n" + 
			"\r\n" + 
			"�α� �˻� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : �˻� ����\r\n" + 
			" 1 : �˻�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	// 18. nProcessCheckEnable
	public static final String SERVERINFO_nProcessCheckEnable =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nProcessCheckEnable\r\n" + 
			"\r\n" + 
			"���μ��� �˻� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : �˻� ����\r\n" + 
			" 1 : �˻�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	// 19. nDaemonCheckEnable
	public static final String SERVERINFO_nDaemonCheckEnable =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nDaemonCheckEnable\r\n" + 
			"\r\n" + 
			"���� ���μ��� �˻� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : �˻� ����\r\n" + 
			" 1 : �˻�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	// 20. nDaemonControlCheckEnable
	public static final String SERVERINFO_nDaemonControlCheckEnable =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nDaemonControlCheckEnable\r\n" + 
			"\r\n" + 
			"���� ���� ��� ��� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ���� ���� �Ұ���\r\n" + 
			" 1 : ���� ���� ����\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	// 21. nControlCheckEnable
	public static final String SERVERINFO_nControlCheckEnable =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nControlCheckEnable\r\n" + 
			"\r\n" + 
			"���� ��� ���� ���� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ���� ��� ���� ����\r\n" + 
			" 1 : ���� ��� ���� ����\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 22. nDiskCheckEnable
	public static final String SERVERINFO_nDiskCheckEnable =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nDiskCheckEnable\r\n" + 
			"\r\n" + 
			"��ũ ��뷮 �˻� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : �˻� ����\r\n" + 
			" 1 : �˻�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 23. nEnable
	public static final String SERVERINFO_nEnable =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : nEnable\r\n" + 
			"\r\n" + 
			"����� ��� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ����\r\n" + 
			" 1 : ���";
	
	
	// 24. strOSName
	public static final String SERVERINFO_strOSName =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : strOSName\r\n" + 
			"\r\n" + 
			"� ü�� �̸��� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	// 25. strVersion
	public static final String SERVERINFO_strVersion =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : strVersion\r\n" + 
			"\r\n" + 
			"� ü�� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	// 26. strProcessorName
	public static final String SERVERINFO_strProcessorName =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : strProcessorName\r\n" + 
			"\r\n" + 
			"���μ��� �̸��� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( Alpha, x86, ... )\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 27. strAgentVersion
	public static final String SERVERINFO_strAgentVersion =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : strAgentVersion\r\n" + 
			"\r\n" + 
			"midKnight Agent ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 28. strServerKind
	public static final String SERVERINFO_strServerKind =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : strServerKind\r\n" + 
			"\r\n" + 
			"�ý��� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( ���� ����� ����, ��Ʈ��ũ, �������� ��쿡�� ����մϴ� )\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 29. strGetCommunity
	public static final String SERVERINFO_strGetCommunity =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : strGetCommunity\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 30. strSetCommunity
	public static final String SERVERINFO_strSetCommunity =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : strSetCommunity\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 31. USE_SNMP
	public static final String SERVERINFO_USE_SNMP =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : USE_SNMP\r\n" + 
			"\r\n" + 
			"SNMP ���� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ������\r\n" + 
			" 1 : ����\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 32. SNMP_VERSION
	public static final String SERVERINFO_SNMP_VERSION =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : SNMP_VERSION\r\n" + 
			"\r\n" + 
			"SNMP ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 33. CHECK_ALIVE_PING
	public static final String SERVERINFO_CHECK_ALIVE_PING =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : CHECK_ALIVE_PING\r\n" + 
			"\r\n" + 
			"Ping ���� ���� �˻� �ɼ��� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : Ping ���� ���� �˻� ����\r\n" + 
			" 1 : Ping ���� ���� �˻� ���\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 34. CHECK_ALIVE_SNMP
	public static final String SERVERINFO_CHECK_ALIVE_SNMP =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : CHECK_ALIVE_SNMP\r\n" + 
			"\r\n" + 
			"SNMP ���� ���� �˻� �ɼ��� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : SNMP ���� ���� �˻� ����\r\n" + 
			" 1 : SNMP ���� ���� �˻� ���\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 35. SNMP_COLLECT_METHOD
	public static final String SERVERINFO_SNMP_COLLECT_METHOD =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : SNMP_COLLECT_METHOD\r\n" + 
			"\r\n" + 
			"SNMP ���� ����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : async (�񵿱��)\r\n" + 
			" 1 : sync (�����)\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	// 36. NW_VENDOR
	public static final String SERVERINFO_NW_VENDOR =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : NW_VENDOR\r\n" + 
			"\r\n" + 
			"��Ʈ��ũ ��� ������ �̸��� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 37. NW_PRODUCT
	public static final String SERVERINFO_NW_PRODUCT =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : NW_PRODUCT\r\n" + 
			"\r\n" + 
			"��Ʈ��ũ ��� ��ǰ �̸��� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 38. SERVER_CONDITION
	public static final String SERVERINFO_SERVER_CONDITION =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : SERVER_CONDITION\r\n" + 
			"\r\n" + 
			"����� ���� ���¸� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ���� ��\r\n" + 
			" 1 : ���� ��\r\n" + 
			" 2 : ���� ��\r\n" + 
			" 3 : ���� ����\r\n" + 
			" 4 : ��� ��\r\n" + 
			" 5 : ��� ����\r\n" + 
			" 6 : ���� ����\r\n" + 
			" 7 : ���� ����\r\n" + 
			" 8 : Unknown\r\n" + 
			" 9 : Ping ����";
	
	// 39. SERVER_CONDITION2
	public static final String SERVERINFO_SERVER_CONDITION2 =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : SERVER_CONDITION2\r\n" + 
			"\r\n" + 
			"SERVER_CONDITION �÷��� ������ �������� ����\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 40. IDC_INDEX
	public static final String SERVERINFO_IDC_INDEX =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : IDC_INDEX\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 41. AUX_SERVER_IP
	public static final String SERVERINFO_AUX_SERVER_IP =
			"���̺� �� : SERVERINFO\r\n" + 
			"�÷� �� : AUX_SERVER_IP\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	
	/**
	 * SERVERINFO_FACILITY ���̺�
	 */
	// 01. NODE_INDEX
	public static final String SERVERINFO_FACILITY_NODE_INDEX = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : NODE_INDEX\r\n" + 
			"\r\n" + 
			"SERVERINFO_FACILITY ���̺��� �⺻Ű(primary key) �Դϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �ش� �÷��� null ���̳� �ߺ� ���� ���� �� ������\r\n" + 
			"�ü����� ������ �ε����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"���� �ش� �÷��� SERVERINFO ���̺��� \r\n" + 
			"nServerIndex �÷��� ������ ������ �����մϴ�\r\n" + 
			"\r\n" + 
			"��, SERVERINFO ���̺���\r\n" + 
			"nServerIndex = 18�� ����(���)�� �ü��� ������\r\n" + 
			"\r\n" + 
			"SERVERINFO_FACILITY ���̺���\r\n" + 
			"NODE_INDEX = 18�� ��(���ڵ�) �Դϴ�";
	
	
	// 02. FACILITY_TYPE
	public static final String SERVERINFO_FACILITY_FACILITY_TYPE = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : FACILITY_TYPE\r\n" + 
			"\r\n" + 
			"�ü��� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 1 : UPS\r\n" + 
			" 2 : CRAC ( �׿��׽��� )\r\n" + 
			" 3 : �Ϸм�ȭ��\r\n" + 
			" 4 : ������\r\n" + 
			" 5 : �ι���\r\n" + 
			" 6 : AVC\r\n" + 
			" 7 : ������\r\n" + 
			" 8 : ����������\r\n" + 
			" 9 : ī�޶� ( ���� ��� ���� )\r\n" + 
			" 10 : VESDA\r\n" + 
			" 11 : STS\r\n" + 
			" 12 : ������\r\n" + 
			" 13 : BMS\r\n" + 
			" 14 : �½�����\r\n" + 
			" 15 : ȭ�� ���ű�\r\n" + 
			" 16 : ���� Ž����\r\n" + 
			" 17 : ī�޶� ��Ʈ�ѷ�\r\n" + 
			" 18 : ��\r\n" + 
			" 19 : ������ ����\r\n" + 
			" 20 : ���� �νı�\r\n" + 
			" 21 : ������\r\n" + 
			" 22 : ǳ����\r\n" + 
			" 23 : ������\r\n" + 
			" 24 : ���� ������ġ\r\n" + 
			" 25 : ǳ�Ӱ�\r\n" + 
			" 26 : PDU\r\n" + 
			" 27 : ���� ����\r\n" + 
			" 28 : �õ���\r\n" + 
			" 29 : XD (���� ��)\r\n" + 
			" 98 : AI-Net ���� ����\r\n" + 
			" 99 : ������\r\n" + 
			" 102 : Access Floor\r\n" + 
			" 200 : IBS ����";
	
	
	// 03. CONN_METHOD
	public static final String SERVERINFO_FACILITY_CONN_METHOD = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : CONN_METHOD\r\n" + 
			"\r\n" + 
			"�ü��� ����� ���� ����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ü��� �������� �����ϴ� ���� ����� �����մϴ� )\r\n" + 
			"\r\n" + 
			" 1 : ���� ����\r\n" + 
			" 2 : �ø��� ��Ʈ ����\r\n" + 
			" 4 : SNMP ����\r\n" + 
			" 8 : PSTN ( ���� ��� ���� )\r\n" + 
			" 16 : TCP/IP ����\r\n" + 
			" 32 : ZigBee ����\r\n" + 
			" 64 : UDP/IP ����\r\n" + 
			" 128 : BACnet ����\r\n" + 
			" 256 :  File ����\r\n" + 
			" 512 : PSM ����\r\n" + 
			" 1024 : DB ����\r\n" + 
			" 2048  : Modbus ����\r\n" + 
			" 4096 : iLon ����\r\n" + 
			" 8192 : LNS DDE ����\r\n" + 
			" 32768 : PLC ����\r\n" + 
			" 12288 : ���� ����\r\n" + 
			" 65536 : IPMI ����\r\n" + 
			" 131072 : SNMP(MANAGER) ����\r\n" + 
			" 196608 : MUX ����\r\n" + 
			" 262144 : UDP RECV ����\r\n" + 
			" 327680 : UDP/IP ����(Bindless)\r\n" + 
			" 393218 : Midas ����\r\n" + 
			" 458752 : Rackguard ����\r\n" + 
			" 524288 : BACnet REST Agent ����\r\n" + 
			" 589824 : REST API ����";
	
	
	// 04. RTU_INDEX
	public static final String SERVERINFO_FACILITY_RTU_INDEX = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : RTU_INDEX\r\n" + 
			"\r\n" + 
			"�ش� �ü����� RCU�� ���Ͽ� ����ϴ� ���\r\n" + 
			"( �ø��� ��Ʈ, PLC ���� ��� �� )\r\n" + 
			"\r\n" + 
			"RCU ����� �ε����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, SERVERINFO ���̺� ���� ��\r\n" + 
			"RCU ����� nServerIndex �÷� ���Դϴ�\r\n" + 
			"\r\n" + 
			"( RCU�� ���Ͽ� ������� �ʴ´ٸ� �⺻ ������ 0�� �����ϴ� )";
	
	
	// 05. RTU_PORT_NUM
	public static final String SERVERINFO_FACILITY_RTU_PORT_NUM = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : RTU_PORT_NUM\r\n" + 
			"\r\n" + 
			"�ش� �ü����� ��ϵ� ��Ʈ ��ȣ�� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���� �ش� �ü����� RCU�� ���Ͽ� ����ϴ� ��쿡��\r\n" + 
			"RCU ����� ��Ʈ ��ȣ�� �ǹ��մϴ�\r\n" + 
			"( �ø��� ��Ʈ, PLC ���� ��� �� )\r\n" + 
			"\r\n" + 
			"�̴� 1470 ���� �������� RCU�� ��Ʈ ��ȣ�� �ƴ�\r\n" + 
			"�ø��� ��Ʈ ���� ������� �ü��� ��� ��� ��\r\n" + 
			"��� �� RCU ���� ��,\r\n" + 
			"�ٷ� �������� �����ϰ� �Ǵ� '��� ��Ʈ' ���� �� �Դϴ�\r\n" + 
			"\r\n" + 
			"��, ���� �� RCU�� �������� ��Ʈ�� ���� �� ���\r\n" + 
			"�����ϴ� ��Ʈ �� �� ��° ��Ʈ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"��κ��� RCU��\r\n" + 
			"�ϳ��� ��Ʈ�� ����ϱ� ������\r\n" + 
			"�ش� �÷� ������ 1 �� ���������\r\n" + 
			"\r\n" + 
			"RCU ���� ��\r\n" + 
			"TCP/IP Multiport RCU ���� ���\r\n" + 
			"RCU �ϳ��� 16���� ��Ʈ�� �����ϱ� ������\r\n" + 
			"\r\n" + 
			"�÷� �� Ȯ�� �� �������ֽñ� �ٶ��ϴ�";
	
	
	// 06. SNMP_MIB
	public static final String SERVERINFO_FACILITY_SNMP_MIB = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : SNMP_MIB\r\n" + 
			"\r\n" + 
			"�ش� �ü����� SNMP ���� ������� ��� �� ���\r\n" + 
			"\r\n" + 
			"��� �� SNMP ���������� ��ȣ�� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ü��� �������� SNMP �������� ��ȣ�� �����մϴ� )";
	
	
	// 07. COMM_PROTOCOL
	public static final String SERVERINFO_FACILITY_COMM_PROTOCOL = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : COMM_PROTOCOL\r\n" + 
			"\r\n" + 
			"�ش� �ü����� ��� �� �������� ��ȣ�� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"�������� ���� �����Ͽ���\r\n" + 
			"�ü��� �������� �������� ��ȣ�� �ٸ� �� �ֽ��ϴ�\r\n" + 
			"\r\n" + 
			"( UPS - PLC ���� �������� ��ȣ : 58 )\r\n" + 
			"( CRAC - PLC ���� �������� ��ȣ : 69 )\r\n" + 
			"( ������ - PLC ���� �������� ��ȣ : 42 )";
	
	// 08. PROTOCOL_DATA
	public static final String SERVERINFO_FACILITY_PROTOCOL_DATA = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : PROTOCOL_DATA\r\n" + 
			"\r\n" + 
			"�ش� �ü����� ���� ���� ����ϱ� ���Ͽ�\r\n" + 
			"�ʿ��� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"�ش� �÷��� �������� �����ڰ� �������� ���� �۾� ��\r\n" + 
			"��ſ� �ʿ��� ������ �ִٰ� �Ǵ� �� �� �����ϸ�\r\n" + 
			"\r\n" + 
			"Modbus ���������� ���� ���\r\n" + 
			"��û ��Ŷ(TX) ���뿡 \r\n" + 
			"�� �� ��� ���� ��û���� ��� ��ȣ ������ �ʿ��մϴ�\r\n" + 
			"\r\n" + 
			"�� �� ��� ��ȣ�� ���� ������\r\n" + 
			"����ϴ� ������� ������ ���� ���� �� �� �ֱ� ������\r\n" + 
			"��� ��Ͻ� Unit ID ��� ������ �Է¹ް� �˴ϴ�\r\n" + 
			"\r\n" + 
			"�̷��� ����ڷκ��� Unit ID ���� �Է¹��� ����\r\n" + 
			"SERVERINFO_FACILITY ���̺��� \r\n" + 
			"PROTOCOL_DATA �÷��� �������� ����˴ϴ�\r\n" + 
			"\r\n" + 
			"( �Է� ���� Unit ID ��� ���ڿ��� �����ڰ� ������ ���̸�\r\n" + 
			"�������� �Ǵܿ� ���� �ٸ� ���ڿ��� �� ���� �ֽ��ϴ� )";
	
	
	// 09. packetLogEnable
	public static final String SERVERINFO_FACILITY_packetLogEnable = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : packetLogEnable\r\n" + 
			"\r\n" + 
			"�ش� �ü����� ��Ŷ �α� ��� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ��Ŷ �α� ��� ����\r\n" + 
			" 1 : ��Ŷ �α� ���";
	
	// 10. packetLogFileName
	public static final String SERVERINFO_FACILITY_packetLogFileName = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : packetLogFileName\r\n" + 
			"\r\n" + 
			"�ش� �ü����� ��Ŷ �α� �̸��� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"�÷� ���뿡 ��Ŷ �α� ������ ����� ��θ� �����ϸ�\r\n" + 
			"\r\n" + 
			"�ش� �ü����� ��� �������� Ȯ�� �� �� �ֽ��ϴ�";
	
	// 11. packetLogMaxFileSize
	public static final String SERVERINFO_FACILITY_packetLogMaxFileSize = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : packetLogMaxFileSize\r\n" + 
			"\r\n" + 
			"�ش� �ü����� ��Ŷ�α� ���� 1���� \r\n" + 
			"\r\n" + 
			"�ִ� ũ��( ���� : MB )�� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"�ü����� ��� �������� Ȯ�� �� �� �ֽ��ϴ�";
	
	// 12. packetLogMaxBackupIndex
	public static final String SERVERINFO_FACILITY_packetLogMaxBackupIndex = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : packetLogMaxBackupIndex\r\n" + 
			"\r\n" + 
			"�ش� �ü����� ��Ŷ �α� ���� ���� ���� ���� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"�ü����� ������ ��Ŷ �α��� �ִ� ũ�⸸ŭ\r\n" + 
			"\r\n" + 
			"��Ŷ ������ ��ϵǸ� ���ο� ��Ŷ �α� ������ �����ϴµ�\r\n" + 
			"\r\n" + 
			"�� �� ���� �� �� �ִ� �ִ� ��Ŷ �α� ������ ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"�ü����� ��� �������� Ȯ�� �� �� �ֽ��ϴ�";
	
	// 13. AUX_PORT_NUM
	public static final String SERVERINFO_FACILITY_AUX_PORT_NUM = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : AUX_PORT_NUM\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 14. PHASE_TYPE
	public static final String SERVERINFO_FACILITY_PHASE_TYPE = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : PHASE_TYPE\r\n" + 
			"\r\n" + 
			"�ü��� ���� �� ��� ��\r\n" + 
			"\r\n" + 
			"���� ������� PSM ���� ����� �����ϸ�\r\n" + 
			"\r\n" + 
			"���� �� �� �ִ� �б� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" single : �ܻ�\r\n" + 
			" three : 3 ��\r\n" + 
			" combi : ȥ��";
	
	// 15. deviceLocation
	public static final String SERVERINFO_FACILITY_deviceLocation = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : deviceLocation\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 16. offeredITSM
	public static final String SERVERINFO_FACILITY_offeredITSM = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : offeredITSM\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 17. operationDepartment
	public static final String SERVERINFO_FACILITY_operationDepartment = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : operationDepartment\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 18. serviceName
	public static final String SERVERINFO_FACILITY_serviceName = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : serviceName\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 19. RESPONSE_TIMEOUT
	public static final String SERVERINFO_FACILITY_RESPONSE_TIMEOUT = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : RESPONSE_TIMEOUT\r\n" + 
			"\r\n" + 
			"�ش� �ü����� ���� Ÿ�Ӿƿ��� �ǹ��մϴ� \r\n" + 
			"\r\n" + 
			"�⺻ �� : 4000 ( ���� : millisecond )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"�ø��� ��Ʈ ���� �������\r\n" + 
			"�ϳ��� RCU �Ʒ��� ���� ��� ���� ����ϴ� ���\r\n" + 
			"\r\n" + 
			"�� �� �ϳ��� ��񿡼� ������ ���� �ʰų�,\r\n" + 
			"������ �ʰ� �ִ� ���\r\n" + 
			"\r\n" + 
			"�ش� ���� ���� �� Ÿ�Ӿƿ� �ð����� ������ ����մϴ�\r\n" + 
			"\r\n" + 
			"�� �� ������ RCU�� �����ִ� �ٸ� ������\r\n" + 
			"���� Ÿ�Ӿƿ� �ð����� ����ϰ� �Ǹ�\r\n" + 
			"���� ���� �ֱ⿡ ������ �ݴϴ�\r\n" + 
			"\r\n" + 
			"�̷��� ��� ������ ���� ����� Ÿ�Ӿƿ��� ª�� �����Ѵٸ�\r\n" + 
			"Ÿ�Ӿƿ� ���� ���� �ʴ� ���信 ���ؼ��� �н��ϰ�\r\n" + 
			"\r\n" + 
			"���� ����� ��û�� �����Ͽ�\r\n" + 
			"\r\n" + 
			"���� ���� �ֱⰡ �и��� ������ �ؼ� �� �� �ֽ��ϴ�";
	
	
	// 20. USE_SO_TIMEOUT
	public static final String SERVERINFO_FACILITY_USE_SO_TIMEOUT = 
			"���̺� �� : SERVERINFO_FACILITY\r\n" + 
			"�÷� �� : USE_SO_TIMEOUT\r\n" + 
			"\r\n" + 
			"�ü����� ���� Ÿ�Ӿƿ� ��� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ���� Ÿ�Ӿƿ� ��� ����\r\n" + 
			" 1 : ���� Ÿ�Ӿƿ� ���\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"TCP/IP ���� ������� ��� �� ��񿡼� ���� �� �� ������\r\n" + 
			"\r\n" + 
			"������ ���������� ��� ������ �����Ǵ� ����\r\n" + 
			"�ش� �ɼ� �������� ��� ���� �̺�Ʈ �߻� ���� �����˴ϴ�\r\n" + 
			"\r\n" + 
			"( �ü����� ���� Ÿ�Ӿƿ� ������� �ٸ� �ɼ��Դϴ� )\r\n" + 
			"( SERVERINFO_FACILITY ���̺��� \r\n" + 
			" RESPONSE_TIMEOUT �÷����� �ٸ� ���� )";
	
	
	
	/**
	 * SERVERINFO_RTU ���̺�
	 */
	// 01. NODE_INDEX
	public static final String SERVERINFO_RTU_NODE_INDEX = 
			"���̺� �� : SERVERINFO_RTU\r\n" + 
			"�÷� �� : NODE_INDEX\r\n" + 
			"\r\n" + 
			"SERVERINFO_RTU ���̺��� �⺻Ű(primary key) �Դϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �ش� �÷��� null ���̳� �ߺ� ���� ���� �� ������\r\n" + 
			"RCU�� ������ �ε����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"���� �ش� �÷��� SERVERINFO ���̺��� \r\n" + 
			"nServerIndex �÷��� ������ ������ �����մϴ�\r\n" + 
			"\r\n" + 
			"��, SERVERINFO ���̺���\r\n" + 
			"nServerIndex = 18�� ����(RCU)�� RCU ���� ������\r\n" + 
			"\r\n" + 
			"SERVERINFO_RTU ���̺���\r\n" + 
			"NODE_INDEX = 18�� ��(���ڵ�) �Դϴ�";
	
	// 02. RTU_TYPE
	public static final String SERVERINFO_RTU_RTU_TYPE = 
			"���̺� �� : SERVERINFO_RTU\r\n" + 
			"�÷� �� : RTU_TYPE\r\n" + 
			"\r\n" + 
			"RCU ������ �ǹ��մϴ�\r\n" + 
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
			" 20 : TCP/IP ����ȭ RCU";
	
	
	// 03. NUM_AI_CHANNEL
	public static final String SERVERINFO_RTU_NUM_AI_CHANNEL = 
			"���̺� �� : SERVERINFO_RTU\r\n" + 
			"�÷� �� : NUM_AI_CHANNEL\r\n" + 
			"\r\n" + 
			"�Ƴ��α� �Է� ä�� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 04. NUM_DI_CHANNEL
	public static final String SERVERINFO_RTU_NUM_DI_CHANNEL = 
			"���̺� �� : SERVERINFO_RTU\r\n" + 
			"�÷� �� : NUM_DI_CHANNEL\r\n" + 
			"\r\n" + 
			"������ �Է� ä�� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 05. NUM_AO_CHANNEL
	public static final String SERVERINFO_RTU_NUM_AO_CHANNEL = 
			"���̺� �� : SERVERINFO_RTU\r\n" + 
			"�÷� �� : NUM_AO_CHANNEL\r\n" + 
			"\r\n" + 
			"�Ƴ��α� ��� ä�� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 06. NUM_DO_CHANNEL
	public static final String SERVERINFO_RTU_NUM_DO_CHANNEL = 
			"���̺� �� : SERVERINFO_RTU\r\n" + 
			"�÷� �� : NUM_DO_CHANNEL\r\n" + 
			"\r\n" + 
			"������ ��� ä�� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 07. NUM_SERIAL_PORT
	public static final String SERVERINFO_RTU_NUM_SERIAL_PORT = 
			"���̺� �� : SERVERINFO_RTU\r\n" + 
			"�÷� �� : NUM_SERIAL_PORT\r\n" + 
			"\r\n" + 
			"RCU ���� �����ϴ� ��Ʈ ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �Ϲ����� RCU�� ��� 1���� ��Ʈ�� ����������\r\n" + 
			"\r\n" + 
			" TCP/IP Multiport RCU�� ��� �ִ� 16���� ��Ʈ�� �����մϴ� )";
	
	
	// 08. USE_PSTN
	public static final String SERVERINFO_RTU_USE_PSTN = 
			"���̺� �� : SERVERINFO_RTU\r\n" + 
			"�÷� �� : USE_PSTN\r\n" + 
			"\r\n" + 
			"PSTN ���� ��� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : PSTN ���� ��� ����\r\n" + 
			" 1 : PSTN ���� ���\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"( PSTN : Public Switched Telephone Network )\r\n" + 
			"\r\n" + 
			"PSTN�̶� ������ ��ġ �� ��ȭ ��Ÿ��� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 09. PSTN_COM_PORT
	public static final String SERVERINFO_RTU_PSTN_COM_PORT = 
			"���̺� �� : SERVERINFO_RTU\r\n" + 
			"�÷� �� : PSTN_COM_PORT\r\n" + 
			"\r\n" + 
			"PSTN ���� �� ��� �� �ø��� ��Ʈ ��ȣ�� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 10. PSTN_PHONE_NUMBER
	public static final String SERVERINFO_RTU_PSTN_PHONE_NUMBER = 
			"���̺� �� : SERVERINFO_RTU\r\n" + 
			"�÷� �� : PSTN_PHONE_NUMBER\r\n" + 
			"\r\n" + 
			"PSTN ���� ��ȭ ��ȣ�� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 11. PASSIVE_TCP_SERVER_PORT
	public static final String SERVERINFO_RTU_PASSIVE_TCP_SERVER_PORT = 
			"���̺� �� : SERVERINFO_RTU\r\n" + 
			"�÷� �� : PASSIVE_TCP_SERVER_PORT\r\n" + 
			"\r\n" + 
			"�ش� RCU�� ��Ʈ ��ȣ�� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"TCP/IP RCU�� ��� �ַ� 1470�� �� ����մϴ�\r\n" + 
			"\r\n" + 
			"( TCP/IP Multiport RCU�� ���\r\n" + 
			" �ִ� 16���� ��Ʈ�� �����ϱ� ������ ������ ��Ʈ ������ �����ϸ�\r\n" + 
			"\r\n" + 
			" SERVERINFO_RTU ���̺��� \r\n" + 
			" PASSIVE_TCP_SERVER_POR �÷����� 0 ���� �����ϰ�\r\n" + 
			"\r\n" + 
			" SERVERINFO_RTU_MULTIPORT ���̺���\r\n" + 
			" 16�� ��Ʈ�� ���Ͽ� ��Ʈ ��ȣ ������ �����մϴ� )";
	
	
	// 12. rcu_id
	public static final String SERVERINFO_RTU_rcu_id = 
			"���̺� �� : SERVERINFO_RTU\r\n" + 
			"�÷� �� : rcu_id\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 13. AUX_TCP_PORT
	public static final String SERVERINFO_RTU_AUX_TCP_PORT = 
			"���̺� �� : SERVERINFO_RTU\r\n" + 
			"�÷� �� : AUX_TCP_PORT\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	/**
	 * SERVER_PERF ���̺�
	 */
	// 01. nPerfIndex
	public static final String SERVER_PERF_nPerfIndex = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : nPerfIndex\r\n" + 
			"\r\n" + 
			"SERVER_PERF ���̺��� �⺻Ű(primary key) �Դϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �ش� �÷��� null ���̳� �ߺ� ���� ���� �� ������\r\n" + 
			"\r\n" + 
			"������ ������ �ε����� �ǹ��մϴ�";
	
	
	// 02. nServerIndex
	public static final String SERVER_PERF_nServerIndex = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : nServerIndex\r\n" + 
			"\r\n" + 
			"�ش� ������ ������ ����� �ε����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �ش� ������ �������ִ� ��� �ε����� �ǹ��ϴ�\r\n" + 
			"\r\n" + 
			"SERVERINFO ���̺���\r\n" + 
			"nServerIndex �÷��� ������ ������ �����մϴ�";
	
	// 03. nPerfType
	public static final String SERVER_PERF_nPerfType = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : nPerfType\r\n" + 
			"\r\n" + 
			"���� �˻� ����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 1 : ������Ʈ\r\n" + 
			" 2 : SNMP\r\n" + 
			" 3 : ��Ʈ ����\r\n" + 
			" 4 : ����Ŭ DB\r\n" + 
			" 5 : RCU ����\r\n" + 
			" 6 : RCU �ø��� ����\r\n" + 
			" 8 : TCP �ø��� ����\r\n" + 
			" 9 : ZigBee �ڵ������ ����\r\n" + 
			" 10 : UDP ����\r\n" + 
			" 11 : BACnet ����\r\n" + 
			" 12 : File ����\r\n" + 
			" 13 : PSM ����\r\n" + 
			" 14 : DB ����\r\n" + 
			" 15 : Modbus ����\r\n" + 
			" 16 : iLON ����\r\n" + 
			" 17 : LNS DDE ����\r\n" + 
			" 18 : PLC ����\r\n" + 
			" 19 : ���󼺴�\r\n" + 
			" 20 : IPMI ����\r\n" + 
			" 22 : ����(����)\r\n" + 
			" 23 : ����(�����·�)\r\n" + 
			" 24 : ����(�����·�) \r\n" + 
			" 25 : ����(SQL)\r\n" + 
			" 26 : ����Ʈ����\r\n" + 
			" 27 : MUX ����\r\n" + 
			" 28 : UDP RECV ����\r\n" + 
			" 29 : REST\r\n" + 
			" 30 : MidasCon\r\n" + 
			" 31 : MidasAp\r\n" + 
			" 32 : ����(���� ī����) \r\n" + 
			" 33 : ������ ����\r\n" + 
			" 34 : �ϰ����ð�\r\n" + 
			" 35 : ����(�ʱ�ȭ)\r\n" + 
			" 36 : ����(�����ð�)\r\n" + 
			" 37 : REST API\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 04. strDisplayName
	public static final String SERVER_PERF_strDisplayName = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : strDisplayName\r\n" + 
			"\r\n" + 
			"���ɸ��� �ǹ��մϴ�";
	
	// 05. strPerfCounter
	public static final String SERVER_PERF_strPerfCounter = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : strPerfCounter\r\n" + 
			"\r\n" + 
			"������ ī����(PerfCounter)�� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"ī���ʹ� �������� �����ڰ� �����ϸ�\r\n" + 
			"��û ��Ŷ�� �����ϱ� ���� ������ �ַ� ���˴ϴ�\r\n" + 
			"\r\n" + 
			"��, ī���ʹ� �ϳ��� ��û�� �ĺ� �� �� �ִ� �����Դϴ�\r\n" +
			"\r\n" +
			"��񸶴� ��� ���, ��Ŷ ������ �����ϱ� ������\r\n" + 
			"�������ݸ��� ������ ī���Ͱ� ���� �� �� �ֽ��ϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��������������������������������������������������\r\n" + 
			"\r\n" + 
			"strPerfCounter �÷��� ��� \r\n" + 
			"\r\n" + 
			"�߿��� �����̱� ������ �ڼ��ϰ� �����ϰڽ��ϴ�\r\n" + 
			"\r\n" + 
			"������ ī���͸� ���� ���ɵ��� \r\n" + 
			"������ �ϳ��� ��û�� ���Ͽ� ������ �޾� ó���˴ϴ�\r\n" + 
			"\r\n" + 
			"���� ���� ������ ������ ī���͸� �������־\r\n" + 
			"��� ������ �����̶��� �� �� �����ϴ�\r\n" + 
			"\r\n" + 
			"���� ���κ��� ���� ���� �����͸�\r\n" + 
			"���� ī���� �������� �ٴ� ���� ( {?} )���� �����ϰų�\r\n" + 
			"��Ʈ ������ ���Ͽ� Bit ON/OFF ���·� �����Ͽ�\r\n" + 
			"\r\n" + 
			"���� �ٸ� ���� ������ ó�� �� �� �ֱ� �����Դϴ�\r\n" + 
			"\r\n" + 
			"A : 3_0_10\\{1}\r\n" + 
			"B : 3_0_10\\{2}\r\n" + 
			"C : 3_0_10\\{3}\r\n" + 
			"\r\n" + 
			"���� A, B, C ������ �ֽ��ϴ�\r\n" + 
			"�̵� ������ ī���� ����� 3_0_10\\{?} ����\r\n" + 
			"\r\n" + 
			"ī���ʹ� '\\' ���ڸ� �������� ������ ������ �˴ϴ�\r\n" + 
			"( 3_0_10 �� {1},{2},{3}(����) )\r\n" + 
			"\r\n" + 
			"���� 3_0_10�� ��û ��Ŷ�� ����µ� ���Ǵ� �����̸�\r\n" + 
			"�� �ڿ� �ٴ� ���� {1}, {2}, {3}�� \r\n" + 
			"\r\n" + 
			"���κ��� ������� ��Ŷ�� ó���Ͽ� ���� �����͸�\r\n" + 
			"������� A{1}, B{2}, C{3} �� ���� ������\r\n" + 
			"������Ʈ �ϰڴٴ� �ǹ��Դϴ�\r\n" + 
			"\r\n" + 
			"�׷��� ������ 3_0_10 �̶�� ������ ������� ��û��Ŷ����\r\n" + 
			"��񿡰� ��û�Ͽ� ������� ���� ��Ŷ�� ó���Ͽ�\r\n" + 
			"A, B, C ���ɿ� ���� �� �� �ִ� �� �Դϴ�.\r\n" + 
			"\r\n" + 
			"�� A, B, C�� ���� �ٸ� ����������\r\n" + 
			"������ �ѹ��� ��û�� ���Ͽ� ������Ŷ�� �޾�\r\n" + 
			"���� ���� �����Ǵ� �� �Դϴ�";
	
	
	// 06. nPerfInterval
	public static final String SERVER_PERF_nPerfInterval = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : nPerfInterval\r\n" + 
			"\r\n" + 
			"������ �����ֱ⸦ �ǹ��մϴ� ( ���� : �� )";
	
	// 07. strMeasure
	public static final String SERVER_PERF_strMeasure = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : strMeasure\r\n" + 
			"\r\n" + 
			"������ ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" �µ� : ��\r\n" + 
			" ���� : %\r\n" + 
			" ���� : V\r\n" + 
			" ���� : A";
	
	// 08. nEnable
	public static final String SERVER_PERF_nEnable = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : nEnable\r\n" + 
			"\r\n" + 
			"���� ��� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ����\r\n" + 
			" 1 : ���";
	
	// 09. dblLastUsage
	public static final String SERVER_PERF_dblLastUsage = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : dblLastUsage\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 10. strLastUsageTime
	public static final String SERVER_PERF_strLastUsageTime = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : strLastUsageTime\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 11. strOperation
	public static final String SERVER_PERF_strOperation = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : strOperation\r\n" + 
			"\r\n" + 
			"������ �������� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( ���� �� : 365 )\r\n" + 
			"( ������ : x/10 )\r\n" + 
			"( ���� �� : 36.5 )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"����������������������������������������������\r\n" + 
			"\r\n" + 
			"( ���� �� : 14 [0x1110] )\r\n" + 
			"( ������ : (x>>0)&1 )\r\n" + 
			"( ���� �� : 0 )\r\n" + 
			"\r\n" + 
			"���� : ���� ��(14 [0x1110])�� ��Ʈ ��������\r\n" + 
			"        ������ ��Ʈ����(>>)���� 0�� �̵��ϰ�(���ڸ�)\r\n" + 
			"        1 [0x0001] �� &(AND) ������ ��\r\n" + 
			"\r\n" + 
			"AND �����̶�\r\n" + 
			"�����ڿ� �ǿ������� ��Ʈ �ڸ��� ��� 1(��)�� �� \r\n" + 
			"��� ���� 1 �̰�\r\n" + 
			"�����ڿ� �ǿ����� �� �ϳ��� 0�̸�\r\n" + 
			"��� ���� 0 �Դϴ�\r\n" + 
			"\r\n" + 
			"��,\r\n" + 
			"0x1110 ( ���� �� : 14 )\r\n" + 
			"0x0001 ( AND���� �ǿ����� : 1 )\r\n" + 
			"\r\n" + 
			"���� ��Ʈ ������ ���� AND ���� ��\r\n" + 
			"������ �ǿ����� ��� 1�� �Ǵ� ��Ʈ �ڸ��� ���� ������\r\n" + 
			"������ ���� ��� 0�̶�� ���� ���� �� �Դϴ�\r\n" + 
			"\r\n" + 
			"����������������������������������������������\r\n" + 
			"\r\n" + 
			"( ���� �� : 15 [0x1111] )\r\n" + 
			"( ������ : (x>>3)&1 )\r\n" + 
			"( ���� �� : 1 )\r\n" + 
			"\r\n" + 
			"���� : ���� ��(15 [0x1111])�� ��Ʈ ��������\r\n" + 
			"        >>(������ ��Ʈ����)���� 3�� �̵��ϰ�\r\n" + 
			"        1[0x0001] �� &(AND) ������ ��\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"0x1111 ( ���� �� : 15 ) ����\r\n" + 
			"������ ��Ʈ����(>>) �� �����ϸ� 0x0001 �Դϴ�\r\n" + 
			"(���������� ��Ʈ ��ĭ��ŭ �̵��ϰ� ������� 0���� ä����)\r\n" + 
			"\r\n" + 
			"�� ��\r\n" + 
			"1 [0x0001] ���� AND ������ �մϴ�\r\n" + 
			"\r\n" + 
			"0x0001 ( ���� ������ ������ ��Ʈ���� 3�� ���� ��� : 1 [0x0001] )\r\n" + 
			"0x0001 ( AND���� �ǿ����� : 1 [0x0001] )\r\n" + 
			"\r\n" + 
			"���� ��Ʈ ������ ���� AND ���� ��\r\n" + 
			"0�� ��Ʈ�� �ڸ�����\r\n" + 
			"�����ڿ� �ǿ������� ��Ʈ�� ��� 1�̱� ������\r\n" + 
			"��� ������ 1�� ���� �� �Դϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���� ���� �Ŀ���\r\n" + 
			"���� �� ( 15 [0x1111] )���� ��Ʈ������ �����ʰ�\r\n" + 
			"�ǿ����ڰ� ( 1 [0x0001] )�� �ƴ϶� ( 9 [0x1001] )�̾��ٸ�\r\n" + 
			"\r\n" + 
			"0x1111 ( ���� �� : 15 )\r\n" + 
			"0x1001 ( AND���� �ǿ����� : 9 )\r\n" + 
			"\r\n" + 
			"������( 15 )�� �ǿ�����( 9 )��\r\n" + 
			"0�� ��Ʈ��\r\n" + 
			"3�� ��Ʈ��\r\n" + 
			"��� 1 �̱� ������ \r\n" + 
			"\r\n" + 
			"��� ���� 9 [0x1001] �Դϴ�\r\n" + 
			"\r\n" + 
			"���� ���� ������� ���� �������Ϳ��� ���� �о�����\r\n" + 
			"��Ʈ ������ ���Ͽ� DI ������ ǥ�� �� �� �ֽ��ϴ�\r\n" + 
			"\r\n" + 
			"0 : OFF\r\n" + 
			"1 : ON \r\n" + 
			"\r\n" + 
			"�Ǵ� \r\n" + 
			"\r\n" + 
			"0 : Normal\r\n" + 
			"1 : Alarm";
	
	// 12. strCommandCode
	public static final String SERVER_PERF_strCommandCode = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : strCommandCode\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 13. nPortNumber
	public static final String SERVER_PERF_nPortNumber = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : nPortNumber\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 14. DATA_FORMAT
	public static final String SERVER_PERF_DATA_FORMAT = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : DATA_FORMAT\r\n" + 
			"\r\n" + 
			"������ ������ ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ���� ���� ( DI )\r\n" + 
			" 1 : ���� ���� ( ��Ƽ �� )\r\n" + 
			" 2 : ���� ������ ( �Ƴ��α� ���� )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���� ����( DI )�� ���̺� ������\r\n" + 
			"( 0, 1 �� �� ǥ���� �� ���ڿ� ���� )\r\n" + 
			"PERF_LABEL_BOOLEAN ���̺��� �����մϴ�\r\n" + 
			"\r\n" + 
			"���� ����( ��Ƽ �� )�� ���̺� ������\r\n" + 
			"( ���� ���� �������� ���ڿ� ���� )\r\n" + 
			"PERF_LABEL_STATUS ���̺��� �����մϴ�";
	
	// 15. DELTA_COLLECTION
	public static final String SERVER_PERF_DELTA_COLLECTION = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : DELTA_COLLECTION\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 16. SAVE_DATA
	public static final String SERVER_PERF_SAVE_DATA = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : SAVE_DATA\r\n" + 
			"\r\n" + 
			"������ ������ ���� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ������ ���� ����\r\n" + 
			" 1 : ������ ���� ��\r\n" + 
			"\r\n" + 
			"0 ( ������ ���� ���� )�� ���� �� FileDB�� ������� �ʽ��ϴ�";
	
	// 17. DB_SAVE_METHOD
	public static final String SERVER_PERF_DB_SAVE_METHOD = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : DB_SAVE_METHOD\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 18. FAIR_LIMIT_UPPER
	public static final String SERVER_PERF_FAIR_LIMIT_UPPER = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : FAIR_LIMIT_UPPER\r\n" + 
			"\r\n" + 
			"���� ���� ���� ���� ���� �� ( ���� : % )";
	
	// 19. FAIR_LIMIT_LOWER
	public static final String SERVER_PERF_FAIR_LIMIT_LOWER = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : FAIR_LIMIT_LOWER\r\n" + 
			"\r\n" + 
			"���� ���� ���� ���� ���� �� ( ���� : % )";
	
	// 20. MODIFY_OPERATION
	public static final String SERVER_PERF_MODIFY_OPERATION = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : MODIFY_OPERATION\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 21. VALID_RANGE_ENABLE
	public static final String SERVER_PERF_VALID_RANGE_ENABLE = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : VALID_RANGE_ENABLE\r\n" + 
			"\r\n" + 
			"���� ��ȿ ���� �ɼ� ��� ����\r\n" + 
			"\r\n" + 
			" 0 : ��� ����\r\n" + 
			" 1 : ���";
	
	// 22. VALID_RANGE_UPPER
	public static final String SERVER_PERF_VALID_RANGE_UPPER = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : VALID_RANGE_UPPER\r\n" + 
			"\r\n" + 
			"���� ���� ��ȿ ���� ���� ��";
	
	// 23. VALID_RANGE_LOWER
	public static final String SERVER_PERF_VALID_RANGE_LOWER = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : VALID_RANGE_LOWER\r\n" + 
			"\r\n" + 
			"���� ���� ��ȿ ���� ���� ��";
	
	// 24. perf_property
	public static final String SERVER_PERF_perf_property = 
			"���̺� �� : SERVER_PERF\r\n" + 
			"�÷� �� : perf_property\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	

	
	/**
	 * ALARM ���̺� ����
	 */
	// 01. nAlarmIndex
	public static final String ALARM_nAlarmIndex = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nAlarmIndex\r\n" + 
			"\r\n" + 
			"ALARM ���̺��� �⺻Ű(primary key) �Դϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �ش� �÷��� null ���̳� �ߺ� ���� ���� �� ������\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� ������ �ε����� �ǹ��մϴ�";
	
	
	// 02. strAlarmName
	public static final String ALARM_strAlarmName = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : strAlarmName\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� �̸��� �ǹ��մϴ�";
	
	// 03. nEventSeverity
	public static final String ALARM_nEventSeverity = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nEventSeverity\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� �ɰ����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 10 : Normal\r\n" + 
			" 20 : Warning\r\n" + 
			" 30 : Minor\r\n" + 
			" 40 : Critical\r\n" + 
			" 50 : Fatal";
	
	// 04. nAlarmKind
	public static final String ALARM_nAlarmKind = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nAlarmKind\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 1 : �ý���\r\n" + 
			" 2 : ����\r\n" + 
			" 3 : �α�\r\n" + 
			" 4 : �׼�\r\n" + 
			" 5 : MK119 �α�\r\n" + 
			" 6 : �����ܼ� �α�\r\n" + 
			" 7 : ����� �α�\r\n" + 
			" 8 : ���� / ���μ���";
	
	// 05. nServerIndex
	public static final String ALARM_nServerIndex = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nServerIndex\r\n" + 
			"\r\n" + 
			"�ش� �̺�Ʈ�� ������ ����� �ε����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �ش� �̺�Ʈ�� �������ִ� ��� �ε����� �ǹ��ϴ�\r\n" + 
			"\r\n" + 
			"SERVERINFO ���̺���\r\n" + 
			"nServerIndex �÷��� ������ ������ �����մϴ�";
	
	// 06. nEnable
	public static final String ALARM_nEnable = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nEnable\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� �߻� ��� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ����\r\n" + 
			" 1 : ���";
	
	// 07. nNotify
	public static final String ALARM_nNotify = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nNotify\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� �뺸 ��� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ����\r\n" + 
			" 1 : ���";
	
	// 08. nWarnStatus
	public static final String ALARM_nWarnStatus = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nWarnStatus\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� �̻� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"-1 : �� �� ����\r\n" + 
			" 0 : ����\r\n" + 
			" 1 : �̻�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 09. dblErrorValue
	public static final String ALARM_dblErrorValue = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : dblErrorValue\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� �̻� ���� �Ǵ��� ���� �ʵ��Դϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 10. strMeasure
	public static final String ALARM_strMeasure = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : strMeasure\r\n" + 
			"\r\n" + 
			"dblErrorValue �÷��� ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 11. nAlarmCheckMode
	public static final String ALARM_nAlarmCheckMode = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nAlarmCheckMode\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �߻� ��带 �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 1 : ������ �ð����� ���°� ���� �� ���\r\n" + 
			" 2 : ������ �ð����� ������ Ƚ����ŭ ������ �߻� �� ���\r\n" + 
			" 3 : ���� �߻��� �ѹ���\r\n" + 
			" 4 : ������ �߻� �� ������\r\n" + 
			" 6 : ������ Ƚ����ŭ ������ �������� �߻� �� ���";
	
	// 12. nAlarmSequence
	public static final String ALARM_nAlarmSequence = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nAlarmSequence\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� �뺸 Ƚ���� �ǹ��մϴ�";
	
	// 13. nAlarmInterval
	public static final String ALARM_nAlarmInterval = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nAlarmInterval\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� ���� �ð��� �ǹ��մϴ� ( ���� : �� )";
	
	// 14. nHitCount
	public static final String ALARM_nHitCount = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nHitCount\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� �̻� �߻� Ƚ���� �ǹ��մϴ�";
	
	// 15. nAlarmMedia
	public static final String ALARM_nAlarmMedia = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nAlarmMedia\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �뺸 ����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ��� ����\r\n" + 
			" 1 : SMS ���\r\n" + 
			" 2 : Mail ���\r\n" + 
			" 3 : SMS, Mail ���\r\n" + 
			" 4 : Action ���\r\n" + 
			" 5 : SMS, Action ���\r\n" + 
			" 6 : Mail, Action ���\r\n" + 
			" 7 : SMS, Mail, Action ��� ( ��� ��� )";
	
	// 16. nActionIndex
	public static final String ALARM_nActionIndex = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nActionIndex\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �뺸 ��� ��\r\n" + 
			"Action( ���� ��� )�� ���� �Ͽ��� ���\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �߻� ��\r\n" + 
			"�����ϰ� �� Action( ���� ���)�� �ε����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �̺�Ʈ �߻� �� �����ϰ� �� �׼�( ���� ) ������\r\n" + 
			"\r\n" + 
			"SERVER_CONTROL ���̺���\r\n" + 
			"nControlIndex �÷� ���� �ǹ��մϴ�";
	
	// 17. nActionServerIndex
	public static final String ALARM_nActionServerIndex = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nActionServerIndex\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �뺸 ��� ��\r\n" + 
			"Action( ���� ��� )�� ���� �Ͽ��� ���\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �߻� ��\r\n" + 
			"�����ϰ� �� Action( ���� ��� )�� �������ִ� \r\n" + 
			"\r\n" + 
			"����� �ε����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, SERVER_CONTROL ( ���� ���� )���̺���\r\n" + 
			"nServerIndex �÷� ������ �ǹ��մϴ�";
	
	// 18. strActionCommand
	public static final String ALARM_strActionCommand = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : strActionCommand\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 19. strActionParameter
	public static final String ALARM_strActionParameter = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : strActionParameter\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �뺸 ��� ��\r\n" + 
			"Action( ���� ��� )�� ���� �Ͽ��� ���\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �߻� ��\r\n" + 
			"�����ϰ� �� Action( ���� ��� )�� �Ķ���� �� �Դϴ�\r\n" + 
			"( �̺�Ʈ ��� ������ ���� �Ķ���� �ʵ� )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �ش� �̺�Ʈ�� �߻��Ͽ��� ��\r\n" + 
			"�����µ� ���� ���� ��ɾ ����ǵ��� �����Ͽ��� ��\r\n" + 
			"�����µ� ���� ������ ���� �� ���� ���� �� �Դϴ�";
	
	// 20. nActionIndex2
	public static final String ALARM_nActionIndex2 = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nActionIndex2\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �뺸 ��� ��\r\n" + 
			"Action( ���� ��� )�� ���� �Ͽ��� ���\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �߻� �� �ڵ� ����( ����ȭ ) ��\r\n" + 
			"�����ϰ� �� Action( ���� ���)�� �ε����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �̺�Ʈ �߻� �� �ڵ� ����( ����ȭ ) ��\r\n" + 
			"�����ϰ� �� �׼� ����\r\n" + 
			"\r\n" + 
			"SERVER_CONTROL ���̺���\r\n" + 
			"nControlIndex �÷� ���� �ǹ��մϴ�";
	
	// 21. strActionCommand2
	public static final String ALARM_strActionCommand2 = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : strActionCommand2\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 22. strActionParameter2
	public static final String ALARM_strActionParameter2 = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : strActionParameter2\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �뺸 ��� ��\r\n" + 
			"Action( ���� ��� )�� ���� �Ͽ��� ���\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �߻� �� �ڵ� ����( ����ȭ ) ��\r\n" + 
			"�����ϰ� �� Action( ���� ��� )�� �Ķ���� �� �Դϴ�\r\n" + 
			"( �̺�Ʈ ��� ������ ���� �Ķ���� �ʵ� )\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �ش� �̺�Ʈ�� �߻� �� �ڵ� ����( ����ȭ ) ��\r\n" + 
			"�����µ� ���� ���� ��ɾ ����ǵ��� �����Ͽ��� ��\r\n" + 
			"�����µ� ���� ������ ���� �� ���� ���� �� �Դϴ�";
	
	// 23. strClassName
	public static final String ALARM_strClassName = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : strClassName\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 24. strCustomMessage
	public static final String ALARM_strCustomMessage = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : strCustomMessage\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� �˸� �޽��� ���� ������ �ǹ��մϴ�";
	
	// 25. nSchedule
	public static final String ALARM_nSchedule = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nSchedule\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� ������ ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : ������ ����\r\n" + 
			" 1 : �����ٸ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 26. AUTO_CLOSE
	public static final String ALARM_AUTO_CLOSE = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : AUTO_CLOSE\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� �ڵ� ���� �ɼ� ��� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : �̺�Ʈ �ڵ� ���� ��� ����\r\n" + 
			" 1 : �̺�Ʈ �ڵ� ���� ���";
	
	// 27. ACTION_MODE
	public static final String ALARM_ACTION_MODE = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : ACTION_MODE\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �뺸 ��� ��\r\n" + 
			"Action( ���� ��� )�� ���� �Ͽ��� ���\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �߻� ��\r\n" + 
			"�����ϰ� �� Action( ���� ��� )�� \r\n" + 
			"����( �뺸 ) ����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : �뺸�� �ѹ��� ����\r\n" + 
			" 1 : �뺸�� �Ź� ����";
	
	// 28. EVENT_GUIDE
	public static final String ALARM_EVENT_GUIDE = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : EVENT_GUIDE\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� ��� ó�� ��ħ ������ �ǹ��մϴ�";
	
	
	// 29. SUSPEND_LIMIT
	public static final String ALARM_SUSPEND_LIMIT = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : SUSPEND_LIMIT\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 30. SUSPEND_START_TIME
	public static final String ALARM_SUSPEND_START_TIME = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : SUSPEND_START_TIME\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 31. SUSPEND_END_TIME
	public static final String ALARM_SUSPEND_END_TIME = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : SUSPEND_END_TIME\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 32. SUSPEND_ALARM
	public static final String ALARM_SUSPEND_ALARM = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : SUSPEND_ALARM\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 33. nSaveEnable
	public static final String ALARM_nSaveEnable = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : nSaveEnable\r\n" + 
			"\r\n" + 
			"�̺�Ʈ ���� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : �̺�Ʈ ���� ����\r\n" + 
			" 1 : �̺�Ʈ ���� ��";
	
	// 34. CAMERA_PRESET2
	public static final String ALARM_CAMERA_PRESET2 = 
			"���̺� �� : ALARM\r\n" + 
			"�÷� �� : CAMERA_PRESET2\r\n" + 
			"\r\n" + 
			"�̺�Ʈ ī�޶� ���� ���� �ʵ��Դϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	
	
	
	/**
	 * EVENTS ���̺� ����
	 */
	// 01. nIndex
	public static final String EVENTS_nIndex = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : nIndex\r\n" + 
			"\r\n" + 
			"EVENTS ���̺��� �⺻Ű(primary key) �Դϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �ش� �÷��� null ���̳� �ߺ� ���� ���� �� ������\r\n" + 
			"\r\n" + 
			"�̺�Ʈ ������ ������ �ε����� �ǹ��մϴ�";
	
	// 02. strEventName
	public static final String EVENTS_strEventName = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strEventName\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �̸��� �ǹ��մϴ�";
	
	// 03. strEventContent
	public static final String EVENTS_strEventContent = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strEventContent\r\n" + 
			"\r\n" + 
			"�߻��� �̺�Ʈ��\r\n" + 
			"\r\n" + 
			"�̺�Ʈ ������ �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��) ���簪: 36.5�� ���� �µ��� ���� 30�� �̻��Դϴ�.\r\n" + 
			"��) ���簪: 55.0�� ���� ������ ���� 50% �̻��Դϴ�.";
	
	// 04. strEventDate
	public static final String EVENTS_strEventDate = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strEventDate\r\n" + 
			"\r\n" + 
			"�̺�Ʈ �߻� �ð��� �ǹ��մϴ�";
	
	// 05. strSystemName
	public static final String EVENTS_strSystemName = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strSystemName\r\n" + 
			"\r\n" + 
			"�⺻ �� : Midknight\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 06. nSystemIndex
	public static final String EVENTS_nSystemIndex = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : nSystemIndex\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 07. nServerIndex
	public static final String EVENTS_nServerIndex = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : nServerIndex\r\n" + 
			"\r\n" + 
			"�ش� �̺�Ʈ�� ��� �� ����� �ε����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �ش� �̺�Ʈ�� ��� �� ��� �ε����� �ǹ��ϴ�\r\n" + 
			"\r\n" + 
			"SERVERINFO ���̺���\r\n" + 
			"nServerIndex �÷��� ������ ������ �����մϴ�";
	
	
	// 08. strHostName
	public static final String EVENTS_strHostName = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strHostName\r\n" + 
			"\r\n" + 
			"�ش� �̺�Ʈ�� �߻��� �����\r\n" + 
			"\r\n" + 
			"������ �ǹ��մϴ�";
	
	// 09. strHostIP
	public static final String EVENTS_strHostIP = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strHostIP\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� �߻��� ��� ��� �� IP�� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"���� ��� �ø��� ��Ʈ ���� ��İ� ����\r\n" + 
			"\r\n" + 
			"RCU�� �̿��� ��� �� �̶��\r\n" + 
			"\r\n" + 
			"RCU�� ��� �� IP�� �ǹ��մϴ�";
	
	// 10. nSeverity
	public static final String EVENTS_nSeverity = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : nSeverity\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� �ɰ����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 10 : Normal\r\n" + 
			" 20 : Warning\r\n" + 
			" 30 : Minor\r\n" + 
			" 40 : Critical\r\n" + 
			" 50 : Fatal";
		
	// 11. strProcessUser
	public static final String EVENTS_strProcessUser = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strProcessUser\r\n" + 
			"\r\n" + 
			"�߻��Ͽ� �̺�Ʈ ���� �������� ��� ��\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� ���¸� ����� ���� ��\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� ���� ó���� ó���ڸ� �ǹ��մϴ�";
	
	// 12. strProcessDate
	public static final String EVENTS_strProcessDate = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strProcessDate\r\n" + 
			"\r\n" + 
			"�߻��Ͽ� �̺�Ʈ ���� �������� ��� ��\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� ���¸�\r\n" + 
			"\r\n" + 
			"ó���ڰ� ���� �Ǵ� ����� ���� ��\r\n" + 
			"\r\n" + 
			"ó���ڰ� ���� �� �̺�Ʈ�� ���� �ð��� �ǹ��մϴ�";
	
	// 13. strProcessContent
	public static final String EVENTS_strProcessContent = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strProcessContent\r\n" + 
			"\r\n" + 
			"�߻��Ͽ� �̺�Ʈ ���� �������� ��� ��\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� ���¸�\r\n" + 
			"\r\n" + 
			"ó���ڰ� ���� �Ǵ� ����� ���� ��\r\n" + 
			"\r\n" + 
			"ó���ڰ� �Է� �� �̺�Ʈ�� ó�� ������ �ǹ��մϴ�";
	
	// 14. strCompleteDate
	public static final String EVENTS_strCompleteDate = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strCompleteDate\r\n" + 
			"\r\n" + 
			"�߻��Ͽ� �̺�Ʈ ���� �������� ��� ��\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� ���¸�\r\n" + 
			"\r\n" + 
			"ó���ڰ� ����� ���� ��\r\n" + 
			"\r\n" + 
			"ó���ڰ� ���� �� �̺�Ʈ�� ���� �ð��� �ǹ��մϴ�";
	
	// 15. nStatus
	public static final String EVENTS_nStatus = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : nStatus\r\n" + 
			"\r\n" + 
			"�̺�Ʈ ������ ��� ��\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� ���¸� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : �߻�\r\n" + 
			" 1 : ����\r\n" + 
			" 2 : ����\r\n" + 
			" 3 : �ڵ� ����( �ڵ� ���� )";
	
	// 16. strSessionID
	public static final String EVENTS_strSessionID = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strSessionID\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";

	// 17. nAlarmIndex
	public static final String EVENTS_nAlarmIndex = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : nAlarmIndex\r\n" + 
			"\r\n" + 
			"�̺�Ʈ ������ ��� ��\r\n" + 
			"\r\n" + 
			"�̺�Ʈ( �˶� )�� �ε����� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, �̺�Ʈ ������ �����ϴ�\r\n" + 
			"\r\n" + 
			"ALARM ���̺���\r\n" + 
			"nAlarmIndex �÷� ������ �ǹ��մϴ�";
	
	// 18. nRepeatCount
	public static final String EVENTS_nRepeatCount = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : nRepeatCount\r\n" + 
			"\r\n" + 
			"�ش� �̺�Ʈ�� �߻�, ���� ���¿��� �߻��� ȸ���� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"��, ������� ���� �̺�Ʈ�� �ݺ� �߻� ȸ���� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 19. strLastOccurTime
	public static final String EVENTS_strLastOccurTime = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strLastOccurTime\r\n" + 
			"\r\n" + 
			"�ش� �̺�Ʈ�� ���������� �߻��� �ð��� �ǹ��մϴ�";
	
	// 20. AUTO_CLOSE
	public static final String EVENTS_AUTO_CLOSE = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : AUTO_CLOSE\r\n" + 
			"\r\n" + 
			"�̺�Ʈ�� �ڵ� ���� �ɼ� ��� ���θ� �ǹ��մϴ�\r\n" + 
			"\r\n" + 
			" 0 : �̺�Ʈ �ڵ� ���� ��� ����\r\n" + 
			" 1 : �̺�Ʈ �ڵ� ���� ���";
	
	// 21. EVENT_PROCESSING_TYPE
	public static final String EVENTS_EVENT_PROCESSING_TYPE = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : EVENT_PROCESSING_TYPE\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 22. strConfirmedUserName
	public static final String EVENTS_strConfirmedUserName = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strConfirmedUserName\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
	
	// 23. strFaultCause
	public static final String EVENTS_strFaultCause = 
			"���̺� �� : EVENTS\r\n" + 
			"�÷� �� : strFaultCause\r\n" + 
			"\r\n" + 
			"( �ڼ��� �÷� ��� ���� ���� )";
		
	
	
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
