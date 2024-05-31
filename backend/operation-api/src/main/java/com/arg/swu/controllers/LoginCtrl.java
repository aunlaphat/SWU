package com.arg.swu.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.arg.swu.configs.JwtTokenUtil;
import com.arg.swu.dto.AllMenuData;
import com.arg.swu.dto.LoginFormData;
import com.arg.swu.dto.ResponseLoginData;
import com.arg.swu.dto.UserData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.SysMoodle;
import com.arg.swu.repositories.AutUserRepo;
import com.arg.swu.repositories.SysMoodleRepository;
import com.arg.swu.services.EntityMapperService;
import com.arg.swu.services.JWTService;
import com.arg.swu.services.LdapService;
import com.arg.swu.services.LoginService;
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
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginCtrl implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(LoginCtrl.class);
	
	private final SysMoodleRepository sysMoodleRepository;

	private final AutUserRepo autUserRepo;
	private final JWTService jwtService;
	private final EntityMapperService mapperService;
	private final LdapService ldapService;
	private final LoginService loginService;
	private final UtilityService utilityService;
	private final JwtTokenUtil jwtTokenUtil;
	
	@PostMapping("authentication")
	public ResponseEntity<Map<String, Object>> login(HttpServletRequest request, HttpServletResponse response,
			@RequestBody LoginFormData data) {
		
		try {
			
			if (null == data) {
				return new ResponseEntity<>(CommonUtils.responseError(MSG_INVALID_USER), HttpStatus.OK);
			}
			
			if (StringUtils.isBlank(data.getUsername()) || StringUtils.isBlank(data.getPassword())) {
				return new ResponseEntity<>(CommonUtils.responseError(MSG_INVALID_USER), HttpStatus.OK);
			}
			
			AutUser user = autUserRepo.findByUsername(data.getUsername());

			if (null == user) {
				return new ResponseEntity<>(CommonUtils.responseError(MSG_INVALID_USER), HttpStatus.OK);
			}
			
			if (!Boolean.TRUE.equals(user.getActiveFlag())) {
				return new ResponseEntity<>(CommonUtils.responseError(MSG_INVALID_USER), HttpStatus.OK);
			}
			
			if (Boolean.TRUE.equals(user.getIsLocal())) {
				
				if (user.getRefUserType().equals(30032002l)) {
					return new ResponseEntity<>(CommonUtils.responseError(MSG_INVALID_USER), HttpStatus.OK);
				}
				
				if (!utilityService.checkPassword(data.getPassword(), user.getPassword())) {
					return new ResponseEntity<>(CommonUtils.responseError(MSG_INVALID_USER), HttpStatus.OK);
				}
			} else {
				
				if (Boolean.TRUE.equals(user.getActivePeriodStatus())) {
					if (!Boolean.TRUE.equals(CommonUtils.checkBetweenDate(user.getActivePeriodStart(), user.getActivePeriodEnd(), new Date()))) {
						return new ResponseEntity<>(CommonUtils.responseError(MSG_INVALID_USER), HttpStatus.OK);
					}
				}

            	String username = CommonUtils.escapeLdapSpecialCharacters(data.getUsername());
				if (!ldapService.authenLdap(username, data.getPassword())) {
					return new ResponseEntity<>(CommonUtils.responseError(MSG_INVALID_USER), HttpStatus.OK);
				}
			}
			
			
			UserData userData = mapperService.convertToData(user, UserData.class);
			List<AllMenuData> allMenuData = loginService.getAllMenu(user.getUserId());
			String token = jwtService.generateToken(userData.getUserId(), userData.getUsername());
			ResponseLoginData entries = new ResponseLoginData(userData, allMenuData, token);

			return new ResponseEntity<>(CommonUtils.response(entries, MSG_LOGIN_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(MSG_INVALID_USER), HttpStatus.OK);
		}
	}
	
	@PostMapping("moodle")
	public ResponseEntity<Map<String, Object>> moodle(HttpServletRequest request, HttpServletResponse response,
			@RequestBody LoginFormData data) {
		
		try {
			
			if (null == data) {
				return new ResponseEntity<>(CommonUtils.responseError(MSG_INVALID_USER), HttpStatus.OK);
			}
			
			if (StringUtils.isBlank(data.getToken())) {
				return new ResponseEntity<>(CommonUtils.responseError(MSG_INVALID_USER), HttpStatus.OK);
			}

			Date expiration = jwtTokenUtil.getExpirationDateFromToken(data.getToken());
			boolean isExpire = expiration.before(new Date());
			if (isExpire) {
				return new ResponseEntity<>(CommonUtils.responseError("Token is expire"), HttpStatus.OK);
			}
			
			SysMoodle sysMoodle = sysMoodleRepository.findActive();
			
			if (null == sysMoodle) {
				return new ResponseEntity<>(CommonUtils.responseError(MSG_INVALID_USER), HttpStatus.OK);
			}
			
			String pwd = utilityService.decryptAES256(sysMoodle.getPassword());
			data = data.toBuilder().username(sysMoodle.getUsername()).password(pwd).build();

			return new ResponseEntity<>(CommonUtils.response(data, MSG_LOGIN_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(MSG_INVALID_USER), HttpStatus.OK);
		}
	}
	
}
