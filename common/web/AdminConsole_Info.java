package common.web;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import org.jsoup.HttpStatusException;

import common.agent.RestAgent;
import common.util.HttpUtil;
import moon.Moon;
import src_ko.util.Util;

public class AdminConsole_Info {
	
	public HashMap headerMap = new HashMap<String, String>();
	public HashMap parameterMap = new HashMap<String, String>();	
	
	private String versionInfo;
	private String version;
	private String build;
	
	private String IP;
	private String PORT;
	private String SESSION_ID;
	
	private String ID;
	private String PW;
	private String USER_NAME;
	
	private int httpStatusCode;
	private String httpStatus = "";
	
	public AdminConsole_Info(String IP, String PORT, String ID, String PW, String JSESSIONID) {
		this.IP = IP;
		this.PORT = PORT;
		this.ID = ID;
		this.PW = PW;
		this.SESSION_ID = JSESSIONID;
	}
	
	public void initHeader() {
		headerMap.put("Content-Type", "application/x-www-form-urlencoded");
		headerMap.put("Accept-Charset", "utf-8");
		headerMap.put("Connection", "keep-alive");
		headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		headerMap.put("Cookie", "JSESSIONID=" + this.SESSION_ID);
	}
	
	public void initParameter() {
		parameterMap.put("cat", "performance");
		parameterMap.put("cmd", "addPerf");		
		parameterMap.put("perfType", "15");
		parameterMap.put("agent", "16");
		parameterMap.put("sort", "item");
		parameterMap.put("order", "ASC");
		parameterMap.put("counterStr", "");
		parameterMap.put("oneMoreAdd", "false");
		parameterMap.put("appObjectChange", "");
		parameterMap.put("rtuIndex", "");
		parameterMap.put("addMethod", "1");
	}
	
	public static String refreshSession(AdminConsole_Info adminConsole) {
				
		String sessionId = null;
		
		try {
			if(Moon.isKorean()) {
				sessionId = new src_ko.agent.HttpAgent().getMK119SessionId(adminConsole);	
			}else {
				sessionId = new src_en.agent.HttpAgent().getMK119SessionId(adminConsole);
			}
		}catch(SocketTimeoutException e) {
			e.printStackTrace();
			System.out.println("응답 타임아웃 초과");
		}catch(ConnectException e) {
			e.printStackTrace();
			System.out.println("서버가 실행 되어있지 않음");
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("예외 발생");
		}
		
		if(sessionId != null) {
			// MK119 AdminConsole 로그인 성공
			adminConsole.set_SESSION_ID(sessionId);
		}else {
			// MK119 AdminConsole 로그인 실패
		}
		
		return sessionId;
	}

	public String get_IP() {
		return IP;
	}

	public void set_IP(String IP) {
		this.IP = IP;
	}

	public String get_PORT() {
		return PORT;
	}

	public void set_PORT(String PORT) {
		this.PORT = PORT;
	}

	public String get_ID() {
		return ID;
	}

	public void set_ID(String ID) {
		this.ID = ID;
	}

	public String get_PW() {
		return PW;
	}

	public void set_PW(String PW) {
		this.PW = PW;
	}

	public String get_SESSION_ID() {
		return SESSION_ID;
	}

	public void set_SESSION_ID(String SESSION_ID) {
		this.SESSION_ID = SESSION_ID;
	}

	public String get_USER_NAME() {
		return USER_NAME;
	}

	public void set_USER_NAME(String USER_NAME) {
		this.USER_NAME = USER_NAME;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(String versionInfo) {
		this.versionInfo = versionInfo;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public String getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatusCode(int httpStatusCode, boolean isException) {
		String status = HttpUtil.getHttpStatus(httpStatusCode);
		this.httpStatusCode = httpStatusCode;
		this.httpStatus = isException ? Util.colorRed(status)  : status;
	}
	
	public void setHttpStatus(String httpStatus, boolean isException) {
		this.httpStatus = isException ? Util.colorRed(httpStatus)  : httpStatus;
	}
	
	public void handleException(Exception exception) {
		try {
			RestAgent.responseBody = exception.getMessage();
			throw exception;
			
		}catch(HttpStatusException e) {
			
			int statusCode = e.getStatusCode();
			this.setHttpStatusCode(statusCode, true);
						
			if(e.getMessage() != null) {
				this.setHttpStatus(e.getMessage(), true);
			}else {
				this.setHttpStatusCode(statusCode, true);
			}
			
			return;
			
		}catch(SocketTimeoutException e) {
			this.setHttpStatus("Response Timeout", true);
			return;
			
		}catch(ConnectException e) {
			this.setHttpStatus("MK119 Server is not Running", true);
			return;
			
		}catch (Exception e) {
			this.setHttpStatus("Exception : " + e.getMessage(), true);
			return;
			
		}
	}
	
	
}