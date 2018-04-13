package com.holly.webservice.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.EventFilter;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class StaxTest {

	//基于光标
	@Test
	public void testParse(){
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		InputStream is = StaxTest.class.getResourceAsStream("/xml/classsroom.xml");
		StringBuffer stringBuffer = new StringBuffer();
		long startime = System.currentTimeMillis();
		XMLStreamReader reader = null;
		try {
			reader = xmlInputFactory.createXMLStreamReader(is);
			while(reader.hasNext()){
			   int next = reader.next();
			   if(next==XMLStreamConstants.START_ELEMENT){
				   stringBuffer.append("<"+reader.getName().toString());
				   for(int i=0;i<reader.getAttributeCount();i++){
					   stringBuffer.append(" "+reader.getAttributeName(i)+"=\""+reader.getAttributeValue(i)+"\""); 
				   }
				   stringBuffer.append(">");
			   }
			  
			   if(next==XMLStreamConstants.CHARACTERS){
				   stringBuffer.append(reader.getText());
			   }
			   if(next==XMLStreamConstants.END_ELEMENT){
				   stringBuffer.append("</"+reader.getName().toString()+">");
				   stringBuffer.append("\r");
			   }
			   
			}
			
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("colspan:"+ (System.currentTimeMillis()-startime)+"millis");
		System.out.println(stringBuffer.toString());
	}
	
	//基于迭代模型
	@Test
	public void testStaxByEvent(){

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		InputStream is = StaxTest.class.getResourceAsStream("/xml/classsroom.xml");
		StringBuffer stringBuffer = new StringBuffer();
		long startime = System.currentTimeMillis();
		XMLEventReader reader = null;
		try {
			reader = xmlInputFactory.createXMLEventReader(is);
			while(reader.hasNext()){
				XMLEvent event = reader.nextEvent();
				  if(event.isStartElement()){
					  StartElement ele =  event.asStartElement();
					   stringBuffer.append("<"+ele.getName().toString());
					   Iterator<Attribute> it=ele.getAttributes();
					   while(it.hasNext()){
						   Attribute attribute = it.next();
						   stringBuffer.append(" "+attribute.getName().toString()+"=\""+attribute.getValue()+"\""); 
					   }
					   stringBuffer.append(">");
				   }

				   if(event.isCharacters()){
					   stringBuffer.append(event.asCharacters());
				   }
				  
				   if(event.isEndElement()){
					   stringBuffer.append("</"+event.asEndElement().getName().toString()+">");
				   }
				   
				}
				
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(stringBuffer);
		System.out.println("colspan:"+ (System.currentTimeMillis()-startime)+"millis");
	}
	
	//基于过滤器
	@Test
	public void testStaxByFilter(){

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		InputStream is = StaxTest.class.getResourceAsStream("/xml/classsroom.xml");
		StringBuffer stringBuffer = new StringBuffer();
		long startime = System.currentTimeMillis();
		XMLEventReader reader = null;
		try {
			reader = xmlInputFactory.createFilteredReader(xmlInputFactory.createXMLEventReader(is),new EventFilter() {
				@Override
				public boolean accept(XMLEvent event) {
					if(event.isStartElement()){
						String name = event.asStartElement().getName().toString();
						if("stuname".equalsIgnoreCase(name)){
							return true;
						}
					}
					if(event.isEndElement()){
						String name = event.asEndElement().getName().toString();
						if("stuname".equalsIgnoreCase(name)){
							return true;
						}
					}
					
					return false;
				}
			});
			while(reader.hasNext()){
				XMLEvent event = reader.nextEvent();
				  if(event.isStartElement()){
					  StartElement ele =  event.asStartElement();
					   stringBuffer.append("<"+ele.getName().toString());
					   Iterator<Attribute> it=ele.getAttributes();
					   while(it.hasNext()){
						   Attribute attribute = it.next();
						   stringBuffer.append(" "+attribute.getName().toString()+"=\""+attribute.getValue()+"\""); 
					   }
					   stringBuffer.append(">");
				   }

				   if(event.isCharacters()){
					   stringBuffer.append(event.asCharacters());
				   }
				  
				   if(event.isEndElement()){
					   stringBuffer.append("</"+event.asEndElement().getName().toString()+">");
				   }
				   
				}
				
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(stringBuffer);
		System.out.println("colspan:"+ (System.currentTimeMillis()-startime)+"millis");
	
	}
	
	@Test
	public void testXpath(){
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		StringBuffer sb = new StringBuffer();
		try {
			DocumentBuilder  documentBuilder = documentBuilderFactory.newDocumentBuilder();
			InputStream is = StaxTest.class.getResourceAsStream("/xml/classsroom.xml");
			Document docment = documentBuilder.parse(is);
			
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodelist = (NodeList)xpath.evaluate("//student[@stuno='s0001']", docment, XPathConstants.NODESET);
			
			for(int i=0;i<nodelist.getLength();i++){
				Node node = (Node) nodelist.item(i);
				NamedNodeMap map = node.getAttributes();
				if(node.getNodeType() == Node.ELEMENT_NODE){
					sb.append("<"+node.getNodeName());
				}
				int attrlen = map.getLength();
				for(int a=0;a<attrlen;a++){
					Node anode = (Node)map.item(a);
					sb.append(" "+anode.getNodeName()+"=\""+anode.getNodeValue()+"\"");
				}
				if(sb.toString().endsWith(">")){
					
				}else{
					sb.append(">");
				}
				if(node.getNodeType() == Node.TEXT_NODE){
					sb.append(node.getTextContent());
				}
				
				bulidChildren(node.getChildNodes(),sb);
				if(node.getNodeType() == Node.ELEMENT_NODE){
					sb.append("</"+node.getNodeName()+">");
				}
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		System.out.println(sb);
		
		
		
	}

	private void bulidChildren(NodeList nodelist, StringBuffer sb) {
		NodeList childNodes = nodelist;
		for (int j = 0; j < childNodes.getLength(); j++) {
			if(childNodes.item(j)==null || isCRLF(childNodes.item(j).getTextContent())){
				continue;
			}
			Node  e = (Node) childNodes.item(j);
			NamedNodeMap map = e.getAttributes();
			if(e.getNodeType() == Node.ELEMENT_NODE){
				sb.append("<"+e.getNodeName());
			}
			int attrlen = map == null ? 0: map.getLength();
			for(int a=0;a<attrlen;a++){
				Node node = map.item(a);
				sb.append(node.getNodeName()+"=\""+map.item(a).getNodeValue()+"\"");
			}
			if(sb.toString().endsWith(">")){
				
			}else{
				sb.append(">");
			}			if(e.getNodeType() == Node.TEXT_NODE){
				sb.append(e.getTextContent());
			}
		
			bulidChildren(e.getChildNodes(),sb);
			if(e.getNodeType() == Node.ELEMENT_NODE){
				sb.append("</"+e.getNodeName()+">");
			}
		}
		
	}
	private boolean isCRLF(String content){
		char[] chars = content.toCharArray();
		for(char c :chars){
			if(c=='\n' || c=='\t'){
				return true;
			}
		}
		return false;
	}
	
	@Test
	public void testXmlStreamWriter() {
		XMLStreamWriter xmlStreamWriter = null;
		try {
			xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(System.out);
			xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
			xmlStreamWriter.writeEndDocument();
			String ns = "http://127.0.0.1/ns";
			xmlStreamWriter.writeStartElement("nsprefix","person",ns);
			xmlStreamWriter.writeAttribute("personId","0001");
			
			xmlStreamWriter.writeComment("some comment here");
			xmlStreamWriter.writeStartElement("nsprefix","personName",ns);
			xmlStreamWriter.writeCharacters("jack");
			
			
			
			xmlStreamWriter.writeEndElement();
			xmlStreamWriter.writeEndElement();
			
			xmlStreamWriter.flush();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(xmlStreamWriter!=null){
				try {
					xmlStreamWriter.close();
				} catch (XMLStreamException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 修改文档节点
	 */
	@Test
	public void testTransform(){
		InputStream  is= this.getClass().getClassLoader().getResourceAsStream("xml/classsroom.xml");
		try {
			DocumentBuilder documentBuilder =  DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = documentBuilder.parse(is);
			
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			NodeList  nodelist = (NodeList) xpath.evaluate("//student[stuname='芳芳']", doc, XPathConstants.NODESET);
			
			Element studentEle = (Element)nodelist.item(0);
			Element ele = (Element) studentEle.getElementsByTagName("mobile").item(0);
			ele.setTextContent("02713456789");
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "true");
			
			transformer.transform(new DOMSource(doc),new StreamResult(System.out));
			
		}  catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
}
