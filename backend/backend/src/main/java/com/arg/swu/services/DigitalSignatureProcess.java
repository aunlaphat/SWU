package com.arg.swu.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.dto.SendOrderSign;
import com.arg.swu.models.DigitalSignatureConsole;
import com.arg.swu.provider.FinancePaymentCopyProvider;
import com.arg.swu.provider.FinancePaymentProvider;
import com.arg.swu.provider.handler.SignProvider;
import com.arg.swu.repositories.DigitalSignatureConsoleRepository;
import com.arg.swu.repositories.FinancePaymentRepository;

import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class DigitalSignatureProcess implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(DigitalSignatureProcess.class);

	@Value("${default.path}")
	private String defaultPath;
	@Value("${app.config.sign.url}")
	private String signUrl;

	@Autowired
	private DigitalSignatureConsoleRepository consoleRepo;
	@Autowired
	private FinancePaymentRepository financePaymentRepo;
	@Autowired
	private FileStorageService mss;
	

	@JmsListener(destination = "signfile?prioritizedMessages=true")
	public void signfile(String message) throws Exception {

		DigitalSignatureConsole console = DigitalSignatureConsole.builder()
													.status("process")
													.message(message)
													.createdDate(new Date())
													.build();

		try
		{
			
			consoleRepo.save(console);

			String[] param = message.split(";");
			Long entityId = Long.valueOf(param[0]);
			Integer moduleTypeFile = Integer.valueOf(param[1]);

			if(entityId == null)
			{
				Assert.notNull(entityId, "entityId must not be null and must not the empty");
			}
			if(moduleTypeFile == null)
			{
				Assert.notNull(moduleTypeFile, "moduleTypeFile [1, 2, 3, 4, 5...] must not be null and must not the empty");
			}

			SignProvider pv = null;

			if (Integer.valueOf(81).equals(moduleTypeFile)) { //Type 8
				pv = FinancePaymentProvider.builder()
					.financePaymentRepo(financePaymentRepo)
					.financePaymentId(entityId)
					.fss(mss)
					.defaultPath(defaultPath)
					.build();
				pv.setModuleFile(8);				
			}
			else if (Integer.valueOf(82).equals(moduleTypeFile)) { //Type 8 for copy
				pv = FinancePaymentCopyProvider.builder()
					.financePaymentRepo(financePaymentRepo)
					.financePaymentId(entityId)
					.fss(mss)				
					.defaultPath(defaultPath)
					.build();
				pv.setModuleFile(8);				
			}
			else
			{
				Assert.notNull(moduleTypeFile, "moduleTypeFile not eq in process provoider");
			}

			/* exec data send to microservice sign */
			pv.builderData();
			 

			String url = signUrl + "/digitaisingature";

			SendOrderSign dataReq = new SendOrderSign();
			dataReq.setTennantId(1L);
			dataReq.setRawBase64Pdf(pv.srcFileBase64());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<SendOrderSign> orderEntity = new HttpEntity<SendOrderSign>(dataReq, headers);

			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<Map> resultPesponse = restTemplate.postForEntity(url, orderEntity, Map.class);
			Map<String, Object> resMap = resultPesponse.getBody();
			LOG.info("DATA RESULT:");

			pv.updateFileResult( Base64.getDecoder().decode((String) resMap.get("entries")) );

			DigitalSignatureConsole consoleSuccess = console.toBuilder().status("success").build();
			consoleRepo.save(consoleSuccess);

		} catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage(),e);
			DigitalSignatureConsole consoleError = console.toBuilder().status("success").build();
			consoleError.setStatus("Fail");
			consoleError.setMessage(console.getMessage()+ " : "+ e.getMessage());
			consoleRepo.save(consoleError);
		}
	}

	public static String convertFileToBase64(File file) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		byte[] bytes = new byte[(int) file.length()];
		fileInputStream.read(bytes);
		fileInputStream.close();

		return Base64.getEncoder().encodeToString(bytes);
	}

	public static String getFileExtension(File file) {
		String fileName = file.getName();
		int lastDotIndex = fileName.lastIndexOf('.');
		if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
			return fileName.substring(lastDotIndex + 1);
		}
		return "";
	}

	public static void convertByteArrayToFile(byte[] bytes, String filePath) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(filePath)) {
			fos.write(bytes);
		}
	}
}
