package moon;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import common.OnionMember;
import src_ko.swing.PremiumLoginFrame;
import src_ko.util.FileUtil;
import src_ko.util.Util;

public class Moon {

	public static String currentLanguage = "Moon";
	
	private static JFrame mainFrame;
	private static JPanel actualPanel;
	private static CardLayout cardLayout;
	
	public static final String KO = "ko";
	public static final String EN = "en";
	
	private static src_ko.swing.MainFrame ko = new src_ko.swing.MainFrame();
	private static src_en.swing.MainFrame en = new src_en.swing.MainFrame();
	
	private static JPanel ko_panel = ko.getActualPanel();
	private static JPanel en_panel = en.getActualPanel();
	
	private static JMenuBar ko_menuBar = ko.getJMenuBar();
	private static JMenuBar en_menuBar = en.getJMenuBar();
	
	private static ComponentEvent componentEvent;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OnionMember.init();
					
					mainFrame = new JFrame();
					actualPanel = new JPanel();
					
					cardLayout = new CardLayout(0, 0);
					actualPanel.setLayout(cardLayout);
					actualPanel.add(ko_panel, Moon.KO);
					actualPanel.add(en_panel, Moon.EN);
					mainFrame.setContentPane(actualPanel);
					mainFrame.setTitle("ModbusAnalyzer");
					initFrame(mainFrame);
										
					showFrame(Moon.KO);
					
					autoAuth();

					// **************** 프레임 제목 업데이트 스레드 *************************************************
					new Thread(new Runnable() {
						public void run() {
							while(true) {								
								try {			
									Thread.sleep(500);
									
									if (Moon.isKorean()) {
										updateTitle(mainFrame, ko);												
									}else {
										updateTitle(mainFrame, en);										
									}
										
									if(mainFrame.isResizable()) {
										ComponentListener[] listeners = mainFrame.getComponentListeners();
										for(ComponentListener c : listeners) {
											if(c != null && componentEvent != null) c.componentResized(componentEvent);
										}
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
	
	public static boolean isKorean() {
		return currentLanguage.equalsIgnoreCase(Moon.KO);
	}
	
	public static void initFrame(JFrame frame) {		
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
	
	public static JFrame getMainFrame() {
		return mainFrame;
	}
	
	public static JPanel getActualPanel() {
		return actualPanel;
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
	
	public static void setVerticalResizable(boolean enabled) {
		ComponentListener[] listener = mainFrame.getComponentListeners();
		boolean haveListener = (listener != null && listener.length > 0);
		if(enabled && haveListener) {
			return;
		}
		
		if(enabled) {
			Dimension cur = mainFrame.getPreferredSize();
			
			mainFrame.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					mainFrame.setMinimumSize(new Dimension(cur.width + 10, cur.height + 10));
					mainFrame.setSize(new Dimension(cur.width + 10, mainFrame.getHeight()));
					componentEvent = e;
					super.componentResized(e);
				}
			});
			mainFrame.setResizable(true);
			
		}else {
			
			for(ComponentListener c : listener) {			
				mainFrame.removeComponentListener(c);
			}
			componentEvent = null;
			initFrame(mainFrame);
		}
	}
	
	public static void autoAuth() {
		try {
			String path = Util.getCurrentPath(Moon.class);
			if(path != null && !path.isEmpty()) {
				File dir = new File(path);
				if(dir.exists() && dir.isDirectory()) {
					if(FileUtil.isOnionDir(dir.getParent())) {
						new PremiumLoginFrame(false, false).loginSuccess(false);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
