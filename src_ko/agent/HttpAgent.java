package src_ko.agent;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import src_ko.info.AdminConsole_Info;
import src_ko.swing.LinkMK119Frame;
import src_ko.swing.ModbusCollectionFrame;
import src_ko.util.Util;

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
				LinkMK119Frame.linkRestAPI(admin, "/midknight/adminConsole");
				return sessionID;
				
			}else {
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%s%s\n", Util.colorRed("MK119 Login Failure"), Util.separator));
				sb.append(String.format("%s:%s MK119 AdminConsole 로그인에 실패하였습니다%s\n", IP, PORT, Util.separator));
				sb.append("\n아래 내용을 확인해주세요\n\n");
				sb.append(String.format("1. <font color='blue'>%s:%s 서버</font> : %s%s\n", IP, PORT,Util.colorRed("MK119 서비스 실행 여부") ,Util.separator));
				sb.append(String.format("2. <font color='blue'>AdminConsole 로그인 정보</font> : ID( %s ) / PW( %s )%s\n", Util.colorRed(ID), Util.colorRed(PW), Util.separator));	
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				
				admin.set_SESSION_ID(sessionID);
				admin.setHttpStatusCode(0, true);
				LinkMK119Frame.linkRestAPI(admin, "/midknight/adminConsole");
				return null;
			}
		}catch(Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s%s\n", Util.colorRed("MK119 Login Failure"), Util.separator));
			sb.append(String.format("%s:%s MK119 AdminConsole 로그인에 실패하였습니다%s\n", IP, PORT, Util.separator));
			sb.append("\n아래 내용을 확인해주세요\n\n");
			sb.append(String.format("1. <font color='blue'>%s:%s 서버</font> : %s%s\n", IP, PORT,Util.colorRed("MK119 서비스 실행 여부") ,Util.separator));
			sb.append(String.format("2. <font color='blue'>AdminConsole 로그인 정보</font> : ID( %s ) / PW( %s )%s\n", Util.colorRed(ID), Util.colorRed(PW), Util.separator));	
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			
			admin.set_SESSION_ID(sessionID);
			admin.handleException(e);
			LinkMK119Frame.linkRestAPI(admin, "/midknight/adminConsole");
			return null;
		}
	}
		
	
	public void addModbusPerfs(AdminConsole_Info adminConsole, ModbusFacility server, Perf[] perfs, boolean useAutoEvent) {
		
//		System.out.println(ModbusPerf.getTotalJSON(perfs));		
		
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
			
			Document page = resultPage.parse();									
			
			if(page.title().contains(server.getStrServerName())) {
				
				if(isSuccess(page, perfs)) {
					// 성능 추가 성공 로직
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("<font color='green'>Successfully MK119 Add Watch Point</font>%s%s\n", Util.separator, Util.separator));
					sb.append(String.format("시설물 종류 : %s%s%s\n", Util.colorBlue(server.getFACILITY_TYPE_String()), Util.separator, Util.separator));
					sb.append(String.format("장비명 : %s%s%s\n\n", Util.colorBlue(server.getStrServerName()), Util.separator, Util.separator));
					sb.append(String.format("위의 장비에 성공적으로 %s개 성능 항목 추가 완료하였습니다%s%s\n", Util.colorBlue(String.valueOf(perfs.length)), Util.separator, Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
					ModbusCollectionFrame.isMK119Adding = false;
					return;
				}else {					
					// 성능 추가에는 성공하였지만 너무 많은 성능 리스트때문에 결과 페이지의 소스가 잘렸을 경우					
					StringBuilder sb = new StringBuilder();				
					sb.append("<font color='red'>Result Unknown MK119 Tasks are Complete</font>\n");
					sb.append(String.format("MK119 성능 추가 작업의 결과를 알 수 없습니다%s\n\n", Util.separator));						
					sb.append(String.format("( %s )%s\n", Util.colorBlue("해당 메시지가 성능 추가 작업의 실패를 의미하지는 않습니다") ,Util.separator));
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					ModbusCollectionFrame.isMK119Adding = false;
					return;
										
					// 세션이 끊어지지는 않았지만 성능 추가에 실패 하였을 경우 (JSON Exception 등)
//					StringBuilder sb = new StringBuilder();
//					sb.append("<font color='red'>Failed to MK119 Add Watch Point</font>\n");
//					sb.append(String.format("알 수 없는 이유로 %s 장비의 성능 추가에 실패하였습니다%s\n", Util.colorBlue(server.getStrServerName()), Util.separator));					
//					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
//					ModbusCollectionFrame.isMK119Adding = false;
//					return;
				}
			}else if(page.title().contains("MK119 Login")){
				// 성능 추가 실패 로직 : 세션 끊김
				StringBuilder sb = new StringBuilder();				
				sb.append("<font color='red'>Failed to MK119 Add Watch Point</font>\n");
				sb.append(String.format("%s AdminConsole [ ID : %s ] 세션이 종료되었습니다%s\n\n", Util.colorBlue(adminConsole.get_IP() + ":" + adminConsole.get_PORT()), Util.colorBlue(adminConsole.get_ID()) ,Util.separator));
				sb.append(String.format("마지막 로그인 정보를 이용하여 세션을 재생성하고%s\n\n",Util.separator));
				sb.append(String.format("시설물 종류 : %s%s%s\n", Util.colorBlue(server.getFACILITY_TYPE_String()), Util.separator, Util.separator));
				sb.append(String.format("장비명 : %s%s%s\n\n", Util.colorBlue(server.getStrServerName()), Util.separator, Util.separator));
				sb.append(String.format("위의 장비에 성능 추가 작업을 이어서 하시겠습니까?%s%s\n",Util.separator, Util.separator));
				
				int userOption= Util.showConfirm(sb.toString());
				
				if(userOption == JOptionPane.YES_OPTION) {										
					// 마지막 로그인 정보를 이용하여 성능 추가 재요청
					String newSession = AdminConsole_Info.refreshSession(adminConsole); 												
					
					if (newSession != null) {						
						new HttpAgent().addModbusPerfs(adminConsole, server, perfs, useAutoEvent);
					}else {
						sb = new StringBuilder();
						sb.append(String.format("<font color='red'>Failed to MK119 Create New Session</font>%s\n", Util.separator));
						sb.append(String.format("MK119 AdminConsole 세션 재생성에 실패하여 작업을 종료합니다%s\n", Util.separator));											
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						ModbusCollectionFrame.isMK119Adding = false;
						return;
					}
							
				}else {
					// 성능 추가 취소
					sb = new StringBuilder();
					sb.append(String.format("<font color='red'>Cancel MK119 Add Watch Point</font>%s\n", Util.separator));
					sb.append(String.format("MK119 성능 추가 작업을 취소하였습니다%s\n", Util.separator));											
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					ModbusCollectionFrame.isMK119Adding = false;
					return;
				}
								
			}else {
				StringBuilder sb = new StringBuilder();				
				sb.append("<font color='red'>Failed to MK119 Add Watch Point</font>\n");
				sb.append(String.format("알 수 없는 이유로 성능 추가에 실패하였습니다%s\n", Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				ModbusCollectionFrame.isMK119Adding = false;
				return;
			}		
		}catch(Exception e) {
			// 성능 추가 예외 로직
			e.printStackTrace();
			StringBuilder sb = new StringBuilder();				
			sb.append("<font color='red'>Failed to MK119 Add Watch Point</font>\n");
			sb.append(String.format("처리 할 수 없는 예외 발생으로 인하여 성능 추가에 실패하였습니다%s\n\n", Util.separator));
			sb.append(String.format("Exception Message : %s%s\n",e.getMessage() ,Util.separator));
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			ModbusCollectionFrame.isMK119Adding = false;
			return;
			
		}finally {
			ModbusCollectionFrame.isMK119Adding = false;
		}				
	}
	
	public boolean toMuchWatchPoints(Document page) {
		try {
			if(page.text().contains("...")) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			return false;
		}
	}
	
	public boolean isSuccess(Document page, Perf[] perfs) {				
		Element resultTable = page.selectFirst("table.result");
		Elements tableRows = resultTable.select("tr.sub");
		List collections = new ArrayList();
		List perfNames = new ArrayList();  
		
		// ModbusCollection 성능명 리스트	
		for(int i = 0; i < perfs.length; i++) {
			collections.add(perfs[i].getDisplayName());
		}
		
		// MK119 장비에 등록된 성능명 리스트
		for(Element e : tableRows) {
			perfNames.add(e.selectFirst("td.result_sub").nextElementSibling().text());
		}
						
		// MK119 장비에 ModbusCollection 성능이 모두 추가 되었는지 검사 결과 리턴
		return perfNames.containsAll(collections);		 
	}
	
}
