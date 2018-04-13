package com.holly.socket.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SendSocketMessage implements ISendSocketMessage {

	@Override
	public void sendMessage(String msg) throws IOException {
		Socket socket = MySockServer.getServerSocketInstance();
		OutputStream os = socket.getOutputStream();
		os.write(msg.getBytes());
		if (os != null) {
			os.close();
		}

	}

	@Override
	public void sendBytes(byte[] bt) throws IOException {
		Socket socket = MySockServer.getServerSocketInstance();
		OutputStream os = socket.getOutputStream();
		os.write(bt);
		if (os != null) {
			os.close();
		}

	}

	@Override
	public void sendFile(File file) throws IOException {
		byte[] sendByte = null;
		int length = 0;
		OutputStream os = null;
		FileInputStream is = null;
		Socket socket = MySockServer.getServerSocketInstance();
		is = new FileInputStream(file);
		BufferedInputStream reader = new BufferedInputStream(is);
		try {
			sendByte = new byte[1024];
			while ((length = reader.read(sendByte, 0, sendByte.length)) > 0) {
				os = socket.getOutputStream();
				os.write(sendByte, 0, length);
			}
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				os.close();
			}
			if (reader != null) {
				reader.close();
			}
			if (is != null) {
				is.close();
			}
		}

	}

}
