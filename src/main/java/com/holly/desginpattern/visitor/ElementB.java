package com.holly.desginpattern.visitor;

public class ElementB extends Element {

	@Override
	public void accept(Visitor vistor) {
		vistor.visitElementB(this);

	}

}
