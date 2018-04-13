package com.holly.activeMQ;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.MessageId;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.holly.activeMQ.bean.Message;
import com.holly.util.UUIDGenerator;

import lombok.extern.log4j.Log4j;

@Log4j
public class BulidBaseMessage {
	private Session session;
	
	private BulidBaseMessage(){}
	
	public BulidBaseMessage(Session session){
		this.session = session;
	}
	
	//发送消息
	public org.apache.activemq.Message bulidMessage(Message message) {
		Assert.notNull(session, "BulidBaseMessage property session must not null !");
		Assert.notNull(message, "message must not null !");
		try {
			MessageFeature messageFeature = message.getMessageFeature();
			org.apache.activemq.Message activeMQMessage = null;
			
			if (messageFeature.getMessageType().equals(MessageType.textMessage)) {
				activeMQMessage = (ActiveMQMessage) session.createTextMessage(message.getMessageContent());
			} else if (messageFeature.getMessageType().equals(MessageType.objectMessage)) {
				activeMQMessage  = (ActiveMQObjectMessage) session.createObjectMessage();
			} else if (messageFeature.getMessageType().equals(MessageType.blobMessage)) {
				activeMQMessage = ((ActiveMQSession) session).createBlobMessage(message.getUrl());
			}
			// 设置属性
			if (messageFeature.getDelay() != 0) {
				activeMQMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, messageFeature.getDelay());
			}
			if (messageFeature.getPeriod() != 0) {
				activeMQMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, messageFeature.getPeriod());
			}
			if (messageFeature.getRepeat() != 0) {
				activeMQMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, messageFeature.getRepeat());
			}
			if (StringUtils.isNotEmpty(messageFeature.getCron())) {
				activeMQMessage.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, messageFeature.getCron());
			}
			//设置持久化
			activeMQMessage.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
			//此处如果session 有事物id 会自动提交 不需要手动提交
			session.commit();
			return activeMQMessage;
		} catch (JMSException e) {
			e.printStackTrace();
		}finally{
			
		}
		return null;

	}

}
