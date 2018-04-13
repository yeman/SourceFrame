package com.holly.desginpattern.visitor;

public class TestVisitorPattern {
	public static void main(String[] args) {
		ConcreateVisitor visitor = new ConcreateVisitor();
		ObjectStructure objectStructure = new ObjectStructure();
		objectStructure.add(new ElementA());
		objectStructure.add(new ElementB());
		objectStructure.accept(visitor);
		ConcreateVisitorB visitorb = new ConcreateVisitorB();
		objectStructure.accept(visitorb);
	}
}
