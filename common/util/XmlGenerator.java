package common.util;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import common.perf.ControlAction;
import common.perf.FmsPerfItem;
import common.perf.FmsPerfItem.EventInfo;
import moon.Moon;
import common.perf.PerfConf;
import common.perf.PerfLabelStatusBean;
import src_ko.agent.Event;
import src_ko.util.Util;

public class XmlGenerator {
	
	public static void generateXML(ArrayList<FmsPerfItem> perfs, boolean useAutoEvent, String encoding, String agentType) {
		String savePath = Util.getFilePath();
				
		if(savePath == null) {
			return;
		}else {
			savePath = savePath + ".xml";
		}
		
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
            
            for(int i = 0; i < perfs.size(); i++) {            	
                String entry = null;
                
                try {
                	switch(agentType) {
	                	case "modbus" :
	                		entry = getModbusPerfEntry(perfs.get(i), useAutoEvent);
	                		break;
	                		
	                	case "common" :
	                		entry = getCommonPerfEntry(perfs.get(i));
	                		break;
	                		
	                	case "snmp" :
	                		entry = getSnmpPerfEntry(perfs.get(i));
	                		break;
                	}
                }
                catch (Exception e) {
                    e.printStackTrace();
                    
                    if(Moon.isKorean()) {
                    	StringBuilder sb = new StringBuilder();
            			sb.append("<font color='red'>Failed to XML Converting</font>\n");
            			sb.append(String.format("XML 파일 변환 작업중 예외가 발생하였습니다%s\n\n", Util.separator));
            			sb.append(String.format("작업중이던 성능 : %s번째 성능 ( 성능명 : %s )%s\n\n", Util.colorBlue(String.valueOf(i+1)), Util.colorBlue(perfs.get(i).getDisplayName()) , Util.separator));        			
            			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
            			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
            			return;	
                    }else {
                    	StringBuilder sb = new StringBuilder();
            			sb.append("<font color='red'>Failed to XML Converting</font>\n");
            			sb.append(String.format("Exception during XML file conversion operation%s\n\n", Util.separator));
            			sb.append(String.format("Performance you were working on last : %s Perf ( Perf Name : %s )%s\n\n", Util.colorBlue(String.valueOf(i+1)), Util.colorBlue(perfs.get(i).getDisplayName()) , Util.separator));        			
            			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
            			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
            			return;
                    }
                    
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
            writer.flush();
            writer.close();
            
            System.out.println("Done (" + perfCount + ")");
            
            File xmlFile = new File(savePath);
            
            if(xmlFile.exists()) {
            	if(Moon.isKorean()) {
            		StringBuilder sb = new StringBuilder();
        			sb.append("<font color='Green'>XML File Converting is Complete</font>\n");			
        			sb.append(String.format("성능 %s개 항목 생성 완료%s%s\n\n",Util.colorBlue(String.valueOf(perfCount)), Util.separator, Util.separator));
        			sb.append("아래의 경로에 XML 파일을 다운로드 완료하였습니다" + Util.separator + Util.separator + "\n\n");
        			sb.append(Util.colorBlue("Path") + " : " + xmlFile.getAbsolutePath().replace("\\", Util.colorBlue("\\")) + Util.separator + Util.separator + "\n");
        			Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);	
            	}else {
            		StringBuilder sb = new StringBuilder();
        			sb.append("<font color='Green'>XML File Converting is Complete</font>\n");			
        			sb.append(String.format("Successfully created %s Performance%s%s\n\n",Util.colorBlue(String.valueOf(perfCount)), Util.separator, Util.separator));
        			sb.append("Successfully downloaded the xml file to the following path" + Util.separator + Util.separator + "\n\n");
        			sb.append(Util.colorBlue("Path") + " : " + xmlFile.getAbsolutePath().replace("\\", Util.colorBlue("\\")) + Util.separator + Util.separator + "\n");
        			Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
            	}
            }
                        
            }
        
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }		
	}
	
	
	public static void generateControlXML(ArrayList<ControlAction> controls, String encoding) {
		String savePath = Util.getFilePath();
		
		if(savePath == null) {
			return;
		}else {
			savePath = savePath + ".xml";
		}
		
        try {
            PrintWriter writer = new PrintWriter(savePath, encoding);
            writer.println("<?xml version=\"1.0\" encoding=\"" + encoding.toUpperCase() + "\"?>");
            writer.println("<controls>");
                       
            String line = "";

            int controlCount = 0;
            
            for(int i = 0; i < controls.size(); i++) {            	
                String entry = null;
                
                try {

            		entry = getControlActionEntry(controls.get(i));

                }catch (Exception e) {
                    e.printStackTrace();
                    
                    if(Moon.isKorean()) {
                    	StringBuilder sb = new StringBuilder();
            			sb.append("<font color='red'>Failed to XML Converting</font>\n");
            			sb.append(String.format("XML 파일 변환 작업중 예외가 발생하였습니다%s\n\n", Util.separator));
            			sb.append(String.format("작업중이던 제어 항목 : %s번째 제어 ( 제어 이름 : %s )%s\n\n", Util.colorBlue(String.valueOf(i+1)), Util.colorBlue(controls.get(i).getControlName()) , Util.separator));        			
            			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
            			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
            			return;	
                    }else {
                    	StringBuilder sb = new StringBuilder();
            			sb.append("<font color='red'>Failed to XML Converting</font>\n");
            			sb.append(String.format("Exception during XML file conversion operation%s\n\n", Util.separator));
            			sb.append(String.format("The last Control worked on : %s Control ( Control Name : %s )%s\n\n", Util.colorBlue(String.valueOf(i+1)), Util.colorBlue(controls.get(i).getControlName()) , Util.separator));        			
            			sb.append(String.format("Exception Message : %s%s\n", e.getMessage(), Util.separator));
            			Util.showMessage(sb.toString(), JOptionPane.ERROR_MESSAGE);
            			return;
                    }
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
            
            File xmlFile = new File(savePath);
            
            if(xmlFile.exists()) {
            	if(Moon.isKorean()) {
            		StringBuilder sb = new StringBuilder();
        			sb.append("<font color='Green'>XML File Converting is Complete</font>\n");			
        			sb.append(String.format("제어 %s개 항목 생성 완료%s%s\n\n",Util.colorBlue(String.valueOf(controlCount)), Util.separator, Util.separator));
        			sb.append("아래의 경로에 XML 파일을 다운로드 완료하였습니다" + Util.separator + Util.separator + "\n\n");
        			sb.append(Util.colorBlue("Path") + " : " + xmlFile.getAbsolutePath().replace("\\", Util.colorBlue("\\")) + Util.separator + Util.separator + "\n");
        			Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
        			return;
            	}else {
            		StringBuilder sb = new StringBuilder();
        			sb.append("<font color='Green'>XML File Converting is Complete</font>\n");			
        			sb.append(String.format("Successfully created %s Control items%s%s\n\n",Util.colorBlue(String.valueOf(controlCount)), Util.separator, Util.separator));
        			sb.append("Successfully downloaded the xml file to the following path" + Util.separator + Util.separator + "\n\n");
        			sb.append(Util.colorBlue("Path") + " : " + xmlFile.getAbsolutePath().replace("\\", Util.colorBlue("\\")) + Util.separator + Util.separator + "\n");
        			Util.showMessage(sb.toString(), JOptionPane.INFORMATION_MESSAGE);
        			return;
            	}
            }
                        
            }
        
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	
	private static String getModbusPerfEntry(FmsPerfItem perf, boolean useAutoEvent){
    	StringBuilder s = new StringBuilder();
        s.append("\t\t<displayname>").append(perf.getDisplayName()).append("</displayname>\r\n");
        s.append("\t\t<counter>").append(perf.getCounter()).append("</counter>\r\n");
        s.append("\t\t<interval>").append((perf.getInterval() != 0) ? perf.getInterval() : 60).append("</interval>\r\n");                
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
        	s.append("notify=\"").append(Event.seqCount).append("\" ");
        	s.append("autoreg=\"").append(Event.autoReg).append("\" ");
        	s.append("name=\"").append(perf.getDisplayName() + " " + Event.name).append("\" ");
        	s.append("msg=\"").append(Event.message).append("\" ");
        	s.append("enable=\"").append(Event.enable).append("\"/>\r\n");
        }
                
        return s.toString();
    }
	
	
	private static String getCommonPerfEntry(FmsPerfItem perf){
    	StringBuilder s = new StringBuilder();
        s.append("\t\t<displayname>").append(perf.getDisplayName()).append("</displayname>\r\n");
        s.append("\t\t<counter>").append(perf.getCounter()).append("</counter>\r\n");
        s.append("\t\t<interval>").append((perf.getInterval() != 0) ? perf.getInterval() : 60).append("</interval>\r\n");                
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
        
        if(perf.getFmsEventInfo() != null && perf.getFmsEventInfo()[0] != null){
        	EventInfo event = perf.getFmsEventInfo()[0];
		  	s.append("\t\t<event severity=\"").append(event.severity).append("\" ");
		  	s.append("threshold=\"").append(event.threshold).append("\" ");
		  	s.append("op=\"").append(event.op).append("\" ");
		  	s.append("mode=\"").append(event.mode).append("\" ");
		  	s.append("duration=\"").append(event.duration).append("\" ");
		  	s.append("count=\"").append(event.count).append("\" ");
		  	s.append("notify=\"").append(event.seqCount).append("\" ");
		  	s.append("autoreg=\"").append(event.autoReg).append("\" ");
		  	s.append("name=\"").append(event.name).append("\" ");
		  	s.append("msg=\"").append(event.msg).append("\" ");
		  	s.append("enable=\"").append(event.enable).append("\"/>\r\n");
        }
                
        return s.toString();
    }
	
	
	private static String getSnmpPerfEntry(FmsPerfItem perf){
    	StringBuilder s = new StringBuilder();
        s.append("\t\t<displayname>").append(perf.getDisplayName()).append("</displayname>\r\n");
        s.append("\t\t<oid>").append(perf.getCounter()).append("</oid>\r\n");
        s.append("\t\t<interval>").append((perf.getInterval() != 0) ? perf.getInterval() : 60).append("</interval>\r\n");                
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
        
        if(perf.getFmsEventInfo() != null && perf.getFmsEventInfo()[0] != null){
        	EventInfo event = perf.getFmsEventInfo()[0];
		  	s.append("\t\t<event severity=\"").append(event.severity).append("\" ");
		  	s.append("threshold=\"").append(event.threshold).append("\" ");
		  	s.append("op=\"").append(event.op).append("\" ");
		  	s.append("mode=\"").append(event.mode).append("\" ");
		  	s.append("duration=\"").append(event.duration).append("\" ");
		  	s.append("count=\"").append(event.count).append("\" ");
		  	s.append("notify=\"").append(event.seqCount).append("\" ");
		  	s.append("autoreg=\"").append(event.autoReg).append("\" ");
		  	s.append("name=\"").append(event.name).append("\" ");
		  	s.append("msg=\"").append(event.msg).append("\" ");
		  	s.append("enable=\"").append(event.enable).append("\"/>\r\n");
        }
                
        return s.toString();
    }
	
	private static String getControlActionEntry(ControlAction control){
    	StringBuilder s = new StringBuilder();
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