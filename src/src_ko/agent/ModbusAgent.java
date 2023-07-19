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
		
	// 클라이언트 소켓
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
					// Modbus RTU 통신					
					if(clientSocket == null) {						
						return null;
					}
					
					// 현재 클라이언트 소켓이 서버와 연결된 상태인지 검사					
						try {											
							// TX 전송 후 로그 출력
							ModbusSender.sendRTU(clientSocket, tx);
							ModbusAgent.printTX(tx, tx.getAgentType());													
							
							// 클라이언트 소켓 : TX 전송 완료 후 응답 대기중 
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // 클라이언트 소켓 : 통신 오류							
							}else {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING);	
							}
							
							// RX 수신 후 로그 출력
							rx = ModbusReceiver.reciveRTU(clientSocket, tx);
							ModbusAgent.printRX(rx, tx.getAgentType());
														
							// 클라이언트 소켓 : 통신중 (요청패킷에 대한 응답패킷을 수신함)
							ClientSocket.setState(ClientSocket.NODE_CONDITION_REGULAR);

							// 클라이언트 소켓 : 응답패킷 수신시 응답 타임아웃 카운트 초기화
							ClientSocket.resetTimeoutCount();

							return rx;
						} catch (SocketTimeoutException e) {
							// 응답 패킷 수신하였지만 처리 할 수 없는 패킷이 있을 경우 패킷 내용 출력
							ModbusAgent.printRX(rx, tx.getAgentType());
							
							// Real-Time Monitoring 기능은 타임아웃 체크를 하지않는다
							if(tx.getAgentType().equalsIgnoreCase("ModbusAgent")) {
								ModbusAgent.responseTimeoutDoNothing(e);
							}else {
								ClientSocket.incrementTimeoutCount();
							}							
							
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // 클라이언트 소켓 : 통신 오류
							}
							
							return null;
							
						} catch (SocketException e) {
							// 장비의 소켓이 닫혀있음
							ModbusAgent.serverSocketClosed(e);
							return null;
						}
						
				} else {
					// Modbus TCP 통신					
					if(clientSocket == null) {						
						return null;
					}					
					// 현재 클라이언트 소켓이 서버와 연결된 상태인지 검사
					
						try {
//							// TX 전송 후 로그 출력
							ModbusSender.sendTCP(clientSocket, tx);
							ModbusAgent.printTX(tx, tx.getAgentType());
							
							// 클라이언트 소켓 : TX 전송 완료 후 응답 대기중 
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // 클라이언트 소켓 : 통신 오류							
							}else {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING);
							}
							
							// RX 수신 후 로그 출력
							rx = ModbusReceiver.reciveTCP(clientSocket, tx);
							ModbusAgent.printRX(rx, tx.getAgentType());
							
							// 클라이언트 소켓 : 통신중 (요청패킷에 대한 응답패킷을 수신함)
							ClientSocket.setState(ClientSocket.NODE_CONDITION_REGULAR);

							// 클라이언트 소켓 : 응답패킷 수신시 응답 타임아웃 카운트 초기화
							ClientSocket.resetTimeoutCount();
							
							return rx;							
						} catch (SocketTimeoutException e) {
							// 응답 패킷 수신하였지만 처리 할 수 없는 패킷이 있을 경우 패킷 내용 출력
							ModbusAgent.printRX(rx, tx.getAgentType());
							
							// Real-Time Monitoring 기능은 타임아웃 체크를 하지않는다
							if(tx.getAgentType().equalsIgnoreCase("ModbusAgent")) {
								ModbusAgent.responseTimeoutDoNothing(e);
							}else {
								ClientSocket.incrementTimeoutCount();
							}							
							
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // 클라이언트 소켓 : 통신 오류
							}
							
							return null;
							
						} catch (SocketException e) {
							// 장비의 소켓이 닫혀있음
							ModbusAgent.serverSocketClosed(e);
							return null;
						}
					}
								
			} catch (EOFException e) {
				// TX 전송 후 RX 대기 중 연결 끊김
				ModbusAgent.waitingLostConnection(e);
				return null;
				
			} catch (SocketException e) {
				// 응답 타임아웃 카운트가 임계값에 도달
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
					// Modbus RTU 통신					
					if(clientSocket == null) {						
						return null;
					}
					
					// 현재 클라이언트 소켓이 서버와 연결된 상태인지 검사					
						try {											
							// TX 전송 후 로그 출력
							ModbusSender.sendRTU(clientSocket, tx);
							ModbusAgent.printTX(tx, tx.getAgentType());
							
							// 클라이언트 소켓 : TX 전송 완료 후 응답 대기중 
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // 클라이언트 소켓 : 통신 오류							
							}else {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING);
							}
							
							// RX 수신 후 로그 출력
							rx = ModbusReceiver.reciveRTU(clientSocket, tx);
							ModbusAgent.printRX(rx, tx.getAgentType());
														
							// 클라이언트 소켓 : 통신중 (요청패킷에 대한 응답패킷을 수신함)
							ClientSocket.setState(ClientSocket.NODE_CONDITION_REGULAR);

							// 클라이언트 소켓 : 응답패킷 수신시 응답 타임아웃 카운트 초기화
							ClientSocket.resetTimeoutCount();

							return rx;
						} catch (SocketTimeoutException e) {
							// 응답 패킷 수신하였지만 처리 할 수 없는 패킷이 있을 경우 패킷 내용 출력
							ModbusAgent.printRX(rx, tx.getAgentType());
//							ModbusAgent.responseTimeout(e);																		
							
							rx = new RX_Info();
							rx.setTxInfo(tx);
							rx.setScanResult("Response Timeout");
							
							ClientSocket.incrementTimeoutCount();
							
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // 클라이언트 소켓 : 통신 오류							
							}
							
							return rx;
							
						} catch (SocketException e) {
							// 장비의 소켓이 닫혀있음
							ModbusAgent.serverSocketClosed(e);
							return null;
						}
						
				} else {
					// Modbus TCP 통신					
					if(clientSocket == null) {						
						return null;
					}					
					// 현재 클라이언트 소켓이 서버와 연결된 상태인지 검사
					
						try {
//							// TX 전송 후 로그 출력
							ModbusSender.sendTCP(clientSocket, tx);
							ModbusAgent.printTX(tx, tx.getAgentType());
							
							// 클라이언트 소켓 : TX 전송 완료 후 응답 대기중 
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // 클라이언트 소켓 : 통신 오류							
							}else {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING);
							}
							
							// RX 수신 후 로그 출력
							rx = ModbusReceiver.reciveTCP(clientSocket, tx);
							ModbusAgent.printRX(rx, tx.getAgentType());
							
							// 클라이언트 소켓 : 통신중 (요청패킷에 대한 응답패킷을 수신함)
							ClientSocket.setState(ClientSocket.NODE_CONDITION_REGULAR);

							// 클라이언트 소켓 : 응답패킷 수신시 응답 타임아웃 카운트 초기화
							ClientSocket.resetTimeoutCount();
							
							return rx;							
						} catch (SocketTimeoutException e) {
							// 응답 패킷 수신하였지만 처리 할 수 없는 패킷이 있을 경우 패킷 내용 출력
							ModbusAgent.printRX(rx, tx.getAgentType());
//							ModbusAgent.responseTimeout(e);
							
							rx = new RX_Info();
							rx.setTxInfo(tx);
							rx.setScanResult("Response Timeout");
							
							ClientSocket.incrementTimeoutCount();
							
							if(ClientSocket.getCurrentTimeoutCount() >= 5) {
								ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // 클라이언트 소켓 : 통신 오류							
							}
							
							return rx;
							
						} catch (SocketException e) {
							// 장비의 소켓이 닫혀있음
							ModbusAgent.serverSocketClosed(e);
							return null;
						}
					}
								
			} catch (EOFException e) {
				// TX 전송 후 RX 대기 중 연결 끊김
				ModbusAgent.waitingLostConnection(e);
				return null;
				
			} catch (SocketException e) {
				// 응답 타임아웃 카운트가 임계값에 도달
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
			
			// 포인트에 인덱스를 지정 후 파싱
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
			
			// 요청 패킷 전송전에 포인트들의 데이터를 초기화
			Collections.sort(pointList);
			ModbusWatchPoint.pointDataClear(pointList);
			
			// 포인트 리스트 저장
			ModbusMonitorFrame.pointList = pointList;
			
			// 모드버스 모니터 프레임 패킷 로그 초기화
			ModbusMonitorFrame.cleaerLog();
			
			for(ReadFunctionGroup fcGroup : functionGroupList) {
				
				// 요청 패킷 처리중 사용자의 요청에 의해 통신이 중지되었을 경우
				if(!ModbusMonitor.isRunning) {
					return;
				}
				
				String txPacket = monitor.sendCommand(fcGroup, clientSocket);
				tx = new TX_Info();
				tx.setContent(txPacket);
				tx = (monitor.getType() == ModbusMonitor.TYPE_RTU) ? new TX_Analyzer().rtuAnalysis(tx) : new TX_Analyzer().tcpAnalysis(tx);
				
				String modbusType = (monitor.getType() == ModbusMonitor.TYPE_RTU) ? "Modbus-RTU" : "Modbus-TCP";
				StringBuilder sb = new StringBuilder("┌─────── 요청 내용 ────────┐");
				sb.append(System.lineSeparator());
				sb.append(String.format("│ 모드버스 타입 : %s\t│", modbusType));
				sb.append(System.lineSeparator());
				sb.append(String.format("│ 장비번호 : %s번\t\t│", tx.getUnitId()));
				sb.append(System.lineSeparator());
				sb.append(String.format("│ 기능코드 : %d\t\t\t│", tx.getFunctionCode()));
				sb.append(System.lineSeparator());
				sb.append(String.format("│ 요청 시작주소 (Modbus DEC) : %s\t│", tx.getModbusAddrString()));
				sb.append(System.lineSeparator());
				sb.append(String.format("│ 요청 시작주소 (Register DEC) : %s\t│", tx.getStartAddress()));
				sb.append(System.lineSeparator());
				sb.append(String.format("│ 요청 시작주소 (Register HEX) : %s\t│", tx.getRegisterAddrHexString()));
				sb.append(System.lineSeparator());
				sb.append(String.format("│ 요청 개수 : %s\t\t│", tx.getRequestCount()));
				sb.append(System.lineSeparator());
				sb.append(String.format("└────────────────────┘", tx.getRequestCount()));				
				
				ModbusMonitorFrame.writeLog(sb.toString());
				ModbusMonitorFrame.writeLog(Timer.getServerTime() + " [ TX ] : " + txPacket);				
				
				
				// 클라이언트 소켓 : TX 전송 완료 후 응답 대기중
				if(ClientSocket.getCurrentTimeoutCount() >= 5) {
					ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // 통신 오류
				}else {
					ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING); // 응답 대기중
				}
				
				String rxPacket = null;
				try {
					rxPacket = monitor.parseResponsePacket(fcGroup, clientSocket);

				}catch (SocketTimeoutException e) {
					// 타임아웃시 해당 요청은 무효처리 후 다음 요청 전송
					ModbusMonitorFrame.writeLog(Timer.getServerTime() + " [ Timeout ] : " + e.getMessage());
					ModbusMonitorFrame.writeLog(System.lineSeparator() + System.lineSeparator() + System.lineSeparator() + System.lineSeparator());
					
					// 클라이언트 소켓 : 통신 오류
					ClientSocket.incrementTimeoutCount();
					if(ClientSocket.getCurrentTimeoutCount() >= 5) ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR);
					continue;
				}
				
				ModbusMonitorFrame.writeLog(Timer.getServerTime() + " [ RX ] : " + rxPacket);
				
				rx = new RX_Info();
				rx.setTxInfo(tx);
				rx.setContent(rxPacket);
				rx = (monitor.getType() == ModbusMonitor.TYPE_RTU) ? new RX_Analyzer().rtuAnalysis(rx) : new RX_Analyzer().tcpAnalysis(rx);
				
				// 데이터 불일치 체크
				String content = ExceptionProvider.getCompareTxRxString(tx, rx);
				if(content != null) {
					ModbusMonitorFrame.writeLog(content);
				}
				
				// ( TX 요청 개수 * 2 == RX 데이터 길이 ) 체크
				String lengthCheck = ExceptionProvider.getRxLengthCheckResult(tx, rx);
				if(lengthCheck != null) {
					ModbusMonitorFrame.writeLog(lengthCheck);
				}
				
				// 예외(Exception) 응답 체크
				String error = RX_Info.getRxHandleContent(rx);
				if(!error.equalsIgnoreCase("") && error.length() >= 1) {
					ModbusMonitorFrame.writeLog(error);
				}
				
				// 데이터 로그 기록
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
				
				// 클라이언트 소켓 : 통신중 (요청패킷에 대한 응답패킷을 수신함)
				ClientSocket.setState(ClientSocket.NODE_CONDITION_REGULAR);

				// 클라이언트 소켓 : 응답패킷 수신시 응답 타임아웃 카운트 초기화
				ClientSocket.resetTimeoutCount();
			}
			
			return;
							
		} catch (EOFException e) {
			// TX 전송 후 RX 대기 중 연결 끊김
			ModbusAgent.waitingLostConnection(e);
			return;
			
		}catch (SocketTimeoutException e) {	
			ClientSocket.incrementTimeoutCount();
			
			if(ClientSocket.getCurrentTimeoutCount() >= 5) {
				ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // 클라이언트 소켓 : 통신 오류
			}
			return;
			
		} catch (SocketException e) {
			// 장비의 소켓이 닫혀있음
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
				// ModbusAgent가 정상적으로 처리 할 수 없는 응답 패킷
				rxPacketType = "Unprocessable";
				rxPacket = ModbusAgent.getTempRxPacket();
			}

			// 전송받은 패킷이 없다면 로그에 아무것도 출력하지 않고 결과 테이블을 초기화
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
	
	// 응답 타임 아웃 처리
	public static void responseTimeout(Exception e) throws SocketException{
		// 응답 타임아웃로 Exception이 발생하였지만 소켓은 연결 된 상태
		// client.isConnected() : true
		// client.isClosed() : false
		
		// 클라이언트 소켓 : 응답 타임아웃 카운트 증가
		ClientSocket.incrementTimeoutCount();

		// 3번 연속으로 장비로부터 응답이 없을 시 커넥션 해제
		// 응답 타임아웃 카운트 : 임계 값(3) 도달 시 소켓 커넥션 해제 및 통신 세팅 재설정
		if ((ClientSocket.getCurrentTimeoutCount()) == ClientSocket.getLimitTimeoutCount()) {
			ClientSocket.setState(ClientSocket.NODE_CONDITION_CONNCLOSE); // 클라이언트 소켓 : 접속종료
			throw new SocketException();
		}
		
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Response Timeout</font>\n");
		msg.append("TX를 성공적으로 전송하였지만 장비에서 5초 동안 정상적인 모드버스 응답이 없습니다" + Util.separator + "\n");
		msg.append("타임아웃 카운트가 임계값에 도달하면 통신오류 상태가 됩니다\n\n");
		msg.append("타임아웃 카운트 임계값 : " + ClientSocket.getLimitTimeoutCount() + "\n");
		msg.append("현재 타임아웃 카운트 : " + ClientSocket.getCurrentTimeoutCount() + "\n\n");
		msg.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
		
		if (ClientSocket.isCurrentConnected(clientSocket)) {
			ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING); // 클라이언트 소켓 : 응답 대기중
			Util.showMessage(msg.toString(), JOptionPane.OK_OPTION);								 								
		}
	}
	
	
	// 응답 타임 아웃 처리
	public static void responseTimeoutDoNothing(Exception e) throws SocketException{
		// 응답 타임아웃로 Exception이 발생하였지만 소켓은 연결 된 상태
		// client.isConnected() : true
		// client.isClosed() : false
		
		// 클라이언트 소켓 : 응답 타임아웃 카운트 증가
		ClientSocket.incrementTimeoutCount();

		// 3번 연속으로 장비로부터 응답이 없을 시 커넥션 해제
		// 응답 타임아웃 카운트 : 임계 값(3) 도달 시 소켓 커넥션 해제 및 통신 세팅 재설정
//		if ((ClientSocket.getCurrentTimeoutCount()) == ClientSocket.getLimitTimeoutCount()) {
//			ClientSocket.setState(ClientSocket.NODE_CONDITgION_CONNCLOSE); // 클라이언트 소켓 : 접속종료
//			throw new SocketException();
//		}
		
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Response Timeout</font>\n");
		msg.append("TX를 성공적으로 전송하였지만 장비에서 5초 동안 정상적인 모드버스 응답이 없습니다" + Util.separator + "\n\n");				
		msg.append("현재 타임아웃 카운트 : " + ClientSocket.getCurrentTimeoutCount() + "\n\n");
		msg.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
		
		if(ClientSocket.getCurrentTimeoutCount() >= 5) {
			ClientSocket.setState(ClientSocket.NODE_CONDITION_COMMERR); // 클라이언트 소켓 : 통신 오류
			Util.showMessage(msg.toString(), JOptionPane.OK_OPTION);
		}else if (ClientSocket.isCurrentConnected(clientSocket)) {
			ClientSocket.setState(ClientSocket.NODE_CONDITION_RESPONSE_WAITING); // 클라이언트 소켓 : 응답 대기중
			Util.showMessage(msg.toString(), JOptionPane.OK_OPTION);								 								
		}
	}
	
	
	
	// 응답 타임아웃 카운트가 임계값에 도달하였을 때
	public static void maxResponseTimeoutCount(Exception e) throws IOException{
		// 응답 타임아웃이 임계값에 도달하여 커넥션 해제
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Max Response Timeout Count</font>\n");
		msg.append("타임아웃 카운트가 임계값에 도달하여 통신을 종료합니다" + Util.separator + "\n");
		msg.append("커넥션 해제 : 장비와의 통신 소켓을 해제합니다\n\n");				
		msg.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
		Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
		clientSocket.close();	
	}
	
	
	// TX 전송 후 응답 대기중 접속이 끊어졌을 때
	public static void waitingLostConnection(Exception e) throws IOException{
		// 통신 중 장비(서버)측에서 연결을 끊어버린 상태 - TX를 송신을 성공하고 RX Response 응답 타임아웃 안에(5second)에
		// 장비가 접속을 끊은 상태
		ClientSocket.setState(ClientSocket.NODE_CONDITION_DISCONNECTED); // 클라이언트 소켓 : 접속 끊김				
		clientSocket.close();
		
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Waiting Lost Connection</font>\n");
		msg.append("응답을 대기하는 중 장비에서 커넥션을 끊어버렸습니다" + Util.separator + "\n\n");		
		msg.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
		Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
	}
	
	
	// 서버 소켓 닫혀 있을 때
	public static void serverSocketClosed(Exception e) throws IOException {
		/**
		 * 응답 타임아웃로 인해 소켓이 끊어졌다가 통신 세팅을 재설정할 때 통신방식을 ModbusRTU 선택시 TX 송신을 완료하여도 서버의 소켓이
		 * 닫혀있으므로, 예외가 발생한다. 그러나 통신방식을 ModbusTCP로 선택할 시 정상적으로 작동한다.
		 * 
		 * 서버 측 소켓을 read하거나 write 하는 시점에 서버 측 소켓 스트림이 닫혀있으면 SocketException 발생
		 *
		 * 2020-11-13 커넥션을 생성하고 일정시간동안 아무런 통신도 하지않으면 자동으로 커넥션이 끊김 (Modbus Slave) TCP
		 * Analyzer를 Server로 등록하고 아무런 통신도 하지않을시 커넥션이 끊기지 않는것으로 보아 슬레이브 측 고유 옵션으로 보인다
		 */
		ClientSocket.setState(ClientSocket.NODE_CONDITION_CONNCLOSE); // 클라이언트 소켓 : 접속종료
		
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Server Socket Closed</font>\n");
		msg.append("장비의 소켓이 닫혀있어 소켓 커넥션이 끊어졌습니다" + Util.separator + "\n\n");
		msg.append("커넥션 해제 : 장비와의 통신 소켓을 해제합니다\n\n");
		msg.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
		Util.showMessage(msg.toString(), JOptionPane.ERROR_MESSAGE);
		clientSocket.close();			
	}
	
	public static void unknownException(Exception e) throws IOException {
		// 응답을 대기하던 중 서버에서 커넥션을 끊을 때		
		// 클라이언트 소켓 : 접속 끊김
		ClientSocket.setState(ClientSocket.NODE_CONDITION_DISCONNECTED);
		
		StringBuilder msg = new StringBuilder();
		msg.append("<font color='red'>Unhandled Exception</font>\n");
		msg.append("예상하지 못한 예외 발생으로 인한 통신 종료\n\n");
		msg.append("커넥션 해제 : 장비와의 통신 소켓을 해제합니다" + Util.separator + "\n\n");			
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
