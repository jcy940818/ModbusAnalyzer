package src_ko.database;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import src_ko.swing.DatabaseAccess_Panel;
import src_ko.swing.MessageFrame;
import src_ko.util.Util;

public class Condition {
	private static Font titlefont = FontManager.getFont(Font.BOLD, 19);
	private static Font boldfont = FontManager.getFont(Font.BOLD, 17);	
	private static Font plainfont = FontManager.getFont(Font.PLAIN, 15);
		
	public static int ConditionCount = 0;
	
	// �÷� �˻� ����� ���� �ʵ�
	private static ArrayList searchColumnList; // �÷� �׸� ����Ʈ
	private static JTextField columnSearch_textField;
	private static Column lastFindColumn = null;	
	private static JComboBox columnList_ComboBox;
	
	public static final int OPERATOR_AND = 0; // AND �� ������ 
	public static final int OPERATOR_OR = 0; // OR �� ������
	
	public static final int CONDITION_EQUAL = 0; // ���� '='
	public static final int CONDITION_NOT_EQUAL = 1; // ���� �ʴ� '!='	
	public static final int CONDITION_MORE = 2; // �̻� '>='
	public static final int CONDITION_LESS = 3; // ���� '<='
	public static final int CONDITION_EXCEED = 4; // �ʰ� '>'
	public static final int CONDITION_LESS_THAN = 5; // �̸� '<'
	public static final int CONDITION_START_WITH = 6; // Ư�� ���ڷ� ���� LIKE '?%'
	public static final int CONDITION_END_WITH = 7; // Ư�� ���ڷ� �� LIKE '%?'
	public static final int CONDITION_INCLUDE = 8; // Ư�� ���ڸ� ���� LIKE '%?%'
	public static final int CONDITION_NOT_INCLUDE = 9; // Ư�� ���ڸ� �������� �ʴ� NOT LIKE '%?%'
	public static final int CONDITION_START_WITH_CASE = 10; // ��ҹ��ڸ� �����Ͽ� Ư�� ���ڷ� ���� LIKE '?%'
	public static final int CONDITION_END_WITH_CASE = 11; // ��ҹ��ڸ� �����Ͽ� Ư�� ���ڷ� �� LIKE '%?'
	public static final int CONDITION_INCLUDE_CASE = 12; // ��ҹ��ڸ� �����Ͽ� Ư�� ���ڸ� ���� LIKE '%?%'
	public static final int CONDITION_NOT_INCLUDE_CASE = 13; // ��ҹ��ڸ� �����Ͽ� Ư�� ���ڸ� �������� �ʴ� NOT LIKE '%?%'
		
	public static boolean isValue(int condition) {
		switch(condition) {
			case CONDITION_EQUAL : return false; // =
			case CONDITION_NOT_EQUAL : return false; // !=
			case CONDITION_MORE : return true; // >=
			case CONDITION_LESS : return true; // <=
			case CONDITION_EXCEED : return true; // >
			case CONDITION_LESS_THAN : return true; // <
			case CONDITION_START_WITH : return false; // LIKE '?%'
			case CONDITION_END_WITH : return false; // LIKE '%?'		
			case CONDITION_INCLUDE : return false; // LIKE '%?%'
			case CONDITION_NOT_INCLUDE : return false; // NOT LIKE '%?%'
			case CONDITION_START_WITH_CASE : return false; // COLLATE Korean_Wansung_CS_AS LIKE '?%'
			case CONDITION_END_WITH_CASE : return false; // COLLATE Korean_Wansung_CS_AS LIKE '%?'	
			case CONDITION_INCLUDE_CASE : return false; // COLLATE Korean_Wansung_CS_AS LIKE '%?%'
			case CONDITION_NOT_INCLUDE_CASE : return false; // COLLATE Korean_Wansung_CS_AS NOT LIKE '%?%'
			default : return false;
		}
	}
	
	private boolean isFirst = false; // ó�� �߰� �� �������� ���� (�ι�° ���Ǻ��ʹ� �� �����ڸ� ����Ͽ��� �ϱ� ������)
	private int index; // 1. �ε���
	private int operator; // 2. �� ������
	private int conditionContent; // 3. ����
	private Column column; // 4. �÷�
	private String param; // 5. �Ķ����
	
	
	// ù��° ���������� ����
	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}
	
	// 1. �ε���
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	
	// 2. �� ������
	public int getOperator() {
		return operator;
	}
	public void setOperator(int operator) {
		this.operator = operator;
	}	
	
		
	// 3. ���� ����
	public int getConditionContent() {
		return conditionContent;
	}
	public void setConditionContent(int conditionContent) {
		this.conditionContent = conditionContent;
	}
	
	// 4. �÷�
	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}

	
	// 5. �Ķ����
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}


	
	public static Condition createCondition() {
		Condition condition = null;
		searchColumnList = DatabaseAccess_Panel.getColumnList();
				
		if(searchColumnList == null) {
			return null;
		}else if(searchColumnList.size() < 1) {
			return null;
		}
		
		columnSearch_textField = new JTextField();
		columnSearch_textField.setFont(boldfont);
		columnSearch_textField.setBackground(Color.WHITE);
		columnSearch_textField.setBorder(new LineBorder(Color.DARK_GRAY, 2));
		columnSearch_textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) return;
				searchColumnDiscription(null);
			}
			
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) return;
				searchColumnDiscription(null);
			}			
		});
		columnSearch_textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchColumnDiscription(lastFindColumn);
			}
		});
			
		
		try {											 			
			JLabel title = new JLabel("<html>���� �˻� ���� �߰�<br><br></html>");
			title.setFont(titlefont);
			
			//�ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
			
			// 1. �� ������ ���� ���ڿ�
			String selectOperator = "<html><font color='blue'>1. �� ������ ����</font></html>";
			JLabel insert = new JLabel(selectOperator);
			insert.setFont(boldfont);
			
			// 1. �� ������ ���� �޺��ڽ�
			JComboBox operator_ComboBox = new JComboBox();
			String[] operators = {
					"AND ����",
					"OR ����",
			};
			operator_ComboBox.setModel(new DefaultComboBoxModel(operators));				
			operator_ComboBox.setFont(boldfont);			
			operator_ComboBox.setBackground(Color.WHITE);
			
			
			JButton operator_help = new JButton("�� ������ ����");
			operator_help.setForeground(Color.BLACK);
			operator_help.setFont(FontManager.getFont(Font.BOLD, 16));
			operator_help.setBackground(Color.LIGHT_GRAY);				
			operator_help.setSize(77, 32);
			operator_help.setHorizontalAlignment(SwingConstants.LEFT);
			operator_help.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.getRootFrame().dispose(); // �˻� ���� �߰� ��ȭ���ڴ� ����ȴ�
					MessageFrame msg_frame = new MessageFrame("�� ������", operatorDiscription); // ����ڿ��� ������ �� �����ڿ� ���� ����
					msg_frame.setFocusable(true);
					msg_frame.requestFocus();
				}
			});
			
			
			//	�ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
			
			
			// 2. �÷� ���� ���ڿ�
			String selectColumn = "<html><br><font color='blue'>2. ���� �÷� ����</font></html>";																		
			JLabel insert2 = new JLabel(selectColumn);
			insert2.setFont(boldfont);
			
			// 2. �÷� ���� �޺��ڽ�
			columnList_ComboBox = new JComboBox();
			columnList_ComboBox.setFont(boldfont);			
			columnList_ComboBox.setBackground(Color.WHITE);
//			columnList_ComboBox.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					// �÷� �޺��ڽ��� ������ ���� �� ����ڰ� ������ �÷������� ������ �����ϰ� �����ش�
//					JComboBox temp = (JComboBox)e.getSource();								
//					Column item = (Column)temp.getSelectedItem();										
//					
//					String msg = String.format("<font color='blue'>�÷� ���� Ȯ��</font>\n���̺� �� : %s\n\n�÷� �� : %s%s\n", item.getTableName(), item.getColumnName(), Util.longSeparator);
//					Util.showMessage(msg, JOptionPane.PLAIN_MESSAGE);											
//				}
//			});
			
			
			// �÷� ����Ʈ �޺��ڽ� ������ �ʱ�ȭ
			if(searchColumnList != null) {
				Object[] colunms = searchColumnList.toArray();
				columnList_ComboBox.setModel(new DefaultComboBoxModel(colunms));
			}
			
			
			//	�ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
			
			// 3. ���� ���� ���ڿ�
			String selectCondition = "<html><br><font color='blue'>3. ���� ����</font></html>";
			JLabel insert3 = new JLabel(selectCondition);
			insert3.setFont(boldfont);
			
			// 3. ���� ���� �޺��ڽ�
			JComboBox condition_ComboBox = new JComboBox();
			String[] conditions = {
					"1. �÷� ������ �Ķ���� ����� ��ġ�ϴ� �׸��� ��ȸ                  ( �÷� = �Ķ���� )",
					"2. �÷� ������ �Ķ���� ����� ��ġ���� �ʴ� �׸��� ��ȸ           ( �÷� != �Ķ���� )",
					
					"3. �÷� ���� �Ķ���� ������ �̻��� �׸��� ��ȸ                        ( �÷� >= �Ķ���� )",
					"4. �÷� ���� �Ķ���� ������ ������ �׸��� ��ȸ                        ( �÷� <= �Ķ���� )",
					
					"5. �÷� ���� �Ķ���� ������ �ʰ��� �׸��� ��ȸ                        ( �÷� > �Ķ���� )",
					"6. �÷� ���� �Ķ���� ������ �̸��� �׸��� ��ȸ                        ( �÷� < �Ķ���� )",
					
					"7. �÷� ������ �Ķ���� �������� �����ϴ� �׸��� ��ȸ               ( ��ҹ��� ���о��� : �÷� ������ Ư�� ���ڷ� �����ϴ� )",
					"8. �÷� ������ �Ķ���� �������� ������ �׸��� ��ȸ                  ( ��ҹ��� ���о��� : �÷� ������ Ư�� ���ڷ� ������ )",					
					"9. �÷� ���뿡 �Ķ���� ������ �����ϴ� �׸��� ��ȸ                  ( ��ҹ��� ���о��� : �÷� ���뿡 Ư�� ���ڸ� �����ϴ� )",
					"10. �÷� ���뿡 �Ķ���� ������ �������� �ʴ� �׸��� ��ȸ         ( ��ҹ��� ���о��� : �÷� ���뿡 Ư�� ���ڸ� �������� �ʴ� )",					
					
					"11. �÷� ������ �Ķ���� �������� �����ϴ� �׸��� ��ȸ             ( ��ҹ��ڸ� �����Ͽ� : �÷� ������ Ư�� ���ڷ� �����ϴ� )",
					"12. �÷� ������ �Ķ���� �������� ������ �׸��� ��ȸ                ( ��ҹ��ڸ� �����Ͽ� : �÷� ������ Ư�� ���ڷ� ������ )",					
					"13. �÷� ���뿡 �Ķ���� ������ �����ϴ� �׸��� ��ȸ                ( ��ҹ��ڸ� �����Ͽ� : �÷� ���뿡 Ư�� ���ڸ� �����ϴ� )",
					"14. �÷� ���뿡 �Ķ���� ������ �������� �ʴ� �׸��� ��ȸ         ( ��ҹ��ڸ� �����Ͽ� : �÷� ���뿡 Ư�� ���ڸ� �������� �ʴ� )"	
			};
			condition_ComboBox.setFont(boldfont);			
			condition_ComboBox.setBackground(Color.WHITE);
			condition_ComboBox.setModel(new DefaultComboBoxModel(conditions));

						
			// 4. ���� �Ķ���� ���ڿ�
			String conditionParam = "<html><br><font color='blue'>4. ���� �Ķ���� �Է�</font></html>";
			JLabel insert4 = new JLabel(conditionParam);
			insert4.setFont(boldfont);
			
			// 4. ���� �Ķ���� �Է� ��
			JTextField inputParam = new JTextField();			
			inputParam.setBackground(Color.WHITE);
			inputParam.setSize(77, 32);
			inputParam.setFont(boldfont);						
			inputParam.setBorder(new LineBorder(Color.DARK_GRAY, 2));
			
			JLabel separator = new JLabel(superSeparator());
			
			
			//	�ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
			
			// ���� �߰� ��ȭ���� ����
			Object[] message = {
					title,
					insert, operator_ComboBox, operator_help, // 1. �� ������ ���� ( AND, OR )
					insert2, columnSearch_textField, columnList_ComboBox, // 2. ���� �÷� ����										
					insert3, condition_ComboBox, // 3. ���� ����
					insert4, inputParam, // 4. ���� �Ķ����
					separator,										
			};			

			int option = JOptionPane.showConfirmDialog(null, message, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);			

			//	�ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
			
			
			if (option == JOptionPane.OK_OPTION) {								
				
				if(inputParam.getText().length() < 1) {
					Util.showMessage("<font color='red'>�˻� ���� �߰� ����</font>\n���� �Ķ���� ������ �Էµ��� �ʾ� �˻� ���� �߰��� �����Ͽ����ϴ�&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				// �������� codition �ν��Ͻ� �ʱ�ȭ
				condition = new Condition();								
				condition.setFirst(false);
				condition.setIndex(Condition.ConditionCount++); // 1. �ε���
				condition.setOperator(operator_ComboBox.getSelectedIndex()); // 2. ��������
				condition.setConditionContent(condition_ComboBox.getSelectedIndex()); // 3. ����
				condition.setColumn((Column)columnList_ComboBox.getSelectedItem()); // 4. �÷�
				condition.setParam(inputParam.getText()); // 5. �Ķ����				
			} else {
				// Util.showMessage("<font color='red'>SQL Exception</font>\n�˻� ���� �߰� �۾��� ����Ͽ����ϴ�&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
				// ����ڰ� ��ȭ���ڸ� �ݰų� ��Ҹ� Ŭ�� ��			
				return null;
			}
			
			// �������� condition �ν��Ͻ� ����			
			return condition;
			
		}catch(Exception exception) {
			// �˻� ���� �߰� ��ư�� ������ ���ο��� �Ͼ�� ��� ���ܸ� ó��
			// ��� ���� �߻� �� null ����
			return null;
		}	
	}
	
	
	// ù��° ���� ���� �� ���
	public static Condition createFirstCondition() {
		Condition condition = null;
		searchColumnList = DatabaseAccess_Panel.getColumnList();
				
		if(searchColumnList == null) {
			return null;
		}else if(searchColumnList.size() < 1) {
			return null;
		}
		
		columnSearch_textField = new JTextField();
		columnSearch_textField.setFont(boldfont);			
		columnSearch_textField.setBackground(Color.WHITE);
		columnSearch_textField.setBorder(new LineBorder(Color.DARK_GRAY, 2));
		columnSearch_textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) return;
				searchColumnDiscription(null);
			}
			
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) return;
				searchColumnDiscription(null);
			}			
		});
		columnSearch_textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchColumnDiscription(lastFindColumn);
			}
		});
		
		
		try {											 			
			JLabel title = new JLabel("<html>���� �˻� ���� �߰�<br></html>");
			title.setFont(titlefont);			
									
			// 1. �÷� ���� ���ڿ�
			String selectColumn = "<html><br><font color='blue'>1. ���� �÷� ����</font></html>";																		
			JLabel insert = new JLabel(selectColumn);
			insert.setFont(boldfont);
			
			// 1. �÷� ���� �޺��ڽ�
			columnList_ComboBox = new JComboBox();
			columnList_ComboBox.setFont(boldfont);			
			columnList_ComboBox.setBackground(Color.WHITE);
//			columnList_ComboBox.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					// �÷� �޺��ڽ��� ������ ���� �� ����ڰ� ������ �÷������� ������ �����ϰ� �����ش�
//					JComboBox temp = (JComboBox)e.getSource();								
//					Column item = (Column)temp.getSelectedItem();										
//					
//					String msg = String.format("<font color='blue'>�÷� ���� Ȯ��</font>\n���̺� �� : %s\n\n�÷� �� : %s%s\n", item.getTableName(), item.getColumnName(), Util.longSeparator);
//					Util.showMessage(msg, JOptionPane.PLAIN_MESSAGE);											
//				}
//			});
			
			
			// �÷� ����Ʈ �޺��ڽ� ������ �ʱ�ȭ
			if(searchColumnList != null) {
				Object[] colunms = searchColumnList.toArray();
				columnList_ComboBox.setModel(new DefaultComboBoxModel(colunms));
			}
			
			
			
			//	�ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
			
			// 2. ���� ���� ���ڿ�
			String selectCondition = "<html><br><font color='blue'>2. ���� ����</font></html>";
			JLabel insert2 = new JLabel(selectCondition);
			insert2.setFont(boldfont);
			
			// 2. ���� ���� �޺��ڽ�
			JComboBox condition_ComboBox = new JComboBox();
			String[] conditions = {
					"1. �÷� ������ �Ķ���� ����� ��ġ�ϴ� �׸��� ��ȸ                  ( �÷� = �Ķ���� )",
					"2. �÷� ������ �Ķ���� ����� ��ġ���� �ʴ� �׸��� ��ȸ           ( �÷� != �Ķ���� )",
					
					"3. �÷� ���� �Ķ���� ������ �̻��� �׸��� ��ȸ                        ( �÷� >= �Ķ���� )",
					"4. �÷� ���� �Ķ���� ������ ������ �׸��� ��ȸ                        ( �÷� <= �Ķ���� )",
					
					"5. �÷� ���� �Ķ���� ������ �ʰ��� �׸��� ��ȸ                        ( �÷� > �Ķ���� )",
					"6. �÷� ���� �Ķ���� ������ �̸��� �׸��� ��ȸ                        ( �÷� < �Ķ���� )",
					
					"7. �÷� ������ �Ķ���� �������� �����ϴ� �׸��� ��ȸ               ( ��ҹ��� ���о��� : �÷� ������ Ư�� ���ڷ� �����ϴ� )",
					"8. �÷� ������ �Ķ���� �������� ������ �׸��� ��ȸ                  ( ��ҹ��� ���о��� : �÷� ������ Ư�� ���ڷ� ������ )",					
					"9. �÷� ���뿡 �Ķ���� ������ �����ϴ� �׸��� ��ȸ                  ( ��ҹ��� ���о��� : �÷� ���뿡 Ư�� ���ڸ� �����ϴ� )",
					"10. �÷� ���뿡 �Ķ���� ������ �������� �ʴ� �׸��� ��ȸ         ( ��ҹ��� ���о��� : �÷� ���뿡 Ư�� ���ڸ� �������� �ʴ� )",					
					
					"11. �÷� ������ �Ķ���� �������� �����ϴ� �׸��� ��ȸ             ( ��ҹ��ڸ� �����Ͽ� : �÷� ������ Ư�� ���ڷ� �����ϴ� )",
					"12. �÷� ������ �Ķ���� �������� ������ �׸��� ��ȸ                ( ��ҹ��ڸ� �����Ͽ� : �÷� ������ Ư�� ���ڷ� ������ )",					
					"13. �÷� ���뿡 �Ķ���� ������ �����ϴ� �׸��� ��ȸ                ( ��ҹ��ڸ� �����Ͽ� : �÷� ���뿡 Ư�� ���ڸ� �����ϴ� )",
					"14. �÷� ���뿡 �Ķ���� ������ �������� �ʴ� �׸��� ��ȸ         ( ��ҹ��ڸ� �����Ͽ� : �÷� ���뿡 Ư�� ���ڸ� �������� �ʴ� )"				
			};
			condition_ComboBox.setFont(boldfont);			
			condition_ComboBox.setBackground(Color.WHITE);
			condition_ComboBox.setModel(new DefaultComboBoxModel(conditions));
			
			//	�ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
			
						
			// 3. ���� �Ķ���� ���ڿ�
			String conditionParam = "<html><br><font color='blue'>3. ���� �Ķ���� �Է�</font></html>";
			JLabel insert3 = new JLabel(conditionParam);
			insert3.setFont(boldfont);
			
			// 3. ���� �Ķ���� �Է� ��
			JTextField inputParam = new JTextField();			
			inputParam.setBackground(Color.WHITE);
			inputParam.setSize(77, 32);
			inputParam.setFont(boldfont);						
			inputParam.setBorder(new LineBorder(Color.DARK_GRAY, 2));
			
			JLabel separator = new JLabel(superSeparator());
			
			
			//�ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
			
			// ���� �߰� ��ȭ���� ����
			Object[] message = {
					title,
					insert, columnSearch_textField, columnList_ComboBox, // 1. ���� �÷� ����										
					insert2, condition_ComboBox, // 2. ���� ����
					insert3, inputParam, // 3. ���� �Ķ����
					separator,										
			};			

			int option = JOptionPane.showConfirmDialog(null, message, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);			

			//	�ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�
			
			
			if (option == JOptionPane.OK_OPTION) {		
				
				if(inputParam.getText().length() < 1) {
					Util.showMessage("<font color='red'>�˻� ���� �߰� ����</font>\n���� �Ķ���� ������ �Էµ��� �ʾ� �˻� ���� �߰��� �����Ͽ����ϴ�&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				// �������� codition �ν��Ͻ� �ʱ�ȭ
				condition = new Condition();			
				condition.setFirst(true);
				condition.setIndex(Condition.ConditionCount++); // 1. �ε���
				condition.setOperator(999); // 2. �������� ( 999: �� ������ ���� )
				condition.setConditionContent(condition_ComboBox.getSelectedIndex()); // 3. ����
				condition.setColumn((Column)columnList_ComboBox.getSelectedItem()); // 4. �÷�
				condition.setParam(inputParam.getText()); // 5. �Ķ����				
			} else {
				// Util.showMessage("<font color='red'>SQL Exception</font>\n�˻� ���� �߰� �۾��� ����Ͽ����ϴ�&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
				// ����ڰ� ��ȭ���ڸ� �ݰų� ��Ҹ� Ŭ�� ��			
				return null;
			}
			
			// �������� condition �ν��Ͻ� ����			
			return condition;
			
		}catch(Exception exception) {
			// �˻� ���� �߰� ��ư�� ������ ���ο��� �Ͼ�� ��� ���ܸ� ó��
			// ��� ���� �߻� �� null ����
			return null;
		}
	}
	
	public static String getSQLContent(Condition condition) {
		StringBuilder sqlContent = new StringBuilder();
		
		String tableName = condition.getColumn().getTableName();
		
		// ���ǹ��� ���� ���̺� �̸��� ��Ī���� �ʱ�ȭ
		switch(tableName) {
			case "SERVERINFO" : 
				tableName = "si";
				break;
			case "SERVERINFO_FACILITY" :
				tableName = "fac";
				break;
			case "SERVERINFO_RTU" :
				tableName = "rtu";
		}
		
		// �ι�° ���Ǻ��ʹ� �������ڸ� ����Ѵ�
		if ((!condition.isFirst()) && (condition.getOperator() != 999)) {
			String operator = condition.getOperator() == 0 ? String.format("%s AND ", System.lineSeparator()) : String.format("%s OR ", System.lineSeparator());
			sqlContent.append(operator);
		}
		
		switch(condition.getConditionContent()) {
			case CONDITION_EQUAL: 
				sqlContent.append(String.format("%s.%s = '%s'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;
			case CONDITION_NOT_EQUAL:
				sqlContent.append(String.format("%s.%s != '%s'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;
			case CONDITION_MORE:
				sqlContent.append(String.format("%s.%s >= '%s'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;
			case CONDITION_LESS:
				sqlContent.append(String.format("%s.%s <= '%s'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;
			case CONDITION_EXCEED:
				sqlContent.append(String.format("%s.%s > '%s'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;
			case CONDITION_LESS_THAN:
				sqlContent.append(String.format("%s.%s < '%s'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;
			case CONDITION_START_WITH:
				sqlContent.append(String.format("%s.%s LIKE '%s%%'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;
			case CONDITION_END_WITH:
				sqlContent.append(String.format("%s.%s LIKE '%%%s'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;
			case CONDITION_INCLUDE:
				sqlContent.append(String.format("%s.%s LIKE '%%%s%%'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;
			case CONDITION_NOT_INCLUDE:
				sqlContent.append(String.format("%s.%s NOT LIKE '%%%s%%'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;		
			case CONDITION_START_WITH_CASE:
				sqlContent.append(String.format("%s.%s COLLATE Korean_Wansung_CS_AS LIKE '%s%%'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;
			case CONDITION_END_WITH_CASE:
				sqlContent.append(String.format("%s.%s COLLATE Korean_Wansung_CS_AS LIKE '%%%s'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;
			case CONDITION_INCLUDE_CASE:
				sqlContent.append(String.format("%s.%s COLLATE Korean_Wansung_CS_AS LIKE '%%%s%%'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;
			case CONDITION_NOT_INCLUDE_CASE:
				sqlContent.append(String.format("%s.%s COLLATE Korean_Wansung_CS_AS NOT LIKE '%%%s%%'", tableName, condition.getColumn().getColumnName(), condition.getParam()));
				break;		
	}
		
		return sqlContent.toString();
	}
	
	// ����ڿ��� ǥ�� ���� �߰� �� ������ ����
	public static String getConditionContent(Condition condition) {
		StringBuilder content = new StringBuilder();
		String temp = Condition.isValue(condition.getConditionContent())?"��":"����";
		
		if((!condition.isFirst())&&(condition.getOperator()==0)) {
			// ù��° ������ �ƴϸ鼭 AND �������� ����ϴ� ���� �� ���
			content.append(String.format("���� ������ ��� �����ϸ鼭%s", System.lineSeparator()));
			content.append(String.format("             %s ���̺���%s", condition.getColumn().getTableName(), System.lineSeparator()));	
		}else if((!condition.isFirst())&&(condition.getOperator()==1)){
			// ù��° ������ �ƴϸ鼭 OR �������� ����ϴ� ���� �� ���
			content.append(String.format("���� ��� ���� ����� �������%s", System.lineSeparator()));
			content.append(String.format("             %s ���̺���%s", condition.getColumn().getTableName(), System.lineSeparator()));
		}else {
			content.append(String.format("%s ���̺���%s", condition.getColumn().getTableName(), System.lineSeparator()));
		}
		content.append(String.format("             %s �÷� %s��",condition.getColumn().getColumnName(), temp));
		content.append(String.format("%s             ", System.lineSeparator()));
		
		switch(condition.getConditionContent()) {
	
			case CONDITION_EQUAL: 
				// =
				content.append(String.format("'%s'��(��) ��ġ�ϴ� �׸� ��ȸ",condition.getParam()));
				break;
			case CONDITION_NOT_EQUAL:
				// !=
				content.append(String.format("'%s'��(��) ��ġ���� �ʴ� �׸� ��ȸ",condition.getParam()));
				break;
			case CONDITION_MORE:
				// >=
				content.append(String.format("'%s'���� �̻��� �׸� ��ȸ",condition.getParam()));
				break;
			case CONDITION_LESS:
				// <=
				content.append(String.format("'%s'���� ������ �׸� ��ȸ",condition.getParam()));
				break;
			case CONDITION_EXCEED:
				// >
				content.append(String.format("'%s'���� �ʰ��� �׸� ��ȸ",condition.getParam()));
				break;
			case CONDITION_LESS_THAN:
				// <
				content.append(String.format("'%s'���� �̸��� �׸� ��ȸ",condition.getParam()));
				break;
				
				
			case CONDITION_START_WITH:
				// LIKE '?%'
				content.append(String.format("%s%s             ","��ҹ��� ���о���", System.lineSeparator()));
				content.append(String.format("'%s'���� �����ϴ� �׸� ��ȸ",condition.getParam()));
				break;
			case CONDITION_END_WITH:
				// LIKE '%?'
				content.append(String.format("%s%s             ","��ҹ��� ���о���", System.lineSeparator()));
				content.append(String.format("'%s'���� ������ �׸� ��ȸ",condition.getParam()));
				break;
			case CONDITION_INCLUDE:
				// LIKE '%?%'
				content.append(String.format("%s%s             ","��ҹ��� ���о���", System.lineSeparator()));
				content.append(String.format("'%s'��(��) �����ϴ� �׸� ��ȸ",condition.getParam()));
				break;
			case CONDITION_NOT_INCLUDE:
				// NOT LIKE '%?%'
				content.append(String.format("%s%s             ","��ҹ��� ���о���", System.lineSeparator()));
				content.append(String.format("'%s'�� �������� �ʴ� �׸� ��ȸ",condition.getParam()));
				break;
				
				
			case CONDITION_START_WITH_CASE:
				// COLLATE Korean_Wansung_CS_AS LIKE '?%'
				content.append(String.format("%s%s             ","��ҹ��ڸ� �����Ͽ�", System.lineSeparator()));
				content.append(String.format("'%s'���� �����ϴ� �׸� ��ȸ",condition.getParam()));				
				break;
			case CONDITION_END_WITH_CASE:
				// COLLATE Korean_Wansung_CS_AS LIKE '%?'
				content.append(String.format("%s%s             ","��ҹ��ڸ� �����Ͽ�", System.lineSeparator()));
				content.append(String.format("'%s'���� ������ �׸� ��ȸ",condition.getParam()));
				break;
			case CONDITION_INCLUDE_CASE:
				// COLLATE Korean_Wansung_CS_AS LIKE '%?%'
				content.append(String.format("%s%s             ","��ҹ��ڸ� �����Ͽ�", System.lineSeparator()));
				content.append(String.format("'%s'��(��) �����ϴ� �׸� ��ȸ",condition.getParam()));
				break;
			case CONDITION_NOT_INCLUDE_CASE:
				// COLLATE Korean_Wansung_CS_AS NOT LIKE '%?%'
				content.append(String.format("%s%s             ","��ҹ��ڸ� �����Ͽ�", System.lineSeparator()));
				content.append(String.format("'%s'�� �������� �ʴ� �׸� ��ȸ",condition.getParam()));
				break;					
		}		
			return content.toString();
	}
	
	
	public static String operatorDiscription = "\r\n" + 
			"1. AND ���� (&)\r\n" + 
			"   AND �����̶� �ѱ۷� �����ϸ� '�׸���' ��� ������\r\n" + 
			"   ���� ���ǵ� �����ϰ� �ڿ� ���� ���ǵ� �����ؾ߸� ��� ������ ��(1)�� �ִ� �����Դϴ�\r\n" + 
			"\r\n" + 
			"   ��, ���� A, B �� ���� ���\r\n" + 
			"\r\n" + 
			"   ���� A �׸��� ���� B�� ��� ��(1) �̾�߸�\r\n" + 
			"   ��� ������ ��(1)�� �شٴ� �ǹ��Դϴ�\r\n" + 
			"\r\n" + 
			"   ���� A�� B �� �� �ϳ��� ������ ����(0)�̶��\r\n" + 
			"   A & B �� ����� ����(0) �Դϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"   MK119 �����ͺ��̽����� AND ������ ���� ���ڸ� �Ʒ��� �����ϴ�\r\n" + 
			"\r\n" + 
			"   ���� A : �ü��� ������ �׿��׽����� ��� ��� �׸��� ��ȸ\r\n" + 
			"   ���� B : ���� \"AR\" �̶�� �ܾ ���Ե� ��� ��� �׸��� ��ȸ\r\n" + 
			"\r\n" + 
			"   ���� ���� A�� B�� AND(&) ���� �ϰ� �Ǹ�( A & B ) �Ʒ��� ������ �ϼ��˴ϴ�\r\n" + 
			"\r\n" + 
			"   ���� A & B : �ü��� ������ �׿��׽����̸鼭 (�׸���)\r\n" + 
			"                   ���� \"AR\" �̶�� �ܾ ���Ե� ��� ��� �׸��� ��ȸ\r\n" + 
			"\r\n" + 
			"   ���� ��� �ü��� ������ �׿��׽����̰� ���ÿ� ���� \"AR\" �� ���ԵǾ�߸� ������ �����մϴ�\r\n" + 
			"\r\n" + 
			" ������������������������������������������������������������������������������������������������������\r\n" + 
			"\r\n" + 
			"2. OR ���� ( | )\r\n" + 
			"   OR �����̶� �ѱ۷� �����ϸ� '�Ǵ�' �̶�� ������\r\n" + 
			"   ���� �����̳� �ڿ� ���� ���� �� �� �ϳ��� �����Ͽ��� ��� ������ ��(1)�� �ִ� �����Դϴ�\r\n" + 
			"\r\n" + 
			"   ��, ���� A, B �� ���� ���\r\n" + 
			"\r\n" + 
			"   ���� A �׸��� ���� B �� �� �ϳ��� ��(1) �̸� \r\n" + 
			"   ��� ������ ��(1)�� �شٴ� �ǹ��Դϴ�\r\n" + 
			"\r\n" + 
			"   �̴� ���� A �� ��(1) �̶��\r\n" + 
			"   ���� B�� ����(0) �̾\r\n" + 
			"   ������� ��� ������ ��(1)�� �شٴ� �ǹ��̸�\r\n" + 
			"\r\n" + 
			"   ���� A�� ����(0) �̾\r\n" + 
			"   ���� B�� ��(1) �̶��\r\n" + 
			"   ��� ������ ��(1)�� �شٴ� �ǹ��Դϴ�\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"   MK119 �����ͺ��̽����� OR ������ ���� ���ڸ� �Ʒ��� �����ϴ�\r\n" + 
			"\r\n" + 
			"   ���� A : �ü��� ������ �׿��׽����� ��� ��� �׸��� ��ȸ\r\n" + 
			"   ���� B : ���� \"AR\" �̶�� �ܾ ���Ե� ��� ��� �׸��� ��ȸ\r\n" + 
			"\r\n" + 
			"   ���� ���� A�� B�� OR( | ) ���� �ϰ� �Ǹ�( A | B ) �Ʒ��� ������ �ϼ��˴ϴ�\r\n" + 
			"\r\n" + 
			"   ���� A | B : �ü��� ������ �׿��׽����̰ų� (�Ǵ�)\r\n" + 
			"                    ���� \"AR\" �̶�� �ܾ ���Ե� ��� ��� �׸��� ��ȸ\r\n" + 
			"\r\n" + 
			"   ���� ��� �ü��� ������ UPS �̴��� ���� \"AR\" �� ���ԵǸ� ������ �����մϴ�\r\n" + 
			"" + 
			"";
	
	public static String superSeparator() {
		return String.format("<html>%s%s%s%s%s%s%s</html>", Util.longSeparator, Util.longSeparator, Util.longSeparator, Util.longSeparator, Util.longSeparator, Util.longSeparator, Util.longSeparator);
	}
	
	// �÷� �˻� �޼ҵ�
	public static void searchColumnDiscription(Column currentColumn) {
		Column findColumn = null;
		
		try {
			// �÷� ����Ʈ �Ǵ� ���� ����Ʈ�� null �� ��� �ƹ��͵� �������� ����
			if (searchColumnList == null || columnList_ComboBox.getItemCount() < 0) {
				columnList_ComboBox.setSelectedIndex(0);
				return;	
			}
						
			String input = columnSearch_textField.getText();
			
			Column[] columns = new Column[searchColumnList.size()];
			
			for(int i = 0; i < searchColumnList.size(); i++) {
				columns[i] = (Column)searchColumnList.get(i);
			}
			
			if(currentColumn != null) {
				findColumn = Column.searchNextColumn(columns, input, lastFindColumn);
			}else {
				findColumn = Column.searchColumn(columns, input);	
			}
			
			
			if(findColumn == null) {
				return;
			}else {				
				int itemCount = columnList_ComboBox.getItemCount();
				
				for(int i = 0; i < itemCount; i++) {
					Column compareColumn = (Column)columnList_ComboBox.getItemAt(i);
					
					if(findColumn.getTable_ColumnName().equalsIgnoreCase(compareColumn.getTable_ColumnName())) {
						columnList_ComboBox.setSelectedIndex(i);
						lastFindColumn = (Column)columnList_ComboBox.getSelectedItem();
						return;
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
