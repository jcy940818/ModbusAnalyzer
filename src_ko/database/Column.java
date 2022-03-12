package src_ko.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import src_ko.info.ONION_Info;

public class Column {
	
	private int index;
	private String tableName;
	private String columnName;
	private String table_ColumnName;
	
	public Column(String table_Column) {
		String tableName = table_Column.split("//")[0];
		String columnName = table_Column.split("//")[1];		
		setTable_ColumnName(tableName + "_" + columnName);
		setTableName(tableName);
		setColumnName(columnName);
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getTable_ColumnName() {
		return table_ColumnName;
	}

	public void setTable_ColumnName(String table_ColumnName) {
		this.table_ColumnName = table_ColumnName;
	}
	
	public static Column searchColumn(Column[] columns, String search) {
		if(columns != null) {
			for(int i = 0; i < columns.length; i++) {
				if(columns[i].getColumnName().toUpperCase().contains(search.toUpperCase()))					
					return columns[i];						
			}	
		}
		return null;		
	}
	
	// 나를 아주 고생시킨 메소드
	public static Column searchNextColumn(Column[] columns, String search, Column currentColumn) {
		if(columns != null) {
			for(int i = 0; i < columns.length; i++) {
				if(columns[i].getColumnName().toUpperCase().contains(search.toUpperCase()))
					if(columns[i].getIndex() <= currentColumn.getIndex()) {
						continue;
					}else {
						return columns[i];	
					}											
			}	
		}
		return null;		
	}
	
	

	@Override
	public String toString() {
		return String.format("%d. %s", index, columnName);
	}
	
	public static Column[] createColumns(String tableName) throws SQLException{
		// SELECT A.TABLE_NAME + '//' + (A.COLUMN_NAME) FROM INFORMATION_SCHEMA.COLUMNS A WHERE A.TABLE_NAME = 'SERVERINFO'
		Column[] columns;
		
		String query = String.format("SELECT (A.TABLE_NAME + '//' + (A.COLUMN_NAME)) AS column_name FROM INFORMATION_SCHEMA.COLUMNS A WHERE A.TABLE_NAME = '%s'", tableName);
		Statement stmt = null;
		ResultSet rs = null;
		
		if(ONION_Info.hasMk119Connection()) {
			try {
				// 전방향 작업을 위한 Statement 인스턴스 생성
				stmt = ONION_Info.getMk119Connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = stmt.executeQuery(query);
				
				int columnCount = DbUtil.getRowCount(rs);
				columns = new Column[columnCount];
				int i = 0;
				
				while(rs.next()) {
					columns[i++] = new Column(rs.getString("column_name"));
				}
				
				return columns;

			}catch (Exception e) {
				System.out.println("[ Column.createColumns() : " + e.getMessage() + " ]");
				return null;
			}finally {
				DbUtil.close(rs, stmt);
			}
		}else {
			return null;
		}				
	}
	
	public static String[] getColumnNameArray(ArrayList<Column> columnList) {
		String[] columnNames = new String[columnList.size()];		
		for(int i = 0; i < columnList.size(); i++) {
			columnNames[i] = columnList.get(i).toString();
		}		
		return columnNames;
	}
	
	public static void setColumnIndexing(ArrayList<Column> columnList) {
		for(int i = 0; i < columnList.size(); i++) {
			columnList.get(i).setIndex(i+1);
		}
	}
	
}
