package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import src_ko.info.ONION_Info;
import src_ko.util.FileUtil;
import src_ko.util.Util;

public class OnionDirCheck_Panel extends JPanel {	
		
	public static JCheckBox isProject_checkBox;
	
	public static String agent = null;
	
	private static JTextField onionDirPath_TextField;
	private static JLabel msgLabel;
	private static JButton checkDirPathButton;
	
	// isProject == true : 프로젝트 디렉토리의 프로토콜 리스트를 다운로드
	// isProject == false : OnionSoftware 디렉토리의 프로토콜 리스트를 다운로드
	public static boolean isProject = false;
	
	public static JButton back_button;
	private static JLabel mk119Icon;
	private static JLabel mk119Icon2;
	private static JLabel mk119Version;
	private static JLabel mk119BuildVersion;
	private static JButton xmlEditButton;
	private static JButton goDirButton;
	private static JLabel onionDirPathLabel;
	
	private static JRadioButton ko_button;
	private static JRadioButton en_button;	
	
	public OnionDirCheck_Panel() {
		// size : 1074, 628
		setBorder(new EmptyBorder(12, 12, 12, 12));
		setSize(1074, 628);
		setBackground(new Color(255, 140, 0));
		setLayout(new BorderLayout(0, 0));

		JPanel actualPanel = new JPanel();
		actualPanel.setSize(1050, 610);
		actualPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		actualPanel.setBackground(Color.WHITE);		
		add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);

		JLabel imageLabel = new JLabel();		
		imageLabel.setBounds(-462, -23, 898, 680);
		actualPanel.add(imageLabel);
		imageLabel.setIcon(new Util().getOnionDirImage());
		
		JLabel dirImageLabel = new JLabel();
		dirImageLabel.setBounds(458, 71, 82, 108);
		dirImageLabel.setIcon(new Util().getFolderImage());
		actualPanel.add(dirImageLabel);
		
		onionDirPathLabel = new JLabel("OnionSoftware Path");
		onionDirPathLabel.setForeground(Color.BLACK);
		onionDirPathLabel.setFont(FontManager.getFont(Font.BOLD, 26));
		onionDirPathLabel.setBounds(552, 97, 395, 47);
		actualPanel.add(onionDirPathLabel);
		
		onionDirPath_TextField = new JTextField();
		onionDirPath_TextField.setHorizontalAlignment(SwingConstants.LEFT);		
		onionDirPath_TextField.setFont(FontManager.getFont(Font.PLAIN, 18));
		onionDirPath_TextField.setBorder(new LineBorder(Color.BLACK, 2));
		onionDirPath_TextField.setBounds(437, 189, 510, 41);
		onionDirPath_TextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkDirPathButton.doClick();
			}
		});
		onionDirPath_TextField.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						if(file != null) {
							onionDirPath_TextField.setText(file.getAbsolutePath());
							checkDirPathButton.doClick();
							return;
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(onionDirPath_TextField);
		
		checkDirPathButton = new JButton("확 인");
		checkDirPathButton.setForeground(Color.BLACK);
		checkDirPathButton.setBackground(Color.WHITE);
		checkDirPathButton.setFont(FontManager.getFont(Font.BOLD, 16));
		checkDirPathButton.setBounds(953, 189, 85, 41);
		checkDirPathButton.setFocusPainted(false);
		checkDirPathButton.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {	
				
				try {
					String path = onionDirPath_TextField.getText();
					
					if(path == null || path.length() < 1) {
						showComponent(false);
						return;
					}else {
						path = path.trim(); 
					}
									
					boolean isOnionDir = checkOnionDir(path, isProject);
					
					if(isOnionDir) {
						ONION_Info.setOnionDirPath(path);
						ONION_Info.setProjectDirPath(path);
						
						String version = FileUtil.getMK119BuildVersion(isProject).replace("build", "Build");
						
						if(version.contains("Fail")) {
							System.out.println("MK119 VersionInfo Load Fail");
							showComponent(false);
							return;
						}
						
						mk119BuildVersion.setText(version);
						ProtocolList_Panel.setProtocolVersion(version);
						ONION_Info.setMK119Version(Double.parseDouble(version.split(" ")[1]));
						showComponent(true);
					}else {
						showComponent(false);
					}
				}catch(Exception ex) {
					showComponent(false);
				}
				
			}
		});
		actualPanel.add(checkDirPathButton);
		
		JButton searchButton = new JButton("탐 색");
		searchButton.setForeground(Color.BLACK);
		searchButton.setFont(FontManager.getFont(Font.BOLD, 16));
		searchButton.setFocusPainted(false);
		searchButton.setBackground(Color.WHITE);
		searchButton.setBounds(953, 140, 85, 41);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = Util.getFilePath();
				if(path != null) {
					onionDirPath_TextField.setText(path);					
				}	
				checkDirPathButton.doClick();
			}
		});
		actualPanel.add(searchButton);
		
		msgLabel = new JLabel();
		msgLabel.setForeground(Color.RED);
		msgLabel.setHorizontalAlignment(SwingConstants.LEFT);
		msgLabel.setFont(FontManager.getFont(Font.PLAIN, 13));
		msgLabel.setBounds(437, 236, 601, 22);
		actualPanel.add(msgLabel);
		
		mk119Icon = new JLabel();
		mk119Icon.setIcon(new Util().getMK2Resource());
		mk119Icon.setHorizontalAlignment(SwingConstants.LEFT);
		mk119Icon.setForeground(new Color(237, 76, 55));
		mk119Icon.setFont(FontManager.getFont(Font.BOLD, 23));
		mk119Icon.setBackground(Color.WHITE);
		mk119Icon.setBounds(437, 281, 76, 41);
		actualPanel.add(mk119Icon);
		
		
		mk119Icon2 = new JLabel();
		mk119Icon2.setIcon(new Util().getMK2Resource());
		mk119Icon2.setHorizontalAlignment(SwingConstants.LEFT);
		mk119Icon2.setForeground(new Color(237, 76, 55));
		mk119Icon2.setFont(FontManager.getFont(Font.BOLD, 23));
		mk119Icon2.setBackground(Color.WHITE);
		mk119Icon2.setBounds(455, 46, 76, 41);
		actualPanel.add(mk119Icon2);
		
		mk119Version = new JLabel("Version");
		mk119Version.setHorizontalAlignment(SwingConstants.LEFT);
		mk119Version.setForeground(new Color(237, 76, 55));
		mk119Version.setFont(FontManager.getFont(Font.BOLD, 23));
		mk119Version.setBackground(Color.WHITE);
		mk119Version.setBounds(518, 279, 264, 41);
		actualPanel.add(mk119Version);
		
		mk119BuildVersion = new JLabel("MK119 Build Version");
		mk119BuildVersion.setHorizontalAlignment(SwingConstants.LEFT);		
		mk119BuildVersion.setForeground(Color.BLUE);
		mk119BuildVersion.setFont(FontManager.getFont(Font.BOLD, 21));
		mk119BuildVersion.setBackground(Color.WHITE);
		mk119BuildVersion.setBounds(437, 321, 601, 41);
		actualPanel.add(mk119BuildVersion);
		
		xmlEditButton = new JButton(" 성능 XML 뷰어 열기");
		xmlEditButton.setForeground(Color.BLACK);
		xmlEditButton.setFocusPainted(false);
		xmlEditButton.setBackground(Color.WHITE);
		xmlEditButton.setFont(FontManager.getFont(Font.BOLD, 20));
		xmlEditButton.setHorizontalAlignment(SwingConstants.LEFT);
		xmlEditButton.setBounds(437, 419, 337, 69);
		xmlEditButton.setIcon(new Util().getXMLImage());
		xmlEditButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String path = (isProject) ? ONION_Info.getProjectDirPath() : ONION_Info.getOnionDirPath();
				String language = (ko_button.isSelected()) ? "ko" : "en";				
				String xmlpath = (isProject) ? (path + "\\conf\\" + language + "\\fms") : (path + "\\midknight\\conf\\" + language + "\\fms");
				
				File xmlDir = new File(xmlpath);
				
				if(!xmlDir.exists()) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s\n", Util.colorRed("Can not found XML Directory Path")));
					sb.append(String.format("아래의 경로에서 XML 디렉토리를 찾을 수 없습니다%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("Path : %s%s%s\n", 
							xmlpath.replace("\\", Util.colorRed("\\")),
							Util.separator,
							Util.separator));
					
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				}else {
					MainFrame.showXmlEditor(xmlDir, FileUtil.getProtocolList(isProject));	
				}
				
			}
		});
		actualPanel.add(xmlEditButton);
		
		goDirButton = new JButton(" 성능 XML 경로 열기");
		goDirButton.setForeground(Color.BLACK);
		goDirButton.setFocusPainted(false);
		goDirButton.setBackground(Color.WHITE);
		goDirButton.setFont(FontManager.getFont(Font.BOLD, 20));
		goDirButton.setHorizontalAlignment(SwingConstants.LEFT);
		goDirButton.setBounds(437, 505, 337, 69);
		goDirButton.setIcon(new Util().getFolder2Image());
		goDirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String path = (isProject) ? ONION_Info.getProjectDirPath() : ONION_Info.getOnionDirPath();
				String language = (ko_button.isSelected()) ? "ko" : "en";
				String xmlpath = (isProject) ? (path + "\\conf\\" + language + "\\fms") : (path + "\\midknight\\conf\\" + language + "\\fms");
				
				File xmlDir = new File(xmlpath);
				
				if(!xmlDir.exists()) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("%s\n", Util.colorRed("Can not found XML Directory Path")));
					sb.append(String.format("아래의 경로에서 XML 디렉토리를 찾을 수 없습니다%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("Path : %s%s%s\n", 
							xmlpath.replace("\\", Util.colorRed("\\")),
							Util.separator,
							Util.separator));
					
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				}else {
					FileUtil.openFile(xmlDir);	
				}
				
			}
		});
		actualPanel.add(goDirButton);
		
		JLabel onionLogo = new JLabel();
		onionLogo.setIcon(new Util().getMainLogoResource());
		onionLogo.setBounds(665, 184, 376, 547);
		actualPanel.add(onionLogo);
		
		isProject_checkBox = new JCheckBox(" MK119 Project");
		isProject_checkBox.setForeground(Color.BLACK);
		isProject_checkBox.setSelected(true);
		isProject_checkBox.setFocusPainted(false);
		isProject_checkBox.setBackground(Color.WHITE);
		isProject_checkBox.setFont(FontManager.getFont(Font.BOLD, 18));
		isProject_checkBox.setBounds(874, 6, 168, 45);
		isProject_checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(isProject_checkBox.isSelected()) {
					// MK119 프로젝트 프로토콜 다운로드
					isProject = true;
					changeSatate();
				}else {
					// OnionSoftware 디렉토리 프로토콜 다운로드
					isProject = false;
					changeSatate();
				}
				
			}
		});
		actualPanel.add(isProject_checkBox);
		
		if(!isProject)setPath();
		
		ko_button = new JRadioButton("ko");
		ko_button.setFocusPainted(false);
		ko_button.setBackground(Color.WHITE);
		ko_button.setFont(FontManager.getFont(Font.BOLD, 20));
		ko_button.setForeground(new Color(0, 128, 0));
		ko_button.setBounds(437, 375, 60, 30);
		ko_button.setSelected(true);
		ko_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JRadioButton button = (JRadioButton)e.getSource();				
				String text = button.getText();
				
				if(text.contains("ko")) {
					ko_button.setForeground(new Color(0, 128, 0));
					en_button.setForeground(Color.LIGHT_GRAY);
				}else{
					ko_button.setForeground(Color.LIGHT_GRAY);
					en_button.setForeground(new Color(0, 128, 0));
				}
				
			}
		});
		actualPanel.add(ko_button);
		
		en_button = new JRadioButton("en");
		en_button.setFocusPainted(false);
		en_button.setForeground(Color.LIGHT_GRAY);
		en_button.setFont(FontManager.getFont(Font.BOLD, 20));
		en_button.setBackground(Color.WHITE);
		en_button.setBounds(501, 375, 60, 30);
		en_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JRadioButton button = (JRadioButton)e.getSource();				
				String text = button.getText();
				
				if(text.contains("ko")) {
					ko_button.setForeground(new Color(0, 128, 0));
					en_button.setForeground(Color.LIGHT_GRAY);
				}else{
					ko_button.setForeground(Color.LIGHT_GRAY);
					en_button.setForeground(new Color(0, 128, 0));
				}
				
			}
		});
		actualPanel.add(en_button);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(ko_button);
		buttonGroup.add(en_button);
		
		back_button = new JButton("돌아가기");
		back_button.setForeground(Color.BLACK);
		back_button.setFont(FontManager.getFont(Font.BOLD, 16));
		back_button.setFocusPainted(false);
		back_button.setBackground(Color.WHITE);
		back_button.setBounds(437, 6, 103, 41);
		back_button.setVisible(false);
		back_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				MainFrame.showModbusMonitor();
				
				if(!ImportModbusPointFrame.isExist) {
					new ImportModbusPointFrame();
				}else {
					ImportModbusPointFrame.existsFrame();
				}
				
				OnionDirCheck_Panel.back_button.setEnabled(false);
				OnionDirCheck_Panel.back_button.setVisible(false);
			}
		});
		actualPanel.add(back_button);
		
		// 컴포넌트는 기본 설정으로 안보임
		showComponent(false);
		setVisible(true);
	}
	
	public static void changeSatate() {
		if(isProject) {
			// MK119 프로젝트 프로토콜 다운로드
			isProject_checkBox.setSelected(isProject);
			mk119Icon2.setVisible(true);
			onionDirPathLabel.setText("MK119 Project Path");
			setPath();
			msgLabel.setText(" MK119 프로젝트 디렉토리 경로상에 공백(\" \") 문자가 포함되면 안됩니다");
			showComponent(false);
		}else {
			// OnionSoftware 디렉토리 프로토콜 다운로드
			isProject_checkBox.setSelected(isProject);
			mk119Icon2.setVisible(false);
			onionDirPathLabel.setText("OnionSoftware Path");			
			setPath();
			msgLabel.setText(" OnionSoftware 디렉토리 경로상에 공백(\" \") 문자가 포함되면 안됩니다");
			showComponent(false);
		}
	}
	
	public static boolean checkOnionDir(String path, boolean isProject) {
		if(isProject) {
			// MK119 프로젝트 디렉토리 검사
			return true;
		}else {
			// OnionSoftware 디렉토리 검사
			return FileUtil.isOnionDir(path);
		}
	}
	
	public static void setPath() {
		
		if(isProject) {
			onionDirPath_TextField.setText("");			
			return;
		}
		
		String C = "C:\\";
		String D = "D:\\";
		
		// D 드라이브 우선
		boolean hasOnionDir = FileUtil.hasOnionDir(D);
		
		if(hasOnionDir) {
			onionDirPath_TextField.setText(D + "OnionSoftware");			
			return;
		}
		
		hasOnionDir = FileUtil.hasOnionDir(C);
		
		if(hasOnionDir) {
			onionDirPath_TextField.setText(C + "OnionSoftware");
			return;
		}
	}
	
	public static  void showComponent(boolean show) {
		mk119Icon.setVisible(show);
		mk119Version.setVisible(show);
		mk119BuildVersion.setVisible(show);
		xmlEditButton.setVisible(show);
		goDirButton.setVisible(show);
		ko_button.setVisible(show);
		en_button.setVisible(show);	
	}
}
