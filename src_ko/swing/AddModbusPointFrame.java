package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import src_ko.util.FileUtil;
import src_ko.util.Util;

public class AddModbusPointFrame extends JFrame {

	private Color mkColor = new Color(237, 76, 55);
	public static boolean isExist = false;
	private JPanel contentPane;
	private JButton mk119Button;
	private JTable table; // frame¸¶´Ù XML ÀÎ½ºÅÏ½º¸¦ °¡Á®¾ß ÇÏ¹Ç·Î table ÇÊµå´Â static ¼Ó¼ºÀ» °¡Áú ¼ö ¾ø´Ù

	private JRadioButton mk_V4_RaidoButton;
	private JRadioButton mk_V10_RaidoButton;
	
	private JButton upload_protocol;
	private JButton upload_xml;
	private JButton upload_excel;
	
	private JButton download_template;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddModbusPointFrame frame = new AddModbusPointFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AddModbusPointFrame() {
		isExist = true;
		setTitle("ModbusAnalyzer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setIconImage(new Util().getIconResource().getImage());
				
		setBounds(100, 100, 1009, 676);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(255, 140, 0), 8));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBorder(null);
		actualPanel.setBackground(Color.WHITE);
		contentPane.add(actualPanel, BorderLayout.CENTER);		
		actualPanel.setLayout(null);
		
		JLabel currentFunction = new JLabel("Modbus Watch Point Upload");
		currentFunction.setForeground(Color.BLACK);
		currentFunction.setIcon(new Util().getSubLogoResource());
		currentFunction.setHorizontalAlignment(SwingConstants.LEFT);
		currentFunction.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 22));
		currentFunction.setBackground(Color.WHITE);
		currentFunction.setBounds(0, 0, 380, 55);
		actualPanel.add(currentFunction);
		
				
		mk119Button = new JButton(new Util().getMK2Resource());
		mk119Button.setForeground(Color.BLACK);
		mk119Button.setText(" Button");
		mk119Button.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 17));
		mk119Button.setFocusPainted(false);
		mk119Button.setContentAreaFilled(false);
		mk119Button.setBorder(UIManager.getBorder("Button.border"));
		mk119Button.setBackground(Color.WHITE);
		mk119Button.setBounds(786, 11, 189, 36);		
		actualPanel.add(mk119Button);
		
		JPanel backGround_Panel = new JPanel();
		backGround_Panel.setBackground(Color.LIGHT_GRAY);
		backGround_Panel.setBounds(10, 57, 965, 564);
		actualPanel.add(backGround_Panel);
		backGround_Panel.setLayout(null);
		
		JPanel mk119Version_Panel = new JPanel();
		mk119Version_Panel.setBackground(Color.WHITE);
		mk119Version_Panel.setBounds(12, 10, 200, 106);
		backGround_Panel.add(mk119Version_Panel);
		mk119Version_Panel.setLayout(null);
		
		JLabel mk119Logo = new JLabel();		
		mk119Logo.setBackground(Color.WHITE);
		mk119Logo.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 20));
		mk119Logo.setHorizontalAlignment(SwingConstants.LEFT);
		mk119Logo.setForeground(Color.BLACK);
		mk119Logo.setIcon(new Util().getMK2Resource());
		mk119Logo.setBounds(12, 10, 85, 26);
		mk119Version_Panel.add(mk119Logo);
		
		JLabel mk119Version = new JLabel();
		mk119Version.setBackground(Color.WHITE);
		mk119Version.setText("Version");
		mk119Version.setForeground(mkColor);
		mk119Version.setHorizontalAlignment(SwingConstants.LEFT);		
		mk119Version.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 20));
		mk119Version.setBounds(92, 8, 94, 26);
		mk119Version_Panel.add(mk119Version);
		
		mk_V4_RaidoButton = new JRadioButton("MK119  V4");
		mk_V4_RaidoButton.setSelected(true);
		mk_V4_RaidoButton.setHorizontalAlignment(SwingConstants.LEFT);
		mk_V4_RaidoButton.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 18));
		mk_V4_RaidoButton.setForeground(mkColor);
		mk_V4_RaidoButton.setBackground(Color.WHITE);
		mk_V4_RaidoButton.setBounds(25, 45, 170, 23);
		mk_V4_RaidoButton.setFocusPainted(false);
		mk_V4_RaidoButton.addActionListener(mkVerionListener);
		mk119Version_Panel.add(mk_V4_RaidoButton);
		
		mk_V10_RaidoButton = new JRadioButton("MK119  V10");
		mk_V10_RaidoButton.setHorizontalAlignment(SwingConstants.LEFT);
		mk_V10_RaidoButton.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 18));
		mk_V10_RaidoButton.setForeground(Color.LIGHT_GRAY);
		mk_V10_RaidoButton.setBackground(Color.WHITE);
		mk_V10_RaidoButton.setBounds(25, 75, 170, 23);
		mk_V10_RaidoButton.setFocusPainted(false);
		mk_V10_RaidoButton.addActionListener(mkVerionListener);
		mk119Version_Panel.add(mk_V10_RaidoButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(mk_V4_RaidoButton);
		group.add(mk_V10_RaidoButton);
		
		JPanel uploadMethod_Panel = new JPanel();
		uploadMethod_Panel.setBackground(Color.WHITE);
		uploadMethod_Panel.setBounds(224, 10, 504, 106);
		backGround_Panel.add(uploadMethod_Panel);
		uploadMethod_Panel.setLayout(null);
		
		upload_protocol = new JButton("Protocol");
		upload_protocol.setForeground(Color.BLACK);
		upload_protocol.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 20));
		upload_protocol.setFocusPainted(false);
		upload_protocol.setIcon(new Util().getFolder2Image());
		upload_protocol.setBackground(Color.WHITE);
		upload_protocol.setBounds(12, 10, 163, 86);
		uploadMethod_Panel.add(upload_protocol);
		
		upload_xml = new JButton( " XML");
		upload_xml.setForeground(Color.BLACK);
		upload_xml.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 20));
		upload_xml.setFocusPainted(false);
		upload_xml.setIcon(new Util().getXMLImage());
		upload_xml.setBackground(Color.WHITE);
		upload_xml.setBounds(187, 10, 146, 86);
		uploadMethod_Panel.add(upload_xml);
		
		upload_excel = new JButton(" Excel");
		upload_excel.setForeground(Color.BLACK);
		upload_excel.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 20));
		upload_excel.setFocusPainted(false);
		upload_excel.setIcon(new Util().getExcelImage());
		upload_excel.setBackground(Color.WHITE);
		upload_excel.setBounds(345, 10, 146, 86);
		uploadMethod_Panel.add(upload_excel);
		
		JPanel downloadTemp_Panel = new JPanel();
		downloadTemp_Panel.setBackground(Color.WHITE);
		downloadTemp_Panel.setBounds(740, 10, 213, 106);
		backGround_Panel.add(downloadTemp_Panel);
		downloadTemp_Panel.setLayout(null);
		
		download_template = new JButton("<html>&nbsp;Template<br>&nbsp;Download</html>");
		download_template.setIcon(new Util().getExcelImage());
		download_template.setFocusPainted(false);
		download_template.setForeground(Color.BLUE);
		download_template.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 18));
		download_template.setBackground(Color.WHITE);
		download_template.setBounds(12, 10, 189, 86);
		download_template.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = new File("C:\\4.2\\ModbusAnalyzer\\MODBUS ¾ç½Ä.xlsx");
				String path = Util.getFilePath();
				
				if(path != null) {
					path += ".xlsx";
					FileUtil.copyFile(file, new File(path));
					System.out.println(path);
				}else {
					return;
				}
			}
		});
		downloadTemp_Panel.add(download_template);
			
		// ÇÁ·¹ÀÓÀÌ È­¸é °¡¿îµ¥¿¡¼­ »ý¼ºµÈ´Ù
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void dispose() {
		isExist = false;
		super.dispose();
	}
	
	ActionListener mkVerionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(mk_V4_RaidoButton.isSelected()) {
				mk_V4_RaidoButton.setForeground(mkColor);
				mk_V10_RaidoButton.setForeground(Color.LIGHT_GRAY);
				
				upload_protocol.setEnabled(true);
				upload_xml.setEnabled(true);
				upload_excel.setEnabled(true);
			}else {
				mk_V4_RaidoButton.setForeground(Color.LIGHT_GRAY);
				mk_V10_RaidoButton.setForeground(mkColor);
				
				upload_protocol.setEnabled(false);
				upload_xml.setEnabled(false);
				upload_excel.setEnabled(true);
			}
			
		}
	};
}
