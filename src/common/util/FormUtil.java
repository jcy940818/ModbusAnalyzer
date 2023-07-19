package common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import common.modbus.ModbusPointExporter;
import common.modbus.ModbusPointLoader;
import common.modbus.ModbusWatchPoint;
import common.perf.FmsActionConf;
import common.perf.FmsActionItem;
import common.perf.FmsPerfConf;
import common.perf.FmsPerfItem;
import common.perf.Perf;
import common.perf.SnmpPerfConf;
import moon.Moon;
import src_ko.util.Util;

public class FormUtil {
	
	public static void excute(File formFile, String type, String encoding) {
		if(formFile == null || !formFile.exists()) return;

		try {
			if(formFile.getAbsolutePath().toLowerCase().endsWith(".xml")) {
				processXML(formFile, type, encoding);
			}else {
				processExcel(formFile, type, encoding);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}


	public static void processXML(File formFile, String type, String encoding) {
		String formType = "Common";
		ArrayList<Perf> perfList = null;
		ArrayList<FmsActionItem> actions = null;
		
		ArrayList<ModbusWatchPoint> modbusPointList = getModbusList(formFile, encoding);
		boolean isModbusForm = (modbusPointList != null) ? true : false;
		String addrFormat = "Modbus (DEC)";
		
		if(isModbusForm) {
			for(ModbusWatchPoint point : modbusPointList) {
				if(point.getCounter().toLowerCase().contains("0x")) {
					addrFormat = "Register (HEX)";
					break;
				}
			}
		}
		
		if(type.equalsIgnoreCase("common")) {
			formType = "Common";
			
			try {
				if(isModbusForm) {
					StringBuilder msg = new StringBuilder();
					
					if(Moon.isKorean()) {
						msg.append("<font color='Green'>���� ���� Ÿ�� ����</font>\n");
						msg.append("�ٿ�ε� ������ ���� ������ Ÿ���� �������ּ���" + Util.separator + Util.separator +"\n");
					}else {
						msg.append("<font color='Green'>Select Excel File Type</font>\n");
						msg.append("Please select the type of Excel file to download" + Util.separator + Util.separator +"\n");
					}
	
					int menu = Util.showOption(msg.toString(), new String[] { "  Common  ", "  Modbus  "}, JOptionPane.QUESTION_MESSAGE);
					switch (menu) {
						case 0: // ù ��° ��ư : �Ϲ� ���� ���� ��� �ٿ�ε�
							perfList = FmsPerfConf.getFmsPerfList(formFile, encoding);
							break;
							
						case 1: // �� ��° ��ư : ������ ���� ���� ��� �ٿ�ε�
							ModbusPointExporter.exportExcel(4, addrFormat, false , true, modbusPointList);
							return;
							
						default :
							// ���ø� �ٿ�ε� ���
							return;
					}
				}else {
					// isModbusForm = false
					perfList = FmsPerfConf.getFmsPerfList(formFile, encoding);
				}
			}catch(Exception e) {
				// ���� ���� XML
				e.printStackTrace();
				perfList = FmsPerfConf.getFmsPerfList(formFile, encoding);
			}
			
		}else if(type.equalsIgnoreCase("snmp")) {
			formType = "SNMP";
			perfList = SnmpPerfConf.getSnmpPerfList(formFile, encoding);
			
		}else if(type.equalsIgnoreCase("control")){
			formType = "Control";
			
			try {
				if(isModbusForm) {
					if(downloadModbusControl(modbusPointList, encoding)) return;
					
				}else {
					actions = FmsActionConf.getFmsActionList(formFile, encoding);
				}
		    	
			}catch(Exception e) {
				// ���� ���� XML
				e.printStackTrace();
				actions = FmsActionConf.getFmsActionList(formFile, encoding);
				
			}
			
			if(actions != null && actions.size() > 0) {
				ExcelUtil.downloadControl(formType, actions);
			}
			return;
			
		}else {
			// Common, SNMP, Control Ÿ���� �ƴ� ���
			return;
		}
		
		if(perfList != null && perfList.size() > 0) {
			ExcelUtil.downloadPerf(formType, perfList);
			return;
		}
	}
	
	
	public static void processExcel(File formFile, String type, String encoding) throws IOException {
		
		ArrayList<ModbusWatchPoint> modbusPointList = getModbusList(formFile, encoding);
		boolean isModbusForm = (modbusPointList != null) ? true : false;
		String addrFormat = "Modbus (DEC)";
		
		if(type.equalsIgnoreCase("control")) {
			if(isModbusForm) {
				for(ModbusWatchPoint point : modbusPointList) {
					if(point.getCounter().toLowerCase().contains("0x")) {
						addrFormat = "Register (HEX)";
						break;
					}
				}
			}
			
			try {
				if(isModbusForm) {
					if(downloadModbusControl(modbusPointList, encoding)) return;
					
				}else {
					// ���� ���� ���� ���
					ArrayList<FmsActionItem> controlList = ExcelUtil.loadControlItem(formFile);
					if(controlList != null && controlList.size() > 0) {
						XmlGenerator.generateControlXML(controlList, encoding);
					}
				}
			}catch(Exception e) {
				// ���� ���� ���� ���
				e.printStackTrace();
				ArrayList<FmsActionItem> controlList = ExcelUtil.loadControlItem(formFile);
				if(controlList != null && controlList.size() > 0) {
					XmlGenerator.generateControlXML(controlList, encoding);
				}
			}
			
		}else {
			
			if(isModbusForm) {
				ArrayList<FmsPerfItem> perfList = new ArrayList<FmsPerfItem>();
				for(int i = 0; i < modbusPointList.size(); i++) {
					perfList.add(modbusPointList.get(i));
				}
				XmlGenerator.generateXML(perfList, true, encoding, type);
				
			}else {
				ArrayList<FmsPerfItem> perfList = ExcelUtil.loadPerfItem(formFile, type);
				if(perfList != null && perfList.size() > 0) {
					XmlGenerator.generateXML(perfList, true, encoding, type);
				}
			}
			
		}
	}
	

	public static ArrayList<ModbusWatchPoint> getModbusList(File formFile, String encoding) {
		try {
			if(formFile.getAbsolutePath().toLowerCase().endsWith(".xml")) {
				// XML
				ArrayList<Perf> modbusPointList = FmsPerfConf.getFmsPerfList(formFile, encoding);
				
				if(modbusPointList != null && modbusPointList.size() > 0) {
					ModbusWatchPoint[] modbusWps = new ModbusWatchPoint[modbusPointList.size()];
			    	for(int i = 0; i < modbusPointList.size(); i++) {
			    		modbusWps[i] = new ModbusWatchPoint(modbusPointList.get(i));
			    		modbusWps[i].init();
			    	}
			    	return ModbusWatchPoint.convertArrayToList(modbusWps);
				}
		    	
			}else {
				// Excel
				ArrayList<ModbusWatchPoint> modbusPointList = ModbusPointLoader.loadExcelV4(formFile);
				if(modbusPointList != null && modbusPointList.size() > 0) {
					return modbusPointList;
				}
			}
	    	
	    	return null;
	    	
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean downloadModbusControl(ArrayList<ModbusWatchPoint> pointList, String encoding) throws Exception{
		ArrayList<FmsActionItem> actions = new ArrayList<FmsActionItem>();
    	
    	boolean allFcCode = false;
    	boolean generateDesc = false;
    	boolean concatMeasure = false;
    	
    	Font font = FontManager.getFont(Font.BOLD, 15);
		
		JLabel insert = new JLabel();
		if(Moon.isKorean()) {
			insert.setText("<html><font color='green'>������ ���� ��ȯ �ɼ�</font>" + Util.longSeparator + Util.longSeparator +  Util.longSeparator +"<br><br></html>");
		}else {
			insert.setText("<html><font color='green'>Modbus control conversion option</font>" + Util.longSeparator + Util.longSeparator +  Util.longSeparator +"<br><br></html>");
		}
		insert.setFont(font);
		
		
		JLabel allFcCode_label = new JLabel();
		if(Moon.isKorean()) {
			allFcCode_label.setText("<html><font color='blue'>���� ��ȯ ��� ����ڵ� ����</font>" + Util.longSeparator + Util.longSeparator +  Util.longSeparator +"<br></html>");
		}else {
			allFcCode_label.setText("<html><font color='blue'>Select control conversion target function code</font>" + Util.longSeparator + Util.longSeparator +  Util.longSeparator +"<br></html>");
		}
		allFcCode_label.setFont(font);
		
		
		JLabel generateDesc_label = new JLabel();
		if(Moon.isKorean()) {
			generateDesc_label.setText("<html><font color='blue'>���� �׸� ���� �ڵ� ����</font><br></html>");
		}else {
			generateDesc_label.setText("<html><font color='blue'>Automatic generation of control item description</font><br></html>");
		}
		generateDesc_label.setFont(font);
		
		
		JLabel concatMeasure_label = new JLabel();
		if(Moon.isKorean()) {
			concatMeasure_label.setText("<html><font color='blue'>���� �׸� ���� ���� ���� ǥ��</font><br></html>");
		}else {
			concatMeasure_label.setText("<html><font color='blue'>Show units of measurement in the control item description</font><br></html>");
		}
		concatMeasure_label.setFont(font);
		
		
		JRadioButton selectFc = new JRadioButton();
		if(Moon.isKorean()) {
			selectFc.setText(" ����ڵ� 1 �Ǵ� 3 �׸� ���Ͽ� ����� ��ȯ");
		}else {
			selectFc.setText(" Function Code 1 or 3 items");
		}
		selectFc.setForeground(Color.BLACK);
		selectFc.setFont(font);
		selectFc.setFocusPainted(false);
		selectFc.setSelected(true);
		
		JRadioButton allFc = new JRadioButton();
		if(Moon.isKorean()) {
			allFc.setText(" ��� ����ڵ� �׸� ���Ͽ� ����� ��ȯ");
		}else {
			allFc.setText(" All Function Code items");
		}		
		allFc.setForeground(Color.BLACK);
		allFc.setFont(font);
		allFc.setFocusPainted(false);
		
		
		ButtonGroup group = new ButtonGroup();
		group.add(selectFc);
		group.add(allFc);
		
		
		JCheckBox concatMeasure_check = new JCheckBox();
		if(Moon.isKorean()) {
			concatMeasure_check.setText(" ���� ���� ���뿡 ������ ���� ���� ǥ��");
		}else {
			concatMeasure_check.setText(" Display units of performance in the control item description");
		}
		concatMeasure_check.setForeground(Color.BLACK);
		concatMeasure_check.setFont(font);
		concatMeasure_check.setFocusPainted(false);
		
		JCheckBox generateDesc_check = new JCheckBox();
		if(Moon.isKorean()) {
			generateDesc_check.setText(" ���� ���� ���� �ڵ� ���� ���");
		}else {
			generateDesc_check.setText(" Enable automatic generation of control item description");
		}
		generateDesc_check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(generateDesc_check.isSelected()) {					
					concatMeasure_label.setEnabled(true);
					concatMeasure_check.setEnabled(true);
					
				}else {
					concatMeasure_label.setEnabled(false);
					concatMeasure_check.setEnabled(false);
				}
			}
		});
		generateDesc_check.setSelected(true);
		generateDesc_check.setForeground(Color.BLACK);
		generateDesc_check.setFont(font);
		generateDesc_check.setFocusPainted(false);
		
		Object[] message = {
				   insert,
				   
				   allFcCode_label, selectFc, allFc,
				   new JLabel("<html><br></html>"),
				   
				   generateDesc_label, generateDesc_check,
				   new JLabel("<html><br></html>"),
				   
				   concatMeasure_label, concatMeasure_check,
				   new JLabel("<html><br></html>"),
			};
			
		int	option = JOptionPane.showConfirmDialog(null, message, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		
		if(option == JOptionPane.OK_OPTION) {
			allFcCode = allFc.isSelected();
			generateDesc = generateDesc_check.isSelected();
			concatMeasure = generateDesc && concatMeasure_check.isSelected();
			
		} else {
			return true;
			
		}
    	
		actions = ModbusWatchPoint.getControlList(pointList, allFcCode, generateDesc, concatMeasure);
    	
    	if(actions != null && actions.size() > 0) {
    		StringBuilder msg = new StringBuilder();
			
			if(Moon.isKorean()) {
				msg.append("<font color='Green'>���� ��� ���� Ÿ�� ����</font>\n");
				msg.append("������ ���� ��ȯ�� �����Ͽ����ϴ�!" + Util.separator + Util.separator +"\n\n");
				msg.append("���� ��� ������ Ÿ���� �������ּ���" + Util.separator + Util.separator +"\n");
			}else {
				msg.append("<font color='Green'>Select Control File Type</font>\n");
				msg.append("Modbus Control conversion succeeded!" + Util.separator + Util.separator +"\n\n");
				msg.append("Please select the type of control form file" + Util.separator + Util.separator +"\n");
			}

			int menu = Util.showOption(msg.toString(), new String[] { "  XML  ", "  Excel  "}, JOptionPane.QUESTION_MESSAGE);
			switch (menu) {
				case 0: // ù ��° ��ư : XML
					XmlGenerator.generateControlXML(actions, encoding);
					return true;
					
				case 1: // �� ��° ��ư : Excel
					ExcelUtil.downloadControl("Control", actions);
					return true;
					
				default :
					// ���ø� �ٿ�ε� ���
					return true;
			}
		} else {
			// ��ȯ�� ���� �׸��� ������ ���
			StringBuilder msg = new StringBuilder();
			if(Moon.isKorean()) {
				msg.append("<font color='red'>���� ��ȯ �Ұ���</font>\n");
				msg.append("������ ����� ��ȯ ������ �׸��� �����ϴ�" + Util.separator + Util.separator +"\n\n");
				msg.append("( �����ϼ̴� ���� ��ȯ �ɼ� ������ Ȯ�����ּ��� )" + Util.separator + Util.separator +"\n");
			}else {
				msg.append("<font color='red'>Unable to control conversion</font>\n");
				msg.append("No items can be converted to modbus control" + Util.separator + Util.separator +"\n\n");
				msg.append("( Please check the control conversion options you selected )" + Util.separator + Util.separator +"\n");
			}
			
			Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
			return true;
    	}
	}
	
}
