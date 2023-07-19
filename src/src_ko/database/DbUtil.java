package src_ko.database;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import common.util.FontManager;
import src_ko.info.ONION_Info;
import src_ko.main.MoonInspector;
import src_ko.swing.MK119_Login_Panel;
import src_ko.swing.MessageFrame;
import src_ko.swing.SqlResultFrame;
import src_ko.util.Util;

public class DbUtil {
	
	public static final String FACILITY = "FROM SERVERINFO si INNER JOIN SERVERINFO_FACILITY fac ON si.nServerIndex = fac.NODE_INDEX"; // �ü��� ���̺� ����
	public static final String RCU = "FROM SERVERINFO si INNER JOIN SERVERINFO_RTU rtu ON si.nServerIndex = rtu.NODE_INDEX"; // RCU ���̺� ����
	public static final String PERF = "FROM SERVER_PERF"; // ���� ���̺�
	public static final String ALARM = "FROM ALARM"; // �̺�Ʈ(�˶�) ���̺�
	public static final String EVENTS = "FROM EVENTS"; // �̺�Ʈ ���� ���̺�
	
	// ����
	private static String Query;
	private static String SELECT = ""; // SELECT ��
	private static String FROM = ""; // FROM ��
	private static String WHERE = ""; // WHERE ��
	private static String ORDER_BY = ""; // ORDRY BY ��
	
	private static boolean SqlRunning = false;
	
	public static String getMK119Version() {
		String VersionInfoQuery = "SELECT PRODUCT_VERSION_MAJOR, PRODUCT_VERSION_MINOR, PRODUCT_VERSION_BUILD FROM GLOBALINFO";
		Statement stmt;
		String versionInfo = null;
		try {
			if(ONION_Info.hasMk119Connection()) {
				
				stmt = ONION_Info.getMk119Connection().createStatement();				
				ResultSet rs = stmt.executeQuery(VersionInfoQuery);
				
				if(rs != null) {					
					while(rs.next()) {
						String major = rs.getString("PRODUCT_VERSION_MAJOR");
						String minor = rs.getString("PRODUCT_VERSION_MINOR");
						String build = rs.getString("PRODUCT_VERSION_BUILD");
						
						versionInfo = String.format("%s.%s",major, minor);						
					}
					return versionInfo;
				}else {
					if(stmt != null)stmt.close();
					return null;
				}								
			}else {
				return null;
			}
		}catch(Exception e) {			
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getMK119Build() {
		String VersionInfoQuery = "SELECT PRODUCT_VERSION_BUILD FROM GLOBALINFO";
		Statement stmt;
		String buildInfo = null;
		try {
			if(ONION_Info.hasMk119Connection()) {
				
				stmt = ONION_Info.getMk119Connection().createStatement();				
				ResultSet rs = stmt.executeQuery(VersionInfoQuery);
				
				if(rs != null) {					
					while(rs.next()) {
				
						String build = rs.getString("PRODUCT_VERSION_BUILD");
						
						buildInfo = String.format("%s", build);						
					}
					return buildInfo;
				}else {
					if(stmt != null)stmt.close();
					return null;
				}								
			}else {
				return null;
			}
		}catch(Exception e) {			
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static String getMK119VersionInfo() {
		String VersionInfoQuery = "SELECT PRODUCT_VERSION_MAJOR, PRODUCT_VERSION_MINOR, PRODUCT_VERSION_BUILD FROM GLOBALINFO";
		Statement stmt;
		String versionInfo = null;
		try {
			if(ONION_Info.hasMk119Connection()) {
				
				stmt = ONION_Info.getMk119Connection().createStatement();				
				ResultSet rs = stmt.executeQuery(VersionInfoQuery);
				
				if(rs != null) {					
					while(rs.next()) {
						String major = rs.getString("PRODUCT_VERSION_MAJOR");
						String minor = rs.getString("PRODUCT_VERSION_MINOR");
						String build = rs.getString("PRODUCT_VERSION_BUILD");
						
						versionInfo = String.format("MK119 %s.%s Build%s",major, minor, build);						
					}
					return versionInfo;
				}else {
					if(stmt != null)stmt.close();
					return null;
				}								
			}else {
				return null;
			}
		}catch(Exception e) {			
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean getCurrentSqlRuningState() {
		return SqlRunning;
	}
	
	public static void startSQL() {
		SqlRunning = true;
	};
	
	public static void endSQL() {	
		SqlRunning = false;
	};
	
	public static void setQuery() {
		DbUtil.Query = String.format("%s %s %s %s", DbUtil.getSELECT(), DbUtil.getFROM(), DbUtil.getWHERE(), DbUtil.getORDER_BY());		
	}
	
	public static String getQuery() {
		return DbUtil.removeSpace(DbUtil.Query);
	}
	
	public static String getQueryDetail() {
		StringBuilder detail = new StringBuilder();
		detail.append(String.format("-- 1. ��ȸ �׸�(�÷�) ����%s", System.lineSeparator()));
		detail.append(String.format("%s%s%s", DbUtil.removeSpace(DbUtil.getSELECT()), System.lineSeparator(), System.lineSeparator()));
		
		detail.append(String.format("-- 2. �˻� ���̺� ����%s", System.lineSeparator()));
		detail.append(String.format("%s%s%s", DbUtil.removeSpace(DbUtil.getFROM()), System.lineSeparator(), System.lineSeparator()));
		
		
		if(DbUtil.removeSpace(DbUtil.getWHERE()).length() < 1) {
			detail.append(String.format("%s%s%s", "-- 3. ������ �˻� ������ �����ϴ�", System.lineSeparator(), System.lineSeparator()));
		}else {
			detail.append(String.format("-- 3. �˻� ���� ����%s", System.lineSeparator()));
			detail.append(String.format("%s%s%s", DbUtil.removeSpace(DbUtil.getWHERE()), System.lineSeparator(), System.lineSeparator()));	
		}
				
		
		if(DbUtil.removeSpace(DbUtil.getORDER_BY()).length() < 1) {
			detail.append(String.format("%s%s%s", "-- 4. ������ ���� ������ �����ϴ�", System.lineSeparator(), System.lineSeparator()));
		}else {
			detail.append(String.format("-- 4. ���� ���� ����%s", System.lineSeparator()));
			detail.append(String.format("%s%s%s", DbUtil.removeSpace(DbUtil.getORDER_BY()), System.lineSeparator(), System.lineSeparator()));
		}
		
		return detail.toString();
	}
	
	
	public static String getSELECT() {
		return DbUtil.SELECT;
	}

	public static void setSELECT(String SELECT) {
		DbUtil.SELECT = SELECT;
		DbUtil.setQuery();
	}

	public static String getFROM() {
		return DbUtil.FROM;
	}

	public static void setFROM(String FROM) {
		DbUtil.FROM = FROM;
		DbUtil.setQuery();
	}

	public static String getWHERE() {
		return DbUtil.WHERE;
	}

	public static void setWHERE(String WHERE) {
		// ������ ������ �����ϴ��� Ȯ�� �� �ʱ�ȭ
		if(WHERE.length() < 1 || WHERE == null) {
			DbUtil.WHERE = "";
		}else {
			DbUtil.WHERE = "WHERE " + WHERE;	
		}
		DbUtil.setQuery();
	}

	public static String getORDER_BY() {
		return DbUtil.ORDER_BY;
	}

	public static void setORDER_BY(String ORDER_BY) {
		DbUtil.ORDER_BY = ORDER_BY;
		DbUtil.setQuery();
	}

	public static boolean close(ResultSet rs, Statement stmt) throws SQLException{			
		try {
			rs.close();
			stmt.close();
			
			// ResultSet, Statement �ν��Ͻ��� ���������� �����ٸ� true ����
			return (rs.isClosed() && stmt.isClosed()) ? true : false; 						
			
		}catch(Exception e) {
			System.out.println("[ DbUtil.close() : ResultSet, Statement �ν��Ͻ��� �ݴ� �� ���ܰ� �߻��Ͽ����ϴ� : " + e.getMessage() + " ]");
			return false;
		}finally {
			rs = null;
			stmt = null;			
		}		
	}	
	
	public static String removeSpace(String query) {
		
		String space1 = " ";
		String space2 = "  ";
		String space3 = "   ";
		String space4 = "    ";
		String space5 = "     ";
		
		if(query.contains(space5)) {
			query = query.replaceAll(space5, space1);
		}else if(query.contains(space4)) {
			query = query.replaceAll(space4, space1);
		}else if(query.contains(space3)) {
			query = query.replaceAll(space3, space1);
		}else if(query.contains(space2)) {
			query = query.replaceAll(space2, space1);
		}
	
		return query;
	}
	
	// ���� ���� �� ��� ���̺� ������ ǥ��
	public static void executeQuery(String sqlServerInfo, String query) {
		if(DbUtil.getCurrentSqlRuningState()) {
			Util.showMessage("<font color='red'>SQL Exception</font>\n���� �������� Query �۾��� �����մϴ�" + Util.separator + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!checkQuery(query)) return;
				
		try {			
			Statement stmt = ONION_Info.getMk119Connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// SQL �۾� ������ �˸���.
			DbUtil.startSQL();
			
			ResultSet rs = stmt.executeQuery(query);
			ONION_Info.setDBResultSet(rs);
					
			// ��� ���̺��� �� ������ 0���̸� ���� ���� ������ Ȯ���ϴ��� ����ڿ��� ����� �޽��� ��� �� ����
			if(DbUtil.getRowCount(rs) < 1) {				
				DbUtil.endSQL();				
				String queryDetail = DbUtil.getQueryDetail();				
				StringBuilder msg = new StringBuilder(String.format("<font color='red'>��ȸ ��� ����</font>\n������ ���������� �����Ͽ����� ��� ���̺� ��ȸ �� �����Ͱ� �����ϴ�%s\n\nTable Row Count(���̺� �� ����) : 0\n","&nbsp;&nbsp;&nbsp;"));
				msg.append("\n<font color ='blue'>����� ���� ������ Ȯ�� �Ͻðڽ��ϱ�?</font>\n");			
				
				int userOption = Util.showConfirm(msg.toString());
				
				if(userOption == JOptionPane.YES_OPTION) {
					new MessageFrame(String.format("<html><font color='blue'>%s</font> ���� ���� Ȯ��</html>", sqlServerInfo), queryDetail);
					return; // ��� ���̺� �������� �������� �ʰ� ����
				}else {
					return; // ��� ���̺� �������� �������� �ʰ� ����
				}
			}
			
			// �ݵ�� ResultSet �ν��Ͻ��� �ʱ�ȭ�� ���� �̷���� �� SqlResultFrame�� �ʱ�ȭ �ؾ��ϱ� ������ �����带 ������� �ʾҴ�.
			String queryDetail = DbUtil.getQueryDetail();
			new SqlResultFrame(sqlServerInfo, queryDetail, rs , "databaseAccess");
			
		}catch(Exception exception) {
			exception.printStackTrace();
			Util.showMessage("<font color='red'>SQL Exception</font>\n" + exception.getMessage() + Util.longSeparator +"\n", JOptionPane.ERROR_MESSAGE);
			System.out.println("\n[ DbUtil.executeQuery() : ���� ���� ���� ]");
			DbUtil.endSQL();		
		}
	}	
	
	
	
	
	// ���� ���� �� ��� ���̺� ������ ǥ��
	public static void executeProcedure(String sqlServerInfo, String query) {
		if(DbUtil.getCurrentSqlRuningState()) {
			Util.showMessage("<font color='red'>SQL Exception</font>\n���� �������� Query �۾��� �����մϴ�" + Util.separator + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!checkQuery(query)) return;
				
		try {			
			Statement stmt = ONION_Info.getMk119Connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// SQL �۾� ������ �˸���.
			DbUtil.startSQL();
			
			ResultSet rs = stmt.executeQuery(query);
			ONION_Info.setDBResultSet(rs);
				
			// ��� ���̺��� �� ������ 0���̸� ���� ���� ������ Ȯ���ϴ��� ����ڿ��� ����� �޽��� ��� �� ����
			if(DbUtil.getRowCount(rs) < 1) {
				DbUtil.endSQL();				
				String queryDetail = query;				
				StringBuilder msg = new StringBuilder(String.format("<font color='red'>��ȸ ��� ����</font>\n������ ���������� �����Ͽ����� ��� ���̺� ��ȸ �� �����Ͱ� �����ϴ�%s\n\nTable Row Count(���̺� �� ����) : 0\n","&nbsp;&nbsp;&nbsp;"));
				msg.append("\n<font color ='blue'>����� ���� ������ Ȯ�� �Ͻðڽ��ϱ�?</font>\n");			
				
				int userOption = Util.showConfirm(msg.toString());
				
				if(userOption == JOptionPane.YES_OPTION) {
					new MessageFrame(String.format("<html><font color='blue'>%s</font> ���� ���� Ȯ��</html>", sqlServerInfo), queryDetail);
					return; // ��� ���̺� �������� �������� �ʰ� ����
				}else {
					return; // ��� ���̺� �������� �������� �ʰ� ����
				}
			}
			
			// �ݵ�� ResultSet �ν��Ͻ��� �ʱ�ȭ�� ���� �̷���� �� SqlResultFrame�� �ʱ�ȭ �ؾ��ϱ� ������ �����带 ������� �ʾҴ�.
			String queryDetail = query;
			new SqlResultFrame(sqlServerInfo, queryDetail, rs, "storedProcedure");
			
		}catch(Exception exception) {
			exception.printStackTrace();
			Util.showMessage("<font color='red'>SQL Exception</font>\n" + exception.getMessage() + Util.longSeparator +"\n", JOptionPane.ERROR_MESSAGE);
			System.out.println("\n[ DbUtil.executeQuery() : ���� ���� ���� ]");
			DbUtil.endSQL();		
		}
	}
	
	
	
	
	
	
	public static boolean checkQuery(String query) {		
		
		if(MoonInspector.isMoon()) return true;
		
		query = query.toUpperCase();
		
		String msg = String.format("%s��û�Ͻ� %s ������ Ȯ�����ּ���%s\n",Util.colorRed("SQL Exception\n"), Util.colorBlue("Query") , Util.separator);
		
		if ((query.length() == 0) || query.equalsIgnoreCase("")) {
			Util.showMessage(msg, JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%s\n", Util.colorRed("SQL Warning"), Util.longSeparator));
				
		String[] strs = query.split(" ");
		
		// switch ������ �Ϸ��� �Ͽ�����, case �������� ��� �񱳸� �� ���� �ƴ϶�
		// String.equlasIgnoreCase()�� ���� ���ڿ��� ���ϱ� ���Ͽ� if-else �� ���
		// ������ ��,�� ���� ���о��� ����ϴ� ����ڰ� ���� �� �ֱ� ������
		
		for (String str : strs) {
			if (str.equalsIgnoreCase("CREATE") || query.contains("CREATE")) {
				sb.append(String.format("��û�Ͻ� ���� ���뿡 %s ����� ���ԵǾ� �ֽ��ϴ�%s\n\n", Util.colorRed("CREATE ( ���� )"), Util.longSeparator));
				sb.append(String.format("���� %s ��ɸ� ���� �� �� �ֽ��ϴ�%s\n", Util.colorBlue("SELECT ( ��ȸ )"), Util.longSeparator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return false;
			} else if (str.equalsIgnoreCase("INSERT") || query.contains("INSERT")) {
				sb.append(String.format("��û�Ͻ� ���� ���뿡 %s ����� ���ԵǾ� �ֽ��ϴ�%s\n\n", Util.colorRed("INSERT ( ���� )"), Util.longSeparator));
				sb.append(String.format("���� %s ��ɸ� ���� �� �� �ֽ��ϴ�%s\n", Util.colorBlue("SELECT ( ��ȸ )"), Util.longSeparator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return false;
			} else if (str.equalsIgnoreCase("UPDATE") || query.contains("UPDATE")) {
				sb.append(String.format("��û�Ͻ� ���� ���뿡 %s ����� ���ԵǾ� �ֽ��ϴ�%s\n\n", Util.colorRed("UPDATE ( ���� )"), Util.longSeparator));
				sb.append(String.format("���� %s ��ɸ� ���� �� �� �ֽ��ϴ�%s\n", Util.colorBlue("SELECT ( ��ȸ )"), Util.longSeparator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return false;
			} else if (str.equalsIgnoreCase("DELETE") || query.contains("DELETE")) {
				sb.append(String.format("��û�Ͻ� ���� ���뿡 %s ����� ���ԵǾ� �ֽ��ϴ�%s\n\n", Util.colorRed("DELETE ( ���� )"), Util.longSeparator));
				sb.append(String.format("���� %s ��ɸ� ���� �� �� �ֽ��ϴ�%s\n", Util.colorBlue("SELECT ( ��ȸ )"), Util.longSeparator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return false;
			} else if (str.equalsIgnoreCase("ALTER") || query.contains("ALTER")) {
				sb.append(String.format("��û�Ͻ� ���� ���뿡 %s ����� ���ԵǾ� �ֽ��ϴ�%s\n\n", Util.colorRed("ALTER ( ���� )"), Util.longSeparator));
				sb.append(String.format("���� %s ��ɸ� ���� �� �� �ֽ��ϴ�%s\n", Util.colorBlue("SELECT ( ��ȸ )"), Util.longSeparator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return false;
			} else if (str.equalsIgnoreCase("DROP") || query.contains("DROP")) {
				sb.append(String.format("��û�Ͻ� ���� ���뿡 %s ����� ���ԵǾ� �ֽ��ϴ�%s\n\n", Util.colorRed("DROP ( ���� )"), Util.longSeparator));
				sb.append(String.format("���� %s ��ɸ� ���� �� �� �ֽ��ϴ�%s\n", Util.colorBlue("SELECT ( ��ȸ )"), Util.longSeparator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return false;
			}// end if-else								
		}// end for-each
		
		return true; // SELECT ��
	}
	
	
	
	/**
	 * ��� ���̺��� �÷����� ����
	 */
	public static int getColumnCount(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		return columnCount;
	}
	
	
	
	/**
	 * ��� ���̺��� �÷����� �迭�� ����
	 */
	public static String[] getColumnNames(ResultSet rs) throws SQLException {
		// columnNames[0] <- �ε��� 0���� ����
		// rsmd.getColumnName(1) <- �ε��� 1���� ����
		String[] columnNames = null;
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCnt = rsmd.getColumnCount(); //�÷��� ��
		columnNames = new String[columnCnt];
		
		if (rs.next()) {
			for (int i = 1; i <= columnCnt; i++) {
				columnNames[i-1] = rsmd.getColumnName(i);
			}
		}		
		
		rs.beforeFirst();
		return columnNames;
	}
	
	
	
	/**
	 * ���� �÷��� ���� ��� ���̺��� �÷����� �迭�� ����
	 */
	public static String[] getOrderedColumnNames(ResultSet rs) throws SQLException {
		
		String[] columnNames = null;
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCnt = rsmd.getColumnCount()+1; //�÷��� ��
		columnNames = new String[columnCnt];
		columnNames[0] = "�� ��";
		
		if (rs.next()) {
			for (int i = 2; i <= columnCnt; i++) {
				columnNames[i-1] = rsmd.getColumnName(i-1);
			}
		}		
		
		rs.beforeFirst();
		return columnNames;
	}
	
	
	
	/**
	 * ���ڷ� �Ѱ��� ResultSet ��ü�� Row ������ �������ش�.
	 */
	public static int getRowCount(ResultSet rs) throws SQLException {
		rs.last();
		int rowCount = rs.getRow();
		rs.beforeFirst();		
		return rowCount;
	}
	
	/**
	 * ���ڷ� �Ѱ��� ���̺� �÷��� �����Ѵٸ� true ����
	 */
	public static boolean columnTableExists(Statement stmt, String tableName, String ColumnName) throws SQLException {
		String query = String.format("SELECT TOP 1 %s FROM %s", ColumnName, tableName);
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (rs != null) {
				DbUtil.close(rs, stmt);
				return true;
			} else {
				DbUtil.close(rs, stmt);
				return false;
			}
		}
	}
	
    /**
     * ���� ����� ������ �÷��� ���ԵǾ� �ִ����� �˻�
     */
	public static boolean columnExists(ResultSetMetaData rsmd, String col) throws SQLException {
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			if (rsmd.getColumnName(i).equalsIgnoreCase(col)) {
				return true;
			}
		}
		return false;
	}
	
	
	
    /**
     * ���� ����� ������ �÷��� ���ԵǾ� �ִ����� �˻�
     */
	public static boolean columnExists(ResultSet rs, String strField) throws SQLException {
		boolean bRetval = false;
		ResultSetMetaData rsmd = rs.getMetaData();
		int nCnt = rsmd.getColumnCount();
		for (int i = 1; i <= nCnt; i++) {
			if (rsmd.getColumnName(i).equalsIgnoreCase(strField)) {
				bRetval = true;
				break;
			}
		}
		return bRetval;
	}
	
		
		
		
		/** MK119 Login Panel Function *******************************************************************************/		
		// MSSQL ����̹� �ε�
		public static boolean loadDriver() {								
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				System.out.println("[ DbUtil.loadDriver() : MSSQL ����̹� �ε� ���� ]");
				return true;
			} catch (ClassNotFoundException e) {
				Util.showMessage("MSSQL ����̹� �ε忡 �����Ͽ����ϴ�\n", JOptionPane.ERROR_MESSAGE);
				System.out.println("[ DbUtil.loadDriver() : MSSQL ����̹� �ε忡 �����Ͽ����ϴ� ]");
				return false;
			}		
		}
		
			
		// localhost �� �����ͺ��̽�(�ܺ�ȣ��Ʈ) Connection ��ȯ
		public static Connection getCustomMk119Connection() throws SQLException{
			// �̹� Connection ��ü�� �����Ѵٸ� �ݾ��ش� (ConnectionPool ���� ���뿹��)
			if(ONION_Info.hasMk119Connection()) ONION_Info.closeMk119Connection();
			
			Connection actualConnection;
			String externalIp = MK119_Login_Panel.MK119_ip.getText();
			String externalPort = MK119_Login_Panel.MK119_port.getText();
			String userid = null;
			String userPw = null;
			String dbName = null;
			
			try {
				if(ONION_Info.sqlServerInfo != null) {
					if(externalIp.equals(ONION_Info.sqlServerInfo[0]) && externalPort.equals(ONION_Info.sqlServerInfo[1])) {
						
						StringBuilder msg = new StringBuilder();
						msg.append("������ �����ͺ��̽� ���� ������ �̿��Ͽ� ���� �Ͻðڽ��ϱ�?" + Util.separator + Util.separator + "\n");
						msg.append(Util.colorBlue("SQL Server IP") + " : " + ONION_Info.sqlServerInfo[0] + "\n");
						msg.append(Util.colorBlue("SQL Server Port") + " : " + ONION_Info.sqlServerInfo[1] + "\n\n");
						msg.append(Util.colorBlue("SQL Server ID") + " : " + ONION_Info.sqlServerInfo[2] + "\n");
						msg.append(Util.colorBlue("SQL Server PW") + " : " + ONION_Info.sqlServerInfo[3] + "\n");
					
						if(Util.showConfirm(msg.toString()) == JOptionPane.YES_OPTION) {
							return getLastConnection(ONION_Info.sqlServerInfo);
						}
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			String[] sqlServerAuthInfo = getExternalDatabaseInfo(MK119_Login_Panel.MK119_ip.getText(), MK119_Login_Panel.MK119_port.getText());
			
			if(sqlServerAuthInfo != null) {
				userid = sqlServerAuthInfo[0];
				userPw = sqlServerAuthInfo[1];						
			} else {
				System.out.println("[ DbUtil.getCustomMk119Connection() : userId, userPw �ʱ�ȭ ���� ]");
				return null;
			}
			
			String url = String.format("jdbc:sqlserver://%s:%s;", externalIp, externalPort);
			Connection conn = makeConnection(url, userid, userPw);
					
			dbName = getExternalDatabaseName(conn, externalIp, externalPort);
			if(dbName == null) {
				Util.showMessage(String.format("<font color='red'>�����ͺ��̽� ���� ���</font>\n<span color='blue'>%s:%s</span> SQL Server ������ �����Ͽ����� ���� �� �����ͺ��̽� ������ ����Ͽ����ϴ�%s\n", externalIp, externalPort, Util.separator), JOptionPane.ERROR_MESSAGE);
				return null;
			}
			url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;", externalIp , externalPort, dbName);
			
			// ��� �� �����ͺ��̽����� ���� �� Ŀ�ؼ�
			actualConnection = makeConnection(url, userid, userPw);
			
			ONION_Info.setMk119Connection(actualConnection);			
			
			try {
				// actuialConnection ��ü�� ��µ��� ��� �� Ŀ�ؼ� ��ü�� �ݴ´�.
				conn.close();
				System.out.println("[ DbUtil.getCustomMk119Connection() : �ν��Ͻ� Ŀ�ؼ� �ݱ� ���� ]");
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
			return actualConnection;
		}
		
		public static Connection getLastConnection(String[] sqlServerInfo) throws SQLException{
			// �̹� Connection ��ü�� �����Ѵٸ� �ݾ��ش� (ConnectionPool ���� ���뿹��)
			if(ONION_Info.hasMk119Connection()) ONION_Info.closeMk119Connection();
			
			Connection actualConnection;
			String ip = sqlServerInfo[0];
			String port = sqlServerInfo[1];
			String userid = sqlServerInfo[2];
			String userPw = sqlServerInfo[3];
			String dbName = null;
			
			String url = String.format("jdbc:sqlserver://%s:%s;", ip, port);
			Connection conn = makeConnection(url, userid, userPw);
					
			dbName = getExternalDatabaseName(conn, ip, port);
			if(dbName == null) {
				Util.showMessage(String.format("<font color='red'>�����ͺ��̽� ���� ���</font>\n<span color='blue'>%s:%s</span> SQL Server ������ �����Ͽ����� ���� �� �����ͺ��̽� ������ ����Ͽ����ϴ�%s\n", ip, port, Util.separator), JOptionPane.ERROR_MESSAGE);
				return null;
			}
			url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;", ip , port, dbName);
			
			// ��� �� �����ͺ��̽����� ���� �� Ŀ�ؼ�
			actualConnection = makeConnection(url, userid, userPw);
			
			ONION_Info.setMk119Connection(actualConnection);			
			
			try {
				// actuialConnection ��ü�� ��µ��� ��� �� Ŀ�ؼ� ��ü�� �ݴ´�.
				conn.close();
				System.out.println("[ DbUtil.getCustomMk119Connection() : �ν��Ͻ� Ŀ�ؼ� �ݱ� ���� ]");
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
			return actualConnection;
		}
				
		
		
		// ����ڿ��� ���� �� �ܺε����ͺ��̽� sa ���� ����(id, pw)�� ���� 
		public static String[] getExternalDatabaseInfo(String ip, String port) {
			
			String separator = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					
			Font boldfont = FontManager.getFont(Font.BOLD, 15);
			Font plainfont = FontManager.getFont(Font.PLAIN, 15);
			
			JLabel insert = new JLabel(String.format("<html>�����ͺ��̽� ����<br>SQL Server <span color='blue'>%s:%s</span>"
					+ separator
					+ "<br><br>���� �� �����ͺ��̽��� SQL Server ���� ����(sa����)�� �Է����ּ���" + separator + "<br><br></html>", ip, port));
			insert.setFont(boldfont);
										
			// SQL SERVER ID
			JLabel userId_label = new JLabel("<html>SQL Server ���� <span color='blue'>ID</span></html>");
			userId_label.setFont(boldfont);							
			JTextField userId_textField = new JTextField("sa");			
			userId_textField.setFont(plainfont);
			
			// SQL SERVER PW
			JLabel userPw_label = new JLabel("<html>SQL Server ����  <span color='blue'>Password</span></html>");
			userPw_label.setFont(boldfont);						
			JPasswordField userPw_passwordField = new JPasswordField();
			userPw_passwordField.setFont(plainfont);
			
			Object[] message = {
					   insert,				   	
					   userId_label, userId_textField,
					   userPw_label, userPw_passwordField,					   
				};

			int option = JOptionPane.showConfirmDialog(null, message, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

			if (option == JOptionPane.OK_OPTION) {	
				boolean isNull = false;
				
				String[] info = new String[] {
						  userId_textField.getText().trim(), 
						  userPw_passwordField.getText().trim()
						  };							
				
				for (String s : info) {
					if ((s.length() == 0) || (s == null)) isNull = true;
				}
				
				if(isNull) {
					Util.showMessage("<font color='red'>SQL Server ���� ���� ����</font>\n�Է��Ͻ� �����ͺ��̽� SQL Server ���� ������ �߸��Ǿ����ϴ�" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
					return null;
				}else {
					return info;
				}
			} else {
				return null;
			}
		}// end getExternalDatabaseInfo()
		


		
		// ����ڿ��� ���� �� �ܺ� �����ͺ��̽� ���� �����ϰ��Ͽ� ��ȯ
		public static String getExternalDatabaseName(Connection conn, String ip, String port) {
			Statement stmt = null;
			ResultSet rs = null;
			JComboBox dbNameComboBox = new JComboBox();		
			String[] dbName = null; 
			
			List dbNameList = new ArrayList();						
			String Query = "SELECT * FROM sys.sysdatabases WHERE dbid >= 5 ORDER BY name ASC";			
					
			Font boldfont = FontManager.getFont(Font.BOLD, 15);
			Font plainfont = FontManager.getFont(Font.PLAIN, 15);
			
			JLabel insert = new JLabel(String.format("<html><span color='blue'>SQL Server ���� ����</span><br><br>�����ͺ��̽� ����<br>SQL Server <span color='blue'>%s:%s</span>"
					+ Util.longSeparator
					+ "<br><br>���� �� �����ͺ��̽��� �������ּ���" + Util.longSeparator + "<br></html>", ip, port));
			insert.setFont(boldfont);
										
			try {
				stmt = conn.createStatement();
			}catch(SQLException e) {
				System.out.println("[ DbUtil.getExternalDatabaseName() : Statement �ν��Ͻ� ���� ���� ]");
			}
			
			try {
				rs = stmt.executeQuery(Query);
				while(rs.next()) {
					dbNameList.add(rs.getString("name"));
				}
			}catch(SQLException e) {
				System.out.println("[ DbUtil.getExternalDatabaseName() : Statement.executeQuery() ���� ]");
			}
			
			dbName = new String[dbNameList.size()];
			
			for(int i = 0; i < dbNameList.size(); i++) {
				dbName[i] = (String)dbNameList.get(i);
			}
			
			dbNameComboBox.setFont(boldfont);
			dbNameComboBox.setBackground(Color.WHITE);
			dbNameComboBox.setModel(new DefaultComboBoxModel(dbName));
			
			Object[] message = { insert, dbNameComboBox };

			int option = JOptionPane.showConfirmDialog(null, message, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

			if (option == JOptionPane.OK_OPTION) {
				String selectedDbName = dbNameComboBox.getSelectedItem().toString();
				
				ONION_Info.setDataBaseName(selectedDbName);
				return selectedDbName;
			} else {
				return null;
			}
		}// end getExternalDatabaseInfo()		
		
		
		
		// url, id, pw �� ���ڷ� �޾� ������ Connection ��ȯ
		public static Connection makeConnection(String url, String id, String pw) {
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(url, id, pw);			 			
							
				System.out.printf("[ DbUtil.makeConnection() : Ŀ�ؼ� ���� ���� (%s:%s �����ͺ��̽��� ���������� �����Ǿ����ϴ�) ]\n", MK119_Login_Panel.MK119_ip.getText(), MK119_Login_Panel.MK119_port.getText());
				
				ONION_Info.setSqlServerInfo(new String[]{MK119_Login_Panel.MK119_ip.getText(),MK119_Login_Panel.MK119_port.getText(), id, pw});
				
			} catch (SQLException e) {
				StringBuilder msg = new StringBuilder("<span color='red'>SQL Server ���� ����</span>\n");
				msg.append("�Ʒ��� ������ Ȯ�����ּ���" + Util.longSeparator + "\n\n");
				msg.append("1. SQL Server <span color='blue'>���� ����</span>\n");
				msg.append("2. SQL Server <span color='blue'>���� ��� Ȱ��ȭ ����</span>\n");
				msg.append(String.format("3. SQL Server IP : <span color='blue'>%s</span>\n", MK119_Login_Panel.MK119_ip.getText()));
				msg.append(String.format("4. SQL Server Port : <span color='blue'>%s</span>\n", MK119_Login_Panel.MK119_port.getText()));
				msg.append(String.format("5. SQL Server ���� �α��� ���� : <span color='blue'>%s</span>\n", id));
				msg.append(String.format("6. SQL Server ���� �α��� ��й�ȣ : <span color='blue'>%s%s</span>\n", pw, Util.longSeparator));				
				Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
				System.out.printf("[ DbUtil.makeConnection() : Ŀ�ؼ� ���� ���� (%s:%s �����ͺ��̽� ������ �����Ͽ����ϴ�) ]\n", MK119_Login_Panel.MK119_ip.getText(), MK119_Login_Panel.MK119_port.getText());
			}
			return connection;
		}
		
		
		public static boolean checkMK119Db(Connection mk119Connection) {
			boolean isMK119DB = false;
			Statement stmt = null;			
			String checkServerInfo = "SELECT TOP 1 * FROM SERVERINFO"; // ���� ���̺� ��ȸ
			String checkServerInfoFacility = "SELECT TOP 1 * FROM SERVERINFO_FACILITY"; // �ü��� ���̺� ��ȸ
			String checkServerPerf = "SELECT TOP 1 * FROM SERVER_PERF"; // ���� ���̺� ��ȸ
			String checkServerInfoRtu = "SELECT TOP 1 * FROM SERVERINFO_RTU"; // RCU ���̺� ��ȸ
			String checkAlarm = "SELECT TOP 1 * FROM ALARM"; // �̺�Ʈ ���̺� ��ȸ
			String checkEvents = "SELECT TOP 1 * FROM EVENTS"; // �̺�Ʈ ���� ���̺� ��ȸ
			
			try {
				stmt = mk119Connection.createStatement();
				stmt.execute(checkServerInfo);
				stmt.execute(checkServerInfoFacility);
				stmt.execute(checkServerPerf);
				stmt.execute(checkServerInfoRtu);
				stmt.execute(checkAlarm);
				stmt.execute(checkEvents);
				isMK119DB = true;
			} catch(SQLException e) {
				isMK119DB = false;
				e.printStackTrace();
				Util.showMessage(String.format("<font color='red'>MK119 �����ͺ��̽� ���� ����</font>\n�����Ͻ� �����ͺ��̽��� MK119 ���̺��� �������� �ʽ��ϴ�" + Util.separator + "\n"), JOptionPane.ERROR_MESSAGE);
			}
			
			return isMK119DB;
		}
		
		public static String getState(int condition) {
			switch(condition) {
				case 0 : return "���� ��";
				case 1 : return "���� ��";
				case 2 : return "���� ��";
				case 3 : return "���� ����";
				case 4 : return "��� ��";
				case 5 : return "��� ����";
				case 6 : return "���� ����";
				case 7 : return "���� ����";
				case 8 : return "Unknown";
				case 9 : return "Ping ����";
				default : return "Unknown";
			}
		}
		
		public static String getRcuType(int rcuType) {
			switch(rcuType) {
				case 3 : return "MK RCU V1.0";
				case 5 : return "TCP/IP RCU";
				case 6 : return "MK119 - REM 2408";
				case 9 : return "MK119 - REM 1204";
				case 11 : return "MK119 - REM 1204 v1.0.3";
				case 12 : return "Passive TCP/IP Server";
				case 13 : return "LSIS XGT PLC";
				case 14 : return "PoscoICT HVAC SI";
				case 15 : return "CIMON PLC";
				case 16 : return "LSIS GLOFA PLC";
				case 17 : return "MK Active RCU";
				case 18 : return "TCP/IP Multiport RCU";
				case 19 : return "Modbus Gateway";
				case 20 : return "TCP/IP ����ȭ RCU";
				case 21 : return "MQTT Broker";
				case 22 : return "MQTT Mosquitto Broker";
				default : return "Unknown";
			}
		}
		
		public static String getPerfType(int perfType) {
			switch(perfType) {
				case 1 : return "������Ʈ";	
				case 2 : return "SNMP";
				case 3 : return "��Ʈ ����";
				case 4 : return "����Ŭ DB";
				case 5 : return "RCU ����";
				case 6 : return "RCU �ø��� ����";
				case 8 : return "TCP �ø��� ����";
				case 9 : return "ZigBee �ڵ������ ����";
				case 10 : return "UDP ����";
				case 11 : return "BACnet ����";
				case 12 : return "File ����";
				case 13 : return "PSM ����";
				case 14 : return "DB ����";
				case 15 : return "Modbus ����";
				case 16 : return "iLON ����";
				case 17 : return "LNS DDE ����";
				case 18 : return "PLC ����";
				case 19 : return "���󼺴�";
				case 20 : return "IPMI ����";
				case 22 : return "����(����)";
				case 23 : return "����(�����·�)";
				case 24 : return "����(�����·�)";
				case 25 : return "����(SQL)";
				case 26 : return "����Ʈ����";
				case 27 : return "MUX ����";
				case 28 : return "UDP RECV ����";
				case 29 : return "REST";
				case 30 : return "MidasCon";
				case 31 : return "MidasAp";
				case 32 : return "����(���� ī����)";
				case 33 : return "������ ����";
				case 34 : return "�ϰ����ð�";
				case 35 : return "����(�ʱ�ȭ)";
				case 36 : return "����(�����ð�)";
				case 37 : return "REST API";
				default : return "�� �� ����";
			}
		}
		
		public static String getConnMethod(int connMethod) {
			switch(connMethod) {
				case 1 : return "���� ����"; 
				case 2 : return "�ø��� ��Ʈ ����"; 
				case 4 : return "SNMP ����"; 
				case 8 : return "PSTN ����"; 
				case 16 : return "TCP/IP ����"; 
				case 32 : return "ZigBee ����"; 
				case 64 : return "UDP/IP ����"; 
				case 128 : return "BACnet ����"; 
				case 256 : return "File ����"; 
				case 512 : return "PSM ����"; 
				case 1024 : return "DB ����"; 
				case 2048 : return "Modbus ����"; 
				case 4096 : return "iLon ����"; 
				case 8192 : return "LNS DDE ����"; 
				case 32768 : return "PLC ����"; 
				case 12288 : return "���� ����"; 
				case 65536 : return "IPMI ����"; 
				case 131072 : return "SNMP(MANAGER) ����"; 
				case 196608 : return "MUX ����"; 
				case 262144 : return "UDP RECV ����"; 
				case 327680 : return "UDP/IP ����"; 
				case 393218 : return "Midas ����"; 
				case 458752 : return "Rackguard ����"; 
				case 524288 : return "BACnet REST Agent ����"; 
				case 589824 : return "REST API ����"; 
				default : return "Unknown";
			}
		}
		
		public static ArrayList<Integer> getAllFacilityCodeList() {
			ArrayList<Integer> facCodeList = new ArrayList<Integer>();
				facCodeList.add(1); // UPS
				facCodeList.add(2); // CRAC
				facCodeList.add(3); // �Ϸм�ȭ��
				facCodeList.add(4); // ������
				facCodeList.add(5); // �ι���
				facCodeList.add(6); // AVC
				facCodeList.add(7); // ������
				facCodeList.add(8); // ����������
//				facCodeList.add(9); // ī�޶�
				facCodeList.add(10); // VESDA
				facCodeList.add(11); // STS
				facCodeList.add(12); // ������
				facCodeList.add(13); // BMS
				facCodeList.add(14); // �½�����
				facCodeList.add(15); // ȭ�� ���ű�
				facCodeList.add(16); // ���� Ž����
				facCodeList.add(17); // ī�޶� ��Ʈ�ѷ�
				facCodeList.add(18); // ��
				facCodeList.add(19); // ������ ����
				facCodeList.add(20); // ���� �νı�
				facCodeList.add(21); // ������
				facCodeList.add(22); // ǳ����
				facCodeList.add(23); // ������
				facCodeList.add(24); // ���� ������ġ
				facCodeList.add(25); // ǳ�Ӱ�
				facCodeList.add(26); // PDU
				facCodeList.add(27); // ���� ����
				facCodeList.add(28); // �õ���
				facCodeList.add(29); // XD
				facCodeList.add(98); // AI-Net ���� ����
				facCodeList.add(99); // ������
				facCodeList.add(102); // Access Floor
				facCodeList.add(103); // CTTS
				facCodeList.add(199); // ����
				facCodeList.add(200); // IBS ���� : AHU				
				facCodeList.add(201); // IBS ���� : FCU
				facCodeList.add(202); // IBS ���� : FPU
				facCodeList.add(203); // IBS ���� : HV
				facCodeList.add(204); // IBS ���� : ���Ϸ�
				facCodeList.add(205); // IBS ���� : PAC
				facCodeList.add(206); // IBS ���� : �ð���
				facCodeList.add(207); // IBS ���� : �ü���
				facCodeList.add(208); // IBS ���� : �ÿ¼���				
				facCodeList.add(209); // IBS ���� : ��
				facCodeList.add(210); // IBS ���� : �����ũ
				facCodeList.add(211); // IBS ���� : ����ý���
				facCodeList.add(212); // IBS ���� : �߼��ý���
				facCodeList.add(213); // IBS ���� : ����
				facCodeList.add(214); // IBS ���� : ����
				return facCodeList;
		}
		
		
		public static String getFacilityType(int facilityType) {
			switch(facilityType) {
				case 1 : return "UPS";
				case 2 : return "CRAC";
				case 3 : return "�Ϸм�ȭ��";
				case 4 : return "������";
				case 5 : return "�ι���";
				case 6 : return "AVC";
				case 7 : return "������";
				case 8 : return "����������";
				case 9 : return "ī�޶�";
				case 10 : return "VESDA";
				case 11 : return "STS";
				case 12 : return "������";
				case 13 : return "BMS";
				case 14 : return "�½�����";
				case 15 : return "ȭ�� ���ű�";
				case 16 : return "���� Ž����";
				case 17 : return "ī�޶� ��Ʈ�ѷ�";
				case 18 : return "��";
				case 19 : return "������ ����";
				case 20 : return "���� �νı�";
				case 21 : return "������";
				case 22 : return "ǳ����";
				case 23 : return "������";
				case 24 : return "���� ������ġ";
				case 25 : return "ǳ�Ӱ�";
				case 26 : return "PDU";
				case 27 : return "���� ����";
				case 28 : return "�õ���";
				case 29 : return "XD";
				case 98 : return "AI-Net ���� ����";
				case 99 : return "������";
				case 102 : return "Access Floor";
				case 103 : return "CTTS";
				case 199 : return "����";
				case 200 : return "IBS ����";
				case 201 : return "FCU";
				case 202 : return "FPU";
				case 203 : return "HV";
				case 204 : return "���Ϸ�";
				case 205 : return "PAC";
				case 206 : return "�ð���";
				case 207 : return "�ü���";
				case 208 : return "�ÿ¼���";				
				case 209 : return "��";
				case 210 : return "�����ũ";
				case 211 : return "����ý���";
				case 212 : return "�߼��ý���";
				case 213 : return "����";
				case 214 : return "����";
				default : return "Unknown";
			}
		}
		
		
		public static String getFacilityType(String facilityType) {
			switch(facilityType) {
				case "FacilityType.ACCESS_FLOOR" : return "Access Floor";
				case "FacilityType.AINETCONTROLLER" : return "AI-Net ��Ʈ�ѷ�";
				case "FacilityType.AINETTEMHUM" : return "AI-Net �½�����";
				case "FacilityType.AINET_SENSOR" : return "AI-Net ���� ����";
				case "FacilityType.AIRCON" : return "������";
				case "FacilityType.AIRFLOWMETER" : return "ǳ����";
				case "FacilityType.AIR_CONDITIONING" : return "���� ����";
				case "FacilityType.ANEMOMETER" : return "ǳ�Ӱ�";
				case "FacilityType.AVC" : return "AVC";
				case "FacilityType.BMS" : return "BMS";
				case "FacilityType.CAMERA" : return "ī�޶�";
				case "FacilityType.CAMERA_CONTROLLER" : return "ī�޶� ��Ʈ�ѷ�";
				case "FacilityType.CTTS" : return "CTTS";
				case "FacilityType.DIGITALMETER" : return "������ ����";
				case "FacilityType.ETC" : return "��Ÿ";
				case "FacilityType.FACILITY" : return "�ü���";
				case "FacilityType.FINGERPRINT" : return "���� �νı�";
				case "FacilityType.FIRERECEIVER" : return "ȭ�� ���ű�";
				case "FacilityType.GENERATOR" : return "������";
				case "FacilityType.HARON" : return "�Ϸм�ȭ��";
				case "FacilityType.HUMIDIFIER" : return "������";
				case "FacilityType.HVAC" : return "CRAC";
				case "FacilityType.IBS_EQUIPMENT" : return "IBS ����";
				case "FacilityType.INVERTER" : return "�ι���";
				case "FacilityType.LEAKDETECTION" : return "����������";
				case "FacilityType.LINEARDETECTOR" : return "���� Ž����";
				case "FacilityType.MOTOR_MONITOR" : return "���� ������ġ";
				case "FacilityType.NETWORK" : return "��Ʈ��ũ";
				case "FacilityType.ORACLEDB" : return "����Ŭ DB";
				case "FacilityType.PDU" : return "PDU";
				case "FacilityType.PRINTER" : return "������";
				case "FacilityType.PRINTERSERVER" : return "������ ����";
				case "FacilityType.RACK" : return "��";
				case "FacilityType.RECTIFIER" : return "������";
				case "FacilityType.RELAY" : return "������";
				case "FacilityType.REMOTE" : return "���� ���� ��ġ";
				case "FacilityType.SENSOR" : return "������";
				case "FacilityType.SERVER" : return "����";
				case "FacilityType.STS" : return "STS";
				case "FacilityType.SWITCHBOARD" : return "������";
				case "FacilityType.TEMHUM" : return "�½�����";
				case "FacilityType.THE_CHILLER" : return "�õ���";
				case "FacilityType.UPS" : return "UPS";
				case "FacilityType.VESDA" : return "VESDA";
				case "FacilityType.XD" : return "XD";
				case "FacilityType.ZIGBEERECEIVER" : return "ZigBee ���ű�";				
				case "IbsFacilityType.AHU" : return "AHU";
				case "IbsFacilityType.BOILER" : return "���Ϸ�";
				case "IbsFacilityType.CHILLER" : return "�ÿ¼���";
				case "IbsFacilityType.COOLING" : return "�ð���";
				case "IbsFacilityType.FAN" : return "��";
				case "IbsFacilityType.FCU" : return "FCU";
				case "IbsFacilityType.FPU" : return "FPU";
				case "IbsFacilityType.GRAYWATERSYS" : return "�߼��ý���";
				case "IbsFacilityType.HV" : return "HV";
				case "IbsFacilityType.LIGHT" : return "����";
				case "IbsFacilityType.PAC" : return "PAC";
				case "IbsFacilityType.POWER" : return "����";
				case "IbsFacilityType.RAINWATERSYS" : return "����ý���";
				case "IbsFacilityType.WATERBATH" : return "�����ũ";
				case "IbsFacilityType.WATERSUPP" : return "�ü���";				
				default : return "Unknown";
			}
		}
		
		public static int getFacilityCode(String facilityType) {
			switch(facilityType) {
				case "FacilityType.UPS" : return 1;
				case "FacilityType.HVAC" : return 2;
				case "FacilityType.HARON" : return 3;
				case "FacilityType.RECTIFIER" : return 4;
				case "FacilityType.INVERTER" : return 5;
				case "FacilityType.AVC" : return 6;
				case "FacilityType.SWITCHBOARD" : return 7;
				case "FacilityType.LEAKDETECTION" : return 8;				
				case "FacilityType.VESDA" : return 10;
				case "FacilityType.STS" : return 11;
				case "FacilityType.RELAY" : return 12;
				case "FacilityType.BMS" : return 13;
				case "FacilityType.TEMHUM" : return 14;
				case "FacilityType.FIRERECEIVER" : return 15;
				case "FacilityType.LINEARDETECTOR" : return 16;
				case "FacilityType.CAMERA_CONTROLLER" : return 17;
				case "FacilityType.RACK" : return 18;
				case "FacilityType.DIGITALMETER" : return 19;
				case "FacilityType.FINGERPRINT" : return 20;
				case "FacilityType.GENERATOR" : return 21;
				case "FacilityType.AIRFLOWMETER" : return 22;
				case "FacilityType.HUMIDIFIER" : return 23;
				case "FacilityType.MOTOR_MONITOR" : return 24;
				case "FacilityType.ANEMOMETER" : return 25;
				case "FacilityType.PDU" : return 26;
				case "FacilityType.AIR_CONDITIONING" : return 27;
				case "FacilityType.THE_CHILLER" : return 28;
				case "FacilityType.XD" : return 29;
				case "FacilityType.AIRCON" : return 97;
				case "FacilityType.AINET_SENSOR" : return 98;
				case "FacilityType.SENSOR" : return 99;
				case "FacilityType.ACCESS_FLOOR" : return 102;
				case "FacilityType.CTTS" : return 103;
				case "FacilityType.SERVER" : return 199;
				case "FacilityType.IBS_EQUIPMENT" : return 200;
				case "IbsFacilityType.AHU" : return 200;
				case "IbsFacilityType.FCU" : return 201;
				case "IbsFacilityType.FPU" : return 202;
				case "IbsFacilityType.HV" : return 203;
				case "IbsFacilityType.BOILER" : return 204;
				case "IbsFacilityType.PAC" : return 205;
				case "IbsFacilityType.COOLING" : return 206;
				case "IbsFacilityType.WATERSUPP" : return 207;
				case "IbsFacilityType.CHILLER" : return 208;
				case "IbsFacilityType.FAN" : return 209;
				case "IbsFacilityType.WATERBATH" : return 210;
				case "IbsFacilityType.RAINWATERSYS" : return 211;
				case "IbsFacilityType.GRAYWATERSYS" : return 212;
				case "IbsFacilityType.LIGHT" : return 213;
				case "IbsFacilityType.POWER" : return 214;
				default : return -1;
			}
		}
		
}
