package com.holly.socket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SockClient {
	private static final String IP = "127.0.0.1";
	private static final int port = 65534;
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

	private SockClient() {

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

}
