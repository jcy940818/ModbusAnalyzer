package common.modbus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import common.util.FontManager;
import common.util.JavaScript;
import moon.Moon;

public class ModbusCellRenderer extends DefaultTableCellRenderer {
	
	private ArrayList<ModbusWatchPoint> pointList = null;
	private String formula = null;
	private String item = null;
	
	public ModbusCellRenderer() { };
	
	public ModbusCellRenderer(String formula, String item){
		this.formula = formula;
		this.item = item;
	}
	
	public ModbusCellRenderer(String formula, String item, ArrayList<ModbusWatchPoint> pointList){
		this.formula = formula;
		this.item = item;
		this.pointList = pointList;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
		
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		String scanReulst = null;
		ModbusWatchPoint point = null;
		
		JComboBox addrTypeComboBox = (Moon.isKorean()) ? src_ko.swing.ModbusMonitor_Panel.addrTypeComboBox : src_en.swing.ModbusMonitor_Panel.addrTypeComboBox;
		JComboBox resultType = (Moon.isKorean()) ? src_ko.swing.ModbusMonitor_Panel.resultType : src_en.swing.ModbusMonitor_Panel.resultType;
		
		if(pointList != null) {
			int index = Integer.parseInt(table.getValueAt(row, 0).toString());
			point = pointList.get(index-1);
		}else {
			point = (ModbusWatchPoint)table.getValueAt(row, 1);
		}
		
		switch(item) {
			case "fc" :
				try {
					scanReulst = String.valueOf(point.getFunctionCode());
				}catch(Exception e) {
					return c;
				}
				break;
		
				
			case "addr" :
				try {
					scanReulst = String.valueOf(point.getRegisterAddr());
					
					switch(addrTypeComboBox.getSelectedItem().toString()) {
						case "Modbus (DEC)" :
							scanReulst = point.getModbusAddrString();
							break;
						case "Register (DEC)" :
							scanReulst = String.valueOf(point.getRegisterAddr());
							break;
						case "Register (HEX)" :							
							scanReulst = point.getRegisterAddrHexString();							
							break;
					}
					
				}catch(Exception e) {
					return c;
				}
				break;
		
		
			case "value" :
				try {
					String pureData = point.getData().getPureValue().toString();
					
					if(pureData.equalsIgnoreCase("none")) {
						if (isSelected) {				
							c.setFont(FontManager.getFont(Font.PLAIN, 15));
							c.setBackground(table.getSelectionBackground());
							c.setForeground(table.getSelectionForeground());
						}else{
							c.setFont(FontManager.getFont(Font.PLAIN, 15));
							c.setForeground(new Color(0, 0, 0));
							c.setBackground(Color.WHITE);
						}
						return c;
						
					}else if(pointList == null && resultType != null && resultType.getSelectedIndex() == 0) {
						scanReulst = String.valueOf(point.getComputedValue(Double.parseDouble(pureData)));
						
					}else {
						scanReulst = pureData;
					}
				}catch(Exception ex) {						
					return c;
				}
			break;
		}
		
		// ±âº» ÄÄÆ÷³ÍÆ® ¼¿ ·»´õ·¯ ¼³Á¤
		if (isSelected) {				
			c.setFont(FontManager.getFont(Font.PLAIN, 15));
			c.setBackground(table.getSelectionBackground());
			c.setForeground(table.getSelectionForeground());
		}else{
			c.setFont(FontManager.getFont(Font.PLAIN, 15));
			c.setForeground(new Color(0, 0, 0));
			c.setBackground(Color.WHITE);
		}
		
		try {	
			if(formula != null) {
				boolean result = false;
				
				result = Boolean.parseBoolean(JavaScript.eval(formula.trim() , scanReulst).toString());
				
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
			
			return c;
			
		}catch(Exception e) {
			e.printStackTrace();
			if (isSelected) {				
				c.setFont(FontManager.getFont(Font.PLAIN, 15));
				c.setBackground(table.getSelectionBackground());
				c.setForeground(table.getSelectionForeground());
			}else{
				c.setFont(FontManager.getFont(Font.PLAIN, 15));
				c.setForeground(new Color(0, 0, 0));
				c.setBackground(Color.WHITE);
			}
			return c;
		}
	}
		
}