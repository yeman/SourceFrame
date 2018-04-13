package com.holly.desginpattern.express;

public class TestExpressPattern {
	public static void main(String[] args) {
		Context context = new Context();
		context.setKeyWord("limit");
		
		AbstractExpression keyWordExpression = new KeyWordExpression();
		AbstractExpression dataTypeExpression = new DataTypeExpression();
		keyWordExpression.interpreter(context);
		
		context.setKeyWord("String");
		dataTypeExpression.interpreter(context);
	}
}
