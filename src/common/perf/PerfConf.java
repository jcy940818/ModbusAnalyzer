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
    /** ���� ���� - ������Ʈ�� �����ϴ� ���� (1) */
    public static final int PERF_TYPE_AGENT = 1;

    /** ���� ���� - SNMP�� �����ϴ� ���� (2) */
    public static final int PERF_TYPE_SNMP = 2;

    /** ���� ���� - ��Ʈ ���� �ð� (3) */
    public static final int PERF_TYPE_PORT = 3;

    /** ���� ���� - ����Ŭ DB (4) */
    public static final int PERF_TYPE_ORACLE = 4;

    /** ���� ���� - RTU ���� (5) */
    public static final int PERF_TYPE_FMS_CONTACT = 5;

    /** ���� ���� - RTU �ø��� (6) */
    public static final int PERF_TYPE_FMS_SERIAL = 6;

    /** ���� ���� - ������ ���� (7) */
    public static final int PERF_TYPE_PRINTER = 7;

    /** ���� ���� - TCP �ø���(8) */
    public static final int PERF_TYPE_FMS_TCP = 8;

    /** ���� ���� - zigbee (9) */
    public static final int PERF_TYPE_FMS_ZB = 9;

    /** ���� ���� - UDP ����(10) */
    public static final int PERF_TYPE_FMS_UDP = 10;

    /** ���� ���� - BACnet ���� */
    public static final int PERF_TYPE_BACNET = 11;

    /** ���� ���� - File ���� */
    public static final int PERF_TYPE_FILE = 12;

    /** ���� ���� - PSM ���� */
    public static final int PERF_TYPE_PSM = 13;

    /** ���� ���� - DB ���� */
    public static final int PERF_TYPE_DB = 14;

    /** ���� ���� - Modbus ���� */
    public static final int PERF_TYPE_MODBUS = 15;

    /** ���� ���� - iLON ���� */
    public static final int PERF_TYPE_ILON = 16;

    /** ���� ���� - LNS DDE ���� */
    public static final int PERF_TYPE_LNS_DDE = 17;

    // 2012.03.36 kering
    // ���� ���� - PLC ����
    public static final int PERF_TYPE_PLC = 18;

    // ���� ����
    public static final int PERF_TYPE_VIRTUAL = 19;

    // IPMI
    public static final int PERF_TYPE_IPMI = 20;
    
    // MANAGER
    public static final int PERF_TYPE_MANAGER = 21;
    
    // ���� ���� ����
    public static final int PERF_TYPE_VIRTUAL_ACCUM = 22;
    // ���� ���� ����(���·�)
    public static final int PERF_TYPE_VIRTUAL_POWER = 23;
    // ���� ���� ����(�����·�)
    public static final int PERF_TYPE_VIRTUAL_POWER_MONTH = 24;
    // ���� SQL ����
    public static final int PERF_TYPE_VIRTUAL_SQL = 25;
    
    //����Ʈ����
    public static final int PERF_TYPE_REPORT = 26;
    
    // MUX
    public static final int PERF_TYPE_MUX = 27;
    public static final int PERF_TYPE_UDP_RECV = 28;
    
    // Interface - Rest ����
    public static final int PERF_TYPE_REST = 29;
    
    public static final int PERF_TYPE_MIDAS_CON = 30;
    
    public static final int PERF_TYPE_MIDAS_AP = 31;
    
    public static final int PERF_TYPE_VIRTUAL_RESET_COUNTER = 32;
    
    /** �� ���� ���� ���� */
    public static final int PERF_TYPE_RACKGUARD = 33;
    
    /** ����(��ȭ) �ð� ���� */
    public static final int PERF_TYPE_VIRTUAL_TIME = 34;
    
    /** �� ��� �����ð� */
    public static final int PERF_TYPE_VIRTUAL_MEASURETIME = 36;

    /** ������Ʈ ���� ���� - �⺻ ���� (1) */
    public static final int AGENT_PERF_BASIC = 1;

    /** ������Ʈ ���� ���� - ���� ������Ʈ (2) */
    public static final int AGENT_PERF_OBJECT = 2;

    /** ������Ʈ ���� ���� - ���ø����̼� ���� (3) */
    public static final int AGENT_PERF_APP = 3;

    /** ������Ʈ ���� ���� - ������ ��Ǯ�� ���� (4) */
    public static final int AGENT_PERF_SPOOLER = 4;

    /** ���� ������ ���� - ������ ������ (1) */
    public static final int DATA_FORMAT_DIGITAL = 1;

    /** ���� ������ ���� - ���� ������ (2) */
    public static final int DATA_FORMAT_STATUS = 2;

    /** ���� ������ ���� - ���� ������ (3) */
    public static final int DATA_FORMAT_MEASURE = 3;

    /** ������ ���� ��� - ���� ���� ������ ���̺� ���� (1) */
    public static final int DATA_SAVING_MODE_MULTI = 1;

    /** ������ ���� ��� - ��� ���� �����͸� �ϳ��� ���̺� ���� (2) */
    public static final int DATA_SAVING_MODE_SINGLE = 2;

    /** RTU ä�� ���� - AI (Analog Input) (1) */
    public static final int RTU_CHANNEL_AI = 1;

    /** RTU ä�� ���� - DI (Digital Input) (2) */
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
     * ���� ������ �˾ƿ´�.
     */
    public int getPerfSize() {

        return this.perfItems.length;
    }

    public PerfItem[] getPerfItems() {

        return this.perfItems;
    }

    /**
     * ���� �̸� ��� ��������
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
