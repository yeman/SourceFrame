package com.holly.desginpattern.chain;

public abstract class Handler {
	protected Handler chain;

	public abstract void chain(Handler handler);

	public void setProcessor(Handler handler) {
		this.chain = handler;

	}
}
