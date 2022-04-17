package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import src_ko.main.MoonInspector;
import src_ko.util.Util;

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
		
		setSize(480, 255);
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
		mk119Link.setBounds(12, 10, 248, 50);
		mk119Link.setHorizontalAlignment(SwingConstants.LEFT);
		mk119Link.setFont(new Font("맑은 고딕", Font.BOLD, 21));
		actualPanel.add(mk119Link);
				
		JButton linkMK119Protocol_Button = new JButton("<html>&nbsp;<font color='blue'>Protocol</font> 정보 데이터 연동</html>");
		linkMK119Protocol_Button.setHorizontalAlignment(SwingConstants.LEFT);
		linkMK119Protocol_Button.setIcon(new Util().getMK2Resource());
		linkMK119Protocol_Button.setForeground(Color.BLACK);
		linkMK119Protocol_Button.setBackground(Color.WHITE);
		linkMK119Protocol_Button.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		linkMK119Protocol_Button.setFocusPainted(false);
		linkMK119Protocol_Button.setBounds(60, 97, 365, 37);		
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
		
		JButton linkMK119PerfData_Button = new JButton("<html>&nbsp;<font color='green'>REST API</font> 성능 데이터 연동</html>");
		linkMK119PerfData_Button.setHorizontalAlignment(SwingConstants.LEFT);
		linkMK119PerfData_Button.setIcon(new Util().getMK2Resource());
		linkMK119PerfData_Button.setForeground(Color.BLACK);
		linkMK119PerfData_Button.setBackground(Color.WHITE);
		linkMK119PerfData_Button.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		linkMK119PerfData_Button.setFocusPainted(false);
		linkMK119PerfData_Button.setBounds(60, 155, 365, 37);
		linkMK119PerfData_Button.setEnabled(true);
		linkMK119PerfData_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				dispose();
			}
		});
		actualPanel.add(linkMK119PerfData_Button);
		
		if(linkProtocol) {
			linkMK119Protocol_Button.setText("<html>&nbsp;<font color='blue'>Protocol</font> 정보 데이터 연동 완료</html>");
			linkMK119Protocol_Button.setEnabled(false);
		}
		if(linkPerfData) {
			linkMK119PerfData_Button.setText("<html>&nbsp;<font color='green'>REST API</font> 성능 데이터 연동 완료</html>");
			linkMK119PerfData_Button.setEnabled(false);
		}
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
