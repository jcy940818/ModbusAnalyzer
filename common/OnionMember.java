package common;

import java.util.HashMap;

public class OnionMember {
	
//	public static void main(String[] args) {
//		init();
//	}
	
	public static HashMap<String, OnionMember> memberMap = new HashMap<>();
	
	private String team; // ÆÀ¸í : °³¹ßÆÀ
	private String name; // ÇÑ±Û ÀÌ¸§ : Á¤Ã¢¿ë
	private String nickName; // ´Ğ³×ÀÓ : Moon
	private String fullName; // ´Ğ³×ÀÓ(ÀÌ¸§) : Moon(Á¤Ã¢¿ë)
	
	public OnionMember(String team, String name, String nickName) {
		this.team = team;
		this.name = name;
		this.nickName = upperCaseFirst(nickName);
		
		if(this.team.equalsIgnoreCase("½Ì°¡ÆúÆÀ")) {
			this.fullName = String.format("%s", name);
		}else {
			this.fullName = String.format("%s(%s)", upperCaseFirst(nickName), name);	
		}
		
		OnionMember.memberMap.put(nickName.toUpperCase(), this);
	}
	
	public static void init() {		
		new OnionMember("ÀÓ¿ø", "Á¶Ã¢Èñ", "Chang");
		new OnionMember("ÀÓ¿ø", "À¯ÀÎÃ¶", "Chul");
		new OnionMember("ÀÓ¿ø", "ÀÌÁø±Ô", "Gianni");
		new OnionMember("ÀÓ¿ø", "ÀÌ½ÂÈñ", "Sonny");
		new OnionMember("ÀÓ¿ø", "½ÅÇöÁø", "Aaron");
		new OnionMember("ÀÓ¿ø", "ÀÌ»óÀÏ", "Sean");
		
		new OnionMember("°æ¿µÁö¿øÆÀ", "ÀÌÁÖÈñ", "Julie");
		new OnionMember("°æ¿µÁö¿øÆÀ", "ÇÏÀ¯Áø", "Jinny");		
		
		new OnionMember("°³¹ßÆÀ", "¾È±âÈ£", "Khan");
		new OnionMember("°³¹ßÆÀ", "ÀÌÇÊÁÖ", "Philip");
		new OnionMember("°³¹ßÆÀ", "¹Ú´ö¼ö", "Tony");
		new OnionMember("°³¹ßÆÀ", "Àå¹Î¿µ", "Steve");
		new OnionMember("°³¹ßÆÀ", "ÀÌ¿ëÈÆ", "Gunny");
		new OnionMember("°³¹ßÆÀ", "¼Õ½Â¹è", "Xabi");
		new OnionMember("°³¹ßÆÀ", "±è½Ã¿ì", "Shoo");
		new OnionMember("°³¹ßÆÀ", "¹è±Ù¿ë", "Yong");
		new OnionMember("°³¹ßÆÀ", "±èÅ¹Çö", "Henry");
		new OnionMember("°³¹ßÆÀ", "¾Èº´Çå", "Jackson");
		new OnionMember("°³¹ßÆÀ", "Àü¼º¹Î", "Stephen");
		new OnionMember("°³¹ßÆÀ", "±è°­¿¬", "Timothy");
		new OnionMember("°³¹ßÆÀ", "Á¤Ã¢¿ë", "Moon"); // Moon
		new OnionMember("°³¹ßÆÀ", "ÀÌ°­È£", "Evan");
		new OnionMember("°³¹ßÆÀ", "½Å¿ëÇÑ", "Hans");
		new OnionMember("°³¹ßÆÀ", "ÃÖÇüÁØ", "Elon");
		new OnionMember("°³¹ßÆÀ", "ÀÌÁøÀÍ", "Logan");
		new OnionMember("°³¹ßÆÀ", "±Ç¿¹Áø", "Roshe");
		new OnionMember("°³¹ßÆÀ", "±è´Ù¼Ø", "Kelly");
		new OnionMember("°³¹ßÆÀ", "ÀÓ±ÔÁø", "Dobby");
		
		new OnionMember("¿µ¾÷ÆÀ", "¿øÀº¼÷", "Elly");
		new OnionMember("¿µ¾÷ÆÀ", "±¸À±¸ğ", "Jacob");
		new OnionMember("¿µ¾÷ÆÀ", "±è¹Ì³ª", "Bling");
		new OnionMember("¿µ¾÷ÆÀ", "ÃÖÇüÁ¾", "Chris");
		new OnionMember("¿µ¾÷ÆÀ", "¿À»óÃ¶", "Scot");
		new OnionMember("¿µ¾÷ÆÀ", "±èÅÂ¹Î", "Harry");
		new OnionMember("¿µ¾÷ÆÀ", "¹Ú¼Ò¶ó", "Zoe");
		new OnionMember("¿µ¾÷ÆÀ", "°­±â¿ë", "Kai");
		
		new OnionMember("PSÆÀ", "³ª±¤È£", "Dan");
		new OnionMember("PSÆÀ", "ÀÓ°æÁø", "Jin");
		new OnionMember("PSÆÀ", "±è°æ³²", "Eddie");
		new OnionMember("PSÆÀ", "¾Èº´¿¬", "Andy");
		new OnionMember("PSÆÀ", "±èµ¿ÈÆ", "Kei");
		new OnionMember("PSÆÀ", "ÀåÁöÈ£", "Sky");
		new OnionMember("PSÆÀ", "¼­È­½Ä", "Alpha");
		new OnionMember("PSÆÀ", "¹Ú»çÁØ", "Juny");
		new OnionMember("PSÆÀ", "Á¶¿ìÁø", "Eric");
		new OnionMember("PSÆÀ", "¹ÚÇöÁØ", "Teri");
		new OnionMember("PSÆÀ", "¹Ú±æ¼ö", "Gray");
		new OnionMember("PSÆÀ", "Ã¤º´¿ë", "Ryan");
		new OnionMember("PSÆÀ", "ÀÌÃ¢ÈÆ", "Jake");
		new OnionMember("PSÆÀ", "Á¤±âÁØ", "Jay");		
		new OnionMember("PSÆÀ", "½ÉÅÂ¸²", "Rusil");
		new OnionMember("PSÆÀ", "Á¤°Ç", "Kun");
		new OnionMember("PSÆÀ", "ÀÌ±ÔÈ£", "Metthew");
		new OnionMember("PSÆÀ", "ÃÖ¼º¿ì", "Zeno");
		new OnionMember("PSÆÀ", "Â÷ÁöÈÆ", "Kevin");
		new OnionMember("PSÆÀ", "ÇÑ¹ÎÈñ", "Mia");
		new OnionMember("PSÆÀ", "¿©È¯¹Î", "Matt");
		new OnionMember("PSÆÀ", "ÀÌÀç¼º", "Woody");
		new OnionMember("PSÆÀ", "ÀÌÇüÂù", "Rick");
		new OnionMember("PSÆÀ", "±è¿ø±â", "Dustin");
		
		new OnionMember("½Ì°¡ÆúÆÀ", "Kelvin", "Kelvin");
		new OnionMember("½Ì°¡ÆúÆÀ", "Jia Hui", "Jia");
		new OnionMember("½Ì°¡ÆúÆÀ", "Jia Hui", "Jia Hui");
		new OnionMember("½Ì°¡ÆúÆÀ", "SoonMeng", "Soon");
		new OnionMember("½Ì°¡ÆúÆÀ", "SoonMeng", "SoonMeng");
		new OnionMember("½Ì°¡ÆúÆÀ", "Tee", "Tee");
		new OnionMember("½Ì°¡ÆúÆÀ", "Miffy", "Miffy");
		
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
