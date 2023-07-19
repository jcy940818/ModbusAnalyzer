package src_ko.util;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import src_ko.info.ONION_Info;
import src_ko.info.Protocol;
import src_ko.swing.MainFrame;

public class FileUtil {
		
	static String ONION_PATH = null;
	static String PROJECT_PATH = null;
	
	public static File decompiler = null; 
	
	// Class 파일
	static File versionInfoClass = null;
	static File systemConfigClass = null;
	static File fmsProtocolClass = null;
	static File fmsMibClass = null;
	static File enumKo = null;
	static File enumEn = null;
	
	// Java 파일
	static File systemConfigJava = null;
	static File versionInfoJava = null;
	static File fmsProtocolJava = null;
	static File fmsMibJava = null;
	
	// Class Clone 파일
	static File versionInfoClone = null;
	static File systemConfigClone = null;
	static File fmsProtocolClone = null;
	static File fmsMibClone = null;
	
	public static ArrayList<Protocol> getProtocolList(boolean isProject) {			
		ArrayList<Protocol> protocolList = null;
		
		try {
			init(isProject);
			
			if(getMK119Version(versionInfoClone) <= 4.2) {
				decompileClass(decompiler, systemConfigClone);
				protocolList = Protocol.getProtocolList_42(systemConfigJava);
				deleteFiles(versionInfoJava, systemConfigJava);
			}else {
				decompileClass(decompiler, fmsProtocolClone);
				decompileClass(decompiler, fmsMibClone);
				protocolList = Protocol.getProtocolList_45(fmsProtocolJava, fmsMibJava, enumKo, enumEn);
				deleteFiles(versionInfoJava, fmsProtocolJava, fmsMibJava);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			deleteCloneFile();
			return protocolList;
		}
	}
	
	public static void download(boolean isProject) {		
		
		try {
			init(isProject);
			
			if(getMK119Version(versionInfoClone) <= 4.2) {
				decompileClass(decompiler, systemConfigClone);
				Protocol.download_SystemConfig(versionInfoJava, systemConfigJava, MainFrame.getCurrentPath());
				deleteFiles(versionInfoJava, systemConfigJava);
			}else {
				decompileClass(decompiler, fmsProtocolClone);
				decompileClass(decompiler, fmsMibClone);
				Protocol.download_FmsProtocol(versionInfoJava, fmsProtocolJava, fmsMibJava, enumKo, enumEn, MainFrame.getCurrentPath());
				deleteFiles(versionInfoJava, fmsProtocolJava, fmsMibJava);
			}

		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			deleteCloneFile();
		}
	}
	
	// 기존 클래스 파일과 기능 수행에 사용 될 클론 파일 로드 
	public static boolean init(boolean isProject) {		
		boolean ret = false;
		
		try {			
			// 디컴파일러 로드
			ret = initDecompiler();
			
			// OnionSoftware 디렉토리 프로토콜 리스트 로드
			ret = initClassFile(isProject);
			ret = initCloneFile();
			return ret;
			
		}catch(Exception e) {
			String path = (isProject) ? PROJECT_PATH : ONION_PATH;
			
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s입력하신 디렉토리 경로에서 기능 수행에 필요한 파일 로드에 실패하였습니다%s\n\n", Util.colorRed("기능 수행 불가능\n"), Util.separator + Util.separator));
			sb.append(String.format("Path : %s%s\n", Util.colorBlue(path), Util.separator + Util.separator));
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	// 디컴파일러 로드
	public static boolean initDecompiler() {
		// 디컴파일러
		String decompilerPath = MainFrame.getCurrentPath() + "\\config";
		decompiler = getDecompiler(new File(decompilerPath));
		
		if(decompiler != null) {
			System.out.println("Decompiler init Success : [ Path : " + decompilerPath + " ]");
			return true;			
		}else {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s아래의 경로에서 기능 수행에 필요한 실행 파일을 찾을 수 없습니다%s\n\n", Util.colorRed("기능 수행 불가능\n"), Util.separator + Util.separator));
			sb.append(String.format("Path : %s%s\n", Util.colorBlue(decompilerPath), Util.separator + Util.separator));
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return false;	
		}
	}
	

	public static boolean initClassFile(boolean isProject) {
		try {
			// OnionSoftware 디렉토리 경로
			ONION_PATH = ONION_Info.getOnionDirPath();
			PROJECT_PATH = ONION_Info.getProjectDirPath();
			
			if(!isProject && !isOnionDir(ONION_PATH)) {
				System.out.println("Not a OnionSoftware Directory : " + ONION_PATH);
				StringBuilder sb = new StringBuilder();
				sb.append(String.format("%s아래의 경로에서 기능 수행에 필요한 내용을 참조 할 수 없습니다%s\n\n", Util.colorRed("올바르지 않은 OnionSoftware 디렉토리 경로\n"), Util.separator + Util.separator));
				sb.append(String.format("Path : %s%s\n", Util.colorBlue(ONION_PATH), Util.separator + Util.separator));
				Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			// Class 파일
			if(isProject) {
				versionInfoClass = new File(PROJECT_PATH + ONION_Info.P_versionInfoClassPath_42);
				if(!versionInfoClass.exists()) versionInfoClass = new File(PROJECT_PATH + ONION_Info.P_versionInfoClassPath_45);
				
				systemConfigClass = new File(PROJECT_PATH + ONION_Info.P_systemConfigClassPath);
				fmsProtocolClass = new File(PROJECT_PATH + ONION_Info.P_fmsProtocolClassPath);
				fmsMibClass = new File(PROJECT_PATH + ONION_Info.P_fmsMibClassPath);
				enumKo = new File(PROJECT_PATH + ONION_Info.P_enumKoPath);
				enumEn = new File(PROJECT_PATH + ONION_Info.P_enumEnPath);
			}else {
				versionInfoClass = new File(ONION_PATH + ONION_Info.versionInfoClassPath);
				systemConfigClass = new File(ONION_PATH + ONION_Info.systemConfigClassPath);
				fmsProtocolClass = new File(ONION_PATH + ONION_Info.fmsProtocolClassPath);
				fmsMibClass = new File(ONION_PATH + ONION_Info.fmsMibClassPath);
				enumKo = new File(ONION_PATH + ONION_Info.enumKoPath);
				enumEn = new File(ONION_PATH + ONION_Info.enumEnPath);
			}
			
			// Java 파일
			systemConfigJava = new File(decompiler.getParentFile().getParent() + "\\SystemConfig.java");
			versionInfoJava = new File(decompiler.getParentFile().getParent() + "\\VersionInfo.java");
			fmsProtocolJava = new File(decompiler.getParentFile().getParent() + "\\FmsProtocol.java");
			fmsMibJava = new File(decompiler.getParentFile().getParent() + "\\FmsMib.java");
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// 기능 수행에 사용 될 클론 파일 생성
	public static boolean initCloneFile() {
		try {
			versionInfoClone = copyFile(versionInfoClass, new File(versionInfoClass.getAbsolutePath().replace(".class", "Clone.class")));
			systemConfigClone = copyFile(systemConfigClass, new File(systemConfigClass.getAbsolutePath().replace(".class", "Clone.class")));
			fmsProtocolClone = copyFile(fmsProtocolClass, new File(fmsProtocolClass.getAbsolutePath().replace(".class", "Clone.class")));
			fmsMibClone = copyFile(fmsMibClass, new File(fmsMibClass.getAbsolutePath().replace(".class", "Clone.class")));
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// 기능 수행에 사용 된 클론 파일 제거
	public static void deleteCloneFile() {
		try {
			if(versionInfoClone != null && versionInfoClone.exists()) deleteFile(versionInfoClone);
			if(systemConfigClone != null && systemConfigClone.exists()) deleteFile(systemConfigClone);
			if(fmsProtocolClone != null && fmsProtocolClone.exists()) deleteFile(fmsProtocolClone);
			if(fmsMibClone != null && fmsMibClone.exists()) deleteFile(fmsMibClone);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// (String) MK119 빌드 버전
	public static String getMK119BuildVersion(boolean isProject) throws Exception{
		boolean ret = init(isProject);
		
		if(!ret) {
			if(isProject) {
				return "MK119 Project Load Fail";
			}else {
				return "OnionSoftware Load Fail";	
			}
		}
		
		decompileClass(decompiler, versionInfoClone);
		HashMap<String, String> map = Protocol.getVersionInfoMap(FileUtil.getFileContent(versionInfoJava, "euc-kr"));
		
		deleteCloneFile();
		deleteFile(versionInfoJava);
		
		return map.get(ONION_Info.PRODUCT_VERSION_FULL) + map.get(ONION_Info.BUILD_DATE);
	}

	// (Double) MK119 버전
	public static double getMK119Version(File versionInfoClass) throws IOException{
		decompileClass(decompiler, versionInfoClass);
		String versionInfo = getFileContent(versionInfoJava, "euc-kr");
		HashMap<String,String> versionMap = Protocol.getVersionInfoMap(versionInfo);
		deleteFile(versionInfoClass);
		return Double.parseDouble(versionMap.get(ONION_Info.PRODUCT_VERSION_MAJOR) + "." + versionMap.get(ONION_Info.PRODUCT_VERSION_MINOR));
	}
	
	
	// 클래스 파일 디컴파일
	public static void decompileClass(File decompiler, File classFile, String... options) throws IOException {
		
		if(!decompiler.exists()) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s아래의 경로에서 기능 수행에 필요한 실행 파일을 찾을 수 없습니다%s\n\n", Util.colorRed("기능 수행 불가능\n"), Util.separator + Util.separator));
			sb.append(String.format("Path : %s\n\n", decompiler.getParent()));
			sb.append(String.format("File : %s\n%s", Util.colorBlue(decompiler.getName()),  Util.separator + Util.separator));			
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(!classFile.exists()) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s아래의 경로에서 기능 수행에 필요한 클래스 파일을 찾을 수 없습니다%s\n\n", Util.colorRed("기능 수행 불가능\n"), Util.separator + Util.separator));
			sb.append(String.format("Path : %s\n\n", classFile.getParent()));
			sb.append(String.format("File : %s\n%s", Util.colorBlue(classFile.getName()),  Util.separator + Util.separator));			
			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(decompiler.getAbsolutePath());
		list.add("-o");
		list.add("-8");
		list.add("-sjava");

		if(options != null && options.length > 0) {
			for(String option : options) {
				list.add(option);
			}
		}
		
		list.add(classFile.getAbsolutePath());
		
		Process process = new ProcessBuilder(list).start();
		
		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		String line = null;
		while((line = input.readLine()) != null) { System.out.println("Decompile Process : " + line); }
		while((line = error.readLine()) != null) { System.out.println("Decompile Process : " + line); }
		
		try {
			process.waitFor(30, TimeUnit.SECONDS);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			input.close();
			error.close();
			process.destroy();
		}
	}
	
	// 유니코드 문자열을 한글 문자열로 변환
	public static String convertString(String uniCode) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < uniCode.length(); i++) {
			if ('\\' == uniCode.charAt(i) && 'u' == uniCode.charAt(i + 1)) {
				Character r = (char) Integer.parseInt(uniCode.substring(i + 2, i + 6), 16);
				sb.append(r);
				i += 5;
			} else {
				sb.append(uniCode.charAt(i));
			}
		}
		return sb.toString();
	}
	
	// content를 내용으로 가지는 파일 생성 후 리턴
	public static File createFile(File file, String content) throws IOException{
		if(!file.exists()) file.createNewFile();
		FileWriter out = new FileWriter(file);
		out.write(content);				
		out.flush();
		out.close();
		return file;
	}
	
	// 인자로 넘겨준 파일을 오픈한다
	public static void openFile(File file) {
		try {
			Desktop.getDesktop().open(file);			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 인자로 넘겨준 파일을 편집 모드로 오픈한다
	public static void editFile(File file) {
		try {
			Desktop.getDesktop().edit(file);			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 인자로 넘겨준 파일의 존재 여부를 검사 후 존재한다면 삭제한다
	public static void deleteFile(File file) {
		try {
			if(file != null && file.exists()) {
				file.delete();
				System.out.println("File Delete Done : " + file.getName() + " [ Path=" + file.getParent() + " ]");
			}else {
				System.out.println("File Delete Fail : " + file.getName() + " [ Path=" + file.getParent() + " ]");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 인자로 넘겨준 파일들을 삭제한다
	public static void deleteFiles(File... files) {
		try {
			for(File file : files) {
				deleteFile(file);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// origin 파일이 존재한다면 Clone 파일을 생성한다
	public static File copyFile(File originFile, File cloneFile){
		try {
			
			if(originFile != null && originFile.exists()) {
				if(!cloneFile.exists()) cloneFile.createNewFile();
				Files.copy(originFile.toPath(), cloneFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				
				System.out.printf("File Copy Done : [ %s ] => [ %s ] [ Path = %s ]\n", originFile.getName(), cloneFile.getName(), cloneFile.getParent());
				return cloneFile;
			}else {
				System.out.printf("File Copy Fail : Can Not Found Origin File [ %s ] [ Path = %s ]\n", originFile.getName(), originFile.getParent());
				return null;
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;		
	}
	
	// 파일 내용 리턴
	public static String getFileContent(File file, String encoding) throws IOException{
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));				
		int ch;
		while ((ch = reader.read()) != -1) {
			sb.append((char) ch);
		}
		reader.close();
		return sb.toString();
	}
	
	// 디렉토리 경로상에 첫번째 실행 파일을 리턴 (모드버스 어날라이저는 제외)
	public static File getDecompiler(File dir) {
		File[] files = dir.listFiles();
		
		for(File file : files) {
			// 디렉토리가 아니라 파일인 항목만 검사
			if(file.isFile()) { 
				String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);				
				if(ext.equalsIgnoreCase("exe")) {					
					if(file.getName().contains("Modbus") || file.getName().contains("Analyzer")) {
						continue;
					}else {
						return file;
					}
				}
			}
		}
		return null;
	}

	// 파라미터로 넘긴 디렉토리 경로가 OnionSoftware 디렉토리가 맞는지 리턴
	public static boolean isOnionDir(String path) {
		File file = new File(path + "\\midknight\\tomcat\\webapps\\midknight\\WEB-INF\\classes\\com\\onion\\mk119s\\conf");
		return file.exists();
	}
	
	// 파라미터로 넘긴 경로상에 OnionSoftware 디렉토리 존재 여부 리턴
	public static boolean hasOnionDir(String path) {
		File dir = new File(path);
		
		if (!dir.exists()) {
			return false;
		}else {
			for(File OnionSoftware : dir.listFiles()) {				
				if(OnionSoftware.isDirectory() && OnionSoftware.getName().contains("OnionSoftware")) {
					// file : OnioSoftrware dir
					
					for(File midknight : OnionSoftware.listFiles()) {
						if(midknight.isDirectory() && midknight.getName().contains("midknight")) {
							// file2 : midknight dir
							
							for(File tomcat : midknight.listFiles()) {
								if(tomcat.isDirectory() && tomcat.getName().contains("tomcat")) {
									// file3 : tomcat dir
									return true;
								}
							}
							
						}
					}
					
				}else {
					continue;
				}
			}// end for-loof
		}
		
		return false;
	}
	
	public static ArrayList<File> getDirFiles(File dir){
		ArrayList<File> fileList = new ArrayList<File>();
		
		File[] dirFiles = dir.listFiles();
		
		for(File file : dirFiles) {
			if(file.isDirectory()) {
				ArrayList<File> subDirFileList = getDirFiles(file);
				for(File subFile : subDirFileList) {
					fileList.add(subFile);
				}
			}else {
				fileList.add(file);
			}
		}
		
		return fileList;
	}
	
	public static String makeDirTree(String basePath, String path) {

		path = path
			.replace(":", "")
			.replace("/", "")
			.replace("*", "")
			.replace("?", "")
			.replace("<", "")
			.replace(">", "")
			.replace("|", "");
		
		File tree = new File(basePath + "\\" + path);
		if(!tree.exists()) tree.mkdirs();
		return tree.getPath();
	}
	
	public static ArrayList<File> getDiffFiles(File[] before, File[] after){
		ArrayList<File> diffFileList = new ArrayList<File>();
		HashMap<String, File> diffFileMap = new HashMap<String, File>();
		
		for(File file : after) {
			diffFileList.add(file);
			diffFileMap.put(file.getParent() + "=" + file.getName(), file);
		}
		
		for(File file : before) {
			String key = file.getParent() + "=" + file.getName();
			if(diffFileMap.containsKey(key)) {
				File sameFile = diffFileMap.get(key);
				diffFileList.remove(file);
			}
		}
		
		return diffFileList;
	}
	
}
