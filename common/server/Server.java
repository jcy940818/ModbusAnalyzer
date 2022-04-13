package common.server;

import java.util.ArrayList;

import common.util.AlphanumComparator;

public class Server implements Comparable {

	public static final int TYPE_FACILITY = 16;
	public static final int TYPE_RTU = 8;

	private boolean overlapping = false;
	private ArrayList<Event> events = new ArrayList<Event>();
	private String ip;
	private int agentType; // 8 : RCU, 16 : ½Ã¼³¹°	

	private ServerGroup group;
	private int index;
	private String name;

	private int type;
	private String typeString;

	private int stateCode; // SERVER_CONDITION
	private String state;

	public boolean isFacility() {
		return (this.agentType == TYPE_FACILITY);
	}
	
	public boolean isRCU() {
		return (this.agentType == TYPE_RTU);
	}
	
	public boolean hasEvent() {
		return (events.size() > 0);
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getAgentType() {
		return agentType;
	}

	public void setAgentType(int agentType) {
		this.agentType = agentType;
	}

	public ServerGroup getGroup() {
		return group;
	}

	public void setGroup(ServerGroup group) {
		this.group = group;
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

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	public boolean isOverlapping() {
		return overlapping;
	}

	public void setOverlapping(boolean overlapping) {
		this.overlapping = overlapping;
	}


	@Override
	public int compareTo(Object obj) {
		Server server = (Server) obj;

		if(this.getGroup() == null || server.getGroup() == null) {
			return 0;
		}
		
		int compareName = AlphanumComparator.comparator.compare(this.name, server.name);
		
		if (this.getGroup().getTree().equals(ServerGroup.ROOT) || server.getGroup().getTree().equals(ServerGroup.ROOT)) {
			boolean thisNoGroup = this.getGroup().getTree().equals(ServerGroup.ROOT);
			boolean facNoGroup = server.getGroup().getTree().equals(ServerGroup.ROOT);

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

		int compareGroup = AlphanumComparator.comparator.compare(this.getGroup().getTree(), server.getGroup().getTree());

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
