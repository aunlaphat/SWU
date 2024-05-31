package com.arg.swu.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.docx.TemplateShortCourseCycleApprovalProposalForm;
import com.arg.swu.dto.CourseActivityData;
import com.arg.swu.dto.CourseActivityMethodData;
import com.arg.swu.dto.CourseAttachData;
import com.arg.swu.dto.CourseCompanyData;
import com.arg.swu.dto.CourseInstructorData;
import com.arg.swu.dto.CourseLogData;
import com.arg.swu.dto.CourseMainData;
import com.arg.swu.dto.CourseMatchingData;
import com.arg.swu.dto.CourseOccupationData;
import com.arg.swu.dto.CourseRequestAttachData;
import com.arg.swu.dto.CourseScloData;
import com.arg.swu.dto.CourseSkillData;
import com.arg.swu.dto.CourseTargetData;
import com.arg.swu.dto.CoursepublicAttachData;
import com.arg.swu.dto.CoursepublicInstructorData;
import com.arg.swu.dto.CoursepublicLogData;
import com.arg.swu.dto.CoursepublicMainData;
import com.arg.swu.dto.CoursepublicMediaData;
import com.arg.swu.dto.DocxShortCourseCycleApprovalProposalFormData;
import com.arg.swu.dto.MasGeneralSkillLevelData;
import com.arg.swu.dto.SwuCurriculumData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.CourseActivity;
import com.arg.swu.models.CourseActivityMethod;
import com.arg.swu.models.CourseAttach;
import com.arg.swu.models.CourseCompany;
import com.arg.swu.models.CourseInstructor;
import com.arg.swu.models.CourseLog;
import com.arg.swu.models.CourseMain;
import com.arg.swu.models.CourseMatching;
import com.arg.swu.models.CourseOccupation;
import com.arg.swu.models.CourseRequestAttach;
import com.arg.swu.models.CourseSclo;
import com.arg.swu.models.CourseSkill;
import com.arg.swu.models.CourseTarget;
import com.arg.swu.models.CoursepublicAttach;
import com.arg.swu.models.CoursepublicInstructor;
import com.arg.swu.models.CoursepublicLog;
import com.arg.swu.models.CoursepublicMain;
import com.arg.swu.models.CoursepublicMedia;
import com.arg.swu.models.FinancePayment;
import com.arg.swu.models.MasCourseType;
import com.arg.swu.models.MasGeneralSkillLevel;
import com.arg.swu.models.MasPersonal;
import com.arg.swu.models.MemberCourse;
import com.arg.swu.repositories.CourseActivityMethodRepository;
import com.arg.swu.repositories.CourseActivityRepository;
import com.arg.swu.repositories.CourseAttachRepository;
import com.arg.swu.repositories.CourseCompanyRepository;
import com.arg.swu.repositories.CourseInstructorRepository;
import com.arg.swu.repositories.CourseLogRepository;
import com.arg.swu.repositories.CourseMainRepository;
import com.arg.swu.repositories.CourseMatchingRepository;
import com.arg.swu.repositories.CourseOccupationRepository;
import com.arg.swu.repositories.CourseRequestAttachRepository;
import com.arg.swu.repositories.CourseScloRepository;
import com.arg.swu.repositories.CourseSkillRepository;
import com.arg.swu.repositories.CourseTargetRepository;
import com.arg.swu.repositories.CoursepublicAttachRepository;
import com.arg.swu.repositories.CoursepublicInstructorRepository;
import com.arg.swu.repositories.CoursepublicLogRepository;
import com.arg.swu.repositories.CoursepublicMainRepository;
import com.arg.swu.repositories.CoursepublicMediaRepository;
import com.arg.swu.repositories.FinancePaymentRepository;
import com.arg.swu.repositories.MasCourseTypeRepository;
import com.arg.swu.repositories.MasGeneralSkillLevelRepository;
import com.arg.swu.repositories.MasPersonalRepository;
import com.arg.swu.repositories.MemberCourseRepository;
import com.arg.swu.services.CommonService;
import com.arg.swu.services.CourseManagementService;
import com.arg.swu.services.EntityMapperService;
import com.arg.swu.services.JmsSender;
import com.arg.swu.services.UtilityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@RestController
@RequestMapping("course-management")
@RequiredArgsConstructor
public class CourseManagementCtrl implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(CourseManagementCtrl.class);
	
	private final CommonService commonService;
	private final CourseManagementService courseManagementService;
	private final EntityMapperService mapperService;
	private final UtilityService utilityService;
	
	private final CourseActivityRepository  courseActivityRepository;
	private final CourseActivityMethodRepository courseActivityMethodRepository;
	private final CourseAttachRepository courseAttachRepository;
	private final CourseCompanyRepository courseCompanyRepository;
	private final CourseInstructorRepository courseInstructorRepository;
	private final CourseLogRepository courseLogRepository;
	private final CourseMainRepository courseMainRepository;
	private final CourseMatchingRepository courseMatchingRepository;
	private final CourseOccupationRepository courseOccupationRepository;
	private final CourseRequestAttachRepository courseRequestAttachRepository;
	private final CourseScloRepository courseScloRepository;
	private final CourseSkillRepository courseSkillRepository;
	private final CourseTargetRepository courseTargetRepository;
	private final CoursepublicAttachRepository coursepublicAttachRepository;
	private final CoursepublicInstructorRepository coursepublicInstructorRepository;
	private final CoursepublicLogRepository coursepublicLogRepository;
	private final CoursepublicMainRepository coursepublicMainRepository;
	private final CoursepublicMediaRepository coursepublicMediaRepository;
	private final MasCourseTypeRepository masCourseTypeRepository;
    private final MasGeneralSkillLevelRepository masGeneralSkillLevelRepository;
    private final MasPersonalRepository masPersonalRepository;
    private final MemberCourseRepository memberCourseRepository;
    private final FinancePaymentRepository financePaymentRepository;
	private final JmsSender jmsSender;
        
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
	
	@PostMapping("course-activity")
	public ResponseEntity<Map<String, Object>> postCourseActivity(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseActivityData data) {
		try {

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseActivity courseActivity = mapperService.convertToEntity(data, CourseActivity.class);
			courseActivity.setCreateBy(userAction);
			courseActivity.setCreateDate(new Date());
			
			courseActivityRepository.save(courseActivity);
			
			if (null != data.getCourseActiviryMethodList() && !data.getCourseActiviryMethodList().isEmpty()) {
				

				List<CourseActivityMethodData> responseData = new ArrayList<>();
				
				for (int i = 0; i < data.getCourseActiviryMethodList().size(); i++) {

					CourseActivityMethod courseActivityMethod = mapperService.convertToEntity(data.getCourseActiviryMethodList().get(i), CourseActivityMethod.class);
					courseActivityMethod.setCourseActivityId(courseActivity.getCourseActivityId());
					courseActivityMethod.setCreateBy(userAction);
					courseActivityMethod.setCreateDate(new Date());
					
					courseActivityMethodRepository.save(courseActivityMethod);
					
					responseData.add(mapperService.convertToEntity(courseActivityMethod, CourseActivityMethodData.class));
				}
			}
			
			data = mapperService.convertToEntity(courseActivity, CourseActivityData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-activity/{courseActivityId}")
	public ResponseEntity<Map<String, Object>> putCourseActivity(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseActivityId", required = true) Long courseActivityId,
			@RequestBody CourseActivityData data) {
		try {
			
			CourseActivity courseActivity = courseActivityRepository.findById(courseActivityId).orElse(null);
			if (null == courseActivity) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseActivity update = mapperService.convertDataToEntity4Update(data, CourseActivity.class, courseActivity, userAction);
			update.setCourseActivityId(courseActivityId);
			
			courseActivityRepository.save(update);
			
			courseActivityMethodRepository.deleteCourseActivityMethodByCourseActivityId(courseActivityId);
			
			if (null != data.getCourseActiviryMethodList() && !data.getCourseActiviryMethodList().isEmpty()) {
				
				List<CourseActivityMethod> courseActivityMethodList = data.getCourseActiviryMethodList().stream().map(o -> {
					CourseActivityMethod courseActivityMethod = mapperService.convertToEntity(o, CourseActivityMethod.class);
					courseActivityMethod.setCourseActivityMethodId(null);
					courseActivityMethod.setCourseActivityId(courseActivityId);
					courseActivityMethod.setCreateBy(userAction);
					courseActivityMethod.setCreateDate(new Date());
					return courseActivityMethod;
				}).toList();
				if (null != courseActivityMethodList && !courseActivityMethodList.isEmpty()) {					
					courseActivityMethodRepository.saveAll(courseActivityMethodList);
				}
			}
			
			data = mapperService.convertToEntity(courseActivity, CourseActivityData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("course-activity/{courseActivityId}")
	public ResponseEntity<Map<String, Object>> getCourseActivity(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseActivityId", required = true) Long courseActivityId) {
		try {
			
			CourseActivity courseActivity = courseActivityRepository.findById(courseActivityId).orElse(null);
			if (null == courseActivity) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseActivityData data = mapperService.convertToEntity(courseActivity, CourseActivityData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@DeleteMapping("course-activity/{courseActivityId}")
	public ResponseEntity<Map<String, Object>> deleteCourseActivity(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseActivityId", required = true) Long courseActivityId) {
		try {
			
			CourseActivity courseActivity = courseActivityRepository.findById(courseActivityId).orElse(null);
			if (null == courseActivity) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			courseActivityMethodRepository.deleteCourseActivityMethodByCourseActivityId(courseActivityId);
			
			courseActivityRepository.delete(courseActivity);
			
			return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("find-course-activity-method")
	public ResponseEntity<Map<String, Object>> findCourseActivityMethod(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseActivityMethodData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCourseActivityMethod(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("course-activity-method")
	public ResponseEntity<Map<String, Object>> postCourseActivityMethod(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseActivityMethodData data) {
		try {

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseActivityMethod courseActivityMethod = mapperService.convertToEntity(data, CourseActivityMethod.class);
			courseActivityMethod.setCreateBy(userAction);
			courseActivityMethod.setCreateDate(new Date());
			
			courseActivityMethodRepository.save(courseActivityMethod);
			
			data = mapperService.convertToEntity(courseActivityMethod, CourseActivityMethodData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-activity-method/{courseActivityMethodId}")
	public ResponseEntity<Map<String, Object>> putCourseActivityMethod(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseActivityMethodId", required = true) Long courseActivityMethodId,
			@RequestBody CourseActivityMethodData data) {
		try {
			
			CourseActivityMethod courseActivityMethod = courseActivityMethodRepository.findById(courseActivityMethodId).orElse(null);
			if (null == courseActivityMethod) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseActivityMethod update = mapperService.convertDataToEntity4Update(data, CourseActivityMethod.class, courseActivityMethod, userAction);
			update.setCourseActivityMethodId(courseActivityMethodId);
			
			courseActivityMethodRepository.save(update);
			
			data = mapperService.convertToEntity(courseActivityMethod, CourseActivityMethodData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-activity-method/{courseActivityMethodId}")
	public ResponseEntity<Map<String, Object>> getCourseActivityMethod(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseActivityMethodId", required = true) Long courseActivityMethodId) {
		try {
			
			CourseActivityMethod courseActivityMethod = courseActivityMethodRepository.findById(courseActivityMethodId).orElse(null);
			if (null == courseActivityMethod) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseActivityMethodData data = mapperService.convertToEntity(courseActivityMethod, CourseActivityMethodData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("find-course-attach")
	public ResponseEntity<Map<String, Object>> findCourseAttach(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseAttachData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCourseAttach(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("course-attach")
	public ResponseEntity<Map<String, Object>> postCourseAttach(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseAttachData data) {
		try {

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseAttach courseAttach = mapperService.convertToEntity(data, CourseAttach.class);
			courseAttach.setCreateBy(userAction);
			courseAttach.setCreateDate(new Date());
			
			courseAttachRepository.save(courseAttach);
			
			data = mapperService.convertToEntity(courseAttach, CourseAttachData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-attach/{courseAttachId}")
	public ResponseEntity<Map<String, Object>> putCourseAttach(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseAttachId", required = true) Long courseAttachId,
			@RequestBody CourseAttachData data) {
		try {
			
			CourseAttach courseAttach = courseAttachRepository.findById(courseAttachId).orElse(null);
			if (null == courseAttach) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseAttach update = mapperService.convertDataToEntity4Update(data, CourseAttach.class, courseAttach, userAction);
			update.setCourseAttachId(courseAttachId);
			
			courseAttachRepository.save(update);
			
			data = mapperService.convertToEntity(courseAttach, CourseAttachData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-attach/{courseAttachId}")
	public ResponseEntity<Map<String, Object>> getCourseAttach(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseAttachId", required = true) Long courseAttachId) {
		try {
			
			CourseAttach courseAttach = courseAttachRepository.findById(courseAttachId).orElse(null);
			if (null == courseAttach) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseAttachData data = mapperService.convertToEntity(courseAttach, CourseAttachData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@DeleteMapping("course-attach/{courseAttachId}")
	public ResponseEntity<Map<String, Object>> deleteCourseAttach(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseAttachId", required = true) Long courseAttachId) {
		try {
			
			CourseAttach courseAttach = courseAttachRepository.findById(courseAttachId).orElse(null);
			if (null == courseAttach) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			courseAttachRepository.delete(courseAttach);
			
			return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("find-course-company")
	public ResponseEntity<Map<String, Object>> findCourseCompany(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseCompanyData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCourseCompany(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("course-company")
	public ResponseEntity<Map<String, Object>> postCourseCompany(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseCompanyData data) {
		try {

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseCompany courseCompany = mapperService.convertToEntity(data, CourseCompany.class);
			courseCompany.setCreateBy(userAction);
			courseCompany.setCreateDate(new Date());
			
			courseCompanyRepository.save(courseCompany);
			
			data = mapperService.convertToEntity(courseCompany, CourseCompanyData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-company/{courseCompanyId}")
	public ResponseEntity<Map<String, Object>> putCourseCompany(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseCompanyId", required = true) Long courseCompanyId,
			@RequestBody CourseCompanyData data) {
		try {
			
			CourseCompany courseCompany = courseCompanyRepository.findById(courseCompanyId).orElse(null);
			if (null == courseCompany) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseCompany update = mapperService.convertDataToEntity4Update(data, CourseCompany.class, courseCompany, userAction);
			update.setCourseCompanyId(courseCompanyId);
			
			courseCompanyRepository.save(update);
			
			data = mapperService.convertToEntity(courseCompany, CourseCompanyData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-company/{courseCompanyId}")
	public ResponseEntity<Map<String, Object>> getCourseCompany(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseCompanyId", required = true) Long courseCompanyId) {
		try {
			
			CourseCompany courseCompany = courseCompanyRepository.findById(courseCompanyId).orElse(null);
			if (null == courseCompany) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseCompanyData data = mapperService.convertToEntity(courseCompany, CourseCompanyData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@DeleteMapping("course-company/{courseCompanyId}")
	public ResponseEntity<Map<String, Object>> deleteCourseCompany(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseCompanyId", required = true) Long courseCompanyId) {
		try {
			
			CourseCompany courseCompany = courseCompanyRepository.findById(courseCompanyId).orElse(null);
			if (null == courseCompany) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			courseCompanyRepository.delete(courseCompany);
			
			return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-course-instructor")
	public ResponseEntity<Map<String, Object>> findCourseInstructor(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseInstructorData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCourseInstructor(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("course-instructor")
	public ResponseEntity<Map<String, Object>> postCourseInstructor(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseInstructorData data) {
		try {

			AutUser userAction = utilityService.getActionUser(request);
			
			if (Boolean.TRUE.equals(data.getInstructorMain())) {			
				int count = courseInstructorRepository.countCourseInstructorMain(data.getCourseId());
				if (count > 0) {
					return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "อาจารย์ผู้รับผิดชอบหลักมีได้คนเดียวเท่านั้น"), HttpStatus.OK);
				}
			}
			
			CourseInstructor courseInstructor = mapperService.convertToEntity(data, CourseInstructor.class);
			courseInstructor.setCreateBy(userAction);
			courseInstructor.setCreateDate(new Date());
			
			courseInstructorRepository.save(courseInstructor);
			
			/** บันทึกรูปอาจารย์ที่คอร์ส/รอบคอร์ส แล้ว copy รูปไปใส่ตาราง mas_personal ด้วย */
			if (null != courseInstructor.getInstructorId()
					&& null != courseInstructor.getModule() 
					&& StringUtils.isNoneBlank(courseInstructor.getPrefix()) 
					&& StringUtils.isNoneBlank(courseInstructor.getFilename())) {
				MasPersonal masPersonal = masPersonalRepository.findById(courseInstructor.getInstructorId()).orElse(null);
				if (null != masPersonal) {
					MasPersonal updatePersonal = masPersonal
							.toBuilder()
							.prefix(courseInstructor.getPrefix())
							.module(courseInstructor.getModule())
							.personalPhotoPath(courseInstructor.getFilename())
							.build();
					masPersonalRepository.save(updatePersonal);
				}
			}
			
			data = mapperService.convertToEntity(courseInstructor, CourseInstructorData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-instructor/{courseInstructorId}")
	public ResponseEntity<Map<String, Object>> putCourseInstructor(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseInstructorId", required = true) Long courseInstructorId,
			@RequestBody CourseInstructorData data) {
		try {
			
			CourseInstructor courseInstructor = courseInstructorRepository.findById(courseInstructorId).orElse(null);
			if (null == courseInstructor) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			if (Boolean.TRUE.equals(data.getInstructorMain())) {			
				int count = courseInstructorRepository.countEditCourseInstructorMain(data.getCourseId(), courseInstructorId);
				if (count > 0) {
					return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "อาจารย์ผู้รับผิดชอบหลักมีได้คนเดียวเท่านั้น"), HttpStatus.OK);
				}
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseInstructor update = mapperService.convertDataToEntity4Update(data, CourseInstructor.class, courseInstructor, userAction);
			update.setCourseInstructorId(courseInstructorId);
			
			courseInstructorRepository.save(update);
			
			/** บันทึกรูปอาจารย์ที่คอร์ส/รอบคอร์ส แล้ว copy รูปไปใส่ตาราง mas_personal ด้วย */
			if (null != update.getInstructorId()
					&& null != update.getModule() 
					&& StringUtils.isNoneBlank(update.getPrefix()) 
					&& StringUtils.isNoneBlank(update.getFilename())) {
				MasPersonal masPersonal = masPersonalRepository.findById(update.getInstructorId()).orElse(null);
				if (null != masPersonal) {
					MasPersonal updatePersonal = masPersonal
							.toBuilder()
							.prefix(courseInstructor.getPrefix())
							.module(courseInstructor.getModule())
							.personalPhotoPath(courseInstructor.getFilename())
							.build();
					masPersonalRepository.save(updatePersonal);
				}
			}
			
			data = mapperService.convertToEntity(courseInstructor, CourseInstructorData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-instructor/{courseInstructorId}")
	public ResponseEntity<Map<String, Object>> getCourseInstructor(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseInstructorId", required = true) Long courseInstructorId) {
		try {
			
			CourseInstructor courseInstructor = courseInstructorRepository.findById(courseInstructorId).orElse(null);
			if (null == courseInstructor) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseInstructorData data = mapperService.convertToEntity(courseInstructor, CourseInstructorData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@DeleteMapping("course-instructor/{courseInstructorId}")
	public ResponseEntity<Map<String, Object>> deleteCourseInstructor(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseInstructorId", required = true) Long courseInstructorId) {
		try {
			
			CourseInstructor courseInstructor = courseInstructorRepository.findById(courseInstructorId).orElse(null);
			if (null == courseInstructor) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}

			courseInstructorRepository.delete(courseInstructor);

			return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("find-course-log")
	public ResponseEntity<Map<String, Object>> findCourseLog(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseLogData data) {
		try {
			
			Map<String, Object> result = new HashMap<>();
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("course-log")
	public ResponseEntity<Map<String, Object>> postCourseLog(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseLogData data) {
		try {

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseLog courseLog = mapperService.convertToEntity(data, CourseLog.class);
			courseLog.setCreateBy(userAction);
			courseLog.setCreateDate(new Date());
			
			courseLogRepository.save(courseLog);
			
			data = mapperService.convertToEntity(courseLog, CourseLogData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-log/{courseLogId}")
	public ResponseEntity<Map<String, Object>> putCourseLog(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseLogId", required = true) Long courseLogId,
			@RequestBody CourseLogData data) {
		try {
			
			CourseLog courseLog = courseLogRepository.findById(courseLogId).orElse(null);
			if (null == courseLog) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseLog update = mapperService.convertDataToEntity4Update(data, CourseLog.class, courseLog, userAction);
			update.setCourseLogId(courseLogId);
			
			courseLogRepository.save(update);
			
			data = mapperService.convertToEntity(courseLog, CourseLogData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-log/{courseLogId}")
	public ResponseEntity<Map<String, Object>> getCourseLog(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseLogId", required = true) Long courseLogId) {
		try {
			
			CourseLog courseLog = courseLogRepository.findById(courseLogId).orElse(null);
			if (null == courseLog) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseLogData data = mapperService.convertToEntity(courseLog, CourseLogData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-log/timeline/{courseId}")
	public ResponseEntity<Map<String, Object>> getCourseLogTimeline(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseId", required = true) Long courseId) {
		try {
			
			if (null == courseId) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			List<CourseLogData> data = courseManagementService.findCourseLogDataByCourseId(courseId);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("find-course-main")
	public ResponseEntity<Map<String, Object>> findCourseMain(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseMainData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			Map<String, Object> result = courseManagementService.findCourseMain(data, userAction);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("course-main")
	public ResponseEntity<Map<String, Object>> postCourseMain(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseMainData data) {
		try {

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseMain courseMain = mapperService.convertDataToEntity(data, CourseMain.class, userAction);
			courseMain.setCourseVersion(1l);
			/** รอส่ง */
			courseMain.setCourseMainStatus(30010002l);
			
			courseMainRepository.save(courseMain);
			
			// general course code
			courseMain.setCourseCode(commonService.generateCourseCode(courseMain));
			
			courseMainRepository.save(courseMain);
			
			// save course log
			CourseLog courseLog = new CourseLog();
			courseLog.setCourseId(courseMain.getCourseId());
			courseLog.setCourseMainStatus(courseMain.getCourseMainStatus());
			courseLog.setActiveFlag(true);
			courseLog.setCreateBy(userAction);
			courseLog.setCreateDate(new Date());
			
			courseLogRepository.save(courseLog);
			
			data = mapperService.convertToEntity(courseMain, CourseMainData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("==============[ postCourseMain ]=============");
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-main/{courseId}")
	public ResponseEntity<Map<String, Object>> putCourseMain(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseId", required = true) Long courseId,
			@RequestBody CourseMainData data) {
		try {
			
			CourseMain courseMain = courseMainRepository.findById(courseId).orElse(null);
			if (null == courseMain) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseMain update = mapperService.convertDataToEntity4Update(data, CourseMain.class, courseMain, userAction);
			update.setCourseId(courseId);
			
			courseMainRepository.save(update);
			
			data = mapperService.convertToEntity(courseMain, CourseMainData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-main/{courseId}")
	public ResponseEntity<Map<String, Object>> getCourseMain(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseId", required = true) Long courseId) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			CourseMainData criteria = new CourseMainData();
			criteria.setCourseId(courseId);
			Map<String, Object> result = courseManagementService.findCourseMain(criteria, userAction);
			List<CourseMainData> list = (List<CourseMainData>) result.get(ENTRIES);
			
			if (null == list || list.isEmpty()) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseMainData data = list.get(0);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-main/status/{courseId}")
	public ResponseEntity<Map<String, Object>> putCourseMainStatus(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseId", required = true) Long courseId,
			@RequestBody CourseMainData data) {

		try {
			
			CourseMain courseMain = courseMainRepository.findById(courseId).orElse(null);
			if (null == courseMain) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			// validate form
			int countCouseSkill = courseSkillRepository.countCourseSkillByCourseId(courseId);
			if (countCouseSkill == 0) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "ต้องมีหมวดหมู่ทักษะอย่างน้อย 1 รายการ"), HttpStatus.OK);
			}
			
			// instructor
			List<CourseInstructor> courseInstructorList = courseInstructorRepository.findByCourseId(courseId);
			if (null == courseInstructorList || courseInstructorList.isEmpty()) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "ต้องมีอาจารย์ผู้รับผิดชอบ 1 ท่านเท่านั้น"), HttpStatus.OK);
			}
			
			/**
			countInstructorMain2 = 1
			countInstructorMain1 = 0
			countInstructorMain0 = -1
			*/
			Long countInstructorMain = courseInstructorList.stream().filter(o -> Boolean.TRUE.equals(o.getInstructorMain())).count();
			if (countInstructorMain.compareTo(1l) == 1) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "ต้องมีอาจารย์ผู้รับผิดชอบ 1 ท่านเท่านั้น"), HttpStatus.OK);
			} else if (countInstructorMain.compareTo(1l) == -1) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "ต้องมีอาจารย์ผู้รับผิดชอบ 1 ท่านเท่านั้น"), HttpStatus.OK);
			}
			

			List<CourseSkill> courseSkillList = courseSkillRepository.findByCourseId(courseId);
			if (null == courseSkillList || courseSkillList.isEmpty()) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "ต้องมีทักษะอย่างน้อย 1 ทักษะ"), HttpStatus.OK);
			}
			BigDecimal result = courseSkillList.stream()
				    .map(CourseSkill::getSkillWeight)
				    .filter(java.util.Objects::nonNull)
				    .reduce(BigDecimal.ZERO, BigDecimal::add);
			
			if (result.compareTo(new BigDecimal(100)) == 1) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "ผลรวมน้ำหนักของทักษะต้องเป็น 100 เท่านั้น"), HttpStatus.OK);
			} else if (result.compareTo(new BigDecimal(100)) == -1) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "ผลรวมน้ำหนักของทักษะต้องเป็น 100 เท่านั้น"), HttpStatus.OK);
			}
			
			courseMain.setUpdateBy(userAction);
			courseMain.setUpdateDate(new Date());
			courseMain.setCourseMainStatus(data.getCourseMainStatus());
			
			courseMainRepository.save(courseMain);
			
			// save course log
			CourseLog courseLog = new CourseLog();
			courseLog.setCourseId(courseMain.getCourseId());
			courseLog.setCourseMainStatus(courseMain.getCourseMainStatus());
			courseLog.setActiveFlag(true);
			courseLog.setCreateBy(userAction);
			courseLog.setCreateDate(new Date());
			
			courseLogRepository.save(courseLog);
			
			// create default coursepublic_main
			
			data = mapperService.convertToEntity(courseMain, CourseMainData.class);
			

			if(data.getCourseMainStatus().compareTo(30010003L) == 0)
			{
				

				Long courseGeneration = commonService.getCourseGeneration(courseId);
				Date now = new Date();
				
				/** CoursepublicMain */
				CoursepublicMain main = CoursepublicMain
						.builder()
						.courseId(courseId)
						/** เปิดรอบหลักสูตร */
						.coursepublicStatus(30014001l)
						.courseGeneration(courseGeneration)
						.publicNameTh(courseMain.getCourseNameTh() + " รุ่นที่ " + courseGeneration)
						.publicNameEn(courseMain.getCourseNameEn() + " #" + courseGeneration)
						.depIdLevel1(courseMain.getDepIdLevel1())
						.depIdLevel2(courseMain.getDepIdLevel2())
						.courseFormat(courseMain.getCourseFormat())
						.courseFormatDescTh(courseMain.getCourseDescTh())
						.courseFormatDescEn(courseMain.getCourseDescEn())
						.createBy(userAction)
						.createDate(now)
						/** เปิดตลอด */
						.publicFormat(30007001l)
						.build();
				
				coursepublicMainRepository.save(main);
				
				Long module = null;
				String prefix = null;
				String filename = null;
				
				MasCourseType mct = masCourseTypeRepository.findById(courseMain.getCourseTypeId()).orElse(null);
				if (null != mct) {
					module = mct.getModule();
					prefix = mct.getPrefix();
					filename = mct.getFilename();				}
				
				/** CoursepublicMedia */
				CoursepublicMedia coursepublicMedia = CoursepublicMedia
						.builder()
						.coursepublicId(main.getCoursepublicId())
						/** ภาพปก Thumbnail */
						.mediaType(30012001l)
						.mediaNameTh("ชื่อ Thumbnail (ภาษาไทย)")
						.mediaNameEn("ชื่อ Thumbnail (ภาษาอังกฤษ)")
						.module(module)
						.prefix(prefix)
						.filename(filename)
						.createBy(userAction)
						.createDate(now)
						.build();
				
				coursepublicMediaRepository.save(coursepublicMedia);
				

				/** CourseInstructor */
				if (null != courseInstructorList && !courseInstructorList.isEmpty()) {
					List<CoursepublicInstructor> coursepublicInstructors = courseInstructorList.stream().map(o -> {
						CoursepublicInstructor cpi = CoursepublicInstructor.builder()
							.coursepublicId(main.getCoursepublicId())
							.externalEmail(o.getExternalEmail())
							.externalNameEn(o.getExternalNameEn())
							.externalNameTh(o.getExternalNameTh())
							.externalPhotoPath(o.getExternalPhotoPath())
							.filename(o.getFilename())
							.prefix(o.getPrefix())
							.module(o.getModule())
							.instructorId(o.getInstructorId())
							.instructorType(o.getInstructorType())
							.instructorMain(o.getInstructorMain())
							.build();
						cpi.setActiveFlag(true);
						cpi.setCreateBy(userAction);
						cpi.setCreateDate(now);
						return cpi;
					}).toList();

					if (null != coursepublicInstructors && !coursepublicInstructors.isEmpty()) {
						coursepublicInstructorRepository.saveAll(coursepublicInstructors);
					}
				}
				
				/** CourseAttach */
				List<CourseAttach> courseAttachList = courseAttachRepository.findByCourseId(courseMain.getCourseId());
				
				List<CoursepublicAttach> coursepublicAttachs = new ArrayList<>();
				for (int i = 0; i < courseAttachList.size(); i++) {
					CoursepublicAttach ca = CoursepublicAttach
							.builder()
							.coursepublicId(main.getCoursepublicId())
							.fileNameTh(courseAttachList.get(i).getFileNameTh())
							.fileNameEn(courseAttachList.get(i).getFileNameEn())
							.filename(courseAttachList.get(i).getFilename())
							.prefix(courseAttachList.get(i).getPrefix())
							.module(courseAttachList.get(i).getModule())
							.createBy(userAction)
							.createDate(now)
							.build();
					coursepublicAttachs.add(ca);
				}
				
				if (null != coursepublicAttachs && !coursepublicAttachs.isEmpty()) {
					coursepublicAttachRepository.saveAll(coursepublicAttachs);
				}
				
				/** CoursepublicLog */
				CoursepublicLog coursepublicLog = CoursepublicLog
						.builder()
						.coursepublicId(main.getCoursepublicId())
						.coursepublicStatus(courseGeneration)
						/** เปิดรอบหลักสูตร */
						.coursepublicStatus(main.getCoursepublicStatus())
						.createBy(userAction)
						.createDate(now)
						.createTimestamp(now)
						.build();
				
				coursepublicLogRepository.save(coursepublicLog);
				
				List<String> listIn = Arrays.asList(String.valueOf(courseMain.getCourseId()), String.valueOf(EMAIL_TEMPLATE_30036006));
				jmsSender.sendMessage(Q_SENDMAIL, String.join(";", listIn));
			}
			else if(data.getCourseMainStatus().equals(30010005L))
			{
				List<String> listIn = Arrays.asList(String.valueOf(courseMain.getCourseId()), String.valueOf(EMAIL_TEMPLATE_30036007));
				jmsSender.sendMessage(Q_SENDMAIL, String.join(";", listIn));
			}
			

			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("course-main/copy")
	public ResponseEntity<Map<String, Object>> postCourseMainCopy(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseMainData data) {

		try {
			
			CourseMain courseMain = courseMainRepository.findById(data.getCourseId()).orElse(null);
			if (null == courseMain) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseMain newMain = courseManagementService.copyCourseMain(courseMain, userAction);
			
			data = mapperService.convertToEntity(newMain, CourseMainData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SAVE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-course-matching")
	public ResponseEntity<Map<String, Object>> findCourseMatching(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseMatchingData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCourseMatching(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("course-matching")
	public ResponseEntity<Map<String, Object>> postCourseMatching(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseMatchingData data) {
		try {

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseMatching courseMatching = mapperService.convertToEntity(data, CourseMatching.class);
			courseMatching.setCreateBy(userAction);
			courseMatching.setCreateDate(new Date());
			
			courseMatchingRepository.save(courseMatching);
			
			data = mapperService.convertToEntity(courseMatching, CourseMatchingData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-matching/{courseMatchingId}")
	public ResponseEntity<Map<String, Object>> putCourseMatching(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseMatchingId", required = true) Long courseMatchingId,
			@RequestBody CourseMatchingData data) {
		try {
			
			CourseMatching courseMatching = courseMatchingRepository.findById(courseMatchingId).orElse(null);
			if (null == courseMatching) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseMatching update = mapperService.convertDataToEntity4Update(data, CourseMatching.class, courseMatching, userAction);
			update.setCourseMatchingId(courseMatchingId);
			
			courseMatchingRepository.save(update);
			
			data = mapperService.convertToEntity(courseMatching, CourseMatchingData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-matching/{courseMatchingId}")
	public ResponseEntity<Map<String, Object>> getCourseMatching(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseMatchingId", required = true) Long courseMatchingId) {
		try {
			
			CourseMatching courseMatching = courseMatchingRepository.findById(courseMatchingId).orElse(null);
			if (null == courseMatching) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseMatchingData data = mapperService.convertToEntity(courseMatching, CourseMatchingData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@DeleteMapping("course-matching/{courseMatchingId}")
	public ResponseEntity<Map<String, Object>> deleteCourseMatching(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseMatchingId", required = true) Long courseMatchingId) {
		try {
			
			CourseMatching courseMatching = courseMatchingRepository.findById(courseMatchingId).orElse(null);
			if (null == courseMatching ) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			courseMatchingRepository.delete(courseMatching);

			return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
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
	
	@PostMapping("course-occupation")
	public ResponseEntity<Map<String, Object>> postCourseOccupation(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseOccupationData data) {
		try {

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseOccupation courseOccupation = mapperService.convertToEntity(data, CourseOccupation.class);
			courseOccupation.setCreateBy(userAction);
			courseOccupation.setCreateDate(new Date());
			
			courseOccupationRepository.save(courseOccupation);
			
			data = mapperService.convertToEntity(courseOccupation, CourseOccupationData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-occupation/{courseOccupationId}")
	public ResponseEntity<Map<String, Object>> putCourseOccupation(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseOccupationId", required = true) Long courseOccupationId,
			@RequestBody CourseOccupationData data) {
		try {
			
			CourseOccupation courseOccupation = courseOccupationRepository.findById(courseOccupationId).orElse(null);
			if (null == courseOccupation) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseOccupation update = mapperService.convertDataToEntity4Update(data, CourseOccupation.class, courseOccupation, userAction);
			update.setCourseOccupationId(courseOccupationId);
			
			courseOccupationRepository.save(update);
			
			data = mapperService.convertToEntity(courseOccupation, CourseOccupationData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-occupation/{courseOccupationId}")
	public ResponseEntity<Map<String, Object>> getCourseOccupation(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseOccupationId", required = true) Long courseOccupationId) {
		try {
			
			CourseOccupation courseOccupation = courseOccupationRepository.findById(courseOccupationId).orElse(null);
			if (null == courseOccupation) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseOccupationData data = mapperService.convertToEntity(courseOccupation, CourseOccupationData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("find-course-request-attach")
	public ResponseEntity<Map<String, Object>> findCourseRequestAttach(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseRequestAttachData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCourseRequestAttach(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("course-request-attach")
	public ResponseEntity<Map<String, Object>> postCourseRequestAttach(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseRequestAttachData data) {
		try {

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseRequestAttach courseRequestAttach = mapperService.convertToEntity(data, CourseRequestAttach.class);
			courseRequestAttach.setCreateBy(userAction);
			courseRequestAttach.setCreateDate(new Date());
			
			courseRequestAttachRepository.save(courseRequestAttach);
			
			data = mapperService.convertToEntity(courseRequestAttach, CourseRequestAttachData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-request-attach/{courseRequestAttachId}")
	public ResponseEntity<Map<String, Object>> putCourseRequestAttach(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseRequestAttachId", required = true) Long courseRequestAttachId,
			@RequestBody CourseRequestAttachData data) {
		try {
			
			CourseRequestAttach courseRequestAttach = courseRequestAttachRepository.findById(courseRequestAttachId).orElse(null);
			if (null == courseRequestAttach) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseRequestAttach update = mapperService.convertDataToEntity4Update(data, CourseRequestAttach.class, courseRequestAttach, userAction);
			update.setCourseRequestAttachId(courseRequestAttachId);
			
			courseRequestAttachRepository.save(update);
			
			data = mapperService.convertToEntity(courseRequestAttach, CourseRequestAttachData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-request-attach/{courseRequestAttachId}")
	public ResponseEntity<Map<String, Object>> getCourseRequestAttach(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseRequestAttachId", required = true) Long courseRequestAttachId) {
		try {
			
			CourseRequestAttach courseRequestAttach = courseRequestAttachRepository.findById(courseRequestAttachId).orElse(null);
			if (null == courseRequestAttach) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseRequestAttachData data = mapperService.convertToEntity(courseRequestAttach, CourseRequestAttachData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@DeleteMapping("course-request-attach/{courseRequestAttachId}")
	public ResponseEntity<Map<String, Object>> deleteCourseRequestAttach(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseRequestAttachId", required = true) Long courseRequestAttachId) {
		try {
			
			CourseRequestAttach courseRequestAttach = courseRequestAttachRepository.findById(courseRequestAttachId).orElse(null);
			if (null == courseRequestAttach) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			courseRequestAttachRepository.delete(courseRequestAttach);
			
			return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("find-course-sclo")
	public ResponseEntity<Map<String, Object>> findCourseSclo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseScloData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCourseSclo(data);				
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("course-sclo")
	public ResponseEntity<Map<String, Object>> postCourseSclo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseScloData data) {
		try {

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseSclo courseSclo = mapperService.convertToEntity(data, CourseSclo.class);
			courseSclo.setCreateBy(userAction);
			courseSclo.setCreateDate(new Date());
			
			courseScloRepository.save(courseSclo);
			
			data = mapperService.convertToEntity(courseSclo, CourseScloData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-sclo/{courseScloId}")
	public ResponseEntity<Map<String, Object>> putCourseSclo(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseScloId", required = true) Long courseScloId,
			@RequestBody CourseScloData data) {
		try {
			
			CourseSclo courseSclo = courseScloRepository.findById(courseScloId).orElse(null);
			if (null == courseSclo) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseSclo update = mapperService.convertDataToEntity4Update(data, CourseSclo.class, courseSclo, userAction);
			update.setCourseScloId(courseScloId);
			
			courseScloRepository.save(update);
			
			data = mapperService.convertToEntity(courseSclo, CourseScloData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-sclo/{courseScloId}")
	public ResponseEntity<Map<String, Object>> getCourseSclo(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseScloId", required = true) Long courseScloId) {
		try {
			
			CourseSclo courseSclo = courseScloRepository.findById(courseScloId).orElse(null);
			if (null == courseSclo) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseScloData data = mapperService.convertToEntity(courseSclo, CourseScloData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@DeleteMapping("course-sclo/{courseScloId}")
	public ResponseEntity<Map<String, Object>> deleteCourseSclo(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseScloId", required = true) Long courseScloId) {
		try {
			
			CourseSclo courseSclo = courseScloRepository.findById(courseScloId).orElse(null);
			if (null == courseSclo) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			courseScloRepository.delete(courseSclo);
			
			return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("find-course-skill")
	public ResponseEntity<Map<String, Object>> findCourseSkill(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseSkillData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCourseSkill(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			addOn.put("sumWeight", result.get("sumWeight"));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("course-skill")
	public ResponseEntity<Map<String, Object>> postCourseSkill(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseSkillData data) {
		try {

			AutUser userAction = utilityService.getActionUser(request);
			
			BigDecimal skillWeight = courseSkillRepository.sumSkillWeight(data.getCourseId());
			BigDecimal totalSkillWeight = skillWeight.add(data.getSkillWeight());
			
			int count = courseSkillRepository.countGeneralSkill(data.getCourseId(), data.getGeneralSkillId());
			if (count > 0) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "หมวดหมู่ทักษะห้ามซ้ำ"), HttpStatus.OK);
			}
			
			if (totalSkillWeight.compareTo(new BigDecimal("100.00")) > 0) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "น้ำหนักต้องไม่เกิน 100"), HttpStatus.OK);
			}
			
			CourseSkill courseSkill = mapperService.convertToEntity(data, CourseSkill.class);
			courseSkill.setCreateBy(userAction);
			courseSkill.setCreateDate(new Date());
			
			courseSkillRepository.save(courseSkill);
			
			data = mapperService.convertToEntity(courseSkill, CourseSkillData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-skill/{courseSkillId}")
	public ResponseEntity<Map<String, Object>> putCourseSkill(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseSkillId", required = true) Long courseSkillId,
			@RequestBody CourseSkillData data) {
		try {
			
			CourseSkill courseSkill = courseSkillRepository.findById(courseSkillId).orElse(null);
			if (null == courseSkill) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			BigDecimal skillWeight = courseSkillRepository.sumEditSkillWeight(data.getCourseId(), courseSkillId);
			BigDecimal totalSkillWeight = skillWeight.add(data.getSkillWeight());
			
			int count = courseSkillRepository.countEditGeneralSkill(data.getCourseId(), data.getGeneralSkillId(), courseSkillId);
			if (count > 0) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "หมวดหมู่ทักษะห้ามซ้ำ"), HttpStatus.OK);
			}
			
			if (totalSkillWeight.compareTo(new BigDecimal("100.00")) > 0) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "น้ำหนักต้องไม่เกิน 100"), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseSkill update = mapperService.convertDataToEntity4Update(data, CourseSkill.class, courseSkill, userAction);
			update.setCourseSkillId(courseSkillId);
			
			courseSkillRepository.save(update);
			
			data = mapperService.convertToEntity(courseSkill, CourseSkillData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-skill/{courseSkillId}")
	public ResponseEntity<Map<String, Object>> getCourseSkill(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseSkillId", required = true) Long courseSkillId) {
		try {
			
			CourseSkill courseSkill = courseSkillRepository.findById(courseSkillId).orElse(null);
			if (null == courseSkill) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseSkillData data = mapperService.convertToEntity(courseSkill, CourseSkillData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@DeleteMapping("course-skill/{courseSkillId}")
	public ResponseEntity<Map<String, Object>> deleteCourseSkill(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseSkillId", required = true) Long courseSkillId) {
		try {
			
			CourseSkill courseSkill = courseSkillRepository.findById(courseSkillId).orElse(null);
			if (null == courseSkill) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			courseSkillRepository.delete(courseSkill);
			
			return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
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
	
	@PostMapping("course-target")
	public ResponseEntity<Map<String, Object>> postCourseTarget(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseTargetData data) {
		try {

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseTarget courseTarget = mapperService.convertToEntity(data, CourseTarget.class);
			courseTarget.setCreateBy(userAction);
			courseTarget.setCreateDate(new Date());
			
			courseTargetRepository.save(courseTarget);
			
			data = mapperService.convertToEntity(courseTarget, CourseTargetData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-target/{courseTargetId}")
	public ResponseEntity<Map<String, Object>> putCourseTarget(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseTargetId", required = true) Long courseTargetId,
			@RequestBody CourseTargetData data) {
		try {
			
			CourseTarget courseTarget = courseTargetRepository.findById(courseTargetId).orElse(null);
			if (null == courseTarget) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CourseTarget update = mapperService.convertDataToEntity4Update(data, CourseTarget.class, courseTarget, userAction);
			update.setCourseTargetId(courseTargetId);
			
			courseTargetRepository.save(update);
			
			data = mapperService.convertToEntity(courseTarget, CourseTargetData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-target/{courseTargetId}")
	public ResponseEntity<Map<String, Object>> getCourseTarget(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseTargetId", required = true) Long courseTargetId) {
		try {
			
			CourseTarget courseTarget = courseTargetRepository.findById(courseTargetId).orElse(null);
			if (null == courseTarget) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseTargetData data = mapperService.convertToEntity(courseTarget, CourseTargetData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	
	@PutMapping("course-target/course/{courseId}")
	public ResponseEntity<Map<String, Object>> putCourseTargetCourse(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseId", required = true) Long courseId,
			@RequestBody List<CourseTargetData> datas) {
		try {
			
			if (null == courseId) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.BAD_REQUEST.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			courseTargetRepository.deleteCourseTargetWhereCourseId(courseId);

			AutUser userAction = utilityService.getActionUser(request);
			
			List<CourseTargetData> responseData = new ArrayList<>();
			
			for (int i = 0; i < datas.size(); i++) {

				CourseTarget courseTarget = mapperService.convertToEntity(datas.get(i), CourseTarget.class);
				courseTarget.setCreateBy(userAction);
				courseTarget.setCreateDate(new Date());
				
				courseTargetRepository.save(courseTarget);
				
				responseData.add(mapperService.convertToEntity(courseTarget, CourseTargetData.class));
			}
			
			return new ResponseEntity<>(CommonUtils.response(responseData, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	
	@GetMapping("course-target/course/{courseId}")
	public ResponseEntity<Map<String, Object>> getCourseTargetCourse(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseId", required = true) Long courseId) {
		try {
			
			List<CourseTarget> courseTarget = courseTargetRepository.findByCourseId(courseId);
			if (null == courseTarget || courseTarget.isEmpty()) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			List<CourseTargetData> responseData = courseTarget.stream().map(o -> mapperService.convertToData(o, CourseTargetData.class)).toList();
			
			return new ResponseEntity<>(CommonUtils.response(responseData, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-occupation/course/{courseId}")
	public ResponseEntity<Map<String, Object>> putCourseOccupationCourse(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseId", required = true) Long courseId,
			@RequestBody List<CourseOccupationData> datas) {
		try {
			
			if (null == courseId) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.BAD_REQUEST.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			courseOccupationRepository.deleteCourseOccupationWhereCourseId(courseId);

			AutUser userAction = utilityService.getActionUser(request);
			
			List<CourseOccupationData> responseData = new ArrayList<>();
			
			for (int i = 0; i < datas.size(); i++) {

				CourseOccupation courseOccupation = mapperService.convertToEntity(datas.get(i), CourseOccupation.class);
				courseOccupation.setCreateBy(userAction);
				courseOccupation.setCreateDate(new Date());
				
				courseOccupationRepository.save(courseOccupation);
				
				responseData.add(mapperService.convertToEntity(courseOccupation, CourseOccupationData.class));
			}
			
			return new ResponseEntity<>(CommonUtils.response(responseData, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	
	@GetMapping("course-occupation/course/{courseId}")
	public ResponseEntity<Map<String, Object>> getCourseOccupationCourse(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseId", required = true) Long courseId) {
		try {
			
			List<CourseOccupation> courseOccupation = courseOccupationRepository.findByCourseId(courseId);
			if (null == courseOccupation || courseOccupation.isEmpty()) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			List<CourseOccupationData> responseData = courseOccupation.stream().map(o -> mapperService.convertToEntity(o, CourseOccupationData.class)).toList();
			
			return new ResponseEntity<>(CommonUtils.response(responseData, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-activity-method/course-activity/{courseActivityId}")
	public ResponseEntity<Map<String, Object>> putCourseActivityMethodCourseActivity(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseActivityId", required = true) Long courseActivityId,
			@RequestBody List<CourseActivityMethodData> datas) {
		try {
			
			if (null == courseActivityId) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.BAD_REQUEST.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			courseActivityMethodRepository.deleteCourseActivityMethodByCourseActivityId(courseActivityId);

			AutUser userAction = utilityService.getActionUser(request);
			
			List<CourseActivityMethodData> responseData = new ArrayList<>();
			
			for (int i = 0; i < datas.size(); i++) {

				CourseActivityMethod courseActivityMethod = mapperService.convertToEntity(datas.get(i), CourseActivityMethod.class);
				courseActivityMethod.setCourseActivityId(courseActivityId);
				courseActivityMethod.setCreateBy(userAction);
				courseActivityMethod.setCreateDate(new Date());
				
				courseActivityMethodRepository.save(courseActivityMethod);
				
				responseData.add(mapperService.convertToEntity(courseActivityMethod, CourseActivityMethodData.class));
			}
			
			return new ResponseEntity<>(CommonUtils.response(responseData, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-activity-method/course-activity/{courseActivityId}")
	public ResponseEntity<Map<String, Object>> getCourseActivityMethodCourseActivity(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseActivityId", required = true) Long courseActivityId) {
		try {
			
			List<CourseActivityMethod> courseActivityMethod = courseActivityMethodRepository.findByCourseActivityId(courseActivityId);
			if (null == courseActivityMethod || courseActivityMethod.isEmpty()) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			List<CourseActivityMethodData> responseData = courseActivityMethod.stream().map(o -> mapperService.convertToEntity(o, CourseActivityMethodData.class)).toList();
			
			return new ResponseEntity<>(CommonUtils.response(responseData, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-matching/course/{courseId}")
	public ResponseEntity<Map<String, Object>> putCourseMatchingCourse(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseId", required = true) Long courseId,
			@RequestBody List<CourseMatchingData> datas) {
		try {
			
			if (null == courseId) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.BAD_REQUEST.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			courseMatchingRepository.deleteCourseMatchingWhereCourseId(courseId);

			AutUser userAction = utilityService.getActionUser(request);
			
			List<CourseMatchingData> responseData = new ArrayList<>();
			
			for (int i = 0; i < datas.size(); i++) {

				CourseMatching courseMatching = CourseMatching
						.builder()
						.courseMatchingId(null)
						.courseId(datas.get(i).getCourseId())
						.subjectSwuId(datas.get(i).getSubjectSwuId())
						.curriculumSwuId(datas.get(i).getCurriculumSwuId())
						.build();
				courseMatching.setActiveFlag(true);
				courseMatching.setCreateBy(userAction);
				courseMatching.setCreateDate(new Date());
				
				courseMatchingRepository.save(courseMatching);
				
				responseData.add(mapperService.convertToEntity(courseMatching, CourseMatchingData.class));
			}
			
			return new ResponseEntity<>(CommonUtils.response(responseData, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-matching/course/{courseId}")
	public ResponseEntity<Map<String, Object>> getCourseMatchingCourse(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseId", required = true) Long courseId) {
		try {
			
			List<CourseMatching> courseMatching = courseMatchingRepository.findByCourseId(courseId);
			if (null == courseMatching || courseMatching.isEmpty()) {
				return new ResponseEntity<>(CommonUtils.response(new ArrayList<>(), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
			}
			
			List<CourseMatchingData> responseData = courseMatching.stream().map(o -> mapperService.convertToEntity(o, CourseMatchingData.class)).toList();
			
			return new ResponseEntity<>(CommonUtils.response(responseData, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-swu-curriculum")
	public ResponseEntity<Map<String, Object>> findSwuCurriculum(HttpServletRequest request, HttpServletResponse response,
			@RequestBody SwuCurriculumData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			Map<String, Object> addOn = new HashMap<>();

			if (null == userAction) {
				addOn.put(TOTAL_RECORDS, 0l);
				return new ResponseEntity<>(CommonUtils.response(new ArrayList<>(), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
			}
			
			if (null == userAction.getRefUserId()) {
				addOn.put(TOTAL_RECORDS, 0l);
				return new ResponseEntity<>(CommonUtils.response(new ArrayList<>(), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
			}
			
			Map<String, Object> result = courseManagementService.findSwuCurriculumByPersonal(data, userAction.getRefUserId());
					
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("==================[ findSwuCurriculum ]=====================");
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	// round
	@PostMapping("find-coursepublic-main")
	public ResponseEntity<Map<String, Object>> findCoursepublicMain(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CoursepublicMainData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			if (StringUtils.isNotBlank(data.getMode())) {
				if (MODE_SEARCH.equals(data.getMode())) {					
					Map<String, Object> result = courseManagementService.findCoursepublicMain(data, userAction);
					
					Map<String, Object> addOn = new HashMap<>();
					addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
					
					return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
				} else if (MODE_DOCX.equals(data.getMode())) {
					DocxShortCourseCycleApprovalProposalFormData criteria = DocxShortCourseCycleApprovalProposalFormData
							.builder()
							.coursepublicId(data.getCoursepublicId())
							.build();
					DocxShortCourseCycleApprovalProposalFormData obj = courseManagementService.findShortCourseCycleApprovalProposalFormData(criteria);
					TemplateShortCourseCycleApprovalProposalForm docx = new TemplateShortCourseCycleApprovalProposalForm
							.TemplateShortCourseCycleApprovalProposalFormBuilder(utilityService.isLangLocal(request) ? "docx/template-short-course-cycle-approval-proposal-form-th.docx" : "docx/template-short-course-cycle-approval-proposal-form-en.docx", 
									obj, 
									utilityService.isLangLocal(request) ? TH : EN)
							.generateParam().build();
					
					String fileData = Base64.getEncoder().encodeToString(docx.exec().exportByteArray());

					return new ResponseEntity<>(CommonUtils.response(fileData, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
				}
			}

			return new ResponseEntity<>(CommonUtils.response(null, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("coursepublic-main")
	public ResponseEntity<Map<String, Object>> postCoursepublicMain(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CoursepublicMainData data) {
		try {
			
			if (null == data || null == data.getCourseId()) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.BAD_REQUEST.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CourseMain courseMain = courseMainRepository.findById(data.getCourseId()).orElse(null);
			if (null == courseMain) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			Date now = new Date();
			
			/** CoursepublicMain */
			CoursepublicMain main = new CoursepublicMain();
			main.setCourseId(courseMain.getCourseId());
			
			/** เปิดรอบหลักสูตร */
			main.setCoursepublicStatus(30014001l);
			
			Long courseGeneration = commonService.getCourseGeneration(courseMain.getCourseId());
			main.setCourseGeneration(courseGeneration);
			main.setPublicNameTh(courseMain.getCourseNameTh() + " รุ่นที่ " + courseGeneration);
			main.setPublicNameEn(courseMain.getCourseNameEn() + " #" + courseGeneration);
			
			main.setDepIdLevel1(courseMain.getDepIdLevel1());
			main.setDepIdLevel2(courseMain.getDepIdLevel2());
			main.setCourseFormat(courseMain.getCourseFormat());
			main.setCourseFormatDescTh(courseMain.getCourseDescTh());	
			main.setCourseFormatDescEn(courseMain.getCourseDescEn());
			main.setCreateBy(userAction);
			main.setCreateDate(now);
			/** เปิดตลอด */
			main.setPublicFormat(30007001l);
			
			coursepublicMainRepository.save(main);
			
			/** CoursepublicMedia */
			CoursepublicMedia coursepublicMedia = new CoursepublicMedia();
			coursepublicMedia.setCoursepublicId(main.getCoursepublicId());
			/** ภาพปก Thumbnail */
			coursepublicMedia.setMediaType(30012001l);
			coursepublicMedia.setMediaNameTh(data.getMediaNameTh());
			coursepublicMedia.setMediaNameEn(data.getMediaNameEn());
			coursepublicMedia.setModule(data.getModule());
			coursepublicMedia.setPrefix(data.getPrefix());
			coursepublicMedia.setFilename(data.getFilename());
			coursepublicMedia.setCreateBy(userAction);
			coursepublicMedia.setCreateDate(now);
			
			coursepublicMediaRepository.save(coursepublicMedia);
			
			/** CourseInstructor */
			List<CourseInstructor> courseInstructorList = courseInstructorRepository.findByCourseId(courseMain.getCourseId());

			if (null != courseInstructorList && !courseInstructorList.isEmpty()) {
				List<CoursepublicInstructor> coursepublicInstructors = courseInstructorList.stream().map(o -> {
					CoursepublicInstructor cpi = CoursepublicInstructor.builder()
						.coursepublicId(main.getCoursepublicId())
						.externalEmail(o.getExternalEmail())
						.externalNameEn(o.getExternalNameEn())
						.externalNameTh(o.getExternalNameTh())
						.externalPhotoPath(o.getExternalPhotoPath())
						.filename(o.getFilename())
						.prefix(o.getPrefix())
						.module(o.getModule())
						.instructorId(o.getInstructorId())
						.instructorType(o.getInstructorType())
						.instructorMain(o.getInstructorMain())
						.build();
					cpi.setActiveFlag(true);
					cpi.setCreateBy(userAction);
					cpi.setCreateDate(now);
					return cpi;
				}).toList();

				if (null != coursepublicInstructors && !coursepublicInstructors.isEmpty()) {
					LOG.info("coursepublicInstructors -> {}", coursepublicInstructors);
					coursepublicInstructorRepository.saveAll(coursepublicInstructors);
				}
			}
			
			/** CourseAttach */
			List<CourseAttach> courseAttachList = courseAttachRepository.findByCourseId(courseMain.getCourseId());
			
			List<CoursepublicAttach> coursepublicAttachs = new ArrayList<>();
			for (int i = 0; i < courseAttachList.size(); i++) {
				CoursepublicAttach ca = new CoursepublicAttach();
				ca.setCoursepublicId(main.getCoursepublicId());
				ca.setFileNameTh(courseAttachList.get(i).getFileNameTh());
				ca.setFileNameEn(courseAttachList.get(i).getFileNameEn());
				ca.setFilename(courseAttachList.get(i).getFilename());
				ca.setPrefix(courseAttachList.get(i).getPrefix());
				ca.setModule(courseAttachList.get(i).getModule());
				ca.setCreateBy(userAction);
				ca.setCreateDate(now);
				coursepublicAttachs.add(ca);
			}
			
			coursepublicAttachRepository.saveAll(coursepublicAttachs);
			
			/** CoursepublicLog */
			CoursepublicLog coursepublicLog = new CoursepublicLog();
			coursepublicLog.setCoursepublicId(main.getCoursepublicId());
			coursepublicLog.setCoursepublicStatus(courseGeneration);
			/** เปิดรอบหลักสูตร */
			coursepublicLog.setCoursepublicStatus(main.getCoursepublicStatus());
			coursepublicLog.setCreateBy(userAction);
			coursepublicLog.setCreateDate(now);
			coursepublicLog.setCreateTimestamp(now);
			
			coursepublicLogRepository.save(coursepublicLog);
			
			data = mapperService.convertToData(main, CoursepublicMainData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("coursepublic-main/{coursepublicId}")
	public ResponseEntity<Map<String, Object>> putCoursepublicMain(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicId", required = true) Long coursepublicId,
			@RequestBody CoursepublicMainData data) {
		try {
			
			CoursepublicMain coursepublicMain = coursepublicMainRepository.findById(coursepublicId).orElse(null);
			if (null == coursepublicMain) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CoursepublicMain update = mapperService.convertDataToEntity4Update(data, CoursepublicMain.class, coursepublicMain, userAction);
			update.setCoursepublicId(coursepublicId);
			
			if (update.getPublicFormat().compareTo(30007001l) == 0) {
				update.setCoursePublicEnd(null);
				update.setCourseRegisEnd(null);
				update.setCourseClassEnd(null);
			}
			
			coursepublicMainRepository.save(update);
			
			data = mapperService.convertToData(update, CoursepublicMainData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("================[ putCoursepublicMain ]==============");
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

	@PostMapping("coursepublic-media")
	public ResponseEntity<Map<String, Object>> postCoursepublicMedia(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CoursepublicMediaData data) {
		try {
			AutUser userAction = utilityService.getActionUser(request);
			
			CoursepublicMedia coursepublicMedia = mapperService.convertDataToEntity(data, CoursepublicMedia.class, userAction);
			
			coursepublicMediaRepository.save(coursepublicMedia);
			
			data = mapperService.convertToData(coursepublicMedia, CoursepublicMediaData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PutMapping("coursepublic-media/{coursepublicMediaId}")
	public ResponseEntity<Map<String, Object>> putCoursepublicMedia(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicMediaId", required = true) Long coursepublicMediaId,
			@RequestBody CoursepublicMediaData data) {
		try {
			
			CoursepublicMedia coursepublicMedia = coursepublicMediaRepository.findById(coursepublicMediaId).orElse(null);
			if (null == coursepublicMedia) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CoursepublicMedia update = mapperService.convertDataToEntity4Update(data, CoursepublicMedia.class, coursepublicMedia, userAction);
			update.setCoursepublicMediaId(coursepublicMediaId);
			
			coursepublicMediaRepository.save(update);
			
			data = mapperService.convertToData(update, CoursepublicMediaData.class);

			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("coursepublic-media/{coursepublicMediaId}")
	public ResponseEntity<Map<String, Object>> getCoursepublicMedia(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicMediaId", required = true) Long coursepublicMediaId) {
		try {

			CoursepublicMedia coursepublicMedia = coursepublicMediaRepository.findById(coursepublicMediaId).orElse(null);
			if (null == coursepublicMedia) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CoursepublicMediaData data = mapperService.convertToData(coursepublicMedia, CoursepublicMediaData.class);

			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@DeleteMapping("coursepublic-media/{coursepublicMediaId}")
	public ResponseEntity<Map<String, Object>> deleteCoursepublicMedia(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicMediaId", required = true) Long coursepublicMediaId) {
		try {

			CoursepublicMedia coursepublicMedia = coursepublicMediaRepository.findById(coursepublicMediaId).orElse(null);
			if (null == coursepublicMedia) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			coursepublicMediaRepository.delete(coursepublicMedia);

			return new ResponseEntity<>(CommonUtils.response(null, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
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

			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("coursepublic-instructor")
	public ResponseEntity<Map<String, Object>> postCoursepublicInstructor(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CoursepublicInstructorData data) {
		try {
			AutUser userAction = utilityService.getActionUser(request);
			
			if (Boolean.TRUE.equals(data.getInstructorMain())) {				
				int count = coursepublicInstructorRepository.countCoursepublicInstructorMain(data.getCoursepublicId());
				if (count > 0) {
					return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "อาจารย์ผู้รับผิดชอบหลักมีได้คนเดียวเท่านั้น"), HttpStatus.OK);
				}
			}
			
			CoursepublicInstructor coursepublicInstructor = mapperService.convertDataToEntity(data, CoursepublicInstructor.class, userAction);
			
			coursepublicInstructorRepository.save(coursepublicInstructor);
			
			/** บันทึกรูปอาจารย์ที่คอร์ส/รอบคอร์ส แล้ว copy รูปไปใส่ตาราง mas_personal ด้วย */
			if (null != coursepublicInstructor.getInstructorId()
					&& null != coursepublicInstructor.getModule() 
					&& StringUtils.isNoneBlank(coursepublicInstructor.getPrefix()) 
					&& StringUtils.isNoneBlank(coursepublicInstructor.getFilename())) {
				MasPersonal masPersonal = masPersonalRepository.findById(coursepublicInstructor.getInstructorId()).orElse(null);
				if (null != masPersonal) {
					MasPersonal updatePersonal = masPersonal
							.toBuilder()
							.prefix(coursepublicInstructor.getPrefix())
							.module(coursepublicInstructor.getModule())
							.personalPhotoPath(coursepublicInstructor.getFilename())
							.build();
					masPersonalRepository.save(updatePersonal);
				}
			}
			
			data = mapperService.convertToData(coursepublicInstructor, CoursepublicInstructorData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PutMapping("coursepublic-instructor/{coursepublicInstructorId}")
	public ResponseEntity<Map<String, Object>> putCoursepublicInstructor(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicInstructorId", required = true) Long coursepublicInstructorId,
			@RequestBody CoursepublicInstructorData data) {
		try {
			
			CoursepublicInstructor coursepublicInstructor = coursepublicInstructorRepository.findById(coursepublicInstructorId).orElse(null);
			if (null == coursepublicInstructor) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			if (Boolean.TRUE.equals(data.getInstructorMain())) {				
				int count = coursepublicInstructorRepository.countEditCoursepublicInstructorMain(data.getCoursepublicId(), coursepublicInstructorId);
				if (count > 0) {
					return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "อาจารย์ผู้รับผิดชอบหลักมีได้คนเดียวเท่านั้น"), HttpStatus.OK);
				}
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CoursepublicInstructor update = mapperService.convertDataToEntity4Update(data, CoursepublicInstructor.class, coursepublicInstructor, userAction);
			update.setCoursepublicInstructorId(coursepublicInstructorId);
			
			coursepublicInstructorRepository.save(update);
			
			/** บันทึกรูปอาจารย์ที่คอร์ส/รอบคอร์ส แล้ว copy รูปไปใส่ตาราง mas_personal ด้วย */
			if (null != update.getInstructorId()
					&& null != update.getModule() 
					&& StringUtils.isNoneBlank(update.getPrefix()) 
					&& StringUtils.isNoneBlank(update.getFilename())) {
				MasPersonal masPersonal = masPersonalRepository.findById(update.getInstructorId()).orElse(null);
				if (null != masPersonal) {
					MasPersonal updatePersonal = masPersonal
							.toBuilder()
							.prefix(update.getPrefix())
							.module(update.getModule())
							.personalPhotoPath(update.getFilename())
							.build();
					masPersonalRepository.save(updatePersonal);
				}
			}
			
			data = mapperService.convertToData(update, CoursepublicInstructorData.class);

			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("coursepublic-instructor/{coursepublicInstructorId}")
	public ResponseEntity<Map<String, Object>> getCoursepublicInstructor(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicInstructorId", required = true) Long coursepublicInstructorId) {
		try {

			CoursepublicInstructor coursepublicInstructor = coursepublicInstructorRepository.findById(coursepublicInstructorId).orElse(null);
			if (null == coursepublicInstructor) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CoursepublicInstructorData data = mapperService.convertToData(coursepublicInstructor, CoursepublicInstructorData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@DeleteMapping("coursepublic-instructor/{coursepublicInstructorId}")
	public ResponseEntity<Map<String, Object>> deleteCoursepublicInstructor(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicInstructorId", required = true) Long coursepublicInstructorId) {
		try {

			CoursepublicInstructor coursepublicInstructor = coursepublicInstructorRepository.findById(coursepublicInstructorId).orElse(null);
			if (null == coursepublicInstructor) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			coursepublicInstructorRepository.delete(coursepublicInstructor);
			
			return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-coursepublic-attach")
	public ResponseEntity<Map<String, Object>> findCoursepublicAttach(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CoursepublicAttachData data) {
		try {
			
			Map<String, Object> result = courseManagementService.findCoursepublicAttach(data);
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));

			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_CREATE_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("coursepublic-attach")
	public ResponseEntity<Map<String, Object>> postCoursepublicAttach(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CoursepublicAttachData data) {
		try {
			AutUser userAction = utilityService.getActionUser(request);
			
			CoursepublicAttach coursepublicAttach = mapperService.convertDataToEntity(data, CoursepublicAttach.class, userAction);
			
			coursepublicAttachRepository.save(coursepublicAttach);
			
			data = mapperService.convertToData(coursepublicAttach, CoursepublicAttachData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PutMapping("coursepublic-attach/{coursepublicAttachId}")
	public ResponseEntity<Map<String, Object>> putCoursepublicAttach(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicAttachId", required = true) Long coursepublicAttachId,
			@RequestBody CoursepublicAttachData data) {
		try {
			
			CoursepublicAttach coursepublicAttach = coursepublicAttachRepository.findById(coursepublicAttachId).orElse(null);
			if (null == coursepublicAttach) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CoursepublicAttach update = mapperService.convertDataToEntity4Update(data, CoursepublicAttach.class, coursepublicAttach, userAction);
			update.setCoursepublicAttachId(coursepublicAttachId);
			
			coursepublicAttachRepository.save(update);
			
			data = mapperService.convertToData(update, CoursepublicAttachData.class);

			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("coursepublic-attach/{coursepublicAttachId}")
	public ResponseEntity<Map<String, Object>> getCoursepublicAttach(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicAttachId", required = true) Long coursepublicAttachId) {
		try {
			
			CoursepublicAttach coursepublicAttach = coursepublicAttachRepository.findById(coursepublicAttachId).orElse(null);
			if (null == coursepublicAttach) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CoursepublicAttachData data = mapperService.convertToData(coursepublicAttach, CoursepublicAttachData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@DeleteMapping("coursepublic-attach/{coursepublicAttachId}")
	public ResponseEntity<Map<String, Object>> deleteCoursepublicAttach(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicAttachId", required = true) Long coursepublicAttachId) {
		try {
			
			CoursepublicAttach coursepublicAttach = coursepublicAttachRepository.findById(coursepublicAttachId).orElse(null);
			if (null == coursepublicAttach) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			coursepublicAttachRepository.delete(coursepublicAttach);
			
			return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-coursepublic-log")
	public ResponseEntity<Map<String, Object>> findCoursepublicLog(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CoursepublicLogData data) {
		try {
			
			Map<String, Object> result = new HashMap<>();
					
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));

			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("coursepublic-log")
	public ResponseEntity<Map<String, Object>> postCoursepublicLog(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CoursepublicLogData data) {
		try {
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PutMapping("coursepublic-log/{coursepublicLogId}")
	public ResponseEntity<Map<String, Object>> putCoursepublicLog(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicLogId", required = true) Long coursepublicLogId,
			@RequestBody CoursepublicLogData data) {
		try {
			
			CoursepublicLog coursepublicLog = coursepublicLogRepository.findById(coursepublicLogId).orElse(null);
			if (null == coursepublicLog) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			CoursepublicLog update = mapperService.convertDataToEntity4Update(data, CoursepublicLog.class, coursepublicLog, userAction);
			update.setCoursepublicLogId(coursepublicLogId);
			
			coursepublicLogRepository.save(update);
			
			data = mapperService.convertToData(update, CoursepublicLogData.class);

			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("coursepublic-log/{coursepublicLogId}")
	public ResponseEntity<Map<String, Object>> getCoursepublicLog(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicLogId", required = true) Long coursepublicLogId) {
		try {

			CoursepublicLog coursepublicLog = coursepublicLogRepository.findById(coursepublicLogId).orElse(null);
			if (null == coursepublicLog) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			CoursepublicLogData data = mapperService.convertToData(coursepublicLog, CoursepublicLogData.class);

			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PutMapping("coursepublic-main/status/{coursepublicId}")
	public ResponseEntity<Map<String, Object>> putCoursepublicMainStatus(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicId", required = true) Long coursepublicId,
			@RequestBody CoursepublicMainData data) {

		try {
			
			CoursepublicMain coursepublicMain = coursepublicMainRepository.findById(coursepublicId).orElse(null);
			if (null == coursepublicMain) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			// validate form
			/*
			int countCouseSkill = courseSkillRepository.countCourseSkillByCourseId(courseId);
			if (countCouseSkill == 0) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "ต้องมีหมวดหมู่ทักษะอย่างน้อย 1 รายการ"), HttpStatus.OK);
			}
			*/

			int countCoursepublicInstrutor = coursepublicInstructorRepository.countCoursepublicInstructorMain(coursepublicId);
			if (countCoursepublicInstrutor != 1) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "ต้องมีอาจารย์ผู้รับผิดชอบ 1 ท่านเท่านั้น"), HttpStatus.OK);
			}
			
			coursepublicMain.setUpdateBy(userAction);
			coursepublicMain.setUpdateDate(new Date());
			coursepublicMain.setCoursepublicStatus(data.getCoursepublicStatus());
			
			coursepublicMainRepository.save(coursepublicMain);
			
			CoursepublicLog coursepublicLog = CoursepublicLog
					.builder()
					.coursepublicId(coursepublicId)
					.coursepublicStatus(data.getCoursepublicStatus())
					.createTimestamp(new Date())
					.build();

			coursepublicLog.setCreateDate(new Date());
			coursepublicLog.setCreateBy(userAction);
			
			coursepublicLogRepository.save(coursepublicLog);

			if ( coursepublicMain.getCoursepublicStatus().compareTo(30014003l) == 0) {

				courseManagementService.coreCourseCreateCourses(coursepublicId);

				List<CoursepublicInstructor> coursepublicInstructors = coursepublicInstructorRepository.findByCoursepublicId(coursepublicId);

				CoursepublicInstructor instructorMain = coursepublicInstructors.stream().filter(o -> Boolean.TRUE.equals(o.getInstructorMain())).findFirst().orElse(null);

				if (null != instructorMain) {
					MasPersonal masPersonal = masPersonalRepository.findById(instructorMain.getInstructorId()).orElse(null);
					if (null != masPersonal) {
						if (null == masPersonal.getMoodleUserId()) {
							courseManagementService.coreUserCreateUsers(masPersonal.getPersonalId());
							MasPersonal masPersonalNew = masPersonalRepository.findById(masPersonal.getPersonalId()).orElse(null);
							if (null != masPersonalNew) {
								courseManagementService.enrolManualEnrolUsers(coursepublicId, masPersonalNew.getMoodleUserId());
							}
						} else {
							courseManagementService.enrolManualEnrolUsers(coursepublicId, masPersonal.getMoodleUserId());
						}
					}
				}
			}
			
			if(data.getCoursepublicStatus().equals(30014002L))
			{
				List<String> listIn = Arrays.asList(String.valueOf(coursepublicMain.getCoursepublicId()), String.valueOf(EMAIL_TEMPLATE_30036008));
				jmsSender.sendMessage(Q_SENDMAIL, String.join(";", listIn));
			}
			else if(data.getCoursepublicStatus().equals(30014003L))
			{
				List<String> listIn = Arrays.asList(String.valueOf(coursepublicMain.getCoursepublicId()), String.valueOf(EMAIL_TEMPLATE_30036009));
				jmsSender.sendMessage(Q_SENDMAIL, String.join(";", listIn));
			}

			data = mapperService.convertToEntity(coursepublicMain, CoursepublicMainData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("coursepublic-log/timeline/{coursepublicId}")
	public ResponseEntity<Map<String, Object>> getCoursepublicLogTimeline(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicId", required = true) Long coursepublicId) {
		try {
			
			if (null == coursepublicId) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			List<CoursepublicLogData> data = courseManagementService.findCoursepublicLogDataByCoursepublicId(coursepublicId);
				
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@DeleteMapping("course-main/{courseId}")
    public ResponseEntity<Map<String, Object>> deleteCourseMain(HttpServletRequest request, HttpServletResponse response,
    		@PathVariable(name = "courseId", required = true) Long courseId) {
		try {
			
			CourseMain courseMain = courseMainRepository.findById(courseId).orElse(null);
			if (null == courseMain) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			if ( courseMain.getCourseMainStatus().compareTo(30010002l) != 0) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}

			courseLogRepository.deleteCourseLogByCourseId(courseId);
			courseTargetRepository.deleteCourseTargetWhereCourseId(courseId);
			courseOccupationRepository.deleteCourseOccupationWhereCourseId(courseId);
			courseInstructorRepository.deleteCourseInstructorByCourseId(courseId);
			courseCompanyRepository.deleteCourseCompanyByCourseId(courseId);
			courseMatchingRepository.deleteCourseMatchingWhereCourseId(courseId);
			courseAttachRepository.deleteCourseAttachByCourseId(courseId);
			courseSkillRepository.deleteCourseSkillByCourseId(courseId);
			courseScloRepository.deleteCourseScloByCourseId(courseId);
			courseActivityRepository.deleteCourseActivityByCourseId(courseId);
			courseActivityMethodRepository.deleteCourseActivityMethodByCourseId(courseId);
			courseRequestAttachRepository.deleteCourseRequestAttachByCourseId(courseId);			
			courseMainRepository.deleteCourseMainByCourseId(courseId);
			
			return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@DeleteMapping("coursepublic-main/{coursepublicId}")
    public ResponseEntity<Map<String, Object>> deleteCoursepublicMain(HttpServletRequest request, HttpServletResponse response,
    		@PathVariable(name = "coursepublicId", required = true) Long coursepublicId) {
		try {
			
			CoursepublicMain coursepublicMain = coursepublicMainRepository.findById(coursepublicId).orElse(null);
			if (null == coursepublicMain) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			if ( coursepublicMain.getCoursepublicStatus().compareTo(30014001l) != 0 && coursepublicMain.getCoursepublicStatus().compareTo(30014008l) != 0 ) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}

			coursepublicMediaRepository.deleteCoursepublicMediaByCoursepublicId(coursepublicId);
			coursepublicInstructorRepository.deleteCoursepublicInstructorByCoursepublicId(coursepublicId);
			coursepublicAttachRepository.deleteCoursepublicAttachByCoursepublicId(coursepublicId);
			coursepublicLogRepository.deleteCoursepublicLogByCoursepublicId(coursepublicId);
			coursepublicMainRepository.deleteCoursepublicMainByCoursepublicId(coursepublicId);
			
			return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
    
        @GetMapping("get-registration-list/{coursepublicId}")
	public ResponseEntity<Map<String, Object>> getRegistrationList(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicId", required = true) Long coursepublicId) {
            try {
                        LOG.info(">>>>>coursepublicId:"+coursepublicId);
                        Map<String, Object> result = courseManagementService.getRegistrationList(coursepublicId);
                        Map<String, Object> addOn = new HashMap<>();
                        addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
        }
        
        @PutMapping("put-cancel-approval/{coursepublicId}")
	public ResponseEntity<Map<String, Object>> putCancelApproval(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicId", required = true) Long coursepublicId,
			@RequestBody CoursepublicMainData data) {

		try {
			LOG.info(">>>>>>CoursePublicId:::"+coursepublicId);
                        Date presentDate = new Date();
			CoursepublicMain coursepublicMain = coursepublicMainRepository.findById(coursepublicId).orElse(null);
			if (null == coursepublicMain) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

			AutUser userAction = utilityService.getActionUser(request);
			
			// validate form
			/*
			int countCouseSkill = courseSkillRepository.countCourseSkillByCourseId(courseId);
			if (countCouseSkill == 0) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "ต้องมีหมวดหมู่ทักษะอย่างน้อย 1 รายการ"), HttpStatus.OK);
			}
			*/
			
			coursepublicMain.setUpdateBy(userAction);
			coursepublicMain.setUpdateDate(presentDate);
			coursepublicMain.setCoursepublicStatus(data.getCoursepublicStatus());
                        coursepublicMain.setRequestCancelDate(presentDate);
			coursepublicMainRepository.save(coursepublicMain);
			
			CoursepublicLog coursepublicLog = CoursepublicLog
					.builder()
					.coursepublicId(coursepublicId)
					.coursepublicStatus(data.getCoursepublicStatus())
                                        .cancelReason(data.getCancelReason())
					.createTimestamp(new Date())
					.build();

			coursepublicLog.setCreateDate(new Date());
			coursepublicLog.setCreateBy(userAction);
			
			coursepublicLogRepository.save(coursepublicLog);

			data = mapperService.convertToEntity(coursepublicMain, CoursepublicMainData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @GetMapping("get-mas-share-percent-data/{depId}")
	public ResponseEntity<Map<String, Object>> getMasSharePercentData(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "depId", required = true) Long depId) {
            try {
                        LOG.info(">>>>>depId:"+depId);
                        Map<String, Object> result = courseManagementService.getMasSharePercentData(depId);
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
        }
        
        @PostMapping("load-mas-general-skill-level-data")
	public ResponseEntity<Map<String, Object>> loadMasGeneralSkillLevelData(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CourseSkillData item) {
            try {
                        LOG.info(">>>>>generalSkillId:"+item.getGeneralSkillId());
                        LOG.info(">>>>>levelNo:"+item.getSkillLevel());
                        List<MasGeneralSkillLevelData> masGeneralSkillLevelDataList = new ArrayList();
                        List<MasGeneralSkillLevel> result = masGeneralSkillLevelRepository.findByGeneralSkillIdAndLevelNo(item.getGeneralSkillId(),item.getSkillLevel());
                        if(!result.isEmpty()){
                            for(int i=0;i<result.size();i++){
                                MasGeneralSkillLevelData masGeneralSkillLevelDataObj = mapperService.convertToEntity(result.get(i), MasGeneralSkillLevelData.class);
                                masGeneralSkillLevelDataList.add(masGeneralSkillLevelDataObj);
                            }
                        }
                        
			return new ResponseEntity<>(CommonUtils.response(masGeneralSkillLevelDataList, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
        }
        
        @GetMapping("get-course-public-log-data/{coursepublicId}")
        public ResponseEntity<Map<String, Object>> getCoursePublicLogData(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicId", required = true) Long coursepublicId) {
            try{
                Map<String, Object> result = courseManagementService.getCoursePublicLogDataLimitOne(coursepublicId,30014004l);
                return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
            }catch(Exception e){
                return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
            }
        }
        
        @PutMapping("put-approve-cancel-status/{coursepublicId}")
	public ResponseEntity<Map<String, Object>> putApproveCancelStatus(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicId", required = true) Long coursepublicId,
			@RequestBody CoursepublicMainData data) {

		try {
                    	AutUser userAction = utilityService.getActionUser(request);
                        LOG.info(">>>>>data.getCoursepublicStatus():::"+data.getCoursepublicStatus());
                        CoursepublicMain coursepublicMain = coursepublicMainRepository.findById(coursepublicId).orElse(null);
			if (null == coursepublicMain) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			coursepublicMain.setUpdateBy(userAction);
			coursepublicMain.setUpdateDate(new Date());
			coursepublicMain.setCoursepublicStatus(data.getCoursepublicStatus());
			
			coursepublicMainRepository.save(coursepublicMain);

			CoursepublicLog coursepublicLog = CoursepublicLog
					.builder()
					.coursepublicId(coursepublicId)
					.coursepublicStatus(data.getCoursepublicStatus())
                                        .approverId(userAction.getUserId())
					.createTimestamp(new Date())
					.build();

			coursepublicLog.setCreateDate(new Date());
			coursepublicLog.setCreateBy(userAction);
			
			coursepublicLogRepository.save(coursepublicLog);
                        
			List<MemberCourse> memberCourseObj = memberCourseRepository.findByCoursepublicId(coursepublicId);
			if (memberCourseObj.isEmpty()) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

                        for(int i=0;i<memberCourseObj.size();i++){
                            memberCourseObj.get(i).setUpdateBy(userAction);
                            memberCourseObj.get(i).setUpdateDate(new Date());
                            memberCourseObj.get(i).setStudyStatus(30016004l);
                            memberCourseRepository.save(memberCourseObj.get(i));
                        }
                        
                        List<FinancePayment> financePaymentObj = financePaymentRepository.findByCoursepublicId(coursepublicId);
                        
                        if (financePaymentObj.isEmpty()) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
                        
                        for(int i=0;i<financePaymentObj.size();i++){
                            financePaymentObj.get(i).setUpdateBy(userAction);
                            financePaymentObj.get(i).setUpdateDate(new Date());
                            financePaymentObj.get(i).setPaymentStatus(30033004l);
                            financePaymentRepository.save(financePaymentObj.get(i));
                        }
                        
//			data = mapperService.convertToEntity(coursepublicMain, CoursepublicMainData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
    @PutMapping("put-reject-cancel-status/{coursepublicId}")
		public ResponseEntity<Map<String, Object>> putRejectCancelStatus(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "coursepublicId", required = true) Long coursepublicId,
			@RequestBody CoursepublicMainData data) {

		try {
                    	AutUser userAction = utilityService.getActionUser(request);
                        LOG.info(">>>>>data.getCoursepublicStatus():::"+data.getCoursepublicStatus());
                        CoursepublicMain coursepublicMain = coursepublicMainRepository.findById(coursepublicId).orElse(null);
			if (null == coursepublicMain) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			coursepublicMain.setUpdateBy(userAction);
			coursepublicMain.setUpdateDate(new Date());
			coursepublicMain.setCoursepublicStatus(data.getCoursepublicStatus());
			
			coursepublicMainRepository.save(coursepublicMain);
                        
			CoursepublicLog coursepublicLog = CoursepublicLog
					.builder()
					.coursepublicId(coursepublicId)
					.coursepublicStatus(data.getCoursepublicStatus())
                                        .approverId(userAction.getUserId())
					.createTimestamp(new Date())
					.build();

			coursepublicLog.setCreateDate(new Date());
			coursepublicLog.setCreateBy(userAction);
			
			coursepublicLogRepository.save(coursepublicLog);
                        
			data = mapperService.convertToEntity(coursepublicMain, CoursepublicMainData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
}
