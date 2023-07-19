package common.util;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;

public class FontManager {

	private static final String DEFAULT_FONT_KIND = "¸¼Àº °íµñ";
	
	private static final String FONT_DIR = "/font";
	private static final String PLAIN_FONT_FILE = FONT_DIR + "/malgun.ttf";
	private static final String BOLD_FONT_FILE = FONT_DIR + "/malgunbd.ttf";
	
	private static final HashMap<String, Font> fontMap = new HashMap<String, Font>();
	
	private static String OS;
	private static boolean windowOS = true;
	
//	static{
//		try {
//			OS = System.getProperty("os.name");
//			windowOS = (OS != null) && !OS.isEmpty() && OS.toLowerCase().contains("window");
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static Font getFont(int fontStyle, int fontSize) {
		try{
//			return (windowOS) ? getWindowSystemFont(fontStyle, fontSize) : getWindowFileFont(fontStyle, fontSize);
			return getWindowSystemFont(fontStyle, fontSize);
			
		}catch(Exception ex) {
			ex.printStackTrace();
			return new Font(DEFAULT_FONT_KIND, fontStyle, fontSize);
		}
	}
	
	public static Font getWindowSystemFont(int fontStyle, int fontSize) {
		try {
			String key = DEFAULT_FONT_KIND + ":" + fontStyle + ":" + fontSize;

			Font font = fontMap.get(key);
			
			if(font != null) {
				return font;
				
			}else{
				font = new Font(DEFAULT_FONT_KIND, fontStyle, fontSize);
				fontMap.put(key, font);
				return font;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			return new Font(DEFAULT_FONT_KIND, fontStyle, fontSize);
		}
	}
	
    public static Font getWindowFileFont(int fontStyle, float fontSize) {
        try {
        	String key = fontStyle + ":" + fontSize;
        	
        	Font font = fontMap.get(key);
        	
        	if(font != null) {
        		return font;
        		
        	}else{
        		InputStream is = (fontStyle == Font.BOLD) ? FontManager.class.getResourceAsStream(BOLD_FONT_FILE) : FontManager.class.getResourceAsStream(PLAIN_FONT_FILE);
                font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(fontStyle, fontSize);
                fontMap.put(key, font);
                return font;
        	}
            
        }catch(Exception ex){
            ex.printStackTrace();
         	return new Font("Dialog", fontStyle, (int)fontSize);
        }
    }

}
