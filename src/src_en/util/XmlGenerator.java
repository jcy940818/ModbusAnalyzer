package src_en.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JOptionPane;

import src_en.agent.ControlAction;
import src_en.agent.Event;
import src_en.agent.Perf;
import src_en.database.QueryParameter;
import src_en.database.StoredProcedure;
import src_en.swing.StoredProcedure_Panel;
import src_ko.util.Util;

public class XmlGenerator {
	
	
	public static void generateXML(Perf[] perfs, boolean useAutoEvent, String encoding, String agentType) {
		String savePath = Util.getFilePath() + ".xml";
				
        try {
            PrintWriter writer = new PrintWriter(savePath, encoding);
            writer.println("<?xml version=\"1.0\" encoding=\"" + encoding.toUpperCase() + "\"?>");
            writer.println("<perfinfo>");
            
            if(agentType.equalsIgnoreCase("snmp")) {
            	 writer.print("\t<mibdir>");
                 writer.print("SNMP");
                 writer.println("</mibdir>");
            }
            
            String line = "";

            int perfCount = 0;
            
            for(int i = 0; i < perfs.length; i++) {            	
                String entry = null;
                
                try {
                	switch(agentType) {                	
	                	case "common" :
	                		entry = getCommonPerfEntry(perfs[i], useAutoEvent);
	                		break;
	                		
	                	case "modbus" : 
	                		entry = getModbusPerfEntry(perfs[i], useAutoEvent);
	                		break;
	                		
	                	case "snmp" :
	                		entry = getSnmpPerfEntry(perfs[i], useAutoEvent);
	                		break;
	                		
	                	case "agent" : 
	                		entry = getAgentPerfEntry(perfs[i], useAutoEvent);
	                		break;
                	}
                	
                }
                catch (Exception e) {
                    e.printStackTrace();
                    
                    StringBuilder sb = new StringBuilder();
        			sb.append("<font color='red'>Failed to XML Converting</font>\n");
        			sb.append(String.format("An exception occurred during XML file conversion operation%s\n\n", Util.separator));
        			sb.append(String.format(
        					"Lastly, the performance that I worked on => %s performance ( Name : %s )"        					
        					+ "%s\n\n",
        					Util.colorBlue("No." + String.valueOf(i+1)),
        					Util.colorBlue(perfs[i].getDisplayName()) ,
        					Util.separator));        			
        			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
        			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
        			return;
                }
                
                if (entry != null) {
                    writer.println("\t<perfitem>");
                    writer.print(entry);
                    writer.println("\t</perfitem>");
                    writer.flush();
                    perfCount++;
                }
            }
            
            writer.println("</perfinfo>");
            writer.close();
            
            System.out.println("Done (" + perfCount + ")");
            
            StringBuilder sb = new StringBuilder();
			sb.append("<font color='Green'>XML File Converting is Complete</font>\n");			
			sb.append(String.format("Successfully converted %s performances%s%s\n",Util.colorBlue(String.valueOf(perfCount)), Util.separator, Util.separator));        						
			Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);            
            }
        
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }		
	}
	
	
	public static void generateControlXML(ControlAction[] controls, String encoding, String agentType) {
		String savePath = Util.getFilePath() + ".xml";
				
        try {
            PrintWriter writer = new PrintWriter(savePath, encoding);
            writer.println("<?xml version=\"1.0\" encoding=\"" + encoding.toUpperCase() + "\"?>");
            writer.println("<controls>");
                       
            String line = "";

            int controlCount = 0;
            
            for(int i = 0; i < controls.length; i++) {            	
                String entry = null;
                
                try {
                	switch(agentType) {                	
	                	case "control" :
	                		entry = getControlActionEntry(controls[i]);
	                		break;	             	                	
                	}                	
                }catch (Exception e) {
                    e.printStackTrace();
                    
                    StringBuilder sb = new StringBuilder();
        			sb.append("<font color='red'>Failed to XML Converting</font>\n");
        			sb.append(String.format("XML 파일 변환 작업중 예외가 발생하였습니다%s\n\n", Util.separator));
        			sb.append(String.format("작업중이던 제어 항목 : %s번째 제어 ( 제어 이름 : %s )%s\n\n",
        					Util.colorBlue(String.valueOf(i+1)),
        					Util.colorBlue(controls[i].getControlName()) ,
        					Util.separator));        			
        			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
        			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
        			return;
                }
                
                if (entry != null) {
                    writer.println("\t<control>");
                    writer.print(entry);
                    writer.println("\t</control>");
                    writer.flush();
                    controlCount++;
                }
            }
            
            writer.println("</controls>");
            writer.close();
            
            System.out.println("Done (" + controlCount + ")");
            
            StringBuilder sb = new StringBuilder();
			sb.append("<font color='Green'>XML File Converting is Complete</font>\n");			
			sb.append(String.format("제어 %s개 항목을 변환 완료하였습니다%s%s\n",Util.colorBlue(String.valueOf(controlCount)), Util.separator, Util.separator));        						
			Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
            }
        
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }		
	}
	
	
	
	public static void generateProcedure(StoredProcedure sp, String savePath) {
				
        try {
            PrintWriter writer = new PrintWriter(savePath, "utf-8");
            writer.println("<?xml version=\"1.0\" encoding=\"" + "UTF-8" + "\"?>");
            writer.println("<storedProcedureInfo>");
          
            String line = "";
            String entry = null;
            
			try {
				entry = getProcedureEntry(sp);
			}catch (Exception e) {
                e.printStackTrace();                
                StringBuilder sb = new StringBuilder();
    			sb.append("<font color='red'>Failed to XML Converting</font>\n");
    			sb.append(String.format("An exception occurred during procedure creation operation%s\n\n", Util.separator));    			
    			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
    			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
    			return;
            }
            
            if (entry != null) {
                writer.println("\t<storedProcedure>");
                writer.print(entry);
                writer.println("\t</storedProcedure>");
                writer.flush();
            }
                        
            writer.println("</storedProcedureInfo>");
            writer.close();
                        
            StoredProcedure.loadStoredProcedureMap();	
            StoredProcedure_Panel.procedureCategory_ComboBox.setSelectedIndex(StoredProcedure_Panel.procedureCategory_ComboBox.getSelectedIndex());
            
            StringBuilder sb = new StringBuilder();
			sb.append("<font color='Green'>StoredProcedure Generate is Complete</font>\n");
			sb.append(String.format("Successfully created a Procedure%s%s\n\n",Util.separator, Util.separator));
			sb.append(String.format("%s : %s%s%s\n",Util.colorBlue("Procedure"),sp.getName(), Util.separator, Util.separator));
			sb.append(String.format("%s : %s%s%s\n",Util.colorBlue("Saved path"),savePath.replace("\\", Util.colorRed("\\")) ,Util.separator, Util.separator));
			Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
            }
        
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	
	}
	
	
	
	
	private static String getCommonPerfEntry(Perf perf, boolean useAutoEvent){
    	StringBuilder s = new StringBuilder(100);
        s.append("\t\t<displayname>").append(perf.getDisplayName()).append("</displayname>\r\n");
        s.append("\t\t<counter>").append(perf.getPerfCounter()).append("\\").append("{").append(perf.getSlot()).append("}").append("</counter>\r\n");
        s.append("\t\t<interval>").append(perf.getInterval()).append("</interval>\r\n");                
        s.append("\t\t<measure>").append(perf.getMeasure()).append("</measure>\r\n");        
        s.append("\t\t<scalefunction>").append(perf.getScaleFunction().replace("&", "&amp;")).append("</scalefunction>\r\n");
        
        if(perf.getDataFormat().equalsIgnoreCase("2")){
        	s.append("\t\t<data format=\"2\">\r\n");
        	        	
        	HashMap<String, String> map = perf.getMultiStatusMap();
        	Set<String> keySet = map.keySet();
    		for (String key : keySet) {	    		
	    	    s.append("\t\t\t<set value=\"")
	    	    .append(key)
	    	    .append("\" label=\"")
	    	    .append(map.get(key)).append("\" />\r\n");
    		}
			s.append("\t\t</data>\r\n");
        }
        else if(perf.getDataFormat().equalsIgnoreCase("1")) {
            s.append("\t\t<data format=\"1\">\r\n");
            s.append("\t\t\t<label0>").append(perf.getBinaryMap().get("0")).append("</label0>\r\n");
            s.append("\t\t\t<label1>").append(perf.getBinaryMap().get("1")).append("</label1>\r\n");
            s.append("\t\t</data>\r\n");   
        }
        else {
            s.append("\t\t<data format=\"3\"/>\r\n");
        }
        
        if(useAutoEvent){
        	s.append("\t\t<event severity=\"").append(Event.severity).append("\" ");
        	s.append("threshold=\"").append(Event.threshold).append("\" ");
        	s.append("op=\"").append(Event.op).append("\" ");
        	s.append("mode=\"").append(Event.mode).append("\" ");
        	s.append("duration=\"").append(Event.duration).append("\" ");
        	s.append("count=\"").append(Event.count).append("\" ");
        	s.append("notify=\"").append(Event.seqCount).append("\" ");
        	s.append("autoreg=\"").append(Event.autoReg).append("\" ");
        	s.append("name=\"").append(perf.getDisplayName() + " " + Event.name).append("\" ");
        	s.append("msg=\"").append(Event.message).append("\" ");
        	s.append("enable=\"").append(Event.enable).append("\"/>\r\n");
        }
                
        return s.toString();
    }
	
	
	private static String getModbusPerfEntry(Perf perf, boolean useAutoEvent){
    	StringBuilder s = new StringBuilder(100);
        s.append("\t\t<displayname>").append(perf.getDisplayName()).append("</displayname>\r\n");
        s.append("\t\t<counter>").append(perf.getPerfCounter().replace("\\\\", "\\")).append("</counter>\r\n");
        s.append("\t\t<interval>").append(perf.getInterval()).append("</interval>\r\n");                
        s.append("\t\t<measure>").append(perf.getMeasure()).append("</measure>\r\n");        
        s.append("\t\t<scalefunction>").append(perf.getScaleFunction().replace("&", "&amp;")).append("</scalefunction>\r\n");
        
        if(perf.getDataFormat().equalsIgnoreCase("2")){
        	s.append("\t\t<data format=\"2\">\r\n");
        	        	
        	HashMap<String, String> map = perf.getMultiStatusMap();
        	Set<String> keySet = map.keySet();
    		for (String key : keySet) {	    		
	    	    s.append("\t\t\t<set value=\"")
	    	    .append(key)
	    	    .append("\" label=\"")
	    	    .append(map.get(key)).append("\" />\r\n");
    		}
			s.append("\t\t</data>\r\n");
        }
        else if(perf.getDataFormat().equalsIgnoreCase("1")) {
            s.append("\t\t<data format=\"1\">\r\n");
            s.append("\t\t\t<label0>").append(perf.getBinaryMap().get("0")).append("</label0>\r\n");
            s.append("\t\t\t<label1>").append(perf.getBinaryMap().get("1")).append("</label1>\r\n");
            s.append("\t\t</data>\r\n");   
        }
        else {
            s.append("\t\t<data format=\"3\"/>\r\n");
        }
        
        if(useAutoEvent){
        	s.append("\t\t<event severity=\"").append(Event.severity).append("\" ");
        	s.append("threshold=\"").append(Event.threshold).append("\" ");
        	s.append("op=\"").append(Event.op).append("\" ");
        	s.append("mode=\"").append(Event.mode).append("\" ");
        	s.append("duration=\"").append(Event.duration).append("\" ");
        	s.append("count=\"").append(Event.count).append("\" ");
        	s.append("notify=\"").append(Event.seqCount).append("\" ");
        	s.append("autoreg=\"").append(Event.autoReg).append("\" ");
        	s.append("name=\"").append(perf.getDisplayName() + " " + Event.name).append("\" ");
        	s.append("msg=\"").append(Event.message).append("\" ");
        	s.append("enable=\"").append(Event.enable).append("\"/>\r\n");
        }
                
        return s.toString();
    }
	
	
	private static String getSnmpPerfEntry(Perf perf, boolean useAutoEvent){
    	StringBuilder s = new StringBuilder(100);
        s.append("\t\t<displayname>").append(perf.getDisplayName()).append("</displayname>\r\n");
        s.append("\t\t<oid>").append(perf.getOid()).append("</oid>\r\n");
        s.append("\t\t<interval>").append(perf.getInterval()).append("</interval>\r\n");                
        s.append("\t\t<units>").append(perf.getMeasure()).append("</units>\r\n");        
        s.append("\t\t<expression>").append(perf.getScaleFunction().replace("&", "&amp;")).append("</expression>\r\n");
        
        if(perf.getDataFormat().equalsIgnoreCase("2")){
        	s.append("\t\t<data format=\"2\">\r\n");
        	        	
        	HashMap<String, String> map = perf.getMultiStatusMap();
        	Set<String> keySet = map.keySet();
    		for (String key : keySet) {	    		
	    	    s.append("\t\t\t<set value=\"")
	    	    .append(key)
	    	    .append("\" label=\"")
	    	    .append(map.get(key)).append("\" />\r\n");
    		}
			s.append("\t\t</data>\r\n");
        }
        else if(perf.getDataFormat().equalsIgnoreCase("1")) {
            s.append("\t\t<data format=\"1\">\r\n");
            s.append("\t\t\t<label0>").append(perf.getBinaryMap().get("0")).append("</label0>\r\n");
            s.append("\t\t\t<label1>").append(perf.getBinaryMap().get("1")).append("</label1>\r\n");
            s.append("\t\t</data>\r\n");   
        }
        else {
            s.append("\t\t<data format=\"3\"/>\r\n");
        }
        
        if(useAutoEvent){
        	s.append("\t\t<event severity=\"").append(Event.severity).append("\" ");
        	s.append("threshold=\"").append(Event.threshold).append("\" ");
        	s.append("op=\"").append(Event.op).append("\" ");
        	s.append("mode=\"").append(Event.mode).append("\" ");
        	s.append("duration=\"").append(Event.duration).append("\" ");
        	s.append("count=\"").append(Event.count).append("\" ");
        	s.append("notify=\"").append(Event.seqCount).append("\" ");
        	s.append("autoreg=\"").append(Event.autoReg).append("\" ");
        	s.append("name=\"").append(perf.getDisplayName() + " " + Event.name).append("\" ");
        	s.append("msg=\"").append(Event.message).append("\" ");
        	s.append("enable=\"").append(Event.enable).append("\"/>\r\n");
        }
                
        return s.toString();
    }
	
	
	
	private static String getAgentPerfEntry(Perf perf, boolean useAutoEvent){
    	StringBuilder s = new StringBuilder(100);
        s.append("\t\t<displayname>").append(perf.getDisplayName()).append("</displayname>\r\n");
        s.append("\t\t<counter>").append(perf.getPerfCounter()).append("\\").append("{").append(perf.getSlot()).append("}").append("</counter>\r\n");
        s.append("\t\t<interval>").append(perf.getInterval()).append("</interval>\r\n");                
        s.append("\t\t<measure>").append(perf.getMeasure()).append("</measure>\r\n");        
        s.append("\t\t<scalefunction>").append(perf.getScaleFunction().replace("&", "&amp;")).append("</scalefunction>\r\n");
        
        if(perf.getDataFormat().equalsIgnoreCase("2")){
        	s.append("\t\t<data format=\"2\">\r\n");
        	        	
        	HashMap<String, String> map = perf.getMultiStatusMap();
        	Set<String> keySet = map.keySet();
    		for (String key : keySet) {	    		
	    	    s.append("\t\t\t<set value=\"")
	    	    .append(key)
	    	    .append("\" label=\"")
	    	    .append(map.get(key)).append("\" />\r\n");
    		}
			s.append("\t\t</data>\r\n");
        }
        else if(perf.getDataFormat().equalsIgnoreCase("1")) {
            s.append("\t\t<data format=\"1\">\r\n");
            s.append("\t\t\t<label0>").append(perf.getBinaryMap().get("0")).append("</label0>\r\n");
            s.append("\t\t\t<label1>").append(perf.getBinaryMap().get("1")).append("</label1>\r\n");
            s.append("\t\t</data>\r\n");   
        }
        else {
            s.append("\t\t<data format=\"3\"/>\r\n");
        }
        
        if(useAutoEvent){
        	s.append("\t\t<event severity=\"").append(Event.severity).append("\" ");
        	s.append("threshold=\"").append(Event.threshold).append("\" ");
        	s.append("op=\"").append(Event.op).append("\" ");
        	s.append("mode=\"").append(Event.mode).append("\" ");
        	s.append("duration=\"").append(Event.duration).append("\" ");
        	s.append("count=\"").append(Event.count).append("\" ");
        	s.append("notify=\"").append(Event.seqCount).append("\" ");
        	s.append("autoreg=\"").append(Event.autoReg).append("\" ");
        	s.append("name=\"").append(perf.getDisplayName() + " " + Event.name).append("\" ");
        	s.append("msg=\"").append(Event.message).append("\" ");
        	s.append("enable=\"").append(Event.enable).append("\"/>\r\n");
        }
                
        return s.toString();
    }
	
	
	private static String getControlActionEntry(ControlAction control){
    	StringBuilder s = new StringBuilder(100);
        s.append("\t\t<displayName>").append(control.getControlName()).append("</displayName>\r\n");
        s.append("\t\t<controlCounter>").append(control.getControlCounter()).append("</controlCounter>\r\n");
        s.append("\t\t<command>").append(control.getCommand()).append("</command>\r\n");                
        s.append("\t\t<desc>").append(control.getDesc()).append("</desc>\r\n");        
        s.append("\t\t<useParam>").append(control.getUseParam()).append("</useParam>\r\n");
        s.append("\t\t<defaultValue/>\r\n");
        s.append("\t\t<waitTime>").append(control.getWaitTime()).append("</waitTime>\r\n");
        return s.toString();
    }
	
	
	private static String getProcedureEntry(StoredProcedure sp){
    	StringBuilder s = new StringBuilder(100);
        s.append("\t\t<name>").append(sp.getName()).append("</name>\r\n");
        s.append("\t\t<content>").append(sp.getContent()).append("</content>\r\n");
        
        
        s.append("\t\t<query><![CDATA[");
        s.append("\r\n-- **************************************** Query Start ****************************************\r\n");
        s.append("\r\n");
        s.append(sp.getQuery());
        s.append("\r\n");
        s.append("\r\n-- **************************************** Query End ****************************************\r\n");
        s.append("\t\t]]></query>\r\n");
        
        
        s.append("\t\t<params>\r\n");               
        if(sp.isUseParam()) {
        	ArrayList<QueryParameter> paramList = sp.getParamList();        	
        	for(int i = 0; i < paramList.size(); i++) {
        		QueryParameter param = paramList.get(i);        		
        		s.append("\t\t\t<param>\r\n");
        		s.append("\t\t\t\t<paramName>").append(param.getName()).append("</paramName>\r\n");
        		s.append("\t\t\t\t<paramExample>").append(param.getExample()).append("</paramExample>\r\n");
        		s.append("\t\t\t</param>\r\n");
        	}        	
        }        
        s.append("\t\t</params>\r\n");
                
        return s.toString();
    }
	
	
}
