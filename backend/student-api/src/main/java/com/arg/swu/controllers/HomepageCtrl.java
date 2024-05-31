package com.arg.swu.controllers;

import java.util.HashMap;
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
import com.arg.swu.dto.CoursepublicMainData;
import com.arg.swu.dto.CoursepublicMediaData;
import com.arg.swu.dto.MasGeneralSkillData;
import com.arg.swu.models.CoursepublicMain;
import com.arg.swu.repositories.CoursepublicMainRepository;
import com.arg.swu.repositories.CoursepublicMediaRepository;
import com.arg.swu.services.CommonService;
import com.arg.swu.services.CourseManagementService;
import com.arg.swu.services.EntityMapperService;
import com.arg.swu.services.MasterService;
import com.arg.swu.services.UtilityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("homepage")
@RequiredArgsConstructor
public class HomepageCtrl implements ApiConstant {

    private static final Logger LOG = LogManager.getLogger(HomepageCtrl.class);

    private final CoursepublicMainRepository coursepublicMainRepository;
    private final CoursepublicMediaRepository coursepublicMediaRepository;
	
	private final CourseManagementService courseManagementService;
	private final EntityMapperService mapperService;
	private final UtilityService utilityService;
    private final MasterService masterService;

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
}
