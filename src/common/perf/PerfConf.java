package common.perf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class PerfConf {
    /** 성능 종류 - 에이전트로 수집하는 성능 (1) */
    public static final int PERF_TYPE_AGENT = 1;

    /** 성능 종류 - SNMP로 수집하는 성능 (2) */
    public static final int PERF_TYPE_SNMP = 2;

    /** 성능 종류 - 포트 응답 시간 (3) */
    public static final int PERF_TYPE_PORT = 3;

    /** 성능 종류 - 오라클 DB (4) */
    public static final int PERF_TYPE_ORACLE = 4;

    /** 성능 종류 - RTU 접점 (5) */
    public static final int PERF_TYPE_FMS_CONTACT = 5;

    /** 성능 종류 - RTU 시리얼 (6) */
    public static final int PERF_TYPE_FMS_SERIAL = 6;

    /** 성능 종류 - 프린터 성능 (7) */
    public static final int PERF_TYPE_PRINTER = 7;

    /** 성능 종류 - TCP 시리얼(8) */
    public static final int PERF_TYPE_FMS_TCP = 8;

    /** 성능 종류 - zigbee (9) */
    public static final int PERF_TYPE_FMS_ZB = 9;

    /** 성능 종류 - UDP 연결(10) */
    public static final int PERF_TYPE_FMS_UDP = 10;

    /** 성능 종류 - BACnet 연결 */
    public static final int PERF_TYPE_BACNET = 11;

    /** 성능 종류 - File 접근 */
    public static final int PERF_TYPE_FILE = 12;

    /** 성능 종류 - PSM 연결 */
    public static final int PERF_TYPE_PSM = 13;

    /** 성능 종류 - DB 접근 */
    public static final int PERF_TYPE_DB = 14;

    /** 성능 종류 - Modbus 연결 */
    public static final int PERF_TYPE_MODBUS = 15;

    /** 성능 종류 - iLON 연결 */
    public static final int PERF_TYPE_ILON = 16;

    /** 성능 종류 - LNS DDE 연결 */
    public static final int PERF_TYPE_LNS_DDE = 17;

    // 2012.03.36 kering
    // 성능 종류 - PLC 연결
    public static final int PERF_TYPE_PLC = 18;

    // 가상 성능
    public static final int PERF_TYPE_VIRTUAL = 19;

    // IPMI
    public static final int PERF_TYPE_IPMI = 20;
    
    // MANAGER
    public static final int PERF_TYPE_MANAGER = 21;
    
    // 가상 누적 성능
    public static final int PERF_TYPE_VIRTUAL_ACCUM = 22;
    // 가상 누적 성능(전력량)
    public static final int PERF_TYPE_VIRTUAL_POWER = 23;
    // 가상 누적 성능(월전력량)
    public static final int PERF_TYPE_VIRTUAL_POWER_MONTH = 24;
    // 가상 SQL 성능
    public static final int PERF_TYPE_VIRTUAL_SQL = 25;
    
    //리포트성능
    public static final int PERF_TYPE_REPORT = 26;
    
    // MUX
    public static final int PERF_TYPE_MUX = 27;
    public static final int PERF_TYPE_UDP_RECV = 28;
    
    // Interface - Rest 성능
    public static final int PERF_TYPE_REST = 29;
    
    public static final int PERF_TYPE_MIDAS_CON = 30;
    
    public static final int PERF_TYPE_MIDAS_AP = 31;
    
    public static final int PERF_TYPE_VIRTUAL_RESET_COUNTER = 32;
    
    /** 랙 도어 연동 성능 */
    public static final int PERF_TYPE_RACKGUARD = 33;
    
    /** 발전(변화) 시간 수집 */
    public static final int PERF_TYPE_VIRTUAL_TIME = 34;
    
    /** 일 장비 측정시간 */
    public static final int PERF_TYPE_VIRTUAL_MEASURETIME = 36;

    /** 에이전트 성능 종류 - 기본 성능 (1) */
    public static final int AGENT_PERF_BASIC = 1;

    /** 에이전트 성능 종류 - 성능 오브젝트 (2) */
    public static final int AGENT_PERF_OBJECT = 2;

    /** 에이전트 성능 종류 - 어플리케이션 성능 (3) */
    public static final int AGENT_PERF_APP = 3;

    /** 에이전트 성능 종류 - 프린터 스풀러 상태 (4) */
    public static final int AGENT_PERF_SPOOLER = 4;

    /** 수집 데이터 형식 - 디지털 데이터 (1) */
    public static final int DATA_FORMAT_DIGITAL = 1;

    /** 수집 데이터 형식 - 상태 데이터 (2) */
    public static final int DATA_FORMAT_STATUS = 2;

    /** 수집 데이터 형식 - 성능 데이터 (3) */
    public static final int DATA_FORMAT_MEASURE = 3;

    /** 데이터 저장 방식 - 성능 별로 각각의 테이블에 저장 (1) */
    public static final int DATA_SAVING_MODE_MULTI = 1;

    /** 데이터 저장 방식 - 모든 성능 데이터를 하나의 테이블에 저장 (2) */
    public static final int DATA_SAVING_MODE_SINGLE = 2;

    /** RTU 채널 종류 - AI (Analog Input) (1) */
    public static final int RTU_CHANNEL_AI = 1;

    /** RTU 채널 종류 - DI (Digital Input) (2) */
    public static final int RTU_CHANNEL_DI = 2;

    private Document doc;
    private PerfItem[] perfItems;

    public PerfConf(String filename) throws FileNotFoundException {

        init(filename);
        NodeList perfNodes = doc.getElementsByTagName("perfitem");
        perfItems = new PerfItem[perfNodes.getLength()];
        for (int i = 0; i < perfNodes.getLength(); i++) {
            PerfItem perf = new PerfItem();
            NodeList perfProps = perfNodes.item(i).getChildNodes();
            for (int j = 0; j < perfProps.getLength(); j++) {
                Node node = perfProps.item(j);
                String nodeName = node.getNodeName();
                if (nodeName.equals("command")) {
                    perf.commandCode = node.getFirstChild().getNodeValue();
                } else if (nodeName.equals("platform")) {
                    node = node.getFirstChild();
                    perf.platform = node == null ? null : node.getNodeValue();
                } else if (nodeName.equals("displayname")) {
                    perf.displayName = node.getFirstChild().getNodeValue();
                } else if (nodeName.equals("interval")) {
                    perf.checkInterval = Integer.parseInt(node.getFirstChild().getNodeValue());
                } else if (nodeName.equals("measure")) {
                    node = node.getFirstChild();
                    if (node == null) {
                        perf.measure = "";
                    } else {
                        perf.measure = node.getNodeValue();
                    }
                } else if (nodeName.equals("counter")) {
                    perf.counter = node.getFirstChild().getNodeValue();
                } else {
                    continue;
                }
                perfItems[i] = perf;
            }
        }
    }

    private void init(String filename) throws FileNotFoundException {

        try {
            // parse an XML file into document
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            InputSource input = new InputSource(new FileReader(new File(filename)));
            doc = docBuilder.parse(input);
            // normalize text representation
            // doc.getDocumentElement().normalize();
        } catch (SAXParseException errSPE) {
            System.out.println(
                "** Parsing error, filename " + filename + ", line " + errSPE.getLineNumber() + ", uri " + errSPE.getSystemId() + "   " + errSPE.getMessage());
            // print stack trace as below
        } catch (SAXException errSE) {
            Exception x = errSE.getException();
            ((x == null) ? errSE : x).printStackTrace();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 성능 갯수를 알아온다.
     */
    public int getPerfSize() {

        return this.perfItems.length;
    }

    public PerfItem[] getPerfItems() {

        return this.perfItems;
    }

    /**
     * 성능 이름 목록 가져오기
     */
    public String[] getPerfNames() {

        int size = getPerfSize();
        String[] nodeNames = new String[size];
        NodeList nodes = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i <= size; i++) {
            Node node = nodes.item(i);
            nodeNames[i] = node.getNodeName();
        }
        return nodeNames;
    }
    
}
