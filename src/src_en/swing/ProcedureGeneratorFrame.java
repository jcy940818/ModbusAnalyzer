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
				
		// �������� ȭ�� ������� �����ȴ�		
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
					false, // ����
					true, // �Ķ���� �̸�
					true, // �Է� ����
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
		
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false); // �÷� ��ġ ���� ���� �Ұ�
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.addKeyListener(new KeyAdapter() {	
			// �� ����  ����
			@Override
			public void keyPressed(KeyEvent e) {
				if( e.getKeyCode() == KeyEvent.VK_DELETE ) {
					int[] selectedRows = table.getSelectedRows();
					int[] selectedColumns = table.getSelectedColumns();
					
					for(int row = 0; row < selectedRows.length; row++) {
						for(int column = 0; column < selectedColumns.length; column++) {			
					
							// ����ڰ� ���� �� �� �ִ� �� ���븸 ����
							if(table.isCellEditable(selectedRows[row], selectedColumns[column])) {
								table.setValueAt("", selectedRows[row], selectedColumns[column]);	
							}
						}
					}					
				}							
			}
		});
		ExcelAdapter ex = new ExcelAdapter(table); // ���� �� ���� �ٿ��ֱ� ���� 
		
		table.getColumnModel().getColumn(0).setPreferredWidth(10); // ����
		table.getColumnModel().getColumn(1).setPreferredWidth(100); // �Ķ���� �̸�
		table.getColumnModel().getColumn(2).setPreferredWidth(200); // �Ķ���� �Է� ����
		
		// DefaultTableCellHeaderRenderer ���� (��� ������ ����)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer�� ������ ��� ���ķ� ����
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// ������ ���̺��� ColumnModel�� ������
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // �� ��
		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // �Ķ���� �̸�
//		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // �Ķ���� �Է� ����
	}
	
	/**
	 * 	���ڵ� �߰� �޼ҵ� 
	 */
	public static void addRecord(JTable table) {
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();						
				
				record = new Vector();
				int index = 0;
				
				if(table.getRowCount() <= 0) {
					// ���̺��� �� ������ 0�� �� ��� : index = 1
					index = 1;
				}else if(table.getRowCount() >= 1){
					// ���̺��� �� ������ �ּ� 1�� �̻� �� ��� ������ ���ڵ��� ( ���� �÷� �� + 1 )
					index = Integer.parseInt(String.valueOf(table.getValueAt(table.getRowCount()-1, 0))) + 1;				
				}
				
				/* column[0] */ record.add(String.valueOf(index)); // ����
				/* column[1] */ record.add(""); // ���ɸ�
				/* column[2] */ record.add("");  // ���� ī����				
				
				model.addRow(record);					
			
		}catch(Exception e) {
			// ���ڵ� �߰� �� ���� �߻� �� �ƹ��͵� �������� ����
			e.printStackTrace();
		}
	}
	
	/**
	 * ���ڵ� ���� �޼ҵ�
	 */
	public void removeRecord(int... index) {
		DefaultTableModel model = (DefaultTableModel) sp_param_table.getModel();
		
		if(index.length == 0) {
			// ���� �� ���� ���ų�
			if(sp_param_table.getRowCount()==0) {
				// ���̺� ���� ���� ��� �ƹ��͵� �������� ����
				return;
			}else {
				model.removeRow(sp_param_table.getRowCount()-1);
			}
		}

		// index[0] : 1��° ���ڵ�
		// index[1] : 2��° ���ڵ�
		// index[2] : 3��° ���ڵ�
		// ���� ��� index[0] (ù��° ���ڵ�)�� �����ϸ�
		// index[1] (�ι�° ���ڵ�)�� index[0] (�ι�° ���ڵ�)�� �Ǳ� ������
		// model.revmoe(index[0]) ������ �����Ѵ�
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
		
		// ���ν��� �̸� �˻�
		if (name.length() < 1) {
			sb.append(Util.colorBlue("[Procedure Name]"));			
			isValid = false;
			inValidCount++;
		}
						
		// ���ν��� ���� �˻�
		if(content.length() < 1) {
			if(inValidCount > 0)
				sb.append(Util.colorBlue(", [Contents of the Procedure]"));
			else
				sb.append(Util.colorBlue("[Contents of the Procedure]"));
			
			isValid = false;
			inValidCount++;
		}
		
		// ���ν��� ���� ���� �˻�
		if(query.length() < 1) {
			if(inValidCount > 0)
				sb.append(Util.colorBlue(", [Procedure Query]"));
			else
				sb.append(Util.colorBlue("[Procedure Query]"));
			
			isValid = false;
			inValidCount++;
		}
		
		// ���ν��� ���� ���� ���� �˻�
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
				
				// ���ν��� ���� ���� �Ķ���� ���� Ȯ��
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
		if(paramTable.getRowCount() == 0) return paramList; // �Ķ���Ͱ� �ʿ����� ���� ���ν��� �� ���
		
		int paramIndex = 0;
		
		for(int i = 0; i < paramTable.getRowCount(); i++) {
			String paramName = paramTable.getValueAt(i, 1).toString().trim();
			
			if(paramName.length() < 1) {
				// (�Ķ���� �Է� ���ð� �ԷµǾ� �ִ���) �Ķ���� �̸��� �Էµ��� �ʾ����� �н� 
				continue;
			}else {
				QueryParameter param = new QueryParameter();
				param.setIndex(paramIndex++); // �Ķ���� �ε���
				param.setName(paramName); // �Ķ���� �̸�
				param.setExample(paramTable.getValueAt(i, 2).toString().trim()); // �Ķ���� �Է� ����
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
	
	
	// �Ķ���� �˻�
	public static boolean checkParamterMapping(StoredProcedure sp) {
		
		// �Ķ���Ͱ� ���� ���ν����� ��� �н�
		if(!sp.isUseParam()) {
			return true;
		}

		// ���� ���ø� ���� �˻� (������ �Ķ���� ������ �Ķ���� ���� ������ Ȯ��)

		ArrayList<QueryParameter> paramList = sp.getParamList();
		
		for(int i = 0; i < paramList.size(); i++) {
			boolean checkParam = sp.getQuery().contains(String.format("[param%d]", i+1));
			
			if(!checkParam) {
				StringBuilder sb = new StringBuilder();
				sb.append(Util.colorRed("StoredProcedure Parameter Mapping Error\n"));		
				
//				sb.append(String.format("%s%s%s\n\n", Util.colorBlue(sp.getName()) + " ���ν����� �Ķ���� ������ ������ �߻��Ͽ����ϴ�", Util.separator, Util.separator));
				sb.append(String.format("%s%s%s\n\n", "There was a problem mapping the parameter contents of procedure " + Util.colorBlue(sp.getName()), Util.separator, Util.separator));
				
//				sb.append(String.format("���� �����Ϸ��� ���ν����� ������ �Ķ���� ������ %s�� �Դϴ�%s%s\n\n", Util.colorBlue(String.valueOf(paramList.size())) , Util.separator, Util.separator));
				sb.append(String.format("The procedure has %s parameters(&lt;param&gt; Tag) set%s%s\n\n", Util.colorBlue(String.valueOf(paramList.size())) , Util.separator, Util.separator));
				
//				sb.append(String.format("�� ���ν����� ���� ���� ���뿡 %s ���ڿ��� ���ԵǾ� �־�� �մϴ�%s%s\n\n", 
				sb.append(String.format("Therefore, the query content of the procedure must contain a %s string%s%s\n\n",				
						(paramList.size() == 1) ? Util.colorBlue(String.format("[param1]")) : Util.colorBlue(String.format("[param1] ~ [param%d]", paramList.size())), 
						Util.separator, Util.separator));
				
//				sb.append(String.format("������ ���� �Էµ� ���ν��� ���� �������� %s ������ ã�� �� �����ϴ�%s%s\n\n\n",
				sb.append(String.format("However, I can't find the %s content in the currently set query(&lt;query&gt; Tag) content%s%s\n\n\n",
						Util.colorRed(String.format("[param%d] ( %s )", i+1, paramList.get(i).getName())),
						Util.separator, Util.separator));
				
//				sb.append(String.format("%s\n", Util.colorGreen(String.format("%s ���ν����� �Ķ����", sp.getName()))));
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
