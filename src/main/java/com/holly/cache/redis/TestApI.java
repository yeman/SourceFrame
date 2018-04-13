package com.holly.cache.redis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class TestApI {
	
	
	private Jedis jedis;
	
	private final String host="127.0.0.1";
	private final int port= 6379;
	private final String password="12345678Az";
	
	@Before
	public void setUp(){
		jedis = new Jedis(host,port);
		String isOk = jedis.auth(password);
		System.out.println("jedis connect status "+ isOk);
	}
	
	@Test
	public void testStringApi(){
		jedis.set("testname", "jack");
		System.out.println("testname:"+jedis.get("testname"));
		
		jedis.append("testname", "append some string here");

		System.out.println("testname:"+jedis.get("testname"));
		jedis.del("testname");
		System.out.println("testname"+jedis.get("testname"));
		
		jedis.mset("name","jack","age","22","email","xxx@gmail.com");
		jedis.incr("age");
		System.out.println(jedis.get("name")+" "+ jedis.get("age")+" "+jedis.get("email")) ; 
		
	}
	
	@Test
	public void testOperateMap(){
		Map map = new HashMap<String,Object>();
		map.put("name", "jackjohns");
		map.put("age", "22");
		map.put("address", "others");
		jedis.hmset("user1key", map);
		//根据key获取map中的values值为list
		System.out.println("map value list "+jedis.hmget("user1key", "name","age"));
		//删除map中的某个key
		jedis.hdel("user1key", "age");
		System.out.println("age "+jedis.hmget("user1key", "age"));
		System.out.println("map"+jedis.hmget("user1key", "name","age","address"));
		System.out.println("map 键值对个数"+jedis.hlen("user1key"));
		System.out.println("exists "+jedis.exists("user1key"));
		System.out.println(" all keys  "+jedis.hkeys("user1key"));
		System.out.println(" all values "+jedis.hvals("user1key"));
		//迭代key
		Iterator<String> keyIterator = jedis.hkeys("user1key").iterator();
		while(keyIterator.hasNext()){
			String key = keyIterator.next();
			System.out.println(" key "+key + " value: " + jedis.hget("user1key", key));
		}
		
	}
	
	@Test
	public void testOperateList(){
		jedis.del("java framework");
		System.out.println("jedis.lrange "+jedis.lrange("java framewordk", 0, -1));
		jedis.lpush("java framework", "struts");
		jedis.lpush("java framework", "spring");
		jedis.lpush("java framework", "myibatis");
		System.out.println("jedis.lrange "+jedis.lrange("java framework", 0, -1));
		
		jedis.del("java framework");
		jedis.rpush("java framework", "struts");
		jedis.rpush("java framework", "spring");
		jedis.rpush("java framework", "myibatis");
		System.out.println("jedis.lrange "+jedis.lrange("java framework", 0, -1));
		
	}
	
	

}
