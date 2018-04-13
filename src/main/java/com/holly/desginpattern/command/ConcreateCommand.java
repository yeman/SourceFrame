package com.holly.desginpattern.command;

import java.io.IOException;

public class ConcreateCommand extends Command{

	public ConcreateCommand(Receiver receiver) {
		super(receiver);
	}

	@Override
	public void execute(String cmd) throws IOException {
		receiver.action(cmd);
	}

}
