package com.holly.desginpattern.state;

public class TestStatePattern {
	public static void main(String[] args) {
		State state = new StateA();
		StateContext context = new StateContext(state);
		context.request();
		context.request();
	}
}
