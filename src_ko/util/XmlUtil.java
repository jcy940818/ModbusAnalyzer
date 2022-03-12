package src_ko.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import src_ko.database.QueryParameter;
import src_ko.database.StoredProcedure;

public class XmlUtil {	
	
	/** 
	 * @param node
	 * @param itemName
	 * @return itemName 이라는 이름의 Node
	 */
	public static Node getNode(Node node, String itemName) throws Exception{
		Element element = (Element) node;		
		Node resultNode = element.getElementsByTagName(itemName).item(0);				
		return resultNode;
	}			
	
	
	/**
	 * @param XmlPath
	 * @return XmlPath 경로의 StoredProcedure 인스턴스 리스트를 리턴
	 */
	public static ArrayList<StoredProcedure> getProcedureList(String XmlPath) throws Exception{
		ArrayList<StoredProcedure> spList = new ArrayList<StoredProcedure>();
		StoredProcedure sp = null;
		ArrayList<QueryParameter> paramList = null;

		File file = new File(XmlPath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		Document doc = builder.parse(file);
		doc.getDocumentElement().normalize();
		XPath xPath = XPathFactory.newInstance().newXPath();

		String expression = "/storedProcedureInfo/storedProcedure";
		NodeList spNodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

		for (int i = 0; i < spNodeList.getLength(); i++) {
			sp = new StoredProcedure();

			Node spNode = spNodeList.item(i); // <StoredProcedure>

			if (spNode.getNodeType() == Node.ELEMENT_NODE) {

				paramList = getParamList(spNode); // <params>

				Element spElement = (Element) spNode; // StoredProcedure
				
				String name = spElement.getElementsByTagName("name").item(0).getTextContent().trim();
				String content = spElement.getElementsByTagName("content").item(0).getTextContent().trim();
				String query = spElement.getElementsByTagName("query").item(0).getTextContent().trim();

				sp.setIndex(i);
				sp.setName(name);
				sp.setContent(content);
				sp.setQuery(query);

				if (paramList.size() > 0) {
					sp.setUseParam(true);
					sp.setParamList(paramList);
				} else {
					sp.setUseParam(false);
					sp.setParamList(null);
				}

				spList.add(sp);
			}
		}

		return spList;
	}
	
	
	/**
	 * @param storedProcedureNode
	 * @return <params> Element 
	 */
	public static ArrayList<QueryParameter> getParamList(Node storedProcedureNode) throws Exception{
		ArrayList<QueryParameter> paramList = new ArrayList<QueryParameter>();

		Node paramsNode = getNode(storedProcedureNode, "params");

		if (storedProcedureNode.getNodeType() == Node.ELEMENT_NODE) {

			NodeList paramNodeList = paramsNode.getChildNodes();

			for (int i = 0; i < paramNodeList.getLength(); i++) {
				Node paramNode = paramNodeList.item(i); // <param>

				if (paramNode.getNodeType() == Node.ELEMENT_NODE) {
					Element paramElement = (Element) paramNode;
					String paramName = paramElement.getElementsByTagName("paramName").item(0).getTextContent().trim();
					String paramExample = paramElement.getElementsByTagName("paramExample").item(0).getTextContent().trim();
					
					QueryParameter param = new QueryParameter(); // 쿼리 파라미터 인스턴스 생성
					param.setIndex(i);
					param.setName(paramName);
					param.setExample(paramExample);					
					paramList.add(param);
				}
			}
		}
		
		// 파라미터 리스트 인덱싱 (초기화 직후에는 순차적으로 인덱싱되어있지 않기 때문에)
		Collections.sort(paramList);
		for(int i = 0; i < paramList.size(); i++) {
			paramList.get(i).setIndex(i);
		}
		
		return paramList;
	}
			
}
