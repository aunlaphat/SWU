package com.arg.swu.mailgateway.repositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arg.swu.mailgateway.model.MailSenderConsole;
import com.arg.swu.mailgateway.model.MailTemplate;
import jakarta.transaction.Transactional;

public interface MailTemplateRepo extends JpaRepository<MailTemplate, Long>
{
    Optional<MailTemplate> findByTemplateTypeId(Long templateId);
}
