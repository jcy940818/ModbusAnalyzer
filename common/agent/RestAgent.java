package common.agent;

import java.net.ConnectException;
import java.util.HashMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import src_ko.info.AdminConsole_Info;
import src_ko.swing.LinkMK119Frame;

public class RestAgent {
	
	public static HashMap<Integer, PerfData> getFacilityPerfDataMap(boolean firstTry, int serverIndex, AdminConsole_Info adminConsole){
		HashMap<Integer, PerfData> perfDataMap = new HashMap<Integer, PerfData>();
		String API = String.format("/midknight/api/perfdata/servers/%d/last", serverIndex);
		
		try {
			String API_URL = String.format("http://%s:%s/midknight/api/perfdata/servers/%d/last", adminConsole.get_IP(), adminConsole.get_PORT(), serverIndex);
			
			
			Connection connection = Jsoup.connect(API_URL)
					.header("Content-Type", "application/json;charset=UTF-8")
					.header("Cookie", "JSESSIONID=" + adminConsole.get_SESSION_ID())
					.ignoreContentType(true)
					.timeout(0)
					.method(Connection.Method.GET);
			
			Connection.Response response = connection.execute();
			
			adminConsole.setHttpStatusCode(response.statusCode(), false);
			
			Document dom = response.parse();
			
			// 세션이 끊어졌으므로 세션을 재생성하고 재시도한다, 단 한번만 재시도 후 실패시 null 리턴
			if(dom.title().contains("MK119 Login") && firstTry) {
				String newSession = AdminConsole_Info.refreshSession(adminConsole);
				
				if (newSession != null) {
					// 세션 재생성 성공
					LinkMK119Frame.linkRestAPI(adminConsole, API);
					return getFacilityPerfDataMap(false, serverIndex, adminConsole);
					
				}else {
					// 세션 재생성 실패
					LinkMK119Frame.linkRestAPI(adminConsole, API);
					return null;					
				}
				
			}else {
				// 세션이 끊어지지 않음
				try {
					JSONArray jsonArray = new JSONArray(dom.body().text());
					
					for(int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonOjbect = jsonArray.getJSONObject(i);
						
						int index = jsonOjbect.getInt("idx");
						
						JSONArray values = jsonOjbect.getJSONArray("values");
						String value = values.getJSONObject(0).getString("value");
						long time = values.getJSONObject(0).getLong("time");
						
						PerfData perfData = new PerfData();
						perfData.setIndex(index);
						perfData.setTime(time);
						perfData.setValue(value);
						perfDataMap.put(index, perfData);
					}
				}catch(JSONException e) {
					e.printStackTrace();
					adminConsole.handleException(e);
					LinkMK119Frame.linkRestAPI(adminConsole, API);
					return null;
				}
			}
			
		}catch(ConnectException e) {
			// MK119 서비스 실행중 아님			
			e.printStackTrace();
			adminConsole.handleException(e);
			LinkMK119Frame.linkRestAPI(adminConsole, API);
			return null;
			
		}catch(Exception e) {
			e.printStackTrace();
			adminConsole.handleException(e);
			LinkMK119Frame.linkRestAPI(adminConsole, API);
			return null;
		}
		
		LinkMK119Frame.linkRestAPI(adminConsole, API);
		return perfDataMap;
	}
	
	
}
