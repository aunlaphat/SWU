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
import com.arg.swu.dto.CourseActivityData;
import com.arg.swu.dto.CourseOccupationData;
import com.arg.swu.dto.CoursepublicInstructorData;
import com.arg.swu.dto.CoursepublicMainData;
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
@RequestMapping("course-detail")
@RequiredArgsConstructor
public class CourseDetailCtrl implements ApiConstant {
    private static final Logger LOG = LogManager.getLogger(CourseDetailCtrl.class);

    private final CommonService commonService;
	private final CourseManagementService courseManagementService;
	private final EntityMapperService mapperService;
	private final UtilityService utilityService;
    private final MasterService masterService;

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

    @PostMapping("find-course-activity")
	public ResponseEntity<Map<String, Object>> findCourseActivity(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseActivityData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCourseActivity(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
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

    @PostMapping("find-course-occupation")
	public ResponseEntity<Map<String, Object>> findCourseOccupation(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseOccupationData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCourseOccupation(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
}
