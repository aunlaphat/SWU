package com.arg.swu.digitalsignatures.controller;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.digitalsignatures.model.DgtCertificate;
import com.arg.swu.digitalsignatures.model.DgtSingingConsole;
import com.arg.swu.digitalsignatures.model.DgtTenantProvider;
import com.arg.swu.digitalsignatures.model.handler.DgtCertificationHandler;
import com.arg.swu.digitalsignatures.model.handler.DgtStatusFailHandler;
import com.arg.swu.digitalsignatures.model.handler.DgtStatusProcessHandler;
import com.arg.swu.digitalsignatures.model.handler.DgtStatusSuccessHandler;
import com.arg.swu.digitalsignatures.model.receive.DgtKeyStoreUnwrap;
import com.arg.swu.digitalsignatures.model.receive.RequestOrderSign;
import com.arg.swu.digitalsignatures.repositories.DgtCertificateRepo;
import com.arg.swu.digitalsignatures.repositories.DgtSignerRepo;
import com.arg.swu.digitalsignatures.repositories.DgtSingingConsoleRepo;
import com.arg.swu.digitalsignatures.service.DigitalsignatureProcess;
import com.arg.swu.digitalsignatures.util.CommonUtils;
import com.arg.swu.digitalsignatures.util.KeystoreUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class DigitalsignatureController {

	@Autowired
	DgtSignerRepo signRepo;
	@Autowired
	DgtCertificateRepo certRepo;
	@Autowired
	DgtSingingConsoleRepo consoleRepo;

	@GetMapping("info")
	public ResponseEntity<Map<String, Object>> info()
	{
		log.info("##### INFO #####");
		return new ResponseEntity<>(CommonUtils.response("Niwwat Roongroj", "Success", null), HttpStatus.OK);
	}

    @PostMapping("generatejks")
	public ResponseEntity<Map<String, Object>> postCourseActivity(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DgtTenantProvider data) {
		try {


            //FILE jks
            KeystoreUtils b = KeystoreUtils.builder().build();
            b.loadFromResource("/ssl/swu/keystore/swu.ac.co.th-only.jks");
            b.generateToBase64();
			
			return new ResponseEntity<>(CommonUtils.response(data, "Success", null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	} 

    @PostMapping("digitaisingature")
	public ResponseEntity<Map<String, Object>> signDigitalsignature(HttpServletRequest request, HttpServletResponse response,
			@RequestBody RequestOrderSign data) {
		try {
			log.info("##### DATA RECEIVE ###### ");
			log.info(String.valueOf(data.getTennantId()));
			
			
			DigitalsignatureProcess<DgtSingingConsole> sign = new DigitalsignatureProcess<DgtSingingConsole>();

			BouncyCastleProvider provider = new BouncyCastleProvider();
                Security.addProvider(provider);
			//Provider name
			sign.setProviderName(provider.getName());

			sign.setDgtCertHandler(new DgtCertificationHandler() {
				@Override
				public DgtKeyStoreUnwrap jks() throws Exception {
					log.info("########## CERTIFICATION Digit ##########"+ data.getTennantId());
					DgtCertificate cert = certRepo.findByTentantProviderAndActiveFlag(data.getTennantId(), Boolean.TRUE);
					

					if(cert == null)
						Assert.notNull(cert , "Digital Signature by tentant not found.");

					byte [] rawData = KeystoreUtils.builder()
											.build().generateBase64ToByteArray(cert.getJksData());
					InputStream jksLoad = new ByteArrayInputStream(rawData);

					BouncyCastleProvider provider = new BouncyCastleProvider();
					Security.addProvider(provider);
					KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
					
					byte [] storePass = KeystoreUtils.builder().build().generateBase64ToByteArray(cert.getJksStorePass());
					byte [] pkPass = KeystoreUtils.builder().build().generateBase64ToByteArray(cert.getJksPrivateKeyPass());
					 
					// log.info("Store Pass enc "+ cert.getJksStorePass());
					
					String storePassStr = (new String(storePass));
					String pkPassStr = (new String(pkPass));

					ks.load(jksLoad, storePassStr.trim().toCharArray());

					String alias = ks.aliases().nextElement();
					log.info("Step#### 1");
					return DgtKeyStoreUnwrap.builder()
												.pin(storePassStr.trim().toCharArray())
												.keyStore(ks)
												.privateKey((PrivateKey) ks.getKey(alias, pkPassStr.trim().toCharArray()))
												.chain(ks.getCertificateChain(alias))
												.build();
					 
					 
				}
				
			});
			
			sign.setDgtStatusProcessHandler(new DgtStatusProcessHandler() {
				@Override
				public Long onStatus(String status, String message) {
					DgtSingingConsole console = DgtSingingConsole.builder()
													.tentant(DgtTenantProvider.builder().tenantId(data.getTennantId()).build())
													.message(message)
													.status(status)
													.build();

					consoleRepo.save(console);
					return console.getId();
				}
			});

			sign.setDgtStatusFailHandler(new DgtStatusFailHandler() {
				@Override
				public void onStatus(Long byId, String status, String message) {
					consoleRepo.updateStatusById(byId, status, message);
				}
			});

			sign.setDgtStatusSuccessHandler(new DgtStatusSuccessHandler() {
				@Override
				public void onStatus(Long byId, String status, String message) {
					consoleRepo.updateStatusSuccessById(byId, status, message, new Date());
				}
			});


			sign.setFilePdfSrc(Base64.getDecoder().decode(data.getRawBase64Pdf()));
			String dataSigned = sign.sign(data).toBase64();

			
			return new ResponseEntity<>(CommonUtils.response(dataSigned, "Success", null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	} 
}
