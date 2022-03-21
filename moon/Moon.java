package moon;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import common.OnionMember;
import src_ko.swing.PremiumLoginFrame;
import src_ko.util.Util;

public class Moon {
	
	public static String currentLanguage = "Moon";
	
	private static JFrame mainFrame;
	private static CardLayout cardLayout;
	
	public static final String KO = "ko";
	public static final String EN = "en";
	
	private static src_ko.swing.MainFrame ko = new src_ko.swing.MainFrame();
	private static src_en.swing.MainFrame en = new src_en.swing.MainFrame();
	
	private static JPanel ko_panel = ko.getActualPanel();
	private static JPanel en_panel = en.getActualPanel();
	
	private static JMenuBar ko_menuBar = ko.getJMenuBar();
	private static JMenuBar en_menuBar = en.getJMenuBar();	
		
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OnionMember.init();
					
					mainFrame = new JFrame();
					JPanel actualPanel = new JPanel();
					
					cardLayout = new CardLayout(0, 0);
					actualPanel.setLayout(cardLayout);
					actualPanel.add(ko_panel, Moon.KO);
					actualPanel.add(en_panel, Moon.EN);
					mainFrame.setContentPane(actualPanel);
					initFrame(mainFrame);
										
					showFrame(Moon.KO);
					
					// 테스트 코드 : 자동 사용자 인증
					new PremiumLoginFrame(true).loginSuccess(true);
					
					// **************** 프레임 제목 업데이트 스레드 *************************************************
					new Thread(new Runnable() {
						public void run() {
							while(true) {								
								try {			
									Thread.sleep(500);
									
									if (currentLanguage.equalsIgnoreCase(Moon.KO)) {
										updateTitle(mainFrame, ko);												
									}else {
										updateTitle(mainFrame, en);										
									}
									
								}catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}).start();
					//************************************************************************************************
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});				
	}
	
	public static void initFrame(JFrame frame) {
		frame.setTitle("ModbusAnalyzer");
		frame.setBounds(100, 100, 1080, 680);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(new Util().getIconResource().getImage());
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setPreferredSize(new Dimension(1074,628));
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void updateTitle(JFrame frame, JFrame sourceFrame) {
		try {
			frame.setTitle(sourceFrame.getTitle());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showFrame(String language) {
		cardLayout.show(mainFrame.getContentPane(), language);
		
		if(language.equals(KO)) {			
			if(currentLanguage.equals(Moon.KO)) return;			
			currentLanguage = Moon.KO;
			mainFrame.setJMenuBar(ko_menuBar);
		}else {			
			if(currentLanguage.equals(Moon.EN)) return;			
			currentLanguage = Moon.EN;
			mainFrame.setJMenuBar(en_menuBar);
		}
		
		mainFrame.pack();
	}
	
}
