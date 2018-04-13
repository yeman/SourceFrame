package com.holly.desginpattern.memento;

public class Memento {
	private Note note;

	public Memento(Note note) {
		this.note = note;
	}

	public Note getNode(){
		return this.note;
	}

}
