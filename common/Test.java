package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

import common.server.ServerGroup;

public class Test {
	
	public static void main(String[] args) {
		Connection conn = makeConnection("jdbc:sqlserver://192.168.1.12:1433;databaseName=SK", "sa", "onion2132?");
		try {
			HashMap<Integer, ServerGroup> map = ServerGroup.getServerGroupMap(conn);
			
			Set<Integer> keys = map.keySet();
			int i = 1;
			for(Integer key : keys) {
				ServerGroup group = map.get(key);
				System.out.println((i++) + ". index : " + group.getGroupIndex() + ", Info : " + group.getTree());
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection makeConnection(String url, String id, String pw) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, id, pw);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
}