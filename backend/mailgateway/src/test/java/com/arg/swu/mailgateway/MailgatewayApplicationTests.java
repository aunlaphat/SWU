package com.arg.swu.mailgateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.arg.swu.mailgateway.googleapi.CreateDraft;
import com.arg.swu.mailgateway.googleapi.GmailApi;
import com.arg.swu.mailgateway.model.MailTemplate;
import com.arg.swu.mailgateway.model.MailTenantProvider;
import com.arg.swu.mailgateway.order.RequestOrderMail;
import com.arg.swu.mailgateway.order.RequestRegisterTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.MessagingException;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class MailgatewayApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void contextLoads() {
	}

	// @Test
	public void infoTest()
	throws Exception {
			ResultActions result = mockMvc.perform(get("/info"));

			// Assert
			result.andDo(print()).andExpect(status().isOk());
	}

	// @Test
	public void registerTemplate() throws Exception
	{
		MailTenantProvider pv = MailTenantProvider.builder().address1("114 Sukhumvit 23, Bangkok 10110, Thailand. Tel +66 2 649 5000, Fax +66 2 258 4007 e-mail : contact@g.swu.ac.th")
									.activeFlag(true)
									.companyCode("SWU")
									.registrationDate(new Date())
									.nameEn("Srinakharinwirot University")
									.nameLo("มหาวิทยาลัยศรีนครินทรวิโรฒ")
									.typeName("EDU")
									.build();
		List<MailTemplate> tms = new ArrayList<MailTemplate>();							
		
		tms.add(MailTemplate.builder()
			.templateFileName("30036001")
			.templateTypeId(30036001L)
			.typeName("สมัครสมาชิก")
			.createdDate(new Date())
			.subject("แจ้งผลการอนุมัติเป็นผู้เรียนของคุณ {NAME}/Welcome new student, {NAME}")							
			.build());
		tms.add(MailTemplate.builder()
			.templateFileName("30036002")
			.templateTypeId(30036002L)
			.typeName("แจ้งลืมรหัสผ่าน")
			.createdDate(new Date())
			.subject("แจ้งลืมรหัสผ่านสำหรับเข้าสู่ระบบ SWU LIFELONG")							
			.build());
		tms.add(MailTemplate.builder()
			.templateFileName("30036003")
			.templateTypeId(30036003L)
			.typeName("แจ้งลืมรหัสผ่าน")
			.createdDate(new Date())
			.subject("แจ้งผลการลงทะเบียนคอร์ส {COURSE_NAME} {DATE_REGISTER}{TIME_REGISTER}/Course application result:{COURSE_NAME} {DATE_REGISTER}{TIME_REGISTER}")							
			.build());
		tms.add(MailTemplate.builder()
			.templateFileName("30036004")
			.templateTypeId(30036004L)
			.typeName("แจ้งผลเมื่อมีผู้เรียน ลงทะเบียนเรียนในรายวิชาที่สอน")
			.createdDate(new Date())
			.subject("แจ้งผลการลงทะเบียนเป็นผู้เรียนในคอร์ส {COURSE_NAME}/Course application result:{COURSE_NAME}")							
			.build());
		tms.add(MailTemplate.builder()
			.templateFileName("30036005")
			.templateTypeId(30036005L)
			.typeName("แจ้งผลการเรียน")
			.createdDate(new Date())
			.subject("แจ้งผลการเรียนของ คุณ {NAME}/Course study result of {NAME}")							
			.build());
		tms.add(MailTemplate.builder()
			.templateFileName("30036006")
			.templateTypeId(30036006L)
			.typeName("แจ้งขออนุมัติ (หลักสูตร)")
			.createdDate(new Date())
			.subject("แจ้งขออนุมัติหลักสูตร โดย {NAME_REQUIRE} {DATE_REQUIRE}{TIME_REQUIRE}/New course proposed by {NAME_REQUIRE} {DATE_REQUIRE}{TIME_REQUIRE}")							
			.build());
		tms.add(MailTemplate.builder()
			.templateFileName("30036007")
			.templateTypeId(30036007L)
			.typeName("แจ้งผลการอนุมัติ (หลักสูตร)")
			.createdDate(new Date())
			.subject("แจ้งผลการขออนุมัติหลักสูตร {COURSE_CODE}{COURSE_NAME} {DATE_APPROVE}{TIME_APPROVE}/New course approval result {COURSE_CODE}{COURSE_NAME} {DATE_APPROVE}{TIME_APPROVE}")							
			.build());
		tms.add(MailTemplate.builder()
			.templateFileName("30036008")
			.templateTypeId(30036008L)
			.typeName("แจ้งขออนุมัติ  (รอบหลักสูตร)")
			.createdDate(new Date())
			.subject("แจ้งขออนุมัติรอบหลักสูตร โดย {NAME_REQUIRE} {DATE_REQUIRE}{TIME_REQUIRE}/New course period proposed by {NAME_REQUIRE} {DATE_REQUIRE}{TIME_REQUIRE}")							
			.build());
		tms.add(MailTemplate.builder()
			.templateFileName("30036009")
			.templateTypeId(30036009L)
			.typeName("แจ้งศูนย์อนุมัติ (รอบหลักสูตร)")
			.createdDate(new Date())
			.subject("แจ้งผลการขออนุมัติรอบหลักสูตร {COURSE_CODE}{COURSE_NAME} {DATE_APPROVE}{TIME_APPROVE}/New course period approval result {COURSE_CODE}{COURSE_NAME} {DATE_APPROVE}{TIME_APPROVE}")							
			.build());
		RequestRegisterTemplate data = RequestRegisterTemplate.builder().mailTemplate(tms).mailTenantProvider(pv).build();

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders
		.post("/registertemplate")                                
		.content(asJsonString(
			data
		))
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		);

		// Assert
		result.andDo(print()).andExpect(status().isOk());

	}

	@Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

	// @Test
	void templateTest()
	{
		Map<String, Object> param = new HashMap();
		param.put("name", "niwat");
		Context thymeleafContext = new Context();
		thymeleafContext.setVariables(param);
		String repose = thymeleafTemplateEngine.process("30036001", thymeleafContext);

		log.info(repose);
	}

	// @Test
	public void sendMailTest() throws Exception
	{
		java.util.Map<String,Object> param = new HashMap();
		param.put("name", "123456789");
		param.put("surname", "123456789");

		 

		ResultActions result = mockMvc.perform(MockMvcRequestBuilders
		.post("/sendmail")                                
		.content(asJsonString(
			RequestOrderMail.builder()
			.tennantId(1L)
			.templateId(30036001L)
			.templateFileName("30036001")
			.sendFromFullName("Mr. Niwat Roongroj")
			.sendFromMail("no-reply@swu.ac.th")
			// .sendToMail("niwatr@ar.co.th")
			.sendToMail("niwatr@ar.co.th")
			.subject("Automated Message / ข้อความอัตโนมัติ")
			.bodyParam(param)
			.build()
		))
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		);

		// Assert
		result.andDo(print()).andExpect(status().isOk());
	}

	public static String asJsonString(final Object obj) {
		try {
				return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
				throw new RuntimeException(e);
		}
	}

	// @Test
	void googleapi() throws IOException, GeneralSecurityException, MessagingException
	{
		// GmailApi.main(null);
		CreateDraft.createDraftMessage("dotnano.niwat@gmail.com", "niwatr@ar.co.th");
	}
}
