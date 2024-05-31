package com.arg.swu.controllers;

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
import com.arg.swu.dto.DropdownCriteriaData;
import com.arg.swu.models.AutUser;
import com.arg.swu.services.DropdownService;
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
@RequestMapping("dropdown")
@RequiredArgsConstructor
public class DropdownCtrl implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(DropdownCtrl.class);
	
	private final DropdownService dropdownService;
	private final UtilityService utilityService;
	
	@PostMapping("lookup")
	public ResponseEntity<Map<String, Object>> getLookup(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getLookup(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("user")
	public ResponseEntity<Map<String, Object>> getUserDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getUser(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("bank")
	public ResponseEntity<Map<String, Object>> getBankDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getBank(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("bank-account")
	public ResponseEntity<Map<String, Object>> getBankAccountDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getBankAccount(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("bank-branch")
	public ResponseEntity<Map<String, Object>> getBankBranchDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getBankBranch(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("course-type")
	public ResponseEntity<Map<String, Object>> getCourseTypeDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getCourseType(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("course-main")
	public ResponseEntity<Map<String, Object>> getCourseMainDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getCourseMain(data, userAction), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("coursepublic-main")
	public ResponseEntity<Map<String, Object>> getCoursepublicMainDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getCoursepublicMain(data, userAction), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("department")
	public ResponseEntity<Map<String, Object>> getDepartmentDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			AutUser userAction = utilityService.getActionUser(request);
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getDepartment(data, userAction), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("general-skill")
	public ResponseEntity<Map<String, Object>> getGeneralSkillDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getGeneralSkill(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("method")
	public ResponseEntity<Map<String, Object>> getMethodDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getMethod(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("occupation")
	public ResponseEntity<Map<String, Object>> getOccupationDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getOccupation(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("occupation-group")
	public ResponseEntity<Map<String, Object>> getOccupationGroupDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getOccupationGroup(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("personal")
	public ResponseEntity<Map<String, Object>> getPersonalDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getPersonal(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
    @PostMapping("role")
	public ResponseEntity<Map<String, Object>> getRoleDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getRole(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
    @PostMapping("amphur")
	public ResponseEntity<Map<String, Object>> getAmphurDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getAmphur(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
    @PostMapping("country")
	public ResponseEntity<Map<String, Object>> getCountryDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getCountry(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
    @PostMapping("prefixname")
	public ResponseEntity<Map<String, Object>> getPrefixnameDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getPrefixname(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
    @PostMapping("province")
	public ResponseEntity<Map<String, Object>> getProvinceDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getProvince(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
    @PostMapping("tambon")
	public ResponseEntity<Map<String, Object>> getTambonDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getTambon(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
    @PostMapping("teacher")
	public ResponseEntity<Map<String, Object>> getTeacherDropdown(HttpServletRequest request, HttpServletResponse response,
			@RequestBody DropdownCriteriaData data) {
		try {
			return new ResponseEntity<>(CommonUtils.response(dropdownService.getTeacher(data), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}    
    
}
