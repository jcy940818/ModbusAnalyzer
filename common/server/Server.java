package common.server;

import common.util.AlphanumComparator;

public class Server implements Comparable {

	public static final int TYPE_FACILITY = 16;
	public static final int TYPE_RTU = 8;

	public static final String NO_GROUP_KO = "장비 관리 ( 그룹 없음 )";
	public static final String NO_GROUP_EN = "Devices ( No Group )";

	private int agentType; // 8 : RCU, 16 : 시설물
	private String groupInfo;

	private int index;
	private String name;

	private int type;
	private String typeString;

	private int stateCode; // SERVER_CONDITION
	private String state;

	public boolean isFacility() {
		return (this.agentType == 16);
	}

	public int getAgentType() {
		return agentType;
	}

	public void setAgentType(int agentType) {
		this.agentType = agentType;
	}

	public String getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(String groupInfo) {
		if (!groupInfo.contains(">")) {
			this.groupInfo = groupInfo;
		} else {
			this.groupInfo = groupInfo.substring(groupInfo.indexOf(" > ") + 3);
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int compareTo(Object obj) {
		Server server = (Server) obj;

		int compareName = AlphanumComparator.comparator.compare(this.name, server.name);

		if (this.groupInfo.equals(Server.NO_GROUP_KO) || server.groupInfo.equals(Server.NO_GROUP_KO)) {
			boolean thisNoGroup = this.groupInfo.equals(Server.NO_GROUP_KO);
			boolean facNoGroup = server.groupInfo.equals(Server.NO_GROUP_KO);

			if (!thisNoGroup && facNoGroup) {
				return -1;
			} else if (thisNoGroup && facNoGroup) {

				if (compareName < 0) {
					return -1;
				} else if (compareName == 0) {
					return 0;
				} else {
					return 1;
				}

			} else if (thisNoGroup && !facNoGroup) {
				return 1;
			}
		}

		int compareGroup = AlphanumComparator.comparator.compare(this.groupInfo, server.groupInfo);

		if (compareGroup < 0) {
			return -1;
		} else if (compareGroup == 0) {

			if (compareName < 0) {
				return -1;
			} else if (compareName == 0) {
				return 0;
			} else {
				return 1;
			}

		} else {
			return 1;
		}
	}

}
