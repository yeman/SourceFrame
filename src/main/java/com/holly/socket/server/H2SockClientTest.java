package com.holly.socket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class H2SockClientTest {
	private static final String IP = "130.87.7.44";
	private static final int port = 10000;
	private static final int timeOut = 100 * 1000;
	private static Socket socket;

	static {
		try {
			 socket = new Socket();
	         socket.connect(new InetSocketAddress(IP, port),timeOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private H2SockClientTest() {

	}

	public static Socket getSocketInstance() throws IOException {
		return socket;
	}


	public static void closeSocket(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		try {
			Socket socket = H2SockClientTest.getSocketInstance();
			System.out.println(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
