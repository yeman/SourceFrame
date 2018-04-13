package com.holly.desginpattern.visitor;

import java.util.ArrayList;
import java.util.List;

public class ObjectStructure {
	private List<Element> elements = new ArrayList<Element>();

	public void add(Element element) {
		elements.add(element);
	}

	public void remove(Element element) {
		elements.remove(element);
	}
	public void accept(Visitor visitor){
		for(Element element : elements ){
			element.accept(visitor);
		}
	}

}
