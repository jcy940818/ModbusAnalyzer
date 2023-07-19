package common.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SystemSeverity {
	
	public static final String GET_SYSTEM_SEVERITY = "SELECT * FROM SYSTEM_SEVERITY ORDER BY nSeverity ASC";
	
	private int nSeverity;
	private String strSeverity;
	private int nBkColor;
	private int nTextColor;
	
	public SystemSeverity(int nSeverity, String strSeverity, int nBkColor, int nTextColor) {	
		this.nSeverity = nSeverity;
		this.strSeverity = strSeverity;
		this.nBkColor = nBkColor;
		this.nTextColor = nTextColor;
	}
	
	public int getnSeverity() { return nSeverity; }
	public String getStrSeverity() { return strSeverity; }
	public int getnBkColor() { return nBkColor; }
	public int getnTextColor() { return nTextColor; }
	
	public static ArrayList<SystemSeverity> getSystemSeverity(Connection conn) throws SQLException{		
		ArrayList<SystemSeverity> severityList = new ArrayList<SystemSeverity>();

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(GET_SYSTEM_SEVERITY);

		while (rs.next()) {
			int nSeverity = rs.getInt("nSeverity");
			String strSeverity = rs.getString("strSeverity");
			int nBkColor = rs.getInt("nBkColor");
			int nTextColor = rs.getInt("nTextColor");
			severityList.add(new SystemSeverity(nSeverity, strSeverity, nBkColor, nTextColor));
		}

		return severityList;
	}
	
	public static ArrayList<SystemSeverity> getDefaultSystemSeverity(){
		ArrayList<SystemSeverity> severityList = new ArrayList<SystemSeverity>();
		severityList.add(new SystemSeverity(10, "Normal", 8421376, 16777215));
		severityList.add(new SystemSeverity(20, "Warning", 15981329, 0));
		severityList.add(new SystemSeverity(30, "Minor", 15429428, 16777215));
		severityList.add(new SystemSeverity(40, "Critical", 8388608, 16777215));
		severityList.add(new SystemSeverity(50, "Fatal", 0, 16777215));
		return severityList;
	}
	
}
