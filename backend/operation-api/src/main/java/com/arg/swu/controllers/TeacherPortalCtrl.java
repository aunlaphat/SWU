package com.arg.swu.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.CoursepublicGradeData;
import com.arg.swu.dto.CoursepublicMainData;
import com.arg.swu.dto.MasPersonalData;
import com.arg.swu.dto.MemberCourseData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.CoursepublicGrade;
import com.arg.swu.models.MasPersonal;
import com.arg.swu.models.SysMoodle;
import com.arg.swu.repositories.CoursepublicGradeRepository;
import com.arg.swu.repositories.MasPersonalRepository;
import com.arg.swu.repositories.SysMoodleRepository;
import com.arg.swu.services.EntityMapperService;
import com.arg.swu.services.JmsSender;
import com.arg.swu.services.TeacherPortalService;
import com.arg.swu.services.UtilityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author sitthichaim
 *
 */
@RestController
@RequestMapping("teacher-portal")
@RequiredArgsConstructor
public class TeacherPortalCtrl implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(TeacherPortalCtrl.class);
	
    private final TeacherPortalService teacherPortalService;
    private final UtilityService utilityService;
    
    private final CoursepublicGradeRepository coursepublicGradeRepository;
    private final JmsSender jmsSender;    
    
    private final MasPersonalRepository masPersonalRepository;
    private final SysMoodleRepository sysMoodleRepository;
    private final EntityMapperService mapperService;
    @Value("${app.config.moodle.url}")
    private String moodleUrl;
    
    @PostMapping("personal-data")
	public ResponseEntity<Map<String, Object>> postPersonalData(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			MasPersonal mp = masPersonalRepository.findById(userAction.getRefUserId()).orElse(null);
			
			List<MasPersonalData> data = new ArrayList<>();
			if (null != mp) {
				MasPersonalData masPersonalData = mapperService.convertToData(mp, MasPersonalData.class);
				data.add(masPersonalData);
			}
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @PostMapping("personal-data-by-id")
	public ResponseEntity<Map<String, Object>> postPersonalData(HttpServletRequest request, HttpServletResponse response,@RequestBody Long personalId) {
		try {
			MasPersonal mp = masPersonalRepository.findById(personalId).orElse(null);
			
			List<MasPersonalData> data = new ArrayList<>();
			if (null != mp) {
				MasPersonalData masPersonalData = mapperService.convertToData(mp, MasPersonalData.class);
				data.add(masPersonalData);
			}
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
          
        @PostMapping("department-data")
	public ResponseEntity<Map<String, Object>> postDepartmentData(HttpServletRequest request, HttpServletResponse response,@RequestBody Long depIdlevel1) {
		try {
			Map<String, Object> result = teacherPortalService.findDepartmentData(depIdlevel1);
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @PostMapping("active-course")
	public ResponseEntity<Map<String, Object>> postActiveCourse(HttpServletRequest request, HttpServletResponse response,@RequestBody Long personalId) {
		try {
			Map<String, Object> result = teacherPortalService.findActiveCourse(personalId);
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        
        @PostMapping("find-course-info")
	public ResponseEntity<Map<String, Object>> findCourseInfo(HttpServletRequest request, HttpServletResponse response,@RequestBody MasPersonalData courseInput) {
		try {
			Map<String, Object> result = teacherPortalService.findCourseInfo(courseInput);
			Map<String, Object> addOn = new HashMap<>();
                        addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @PostMapping("find-pass-grade")
	public ResponseEntity<Map<String, Object>> findPassGrade(HttpServletRequest request, HttpServletResponse response,@RequestBody CoursepublicMainData coursepublicGradeData) {
		try {
			Map<String, Object> result = teacherPortalService.findPassGrade(coursepublicGradeData);
			Map<String, Object> addOn = new HashMap<>();
                        addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @PostMapping("find-mas-grade-config")
	public ResponseEntity<Map<String, Object>> findMasGradeConfig(HttpServletRequest request, HttpServletResponse response,@RequestBody CoursepublicMainData coursepublicGradeData) {
		try {
			Map<String, Object> result = teacherPortalService.findMasGradeConfig(coursepublicGradeData);
			Map<String, Object> addOn = new HashMap<>();
                        addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @PutMapping("save-pass-grade")
        public ResponseEntity<Map<String, Object>> putPassGrade(HttpServletRequest request, HttpServletResponse response,@RequestBody CoursepublicGradeData coursepublicGradeData) {
		try {
                        AutUser userAction = utilityService.getActionUser(request);
			Map<String, Object> result = teacherPortalService.updatePassGrade(userAction,coursepublicGradeData);
			
                        
                        Map<String, Object> addOn = new HashMap<>();
                        addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_EDIT_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @PostMapping("save-edit-grade")
        public ResponseEntity<Map<String, Object>> postEditGrade(HttpServletRequest request, HttpServletResponse response,@RequestBody CoursepublicGradeData coursepublicGradeData) {
		try {
                        AutUser userAction = utilityService.getActionUser(request);
			Map<String, Object> result = teacherPortalService.updateEditGrade(userAction,coursepublicGradeData);
			
                        
                        Map<String, Object> addOn = new HashMap<>();
                        addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_EDIT_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @PostMapping("find-student-list")
        public ResponseEntity<Map<String, Object>> findStudentList(HttpServletRequest request, HttpServletResponse response,@RequestBody MemberCourseData memberCourseData) {
		try {
			Map<String, Object> result = teacherPortalService.findStudentList(memberCourseData);
			
                        Map<String, Object> addOn = new HashMap<>();
                        addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @PostMapping("find-study-result-list")
        public ResponseEntity<Map<String, Object>> findStudyResultList(HttpServletRequest request, HttpServletResponse response,@RequestBody MemberCourseData memberCourseData) {
		try {
			Map<String, Object> result = teacherPortalService.findStudyResultList(memberCourseData);
			
                        Map<String, Object> addOn = new HashMap<>();
                        addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
   
        @PostMapping("find-point-in-range")
        public ResponseEntity<Map<String, Object>> findPointInRange(HttpServletRequest request, HttpServletResponse response,@RequestBody CoursepublicGradeData memberCourseData) {
		try {
			Map<String, Object> result = teacherPortalService.findPointInRange(memberCourseData);
			
                        Map<String, Object> addOn = new HashMap<>();
                        addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        @PutMapping("put-member-data/{memberCourseId}")
	public ResponseEntity<Map<String, Object>> putMemberData(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "memberCourseId", required = true) Long memberCourseId,
			@RequestBody MemberCourseData memberCourseData) {
		try {
                        AutUser userAction = utilityService.getActionUser(request);
			Map<String, Object> result = teacherPortalService.putMemberData(userAction,memberCourseId,memberCourseData);
			
                        Map<String, Object> addOn = new HashMap<>();
                        addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_EDIT_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @PutMapping("put-confirm-member-data/{memberCourseId}")
	public ResponseEntity<Map<String, Object>> putConfirmMemberData(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "memberCourseId", required = true) Long memberCourseId,
			@RequestBody MemberCourseData memberCourseData) {
		try {
                        AutUser userAction = utilityService.getActionUser(request);
			Map<String, Object> result = teacherPortalService.putConfirmMemberData(userAction,memberCourseId,memberCourseData);
			
                        Map<String, Object> addOn = new HashMap<>();
                        addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));

			
        

			List<String> listIn = Arrays.asList(String.valueOf(memberCourseId), String.valueOf(EMAIL_TEMPLATE_30036005));
			jmsSender.sendMessage(Q_SENDMAIL, String.join(";", listIn));

			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_EDIT_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @PostMapping("find-member-count")
        public ResponseEntity<Map<String, Object>> findMemberCount(HttpServletRequest request, HttpServletResponse response,@RequestBody MemberCourseData memberCourseData) {
		try {
                        AutUser userAction = utilityService.getActionUser(request);
			Map<String, Object> result = teacherPortalService.findMemberCount(memberCourseData);
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @PostMapping("delete-grade")
        public ResponseEntity<Map<String, Object>> deleteGrade(HttpServletRequest request, HttpServletResponse response,@RequestBody CoursepublicGradeData coursepublicGradeData) {
		try {
                        AutUser userAction = utilityService.getActionUser(request);
			Map<String, Object> result = teacherPortalService.deleteGrade(coursepublicGradeData,userAction);
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @DeleteMapping("coursepublic-grade/{coursepublicGradeId}")
        public ResponseEntity<Map<String, Object>> deleteCoursepublicGrade(HttpServletRequest request, HttpServletResponse response,
        		@PathVariable(name = "coursepublicGradeId", required = true) Long coursepublicGradeId) {
    		try {
    			
    			CoursepublicGrade coursepublicGrade = coursepublicGradeRepository.findById(coursepublicGradeId).orElse(null);
    			if (null == coursepublicGrade) {
    				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "common.alert.dupplicate"), HttpStatus.OK);
    			}
    			coursepublicGradeRepository.deleteById(coursepublicGradeId);
    			
    			return new ResponseEntity<>(CommonUtils.response(null, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
    		} catch (Exception e) {
    			LOG.error(e.getMessage(), e);
    			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
    		}
    	}
        
        @PostMapping("find-passing-criteria")
        public ResponseEntity<Map<String, Object>> findPassingCriteria(HttpServletRequest request, HttpServletResponse response,@RequestBody CoursepublicGradeData coursepublicGradeData) {
		try {
			Map<String, Object> result = teacherPortalService.findPassingCriteria(coursepublicGradeData);
                        Map<String, Object> addOn = new HashMap<>();
                        addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			return new ResponseEntity<>(CommonUtils.response(null, MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        @PostMapping("find-point-range-criteria")
        public ResponseEntity<Map<String, Object>> findPointRangeCriteria(HttpServletRequest request, HttpServletResponse response,@RequestBody CoursepublicGradeData coursepublicGradeData) {
		try {
			Map<String, Object> result = teacherPortalService.findPointRangeCriteria(coursepublicGradeData);
                        Map<String, Object> addOn = new HashMap<>();
                        addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        @PutMapping("update-status-course")
        public ResponseEntity<Map<String, Object>> updateStatusCourse(HttpServletRequest request, HttpServletResponse response,@RequestBody CoursepublicMainData coursepublicMainData) {
		try {
                        AutUser userAction = utilityService.getActionUser(request);
			Map<String, Object> result = teacherPortalService.updateStatusCourse(userAction,coursepublicMainData);
			 
	        

			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @PostMapping("find-course-thumbnail")
        public ResponseEntity<Map<String, Object>>  findCourseThumbnail(HttpServletRequest request, HttpServletResponse response,@RequestBody CoursepublicMainData coursepublicMainData) {
                try {
			Map<String, Object> result = teacherPortalService.findCourseThumbnail(coursepublicMainData);

			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
        }
        
        @PostMapping("find-selected-grade")
        public ResponseEntity<Map<String, Object>> findSelectedGrade(HttpServletRequest request, HttpServletResponse response,@RequestBody CoursepublicGradeData coursepublicGradeData){
            try {
			Map<String, Object> result = teacherPortalService.findSelectedGrade(coursepublicGradeData.getCoursepublicId());

			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
        }
        @GetMapping("moodle-activity")
	public ResponseEntity<Map<String, Object>> moodleActivity(HttpServletRequest request, HttpServletResponse response,
		@RequestParam(value = "wstoken", required = true) String wstoken,
		@RequestParam(value = "wsfunction", required = true) String wsfunction,
		@RequestParam(value = "moodlewsrestformat", required = true) String moodlewsrestformat,
		@RequestParam(value = "userid", required = true) String userid,
		@RequestParam(value = "courseid", required = true) String courseid
	) {

		try {
			//Get Moodle token from sys_moodle
			SysMoodle sysMoodle = sysMoodleRepository.findActive();
			String moodleToken = "";
			if(sysMoodle != null) {
				moodleToken = sysMoodle.getToken();
			}
			// String url = moodleUrl +"?wstoken="+wstoken
			// 			+"&wsfunction="+wsfunction
			// 			+"&courseid="+courseid
			// 			+"&userid="+userid
			// 			+"&moodlewsrestformat="+moodlewsrestformat;
			

			// String url = moodleUrl + "/webservice/rest/server.php";
			String url = moodleUrl +"?wstoken="+ moodleToken
								+"&moodlewsrestformat="+moodlewsrestformat
								+"&wsfunction="+wsfunction
								+"&enrolments[0][userid]="+userid
								+"&enrolments[0][courseid]="+courseid;
			RestTemplate restTemplate = new RestTemplate();
			
			LOG.info("Connection to ... "+ url);
			ResponseEntity<Object> resultPesponse = restTemplate.getForEntity(url, Object.class);
			Object resMap = resultPesponse.getBody();

			LOG.info("DATA RESULT:"+ resMap);
			return new ResponseEntity<>(CommonUtils.response(resMap, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
}
