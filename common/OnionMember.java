package common;

import java.util.HashMap;

public class OnionMember {
	
//	public static void main(String[] args) {
//		init();
//	}
	
	public static HashMap<String, OnionMember> memberMap = new HashMap<>();
	
	private String team; // ���� : ������
	private String name; // �ѱ� �̸� : ��â��
	private String nickName; // �г��� : Moon
	private String fullName; // �г���(�̸�) : Moon(��â��)
	
	public OnionMember(String team, String name, String nickName) {
		this.team = team;
		this.name = name;
		this.nickName = upperCaseFirst(nickName);
		
		if(this.team.equalsIgnoreCase("�̰�����")) {
			this.fullName = String.format("%s", name);
		}else {
			this.fullName = String.format("%s(%s)", upperCaseFirst(nickName), name);	
		}
		
		OnionMember.memberMap.put(nickName.toUpperCase(), this);
	}
	
	public static void init() {		
		new OnionMember("�ӿ�", "��â��", "Chang");
		new OnionMember("�ӿ�", "����ö", "Chul");
		new OnionMember("�ӿ�", "������", "Gianni");
		new OnionMember("�ӿ�", "�̽���", "Sonny");
		new OnionMember("�ӿ�", "������", "Aaron");
		new OnionMember("�ӿ�", "�̻���", "Sean");
		
		new OnionMember("�濵������", "������", "Julie");
		new OnionMember("�濵������", "������", "Jinny");		
		
		new OnionMember("������", "�ȱ�ȣ", "Khan");
		new OnionMember("������", "������", "Philip");
		new OnionMember("������", "�ڴ���", "Tony");
		new OnionMember("������", "��ο�", "Steve");
		new OnionMember("������", "�̿���", "Gunny");
		new OnionMember("������", "�ս¹�", "Xabi");
		new OnionMember("������", "��ÿ�", "Shoo");
		new OnionMember("������", "��ٿ�", "Yong");
		new OnionMember("������", "��Ź��", "Henry");
		new OnionMember("������", "�Ⱥ���", "Jackson");
		new OnionMember("������", "������", "Stephen");
		new OnionMember("������", "�谭��", "Timothy");
		new OnionMember("������", "��â��", "Moon"); // Moon
		new OnionMember("������", "�̰�ȣ", "Evan");
		new OnionMember("������", "�ſ���", "Hans");
		new OnionMember("������", "������", "Elon");
		new OnionMember("������", "������", "Logan");
		new OnionMember("������", "�ǿ���", "Roshe");
		new OnionMember("������", "��ټ�", "Kelly");
		new OnionMember("������", "�ӱ���", "Dobby");
		
		new OnionMember("������", "������", "Elly");
		new OnionMember("������", "������", "Jacob");
		new OnionMember("������", "��̳�", "Bling");
		new OnionMember("������", "������", "Chris");
		new OnionMember("������", "����ö", "Scot");
		new OnionMember("������", "���¹�", "Harry");
		new OnionMember("������", "�ڼҶ�", "Zoe");
		new OnionMember("������", "�����", "Kai");
		
		new OnionMember("PS��", "����ȣ", "Dan");
		new OnionMember("PS��", "�Ӱ���", "Jin");
		new OnionMember("PS��", "��泲", "Eddie");
		new OnionMember("PS��", "�Ⱥ���", "Andy");
		new OnionMember("PS��", "�赿��", "Kei");
		new OnionMember("PS��", "����ȣ", "Sky");
		new OnionMember("PS��", "��ȭ��", "Alpha");
		new OnionMember("PS��", "�ڻ���", "Juny");
		new OnionMember("PS��", "������", "Eric");
		new OnionMember("PS��", "������", "Teri");
		new OnionMember("PS��", "�ڱ��", "Gray");
		new OnionMember("PS��", "ä����", "Ryan");
		new OnionMember("PS��", "��â��", "Jake");
		new OnionMember("PS��", "������", "Jay");		
		new OnionMember("PS��", "���¸�", "Rusil");
		new OnionMember("PS��", "����", "Kun");
		new OnionMember("PS��", "�̱�ȣ", "Metthew");
		new OnionMember("PS��", "�ּ���", "Zeno");
		new OnionMember("PS��", "������", "Kevin");
		new OnionMember("PS��", "�ѹ���", "Mia");
		new OnionMember("PS��", "��ȯ��", "Matt");
		new OnionMember("PS��", "���缺", "Woody");
		new OnionMember("PS��", "������", "Rick");
		new OnionMember("PS��", "�����", "Dustin");
		new OnionMember("PS��", "���ظ�", "Joon");
		
		new OnionMember("�̰�����", "Kelvin", "Kelvin");
		new OnionMember("�̰�����", "Jia Hui", "Jia");
		new OnionMember("�̰�����", "Jia Hui", "Jia Hui");
		new OnionMember("�̰�����", "SoonMeng", "Soon");
		new OnionMember("�̰�����", "SoonMeng", "SoonMeng");
		new OnionMember("�̰�����", "Tee", "Tee");
		new OnionMember("�̰�����", "Miffy", "Miffy");
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
