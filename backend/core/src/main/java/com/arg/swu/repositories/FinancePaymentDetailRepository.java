package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arg.swu.models.FinancePaymentDetail;

public interface FinancePaymentDetailRepository extends JpaRepository<FinancePaymentDetail, Long> {

}
