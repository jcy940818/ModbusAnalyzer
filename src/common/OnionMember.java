package common;

import java.util.HashMap;

public class OnionMember {
	
	public static HashMap<String, OnionMember> memberMap = new HashMap<>();
	
	public static OnionMember loginUser;
	
	private String team; // 팀명 : 개발팀
	private String name; // 한글 이름 : 정창용
	private String nickName; // 닉네임 : Moon
	private String fullName; // 닉네임(이름) : Moon(정창용)
	
	public OnionMember(String team, String name, String nickName) {
		this.team = team;
		this.name = name;
		this.nickName = upperCaseFirst(nickName);
		
		if(this.team.equalsIgnoreCase("싱가폴팀")) {
			this.fullName = String.format("%s", name);
		}else {
			this.fullName = String.format("%s(%s)", upperCaseFirst(nickName), name);	
		}
		
		OnionMember.memberMap.put(nickName.toUpperCase(), this);
	}
	
	public static void init() {
		new OnionMember("개발팀", "정창용", "Moon"); // Moon
	}
	
	public String getTeam() {
		return team;
	}
	public String getName() {
		return name;
	}
	public String getNickName() {
		return nickName;
	}
	public String getFullName() {
		return fullName;
	}
	public String toString() {
		return "Team : " + this.team
				+ " / Name : " + this.name
				+ " / NickName : " + this.nickName
				+ " / FullName : " + this.fullName;
	}
	
	public static String upperCaseFirst(String text) {
		text = text.toLowerCase();
		char[] arr = text.toCharArray();
		arr[0] = Character.toUpperCase(arr[0]);
		return new String(arr);
	}
}
