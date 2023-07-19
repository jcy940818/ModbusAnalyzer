package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import common.LanguagePanel;
import common.Program_Info;
import common.util.FontManager;
import moon.Moon;
import src_ko.info.ONION_Info;
import src_ko.util.Util;

public class Information_Panel extends JPanel {
	
	public static final String Version = Program_Info.PROGRAM_VERSION;	
	
	// 정보 프레임에 표시되는 어니언 제품 개수
	public static final int ONION_ITEM_COUNT = 9;		
	
	private static JLabel versionLabel;
	private PremiumLoginFrame loginFrame;
	public static LanguagePanel LanguagePanel;
	
	public static JLabel user;
	public static JLabel userName;
	
	private int itemIndex = 0;
	float direction = -0.05f;	
	
	private static JPanel actualPanel; 
	
	public Information_Panel() {
		setBorder(new EmptyBorder(12, 12, 12, 12));
		// size : 1074, 628
		setSize(1074, 628);
		setBackground(new Color(255, 140, 0));

		setLayout(new BorderLayout(0, 0));
		actualPanel = new JPanel();
		actualPanel.setSize(1050, 610);
		actualPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		actualPanel.setBackground(Color.WHITE);
		add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		JLabel programInfo = new JLabel("Program Information");
		programInfo.setForeground(Color.BLACK);
		programInfo.setIcon(new Util().getSubLogoResource());
		programInfo.setHorizontalAlignment(SwingConstants.LEFT);
		programInfo.setHorizontalAlignment(SwingConstants.LEFT);
		programInfo.setFont(FontManager.getFont(Font.BOLD, 24));
		programInfo.setBounds(12, 10, 493, 55);
		actualPanel.add(programInfo);
		
		JLabel progreamName = new JLabel("ModbusAnalyzer");
		progreamName.setForeground(Color.BLACK);
		progreamName.setHorizontalAlignment(SwingConstants.LEFT);
		progreamName.setFont(FontManager.getFont(Font.BOLD, 22));
		progreamName.setBounds(23, 90, 255, 55);
		actualPanel.add(progreamName);
		
		versionLabel = new JLabel(String.format("<html>Version <font color='blue'>%s</font></html>", Version));
		versionLabel.setForeground(Color.BLACK);
		versionLabel.setHorizontalAlignment(SwingConstants.LEFT);
		versionLabel.setFont(FontManager.getFont(Font.BOLD, 22));
		versionLabel.setBounds(211, 90, 570, 55);
		actualPanel.add(versionLabel);
		
		JLabel LanguageLabel = new JLabel("Language : ");
		LanguageLabel.setHorizontalAlignment(SwingConstants.LEFT);
		LanguageLabel.setForeground(Color.BLACK);
		LanguageLabel.setFont(FontManager.getFont(Font.BOLD, 22));
		LanguageLabel.setBounds(23, 150, 126, 55);
		actualPanel.add(LanguageLabel);
		
		
		LanguagePanel = new LanguagePanel(Moon.KO);
		LanguagePanel.setBounds(153, 152, 255, 55);
		actualPanel.add(LanguagePanel);
		
		
		JLabel copyright = new JLabel("Copyright : ");
		copyright.setForeground(Color.BLACK);
		copyright.setHorizontalAlignment(SwingConstants.LEFT);
		copyright.setFont(FontManager.getFont(Font.BOLD, 22));
		copyright.setBounds(23, 215, 126, 55);
		actualPanel.add(copyright);
		
		JLabel companyName = new JLabel("ONION Software");
		companyName.setIcon(new Util().getSubLogoResource());
		companyName.setForeground(new Color(255, 140, 0));
		companyName.setHorizontalAlignment(SwingConstants.LEFT);
		companyName.setFont(FontManager.getFont(Font.BOLD, 22));
		companyName.setBounds(151, 215, 255, 55);
		companyName.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				 if (e.getButton() == 1) {  } // 왼쪽 클릭				 
				 if (e.getButton() == 1 && e.getClickCount() == 2) { showLoginForm(false); } // 더블 클릭				 
				 if (e.getButton() == 3) { showLoginForm(false); } // 오른쪽 클릭
			}
		});
		actualPanel.add(companyName);
		
		JLabel MoonCard = new JLabel();
		MoonCard.setBounds(5, 350, 508, 250);
		MoonCard.setIcon(new Util().getMoonCardResource());
		actualPanel.add(MoonCard);
		
		user = new JLabel("Developer : ");
		user.setForeground(Color.BLACK);
		user.setHorizontalAlignment(SwingConstants.LEFT);
		user.setFont(FontManager.getFont(Font.BOLD, 22));
		user.setBounds(23, 285, 482, 55);
		user.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				 if (e.getButton() == 1) {  } // 왼쪽 클릭				 
				 if (e.getButton() == 1 && e.getClickCount() == 2) { showLoginForm(true); } // 더블 클릭				 
				 if (e.getButton() == 3) { showLoginForm(true); } // 오른쪽 클릭
			}
		});
		actualPanel.add(user);
		
		userName = new JLabel("Moon(정창용)");
		userName.setForeground(Color.BLACK);
		userName.setIcon(new Util().getMoonResource());
		userName.setHorizontalAlignment(SwingConstants.LEFT);
		userName.setFont(FontManager.getFont(Font.BOLD, 22));
		userName.setBounds(151, 285, 354, 55);
		actualPanel.add(userName);
		
		
		FadeLabel onionItem = new FadeLabel();		
		onionItem.setIcon(new Util().getOnionItemResource(itemIndex));
		onionItem.setHorizontalAlignment(SwingConstants.CENTER);
		onionItem.setFont(FontManager.getFont(Font.BOLD, 22));
		onionItem.setBounds(530, 244, 533, 383);
		actualPanel.add(onionItem);
		
		
		JLabel dataCenterImage = new JLabel();		
		dataCenterImage.setIcon(new Util().getDataCenterResource());
		dataCenterImage.setHorizontalAlignment(SwingConstants.LEFT);
		dataCenterImage.setFont(FontManager.getFont(Font.BOLD, 22));
		dataCenterImage.setBounds(517, 0, 570, 637);
		actualPanel.add(dataCenterImage);		
		
		
		// Fade in / out
		Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float alpha = onionItem.getAlpha();
                
                if(alpha == 0) {
                	itemIndex += 1;
                	if(itemIndex >= (ONION_ITEM_COUNT+1)) itemIndex = 0;
                	onionItem.setIcon(new Util().getOnionItemResource(itemIndex));
                }
                
                alpha += direction;
                                
                if (alpha < 0) {
                    alpha = 0;
                    direction = 0.05f;
                }else if (alpha > 1) {
                    alpha = 1;
                    direction = -0.05f;
                }
                onionItem.setAlpha(alpha);
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.start();		
	}

	public void showLoginForm(boolean isMoon) {
		if(ONION_Info.onionLogin) {
			if(ONION_Info.userFullName != null) {	
				if(ONION_Info.userFullName.contains("정창용")) {
					
					// 관리자 로그인 인증 완료후 수행되는 로직					
					Util.showMessage(String.format("<font color='blue'>관리자 인증 완료</font>\n %s님 관리자 인증 완료되었습니다%s\n", ONION_Info.userFullName, Util.separator), JOptionPane.INFORMATION_MESSAGE);
										
				}else {
					Util.showMessage(String.format("<font color='blue'>사용자 인증 완료</font>\n %s님 사용자 인증 완료되었습니다%s\n", ONION_Info.userFullName, Util.separator), JOptionPane.INFORMATION_MESSAGE);
				}	
			}else {
				Util.showMessage(String.format("<font color='blue'>사용자 인증 완료</font>\n사용자 인증 완료되었습니다%s\n", Util.separator), JOptionPane.INFORMATION_MESSAGE);
			}
		 }else {
			 if(!PremiumLoginFrame.isExist) loginFrame = new PremiumLoginFrame(isMoon, true);	 			 
		 }
	}
		
	public static void setVersion(String version) {
		Information_Panel.versionLabel.setText(String.format("<html>Version <font color='red'>%s</font></html>", version));
	}
	
	public static void loginOnionMemeber(String member) {
		if(member.equalsIgnoreCase("Moon")) {
			user.setText("<html><font color='blue'>Developer</font> : </html>");
			userName.setIcon(new Util().getMoonResource());
			userName.setText(ONION_Info.userFullName);						
			
			src_en.swing.Information_Panel.user.setText("<html><font color='blue'>Developer</font> : </html>");
			src_en.swing.Information_Panel.userName.setIcon(new Util().getMoonResource());
			src_en.swing.Information_Panel.userName.setText(ONION_Info.userFullName);
		}else {
			user.setText(String.format("<html><font color='orange'>ONION Member</font> : %s님</html>", ONION_Info.userFullName));
			userName.setIcon(null);
			userName.setText(null);
			
			src_en.swing.Information_Panel.user.setText(String.format("<html><font color='orange'>ONION Member</font> : %s님</html>", ONION_Info.userFullName));
			src_en.swing.Information_Panel.userName.setIcon(null);
			src_en.swing.Information_Panel.userName.setText(null);
		}
	}
		
}
