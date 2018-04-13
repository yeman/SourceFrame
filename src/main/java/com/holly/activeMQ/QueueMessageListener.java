package com.holly.activeMQ;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;

public class QueueMessageListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		if(message instanceof ActiveMQTextMessage){
			ActiveMQTextMessage msg = (ActiveMQTextMessage)message;
			String messageId = msg.getJMSMessageID();
			System.out.println("messageId"+messageId);
			try {
				String content = msg.getText();
				System.out.println("content"+content);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String groupId = msg.getGroupID();
			System.out.println("groupId"+groupId);
		}
	}

}
