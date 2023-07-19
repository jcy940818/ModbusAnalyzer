package common.perf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import src_ko.util.Util;

public class FmsActionConf {

    protected Document doc;
    protected XMLConfiguration config;
    private FmsActionItem[] actionItems;

    public FmsActionConf(String filename, String encoding) throws ConfigurationException, FileNotFoundException {

    	config = XmlUtils.createXMLConfiguration(filename, encoding);
        
        doc = config.getDocument();
        NodeList perfNodes = doc.getElementsByTagName("control");
        actionItems = new FmsActionItem[perfNodes.getLength()];
        for (int i = 0; i < actionItems.length; i++) {
            actionItems[i] = new FmsActionItem();
            actionItems[i].displayName = config.getString("control("+i+").displayName");
            actionItems[i].counter = config.getString("control("+i+").controlCounter");
            actionItems[i].command = config.getString("control("+i+").command");
            actionItems[i].desc = config.getString("control("+i+").desc", "");
            actionItems[i].useParam = config.getInt("control("+i+").useParam", 0);
            actionItems[i].defaultValue = config.getString("control("+i+").defaultValue");
            actionItems[i].waitTime = config.getInt("control("+i+").waitTime");
        }
    }

    public FmsActionItem[] getActionItems() {
        return actionItems;
    }

    public void save(PrintStream ps) throws ConfigurationException {
        config.save(ps);
    }

    public void save() throws ConfigurationException {
        config.save();
    }
    
    public static ArrayList<FmsActionItem> getFmsActionList(File xmlFile, String encoding) {
    	ArrayList<FmsActionItem> actionList = new ArrayList<FmsActionItem>();
    	
    	try {
    		FmsActionConf conf = new FmsActionConf(xmlFile.getAbsolutePath(), encoding);
    		FmsActionItem actionItems[] = conf.getActionItems();
    		
    		for(int i = 0; i < actionItems.length; i++) {
    			actionList.add(actionItems[i]);
    		}
    		
    		return actionList;
    		
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