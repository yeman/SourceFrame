package com.holly.socket.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ISendSocketMessage {

		public void sendMessage(String msg) throws IOException;
		public void sendBytes(byte[] bt) throws IOException;
		public void sendFile(File file) throws FileNotFoundException, IOException;
		
}
