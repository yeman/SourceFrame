package com.holly.activeMQ.service;

import java.util.Set;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.holly.activeMQ.bean.Message;
import com.holly.cache.redis.RedisClient;
import com.holly.json.JSONUtil;
import com.holly.util.PropertiesUtils;

import lombok.extern.log4j.Log4j;

@Log4j
@Repository(value="defaultMessageConsumer")
public class DefaultMessageConsumer{
	
	@Resource(name="connectionFactory")
	private ConnectionFactory connectionFactory;
	
	@Resource(name = "redisClient")
	private RedisClient redisClient;
	final long timeout = Long.parseLong(PropertiesUtils.getProperty("activeMq.recive.timeout"));
	
	public Message reciveMessage(String userCode,String messageSendType) throws JMSException {
		Message rtmessage = new Message();
		Connection connection =null; 
		Session session = null;
		MessageConsumer messageConsumer = null;
		connection = connectionFactory.createConnection();
		session = connection.createSession(Boolean.TRUE, Session.CLIENT_ACKNOWLEDGE);
		try {
			if("1".equals(messageSendType)){
				connection.start();
				
				final String destionation = PropertiesUtils.getProperty("activeMq.queue")+"."+userCode;
				ActiveMQQueue activeMQQueue = new ActiveMQQueue(destionation);
				messageConsumer =  session.createConsumer(activeMQQueue);
				javax.jms.Message message = messageConsumer.receive(timeout);
				if(message instanceof ActiveMQTextMessage){
					ActiveMQTextMessage textMessage = (ActiveMQTextMessage)message;
					String jsonStr = redisClient.rpop(destionation);
					if(StringUtils.isNotEmpty(jsonStr)){
						rtmessage = JSONUtil.toBean(jsonStr, Message.class);
						rtmessage.setStatus("1");
						System.out.println("recive:"+jsonStr);
						
					}
				}
				
				
			}
			if("2".equals(messageSendType)){
				 final String destionation = PropertiesUtils.getProperty("activeMq.topic")+"group."+"jack";
				 Topic topic = session.createTopic(destionation);
				//模拟数据库
				Set<String> dbUserCodes = redisClient.smembers("group");
				for(String dbuserCode: dbUserCodes){
					if(userCode.equals(dbuserCode)){
						messageConsumer =  session.createDurableSubscriber(topic, userCode);
						connection.start();
						javax.jms.Message message = messageConsumer.receive(timeout);
						if(message instanceof ActiveMQTextMessage){
							ActiveMQTextMessage textMessage = (ActiveMQTextMessage)message;
							System.out.println("#########"+textMessage.getText());
							//String jsonStr = redisClient.lpop(destionation);//获取消息
							//if(StringUtils.isNotEmpty(jsonStr)){
							//	rtmessage = JSONUtil.toBean(jsonStr, Message.class);
							//	System.out.println("recive:"+jsonStr);
							//}
							//redisClient.srem("group", userCode);
						}
					}
				}
				
				
			}
			
		  session.commit();
		} catch (NumberFormatException e) {
			session.rollback();
			log.error(e.getMessage());
			e.printStackTrace();
		}finally {
			if(messageConsumer!=null){
				messageConsumer.close();
			}
			if(session!=null){
				session.close();
			}
			if(connection!=null){
				connection.close();
			}
		}
		return rtmessage;
	}
	
	
	

}
