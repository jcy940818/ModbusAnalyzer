package src_en.database;

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
import src_en.info.ONION_Info;
import src_en.main.MoonInspector;
import src_en.swing.MK119_Login_Panel;
import src_en.swing.MessageFrame;
import src_en.swing.SqlResultFrame;
import src_en.util.Util;

public class DbUtil {
	
	public static final String FACILITY = "FROM SERVERINFO si INNER JOIN SERVERINFO_FACILITY fac ON si.nServerIndex = fac.NODE_INDEX"; // Facility 테이블 조인
	public static final String RCU = "FROM SERVERINFO si INNER JOIN SERVERINFO_RTU rtu ON si.nServerIndex = rtu.NODE_INDEX"; // RCU 테이블 조인
	public static final String PERF = "FROM SERVER_PERF"; // 성능 테이블
	public static final String ALARM = "FROM ALARM"; // 이벤트(알람) 테이블
	public static final String EVENTS = "FROM EVENTS"; // 이벤트 내역 테이블
	
	// 쿼리
	private static String Query;
	private static String SELECT = ""; // SELECT 절
	private static String FROM = ""; // FROM 절
	private static String WHERE = ""; // WHERE 절
	private static String ORDER_BY = ""; // ORDRY BY 절
	
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
		detail.append(String.format("-- 1. The Contents of the inquiry Columns%s", System.lineSeparator()));
		detail.append(String.format("%s%s%s", DbUtil.removeSpace(DbUtil.getSELECT()), System.lineSeparator(), System.lineSeparator()));
		
		detail.append(String.format("-- 2. The Contents of the inquiry Table%s", System.lineSeparator()));
		detail.append(String.format("%s%s%s", DbUtil.removeSpace(DbUtil.getFROM()), System.lineSeparator(), System.lineSeparator()));
		
		
		if(DbUtil.removeSpace(DbUtil.getWHERE()).length() < 1) {
			detail.append(String.format("%s%s%s", "-- 3. There's no set inquiry Condition", System.lineSeparator(), System.lineSeparator()));
		}else {
			detail.append(String.format("-- 3. Contents of the inquiry Condition%s", System.lineSeparator()));
			detail.append(String.format("%s%s%s", DbUtil.removeSpace(DbUtil.getWHERE()), System.lineSeparator(), System.lineSeparator()));	
		}
				
		
		if(DbUtil.removeSpace(DbUtil.getORDER_BY()).length() < 1) {
			detail.append(String.format("%s%s%s", "-- 4. There are no set Sorting Condition", System.lineSeparator(), System.lineSeparator()));
		}else {
			detail.append(String.format("-- 4. Contents of the Sorting Condition%s", System.lineSeparator()));
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
		// 설정된 조건이 존재하는지 확인 후 초기화
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
			
			// ResultSet, Statement 인스턴스가 정상적으로 닫혔다면 true 리턴
			return (rs.isClosed() && stmt.isClosed()) ? true : false; 						
			
		}catch(Exception e) {
			System.out.println("[ DbUtil.close() : ResultSet, Statement 인스턴스를 닫는 중 예외가 발생하였습니다 : " + e.getMessage() + " ]");
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
	
	// 쿼리 실행 후 결과 테이블 프레임 표시
	public static void executeQuery(String sqlServerInfo, String query) {
		if(DbUtil.getCurrentSqlRuningState()) {
			Util.showMessage("<font color='red'>SQL Exception</font>\nAnother Query task already in progress exists" + Util.separator + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!checkQuery(query)) return;
				
		try {			
			Statement stmt = ONION_Info.getMk119Connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// SQL 작업 시작을 알린다.
			DbUtil.startSQL();
			
			ResultSet rs = stmt.executeQuery(query);
			ONION_Info.setDBResultSet(rs);
					
			// 결과 테이블의 행 개수가 0개이면 수행 쿼리 내용을 확인하는지 사용자에게 물어보는 메시지 출력 후 리턴
			if(DbUtil.getRowCount(rs) < 1) {				
				DbUtil.endSQL();				
				String queryDetail = DbUtil.getQueryDetail();				
				StringBuilder msg = new StringBuilder(String.format("<font color='red'>No table inquiry results</font>"
						+ "\n"
						+ "The query was performed normally, but there was no data in the table"
						+ "%s"
						+ "\n\nTable Row Count : 0\n","&nbsp;&nbsp;&nbsp;"));
				msg.append("\n<font color ='blue'>Do you want to check the contents of the query you performed?</font>\n");			
				
				int userOption = Util.showConfirm(msg.toString());
				
				if(userOption == JOptionPane.YES_OPTION) {
					new MessageFrame(String.format("<html><font color='blue'>%s</font> Executed query</html>", sqlServerInfo), queryDetail);
					return; // 결과 테이블 프레임을 생성하지 않고 리턴
				}else {
					return; // 결과 테이블 프레임을 생성하지 않고 리턴
				}
			}
			
			// 반드시 ResultSet 인스턴스의 초기화가 먼저 이루어진 후 SqlResultFrame을 초기화 해야하기 때문에 스레드를 사용하지 않았다.
			String queryDetail = DbUtil.getQueryDetail();
			new SqlResultFrame(sqlServerInfo, queryDetail, rs , "databaseAccess");
			
		}catch(Exception exception) {
			exception.printStackTrace();
			Util.showMessage("<font color='red'>SQL Exception</font>\n" + exception.getMessage() + Util.longSeparator +"\n", JOptionPane.ERROR_MESSAGE);
			System.out.println("\n[ DbUtil.executeQuery() : 쿼리 실행 실패 ]");
			DbUtil.endSQL();		
		}
	}	
	
	
	
	
	// 쿼리 실행 후 결과 테이블 프레임 표시
	public static void executeProcedure(String sqlServerInfo, String query) {
		if(DbUtil.getCurrentSqlRuningState()) {
			Util.showMessage("<font color='red'>SQL Exception</font>\nAnother query task already in progress exists" + Util.separator + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!checkQuery(query)) return;
				
		try {			
			Statement stmt = ONION_Info.getMk119Connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			// SQL 작업 시작을 알린다.
			DbUtil.startSQL();
			
			ResultSet rs = stmt.executeQuery(query);
			ONION_Info.setDBResultSet(rs);
				
			// 결과 테이블의 행 개수가 0개이면 수행 쿼리 내용을 확인하는지 사용자에게 물어보는 메시지 출력 후 리턴
			if(DbUtil.getRowCount(rs) < 1) {
				DbUtil.endSQL();				
				String queryDetail = query;				
				StringBuilder msg = new StringBuilder(String.format("<font color='red'>No table inquiry results</font>"
						+ "\n"
						+ "The query was performed normally, but there was no data in the table"
						+ "%s"
						+ "\n\nTable Row Count : 0\n","&nbsp;&nbsp;&nbsp;"));
				msg.append("\n<font color ='blue'>Do you want to check the contents of the query you performed?</font>\n");			
				
				int userOption = Util.showConfirm(msg.toString());
				
				if(userOption == JOptionPane.YES_OPTION) {
					new MessageFrame(String.format("<html><font color='blue'>%s</font> Executed query</html>", sqlServerInfo), queryDetail);
					return; // 결과 테이블 프레임을 생성하지 않고 리턴
				}else {
					return; // 결과 테이블 프레임을 생성하지 않고 리턴
				}
			}
			
			// 반드시 ResultSet 인스턴스의 초기화가 먼저 이루어진 후 SqlResultFrame을 초기화 해야하기 때문에 스레드를 사용하지 않았다.
			String queryDetail = query;
			new SqlResultFrame(sqlServerInfo, queryDetail, rs, "storedProcedure");
			
		}catch(Exception exception) {
			exception.printStackTrace();
			Util.showMessage("<font color='red'>SQL Exception</font>\n" + exception.getMessage() + Util.longSeparator +"\n", JOptionPane.ERROR_MESSAGE);
			System.out.println("\n[ DbUtil.executeQuery() : 쿼리 실행 실패 ]");
			DbUtil.endSQL();		
		}
	}
	
	
	
	
	
	
	public static boolean checkQuery(String query) {		
		
		if(MoonInspector.isMoon()) return true;
		
		query = query.toUpperCase();
		
		String msg = String.format("%sPlease check the requested %s%s\n",Util.colorRed("SQL Exception\n"), Util.colorBlue("Query") , Util.separator);
		
		if ((query.length() == 0) || query.equalsIgnoreCase("")) {
			Util.showMessage(msg, JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s%s\n", Util.colorRed("SQL Warning"), Util.longSeparator));
				
		String[] strs = query.split(" ");
		
		// switch 문으로 하려고 하였으나, case 조건으로 상수 비교를 할 것이 아니라
		// String.equlasIgnoreCase()를 통해 문자열을 비교하기 위하여 if-else 문 사용
		// 쿼리를 대,소 문자 구분없이 사용하는 사용자가 있을 수 있기 때문에
		
		for (String str : strs) {
			if (str.equalsIgnoreCase("CREATE") || query.contains("CREATE")) {
				sb.append(String.format("The requested query contains a data %s command%s\n\n", Util.colorRed("creation ( CREATE )"), Util.longSeparator));
				sb.append(String.format("Can only execute the %s command%s\n", Util.colorBlue("inquiry ( SELECT )"), Util.longSeparator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return false;
			} else if (str.equalsIgnoreCase("INSERT") || query.contains("INSERT")) {
				sb.append(String.format("The requested query contains a data %s command%s\n\n", Util.colorRed("insertion ( INSERT )"), Util.longSeparator));
				sb.append(String.format("Can only execute the %s command%s\n", Util.colorBlue("inquiry ( SELECT )"), Util.longSeparator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return false;
			} else if (str.equalsIgnoreCase("UPDATE") || query.contains("UPDATE")) {
				sb.append(String.format("The requested query contains a data %s command%s\n\n", Util.colorRed("update ( UPDATE )"), Util.longSeparator));
				sb.append(String.format("Can only execute the %s command%s\n", Util.colorBlue("inquiry ( SELECT )"), Util.longSeparator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return false;
			} else if (str.equalsIgnoreCase("DELETE") || query.contains("DELETE")) {
				sb.append(String.format("The requested query contains a data %s command%s\n\n", Util.colorRed("delete ( DELETE )"), Util.longSeparator));
				sb.append(String.format("Can only execute the %s command%s\n", Util.colorBlue("inquiry ( SELECT )"), Util.longSeparator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return false;
			} else if (str.equalsIgnoreCase("ALTER") || query.contains("ALTER")) {
				sb.append(String.format("The requested query contains a data %s command%s\n\n", Util.colorRed("modification ( ALTER )"), Util.longSeparator));
				sb.append(String.format("Can only execute the %s command%s\n", Util.colorBlue("inquiry ( SELECT )"), Util.longSeparator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return false;
			} else if (str.equalsIgnoreCase("DROP") || query.contains("DROP")) {
				sb.append(String.format("The requested query contains a data %s command%s\n\n", Util.colorRed("delete ( DROP )"), Util.longSeparator));
				sb.append(String.format("Can only execute the %s command%s\n", Util.colorBlue("inquiry ( SELECT )"), Util.longSeparator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return false;
			}// end if-else								
		}// end for-each
		
		return true; // SELECT 문
	}
	
	
	
	/**
	 * 결과 테이블의 컬럼개수 리턴
	 */
	public static int getColumnCount(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		return columnCount;
	}
	
	
	
	/**
	 * 결과 테이블의 컬럼명을 배열로 리턴
	 */
	public static String[] getColumnNames(ResultSet rs) throws SQLException {
		// columnNames[0] <- 인덱스 0부터 시작
		// rsmd.getColumnName(1) <- 인덱스 1부터 시작
		String[] columnNames = null;
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCnt = rsmd.getColumnCount(); //컬럼의 수
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
	 * 순서 컬럼을 가진 결과 테이블의 컬럼명을 배열로 리턴
	 */
	public static String[] getOrderedColumnNames(ResultSet rs) throws SQLException {
		
		String[] columnNames = null;
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCnt = rsmd.getColumnCount()+1; //컬럼의 수
		columnNames = new String[columnCnt];
		columnNames[0] = "Sequence";
		
		if (rs.next()) {
			for (int i = 2; i <= columnCnt; i++) {
				columnNames[i-1] = rsmd.getColumnName(i-1);
			}
		}		
		
		rs.beforeFirst();
		return columnNames;
	}
	
	
	
	/**
	 * 인자로 넘겨준 ResultSet 객체의 Row 개수를 리턴해준다.
	 */
	public static int getRowCount(ResultSet rs) throws SQLException {
		rs.last();
		int rowCount = rs.getRow();
		rs.beforeFirst();		
		return rowCount;
	}
	
	/**
	 * 인자로 넘겨준 테이블에 컬럼이 존재한다면 true 리턴
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
     * 쿼리 결과에 지정된 컬럼이 포함되어 있는지를 검사
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
     * 쿼리 결과에 지정된 컬럼이 포함되어 있는지를 검사
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
		// MSSQL 드라이버 로드
		public static boolean loadDriver() {								
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				System.out.println("[ DbUtil.loadDriver() : MSSQL 드라이버 로드 성공 ]");
				return true;
			} catch (ClassNotFoundException e) {
				Util.showMessage("<font color='red'>Database Error</font>\nMS-SQL database driver load failed"
						+ Util.separator + Util.separator 
						+ "\n", JOptionPane.ERROR_MESSAGE);
				
				System.out.println("[ DbUtil.loadDriver() : MSSQL 드라이버 로드에 실패하였습니다 ]");
				return false;
			}		
		}
		
			
		// localhost 외 데이터베이스(외부호스트) Connection 반환
		public static Connection getCustomMk119Connection() throws SQLException{
			// 이미 Connection 객체가 존재한다면 닫아준다 (ConnectionPool 추후 적용예정)
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
						msg.append("Use the last Database Connection Information to Access?" + Util.separator + Util.separator + "\n");
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
				System.out.println("[ DbUtil.getCustomMk119Connection() : userId, userPw 초기화 실패 ]");
				return null;
			}
			
			String url = String.format("jdbc:sqlserver://%s:%s;", externalIp, externalPort);
			Connection conn = makeConnection(url, userid, userPw);
					
			dbName = getExternalDatabaseName(conn, externalIp, externalPort);
			if(dbName == null) {
				Util.showMessage(String.format("<font color='red'>Unselect database</font>\n<span color='blue'>%s:%s</span> SQL Server authentication was successful, "
						+ Util.separator + Util.separator+ "\n\n"
						+ "but the database to be accessed was deselected%s\n", externalIp, externalPort, Util.separator), JOptionPane.ERROR_MESSAGE);
				return null;
			}
			url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;", externalIp , externalPort, dbName);
			
			// 사용 할 데이터베이스까지 선택 된 커넥션
			actualConnection = makeConnection(url, userid, userPw);
			
			ONION_Info.setMk119Connection(actualConnection);			
			
			try {
				// actuialConnection 객체를 얻는데에 사용 된 커넥션 객체를 닫는다.
				conn.close();
				System.out.println("[ DbUtil.getCustomMk119Connection() : 인스턴스 커넥션 닫기 성공 ]");
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
			return actualConnection;
		}
		
		public static Connection getLastConnection(String[] sqlServerInfo) throws SQLException{
			// 이미 Connection 객체가 존재한다면 닫아준다 (ConnectionPool 추후 적용예정)
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
				Util.showMessage(String.format("<font color='red'>Unselect database</font>\n<span color='blue'>%s:%s</span> SQL Server authentication was successful, "
						+ Util.separator + Util.separator+ "\n\n"
						+ "but the database to be accessed was deselected%s\n", ip, port, Util.separator), JOptionPane.ERROR_MESSAGE);
				return null;
			}
			url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;", ip , port, dbName);
			
			// 사용 할 데이터베이스까지 선택 된 커넥션
			actualConnection = makeConnection(url, userid, userPw);
			
			ONION_Info.setMk119Connection(actualConnection);			
			
			try {
				// actuialConnection 객체를 얻는데에 사용 된 커넥션 객체를 닫는다.
				conn.close();
				System.out.println("[ DbUtil.getCustomMk119Connection() : 인스턴스 커넥션 닫기 성공 ]");
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
			return actualConnection;
		}
				
		
		
		// 사용자에게 접속 할 외부데이터베이스 sa 계정 정보(id, pw)를 받음 
		public static String[] getExternalDatabaseInfo(String ip, String port) {
			
			String separator = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					
			Font boldfont = FontManager.getFont(Font.BOLD, 15);
			Font plainfont = FontManager.getFont(Font.PLAIN, 15);
			
			JLabel insert = new JLabel(String.format("<html>Create a Database Connection ( SQL Server <span color='blue'>%s:%s</span> )"
					+ separator 
					+ "<br><br>"
					+ "Please enter your SQL Server authentication information"										
					+ separator + "<br><br></html>", ip, port));
			insert.setFont(boldfont);
										
			// SQL SERVER ID
			JLabel userId_label = new JLabel("<html>SQL Server <span color='blue'>ID</span></html>");
			userId_label.setFont(boldfont);							
			JTextField userId_textField = new JTextField("sa");			
			userId_textField.setFont(plainfont);
			
			// SQL SERVER PW
			JLabel userPw_label = new JLabel("<html>SQL Server <span color='blue'>Password</span></html>");
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
					Util.showMessage("<font color='red'>SQL Server authentication information error</font>"
							+ "\n"
							+ "The authentication information to access the SQL Server you entered is invalid" + Util.separator + "\n", JOptionPane.ERROR_MESSAGE);
					return null;
				}else {
					return info;
				}
			} else {
				return null;
			}
		}// end getExternalDatabaseInfo()
		


		
		// 사용자에게 접속 할 외부 데이터베이스 명을 선택하게하여 반환
		public static String getExternalDatabaseName(Connection conn, String ip, String port) {
			Statement stmt = null;
			ResultSet rs = null;
			JComboBox dbNameComboBox = new JComboBox();		
			String[] dbName = null; 
			
			List dbNameList = new ArrayList();						
			String Query = "SELECT * FROM sys.sysdatabases WHERE dbid >= 5 ORDER BY name ASC";			
					
			Font boldfont = FontManager.getFont(Font.BOLD, 15);
			Font plainfont = FontManager.getFont(Font.PLAIN, 15);
			
			JLabel insert = new JLabel(String.format("<html><span color='blue'>SQL Server authentication success</span>"
					+ "<br><br>"
					+ "Please select the database you want to access"
					+ "<br><br>"
					+ "Database list of SQL Server <span color='blue'>( %s:%s )</span>"
					+ Util.longSeparator
					+ "<br>"
					+ "</html>", ip, port));
			insert.setFont(boldfont);
										
			try {
				stmt = conn.createStatement();
			}catch(SQLException e) {
				System.out.println("[ DbUtil.getExternalDatabaseName() : Statement 인스턴스 생성 실패 ]");
			}
			
			try {
				rs = stmt.executeQuery(Query);
				while(rs.next()) {
					dbNameList.add(rs.getString("name"));
				}
			}catch(SQLException e) {
				System.out.println("[ DbUtil.getExternalDatabaseName() : Statement.executeQuery() 실패 ]");
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
		
		
		
		// url, id, pw 를 인자로 받아 생성한 Connection 반환
		public static Connection makeConnection(String url, String id, String pw) {
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(url, id, pw);			 			
//				Util.showMessage("<span color='blue'>SQL Server 인증 성공</span>\n데이터베이스 접속에 성공하였습니다&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", JOptionPane.INFORMATION_MESSAGE);			
				System.out.printf("[ DbUtil.makeConnection() : 커넥션 생성 성공 (%s:%s 데이터베이스와 정상적으로 연동되었습니다) ]\n", MK119_Login_Panel.MK119_ip.getText(), MK119_Login_Panel.MK119_port.getText());
				
				ONION_Info.setSqlServerInfo(new String[]{MK119_Login_Panel.MK119_ip.getText(),MK119_Login_Panel.MK119_port.getText(), id, pw});
				
			} catch (SQLException e) {
				StringBuilder msg = new StringBuilder("<span color='red'>SQL Server Authentication Failed</span>\n");
				msg.append("Please check the contents below" + Util.longSeparator + "\n\n");
				msg.append("1. Whether the SQL Server is <span color='blue'>running or not</span>" + Util.longSeparator +"\n");
				msg.append("2. Whether the SQL Server authentication mode is <span color='blue'>activated or not</span>" + Util.longSeparator + "\n");
				msg.append(String.format("3. SQL Server IP : <span color='blue'>%s</span>\n", MK119_Login_Panel.MK119_ip.getText()));
				msg.append(String.format("4. SQL Server Port : <span color='blue'>%s</span>\n", MK119_Login_Panel.MK119_port.getText()));
				msg.append(String.format("5. SQL Server Login ID : <span color='blue'>%s</span>\n", id));
				msg.append(String.format("6. SQL Server Login PW : <span color='blue'>%s%s</span>\n", pw, Util.longSeparator));				
				Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
				System.out.printf("[ DbUtil.makeConnection() : 커넥션 생성 실패 (%s:%s 데이터베이스 연동에 실패하였습니다) ]\n", MK119_Login_Panel.MK119_ip.getText(), MK119_Login_Panel.MK119_port.getText());
			}
			return connection;
		}
		
		
		public static boolean checkMK119Db(Connection mk119Connection) {
			boolean isMK119DB = false;
			Statement stmt = null;			
			String checkServerInfo = "SELECT TOP 1 * FROM SERVERINFO"; // Server 테이블 조회
			String checkServerInfoFacility = "SELECT TOP 1 * FROM SERVERINFO_FACILITY"; // Facility 테이블 조회
			String checkServerPerf = "SELECT TOP 1 * FROM SERVER_PERF"; // 성능 테이블 조회
			String checkServerInfoRtu = "SELECT TOP 1 * FROM SERVERINFO_RTU"; // RCU 테이블 조회
			String checkAlarm = "SELECT TOP 1 * FROM ALARM"; // 이벤트 테이블 조회
			String checkEvents = "SELECT TOP 1 * FROM EVENTS"; // 이벤트 내역 테이블 조회
			
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
				Util.showMessage(String.format("<font color='red'>MK119 Database Authentication Failed</font>\nThe database you selected is not MK119 database" + Util.separator + "\n"), JOptionPane.ERROR_MESSAGE);
			}
			
			return isMK119DB;
		}
		
		
		public static String getConnMethod(int connMethod) {
			switch(connMethod) {
				case 1 : return "BACnet Connection"; 
				case 2 : return "Serial Port"; 
				case 4 : return "SNMP Connection"; 
				case 8 : return "PSTN Connection"; 
				case 16 : return "TCP/IP Connection"; 
				case 32 : return "ZigBee Connection"; 
				case 64 : return "UDP/IP Connection"; 
				case 128 : return "BACnet Connection"; 
				case 256 : return "File Access"; 
				case 512 : return "PSM Connection"; 
				case 1024 : return "DB Access"; 
				case 2048 : return "Modbus Connection"; 
				case 4096 : return "iLon Connection"; 
				case 8192 : return "LNS DDE Connection"; 
				case 32768 : return "PLC Connection"; 
				case 12288 : return "Virtual"; 
				case 65536 : return "IPMI Connection"; 
				case 131072 : return "SNMP(MANAGER) Connection";
				case 196608 : return "MUX Connection";
				case 262144 : return "UDP RECV Connection";
				case 327680 : return "UDP/IP Connection";
				case 393218 : return "Midas Connection"; 
				case 458752 : return "Rackguard Connection"; 
				case 524288 : return "BACnet REST Agent Connection";
				case 589824 : return "REST API Connection";
				default : return "Unknown";
			}
		}
		
		public static ArrayList<Integer> getAllFacilityCodeList() {
			ArrayList<Integer> facCodeList = new ArrayList<Integer>();
				facCodeList.add(1); // UPS
				facCodeList.add(2); // CRAC
				facCodeList.add(3); // 하론소화기
				facCodeList.add(4); // 정류기
				facCodeList.add(5); // 인버터
				facCodeList.add(6); // AVC
				facCodeList.add(7); // 분전반
				facCodeList.add(8); // 누수감지기
//				facCodeList.add(9); // 카메라
				facCodeList.add(10); // VESDA
				facCodeList.add(11); // STS
				facCodeList.add(12); // 계전기
				facCodeList.add(13); // BMS
				facCodeList.add(14); // 온습도계
				facCodeList.add(15); // 화재 수신기
				facCodeList.add(16); // 선형 탐지기
				facCodeList.add(17); // 카메라 컨트롤러
				facCodeList.add(18); // 랙
				facCodeList.add(19); // 디지털 미터
				facCodeList.add(20); // 지문 인식기
				facCodeList.add(21); // 발전기
				facCodeList.add(22); // 풍량계
				facCodeList.add(23); // 가습기
				facCodeList.add(24); // 모터 감시장치
				facCodeList.add(25); // 풍속계
				facCodeList.add(26); // PDU
				facCodeList.add(27); // 공조 설비
				facCodeList.add(28); // 냉동기
				facCodeList.add(29); // XD
				facCodeList.add(98); // AI-Net 다중 센서
				facCodeList.add(99); // 센서류
				facCodeList.add(102); // Access Floor
				facCodeList.add(103); // CTTS
				facCodeList.add(199); // 서버
				facCodeList.add(200); // IBS 설비 : AHU				
				facCodeList.add(201); // IBS 설비 : FCU
				facCodeList.add(202); // IBS 설비 : FPU
				facCodeList.add(203); // IBS 설비 : HV
				facCodeList.add(204); // IBS 설비 : 보일러
				facCodeList.add(205); // IBS 설비 : PAC
				facCodeList.add(206); // IBS 설비 : 냉각수
				facCodeList.add(207); // IBS 설비 : 시수조
				facCodeList.add(208); // IBS 설비 : 냉온수기				
				facCodeList.add(209); // IBS 설비 : 팬
				facCodeList.add(210); // IBS 설비 : 배수탱크
				facCodeList.add(211); // IBS 설비 : 우수시스템
				facCodeList.add(212); // IBS 설비 : 중수시스템
				facCodeList.add(213); // IBS 설비 : 조명
				facCodeList.add(214); // IBS 설비 : 전력
				return facCodeList;
		}
		
		
		public static String getFacilityType(int facilityType) {
			switch(facilityType) {
				case 1 : return "UPS";
				case 2 : return "CRAC";
				case 3 : return "Fire Extinguisher";
				case 4 : return "Rectifier";
				case 5 : return "Inverter";
				case 6 : return "AVC";
				case 7 : return "Distribution Panel";
				case 8 : return "Leakage Detector";
				case 9 : return "Camera";
				case 10 : return "VESDA";
				case 11 : return "STS";
				case 12 : return "Relay";
				case 13 : return "BMS";
				case 14 : return "Temp Hum Monitor";
				case 15 : return "Fire Detector";
				case 16 : return "Heat Detector";
				case 17 : return "Camera Controller";
				case 18 : return "Rack";
				case 19 : return "Digital Meter";
				case 20 : return "Fingerprint Reader";
				case 21 : return "Generator";
				case 22 : return "Air Flow Meter";
				case 23 : return "Humidifier";
				case 24 : return "Motor Monitor";
				case 25 : return "Anemometer";
				case 26 : return "PDU";
				case 27 : return "HVAC";
				case 28 : return "Chiller";
				case 29 : return "XD";
				case 98 : return "AI-Net Multi Sensor";
				case 99 : return "Sensor";
				case 102 : return "Access Floor";
				case 103 : return "CTTS";
				case 199 : return "Server";
				case 200 : return "IBS Equipment";				
				case 201 : return "FCU";
				case 202 : return "FPU";
				case 203 : return "HV";
				case 204 : return "Boiler";
				case 205 : return "PAC";
				case 206 : return "Cooling water";
				case 207 : return "Water Supplier";
				case 208 : return "Chiller";				
				case 209 : return "Fan";
				case 210 : return "Drain Tank";
				case 211 : return "Rain Water System";
				case 212 : return "Heavy water system";
				case 213 : return "Light";
				case 214 : return "Power";
				default : return "Unknown";
			}
		}
		
		
		public static String getFacilityType(String facilityType) {
			switch(facilityType) {
				case "FacilityType.ACCESS_FLOOR" : return "Access Floor";
				case "FacilityType.AINETCONTROLLER" : return "AI-Net Controller";
				case "FacilityType.AINETTEMHUM" : return "AI-Net Temp Hum Monitor";
				case "FacilityType.AINET_SENSOR" : return "AI-Net Multi Sensor";
				case "FacilityType.AIRCON" : return "Air Conditioner";
				case "FacilityType.AIRFLOWMETER" : return "Air Flow Meter";
				case "FacilityType.AIR_CONDITIONING" : return "HVAC";
				case "FacilityType.ANEMOMETER" : return "Anemometer";
				case "FacilityType.AVC" : return "AVC";
				case "FacilityType.BMS" : return "BMS";
				case "FacilityType.CAMERA" : return "Camera";
				case "FacilityType.CAMERA_CONTROLLER" : return "Camera Controller";
				case "FacilityType.CTTS" : return "CTTS";
				case "FacilityType.DIGITALMETER" : return "Digital Meter";
				case "FacilityType.ETC" : return "Etc";
				case "FacilityType.FACILITY" : return "Facility";
				case "FacilityType.FINGERPRINT" : return "Fingerprint Reader";
				case "FacilityType.FIRERECEIVER" : return "Fire Detector";
				case "FacilityType.GENERATOR" : return "Generator";
				case "FacilityType.HARON" : return "Fire Extinguisher";
				case "FacilityType.HUMIDIFIER" : return "Humidifier";
				case "FacilityType.HVAC" : return "CRAC";
				case "FacilityType.IBS_EQUIPMENT" : return "IBS Equipment";
				case "FacilityType.INVERTER" : return "Inverter";
				case "FacilityType.LEAKDETECTION" : return "Leakage Detector";
				case "FacilityType.LINEARDETECTOR" : return "Heat Detector";
				case "FacilityType.MOTOR_MONITOR" : return "Motor Monitor";
				case "FacilityType.NETWORK" : return "Network";
				case "FacilityType.ORACLEDB" : return "Oracle DB";
				case "FacilityType.PDU" : return "PDU";
				case "FacilityType.PRINTER" : return "Printer";
				case "FacilityType.PRINTERSERVER" : return "Printer Server";
				case "FacilityType.RACK" : return "Rack";
				case "FacilityType.RECTIFIER" : return "Rectifier";
				case "FacilityType.RELAY" : return "Relay";
				case "FacilityType.REMOTE" : return "Remote Controller";
				case "FacilityType.SENSOR" : return "Sensor";
				case "FacilityType.SERVER" : return "Server";
				case "FacilityType.STS" : return "STS";
				case "FacilityType.SWITCHBOARD" : return "Distribution Panel";
				case "FacilityType.TEMHUM" : return "Temp Hum Monitor";
				case "FacilityType.THE_CHILLER" : return "Chiller";
				case "FacilityType.UPS" : return "UPS";
				case "FacilityType.VESDA" : return "VESDA";
				case "FacilityType.XD" : return "XD";
				case "FacilityType.ZIGBEERECEIVER" : return "ZigBee Receiver";				
				case "IbsFacilityType.AHU" : return "AHU";
				case "IbsFacilityType.BOILER" : return "Boiler";
				case "IbsFacilityType.CHILLER" : return "Chiller";
				case "IbsFacilityType.COOLING" : return "Cooling water";
				case "IbsFacilityType.FAN" : return "Fan";
				case "IbsFacilityType.FCU" : return "FCU";
				case "IbsFacilityType.FPU" : return "FPU";
				case "IbsFacilityType.GRAYWATERSYS" : return "Heavy water system";
				case "IbsFacilityType.HV" : return "HV";
				case "IbsFacilityType.LIGHT" : return "Light";
				case "IbsFacilityType.PAC" : return "PAC";
				case "IbsFacilityType.POWER" : return "Power";
				case "IbsFacilityType.RAINWATERSYS" : return "Rain Water System";
				case "IbsFacilityType.WATERBATH" : return "Drain Tank";
				case "IbsFacilityType.WATERSUPP" : return "Water Supplier";
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
