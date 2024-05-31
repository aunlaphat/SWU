package com.arg.swu.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.Charge3dCallbackReq;
import com.arg.swu.dto.Charge3dResp;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.SysKbank;
import com.arg.swu.models.SysWebhookKbank;
import com.arg.swu.repositories.SysKbankRepository;
import com.arg.swu.repositories.SysWebhookKbankRepository;
import com.arg.swu.services.JmsSender;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@RestController
@RequestMapping("operationapi")
@RequiredArgsConstructor
public class WebhookCtrl implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(WebhookCtrl.class);
	
	private final JmsSender jmsSender;
	private final SysKbankRepository kbankRepository;
	private final SysWebhookKbankRepository sysWebhookKbankRepository;
	
	@PostMapping("payment/notifyqr")
	public ResponseEntity<Charge3dResp> postWebhookQr(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String stringRequestEntity) {
		
		Charge3dResp resp = new Charge3dResp();
		
		try {
			String remoteAddr = request.getRemoteAddr();
			SysWebhookKbank webhookKbank = SysWebhookKbank
					.builder()
					.route(SOURCE_TYPE_QR)
					.data(stringRequestEntity)
					.remoteAddr(remoteAddr)
					.activeFlag(true)
					.createBy(new AutUser(2l))
					.createDate(new Date())
					.build();
			
			sysWebhookKbankRepository.save(webhookKbank);
			
	        Charge3dCallbackReq req = new Gson().fromJson(stringRequestEntity, Charge3dCallbackReq.class);
	        
	        SysKbank key = kbankRepository.findActive();
	        
	        // generate check sum from charge response
	        String checkSum = generateChecksum(req, key.getSecretKey());
	        LOG.info("checkSum -> {}", checkSum);

        	String errorMsg = null;
        	
        	boolean isChecksum = true;
        	
	        if (StringUtils.isBlank(checkSum)) {
	        	isChecksum = false;
	        	errorMsg = "stringRequestEntity was wrong";
	        } else if (!checkSum.equals(req.getChecksum())) {
	        	isChecksum = false;
        		errorMsg = "Invalid - checksum calculated = " + checkSum + ", received=" + req.getChecksum();
        			
	        } else if (!TRANSACTION_STATE_AUTHORIZED.equals(req.getTransactionState()) || !SUCCESS.equals(req.getStatus())) {
        		errorMsg = "Transaction state not equals AUTHORIZED or status is not equals success";
	        }
	        
        	SysWebhookKbank update = webhookKbank
        			.toBuilder()
        			.chargeId(req.getId())
        			.checksum(isChecksum)
        			.updateBy(new AutUser(2l))
        			.updateDate(new Date())
        			.errorMsg(errorMsg)
        			.build();
			sysWebhookKbankRepository.save(update);
        	
	        if (isChecksum && StringUtils.isBlank(errorMsg)) {
	        	return ResponseEntity.ok(Charge3dResp.builder().responseCode("200").responseMSG("OK").build());
	        } else {
	        	return ResponseEntity.ok(Charge3dResp.builder().responseCode("ERROR").responseMSG(errorMsg).build());
	        }
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			LOG.error("==============[ postWebhookQr ]==============");
			resp = Charge3dResp.builder().responseCode("ERROR").responseMSG("JSON parsong error").build();
			return ResponseEntity.ok(resp);
		}
	}
	
	@PostMapping("payment/notify")
	public ResponseEntity<Charge3dResp> postWebhookCard(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String stringRequestEntity) {
		
		Charge3dResp resp = new Charge3dResp();
		
		try {
			
			String remoteAddr = request.getRemoteAddr();
			SysWebhookKbank webhookKbank = SysWebhookKbank
					.builder()
					.route(SOURCE_TYPE_CARD)
					.data(stringRequestEntity)
					.remoteAddr(remoteAddr)
					.activeFlag(true)
					.createBy(new AutUser(2l))
					.createDate(new Date())
					.build();
			
			sysWebhookKbankRepository.save(webhookKbank);
			
	        Charge3dCallbackReq req = new Gson().fromJson(stringRequestEntity, Charge3dCallbackReq.class);
	        
	        SysKbank key = kbankRepository.findActive();
	        
	        // generate check sum from charge response
	        String checkSum = generateChecksum(req, key.getSecretKey());
	        
        	String errorMsg = null;
        	
        	boolean isChecksum = true;
        	
	        if (StringUtils.isBlank(checkSum)) {
	        	isChecksum = false;
	        	errorMsg = "stringRequestEntity was wrong";
	        } else if (!checkSum.equals(req.getChecksum())) {
	        	isChecksum = false;
        		errorMsg = "Invalid - checksum calculated = " + checkSum + ", received=" + req.getChecksum();
        			
	        } else if (!TRANSACTION_STATE_AUTHORIZED.equals(req.getTransactionState()) || !SUCCESS.equals(req.getStatus())) {
        		errorMsg = "Transaction state not equals AUTHORIZED or status is not equals success";
	        }
	        
        	SysWebhookKbank update = webhookKbank
        			.toBuilder()
        			.chargeId(req.getId())
        			.checksum(isChecksum)
        			.updateBy(new AutUser(2l))
        			.updateDate(new Date())
        			.errorMsg(errorMsg)
        			.build();
			sysWebhookKbankRepository.save(update);
        	
	        if (isChecksum && StringUtils.isBlank(errorMsg)) {
	        	jmsSender.sendMessage(Q_RECONCILE_PAYMENT, req.getId());
	        	return ResponseEntity.ok(Charge3dResp.builder().responseCode("200").responseMSG("OK").build());
	        } else {
	        	return ResponseEntity.ok(Charge3dResp.builder().responseCode("ERROR").responseMSG(errorMsg).build());
	        }
	        
		} catch (Exception e) {
			LOG.error("==============[ postWebhookCard ]==============");
			LOG.error(e.getMessage(), e);
			resp = Charge3dResp.builder().responseCode("ERROR").responseMSG("JSON parsong error").build();
			return ResponseEntity.ok(resp);
		}
	}

    protected String generateChecksum(Charge3dCallbackReq charge, String secretKey) {
        String amount = Optional.ofNullable(charge.getAmount())
            .map(d -> String.format("%.4f", d)).orElse("null");
        LOG.info(amount);
        String initString = String.join("", Arrays.asList(charge.getId(),
            amount,
            charge.getCurrency(),
            charge.getStatus(),
            charge.getTransactionState(),
            StringUtils.isBlank(charge.getFailureCode()) ? "" : charge.getFailureCode(),
            secretKey));
        return CommonUtils.hashSha256(initString);
    }
    
	
}
