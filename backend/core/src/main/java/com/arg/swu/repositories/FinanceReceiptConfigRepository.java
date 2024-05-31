package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.FinanceReceiptConfig;

/**
 * 
 * @author sitthichaim
 *
 */
public interface FinanceReceiptConfigRepository extends JpaRepository<FinanceReceiptConfig, Long> {

	@Query("select frc from FinanceReceiptConfig frc WHERE frc.activeFlag = true")
	public FinanceReceiptConfig findActive();
	
}
