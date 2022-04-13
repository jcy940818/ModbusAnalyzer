package common.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerGroup {
	
	public static String ROOT = "<ROOT>";
	
	public static final String GET_SERVERGROUP = "SELECT * FROM SERVERGROUP ORDER BY nGroupIndex";	
	public static final String GET_SERVERGROUPMAP = "SELECT * FROM SERVERGROUPMAP";
	
	private int groupIndex;
	private int parentIndex;
	private String groupName;
	private ServerGroup parentGroup;
	
	public ServerGroup(int groupIndex, int parentIndex, String groupName) {
		this.groupIndex = groupIndex;
		this.parentIndex = parentIndex;
		this.groupName = groupName;
	}
	
	@Override
	public String toString() {
		return this.getTree();
	}

	public static HashMap<Integer, ServerGroup> getServerGroup(Connection conn) throws SQLException{
		HashMap<Integer, ServerGroup> groupMap = new HashMap<Integer, ServerGroup>();
		ArrayList<ServerGroup> groupList = new ArrayList<ServerGroup>();
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {		
			stmt = conn.createStatement();
			rs = stmt.executeQuery(GET_SERVERGROUP);
			
			boolean isRoot = true;
			while (rs.next()) {
				int groupIndex = rs.getInt("nGroupIndex");
				int parentIndex = rs.getInt("nParentIndex");
				String groupName = rs.getString("strGroupName");
				
				ServerGroup group = new ServerGroup(groupIndex, parentIndex, groupName);
				
				groupList.add(group);
				groupMap.put(group.getGroupIndex(), group);
				
				if(isRoot) ServerGroup.ROOT = group.getGroupName();
				isRoot = false;
			}
			
			for(ServerGroup group : groupList) {
				int parentIndex = group.getParentIndex();			
				if(groupMap.containsKey(parentIndex)) {
					ServerGroup parentGroup = groupMap.get(parentIndex);
					group.setParentGroup(parentGroup);
				}else {
					group.setParentGroup(null);
				}
			}
	
			return groupMap;
		}finally {
			if(rs != null && !rs.isClosed()) rs.close();
			if(stmt != null && !stmt.isClosed()) stmt.close();
		}
	}
	
	public static HashMap<Integer, Integer> getServerGroupMapping(Connection conn) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			HashMap<Integer, Integer> groupMap = new HashMap<Integer, Integer>();
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(GET_SERVERGROUPMAP);
			
			while (rs.next()) {
				int serverIndex = rs.getInt("nServerIndex");
				int groupIndex = rs.getInt("nGroupIndex");
				groupMap.put(serverIndex, groupIndex);
			}
			
			return groupMap;
		}finally {
			if(rs != null && !rs.isClosed()) rs.close();
			if(stmt != null && !stmt.isClosed()) stmt.close();
		}
	}
	
	// 재귀호출 주의!
	public String getTree() {
		if(this.getParentGroup() != null) {
			return this.getParentGroup().getTree() + " > " + this.getGroupName();
		}else {
			return this.getGroupName();
		}
	}

	public int getGroupIndex() {
		return groupIndex;
	}
	public int getParentIndex() {
		return parentIndex;
	}
	public String getGroupName() {
		return groupName;
	}
	public ServerGroup getParentGroup() {
		return parentGroup;
	}
	public void setGroupIndex(int groupIndex) {
		this.groupIndex = groupIndex;
	}
	public void setParentIndex(int parentIndex) {
		this.parentIndex = parentIndex;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public void setParentGroup(ServerGroup parentGroup) {
		this.parentGroup = parentGroup;
	}
}
