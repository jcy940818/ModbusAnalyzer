package src_en.util;

import javax.swing.JOptionPane;

public class MessageUtil_en {
	
	// ����
	public static void common_1(String rowNum, String param) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n", Util.colorBlue(param), Util.colorBlue(rowNum) ,Util.separator));
		
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	// ���ɸ� ************************************************************************************************
	public static void perfName_1(String rowNum) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���ɸ�</font> ���뿡 ������ �ֽ��ϴ�%s\n", rowNum, Util.separator));
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n", Util.colorBlue("Performance Name"), Util.colorBlue(rowNum) ,Util.separator));
		
//		sb.append(String.format("\n���ɸ��� �ݵ�� �Է��ؾ� �ϴ� �ʵ��Դϴ�%s\n", Util.separator));
		sb.append(String.format("\nThe Performance name is a field that you must enter%s\n", Util.separator));
		
//		sb.append(String.format("\n���̺� <font color='blue'>%s</font> ���� ���ɸ� ������ �Է����ּ���%s\n",rowNum, Util.separator));
		sb.append(String.format("\nPlease enter the Performance names in row %s of the table%s\n", Util.colorBlue(rowNum), Util.separator));
		
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	public static void perfName_2(String rowNum, String perfName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���ɸ�</font> ���뿡 ������ �ֽ��ϴ�%s\n", rowNum, Util.separator));
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n", Util.colorBlue("Performance Name"), Util.colorBlue(rowNum) ,Util.separator));
		
//		sb.append(String.format("\n���ɸ� �Ʒ��� Ư�� ���ڸ� ������ Ư�� ���ڴ� ���� �� �� �����ϴ�%s\n", Util.separator));
		sb.append(String.format("\nThe Performance name cannot include special characters other than the special characters below%s\n", Util.separator));
		
//		sb.append(String.format("\n���ɸ� ���� ��� Ư�� ���� : <font color='blue'> .  #  { }  ( )  [ ]  _  -  /  :</font>%s\n", Util.separator));
		sb.append(String.format("\nAllowed special characters : <font color='blue'> .  #  { }  ( )  [ ]  _  -  /  :</font>%s\n", Util.separator));
		
		
//		sb.append(String.format("\n���� ���̺� <font color='blue'>%s</font> ���� ���ɸ� ���� : <font color='red'>%s</font>%s\n", rowNum, perfName ,Util.separator));
		sb.append(String.format("\nContents of %s currently entered in row %s : %s%s\n", Util.colorBlue("Performance name"), Util.colorBlue(rowNum), Util.colorRed(perfName) ,Util.separator));
		
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}

	
	// ����ڵ� ************************************************************************************************
	public static void functionCode_1(String rowNum, String functionCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>����ڵ�</font> ���뿡 ������ �ֽ��ϴ�%s\n", rowNum, Util.separator));
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n", Util.colorBlue("Function Code"), Util.colorBlue(rowNum) ,Util.separator));
		
		sb.append(String.format("\n<font color='blue'>Supporting Modbus Function Code items</font>\n"));
		
		
		sb.append(String.format("FC 01 : Read Coil Status\n"));
		sb.append(String.format("FC 02 : Read Input Status\n"));
		sb.append(String.format("FC 03 : Read Holding Registers\n"));
		sb.append(String.format("FC 04 : Read Input Registers\n\n"));
		
//		sb.append(String.format("���� ���̺� <font color='blue'>%s</font> ���� ����ڵ� ���� : <font color='red'>%s</font>%s\n",rowNum, functionCode ,Util.separator));
		sb.append(String.format("Contents of %s currently entered in row %s : %s%s\n", Util.colorBlue("Function Code"), Util.colorBlue(rowNum), Util.colorRed(functionCode) ,Util.separator));
		
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	
	// ������ Ÿ�� ************************************************************************************************
	public static void dataType_1(String rowNum, String dataType) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>������ Ÿ��</font> ���뿡 ������ �ֽ��ϴ�%s%s%s\n", rowNum, Util.separator, Util.separator, Util.separator));
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n", Util.colorBlue("Data Type"), Util.colorBlue(rowNum) ,Util.separator));		
		
//		sb.append(String.format("\n<font color='blue'>Modbus Collection ���� ������ Ÿ��</font>%s\n", Util.separator));			
		sb.append(String.format("\n<font color='blue'>Supporting Modbus Data Type items</font>\n"));
		
		sb.append(String.format("1. %s%s%s\n","BINARY" , Util.separator, Util.separator));				
		sb.append(String.format("2. %s%s%s\n","TWO BYTE INT SIGNED" , Util.separator, Util.separator));
		sb.append(String.format("3. %s%s%s\n","TWO BYTE INT UNSIGNED" , Util.separator, Util.separator));
		sb.append(String.format("4. %s%s%s\n","FOUR BYTE INT SIGNED (A B C D)" , Util.separator, Util.separator));
		sb.append(String.format("5. %s%s%s\n","FOUR BYTE INT SIGNED (C D A B)" , Util.separator, Util.separator));
		sb.append(String.format("6. %s%s%s\n","FOUR BYTE INT UNSIGNED (A B C D)" , Util.separator, Util.separator));
		sb.append(String.format("7. %s%s%s\n","FOUR BYTE INT UNSIGNED (C D A B)" , Util.separator, Util.separator));
		sb.append(String.format("8. %s%s%s\n","FOUR BYTE FLOAT (A B C D)" , Util.separator, Util.separator));
		sb.append(String.format("9. %s%s%s\n","FOUR BYTE FLOAT (C D A B)" , Util.separator, Util.separator));
		sb.append(String.format("10. %s%s%s\n","EIGHT BYTE INT SIGNED (A B C D)" , Util.separator, Util.separator));
		sb.append(String.format("11. %s%s%s\n","EIGHT BYTE DOUBLE (A B C D)" , Util.separator, Util.separator));			
		
//		sb.append(String.format("\n���� ���̺� <font color='blue'>%s</font> ���� ������ Ÿ�� ���� : <font color='red'>%s</font>%s%s\n",rowNum, dataType ,Util.separator,Util.separator));
		sb.append(String.format("\nContents of %s currently entered in row %s : %s%s\n", Util.colorBlue("Data Type"), Util.colorBlue(rowNum), Util.colorRed(dataType) ,Util.separator));
		
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	// ������ Ÿ�� ������ BINARY �ε� ����ڵ尡 1, 2���� �ƴ� ���
	public static void dataType_2(String rowNum, String functionCode, String dataType) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>������ Ÿ��</font> ���뿡 ������ �ֽ��ϴ�%s%s%s\n\n", rowNum, Util.separator, Util.separator, Util.separator));
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n\n", Util.colorBlue("Data Type"), Util.colorBlue(rowNum) ,Util.separator));
		
//		sb.append(String.format("<font color='blue'>BINARY</font> ������ Ÿ���� ON/OFF ���¸� ��Ÿ���� ������ Ÿ������%s%s\n\n", Util.separator, Util.separator));				
		sb.append(String.format("%s data type represents ON/OFF status%s%s\n\n",Util.colorBlue("BINARY") ,Util.separator, Util.separator));
		
//		sb.append(String.format("����ڵ� 1, 2���� ����ϴ� �������� �׸� ��� �� �� �ֽ��ϴ�%s%s\n\n", Util.separator, Util.separator));
		sb.append(String.format("Therefore, you can only use items that use function codes 1 and 2%s%s\n\n", Util.separator, Util.separator));
		
//		sb.append(String.format("���� ���̺� <font color='blue'>%s</font> ���� ����ڵ� ���� : <font color='red'>%s</font>%s\n",rowNum, functionCode ,Util.separator));
		sb.append(String.format("Contents of %s currently entered in row %s : %s%s\n", Util.colorBlue("Function Code"), Util.colorBlue(rowNum), Util.colorRed(functionCode) ,Util.separator));
		
//		sb.append(String.format("���� ���̺� <font color='blue'>%s</font> ���� ������ Ÿ�� ���� : <font color='red'>%s</font>%s\n",rowNum ,dataType ,Util.separator));
		sb.append(String.format("Contents of %s currently entered in row %s : %s%s\n", Util.colorBlue("Data Type"), Util.colorBlue(rowNum), Util.colorRed(dataType) ,Util.separator));
				
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	
	// ����ڵ� ************************************************************************************************
	// ����ڵ尡 1, 2���ε� ������ Ÿ�� ������ BINARY�� �ƴ� ���
	public static void dataType_3(String rowNum, String functionCode, String dataType) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>������ Ÿ��</font> ���뿡 ������ �ֽ��ϴ�%s%s%s\n\n", rowNum, Util.separator, Util.separator, Util.separator));
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n\n", Util.colorBlue("Data Type"), Util.colorBlue(rowNum) ,Util.separator));
		
//		sb.append(String.format("����ڵ� 1, 2���� ����ϴ� �������ʹ�\n\n������ Ÿ������ ���� <font color='blue'>BINARY</font> Ÿ�Ը��� ��� �� �� �ֽ��ϴ�%s%s\n\n", Util.separator, Util.separator));				
		sb.append(String.format("Items that use function codes 1 and 2 must use %s as a data type%s%s\n\n",Util.colorBlue("BINARY") ,Util.separator, Util.separator));
		
		sb.append(String.format("Contents of %s currently entered in row %s : %s%s\n", Util.colorBlue("Function Code"), Util.colorBlue(rowNum), Util.colorRed(functionCode) ,Util.separator));
		sb.append(String.format("Contents of %s currently entered in row %s : %s%s\n", Util.colorBlue("Data Type"), Util.colorBlue(rowNum), Util.colorRed(dataType) ,Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	// ���� ************************************************************************************************
	public static void slot_1(String rowNum, String slot) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n", Util.colorBlue("Slot"), Util.colorBlue(rowNum) ,Util.separator));
		
		sb.append(String.format("\nThe Slot value can only be entered with an integer value greater than or equal to 1%s\n\n", Util.separator));
		
		sb.append(String.format("Contents of %s currently entered in row %s : %s%s\n", Util.colorBlue("Slot"), Util.colorBlue(rowNum), Util.colorRed(slot) ,Util.separator));
		
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	public static void slot_2(String rowNum, String slot) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n", Util.colorBlue("Slot"), Util.colorBlue(rowNum) ,Util.separator));
		
		sb.append(String.format("\nThe contents of the currently entered Slot cannot be converted to an %s%s\n\n", Util.colorBlue("integer value"), Util.separator));
				
		sb.append(String.format("Contents of %s currently entered in row %s : %s%s\n", Util.colorBlue("Slot"), Util.colorBlue(rowNum), Util.colorRed(slot) ,Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	
	// �����ֱ� ************************************************************************************************
	public static void interval_1(String rowNum, String interval) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� �ֱ�</font> ���뿡 ������ �ֽ��ϴ�%s\n", rowNum, Util.separator));
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n", Util.colorBlue("Interval"), Util.colorBlue(rowNum) ,Util.separator));
		
//		sb.append(String.format("\n���� �ֱ�� 1 �̻��� ���� ���� ���� �Է� �� �� �ֽ��ϴ�%s\n\n", Util.separator));
		sb.append(String.format("\nThe Interval value can only be entered with an integer value greater than or equal to 1%s\n\n", Util.separator));
		
//		sb.append(String.format("���� ���̺� <font color='blue'>%s</font> ���� �����ֱ� �ּ� ���� : <font color='red'>%s</font>%s\n",rowNum, interval ,Util.separator));
		sb.append(String.format("Contents of %s currently entered in row %s : %s%s\n", Util.colorBlue("Interval"), Util.colorBlue(rowNum), Util.colorRed(interval) ,Util.separator));
		
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	public static void interval_2(String rowNum, String interval) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� �ֱ�</font> ���뿡 ������ �ֽ��ϴ�%s\n", rowNum, Util.separator));
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n", Util.colorBlue("Interval"), Util.colorBlue(rowNum) ,Util.separator));
		
//		sb.append(String.format("\n�Էµ� �����ֱ� ������ <font color='blue'>���� ��</font>���� ��ȯ �� �� �����ϴ�%s\n\n", Util.separator));
		sb.append(String.format("\nThe contents of the currently entered interval cannot be converted to an %s%s\n\n", Util.colorBlue("integer value"), Util.separator));
				
		sb.append(String.format("Contents of %s currently entered in row %s : %s%s\n", Util.colorBlue("Interval"), Util.colorBlue(rowNum), Util.colorRed(interval) ,Util.separator));
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	
	// ������ ************************************************************************************************
	public static void scaleFunction_1(String rowNum, String scaleFunction) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>������</font> ���뿡 ������ �ֽ��ϴ�%s\n", rowNum, Util.separator));
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n", Util.colorBlue("Scale"), Util.colorBlue(rowNum) ,Util.separator));
		
//		sb.append(String.format("\n���� ���̺� <font color='blue'>%s</font> ���� ������ ���� : <font color='red'>%s</font>%s\n",rowNum , scaleFunction ,Util.separator));
		sb.append(String.format("\nContents of %s currently entered in row %s : %s%s\n", Util.colorBlue("Scale"), Util.colorBlue(rowNum), Util.colorRed(scaleFunction) ,Util.separator));
		
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	
	// �������� ************************************************************************************************
	
	
	public static void binary_1(String rowNum, String functionCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� ���� : 1</font> ���뿡 ������ �ֽ��ϴ�%s\n", rowNum, Util.separator));
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n", Util.colorBlue("Label : 1"), Util.colorBlue(rowNum) ,Util.separator));
		
		if (functionCode.equals("1") || functionCode.equals("2")) {
//			sb.append(String.format("\n����ڵ� 1, 2���� ����ϴ� �������ʹ�\n\n�ݵ�� <font color='blue'>���� ���� : 0, 1</font> ������ �Է��ؾ� �մϴ�%s\n", Util.separator));
			sb.append(String.format("\nItems using %s 1 and 2 must be entered with %s%s\n",Util.colorBlue("Function Code"), Util.colorBlue("Label 0, 1"),Util.separator));
		}			
		
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	public static void binary_2(String rowNum, String functionCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� ���� : 0</font> ���뿡 ������ �ֽ��ϴ�%s\n", rowNum, Util.separator));
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n", Util.colorBlue("Label : 0"), Util.colorBlue(rowNum) ,Util.separator));
		
		if (functionCode.equals("1") || functionCode.equals("2")) {
//			sb.append(String.format("\n����ڵ� 1, 2���� ����ϴ� �������ʹ�\n\n�ݵ�� <font color='blue'>���� ���� : 0, 1</font> ������ �Է��ؾ� �մϴ�%s\n", Util.separator));
			sb.append(String.format("\nItems using %s 1 and 2 must be entered with %s%s\n",Util.colorBlue("Function Code"), Util.colorBlue("Label 0, 1"),Util.separator));
		}
		
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	public static void binary_3(String rowNum) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("<font color='red'>Table Validation Exception</font>\n");		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� ���� : 0</font> ���뿡 ������ �ֽ��ϴ�%s\n", rowNum, Util.separator));				
//		sb.append(String.format("\n����ڵ� 1, 2���� ����ϴ� �������ʹ�\n\n�ݵ�� <font color='blue'>���� ���� : 0, 1</font> ������ �Է��ؾ� �մϴ�%s\n", Util.separator));
//		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		
		binary_2(rowNum, "1");
	}
	
	public static void binary_4(String rowNum) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("<font color='red'>Table Validation Exception</font>\n");
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� ���� : 1</font> ���뿡 ������ �ֽ��ϴ�%s\n", rowNum, Util.separator));			
//		sb.append(String.format("\n����ڵ� 1, 2���� ����ϴ� �������ʹ�\n\n�ݵ�� <font color='blue'>���� ���� : 0, 1</font> ������ �Է��ؾ� �մϴ�%s\n", Util.separator));
//		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		
		binary_1(rowNum, "1");
	}
	
	
	// ���߻��� ************************************************************************************************
	public static void multiStatus_1(String rowNum) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� ����</font>�� <font color='blue'>���� ����</font> ���뿡 ������ �ֽ��ϴ�%s\n\n", rowNum, Util.separator));		
		sb.append(String.format("There's a problem with the %s and %s of row %s of the table%s\n\n", Util.colorBlue("Label : 0, 1"), Util.colorBlue("Multiple States"), Util.colorBlue(rowNum) ,Util.separator));
		
//		sb.append(String.format("������ ������ <font color='blue'>���� ����</font> �̸鼭 ���ÿ� <font color='blue'>���� ����</font> �� ���� �����ϴ�%s\n", Util.separator));
		sb.append(String.format("The data format cannot be %s and %s at the same time%s\n",Util.colorBlue("Binary(Label : 0, 1)"),Util.colorBlue("Multiple States"),Util.separator));
		
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	public static void multiStatus_2(String rowNum, String multiStatus, Exception e) {
		StringBuilder sb = new StringBuilder();
		sb.append("<font color='red'>Table Validation Exception</font>\n");
		
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� ����</font> ���뿡 ������ �ֽ��ϴ�%s\n", rowNum, Util.separator));
		sb.append(String.format("There's a problem with the %s of row %s of the table%s\n", Util.colorBlue("Multiple States"), Util.colorBlue(rowNum) ,Util.separator));		
		
//		sb.append(String.format("\n���� ���̺� <font color='blue'>%s</font> ���� ���� ���� ���� : <font color='red'>%s</font>%s\n",rowNum , multiStatus ,Util.separator));
		sb.append(String.format("\nContents of %s currently entered in row %s : %s%s\n", Util.colorBlue("Multiple States"), Util.colorBlue(rowNum), Util.colorRed(multiStatus) ,Util.separator));
		
		if(e instanceof NumberFormatException) {
//			sb.append(String.format("\n���� ���� �Է� ��� : <font color='blue'>����1; ����1; ����2; ����2; ����3; ����3; ...</font>%s%s\n", Util.separator, Util.separator));
			sb.append(String.format("\nMultiple States Input Format : <font color='blue'>Number1; String1; Number2; String2; Number3; String3; ...</font>%s%s\n", Util.separator, Util.separator));
			sb.append(String.format("\nInput Example : <font color='blue'>0; Normal; 1; Warning; 2; Minor; 3; Critical; 4; Fatal;</font>%s%s\n", Util.separator, Util.separator));
			sb.append(String.format("     �� Content : <font color='blue'>0=Normal , 1=Warning , 2=Minor , 3=Critical , 4=Fatal</font>%s%s\n", Util.separator, Util.separator));
		}
		
		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	public static void multiStatus_3(String rowNum) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("<font color='red'>Table Validation Exception</font>\n");
//		sb.append(String.format("���̺� <font color='blue'>%s</font> ���� <font color='blue'>���� ����</font>�� <font color='blue'>���� ����</font> ���뿡 ������ �ֽ��ϴ�%s\n\n", rowNum, Util.separator));
//		sb.append(String.format("������ ������ <font color='blue'>���� ����</font> �̸鼭 ���ÿ� <font color='blue'>���� ����</font> �� ���� �����ϴ�%s\n", Util.separator));
//		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
		
		multiStatus_1(rowNum);
	}
	
}
