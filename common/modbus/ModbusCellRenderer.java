package common.modbus;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import common.util.JavaScript;
import src_ko.swing.ModbusMonitor_Panel;

public class ModbusCellRenderer extends DefaultTableCellRenderer {
	
	private String formula = null;
	private String item = null;
	
	public ModbusCellRenderer() { };
	
	public ModbusCellRenderer(String formula, String item){
		this.formula = formula;
		this.item = item;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
		
		Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		String scanReulst = null;
		ModbusWatchPoint point = (ModbusWatchPoint)table.getValueAt(row, 1);
		
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
					
					switch(ModbusMonitor_Panel.addrTypeComboBox.getSelectedItem().toString()) {
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
							c.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 15));
							c.setBackground(table.getSelectionBackground());
							c.setForeground(table.getSelectionForeground());
						}else{
							c.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 15));
							c.setForeground(new Color(0, 0, 0));
							c.setBackground(Color.WHITE);
						}
						return c;
						
					}else if(ModbusMonitor_Panel.resultType != null && ModbusMonitor_Panel.resultType.getSelectedIndex() == 0) {
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
			c.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 15));
			c.setBackground(table.getSelectionBackground());
			c.setForeground(table.getSelectionForeground());
		}else{
			c.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 15));
			c.setForeground(new Color(0, 0, 0));
			c.setBackground(Color.WHITE);
		}
		
		try {	
			if(formula != null) {
				boolean result = false;
				
				result = (boolean)JavaScript.eval(formula.trim() , scanReulst);
				
				if(result) {
					if (isSelected) {
						c.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 15));
						c.setBackground(table.getSelectionBackground());
						c.setForeground(table.getSelectionForeground());
					}else{
						c.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 15));
						c.setForeground(Color.BLACK);
						c.setBackground(Color.GREEN);
					}
				}
			}
			
			return c;
			
		}catch(Exception e) {
			e.printStackTrace();
			if (isSelected) {				
				c.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 15));
				c.setBackground(table.getSelectionBackground());
				c.setForeground(table.getSelectionForeground());
			}else{
				c.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 15));
				c.setForeground(new Color(0, 0, 0));
				c.setBackground(Color.WHITE);
			}
			return c;
		}
	}
		
}