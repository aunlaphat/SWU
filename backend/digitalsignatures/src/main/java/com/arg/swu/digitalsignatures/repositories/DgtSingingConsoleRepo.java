package com.arg.swu.digitalsignatures.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arg.swu.digitalsignatures.model.DgtSingingConsole;

import jakarta.transaction.Transactional;

public interface DgtSingingConsoleRepo extends JpaRepository<DgtSingingConsole, Long>
{
    @Transactional
    @Modifying
    @Query("update DgtSingingConsole c set c.status =:status, c.message = :msg where c.id =:entryId")
    public void updateStatusById(@Param("entryId") Long entryId
    , @Param("status") String status
    , @Param("msg") String msg
    );

    @Transactional
    @Modifying
    @Query("update DgtSingingConsole c set c.status =:status, c.message = :msg, c.issueTrustDate = :issueTrustDate where c.id =:entryId")
    public void updateStatusSuccessById(@Param("entryId") Long entryId
    , @Param("status") String status
    , @Param("msg") String msg
    , @Param("issueTrustDate") Date issueTrustDate
    );
}
