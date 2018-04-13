package com.holly.desginpattern.mediator;

public class TestMediatorPattern {
	public static void main(String[] args) {
		ConcreateMediator mediator = new ConcreateMediator();
		
		Colleague colleagueA = new ColleagueA(mediator);
		Colleague colleagueB = new ColleagueB(mediator);
		mediator.setColleaguea(colleagueA);
		mediator.setColleagueb(colleagueB);
		colleagueA.sendMessage("房子卖多少W?");
		colleagueB.sendMessage("200W");
		colleagueA.sendMessage("能不能便宜点");
	}
}
