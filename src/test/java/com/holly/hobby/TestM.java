package com.holly.hobby;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class TestM {

	public static void main(String[] args) {
		String xml = null;
		String retxml="";
		try {
			retxml = URLDecoder.decode(xml, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(retxml);
		
		
		
	}
}
