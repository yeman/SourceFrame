package com.holly.activeMQ.service;

import java.util.Properties;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.MessageId;
import org.apache.activemq.command.ProducerId;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Repository;

import com.holly.activeMQ.BulidBaseMessage;
import com.holly.activeMQ.bean.Message;
import com.holly.cache.redis.RedisClient;
import com.holly.json.JSONUtil;
import com.holly.util.PropertiesUtils;
import com.holly.util.UUIDGenerator;

import lombok.extern.log4j.Log4j;

@Log4j
@Repository(value="messageProducer")
public class MessageProducer {

	@Resource(name = "jmsQueueTemplate")
	private JmsTemplate jmsQueueTemplate;

	@Resource(name = "jmsTopicTemplate")
	private JmsTemplate jmsTopicTemplate;

	@Resource(name = "redisClient")
	private RedisClient redisClient;
	
	public boolean sendMessage(final Message message) {
		try {
			//设置Destion
			if("1".equals(message.getMessageSendType())){
				final String destionation = PropertiesUtils.getProperty("activeMq.queue")+"."+message.getToUser();
				executeSend(destionation,message);
			}else if("2".equals(message.getMessageSendType())){
				final String destionation = PropertiesUtils.getProperty("activeMq.topic")+"group."+message.getToUser();
				executeSendTopic(destionation,message);
			}else{
				log.error(" unsupport messagetype ");
				throw new RuntimeException(" unsupport messagetype ");
			}
			
			return true;
		} catch (JmsException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	//发送点对点消息
	public void executeSend(final String destionation,final Message message){
		jmsQueueTemplate.send(destionation,new MessageCreator() {
			@Override
			public javax.jms.Message createMessage(Session session) throws JMSException {
				BulidBaseMessage baseMessage = new BulidBaseMessage(session);
				org.apache.activemq.Message  activeMQMessage = baseMessage.bulidMessage(message);
				// 存入数据库 省略 用redis临时代替
				redisClient.lpush(destionation, JSONUtil.toJsonString(message));
				return activeMQMessage;
			}
		});
	}
	//发送订阅消息
	public void executeSendTopic(final String destionation,final Message message){
		jmsTopicTemplate.send(destionation,new MessageCreator() {
			@Override
			public javax.jms.Message createMessage(Session session) throws JMSException {
				BulidBaseMessage baseMessage = new BulidBaseMessage(session);
				org.apache.activemq.Message  activeMQMessage = baseMessage.bulidMessage(message);
				// 存入数据库 省略 用redis临时代替
				redisClient.lpush(destionation, JSONUtil.toJsonString(message));
				return activeMQMessage;
			}
		});
	}

}
