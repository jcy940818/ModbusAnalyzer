package src_en.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import common.util.FontManager;
import src_en.database.DbUtil;
import src_en.database.QueryParameter;
import src_en.database.StoredProcedure;
import src_en.util.ExcelAdapter;
import src_en.util.Util;
import src_en.util.XmlGenerator;
import src_ko.swing.MainFrame;

public class ProcedureGeneratorFrame extends JFrame {

	public static boolean isExist = false;
	private JPanel contentPane;
	private JTable sp_param_table;

	JTextField sp_name;
	JTextArea sp_content;
	JTextArea sp_query;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProcedureGeneratorFrame frame = new ProcedureGeneratorFrame();
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
	public ProcedureGeneratorFrame() {
		setTitle("ModbusAnalyzer : Procedure Generator");		
		setIconImage(new Util().getIconResource().getImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		ProcedureGeneratorFrame.isExist = true;
		setResizable(false);
		
		setBounds(100, 100, 1138, 662);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new LineBorder(new Color(255, 140, 0), 8));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBackground(Color.WHITE);
		contentPane.add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		JLabel lblProcedureGenerator = new JLabel("Procedure Generator");
		lblProcedureGenerator.setForeground(Color.BLACK);
		lblProcedureGenerator.setIcon(new Util().getSubLogoResource());
		lblProcedureGenerator.setHorizontalAlignment(SwingConstants.LEFT);
		lblProcedureGenerator.setFont(FontManager.getFont(Font.BOLD, 22));
		lblProcedureGenerator.setBackground(Color.WHITE);
		lblProcedureGenerator.setBounds(0, 0, 330, 60);
		actualPanel.add(lblProcedureGenerator);
		
		JLabel lblNewLabel = new JLabel("Procedure Name");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(FontManager.getFont(Font.BOLD, 17));
		lblNewLabel.setBounds(14, 77, 163, 33);
		actualPanel.add(lblNewLabel);
		
		sp_name = new JTextField();
		sp_name.setBorder(new LineBorder(Color.BLACK, 2));
		sp_name.setBounds(12, 114, 440, 38);
		sp_name.setForeground(Color.BLACK);
		sp_name.setFont(FontManager.getFont(Font.PLAIN, 16));		
		sp_name.setBackground(Color.WHITE);		
		sp_name.setFocusTraversalKeysEnabled(false);
		sp_name.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {			
				if (e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_ENTER)
					sp_content.requestFocus();
			}
		});
		actualPanel.add(sp_name);
		
		JLabel lblContentsOfThe = new JLabel("Contents of the Procedure");
		lblContentsOfThe.setHorizontalAlignment(SwingConstants.LEFT);
		lblContentsOfThe.setForeground(Color.BLACK);
		lblContentsOfThe.setFont(FontManager.getFont(Font.BOLD, 17));
		lblContentsOfThe.setBackground(Color.WHITE);
		lblContentsOfThe.setBounds(14, 162, 438, 33);
		actualPanel.add(lblContentsOfThe);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new LineBorder(Color.BLACK, 2));
		scrollPane_1.setBounds(12, 199, 440, 80);
		actualPanel.add(scrollPane_1);
		
		sp_content = new JTextArea();
		sp_content.setForeground(Color.BLACK);
		sp_content.setFont(FontManager.getFont(Font.PLAIN, 16));
		sp_content.setBackground(Color.WHITE);
		sp_content.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {			
				if (e.getKeyCode() == KeyEvent.VK_TAB)
					sp_query.requestFocus();
			}
		});
		scrollPane_1.setViewportView(sp_content);
		
		JLabel lblContentsOfThe_1 = new JLabel("Procedure Query");
		lblContentsOfThe_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblContentsOfThe_1.setForeground(Color.BLACK);
		lblContentsOfThe_1.setFont(FontManager.getFont(Font.BOLD, 17));
		lblContentsOfThe_1.setBackground(Color.WHITE);
		lblContentsOfThe_1.setBounds(475, 77, 629, 33);
		actualPanel.add(lblContentsOfThe_1);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBorder(new LineBorder(Color.BLACK, 2));
		scrollPane_2.setBounds(473, 114, 631, 491);
		actualPanel.add(scrollPane_2);
		
		sp_query = new JTextArea();
		sp_query.setForeground(Color.BLACK);
		sp_query.setFont(FontManager.getFont(Font.PLAIN, 16));
		sp_query.setBackground(Color.WHITE);
		scrollPane_2.setViewportView(sp_query);
		
		JLabel lblParametersOfThe = new JLabel("Parameters of the Procedure");
		lblParametersOfThe.setHorizontalAlignment(SwingConstants.LEFT);
		lblParametersOfThe.setForeground(Color.BLACK);
		lblParametersOfThe.setFont(FontManager.getFont(Font.BOLD, 17));
		lblParametersOfThe.setBackground(Color.WHITE);
		lblParametersOfThe.setBounds(14, 289, 243, 33);
		actualPanel.add(lblParametersOfThe);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBorder(new LineBorder(Color.BLACK, 2));
		scrollPane_3.setBounds(12, 326, 440, 279);
		actualPanel.add(scrollPane_3);
		
		sp_param_table = new JTable();		
		sp_param_table.setForeground(Color.BLACK);
		sp_param_table.setFont(FontManager.getFont(Font.PLAIN, 14));
		scrollPane_3.setViewportView(sp_param_table);
		
		resetTable(sp_param_table);
		
		JButton add_button = new JButton("Add");
		add_button.setForeground(Color.BLACK);
		add_button.setFont(FontManager.getFont(Font.BOLD, 14));
		add_button.setBackground(Color.WHITE);
		add_button.setBounds(287, 289, 67, 30);
		add_button.setFocusPainted(false);
		add_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addRecord(sp_param_table);
			}
		});
		actualPanel.add(add_button);
		
		JButton delete_button = new JButton("Delete");
		delete_button.setForeground(Color.BLACK);
		delete_button.setFont(FontManager.getFont(Font.BOLD, 14));
		delete_button.setBackground(Color.WHITE);
		delete_button.setBounds(362, 289, 90, 30);
		delete_button.setFocusPainted(false);
		delete_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selected = sp_param_table.getSelectedRows();					
				removeRecord(selected);
			}
		});
		actualPanel.add(delete_button);
		
		JButton reset_Button = new JButton("Reset");
		reset_Button.setBackground(Color.WHITE);
		reset_Button.setForeground(Color.BLUE);
		reset_Button.setFont(FontManager.getFont(Font.BOLD, 14));
		reset_Button.setBounds(1007, 10, 97, 33);
		reset_Button.setFocusPainted(false);
		reset_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetForm();
				sp_name.requestFocus();
			}
		});
		actualPanel.add(reset_Button);
		
		JButton generateProcedure_button = new JButton("Generate");
		generateProcedure_button.setBackground(Color.WHITE);
		generateProcedure_button.setForeground(Color.BLUE);
		generateProcedure_button.setFont(FontManager.getFont(Font.BOLD, 14));
		generateProcedure_button.setBounds(888, 10, 111, 33);
		generateProcedure_button.setFocusPainted(false);
		generateProcedure_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StoredProcedure sp = createProcedure();
				
				String category = null;
				String procedureSavePath = null;
				String userSavePath = null;
				
				JLabel insert = new JLabel(String.format("<html><span color='blue'>Create a Procedure</span>"
						+ Util.longSeparator
						+ "<br><br>Choose a procedure category" + Util.longSeparator + "<br></html>"));
				insert.setFont(FontManager.getFont(Font.BOLD, 15));
				
//				"Common","Facility", "RCU", "Performance", "Event", "Event History", "Statistics Data"
				String[] categoryContent = new String[] {"Common","Facility", "RCU", "Performance", "Event", "Event History", "Statistics Data"}; 
				JComboBox catogoryComboBox = new JComboBox();				
				catogoryComboBox.setFont(FontManager.getFont(Font.BOLD, 15));
				catogoryComboBox.setBackground(Color.WHITE);
				catogoryComboBox.setModel(new DefaultComboBoxModel(categoryContent));								
				
				Object[] message = { insert, catogoryComboBox };

				
				int option;
				
				if(sp != null) {
					option = JOptionPane.showConfirmDialog(null, message, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
				}else {
					return;
				}

				if (option == JOptionPane.OK_OPTION) {
					category = catogoryComboBox.getSelectedItem().toString();
					
					switch (category) {
						case "Common":
							category = StoredProcedure.COMMON;
							break;						
						case "Facility":
							category = StoredProcedure.SERVERINFO_FACILITY;
							break;		
						case "RCU":
							category = StoredProcedure.SERVERINFO_RTU;
							break;		
						case "Performance":
							category = StoredProcedure.SERVER_PERF;
							break;		
						case "Event":
							category = StoredProcedure.ALARM;
							break;		
						case "Event History":
							category = StoredProcedure.EVENTS;
							break;							
						case "Statistics Data":
							category = StoredProcedure.SERVER_PERFREPORT;
							break;		
						case "User path" :
						default:
							userSavePath = Util.getFilePath() + ".xml";
							break;
					}
				}else {
					// Not OK_OPTION
					return;
				}
				
				procedureSavePath = MainFrame.getCurrentPath() + "\\" + StoredProcedure.STORED_PROCEDURE + "\\" + category + "\\" + sp.getName() + ".xml";
				
				if(userSavePath != null)
					procedureSavePath = userSavePath; 
				
				
				XmlGenerator.generateProcedure(sp, procedureSavePath);
				
			}
		});
		actualPanel.add(generateProcedure_button);
				
		// 프레임이 화면 가운데에서 생성된다		
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void resetTable(JTable table){
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));
		
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		table.setModel(new DefaultTableModel(
				new Object[][] {
					
				},
				new String[] {
					"Index",
					"Parameter Name",
					"Input example",
				}
			) {
				boolean[] columnEditables = new boolean[] {
					false, // 순서
					true, // 파라미터 이름
					true, // 입력 예시
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
		
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false); // 컬럼 위치 임의 변경 불가
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.addKeyListener(new KeyAdapter() {	
			// 셀 내용  삭제
			@Override
			public void keyPressed(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_DELETE ) {
					int[] selectedRows = table.getSelectedRows();
					int[] selectedColumns = table.getSelectedColumns();
					
					for(int row = 0; row < selectedRows.length; row++) {
						for(int column = 0; column < selectedColumns.length; column++) {			
					
							// 사용자가 수정 할 수 있는 셀 내용만 삭제
							if(table.isCellEditable(selectedRows[row], selectedColumns[column])) {
								table.setValueAt("", selectedRows[row], selectedColumns[column]);	
							}
						}
					}					
				}							
			}
		});
		ExcelAdapter ex = new ExcelAdapter(table); // 여러 열 복사 붙여넣기 가능 
		
		table.getColumnModel().getColumn(0).setPreferredWidth(10); // 순서
		table.getColumnModel().getColumn(1).setPreferredWidth(100); // 파라미터 이름
		table.getColumnModel().getColumn(2).setPreferredWidth(200); // 파라미터 입력 예시
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 파라미터 이름
//		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 파라미터 입력 예시
	}
	
	/**
	 * 	레코드 추가 메소드 
	 */
	public static void addRecord(JTable table) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();						
				
				record = new Vector();
				int index = 0;
				
				if(table.getRowCount() <= 0) {
					// 테이블의 행 개수가 0개 일 경우 : index = 1
					index = 1;
				}else if(table.getRowCount() >= 1){
					// 테이블의 행 개수가 최소 1개 이상 일 경우 마지막 레코드의 ( 순서 컬럼 값 + 1 )
					index = Integer.parseInt(String.valueOf(table.getValueAt(table.getRowCount()-1, 0))) + 1;				
				}
				
				/* column[0] */ record.add(String.valueOf(index)); // 순서
				/* column[1] */ record.add(""); // 성능명
				/* column[2] */ record.add("");  // 성능 카운터				
				
				model.addRow(record);					
			
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();
		}
	}
	
	/**
	 * 레코드 삭제 메소드
	 */
	public void removeRecord(int... index) {
		DefaultTableModel model = (DefaultTableModel) sp_param_table.getModel();
		
		if(index.length == 0) {
			// 선택 된 행이 없거나
			if(sp_param_table.getRowCount()==0) {
				// 테이블에 행이 없을 경우 아무것도 수행하지 않음
				return;
			}else {
				model.removeRow(sp_param_table.getRowCount()-1);
			}
		}

		// index[0] : 1번째 레코드
		// index[1] : 2번째 레코드
		// index[2] : 3번째 레코드
		// 위의 경우 index[0] (첫번째 레코드)를 삭제하면
		// index[1] (두번째 레코드)이 index[0] (두번째 레코드)가 되기 때문에
		// model.revmoe(index[0]) 로직을 수행한다
		for(int i = 0; i < index.length; i++) {
			model.removeRow(index[0]);
		}
	}
	
	public StoredProcedure createProcedure() {
		StoredProcedure sp = new StoredProcedure();
		boolean isValid = true;
		int inValidCount = 0;
		
		String name =  sp_name.getText().trim();
		String content = sp_content.getText().trim();
		String query = sp_query.getText().trim();
		
		StringBuilder sb = new StringBuilder("<font color='red'>Procedure Form Error</font>\n");
		
		sb.append("Please enter ");
		
		// 프로시저 이름 검사
		if (name.length() < 1) {
			sb.append(Util.colorBlue("[Procedure Name]"));			
			isValid = false;
			inValidCount++;
		}
						
		// 프로시저 내용 검사
		if(content.length() < 1) {
			if(inValidCount > 0)
				sb.append(Util.colorBlue(", [Contents of the Procedure]"));
			else
				sb.append(Util.colorBlue("[Contents of the Procedure]"));
			
			isValid = false;
			inValidCount++;
		}
		
		// 프로시저 수행 쿼리 검사
		if(query.length() < 1) {
			if(inValidCount > 0)
				sb.append(Util.colorBlue(", [Procedure Query]"));
			else
				sb.append(Util.colorBlue("[Procedure Query]"));
			
			isValid = false;
			inValidCount++;
		}
		
		// 프로시저 수행 쿼리 내용 검사
		if(!(query.length() < 1)) {
			if(DbUtil.checkQuery(query)) {
				ArrayList<QueryParameter> paramList = getPramList(sp_param_table);				
				
				sp.setName(name);
				sp.setContent(content);
				sp.setQuery(query);
				sp.setParamList(paramList);
				if(paramList.size() > 0) {
					sp.setUseParam(true);
				}else {
					sp.setUseParam(false);
				}
				
				// 프로시저 수행 쿼리 파라미터 매핑 확인
				if(!checkParamterMapping(sp)) 
					return null;
			}else {
				return null;
			}
		}
		
		sb.append(" information" + Util.separator + "\n");
		
		if(isValid) {
			return sp;
		}else {
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	private ArrayList<QueryParameter> getPramList(JTable paramTable) {
		ArrayList<QueryParameter> paramList = new ArrayList<QueryParameter>();
		if(paramTable.getRowCount() == 0) return paramList; // 파라미터가 필요하지 않은 프로시저 일 경우
		
		int paramIndex = 0;
		
		for(int i = 0; i < paramTable.getRowCount(); i++) {
			String paramName = paramTable.getValueAt(i, 1).toString().trim();
			
			if(paramName.length() < 1) {
				// (파라미터 입력 예시가 입력되어 있더라도) 파라미터 이름이 입력되지 않았으면 패스 
				continue;
			}else {
				QueryParameter param = new QueryParameter();
				param.setIndex(paramIndex++); // 파라미터 인덱스
				param.setName(paramName); // 파라미터 이름
				param.setExample(paramTable.getValueAt(i, 2).toString().trim()); // 파라미터 입력 예시
				paramList.add(param);
			}
		}
	
		Collections.sort(paramList);
		return paramList;
	}
	
	public void resetForm() {
		sp_name.setText(null);
		sp_content.setText(null);
		sp_query.setText(null);
		resetTable(sp_param_table);
	}
	
	
	// 파라미터 검사
	public static boolean checkParamterMapping(StoredProcedure sp) {
		
		// 파라미터가 없는 프로시저일 경우 패스
		if(!sp.isUseParam()) {
			return true;
		}

		// 쿼리 템플릿 내용 검사 (쿼리의 파라미터 개수와 파라미터 매핑 내용을 확인)

		ArrayList<QueryParameter> paramList = sp.getParamList();
		
		for(int i = 0; i < paramList.size(); i++) {
			boolean checkParam = sp.getQuery().contains(String.format("[param%d]", i+1));
			
			if(!checkParam) {
				StringBuilder sb = new StringBuilder();
				sb.append(Util.colorRed("StoredProcedure Parameter Mapping Error\n"));		
				
//				sb.append(String.format("%s%s%s\n\n", Util.colorBlue(sp.getName()) + " 프로시저의 파라미터 매핑중 문제가 발생하였습니다", Util.separator, Util.separator));
				sb.append(String.format("%s%s%s\n\n", "There was a problem mapping the parameter contents of procedure " + Util.colorBlue(sp.getName()), Util.separator, Util.separator));
				
//				sb.append(String.format("현재 생성하려는 프로시저는 설정된 파라미터 개수가 %s개 입니다%s%s\n\n", Util.colorBlue(String.valueOf(paramList.size())) , Util.separator, Util.separator));
				sb.append(String.format("The procedure has %s parameters(&lt;param&gt; Tag) set%s%s\n\n", Util.colorBlue(String.valueOf(paramList.size())) , Util.separator, Util.separator));
				
//				sb.append(String.format("즉 프로시저의 수행 쿼리 내용에 %s 문자열이 포함되어 있어야 합니다%s%s\n\n", 
				sb.append(String.format("Therefore, the query content of the procedure must contain a %s string%s%s\n\n",				
						(paramList.size() == 1) ? Util.colorBlue(String.format("[param1]")) : Util.colorBlue(String.format("[param1] ~ [param%d]", paramList.size())), 
						Util.separator, Util.separator));
				
//				sb.append(String.format("하지만 현재 입력된 프로시저 수행 쿼리에서 %s 내용을 찾을 수 없습니다%s%s\n\n\n",
				sb.append(String.format("However, I can't find the %s content in the currently set query(&lt;query&gt; Tag) content%s%s\n\n\n",
						Util.colorRed(String.format("[param%d] ( %s )", i+1, paramList.get(i).getName())),
						Util.separator, Util.separator));
				
//				sb.append(String.format("%s\n", Util.colorGreen(String.format("%s 프로시저의 파라미터", sp.getName()))));
				sb.append(String.format("%s\n", Util.colorGreen(String.format("Parameters of procedure %s", sp.getName()))));
				for(int i2 = 0; i2 < paramList.size(); i2++) {
					QueryParameter param = paramList.get(i2);
					sb.append(String.format("%d. %s = %s\n",i2+1 ,String.format("[param%d]", i2+1),param.getName()));
				}
						
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);				
				return false;
			}				
		}
		
		return true;
	}
	
	
	@Override
	public void dispose() {
		ProcedureGeneratorFrame.isExist = false;
		super.dispose();
	}
}
