package com.holly.desginpattern.iterator;

import java.util.ArrayList;
import java.util.List;

public class ConcreateAggrage extends Aggregate{
	private List<Object> items = new ArrayList<Object>();

	@Override
	public MyIterator createIterator() {
		return new ConcreateIterator(this);
	}

	public Object get(int i) {
		return items.get(i);
	}

	public int size() {
		return items.size();
	}
	public void set (int index,Object element){
		items.add(index, element);
	}
	public void add(Object element){
		items.add(element);
	}

}
