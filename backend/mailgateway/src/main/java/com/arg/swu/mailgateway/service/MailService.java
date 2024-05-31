package com.arg.swu.mailgateway.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.arg.swu.mailgateway.model.MailSenderConsole;
import com.arg.swu.mailgateway.model.MailTemplate;
import com.arg.swu.mailgateway.model.MailTenantProvider;
import com.arg.swu.mailgateway.model.provider.SimpleOrderProvider;
import com.arg.swu.mailgateway.order.RequestOrderMail;
import com.arg.swu.mailgateway.repositories.MailSenderConsoleRepo;
import com.arg.swu.mailgateway.repositories.MailTemplateRepo;
import com.arg.swu.mailgateway.repositories.MailTenantProviderRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class MailService {

    @Autowired
    private MailTenantProviderRepo repo;
    @Autowired
    private MailTemplateRepo tmRepo;
    @Autowired
    private MailSenderConsoleRepo consoleRepo;
    @Autowired
    private MailSender senderSystem;
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;
    
    public void sendMail(RequestOrderMail order)
    {
        MailTenantProvider tenant = repo.findById(order.getTennantId()).orElse(null);

        if(tenant != null)
        log.info(" Tenant ID "+ tenant.getTenantId());

        SimpleMailMessage msg = new SimpleMailMessage();
        
        msg.setFrom(order.getSendFromMail());
        msg.setSubject(order.getSubject());
        msg.setTo(order.getSendToMail());
        msg.setText(
            "Dear " + order.getDearTopic()
                + ", thank you for placing order. Your order number is ");
                // + order.getBodyParam().get("param1"));
        log.info("### msg " + msg);
        
        Map model = new HashMap();
                // model.put("user", user);
        
        

        SimpleOrderProvider mail = SimpleOrderProvider.builder().mailSender(senderSystem).templateMessage(msg).build();
        mail.placeOrder(order);
    }

    public void sendMailWithtemplate(RequestOrderMail order, Map<String, Object> templateModel) throws MessagingException
    {
        //update status
        MailSenderConsole console = MailSenderConsole.builder()
                                        .tentant(MailTenantProvider.builder().tenantId(order.getTennantId()).build())
                                        .createdDate(new Date())
                                        .status("process")
                                        .message("Order send email by").build();

        try {
            

            consoleRepo.save(console);
 
        
            //Add param
            Context thymeleafContext = new Context();
            thymeleafContext.setVariables(order.getBodyParam());
            log.info("##### PARAM ####");
            log.info(order.getBodyParam().toString());
            //Load template
            String htmlBody = thymeleafTemplateEngine.process(order.getTemplateFileName(), thymeleafContext);

            //Send mail
            sendHtmlMessage(order.getSendToMail(), order.getSubject(), htmlBody);

            //update status
            console.setStatus("success");
            console.setMessage("Send mail success.");
            console.setSendDate(new Date());
            consoleRepo.save(console);
        } catch (MessagingException e) {
            //update status
            console.setStatus("fail");
            console.setMessage(e.getMessage());
            consoleRepo.save(console);

            log.error(e.getMessage(), e);
            throw e;
        } 
        
    }
    
    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        emailSender.send(message);
    }

    


}
