package com.holly.desginpattern.iterator;

public abstract class MyIterator {

	protected abstract Object first();
	protected abstract Object next();
	protected abstract boolean isDone();
	protected abstract Object currentItem();
}
