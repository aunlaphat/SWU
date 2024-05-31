package com.arg.swu.configs;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author sitthichaim
 *
 */
@Configuration
public class JmsConfig {

    @Value("${activemq.broker.url}")
    private String brokerUrl;
    
    @Value("${activemq.username}")
    private String username;
    
    @Value("${activemq.password}")
    private String password;
	
    @Bean
	public ActiveMQConnectionFactory connectionFactory(){
	    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
	    // connectionFactory.setTrustedPackages(Arrays.asList("com.arg.swu.services"));
	    connectionFactory.setBrokerURL(brokerUrl);
	    connectionFactory.setPassword(username);
	    connectionFactory.setUserName(password);
	    return connectionFactory;
	}

	@Bean
	public JmsTemplate jmsTemplate(){
	    JmsTemplate template = new JmsTemplate();
	    template.setConnectionFactory(connectionFactory());
	    return template;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
	    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
	    factory.setConnectionFactory(connectionFactory());
	    factory.setConcurrency("2-3");
	    return factory;
	}
    
}