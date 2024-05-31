package com.arg.swu.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.models.CreateQrData;
import com.arg.swu.models.FinancePayment;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class PaymentGatewayService implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(PaymentGatewayService.class);
	
	private final JdbcTemplate jdbcTemplate;
	
	public CreateQrData findCreateQrData(Long paymentId) {
		return jdbcTemplate.queryForObject("""
				select fp.payment_amount amount, fp.ref1 reference_order, cm.public_name_en description
				from finance_payment fp 
				left join coursepublic_main cm on fp.coursepublic_id = cm.coursepublic_id 
				where fp.payment_id = ?
				""", new BeanPropertyRowMapper<CreateQrData>(CreateQrData.class), paymentId);
	}
	
}
