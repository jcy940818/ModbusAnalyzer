package common.util;

public class TextUtil {
	
	public static String setTextStyle(String text, int front, int back) {
		StringBuilder sb = new StringBuilder();
		sb.append("<span style=\"background-color:#" + getColorHexString(back) + ";\">");
		sb.append("<font color='#" + getColorHexString(front) + "'>");
		sb.append("&nbsp;&nbsp;");
		sb.append(text);
		sb.append("&nbsp;&nbsp;");
		sb.append("</font>");
		sb.append("</span>");
		return sb.toString();
	}

	public static String getColorHexString(int colorInt) {
		String str = "000000" + Integer.toHexString(colorInt);
		return str.substring(str.length() - 6);
	}
	
}
