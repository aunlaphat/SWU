package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.SysWebhookKbank;

/**
 * 
 * @author sitthichaim
 *
 */
public interface SysWebhookKbankRepository extends JpaRepository<SysWebhookKbank, Long> {
	
	@Query("SELECT sw FROM SysWebhookKbank sw WHERE sw.chargeId = :chargeId")
	public SysWebhookKbank findActiveFlagByChargeId(String chargeId);
	
}
