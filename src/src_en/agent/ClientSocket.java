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
	
	// Ŭ���̾�Ʈ ���� : ���� ����
	public static boolean isFirst = true;
	public static String ip = "0.0.0.0";
	public static int port = 0;
		
	// Ŭ���̾�Ʈ ���� : ������ ���� ����	
	public static boolean hasLastConnectionInfo = false;
	
	// Ŭ���̾�Ʈ ���� : ���� Ÿ�Ӿƿ� ����
	private static int CURRENT_TIMEOUT_COUNT = 0; // ���� Ÿ�Ӿƿ� ī��Ʈ
	private static final int LIMIT_TIMEOUT_COUNT = 3; // ���� Ÿ�Ӿƿ� ī��Ʈ �Ӱ� ��
	private static final int CONNECT_TIMEOUT = 5000; // ��� ���� ���� Ÿ�Ӿƿ� (�и��� s)
	public static final int RESPONSE_TIMEOUT = 5000; // ���� ��� ���� Ÿ�Ӿƿ� (�и��� s)
	
	// Ŭ���̾�Ʈ ���� : ���� ����
	public static int state = 0; // ���� Ŭ���̾�Ʈ ���� ���� : ���� ��
	public static final int NODE_CONDITION_INIT = 0; // ���� ��
	public static final int NODE_CONDITION_CONNSUCCESS = 1; // ���Ӽ���
	public static final int NODE_CONDITION_CONNECTING = 2; // ������
	public static final int NODE_CONDITION_REGULAR = 3; // �����
	public static final int NODE_CONDITION_COMMERR = 4; // ��ſ���
	public static final int NODE_CONDITION_CONNCLOSE = 5; // ��������
	public static final int NODE_CONDITION_CONNFAIL = 6; // ���ӽ���
	public static final int NODE_CONDITION_SYSTEM_DOWN = 7; // ping ����
	public static final int NODE_CONDITION_RESPONSE_WAITING = 8; // ���� �����
	public static final int NODE_CONDITION_DISCONNECTED = 9; // ���� ����
	
	// 2021-01-14 : ��Ŷ�α׸� ���Ϸ� ������ �� ����Ϸ��� ����ǵ� ���� ���Ϸ� ���� �� �ʿ䰡 ������ �; �ϴ� ����
	// Ŭ���̾�Ʈ ���� : ���ǵ��� ������ ����� ��� (��Ŷ�α�) 
	public static StringBuilder packetLogContent;
	public static StringBuilder packetLogContent_temp;
	
	// Ŭ���̾�Ʈ ���� :���� ��ȯ
	public static String getStringState(int state) {
		String currentState = "";		
		
		switch(state) {
//		case 0 : currentState = "���� ��"; break;
		case 0 : currentState = ClientSocket.SOCKET_STATUS_BEFORE_CONNECTING; break;
		
//		case 1 : currentState = "���Ӽ���"; break;
		case 1 : currentState = ClientSocket.SOCKET_STATUS_CONNECTED; break;
		
		
//		case 2 : currentState = "������"; break;
		case 2 : currentState = ClientSocket.SOCKET_STATUS_CONNECTING; break;
		
		
//		case 3 : currentState = ClientSocket."�����"; break;
		case 3 : currentState = ClientSocket.SOCKET_STATUS_COMMUNICATING; break;
		
		
//		case 4 : currentState = ClientSocket."��ſ���"; break;
		case 4 : currentState = ClientSocket.SOCKET_STATUS_COMMUNICATION_ERROR; break;
		
		
//		case 5 : currentState = ClientSocket."��������"; break;
		case 5 : currentState = ClientSocket.SOCKET_STATUS_CONNECTION_CLOSED; break;
		
		
//		case 6 : currentState = ClientSocket."���ӽ���"; break;
		case 6 : currentState = ClientSocket.SOCKET_STATUS_CONNECTION_FAILED; break;
		
		
//		case 7 : currentState = ClientSocket."ping ����"; break;
		case 7 : currentState = ClientSocket.SOCKET_STATUS_PING_FAILED; break;
		
		
//		case 8 : currentState = ClientSocket."���� �����"; break;
		case 8 : currentState = ClientSocket.SOCKET_STATUS_WAITING_RESPONSE; break;
		
		
//		case 9 : currentState = ClientSocket."���Ӳ���"; break;
		case 9 : currentState = ClientSocket.SOCKET_STATUS_CONNECTION_IS_CUT_OFF; break;		
		
		}
		
		return currentState;		
	}
	
	public static Socket getClientSocket(String ip, int port) {		
		
		clientSocket = new ClientSocket();
		String socketIp;
		int socketPort;
		
		// IP, Port�� ����ڿ��� �Է¹޾Ƽ� ���� ���� ������ �ʱ�ȭ�Ѵ�.						
		try {

			if(!ClientSocket.isFirst) {
				// ��� ���� ���� �Է��� ó���� �ƴ� ���				
				StringBuilder msg = new StringBuilder();
				msg.append("Do you want to reconnect using the last connection information?" + Util.separator + Util.separator + "\n");
				msg.append("IP : <font color='blue'>" + ClientSocket.ip + "</font>\n");
				msg.append("Port : <font color='blue'>" + ClientSocket.port + "</font>\n");
				
				int retry = Util.showConfirm(msg.toString());
				
				if(retry == JOptionPane.YES_OPTION) {
					socketIp = ClientSocket.ip;
					socketPort = ClientSocket.port;
				} else {
					// retry == JOptionPane.NO_OPTION (��� ��ư)
					// retry == JOptionPane.CLOSED_OPTION (X ��ư)
					// ���ȣ�� : ö���ϰ� ����� �غ� �� , Integer.parseInt ����ó���� ����
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
				// ó������ ��� ���������� �Է� �� ���
				// �Ű����ڷ� ���� ip, port ������ ���� ����
				socketIp = ip;
				socketPort = port;
			}
			
			// Ŭ���̾�Ʈ ���� : ���� ��
			ClientSocket.setState(ClientSocket.NODE_CONDITION_CONNECTING); // ������			
									
			// Ŭ���̾�Ʈ ���� : ���� Ÿ�Ӿƿ� 5��
			clientSocket.connect(new InetSocketAddress(socketIp, socketPort), ClientSocket.CONNECT_TIMEOUT);
			// Ŭ���̾�Ʈ ���� : ���� Ÿ�Ӿƿ� 5��
			clientSocket.setSoTimeout(ClientSocket.RESPONSE_TIMEOUT);
			
			// Ŭ���̾�Ʈ ���� : ���� ���� (��ȿ�� ip, port)			
			ClientSocket.setState(ClientSocket.NODE_CONDITION_CONNSUCCESS); // ���� ����			
			
			// Ŭ���̾�Ʈ ���� : �ּ� �ѹ� �̻��� ��� ������ �̷����
			ClientSocket.isFirst = false;
			src_ko.agent.ClientSocket.isFirst = false;
			
			// ���������� ������ ���� ������ ClientSocket�� static �ʵ忡 �����Ѵ�.
			ClientSocket.ip = clientSocket.getInetAddress().toString().split("/")[1];
			ClientSocket.port = clientSocket.getPort();
			src_ko.agent.ClientSocket.ip = ClientSocket.ip;		
			src_ko.agent.ClientSocket.port = ClientSocket.port;
							
			// ��� ���� ���� �޽���
			StringBuilder msg = new StringBuilder();
			msg.append(Util.colorGreen("Successful connection") + "\n");
			msg.append("IP : " + Util.colorBlue(ClientSocket.ip) +"\n");
			msg.append("Port : " + Util.colorBlue(String.valueOf(ClientSocket.port)) + "\n");			
			Util.showMessage(msg.toString(), JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			// Ŭ���̾�Ʈ ���� : ���� ���� (IP, port ��ȿ���� ����)
			ClientSocket.setState(ClientSocket.NODE_CONDITION_CONNFAIL);			
			
			StringBuilder msg = new StringBuilder();
			msg.append("<font color='red'>Failed to connect</font>\n");
			msg.append("Please check the connection information&nbsp;&nbsp;&nbsp;\n\n");
			msg.append("IP : " + Util.colorRed(ip) +"\n");
			msg.append("Port : " + Util.colorRed(String.valueOf(port)) +"\n");
			Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);			
			return null;
		}
		
		// ���������� Ŭ���̾�Ʈ ������ ��ȯ�� �� Ÿ�Ӿƿ� ī��Ʈ�� �ʱ�ȭ
		ClientSocket.resetTimeoutCount();
		
		// ��Ŷ�α� ���� �ʱ�ȭ
		packetLogContent = new StringBuilder();
		packetLogContent_temp = new StringBuilder();
		
		// ������ ��Ŷ�α� ���� �ʱ�ȭ
		ModbusAgent_Panel.getPacketLog().setText(null);
		
		return clientSocket;
	}
	
	// Ŭ���̾�Ʈ ���� : ���� ��� ���� ���� ��ȯ
	public static boolean getIsFirst() {
		return ClientSocket.isFirst;
	}
	
	// Ŭ���̾�Ʈ ���� : ���� ����
	public static void setState(int state) {
		ClientSocket.state = state;
		src_ko.agent.ClientSocket.state = state;
	}
	
	// Ŭ���̾�Ʈ ���� : ���� ���� ��ȯ
	public static int getState() {
		return ClientSocket.state;
	}
	
	// Ŭ���̾�Ʈ ���� : ���� ���¸� ���ڿ��� ��ȯ
	public static String getCurrentState() {
		return getStringState(getState());
	}
	
	// Ŭ���̾�Ʈ ���� : ���� Ÿ�Ӿƿ� �Ӱ谪 ��ȯ
	public static int getLimitTimeoutCount() {
		return ClientSocket.LIMIT_TIMEOUT_COUNT;
	}
	
	// Ŭ���̾�Ʈ ���� : ���� ���� Ÿ�Ӿƿ� ī��Ʈ ��ȯ
	public static int getCurrentTimeoutCount() {
		return ClientSocket.CURRENT_TIMEOUT_COUNT;
	}
	
	// Ŭ���̾�Ʈ ���� : Ÿ�Ӿƿ� ī��Ʈ ����
	public static void incrementTimeoutCount() {
		ClientSocket.CURRENT_TIMEOUT_COUNT++;
	}
	
	// Ŭ���̾�Ʈ ���� : Ÿ�Ӿƿ� ī��Ʈ �ʱ�ȭ
	public static void resetTimeoutCount() {
		ClientSocket.CURRENT_TIMEOUT_COUNT = 0;
	}	
	
	// Ŭ���̾�Ʈ ���� : ���ڷ� �Ѱܹ��� ������ ���� ����� ������ �ִ��� Ȯ��
	public static boolean isCurrentConnected(Socket socket) {	
		return socket.isConnected() && ! socket.isClosed(); 
	}
	
	// Ŭ���̾�Ʈ ���� : ���ӵ� IP:Port - ���� ����� ���ڿ� ��ȯ
	public static String getConnectedInfo() {
		return ip + ":" + port + " - " + getCurrentState();
	}
	
	public static String getSimpleConnectedInfo() {
		return ip + ":" + port;
	}
	
	// ��ȭ���ڷ� ����ڿ��� IP, Port ������  �Է¹޾� String[] ���·� ��ȯ
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
