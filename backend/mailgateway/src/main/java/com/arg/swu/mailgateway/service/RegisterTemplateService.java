package com.arg.swu.mailgateway.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arg.swu.mailgateway.model.MailTemplate;
import com.arg.swu.mailgateway.model.MailTenantProvider;
import com.arg.swu.mailgateway.order.RequestOrderMail;
import com.arg.swu.mailgateway.order.RequestRegisterTemplate;
import com.arg.swu.mailgateway.repositories.MailTemplateRepo;
import com.arg.swu.mailgateway.repositories.MailTenantProviderRepo;

@Service
public class RegisterTemplateService {
    @Autowired
    private MailTemplateRepo tmRepo;
    @Autowired
    private MailTenantProviderRepo tenantRepo;

    public void register(RequestRegisterTemplate order)
    {
       MailTenantProvider tenant = order.getMailTenantProvider();
    //    List<MailTemplate> templates = order.getMailTemplate();

       tenantRepo.save(tenant);
       
    //    for(MailTemplate mt : templates)
    //    {
    //         mt.setTentant(tenant);
    //         tmRepo.save(mt);
    //    } 
       
    }
}
