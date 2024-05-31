package com.arg.swu.mailgateway.model.provider;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.arg.swu.mailgateway.model.handler.OrderManager;
import com.arg.swu.mailgateway.order.RequestOrderMail;

import jakarta.mail.internet.InternetAddress;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class SimpleOrderProvider implements OrderManager {

    private MailSender mailSender;
    
    private SimpleMailMessage templateMessage;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }

    public void placeOrder(RequestOrderMail order) {

        // Do the business calculations...

        // Call the collaborators to persist the order...

        // Create a thread safe "copy" of the template message and customize it
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

        
        try{
            this.mailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            log.error(ex.getMessage());
            throw ex;
        }
    }


}
