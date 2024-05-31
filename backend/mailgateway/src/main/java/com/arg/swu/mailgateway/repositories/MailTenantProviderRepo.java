package com.arg.swu.mailgateway.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arg.swu.mailgateway.model.MailSenderConsole;
import com.arg.swu.mailgateway.model.MailTemplate;
import com.arg.swu.mailgateway.model.MailTenantProvider;

import jakarta.transaction.Transactional;

public interface MailTenantProviderRepo extends JpaRepository<MailTenantProvider, Long>
{
    
}
