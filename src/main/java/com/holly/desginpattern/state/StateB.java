package com.holly.desginpattern.state;

public class StateB extends State{
	@Override
	public void handle(StateContext context) {

		context.setState(new StateA());
	}

}
