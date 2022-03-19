package common.perf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.XMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SnmpPerfConf {
    private Document doc;
    private String strFile;
    private String mibDir = null;
    private SnmpPerfItem[] snmpPerfItems;
    // 파일을 읽은 시각
    private long loadTime = System.currentTimeMillis();

    public SnmpPerfConf(String filename, String encoding) throws ConfigurationException, FileNotFoundException {
        init(filename);
        XMLConfiguration config = XmlUtils.createXMLConfiguration(filename, encoding);
        
        mibDir = doc.getElementsByTagName("mibdir").item(0).getFirstChild().getNodeValue();
        NodeList perfNodes = doc.getElementsByTagName("perfitem");
        snmpPerfItems = new SnmpPerfItem[perfNodes.getLength()];
        for (int i = 0; i < snmpPerfItems.length; i++) {
            snmpPerfItems[i] = new SnmpPerfItem();
            snmpPerfItems[i].displayName = config.getString("perfitem("+i+").displayname");
            snmpPerfItems[i].oid = config.getString("perfitem("+i+").oid");
            snmpPerfItems[i].interval = config.getInt("perfitem("+i+").interval", 60);
            snmpPerfItems[i].units = config.getString("perfitem("+i+").units", "");
            snmpPerfItems[i].expression = config.getString("perfitem("+i+").expression", "x");
            snmpPerfItems[i].dataFormat = config.getInt("perfitem("+i+").data[@format]", PerfConf.DATA_FORMAT_MEASURE);
            if (snmpPerfItems[i].dataFormat == PerfConf.DATA_FORMAT_DIGITAL) {
                snmpPerfItems[i].binLabel = new String[2];
                snmpPerfItems[i].binLabel[0] = config.getString("perfitem("+i+").data.label0", "0");
                snmpPerfItems[i].binLabel[1] = config.getString("perfitem("+i+").data.label1", "1");
            }else if (snmpPerfItems[i].dataFormat == PerfConf.DATA_FORMAT_STATUS) {
            	int size= getLabelSize(perfNodes.item(i));
            	snmpPerfItems[i].labels = new PerfLabelStatusBean[size];
            	for ( int j = 0 ; j< snmpPerfItems[i].labels.length ; j ++ ){
            		snmpPerfItems[i].labels[j] = new PerfLabelStatusBean();
            		snmpPerfItems[i].labels[j].label = config.getString("perfitem("+i+").data.set("+j+")[@label]");
            		snmpPerfItems[i].labels[j].value = config.getInt("perfitem("+i+").data.set("+j+")[@value]");
            	}
            }
            try {
                snmpPerfItems[i].autoReg = config.getBoolean("perfitem("+i+")[@autoreg]", false);
            } catch (ConversionException e) { // 변환 오류 발생 시 false로 지정
                snmpPerfItems[i].autoReg = false;
            }
//            if(filename.contains("Waton")){
//	            System.out.println("\t<perfitem>");
//	            System.out.println("\t\t<displayname>" + snmpPerfItems[i].displayName + "</displayname>");
//	            System.out.println("\t\t<oid>" + snmpPerfItems[i].oid + "</oid>");
//	            System.out.println("\t\t<interval>" + snmpPerfItems[i].interval + "</interval>");
//	            System.out.println("\t\t<units>" + snmpPerfItems[i].units + "</units>");
//	            System.out.println("\t\t<expression>" + snmpPerfItems[i].expression + "</expression>");
//	            System.out.println("\t\t<data format=\"3\"/>");
//	            System.out.println("\t\t<event severity = \"40\" threshold=\"120\" op=\"&gt;=\" mode=\"5\" duration=\"10\" count=\"3\" notify=\"3\" autoreg=\"TRUE\" name = \"" + snmpPerfItems[i].displayName + " 상한\" msg=\"{value|label}\"/>");
//	            System.out.println("\t\t<event severity = \"40\" threshold=\"80\" op=\"&lt;=\" mode=\"5\" duration=\"10\" count=\"3\" notify=\"3\" autoreg=\"TRUE\" name = \"" + snmpPerfItems[i].displayName + " 하한\" msg=\"{value|label}\"/>");
//	            System.out.println("\t</perfitem>");
//            }
            snmpPerfItems[i].evt = new SnmpPerfItem.EventInfo[getEventSize(perfNodes.item(i))];
            for (int j = 0; j < snmpPerfItems[i].evt.length; j++) {
                snmpPerfItems[i].evt[j] = new SnmpPerfItem.EventInfo();
                snmpPerfItems[i].evt[j].name = config.getString("perfitem("+i+").event("+j+")[@name]");
                snmpPerfItems[i].evt[j].severity = config.getInt("perfitem("+i+").event("+j+")[@severity]", 20);
                snmpPerfItems[i].evt[j].threshold = config.getDouble("perfitem("+i+").event("+j+")[@threshold]");
                snmpPerfItems[i].evt[j].op = config.getString("perfitem("+i+").event("+j+")[@op]", ">=");
                snmpPerfItems[i].evt[j].mode = config.getInt("perfitem("+i+").event("+j+")[@mode]", 1);
                snmpPerfItems[i].evt[j].duration = config.getInt("perfitem("+i+").event("+j+")[@duration]", 600); // 10분
                snmpPerfItems[i].evt[j].count = config.getInt("perfitem("+i+").event("+j+")[@count]", 0);
                snmpPerfItems[i].evt[j].seqCount = config.getInt("perfitem("+i+").event("+j+")[@notify]", 3);
                snmpPerfItems[i].evt[j].msg = config.getString("perfitem("+i+").event("+j+")[@msg]", "");
                snmpPerfItems[i].evt[j].autoClose = config.getInt("perfitem("+i+").event("+j+")[@autoclose]", 1) == 1;
                snmpPerfItems[i].evt[j].enable = config.getInt("perfitem("+i+").event("+j+")[@enable]", 1);
                try {
                	snmpPerfItems[i].evt[j].autoReg = config.getBoolean("perfitem("+i+").event("+j+")[@autoreg]",false);
                } catch (ConversionException e) { // 변환 오류 발생 시 false로 지정
                	snmpPerfItems[i].evt[j].autoReg = false;
                }
            }
        }
    }

    private int getEventSize(Node perfItem) {

        NodeList nodes = perfItem.getChildNodes();
        int size = 0;
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE && nodes.item(i).getNodeName().equals("event")) {
                size++;
            }
        }
        return size;
    }
    
    private void init(String filename) {
        try {
            // parse an XML file into document
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            InputSource input = new InputSource(new FileReader(new File(filename)));
            doc = docBuilder.parse(input);

            // normalize text representation
            // doc.getDocumentElement().normalize();
        }
        catch (SAXParseException errSPE) {
            System.out.println("** Parsing error" + ", line " + errSPE.getLineNumber() + ", uri "
                    + errSPE.getSystemId() + "   " + errSPE.getMessage());
            // print stack trace as below
        }
        catch (SAXException errSE) {
            Exception x = errSE.getException();
            ((x == null) ? errSE : x).printStackTrace();
        }
        catch (Throwable t) {
            t.printStackTrace(System.err);
        }

        strFile = new String(filename);
    }

    /**
     * 성능 갯수를 알아온다.
     */
    public int getSnmpPerfSize() {
        return this.snmpPerfItems.length;
    }

    public SnmpPerfItem[] getPerfItems() {
        return this.snmpPerfItems;
    }
    
    private int getLabelSize(Node perfItem) {

        NodeList nodes = perfItem.getChildNodes();
        int size = 0;
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE && nodes.item(i).getNodeName().equals("data")) {
            	NodeList nodes2 = nodes.item(i).getChildNodes();
                for (int j = 0; j < nodes2.getLength(); j++) {
                    if (nodes2.item(j).getNodeType() == Node.ELEMENT_NODE && nodes2.item(j).getNodeName().equals("set")) {
                        size++;
                    }
                }
            }
        }
        return size;
    }
    /**
     * 성능 이름 목록 가져오기
     */
    public String[] getSnmpPerfNames() {
        int size = getSnmpPerfSize();
        System.err.println("size:" + size);
        String[] nodeNames = new String[size];
        NodeList nodes = doc.getDocumentElement().getChildNodes();
        for (int i = 1; i <= size; i++) { // 첫번째 노드(<number>) 이후 부터가 성능 항목이므로...
            Node node = nodes.item(i);
            nodeNames[i - 1] = node.getNodeName();
            System.err.println(nodeNames[i - 1]);
        }
        return nodeNames;
    }

	public long getLoadTime() {
		return loadTime;
	}

	public void setLoadTime(long loadTime) {
		this.loadTime = loadTime;
	}
	
	public static void main(String[] args) {
        try {
            SnmpPerfConf conf = new SnmpPerfConf("C:\\OnionSoftware\\midknight\\conf\\ko\\fms\\ups_rfc1628.xml", "EUC-KR");
            SnmpPerfItem perfItems[] = conf.getPerfItems();
            for (int i = 0; i < perfItems.length; i++) {
                System.out.println("["+(i+1)+"] "+perfItems[i].toString());
            }
        } catch (Exception e) {
            System.exit(0);
        }
    }
}
