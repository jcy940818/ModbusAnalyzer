package src_ko.agent;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import com.serotonin.modbus4j.base.KeyedModbusLocator;
import com.serotonin.modbus4j.base.ReadFunctionGroup;
import com.serotonin.modbus4j.locator.BaseLocator;

import common.agent.PerfData;
import common.modbus.ModbusMonitor;
import common.modbus.ModbusWatchPoint;
import src_ko.analyzer.RX.RX_Analyzer;
import src_ko.analyzer.TX.TX_Analyzer;
import src_ko.info.RX_Info;
import src_ko.info.TX_Info;
import src_ko.swing.ExceptionScan_Panel;
import src_ko.swing.ModbusAgent_Panel;
import src_ko.swing.ModbusMonitorFrame;
import src_ko.swing.RealTime_Panel;
import src_ko.swing.SimpleValueScan_Panel;
import src_ko.util.ExceptionProvider;
import src_ko.util.Timer;
import src_ko.util.Util;

public class ModbusAgent {

	public static final int CONN_METHOD_MODBUS = 2048;
	public static final int MODBUS_TYPE_RTU = 997;
	public static final int MODBUS_TYPE_TCP = 998;
	
	public static final String FC01 = "Read Coil Status";
	public static final String FC02 = "Read Input Status";
	public static final String FC03 = "Read Holding Registers";
	public static final String FC04 = "Read Input Registers";
	public static final String FC05 = "Force Single Coil";
	public static final String FC06 = "Preset Single Register";
	public static final String FC15 = "Force Multiple Coils";
	public static final String FC16 = "Preset Multiple Registers";
	
	public static boolean isRTU = false;
	public static int lastFunctionCode = 3;
		
	// Ŭ���̾�Ʈ ����
	public static Socket clientSocket = null;
	private static String log = null;
	private static String tempRxPacket = null;
	 
	public static RX_Info communicate(Socket client,TX_Info tx ,boolean isRTU, int timeout) throws IOException, EOFException, SocketException {
		timeout = (timeout >= 0) ? timeout : ClientSocket.RESPONSE_TIMEOUT;
		RX_Info rx = null;
		
		clientSocket = client;
		clientSocket.setSoTimeout(timeout);

			try {
				if (isRTU) {
					// Modbus RTU ���					
					if(clientSocket == null) {						
						return null;
					}
					
					// ���� Ŭ���̾�Ʈ ������ ������ ����� �������� �˻�					
						try {											
							// TX ���� �� �α� ���
							ModbusSender.sendRTU(clientSocket, tx);
							ModbusAgent.printTX(tx, tx.getAgentType());													
							
							// Ŭ���̾�Ʈ ���� : TX ���� �Ϸ� �� ���� ����� 
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // Ŭ���̾�Ʈ ���� : ��� ����							
							}else {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING);	
							}
							
							// RX ���� �� �α� ���
							rx = ModbusReceiver.reciveRTU(clientSocket, tx);
							ModbusAgent.printRX(rx, tx.getAgentType());
														
							// Ŭ���̾�Ʈ ���� : ����� (��û��Ŷ�� ���� ������Ŷ�� ������)
							ClientSocket.setState(ClientSocket.NODE_CONDITION_REGULAR);

							// Ŭ���̾�Ʈ ���� : ������Ŷ ���Ž� ���� Ÿ�Ӿƿ� ī��Ʈ �ʱ�ȭ
							ClientSocket.resetTimeoutCount();

							return rx;
						} catch (SocketTimeoutException e) {
							// ���� ��Ŷ �����Ͽ����� ó�� �� �� ���� ��Ŷ�� ���� ��� ��Ŷ ���� ���
							ModbusAgent.printRX(rx, tx.getAgentType());
							
							// Real-Time Monitoring ����� Ÿ�Ӿƿ� üũ�� �����ʴ´�
							if(tx.getAgentType().equalsIgnoreCase("ModbusAgent")) {
								ModbusAgent.responseTimeoutDoNothing(e);
							}else {
								ClientSocket.incrementTimeoutCount();
							}							
							
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // Ŭ���̾�Ʈ ���� : ��� ����
							}
							
							return null;
							
						} catch (SocketException e) {
							// ����� ������ ��������
							ModbusAgent.serverSocketClosed(e);
							return null;
						}
						
				} else {
					// Modbus TCP ���					
					if(clientSocket == null) {						
						return null;
					}					
					// ���� Ŭ���̾�Ʈ ������ ������ ����� �������� �˻�
					
						try {
//							// TX ���� �� �α� ���
							ModbusSender.sendTCP(clientSocket, tx);
							ModbusAgent.printTX(tx, tx.getAgentType());
							
							// Ŭ���̾�Ʈ ���� : TX ���� �Ϸ� �� ���� ����� 
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // Ŭ���̾�Ʈ ���� : ��� ����							
							}else {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING);
							}
							
							// RX ���� �� �α� ���
							rx = ModbusReceiver.reciveTCP(clientSocket, tx);
							ModbusAgent.printRX(rx, tx.getAgentType());
							
							// Ŭ���̾�Ʈ ���� : ����� (��û��Ŷ�� ���� ������Ŷ�� ������)
							ClientSocket.setState(ClientSocket.NODE_CONDITION_REGULAR);

							// Ŭ���̾�Ʈ ���� : ������Ŷ ���Ž� ���� Ÿ�Ӿƿ� ī��Ʈ �ʱ�ȭ
							ClientSocket.resetTimeoutCount();
							
							return rx;							
						} catch (SocketTimeoutException e) {
							// ���� ��Ŷ �����Ͽ����� ó�� �� �� ���� ��Ŷ�� ���� ��� ��Ŷ ���� ���
							ModbusAgent.printRX(rx, tx.getAgentType());
							
							// Real-Time Monitoring ����� Ÿ�Ӿƿ� üũ�� �����ʴ´�
							if(tx.getAgentType().equalsIgnoreCase("ModbusAgent")) {
								ModbusAgent.responseTimeoutDoNothing(e);
							}else {
								ClientSocket.incrementTimeoutCount();
							}							
							
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // Ŭ���̾�Ʈ ���� : ��� ����
							}
							
							return null;
							
						} catch (SocketException e) {
							// ����� ������ ��������
							ModbusAgent.serverSocketClosed(e);
							return null;
						}
					}
								
			} catch (EOFException e) {
				// TX ���� �� RX ��� �� ���� ����
				ModbusAgent.waitingLostConnection(e);
				return null;
				
			} catch (SocketException e) {
				// ���� Ÿ�Ӿƿ� ī��Ʈ�� �Ӱ谪�� ����
				ModbusAgent.maxResponseTimeoutCount(e);
				return null;
				
			} catch (NullPointerException e) {				
				clientSocket.close();			
				return null;				
			} catch (Exception e) {
				ModbusAgent.unknownException(e);						
				return null;
			}
			
	}// communicate
	
	
	
	public static RX_Info registerScan(Socket client,TX_Info tx ,boolean isRTU, int timeout) throws IOException, EOFException, SocketException {
		RX_Info rx = null;
		
		clientSocket = client;
		clientSocket.setSoTimeout(timeout);

			try {
				if (isRTU) {
					// Modbus RTU ���					
					if(clientSocket == null) {						
						return null;
					}
					
					// ���� Ŭ���̾�Ʈ ������ ������ ����� �������� �˻�					
						try {											
							// TX ���� �� �α� ���
							ModbusSender.sendRTU(clientSocket, tx);
							ModbusAgent.printTX(tx, tx.getAgentType());
							
							// Ŭ���̾�Ʈ ���� : TX ���� �Ϸ� �� ���� ����� 
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // Ŭ���̾�Ʈ ���� : ��� ����							
							}else {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING);
							}
							
							// RX ���� �� �α� ���
							rx = ModbusReceiver.reciveRTU(clientSocket, tx);
							ModbusAgent.printRX(rx, tx.getAgentType());
														
							// Ŭ���̾�Ʈ ���� : ����� (��û��Ŷ�� ���� ������Ŷ�� ������)
							ClientSocket.setState(ClientSocket.NODE_CONDITION_REGULAR);

							// Ŭ���̾�Ʈ ���� : ������Ŷ ���Ž� ���� Ÿ�Ӿƿ� ī��Ʈ �ʱ�ȭ
							ClientSocket.resetTimeoutCount();

							return rx;
						} catch (SocketTimeoutException e) {
							// ���� ��Ŷ �����Ͽ����� ó�� �� �� ���� ��Ŷ�� ���� ��� ��Ŷ ���� ���
							ModbusAgent.printRX(rx, tx.getAgentType());
//							ModbusAgent.responseTimeout(e);																		
							
							rx = new RX_Info();
							rx.setTxInfo(tx);
							rx.setScanResult("Response Timeout");
							
							ClientSocket.incrementTimeoutCount();
							
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // Ŭ���̾�Ʈ ���� : ��� ����							
							}
							
							return rx;
							
						} catch (SocketException e) {
							// ����� ������ ��������
							ModbusAgent.serverSocketClosed(e);
							return null;
						}
						
				} else {
					// Modbus TCP ���					
					if(clientSocket == null) {						
						return null;
					}					
					// ���� Ŭ���̾�Ʈ ������ ������ ����� �������� �˻�
					
						try {
//							// TX ���� �� �α� ���
							ModbusSender.sendTCP(clientSocket, tx);
							ModbusAgent.printTX(tx, tx.getAgentType());
							
							// Ŭ���̾�Ʈ ���� : TX ���� �Ϸ� �� ���� ����� 
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // Ŭ���̾�Ʈ ���� : ��� ����							
							}else {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING);
							}
							
							// RX ���� �� �α� ���
							rx = ModbusReceiver.reciveTCP(clientSocket, tx);
							ModbusAgent.printRX(rx, tx.getAgentType());
							
							// Ŭ���̾�Ʈ ���� : ����� (��û��Ŷ�� ���� ������Ŷ�� ������)
							ClientSocket.setState(ClientSocket.NODE_CONDITION_REGULAR);

							// Ŭ���̾�Ʈ ���� : ������Ŷ ���Ž� ���� Ÿ�Ӿƿ� ī��Ʈ �ʱ�ȭ
							ClientSocket.resetTimeoutCount();
							
							return rx;							
						} catch (SocketTimeoutException e) {
							// ���� ��Ŷ �����Ͽ����� ó�� �� �� ���� ��Ŷ�� ���� ��� ��Ŷ ���� ���
							ModbusAgent.printRX(rx, tx.getAgentType());
//							ModbusAgent.responseTimeout(e);
							
							rx = new RX_Info();
							rx.setTxInfo(tx);
							rx.setScanResult("Response Timeout");
							
							ClientSocket.incrementTimeoutCount();
							
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // Ŭ���̾�Ʈ ���� : ��� ����							
							}
							
							return rx;
							
						} catch (SocketException e) {
							// ����� ������ ��������
							ModbusAgent.serverSocketClosed(e);
							return null;
						}
					}
								
			} catch (EOFException e) {
				// TX ���� �� RX ��� �� ���� ����
				ModbusAgent.waitingLostConnection(e);
				return null;
				
			} catch (SocketException e) {
				// ���� Ÿ�Ӿƿ� ī��Ʈ�� �Ӱ谪�� ����
				ModbusAgent.maxResponseTimeoutCount(e);
				return null;
				
			} catch (NullPointerException e) {				
				clientSocket.close();			
				return null;				
			} catch (Exception e) {
				ModbusAgent.unknownException(e);						
				return null;
			}
			
	}// exceptionScan
	
	
	
	public static void modbusCommunicate(ModbusMonitor monitor, Socket client, ArrayList<ModbusWatchPoint> pointList, int timeout, int maxCount) throws IOException, EOFException, SocketException {
		try {
			ModbusMonitor.isRunning = true;
			clientSocket = client;
			TX_Info tx = null;
			RX_Info rx = null;

			if(clientSocket == null) {
				return;
			}else {
				clientSocket.setSoTimeout((timeout >= 0) ? timeout : ClientSocket.RESPONSE_TIMEOUT);
			}
			
			// ����Ʈ�� �ε����� ���� �� �Ľ�
			for(ModbusWatchPoint point : pointList) {
				point.setIndex(monitor.index++);
				monitor.parseCommand(point);
			}
			
			monitor.init(monitor.getType(), client.getInetAddress().getHostAddress(), client.getPort());
			if(!(maxCount > 2000) && !(maxCount < 0)) {
				monitor.setMaxReadBitCount(maxCount);
			}
			if(!(maxCount > 125) && !(maxCount < 0)) {
				monitor.setMaxReadRegisterCount(maxCount);	
			}
			
			List<ReadFunctionGroup> functionGroupList = monitor.getFuntionGroupList();
			
			// ��û ��Ŷ �������� ����Ʈ���� �����͸� �ʱ�ȭ
			Collections.sort(pointList);
			ModbusWatchPoint.pointDataClear(pointList);
			
			// ����Ʈ ����Ʈ ����
			ModbusMonitorFrame.pointList = pointList;
			
			// ������ ����� ������ ��Ŷ �α� �ʱ�ȭ
			ModbusMonitorFrame.cleaerLog();
			
			for(ReadFunctionGroup fcGroup : functionGroupList) {
				
				// ��û ��Ŷ ó���� ������� ��û�� ���� ����� �����Ǿ��� ���
				if(!ModbusMonitor.isRunning) {
					return;
				}
				
				String txPacket = monitor.sendCommand(fcGroup, clientSocket);
				tx = new TX_Info();
				tx.setContent(txPacket);
				tx = (monitor.getType() == ModbusMonitor.TYPE_RTU) ? new TX_Analyzer().rtuAnalysis(tx) : new TX_Analyzer().tcpAnalysis(tx);
				
				String modbusType = (monitor.getType() == ModbusMonitor.TYPE_RTU) ? "Modbus-RTU" : "Modbus-TCP";
				StringBuilder sb = new StringBuilder("���������������� ��û ���� ������������������");
				sb.append(System.lineSeparator());
				sb.append(String.format("�� ������ Ÿ�� : %s\t��", modbusType));
				sb.append(System.lineSeparator());
				sb.append(String.format("�� ����ȣ : %s��\t\t��", tx.getUnitId()));
				sb.append(System.lineSeparator());
				sb.append(String.format("�� ����ڵ� : %d\t\t\t��", tx.getFunctionCode()));
				sb.append(System.lineSeparator());
				sb.append(String.format("�� ��û �����ּ� (Modbus DEC) : %s\t��", tx.getModbusAddrString()));
				sb.append(System.lineSeparator());
				sb.append(String.format("�� ��û �����ּ� (Register DEC) : %s\t��", tx.getStartAddress()));
				sb.append(System.lineSeparator());
				sb.append(String.format("�� ��û �����ּ� (Register HEX) : %s\t��", tx.getRegisterAddrHexString()));
				sb.append(System.lineSeparator());
				sb.append(String.format("�� ��û ���� : %s\t\t��", tx.getRequestCount()));
				sb.append(System.lineSeparator());
				sb.append(String.format("��������������������������������������������", tx.getRequestCount()));				
				
				ModbusMonitorFrame.writeLog(sb.toString());
				ModbusMonitorFrame.writeLog(Timer.getServerTime() + " [ TX ] : " + txPacket);				
				
				
				// Ŭ���̾�Ʈ ���� : TX ���� �Ϸ� �� ���� �����
				if(ClientSocket.getCurrentTimeoutCount() >= 5) {
					ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // ��� ����
				}else {
					ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING); // ���� �����
				}
				
				String rxPacket = null;
				try {
					rxPacket = monitor.parseResponsePacket(fcGroup, clientSocket);

				}catch (SocketTimeoutException e) {
					// Ÿ�Ӿƿ��� �ش� ��û�� ��ȿó�� �� ���� ��û ����
					ModbusMonitorFrame.writeLog(Timer.getServerTime() + " [ Timeout ] : " + e.getMessage());
					ModbusMonitorFrame.writeLog(System.lineSeparator() + System.lineSeparator() + System.lineSeparator() + System.lineSeparator());
					
					// Ŭ���̾�Ʈ ���� : ��� ����
					ClientSocket.incrementTimeoutCount();
					if(ClientSocket.getCurrentTimeoutCount() >= 5) ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR);
					continue;
				}
				
				ModbusMonitorFrame.writeLog(Timer.getServerTime() + " [ RX ] : " + rxPacket);
				
				rx = new RX_Info();
				rx.setTxInfo(tx);
				rx.setContent(rxPacket);
				rx = (monitor.getType() == ModbusMonitor.TYPE_RTU) ? new RX_Analyzer().rtuAnalysis(rx) : new RX_Analyzer().tcpAnalysis(rx);
				
				// ������ ����ġ üũ
				String content = ExceptionProvider.getCompareTxRxString(tx, rx);
				if(content != null) {
					ModbusMonitorFrame.writeLog(content);
				}
				
				// ( TX ��û ���� * 2 == RX ������ ���� ) üũ
				String lengthCheck = ExceptionProvider.getRxLengthCheckResult(tx, rx);
				if(lengthCheck != null) {
					ModbusMonitorFrame.writeLog(lengthCheck);
				}
				
				// ����(Exception) ���� üũ
				String error = RX_Info.getRxHandleContent(rx);
				if(!error.equalsIgnoreCase("") && error.length() >= 1) {
					ModbusMonitorFrame.writeLog(error);
				}
				
				// ������ �α� ���
				List locators = fcGroup.getLocators();
				for (int i = 0; i < locators.size(); i++) {
					KeyedModbusLocator keyLocator = (KeyedModbusLocator) locators.get(i);
					BaseLocator locator = (BaseLocator) keyLocator.getLocator();
					int cmd = monitor.locators.indexOf(locator);
					ModbusWatchPoint point = monitor.points.get(cmd);
					PerfData perfData = point.getData();
					
					boolean hasName = !point.getDisplayName().trim().equalsIgnoreCase("") && point.getDisplayName().trim().length() > 0;
					
					switch(ModbusMonitorFrame.addrTypeComboBox.getSelectedItem().toString()){
						case "Modbus (DEC)" :
							if(hasName) {
								ModbusMonitorFrame.writeLog(String.format("%d.  [ %s ] = %s     ( %s = %s )",
										point.getIndex(), 
										point.getDecCounter(), 
										PerfData.getPerfPureValue(perfData), 
										point.getDisplayName(), 
										PerfData.getPerfContent(point, perfData)),
										point);
							}else {
								ModbusMonitorFrame.writeLog(String.format("%d.  [ %s ] = %s", point.getIndex(), point.getDecCounter(), PerfData.getPerfPureValue(perfData)), point);	
							}
							
							break;
							
						case "Register (DEC)" :
							if(hasName) {
								ModbusMonitorFrame.writeLog(String.format("%d.  [ %s ] = %s     ( %s = %s )", 
										point.getIndex(), 
										point.getRegCounter(), 
										PerfData.getPerfPureValue(perfData),
										point.getDisplayName(), 
										PerfData.getPerfContent(point, perfData)), 
										point);
							}else {
								ModbusMonitorFrame.writeLog(String.format("%d.  [ %s ] = %s", point.getIndex(), point.getRegCounter(), PerfData.getPerfPureValue(perfData)), point);	
							}
							
							break;
							
						case "Register (HEX)" :
							if(hasName) {
								ModbusMonitorFrame.writeLog(String.format("%d.  [ %s ] = %s     ( %s = %s )", 
										point.getIndex(), 
										point.getHexCounter(), 
										PerfData.getPerfPureValue(perfData),
										point.getDisplayName(), 
										PerfData.getPerfContent(point, perfData)),
										point);
							}else {
								ModbusMonitorFrame.writeLog(String.format("%d.  [ %s ] = %s", point.getIndex(), point.getHexCounter(), PerfData.getPerfPureValue(perfData)), point);
							}
							
							break;
							
						default :
							if(hasName) {
								ModbusMonitorFrame.writeLog(String.format("%d.  [ %s ] = %s     ( %s = %s )", 
										point.getIndex(), 
										point.getDecCounter(), 
										PerfData.getPerfPureValue(perfData), 
										point.getDisplayName(), 
										PerfData.getPerfContent(point, perfData)),
										point);
							}else {
								ModbusMonitorFrame.writeLog(String.format("%d.  [ %s ] = %s", point.getIndex(), point.getDecCounter(), PerfData.getPerfPureValue(perfData)), point);	
							}
							
							break;
					}
				}
				
				ModbusMonitorFrame.writeLog(System.lineSeparator() + System.lineSeparator() + System.lineSeparator() + System.lineSeparator());
				
				// Ŭ���̾�Ʈ ���� : ����� (��û��Ŷ�� ���� ������Ŷ�� ������)
				ClientSocket.setState(ClientSocket.NODE_CONDITION_REGULAR);

				// Ŭ���̾�Ʈ ���� : ������Ŷ ���Ž� ���� Ÿ�Ӿƿ� ī��Ʈ �ʱ�ȭ
				ClientSocket.resetTimeoutCount();
			}
			
			return;
							
		} catch (EOFException e) {
			// TX ���� �� RX ��� �� ���� ����
			ModbusAgent.waitingLostConnection(e);
			return;
			
		}catch (SocketTimeoutException e) {	
			ClientSocket.incrementTimeoutCount();
			
			if(ClientSocket.getCurrentTimeoutCount() >= 5) {
				ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // Ŭ���̾�Ʈ ���� : ��� ����
			}
			return;
			
		} catch (SocketException e) {
			// ����� ������ ��������
			ModbusAgent.serverSocketClosed(e);
			return;
			
		} catch (NullPointerException e) {
			clientSocket.close();
			return;

		} catch (Exception e) {
			ModbusAgent.unknownException(e);
			return;
			
		}finally {
			ModbusMonitor.isRunning = false;
			
			if(ModbusMonitorFrame.pointTable != null) {
				
				ModbusMonitorFrame.resetTable(ModbusMonitorFrame.pointTable, null);
				if(ModbusMonitorFrame.pointList != null && ModbusMonitorFrame.pointList.size() > 0) {
					ModbusMonitorFrame.addRecord(ModbusMonitorFrame.pointTable, ModbusMonitorFrame.pointList);
				}
				
				try {
					String formula = ModbusMonitorFrame.search_textField.getText();
					if(formula != null) formula = formula.toLowerCase().replace("only", "");
					
					ModbusMonitorFrame.setTableStyle(ModbusMonitorFrame.pointTable, formula);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
			}
			
		}// finally
			
	}// modbusCommunicate
	
	
	
	public static void cleaerTempRxPacket() {
		ModbusAgent.tempRxPacket = null;
	}
	
	public static void setTempRxPacket(byte[] tokens) {
		StringBuilder packet = new StringBuilder();		
		for(int i = 0; i < tokens.length; i++) {
			packet.append(String.format("%02x", (tokens[i] & 0xff)));
		}
		ModbusAgent.tempRxPacket = packet.toString();
	}
	
	
	public static String getTempRxPacket() {
		return ModbusAgent.tempRxPacket;
	}
	
	
	public static void printTX(TX_Info tx, String agentType) {
		String log = null;

		if (agentType.equalsIgnoreCase("ModbusAgent")) {
			log = String.format("%s [ %s TX ] : %s%s", Timer.getServerTime(), (tx.isRTU() ? "RTU" : "TCP"), tx.getContent(), System.lineSeparator());
			System.out.printf(log);

			ModbusAgent_Panel.getPacketLog().append(log);
			ModbusAgent_Panel.scrollUp();
		}else if (agentType.equalsIgnoreCase("RealTime")) {
			log = String.format("%s [ %s TX ] : %s%s", Timer.getServerTime(), (tx.isRTU() ? "RTU" : "TCP"), tx.getContent(), System.lineSeparator());
			System.out.printf(log);

			RealTime_Panel.clearPacketLog();
			RealTime_Panel.getPacketLog().append(log);
			RealTime_Panel.scrollUp();
		}else {
			log = String.format("%s [ %s TX ] : %s [ Address : %s (0x%04X) ]%s", Timer.getServerTime(),
					(tx.isRTU() ? "RTU" : "TCP"), tx.getContent(),
					String.format("%s%04d", tx.getModbusAddress(), tx.getStartAddress() + 1), tx.getStartAddress(),
					System.lineSeparator());
			System.out.printf(log);

			if (agentType.equalsIgnoreCase("SimpleValueScan")) {
				SimpleValueScan_Panel.getPacketLog().append(log);
				SimpleValueScan_Panel.scrollUp();
			} else if (agentType.equalsIgnoreCase("ExceptionScan")) {
				ExceptionScan_Panel.getPacketLog().append(log);
				ExceptionScan_Panel.scrollUp();
			}
		}
		
	}
	
	
	public static void printRX(RX_Info rx, String agentType) throws NullPointerException, SocketException {
		String rxPacketType;
		String rxPacket;
		String log;
		boolean isException = false;

		try {
			if (rx != null) {
				rxPacketType = rx.isRTU() ? "RTU" : "TCP";
				rxPacket = (rx.getActualPacket() != null) ? rx.getActualPacket() : ModbusAgent.getTempRxPacket();
				isException = rx.isException();
			} else {
				// RX_Info is null
				// ModbusAgent�� ���������� ó�� �� �� ���� ���� ��Ŷ
				rxPacketType = "Unprocessable";
				rxPacket = ModbusAgent.getTempRxPacket();
			}

			// ���۹��� ��Ŷ�� ���ٸ� �α׿� �ƹ��͵� ������� �ʰ� ��� ���̺��� �ʱ�ȭ
			if (rxPacket == null) {
				if (agentType.equalsIgnoreCase("ModbusAgent")) {
					ModbusAgent_Panel.resetTable(ModbusAgent_Panel.getResultTable());
					ModbusAgent_Panel.scrollUp();
				}else if (agentType.equalsIgnoreCase("RealTime")) {
					RealTime_Panel.clearPacketLog();
					RealTime_Panel.resetTable(RealTime_Panel.getResultTable());
					RealTime_Panel.scrollUp();
				}else if (agentType.equalsIgnoreCase("SimpleValueScan")) {
					SimpleValueScan_Panel.scrollUp();
				} else if (agentType.equalsIgnoreCase("ExceptionScan")) {
					ExceptionScan_Panel.scrollUp();
				}
				return;
			}

			log = String.format("%s [ %s RX ] : %s", Timer.getServerTime(), rxPacketType, rxPacket);

			if ((rx != null) && rx.isRTU() && rx.isCRCError()) {
				log += String.format(" ( Read Incorrect CRC : 0x%04x / Expected CRC : 0x%04x )", rx.getCrc() & 0xffff , rx.getExpectedCrc() & 0xffff);
			}

			if (isException) {
				log += String.format(" ( 0x%02x : %s )%s", rx.getExceptionCode(), rx.getExceptionContent(),
						System.lineSeparator());
			} else {
				log += String.format("%s", System.lineSeparator());
			}

			System.out.println(log);

			if (agentType.equalsIgnoreCase("ModbusAgent")) {
				ModbusAgent_Panel.getPacketLog().append(log);
				ModbusAgent_Panel.resetTable(ModbusAgent_Panel.getResultTable());
				ModbusAgent_Panel.scrollUp();
			} else if (agentType.equalsIgnoreCase("RealTime")) {
				RealTime_Panel.clearPacketLog();
				if(isException) RealTime_Panel.resetTable(RealTime_Panel.getResultTable());					
				RealTime_Panel.getPacketLog().append(log);
				RealTime_Panel.scrollUp();
			} else if (agentType.equalsIgnoreCase("SimpleValueScan")) {
				SimpleValueScan_Panel.getPacketLog().append(log);
				SimpleValueScan_Panel.scrollUp();
			} else if (agentType.equalsIgnoreCase("ExceptionScan")) {
				ExceptionScan_Panel.getPacketLog().append(log);
				ExceptionScan_Panel.scrollUp();
			}

		} catch (NullPointerException e) {

			if (agentType.equalsIgnoreCase("ModbusAgent")) {
				ModbusAgent_Panel.resetTable(ModbusAgent_Panel.getResultTable());
				ModbusAgent_Panel.scrollUp();
			}else if (agentType.equalsIgnoreCase("RealTime")) {
				RealTime_Panel.resetTable(RealTime_Panel.getResultTable());
				RealTime_Panel.scrollUp();
			}else if (agentType.equalsIgnoreCase("SimpleValueScan")) {
				SimpleValueScan_Panel.scrollUp();
			} else if (agentType.equalsIgnoreCase("ExceptionScan")) {
				ExceptionScan_Panel.scrollUp();
			}
			return;
		}
	}
	
	// ���� Ÿ�� �ƿ� ó��
	public static void responseTimeout(Exception e) throws SocketException{
		// ���� Ÿ�Ӿƿ��� Exception�� �߻��Ͽ����� ������ ���� �� ����
		// client.isConnected() : true
		// client.isClosed() : false
		
		// Ŭ���̾�Ʈ ���� : ���� Ÿ�Ӿƿ� ī��Ʈ ����
		ClientSocket.incrementTimeoutCount();

		// 3�� �������� ���κ��� ������ ���� �� Ŀ�ؼ� ����
		// ���� Ÿ�Ӿƿ� ī��Ʈ : �Ӱ� ��(3) ���� �� ���� Ŀ�ؼ� ���� �� ��� ���� �缳��
		if ((ClientSocket.getCurrentTimeoutCount()) == ClientSocket.getLimitTimeoutCount()) {
			ClientSocket.setState(ClientSocket.NODE_CONDITION_CONNCLOSE); // Ŭ���̾�Ʈ ���� : ��������
			throw new SocketException();
		}
		
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Response Timeout</font>\n");
		msg.append("TX�� ���������� �����Ͽ����� ��񿡼� 5�� ���� �������� ������ ������ �����ϴ�" + Util.separator + "\n");
		msg.append("Ÿ�Ӿƿ� ī��Ʈ�� �Ӱ谪�� �����ϸ� ��ſ��� ���°� �˴ϴ�\n\n");
		msg.append("Ÿ�Ӿƿ� ī��Ʈ �Ӱ谪 : " + ClientSocket.getLimitTimeoutCount() + "\n");
		msg.append("���� Ÿ�Ӿƿ� ī��Ʈ : " + ClientSocket.getCurrentTimeoutCount() + "\n\n");
		msg.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
		
		if (ClientSocket.isCurrentConnected(clientSocket)) {
			ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING); // Ŭ���̾�Ʈ ���� : ���� �����
			Util.showMessage(msg.toString(), JOptionPane.OK_OPTION);								 								
		}
	}
	
	
	// ���� Ÿ�� �ƿ� ó��
	public static void responseTimeoutDoNothing(Exception e) throws SocketException{
		// ���� Ÿ�Ӿƿ��� Exception�� �߻��Ͽ����� ������ ���� �� ����
		// client.isConnected() : true
		// client.isClosed() : false
		
		// Ŭ���̾�Ʈ ���� : ���� Ÿ�Ӿƿ� ī��Ʈ ����
		ClientSocket.incrementTimeoutCount();

		// 3�� �������� ���κ��� ������ ���� �� Ŀ�ؼ� ����
		// ���� Ÿ�Ӿƿ� ī��Ʈ : �Ӱ� ��(3) ���� �� ���� Ŀ�ؼ� ���� �� ��� ���� �缳��
//		if ((ClientSocket.getCurrentTimeoutCount()) == ClientSocket.getLimitTimeoutCount()) {
//			ClientSocket.setState(ClientSocket.NODE_CONDITgION_CONNCLOSE); // Ŭ���̾�Ʈ ���� : ��������
//			throw new SocketException();
//		}
		
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Response Timeout</font>\n");
		msg.append("TX�� ���������� �����Ͽ����� ��񿡼� 5�� ���� �������� ������ ������ �����ϴ�" + Util.separator + "\n\n");				
		msg.append("���� Ÿ�Ӿƿ� ī��Ʈ : " + ClientSocket.getCurrentTimeoutCount() + "\n\n");
		msg.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
		
		if(ClientSocket.getCurrentTimeoutCount() >= 5) {
			ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // Ŭ���̾�Ʈ ���� : ��� ����
			Util.showMessage(msg.toString(), JOptionPane.OK_OPTION);
		}else if (ClientSocket.isCurrentConnected(clientSocket)) {
			ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING); // Ŭ���̾�Ʈ ���� : ���� �����
			Util.showMessage(msg.toString(), JOptionPane.OK_OPTION);								 								
		}
	}
	
	
	
	// ���� Ÿ�Ӿƿ� ī��Ʈ�� �Ӱ谪�� �����Ͽ��� ��
	public static void maxResponseTimeoutCount(Exception e) throws IOException{
		// ���� Ÿ�Ӿƿ��� �Ӱ谪�� �����Ͽ� Ŀ�ؼ� ����
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Max Response Timeout Count</font>\n");
		msg.append("Ÿ�Ӿƿ� ī��Ʈ�� �Ӱ谪�� �����Ͽ� ����� �����մϴ�" + Util.separator + "\n");
		msg.append("Ŀ�ؼ� ���� : ������ ��� ������ �����մϴ�\n\n");				
		msg.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
		Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
		clientSocket.close();	
	}
	
	
	// TX ���� �� ���� ����� ������ �������� ��
	public static void waitingLostConnection(Exception e) throws IOException{
		// ��� �� ���(����)������ ������ ������� ���� - TX�� �۽��� �����ϰ� RX Response ���� Ÿ�Ӿƿ� �ȿ�(5second)��
		// ��� ������ ���� ����
		ClientSocket.setState(ClientSocket.NODE_CONDITION_DISCONNECTED); // Ŭ���̾�Ʈ ���� : ���� ����				
		clientSocket.close();
		
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Waiting Lost Connection</font>\n");
		msg.append("������ ����ϴ� �� ��񿡼� Ŀ�ؼ��� ������Ƚ��ϴ�" + Util.separator + "\n\n");		
		msg.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
		Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	
	// ���� ���� ���� ���� ��
	public static void serverSocketClosed(Exception e) throws IOException {
		/**
		 * ���� Ÿ�Ӿƿ��� ���� ������ �������ٰ� ��� ������ �缳���� �� ��Ź���� ModbusRTU ���ý� TX �۽��� �Ϸ��Ͽ��� ������ ������
		 * ���������Ƿ�, ���ܰ� �߻��Ѵ�. �׷��� ��Ź���� ModbusTCP�� ������ �� ���������� �۵��Ѵ�.
		 * 
		 * ���� �� ������ read�ϰų� write �ϴ� ������ ���� �� ���� ��Ʈ���� ���������� SocketException �߻�
		 *
		 * 2020-11-13 Ŀ�ؼ��� �����ϰ� �����ð����� �ƹ��� ��ŵ� ���������� �ڵ����� Ŀ�ؼ��� ���� (Modbus Slave) TCP
		 * Analyzer�� Server�� ����ϰ� �ƹ��� ��ŵ� ���������� Ŀ�ؼ��� ������ �ʴ°����� ���� �����̺� �� ���� �ɼ����� ���δ�
		 */
		ClientSocket.setState(ClientSocket.NODE_CONDITION_CONNCLOSE); // Ŭ���̾�Ʈ ���� : ��������
		
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Server Socket Closed</font>\n");
		msg.append("����� ������ �����־� ���� Ŀ�ؼ��� ���������ϴ�" + Util.separator + "\n\n");
		msg.append("Ŀ�ؼ� ���� : ������ ��� ������ �����մϴ�\n\n");
		msg.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
		Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
		clientSocket.close();			
	}
	
	public static void unknownException(Exception e) throws IOException {
		// ������ ����ϴ� �� �������� Ŀ�ؼ��� ���� ��		
		// Ŭ���̾�Ʈ ���� : ���� ����
		ClientSocket.setState(ClientSocket.NODE_CONDITION_DISCONNECTED);
		
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Unhandled Exception</font>\n");
		msg.append("�������� ���� ���� �߻����� ���� ��� ����\n\n");
		msg.append("Ŀ�ؼ� ���� : ������ ��� ������ �����մϴ�" + Util.separator + "\n\n");			
		msg.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
		Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
		clientSocket.close();	
	}
	
	public static void printSocketState(Socket clientSocket) {
		System.out.println();
		System.out.println("isBound : " + clientSocket.isBound());
		System.out.println("isClosed : " + clientSocket.isClosed());
		System.out.println("isConnected : " + clientSocket.isConnected());
		System.out.println("isInputShutdown : " + clientSocket.isInputShutdown());
		System.out.println("isOutputShutdown : " + clientSocket.isOutputShutdown());
		System.out.println();
	}

}
