package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import common.util.FontManager;
import common.util.TableUtil;
import src_ko.util.FileUtil;
import src_ko.util.Util;

public class DecompilePanel extends JPanel {
	
	public ArrayList<File> fileList = new ArrayList<File>();
	public HashMap<String, File> fileMap = new HashMap<String, File>();
	
	private JTextField classFilePath_TextField;
	private JButton addClassButton;
	private JButton deleteClassButton;
	
	private JButton runDecompile;
	private JButton goDecompilePath;
	private JLabel decompile;
	private JTable fileTable;
	private JLabel label;
	private JTextField search_TextField;
	private JLabel fileCount;
	
	public DecompilePanel() {
		// size : 1074, 628
		setBorder(new EmptyBorder(12, 12, 12, 12));
		setSize(1074, 628);
		setBackground(new Color(255, 140, 0));
		setLayout(new BorderLayout(0, 0));
		setVisible(true);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setSize(1050, 610);
		actualPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		actualPanel.setBackground(Color.WHITE);		
		add(actualPanel, BorderLayout.CENTER);
		actualPanel.setLayout(null);
		
		decompile = new JLabel("Class Decompile");
		decompile.setIcon(new Util().getSubLogoResource());
		decompile.setForeground(Color.BLACK);
		decompile.setFont(FontManager.getFont(Font.BOLD, 26));
		decompile.setBounds(6, 8, 270, 47);
		actualPanel.add(decompile);
		
		fileCount = new JLabel("0");
		fileCount.setForeground(Color.BLUE);
		fileCount.setFont(FontManager.getFont(Font.BOLD, 26));
		fileCount.setBounds(278, 8, 270, 47);
		actualPanel.add(fileCount);
		
		JLabel classFile = new JLabel("Class File");
		classFile.setHorizontalAlignment(SwingConstants.RIGHT);
		classFile.setForeground(Color.BLACK);
		classFile.setFont(FontManager.getFont(Font.BOLD, 22));
		classFile.setBounds(15, 84, 100, 41);
		actualPanel.add(classFile);
		
		JLabel searchClass = new JLabel("Search");
		searchClass.setHorizontalAlignment(SwingConstants.RIGHT);
		searchClass.setForeground(Color.BLACK);
		searchClass.setFont(FontManager.getFont(Font.BOLD, 22));
		searchClass.setBounds(15, 136, 100, 35);
		actualPanel.add(searchClass);
		
		classFilePath_TextField = new JTextField();
		classFilePath_TextField.setHorizontalAlignment(SwingConstants.LEFT);		
		classFilePath_TextField.setFont(FontManager.getFont(Font.PLAIN, 18));
		classFilePath_TextField.setBorder(new LineBorder(Color.BLACK, 2));
		classFilePath_TextField.setBounds(123, 86, 825, 41);
		classFilePath_TextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addClassButton.doClick();
			}
		});
		
		label = new JLabel("클래스 파일 경로에 한글 문자는 포함 할 수 없습니다");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setForeground(Color.RED);
		label.setFont(FontManager.getFont(Font.PLAIN, 13));
		label.setBounds(128, 60, 457, 22);
		actualPanel.add(label);
		
		classFilePath_TextField.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					
					for (File file : droppedFiles) {
						
						if(file != null && file.exists()) {							
							if(file.isDirectory()) {
								ArrayList<File> dirFileList = FileUtil.getDirFiles(file);
								for(File dirFile : dirFileList) {
									if(checkClassFile(dirFile)) addFile(dirFile);
								}
							}else {								
								if(droppedFiles.size() == 1) classFilePath_TextField.setText(file.getAbsolutePath()); // 파일이 하나만 드랍되었을 경우 텍스트 필드에 경로를 표시
								if(checkClassFile(file)) addFile(file);
							}
							
						}
					}
					
					doTableFilter();
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(classFilePath_TextField);
		
		addClassButton = new JButton("추 가");
		addClassButton.setForeground(Color.BLACK);
		addClassButton.setBackground(Color.WHITE);
		addClassButton.setFont(FontManager.getFont(Font.BOLD, 16));
		addClassButton.setBounds(860, 138, 85, 35);
		addClassButton.setFocusPainted(false);
		addClassButton.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				String path = classFilePath_TextField.getText().trim();
				File classFile = new File(path);
				
				if(classFile.exists()) {
										
					if(fileMap.containsKey(classFile.getParent() + "=" + classFile.getName())) {
						try {
							File file = fileMap.get(classFile.getParent() + "=" + classFile.getName());
							int index = fileList.indexOf(file);
							
							File tableFile = fileMap.get(fileTable.getValueAt(index, 1).toString() + "=" + fileTable.getValueAt(index, 2).toString());
							
							StringBuilder sb = new StringBuilder();
							sb.append(Util.colorRed("중복되는 클래스 파일") + Util.separator + Util.separator + "\n");
							
							if(file.equals(tableFile)) {
								sb.append("입력하신 클래스 파일은 테이블 " + Util.colorBlue(String.valueOf(index + 1)) + "번째 행에 이미 존재합니다" + Util.separator + Util.separator + "\n\n");	
							}else {
								sb.append("입력하신 클래스 파일은 테이블에 이미 존재합니다" + Util.separator + Util.separator + "\n\n");
							}
							
							sb.append("File : " + classFile.getAbsoluteFile().toString().replace("\\", Util.colorBlue("\\")));
							sb.append(Util.separator + Util.separator + "\n");
							Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
							return;
							
						}catch(ArrayIndexOutOfBoundsException ex) {
							ex.printStackTrace();
							StringBuilder sb = new StringBuilder();
							sb.append(Util.colorRed("중복되는 클래스 파일") + Util.separator + Util.separator + "\n");
							sb.append("입력하신 클래스 파일은 테이블에 이미 존재합니다" + Util.separator + Util.separator + "\n\n");
							sb.append("File : " + classFile.getAbsoluteFile().toString().replace("\\", Util.colorBlue("\\")));
							sb.append(Util.separator + Util.separator + "\n");
							Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
							return;
						}
					}
					
					if(classFile.getAbsolutePath().endsWith(".class")) {
						
						// 정상적인 클래스 파일
						addFile(classFile);
						doTableFilter();
						
					}else {
						StringBuilder sb = new StringBuilder();
						sb.append(Util.colorRed("올바르지 않은 클래스 파일") + Util.separator + Util.separator + "\n");
						sb.append("입력하신 파일은 클래스 파일(.class)이 아닙니다" + Util.separator + Util.separator + "\n\n");
						sb.append("File : " + classFile.getAbsoluteFile().toString().replace("\\", Util.colorRed("\\")));
						sb.append(Util.separator + Util.separator + "\n");
						Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
						return;
					}
				}else {
					StringBuilder sb = new StringBuilder();
					sb.append(Util.colorRed("존재하지 않는 클래스 파일") + Util.separator + Util.separator + "\n");
					sb.append("입력하신 파일은 존재하지 않는 클래스 파일입니다" + Util.separator + Util.separator + "\n\n");
					sb.append("File : " + classFile.getAbsoluteFile().toString().replace("\\", Util.colorRed("\\")));
					sb.append(Util.separator + Util.separator + "\n");
					Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
			}
		});
		actualPanel.add(addClassButton);
		
		search_TextField = new JTextField();
		search_TextField.setHorizontalAlignment(SwingConstants.LEFT);
		search_TextField.setFont(FontManager.getFont(Font.PLAIN, 18));
		search_TextField.setBorder(new LineBorder(Color.BLACK, 2));
		search_TextField.setBounds(123, 138, 732, 35);
		search_TextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			public void keyReleased(KeyEvent e) {
				try {
					doTableFilter();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(search_TextField);
		
		
		deleteClassButton = new JButton("삭 제");
		deleteClassButton.setForeground(Color.BLACK);
		deleteClassButton.setFont(FontManager.getFont(Font.BOLD, 16));
		deleteClassButton.setFocusPainted(false);
		deleteClassButton.setBackground(Color.WHITE);
		deleteClassButton.setBounds(953, 138, 85, 35);
		deleteClassButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteFile();
			}
		});
		actualPanel.add(deleteClassButton);
		
		JButton searchButton = new JButton("찾 기");
		searchButton.setForeground(Color.BLACK);
		searchButton.setFont(FontManager.getFont(Font.BOLD, 16));
		searchButton.setFocusPainted(false);
		searchButton.setBackground(Color.WHITE);
		searchButton.setBounds(953, 86, 85, 41);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = Util.getFilePath();
				if(path != null) {
					classFilePath_TextField.setText(path);
				}
			}
		});
		actualPanel.add(searchButton);
		
		runDecompile = new JButton("Run Decompile");
		runDecompile.setForeground(new Color(0, 128, 0));
		runDecompile.setFocusPainted(false);
		runDecompile.setBackground(Color.WHITE);
		runDecompile.setFont(FontManager.getFont(Font.BOLD, 20));
		runDecompile.setBounds(602, 8, 190, 55);
		runDecompile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					ArrayList<File> selectedFile = getSelectedFile();
					
					if(selectedFile == null || selectedFile.size() < 1) return;
					
					StringBuilder msg = new StringBuilder(Util.colorGreen("Run Decompile") + Util.separator + Util.separator + "\n");
					msg.append("선택된 클래스 파일 " + Util.colorBlue(String.valueOf(selectedFile.size())) + "개" + Util.separator + Util.separator + "\n\n");
					msg.append("디컴파일 작업을 수행 하시겠습니까?" + Util.separator + Util.separator + "\n");					
					
					if(Util.showConfirm(msg.toString()) != JOptionPane.YES_OPTION) return;
					
					String decompilePath = MainFrame.getCurrentPath() + "\\decompile";
					File decompileDir = new File(decompilePath);
					if(!decompileDir.exists()) decompileDir.mkdir();
					
					if(FileUtil.decompiler == null) {
						if(!FileUtil.initDecompiler()) {
							return;
						}
					}
					
					for(File classFile : selectedFile) {
						if(classFile == null || !classFile.exists()) continue;
						
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									FileUtil.decompileClass(FileUtil.decompiler, classFile, new String[]{"-d", decompilePath});
								}catch(Exception ex1) {
									ex1.printStackTrace();
								}
							}
						}).start();
					}
					
				}catch(Exception ex2) {
					ex2.printStackTrace();
				}
				
			}
		});
		actualPanel.add(runDecompile);
		
		goDecompilePath = new JButton("Decompile Path");
		goDecompilePath.setHorizontalAlignment(SwingConstants.LEFT);
		goDecompilePath.setForeground(Color.BLACK);
		goDecompilePath.setFocusPainted(false);
		goDecompilePath.setBackground(Color.WHITE);
		goDecompilePath.setFont(FontManager.getFont(Font.BOLD, 20));
		goDecompilePath.setBounds(800, 8, 240, 55);
		goDecompilePath.setIcon(new Util().getFolder2Image());
		goDecompilePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File decompileDir = new File(MainFrame.getCurrentPath() + "\\decompile");
				if(!decompileDir.exists()) decompileDir.mkdir();
				FileUtil.openFile(decompileDir);
			}
		});
		actualPanel.add(goDecompilePath);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(8, 177, 1034, 420);
		scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
		scrollPane.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					
					for (File file : droppedFiles) {
						
						if(file != null && file.exists()) {							
							if(file.isDirectory()) {
								ArrayList<File> dirFileList = FileUtil.getDirFiles(file);
								for(File dirFile : dirFileList) {
									if(checkClassFile(dirFile)) addFile(dirFile);
								}
							}else {
								if(checkClassFile(file)) addFile(file);
							}
							
						}
					}
					
					doTableFilter();
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		actualPanel.add(scrollPane);
		
		fileTable = new JTable();
		fileTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 1) {
					// 왼쪽 클릭
					
				} 
				if (e.getButton() == 1 && e.getClickCount() == 2) {
					// 왼쪽 버튼 더블 클릭
					String decompilePath = MainFrame.getCurrentPath() + "\\decompile";
					File decompileDir = new File(decompilePath);
					if(!decompileDir.exists()) decompileDir.mkdir();
					
					int row = fileTable.getSelectedRow();
					String key = fileTable.getValueAt(row, 1).toString() + "=" + fileTable.getValueAt(row, 2).toString();
					File file = fileMap.get(key);
					
					if(file != null) {
						try {
							if(FileUtil.decompiler == null) {
								if(!FileUtil.initDecompiler()) {
									return;
								}
							}
							
							File[] before = new File(MainFrame.getCurrentPath()).listFiles();
							
							FileUtil.decompileClass(FileUtil.decompiler, file);
							
							File[] after = new File(MainFrame.getCurrentPath()).listFiles();
							
							ArrayList<File> diff = FileUtil.getDiffFiles(before, after);
							
							if(diff == null || diff.size() < 1) {
								StringBuilder sb = new StringBuilder();
								sb.append(Util.colorRed("클래스 디컴파일 실패") + Util.separator + Util.separator + "\n");
								sb.append("요청하신 클래스 파일은 디컴파일 작업을 수행 할 수 없습니다" + Util.separator + Util.separator + "\n\n");
								sb.append("File : " + file.getAbsoluteFile().toString().replace("\\", Util.colorBlue("\\")));
								sb.append(Util.separator + Util.separator + "\n");
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
								return;
							}
							
							for(File f : diff) {
								if(f.getAbsolutePath().toLowerCase().endsWith(".java") && f.exists()) {
									File copyJavaFile = new File(decompilePath + "\\" + f.getName());
									copyJavaFile = FileUtil.copyFile(f, copyJavaFile);
									
									if(copyJavaFile.exists()) {
										FileUtil.deleteFile(f);
										FileUtil.openFile(copyJavaFile);
									}
								}
							}
							
						}catch(Exception te) {
							te.printStackTrace();
						}
					}
					
				}
				if (e.getButton() == 3) {
					String decompilePath = MainFrame.getCurrentPath() + "\\decompile";
					File decompileDir = new File(decompilePath);
					if(!decompileDir.exists()) decompileDir.mkdir();
					
					int row = fileTable.getSelectedRow();
					String key = fileTable.getValueAt(row, 1).toString() + "=" + fileTable.getValueAt(row, 2).toString();
					File file = fileMap.get(key);
					
					if(file != null) {
						try {
							if(FileUtil.decompiler == null) {
								if(!FileUtil.initDecompiler()) {
									return;
								}
							}
							
							File[] before = new File(MainFrame.getCurrentPath()).listFiles();
							
							FileUtil.decompileClass(FileUtil.decompiler, file);
							
							File[] after = new File(MainFrame.getCurrentPath()).listFiles();
							
							ArrayList<File> diff = FileUtil.getDiffFiles(before, after);
							
							if(diff == null || diff.size() < 1) {
								StringBuilder sb = new StringBuilder();
								sb.append(Util.colorRed("클래스 디컴파일 실패") + Util.separator + Util.separator + "\n");
								sb.append("요청하신 클래스 파일은 디컴파일 작업을 수행 할 수 없습니다" + Util.separator + Util.separator + "\n\n");
								sb.append("File : " + file.getAbsoluteFile().toString().replace("\\", Util.colorBlue("\\")));
								sb.append(Util.separator + Util.separator + "\n");
								Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
								return;
							}
							
							for(File f : diff) {
								if(f.getAbsolutePath().toLowerCase().endsWith(".java") && f.exists()) {
									File copyJavaFile = new File(decompilePath + "\\" + f.getName());
									copyJavaFile = FileUtil.copyFile(f, copyJavaFile);
									
									if(copyJavaFile.exists()) {
										FileUtil.deleteFile(f);
									}
								}
							}
							
						}catch(Exception te) {
							te.printStackTrace();
						}
					}
					
				}
			}
		});
		fileTable.setBackground(Color.WHITE);
		fileTable.setCellSelectionEnabled(true);
		
		scrollPane.setViewportView(fileTable);
		resetTable(fileTable, null);
	}
	
	public void resetTable(JTable table, Object[][] content){
		String[] header = new String[] {
				"순 서",
				"클래스 파일 경로",
				"클래스 파일",				
			};
		
		table.setModel(new DefaultTableModel(content, header) {
				boolean[] columnEditables = new boolean[] {
						false, // 순 서 : 수정 불가
						false, // 모드버스 포인트 : 수정 불가
						false, // 기능코드 : 수정 불가						
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
		});
		
		setTableStyle(table);
	}
	
	public static void setTableStyle(JTable table) {
		// 이동 불가, 셀 크기 조절 불가
		table.getTableHeader().setBackground(new Color(255, 255, 153));
		table.getTableHeader().setForeground(Color.BLACK);
		table.getTableHeader().setFont(FontManager.getFont(Font.BOLD, 16));
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(true);
		
		// 테이블 셀 설정
		table.setBorder(new EmptyBorder(0, 3, 0, 0));
		table.setRowMargin(3);
		table.setFont(FontManager.getFont(Font.PLAIN, 15));
		table.setRowHeight(25);
		
		// 테이블 셀 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(30); // 순 서
		table.getColumnModel().getColumn(1).setPreferredWidth(600); // 파일 경로
		table.getColumnModel().getColumn(2).setPreferredWidth(300); // 파일 이름
		
		// DefaultTableCellHeaderRenderer 생성 (가운데 정렬을 위한)
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		
		// DefaultTableCellHeaderRenderer의 정렬을 가운데 정렬로 지정
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		// 정렬할 테이블의 ColumnModel을 가져옴
		TableColumnModel tcmSchedule = table.getColumnModel();
		
		tcmSchedule.getColumn(0).setCellRenderer(tScheduleCellRenderer); // 순 서
//		tcmSchedule.getColumn(1).setCellRenderer(tScheduleCellRenderer); // 파일 경로
//		tcmSchedule.getColumn(2).setCellRenderer(tScheduleCellRenderer); // 파일 이름
	}
	
	private boolean checkClassFile(File file) {
		if(!file.exists()) {			
			return false;
		}
		
		if(fileMap.containsKey(file.getParent() + "=" + file.getName())) {
			return false;
		}
		
		if(file.getAbsolutePath().toLowerCase().endsWith(".class")) {
			return true;
		}else {
			return false;
		}
	}
	
	private void addFile(File file) {
		fileList.add(file);
		fileMap.put(file.getParent() + "=" + file.getName(), file);
	}
	
	private void deleteFile(){
		int[] selectedRow = fileTable.getSelectedRows();
		
		if(selectedRow == null || selectedRow.length < 1) return;
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s", Util.colorGreen("Delete Class File")) + Util.separator + Util.separator + "\n");
		sb.append(String.format("선택하신 클래스 파일 %s개를 삭제 하시겠습니까?", Util.colorBlue(String.valueOf(selectedRow.length))));
		sb.append(Util.separator + Util.separator + "\n");
		
		if(Util.showConfirm(sb.toString()) != JOptionPane.OK_OPTION) return;
		
		for(int i : selectedRow) {
			String key = fileTable.getValueAt(i, 1).toString() + "=" + fileTable.getValueAt(i, 2).toString();
			File file = fileMap.get(key);
			fileList.remove(file);
		}
		
		fileMap.clear();
		for(File file : fileList) {
			fileMap.put(file.getParent() + "=" + file.getName(), file);
		}
		
		doTableFilter();
	}
	
	public void updateTable(JTable table, ArrayList<File> fileList) {
		resetTable(table, null);
		
		try {
			Vector record;
			
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			for(int i = 0; i < fileList.size(); i++) {
				
				File classFile = fileList.get(i);
				record = new Vector();
				
				/* column[0] */ record.add(i+1); // 순 서
				/* column[1] */ record.add(classFile.getParent()); // 파일 경로
				/* column[2] */ record.add(classFile.getName());  // 파일 이름
				
				model.addRow(record);
			}
			
			fileCount.setText(String.valueOf(this.fileList.size()));
			
			if(this.fileList != null) {
				int total = this.fileList.size();
				int searched = table.getRowCount();
				String text = String.format("클래스 파일 이름 ( %d / %d )", searched, total);
				TableUtil.setTableHeader(table, 2, text);
			}else {
				TableUtil.setTableHeader(table, 2, "클래스 파일 이름");
			}
			
		}catch(Exception e) {
			// 레코드 추가 중 예외 발생 시 아무것도 수행하지 않음
			e.printStackTrace();
		}
	}
	
	public void doTableFilter() {
		if(search_TextField == null) return;

		ArrayList<File> filterList = new ArrayList<File>();
		String text = search_TextField.getText();

		boolean noSearch = (text == null || text.length() == 0 || text.equals(""));

		if(noSearch) {
			updateTable(fileTable, fileList);
			return;
			
		}else{
			text = text.toLowerCase().trim();
			
		}

		for(int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);
			boolean isContain = false;
			
			String searchFilePath = file.getAbsolutePath().toLowerCase();

			if(text.contains(",")) {
				String[] textToken = text.split(",");
				for (int i2 = 0; i2 < textToken.length; i2++) {
					String token = textToken[i2].trim();
					if (searchFilePath.contains(token)) isContain = true;
				}
			}else{
				if (searchFilePath.contains(text)) isContain = true;
			}
				
			if(isContain) filterList.add(file);
			
		} // for loop
		
		updateTable(fileTable, filterList);
	}
	
	public ArrayList<File> getSelectedFile(){
		ArrayList<File> selectedFiles = new ArrayList<File>();
		
		int[] selectedRow = fileTable.getSelectedRows();
		
		for(int i : selectedRow) {
			String key = fileTable.getValueAt(i, 1).toString() + "=" + fileTable.getValueAt(i, 2).toString();
			File file = fileMap.get(key);
			selectedFiles.add(file);
		}
		
		return selectedFiles;
	}
				
}
