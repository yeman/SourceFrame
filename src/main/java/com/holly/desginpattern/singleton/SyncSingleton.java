package com.holly.desginpattern.singleton;

public class SyncSingleton {
	private static int count;
	private static SyncSingleton syncSingleton;
	
	private SyncSingleton(){
		System.out.println("SyncSingleton私有构造器执行 "+(++count));
	}
	public static SyncSingleton getInstance(){
		if(syncSingleton==null){
			synchronized (SyncSingleton.class) {
				if(syncSingleton==null){
					syncSingleton = new SyncSingleton();
				}
			}
		}
		return syncSingleton;
	}
	
}
