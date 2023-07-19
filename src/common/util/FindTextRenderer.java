package common.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class FindTextRenderer extends DefaultTableCellRenderer {

	private int colNum = 0;
	private String search = null;
	private Color color = null;
	
	
	public FindTextRenderer(int colNum, String search, Color color) {
		this.colNum = colNum;
		this.search = search;
		this.color = color;	
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if(table.getValueAt(row, this.colNum) == null) {
			return c;		
		}
		
		String cellValue = table.getValueAt(row, this.colNum).toString();

		try {

			if (search.length() < 1 || search.equalsIgnoreCase("")) {
				// 검색어가 존재하지 않을 경우
				if (isSelected) {				
					c.setBackground(table.getSelectionBackground());
					c.setForeground(table.getSelectionForeground());
					return c;
				}else{
					c.setFont(FontManager.getFont(Font.PLAIN, 15));
					c.setForeground(new Color(0, 0, 0));
					c.setBackground(Color.WHITE);
					return c;
				}
				
			}else {
				boolean result = cellValue.equalsIgnoreCase(search);

				if (result) {
					
					// ★ 검색 문자열을 포함할 경우
					if (isSelected) {
						c.setFont(FontManager.getFont(Font.BOLD, 16));
						c.setBackground(table.getSelectionBackground());
						c.setForeground(table.getSelectionForeground());
						return c;
					} else {
						c.setFont(FontManager.getFont(Font.BOLD, 16));
						c.setBackground(this.color);
						c.setForeground(Color.BLACK);
						return c;
					}
					
				}else {
					
					// 검색 문자열을 포함하지 않을 경우
					if (isSelected) {				
						c.setBackground(table.getSelectionBackground());
						c.setForeground(table.getSelectionForeground());
						return c;
					}else{
						c.setFont(FontManager.getFont(Font.PLAIN, 15));
						c.setForeground(new Color(0, 0, 0));
						c.setBackground(Color.WHITE);
						return c;
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return c;
		}
		
	}

}
