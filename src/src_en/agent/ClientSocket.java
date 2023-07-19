package src_en.agent;

import java.awt.Font;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import common.util.FontManager;
import src_en.swing.ModbusAgent_Panel;
import src_en.util.Util;

public class ClientSocket extends Socket {
				
	public static final String SOCKET_STATUS_BEFORE_CONNECTING = "Before Connecting";
	public static final String SOCKET_STATUS_CONNECTED = "Connected";
	public static final String SOCKET_STATUS_CONNECTING = "Connecting";
	public static final String SOCKET_STATUS_COMMUNICATING = "Communicating";
	public static final String SOCKET_STATUS_COMMUNICATION_ERROR = "Communication Error";
	public static final String SOCKET_STATUS_CONNECTION_CLOSED = "Connection Closed";
	public static final String SOCKET_STATUS_CONNECTION_FAILED = "Connection Failed";
	public static final String SOCKET_STATUS_PING_FAILED = "Ping Failed";
	public static final String SOCKET_STATUS_WAITING_RESPONSE = "Waiting Response";
	public static final String SOCKET_STATUS_CONNECTION_IS_CUT_OFF = "Connection is Cut Off";
	
	private static Font boldfont = FontManager.getFont(Font.BOLD, 17);
	
	private static Socket clientSocket = null; 
	
	// 클라이언트 소켓 : 연결 정보
	public static boolean isFirst = true;
	public static String ip = "0.0.0.0";
	public static int port = 0;
		
	// 클라이언트 소켓 : 마지막 연결 정보	
	public static boolean hasLastConnectionInfo = false;
	
	// 클라이언트 소켓 : 응답 타임아웃 관련
	private static int CURRENT_TIMEOUT_COUNT = 0; // 현재 타임아웃 카운트
	private static final int LIMIT_TIMEOUT_COUNT = 3; // 응답 타임아웃 카운트 임계 값
	private static final int CONNECT_TIMEOUT = 5000; // 장비 접속 응답 타임아웃 (밀리초 s)
	public static final int RESPONSE_TIMEOUT = 5000; // 응답 대기 응답 타임아웃 (밀리초 s)
	
	// 클라이언트 소켓 : 상태 관련
	public static int state = 0; // 현재 클라이언트 소켓 상태 : 접속 전
	public static final int NODE_CONDITION_INIT = 0; // 접속 전
	public static final int NODE_CONDITION_CONNSUCCESS = 1; // 접속성공
	public static final int NODE_CONDITION_CONNECTING = 2; // 접속중
	public static final int NODE_CONDITION_REGULAR = 3; // 통신중
	public static final int NODE_CONDITION_COMMERR = 4; // 통신오류
	public static final int NODE_CONDITION_CONNCLOSE = 5; // 접속종료
	public static final int NODE_CONDITION_CONNFAIL = 6; // 접속실패
	public static final int NODE_CONDITION_SYSTEM_DOWN = 7; // ping 실패
	public static final int NODE_CONDITION_RESPONSE_WAITING = 8; // 응답 대기중
	public static final int NODE_CONDITION_DISCONNECTED = 9; // 접속 끊김
	
	// 2021-01-14 : 패킷로그를 파일로 저장할 때 사용하려고 만든건데 굳이 파일로 저장 할 필요가 있을까 싶어서 일단 보류
	// 클라이언트 소켓 : 세션동안 서버와 통신한 기록 (패킷로그) 
	public static StringBuilder packetLogContent;
	public static StringBuilder packetLogContent_temp;
	
	// 클라이언트 소켓 :상태 반환
	public static String getStringState(int state) {
		String currentState = "";		
		
		switch(state) {
//		case 0 : currentState = "접속 전"; break;
		case 0 : currentState = ClientSocket.SOCKET_STATUS_BEFORE_CONNECTING; break;
		
//		case 1 : currentState = "접속성공"; break;
		case 1 : currentState = ClientSocket.SOCKET_STATUS_CONNECTED; break;
		
		
//		case 2 : currentState = "접속중"; break;
		case 2 : currentState = ClientSocket.SOCKET_STATUS_CONNECTING; break;
		
		
//		case 3 : currentState = ClientSocket."통신중"; break;
		case 3 : currentState = ClientSocket.SOCKET_STATUS_COMMUNICATING; break;
		
		
//		case 4 : currentState = ClientSocket."통신오류"; break;
		case 4 : currentState = ClientSocket.SOCKET_STATUS_COMMUNICATION_ERROR; break;
		
		
//		case 5 : currentState = ClientSocket."접속종료"; break;
		case 5 : currentState = ClientSocket.SOCKET_STATUS_CONNECTION_CLOSED; break;
		
		
//		case 6 : currentState = ClientSocket."접속실패"; break;
		case 6 : currentState = ClientSocket.SOCKET_STATUS_CONNECTION_FAILED; break;
		
		
//		case 7 : currentState = ClientSocket."ping 실패"; break;
		case 7 : currentState = ClientSocket.SOCKET_STATUS_PING_FAILED; break;
		
		
//		case 8 : currentState = ClientSocket."응답 대기중"; break;
		case 8 : currentState = ClientSocket.SOCKET_STATUS_WAITING_RESPONSE; break;
		
		
//		case 9 : currentState = ClientSocket."접속끊김"; break;
		case 9 : currentState = ClientSocket.SOCKET_STATUS_CONNECTION_IS_CUT_OFF; break;		
		
		}
		
		return currentState;		
	}
	
	public static Socket getClientSocket(String ip, int port) {		
		
		clientSocket = new ClientSocket();
		String socketIp;
		int socketPort;
		
		// IP, Port를 사용자에게 입력받아서 서버 소켓 정보를 초기화한다.						
		try {

			if(!ClientSocket.isFirst) {
				// 통신 연결 정보 입력이 처음이 아닐 경우				
				StringBuilder msg = new StringBuilder();
				msg.append("Do you want to reconnect using the last connection information?" + Util.separator + Util.separator + "\n");
				msg.append("IP : <font color='blue'>" + ClientSocket.ip + "</font>\n");
				msg.append("Port : <font color='blue'>" + ClientSocket.port + "</font>\n");
				
				int retry = Util.showConfirm(msg.toString());
				
				if(retry == JOptionPane.YES_OPTION) {
					socketIp = ClientSocket.ip;
					socketPort = ClientSocket.port;
				} else {
					// retry == JOptionPane.NO_OPTION (취소 버튼)
					// retry == JOptionPane.CLOSED_OPTION (X 버튼)
					// 재귀호출 : 철저하게 디버깅 해볼 것 , Integer.parseInt 예외처리도 하자
					ClientSocket.isFirst = true;
					
					String[] connectionInfo = getConnectionInfo();
					
					try {
						if(connectionInfo != null) {
							socketIp = connectionInfo[0];
							socketPort = Integer.parseInt(connectionInfo[1]);
							clientSocket = getClientSocket(socketIp, socketPort);
							return clientSocket;
						}else {
							Util.showMessage("<font color='red'>Cancel the Connection</font>\nCanceled the Connection&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
							return null;
						}
					}catch(NumberFormatException e) {
						Util.showMessage("<font color='red'>Validity error</font>\nPlease check the Port Number information you entered&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
						return null;
					}				
				}// end -> if(retry == JOptionPane.YES_OPTION)
								
			} else {
				// 처음으로 통신 연결정보를 입력 할 경우
				// 매개인자로 받은 ip, port 정보로 소켓 접속
				socketIp = ip;
				socketPort = port;
			}
			
			// 클라이언트 소켓 : 접속 중
			ClientSocket.setState(ClientSocket.NODE_CONDITION_CONNECTING); // 접속중			
									
			// 클라이언트 소켓 : 연결 타임아웃 5초
			clientSocket.connect(new InetSocketAddress(socketIp, socketPort), ClientSocket.CONNECT_TIMEOUT);
			// 클라이언트 소켓 : 응답 타임아웃 5초
			clientSocket.setSoTimeout(ClientSocket.RESPONSE_TIMEOUT);
			
			// 클라이언트 소켓 : 접속 성공 (유효한 ip, port)			
			ClientSocket.setState(ClientSocket.NODE_CONDITION_CONNSUCCESS); // 접속 성공			
			
			// 클라이언트 소켓 : 최소 한번 이상의 통신 연결이 이루어짐
			ClientSocket.isFirst = false;
			src_ko.agent.ClientSocket.isFirst = false;
			
			// 마지막으로 접속한 연결 정보를 ClientSocket의 static 필드에 저장한다.
			ClientSocket.ip = clientSocket.getInetAddress().toString().split("/")[1];
			ClientSocket.port = clientSocket.getPort();
			src_ko.agent.ClientSocket.ip = ClientSocket.ip;		
			src_ko.agent.ClientSocket.port = ClientSocket.port;
							
			// 통신 접속 성공 메시지
			StringBuilder msg = new StringBuilder();
			msg.append(Util.colorGreen("Successful connection") + "\n");
			msg.append("IP : " + Util.colorBlue(ClientSocket.ip) +"\n");
			msg.append("Port : " + Util.colorBlue(String.valueOf(ClientSocket.port)) + "\n");			
			Util.showMessage(msg.toString(), JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			// 클라이언트 소켓 : 접속 실패 (IP, port 유효하지 않음)
			ClientSocket.setState(ClientSocket.NODE_CONDITION_CONNFAIL);			
			
			StringBuilder msg = new StringBuilder();
			msg.append("<font color='red'>Failed to connect</font>\n");
			msg.append("Please check the connection information&nbsp;&nbsp;&nbsp;\n\n");
			msg.append("IP : " + Util.colorRed(ip) +"\n");
			msg.append("Port : " + Util.colorRed(String.valueOf(port)) +"\n");
			Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);			
			return null;
		}
		
		// 정상적으로 클라이언트 소켓을 반환할 시 타임아웃 카운트를 초기화
		ClientSocket.resetTimeoutCount();
		
		// 패킷로그 내용 초기화
		packetLogContent = new StringBuilder();
		packetLogContent_temp = new StringBuilder();
		
		// 프레임 패킷로그 내용 초기화
		ModbusAgent_Panel.getPacketLog().setText(null);
		
		return clientSocket;
	}
	
	// 클라이언트 소켓 : 최초 통신 연결 여부 반환
	public static boolean getIsFirst() {
		return ClientSocket.isFirst;
	}
	
	// 클라이언트 소켓 : 상태 저장
	public static void setState(int state) {
		ClientSocket.state = state;
		src_ko.agent.ClientSocket.state = state;
	}
	
	// 클라이언트 소켓 : 현재 상태 반환
	public static int getState() {
		return ClientSocket.state;
	}
	
	// 클라이언트 소켓 : 현재 상태를 문자열로 반환
	public static String getCurrentState() {
		return getStringState(getState());
	}
	
	// 클라이언트 소켓 : 응답 타임아웃 임계값 반환
	public static int getLimitTimeoutCount() {
		return ClientSocket.LIMIT_TIMEOUT_COUNT;
	}
	
	// 클라이언트 소켓 : 현재 응답 타임아웃 카운트 반환
	public static int getCurrentTimeoutCount() {
		return ClientSocket.CURRENT_TIMEOUT_COUNT;
	}
	
	// 클라이언트 소켓 : 타임아웃 카운트 증가
	public static void incrementTimeoutCount() {
		ClientSocket.CURRENT_TIMEOUT_COUNT++;
	}
	
	// 클라이언트 소켓 : 타임아웃 카운트 초기화
	public static void resetTimeoutCount() {
		ClientSocket.CURRENT_TIMEOUT_COUNT = 0;
	}	
	
	// 클라이언트 소켓 : 인자로 넘겨받은 소켓이 현재 연결된 서버가 있는지 확인
	public static boolean isCurrentConnected(Socket socket) {	
		return socket.isConnected() && ! socket.isClosed(); 
	}
	
	// 클라이언트 소켓 : 접속된 IP:Port - 상태 양식의 문자열 반환
	public static String getConnectedInfo() {
		return ip + ":" + port + " - " + getCurrentState();
	}
	
	public static String getSimpleConnectedInfo() {
		return ip + ":" + port;
	}
	
	// 대화상자로 사용자에게 IP, Port 정보를  입력받아 String[] 형태로 반환
	public static String[] getConnectionInfo() {
		String ConnectionInfo = null;
				
		Font font = FontManager.getFont(Font.BOLD, 15);				
		
		JLabel insert = new JLabel("<html><font color='blue'>Connection information" +  Util.separator + Util.separator + "</font><br></html>");
		insert.setFont(boldfont);
		
		JLabel ip_label = new JLabel("<html><font color='black'>IP</font></html>");
		ip_label.setFont(boldfont);
		
		JLabel port_label = new JLabel("<html><font color='black'>Port</font></html>");
		port_label.setFont(boldfont);		
		
		font = FontManager.getFont(Font.PLAIN, 15);
		
		JTextField ip = new JTextField();
		ip.setFont(font);
		
		
		JTextField port = new JTextField();
		port.setFont(font);
		
		if(hasLastConnectionInfo) {			
			ip.setText(ClientSocket.ip);
			port.setText(String.valueOf(ClientSocket.port));
		}		
		
		Object[] message = {
				   insert,
			       ip_label, ip,
			       port_label, port,
			};
		
		int option = JOptionPane.showConfirmDialog(null, message, "ModbusAnalyer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		
		if (option == JOptionPane.OK_OPTION) {
			
			try {
				Integer.parseInt(port.getText().trim());
			}catch (NumberFormatException e) {				
				Util.showMessage("<font color='red'>Validity error</font>\nPlease check the Port Number information you entered&nbsp;&nbsp;&nbsp;&nbsp;\n", JOptionPane.ERROR_MESSAGE);
				return null;
			}						
			
			return new String[] { ip.getText().trim() , port.getText().trim() };
			
		} else {
			return null;
		}
	}
	
	public static void setHasLastConnectionInfo(boolean has) {
		ClientSocket.hasLastConnectionInfo = has;
		src_ko.agent.ClientSocket.hasLastConnectionInfo = has;
	}
	
}// ClientSocket
