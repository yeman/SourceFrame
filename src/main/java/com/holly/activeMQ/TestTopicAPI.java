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
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class TestTopicAPI {

	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
	private static final long expiration = 3000L;
	
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
				message.setJMSExpiration(expiration);
				messageProducer.send(message);
			}
		} catch (JMSException e) {
			System.out.println("出错. ");
			e.printStackTrace();
		}

	}

	public void producer(String topicName, String content, int sendnum ) {
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
			destination = new ActiveMQTopic(topicName);
			MessageProducer messageProducer = session.createProducer(destination);
			messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);//持久订阅
			sendTxtMsg(session, messageProducer, content, sendnum);
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
	
	public void Consumer(String clientID,String topicname) {
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
		MessageConsumer consumer = null;
		try {
			connection.setClientID(clientID);
			connection.start();
			session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(topicname);
			consumer = session.createDurableSubscriber(topic, clientID);
			String msg = null;
			while(true){
				msg = reciveMsg(clientID,consumer);
				if(!StringUtils.isEmpty(msg)){
					msg = reciveMsg(clientID,consumer);
				}else{
					break;
				}
			}
			
			//session.commit();

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(consumer!=null){
				try {
					consumer.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
	
	
	
	
	private String reciveMsg(String clientID,MessageConsumer consumer) {
		Message msg = null; 
		String content=null;
		try {
			msg = consumer.receive(expiration);
			if(msg instanceof TextMessage){
				TextMessage  textMessage= (TextMessage)msg;
				content = textMessage.getText();
			}
			System.out.println(clientID+" 收到消息：" + content);
			return content;
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	@Test
	public  void testTopicPublish(){
		producer("topic01", "好消息333", 1);
	}
	
	@Test
	public void testConsumer(){
		Consumer("qqq","topic01");
		Consumer("aaa","topic01");
		Consumer("zzz","topic01");
	}
	

}
