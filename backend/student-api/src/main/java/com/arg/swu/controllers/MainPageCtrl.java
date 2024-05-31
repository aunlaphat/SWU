package com.arg.swu.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.CourseMainData;
import com.arg.swu.dto.CoursepublicInstructorData;
import com.arg.swu.dto.CoursepublicMainData;
import com.arg.swu.dto.CoursepublicMediaData;
import com.arg.swu.dto.MasCourseTypeData;
import com.arg.swu.dto.MasGeneralSkillData;
import com.arg.swu.dto.NewsInfoAttachData;
import com.arg.swu.dto.NewsInfoData;
import com.arg.swu.models.CoursepublicMain;
import com.arg.swu.models.NewsInfo;
import com.arg.swu.models.NewsInfoAttach;
import com.arg.swu.repositories.CoursepublicMainRepository;
import com.arg.swu.repositories.NewsInfoAttachRepository;
import com.arg.swu.repositories.NewsInfoRepository;
import com.arg.swu.services.CourseManagementService;
import com.arg.swu.services.EntityMapperService;
import com.arg.swu.services.MasterService;
import com.arg.swu.services.NewsManagementService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@RestController
@RequestMapping("mainpage")
@RequiredArgsConstructor
public class MainPageCtrl implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(MainPageCtrl.class);
	
	private final EntityMapperService mapperService;
	private final MasterService masterService;
	
	private final CourseManagementService courseManagementService;
	private final NewsManagementService newsManagementService;

	private final CoursepublicMainRepository coursepublicMainRepository;
	private final NewsInfoRepository newsInfoRepository;
	private final NewsInfoAttachRepository newsInfoAttachRepository;
	
	@PostMapping("find-coursepublic-main")
	public ResponseEntity<Map<String, Object>> findCoursepublicMain(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CoursepublicMainData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCoursepublicMain(data, null);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));

			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_CREATE_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("coursepublic-main/{coursepublicId}")
	public ResponseEntity<Map<String, Object>> getCoursepublicMain(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicId", required = true) Long coursepublicId) {
		try {
			
			CoursepublicMain coursepublicMain = coursepublicMainRepository.findById(coursepublicId).orElse(null);
			if (null == coursepublicMain) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			CoursepublicMainData data = mapperService.convertToData(coursepublicMain, CoursepublicMainData.class);
			
			String courseCode = coursepublicMainRepository.getCourseCode(coursepublicId);
			data.setCourseCode(courseCode);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
    @PostMapping("find-coursepublic-instructor")
	public ResponseEntity<Map<String, Object>> findCoursepublicInstructor(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CoursepublicInstructorData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCoursepublicInstructor(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));

			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_CREATE_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("find-course-type")
	public ResponseEntity<Map<String, Object>> findCourseType(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasCourseTypeData data) {
		try {
			
			Map<String, Object> result = masterService.findCourseTypeCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-coursepublic-media")
	public ResponseEntity<Map<String, Object>> findCoursepublicMedia(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CoursepublicMediaData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCoursepublicMedia(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));

			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_CREATE_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

    @PostMapping("find-general-skill")
	public ResponseEntity<Map<String, Object>> findGeneralSkill(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasGeneralSkillData data) {
		try {
			
			Map<String, Object> result = masterService.findGeneralSkillByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

    @PostMapping("find-course-main")
	public ResponseEntity<Map<String, Object>> findCourseMain(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseMainData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCourseMain(data, null);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

    @PostMapping("find-news-info")
	public ResponseEntity<Map<String, Object>> findNewsInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody NewsInfoData data) {
		try {
			
			Map<String, Object> result = newsManagementService.findNewsData(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
    
    @GetMapping("news-info/{newsId}")
	public ResponseEntity<Map<String, Object>> getNewsInfo(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "newsId", required = true) Long newsId) {
		try {
			
			NewsInfo newsInfo = newsInfoRepository.findById(newsId).orElse(null);
			if (null == newsInfo) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}

			NewsInfoData data = mapperService.convertToData(newsInfo, NewsInfoData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
    
    @GetMapping("news-info-attach/news-info/{newsId}")
	public ResponseEntity<Map<String, Object>> getNewsInfoAttachNewsInfo(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "newsId", required = true) Long newsId) {
		try {
			
			List<NewsInfoAttach> list = newsInfoAttachRepository.findByNewsId(newsId);
			if (null == list || list.isEmpty()) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}

			List<NewsInfoAttachData> data = list.stream().map(o -> mapperService.convertToData(o, NewsInfoAttachData.class)).toList();
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
    
    @GetMapping("course/count")
	public ResponseEntity<Map<String, Object>> getCourseCount(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			Map<String, Object> result = courseManagementService.getCountAllCourse();
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put("totalCourses", result.get("totalCourses"));
			addOn.put("transferableCourses", result.get("transferableCourses"));
			addOn.put("untransferableCourses", result.get("untransferableCourses"));
			addOn.put("totalSkilledCourses", result.get("totalSkilledCourses"));
			
			return new ResponseEntity<>(CommonUtils.response(null, MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
    
    
}