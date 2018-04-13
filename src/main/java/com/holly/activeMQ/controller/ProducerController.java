package com.holly.activeMQ.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.holly.activeMQ.bean.Message;
import com.holly.activeMQ.service.MessageProducer;


@RestController
@RequestMapping("/producerController")
public class ProducerController {
	
	@Autowired
	MessageProducer messageProducer;
	
	@RequestMapping(value="/sendMessage")
	public ModelAndView sendMessage(Message message){
		ModelAndView mv = new ModelAndView();
		messageProducer.sendMessage(message);
		mv.addObject(message).setViewName("/message/ok");
		return mv;
	}
}
