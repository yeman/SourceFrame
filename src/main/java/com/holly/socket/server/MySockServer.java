package com.holly.socket.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MySockServer {
	private static final int port = 65534;
	private static final int timeOut = 100 * 1000;
	private static ServerSocket serverSocket;

	static {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private MySockServer() {

	}

	public static Socket getServerSocketInstance() throws IOException {
		Socket socket = serverSocket.accept();
		return socket;
	}

	public static void closeServerSocket() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
