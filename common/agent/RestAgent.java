package common.agent;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import src_ko.agent.ModbusFacility;
import src_ko.info.AdminConsole_Info;
import src_ko.swing.LinkMK119Frame;

public class RestAgent {
	
	public static String responseBody;
	
	public static HashMap<Integer, PerfData> getFacilityPerfDataMap(int serverIndex, AdminConsole_Info adminConsole){
		HashMap<Integer, PerfData> perfDataMap = new HashMap<Integer, PerfData>();
		
		String apiContent = String.format("Last Perf Data ( Server index : %d )", serverIndex);
		String apiURL = String.format("/midknight/api/perfdata/servers/%d/last", serverIndex);
		LinkMK119Frame.linkRestAPI(true, adminConsole, apiContent, apiURL);
		
		try {
			String API_URL = String.format("http://%s:%s/midknight/api/perfdata/servers/%d/last", adminConsole.get_IP(), adminConsole.get_PORT(), serverIndex);
			
			Connection connection = Jsoup.connect(API_URL)
					.header("Content-Type", "application/json;charset=UTF-8")
					.header("Cookie", "JSESSIONID=" + adminConsole.get_SESSION_ID())
					.ignoreContentType(true)
					.maxBodySize(0)
					.timeout(0)
					.method(Connection.Method.GET);
			
			Connection.Response response = connection.execute();
			
			adminConsole.setHttpStatusCode(response.statusCode(), false);
			
			Document dom = response.parse();
			responseBody = dom.body().text();
			
			// 세션이 끊어지지 않음
			try {
				JSONArray jsonArray = new JSONArray(responseBody);
				
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
				LinkMK119Frame.linkRestAPI(false, adminConsole, apiContent, apiURL);
				return null;				
			}
			
		}catch(ConnectException e) {
			// MK119 서비스 실행중 아님			
			e.printStackTrace();
			adminConsole.handleException(e);
			LinkMK119Frame.linkRestAPI(false, adminConsole, apiContent, apiURL);
			return null;
			
		}catch(Exception e) {
			e.printStackTrace();
			adminConsole.handleException(e);
			LinkMK119Frame.linkRestAPI(false, adminConsole, apiContent, apiURL);
			return null;
		}
		
		LinkMK119Frame.linkRestAPI(false, adminConsole, apiContent, apiURL);
		return perfDataMap;
	}
	
	
	public static ArrayList<PerfData> getPerfRowData(int perfIndex, AdminConsole_Info adminConsole, String startTime, String endTime){
		ArrayList<PerfData> rowDataList = new ArrayList<PerfData>();
		
		String apiContent = String.format("Perf Row Data ( Perf index : %d )", perfIndex, startTime, endTime);
		String apiURL = String.format("/midknight/api/perfdata/file/%d?datetime_from=%s&datetime_to=%s", perfIndex, startTime, endTime);
		LinkMK119Frame.linkRestAPI(true, adminConsole, apiContent, apiURL);
		
		try {
			String API_URL = String.format("http://%s:%s/midknight/api/perfdata/file/%d?datetime_from=%s&datetime_to=%s", adminConsole.get_IP(), adminConsole.get_PORT(), perfIndex, startTime, endTime);
			
			Connection connection = Jsoup.connect(API_URL)
					.header("Content-Type", "application/json;charset=UTF-8")
					.header("Cookie", "JSESSIONID=" + adminConsole.get_SESSION_ID())
					.ignoreContentType(true)
					.maxBodySize(0)
					.timeout(0)
					.method(Connection.Method.GET);
			
			Connection.Response response = connection.execute();
			
			adminConsole.setHttpStatusCode(response.statusCode(), false);
			
			Document dom = response.parse();
			responseBody = dom.body().text();
			
			// 세션이 끊어지지 않음
			try {
				JSONArray jsonArray = new JSONArray(responseBody);
				
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonOjbect = jsonArray.getJSONObject(i);
											
					String value = jsonOjbect.getString("value");
					long time = jsonOjbect.getLong("time");
					
					PerfData perfData = new PerfData();
					perfData.setIndex(perfIndex);
					perfData.setTime(time);
					perfData.setValue(value);
					rowDataList.add(perfData);
				}
			}catch(JSONException e) {
				e.printStackTrace();
				adminConsole.handleException(e);
				LinkMK119Frame.linkRestAPI(false, adminConsole, apiContent, apiURL);
				return null;
			}
			
		}catch(ConnectException e) {
			// MK119 서비스 실행중 아님			
			e.printStackTrace();
			adminConsole.handleException(e);
			LinkMK119Frame.linkRestAPI(false, adminConsole, apiContent, apiURL);
			return null;
			
		}catch(Exception e) {
			e.printStackTrace();
			adminConsole.handleException(e);
			LinkMK119Frame.linkRestAPI(false, adminConsole, apiContent, apiURL);
			return null;
		}
		
		LinkMK119Frame.linkRestAPI(false, adminConsole, apiContent, apiURL);
		Collections.sort(rowDataList);
		return rowDataList;
	}
	
	public static HashMap<String, ModbusFacility> getFacilityAll(AdminConsole_Info adminConsole){
		HashMap<String, ModbusFacility> facilityMap = null;
		
		try {
			String API_URL = String.format("http://%s:%s/midknight/api/metadata/servers", adminConsole.get_IP(), adminConsole.get_PORT());
			
			Connection connection = Jsoup.connect(API_URL)
					.header("Content-Type", "application/x-www-form-urlencoded;charset:utf-8")
					.header("Cookie", "JSESSIONID=" + adminConsole.get_SESSION_ID())
					.ignoreContentType(true)
					.maxBodySize(0)
					.timeout(0)
					.method(Connection.Method.GET);
			
			Connection.Response response = connection.execute();
			
			adminConsole.setHttpStatusCode(response.statusCode(), false);
			
			Document dom = response.parse();
			responseBody = dom.body().text();
			
			// 세션이 끊어지지 않음
			try {
				facilityMap = new HashMap<String, ModbusFacility>();
				JSONArray jsonArray = new JSONArray(responseBody);
				
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonOjbect = jsonArray.getJSONObject(i);
											
					if(jsonOjbect.getInt("type") == 16) {
						// 장비 타입이 시설물인 경우만 수행
						ModbusFacility fac = new ModbusFacility();
						fac.setnServerIndex(jsonOjbect.getInt("idx"));
						fac.setStrServerName(jsonOjbect.getString("name"));
						fac.setFacilityType(jsonOjbect.getInt("subType"));
						facilityMap.put(fac.getStrServerName(), fac);
					}else {
						// 장비가 시설물이 아닌 경우 ( RCU 등 )
						continue;
					}
					
				}
			}catch(JSONException e) {
				e.printStackTrace();
				return null;
			}
			
		}catch(ConnectException e) {
			// MK119 서비스 실행중 아님			
			e.printStackTrace();
			return null;
			
		}catch(Exception e) {
			e.printStackTrace();			
			return null;
		}
				
		return facilityMap;
	}
	
}
