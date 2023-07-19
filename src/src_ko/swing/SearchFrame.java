package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import common.web.AdminConsole_Info;
import src_ko.agent.ModbusAgent;
import src_ko.agent.ModbusFacility;
import src_ko.database.DbUtil;
import src_ko.info.ONION_Info;
import src_ko.util.Util;

public class SearchFrame extends JFrame {

	private JPanel contentPane;
	
	private Connection mkConnection;
	
	private AdminConsole_Info adminConsole;
	private ModbusFacility modbusFacility;
	
	
	private String mkSqlServerInfo;		

	public static boolean isExist = false;
	private JTextField searchBar_TextField;
	private JLabel result_value_label;
	private JLabel facility_value_label;
	private JLabel connMethod_value_label; 
	private JLabel message_value_label;
	private JButton addPerfMK119;
	
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SearchFrame frame = new SearchFrame(null, "localhost:1433");
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
	public SearchFrame(AdminConsole_Info AdminConsole, String SqlServerInfo) {
		SearchFrame.isExist = true;
		
		this.adminConsole = AdminConsole;
		this.mkSqlServerInfo = SqlServerInfo;
		
		setBackground(Color.WHITE);
		setResizable(false);
		setTitle(String.format("ModbusAnalyzer : %s:%s [ %s ]", adminConsole.get_IP(), adminConsole.get_PORT(), adminConsole.getVersionInfo()));
		setIconImage(new Util().getIconResource().getImage());	
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 645, 481);
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(255, 140, 0), 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBackground(Color.WHITE);
		contentPane.add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		JLabel SearchFacility = new JLabel("Modbus Facility Search");
		SearchFacility.setForeground(Color.BLACK);
		SearchFacility.setIcon(new Util().getSubLogoResource());
		SearchFacility.setHorizontalAlignment(SwingConstants.LEFT);
		SearchFacility.setFont(FontManager.getFont(Font.BOLD, 22));
		SearchFacility.setBackground(Color.WHITE);
		SearchFacility.setBounds(0, 0, 316, 60);
		actualPanel.add(SearchFacility);
		
		JLabel label = new JLabel("\uC7A5\uBE44\uBA85");
		label.setOpaque(true);
		label.setForeground(Color.BLACK);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(FontManager.getFont(Font.BOLD, 20));
		label.setBackground(UIManager.getColor("Button.background"));
		label.setBounds(12, 84, 86, 44);
		actualPanel.add(label);
		
		JButton searchButton = new JButton("\uAC80 \uC0C9");
		searchButton.setForeground(Color.BLACK);
		searchButton.setFont(FontManager.getFont(Font.BOLD, 18));
		searchButton.setFocusPainted(false);
		searchButton.setContentAreaFilled(false);
		searchButton.setBorder(UIManager.getBorder("Button.border"));
		searchButton.setBackground(Color.WHITE);
		searchButton.setBounds(535, 84, 86, 44);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					searchFacility(searchBar_TextField.getText().trim());
				}catch(Exception exception) {
					// 모드버스 시설물 검색중 예외 발생 시 아무것도 수행하지 않음
					exception.printStackTrace();
					
					setResult(false);
					setFacilityType("Unknown");
					setConnMethod("Unknown");
					setMessage("존재하지 않는 시설물 입니다");
					addPerfMK119.setEnabled(false);
				}finally {
					searchBar_TextField.requestFocus();
					result_value_label.setVisible(true);
					facility_value_label.setVisible(true);
					connMethod_value_label.setVisible(true);
					message_value_label.setVisible(true);
				}
			}
		});
		actualPanel.add(searchButton);
		
		searchBar_TextField = new JTextField();
		searchBar_TextField.setForeground(Color.BLACK);
		searchBar_TextField.setFont(FontManager.getFont(Font.BOLD, 18));
		searchBar_TextField.setBounds(110, 84, 419, 44);
		searchBar_TextField.setBorder(new LineBorder(new Color(255, 140, 0), 3));	
		searchBar_TextField.addFocusListener(new FocusListener() {			
			@Override
			public void focusLost(FocusEvent e) {
				searchBar_TextField.setBorder(UIManager.getBorder("TextField.border"));
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				searchBar_TextField.setBorder(new LineBorder(new Color(255, 140, 0), 3));			
			}
		});
		searchBar_TextField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				searchButton.doClick();				
			}
		});
		searchBar_TextField.setColumns(10);
		actualPanel.add(searchBar_TextField);
		
		JLabel result_Label = new JLabel("\uACB0 \uACFC");
		result_Label.setOpaque(true);
		result_Label.setHorizontalAlignment(SwingConstants.CENTER);
		result_Label.setForeground(Color.BLACK);
		result_Label.setFont(FontManager.getFont(Font.BOLD, 18));
		result_Label.setBackground(UIManager.getColor("Button.background"));
		result_Label.setBounds(12, 155, 107, 44);
		actualPanel.add(result_Label);
		
		result_value_label = new JLabel("결 과");
		result_value_label.setOpaque(true);
		result_value_label.setHorizontalAlignment(SwingConstants.CENTER);
		result_value_label.setForeground(Color.BLACK);
		result_value_label.setFont(FontManager.getFont(Font.BOLD, 20));
		result_value_label.setBackground(Color.GREEN);
		result_value_label.setBounds(131, 155, 194, 44);
		actualPanel.add(result_value_label);
		
		JLabel facility_label = new JLabel("\uC2DC\uC124\uBB3C");
		facility_label.setOpaque(true);
		facility_label.setHorizontalAlignment(SwingConstants.CENTER);
		facility_label.setForeground(Color.BLACK);
		facility_label.setFont(FontManager.getFont(Font.BOLD, 18));
		facility_label.setBackground(SystemColor.menu);
		facility_label.setBounds(12, 209, 107, 44);
		actualPanel.add(facility_label);
		
		facility_value_label = new JLabel("\uC2DC\uC124\uBB3C");
		facility_value_label.setForeground(Color.BLACK);
		facility_value_label.setFont(FontManager.getFont(Font.BOLD, 18));
		facility_value_label.setBackground(Color.WHITE);
		facility_value_label.setBounds(131, 209, 486, 44);
		actualPanel.add(facility_value_label);
		
		JLabel method_label = new JLabel("\uC5F0\uACB0 \uBC29\uC2DD");
		method_label.setOpaque(true);
		method_label.setHorizontalAlignment(SwingConstants.CENTER);
		method_label.setForeground(Color.BLACK);
		method_label.setFont(FontManager.getFont(Font.BOLD, 18));
		method_label.setBackground(UIManager.getColor("Button.background"));
		method_label.setBounds(12, 263, 107, 44);
		actualPanel.add(method_label);
		
		connMethod_value_label = new JLabel("\uC5F0\uACB0 \uBC29\uC2DD");
		connMethod_value_label.setForeground(Color.BLACK);
		connMethod_value_label.setFont(FontManager.getFont(Font.BOLD, 18));
		connMethod_value_label.setBackground(Color.WHITE);
		connMethod_value_label.setBounds(131, 263, 486, 44);
		actualPanel.add(connMethod_value_label);
		
		JLabel message_label = new JLabel("\uB0B4 \uC6A9");
		message_label.setOpaque(true);
		message_label.setHorizontalAlignment(SwingConstants.CENTER);
		message_label.setForeground(Color.BLACK);
		message_label.setFont(FontManager.getFont(Font.BOLD, 18));
		message_label.setBackground(SystemColor.menu);
		message_label.setBounds(12, 317, 107, 44);
		actualPanel.add(message_label);
		
		message_value_label = new JLabel("\uBA54\uC2DC\uC9C0");
		message_value_label.setForeground(Color.BLACK);
		message_value_label.setFont(FontManager.getFont(Font.BOLD, 18));
		message_value_label.setBackground(Color.WHITE);
		message_value_label.setBounds(131, 317, 486, 44);
		actualPanel.add(message_value_label);
		
		addPerfMK119 = new JButton();
		addPerfMK119.setText(" \uC131\uB2A5 \uCD94\uAC00");
		addPerfMK119.setForeground(Color.BLACK);
		addPerfMK119.setFont(FontManager.getFont(Font.BOLD, 18));
		addPerfMK119.setFocusPainted(false);
		addPerfMK119.setContentAreaFilled(false);
		addPerfMK119.setBorder(UIManager.getBorder("Button.border"));
		addPerfMK119.setBackground(Color.WHITE);					
		addPerfMK119.setBounds(12, 371, 605, 60);
		addPerfMK119.setIcon(new Util().getMK2Resource());
		addPerfMK119.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				activeModbusCollection();
			}
		});
		actualPanel.add(addPerfMK119);
						
		result_value_label.setVisible(false);
		facility_value_label.setVisible(false);
		connMethod_value_label.setVisible(false);
		message_value_label.setVisible(false);		
		addPerfMK119.setEnabled(false);
		
		mkConnection = getMkConnection();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	public void searchFacility(String serverName) throws SQLException{
		String query = String.format("SELECT nServerIndex, strServerName, FACILITY_TYPE, CONN_METHOD, COMM_PROTOCOL FROM SERVERINFO si INNER JOIN SERVERINFO_FACILITY fac ON si.nServerIndex = fac.NODE_INDEX WHERE si.strServerName = '%s'", serverName);
		
		int nServerIndex = 0;
		String strServerName;
		int FACILITY_TYPE = 0;
		int CONN_METHOD = 0;
		int COMM_PROTOCOL = 0;
		
		Statement stmt = null;
		ResultSet rs = null;
		
		// 프레임 생성자에서 초기화 된 mkConnection이 닫혀있거나
		// 프레임 생성시 초기화 된 SQL Server 정보와 현재 데이터베이스 SQL Server 정보가 일치하지 않으면 프레임을 종료한다
		if (!this.mkConnection.isClosed() && (ONION_Info.getSimpleSqlServerInfo().equals(this.mkSqlServerInfo))) {
				stmt = this.mkConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);			
		}else {
			lostConnection();
		}
		
		rs = stmt.executeQuery(query);
		
		if(rs != null) {
			int rowCount = DbUtil.getRowCount(rs);
			
			if(rowCount == 0) {
				// 입력된 장비명을 가진 시설물이 존재하지 않을 경우
				setResult(false);
				setFacilityType("Unknown");
				setConnMethod("Unknown");
				setMessage("존재하지 않는 시설물 입니다");
				addPerfMK119.setEnabled(false);
			}else if(rowCount == 1) {
				// 입력된 장비명을 가진 시설물이 존재할 경우
				
				while(rs.next()) {
					nServerIndex = rs.getInt("nServerIndex");
					strServerName = rs.getString("strServerName");
					FACILITY_TYPE = rs.getInt("FACILITY_TYPE");
					CONN_METHOD = rs.getInt("CONN_METHOD");
					COMM_PROTOCOL = rs.getInt("COMM_PROTOCOL");
					
					this.modbusFacility = null;
					this.modbusFacility = new ModbusFacility(nServerIndex, strServerName, FACILITY_TYPE, DbUtil.getFacilityType(FACILITY_TYPE), CONN_METHOD, COMM_PROTOCOL);					
				}
				
				// 입력된 장비명을 가진 시설물이 존재할 경우 : 모드버스 연결 방식으로 통신하는지 검사
				if(CONN_METHOD == ModbusAgent.CONN_METHOD_MODBUS && (COMM_PROTOCOL == ModbusAgent.MODBUS_TYPE_RTU || COMM_PROTOCOL == ModbusAgent.MODBUS_TYPE_TCP)) {
					setResult(true);
					setFacilityType(String.valueOf(FACILITY_TYPE));
					setConnMethod(String.valueOf(CONN_METHOD));
					setMessage("해당 장비는 모드버스 성능 추가가 가능합니다");
					addPerfMK119.setEnabled(true);
				}else {
					setResult(false);
					setFacilityType(String.valueOf(FACILITY_TYPE));
					setConnMethod(String.valueOf(CONN_METHOD));
					setMessage("Modbus 연결 방식으로 통신하는 시설물이 아닙니다");	
					addPerfMK119.setEnabled(false);
				}				
				
			}else {
				setResult(false);
				setFacilityType("Unknown");
				setConnMethod("Unknown");
				setMessage("검색 결과를 확인 할 수 없습니다");
				addPerfMK119.setEnabled(false);
			}
		}
		
		DbUtil.close(rs, stmt);		
	}
	
	public Connection getMkConnection() {
		Connection conn = null;
		
		try {
			if(ONION_Info.hasMk119Connection()) {
				if(ONION_Info.getMk119Connection() != null) {
					if(!ONION_Info.getMk119Connection().isClosed()) {
						conn = ONION_Info.getMk119Connection();
					}
				}	
			}else {
				lostConnection();		
			}
		}catch(Exception e) {
			lostConnection();	
		}
		
		return conn;
	}
	
	public void lostConnection() {
		StringBuilder sb = new StringBuilder();
		sb.append(Util.colorRed("Database Connection Lost\n"));
		sb.append(String.format("<font color='blue'>%s</font> SQL Server\n\n데이터베이스 Connection 인스턴스가 반환되어 프레임을 종료합니다%s\n", this.mkSqlServerInfo, Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		dispose();			
	}
		
	
	public void activeModbusCollection() {
		boolean inspect = true;
		
		inspect = this.modbusFacility != null;
		inspect = this.result_value_label.getText().contains("Valid");
		inspect = this.connMethod_value_label.getText().contains("Modbus");
		inspect = this.message_value_label.getText().contains("가능");
		
		if(inspect) {
			if(!ModbusCollectionFrame.isExist) {				
				new ModbusCollectionFrame(this.adminConsole, this.modbusFacility);	
				
				try {
					if(ONION_Info.hasMk119Connection()) ONION_Info.closeMk119Connection();
				}catch(SQLException e) {
					System.out.println("[ MainFrame.showMK119Login() : SQL Exception ]");
				}
				
			}else {
				StringBuilder sb = new StringBuilder();
				sb.append(Util.colorRed("ModbusCollection Already Exists") + Util.separator + "\n");
				sb.append("ModbusCollection 프레임이 이미 존재합니다" + Util.separator + "\n");
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			}
		}
		
		dispose();
	}
	
	public void setResult(boolean isValid) {
		if(isValid) {
			result_value_label.setText("Valid");
			result_value_label.setBackground(Color.GREEN);
		}else {
			result_value_label.setText("Invalid");
			result_value_label.setBackground(Color.RED);
		}
	}
	
	public void setFacilityType(String facilityType) {
		try {
			int index = Integer.parseInt(facilityType);
			facility_value_label.setText(DbUtil.getFacilityType(index));		
		}catch(Exception e) {
			facility_value_label.setText(facilityType);
		}
	}
	
	public void setConnMethod(String connMethod) {
		try {
			String modbusType = null;
			
			if(this.modbusFacility.getCONN_METHOD() == ModbusAgent.CONN_METHOD_MODBUS && this.modbusFacility.getCOMM_PROTOCOL() == ModbusAgent.MODBUS_TYPE_RTU) {				
				modbusType = " ( Type : Modbus RTU )";		
				int index = Integer.parseInt(connMethod);
				connMethod_value_label.setText(DbUtil.getConnMethod(index) + modbusType);				
			}else if(this.modbusFacility.getCONN_METHOD() == ModbusAgent.CONN_METHOD_MODBUS && this.modbusFacility.getCOMM_PROTOCOL() == ModbusAgent.MODBUS_TYPE_TCP) {
				modbusType = " ( Type : Modbus TCP )";	
				int index = Integer.parseInt(connMethod);
				connMethod_value_label.setText(DbUtil.getConnMethod(index) + modbusType);
			}else {
				int index = Integer.parseInt(connMethod);
				connMethod_value_label.setText(DbUtil.getConnMethod(index));
			}
		}catch(Exception e) {
			connMethod_value_label.setText(connMethod);
		}		
	}
	
	public void setMessage(String msg) {
		message_value_label.setText(msg);
	}	
	
	@Override
	public void dispose() {
		SearchFrame.isExist = false;
		super.dispose();
	}
}
