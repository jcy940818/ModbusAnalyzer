package src_ko.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Inspecter {
	
	// MK119 성능명 허용 특수문자 : . # {} () [] _ - / :
	public static final String REGEX = "[a-z A-Z 0-9 ㄱ-ㅎ 가-힣 . # {} () \\[ \\] _ \\- / :]*$";
	
	public static boolean isVaildName(String name) {		
		Pattern pattern = Pattern.compile(REGEX);
		Matcher matcher = pattern.matcher(name);		
		return matcher.matches();		
	}
	
	// 숫자 검사기
	public static boolean isNumeric(String str) {
		return Pattern.matches("^[0-9]*$", str);
	}

	// 영어 검사기
	public static boolean isAlpha(String str) {
		return Pattern.matches("^[a-zA-Z]*$", str);
	}

	// 한국어 혹은 영어 검사기
	public static boolean isAlphaNumeric(String str) {
		return Pattern.matches("[a-zA-Z0-9]*$", str);
	}

	// 한국어 검사기
	public static boolean isKorean(String str) {
		return Pattern.matches("[가-힣]*$", str);
	}

	// 대문자 검사기
	public static boolean isUpper(String str) {
		return Pattern.matches("^[A-Z]*$", str);
	}

	// 소문자 검사기
	public static boolean isDowner(String str) {
		return Pattern.matches("^[a-z]*$", str);
	}

	// 이메일 검사기
	public static boolean isEmail(String str) {
		return Pattern.matches("^[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$", str);
	}

	// URL 검사기 (Http, Https 제외)
	public static boolean isURL(String str) {
	    return Pattern.matches("^[^((http(s?))\\:\\/\\/)]([0-9a-zA-Z\\-]+\\.)+[a-zA-Z]{2,6}(\\:[0-9]+)?(\\/\\S*)?$",
	            str);
	}

}
