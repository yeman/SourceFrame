package com.holly.webservice.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import com.holly.domain.Dept;
import com.holly.domain.UserInfo;

public class Bean2Xml {
	
	@Test
	public void testBean2Xml(){
		try {
			JAXBContext jaxb = JAXBContext.newInstance(UserInfo.class);
			Marshaller marshaller = jaxb.createMarshaller();
			UserInfo userInfo = new UserInfo("0001", "00001", new Dept("org0001","质保","org0000"), "管理员", "12345678");
			marshaller.marshal(userInfo, System.out);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} 
}
