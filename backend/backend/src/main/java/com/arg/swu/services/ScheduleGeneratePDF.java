package com.arg.swu.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.signedness.qual.Signed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.dto.SendOrderSign;
import com.arg.swu.dto.UploadFileData;
import com.arg.swu.models.MemberCourse;
import com.arg.swu.models.MemberInfo;
import com.arg.swu.repositories.MemberCourseRepository;
import com.arg.swu.repositories.MemberInfoRepository;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class ScheduleGeneratePDF implements ApiConstant {
 
	@Value("${default.path}")
	private String defaultPath;
	@Value("${app.config.sign.url}")
	private String signUrl;
	@Value("${app.config.env.url}")
	private String envurl;

	private final MemberCourseRepository memberCourseRepository;
	private final MemberInfoRepository memberInfoRepository;

	private final VirtualTranscriptReportService vtreport;
	private final FileStorageService filestorage;
	private final JWTService jwtService;
	
	@JmsListener(destination = "printingscheduled?prioritizedMessages=true")
	public void printingscheduled(String msg) throws Exception {
		log.info("##### MSG #####");
		log.info(msg);

		Assert.isTrue(StringUtils.isNotBlank(msg), "Printing schedule request MSG member cosurce []");

		Long memberCourseId = Long.valueOf(msg);




		MemberCourse memberCourse = memberCourseRepository.findById(memberCourseId).orElse(null);
		if (null == memberCourse) {
			log.info(SUCCESS);
			return;
		}
		List<MemberCourse> memberCourses = new ArrayList<MemberCourse>(Arrays.asList(memberCourse));

		process(memberCourses);
	}

	@Scheduled(cron = "0 0 1 * * *")
	@JmsListener(destination = "printingscheduledall?prioritizedMessages=true")
	public void printingscheduled() throws Exception {

			List<MemberCourse> memberCourses = memberCourseRepository.findByStudyStatusAndGenPdfFlag(30016002l);
			if (null == memberCourses || memberCourses.isEmpty()) {
				log.info(SUCCESS);
				return;
			}

			
			process(memberCourses);

	}

	@Transactional
	private void process(List<MemberCourse> memberCourses) throws Exception
	{
		if ( !memberCourses.isEmpty()) {
					

				// for (MemberCourse course : memberCourses) {
				for (int i = 0; i < memberCourses.size(); i++) {
					
					log.info(" GENERATE  FILE TO MEMBER ID "+ memberCourses.get(i).getMemberId());
					// MemberInfo mi = memberInfoRepository.findById(memberCourses.get(i).getMemberId()).orElse(null);
					Long memberId = memberCourses.get(i).getMemberId();
					
					String vtTokenTh = (Base64.getEncoder().encodeToString(jwtService.generateToken(
						memberId, "virtual_transcript_th").getBytes()));
					log.info("token....{}", envurl+"/virtual-transcript-verify-th?token="+vtTokenTh);
					
					byte[] fileByteVertualThArray = vtreport.generateVirtualTh(memberId, envurl+"/virtual-transcript-verify-th?token="+vtTokenTh);
					String fileNameVirtualTh = (new Date()).getTime() + "-" + memberId
							+ FilenameUtils.EXTENSION_SEPARATOR + "pdf";
					UploadFileData responseVertualTh = filestorage.saveByteArrayToStorage(fileByteVertualThArray,
							fileNameVirtualTh, 3);
					String virtualTranscriptPdfPathTh = responseVertualTh.getFullpath();
					//Signed
					// byte[] fileByteVertualArraySigned = signing(fileByteVertualArray);
					// UploadFileData responseVertualSigned = filestorage.saveByteArrayToStorage(fileByteVertualArraySigned,
					// 		fileNameVirtual, 3);

					String vtTokenEn = (Base64.getEncoder().encodeToString(jwtService.generateToken(
						memberId, "virtual_transcript_en").getBytes()));
					
					byte[] fileByteVertualEnArray = vtreport.generateVirtualEn(memberId, envurl+"/virtual-transcript-verify-en?token="+vtTokenEn);
					String fileNameVirtualEn = (new Date()).getTime() + "-" + memberId
							+ FilenameUtils.EXTENSION_SEPARATOR + "pdf";
					UploadFileData responseVertualEn = filestorage.saveByteArrayToStorage(fileByteVertualEnArray,
							fileNameVirtualEn, 3);
					String virtualTranscriptPdfPathEn = responseVertualEn.getFullpath();
					//Signed
					// byte[] fileByteVertualArraySigned = signing(fileByteVertualArray);
					// UploadFileData responseVertualSigned = filestorage.saveByteArrayToStorage(fileByteVertualArraySigned,
					// 		fileNameVirtual, 3);

					String lnTokenTh = (Base64.getEncoder().encodeToString(jwtService.generateToken(
						memberId, "learning_th").getBytes()));
					
					byte[] fileLearningByteArrayTh = vtreport.generateLearningTh(memberId, envurl+"/learning-outcome-verify-th?token="+lnTokenTh);
					String fileNameLearningTh = (new Date()).getTime() + "-" + memberId
							+ FilenameUtils.EXTENSION_SEPARATOR + "pdf";
					UploadFileData responseLearningTh = filestorage.saveByteArrayToStorage(fileLearningByteArrayTh,
							fileNameLearningTh, 3);
					String learningOutcomesPdfPathTh = responseLearningTh.getFullpath();
					//Signed
					// byte[] fileLearningByteArraySigned = signing(fileLearningByteArray);
					// UploadFileData responseLearningSigned = filestorage.saveByteArrayToStorage(fileLearningByteArraySigned,
					// 		fileNameVirtual, 3);
					
					String lnTokenEn = (Base64.getEncoder().encodeToString(jwtService.generateToken(
							memberId, "learning_th").getBytes()));
						
						byte[] fileLearningByteArrayEn = vtreport.generateLearningEn(memberId, envurl+"/learning-outcome-verify-en?token="+lnTokenEn);
						String fileNameLearningEn = (new Date()).getTime() + "-" + memberId
								+ FilenameUtils.EXTENSION_SEPARATOR + "pdf";
						UploadFileData responseLearningEn = filestorage.saveByteArrayToStorage(fileLearningByteArrayEn,
								fileNameLearningEn, 3);
						String learningOutcomesPdfPathEn = responseLearningEn.getFullpath();
						//Signed
						// byte[] fileLearningByteArraySigned = signing(fileLearningByteArray);
						// UploadFileData responseLearningSigned = filestorage.saveByteArrayToStorage(fileLearningByteArraySigned,
						// 		fileNameVirtual, 3);

					String expTokenTh = (Base64.getEncoder().encodeToString(jwtService.generateToken(
						memberId, "experience_th").getBytes()));
					byte[] fileExperienceByteArrayTh = vtreport.generateExperienceTh(memberId, envurl+"/experience-transcript-verify-th?token="+expTokenTh);
					String fileNameExperienceTh = (new Date()).getTime() + "-" + memberId
							+ FilenameUtils.EXTENSION_SEPARATOR + "pdf";
					UploadFileData responseExperienceTh = filestorage.saveByteArrayToStorage(fileExperienceByteArrayTh,
							fileNameExperienceTh, 3);
					String experienceTranscriptPdfPathTh = responseExperienceTh.getFullpath();
					// Signed
					// byte[] fileExperienceByteArraySigned = signing(fileExperienceByteArray);
					// UploadFileData responseExperienceSigned = filestorage.saveByteArrayToStorage(fileExperienceByteArraySigned,
					// 		fileNameVirtual, 3);
					
					String expTokenEn = (Base64.getEncoder().encodeToString(jwtService.generateToken(
							memberId, "experience_en").getBytes()));
						byte[] fileExperienceByteArrayEn = vtreport.generateExperienceEn(memberId, envurl+"/experience-transcript-verify-en?token="+expTokenEn);
						String fileNameExperienceEn = (new Date()).getTime() + "-" + memberId
								+ FilenameUtils.EXTENSION_SEPARATOR + "pdf";
						UploadFileData responseExperienceEn = filestorage.saveByteArrayToStorage(fileExperienceByteArrayEn,
								fileNameExperienceEn, 3);
						String experienceTranscriptPdfPathEn = responseExperienceEn.getFullpath();
						// Signed
						// byte[] fileExperienceByteArraySigned = signing(fileExperienceByteArray);
						// UploadFileData responseExperienceSigned = filestorage.saveByteArrayToStorage(fileExperienceByteArraySigned,
						// 		fileNameVirtual, 3);
					
							// mi.setVirtualTranscriptPdfPath(virtualTranscriptPdfPath);
							// mi.setLearningOutcomesPdfPath(learningOutcomesPdfPath);
							// mi.setExperienceTranscriptPdfPath(experienceTranscriptPdfPath);
							

							// memberInfoRepository.save(mi);
							memberInfoRepository.updateFileById(
									memberCourses.get(i).getMemberId(),
									virtualTranscriptPdfPathTh, 
									virtualTranscriptPdfPathEn, 
									learningOutcomesPdfPathTh, 
									learningOutcomesPdfPathEn, 
									experienceTranscriptPdfPathTh,
									experienceTranscriptPdfPathEn,
									vtTokenTh,
									lnTokenEn,
									expTokenTh,
									vtTokenEn,
									lnTokenTh,
									expTokenEn
							);
							
								if (memberCourses.get(i).getPassStatus() && memberCourses.get(i).getPassStatus() != null) {
									
									String certEnToken = Base64.getEncoder().encodeToString(jwtService.generateToken(memberId, "certificate_en").getBytes());
									String certThToken = Base64.getEncoder().encodeToString(jwtService.generateToken(memberId, "certificate_th").getBytes());

								    
									byte[] fileCertificateThByteArray = vtreport.generateCertTh(memberCourses.get(i).getMemberCourseId(), envurl+"/certificate-verify-th?token="+certThToken);
									String fileNameCertificateTh = (new Date()).getTime() + "-" + memberCourses.get(i).getMemberCourseId()
											+ FilenameUtils.EXTENSION_SEPARATOR + "pdf";
									// @Signed
									byte[] fileCertificateThByteArraySigned = signing(fileCertificateThByteArray);
									UploadFileData responseCertificateThSigned = filestorage.saveByteArrayToStorage(fileCertificateThByteArraySigned,
											fileNameCertificateTh, 3);
									String certThGentPdfPath = responseCertificateThSigned.getFullpath();
									 
									byte[] fileCertificateEnByteArray = vtreport.generateCertEn(memberCourses.get(i).getMemberCourseId(), envurl+"/certificate-verify-en?token="+certEnToken);
									String fileNameCertificateEn = (new Date()).getTime() + "-" + memberCourses.get(i).getMemberCourseId()
											+ FilenameUtils.EXTENSION_SEPARATOR + "pdf";
									//  @Signed
									byte[] fileCertificateEnByteArraySigned = signing(fileCertificateEnByteArray);
									UploadFileData responseCertificateEnSigned = filestorage.saveByteArrayToStorage(fileCertificateEnByteArraySigned,
											fileNameCertificateEn, 3);
									String certEnGentPdfPath = responseCertificateEnSigned.getFullpath();
									
									MemberCourse course = memberCourses.get(i)
											.toBuilder()
											.genPdfFlag(true)
											.genPdfDate(new Date())
											.build();
											
									course.setCertificateNonCaPathTh(fileNameCertificateTh);
									course.setCertificateCaPathTh(certThGentPdfPath);
									course.setCertificateNonCaPathEn(fileNameCertificateEn);
									course.setCertificateCaPathEn(certEnGentPdfPath);
									course.setCertificateVerifyEn(certEnToken);
									course.setCertificateVerifyTh(certThToken);
									
									
									//Update 
									memberCourseRepository.save(course);
									memberCourseRepository.flush();
								} else {
									MemberCourse course = memberCourses.get(i)
											.toBuilder()
											.genPdfFlag(true)
											.genPdfDate(new Date())
											.build();
									
									memberCourseRepository.save(course);
									memberCourseRepository.flush();
								}
							
							
//							log.info(">>>>>>> virtualTranscriptPdfPathTh ID " + virtualTranscriptPdfPathTh
//									+ memberCourses.get(i).getMemberId());
//							log.info(">>>>>>> virtualTranscriptPdfPathEn ID " + virtualTranscriptPdfPathEn
//									+ memberCourses.get(i).getMemberId());
//							log.info(">>>>>>> learningOutcomesPdfPathTh ID " + learningOutcomesPdfPathTh
//									+ memberCourses.get(i).getMemberId());
//							log.info(">>>>>>> learningOutcomesPdfPathEn ID " + learningOutcomesPdfPathEn
//									+ memberCourses.get(i).getMemberId());
//							log.info(">>>>>>> experienceTranscriptPdfPathTh ID " + experienceTranscriptPdfPathTh
//									+ memberCourses.get(i).getMemberId());
//							log.info(">>>>>>> experienceTranscriptPdfPathEn ID " + experienceTranscriptPdfPathEn
//									+ memberCourses.get(i).getMemberId());

				}
			
		}
	}
 

	byte [] signing(byte [] rawDataBase64)
	{
		String url = signUrl + "/digitaisingature";

		SendOrderSign dataReq = new SendOrderSign();
		dataReq.setTennantId(1L);
		dataReq.setRawBase64Pdf(Base64.getEncoder().encodeToString(rawDataBase64));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<SendOrderSign> orderEntity = new HttpEntity<SendOrderSign>(dataReq, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Map> resultPesponse = restTemplate.postForEntity(url, orderEntity, Map.class);
		Map<String, Object> resMap = resultPesponse.getBody();
		log.info("DATA RESULT:");

		return Base64.getDecoder().decode((String) resMap.get("entries"));
	}

}
