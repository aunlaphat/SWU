package com.arg.swu.controllers;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.MemberAddressData;
import com.arg.swu.dto.MemberInfoData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.MemberAddress;
import com.arg.swu.models.MemberInfo;
import com.arg.swu.repositories.AutPermissionRepository;
import com.arg.swu.repositories.AutRoleRepository;
import com.arg.swu.repositories.AutUserRepo;
import com.arg.swu.repositories.MemberAddressRepository;
import com.arg.swu.repositories.MemberInfoRepository;
import com.arg.swu.services.EntityMapperService;
import com.arg.swu.services.LdapService;
import com.arg.swu.services.UserManagementService;
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
@RequestMapping("user-management")
@RequiredArgsConstructor
public class UserManagementCtrl implements ApiConstant {

    private static final Logger LOG = LogManager.getLogger(UserManagementCtrl.class);

    private final EntityMapperService mapperService;
    private final UtilityService utilityService;
    private final LdapService ldapService;
    private final UserManagementService userManagementService;

    private final AutPermissionRepository autPermissionRepository;
    private final AutRoleRepository autRoleRepository;
    private final AutUserRepo autUserRepo;
    private final MemberAddressRepository memberAddressRepository;
    private final MemberInfoRepository memberInfoRepository;
    
	@PostMapping("find-member-info")
	public ResponseEntity<Map<String, Object>> findMemberInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MemberInfoData data ) {
	
		try {
			Map<String, Object> result = userManagementService.findMemberInfoByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("member-info")
	public ResponseEntity<Map<String, Object>> postMemberInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MemberInfoData data) {
		try {
			AutUser userAction = utilityService.getActionUser(request);
			
			MemberInfo memberInfo = mapperService.convertDataToEntity(data, MemberInfo.class, userAction);
			memberInfoRepository.save(memberInfo);
			
			MemberAddress memberAddress = mapperService.convertDataToEntity(data.getMemberAdderss(), MemberAddress.class, userAction);
			memberAddress.setMemberId(memberInfo.getMemberId());
			memberAddressRepository.save(memberAddress);
			
			data = mapperService.convertToEntity(memberInfo, MemberInfoData.class);
			data.setMemberAdderss(mapperService.convertToData(memberAddress, MemberAddressData.class));
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("member-info/{memberId}")
	public ResponseEntity<Map<String, Object>> putMemberInfo(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "memberId", required = true) Long memberId,
			@RequestBody MemberInfoData data) {
		try {
			MemberInfo memberInfo = memberInfoRepository.findById(memberId).orElse(null);
			if (null == memberInfo) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MemberInfo update = mapperService.convertDataToEntity4Update(data, MemberInfo.class, memberInfo, userAction);
			update.setMemberId(memberId);
			
			memberInfoRepository.save(update);
			
			if (null != data.getMemberAdderss() && null != data.getMemberAdderss().getMemberAddressId()) {

				MemberAddress memberAddress = memberAddressRepository.findById(data.getMemberAdderss().getMemberAddressId()).orElse(null);
				if (null != memberAddress) {
					
					MemberAddress updateChild = mapperService.convertDataToEntity(data.getMemberAdderss(), MemberAddress.class, userAction);
					updateChild.setMemberAddressId(data.getMemberAdderss().getMemberAddressId());					
					memberAddressRepository.save(updateChild);
					
					MemberAddressData address = mapperService.convertToData(updateChild, MemberAddressData.class);
					
					data = mapperService.convertToData(update, MemberInfoData.class);
					data.setMemberAdderss(address);
					
				}
			}
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("member-info/{memberId}")
	public ResponseEntity<Map<String, Object>> getMemberInfo(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "memberId", required = true) Long memberId) {
		try {
			MemberInfo memberInfo = memberInfoRepository.findById(memberId).orElse(null);
			if(null == memberInfo) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			MemberInfoData data = mapperService.convertToData(memberInfo, MemberInfoData.class);
			
			MemberAddress ma = memberAddressRepository.findByMemberId(memberId);
			
			data.setMemberAdderss(mapperService.convertToEntity(ma, MemberAddressData.class));
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
    
	@PostMapping("find-member-address")
	public ResponseEntity<Map<String, Object>> findMemberAddress(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MemberAddressData data ) {
	
		try {
			Map<String, Object> result = userManagementService.findMemberAddressByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("member-address")
	public ResponseEntity<Map<String, Object>> postMemberAddress(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MemberAddressData data) {
		try {
			AutUser userAction = utilityService.getActionUser(request);
			
			MemberAddress memberAddress = mapperService.convertDataToEntity(data, MemberAddress.class, userAction);
			memberAddressRepository.save(memberAddress);
			
			data = mapperService.convertToEntity(memberAddress, MemberAddressData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("member-address/{memberAddressId}")
	public ResponseEntity<Map<String, Object>> putMemberAddress(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "memberAddressId", required = true) Long memberAddressId,
			@RequestBody MemberAddressData data) {
		try {
			MemberAddress memberAddress = memberAddressRepository.findById(memberAddressId).orElse(null);
			if (null == memberAddress) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MemberAddress update = mapperService.convertDataToEntity4Update(data, MemberAddress.class, memberAddress, userAction);
			update.setMemberAddressId(memberAddressId);
			
			memberAddressRepository.save(update);
			
			data = mapperService.convertToData(update, MemberAddressData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("member-address/{memberAddressId}")
	public ResponseEntity<Map<String, Object>> getMemberAddress(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "memberAddressId", required = true) Long memberAddressId) {
		try {
			MemberAddress memberAddress = memberAddressRepository.findById(memberAddressId).orElse(null);
			if(null == memberAddress) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			MemberAddressData data = mapperService.convertToData(memberAddress, MemberAddressData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

}
