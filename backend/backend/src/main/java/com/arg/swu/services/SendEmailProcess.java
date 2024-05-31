package com.arg.swu.services;

import java.text.MessageFormat;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.arg.swu.models.NotiInfo;
import com.arg.swu.models.SendOrderMail;
import com.arg.swu.models.handler.EmailProviderInterface;
import com.arg.swu.models.provider.CourseRegisterProvider;
import com.arg.swu.models.provider.InstructorCourseRegisterProvider;
import com.arg.swu.models.provider.MemberRegisterProvider;
import com.arg.swu.models.provider.RequestApproveCourseProvider;
import com.arg.swu.models.provider.RequestApproveCoursePublicProvider;
import com.arg.swu.repositories.CoursepublicMainRepository;
import com.arg.swu.repositories.MemberCourseRepository;
import com.arg.swu.repositories.MemberInfoRepository;
import com.arg.swu.repositories.NotiInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SendEmailProcess {
	
	@Value("${app.config.mailgateway.url}")
	private String mailService;
	@Value("${app.config.env.url}")
	private String envUrl;
	@Value("${app.config.env.urladmin}")
	private String envUrlAdmin;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	 
	private final MemberInfoRepository memberRepo;
	private final NotiInfoRepository notiRepo;

	private final MemberCourseRepository courseRepo;
    private final CoursepublicMainRepository coursePulicRepo;
	

	@JmsListener(destination = "sendmail?prioritizedMessages=true")
	public void recever(String message) throws Exception {
		log.info("###### MSG #######");
		log.info(message);
		String[] convert = message.split(";");
		Long id = Long.valueOf(convert[0]);
		Long templateId = Long.valueOf(convert[1]);

		

		SendOrderMail data = SendOrderMail.builder().tennantId(1L).templateId(templateId).build();

		EmailProviderInterface pro = null;
		NotiInfo notiInfo = notiRepo.findByNotiTopicAndActiveFlag(templateId, Boolean.TRUE).orElse(null);
		if((Long.valueOf(30036001)).equals(templateId))
		{
			pro = MemberRegisterProvider.builder()
										.memberRepo(memberRepo)
										.memberId(id)
										.build();

			Map<String, Object> param = pro.generateParam();
			String subject = notiInfo.getNotiSubject();
			data.setSubject(MessageFormat.format(subject, param.get("name"), param.get("name")));
			data.setSendToMail((String) param.get("email"));
			data.setSendFromMail("casswuac@gmail.com");  
			data.setTemplateFileName(notiInfo.getTemplateFileName());

			data.setBodyParam(pro.generateParam());

			sendMail(data);
		}
		else if((Long.valueOf(30036002)).equals(templateId))
		{
			pro = MemberRegisterProvider.builder()
										.envUrl(envUrl)
										.memberRepo(memberRepo)
										.memberId(id)
										.build();

			Map<String, Object> param = pro.generateParam();
			String subject = notiInfo.getNotiSubject();
			data.setSubject(subject);
			data.setSendToMail((String) param.get("email"));
			data.setSendFromMail("casswuac@gmail.com");  
			data.setTemplateFileName(notiInfo.getTemplateFileName());

			data.setBodyParam(pro.generateParam());
			sendMail(data);
		}
		else if((Long.valueOf(30036003)).equals(templateId))
		{
			pro = CourseRegisterProvider.builder()
										.memberInfoRepo(memberRepo)
										.coursePulicRepo(coursePulicRepo)
										.courseRepo(courseRepo)
										.memberCourseId(id)
										.build();

			Map<String, Object> param = pro.generateParam();
			String subject = notiInfo.getNotiSubject();
			log.info(param.toString());
			data.setSubject(MessageFormat.format(subject, param.get(CourseRegisterProvider.COURSE_NAME)
														, param.get(CourseRegisterProvider.DATE_RESISTER)));
			
			data.setSendToMail((String) param.get(CourseRegisterProvider.EMAIL));
			data.setSendFromMail("casswuac@gmail.com");  
			data.setTemplateFileName(notiInfo.getTemplateFileName());

			data.setBodyParam(pro.generateParam());
			sendMail(data);
		}
		else if((Long.valueOf(30036004)).equals(templateId))
		{
			pro = InstructorCourseRegisterProvider.builder()										
										.jdbcTemplate(jdbcTemplate)
										.memberCourseId(id)
										.build();

										
			Map<String, Object> param = pro.generateParam();
			log.info(param.toString());
			String subject = notiInfo.getNotiSubject();
			data.setSubject(MessageFormat.format(subject, param.get(CourseRegisterProvider.COURSE_NAME), param.get(CourseRegisterProvider.COURSE_NAME)));
			data.setSendToMail((String) param.get(CourseRegisterProvider.EMAIL));
			data.setSendFromMail("casswuac@gmail.com");  
			data.setTemplateFileName(notiInfo.getTemplateFileName());

			data.setBodyParam(pro.generateParam());
			sendMail(data);
		}
		else if((Long.valueOf(30036005)).equals(templateId))
		{
			pro = CourseRegisterProvider.builder()
										.memberInfoRepo(memberRepo)
										.coursePulicRepo(coursePulicRepo)
										.courseRepo(courseRepo)
										.memberCourseId(id)
										.build();

			Map<String, Object> param = pro.generateParam();
			String subject = notiInfo.getNotiSubject();
			data.setSubject(MessageFormat.format(subject, param.get(CourseRegisterProvider.NAME), param.get(CourseRegisterProvider.NAME)));
			data.setSendToMail((String) param.get(CourseRegisterProvider.EMAIL));
			data.setSendFromMail("casswuac@gmail.com");  
			data.setTemplateFileName(notiInfo.getTemplateFileName());

			data.setBodyParam(pro.generateParam());
			sendMail(data);
		}
		else if((Long.valueOf(30036006)).equals(templateId))
		{
			pro = RequestApproveCourseProvider.builder()
										.jdbcTemplate(jdbcTemplate)
										.statusId(30010003L)
										.courseId(id)
										.envUrl(envUrlAdmin)
										.build();

			Map<String, Object> param = pro.generateParam();
			String subject = notiInfo.getNotiSubject();
			data.setSubject(MessageFormat.format(subject, param.get(RequestApproveCourseProvider.PROFESSOR)
														, param.get(RequestApproveCourseProvider.DATE_REQUIRE)
														, param.get(RequestApproveCourseProvider.PROFESSOR_EN)
														, param.get(RequestApproveCourseProvider.DATE_REQUIRE_EN)
														));
			data.setSendToMail((String) param.get(RequestApproveCourseProvider.EMAIL));
			data.setSendFromMail("casswuac@gmail.com");  
			data.setTemplateFileName(notiInfo.getTemplateFileName());

			data.setBodyParam(pro.generateParam());
			sendMail(data);

			if(param.containsKey(RequestApproveCourseProvider.STAFF_EMAIL))
			{
				data.setSubject(MessageFormat.format(subject, param.get(RequestApproveCourseProvider.STAFF_NAME)
														, param.get(RequestApproveCourseProvider.DATE_REQUIRE)
														, param.get(RequestApproveCourseProvider.STAFF_NAME_EN)
														, param.get(RequestApproveCourseProvider.DATE_REQUIRE_EN)
														));

				param.put(RequestApproveCourseProvider.NAME_REQUIRE, param.get(RequestApproveCourseProvider.STAFF_NAME));										
				data.setSendToMail((String) param.get(RequestApproveCourseProvider.STAFF_EMAIL));
				data.setBodyParam(pro.generateParam());
				sendMail(data);
			}
			
		}
		else if((Long.valueOf(30036007)).equals(templateId))
		{
			pro = RequestApproveCourseProvider.builder()
										.jdbcTemplate(jdbcTemplate)
										.statusId(30010005L)
										.courseId(id)
										.envUrl(envUrlAdmin)
										.build();

			Map<String, Object> param = pro.generateParam();
			String subject = notiInfo.getNotiSubject();
			data.setSubject(MessageFormat.format(subject, param.get(RequestApproveCourseProvider.PROFESSOR)
														, param.get(RequestApproveCourseProvider.DATE_REQUIRE)
														, param.get(RequestApproveCourseProvider.PROFESSOR_EN)
														, param.get(RequestApproveCourseProvider.DATE_REQUIRE_EN)
														));
			data.setSendToMail((String) param.get(RequestApproveCourseProvider.EMAIL));
			data.setSendFromMail("casswuac@gmail.com");  
			data.setTemplateFileName(notiInfo.getTemplateFileName());

			data.setBodyParam(pro.generateParam());
			sendMail(data);

			if(param.containsKey(RequestApproveCourseProvider.STAFF_EMAIL))
			{
				data.setSubject(MessageFormat.format(subject, param.get(RequestApproveCourseProvider.STAFF_NAME)
														, param.get(RequestApproveCourseProvider.DATE_REQUIRE)
														, param.get(RequestApproveCourseProvider.STAFF_NAME_EN)
														, param.get(RequestApproveCourseProvider.DATE_REQUIRE_EN)
														));

				param.put(RequestApproveCourseProvider.NAME_REQUIRE, param.get(RequestApproveCourseProvider.STAFF_NAME));										
				data.setSendToMail((String) param.get(RequestApproveCourseProvider.STAFF_EMAIL));
				data.setBodyParam(pro.generateParam());
				sendMail(data);
			}
			
		}
		else if((Long.valueOf(30036008)).equals(templateId))
		{
			pro = RequestApproveCoursePublicProvider.builder()
										.jdbcTemplate(jdbcTemplate)
										.coursepublicId(id)
										.statusId(30014002L)
										.envUrl(envUrlAdmin)
										.build();

			Map<String, Object> param = pro.generateParam();
			String subject = notiInfo.getNotiSubject();
			data.setSubject(MessageFormat.format(subject, param.get(RequestApproveCoursePublicProvider.PROFESSOR)
														, param.get(RequestApproveCoursePublicProvider.DATE_REQUIRE)
														, param.get(RequestApproveCoursePublicProvider.PROFESSOR_EN)
														, param.get(RequestApproveCoursePublicProvider.DATE_REQUIRE_EN)
														));
			data.setSendToMail((String) param.get(RequestApproveCoursePublicProvider.EMAIL));
			data.setSendFromMail("casswuac@gmail.com");  
			data.setTemplateFileName(notiInfo.getTemplateFileName());

			data.setBodyParam(pro.generateParam());
			sendMail(data);

			if(param.containsKey(RequestApproveCoursePublicProvider.STAFF_EMAIL))
			{
				data.setSubject(MessageFormat.format(subject, param.get(RequestApproveCoursePublicProvider.STAFF_NAME)
														, param.get(RequestApproveCoursePublicProvider.DATE_REQUIRE)
														, param.get(RequestApproveCoursePublicProvider.STAFF_NAME_EN)
														, param.get(RequestApproveCoursePublicProvider.DATE_REQUIRE_EN)
														));

				param.put(RequestApproveCoursePublicProvider.NAME_REQUIRE, param.get(RequestApproveCoursePublicProvider.STAFF_NAME));										
				data.setSendToMail((String) param.get(RequestApproveCoursePublicProvider.STAFF_EMAIL));
				data.setBodyParam(pro.generateParam());
				sendMail(data);
			}
			
		}
		else if((Long.valueOf(30036009)).equals(templateId))
		{
			pro = RequestApproveCoursePublicProvider.builder()
										.jdbcTemplate(jdbcTemplate)
										.coursepublicId(id)
										.statusId(30014003L)
										.envUrl(envUrlAdmin)
										.build();

			Map<String, Object> param = pro.generateParam();
			String subject = notiInfo.getNotiSubject();
			data.setSubject(MessageFormat.format(subject, param.get(RequestApproveCoursePublicProvider.PROFESSOR)
														, param.get(RequestApproveCoursePublicProvider.DATE_REQUIRE)
														, param.get(RequestApproveCoursePublicProvider.PROFESSOR_EN)
														, param.get(RequestApproveCoursePublicProvider.DATE_REQUIRE_EN)
														));
			data.setSendToMail((String) param.get(RequestApproveCoursePublicProvider.EMAIL));
			data.setSendFromMail("casswuac@gmail.com");  
			data.setTemplateFileName(notiInfo.getTemplateFileName());

			data.setBodyParam(pro.generateParam());
			sendMail(data);

			if(param.containsKey(RequestApproveCoursePublicProvider.STAFF_EMAIL))
			{
				data.setSubject(MessageFormat.format(subject, param.get(RequestApproveCoursePublicProvider.STAFF_NAME)
														, param.get(RequestApproveCoursePublicProvider.DATE_REQUIRE)
														, param.get(RequestApproveCoursePublicProvider.STAFF_NAME_EN)
														, param.get(RequestApproveCoursePublicProvider.DATE_REQUIRE_EN)
														));

				param.put(RequestApproveCoursePublicProvider.NAME_REQUIRE, param.get(RequestApproveCoursePublicProvider.STAFF_NAME));										
				data.setSendToMail((String) param.get(RequestApproveCoursePublicProvider.STAFF_EMAIL));
				data.setBodyParam(pro.generateParam());
				sendMail(data);
			}
			
		}

		


	}

	private void sendMail(SendOrderMail data)
	{
		String url = mailService + "/sendmail";

		log.info("URL :::::: "+ mailService+ "/sendmail");
		
		data.setTennantId(1L);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<SendOrderMail> orderEntity = 
			new HttpEntity<SendOrderMail>(data, headers);
			
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Map> resultPesponse = restTemplate.postForEntity(url, orderEntity, Map.class);
		Map<String,Object> resMap = resultPesponse.getBody();
		log.info("DATA RESULT:" + resMap);
	}
	
}
