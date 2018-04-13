package com.holly.desginpattern.express;

//数据类型解释器
public class DataTypeExpression extends AbstractExpression {

	@Override
	public void interpreter(Context context) {
		if ("int".equals(context.getKeyWord())) {
			context.setOutput("数据类型  int " + context.getKeyWord().toUpperCase());
		} else if ("String".equals(context.getKeyWord())) {
			context.setOutput("数据类型 String " + context.getKeyWord().toUpperCase());
		} else {
			context.setOutput("关键字 " + context.getKeyWord().toLowerCase());
		}
		System.out.println("DataTypeExpression 输出 " + context.getOutput());

	}

}
