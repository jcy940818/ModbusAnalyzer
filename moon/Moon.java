package moon;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import src_en.swing.MainFrame;
import src_ko.util.Util;

public class Moon {
					
	private static CardLayout cardLayout;
	
	public static String KO = "ko";
	public static String EN = "en";
	
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
					JFrame frame = new JFrame();
					JPanel actualPanel = new JPanel();
					
					cardLayout = new CardLayout(0, 0);
					actualPanel.setLayout(cardLayout);
					actualPanel.add(ko_panel, KO);
					actualPanel.add(en_panel, EN);
					frame.setContentPane(actualPanel);					
					initFrame(frame);							
					
					showFrame(frame, KO);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});				
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
	
	public static void showFrame(JFrame frame, String language) {
		cardLayout.show(frame.getContentPane(), language);
		
		if(language.equals(KO)) {			
			frame.setJMenuBar(ko_menuBar);			
		}else if(language.equals(EN)) {			
			frame.setJMenuBar(en_menuBar);	
		}else {
			frame.setJMenuBar(ko_menuBar);
		}
		
		frame.pack();
	}
	
}
