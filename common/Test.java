package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JOptionPane;

import common.server.ServerGroup;
import src_ko.util.Util;

public class Test {
	
	public static void main(String[] args) {
//		Connection conn = makeConnection("jdbc:sqlserver://192.168.1.12:1433;databaseName=SK", "sa", "onion2132?");
		
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