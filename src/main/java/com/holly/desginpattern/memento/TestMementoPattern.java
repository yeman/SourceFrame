package com.holly.desginpattern.memento;

public class TestMementoPattern {
	public static void main(String[] args) {
		Originator originator  = new Originator();
		originator.setNote(new Note("title01","content01"));
		originator.show();
		
		//备份,修改
		Caretaker caretaker = new Caretaker();
		caretaker.setMemento(originator.createMementor());
		originator.setNote(new Note("modify title","modify content"));
		originator.show();
		
		//恢复
		originator.setMementor(caretaker.getMemento());
		originator.show();
	}
}
