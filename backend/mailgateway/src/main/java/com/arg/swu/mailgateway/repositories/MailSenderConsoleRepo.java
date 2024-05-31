package com.arg.swu.mailgateway.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arg.swu.mailgateway.model.MailSenderConsole;

import jakarta.transaction.Transactional;

public interface MailSenderConsoleRepo extends JpaRepository<MailSenderConsole, Long>
{
    @Transactional
    @Modifying
    @Query("update MailSenderConsole c set c.status =:status, c.message = :msg where c.id =:entryId")
    public void updateStatusById(@Param("entryId") Long entryId
    , @Param("status") String status
    , @Param("msg") String msg
    );

    @Transactional
    @Modifying
    @Query("update MailSenderConsole c set c.status =:status, c.message = :msg, c.sendDate = :sendDate where c.id =:entryId")
    public void updateStatusSuccessById(@Param("entryId") Long entryId
    , @Param("status") String status
    , @Param("msg") String msg
    , @Param("sendDate") Date sendDate
    );
}
