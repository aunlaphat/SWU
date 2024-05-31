package com.arg.swu.digitalsignatures.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arg.swu.digitalsignatures.model.DgtCertificate;

public interface DgtCertificateRepo extends JpaRepository<DgtCertificate, Long>
{
    @Query("from DgtCertificate cert where cert.tentantProvider.tenantId = :tennatId and cert.activeFlag = :active")
    DgtCertificate findByTentantProviderAndActiveFlag(@Param("tennatId") Long tennatId, @Param("active") Boolean active);
}
