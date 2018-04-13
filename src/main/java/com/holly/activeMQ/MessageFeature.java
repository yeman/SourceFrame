package com.holly.activeMQ;

import java.io.InputStream;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

//消息特征类
@Getter
@Setter
public class MessageFeature {

	private MessageType messageType = MessageType.textMessage;
	private int repeat = 1;
	private long period;
	private long delay;
	private String cron;
	public MessageFeature() {
		
	}

	public MessageFeature(MessageType messageType, int repeat, long period, long delay, String cron) {
		super();
		this.messageType = messageType;
		this.repeat = repeat;
		this.period = period;
		this.delay = delay;
		this.cron = cron;
	}

}
