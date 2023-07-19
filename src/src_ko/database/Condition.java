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
	
	// 컬럼 검색 기능을 위한 필드
	private static ArrayList searchColumnList; // 컬럼 항목 리스트
	private static JTextField columnSearch_textField;
	private static Column lastFindColumn = null;	
	private static JComboBox columnList_ComboBox;
	
	public static final int OPERATOR_AND = 0; // AND 논리 연산자 
	public static final int OPERATOR_OR = 0; // OR 논리 연산자
	
	public static final int CONDITION_EQUAL = 0; // 같다 '='
	public static final int CONDITION_NOT_EQUAL = 1; // 같지 않다 '!='	
	public static final int CONDITION_MORE = 2; // 이상 '>='
	public static final int CONDITION_LESS = 3; // 이하 '<='
	public static final int CONDITION_EXCEED = 4; // 초과 '>'
	public static final int CONDITION_LESS_THAN = 5; // 미만 '<'
	public static final int CONDITION_START_WITH = 6; // 특정 문자로 시작 LIKE '?%'
	public static final int CONDITION_END_WITH = 7; // 특정 문자로 끝 LIKE '%?'
	public static final int CONDITION_INCLUDE = 8; // 특정 문자를 포함 LIKE '%?%'
	public static final int CONDITION_NOT_INCLUDE = 9; // 특정 문자를 포함하지 않는 NOT LIKE '%?%'
	public static final int CONDITION_START_WITH_CASE = 10; // 대소문자를 구분하여 특정 문자로 시작 LIKE '?%'
	public static final int CONDITION_END_WITH_CASE = 11; // 대소문자를 구분하여 특정 문자로 끝 LIKE '%?'
	public static final int CONDITION_INCLUDE_CASE = 12; // 대소문자를 구분하여 특정 문자를 포함 LIKE '%?%'
	public static final int CONDITION_NOT_INCLUDE_CASE = 13; // 대소문자를 구분하여 특정 문자를 포함하지 않는 NOT LIKE '%?%'
		
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
	
	private boolean isFirst = false; // 처음 추가 된 조건인지 여부 (두번째 조건부터는 논리 연산자를 사용하여야 하기 때문에)
	private int index; // 1. 인덱스
	private int operator; // 2. 논리 연산자
	private int conditionContent; // 3. 조건
	private Column column; // 4. 컬럼
	private String param; // 5. 파라미터
	
	
	// 첫번째 조건인지의 여부
	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}
	
	// 1. 인덱스
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	
	// 2. 논리 연산자
	public int getOperator() {
		return operator;
	}
	public void setOperator(int operator) {
		this.operator = operator;
	}	
	
		
	// 3. 조건 내용
	public int getConditionContent() {
		return conditionContent;
	}
	public void setConditionContent(int conditionContent) {
		this.conditionContent = conditionContent;
	}
	
	// 4. 컬럼
	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}

	
	// 5. 파라미터
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
			JLabel title = new JLabel("<html>쿼리 검색 조건 추가<br><br></html>");
			title.setFont(titlefont);
			
			//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			// 1. 논리 연산자 선택 문자열
			String selectOperator = "<html><font color='blue'>1. 논리 연산자 선택</font></html>";
			JLabel insert = new JLabel(selectOperator);
			insert.setFont(boldfont);
			
			// 1. 논리 연산자 선택 콤보박스
			JComboBox operator_ComboBox = new JComboBox();
			String[] operators = {
					"AND 연산",
					"OR 연산",
			};
			operator_ComboBox.setModel(new DefaultComboBoxModel(operators));				
			operator_ComboBox.setFont(boldfont);			
			operator_ComboBox.setBackground(Color.WHITE);
			
			
			JButton operator_help = new JButton("논리 연산자 설명");
			operator_help.setForeground(Color.BLACK);
			operator_help.setFont(FontManager.getFont(Font.BOLD, 16));
			operator_help.setBackground(Color.LIGHT_GRAY);				
			operator_help.setSize(77, 32);
			operator_help.setHorizontalAlignment(SwingConstants.LEFT);
			operator_help.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.getRootFrame().dispose(); // 검색 조건 추가 대화상자는 종료된다
					MessageFrame msg_frame = new MessageFrame("논리 연산자", operatorDiscription); // 사용자에게 보여줄 논리 연산자에 대한 설명
					msg_frame.setFocusable(true);
					msg_frame.requestFocus();
				}
			});
			
			
			//	★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			
			// 2. 컬럼 선택 문자열
			String selectColumn = "<html><br><font color='blue'>2. 조건 컬럼 선택</font></html>";																		
			JLabel insert2 = new JLabel(selectColumn);
			insert2.setFont(boldfont);
			
			// 2. 컬럼 선택 콤보박스
			columnList_ComboBox = new JComboBox();
			columnList_ComboBox.setFont(boldfont);			
			columnList_ComboBox.setBackground(Color.WHITE);
//			columnList_ComboBox.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					// 컬럼 콤보박스로 아이템 변경 시 사용자가 선택한 컬럼에대한 정보를 간략하게 보여준다
//					JComboBox temp = (JComboBox)e.getSource();								
//					Column item = (Column)temp.getSelectedItem();										
//					
//					String msg = String.format("<font color='blue'>컬럼 내용 확인</font>\n테이블 명 : %s\n\n컬럼 명 : %s%s\n", item.getTableName(), item.getColumnName(), Util.longSeparator);
//					Util.showMessage(msg, JOptionPane.PLAIN_MESSAGE);											
//				}
//			});
			
			
			// 컬럼 리스트 콤보박스 아이템 초기화
			if(searchColumnList != null) {
				Object[] colunms = searchColumnList.toArray();
				columnList_ComboBox.setModel(new DefaultComboBoxModel(colunms));
			}
			
			
			//	★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			// 3. 조건 선택 문자열
			String selectCondition = "<html><br><font color='blue'>3. 조건 선택</font></html>";
			JLabel insert3 = new JLabel(selectCondition);
			insert3.setFont(boldfont);
			
			// 3. 조건 선택 콤보박스
			JComboBox condition_ComboBox = new JComboBox();
			String[] conditions = {
					"1. 컬럼 내용이 파라미터 내용과 일치하는 항목을 조회                  ( 컬럼 = 파라미터 )",
					"2. 컬럼 내용이 파라미터 내용과 일치하지 않는 항목을 조회           ( 컬럼 != 파라미터 )",
					
					"3. 컬럼 값이 파라미터 값보다 이상인 항목을 조회                        ( 컬럼 >= 파라미터 )",
					"4. 컬럼 값이 파라미터 값보다 이하인 항목을 조회                        ( 컬럼 <= 파라미터 )",
					
					"5. 컬럼 값이 파라미터 값보다 초과인 항목을 조회                        ( 컬럼 > 파라미터 )",
					"6. 컬럼 값이 파라미터 값보다 미만인 항목을 조회                        ( 컬럼 < 파라미터 )",
					
					"7. 컬럼 내용이 파라미터 내용으로 시작하는 항목을 조회               ( 대소문자 구분없이 : 컬럼 내용이 특정 문자로 시작하는 )",
					"8. 컬럼 내용이 파라미터 내용으로 끝나는 항목을 조회                  ( 대소문자 구분없이 : 컬럼 내용이 특정 문자로 끝나는 )",					
					"9. 컬럼 내용에 파라미터 내용을 포함하는 항목을 조회                  ( 대소문자 구분없이 : 컬럼 내용에 특정 문자를 포함하는 )",
					"10. 컬럼 내용에 파라미터 내용을 포함하지 않는 항목을 조회         ( 대소문자 구분없이 : 컬럼 내용에 특정 문자를 포함하지 않는 )",					
					
					"11. 컬럼 내용이 파라미터 내용으로 시작하는 항목을 조회             ( 대소문자를 구분하여 : 컬럼 내용이 특정 문자로 시작하는 )",
					"12. 컬럼 내용이 파라미터 내용으로 끝나는 항목을 조회                ( 대소문자를 구분하여 : 컬럼 내용이 특정 문자로 끝나는 )",					
					"13. 컬럼 내용에 파라미터 내용을 포함하는 항목을 조회                ( 대소문자를 구분하여 : 컬럼 내용에 특정 문자를 포함하는 )",
					"14. 컬럼 내용에 파라미터 내용을 포함하지 않는 항목을 조회         ( 대소문자를 구분하여 : 컬럼 내용에 특정 문자를 포함하지 않는 )"	
			};
			condition_ComboBox.setFont(boldfont);			
			condition_ComboBox.setBackground(Color.WHITE);
			condition_ComboBox.setModel(new DefaultComboBoxModel(conditions));

						
			// 4. 조건 파라미터 문자열
			String conditionParam = "<html><br><font color='blue'>4. 조건 파라미터 입력</font></html>";
			JLabel insert4 = new JLabel(conditionParam);
			insert4.setFont(boldfont);
			
			// 4. 조건 파라미터 입력 폼
			JTextField inputParam = new JTextField();			
			inputParam.setBackground(Color.WHITE);
			inputParam.setSize(77, 32);
			inputParam.setFont(boldfont);						
			inputParam.setBorder(new LineBorder(Color.DARK_GRAY, 2));
			
			JLabel separator = new JLabel(superSeparator());
			
			
			//	★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			// 조건 추가 대화상자 내용
			Object[] message = {
					title,
					insert, operator_ComboBox, operator_help, // 1. 논리 연산자 선택 ( AND, OR )
					insert2, columnSearch_textField, columnList_ComboBox, // 2. 조건 컬럼 선택										
					insert3, condition_ComboBox, // 3. 조건 선택
					insert4, inputParam, // 4. 조건 파라미터
					separator,										
			};			

			int option = JOptionPane.showConfirmDialog(null, message, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);			

			//	★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			
			if (option == JOptionPane.OK_OPTION) {								
				
				if(inputParam.getText().length() < 1) {
					Util.showMessage("<font color='red'>검색 조건 추가 실패</font>\n조건 파라미터 내용이 입력되지 않아 검색 조건 추가에 실패하였습니다&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				// 리턴해줄 codition 인스턴스 초기화
				condition = new Condition();								
				condition.setFirst(false);
				condition.setIndex(Condition.ConditionCount++); // 1. 인덱스
				condition.setOperator(operator_ComboBox.getSelectedIndex()); // 2. 논리연산자
				condition.setConditionContent(condition_ComboBox.getSelectedIndex()); // 3. 조건
				condition.setColumn((Column)columnList_ComboBox.getSelectedItem()); // 4. 컬럼
				condition.setParam(inputParam.getText()); // 5. 파라미터				
			} else {
				// Util.showMessage("<font color='red'>SQL Exception</font>\n검색 조건 추가 작업을 취소하였습니다&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
				// 사용자가 대화상자를 닫거나 취소를 클릭 함			
				return null;
			}
			
			// 정상적인 condition 인스턴스 리턴			
			return condition;
			
		}catch(Exception exception) {
			// 검색 조건 추가 버튼의 리스너 내부에서 일어나는 모든 예외를 처리
			// 모든 예외 발생 시 null 리턴
			return null;
		}	
	}
	
	
	// 첫번째 조건 생성 시 사용
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
			JLabel title = new JLabel("<html>쿼리 검색 조건 추가<br></html>");
			title.setFont(titlefont);			
									
			// 1. 컬럼 선택 문자열
			String selectColumn = "<html><br><font color='blue'>1. 조건 컬럼 선택</font></html>";																		
			JLabel insert = new JLabel(selectColumn);
			insert.setFont(boldfont);
			
			// 1. 컬럼 선택 콤보박스
			columnList_ComboBox = new JComboBox();
			columnList_ComboBox.setFont(boldfont);			
			columnList_ComboBox.setBackground(Color.WHITE);
//			columnList_ComboBox.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					// 컬럼 콤보박스로 아이템 변경 시 사용자가 선택한 컬럼에대한 정보를 간략하게 보여준다
//					JComboBox temp = (JComboBox)e.getSource();								
//					Column item = (Column)temp.getSelectedItem();										
//					
//					String msg = String.format("<font color='blue'>컬럼 내용 확인</font>\n테이블 명 : %s\n\n컬럼 명 : %s%s\n", item.getTableName(), item.getColumnName(), Util.longSeparator);
//					Util.showMessage(msg, JOptionPane.PLAIN_MESSAGE);											
//				}
//			});
			
			
			// 컬럼 리스트 콤보박스 아이템 초기화
			if(searchColumnList != null) {
				Object[] colunms = searchColumnList.toArray();
				columnList_ComboBox.setModel(new DefaultComboBoxModel(colunms));
			}
			
			
			
			//	★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			// 2. 조건 선택 문자열
			String selectCondition = "<html><br><font color='blue'>2. 조건 선택</font></html>";
			JLabel insert2 = new JLabel(selectCondition);
			insert2.setFont(boldfont);
			
			// 2. 조건 선택 콤보박스
			JComboBox condition_ComboBox = new JComboBox();
			String[] conditions = {
					"1. 컬럼 내용이 파라미터 내용과 일치하는 항목을 조회                  ( 컬럼 = 파라미터 )",
					"2. 컬럼 내용이 파라미터 내용과 일치하지 않는 항목을 조회           ( 컬럼 != 파라미터 )",
					
					"3. 컬럼 값이 파라미터 값보다 이상인 항목을 조회                        ( 컬럼 >= 파라미터 )",
					"4. 컬럼 값이 파라미터 값보다 이하인 항목을 조회                        ( 컬럼 <= 파라미터 )",
					
					"5. 컬럼 값이 파라미터 값보다 초과인 항목을 조회                        ( 컬럼 > 파라미터 )",
					"6. 컬럼 값이 파라미터 값보다 미만인 항목을 조회                        ( 컬럼 < 파라미터 )",
					
					"7. 컬럼 내용이 파라미터 내용으로 시작하는 항목을 조회               ( 대소문자 구분없이 : 컬럼 내용이 특정 문자로 시작하는 )",
					"8. 컬럼 내용이 파라미터 내용으로 끝나는 항목을 조회                  ( 대소문자 구분없이 : 컬럼 내용이 특정 문자로 끝나는 )",					
					"9. 컬럼 내용에 파라미터 내용을 포함하는 항목을 조회                  ( 대소문자 구분없이 : 컬럼 내용에 특정 문자를 포함하는 )",
					"10. 컬럼 내용에 파라미터 내용을 포함하지 않는 항목을 조회         ( 대소문자 구분없이 : 컬럼 내용에 특정 문자를 포함하지 않는 )",					
					
					"11. 컬럼 내용이 파라미터 내용으로 시작하는 항목을 조회             ( 대소문자를 구분하여 : 컬럼 내용이 특정 문자로 시작하는 )",
					"12. 컬럼 내용이 파라미터 내용으로 끝나는 항목을 조회                ( 대소문자를 구분하여 : 컬럼 내용이 특정 문자로 끝나는 )",					
					"13. 컬럼 내용에 파라미터 내용을 포함하는 항목을 조회                ( 대소문자를 구분하여 : 컬럼 내용에 특정 문자를 포함하는 )",
					"14. 컬럼 내용에 파라미터 내용을 포함하지 않는 항목을 조회         ( 대소문자를 구분하여 : 컬럼 내용에 특정 문자를 포함하지 않는 )"				
			};
			condition_ComboBox.setFont(boldfont);			
			condition_ComboBox.setBackground(Color.WHITE);
			condition_ComboBox.setModel(new DefaultComboBoxModel(conditions));
			
			//	★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
						
			// 3. 조건 파라미터 문자열
			String conditionParam = "<html><br><font color='blue'>3. 조건 파라미터 입력</font></html>";
			JLabel insert3 = new JLabel(conditionParam);
			insert3.setFont(boldfont);
			
			// 3. 조건 파라미터 입력 폼
			JTextField inputParam = new JTextField();			
			inputParam.setBackground(Color.WHITE);
			inputParam.setSize(77, 32);
			inputParam.setFont(boldfont);						
			inputParam.setBorder(new LineBorder(Color.DARK_GRAY, 2));
			
			JLabel separator = new JLabel(superSeparator());
			
			
			//★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			// 조건 추가 대화상자 내용
			Object[] message = {
					title,
					insert, columnSearch_textField, columnList_ComboBox, // 1. 조건 컬럼 선택										
					insert2, condition_ComboBox, // 2. 조건 선택
					insert3, inputParam, // 3. 조건 파라미터
					separator,										
			};			

			int option = JOptionPane.showConfirmDialog(null, message, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);			

			//	★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			
			if (option == JOptionPane.OK_OPTION) {		
				
				if(inputParam.getText().length() < 1) {
					Util.showMessage("<font color='red'>검색 조건 추가 실패</font>\n조건 파라미터 내용이 입력되지 않아 검색 조건 추가에 실패하였습니다&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
					return null;
				}
				
				// 리턴해줄 codition 인스턴스 초기화
				condition = new Condition();			
				condition.setFirst(true);
				condition.setIndex(Condition.ConditionCount++); // 1. 인덱스
				condition.setOperator(999); // 2. 논리연산자 ( 999: 논리 연산자 없음 )
				condition.setConditionContent(condition_ComboBox.getSelectedIndex()); // 3. 조건
				condition.setColumn((Column)columnList_ComboBox.getSelectedItem()); // 4. 컬럼
				condition.setParam(inputParam.getText()); // 5. 파라미터				
			} else {
				// Util.showMessage("<font color='red'>SQL Exception</font>\n검색 조건 추가 작업을 취소하였습니다&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
				// 사용자가 대화상자를 닫거나 취소를 클릭 함			
				return null;
			}
			
			// 정상적인 condition 인스턴스 리턴			
			return condition;
			
		}catch(Exception exception) {
			// 검색 조건 추가 버튼의 리스너 내부에서 일어나는 모든 예외를 처리
			// 모든 예외 발생 시 null 리턴
			return null;
		}
	}
	
	public static String getSQLContent(Condition condition) {
		StringBuilder sqlContent = new StringBuilder();
		
		String tableName = condition.getColumn().getTableName();
		
		// 조건문에 사용될 테이블 이름을 별칭으로 초기화
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
		
		// 두번째 조건부터는 논리연산자를 사용한다
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
	
	// 사용자에게 표시 해줄 추가 된 조건의 내용
	public static String getConditionContent(Condition condition) {
		StringBuilder content = new StringBuilder();
		String temp = Condition.isValue(condition.getConditionContent())?"값":"내용";
		
		if((!condition.isFirst())&&(condition.getOperator()==0)) {
			// 첫번째 조건이 아니면서 AND 논리연산을 사용하는 조건 일 경우
			content.append(String.format("앞의 조건을 모두 만족하면서%s", System.lineSeparator()));
			content.append(String.format("             %s 테이블의%s", condition.getColumn().getTableName(), System.lineSeparator()));	
		}else if((!condition.isFirst())&&(condition.getOperator()==1)){
			// 첫번째 조건이 아니면서 OR 논리연산을 사용하는 조건 일 경우
			content.append(String.format("앞의 모든 조건 결과에 상관없이%s", System.lineSeparator()));
			content.append(String.format("             %s 테이블의%s", condition.getColumn().getTableName(), System.lineSeparator()));
		}else {
			content.append(String.format("%s 테이블의%s", condition.getColumn().getTableName(), System.lineSeparator()));
		}
		content.append(String.format("             %s 컬럼 %s이",condition.getColumn().getColumnName(), temp));
		content.append(String.format("%s             ", System.lineSeparator()));
		
		switch(condition.getConditionContent()) {
	
			case CONDITION_EQUAL: 
				// =
				content.append(String.format("'%s'과(와) 일치하는 항목 조회",condition.getParam()));
				break;
			case CONDITION_NOT_EQUAL:
				// !=
				content.append(String.format("'%s'과(와) 일치하지 않는 항목 조회",condition.getParam()));
				break;
			case CONDITION_MORE:
				// >=
				content.append(String.format("'%s'보다 이상인 항목 조회",condition.getParam()));
				break;
			case CONDITION_LESS:
				// <=
				content.append(String.format("'%s'보다 이하인 항목 조회",condition.getParam()));
				break;
			case CONDITION_EXCEED:
				// >
				content.append(String.format("'%s'보다 초과인 항목 조회",condition.getParam()));
				break;
			case CONDITION_LESS_THAN:
				// <
				content.append(String.format("'%s'보다 미만인 항목 조회",condition.getParam()));
				break;
				
				
			case CONDITION_START_WITH:
				// LIKE '?%'
				content.append(String.format("%s%s             ","대소문자 구분없이", System.lineSeparator()));
				content.append(String.format("'%s'으로 시작하는 항목 조회",condition.getParam()));
				break;
			case CONDITION_END_WITH:
				// LIKE '%?'
				content.append(String.format("%s%s             ","대소문자 구분없이", System.lineSeparator()));
				content.append(String.format("'%s'으로 끝나는 항목 조회",condition.getParam()));
				break;
			case CONDITION_INCLUDE:
				// LIKE '%?%'
				content.append(String.format("%s%s             ","대소문자 구분없이", System.lineSeparator()));
				content.append(String.format("'%s'을(를) 포함하는 항목 조회",condition.getParam()));
				break;
			case CONDITION_NOT_INCLUDE:
				// NOT LIKE '%?%'
				content.append(String.format("%s%s             ","대소문자 구분없이", System.lineSeparator()));
				content.append(String.format("'%s'을 포함하지 않는 항목 조회",condition.getParam()));
				break;
				
				
			case CONDITION_START_WITH_CASE:
				// COLLATE Korean_Wansung_CS_AS LIKE '?%'
				content.append(String.format("%s%s             ","대소문자를 구분하여", System.lineSeparator()));
				content.append(String.format("'%s'으로 시작하는 항목 조회",condition.getParam()));				
				break;
			case CONDITION_END_WITH_CASE:
				// COLLATE Korean_Wansung_CS_AS LIKE '%?'
				content.append(String.format("%s%s             ","대소문자를 구분하여", System.lineSeparator()));
				content.append(String.format("'%s'으로 끝나는 항목 조회",condition.getParam()));
				break;
			case CONDITION_INCLUDE_CASE:
				// COLLATE Korean_Wansung_CS_AS LIKE '%?%'
				content.append(String.format("%s%s             ","대소문자를 구분하여", System.lineSeparator()));
				content.append(String.format("'%s'을(를) 포함하는 항목 조회",condition.getParam()));
				break;
			case CONDITION_NOT_INCLUDE_CASE:
				// COLLATE Korean_Wansung_CS_AS NOT LIKE '%?%'
				content.append(String.format("%s%s             ","대소문자를 구분하여", System.lineSeparator()));
				content.append(String.format("'%s'을 포함하지 않는 항목 조회",condition.getParam()));
				break;					
		}		
			return content.toString();
	}
	
	
	public static String operatorDiscription = "\r\n" + 
			"1. AND 연산 (&)\r\n" + 
			"   AND 연산이란 한글로 직역하면 '그리고' 라는 뜻으로\r\n" + 
			"   앞의 조건도 만족하고 뒤에 오는 조건도 만족해야만 결과 값으로 참(1)을 주는 연산입니다\r\n" + 
			"\r\n" + 
			"   즉, 조건 A, B 가 있을 경우\r\n" + 
			"\r\n" + 
			"   조건 A 그리고 조건 B가 모두 참(1) 이어야만\r\n" + 
			"   결과 값으로 참(1)을 준다는 의미입니다\r\n" + 
			"\r\n" + 
			"   만약 A와 B 둘 중 하나라도 명제가 거짓(0)이라면\r\n" + 
			"   A & B 의 결과는 거짓(0) 입니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"   MK119 데이터베이스에서 AND 연산의 예를 들자면 아래와 같습니다\r\n" + 
			"\r\n" + 
			"   조건 A : 시설물 종류가 항온항습기인 모든 장비 항목을 조회\r\n" + 
			"   조건 B : 장비명에 \"AR\" 이라는 단어가 포함된 모든 장비 항목을 조회\r\n" + 
			"\r\n" + 
			"   위의 조건 A와 B를 AND(&) 연산 하게 되면( A & B ) 아래의 조건이 완성됩니다\r\n" + 
			"\r\n" + 
			"   조건 A & B : 시설물 종류가 항온항습기이면서 (그리고)\r\n" + 
			"                   장비명에 \"AR\" 이라는 단어가 포함된 모든 장비 항목만을 조회\r\n" + 
			"\r\n" + 
			"   위의 경우 시설물 종류가 항온항습기이고 동시에 장비명에 \"AR\" 이 포함되어야만 조건을 만족합니다\r\n" + 
			"\r\n" + 
			" ───────────────────────────────────────────────────\r\n" + 
			"\r\n" + 
			"2. OR 연산 ( | )\r\n" + 
			"   OR 연산이란 한글로 직역하면 '또는' 이라는 뜻으로\r\n" + 
			"   앞의 조건이나 뒤에 오는 조건 둘 중 하나만 만족하여도 결과 값으로 참(1)을 주는 연산입니다\r\n" + 
			"\r\n" + 
			"   즉, 조건 A, B 가 있을 경우\r\n" + 
			"\r\n" + 
			"   조건 A 그리고 조건 B 둘 중 하나라도 참(1) 이면 \r\n" + 
			"   결과 값으로 참(1)을 준다는 의미입니다\r\n" + 
			"\r\n" + 
			"   이는 조건 A 가 참(1) 이라면\r\n" + 
			"   조건 B는 거짓(0) 이어도\r\n" + 
			"   상관없이 결과 값으로 참(1)을 준다는 의미이며\r\n" + 
			"\r\n" + 
			"   조건 A가 거짓(0) 이어도\r\n" + 
			"   조건 B가 참(1) 이라면\r\n" + 
			"   결과 값으로 참(1)을 준다는 의미입니다\r\n" + 
			"\r\n" + 
			"\r\n" + 
			"   MK119 데이터베이스에서 OR 연산의 예를 들자면 아래와 같습니다\r\n" + 
			"\r\n" + 
			"   조건 A : 시설물 종류가 항온항습기인 모든 장비 항목을 조회\r\n" + 
			"   조건 B : 장비명에 \"AR\" 이라는 단어가 포함된 모든 장비 항목을 조회\r\n" + 
			"\r\n" + 
			"   위의 조건 A와 B를 OR( | ) 연산 하게 되면( A | B ) 아래의 조건이 완성됩니다\r\n" + 
			"\r\n" + 
			"   조건 A | B : 시설물 종류가 항온항습기이거나 (또는)\r\n" + 
			"                    장비명에 \"AR\" 이라는 단어가 포함된 모든 장비 항목을 조회\r\n" + 
			"\r\n" + 
			"   위의 경우 시설물 종류가 UPS 이더라도 장비명에 \"AR\" 이 포함되면 조건을 만족합니다\r\n" + 
			"" + 
			"";
	
	public static String superSeparator() {
		return String.format("<html>%s%s%s%s%s%s%s</html>", Util.longSeparator, Util.longSeparator, Util.longSeparator, Util.longSeparator, Util.longSeparator, Util.longSeparator, Util.longSeparator);
	}
	
	// 컬럼 검색 메소드
	public static void searchColumnDiscription(Column currentColumn) {
		Column findColumn = null;
		
		try {
			// 컬럼 리스트 또는 조건 리스트가 null 일 경우 아무것도 수행하지 않음
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
