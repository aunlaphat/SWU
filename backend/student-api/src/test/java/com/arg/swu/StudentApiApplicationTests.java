package com.arg.swu;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StudentApiApplicationTests {

	@Test
	void contextLoads() {
	}

	// @Test
	void testmoodle() throws Exception
	{
		String url = "/student-portal/moodle-activity?wstoken=9153b80e17c7f4f9508a2c80a05bf5aa&wsfunction=core_completion_get_activities_completion_status&courseid=2&userid=6454&moodlewsrestformat=json";

		// ResultActions result = mockMvc.perform(get(url));

		// Assert
		// result.andExpect(status().isOk());
		// RestTemplate restTemplate = new RestTemplate();
		
		// ResponseEntity<Map> resultPesponse = restTemplate.getForEntity(url, Map.class);
		// log.info(""+resultPesponse.getBody());
		// Map<String, Object> resMap = resultPesponse.getBody();
	}

}
