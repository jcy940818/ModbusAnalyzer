package src_en.database;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import common.util.FontManager;
import moon.Moon;
import src_en.swing.MainFrame;
import src_en.util.Util;
import src_en.util.XmlUtil;

public class StoredProcedure implements Comparable{
	
	// "Common","Facility", "RCU", "Performance", "Event", "Event History", "Statistics Data"
		
	// ���ν��� ī�װ� ���
	public static final String STORED_PROCEDURE = "storedProcedure" + "\\" + Moon.EN; // ���� ���ν���
	public static final String COMMON = "COMMON"; // ����
	public static final String SERVERINFO_FACILITY = "SERVERINFO_FACILITY"; // �ü��� ����
	public static final String SERVERINFO_RTU = "SERVERINFO_RTU"; // RCU ����
	public static final String SERVER_PERF = "SERVER_PERF"; // ���� ����
	public static final String ALARM = "ALARM"; // �̺�Ʈ ����
	public static final String EVENTS = "EVENTS"; // �̺�Ʈ ���� ����
	public static final String SERVER_PERFREPORT = "SERVER_PERFREPORT"; // ��� ������ ����
	
	private static Font titlefont = FontManager.getFont(Font.BOLD, 17);
	private static Font boldfont = FontManager.getFont(Font.BOLD, 16);	
	private static Font plainfont = FontManager.getFont(Font.PLAIN, 15);
	
	private static HashMap<String, ArrayList<StoredProcedure>> procedureMap;	
	
	private int index; // ���ν��� �ε���	
	private String code; // "category-index"
	private String fileName; // ���ν��� xml ���� �̸�
	private String filePath; // ���ν��� xml ���� ���
	
	private String category; // ���ν��� ���� (�ü��� ����, ���� ����, ��赥���� ���� ��)
	private String name; // ���ν��� �̸�
	private String content; // ���ν��� ���� ����
	private String query; // ���� ���ø� (�Ķ���Ͱ� ���ε��� ���� ����)
	private String executeQuery; // ���� ���� (�Ķ���Ͱ� ���εǾ� ������ ���� �� ����)
	private boolean useParam; // �Ķ���� ��� ����
	private ArrayList<QueryParameter> paramList; // �Ķ���� ����Ʈ
	
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getExecuteQuery() {
		return executeQuery;
	}
	public void setExecuteQuery(String executeQuery) {
		this.executeQuery = executeQuery;
	}
	public boolean isUseParam() {
		return useParam;
	}
	public void setUseParam(boolean useParam) {
		this.useParam = useParam;
	}
	public ArrayList<QueryParameter> getParamList() {
		return paramList;
	}
	public void setParamList(ArrayList<QueryParameter> paramList) {
		this.paramList = paramList;
	}
	
	public static HashMap<String, ArrayList<StoredProcedure>> getProcedureMap(){
		return procedureMap;
	}
	
	public static void init() {
		makeDir(MainFrame.getCurrentPath(), "storedProcedure");
		
		String storedProcedurePath = makeDir(MainFrame.getCurrentPath(), StoredProcedure.STORED_PROCEDURE);		
		makeDir(storedProcedurePath, StoredProcedure.COMMON);
		makeDir(storedProcedurePath, StoredProcedure.SERVERINFO_FACILITY);
		makeDir(storedProcedurePath, StoredProcedure.SERVERINFO_RTU);
		makeDir(storedProcedurePath, StoredProcedure.SERVER_PERF);
		makeDir(storedProcedurePath, StoredProcedure.ALARM);
		makeDir(storedProcedurePath, StoredProcedure.EVENTS);
		makeDir(storedProcedurePath, StoredProcedure.SERVER_PERFREPORT);
	}
	
	public static String makeDir(String path, String file) {
		File dir = new File(path + "\\" + file);		
		
		if(!dir.exists()) {
			dir.mkdir();
		}else {
			return file;
		}
		
		return dir.getAbsolutePath();
	}
	
	public static void loadStoredProcedureMap(){
		procedureMap = new HashMap<String, ArrayList<StoredProcedure>>();
				
		procedureMap.put(StoredProcedure.COMMON, loadStoredProcedure(null, StoredProcedure.COMMON)); // ����
		procedureMap.put(StoredProcedure.SERVERINFO_FACILITY, loadStoredProcedure(null, StoredProcedure.SERVERINFO_FACILITY)); // �ü���
		procedureMap.put(StoredProcedure.SERVERINFO_RTU, loadStoredProcedure(null, StoredProcedure.SERVERINFO_RTU)); // RCU
		procedureMap.put(StoredProcedure.SERVER_PERF, loadStoredProcedure(null, StoredProcedure.SERVER_PERF)); // ����
		procedureMap.put(StoredProcedure.ALARM, loadStoredProcedure(null, StoredProcedure.ALARM)); // �̺�Ʈ
		procedureMap.put(StoredProcedure.EVENTS, loadStoredProcedure(null, StoredProcedure.EVENTS)); // �̺�Ʈ ����
		procedureMap.put(StoredProcedure.SERVER_PERFREPORT, loadStoredProcedure(null, StoredProcedure.SERVER_PERFREPORT)); // ��� ������								
	}
	
	public static ArrayList<StoredProcedure> loadStoredProcedure(String path, String category){
		ArrayList<StoredProcedure> list = new ArrayList<StoredProcedure>();
		String autoPath = MainFrame.getCurrentPath() + "\\" + StoredProcedure.STORED_PROCEDURE + "\\" + category;
		
		if(path != null) {
			autoPath =  path; 
			System.out.println("User Procedure Path : " + autoPath);
		}
		
		File file = new File(autoPath);
		File[] fs = file.listFiles();
		
		if(fs == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(Util.colorRed("StoredProcedure Load Exception\n"));
			sb.append(String.format("Path : %s%s%s\n\n", Util.colorBlue(autoPath.replace(category, "")), Util.separator, Util.separator));
			sb.append(String.format("Can't find the %s%s%s\n", Util.colorBlue(category) + " directory", Util.separator, Util.separator));							

			int menu = Util.showOption(sb.toString(), new String[] { "Keep loading", "Set User path", "Cancel" }, JOptionPane.QUESTION_MESSAGE);
					
			switch (menu) {
				case -1: // ����ڰ� �޴��� �������� �ʰ� ��ȭ���ڸ� �ݾ��� ���
					sb = new StringBuilder();
					sb.append(Util.colorRed("Cancel StoredProcedure Load\n"));
					sb.append(String.format("Canceled the procedure loading operation%s%s\n", Util.separator, Util.separator));							
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);		
					return null;
				case 0: // �ش� ī�װ� ���ν��� �����ϰ� ��� �ε�
					break;
				case 1: // ���ν��� ���丮 ��� ���� ����
					String userPath = Util.getFilePath();
					return loadStoredProcedure(userPath, category);
				case 2: // �� ��
					sb = new StringBuilder();
					sb.append(Util.colorRed("Cancel StoredProcedure Load\n"));
					sb.append(String.format("Canceled the procedure loading operation%s%s\n", Util.separator, Util.separator));							
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);		
					return null;
			}
			
		}else {
			for(File f : fs) {
					// ���丮�� �ƴ϶� ������ �׸� �˻�
					if(f.isFile()) { 
						String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1);				
						if(ext.equalsIgnoreCase("xml")) {
							// ���� Ȯ���ڰ� xml �� ���ϸ� ����
							
							try {
								ArrayList<StoredProcedure> tempList = XmlUtil.getProcedureList(f.getAbsolutePath());
								
								for(int i = 0; i < tempList.size(); i++) {							
									tempList.get(i).setIndex(i);
									tempList.get(i).setCategory(category);
									tempList.get(i).setCode(category + "-" + i);
									tempList.get(i).setFileName(f.getName());
									tempList.get(i).setFilePath(f.getAbsolutePath());
									list.add(tempList.get(i));
								}
								
							}catch(Exception e) {
								e.printStackTrace();
								StringBuilder sb = new StringBuilder();
								sb.append(Util.colorRed("StoredProcedure Load Exception\n"));
								sb.append(String.format("Path : %s%s%s\n\n", Util.colorBlue(autoPath), Util.separator, Util.separator));
								sb.append(String.format("%s%s%s\n\n", Util.colorBlue(category) + " directory", Util.separator, Util.separator));
//								sb.append(String.format("%s%s%s\n\n", Util.colorBlue(f.getName()) + " ������ ���ν��� �ε��� ���ܰ� �߻��Ͽ����ϴ�", Util.separator, Util.separator));
								sb.append(String.format("%s%s%s\n\n", "An exception occurred while loading the procedure in file " + Util.colorBlue(f.getName()), Util.separator, Util.separator));
								sb.append(String.format("Exception Message : %s%s%s\n" , e.getMessage(), Util.separator, Util.separator));
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
							}
							
						}				
					}
				}
		}
		
		// ���ν������� �������� ����Ʈ �ε���
		Collections.sort(list);
		for(int i = 0; i < list.size(); i++) {
			list.get(i).setIndex(i);
			list.get(i).setCode(list.get(i).getCategory() + "-" + i);
		}
		
		return list;
	}
	
	public static boolean mappingParamter(StoredProcedure sp) {
		
		// �Ķ���Ͱ� ���� ���ν����� ��� �н�
		if(!sp.isUseParam()) {
			sp.setExecuteQuery(sp.getQuery());
			return true;
		}
		
		boolean inputSuccess = inputParameter(sp);
		
		// ���� ���ø� ���� �˻� (������ �Ķ���� ������ �Ķ���� ���� ������ Ȯ��)
		if(inputSuccess) {
			ArrayList<QueryParameter> paramList = sp.getParamList();
			
			for(int i = 0; i < paramList.size(); i++) {
				boolean checkParam = sp.getQuery().contains(String.format("[param%d]", i+1));
				
				if(!checkParam) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("StoredProcedure Parameter Mapping Error\n"));
					sb.append(String.format("Procedure XML File : %s%s%s\n\n", Util.colorBlue(sp.getFileName()), Util.separator, Util.separator));
					
//					sb.append(String.format("%s%s%s\n\n", Util.colorBlue(sp.getName()) + " ���ν����� �Ķ���� ������ ������ �߻��Ͽ����ϴ�", Util.separator, Util.separator));
					sb.append(String.format("%s%s%s\n\n", "There was a problem mapping the parameter contents of procedure " + Util.colorBlue(sp.getName()), Util.separator, Util.separator));
					
//					sb.append(String.format("�ش� ���ν����� ������ �Ķ����(&lt;param&gt; �±�) ������ %s�� �Դϴ�%s%s\n\n", Util.colorBlue(String.valueOf(paramList.size())) , Util.separator, Util.separator));
					sb.append(String.format("The procedure has %s parameters(&lt;param&gt; Tag) set%s%s\n\n", Util.colorBlue(String.valueOf(paramList.size())) , Util.separator, Util.separator));
					
//					sb.append(String.format("�� ���ν����� ���� ���� ���뿡 %s ���ڿ��� ���ԵǾ� �־�� �մϴ�%s%s\n\n", 
					sb.append(String.format("Therefore, the query content of the procedure must contain a %s string%s%s\n\n",							
							(paramList.size() == 1) ? Util.colorBlue(String.format("[param1]")) : Util.colorBlue(String.format("[param1] ~ [param%d]", paramList.size())), 
							Util.separator, Util.separator));
					
//					sb.append(String.format("������ ���� ������ ����(&lt;query&gt; �±�) ���뿡�� %s ������ ã�� �� �����ϴ�%s%s\n\n\n",
					sb.append(String.format("However, I can't find the %s content in the currently set query(&lt;query&gt; Tag) content%s%s\n\n\n",		
							Util.colorRed(String.format("[param%d] ( %s )", i+1, paramList.get(i).getName())),
							Util.separator, Util.separator));
					
//					sb.append(String.format("%s\n", Util.colorGreen(String.format("%s ���ν����� �Ķ����", sp.getName()))));
					sb.append(String.format("%s\n", Util.colorGreen(String.format("Parameters of procedure %s", sp.getName()))));
					for(int i2 = 0; i2 < paramList.size(); i2++) {
						QueryParameter param = paramList.get(i2);
						sb.append(String.format("%d. %s = %s\n",i2+1 ,String.format("[param%d]", i2+1),param.getName()));
					}
							
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);				
					return false;
				}				
			}
						
			// �Է� �Ķ���� ����
			sp.setExecuteQuery(sp.getQuery()); // ���� ���ø��� ����
			for(int i = 0; i < paramList.size(); i++) {
				sp.setExecuteQuery(sp.getExecuteQuery().replace(String.format("[param%d]", i+1), String.format("%s", paramList.get(i).getValue())));
			}
			
		}else {
			// �Ķ���� �Է¿� �����Ͽ��� ���
			return inputSuccess;
		}
		
		return true;
	}
	
	
	public static boolean inputParameter(StoredProcedure sp) {		
		
		try {
			ArrayList<QueryParameter> paramList = sp.getParamList();
			
			ArrayList msgList = new ArrayList();
			ArrayList paramValueList = new ArrayList();
			
			JLabel title = new JLabel("<html>" + Util.colorBlue("Enter the parameters") + Util.longSeparator +"<br><br></html>");			
			title.setFont(titlefont);
			msgList.add(title);
									
			JLabel procedureName = new JLabel("<html>" + sp.getName() + Util.longSeparator +"<br><br></html>");
			procedureName.setFont(titlefont);
			msgList.add(procedureName);

			for (int i = 0; i < paramList.size(); i++) {
				QueryParameter param = paramList.get(i);
				
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("<html>%s. ", Util.colorBlue(String.valueOf(param.getIndex() + 1))));
				sb.append(String.format("%s", Util.colorBlue(param.getName())));				
				if(param.getExample().length() > 0) {
					sb.append(String.format(" ( Input example : %s )%s</html>", param.getExample(), Util.longSeparator));
				}else {
					sb.append(Util.longSeparator + "</html>");
				}
				
				JLabel label = new JLabel(sb.toString());				
				label.setFont(boldfont);
				msgList.add(label);

				JTextField inputParam = new JTextField();
				inputParam.setBackground(Color.WHITE);
				inputParam.setSize(77, 32);
				inputParam.setFont(boldfont);
				inputParam.setBorder(new LineBorder(Color.DARK_GRAY, 2));
				
				msgList.add(inputParam);
				paramValueList.add(inputParam);
				
				msgList.add(new JLabel("<html><br></html>"));
			}

			Object[] message = msgList.toArray();

			int option = JOptionPane.showConfirmDialog(null, message, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);

			if (option == JOptionPane.OK_OPTION) {

				for(int i = 0; i < paramValueList.size(); i++) {
					QueryParameter param = paramList.get(i);
					String paramValue =  ((JTextField)(paramValueList.get(i))).getText();
					
					if(paramValue.length() < 1) {
//						String str = String.format("%s%s �Ķ������ ������ �Էµ��� �ʾ� ���ν��� ���࿡ �����Ͽ����ϴ�&nbsp;&nbsp;&nbsp;&nbsp;\n",Util.colorRed("Parameter input Error\n") ,Util.colorBlue(param.getName()));
						String str = String.format("%sThe procedure execution failed because the contents of parameter %s were not entered&nbsp;&nbsp;&nbsp;&nbsp;\n",Util.colorRed("Parameter input Error\n") ,Util.colorBlue(param.getName()));
						Util.showMessage(str , JOptionPane.ERROR_MESSAGE);
						return false;
					}
					
					// �Է¹��� �Ķ���� ���뿡 ������ ���ٸ� ����
					param.setValue(paramValue);
				}
				
				return true;
				
			} else {
				 Util.showMessage("<font color='red'>Cancel the procedure</font>\nThe procedure has been canceled&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
				 return false;
			}
			
		} catch (Exception exception) {
			// ���ο��� �Ͼ�� ��� ���ܸ� ó��
		}
		
		return true;
	}
	
	
	public String toString() {
		return this.getName();
	}		
	
	@Override
	public int compareTo(Object obj) {		
		StoredProcedure sp = (StoredProcedure)obj;				
		return this.getName().compareTo(sp.getName());		
	}		
	
	public void userChoice() {				
		
				
	}
		
}
