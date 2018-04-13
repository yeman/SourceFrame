package com.holly.cache.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.TypeReference;

public interface IRedisClient {
	
	public <T> T get(String key, TypeReference<T> typeReference);

	public <T> void set(String key, T value);

	public String setObject(String key, Object value);

	public <T> void setex(String key, T value, int time);

	public String setexObject(String key, Object value, int time);

	public <T> T get(String key, Class<T> clazz);

	public Object getObject(String key);

	public void remove(String key);

	public void removeObject(String key);

	public Long hsetObject(String key, String field, Object value);

	public Object hgetObject(String key, String field);

	public void hdelObject(String key, String field);

	public Map<String, Object> hgetAllObjects(String key);

	public Long expire(String key, int seconds);

	public String select(int index);

	public String flushDB();

	public Long dbSize();

	public Set<String> getAllKey();

	public List getAllValue();

	public Set<String> getKeys(String pattern);
}
