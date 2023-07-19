package src_en.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import common.util.FontManager;
import src_en.util.JavaScript;

class ScanCellRenderer extends DefaultTableCellRenderer {
	
	private String expression = null;
	
	public ScanCellRenderer() { };
	
	public ScanCellRenderer(String search){
		this.expression = search;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
		
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
		String scanReulst = null;
		
		try {
			scanReulst = table.getValueAt(row, 3).toString();
		}catch(Exception e) {
			// 테이블 셀에 내용이 없을 경우 (제어 성공 결과 등)						
			return c;
		}
		
			try {												
				if (scanReulst.contains("Exception") ||
					scanReulst.contains("Data Mismatch") ||
					scanReulst.contains("Response Timeout") ||
					scanReulst.contains("Unprocessable") ||					
					scanReulst.contains("CRC Error") ||
					scanReulst.contains("Unknown")) {
					// Not Good
					if (isSelected) {
						c.setFont(FontManager.getFont(Font.BOLD, 15));
						c.setBackground(table.getSelectionBackground());
						c.setForeground(table.getSelectionForeground());
						return c;
					}else{
						c.setFont(FontManager.getFont(Font.BOLD, 15));
						c.setForeground(Color.BLACK);
						c.setBackground(Color.RED);
						return c;
					}
				}else {
					// Good
					if (isSelected) {				
						c.setBackground(table.getSelectionBackground());
						c.setForeground(table.getSelectionForeground());
					}else{
						c.setFont(FontManager.getFont(Font.PLAIN, 15));
						c.setForeground(new Color(0, 0, 0));
						c.setBackground(Color.WHITE);
					}
				}
			
			try {
				// Search Value Scan
				if(expression != null) {
					boolean result = false;					
					
					result = (boolean)JavaScript.eval(expression.trim() , scanReulst);
					
					if(result) {
						if (isSelected) {
							c.setFont(FontManager.getFont(Font.BOLD, 15));
							c.setBackground(table.getSelectionBackground());
							c.setForeground(table.getSelectionForeground());
						}else{				
							c.setFont(FontManager.getFont(Font.BOLD, 15));
							c.setForeground(Color.BLACK);
							c.setBackground(Color.GREEN);
						}
					}
				}
			}catch(Exception e) {
				// Search Value Scan Expression Error
				e.printStackTrace();
			}
	
			return c;			
			
		}catch(Exception e) {
			e.printStackTrace();
			return c;
		}
			
	}
		
}