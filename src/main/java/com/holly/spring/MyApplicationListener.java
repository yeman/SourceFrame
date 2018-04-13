package com.holly.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.holly.activeMQ.controller.ProducerController;

import redis.clients.jedis.ShardedJedisPool;
//可以进行初始化
public class MyApplicationListener implements ApplicationListener<ContextRefreshedEvent>{


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		 ApplicationContext context =  event.getApplicationContext();
		// ShardedJedisPool bean1 = (ShardedJedisPool)context.getBean("shardedJedisPool");
		// ProducerController bean2 = (ProducerController)context.getBean("producerController");
		
	}

}
