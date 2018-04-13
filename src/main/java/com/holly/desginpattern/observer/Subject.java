package com.holly.desginpattern.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
	protected String name;
	protected boolean state;
	protected List<OberServer> list = new ArrayList<OberServer>();

	public Subject(String name) {
		this.name = name;
	}

	public abstract boolean getState();

	public abstract void setState(boolean state);

	public abstract void notifyOberserver();

	public void addAttach(OberServer oberServer) {
		list.add(oberServer);
	}

	public void delAttach(OberServer oberServer) {
		list.remove(oberServer);
	}

}
