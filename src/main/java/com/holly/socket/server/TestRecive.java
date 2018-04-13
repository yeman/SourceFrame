package com.holly.socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TestRecive {

	public static void main(String[] args) throws IOException {
		Socket socket = SockClient.getSocketInstance();
		InputStream is = socket.getInputStream();
		InputStreamReader isr =new  InputStreamReader(is);
		BufferedReader br =new BufferedReader(isr);
	    String info =null;
		while((info=br.readLine())!=null){
			 System.out.println("@@Client"+info);
		}
	}
}
