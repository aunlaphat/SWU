package com.arg.swu.digitalsignatures.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arg.swu.digitalsignatures.model.DgtTenantProvider;

public interface DgtTenantProviderRepo extends JpaRepository<DgtTenantProvider, Long>
{

}
