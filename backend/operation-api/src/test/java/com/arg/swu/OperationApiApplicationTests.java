package com.arg.swu;

import java.util.HashMap;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.arg.swu.dto.ReportOfferedCourseData;
import com.arg.swu.report.AdminReport;
import com.arg.swu.report.JasperReportType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class OperationApiApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private MockMvc mockMvc;
	// @Test
	void contextLoads() {
	}

	// @Test
	void testreport() throws JRException, Exception{

		java.awt.image.BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo.png"));
		java.awt.image.BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
		Object a;
		AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
		.reportName("/com/arg/swu/jrxml/example.jrxml")
		.reportType(JasperReportType.PDF)
		.params(new HashMap<>())
		.build();

		adminReport.getParams().put("logo", image);
		adminReport.getParams().put("background", background);

		byte[] result = adminReport.execDatasource();

		

		System.out.println("######### RESULT ########");
		System.out.println(result);
	}

	// @Test
	void testexport() throws Exception
	{
		ReportOfferedCourseData data = new ReportOfferedCourseData();
		 
		ResultActions result = mockMvc.perform(
				MockMvcRequestBuilders
						.post("/find-offered-course-report")                                
						.content(asJsonString(data))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						
				);

		// Assert
		result
		.andDo(print())
		.andExpect(status().isOk());
	}

	public static String asJsonString(final Object obj) {
		try {
				return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
				throw new RuntimeException(e);
		}
}
}
