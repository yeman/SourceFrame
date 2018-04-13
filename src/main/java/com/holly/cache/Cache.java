package com.holly.cache;

public interface Cache {
	
	/**
	 * 添加缓存
	 * @param key
	 * @param value
	 */
	public void put(Object key,Object value) ;
	
	/**
	 * 获取缓存数据
	 * @param key
	 * @return
	 */
	public Object get(Object key);
	
	/**
	 * 清楚缓存
	 */
	public void clear();
	
	/**
	 * 
	 */
	public void destory();
	
	/**
	 * 判断缓存是否包含
	 * @param key
	 * @return
	 */
	public Object contatins(Object key);
	
	/**
	 * 获取缓存所有剑指
	 * @return
	 */
	public Object getAllKeys();
}
