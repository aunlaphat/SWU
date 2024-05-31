package com.arg.swu.mailgateway.controller;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.arg.swu.mailgateway.order.RequestOrderMail;
import com.arg.swu.mailgateway.order.RequestRegisterTemplate;
import com.arg.swu.mailgateway.service.MailService;
import com.arg.swu.mailgateway.service.RegisterTemplateService;
import com.arg.swu.mailgateway.util.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class MailGatewayController {
    @Value("${build.version}")
    private String buildVersion;
    @Value("${build.timestamp}")
    private String buildTimestamp;

    @Autowired
    private MailService mailService;

	@Autowired
	private RegisterTemplateService tmService;

    @GetMapping("info")
	public ResponseEntity<Map<String, Object>> info()
	{
		log.info("##### INFO #####");
		return new ResponseEntity<>(CommonUtils.response(MessageFormat.format("Mail Gateway version {0} {1}", buildVersion, buildTimestamp), "Success", null), HttpStatus.OK);
	}

	@PostMapping("registertemplate")
	public ResponseEntity<Map<String, Object>> registertemplate(HttpServletRequest request, HttpServletResponse response,
			@RequestBody RequestRegisterTemplate data) {
		try {
            log.info("######### Register template MAIL #######");
            
			tmService.register(data);

			return new ResponseEntity<>(CommonUtils.response(null, "Success", null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	} 

    @PostMapping("sendmail")
	public ResponseEntity<Map<String, Object>> sendmail(HttpServletRequest request, HttpServletResponse response,
			@RequestBody RequestOrderMail data) {
		try {
            log.info("######### SEND MAIL #######");
            log.info(MessageFormat.format("### Order mail {0}", data.getTennantId()));
            log.info(MessageFormat.format("### mail to {0}", data.getSendToMail()));
            log.info(MessageFormat.format("### mail from {0}", data.getSendFromMail()));
            log.info(MessageFormat.format("### mail fullname {0}", data.getSendFromFullName()));
            
			/**/
			Map<String , Object> param = new HashMap();
			
			mailService.sendMailWithtemplate(data, param);

			return new ResponseEntity<>(CommonUtils.response(null, "Success", null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	} 
}
