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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import common.util.FormUtil;
import moon.Moon;
import src_ko.util.FileUtil;
import src_ko.util.Util;

public class FormExcelConvertXmlPanel extends JPanel {	
	
	private JRadioButton typeCommon_radioButton;
	private JRadioButton typeSNMP_radioButton;
	private JRadioButton typeControl_radioButton;
	
	private JTextField dragAndDrop_TextField;
	private JButton downloadFormExcel_Button;
	
	private JRadioButton eucKr_button;
	private JRadioButton utf8_button;
	
	public FormExcelConvertXmlPanel() { 
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
		
		JLabel xmlImageLabel = new JLabel();
		xmlImageLabel.setBounds(440, 38, 58, 89);
		xmlImageLabel.setIcon(new Util().getXMLImage());
		actualPanel.add(xmlImageLabel);
		
		JLabel titleLabel = new JLabel("XML Generator");
		titleLabel.setForeground(Color.BLACK);
		titleLabel.setFont(FontManager.getFont(Font.BOLD, 26));
		titleLabel.setBounds(495, 59, 294, 47);
		actualPanel.add(titleLabel);
		
		dragAndDrop_TextField = new JTextField("XML or Excel   Drag & Drop");
		dragAndDrop_TextField.setBounds(440, 423, 362, 171);
		dragAndDrop_TextField.setBackground(Color.WHITE);
		dragAndDrop_TextField.setForeground(Color.BLACK);
		dragAndDrop_TextField.setHorizontalAlignment(SwingConstants.CENTER);
		dragAndDrop_TextField.setEditable(false);
		dragAndDrop_TextField.setFont(FontManager.getFont(Font.BOLD, 22));		
		dragAndDrop_TextField.setColumns(10);
		dragAndDrop_TextField.setBorder(new LineBorder(Color.BLACK, 2));
		dragAndDrop_TextField.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					
					for (File file : droppedFiles) {
						
						if(file != null && file.exists()) {
							
							convert(file);
							return;
							
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(dragAndDrop_TextField);
		
		JButton uploadButton = new JButton(" 양식 업로드");
		uploadButton.setIcon(new Util().getExcelImage());
		uploadButton.setForeground(Color.BLACK);
		uploadButton.setFont(FontManager.getFont(Font.BOLD, 19));
		uploadButton.setFocusPainted(false);
		uploadButton.setBackground(Color.WHITE);
		uploadButton.setBounds(810, 90, 230, 70);		
		uploadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String path = Util.getFilePath();
				
				if(path == null || path.length() < 1) {
					return;
					
				}else {
					File excelFile = new File(path);
					if(excelFile != null && excelFile.exists()) {
						convert(excelFile);
					}
				}
				
			}
		});
		actualPanel.add(uploadButton);
		
		downloadFormExcel_Button = new JButton(" 양식 다운로드");
		downloadFormExcel_Button.setForeground(Color.BLACK);
		downloadFormExcel_Button.setFocusPainted(false);
		downloadFormExcel_Button.setBackground(Color.WHITE);
		downloadFormExcel_Button.setFont(FontManager.getFont(Font.BOLD, 19));		
		downloadFormExcel_Button.setBounds(810, 12, 230, 70);
		downloadFormExcel_Button.setIcon(new Util().getExcelImage());
		downloadFormExcel_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String type = "Common";
					
					if(typeCommon_radioButton.isSelected()) {
						type = "Common";
						
					}else if(typeSNMP_radioButton.isSelected()) {
						type = "SNMP";
						
					}else if(typeControl_radioButton.isSelected()){
						type = "Control";
						
					}
					
					String filePath = String.format("%s\\%s\\V4\\%s\\%s.xlsx",
							MainFrame.getCurrentPath(),
							"template",
							Moon.currentLanguage,
							type
							);
					
					File file = new File(filePath);
					
					if(!file.exists()) {
						StringBuilder sb = new StringBuilder();
						sb.append(Util.colorRed("Form Excel File that does not Exist") + Util.separator + "\n");
						sb.append("아래의 경로에 양식 파일이 존재하지 않습니다" + Util.separator + Util.separator + "\n\n");
						sb.append(Util.colorRed("Path") + " : " + file.getAbsolutePath().replace("\\", Util.colorRed("\\")) + Util.separator + Util.separator + "\n");
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					String downloadPath = Util.getFilePath();
				
					if(downloadPath != null) {
						downloadPath += ".xlsx";
						File downloadFile = new File(downloadPath);
						FileUtil.copyFile(file, downloadFile);
						if(downloadFile.exists()) {
							StringBuilder sb = new StringBuilder();
							sb.append(Util.colorGreen("Form Excel File Download Successful") + Util.separator + "\n");
							sb.append("아래의 경로에 양식 파일을 다운로드 완료하였습니다" + Util.separator + Util.separator + "\n\n");
							sb.append(Util.colorBlue("Path") + " : " + downloadFile.getAbsolutePath().replace("\\", Util.colorBlue("\\")) + Util.separator + Util.separator + "\n");
							Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						
					}else {
						return;
						
					}
				}catch(Exception exception) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("Failed to Download Form Excel File") + Util.separator + "\n");
					sb.append("처리 할 수 없는 예외가 발생하여 양식 파일 다운로드에 실패하였습니다" + Util.separator + Util.separator + "\n\n");
					sb.append(Util.colorRed("Exception Message") + " : " + exception.getMessage() + Util.separator + Util.separator + "\n");
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
			}
		});
		actualPanel.add(downloadFormExcel_Button);
		
		JLabel onionLogo = new JLabel();
		onionLogo.setIcon(new Util().getMainLogoResource());
		onionLogo.setBounds(670, 184, 376, 547);
		actualPanel.add(onionLogo);
		
		JLabel lblXmlType = new JLabel("XML Type");
		lblXmlType.setForeground(Color.BLACK);
		lblXmlType.setFont(FontManager.getFont(Font.BOLD, 20));
		lblXmlType.setBounds(445, 181, 200, 47);
		actualPanel.add(lblXmlType);
		
		typeCommon_radioButton = new JRadioButton(" 일반 성능");
		typeCommon_radioButton.setSelected(true);
		typeCommon_radioButton.setForeground(new Color(0, 128, 0));
		typeCommon_radioButton.setFont(FontManager.getFont(Font.BOLD, 20));
		typeCommon_radioButton.setFocusPainted(false);
		typeCommon_radioButton.setBackground(Color.WHITE);
		typeCommon_radioButton.setBounds(445, 231, 130, 30);
		typeCommon_radioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				typeCommon_radioButton.setForeground(new Color(0, 128, 0));
				typeSNMP_radioButton.setForeground(Color.LIGHT_GRAY);
				typeControl_radioButton.setForeground(Color.LIGHT_GRAY);
			}
		});
		actualPanel.add(typeCommon_radioButton);
		
		typeSNMP_radioButton = new JRadioButton(" SNMP 성능");		
		typeSNMP_radioButton.setForeground(Color.LIGHT_GRAY);
		typeSNMP_radioButton.setFont(FontManager.getFont(Font.BOLD, 20));
		typeSNMP_radioButton.setFocusPainted(false);
		typeSNMP_radioButton.setBackground(Color.WHITE);
		typeSNMP_radioButton.setBounds(584, 231, 150, 30);
		typeSNMP_radioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				typeCommon_radioButton.setForeground(Color.LIGHT_GRAY);
				typeSNMP_radioButton.setForeground(new Color(0, 128, 0));
				typeControl_radioButton.setForeground(Color.LIGHT_GRAY);
			}
		});
		actualPanel.add(typeSNMP_radioButton);
		
		typeControl_radioButton = new JRadioButton(" 제어");		
		typeControl_radioButton.setForeground(Color.LIGHT_GRAY);
		typeControl_radioButton.setFont(FontManager.getFont(Font.BOLD, 20));
		typeControl_radioButton.setFocusPainted(false);
		typeControl_radioButton.setBackground(Color.WHITE);
		typeControl_radioButton.setBounds(745, 231, 100, 30);
		typeControl_radioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				typeCommon_radioButton.setForeground(Color.LIGHT_GRAY);
				typeSNMP_radioButton.setForeground(Color.LIGHT_GRAY);
				typeControl_radioButton.setForeground(new Color(0, 128, 0));
			}
		});
		actualPanel.add(typeControl_radioButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(typeCommon_radioButton);
		group.add(typeSNMP_radioButton);
		group.add(typeControl_radioButton);
		
		JLabel lblXmlEncoding = new JLabel("XML Encoding");
		lblXmlEncoding.setForeground(Color.BLACK);
		lblXmlEncoding.setFont(FontManager.getFont(Font.BOLD, 20));
		lblXmlEncoding.setBounds(445, 301, 200, 47);
		actualPanel.add(lblXmlEncoding);
		
		eucKr_button = new JRadioButton(" EUC-KR");
		eucKr_button.setFocusPainted(false);
		eucKr_button.setBackground(Color.WHITE);
		eucKr_button.setFont(FontManager.getFont(Font.BOLD, 20));
		eucKr_button.setForeground(new Color(0, 128, 0));
		eucKr_button.setBounds(445, 351, 120, 30);
		eucKr_button.setSelected(true);
		eucKr_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eucKr_button.setForeground(new Color(0, 128, 0));
				utf8_button.setForeground(Color.LIGHT_GRAY);
			}
		});
		actualPanel.add(eucKr_button);
		
		utf8_button = new JRadioButton(" UTF-8");
		utf8_button.setFocusPainted(false);
		utf8_button.setForeground(Color.LIGHT_GRAY);
		utf8_button.setFont(FontManager.getFont(Font.BOLD, 20));
		utf8_button.setBackground(Color.WHITE);
		utf8_button.setBounds(575, 351, 120, 30);
		utf8_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eucKr_button.setForeground(Color.LIGHT_GRAY);
				utf8_button.setForeground(new Color(0, 128, 0));
			}
		});
		actualPanel.add(utf8_button);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(eucKr_button);
		buttonGroup.add(utf8_button);
			
		setVisible(true);
	}
	
	public void convert(File formFile) {
		
		String type = "common";
		String encoding = "euc-kr";
		
		if(typeCommon_radioButton.isSelected()) {
			type = "common";
			
		}else if(typeSNMP_radioButton.isSelected()) {
			type = "snmp";
			
		}else if(typeControl_radioButton.isSelected()){
			type = "control";
			
		}else {
			type = "common";
		}
		
		if(eucKr_button.isSelected()) {
			encoding = "euc-kr";
			
		}else if(utf8_button.isSelected()){
			encoding = "utf-8";
			
		}else {
			encoding = "euc-kr";
		}
		
		try {
			
			FormUtil.excute(formFile, type, encoding);
			
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
}