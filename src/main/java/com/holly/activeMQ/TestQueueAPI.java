package com.holly.activeMQ;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class TestQueueAPI {

	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;//"failover:(tcp://172.16.52.3:61616,tcp://172.16.52.5:61616,tcp://172.16.52.8:61616)?randomize=false";//ActiveMQConnection.DEFAULT_BROKER_URL;
	private static final long expiration = 10000;
	public Connection getConntention() {

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKER_URL);
		try {
			return connectionFactory.createConnection();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	private void sendTxtMsg(Session session, MessageProducer messageProducer, String content, int sendnum) {
		try {
			for (int i = 0; i < sendnum; i++) {
				TextMessage message = session.createTextMessage("第"+(i+1)+"条:"+content);
				System.out.println(message.getText());
				messageProducer.send(message);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	public void producer(String queueName, String content, int sendnum) {
		// 1创建ConnectionFactory工厂
		// 2 通过连接工厂获取连接
		// 3 启动连接
		// 4 创建session
		// 5 创建消息队列
		// 6 发送消息
		// 7 提交会话
		// 8 关闭连接
		Connection connection = null;
		Session session = null;
		connection = getConntention();
		Destination destination = null;
		try {
			connection.start();
			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			destination = new ActiveMQQueue(queueName);
			MessageProducer messageProducer = session.createProducer(destination);
			messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);//非持久化
			messageProducer.setPriority(2);
			//messageProducer.setTimeToLive(8000);//设置消息过期时间8秒
			sendTxtMsg(session, messageProducer, content, sendnum);
			messageProducer.close();
			session.commit();

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	public void Consumer(String queueName) {
		// 1创建ConnectionFactory工厂
		// 2 通过连接工厂获取连接
		// 3 启动连接
		// 4 创建session
		// 5 创建消息队列
		// 6 发送消息
		// 7 提交会话
		// 8 关闭连接
		Connection connection = null;
		Session session = null;
		connection = getConntention();
		Destination destination = null;
		try {
			connection.start();
			session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			destination = new ActiveMQQueue(queueName);
			MessageConsumer consumer = session.createConsumer(destination);
			String msg =null;
			while(true){
				msg = reciveMsg(consumer);
				if(!StringUtils.isEmpty(msg)){
					msg = reciveMsg(consumer);
				}else{
					break;
				}
			}
			
			//session.commit();

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	
	
	
	private String reciveMsg(MessageConsumer consumer) {
		Message msg = null; 
		String content=null;
		try {
			msg = consumer.receive(expiration);
			if(msg instanceof TextMessage){
				TextMessage  textMessage= (TextMessage)msg;
				content = textMessage.getText();
				
			}
			System.out.println("Consumer msg "+content);
			return content;
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	@Test
	public void testPublish(){
		producer("test02", "ccccccc", 10);
	}
	
	@Test
	public void testConsumer(){
		Consumer("test02");
	}

}
