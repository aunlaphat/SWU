package com.arg.swu.controllers;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.CourseMainData;
import com.arg.swu.dto.CourseTargetData;
import com.arg.swu.dto.CoursepublicMainData;
import com.arg.swu.dto.CoursepublicMediaData;
import com.arg.swu.dto.MasCourseTypeData;
import com.arg.swu.services.CommonService;
import com.arg.swu.services.CourseManagementService;
import com.arg.swu.services.EntityMapperService;
import com.arg.swu.services.MasterService;
import com.arg.swu.services.UtilityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("all-courses")
@RequiredArgsConstructor
public class AllCoursesCtrl implements ApiConstant {
    private static final Logger LOG = LogManager.getLogger(AllCoursesCtrl.class);

    private final CommonService commonService;
	private final CourseManagementService courseManagementService;
	private final EntityMapperService mapperService;
	private final UtilityService utilityService;
    private final MasterService masterService;

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

    @PostMapping("find-course-target")
	public ResponseEntity<Map<String, Object>> findCourseTarget(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseTargetData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCourseTarget(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
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
}
