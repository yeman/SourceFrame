package com.holly.desginpattern.singleton;

public class TestWeek {
	public static void main(String[] args) {
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				String threadName = Thread.currentThread().getName();
				//LazySingleton single = LazySingleton.getInstance();
				//System.out.println("线程"+threadName+"\t=>"+single.hashCode());
				//HungerSingleton single = HungerSingleton.getInstance();
				//System.out.println("线程"+threadName+"\t=>"+single.hashCode());
				SyncSingleton single = SyncSingleton.getInstance();
				System.out.println("线程"+threadName+"\t=>"+single.hashCode());
			}
		};
		
		for (int i = 0; i < 100; i++) {
			new Thread(r,""+i).start();
			
		}
	}
}
