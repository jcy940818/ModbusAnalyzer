package src_en.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import src_en.agent.ControlAction;
import src_en.agent.Perf;
import src_en.util.Util;

public class ItemUploadFrame extends JFrame {

	private JPanel contentPane;
	private String agentType;
	private JTextArea itemList_textArea;	
	public static boolean isExist;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ItemUploadFrame frame = new ItemUploadFrame("ModbusAnalyzer", "Item Upload", "Common");
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public ItemUploadFrame(String title, String content, String agentType) {
		ItemUploadFrame.isExist = true;
		this.agentType = agentType; 
				
		setBackground(Color.WHITE);
		setIconImage(new Util().getIconResource().getImage());
		setTitle(title);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 678, 641);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(255, 140, 0), 8));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		scrollPane.setBounds(22, 67, 627, 466);
		contentPane.add(scrollPane);
		
		itemList_textArea = new JTextArea();
		itemList_textArea.setFont(FontManager.getFont(Font.PLAIN, 18));
		itemList_textArea.setForeground(Color.BLACK);
		itemList_textArea.setBackground(Color.WHITE);		
		scrollPane.setViewportView(itemList_textArea);
		
		JLabel lblSimpleUpload = new JLabel(content);
		lblSimpleUpload.setIcon(new Util().getSubLogoResource());
		lblSimpleUpload.setHorizontalAlignment(SwingConstants.LEFT);
		lblSimpleUpload.setFont(FontManager.getFont(Font.BOLD, 20));
		lblSimpleUpload.setBounds(12, 14, 493, 47);
		contentPane.add(lblSimpleUpload);
		
		JButton ok_button = new JButton("Upload");
		ok_button.setBackground(Color.WHITE);
		ok_button.setFont(FontManager.getFont(Font.BOLD, 16));
		ok_button.setFocusPainted(false);
		ok_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				
				ArrayList<String> itemList = getItemList();
				
				switch(agentType) {
					
					case "common" :
						
						for(int i = 0; i < itemList.size(); i++) {
							String perfInfo = itemList.get(i).trim();
							Perf commonPerf = null;
							
							if(perfInfo.contains(",") && perfInfo.contains("=")) {
								// User Custom PerfInfo Upload
								commonPerf = Perf.createCustomPerf(perfInfo);							
							}else {
								commonPerf = new Perf();
								commonPerf.setDisplayName(perfInfo);						
								commonPerf.setPerfCounter("");
								commonPerf.setSlot(1);
								commonPerf.setInterval("60"); // 수집주기 ( 기본 : 60초 )
								commonPerf.setMeasure(""); // 단 위
								commonPerf.setScaleFunction("x"); // 보정식 ( 기본 : x )
							}
							
							CommonXmlGeneratorFrame.addRecord(commonPerf);
						}
				
						break; // case COMMON
					/**********************************************************************/
					case "snmp" :
						
						for(int i = 0; i < itemList.size(); i++) {
							String perfInfo = itemList.get(i).trim();
							Perf snmpPerf = null;
							
							if(perfInfo.contains(",") && perfInfo.contains("=")) {
								// User Custom PerfInfo Upload
								snmpPerf = Perf.createCustomPerf(perfInfo);
							}else {
								snmpPerf = new Perf();
								snmpPerf.setDisplayName(perfInfo);
								snmpPerf.setOid(perfInfo);
								snmpPerf.setInterval("60"); // 수집주기 ( 기본 : 60초 )
								snmpPerf.setMeasure(""); // 단 위
								snmpPerf.setScaleFunction("x"); // 보정식 ( 기본 : x )
							}
							
							SnmpXmlGeneratorFrame.addRecord(snmpPerf);
						}
						
						break; // case SNMP			
					/**********************************************************************/
					case "agent" :
						
						for(int i = 0; i < itemList.size(); i++) {
							String perfInfo = itemList.get(i).trim();
							Perf agentPerf = null;
							
							if(perfInfo.contains(",") && perfInfo.contains("=")) {
								// User Custom PerfInfo Upload
								agentPerf = Perf.createCustomPerf(perfInfo);
								
								if(agentPerf.getPerfCounter().length() < 1) agentPerf.setPerfCounter("0");
								if(agentPerf.getSlot() == 1) agentPerf.setSlot(i+1);
							}else {
								agentPerf = new Perf();
								agentPerf.setDisplayName(perfInfo);						
								agentPerf.setPerfCounter("0");
								agentPerf.setInterval("60"); // 수집주기 ( 기본 : 60초 )
								agentPerf.setSlot(i+1);
								agentPerf.setMeasure(""); // 단 위
								agentPerf.setScaleFunction("x"); // 보정식 ( 기본 : x )
							}
							
							AgentXmlGeneratorFrame.addRecord(agentPerf);
						}
					
						break; // case AGENT
					/**********************************************************************/
					case "control" :
						
						for(int i = 0; i < itemList.size(); i++) {
							String controlInfo = itemList.get(i).trim();
							ControlAction controlAction = null;
							
							if(controlInfo.contains(",") && controlInfo.contains("=")) {
								// User Custom ControlAction Upload
								controlAction = ControlAction.createCustomControl(controlInfo);																
							}else {																
								controlAction = new ControlAction();
								controlAction.setControlName(controlInfo);
								controlAction.setControlCounter("CONTROL");
								controlAction.setCommand("");
								controlAction.setDesc("");
								controlAction.setUseParam(1);
								controlAction.setWaitTime(1);
							}
							
							ControlXmlGeneratorFrame.addRecord(controlAction);
						}
					
						break; // case Control Action
					/**********************************************************************/
					default : 
						break;
						
				}// end switch
				
			}
		});
		ok_button.setBounds(215, 547, 97, 42);
		contentPane.add(ok_button);
		
		JButton cancel_button = new JButton("Reset");
		cancel_button.setBackground(Color.WHITE);
		cancel_button.setFont(FontManager.getFont(Font.BOLD, 16));
		cancel_button.setBounds(342, 547, 97, 42);
		cancel_button.setFocusPainted(false);
		cancel_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				itemList_textArea.setText("");
			}
		});
		contentPane.add(cancel_button);
		
		JButton property_button = new JButton("Item Property");
		property_button.setForeground(Color.BLUE);
		property_button.setFont(FontManager.getFont(Font.BOLD, 16));
		property_button.setBackground(Color.WHITE);
		property_button.setBounds(497, 14, 152, 43);
		property_button.setFocusPainted(false);
		property_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				StringBuilder sb = new StringBuilder();
				
				if(agentType.equalsIgnoreCase("control")) {
					sb.append(Util.colorBlue("Item Property Information") + Util.separator + Util.separator + "\n");
					sb.append(String.format("01. 제어 이름 : controlname 또는 name%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("02. 제어 명령어 : command 또는 cmd%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("03. 제어 내용 : description 또는 desc%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("04. 제어 파라미터 사용여부 : useParam 또는 param%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("05. 제어 타임아웃 : waitTime 또는 timeout 또는 time%s%s\n", Util.separator, Util.separator));
				}else {
//					sb.append(Util.colorBlue("Item Property Information") + Util.separator + Util.separator + "\n");
//					sb.append(String.format("01. 성능명 : displayname 또는 name%s%s\n\n", Util.separator, Util.separator));
//					sb.append(String.format("02. 성능 카운터 : perfcounter 또는 counter%s%s\n\n", Util.separator, Util.separator));
//					sb.append(String.format("03. OID (SNMP) : oid%s%s\n\n", Util.separator, Util.separator));
//					sb.append(String.format("04. 슬 롯 : slot%s%s\n\n", Util.separator, Util.separator));
//					sb.append(String.format("05. 수집주기 : interval%s%s\n\n", Util.separator, Util.separator));
//					sb.append(String.format("06. 단 위 : measure 또는 units%s%s\n\n", Util.separator, Util.separator));
//					sb.append(String.format("07. 보정식 : scalefunction 또는 scale 또는 expression%s%s\n\n", Util.separator, Util.separator));
//					sb.append(String.format("08. 이진 상태 (0) : label0 또는 0%s%s\n\n", Util.separator, Util.separator));
//					sb.append(String.format("09. 이진 상태 (1) : label1 또는 1%s%s\n\n", Util.separator, Util.separator));
//					sb.append(String.format("10. 다중 상태 : multistatus 또는 status%s%s\n", Util.separator, Util.separator));
					
					sb.append(Util.colorGreen("Item Property Information") + Util.separator + Util.separator + "\n");
					sb.append(String.format("01. <font color='blue'>Perf Name</font> : displayname <font color='red'>OR</font> name%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("02. <font color='blue'>Perf Counter</font> : perfcounter <font color='red'>OR</font> counter%s%s\n\n", Util.separator, Util.separator));
//					sb.append(String.format("03. <font color='blue'>OID (SNMP)</font> : oid%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("03. <font color='blue'>Slot</font> : slot%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("04. <font color='blue'>Interval</font> : interval%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("05. <font color='blue'>Units</font> : measure <font color='red'>OR</font> units%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("06. <font color='blue'>Scale</font> : scalefunction <font color='red'>OR</font> scale <font color='red'>OR</font> expression%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("07. <font color='blue'>LABEL (0)</font> : label0 <font color='red'>OR</font> 0%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("08. <font color='blue'>LABEL (1)</font> : label1 <font color='red'>OR</font> 1%s%s\n\n", Util.separator, Util.separator));
					sb.append(String.format("09. <font color='blue'>Multiple states</font> : multistatus <font color='red'>OR</font> status%s%s\n\n", Util.separator, Util.separator));
					
					sb.append("─────────────────────────────────────────\n");
					sb.append(Util.colorGreen("Upload input example") + Util.separator + Util.separator + "\n\n");
					sb.append("example 1 : displayname=Voltage, interval=30, measure=V, scalefunction=x/10"  + Util.separator + Util.separator + "\n");
					sb.append("example 1 Contents : PerfName=Voltage, interval=30(seconds), units=V, scale=x/10"  + Util.separator + Util.separator + "\n\n");
					
					sb.append("example 2 : displayname=Alarm, label0=Alarm OFF, label1=Alarm ON"  + Util.separator + Util.separator + "\n");
					sb.append("example 2 Contents : PerfName=Alarm, LABEL(0)=Alarm OFF, LABEL (1)=Alarm ON"  + Util.separator + Util.separator + "\n\n");
					
					sb.append("example 3 : displayname=Unit Status, multistatus=0; Normal; 1; Run; 2; Stop;"  + Util.separator + Util.separator + "\n");
					sb.append("example 3 Contents : PerfName=Unit Status, Multiple states=(0:Normal),(1:Run),(2:Stop)"  + Util.separator + Util.separator + "\n");
				}				
				
				Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
				return;
				
			}
		});
		contentPane.add(property_button);
	
		setVisible(true);
		setLocationRelativeTo(null); // 프레임 생성자의 마지막에 넣어줘야 프레임이 화면 가운데에서 실행된다.
	}
	
	
	public ArrayList<String> getItemList() {
		ArrayList<String> items = new ArrayList();
		String item = itemList_textArea.getText();
		Scanner sc = new Scanner(item);
		
		while(sc.hasNextLine()) {
			items.add(sc.nextLine().trim());
		}
		
		return items;
	}
	
	@Override
	public void dispose() {
		ItemUploadFrame.isExist = false;
		super.dispose();
	}
	
}
