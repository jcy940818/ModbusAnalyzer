package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import src_ko.info.ONION_Info;
import src_ko.main.MoonInspector;
import src_ko.util.Util;
import javax.swing.JSeparator;
import java.awt.SystemColor;

public class LinkMK119Frame extends JFrame{
	
//	public static void main(String[] args) {
//		new LinkMK119Frame(false, false);
//	}
	
	/**
	 * Create the panel.
	 */
	public LinkMK119Frame(boolean linkProtocol, boolean linkPerfData) {
		setTitle("ModbusAnalyzer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(new Util().getIconResource().getImage());
		setResizable(false);
		
		setSize(545, 673);
		setBackground(Color.WHITE);
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel actualPanel = new JPanel();		
		actualPanel.setBorder(new LineBorder(new Color(255, 140, 0), 5));
		actualPanel.setBackground(Color.WHITE);
		getContentPane().add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		JLabel mk119Link = new JLabel("MK119 Data Link");
		mk119Link.setForeground(new Color(237, 76, 55));
//		mk119Link.setForeground(Color.BLACK);
		mk119Link.setBackground(Color.WHITE);
		mk119Link.setIcon(new Util().getSubLogoResource());
		mk119Link.setBounds(12, 10, 515, 50);
		mk119Link.setHorizontalAlignment(SwingConstants.LEFT);
		mk119Link.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 21));
		actualPanel.add(mk119Link);
				
		JButton linkMK119Protocol_Button = new JButton("<html>&nbsp;<font color='blue'>Protocol</font> ¡§∫∏ µ•¿Ã≈Õ ø¨µø</html>");
		linkMK119Protocol_Button.setHorizontalAlignment(SwingConstants.LEFT);
		linkMK119Protocol_Button.setIcon(new Util().getMK2Resource());
		linkMK119Protocol_Button.setForeground(Color.BLACK);
		linkMK119Protocol_Button.setBackground(Color.WHITE);
		linkMK119Protocol_Button.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 16));
		linkMK119Protocol_Button.setFocusPainted(false);
		linkMK119Protocol_Button.setBounds(53, 316, 365, 37);		
		linkMK119Protocol_Button.setEnabled(true);
		linkMK119Protocol_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OnionDirCheck_Panel.connectServerList = true;
				OnionDirCheck_Panel.back_button.setVisible(true);
				MainFrame.showOnionDirCheck(MoonInspector.isMoon());
				dispose();
			}
		});
		actualPanel.add(linkMK119Protocol_Button);
		
		JButton linkMK119PerfData_Button = new JButton("<html>&nbsp;<font color='green'>REST API</font> º∫¥… µ•¿Ã≈Õ ø¨µø</html>");
		linkMK119PerfData_Button.setHorizontalAlignment(SwingConstants.LEFT);
		linkMK119PerfData_Button.setIcon(new Util().getMK2Resource());
		linkMK119PerfData_Button.setForeground(Color.BLACK);
		linkMK119PerfData_Button.setBackground(Color.WHITE);
		linkMK119PerfData_Button.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 16));
		linkMK119PerfData_Button.setFocusPainted(false);
		linkMK119PerfData_Button.setBounds(53, 516, 365, 37);
		linkMK119PerfData_Button.setEnabled(true);
		linkMK119PerfData_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdminConsole_LoginFrame.showLoginForm(ONION_Info.getSimpleSqlServerInfo(), "MK119Lite");
				dispose();
			}
		});
		actualPanel.add(linkMK119PerfData_Button);
		
		JLabel mk119DB = new JLabel("1. MK119 Database Information");
		mk119DB.setHorizontalAlignment(SwingConstants.LEFT);
		mk119DB.setForeground(new Color(0, 128, 0));
		mk119DB.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 20));
		mk119DB.setBackground(Color.WHITE);
		mk119DB.setBounds(27, 82, 500, 37);
		actualPanel.add(mk119DB);
		
		JLabel mk119SqlServer = new JLabel("SQL Server : " + ONION_Info.getSimpleSqlServerInfo());
		mk119SqlServer.setHorizontalAlignment(SwingConstants.LEFT);
		mk119SqlServer.setForeground(Color.BLACK);
		mk119SqlServer.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 17));
		mk119SqlServer.setBackground(Color.WHITE);
		mk119SqlServer.setBounds(53, 117, 474, 37);
		actualPanel.add(mk119SqlServer);
		
		JLabel mk119DbName = new JLabel("DB Name : " + ONION_Info.getDataBaseName());
		mk119DbName.setHorizontalAlignment(SwingConstants.LEFT);
		mk119DbName.setForeground(Color.BLACK);
		mk119DbName.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 17));
		mk119DbName.setBackground(Color.WHITE);
		mk119DbName.setBounds(53, 154, 474, 37);
		actualPanel.add(mk119DbName);
		
		JLabel mk119ProtocolDataLink = new JLabel("2. MK119 Protocol Data Link");
		mk119ProtocolDataLink.setHorizontalAlignment(SwingConstants.LEFT);
		mk119ProtocolDataLink.setForeground(new Color(0, 128, 0));
		mk119ProtocolDataLink.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 20));
		mk119ProtocolDataLink.setBackground(Color.WHITE);
		mk119ProtocolDataLink.setBounds(27, 223, 500, 37);
		actualPanel.add(mk119ProtocolDataLink);
		
		JLabel mk119ProtocolVersion = new JLabel("Protocol : «¡∑Œ≈‰ƒ› ¡§∫∏ ø¨µø ¿¸");
		mk119ProtocolVersion.setHorizontalAlignment(SwingConstants.LEFT);
		mk119ProtocolVersion.setForeground(Color.BLACK);
		mk119ProtocolVersion.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 17));
		mk119ProtocolVersion.setBackground(Color.WHITE);
		mk119ProtocolVersion.setBounds(53, 270, 474, 37);
		actualPanel.add(mk119ProtocolVersion);
		
		JLabel mk119RestDataLink = new JLabel("3. MK119 REST API Data Link");
		mk119RestDataLink.setHorizontalAlignment(SwingConstants.LEFT);
		mk119RestDataLink.setForeground(new Color(0, 128, 0));
		mk119RestDataLink.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 20));
		mk119RestDataLink.setBackground(Color.WHITE);
		mk119RestDataLink.setBounds(27, 397, 500, 37);
		actualPanel.add(mk119RestDataLink);
		
		JLabel mk119RestStatus = new JLabel("Status : REST API ø¨µø ¿¸");
		mk119RestStatus.setHorizontalAlignment(SwingConstants.LEFT);
		mk119RestStatus.setForeground(Color.BLACK);
		mk119RestStatus.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 17));
		mk119RestStatus.setBackground(Color.WHITE);
		mk119RestStatus.setBounds(53, 432, 474, 37);
		actualPanel.add(mk119RestStatus);
		
		JLabel mk119RestAdminInfo = new JLabel("AdminConsole : REST API ø¨µø ¿¸");
		mk119RestAdminInfo.setHorizontalAlignment(SwingConstants.LEFT);
		mk119RestAdminInfo.setForeground(Color.BLACK);
		mk119RestAdminInfo.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 17));
		mk119RestAdminInfo.setBackground(Color.WHITE);
		mk119RestAdminInfo.setBounds(53, 469, 474, 37);
		actualPanel.add(mk119RestAdminInfo);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(160, 160, 160));
		separator.setBounds(5, 210, 528, 19);
		actualPanel.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(SystemColor.controlShadow);
		separator_1.setBounds(5, 380, 528, 19);
		actualPanel.add(separator_1);
		
		if(linkProtocol && linkPerfData) {
			mk119Link.setText("MK119 Data Link Completed");
		}
		if(linkProtocol) {
			linkMK119Protocol_Button.setText("<html>&nbsp;<font color='blue'>Protocol</font> ¡§∫∏ µ•¿Ã≈Õ ø¨µø øœ∑·</html>");
			linkMK119Protocol_Button.setEnabled(false);
		}
		if(linkPerfData) {
			linkMK119PerfData_Button.setText("<html>&nbsp;<font color='green'>REST API</font> º∫¥… µ•¿Ã≈Õ ø¨µø øœ∑·</html>");
			linkMK119PerfData_Button.setEnabled(false);
		}
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
