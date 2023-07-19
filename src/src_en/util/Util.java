package src_en.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.net.URLDecoder;
import java.security.CodeSource;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import common.util.FontManager;

public class Util {
	
	public static String separator = "&nbsp;&nbsp;&nbsp;";
	public static String longSeparator = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	
	public static FocusListener focusListener = new FocusListener() {
		public void focusLost(FocusEvent e) {
			JTextField text = (JTextField)e.getSource();
			text.setBorder(UIManager.getBorder("TextField.border"));
		}
		
		@Override
		public void focusGained(FocusEvent e) {
			JTextField text = (JTextField)e.getSource();
			text.setBorder(new LineBorder(Color.black, 2));			
		}
	};
	
	public ImageIcon getMK119Resource() {
		String MK119Resource = "MK119.png";
		String ImageFile = MK119Resource;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	public ImageIcon getMK2Resource() {
		String MK119Resource = "MK2.png";
		String ImageFile = MK119Resource;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	public ImageIcon getMK119BackResource() {
		// MK119 AdminConsole Background : RGB (72,77,83)
		String MK119backResource = "mk119back.gif";
		String ImageFile = MK119backResource;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
		
	public ImageIcon getMainLogoResource() {
		String mainLogoResource = "ONIONSoftware.png";
		String ImageFile = mainLogoResource;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}

	public ImageIcon getSubLogoResource() {
		String subLogoResource = "ONION.png";
		String ImageFile = subLogoResource;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}

	public ImageIcon getIconResource() {
		String iconResource = "ONIONIcon.png";
		String ImageFile = iconResource;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	public ImageIcon getIconResource2() {
		String iconResource = "ONIONIcon2.png";
		String ImageFile = iconResource;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	public ImageIcon getMoonResource() {
		String Moon = "Moon.png";
		String ImageFile = Moon;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
		
	public ImageIcon getMoonCardResource() {
		String MoonCard = "MoonCard.PNG";
		String ImageFile = MoonCard;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	
	public ImageIcon getOnionItemResource(int itemIndex) {
		String onionItem = String.format("onionItem%d.png", itemIndex);
		String ImageFile = onionItem;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	public ImageIcon getDataCenterResource() {
		String dataCenter = "dataCenter.png";
		String ImageFile = dataCenter;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	public ImageIcon getOnionScreenResource() {
		String onionScreen = "ONION_Screen.jpg";
		String ImageFile = onionScreen;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}		
	
	public ImageIcon getLoginImage() {
		String loginImage = "login.png";
		String ImageFile = loginImage;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	public ImageIcon getLoginFormImage() {
		String loginImage = "loginForm.png";
		String ImageFile = loginImage;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	public ImageIcon getFolderImage() {
		String dirImage = "dir.png";
		String ImageFile = dirImage;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	public ImageIcon getFolder2Image() {
		String dirImage = "dir2.png";
		String ImageFile = dirImage;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	public ImageIcon getOnionDirImage() {
		String onionDirImage = "onionDir.jpg";
		String ImageFile = onionDirImage;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	public ImageIcon getExcelImage() {
		String excelImage = "excel.png";
		String ImageFile = excelImage;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	public ImageIcon getXMLImage() {
		String xmlImage = "xml.png";
		String ImageFile = xmlImage;
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(ImageFile));
		return icon;
	}
	
	public static String getCurrentPath(Class aclass) throws Exception {				
		CodeSource codeSource = aclass.getProtectionDomain().getCodeSource();

		File jarFile;

		if (codeSource.getLocation() != null) {
			jarFile = new File(codeSource.getLocation().toURI());
		} else {
			String path = aclass.getResource(aclass.getSimpleName() + ".class").getPath();
			String jarFilePath = path.substring(path.indexOf(":") + 1, path.indexOf("!"));
			jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
			jarFile = new File(jarFilePath);
		}
		return jarFile.getParentFile().getAbsolutePath();
	}
	
	public static String colorRed(String str) {
		return String.format("<font color='red'>%s</font>", str);
	}
	
	public static String colorGreen(String str) {
		return String.format("<font color='green'>%s</font>", str);
	}	
	
	public static String colorBlue(String str) {
		return String.format("<font color='blue'>%s</font>", str);
	}
	
	public static String colorOrange(String str) {
		return String.format("<font color='orange'>%s</font>", str);
	}
	
	public static void showMessage(String msg, int msgType) {
		msg = msg.replaceFirst("\n", "<br><br>").replaceAll("\n", "<br>");
		JLabel label = new JLabel("<html><font color='black'>" + msg + "<br></font></html>");
		label.setFont(FontManager.getFont(Font.BOLD, 15));
		JOptionPane.showMessageDialog(null, label, "ModbusAnalyzer", msgType);
	}
	
	public static int showConfirm(String msg) {
		msg = msg.replaceFirst("\n", "<br><br>").replaceAll("\n", "<br>");
		JLabel label = new JLabel("<html><font color='black'>" + msg + "<br></font></html>");
		label.setFont(FontManager.getFont(Font.BOLD, 15));		
		return JOptionPane.showConfirmDialog(null, label, "ModbusAnalyzer", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);		
	}

	public static int showOption(String msg, String[] options, int msgType) {
		msg = msg.replaceFirst("\n", "<br><br>").replaceAll("\n", "<br>");
		JLabel label = new JLabel("<html><font color='black'>" + msg + "<br></font></html>");		
		label.setFont(FontManager.getFont(Font.BOLD, 15));						
		return JOptionPane.showOptionDialog(null, label, "ModbusAnalyzer", JOptionPane.DEFAULT_OPTION, msgType, null, options, null);		
	}
	
	public static int showOption(String msg, String[] options, int msgType, boolean useLineSeparator) {
		if(useLineSeparator) {
			msg = msg.replaceFirst("\n", "<br><br>").replaceAll("\n", "<br>");
		}else {
			msg = msg.replaceAll("\n", "<br>");
		}
		JLabel label = new JLabel("<html><font color='black'>" + msg + "<br></font></html>");		
		label.setFont(FontManager.getFont(Font.BOLD, 15));						
		return JOptionPane.showOptionDialog(null, label, "ModbusAnalyzer", JOptionPane.DEFAULT_OPTION, msgType, null, options, null);		
	}
	
	public static Object showInput(String msg, int msgType) {
		msg = msg.replaceFirst("\n", "<br><br>").replaceAll("\n", "<br>");
		JLabel label = new JLabel("<html><font color='black'>" + msg + "<br></font></html>");		
		label.setFont(FontManager.getFont(Font.BOLD, 15));		
		return JOptionPane.showInputDialog(null, label, "ModbusAnalyzer", msgType);
	}
	
	// JOptionPane 커스텀 버전
	public static void userChoice() {		
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='blue'>Modbus Analyzer</font>\n");
		msg.append("어떤 작업을 수행 하시겠습니까?\n");

		int menu = Util.showOption(msg.toString(), new String[] { "첫 번째 버튼", "두 번째 버튼", "세 번째 버튼" }, JOptionPane.WARNING_MESSAGE);

		switch (menu) {
			case -1: // 사용자가 메뉴를 선택하지 않고 대화상자를 나갔을 때
			case 0: // 첫 번째 버튼
			case 1: // 두 번째 버튼
			case 2: // 세 번째 버튼
		}
	}

	// 디렉토리 경로 반환
	public static String getFilePath() {

		JFileChooser chooser = null;

		try {
			chooser = new JSystemFileChooser();
			chooser.setPreferredSize(new Dimension(800, 500));
			chooser.setDialogTitle("ModbusAnalyzer");
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			setFileChooserFont(chooser.getComponents()); // FileChooser 폰트 변경
			chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		} catch (Exception e) {
			// 예외 발생시 아무것도 수행하지 않음			
			return null;
		}

		int ret = chooser.showSaveDialog(null);
		// 경로를 선택하지 않을 시 null 리턴
		if (ret != JFileChooser.APPROVE_OPTION)
			return null;

		String path = chooser.getSelectedFile().getPath().trim();
		System.out.println("Selected Path : " + path);
		return path;
	}

	// FileChooser 클래스 폰트를 설정해주는 메소드
	public static void setFileChooserFont(Component[] comp) {
		java.awt.Font font = new java.awt.Font("맑은 고딕", java.awt.Font.PLAIN, 14);
		for (int i = 0; i < comp.length; i++) {
			if (comp[i] instanceof Container)
				setFileChooserFont(((Container) comp[i]).getComponents());
			try {
				comp[i].setFont(font);
			} catch (Exception e) {
				// 아무것도 하지 않는다
			}
		}
	}

}// end Moon Util
