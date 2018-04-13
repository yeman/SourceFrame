package com.holly.desginpattern.express;

//解释器
public class KeyWordExpression extends AbstractExpression{

	@Override
	public void interpreter(Context context) {
		if("limit".equals(context.getKeyWord())){
			context.setOutput("关键字 "+ context.getKeyWord().toUpperCase());
		}else if("Abstract".equals(context.getKeyWord())){
			context.setOutput("关键字 "+ context.getKeyWord().toUpperCase());
		}else{
			context.setOutput("关键字 "+ context.getKeyWord().toLowerCase());
		}
		System.out.println("KeyWordExpression 输出"+context.getOutput());
		
	}

}
