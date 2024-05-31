package com.arg.swu.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class JmsSender {
	
	private final Logger LOG = LogManager.getLogger(JmsSender.class);

    private final JmsTemplate jmsTemplate;
    
    public void sendMessage(String topic, Object message){
        try{
            LOG.info("Attempting Send message to Topic: {}", topic);
            jmsTemplate.convertAndSend(topic, message);
        } catch(Exception e){
        	LOG.error("Recieved Exception during send Message: ", e);
        }
    }
    
}
