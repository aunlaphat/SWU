package com.arg.swu.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.SysSyncData;
import com.arg.swu.models.SysSyncProgress;
import com.arg.swu.models.TempDepartment;
import com.arg.swu.repositories.MasDepartmentRepository;
import com.arg.swu.repositories.SysSyncDataRepository;
import com.arg.swu.repositories.SysSyncProgressRepository;
import com.arg.swu.repositories.TempDepartmentRepository;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleGenerateDepartment implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(ScheduleGenerateDepartment.class);
	
	private final MasDepartmentRepository masDepartmentRepository;
	private final ScheduleService scheduleService;
	private final SysSyncDataRepository sysSyncDataRepository;
	private final SysSyncProgressRepository sysSyncProgressRepository;
	private final TempDepartmentRepository tempDepartmentRepository;
	
	
	@Value("${api.department.basic.auth.domain}")
	private String baseUrl;
	
	@Value("${api.officer.basic.auth.username}")
	private String basicAuthUsername;
	
	@Value("${api.officer.basic.auth.password}")
	private String basicAuthPassword;
	
	@Value("${api.officer.basic.auth.apikey}")
	private String basicAuthApiKey;
	
	@Scheduled(cron = "0 55 0 1 * *")
	@JmsListener(destination = "dumpmasterdepartment?prioritizedMessages=true")
	public void dumpMasDepartment() {
		
		AutUser userAction = new AutUser(2l);
		Date now = new Date();
		
		// progress
		SysSyncProgress ssp = sysSyncProgressRepository.findByTableName(SyncTable.MAS_DEPARTMENT.getValue());
		if (null == ssp) {
			SysSyncProgress update = SysSyncProgress
					.builder()
					.tableName(SyncTable.MAS_DEPARTMENT.getValue())
					.progress(new BigDecimal(100).setScale(2, RoundingMode.HALF_EVEN))
					.executeRow(0l)
					.activeFlag(true)
					.createBy(userAction)
					.createDate(now)
					.build();
			sysSyncProgressRepository.save(update);
			ssp = update;
		}
		
		if (ssp.getProgress().compareTo(BigDecimal.ZERO) > 0 && ssp.getProgress().compareTo(new BigDecimal("100")) < 0) {
			return;
		}
		
		SysSyncData syncData = SysSyncData
				.builder()
				.totalRecordBeforeSync(masDepartmentRepository.count())
				.tableName(SyncTable.MAS_DEPARTMENT.getValue())
				.activeFlag(true)
				.createBy(userAction)
				.createDate(new Date())
				.build();
		
		sysSyncDataRepository.save(syncData);

		StringJoiner joiner = new StringJoiner(":");
		joiner.add(basicAuthUsername);
		joiner.add(basicAuthPassword);
		byte[] plainCredsBytes = joiner.toString().getBytes();
		String base64Creds = Base64.getEncoder().encodeToString(plainCredsBytes);
		
		StringJoiner basicAuth = new StringJoiner(" ");
		basicAuth.add("Basic");
		basicAuth.add(base64Creds);
		
		String authorization = basicAuth.toString();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", authorization);
		headers.add("apiKey", basicAuthApiKey);
		
		String requestBody = "{\n  \"psearch\": \"all\",\n  \"pvalue\": \"all\"\n}";
		
		// Set up the request entity with headers and body
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        
        // Create RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        
        // Make the POST request
        ResponseEntity<List<TempDepartment>> responseString = restTemplate.exchange(
        		baseUrl,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<TempDepartment>>() {});
        
	    if (HttpStatus.OK.equals(responseString.getStatusCode())) {
	    	List<TempDepartment> responseList = responseString.getBody();
	    	LOG.info("responseList size -> {}", responseList.size());
	    	LOG.info("responseList -> {}", responseList.get(0));

	    	if (null != responseList && !responseList.isEmpty()) {
	    		
	    		List<TempDepartment> tempDepartmentList = responseList.stream().map(o -> {
	    			TempDepartment tp = o.toBuilder().activeFlag(true).createBy(userAction).createDate(now).build();
	    			return tp;
	    		}).toList();
	    		
	    		int batchSize = 100;
	    		int totalObjects = tempDepartmentList.size();
	    		
	    		ssp = ssp.toBuilder()
    					.executeRow((long) totalObjects)
    					.updateBy(userAction)
    					.updateDate(now)
    					.build();
    			sysSyncProgressRepository.save(ssp);
    			
	    		long start = System.currentTimeMillis();
	    		
	    		BigDecimal progress = new BigDecimal(BigInteger.ZERO);
	    		double total = totalObjects;
	    		
	    		for (int i = 0; i < totalObjects; i = i + batchSize) {
	    		    if( i+ batchSize > totalObjects){
	    		        List<TempDepartment> arr = tempDepartmentList.subList(i, totalObjects - 1);
	    		        tempDepartmentRepository.saveAll(arr);
	    		        break;
	    		    }
	    		    
    		    	progress = new BigDecimal((double) (i / total * 100)).setScale(2, RoundingMode.HALF_EVEN);
	    		    LOG.info("progress -> {}", progress);
	    		    
	    		    ssp = ssp.toBuilder()
	    					.progress(progress)
	    					.updateBy(userAction)
	    					.updateDate(now)
	    					.build();
	    			sysSyncProgressRepository.save(ssp);
	    			
	    		    List<TempDepartment> arr = tempDepartmentList.subList(i, i + batchSize);
	    		    tempDepartmentRepository.saveAll(arr);
	    		}
	    		
	    		long end = (System.currentTimeMillis() - start);
	    		long second = (end / 1000);
	    		
	    		ssp = ssp.toBuilder()
    					.progress(new BigDecimal(100).setScale(2, RoundingMode.HALF_EVEN))
    					.toTemp((System.currentTimeMillis() - start) / 1000)
    					.updateBy(userAction)
    					.updateDate(now)
    					.build();
    			sysSyncProgressRepository.save(ssp);
    			
    			LOG.info("create personal temp : {} rows, second: {}", responseString.getBody().size(), second);
    			
    			scheduleService.dumpMasDepartmentFromApi();
    			
	    		ssp = ssp.toBuilder()
    					.progress(new BigDecimal(100).setScale(2, RoundingMode.HALF_EVEN))
    					.toActual((System.currentTimeMillis() - start) / 1000)
    					.updateBy(userAction)
    					.updateDate(now)
    					.build();
	    		sysSyncProgressRepository.save(ssp);
	    		
	    		SysSyncData updateSync = syncData
	    				.toBuilder()
	    				.totalRecordAfterSync(masDepartmentRepository.count())
	    				.updateBy(userAction)
	    				.updateDate(now)
	    				.build();
	    		
	    		sysSyncDataRepository.save(updateSync);
	    	}
	    }
	}
}
