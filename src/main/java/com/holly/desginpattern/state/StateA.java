package com.holly.desginpattern.state;

public class StateA extends State{

	@Override
	public void handle(StateContext context) {
		context.setState(new StateB());
	}

}
