package com.holly.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public abstract class CustomCache {
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final ReadLock rl = lock.readLock();
	private final WriteLock wl = lock.writeLock();
	private ConcurrentMap<String, Object> map = new ConcurrentHashMap<String, Object>();
	
	
}
