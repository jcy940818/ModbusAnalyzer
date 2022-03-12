package src_ko.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import src_ko.util.Util;

public class CommandFrame extends JFrame{
	
	public static boolean isExist = false;
	private static JTextArea cmd;
	
	public static void main(String[] args){
			
		new CommandFrame();
		
		try {
			execute(null);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public CommandFrame() {		
		CommandFrame.isExist = true;
		
		setTitle("ModbusAnalyzer");
		setBounds(100, 100, 1080, 680);
		setIconImage(new Util().getIconResource().getImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JPanel actualPanel = new JPanel();
		actualPanel.setBounds(0, 0, 10, 10);
		getContentPane().add(actualPanel);
		actualPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		actualPanel.add(scrollPane);
		
		cmd = new JTextArea();
		cmd.setTabSize(8);			
		cmd.setBackground(Color.BLACK);
		cmd.setForeground(Color.WHITE);
		cmd.setFont(new Font("굴림체", Font.PLAIN, 17));
		cmd.setEditable(false);
		scrollPane.setViewportView(cmd);
		
		// 프레임 포커싱 상태에서 "ESC" 버튼을 클릭하면 프레임이 닫힌다
		MyKeyListener listener = new MyKeyListener();
		cmd.addKeyListener(listener);
		addKeyListener(listener);
		
		setVisible(true);
		setLocationRelativeTo(null); // 프레임 생성자의 마지막에 넣어줘야 프레임이 화면 가운데에서 실행된다.
	}
	
	
	@Override
	public void dispose() {
		CommandFrame.isExist = false;
		super.dispose();
	}
	
	
	public static void write(JTextArea cmd, String content) {
		cmd.append(content);
	}
	
	
	public static void clear(JTextArea cmd) {
		cmd.setText(null);
	}

	
	public static void execute(ArrayList<String> param) throws IOException{
		ArrayList<String> list = new ArrayList<String>();
		list.add("cmd.exe");
		list.add("/C");
		
		list.add("ipconfig");		

		Process process = new ProcessBuilder(list).start();
		
		StringBuilder sb = new StringBuilder();
		for(String s : list) {
			sb.append(s + " ");
		}
		write(cmd, "\nModbusAnalyzer>" + sb.toString() + "\n");
		
		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		String line = null;
		
		while((line = input.readLine()) != null) {
			write(cmd, line + "\n");
			System.out.println("Process InputStream : " + line); 
			}
		
		while((line = error.readLine()) != null) { 
			write(cmd, line + "\n");
			System.out.println("Process ErrorStream : " + line); 
			}
		
		try {
			process.waitFor(10, TimeUnit.SECONDS);			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			input.close();
			error.close();
			process.destroy();
		}
	}
	
	
	
	//사용자 정의 키 이벤트 리스너
	class MyKeyListener extends KeyAdapter{
		public void keyPressed(KeyEvent e) {			
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {				
				dispose();
			}
		}		
		
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				dispose();
			}
		}		
	}
		
}


