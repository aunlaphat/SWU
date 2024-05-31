package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.FinancePayment;
import java.util.List;

/**
 * 
 * @author sutthiyapakc
 *
 */
public interface FinancePaymentRepository extends JpaRepository<FinancePayment, Long> {

	@Query("SELECT fp FROM FinancePayment fp WHERE fp.paymentType = :paymentType and fp.ref1 = :ref1")
	public FinancePayment findByPaymentTypeAndRef1(Long paymentType, String ref1);
	
	@Query("SELECT fp FROM FinancePayment fp WHERE fp.ref1 = :ref1")
	public FinancePayment findByRef1(String ref1);
	
	@Query("SELECT fp FROM FinancePayment fp WHERE fp.chargeId = :chargeId")
	public FinancePayment findByChargeId(String chargeId);
        
        public List<FinancePayment> findByCoursepublicId(Long coursepublicId);

	public FinancePayment findByDetailImpId(Long id);
	
	
}