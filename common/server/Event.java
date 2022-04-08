package common.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import src_ko.info.ONION_Info;

public class Event {
	
	/**
     * 특정 장비에 발생 또는 인지 상태의 이벤트가 있는가
     * 
     * @param index
     * @return
     * @throws SQLException
     */
    public boolean isEvent(int index) throws SQLException{
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT nStatus ");
        sql.append("FROM EVENTS WITH(NOLOCK) ");
        sql.append("WHERE (nStatus = 0 or nStatus = 1) and nServerIndex = " + index);

        try {
            conn = ONION_Info.getMk119Connection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toString());
            if(rs==null){
            	return false;
            }
            else
            return true;
        } catch(Exception e) {
        	e.printStackTrace();
        	return false;
        }
    }
    
    
    /**
     * 장비에서 발생한 이벤트에서 최저 상태 중 최고 레벨 이벤트를 얻는다.
     * @param index 장비 ID
     * @return 최고 레벨 이벤트
     * @throws SQLException
     */
	public String getEventLogServer(int index) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		try {
			conn = ONION_Info.getMk119Connection();
			stmt = conn.createStatement();

			sql.append("SELECT a.*, b.strSeverity, ");
			sql.append("b.nBkColor, ");
			sql.append("b.nTextColor ");
			sql.append("FROM EVENTS a WITH(NOLOCK), SYSTEM_SEVERITY b WITH(NOLOCK) ");
			sql.append("WHERE a.nSeverity = b.nSeverity ");
			sql.append("AND nServerIndex = (").append(index).append(")");
			sql.append("AND nStatus=");
			sql.append("(SELECT MIN(nStatus) FROM EVENTS WITH(NOLOCK) WHERE nServerIndex = (").append(index)
					.append(")) ");
			sql.append("ORDER BY a.nSeverity DESC");

			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				// 수행 내용
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
