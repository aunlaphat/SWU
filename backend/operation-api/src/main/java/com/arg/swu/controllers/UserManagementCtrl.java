package com.arg.swu.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.AutRoleData;
import com.arg.swu.dto.AutUserData;
import com.arg.swu.dto.MemberAddressData;
import com.arg.swu.dto.MemberInfoData;
import com.arg.swu.models.AutPermission;
import com.arg.swu.models.AutRole;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.AutUserRole;
import com.arg.swu.models.AutUserRolePK;
import com.arg.swu.models.MemberAddress;
import com.arg.swu.models.MemberInfo;
import com.arg.swu.repositories.AutPermissionRepository;
import com.arg.swu.repositories.AutRoleRepository;
import com.arg.swu.repositories.AutUserRepo;
import com.arg.swu.repositories.AutUserRoleRepository;
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
    private final AutUserRoleRepository autUserRoleRepository;

    @SuppressWarnings("unchecked")
    @PostMapping("find-user")
    public ResponseEntity<Map<String, Object>> findUser(HttpServletRequest request, HttpServletResponse response,
            @RequestBody AutUserData data) {
		try {
			
			Map<String, Object> result = userManagementService.findUserByCondition(data);
			
			List<AutUserData> userList = (List<AutUserData>) result.get(ENTRIES);
			if (null != userList && !userList.isEmpty()) {
                for (int i = 0; i < userList.size(); i++) {
                    List<AutRoleData> roles = userManagementService.getRolesByUserId(userList.get(i).getUserId());
                    userList.get(i).setRoles(roles);
                }
			}

			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(userList, MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
    }
    
    @PostMapping("user")
    public ResponseEntity<Map<String, Object>> postUser(HttpServletRequest request, HttpServletResponse response,
            @RequestBody AutUserData data) {
        try {

            AutUser userAction = utilityService.getActionUser(request);

            AutUser autUser = mapperService.convertDataToEntity(data, AutUser.class, userAction);

            int count = autUserRepo.countUsername(autUser.getUsername());
            if (count > 0) {
                return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "common.alert.dupplicate"), HttpStatus.OK);
            }

            if (Boolean.TRUE.equals(autUser.getIsLocal())) {
                String defaultPassword = utilityService.generateDefaultPassword(autUser.getUsername());
                String password = utilityService.encryptAES256(defaultPassword);
                autUser.setPassword(password);
            } else {
            	String username = CommonUtils.escapeLdapSpecialCharacters(autUser.getUsername());
                // Ldap check user is exist
                boolean userIsExists = ldapService.userIsExists(username);
                if (!userIsExists) {
                    return new ResponseEntity<>(CommonUtils.responseError(MSG_DATA_NOTFOUND), HttpStatus.OK);
                }
                autUser.setPassword(null);
            }

            autUserRepo.save(autUser);
            
            if (null != data.getRoleIds() && !data.getRoleIds().isEmpty()) {
            	for (int i = 0; i < data.getRoleIds().size(); i++) {
            		Long roleId = data.getRoleIds().get(i);
            		AutUserRolePK id = new AutUserRolePK(autUser.getUserId(), roleId);
        			AutUserRole newRole = AutUserRole
        					.builder()
        					.id(id)
        					.activeFlag(true)
        					.createBy(userAction)
        					.createDate(new Date())
        					.build();
        			
        			autUserRoleRepository.save(newRole);
				}
            }
            
            data = mapperService.convertToData(autUser, AutUserData.class);
            data.setPassword(null);
            
            return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }
    
    @PutMapping("user/{userId}")
    public ResponseEntity<Map<String, Object>> putUser(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "userId", required = true) Long userId,
            @RequestBody AutUserData data) {
        try {

            AutUser autUser = autUserRepo.findById(userId).orElse(null);
			if (null == autUser) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}

            int count = autUserRepo.countEditUsername(autUser.getUsername(), userId);
            if (count > 0) {
                return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "common.alert.dupplicate"), HttpStatus.OK);
            }

            AutUser userAction = utilityService.getActionUser(request);
            
            AutUser update = mapperService.convertDataToEntity4Update(data, AutUser.class, autUser, userAction);
            update.setUserId(userId);

            autUserRepo.save(update);
            
            if (null != data.getRoleIds() && !data.getRoleIds().isEmpty()) {
            	
            	autUserRoleRepository.deleteByUserId(userId);
            	
            	for (int i = 0; i < data.getRoleIds().size(); i++) {
            		Long roleId = data.getRoleIds().get(i);
            		AutUserRolePK id = new AutUserRolePK(userId, roleId);

        			// create user role
        			AutUserRole newRole = AutUserRole
        					.builder()
        					.id(id)
        					.activeFlag(true)
        					.createBy(userAction)
        					.createDate(new Date())
        					.build();
        			
        			autUserRoleRepository.save(newRole);
				}
            }
            
            data = mapperService.convertToData(autUser, AutUserData.class);
            data.setPassword(null);
            
            return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }
    
    @GetMapping("user/{userId}")
    public ResponseEntity<Map<String, Object>> getUser(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "userId", required = true) Long userId) {
        try {

            AutUser autUser = autUserRepo.findById(userId).orElse(null);
			if (null == autUser) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
            
			AutUserData data = mapperService.convertToData(autUser, AutUserData.class);

			List<Long> roleIds = userManagementService.getRolesByUserId(data.getUserId()).stream().map(AutRoleData::getRoleId).toList();
			data.setRoleIds(roleIds);
			
            data.setPassword(null);
            
            return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }
    
    @PostMapping("find-role")
    public ResponseEntity<Map<String, Object>> findRole(HttpServletRequest request, HttpServletResponse response,
            @RequestBody AutRoleData data) {
		try {
			
			Map<String, Object> result = userManagementService.findRoleByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
    }
    
    @PostMapping("role")
    public ResponseEntity<Map<String, Object>> postRole(HttpServletRequest request, HttpServletResponse response,
            @RequestBody AutRoleData data) {
        try {

            AutUser userAction = utilityService.getActionUser(request);

            AutRole autRole = mapperService.convertDataToEntity(data, AutRole.class, userAction);

            int count = autRoleRepository.countRoleName(autRole.getName());
            if (count > 0) {
                return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "common.alert.dupplicate"), HttpStatus.OK);
            }

            autRoleRepository.save(autRole);

            List<AutPermission> permissionList = data.getAllMenu()
                    .stream()
                    .map(o -> {
                        AutPermission permission = mapperService.convertDataToEntity(o, AutPermission.class, userAction);
                        permission.setRoleId(autRole.getRoleId());
                        return permission;
                    }).toList();

            autPermissionRepository.saveAll(permissionList);

            return new ResponseEntity<>(CommonUtils.response(null, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }

    @PutMapping("role/{roleId}")
    public ResponseEntity<Map<String, Object>> putRole(HttpServletRequest request, HttpServletResponse response,
            @PathVariable(name = "roleId", required = true) Long roleId,
            @RequestBody AutRoleData data) {
        try {

            AutRole autRole = autRoleRepository.findById(roleId).orElse(null);
            if (null == autRole) {
                return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
            }
            int count = autRoleRepository.countEditRoleName(autRole.getName(), roleId);
            if (count > 0) {
                return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "common.alert.dupplicate"), HttpStatus.OK);
            }

            AutUser userAction = utilityService.getActionUser(request, true);

            AutRole update = mapperService.convertDataToEntity4Update(data, AutRole.class, autRole, userAction);
            update.setRoleId(roleId);

            autRoleRepository.save(update);

            List<AutPermission> permissionList = data.getAllMenu()
                    .stream()
                    .map(o -> {
                        if (null != o.getPermissionId()) {
                            AutPermission oldEntity = autPermissionRepository.findById(o.getPermissionId()).orElse(new AutPermission());
                            AutPermission permission = mapperService.convertDataToEntity4Update(o, AutPermission.class, oldEntity, userAction);
                            permission.setRoleId(roleId);
                            return permission;
                        } else {
                            AutPermission permission = mapperService.convertDataToEntity(o, AutPermission.class, userAction);
                            permission.setRoleId(roleId);
                            return permission;
                        }
                    }).toList();
            
            autPermissionRepository.saveAll(permissionList);

            return new ResponseEntity<>(CommonUtils.response(null, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }

    @GetMapping("role/{roleId}")
    public ResponseEntity<Map<String, Object>> getRole(HttpServletRequest request, HttpServletResponse response,
            @PathVariable(name = "roleId", required = true) Long roleId) {
        try {

            AutRole autRole = autRoleRepository.findById(roleId).orElse(new AutRole());
            if (null == autRole) {
                return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
            }
            
            AutRoleData data = mapperService.convertToData(autRole, AutRoleData.class);

            data.setAllMenu(userManagementService.getPermission(data));
            
            return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }

    @GetMapping("role")
    public ResponseEntity<Map<String, Object>> getRole(HttpServletRequest request, HttpServletResponse response) {
        try {
            
            AutRoleData data = new AutRoleData();
            data.setActiveFlag(true);

            data.setAllMenu(userManagementService.getPermission(data));

            return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }
    
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
			
			if(null == ma) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
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
