package common.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.swing.JOptionPane;

import common.util.TextUtil;
import src_ko.database.DbUtil;
import src_ko.info.ONION_Info;
import src_ko.util.Util;

public class Event implements Comparable{
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final String GET_EVENT = 
			"SELECT a.*, b.strSeverity, b.nBkColor, b.nTextColor\r\n" + 
			"\r\n" + 
			"FROM EVENTS a WITH(NOLOCK), SYSTEM_SEVERITY b WITH(NOLOCK)\r\n" + 
			"\r\n" + 
			"WHERE a.nSeverity = b.nSeverity\r\n" + 
			"AND ( nStatus = 0 or nStatus = 1 )\r\n" + 
			"\r\n" + 
			"ORDER BY a.nSeverity DESC, a.nServerIndex ASC";
	
	public static void showSimpleEventInfo(Server server) {
		int normal_0 = 0;
		int normal_1 = 0;
		
		int warning_0 = 0;
		int warning_1 = 0;
		
		int minor_0 = 0;
		int minor_1 = 0;
		
		int critical_0 = 0;
		int critical_1 = 0;
		
		int fatal_0 = 0;
		int fatal_1 = 0;
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%s%s\n", Util.colorBlue("이벤트 내역"),Util.separator, Util.separator));
		sb.append(String.format("%s : %s%s%s\n", Util.colorBlue("장비명"), server.getName(),Util.separator, Util.separator));
		
		String type = server.isFacility() ? ((Facility)server).getTypeString() : ((RCU)server).getRcuTypeDetail();
		sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("장비 종류"), type,Util.separator, Util.separator));
		
		try {
			ArrayList<Event> events = server.getEvents();
			
			for(int i = 0; i < events.size(); i++) {
				Event e = events.get(i);
				
				switch(e.getSeverity()) {
					case 10:
						if(e.getStatus() == 0) {
							normal_0++;
						}else if(e.getStatus() == 1){
							normal_1++;
						}
						break;
						
					case 20:
						if(e.getStatus() == 0) {
							warning_0++;
						}else if(e.getStatus() == 1){
							warning_1++;
						}
						break;
						
					case 30:
						if(e.getStatus() == 0) {
							minor_0++;
						}else if(e.getStatus() == 1){
							minor_1++;
						}
						break;
						
					case 40:
						if(e.getStatus() == 0) {
							critical_0++;
						}else if(e.getStatus() == 1){
							critical_1++;
						}
						break;
						
					case 50:
						if(e.getStatus() == 0) {
							fatal_0++;
						}else if(e.getStatus() == 1){
							fatal_1++;
						}
						break;
				}
			}
		
			ArrayList<SystemSeverity> seve = SystemSeverity.getSystemSeverity(ONION_Info.getMk119Connection());
			for(int i = 0; i < seve.size(); i++) {
				SystemSeverity s = seve.get(i);
				
				int _0 = 0;
				int _1 = 0;
				
				switch(s.getnSeverity()) {
					case 10 :
						_0 = normal_0;
						_1 = normal_1;
						break;
					case 20 :
						_0 = warning_0;
						_1 = warning_1;
						break;
					case 30 :
						_0 = minor_0;
						_1 = minor_1;
						break;
					case 40 :
						_0 = critical_0;
						_1 = critical_1;
						break;
					case 50 :
						_0 = fatal_0;
						_1 = fatal_1;
						break;
				}
				
				sb.append(String.format("%s", TextUtil.setTextStyle(s.getStrSeverity(), s.getnTextColor(), s.getnBkColor())));
				sb.append("&nbsp;&nbsp;");
				sb.append((_0 > 0) ? Util.colorRed("발 생 : " + _0) : "발 생 : " + _0);
				sb.append("&nbsp;&nbsp;/&nbsp;&nbsp;");
				sb.append((_1 > 0) ? Util.colorRed("인 지 : " + _1) : "인 지 : " + _1);
				sb.append(Util.separator + Util.separator + Util.separator + Util.separator + Util.separator + "\n\n");
			}
			
			int lastLineSeparator = sb.lastIndexOf("\n");
			sb.replace(lastLineSeparator, lastLineSeparator+1, "");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
				
		int menu = Util.showOption(sb.toString(), new String[] { "이벤트 내용 보기", " 확 인 "}, JOptionPane.INFORMATION_MESSAGE);
		switch (menu) {					
			case 0: // 이벤트 내용 보기
				showDetailEventInfo(server);
				return;
			default :
				return;
		}
	}
	
	public static void showDetailEventInfo(Server server) {
	
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%s%s\n", Util.colorBlue("이벤트 내역"),Util.separator, Util.separator));
		sb.append(String.format("%s : %s%s%s\n", Util.colorBlue("장비명"), server.getName(),Util.separator, Util.separator));
		
		String type = server.isFacility() ? ((Facility)server).getTypeString() : ((RCU)server).getRcuTypeDetail();
		sb.append(String.format("%s : %s%s%s\n\n", Util.colorBlue("장비 종류"), type,Util.separator, Util.separator));
		
		try {
			ArrayList<Event> events = server.getEvents();
			Collections.sort(events);
			
			for(int i = 0; i < events.size(); i++) {
				Event e = events.get(i);
				
				sb.append(String.format("%d. ", i + 1));
				sb.append(String.format(" %s ", TextUtil.setTextStyle(e.getSeverityName(), e.getSeverityTextColor(), e.getSeverityBkColor())));
				sb.append("&nbsp;" + Util.colorBlue("-") + "&nbsp;");
				
				String status = e.getStatus() == 0 ? "발생 상태" : "인지 상태";
				sb.append(String.format("%s : %s", Util.colorBlue("상 태"), status));
				sb.append("&nbsp;&nbsp;" + Util.colorRed("/") + "&nbsp;&nbsp;");
				
				sb.append(String.format("%s : %s", Util.colorBlue("발생 시각"), e.getEventDate()));
				sb.append("&nbsp;&nbsp;" + Util.colorRed("/") + "&nbsp;&nbsp;");
				
				sb.append(String.format("%s : %s",Util.colorBlue("이벤트명"), e.getEventName()));
				sb.append("&nbsp;&nbsp;" + Util.colorRed("/") + "&nbsp;&nbsp;");
				
				sb.append(String.format("%s : %s", Util.colorBlue("내 용") ,e.getEventContent()));
				
				sb.append(Util.separator + Util.separator + Util.separator + Util.separator);
				sb.append("\n");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
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
	
	@Override
	public int compareTo(Object obj) {
		Event event = (Event)obj;
		
		Date date1 = null;
		Date date2 = null;
		
		try {
			date1 = sdf.parse(this.getEventDate());
			date2 = sdf.parse(event.getEventDate());
		}catch(Exception e) {
			e.printStackTrace();
			date1 = new Date();
			date2 = new Date();
		}
		
		long dateMs1 = date1.getTime();
		long dateMs2 = date2.getTime();
				
		// 가장 최근 발생한 이벤트부터
		if(dateMs1 > dateMs2) {
			return -1;
		}else if(dateMs1 == dateMs2) {
			
			// 이벤트 심각도가 높은 순위부터
			if(this.getSeverity() > event.getSeverity()) {
				return -1;
			}else if(this.getSeverity() == event.getSeverity()) {
				return 0;
			}else {
				return 1;
			}
			
		}else {
			return 1;
		}
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
    
    public static ArrayList<Event> getEvents(Connection connection){
    	ArrayList<Event> eventList = new ArrayList<Event>();
    	
    	try {
	    	Statement stmt = connection.createStatement();
	    	ResultSet rs = stmt.executeQuery(Event.GET_EVENT);
	    	
	    	while(rs.next()) {
	    		try {
		    		Event event = Event.makeBean(rs);		    		
		    		eventList.add(event);
	    		}catch(SQLException e) {
	    			e.printStackTrace();
	    			continue;
	    		}
	    	}
    	
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return eventList;
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
            bean.eventContent = bean.eventContent.substring(bean.eventContent.indexOf("$$") + 2, bean.eventContent.length());
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
