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

public class RestAgent {
	
	public static HashMap<Integer, PerfData> getFacilityPerfDataMap(boolean firstTry, int serverIndex, AdminConsole_Info adminConsole){
		HashMap<Integer, PerfData> perfDataMap = new HashMap<Integer, PerfData>();
		
		try {
			String API_URL = String.format("http://%s:%s/midknight/api/perfdata/servers/%d/last", adminConsole.get_IP(), adminConsole.get_PORT(), serverIndex);
			
			Connection connection = Jsoup.connect(API_URL)
					.header("Content-Type", "application/json;charset=UTF-8")
					.header("Cookie", "JSESSIONID=" + adminConsole.get_SESSION_ID())
					.ignoreContentType(true)
					.timeout(0)
					.method(Connection.Method.GET);
			
			Connection.Response response = connection.execute();
			
			adminConsole.setHttpStatusCode(response.statusCode());
			
			Document dom = response.parse();
			
			// 세션이 끊어졌으므로 세션을 재생성하고 재시도한다, 단 한번만 재시도 후 실패시 null 리턴
			if(dom.title().contains("MK119 Login") && firstTry) {
				String newSession = AdminConsole_Info.refreshSession(adminConsole); 												
				if (newSession != null) {						
					return getFacilityPerfDataMap(true, serverIndex, adminConsole);
				}else {
					// 세션 재생성 실패
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
					return null;
				}
			}
			
		}catch(ConnectException e) {
			// MK119 서비스 실행중 아님
			// 한번 세션이 생성되었는데 서비스 재시작하면 안된다?
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return perfDataMap;
	}
	
	
	
}
