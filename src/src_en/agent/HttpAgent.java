package src_en.agent;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import common.agent.RestAgent;
import common.web.AdminConsole_Info;
import src_en.swing.ModbusCollectionFrame;
import src_en.util.Util;

public class HttpAgent {	
	
	public static final int REQUEST_TIME_OUT = 5000;
	
	private static Map<String, String> webCookies;
	private String sessionID;
		
	/**
	 * MK119 AdminConsole 로그인에  성공하면 sesssionID를, 실패하면 null을 리턴
	 */
	public String getMK119SessionId(AdminConsole_Info admin) throws IOException, ConnectException, SocketTimeoutException{
		
		String IP = admin.get_IP();
		String PORT = admin.get_PORT();
		String ID = admin.get_ID();
		String PW = admin.get_PW();
		
		try {			
			String adminConsole = String.format("http://%s:%s/midknight/adminConsole", IP, PORT);
			
			Connection.Response loginForm = Jsoup.connect(adminConsole)
					.header("Connection", "keep-alive")
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
					.header("Accept-Charset", "utf-8")
					.header("Content-Type", "application/x-www-form-urlencoded")
					.data("cat", "noSession")
					.data("cmd", "login")
					.data("id", ID)
					.data("pw", PW)					
	//				.cookie("key", "value")
					.method(Connection.Method.POST)
					.timeout(REQUEST_TIME_OUT)
					.execute();
			
			Document page = loginForm.parse();
			
			if(page.title().contains("Admin Console")) {
				System.out.println(String.format("[ %s:%s AdminConsole 로그인 성공 : ID(%s) / PW(%s) ]",IP, PORT, ID, PW));
				
				webCookies = loginForm.cookies();
				
				Set keys = webCookies.keySet();
				Iterator it = keys.iterator();
	
				while (it.hasNext()) {
					String key = (String) it.next();
					if(key.equalsIgnoreCase("JSESSIONID")) {
						sessionID = webCookies.get(key);
						System.out.println(String.format("[ %s:%s MK119 JSESSION : %s ]", IP, PORT, sessionID));									
					}
				}
				
				admin.set_SESSION_ID(sessionID);
				admin.setHttpStatusCode(loginForm.statusCode(), false);				
				return sessionID;
				
			}else {
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%s%s\n", Util.colorRed("MK119 Login Failure"), Util.separator));
				sb.append(String.format("Failed to Login to %s:%s MK119 AdminConsole%s\n", IP, PORT, Util.separator));
				sb.append("\nPlease check the contents below\n\n");
				sb.append(String.format("1. <font color='blue'>%s:%s Server</font> : %s%s\n", IP, PORT,Util.colorRed("MK119 service is executed or not") ,Util.separator));
				sb.append(String.format("2. <font color='blue'>AdminConsole Login information</font> : ID( %s ) / PW( %s )%s\n", Util.colorRed(ID), Util.colorRed(PW), Util.separator));	
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				admin.set_SESSION_ID(sessionID);
				admin.setHttpStatusCode(0, true);				
				return null;
			}
		}catch(Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s%s\n", Util.colorRed("MK119 Login Failure"), Util.separator));
			sb.append(String.format("Failed to Login to %s:%s MK119 AdminConsole%s\n", IP, PORT, Util.separator));
			sb.append("\nPlease check the contents below\n\n");
			sb.append(String.format("1. <font color='blue'>%s:%s Server</font> : %s%s\n", IP, PORT,Util.colorRed("MK119 service is executed or not") ,Util.separator));
			sb.append(String.format("2. <font color='blue'>AdminConsole Login information</font> : ID( %s ) / PW( %s )%s\n", Util.colorRed(ID), Util.colorRed(PW), Util.separator));	
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			
			admin.set_SESSION_ID(sessionID);
			admin.handleException(e);
			return null;
		}
	}
	
	
	public void addModbusPerfs(AdminConsole_Info adminConsole, ModbusFacility server, Perf[] perfs, boolean useAutoEvent) {
		
		if(perfs == null || perfs.length < 1) return;
		
		try {
			String adminConsoleURL = String.format("http://%s:%s/midknight/adminConsole", adminConsole.get_IP(), adminConsole.get_PORT());
			
			Connection addPerfForm = Jsoup.connect(adminConsoleURL)
					.header("Content-Type", "application/x-www-form-urlencoded")					
					.header("Connection", "keep-alive")
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
					.header("Cookie", "JSESSIONID=" + adminConsole.get_SESSION_ID())
					.data("cat", "performance")
					.data("cmd", "addPerf")
					.data("index", String.valueOf(server.getnServerIndex()))
					.data("perfType", "15")
					.data("name", server.getStrServerName())
					.data("agent", "16")
					.data("sort", "item")
					.data("order", "ASC")
					.data("counterStr", "")
					.data("oneMoreAdd", "false")
					.data("appObjectChange", "")
					.data("rtuIndex", "")
					.data("addMethod", "1")
					.data("oid", "")
					.data("perfInterval", "60")		
					.timeout(0) // 타임아웃 없음
					.method(Connection.Method.POST);
					
			
			// MK119 버전에 따라 인코딩 방식이 다르다
			double adminConsoleVersion = Double.parseDouble(adminConsole.getVersion());
			
			if(adminConsoleVersion <= 4.2) {
				addPerfForm.postDataCharset("EUC-KR");
			}else if(adminConsoleVersion > 4.2) {
				addPerfForm.postDataCharset("UTF-8");
			}else {
				addPerfForm.postDataCharset("UTF-8");
			}
			
			// 성능, 이벤트 정보 파라미터 초기화
			for(int i = 0; i < perfs.length; i++) {
				addPerfForm.data("perfCounter", perfs[i].getJsonString());				
			}
			
			Connection.Response resultPage = addPerfForm.execute();
				
			if(getAddPerfResult(adminConsole, server, perfs)) {
				// 성능 추가 성공 로직
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("<font color='green'>Successfully MK119 Add Watch Point</font>%s%s\n", Util.separator, Util.separator));
				sb.append(String.format("Facility Type : %s%s%s\n", Util.colorBlue(server.getFACILITY_TYPE_String()), Util.separator, Util.separator));
				sb.append(String.format("Device Name : %s%s%s\n\n", Util.colorBlue(server.getStrServerName()), Util.separator, Util.separator));
				sb.append(String.format("Successfully added %s Performances to the Device%s%s\n", Util.colorBlue(String.valueOf(perfs.length)), Util.separator, Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
				ModbusCollectionFrame.isMK119Adding = false;
				return;
				
			}else if(resultPage.parse().title().contains("MK119 Login")){
				// 성능 추가 실패 로직 : 세션 끊김
				StringBuilder sb = new StringBuilder();				
				sb.append("<font color='red'>Failed to MK119 Add Watch Point</font>\n");
				sb.append(String.format("%s AdminConsole [ ID : %s ] The session is over%s\n\n", Util.colorBlue(adminConsole.get_IP() + ":" + adminConsole.get_PORT()), Util.colorBlue(adminConsole.get_ID()) ,Util.separator));
				sb.append(String.format("Would you like to recreate the session using the last login information%s%s\n\n",Util.separator, Util.separator));
				sb.append(String.format("and then add performance to the device below?%s%s\n\n",Util.separator, Util.separator));
				sb.append(String.format("Facility Type : %s%s%s\n", Util.colorBlue(server.getFACILITY_TYPE_String()), Util.separator, Util.separator));
				sb.append(String.format("Device Name : %s%s%s\n", Util.colorBlue(server.getStrServerName()), Util.separator, Util.separator));
				
				int userOption= Util.showConfirm(sb.toString());
				
				if(userOption == JOptionPane.YES_OPTION) {										
					// 마지막 로그인 정보를 이용하여 성능 추가 재요청
					String newSession = AdminConsole_Info.refreshSession(adminConsole); 												
					
					if (newSession != null) {						
						new HttpAgent().addModbusPerfs(adminConsole, server, perfs, useAutoEvent);
					}else {
						sb = new StringBuilder();
						sb.append(String.format("<font color='red'>Failed to MK119 Create New Session</font>%s\n", Util.separator));
						sb.append(String.format("MK119 AdminConsole session regeneration failed, so I'm ending the task%s\n", Util.separator));											
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						ModbusCollectionFrame.isMK119Adding = false;
						return;
					}
							
				}else {
					// 성능 추가 취소
					sb = new StringBuilder();
					sb.append(String.format("<font color='red'>Cancel MK119 Add Watch Point</font>%s\n", Util.separator));
					sb.append(String.format("Adding MK119 performance has been canceled%s\n", Util.separator));											
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					ModbusCollectionFrame.isMK119Adding = false;
					return;
				}
								
			}else {
				StringBuilder sb = new StringBuilder();				
				sb.append("<font color='green'>Result Unknown MK119 Tasks are Complete</font>\n");
				sb.append(String.format("I don't know the result of adding MK119 performance%s\n\n", Util.separator));						
				sb.append(String.format("( %s )%s\n", Util.colorBlue("The message does not mean that the operation fails") ,Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.QUESTION_MESSAGE);
				ModbusCollectionFrame.isMK119Adding = false;
				return;
			}
		}catch(Exception e) {
			// 성능 추가 예외 로직
			e.printStackTrace();
			StringBuilder sb = new StringBuilder();				
			sb.append("<font color='red'>Failed to MK119 Add Watch Point</font>\n");
			sb.append(String.format("Operation failed due to an unprocessable exception%s\n\n", Util.separator));
			sb.append(String.format("Exception Message : %s%s\n",e.getMessage() ,Util.separator));
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			ModbusCollectionFrame.isMK119Adding = false;
			return;
			
		}finally {
			ModbusCollectionFrame.isMK119Adding = false;
		}				
	}
		
	public boolean getAddPerfResult(AdminConsole_Info adminConsole, ModbusFacility facility, Perf[] perfs) throws JSONException{				
		try {
			boolean result = true;
			JSONObject serverAndPerf = RestAgent.getServerAndPerf(adminConsole, facility.getnServerIndex());
			
			int serverIndex = serverAndPerf.getJSONObject("serverInfo").getInt("idx");
			
			if(facility.getnServerIndex() == serverIndex) {
				JSONArray serverPerfs = serverAndPerf.getJSONArray("perfs");
				
				HashMap<String, Perf> perfMap = new HashMap();
				
				for(int i = 0; i < serverPerfs.length(); i++) {
					JSONObject serverPerf = (JSONObject)serverPerfs.get(i);
					Perf perf = new Perf();
					perf.setIndex(serverPerf.getInt("idx"));
					perf.setDisplayName(serverPerf.getString("name"));
					perf.setMeasure(serverPerf.getString("measure"));
					perf.setDataFormat(serverPerf.getString("format"));
					perfMap.put(perf.getDisplayName(), perf);
				}
				
				for(Perf checkPerf : perfs) {					
					
					if(checkPerf.getDisplayName().startsWith("{") && checkPerf.getDisplayName().contains("}")) {
						String perfName = checkPerf.getDisplayName();
						checkPerf.setDisplayName(perfName.split("}")[1]);
					}
					
					if(perfMap.containsKey(checkPerf.getDisplayName())) {
						Perf serverPerf = perfMap.get(checkPerf.getDisplayName());												
						
						result = result
							&& checkPerf.getDisplayName().equals(serverPerf.getDisplayName())
							&& checkPerf.getMeasure().equals(serverPerf.getMeasure())
							&& checkPerf.getDataFormat().equals(serverPerf.getDataFormat());
						
						if(!result) return false;
						
					}else {
						return false;
					}
				}
				
				return result;
				
			}else {
				return false;
			}
		
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}		
	}
	
}
