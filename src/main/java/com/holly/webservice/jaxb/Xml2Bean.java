package com.holly.webservice.jaxb;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.holly.domain.UserInfo;

public class Xml2Bean {

	@Test
	public void testXml2Bean() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><userInfo><dept><deptId>org0001</deptId><deptName>质保</deptName><parentDeptId>org0000</parentDeptId></dept><passwd>12345678</passwd><userCode>00001</userCode><userId>0001</userId><userName>123</userName></userInfo>";
		InputStreamReader inputStreamReader = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(UserInfo.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			//
			inputStreamReader = new InputStreamReader(new StringBufferInputStream(xml), "UTF-8");
			Object obj = unmarshaller.unmarshal(inputStreamReader);
			System.out.println(obj);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
