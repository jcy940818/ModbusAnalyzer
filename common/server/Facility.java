package common.server;

import java.util.ArrayList;

import common.perf.Perf;

public class Facility implements Comparable {
	private String groupInfo; // 그룹 정보
	private String group;
	
	private int index; // 장비 인덱스	
	private String name; // 장비명
	
	private int facType; // 시설물 종류
	private String facTypeString;
	
	private int connCode; // 연결 방식
	private String connMethod;
	
	private boolean isCommon; // 프로토콜 정보
	private int commProtocol;
	private int snmpProtocol;
	
	private int conditionCode; // 현재 상태
	private String conditionString;
	
	private ArrayList<Perf> perfs;

	@Override
	public int compareTo(Object obj) {
		Facility fac =  (Facility)obj;
		
		if(this.index < fac.getIndex()) {
			return -1;
		}else if(this.index == fac.getIndex()) {
			return 0;
		}else {
			return 1;
		}
	}

	public String getGroupInfo() {
		return groupInfo;
	}

	public void setGroupInfo(String groupInfo) {
		this.groupInfo = groupInfo;
		String[] groups = groupInfo.split(" ");
		this.group = groups[groups.length - 1];
	}

	public String getGroup() {
		return group;
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

	public int getFacType() {
		return facType;
	}

	public void setFacType(int facType) {
		this.facType = facType;
	}

	public String getFacTypeString() {
		return facTypeString;
	}

	public void setFacTypeString(String facTypeString) {
		this.facTypeString = facTypeString;
	}

	public int getConnCode() {
		return connCode;
	}

	public void setConnCode(int connCode) {
		this.connCode = connCode;
	}

	public String getConnMethod() {
		return connMethod;
	}

	public void setConnMethod(String connMethod) {
		this.connMethod = connMethod;
	}

	public boolean isCommon() {
		return isCommon;
	}

	public void setCommon(boolean isCommon) {
		this.isCommon = isCommon;
	}
	
	public int getCommProtocol() {
		return commProtocol;
	}

	public void setCommProtocol(int commProtocol) {
		this.commProtocol = commProtocol;
	}

	public int getSnmpProtocol() {
		return snmpProtocol;
	}

	public void setSnmpProtocol(int snmpProtocol) {
		this.snmpProtocol = snmpProtocol;
	}

	public int getConditionCode() {
		return conditionCode;
	}

	public void setConditionCode(int conditionCode) {
		this.conditionCode = conditionCode;
	}

	public String getConditionString() {
		return conditionString;
	}

	public void setConditionString(String conditionString) {
		this.conditionString = conditionString;
	}

	public ArrayList<Perf> getPerfs() {
		return perfs;
	}

	public void setPerfs(ArrayList<Perf> perfs) {
		this.perfs = perfs;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
