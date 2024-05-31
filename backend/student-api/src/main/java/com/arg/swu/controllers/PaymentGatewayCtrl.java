package com.arg.swu.controllers;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.Charge3dCallbackReq;
import com.arg.swu.dto.CheckoutAdditionalData;
import com.arg.swu.dto.CheckoutData;
import com.arg.swu.dto.InquiryQrData;
import com.arg.swu.dto.KPaymentData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.CreateQrData;
import com.arg.swu.models.FinancePayment;
import com.arg.swu.models.SysKbank;
import com.arg.swu.models.SysCallKbank;
import com.arg.swu.repositories.FinancePaymentRepository;
import com.arg.swu.repositories.SysCallKbankRepository;
import com.arg.swu.repositories.SysKbankRepository;
import com.arg.swu.services.JmsSender;
import com.arg.swu.services.PaymentGatewayService;
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
@RequestMapping("payment-gateway")
@RequiredArgsConstructor
public class PaymentGatewayCtrl implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger();
	
	private final JmsSender jmsSender;
	
	private final FinancePaymentRepository financePaymentRepository;
	
	private final SysKbankRepository kbankRepository;
	private final PaymentGatewayService paymentGatewayService;
	private final SysCallKbankRepository sysCallKbankRepository;
	
	@Value("${app.client.host}")
	private String appClientHost;

	
	@GetMapping("card/{paymentId}")
	public ResponseEntity<Map<String, Object>> getCard(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "paymentId", required = true) Long paymentId) {
		try {
			
			CreateQrData data = paymentGatewayService.findCreateQrData(paymentId);
			
			if (null == data) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CreateQrData requestData = data.toBuilder().currency(CURRENCY_THB).sourceType(SOURCE_TYPE_CARD).build();

	        SysKbank key = kbankRepository.findActive();
	        
	        KPaymentData res = KPaymentData
	        		.builder()
	    			.src(key.getSrc())
	    			.apiKey(key.getPublicKey())
	    			.amount(requestData.getAmount())
	    			.currency(CURRENCY_THB)
	    			.paymentMethod(SOURCE_TYPE_CARD)
	    			.name(key.getName())
	    			.mid(key.getMid())
	    			.tid(key.getTid())
	    			.referenceOrder(requestData.getReferenceOrder())
	        		.build();
		    
			return new ResponseEntity<>(CommonUtils.response(res, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			LOG.error("==============[ getCard ]==============");
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("card/{referenceOrder}")
	public ModelAndView postCard(
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "mid", required = true) String mid,
			@RequestParam(value = "paymentMethods", required = true) String paymentMethods,
			@PathVariable(name = "referenceOrder", required = true) String referenceOrder
			) {
		try {
			
	        SysKbank key = kbankRepository.findActive();

    		FinancePayment financePayment = financePaymentRepository.findByRef1(referenceOrder);
    		
			CreateQrData data = paymentGatewayService.findCreateQrData(financePayment.getPaymentId());
    		
    		CheckoutAdditionalData additionalData = CheckoutAdditionalData
    				.builder()
    				.mid(key.getMid())
    				.tid(key.getTid())
    				.build();
    		
			CheckoutData requestData = CheckoutData
					.builder()
					.amount(data.getAmount())
					.currency(CURRENCY_THB)
					.description(data.getDescription())
					.sourceType(SOURCE_TYPE_CARD)
					.mode(TOKEN)
					.token(token)
					.referenceOrder(data.getReferenceOrder())
					.additionalData(additionalData)
					.build();

	        String requestBody = new Gson().toJson(requestData);
	        
	        SysCallKbank callKbank = SysCallKbank
	        		.builder()
	        		.url(key.getBaseUrl() + key.getUriCard())
	        		.method(HttpMethod.POST.name())
	        		.request(requestBody)
	        		.activeFlag(true)
	        		.createBy(new AutUser(2l))
	        		.createDate(new Date())
	        		.build();
	        
	        sysCallKbankRepository.save(callKbank);
			
			HttpHeaders headers = createHttpHeaders(key.getSecretKey());

	        // Set up the request entity with headers and body
	        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
	        
	        // Create RestTemplate
	        RestTemplate restTemplate = new RestTemplate();

	        // Make the POST request
	        ResponseEntity<String> responseString = restTemplate.exchange(
	        		key.getBaseUrl() + key.getUriCard(),
	                HttpMethod.POST,
	                requestEntity,
	                String.class);
		    if (HttpStatus.OK.equals(responseString.getStatusCode())) {
		    	
		    	String responseBody = responseString.getBody();
		    	SysCallKbank updateResponse = callKbank
		    			.toBuilder()
		    			.response(responseBody)
		    			.updateBy(new AutUser(2l))
		    			.updateDate(new Date())
		    			.build();

		        sysCallKbankRepository.save(updateResponse);
		        
		        Charge3dCallbackReq callbackReq = new Gson().fromJson(responseBody, Charge3dCallbackReq.class);
		        
		        if (TRANSACTION_STATE_PRE_AUTHORIZED.equals(callbackReq.getTransactionState()) && SUCCESS.equals(callbackReq.getStatus())) {
			        
			        FinancePayment updateFinancePayment = financePayment
			        		.toBuilder()
			        		.chargeId(callbackReq.getId())
			        		.updateBy(new AutUser(2l))
			        		.updateDate(new Date())
			        		.build();
			        
			        financePaymentRepository.save(updateFinancePayment);
		        }
			    return new ModelAndView(REDIRECT + callbackReq.getRedirectUrl());
		        
		    }
		    return new ModelAndView(REDIRECT + appClientHost + "/payment/fail");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			LOG.error("==============[ postCard ]==============");
		    return new ModelAndView(REDIRECT + appClientHost + "/payment/fail");
		}
	}
	
	@GetMapping("qr/{paymentId}")
	public ResponseEntity<Map<String, Object>> getQr(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "paymentId", required = true) Long paymentId) {
		try {
			
			CreateQrData data = paymentGatewayService.findCreateQrData(paymentId);
			
			if (null == data) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CreateQrData requestData = data.toBuilder().currency(CURRENCY_THB).sourceType(SOURCE_TYPE_QR).build();

	        SysKbank key = kbankRepository.findActive();

	        String requestBody = new Gson().toJson(requestData);
	        
	        SysCallKbank callKbank = SysCallKbank
	        		.builder()
	        		.url(key.getBaseUrl() + key.getUriCard())
	        		.method(HttpMethod.POST.name())
	        		.request(requestBody)
	        		.activeFlag(true)
	        		.createBy(new AutUser(2l))
	        		.createDate(new Date())
	        		.build();
	        
	        sysCallKbankRepository.save(callKbank);
			
			HttpHeaders headers = createHttpHeaders(key.getSecretKey());

	        // Set up the request entity with headers and body
	        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
	        
	        // Create RestTemplate
	        RestTemplate restTemplate = new RestTemplate();

	        // Make the POST request
	        ResponseEntity<String> responseString = restTemplate.exchange(
	        		key.getBaseUrl() + key.getUriQr(),
	                HttpMethod.POST,
	                requestEntity,
	                String.class);
		    if (HttpStatus.OK.equals(responseString.getStatusCode())) {

		    	String responseBody = responseString.getBody();
		    	SysCallKbank updateResponse = callKbank
		    			.toBuilder()
		    			.response(responseBody)
		    			.updateBy(new AutUser(2l))
		    			.updateDate(new Date())
		    			.build();

		        sysCallKbankRepository.save(updateResponse);
		        
		    	CheckoutData responseData = new Gson().fromJson(responseBody, CheckoutData.class);
		    	KPaymentData res = KPaymentData.builder()
		    			.src(key.getSrc())
		    			.apiKey(key.getPublicKey())
		    			.amount(responseData.getAmount())
		    			.currency(CURRENCY_THB)
		    			.paymentMethod(SOURCE_TYPE_QR)
		    			.name(key.getName())
		    			.mid(key.getMid())
		    			.tid(key.getTid())
		    			.orderId(responseData.getId())
		    			.referenceOrder(requestData.getReferenceOrder())
		    			.build();
				return new ResponseEntity<>(CommonUtils.response(res, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		    }
		    
			return new ResponseEntity<>(CommonUtils.response(null, MSG_DATA_NOTFOUND, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			LOG.error("==============[ getQr ]==============");
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("qr/inquiry")
	public ModelAndView postQrInquiry(HttpServletRequest request, HttpServletResponse response, @RequestBody String body) {
		
		try {
			
	        if (StringUtils.isBlank(body)) {
			    return new ModelAndView(REDIRECT + "payment/fail");
	        }
	        
	        String chargeId = body.split("=")[1];
	        
	        SysKbank key = kbankRepository.findActive();
	        
	        StringBuilder sb = new StringBuilder();
	        sb.append(key.getBaseUrl());
	        /** Inquiry */
	        sb.append("/qr/v2/qr/");
	        sb.append(chargeId);
	        
	        SysCallKbank callKbank = SysCallKbank
	        		.builder()
	        		.url(sb.toString())
	        		.method(HttpMethod.GET.name())
	        		.request(chargeId)
	        		.activeFlag(true)
	        		.createBy(new AutUser(2l))
	        		.createDate(new Date())
	        		.build();
	        
	        sysCallKbankRepository.save(callKbank);
	        
	        HttpHeaders headers = createHttpHeaders(key.getSecretKey());

	        // Set up the request entity with headers and body
	        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
	        
	        // Create RestTemplate
	        RestTemplate restTemplate = new RestTemplate();

	        // Make the POST request
	        ResponseEntity<String> responseString = restTemplate.exchange(
	        		sb.toString(),
	                HttpMethod.GET,
	                requestEntity,
	                String.class);
	        

		    if (HttpStatus.OK.equals(responseString.getStatusCode())) {
		    	String responseBody = responseString.getBody();
		    	SysCallKbank updateResponse = callKbank
		    			.toBuilder()
		    			.response(responseBody)
		    			.updateBy(new AutUser(2l))
		    			.updateDate(new Date())
		    			.build();

		        sysCallKbankRepository.save(updateResponse);
		        
		    	InquiryQrData inquiryQrData = new Gson().fromJson(responseBody, InquiryQrData.class);
		    	
		    	if (TRANSACTION_STATE_AUTHORIZED.equals(inquiryQrData.getTransactionState())) {
		    		FinancePayment financePayment = financePaymentRepository.findByRef1(inquiryQrData.getReferenceOrder());
		    		financePayment.setChargeId(inquiryQrData.getId());
		    		financePayment.setUpdateBy(new AutUser(2l));
		    		financePayment.setUpdateDate(new Date());
		    		
		    		financePaymentRepository.save(financePayment);
		        	jmsSender.sendMessage(Q_RECONCILE_PAYMENT, inquiryQrData.getId());
		    	}
		    }
		    
		    return new ModelAndView(REDIRECT + appClientHost + "/payment/callback");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			LOG.error("==============[ postQrInquiry ]==============");
		    return new ModelAndView(REDIRECT + appClientHost + "/payment/fail");
		}
	}
	
	private HttpHeaders createHttpHeaders(String secretKey) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.add("x-api-key", secretKey);
	    return headers;
	}
	
}
