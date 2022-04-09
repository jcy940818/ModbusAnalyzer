package common.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import src_ko.database.DbUtil;

public class Event {
	
	public static void main(String[] args) {
		Connection conn = DbUtil.makeConnection("jdbc:sqlserver://localhost:1433;databaseName=MK119_4.2", "sa", "1");
		int serverIndex = 28;
		boolean isEvent = Event.isEvent(conn, serverIndex);
		
        if (isEvent) {
        	Event event = Event.getEventLogServer(conn, serverIndex);
        	printEvent(event);
        }else {
            String eventInfo = "정상";            
        }
	}
	
	public static void printEvent(Event event) {
		System.out.println("Event index : " + event.getIndex());
    	System.out.println("strEventName : " + event.getEventName());
    	System.out.println("strEventContent : " + event.getEventContent());
    	System.out.println("strEventDate : " + event.getEventDate());
    	System.out.println("strSystemName : " + event.getSystemName());
    	System.out.println("strSystemIndex : " + event.getSystemIndex());
    	System.out.println("nServerIndex : " + event.getServerIndex());
    	System.out.println("strHostName : " + event.getHostName());
    	System.out.println("strHostIP : " + event.getHostIP());
    	System.out.println("nServerity : " + event.getSeverity());
    	System.out.println("strProcessUser : " + event.getProcessUser());
    	System.out.println("strProcessDate : " + event.getProcessDate());
    	System.out.println("strProcessContent : " + event.getProcessContent());
    	System.out.println("strCompleateDate : " + event.getCompleteDate());
    	System.out.println("nStatue : " + event.getStatus());
    	System.out.println("strSessionID : " + event.getSessionID());
    	System.out.println("nAlarmIndex : " + "필드 없음");
    	System.out.println("nRepeatCount : " + "필드 없음");
    	System.out.println("strLastOccurTime : " + "필드 없음");
    	System.out.println("AUTO_CLOSE : " + "필드 없음");
    	System.out.println("strSeverity : " + event.getSeverityName());
    	System.out.println("nBkColor : " + event.getSeverityBkColor() + " (" + event.getSeverityBkColorHexString() + ")");
    	System.out.println("nTextColor : " + event.getSeverityTextColor() + " (" + event.getSeverityTextColorHexString() + ")");
	}
	
	/** EVENTS 테이블 */
    public int index;
    /** 이벤트 이름 */
    public String eventName;
    /** 이벤트 내용 */
    public String eventContent;
    /** 이벤트 날짜 */
    public String eventDate;
    /** 시스템 이름 */
    public String systemName;
    /** 시스템 인덱스 */
    public int systemIndex;
    /** 호스트 이름 */
    public String hostName;
    /** 아이피 */
    public String hostIP;
    public int severity;
    /** 처리자 */
    public String processUser;
    /** 처리일 */
    public String processDate;
    /** 처리내용 */
    public String processContent;
    /** 완료일 */
    public String completeDate;
    /** 상태 */
    public int status = -1;
    public String sessionID;
    /** 서버 인덱스 */
    public int serverIndex;
    /** 서버 이름 */
    public String severName;
    
    /** SYSTEM_SEVERITY 테이블 */
    public String severityName;
    public int severityBkColor = 0;
    public int severityTextColor = 0;
    public int eventProcessingType = 0;
    public String Description = "";
    
    public Event() {
		
	}
	
	public Event(int index) {
        this.index = index;
    }
    
    /**
     * BkColor를 HexString으로 만들어서 리턴
     */
    public String getSeverityBkColorHexString() {
        String str = "000000" + Integer.toHexString(severityBkColor);
        return str.substring(str.length() - 6);
    }

    /**
     * TextColor를 HexString으로 만들어서 리턴
     */
    public String getSeverityTextColorHexString() {
        String str = "000000" + Integer.toHexString(severityTextColor);
        return str.substring(str.length() - 6);
    }
    
	/**
     * 특정 장비에 발생 또는 인지 상태의 이벤트가 있는가
     * 
     * @param index
     * @return
     * @throws SQLException
     */
    public static boolean isEvent(Connection connection, int index){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT nStatus ");
        sql.append("FROM EVENTS WITH(NOLOCK) ");
        sql.append("WHERE (nStatus = 0 or nStatus = 1) and nServerIndex = " + index);

        try {
            conn = connection;
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toString());
            if(rs==null){
            	return false;
            }
            else
            return true;
        } catch(Exception e) {
        	e.printStackTrace();
        	return false;
        }
    }
    
    
    /**
     * 장비에서 발생한 이벤트에서 최저 상태 중 최고 레벨 이벤트를 얻는다.
     * @param index 장비 ID
     * @return 최고 레벨 이벤트
     * @throws SQLException
     */
	public static Event getEventLogServer(Connection connection, int index){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		try {
			conn = connection;
			stmt = conn.createStatement();

			sql.append("SELECT a.*, b.strSeverity, ");
			sql.append("b.nBkColor, ");
			sql.append("b.nTextColor ");
			sql.append("FROM EVENTS a WITH(NOLOCK), SYSTEM_SEVERITY b WITH(NOLOCK) ");
			sql.append("WHERE a.nSeverity = b.nSeverity ");
			sql.append("AND nServerIndex = (").append(index).append(")");
			sql.append("AND nStatus=");
			sql.append("(SELECT MIN(nStatus) FROM EVENTS WITH(NOLOCK) WHERE nServerIndex = (").append(index)
					.append(")) ");
			sql.append("ORDER BY a.nSeverity DESC");

			rs = stmt.executeQuery(sql.toString());

			Event bean = new Event();
			if (rs.next()) {
				bean = Event.makeBean(rs);
			}

			 return bean;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
     * SYSTEM_SEVERITY 테이블과 JOIN해서 사용할 것
     */
    public static Event makeBean(ResultSet rs) throws SQLException {
    	Event bean = new Event(rs.getInt("nIndex"));
        String temp;
        temp = rs.getString("strEventName");
        bean.eventName = (temp == null) ? "" : temp;
        temp = rs.getString("strEventContent");
        bean.eventContent = (temp == null) ? "" : temp;
        if (bean.eventContent.indexOf("$$") > -1) {
            bean.eventContent = bean.eventContent.substring(bean.eventContent.indexOf("$$") + 2, bean.eventContent
                    .length());
        }
        temp = rs.getString("strEventDate");
        bean.eventDate = (temp == null) ? "" : temp;
        temp = rs.getString("strSystemName");
        bean.systemName = (temp == null) ? "" : temp;
        bean.systemIndex = rs.getInt("nSystemIndex");
        bean.serverIndex = rs.getInt("nServerIndex");
        temp = rs.getString("strHostName");
        bean.hostName = (temp == null) ? "" : temp;
        temp = rs.getString("strHostIP");
        bean.hostIP = (temp == null) ? "" : temp;
        bean.severity = rs.getInt("nSeverity");
        temp = rs.getString("strProcessUser");
        bean.processUser = (temp == null) ? "" : temp;
        temp = rs.getString("strProcessDate");
        bean.processDate = (temp == null) ? "" : temp;
        temp = rs.getString("strProcessContent");
        bean.processContent = (temp == null) ? "" : temp;
        temp = rs.getString("strCompleteDate");
        bean.completeDate = (temp == null) ? "" : temp;
        bean.status = rs.getInt("nStatus");
        
        if(DbUtil.columnExists(rs, "strServerName"))
        bean.severName = rs.getString("strServerName");

        temp = rs.getString("strSessionID");
        bean.sessionID = (temp == null) ? "" : temp;

        // SYSTEM_SEVERITY 테이블
        temp = rs.getString("strSeverity");

        bean.severityName = (temp == null) ? "" : temp;
        
        if(DbUtil.columnExists(rs, "nBkColor"))
        bean.severityBkColor = rs.getInt("nBkColor");
        
        if(DbUtil.columnExists(rs, "nTextColor"))
        bean.severityTextColor = rs.getInt("nTextColor");

        return bean;
    }
	

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventContent() {
		return eventContent;
	}

	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public int getSystemIndex() {
		return systemIndex;
	}

	public void setSystemIndex(int systemIndex) {
		this.systemIndex = systemIndex;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getHostIP() {
		return hostIP;
	}

	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public String getProcessUser() {
		return processUser;
	}

	public void setProcessUser(String processUser) {
		this.processUser = processUser;
	}

	public String getProcessDate() {
		return processDate;
	}

	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}

	public String getProcessContent() {
		return processContent;
	}

	public void setProcessContent(String processContent) {
		this.processContent = processContent;
	}

	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public int getServerIndex() {
		return serverIndex;
	}

	public void setServerIndex(int serverIndex) {
		this.serverIndex = serverIndex;
	}

	public String getSeverName() {
		return severName;
	}

	public void setSeverName(String severName) {
		this.severName = severName;
	}

	public String getSeverityName() {
		return severityName;
	}

	public void setSeverityName(String severityName) {
		this.severityName = severityName;
	}

	public int getSeverityBkColor() {
		return severityBkColor;
	}

	public void setSeverityBkColor(int severityBkColor) {
		this.severityBkColor = severityBkColor;
	}

	public int getSeverityTextColor() {
		return severityTextColor;
	}

	public void setSeverityTextColor(int severityTextColor) {
		this.severityTextColor = severityTextColor;
	}

	public int getEventProcessingType() {
		return eventProcessingType;
	}

	public void setEventProcessingType(int eventProcessingType) {
		this.eventProcessingType = eventProcessingType;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}
	
}
