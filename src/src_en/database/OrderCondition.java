package src_en.database;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import src_en.swing.DatabaseAccess_Panel;
import src_en.util.Util;

public class OrderCondition {
	private static Font titlefont = FontManager.getFont(Font.BOLD, 19);
	private static Font boldfont = FontManager.getFont(Font.BOLD, 17);	
	private static Font plainfont = FontManager.getFont(Font.PLAIN, 15);
	private static JComboBox columnList_ComboBox;
	private static JComboBox condition_ComboBox;
	private static String selectedColumn = null;
	
	private static ArrayList searchColumnList; // 컬럼 항목 리스트
	private static JTextField columnSearch_textField;
	private static Column lastFindColumn = null;		
		
	private Column column;
	private String order; // ASC (오름차순), DESC(내림차순)
	private String orderContent; // 정렬 쿼리 내용
	
	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	// 주요 메소드
	public static OrderCondition createOrderCondition() {
		
		OrderCondition orderCondition = null;
		
		searchColumnList = DatabaseAccess_Panel.getColumnList();
				
		// 조회 항목(컬럼) 리스트가 null 경우 아무것도 수행하지 않음
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
			JLabel title = new JLabel("<html>Add Result table Sorting criteria&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br></html>");
			title.setFont(titlefont);
									
			// 1. 정렬 기준 컬럼 선택 문자열
			String selectColumn = "<html><br><font color='blue'>1. Sort criteria Column</font></html>";																		
			JLabel insert = new JLabel(selectColumn);
			insert.setFont(boldfont);
			
			// 1. 정렬 기준 컬럼 선택 콤보박스
			columnList_ComboBox = new JComboBox();
			columnList_ComboBox.setFont(boldfont);			
			columnList_ComboBox.setBackground(Color.WHITE);
			columnList_ComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 컬럼 콤보박스로 아이템 변경 시 사용자가 선택한 컬럼에대한 정보를 간략하게 보여준다
					JComboBox temp = (JComboBox)e.getSource();								
					Column item = (Column)temp.getSelectedItem();										
					
					if(columnList_ComboBox != null)		
						selectedColumn = ((Column)columnList_ComboBox.getSelectedItem()).getColumnName();
					
					String[] conditions = {
							String.format("Sort them in Ascending order based on Column [%s] content", selectedColumn),
							String.format("Sort them in Descending order based on Column [%s] content", selectedColumn)
					};
					condition_ComboBox.setModel(new DefaultComboBoxModel(conditions));
					
//					String msg = String.format("<font color='blue'>컬럼 내용 확인</font>\n테이블 명 : %s\n\n컬럼 명 : %s%s\n", item.getTableName(), item.getColumnName(), Util.separator);
//					Util.showMessage(msg, JOptionPane.PLAIN_MESSAGE);											
				}
			});
			
			
			// 컬럼 리스트 콤보박스 아이템 초기화
			if(searchColumnList != null) {
				Object[] colunms = searchColumnList.toArray();
				columnList_ComboBox.setModel(new DefaultComboBoxModel(colunms));
			}
			
			//	★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			// 2. 정렬 조건 선택 문자열
			String selectCondition = "<html><br><font color='blue'>2. Sort content</font></html>";
			JLabel insert2 = new JLabel(selectCondition);
			insert2.setFont(boldfont);
			
			// 2. 정렬 조건 선택 콤보박스
			condition_ComboBox = new JComboBox();
			
			if(columnList_ComboBox != null)		
				selectedColumn = ((Column)columnList_ComboBox.getSelectedItem()).getColumnName();
			
			String[] conditions = {
					String.format("Sort them in Ascending order based on Column [%s] content", selectedColumn),
					String.format("Sort them in Descending order based on Column [%s] content", selectedColumn)
			};
			condition_ComboBox.setFont(boldfont);			
			condition_ComboBox.setBackground(Color.WHITE);
			condition_ComboBox.setModel(new DefaultComboBoxModel(conditions));
			
			//	★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★			

			JLabel separator = new JLabel("<html>" + superSeparator() + superSeparator() + superSeparator() + superSeparator() + "</html>");
			
			// 조건 추가 대화상자 내용
			Object[] message = {
					title,
					insert, columnSearch_textField, columnList_ComboBox, // 1. 정렬 기준 컬럼 선택										
					insert2, condition_ComboBox, // 2. 정렬 조건 선택				
					separator
			};

			int option = JOptionPane.showConfirmDialog(null, message, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);			

			//	★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
			
			
			if (option == JOptionPane.OK_OPTION) {
				
				// 리턴해줄 codition 인스턴스 초기화
				orderCondition = new OrderCondition();
				orderCondition.setColumn((Column) columnList_ComboBox.getSelectedItem());								
				orderCondition.setOrder((condition_ComboBox.getSelectedIndex() == 0) ? "ASC" : "DESC");				
			} else {
				// Util.showMessage("<font color='red'>SQL Exception</font>\n검색 조건 추가 작업을 취소하였습니다&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
				// 사용자가 대화상자를 닫거나 취소를 클릭 함			
				return null;
			}
			
			// 정상적인 condition 인스턴스 리턴			
			return orderCondition;
			
		}catch(Exception exception) {
			// 검색 조건 추가 버튼의 리스너 내부에서 일어나는 모든 예외를 처리
			// 모든 예외 발생 시 null 리턴
			return null;
		}
	}
	
	public static String getOrderByContent(OrderCondition condition) {
		String tableName = condition.getColumn().getTableName();
		String columnName = condition.getColumn().getColumnName();
		String order = condition.getOrder();		
		
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
		
		
		return String.format("ORDER BY %s.%s %s ", tableName, columnName, order);				
	}
	
	public static String superSeparator() {
		return String.format("%s%s%s", Util.longSeparator, Util.longSeparator, Util.longSeparator);
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
