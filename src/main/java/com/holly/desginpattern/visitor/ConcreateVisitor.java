package com.holly.desginpattern.visitor;

public class ConcreateVisitor extends Visitor{

	@Override
	public void visitElementA(ElementA element) {
		System.out.println(this.getClass().getSimpleName()+"访问"+element.getClass().getSimpleName());
		
	}

	@Override
	public void visitElementB(ElementB element) {
		System.out.println(this.getClass().getSimpleName()+"访问"+element.getClass().getSimpleName());
	}

}
