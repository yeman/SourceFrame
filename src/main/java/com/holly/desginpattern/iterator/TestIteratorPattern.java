package com.holly.desginpattern.iterator;

public class TestIteratorPattern {

	public static void main(String[] args) {
		ConcreateAggrage aggregate = new ConcreateAggrage();
		aggregate.add("1");
		aggregate.set(1, "a");
		aggregate.add("1");
		aggregate.add("aaaa");
		aggregate.add("bbb");
		MyIterator myIterator = aggregate.createIterator();
		System.out.println(myIterator.first());
		while (!myIterator.isDone()) {
			System.out.println(myIterator.next());
			
		}
	}
}
