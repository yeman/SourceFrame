package com.holly.desginpattern.iterator;

import java.util.ArrayList;
import java.util.List;


public class ConcreateIterator extends MyIterator{
	private ConcreateAggrage aggregate;
	private  int count;
	
	ConcreateIterator(ConcreateAggrage aggregate ){
		 this.aggregate = aggregate;
	}
	
	@Override
	protected Object first() {
		return aggregate.get(0);
	}

	@Override
	protected synchronized Object next() {
		if(aggregate.size()> count){
			count++;
			return aggregate.get(count-1);
		}
		return null;
	}

	@Override
	protected boolean isDone() {
		if(aggregate.size() > count){
			return false;
		}
		return true;
	}

	@Override
	protected Object currentItem() {
		return aggregate.get(count);
	}

}
