package com.holly.desginpattern.memento;

public class Originator {
	private Note note;

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}
	
	public Memento createMementor(){
		return new Memento(note);
	}
	public void setMementor(Memento memento){
		note = memento.getNode();
	}
	public void show(){
		System.out.println(this.note);
	}
	
}
