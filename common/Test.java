package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import src_ko.util.Util;

public class Test {
	
	public static void main(String[] args) {
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