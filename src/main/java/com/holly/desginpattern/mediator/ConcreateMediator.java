package com.holly.desginpattern.mediator;
//中介者实现类,协调两者关系
public class ConcreateMediator extends Mediator {
	private Colleague colleaguea;
	private Colleague colleagueb;

	public void setColleaguea(Colleague colleaguea) {
		this.colleaguea = colleaguea;
	}

	public void setColleagueb(Colleague colleagueb) {
		this.colleagueb = colleagueb;
	}
	
	//协调两者关系
	@Override
	public void sendMessage(String message, Colleague colleague) {
		if(colleague ==colleaguea){
			colleagueb.notify(message);
		}else if(colleague ==colleagueb){
			colleaguea.notify(message);
		}else{
			System.out.println("Error 暂未扩展");
		}
	}

}
