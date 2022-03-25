package common.perf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConversionException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import src_ko.util.Util;

public class FmsPerfConf extends XmlPerfConfiguration {
	
    private FmsPerfItem[] perfItems;
   
    public FmsPerfConf(String filename, String encoding) throws ConfigurationException, FileNotFoundException {
        config = XmlUtils.createXMLConfiguration(filename, encoding);
        
        doc = config.getDocument();
        NodeList perfNodes = doc.getElementsByTagName("perfitem");
        perfItems = new FmsPerfItem[perfNodes.getLength()];
        for (int i = 0; i < perfItems.length; i++) {
            perfItems[i] = new FmsPerfItem();
            perfItems[i].displayName = config.getString("perfitem("+i+").displayname");
            perfItems[i].enable = config.getInt("perfitem("+i+").enable",1);
            perfItems[i].counter = config.getString("perfitem("+i+").counter");
            perfItems[i].interval = config.getInt("perfitem("+i+").interval", 60);
            perfItems[i].measure = config.getString("perfitem("+i+").measure", "");
            perfItems[i].scaleFunc = config.getString("perfitem("+i+").scalefunction", "x");
            perfItems[i].dataFormat = config.getInt("perfitem("+i+").data[@format]", PerfConf.DATA_FORMAT_MEASURE);
            if (perfItems[i].dataFormat == PerfConf.DATA_FORMAT_DIGITAL) {
                perfItems[i].binLabel = new String[2];
                perfItems[i].binLabel[0] = config.getString("perfitem("+i+").data.label0", "0");
                perfItems[i].binLabel[1] = config.getString("perfitem("+i+").data.label1", "1");
            }else if (perfItems[i].dataFormat == PerfConf.DATA_FORMAT_STATUS) {
            	int size= getLabelSize(perfNodes.item(i));
            	perfItems[i].labels = new PerfLabelStatusBean[size];
            	for ( int j = 0 ; j< perfItems[i].labels.length ; j ++ ){
            		perfItems[i].labels[j] = new PerfLabelStatusBean();
            		perfItems[i].labels[j].label = config.getString("perfitem("+i+").data.set("+j+")[@label]");
            		perfItems[i].labels[j].value = config.getInt("perfitem("+i+").data.set("+j+")[@value]");
            	}
            }
            try {
                perfItems[i].autoReg = config.getBoolean("perfitem("+i+")[@autoreg]", false);
            } catch (ConversionException e) { // 변환 오류 발생 시 false로 지정
                perfItems[i].autoReg = false;
            }

            perfItems[i].evt = new FmsPerfItem.EventInfo[getEventSize(perfNodes.item(i))];
            for (int j = 0; j < perfItems[i].evt.length; j++) {
                perfItems[i].evt[j] = new FmsPerfItem.EventInfo();
                perfItems[i].evt[j].name = config.getString("perfitem("+i+").event("+j+")[@name]");
                perfItems[i].evt[j].severity = config.getInt("perfitem("+i+").event("+j+")[@severity]", 20);
                perfItems[i].evt[j].threshold = config.getDouble("perfitem("+i+").event("+j+")[@threshold]");
                perfItems[i].evt[j].op = config.getString("perfitem("+i+").event("+j+")[@op]", ">=");
                perfItems[i].evt[j].mode = config.getInt("perfitem("+i+").event("+j+")[@mode]", 1);
                perfItems[i].evt[j].duration = config.getInt("perfitem("+i+").event("+j+")[@duration]", 600); // 10분
                perfItems[i].evt[j].count = config.getInt("perfitem("+i+").event("+j+")[@count]", 0);
                perfItems[i].evt[j].seqCount = config.getInt("perfitem("+i+").event("+j+")[@notify]", 3);
                perfItems[i].evt[j].msg = config.getString("perfitem("+i+").event("+j+")[@msg]", "");
                perfItems[i].evt[j].autoClose = config.getInt("perfitem("+i+").event("+j+")[@autoclose]", 1) == 1;
                perfItems[i].evt[j].enable = config.getInt("perfitem("+i+").event("+j+")[@enable]", 1);
                perfItems[i].evt[j].threshold2 = config.getDouble("perfitem("+i+").event("+j+")[@threshold2]", 0);
                try {
                	perfItems[i].evt[j].autoReg = config.getBoolean("perfitem("+i+").event("+j+")[@autoreg]",false);
                } catch (ConversionException e) { // 변환 오류 발생 시 false로 지정
                	perfItems[i].evt[j].autoReg = false;
                }
            }
        }
    }

    private int getEventSize(Node perfItem) {

        NodeList nodes = perfItem.getChildNodes();
        int size = 0;
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE && "event".equals(nodes.item(i).getNodeName())) {
                size++;
            }
        }
        return size;
    }

    private int getLabelSize(Node perfItem) {

        NodeList nodes = perfItem.getChildNodes();
        int size = 0;
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE && "data".equals(nodes.item(i).getNodeName())) {
            	NodeList nodes2 = nodes.item(i).getChildNodes();
                for (int j = 0; j < nodes2.getLength(); j++) {
                    if (nodes2.item(j).getNodeType() == Node.ELEMENT_NODE && "set".equals(nodes2.item(j).getNodeName())) {
                        size++;
                    }
                }
            }
        }
        return size;
    }

    /**
     * 성능 갯수를 알아온다.
     */
    public int getPerfSize() {
        return perfItems.length;
    }

    public FmsPerfItem[] getPerfItems() {
        return perfItems;
    }

    /**
     * 성능 이름 목록 가져오기
     */
    public String[] getPerfNames() {
        int size = getPerfSize();
        String[] nodeNames = new String[size];
        NodeList nodes = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < size; i++) {   // 첫번째 노드(<number>) 이후 부터가 성능 항목이므로...
            Node node = nodes.item(i);
            nodeNames[i] = node.getNodeName();
        }
        return nodeNames;
    }

    public void save(PrintStream ps) throws ConfigurationException {
        config.save(ps);
    }

    public void save() throws ConfigurationException {
        config.save();
    }
    
    public static ArrayList<Perf> getFmsPerfList(File xmlFile, String encoding) {        	
    	ArrayList<Perf> perfList = new ArrayList<Perf>();
    	
    	try {    		
    		FmsPerfConf conf = new FmsPerfConf(xmlFile.getAbsolutePath(), encoding);
    		FmsPerfItem perfItems[] = conf.getPerfItems();
    		
    		for(int i = 0; i < perfItems.length; i++) {
    			perfList.add(perfItems[i]);
    		}
    		
    		return perfList;
    	}catch(Exception e) {    		
    		e.printStackTrace();
    		StringBuilder sb = new StringBuilder();
    		sb.append(String.format("%s%s%s\n", Util.colorRed("XML Load Fail"), Util.separator , Util.separator));    		
    		sb.append(String.format("Message : %s%s%s\n", e.getMessage(), Util.separator, Util.separator));
    		Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
    		return null;
    	}
    }
   
}
