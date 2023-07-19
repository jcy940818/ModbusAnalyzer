package src_ko.info;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.OnionMember;
import src_ko.util.Util;
 
public class ONION_Info {
	
	public static final String versionInfoClassPath = "\\midknight\\tomcat\\webapps\\midknight\\WEB-INF\\classes\\com\\onion\\mk119s\\conf\\VersionInfo.class";
	public static final String systemConfigClassPath = "\\midknight\\tomcat\\webapps\\midknight\\WEB-INF\\classes\\com\\onion\\mk119s\\conf\\SystemConfig.class";
	public static final String fmsProtocolClassPath = "\\midknight\\tomcat\\webapps\\midknight\\WEB-INF\\classes\\com\\onion\\mk119s\\conf\\constant\\FmsProtocol.class";
	public static final String fmsMibClassPath = "\\midknight\\tomcat\\webapps\\midknight\\WEB-INF\\classes\\com\\onion\\mk119s\\conf\\constant\\FmsMib.class";
	public static final String enumKoPath = "\\midknight\\tomcat\\webapps\\midknight\\WEB-INF\\classes\\resources\\enum_ko.properties";
	public static final String enumEnPath = "\\midknight\\tomcat\\webapps\\midknight\\WEB-INF\\classes\\resources\\enum_en.properties";
	
	public static final String P_versionInfoClassPath_42 = "\\web\\WEB-INF\\classes\\com\\onion\\mk119s\\conf\\VersionInfo.class";
	public static final String P_versionInfoClassPath_45 = "\\bin\\com\\onion\\mk119s\\conf\\VersionInfo.class";
	public static final String P_systemConfigClassPath = "\\web\\WEB-INF\\classes\\com\\onion\\mk119s\\conf\\SystemConfig.class";
	public static final String P_fmsProtocolClassPath = "\\bin\\com\\onion\\mk119s\\conf\\constant\\FmsProtocol.class";
	public static final String P_fmsMibClassPath = "\\bin\\com\\onion\\mk119s\\conf\\constant\\FmsMib.class";
	public static final String P_enumKoPath = "\\src\\main\\resources\\resources\\enum_ko.properties";
	public static final String P_enumEnPath = "\\src\\main\\resources\\resources\\enum_en.properties";
	
	public static final String PRODUCT_NAME = "PRODUCT_NAME"; // MK119
	public static final String PRODUCT_VERSION_MAJOR = "PRODUCT_VERSION_MAJOR"; // 4
	public static final String PRODUCT_VERSION_MINOR = "PRODUCT_VERSION_MINOR"; // 2
	public static final String PRODUCT_VERSION_BUILD = "PRODUCT_VERSION_BUILD"; // 590
	public static final String BUILD_DATE = "BUILD_DATE"; // (2020-01-10)
	public static final String PRODUCT_VERSION = "PRODUCT_VERSION"; // 4.2 build591
	public static final String PRODUCT_VERSION_FULL = "PRODUCT_VERSION_FULL"; // MK119 4.2 build591
	
	private static String adminConsoleInfo = null;
	
	// MK119 : Database Connection
	public static String[] sqlServerInfo = null;
	private static boolean hasMk119Connection = false;
	private static Connection mk119Connection;
	private static boolean hasDbResultSet = false;
	private static ResultSet dbResultSet;
	private static String dataBaseName = null;
	
	// OnionSoftware Directory
	private static String onionDirPath = null; 	
	private static String projectDirPath = null;	
	private static double MK119Version = 0;
	
	public static boolean onionLogin = false;
	 
	public static OnionMember user;
	public static String userName;
	public static String userFullName;
	
	public static final String loginId = "onion";
	public static final String loginPassword = "20000901";
	
	public static String getOnionDirPath() {
		return onionDirPath;
	}

	public static void setOnionDirPath(String onionDirPath) {
		ONION_Info.onionDirPath = onionDirPath;
	}
	
	public static String getProjectDirPath() {
		return projectDirPath;
	}

	public static void setProjectDirPath(String projectDirPath) {
		ONION_Info.projectDirPath = projectDirPath;
	}

	public static String getAdminConsoleIp() {
		if((ONION_Info.sqlServerInfo != null)&&(ONION_Info.getDataBaseName() != null)) {
			return ONION_Info.sqlServerInfo[0];
		}else {
			return "127.0.0.1";
		}
	}
	
	public static String getDataBaseName() {
		return dataBaseName;
	}

	public static void setDataBaseName(String dataBaseName) {
		ONION_Info.dataBaseName = dataBaseName;
	}

	public static void setSqlServerInfo(String[] info) {
		ONION_Info.sqlServerInfo = new String[4];
		ONION_Info.sqlServerInfo[0] = info[0];
		ONION_Info.sqlServerInfo[1] = info[1];
		ONION_Info.sqlServerInfo[2] = info[2];
		ONION_Info.sqlServerInfo[3] = info[3];
	}
	
	public static void resetSqlServerInfo() {
		ONION_Info.sqlServerInfo = null;
	}
	
	public static String getSimpleSqlServerInfo() {
		if((ONION_Info.sqlServerInfo != null)&&(ONION_Info.getDataBaseName() != null)) {
			return String.format("%s:%s", ONION_Info.sqlServerInfo[0],ONION_Info.sqlServerInfo[1]);
		}else {
			return "0.0.0.0:0";
		}
	}
	
	public static String getSqlServerInfo() {
		if((ONION_Info.sqlServerInfo != null)&&(ONION_Info.getDataBaseName() != null)) {
			return String.format("%s:%s [ %s ]", ONION_Info.sqlServerInfo[0],ONION_Info.sqlServerInfo[1],ONION_Info.getDataBaseName());
		}else {
			return "0.0.0.0:0";
		}
	}

	public static Connection getMk119Connection() {
		if(mk119Connection == null) ONION_Info.setHasMk119Connection(false);
		return mk119Connection;
	}

	public static void setMk119Connection(Connection mk119Connection) {
		ONION_Info.mk119Connection = mk119Connection;
		ONION_Info.setHasMk119Connection(true);
		System.out.println("\n[ ONION_Info.setMk119Connection() : MK119 Connection 설정 완료 ]");
	}
	
	public static void closeMk119Connection() throws SQLException{
		// Connection이 존재한다면 커넥션을 닫아준다
		if(ONION_Info.getMk119Connection() != null) {
			ONION_Info.mk119Connection.close();
			ONION_Info.mk119Connection = null;			
			ONION_Info.setHasMk119Connection(false);
			System.out.println("\n[ ONION_Info.closeMk119Connection() : MK119 Connection 반환 완료 ]");
		}
	}

	public static boolean hasMk119Connection() {
		return hasMk119Connection;
	}

	public static void setHasMk119Connection(boolean hasMk119Connection) {
		ONION_Info.hasMk119Connection = hasMk119Connection;
	}
	
	public static void setHasDBResultSet(boolean hasDBResultSet) {
		ONION_Info.hasDbResultSet = hasDBResultSet;
	}
	
	public static void setDBResultSet(ResultSet rs) {
		ONION_Info.dbResultSet = rs;
		ONION_Info.setHasDBResultSet(true);
	}
	
	public static ResultSet getDBResultSet() {
		return ONION_Info.dbResultSet;
	}
	
	public static boolean isOnionMeber(String name) {		
		return OnionMember.memberMap.containsKey(name.toUpperCase());		
	}
	
	public static String getMemberName(String enName) {
		OnionMember member = OnionMember.memberMap.get(enName.toUpperCase());
		
		if(member.getFullName().contains("Moon") && member.getName().equals("정창용")) {
			ONION_Info.userName = Util.colorBlue(member.getName()) + " 개발자";
			src_en.info.ONION_Info.userName = ONION_Info.userName; 
			
		}else {
			ONION_Info.userName = member.getName();	
			src_en.info.ONION_Info.userName = ONION_Info.userName; 
		}
		
		ONION_Info.userFullName = member.getFullName();
		src_en.info.ONION_Info.userFullName = ONION_Info.userFullName; 
		
		return ONION_Info.userName;
	}

	public static double getMK119Version() {
		return MK119Version;
	}

	public static void setMK119Version(double mK119Version) {
		MK119Version = mK119Version;
	}

	
}
