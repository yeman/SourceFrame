package com.holly.desginpattern.visitor;

public class ElementA extends Element{

	@Override
	public void accept(Visitor vistor) {
		vistor.visitElementA(this);
		
	}

}
