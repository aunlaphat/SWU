package com.arg.swu.digitalsignatures.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arg.swu.digitalsignatures.model.DgtSigner;

public interface DgtSignerRepo extends JpaRepository<DgtSigner, Long>
{
    @Query("select sign from DgtSigner sign where tentantProvider.tenantId = :tennatId and active = :active")
    DgtSigner findByTentantProviderAndActive(@Param("tennatId") Long tennatId, @Param("active") Boolean active);
}
