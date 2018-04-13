package com.holly.activeMQ.bean;

import java.io.Serializable;
import java.net.URL;

import com.holly.activeMQ.MessageFeature;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Message implements Serializable {

	private String messageId;
	private String messageType;
	private String messageTitle;
	private String messageContent;
	private String messageSendType;// 1点对点 2 订阅消息
	private String status;// 0未消费 1 已消费
	private String fromUser;
	private String toUser;
	private URL url;//url
	private MessageFeature messageFeature;

	public Message() {
		
	};

	public Message(String messageId, String messageType, String messageTitle, String messageContent, String fromUser,
			String toUser) {
		super();
		this.messageId = messageId;
		this.messageType = messageType;
		this.messageTitle = messageTitle;
		this.messageContent = messageContent;
		this.fromUser = fromUser;
		this.toUser = toUser;
	}

	

}
