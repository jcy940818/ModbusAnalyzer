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
		
	// 프로시저 카테고리 상수
	public static final String STORED_PROCEDURE = "storedProcedure" + "\\" + Moon.EN; // 저장 프로시저
	public static final String COMMON = "COMMON"; // 공통
	public static final String SERVERINFO_FACILITY = "SERVERINFO_FACILITY"; // 시설물 관련
	public static final String SERVERINFO_RTU = "SERVERINFO_RTU"; // RCU 관련
	public static final String SERVER_PERF = "SERVER_PERF"; // 성능 관련
	public static final String ALARM = "ALARM"; // 이벤트 관련
	public static final String EVENTS = "EVENTS"; // 이벤트 내역 관련
	public static final String SERVER_PERFREPORT = "SERVER_PERFREPORT"; // 통계 데이터 관련
	
	private static Font titlefont = FontManager.getFont(Font.BOLD, 17);
	private static Font boldfont = FontManager.getFont(Font.BOLD, 16);	
	private static Font plainfont = FontManager.getFont(Font.PLAIN, 15);
	
	private static HashMap<String, ArrayList<StoredProcedure>> procedureMap;	
	
	private int index; // 프로시저 인덱스	
	private String code; // "category-index"
	private String fileName; // 프로시저 xml 파일 이름
	private String filePath; // 프로시저 xml 파일 경로
	
	private String category; // 프로시저 종류 (시설물 관련, 성능 관련, 통계데이터 관련 등)
	private String name; // 프로시저 이름
	private String content; // 프로시저 수행 내용
	private String query; // 쿼리 템플릿 (파라미터가 매핑되지 않은 쿼리)
	private String executeQuery; // 수행 쿼리 (파라미터가 매핑되어 실제로 수행 될 쿼리)
	private boolean useParam; // 파라미터 사용 여부
	private ArrayList<QueryParameter> paramList; // 파라미터 리스트
	
	
	
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
				
		procedureMap.put(StoredProcedure.COMMON, loadStoredProcedure(null, StoredProcedure.COMMON)); // 공통
		procedureMap.put(StoredProcedure.SERVERINFO_FACILITY, loadStoredProcedure(null, StoredProcedure.SERVERINFO_FACILITY)); // 시설물
		procedureMap.put(StoredProcedure.SERVERINFO_RTU, loadStoredProcedure(null, StoredProcedure.SERVERINFO_RTU)); // RCU
		procedureMap.put(StoredProcedure.SERVER_PERF, loadStoredProcedure(null, StoredProcedure.SERVER_PERF)); // 성능
		procedureMap.put(StoredProcedure.ALARM, loadStoredProcedure(null, StoredProcedure.ALARM)); // 이벤트
		procedureMap.put(StoredProcedure.EVENTS, loadStoredProcedure(null, StoredProcedure.EVENTS)); // 이벤트 내역
		procedureMap.put(StoredProcedure.SERVER_PERFREPORT, loadStoredProcedure(null, StoredProcedure.SERVER_PERFREPORT)); // 통계 데이터								
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
				case -1: // 사용자가 메뉴를 선택하지 않고 대화상자를 닫았을 경우
					sb = new StringBuilder();
					sb.append(Util.colorRed("Cancel StoredProcedure Load\n"));
					sb.append(String.format("Canceled the procedure loading operation%s%s\n", Util.separator, Util.separator));							
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);		
					return null;
				case 0: // 해당 카테고리 프로시저 제외하고 계속 로드
					break;
				case 1: // 프로시저 디렉토리 경로 직접 지정
					String userPath = Util.getFilePath();
					return loadStoredProcedure(userPath, category);
				case 2: // 취 소
					sb = new StringBuilder();
					sb.append(Util.colorRed("Cancel StoredProcedure Load\n"));
					sb.append(String.format("Canceled the procedure loading operation%s%s\n", Util.separator, Util.separator));							
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);		
					return null;
			}
			
		}else {
			for(File f : fs) {
					// 디렉토리가 아니라 파일인 항목만 검사
					if(f.isFile()) { 
						String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1);				
						if(ext.equalsIgnoreCase("xml")) {
							// 파일 확장자가 xml 인 파일만 수행
							
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
//								sb.append(String.format("%s%s%s\n\n", Util.colorBlue(f.getName()) + " 파일의 프로시저 로드중 예외가 발생하였습니다", Util.separator, Util.separator));
								sb.append(String.format("%s%s%s\n\n", "An exception occurred while loading the procedure in file " + Util.colorBlue(f.getName()), Util.separator, Util.separator));
								sb.append(String.format("Exception Message : %s%s%s\n" , e.getMessage(), Util.separator, Util.separator));
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
							}
							
						}				
					}
				}
		}
		
		// 프로시저명을 기준으로 리스트 인덱싱
		Collections.sort(list);
		for(int i = 0; i < list.size(); i++) {
			list.get(i).setIndex(i);
			list.get(i).setCode(list.get(i).getCategory() + "-" + i);
		}
		
		return list;
	}
	
	public static boolean mappingParamter(StoredProcedure sp) {
		
		// 파라미터가 없는 프로시저일 경우 패스
		if(!sp.isUseParam()) {
			sp.setExecuteQuery(sp.getQuery());
			return true;
		}
		
		boolean inputSuccess = inputParameter(sp);
		
		// 쿼리 템플릿 내용 검사 (쿼리의 파라미터 개수와 파라미터 매핑 내용을 확인)
		if(inputSuccess) {
			ArrayList<QueryParameter> paramList = sp.getParamList();
			
			for(int i = 0; i < paramList.size(); i++) {
				boolean checkParam = sp.getQuery().contains(String.format("[param%d]", i+1));
				
				if(!checkParam) {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("StoredProcedure Parameter Mapping Error\n"));
					sb.append(String.format("Procedure XML File : %s%s%s\n\n", Util.colorBlue(sp.getFileName()), Util.separator, Util.separator));
					
//					sb.append(String.format("%s%s%s\n\n", Util.colorBlue(sp.getName()) + " 프로시저의 파라미터 매핑중 문제가 발생하였습니다", Util.separator, Util.separator));
					sb.append(String.format("%s%s%s\n\n", "There was a problem mapping the parameter contents of procedure " + Util.colorBlue(sp.getName()), Util.separator, Util.separator));
					
//					sb.append(String.format("해당 프로시저는 설정된 파라미터(&lt;param&gt; 태그) 개수가 %s개 입니다%s%s\n\n", Util.colorBlue(String.valueOf(paramList.size())) , Util.separator, Util.separator));
					sb.append(String.format("The procedure has %s parameters(&lt;param&gt; Tag) set%s%s\n\n", Util.colorBlue(String.valueOf(paramList.size())) , Util.separator, Util.separator));
					
//					sb.append(String.format("즉 프로시저의 수행 쿼리 내용에 %s 문자열이 포함되어 있어야 합니다%s%s\n\n", 
					sb.append(String.format("Therefore, the query content of the procedure must contain a %s string%s%s\n\n",							
							(paramList.size() == 1) ? Util.colorBlue(String.format("[param1]")) : Util.colorBlue(String.format("[param1] ~ [param%d]", paramList.size())), 
							Util.separator, Util.separator));
					
//					sb.append(String.format("하지만 현재 설정된 쿼리(&lt;query&gt; 태그) 내용에서 %s 내용을 찾을 수 없습니다%s%s\n\n\n",
					sb.append(String.format("However, I can't find the %s content in the currently set query(&lt;query&gt; Tag) content%s%s\n\n\n",		
							Util.colorRed(String.format("[param%d] ( %s )", i+1, paramList.get(i).getName())),
							Util.separator, Util.separator));
					
//					sb.append(String.format("%s\n", Util.colorGreen(String.format("%s 프로시저의 파라미터", sp.getName()))));
					sb.append(String.format("%s\n", Util.colorGreen(String.format("Parameters of procedure %s", sp.getName()))));
					for(int i2 = 0; i2 < paramList.size(); i2++) {
						QueryParameter param = paramList.get(i2);
						sb.append(String.format("%d. %s = %s\n",i2+1 ,String.format("[param%d]", i2+1),param.getName()));
					}
							
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);				
					return false;
				}				
			}
						
			// 입력 파라미터 매핑
			sp.setExecuteQuery(sp.getQuery()); // 쿼리 템플릿을 지정
			for(int i = 0; i < paramList.size(); i++) {
				sp.setExecuteQuery(sp.getExecuteQuery().replace(String.format("[param%d]", i+1), String.format("%s", paramList.get(i).getValue())));
			}
			
		}else {
			// 파라미터 입력에 실패하였을 경우
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
//						String str = String.format("%s%s 파라미터의 내용이 입력되지 않아 프로시저 실행에 실패하였습니다&nbsp;&nbsp;&nbsp;&nbsp;\n",Util.colorRed("Parameter input Error\n") ,Util.colorBlue(param.getName()));
						String str = String.format("%sThe procedure execution failed because the contents of parameter %s were not entered&nbsp;&nbsp;&nbsp;&nbsp;\n",Util.colorRed("Parameter input Error\n") ,Util.colorBlue(param.getName()));
						Util.showMessage(str , JOptionPane.ERROR_MESSAGE);
						return false;
					}
					
					// 입력받은 파라미터 내용에 문제가 없다면 저장
					param.setValue(paramValue);
				}
				
				return true;
				
			} else {
				 Util.showMessage("<font color='red'>Cancel the procedure</font>\nThe procedure has been canceled&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
				 return false;
			}
			
		} catch (Exception exception) {
			// 내부에서 일어나는 모든 예외를 처리
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
