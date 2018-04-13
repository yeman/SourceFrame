package com.holly.desginpattern.command;

import java.io.IOException;

public class Invoker {
	private Command command;
	public Invoker(Command command){
		this.command = command;
	} 
	public void executeCommand(String code) throws IOException{
		command.execute(code);
	}
}
