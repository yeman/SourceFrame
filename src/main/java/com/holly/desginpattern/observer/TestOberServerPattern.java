package com.holly.desginpattern.observer;

public class TestOberServerPattern {

	public static void main(String[] args) {
		Subject subject = new ConcreateSubject("邮件群发");
		
		subject.addAttach(new OberServerA("jackyin",subject));
		subject.addAttach(new OberServerB("jackson",subject));
		subject.addAttach(new OberServerB("tina",subject));
		subject.setState(true);
		subject.notifyOberserver();
		//======================================
		subject.setState(false);//停用
		subject.notifyOberserver();
		
	}
}
