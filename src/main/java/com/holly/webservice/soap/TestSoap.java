package com.holly.webservice.soap;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.holly.domain.Dept;
import com.holly.domain.UserInfo;

public class TestSoap {

	@Test
	public void testBuildSoap() {
		SOAPMessage soapMessage = null;
		try {
			soapMessage = MessageFactory.newInstance().createMessage();
			SOAPBody soapBody = soapMessage.getSOAPBody();
			// 目标命名空间
			QName qname = new QName("http://jaxws.webservice.holly.com", "login", "ns");
			SOAPBodyElement soapBodyElement = soapBody.addBodyElement(qname);
			soapBodyElement.addChildElement("username").setTextContent("testa");
			soapBodyElement.addChildElement("password").setTextContent("testbb");
			soapMessage.writeTo(System.out);

		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送soap消息
	 */
	@Test
	public void testSendSoap(){
		//1 创建服务
		//2 创建dispatch
		//3 创建soapMessage
		//4创建Qname指定消息中传递消息
		//5通过dispatch传递消息,会返回相应的消息
		URL url;
		try {
			url = new URL("http://localhost:9999/ns?wsdl");
			String ns = "http://jaxws.webservice.holly.com/";
			QName qname = new QName(ns,"myservice");
			Service service = Service.create(url,qname);
			
			Dispatch<SOAPMessage> dispatch =  service.createDispatch(new QName(ns, "testPort"),SOAPMessage.class,Service.Mode.MESSAGE);
			
			//创建消息
			SOAPMessage soapMessage = null;
			soapMessage = MessageFactory.newInstance().createMessage();
			SOAPBody soapBody = soapMessage.getSOAPBody();
			// 目标命名空间
			QName ename = new QName(ns, "login", "nn");
			SOAPBodyElement soapBodyElement = soapBody.addBodyElement(ename);
			soapBodyElement.addChildElement("username").setTextContent("testa");
			soapBodyElement.addChildElement("password").setTextContent("testbb");
			soapMessage.writeTo(System.out);
			
			
			SOAPMessage response =  dispatch.invoke(soapMessage);
			Document doc = response.getSOAPBody().extractContentAsDocument();
			Element ele = (Element) doc.getElementsByTagName("deptName").item(0);
			System.out.println("部门:"+ele.getTextContent())	;
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (SOAPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//基于payload方式传递
	@Test
	public void testSendByPayLoad(){
		//1 创建服务
		try {
			URL url = new URL("http://localhost:9999/ns?wsdl");
			String ns ="http://jaxws.webservice.holly.com/";
			QName qname = new QName(ns, "myservice");
			Service service =  Service.create(url,qname);
			
			//2 创建Dispatch 
			Dispatch<Source> dispatch = service.createDispatch(new QName(ns, "testPort"),Source.class,Mode.PAYLOAD);
			
			//3 封装Source信息(通过对象反编排的方式)
			Dept dept = new Dept("org002", "销售部", "org0000");
			UserInfo u1 = new UserInfo("00001","0001",dept,"jackson","tony");
			JAXBContext jaxbContext = JAXBContext.newInstance(UserInfo.class);
			Marshaller marshaller =jaxbContext.createMarshaller();
			StringWriter writer = new StringWriter();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			marshaller.marshal(u1,writer);
			System.out.println(writer);
			String payload = "<nn:add>"+writer.toString()+"</nn:add>";
			
			
			Reader reader = new StringReader(payload);
			Source source = new StreamSource(reader);
			
			Source response = dispatch.invoke(source);
			//4 将 返回的source转换为dom进行操作
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Result result = new DOMResult();
			transformer.transform(source, result);
			
			XPath xpath = XPathFactory.newInstance().newXPath();
			//xpath.evaluate("", item, returnType)
			
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
