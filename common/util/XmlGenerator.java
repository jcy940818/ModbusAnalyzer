package common.util;

import java.io.PrintWriter;

import javax.swing.JOptionPane;

import common.perf.ControlAction;
import common.perf.FmsPerfItem;
import common.perf.PerfConf;
import common.perf.PerfLabelStatusBean;
import src_ko.agent.Event;
import src_ko.util.Util;

public class XmlGenerator {
	
	public static void generatePerfXML(FmsPerfItem[] perfs, boolean useAutoEvent, String encoding, String agentType) {
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
	                		
	                	case "snmp" :
	                		entry = getSnmpPerfEntry(perfs[i], useAutoEvent);
	                		break;
                	}
                }
                catch (Exception e) {
                    e.printStackTrace();
                    
                    StringBuilder sb = new StringBuilder();
        			sb.append("<font color='red'>Failed to XML Converting</font>\n");
        			sb.append(String.format("XML 파일 변환 작업중 예외가 발생하였습니다%s\n\n", Util.separator));
        			sb.append(String.format("작업중이던 성능 : %s번째 성능 ( 성능명 : %s )%s\n\n",
        					Util.colorBlue(String.valueOf(i+1)),
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
			sb.append(String.format("성능 %s개 항목을 변환 완료하였습니다%s%s\n",Util.colorBlue(String.valueOf(perfCount)), Util.separator, Util.separator));        						
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
	
	
	private static String getCommonPerfEntry(FmsPerfItem perf, boolean useAutoEvent){
    	StringBuilder s = new StringBuilder(100);
        s.append("\t\t<displayname>").append(perf.getDisplayName()).append("</displayname>\r\n");
        s.append("\t\t<counter>").append(perf.getCounter()).append("</counter>\r\n");
        s.append("\t\t<interval>").append(perf.getInterval()).append("</interval>\r\n");                
        s.append("\t\t<measure>").append(perf.getMeasure()).append("</measure>\r\n");        
        s.append("\t\t<scalefunction>").append(perf.getScaleFunction().replace("&", "&amp;")).append("</scalefunction>\r\n");
        
        if(perf.getDataFormat() == PerfConf.DATA_FORMAT_STATUS){
        	s.append("\t\t<data format=\"2\">\r\n");
        	        	        	
        	PerfLabelStatusBean[] labels = perf.getStatusLabels();
        	
        	if(labels != null) {
	        	for(PerfLabelStatusBean label : labels) {
	        		 s.append("\t\t\t<set value=\"")
	 	    	    .append(label.value)
	 	    	    .append("\" label=\"")
	 	    	    .append(label.label).append("\" />\r\n");
	        	}
        	}
        	s.append("\t\t</data>\r\n");
        	
        }
        else if(perf.getDataFormat() == PerfConf.DATA_FORMAT_DIGITAL){
            s.append("\t\t<data format=\"1\">\r\n");
            s.append("\t\t\t<label0>").append(perf.binLabel[0]).append("</label0>\r\n");
            s.append("\t\t\t<label1>").append(perf.binLabel[1]).append("</label1>\r\n");
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
        	s.append("notify=\"").append(Event.notify).append("\" ");
        	s.append("autoreg=\"").append(Event.autoReg).append("\" ");
        	s.append("name=\"").append(perf.getDisplayName() + " " + Event.name).append("\" ");
        	s.append("msg=\"").append(Event.message).append("\" ");
        	s.append("enable=\"").append(Event.enable).append("\"/>\r\n");
        }
                
        return s.toString();
    }
	
	
	private static String getSnmpPerfEntry(FmsPerfItem perf, boolean useAutoEvent){
    	StringBuilder s = new StringBuilder(100);
        s.append("\t\t<displayname>").append(perf.getDisplayName()).append("</displayname>\r\n");
        s.append("\t\t<oid>").append(perf.getCounter()).append("</oid>\r\n");
        s.append("\t\t<interval>").append(perf.getInterval()).append("</interval>\r\n");                
        s.append("\t\t<units>").append(perf.getMeasure()).append("</units>\r\n");        
        s.append("\t\t<expression>").append(perf.getScaleFunction().replace("&", "&amp;")).append("</expression>\r\n");
        
        if(perf.getDataFormat() == PerfConf.DATA_FORMAT_STATUS){
        	s.append("\t\t<data format=\"2\">\r\n");
        	        	        	
        	PerfLabelStatusBean[] labels = perf.getStatusLabels();
        	
        	if(labels != null) {
	        	for(PerfLabelStatusBean label : labels) {
	        		 s.append("\t\t\t<set value=\"")
	 	    	    .append(label.value)
	 	    	    .append("\" label=\"")
	 	    	    .append(label.label).append("\" />\r\n");
	        	}
        	}
        	s.append("\t\t</data>\r\n");
        	
        }
        else if(perf.getDataFormat() == PerfConf.DATA_FORMAT_DIGITAL){
            s.append("\t\t<data format=\"1\">\r\n");
            s.append("\t\t\t<label0>").append(perf.binLabel[0]).append("</label0>\r\n");
            s.append("\t\t\t<label1>").append(perf.binLabel[1]).append("</label1>\r\n");
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
        	s.append("notify=\"").append(Event.notify).append("\" ");
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
	
}