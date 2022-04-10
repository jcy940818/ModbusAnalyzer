package common.server;

public class ServerGroup {
	public static final String GET_SERVERGROUP = "SELECT * FROM SERVERGROUP";
	public static final String GET_SERVERGROUPMAP = "SELECT * FROM SERVERGROUPMAP";

	private int groupIndex;
	private int parentIndex;
	private String groupName;
	
	public int getGroupIndex() {
		return groupIndex;
	}
	public int getParentIndex() {
		return parentIndex;
	}
	public String getGroupName() {
		return groupName;
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
}
