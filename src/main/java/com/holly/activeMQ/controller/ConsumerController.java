package com.holly.activeMQ.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.jms.JMSException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.holly.activeMQ.bean.Message;
import com.holly.activeMQ.service.DefaultMessageConsumer;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/consumerController")
@Log4j
public class ConsumerController {

	@Resource(name="defaultMessageConsumer")
	private DefaultMessageConsumer defaultMessageConsumer;
	
	@RequestMapping(value="/reciveMessage")
	public Message reciveMessage(@RequestParam(required=true) String userCode,
								@RequestParam(required=true,defaultValue="1")String messageSendType ){
		Message message = null;
		try {
			message = defaultMessageConsumer.reciveMessage(userCode,messageSendType);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return message;
	}
}
