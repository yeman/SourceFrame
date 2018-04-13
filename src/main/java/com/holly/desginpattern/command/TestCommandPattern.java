package com.holly.desginpattern.command;

import java.io.IOException;
import java.net.InetAddress;

public class TestCommandPattern {

	public static void main(String[] args) throws IOException {
		Receiver receiver = new Receiver();
		ConcreateCommand command = new ConcreateCommand(receiver);
		Invoker invoker = new Invoker(command);
		invoker.executeCommand("ipconfig /all");
	}
}
