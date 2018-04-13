package com.holly.socket.server;

import java.io.File;
import java.io.IOException;

public class TestSend {

	public static void main(String[] args) throws IOException {
		ISendSocketMessage send = new SendSocketMessage();
		//System.out.println("server send message begin....");
		//send.sendMessage("测试");
		//System.out.println("server send message end....");
		
		System.out.println("server send file begin....");
		send.sendFile(new File("c:/1_tivonalh.jpg"));
		System.out.println("server send file end....");
	}
}
