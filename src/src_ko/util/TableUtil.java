package src_ko.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import common.util.FontManager;
import src_ko.database.DbUtil;
import src_ko.exception.TableLoadException;
import src_ko.info.ONION_Info;
import src_ko.swing.MessageFrame;
import src_ko.swing.ProgressFrame;
import src_ko.swing.SqlResultFrame;


public class TableUtil extends Thread{
	
	private static final int limitRowCount = 1000000;
	
	private SqlResultFrame sqlResultFrame;
	private JTable table;
	private ResultSet rs; // TableUtil 인스턴스마다 독립적인 ResultSet 필드를 갖는다
	private String sqlServerInfo;
	private String queryDetail;	
	private String agentType;
	
	public TableUtil(SqlResultFrame sqlResultFrame,JTable table, ResultSet rs, String sqlServerInfo , String queryDetail, String agentType) {
		this.sqlResultFrame = sqlResultFrame;
		this.table = table;
		this.rs = rs;
		this.sqlServerInfo = sqlServerInfo;
		this.queryDetail = queryDetail;
		this.agentType = agentType;
	}
	
	public void run() {
		try {					
			
			if(this.agentType.equalsIgnoreCase("databaseAccess")) {
				this.updateTable(table, rs);
			}else if(this.agentType.equalsIgnoreCase("storedProcedure")){
				this.updateProcedureTable(table, rs);
			}else {
				this.updateTable(table, rs);
			}
			
			
			rs.close();
			
			if(rs.isClosed()) {
				rs = null;
			}else {
				
			}
			
		}catch(SQLException e) {											
			Util.showMessage(String.format("<font color='red'>SQL Exception</font>\n%s%s%s\n", e.getMessage(), Util.longSeparator, Util.longSeparator, System.lineSeparator()), JOptionPane.ERROR_MESSAGE);
			DbUtil.endSQL();
		}catch(TableLoadException e) {
			Util.showMessage(String.format("<font color='red'>결과 테이블 로딩 취소</font>\n%s&nbsp;&nbsp;&nbsp;%s", e.getMessage(), System.lineSeparator()), JOptionPane.ERROR_MESSAGE);
			DbUtil.endSQL();
		}catch (Exception e) {
			Util.showMessage(String.format("<font color='red'>Unknown Exception</font>\n%s%s%s\n", e.getMessage(), Util.longSeparator, Util.longSeparator, System.lineSeparator()), JOptionPane.ERROR_MESSAGE);
			DbUtil.endSQL();
		} finally {
			rs = null;			
			// SQL 작업을 종료
			// 해당 코드 때문에 n개 행 조회 쿼리가 수행되고 endSQL() 메소드가 실행되는 문제가 있었다
			// DbUtil.endSQL(); 			
		}
	}
	
		
	public void updateTable(JTable table, ResultSet rs) throws SQLException, TableLoadException {
		
		// 현재 커넥션이 MK119 커넥션 일때만 상위 n개 테이블을 조회할 것인지 사용자에게 물어본다
		if(DbUtil.checkMK119Db(ONION_Info.getMk119Connection())) {
			int rowCount = DbUtil.getRowCount(rs);
			
			// 결과 테이블의 행(Row Count) : 100만개 이상 일 경우 사용자에게 대화상자 표시
			if(rowCount >= limitRowCount) {
				StringBuilder msg = new StringBuilder(); 
				msg.append(String.format("<font color='red'>SQL Warning</font>\n결과 테이블의 행 개수가 너무 많습니다%s%s\n\n", Util.longSeparator,Util.longSeparator));
				msg.append(String.format("결과 테이블 행 개수(Row Count) : <font color='blue'>%d</font>\n\n", rowCount));
				msg.append("( 해당 대화상자는 결과 테이블의 행 개수가 1,000,000개 이상 일 경우 나타납니다 )" + Util.longSeparator + "\n");								
				msg.append("( 결과 테이블의 행 개수가 1,000,000개 이상인 경우 <font color='red'>.xlsx 파일로 저장 할 수 없습니다</font> )\n");
				
				int menu =  Util.showOption(msg.toString(), new String[] {"계속 실행","상위 n개 행 조회","수행 쿼리 내용만 확인" ,"실행 취소"}, JOptionPane.WARNING_MESSAGE);				
				
				switch (menu) {
				case 0: // 계속 실행
					break;
				case 1: // 상위 n개 행 조회
					String newRowCountString = (String)Util.showInput(String.format("<font color='blue'>테이블 상위 n개 행 조회</font>\n조회하실 행의 개수(Row Count)를 입력해주세요%s%s\n", Util.longSeparator, Util.longSeparator), JOptionPane.QUESTION_MESSAGE);
					if(newRowCountString == null) throw new SQLException("조회 할 테이블의 상위 n개 행 개수(Row Count) 입력을 취소하였습니다");
					
					// 사용자에게 입력받을 조회 할 테이블의 상위 n개 행 개수
					int newRowCount;
					
					try {						
						newRowCount = Integer.parseInt(newRowCountString);						
					}catch(NumberFormatException e) {
						rs.close();
						
						if(rs.isClosed()) {
							rs = null;
							System.out.println("[ TableUtil.updateTable() : 상위 n개 테이블 조회 입력에 실패하여 ResultSet 인스턴스 반환 완료 ]");
						}
						
						throw new SQLException("조회하실 테이블 상위 n개 행 개수 입력 폼에는 1 이상의 정수만 입력 하실 수 있습니다");
					}
							
					SqlResultFrame.DisposeFrame(sqlResultFrame); // 기존에 생성 작업중이던 SqlResultFrame 인스턴스 Dispose					
										
					if(DbUtil.getSELECT().contains("TOP")) {
						String[] selectToken = DbUtil.getSELECT().split(" ");
						
						StringBuilder newSelect = new StringBuilder(String.format("SELECT TOP %d ", newRowCount));  
						
						for(int i = 3; i < selectToken.length; i++) {
							
							if(selectToken[i].equalsIgnoreCase("")) continue; // 공백이면 무시
							selectToken[i] = selectToken[i].trim().replaceAll(" ", "");
							
							if(selectToken[i].contains(",")) {
								newSelect.append(selectToken[i]);
							}else {
								newSelect.append(" " + selectToken[i] + " ");
							}
						}
											
						DbUtil.setSELECT(newSelect.toString());
					}else {
						DbUtil.setSELECT(DbUtil.getSELECT().replace("SELECT", String.format("SELECT TOP %d ", newRowCount)));
					}
																							
					System.out.println(String.format("[ TableUtil.updateTable() n개 행 조회 쿼리 : %s ]", DbUtil.getQuery()));
					DbUtil.executeQuery(this.sqlServerInfo ,DbUtil.getQuery());
					return;
				case 2 : // 수행 쿼리 내용만 확인 
					new MessageFrame(String.format("<html><font color='blue'>%s</font> 수행 쿼리 확인</html>", this.sqlServerInfo), queryDetail);
					return;
				case -1 : // 사용자가 메뉴를 선택하지 않고 대화상자를 나갔을 때
				case 3: // 실행 취소
					rs.close();
					if(rs.isClosed())System.out.println("\n[ TableUtil.updateTable() : 사용자의 요청에 따라 결과 테이블 로딩 작업 취소 및 ResultSet 인스턴스 반환에 성공하였습니다 ]\n");
					throw new SQLException("결과 테이블 로딩 작업을 취소하였습니다");		
				}
			}
		}// 커넥션 검사 및 상위 n개 행 테이블 조회 검사
		
		// 테이블 헤더 설정
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowMargin(3);
		table.setRowHeight(25);
				
		// 테이블 초기화
			
		table.setModel(new DefaultTableModel(TableUtil.getOrderedResultSetTable(rs), DbUtil.getOrderedColumnNames(rs)) {
			// 테이블 셀 내용 수정 금지
			public boolean isCellEditable(int i, int c) {
				return false;
			}
		});
		
		// 셀 크기 임의 변경 불가, 셀 위치 이동 불가
		table.getTableHeader().setReorderingAllowed(false); // 열 위치 변경
//		table.getTableHeader().setResizingAllowed(false); // 셀 와이드 크기는 변경 가능하다 
				
		TableUtil.setFirtColumnCenter(table); // 첫번째 컬럼 내용 가운데 정렬		//		
		autoColSize(table, table.getModel());
		resizeColumnWidth(table); // 순서 컬럼만 내용에 맞게 크기를 조절해준다.
		
		// 완성된 결과 테이블을 보여준다 SqlResultFrame.setVisible(true)
		SqlResultFrame.ResultSetloadSuccess(sqlResultFrame);
		
		// 프로그램에게 쿼리 작업이 종료되었음을 알린다
		DbUtil.endSQL();
	}	
	
	
	
	public void updateProcedureTable(JTable table, ResultSet rs) throws SQLException, TableLoadException {
			
			// 현재 커넥션이 MK119 커넥션 일때만 상위 n개 테이블을 조회할 것인지 사용자에게 물어본다
			if(DbUtil.checkMK119Db(ONION_Info.getMk119Connection())) {
				int rowCount = DbUtil.getRowCount(rs);
				
				// 결과 테이블의 행(Row Count) : 100만개 이상 일 경우 사용자에게 대화상자 표시
				if(rowCount >= limitRowCount) {
					StringBuilder msg = new StringBuilder(); 
					msg.append(String.format("<font color='red'>SQL Warning</font>\n결과 테이블의 행 개수가 너무 많습니다%s%s\n\n", Util.longSeparator,Util.longSeparator));
					msg.append(String.format("결과 테이블 행 개수(Row Count) : <font color='blue'>%d</font>\n\n", rowCount));
					msg.append("( 해당 대화상자는 결과 테이블의 행 개수가 1,000,000개 이상 일 경우 나타납니다 )" + Util.longSeparator + "\n");								
					msg.append("( 결과 테이블의 행 개수가 1,000,000개 이상인 경우 <font color='red'>.xlsx 파일로 저장 할 수 없습니다</font> )\n");
					
					int menu =  Util.showOption(msg.toString(), new String[] {"계속 실행", "수행 쿼리 내용만 확인" ,"실행 취소"}, JOptionPane.WARNING_MESSAGE);				
					
					switch (menu) {
					case 0: // 계속 실행
						break;
					case 1 : // 수행 쿼리 내용만 확인 
						new MessageFrame(String.format("<html><font color='blue'>%s</font> 수행 쿼리 확인</html>", this.sqlServerInfo), queryDetail);
						DbUtil.endSQL();
						return;
					case -1 : // 사용자가 메뉴를 선택하지 않고 대화상자를 나갔을 때
					case 2: // 실행 취소
						rs.close();
						throw new SQLException("결과 테이블 로딩 작업을 취소하였습니다");		
					}
				}
			}// 커넥션 검사 및 상위 n개 행 테이블 조회 검사
			
			// 테이블 헤더 설정
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getTableHeader().setBackground(new Color(255, 255, 153));
			table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 15));
			
			// 테이블 셀 설정
			table.setBorder(new EmptyBorder(0, 3, 0, 0));
			table.setFont(FontManager.getFont(Font.PLAIN, 15));
			table.setRowMargin(3);
			table.setRowHeight(25);
					
			// 테이블 초기화
				
			table.setModel(new DefaultTableModel(TableUtil.getOrderedResultSetTable(rs), DbUtil.getOrderedColumnNames(rs)) {
				// 테이블 셀 내용 수정 금지
				public boolean isCellEditable(int i, int c) {
					return false;
				}
			});
			
			// 셀 크기 임의 변경 불가, 셀 위치 이동 불가
			table.getTableHeader().setReorderingAllowed(false); // 열 위치 변경
	//		table.getTableHeader().setResizingAllowed(false); // 셀 와이드 크기는 변경 가능하다 
					
			TableUtil.setFirtColumnCenter(table); // 첫번째 컬럼 내용 가운데 정렬		
			autoColSize(table, table.getModel());
			resizeProcedureColumnWidth(table);
			
			// 완성된 결과 테이블을 보여준다 SqlResultFrame.setVisible(true)
			SqlResultFrame.ResultSetloadSuccess(sqlResultFrame);
			
			// 프로그램에게 쿼리 작업이 종료되었음을 알린다
			DbUtil.endSQL();
		}
	
	
	
	
	
	
	
	
	
	// 테이블 헤더의 내용에 맞게 와이드를 조절해준다.
	private static void autoColSize(JTable table, TableModel model) {
		TableColumn column = null;
		Component comp = null;
		int headerWidth = 0;
		int cellWidth = 0;
		int font = 11;

		for (int i = 0, n = model.getColumnCount(); i < n; i++) {

			column = table.getColumnModel().getColumn(i);
			try {
				headerWidth = column.getHeaderValue().toString().length() * font;
				column.setPreferredWidth(headerWidth);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
		
	
	// 테이블 컬럼의 와이드 크기를 셀의 내용에 맞게 정해준다.
	public static void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
//		    for (int column = 0; column < table.getColumnCount(); column++) { // 모든 행을
		for (int column = 0; column < 1; column++) { // 첫번째 행만
			int width = 50; // Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}
			columnModel.getColumn(column).setPreferredWidth(width + 3);
		}
	}
	
	
	// 테이블 컬럼의 와이드 크기를 셀의 내용에 맞게 정해준다.
	public static void resizeProcedureColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) { // 모든 행을
			int width = 90;
			
			// 첫번째 컬럼(순서)만 50
			width = (column != 0) ? 90 : 50;						
			
			for (int row = 0; row < table.getRowCount(); row++) {								
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}
			
			if(column != 0) width = width + 5; // 첫번째 열만 제외하고 너비에 여유를 준다
			
			columnModel.getColumn(column).setPreferredWidth(width + 3);
		}
	}
	
	
	/**
	 * ResultSet 결과를 2차원 배열 형태로 리턴
	 */
	public static Object[][] getResultSetTable(ResultSet rs) throws SQLException {
		rs.beforeFirst();
		
		// 결과 테이블 정보		
		int resultRowCount = DbUtil.getRowCount(rs);
		int resultColumnCount = DbUtil.getColumnCount(rs);

		Object[][] rowData = new Object[resultRowCount][resultColumnCount];
		
		int index = 0;

		while (rs.next()) {
			rowData[index] = new Object[resultColumnCount];

			for (int i = 0; i < resultColumnCount; i++) {
				rowData[index][i] = String.valueOf(rs.getObject(i + 1));
			}

			index++;
		}
		
		rs.beforeFirst();
		return rowData;
	}
	
	/**
	 * 순서 컬럼의 값을 가진 ResultSet 결과를 2차원 배열 형태로 리턴
	 */
	public static Object[][] getOrderedResultSetTable(ResultSet rs) throws SQLException, TableLoadException {
					
		// SqlResultFrame을 표시해주기 전에 테이블 업로드 진행도를 표시해준다.
		ProgressFrame p = new ProgressFrame();
		
		try {
			rs.beforeFirst();
			
			// 결과 테이블 정보		
			int resultRowCount = DbUtil.getRowCount(rs);
			int resultColumnCount = DbUtil.getOrderedColumnNames(rs).length;
	
			Object[][] rowData = new Object[resultRowCount][resultColumnCount];
			
			int index = 0;
	
			while (rs.next()) {
				if(!p.isRunningProgress()) {
					throw new TableLoadException("결과 테이블 로딩 작업이 취소되었습니다");
				}
				
				rowData[index] = new Object[resultColumnCount];
	
				for (int i = 0; i < resultColumnCount; i++) {
					if(i == 0) {
						rowData[index][i] = String.valueOf(rs.getRow());
					} else {
						rowData[index][i] = String.valueOf(rs.getObject(i));	
					}
	
					// 프로그레스 프레임의 진행도 업데이트 : 100%에 도달하면 자동으로 진행도 표시 프레임은 종료된다.
					p.updateProgress(ProgressFrame.getPercent(rs.getRow(), resultRowCount));				
				}
	
				index++;
			}
			
			rs.beforeFirst();		
			return rowData;
		}catch(SQLException e) {
			// 작업중 SQLException이 발생하면 ProgressFrame을 종료하고 예외를 리턴
			p.dispose();
			throw e;
		}
	}
	
	
	
	/**
	 * 인자로 넘겨준 JTable의 첫번째 컬럼의 내용을 가운데 정렬해준다.
	 * 주로 인덱스 컬럼을 표시해주기 위해서 구현
	 */
	public static void setFirtColumnCenter(JTable table) {
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();

		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();

		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer);
	}
	
}
