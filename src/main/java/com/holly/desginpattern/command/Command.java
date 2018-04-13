package com.holly.desginpattern.command;

import java.io.IOException;

public abstract class Command {
	protected Receiver receiver;
	public Command(Receiver receiver ){
		this.receiver = receiver;
	}
	public abstract void execute(String cmd) throws IOException;
	
}	
