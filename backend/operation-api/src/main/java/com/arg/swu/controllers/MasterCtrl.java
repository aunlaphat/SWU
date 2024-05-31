package com.arg.swu.controllers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.arg.swu.dto.AutUserData;
import com.arg.swu.dto.MasBankAccountData;
import com.arg.swu.dto.MasBankBranchData;
import com.arg.swu.dto.MasBankChargeAttachData;
import com.arg.swu.dto.MasBankChargeData;
import com.arg.swu.dto.MasBankData;
import com.arg.swu.dto.MasCourseTypeData;
import com.arg.swu.dto.MasDepartmentData;
import com.arg.swu.dto.MasGeneralSkillData;
import com.arg.swu.dto.MasGeneralSkillLevelData;
import com.arg.swu.dto.MasOccupationData;
import com.arg.swu.dto.MasOccupationGroupData;
import com.arg.swu.dto.MasOccupationSkillData;
import com.arg.swu.dto.MasPersonalData;
import com.arg.swu.dto.MasSharePercentAttachData;
import com.arg.swu.dto.MasSharePercentData;
import com.arg.swu.dto.MasSharePercentHistoryData;
import com.arg.swu.dto.MasWebsiteBannerData;
import com.arg.swu.dto.NotiInfoData;
import com.arg.swu.dto.SysProgressData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.MasBank;
import com.arg.swu.models.MasBankAccount;
import com.arg.swu.models.MasBankBranch;
import com.arg.swu.models.MasBankCharge;
import com.arg.swu.models.MasBankChargeAttach;
import com.arg.swu.models.MasCourseType;
import com.arg.swu.models.MasDepartment;
import com.arg.swu.models.MasGeneralSkill;
import com.arg.swu.models.MasGeneralSkillLevel;
import com.arg.swu.models.MasOccupation;
import com.arg.swu.models.MasOccupationGroup;
import com.arg.swu.models.MasOccupationSkill;
import com.arg.swu.models.MasPersonal;
import com.arg.swu.models.MasSharePercent;
import com.arg.swu.models.MasSharePercentAttach;
import com.arg.swu.models.MasSharePercentHistory;
import com.arg.swu.models.MasBanner;
import com.arg.swu.models.SysSyncProgress;
import com.arg.swu.repositories.AutUserRepo;
import com.arg.swu.repositories.MasBankAccountRepository;
import com.arg.swu.repositories.MasBankBranchRepository;
import com.arg.swu.repositories.MasBankChargeAttachRepository;
import com.arg.swu.repositories.MasBankChargeRepository;
import com.arg.swu.repositories.MasBankRepository;
import com.arg.swu.repositories.MasCourseTypeRepository;
import com.arg.swu.repositories.MasDepartmentRepository;
import com.arg.swu.repositories.MasGeneralSkillLevelRepository;
import com.arg.swu.repositories.MasGeneralSkillRepository;
import com.arg.swu.repositories.MasOccupationGroupRepository;
import com.arg.swu.repositories.MasOccupationRepository;
import com.arg.swu.repositories.MasOccupationSkillRepository;
import com.arg.swu.repositories.MasPersonalRepository;
import com.arg.swu.repositories.MasSharePercentAttachRepository;
import com.arg.swu.repositories.MasSharePercentHistoryRepository;
import com.arg.swu.repositories.MasSharePercentRepository;
import com.arg.swu.repositories.MasWebsiteBannerRepository;
import com.arg.swu.repositories.SysSyncProgressRepository;
import com.arg.swu.services.CommonService;
import com.arg.swu.services.EntityMapperService;
import com.arg.swu.services.JmsSender;
import com.arg.swu.services.MasterService;
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
@RequestMapping("master")
@RequiredArgsConstructor
public class MasterCtrl implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(MasterCtrl.class);
	
	private final CommonService commonService;
	private final EntityMapperService mapperService;
	private final UtilityService utilityService;
	private final MasterService masterService;
	private final JmsSender jmsSender;
	
	private final AutUserRepo autUserRepo;
	
	private final MasBankAccountRepository masBankAccountRepository;
	private final MasBankBranchRepository masBankBranchRepository;
	private final MasBankRepository masBankRepository;
	private final MasBankChargeRepository masBankChargeRepository;
	private final MasBankChargeAttachRepository masBankChargeAttachRepository;
	private final MasCourseTypeRepository masCourseTypeRepository;
	private final MasDepartmentRepository masDepartmentRepository;
	private final MasGeneralSkillRepository masGeneralSkillRepository;
	private final MasOccupationGroupRepository masOccupationGroupRepository;
	private final MasOccupationRepository masOccupationRepository;
	private final MasOccupationSkillRepository masOccupationSkillRepository;
	private final MasPersonalRepository masPersonalRepository;
	private final MasSharePercentRepository masSharePercentRepository;
	private final MasSharePercentAttachRepository masSharePercentAttachRepository;
	private final MasSharePercentHistoryRepository masSharePercentHistoryRepository;
	private final MasGeneralSkillLevelRepository masGeneralSkillLevelRepository;
	private final MasWebsiteBannerRepository masWebsiteBannerRepository;
	
	private final SysSyncProgressRepository sysSyncProgressRepository;
	
	@GetMapping("check-login")
	public ResponseEntity<Map<String, Object>> checkLogin(HttpServletRequest req, HttpServletResponse res) {
		return new ResponseEntity<>(CommonUtils.response(null, MSG_LOGIN_SUCCESS, null), HttpStatus.OK);
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
	
	@PostMapping("general-skill")
	public ResponseEntity<Map<String, Object>> postGeneralSkill(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasGeneralSkillData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasGeneralSkill masGeneralSkill = mapperService.convertToEntity(data, MasGeneralSkill.class);
			masGeneralSkill.setGeneralSkillCode(commonService.generateRunningNumber(RunningNumber.GENERAL_SKILL));
			masGeneralSkill.setCreateBy(userAction);
			masGeneralSkill.setCreateDate(new Date());
			
			masGeneralSkillRepository.save(masGeneralSkill);
			
			if (null != data.getSkillLevelList() && !data.getSkillLevelList().isEmpty()) {
				

				List<MasGeneralSkillLevelData> responseData = new ArrayList<>();
				
				for (int i = 0; i < data.getSkillLevelList().size(); i++) {

					MasGeneralSkillLevel masGeneralSkillLevel = mapperService.convertToEntity(data.getSkillLevelList().get(i), MasGeneralSkillLevel.class);
					masGeneralSkillLevel.setGeneralSkillId(masGeneralSkill.getGeneralSkillId());
					masGeneralSkillLevel.setCreateBy(userAction);
					masGeneralSkillLevel.setCreateDate(new Date());
					
					masGeneralSkillLevelRepository.save(masGeneralSkillLevel);
					
					responseData.add(mapperService.convertToEntity(masGeneralSkillLevel, MasGeneralSkillLevelData.class));
				}
			}
			
			data = mapperService.convertToEntity(masGeneralSkill, MasGeneralSkillData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("general-skill/{generalSkillId}")
	public ResponseEntity<Map<String, Object>> putGeneralSkill(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "generalSkillId", required = true) Long generalSkillId,
			@RequestBody MasGeneralSkillData data) {
		try {
			
			MasGeneralSkill masGeneralSkill = masGeneralSkillRepository.findById(generalSkillId).orElse(null);
			if (null == masGeneralSkill) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasGeneralSkill update = mapperService.convertDataToEntity4Update(data, MasGeneralSkill.class, masGeneralSkill, userAction);
			update.setGeneralSkillId(generalSkillId);
			
			masGeneralSkillRepository.save(update);
			
			List<MasGeneralSkillLevelData> responseList = new ArrayList<>();			
			
			if (null != data.getSkillLevelList() && !data.getSkillLevelList().isEmpty()) {
				List<MasGeneralSkillLevel> levelList = data.getSkillLevelList().stream().map(o -> {
					MasGeneralSkillLevel masGeneralSkillLevel = null;
					if (null != o.getSkillLevelId())	{						
						masGeneralSkillLevel = masGeneralSkillLevelRepository.findById(o.getSkillLevelId()).orElse(null);
					}				
					if (null == masGeneralSkillLevel) {
						masGeneralSkillLevel = new MasGeneralSkillLevel();
						masGeneralSkillLevel.setCreateBy(userAction);
						masGeneralSkillLevel.setCreateDate(new Date());
					} else {
						masGeneralSkillLevel.setUpdateBy(userAction);
						masGeneralSkillLevel.setUpdateDate(new Date());
					}
					masGeneralSkillLevel.setGeneralSkillId(generalSkillId);
					masGeneralSkillLevel.setLevelNo(o.getLevelNo());
					masGeneralSkillLevel.setDescTh(o.getDescTh());
					masGeneralSkillLevel.setDescEn(o.getDescEn());
					masGeneralSkillLevel.setEvaluationEvident(o.getEvaluationEvident());
					masGeneralSkillLevel.setEvaluationCriteria(o.getEvaluationCriteria());
					masGeneralSkillLevel.setActiveFlag(o.getActiveFlag());
					return masGeneralSkillLevel;
				}).toList();
				
				if (null != levelList && !levelList.isEmpty()) {
					masGeneralSkillLevelRepository.saveAll(levelList);
					responseList = levelList.stream().map(o -> mapperService.convertToData(o, MasGeneralSkillLevelData.class)).toList();
				}
				
			}
			
			data = mapperService.convertToData(update, MasGeneralSkillData.class);
			data.setSkillLevelList(responseList);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("general-skill/{generalSkillId}")
	public ResponseEntity<Map<String, Object>> getGeneralSkill(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "generalSkillId", required = true) Long generalSkillId) {
		try {
			
			MasGeneralSkill masGeneralSkill = masGeneralSkillRepository.findById(generalSkillId).orElse(null);
			if (null == masGeneralSkill) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}

			List<MasGeneralSkillLevel> levelList = masGeneralSkillLevelRepository.findByGeneralSkillId(generalSkillId);
			List<MasGeneralSkillLevelData> responseList = new ArrayList<>();
			
			if (null != levelList && !levelList.isEmpty()) {
				masGeneralSkillLevelRepository.saveAll(levelList);
				responseList = levelList.stream().map(o -> mapperService.convertToData(o, MasGeneralSkillLevelData.class)).toList();
			}
			
			MasGeneralSkillData data = mapperService.convertToData(masGeneralSkill, MasGeneralSkillData.class);
			data.setSkillLevelList(responseList);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-bank-account")
	public ResponseEntity<Map<String, Object>> findBankAccount(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasBankAccountData data) {
		try {
			
			Map<String, Object> result = masterService.findBankAccountByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("bank-account")
	public ResponseEntity<Map<String, Object>> postBankAccount(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasBankAccountData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasBankAccount bankAccount = mapperService.convertToEntity(data, MasBankAccount.class);			
			bankAccount.setCreateBy(userAction);
			bankAccount.setCreateDate(new Date());
			
			masBankAccountRepository.save(bankAccount);
			
			data = mapperService.convertToEntity(bankAccount, MasBankAccountData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("bank-account/{bankAccountId}")
	public ResponseEntity<Map<String, Object>> putBankAccount(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "bankAccountId", required = true) Long bankAccountId,
			@RequestBody MasBankAccountData data) {
		try {
			
			MasBankAccount masBankAccount = masBankAccountRepository.findById(bankAccountId).orElse(null);
			if (null == masBankAccount) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasBankAccount update = mapperService.convertDataToEntity4Update(data, MasBankAccount.class, masBankAccount, userAction);
			update.setBankAccountId(bankAccountId);
			
			masBankAccountRepository.save(update);
			
			data = mapperService.convertToData(update, MasBankAccountData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("bank-account/{bankAccountId}")
	public ResponseEntity<Map<String, Object>> getBankAccount(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "bankAccountId", required = true) Long bankAccountId) {
		try {
			
			MasBankAccount masBankAccount = masBankAccountRepository.findById(bankAccountId).orElse(null);
			if (null == masBankAccount) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			MasBankAccountData data = mapperService.convertToData(masBankAccount, MasBankAccountData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("bank-account/active")
	public ResponseEntity<Map<String, Object>> getBankAccountActive(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			MasBankAccount masBankAccount = masBankAccountRepository.findActive();
			if (null == masBankAccount) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			MasBankAccountData data = mapperService.convertToData(masBankAccount, MasBankAccountData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-bank-branch")
	public ResponseEntity<Map<String, Object>> findBankBranch(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasBankBranchData data) {
		try {
			
			Map<String, Object> result = masterService.findBankBranchByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("bank-branch")
	public ResponseEntity<Map<String, Object>> postBankBranch(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasBankBranchData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasBankBranch bankBranch = mapperService.convertToEntity(data, MasBankBranch.class);			
			bankBranch.setCreateBy(userAction);
			bankBranch.setCreateDate(new Date());
			int count = masBankBranchRepository.countBankBranchCode(bankBranch.getBankBranchCode(), bankBranch.getBankId());
			if (count > 0) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "common.alert.dupplicate"), HttpStatus.OK);
			}
			
			masBankBranchRepository.save(bankBranch);
			
			data = mapperService.convertToEntity(bankBranch, MasBankBranchData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("bank-branch/{bankBranchId}")
	public ResponseEntity<Map<String, Object>> putBankBranch(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "bankBranchId", required = true) Long bankBranchId,
			@RequestBody MasBankBranchData data) {
		try {
			
			MasBankBranch masBankBranch = masBankBranchRepository.findById(bankBranchId).orElse(null);
			if (null == masBankBranch) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			int count = masBankBranchRepository.countEditBankBranchCode(data.getBankBranchCode(), data.getBankId(), bankBranchId);
			if (count > 0) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "common.alert.dupplicate"), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasBankBranch update = mapperService.convertDataToEntity4Update(data, MasBankBranch.class, masBankBranch, userAction);
			update.setBankBranchId(bankBranchId);
			
			masBankBranchRepository.save(update);
			
			data = mapperService.convertToData(update, MasBankBranchData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("bank-branch/{bankBranchId}")
	public ResponseEntity<Map<String, Object>> getBankBranch(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "bankBranchId", required = true) Long bankBranchId) {
		try {
			
			MasBankBranch masBankBranch = masBankBranchRepository.findById(bankBranchId).orElse(null);
			if (null == masBankBranch) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			MasBankBranchData data = mapperService.convertToData(masBankBranch, MasBankBranchData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("find-bank")
	public ResponseEntity<Map<String, Object>> findBank(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasBankData data) {
		try {
			
			Map<String, Object> result = masterService.findBankByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("bank")
	public ResponseEntity<Map<String, Object>> postBank(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasBankData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasBank masBank = mapperService.convertToEntity(data, MasBank.class);
			masBank.setCreateBy(userAction);
			masBank.setCreateDate(new Date());
			
			int count = masBankRepository.countBankCode(masBank.getBankCode());
			if (count > 0) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "common.alert.dupplicate"), HttpStatus.OK);
			}
			
			masBankRepository.save(masBank);
			
			data = mapperService.convertToEntity(masBank, MasBankData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("bank/{bankId}")
	public ResponseEntity<Map<String, Object>> putBank(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "bankId", required = true) Long bankId,
			@RequestBody MasBankData data) {
		try {
			
			MasBank masBank = masBankRepository.findById(bankId).orElse(null);
			if (null == masBank) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			int count = masBankRepository.countEditBankCode(data.getBankCode(), bankId);
			if (count > 0) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "common.alert.dupplicate"), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasBank update = mapperService.convertDataToEntity4Update(data, MasBank.class, masBank, userAction);
			update.setBankId(bankId);
			
			masBankRepository.save(update);
			
			data = mapperService.convertToData(update, MasBankData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("bank/{bankId}")
	public ResponseEntity<Map<String, Object>> getBank(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "bankId", required = false) Long bankId) {
		try {
			
			MasBank masBank = masBankRepository.findById(bankId).orElse(null);
			if (null == masBank) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			MasBankData data = mapperService.convertToData(masBank, MasBankData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-occupation-group")
	public ResponseEntity<Map<String, Object>> findOccupationGroup(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasOccupationGroupData data) {
		try {
			
			Map<String, Object> result = masterService.findOccupationGroupCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("occupation-group")
	public ResponseEntity<Map<String, Object>> postOccupationGroup(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasOccupationGroupData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasOccupationGroup masOccupationGroup = mapperService.convertToEntity(data, MasOccupationGroup.class);			
			masOccupationGroup.setCreateBy(userAction);
			masOccupationGroup.setCreateDate(new Date());
			
			masOccupationGroupRepository.save(masOccupationGroup);
			
			data = mapperService.convertToEntity(masOccupationGroup, MasOccupationGroupData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("occupation-group/{occupationGroupId}")
	public ResponseEntity<Map<String, Object>> putOccupationGroup(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "occupationGroupId", required = true) Long occupationGroupId,
			@RequestBody MasOccupationGroupData data) {
		try {
			
			MasOccupationGroup masOccupationGroup = masOccupationGroupRepository.findById(occupationGroupId).orElse(null);
			if (null == masOccupationGroup) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasOccupationGroup update = mapperService.convertDataToEntity4Update(data, MasOccupationGroup.class, masOccupationGroup, userAction);
			update.setOccupationGroupId(occupationGroupId);
			
			masOccupationGroupRepository.save(update);
			
			data = mapperService.convertToData(update, MasOccupationGroupData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("occupation-group/{occupationGroupId}")
	public ResponseEntity<Map<String, Object>> getOccupationGroup(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "occupationGroupId", required = true) Long occupationGroupId) {
		try {
			
			MasOccupationGroup masOccupationGroup = masOccupationGroupRepository.findById(occupationGroupId).orElse(null);
			if (null == masOccupationGroup) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			MasOccupationGroupData data = mapperService.convertToData(masOccupationGroup, MasOccupationGroupData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

    @PostMapping("find-share-percent")
	public ResponseEntity<Map<String, Object>> findSharePercent(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasSharePercentData data) {
		try {
			Map<String, Object> result = masterService.findSharePercentTableData(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
                    
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("share-percent")
	public ResponseEntity<Map<String, Object>> postSharePercent(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasSharePercentData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasSharePercent masSharePercent = mapperService.convertToEntity(data, MasSharePercent.class);			
			masSharePercent.setCreateBy(userAction);
			masSharePercent.setCreateDate(new Date());
			
			masSharePercentRepository.save(masSharePercent);
			
			data = mapperService.convertToEntity(masSharePercent, MasSharePercentData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("share-percent/{sharePercentId}")
	public ResponseEntity<Map<String, Object>> putSharePercent(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "sharePercentId", required = true) Long sharePercentId,
			@RequestBody MasSharePercentData data) {
		try {
			
			MasSharePercent masSharePercent = masSharePercentRepository.findById(sharePercentId).orElse(null);
			if (null == masSharePercent) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasSharePercent update = mapperService.convertDataToEntity4Update(data, MasSharePercent.class, masSharePercent, userAction);
			update.setSharePercentId(sharePercentId);
			
			masSharePercentRepository.save(update);
			
			data = mapperService.convertToData(update, MasSharePercentData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("share-percent/{sharePercentId}")
	public ResponseEntity<Map<String, Object>> getSharePercent(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "sharePercentId", required = true) Long sharePercentId) {
		try {
			
			MasSharePercent masSharePercent = masSharePercentRepository.findById(sharePercentId).orElse(null);
			if (null == masSharePercent) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			MasSharePercentData data = mapperService.convertToData(masSharePercent, MasSharePercentData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
    @PostMapping("find-share-percent-attach")
	public ResponseEntity<Map<String, Object>> findSharePercentAttach(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasSharePercentAttachData data) {
		try {
            Map<String, Object> result = masterService.findSharePercentAttachTableData(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("share-percent-attach")
	public ResponseEntity<Map<String, Object>> postSharePercentAttach(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasSharePercentAttachData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasSharePercentAttach masSharePercentAttach = mapperService.convertToEntity(data, MasSharePercentAttach.class);			
			masSharePercentAttach.setCreateBy(userAction);
			masSharePercentAttach.setCreateDate(new Date());
			
			masSharePercentAttachRepository.save(masSharePercentAttach);
			
			data = mapperService.convertToEntity(masSharePercentAttach, MasSharePercentAttachData.class);
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("share-percent-attach/{sharePercentAttachId}")
	public ResponseEntity<Map<String, Object>> putSharePercentAttach(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "sharePercentAttachId", required = true) Long sharePercentAttachId,
			@RequestBody MasSharePercentAttachData data) {
		try {
			
			MasSharePercentAttach masSharePercentAttach = masSharePercentAttachRepository.findById(sharePercentAttachId).orElse(null);
			if (null == masSharePercentAttach) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasSharePercentAttach update = mapperService.convertDataToEntity4Update(data, MasSharePercentAttach.class, masSharePercentAttach, userAction);
			update.setSharePercentAttachId(sharePercentAttachId);
			
			masSharePercentAttachRepository.save(update);
			
			data = mapperService.convertToData(update, MasSharePercentAttachData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("share-percent-attach/{sharePercentAttachId}")
	public ResponseEntity<Map<String, Object>> getSharePercentAttach(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "sharePercentAttachId", required = true) Long sharePercentAttachId) {
		try {
			
			MasSharePercentAttach masSharePercentAttach= masSharePercentAttachRepository.findById(sharePercentAttachId).orElse(null);
			if (null == masSharePercentAttach) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			MasSharePercentAttachData data = mapperService.convertToData(masSharePercentAttach, MasSharePercentAttachData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
    @PostMapping("find-share-percent-history")
	public ResponseEntity<Map<String, Object>> findSharePercentHistory(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasSharePercentHistoryData data) {
		try {
            Map<String, Object> result = masterService.findSharePercentHistoryTableData(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("share-percent-history")
	public ResponseEntity<Map<String, Object>> postSharePercentHistory(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasSharePercentHistoryData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasSharePercentHistory masSharePercentHistory = mapperService.convertToEntity(data, MasSharePercentHistory.class);
            masSharePercentHistory.setCreateBy(userAction);
			masSharePercentHistory.setCreateDate(new Date());
                        
			masSharePercentHistoryRepository.save(masSharePercentHistory);
			
			data = mapperService.convertToEntity(masSharePercentHistory, MasSharePercentHistoryData.class);
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("share-percent-history/{sharePercentHistoryId}")
	public ResponseEntity<Map<String, Object>> putSharePercentHistory(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "sharePercentHistoryId", required = true) Long sharePercentHistoryId,
			@RequestBody MasSharePercentHistoryData data) {
		try {
			
			MasSharePercentHistory masSharePercentHistory = masSharePercentHistoryRepository.findById(sharePercentHistoryId).orElse(null);
			if (null == masSharePercentHistory) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasSharePercentHistory update = mapperService.convertDataToEntity4Update(data, MasSharePercentHistory.class, masSharePercentHistory, userAction);
			update.setSharePercentHistoryId(sharePercentHistoryId);
			
			masSharePercentHistoryRepository.save(update);
			
			data = mapperService.convertToData(update, MasSharePercentHistoryData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("share-percent-history/{sharePercentHistoryId}")
	public ResponseEntity<Map<String, Object>> getSharePercentHistory(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "sharePercentHistoryId", required = true) Long sharePercentHistoryId) {
		try {
			
			MasSharePercentHistory masSharePercentHistory= masSharePercentHistoryRepository.findById(sharePercentHistoryId).orElse(null);
			if (null == masSharePercentHistory) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			MasSharePercentHistoryData data = mapperService.convertToData(masSharePercentHistory, MasSharePercentHistoryData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
    @PostMapping("find-department")
	public ResponseEntity<Map<String, Object>> findDepartment(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasDepartmentData data) {
		try {
			Map<String, Object> result = masterService.findDepartmentCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			addOn.put("syncDate", result.get("syncDate"));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("department")
	public ResponseEntity<Map<String, Object>> postDepartment(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasDepartmentData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasDepartment masDepartment = mapperService.convertToEntity(data, MasDepartment.class);			
			masDepartment.setCreateBy(userAction);
			masDepartment.setCreateDate(new Date());
			
			masDepartmentRepository.save(masDepartment);
			
			data = mapperService.convertToEntity(masDepartment, MasDepartmentData.class);
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("department/{depId}")
	public ResponseEntity<Map<String, Object>> putDepartment(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "depId", required = false) Long depId,
			@RequestBody MasDepartmentData data) {
		try {
			
			MasDepartment masDepartment = masDepartmentRepository.findById(depId).orElse(null);
			if (null == masDepartment) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasDepartment update = mapperService.convertDataToEntity4Update(data, MasDepartment.class, masDepartment, userAction);
			update.setDepId(depId);
			
			masDepartmentRepository.save(update);
			
			data = mapperService.convertToData(update, MasDepartmentData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
    @GetMapping("department/{depId}")
	public ResponseEntity<Map<String, Object>> getDepartment(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "depId", required = false) Long depId) {
		try {
			
			MasDepartment masDepartment= masDepartmentRepository.findById(depId).orElse(null);
			if (null == masDepartment) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			MasDepartmentData data = mapperService.convertToData(masDepartment, MasDepartmentData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-personal")
	public ResponseEntity<Map<String, Object>> findPersonal(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasPersonalData data) {
		try {
			
			 Map<String, Object> result = masterService.findPersonalCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			addOn.put("syncDate", result.get("syncDate"));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("personal")
	public ResponseEntity<Map<String, Object>> postPersonal(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasPersonalData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasPersonal masPersonal = mapperService.convertToEntity(data, MasPersonal.class);			
			masPersonal.setCreateBy(userAction);
			masPersonal.setCreateDate(new Date());
			
			masPersonalRepository.save(masPersonal);
			
			data = mapperService.convertToEntity(masPersonal, MasPersonalData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("personal/{personalId}")
	public ResponseEntity<Map<String, Object>> putPersonal(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "personalId", required = true) Long personalId,
			@RequestBody MasPersonalData data) {
		try {
			
			MasPersonal masPersonal = masPersonalRepository.findById(personalId).orElse(null);
			if (null == masPersonal) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasPersonal update = mapperService.convertDataToEntity4Update(data, MasPersonal.class, masPersonal, userAction);
			update.setPersonalId(personalId);
			
			masPersonalRepository.save(update);
			
			data = mapperService.convertToData(update, MasPersonalData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("personal/{personalId}")
	public ResponseEntity<Map<String, Object>> getPersonal(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "personalId", required = true) Long personalId) {
		try {
			
			MasPersonal masPersonal = masPersonalRepository.findById(personalId).orElse(null);
			if (null == masPersonal) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}

			MasPersonalData data = mapperService.convertToData(masPersonal, MasPersonalData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("personal/user/{personalId}")
	public ResponseEntity<Map<String, Object>> getPersonalUser(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "personalId", required = true) Long personalId) {
		try {
			
			MasPersonal masPersonal = masPersonalRepository.findById(personalId).orElse(null);
			if (null == masPersonal) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			int countRefUserId = autUserRepo.countRefUserId(personalId);
			if (countRefUserId > 0) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "common.alert.dupplicate"), HttpStatus.OK);
			}
			
			AutUserData user = AutUserData.builder()
					.username(masPersonal.getBuasriId())
					.refUserType(30032001l)
					.accessLevel(30025004l)
					.refUserId(masPersonal.getPersonalId())
					.firstnameTh(masPersonal.getFirstnameTh())
					.lastnameTh(masPersonal.getLastnameTh())
					.firstnameEn(masPersonal.getFirstnameEn())
					.lastnameEn(masPersonal.getLastnameEn())
					.depIdLevel1(masPersonal.getDepIdLevel1())
					.depIdLevel2(masPersonal.getDepIdLevel2())
					.email(masPersonal.getEmail())
					.activeFlag(true)
					.isLocal(false)
					.build();
			
			return new ResponseEntity<>(CommonUtils.response(user, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
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
	
	@PostMapping("course-type")
	public ResponseEntity<Map<String, Object>> postCourseType(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasCourseTypeData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasCourseType masCourseType = mapperService.convertToEntity(data, MasCourseType.class);	
			masCourseType.setCourseTypeCode(commonService.generateRunningNumber(RunningNumber.COURSE_TYPE));
			masCourseType.setCreateBy(userAction);
			masCourseType.setCreateDate(new Date());
			
			masCourseTypeRepository.save(masCourseType);
			
			data = mapperService.convertToEntity(masCourseType, MasCourseTypeData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("course-type/{courseTypeId}")
	public ResponseEntity<Map<String, Object>> putCourseType(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseTypeId", required = true) Long courseTypeId,
			@RequestBody MasCourseTypeData data) {
		try {
			
			MasCourseType masCourseType = masCourseTypeRepository.findById(courseTypeId).orElse(null);
			if (null == masCourseType) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasCourseType update = mapperService.convertDataToEntity4Update(data, MasCourseType.class, masCourseType, userAction);
			update.setCourseTypeId(courseTypeId);
			
			masCourseTypeRepository.save(update);
			
			data = mapperService.convertToData(update, MasCourseTypeData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("course-type/{courseTypeId}")
	public ResponseEntity<Map<String, Object>> getCourseType(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "courseTypeId", required = true) Long courseTypeId) {
		try {
			
			MasCourseType masCourseType = masCourseTypeRepository.findById(courseTypeId).orElse(null);
			if (null == masCourseType) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}

			MasCourseTypeData data = mapperService.convertToData(masCourseType, MasCourseTypeData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-occupation")
	public ResponseEntity<Map<String, Object>> findOccupation(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasOccupationData data) {
		try {
			
			Map<String, Object> result = masterService.findOccupationCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("occupation")
	public ResponseEntity<Map<String, Object>> postOccupation(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasOccupationData data) {
		try {
			
			LOG.info("data => {}", data);
			AutUser userAction = utilityService.getActionUser(request);
			
			List<MasOccupationSkillData> temp = data.getOccupationSkills();
			
			MasOccupation masOccupation = mapperService.convertToEntity(data, MasOccupation.class);	
			masOccupation.setOccupationCode(commonService.generateRunningNumber(RunningNumber.OCCUPATION));
			masOccupation.setCreateBy(userAction);
			masOccupation.setCreateDate(new Date());
			
			masOccupationRepository.save(masOccupation);
			
			data = mapperService.convertToEntity(masOccupation, MasOccupationData.class);
			
			if (null != temp && !temp.isEmpty()) {
				LOG.info("temp -> {}", temp);
				List<MasOccupationSkill> occupationSkillList = temp.stream().map(o -> {
					MasOccupationSkill mos = mapperService.convertToEntity(o, MasOccupationSkill.class);
					if (null == mos.getOccSkillId()) {
						mos.setOccupationId(masOccupation.getOccupationId());
						mos.setCreateBy(userAction);
						mos.setCreateDate(new Date());
					} else {
						mos = masOccupationSkillRepository.findById(mos.getOccSkillId()).orElse(null);
					}
					return mos;	
				}).toList();
				masOccupationSkillRepository.saveAll(occupationSkillList);
				
				List<MasOccupationSkillData> list = occupationSkillList.stream().map(o -> mapperService.convertToData(o, MasOccupationSkillData.class)).toList();
				data.setOccupationSkills(list);
				
			}
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("occupation/{occupationId}")
	public ResponseEntity<Map<String, Object>> putOccupation(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "occupationId", required = true) Long occupationId,
			@RequestBody MasOccupationData data) {
		try {
			
			MasOccupation masOccupation = masOccupationRepository.findById(occupationId).orElse(null);
			if (null == masOccupation) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasOccupation update = mapperService.convertDataToEntity4Update(data, MasOccupation.class, masOccupation, userAction);
			update.setOccupationId(occupationId);
			
			masOccupationRepository.save(update);

			List<MasOccupationSkillData> responseList = new ArrayList<>();			
			
			if (null != data.getOccupationSkills() && !data.getOccupationSkills().isEmpty()) {
				
				List<MasOccupationSkill> skills = data.getOccupationSkills().stream().map(o -> {
					MasOccupationSkill masOccupationSkill = masOccupationSkillRepository.findById(o.getOccSkillId()).orElse(null);
					if (null == masOccupationSkill) {
						masOccupationSkill.setOccSkillId(null);
						masOccupationSkill.setCreateBy(userAction);
						masOccupationSkill.setCreateDate(new Date());
					} else {
						masOccupationSkill.setUpdateBy(userAction);
						masOccupationSkill.setUpdateDate(new Date());
					}
					masOccupationSkill.setOccupationId(occupationId);
					masOccupationSkill.setGeneralSkillId(o.getGeneralSkillId());
					masOccupationSkill.setActiveFlag(o.getActiveFlag());
					masOccupationSkill.setOccSkillId(o.getOccSkillId());
					return masOccupationSkill;
				}).toList();
				
				if (null != skills && !skills.isEmpty()) {
					masOccupationSkillRepository.saveAll(skills);
					responseList = skills.stream().map(o -> mapperService.convertToData(o, MasOccupationSkillData.class)).toList();
				}
				
			}

			data = mapperService.convertToData(update, MasOccupationData.class);
		
			data.setOccupationSkills(responseList);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {

			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("occupation/{occupationId}")
	public ResponseEntity<Map<String, Object>> getOccupation(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "occupationId", required = true) Long occupationId) {
		try {
			
			MasOccupation masOccupation = masOccupationRepository.findById(occupationId).orElse(null);
			if (null == masOccupation) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}

			MasOccupationData data = mapperService.convertToData(masOccupation, MasOccupationData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-occupation-skill")
	public ResponseEntity<Map<String, Object>> findOccupationSkill(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasOccupationSkillData data) {
		try {
			
			Map<String, Object> result = masterService.findOccupationSkillCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("occupation-skill")
	public ResponseEntity<Map<String, Object>> postOccupationSkill(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasOccupationSkillData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasOccupationSkill masOccupationSkill = mapperService.convertToEntity(data, MasOccupationSkill.class);			
			masOccupationSkill.setCreateBy(userAction);
			masOccupationSkill.setCreateDate(new Date());
			
			masOccupationSkillRepository.save(masOccupationSkill);
			
			data = mapperService.convertToEntity(masOccupationSkill, MasOccupationSkillData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("occupation-skill/{occSkillId}")
	public ResponseEntity<Map<String, Object>> putOccupationSkill(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "occSkillId", required = true) Long occSkillId,
			@RequestBody MasOccupationSkillData data) {
		try {
			
			MasOccupationSkill masOccupationSkill = masOccupationSkillRepository.findById(occSkillId).orElse(null);
			if (null == masOccupationSkill) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasOccupationSkill update = mapperService.convertDataToEntity4Update(data, MasOccupationSkill.class, masOccupationSkill, userAction);
			update.setOccSkillId(occSkillId);
			
			masOccupationSkillRepository.save(update);
			
			data = mapperService.convertToData(update, MasOccupationSkillData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("occupation-skill/{occSkillId}")
	public ResponseEntity<Map<String, Object>> getOccupationSkill(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "occSkillId", required = true) Long occSkillId) {
		try {
			
			MasOccupationSkill masOccupationSkill = masOccupationSkillRepository.findById(occSkillId).orElse(null);
			if (null == masOccupationSkill) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}

			MasOccupationSkillData data = mapperService.convertToData(masOccupationSkill, MasOccupationSkillData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-bank-charge")
	public ResponseEntity<Map<String, Object>> findBankCharge(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasBankChargeData data) {
		try {

			Map<String, Object> result = masterService.findBankChargeCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("bank-charge")
	public ResponseEntity<Map<String, Object>> postBankCharge(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasBankChargeData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasBankCharge masBankCharge = mapperService.convertToEntity(data, MasBankCharge.class);			
			masBankCharge.setCreateBy(userAction);
			masBankCharge.setCreateDate(new Date());
			
			masBankChargeRepository.save(masBankCharge);
			
			data = mapperService.convertToEntity(masBankCharge, MasBankChargeData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("bank-charge/{chargeId}")
	public ResponseEntity<Map<String, Object>> putBankCharge(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "chargeId", required = true) Long chargeId,
			@RequestBody MasBankChargeData data) {
		try {
			
			MasBankCharge masBankCharge = masBankChargeRepository.findById(chargeId).orElse(null);
			if (null == masBankCharge) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasBankCharge update = mapperService.convertDataToEntity4Update(data, MasBankCharge.class, masBankCharge, userAction);
			update.setChargeId(chargeId);
			
			masBankChargeRepository.save(update);
			
			data = mapperService.convertToData(update, MasBankChargeData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("bank-charge/{chargeId}")
	public ResponseEntity<Map<String, Object>> getBankCharge(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "chargeId", required = true) Long chargeId) {
		try {
			
			MasBankCharge masBankCharge = masBankChargeRepository.findById(chargeId).orElse(null);
			if (null == masBankCharge) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}

			MasBankChargeData data = mapperService.convertToData(masBankCharge, MasBankChargeData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-bank-charge-attach")
	public ResponseEntity<Map<String, Object>> findBankChargeAttach(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasBankChargeAttachData data) {
		try {
			
			Map<String, Object> result = masterService.findBankChargeAttachCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("bank-charge-attach")
	public ResponseEntity<Map<String, Object>> postBankChargeAttach(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasBankChargeAttachData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasBankChargeAttach masBankChargeAttach = mapperService.convertToEntity(data, MasBankChargeAttach.class);			
			masBankChargeAttach.setCreateBy(userAction);
			masBankChargeAttach.setCreateDate(new Date());
			
			masBankChargeAttachRepository.save(masBankChargeAttach);
			
			data = mapperService.convertToEntity(masBankChargeAttach, MasBankChargeAttachData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("bank-charge-attach/{chargeId}")
	public ResponseEntity<Map<String, Object>> putBankChargeAttach(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "chargeId", required = true) Long chargeId,
			@RequestBody MasBankChargeAttachData data) {
		try {
			
			MasBankChargeAttach masBankChargeAttach = masBankChargeAttachRepository.findById(chargeId).orElse(null);
			if (null == masBankChargeAttach) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasBankChargeAttach update = mapperService.convertDataToEntity4Update(data, MasBankChargeAttach.class, masBankChargeAttach, userAction);
			update.setChargeId(chargeId);
			
			masBankChargeAttachRepository.save(update);
			
			data = mapperService.convertToData(update, MasBankChargeAttachData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("bank-charge-attach/{chargeId}")
	public ResponseEntity<Map<String, Object>> getBankChargeAttach(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "chargeId", required = true) Long chargeId) {
		try {
			
			MasBankChargeAttach masBankChargeAttach = masBankChargeAttachRepository.findById(chargeId).orElse(null);
			if (null == masBankChargeAttach) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}

			MasBankChargeAttachData data = mapperService.convertToData(masBankChargeAttach, MasBankChargeAttachData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
    @PostMapping("find-noti-info")
    public ResponseEntity<Map<String, Object>> findNotiInfo(HttpServletRequest request, HttpServletResponse response,@RequestBody NotiInfoData criteria){
        try {
        	
            Map<String, Object> result = masterService.findNotiInfo(criteria);
            Map<String, Object> addOn = new HashMap<>();
            addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
            
            return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
        } catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }
        
    @PutMapping("update-noti-detail")
    public ResponseEntity<Map<String, Object>> updateNotiDetail(HttpServletRequest request, HttpServletResponse response,@RequestBody NotiInfoData criteria){
        try {
        	
        	AutUser userAction = utilityService.getActionUser(request);
            Map<String, Object> result = masterService.updateNotiDetail(userAction, criteria);
            
            return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
        } catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
        }
    }

	@GetMapping("personal/pull")
	public ResponseEntity<Map<String, Object>> getPersonalPull(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			SysSyncProgress progress = sysSyncProgressRepository.findByTableName(SyncTable.MAS_PERSONAL.getValue());
			if (null == progress) {
				SysSyncProgress update = SysSyncProgress
						.builder()
						.tableName(SyncTable.MAS_PERSONAL.getValue())
						.progress(new BigDecimal(100).setScale(2, RoundingMode.HALF_EVEN))
						.executeRow(0l)
						.activeFlag(true)
						.createBy(userAction)
						.createDate(new Date())
						.build();
				sysSyncProgressRepository.save(update);
				progress = update;
			}
			
			SysSyncProgress update = progress.toBuilder()
					.executeRow(0l)
					.progress(new BigDecimal(BigInteger.ZERO))
					.updateBy(userAction)
					.updateDate(new Date())
					.build();
			
			sysSyncProgressRepository.save(update);
			
			jmsSender.sendMessage(Q_DUMP_MASTER_PERSONAL, "");
			
			return new ResponseEntity<>(CommonUtils.response(MSG_SAVE_SUCCESS, MSG_SAVE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("personal/check")
	public ResponseEntity<Map<String, Object>> getPersonalCheck(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			SysSyncProgress progress = sysSyncProgressRepository.findByTableName(SyncTable.MAS_PERSONAL.getValue());
			
			if (null == progress) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			SysProgressData data = mapperService.convertToData(progress, SysProgressData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SAVE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	
	@PostMapping("find-website-banner")
	public ResponseEntity<Map<String, Object>> findWebsiteBanner(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasWebsiteBannerData data) {
		try {
			
			Map<String, Object> result = masterService.findWebsiteBannerByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("website-banner")
	public ResponseEntity<Map<String, Object>> postWebsiteBanner(HttpServletRequest request, HttpServletResponse response,
			@RequestBody MasWebsiteBannerData data) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasBanner masWebsiteBanner = mapperService.convertToEntity(data, MasBanner.class);
			masWebsiteBanner.setCreateBy(userAction);
			masWebsiteBanner.setUpdateDate(new Date());
			masWebsiteBanner.setCreateDate(new Date());
			
			masWebsiteBannerRepository.save(masWebsiteBanner);
			
			data = mapperService.convertToEntity(masWebsiteBanner, MasWebsiteBannerData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("website-banner/{bannerId}")
	public ResponseEntity<Map<String, Object>> putWebsiteBanner(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "bannerId", required = true) Long bannerId,
			@RequestBody MasWebsiteBannerData data) {
		try {
			
			MasBanner masWebsiteBanner = masWebsiteBannerRepository.findById(bannerId).orElse(null);
			if (null == masWebsiteBanner) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			MasBanner update = mapperService.convertDataToEntity4Update(data, MasBanner.class, masWebsiteBanner, userAction);
			update.setBannerId(bannerId);
			
			masWebsiteBannerRepository.save(update);
			
			List<MasWebsiteBannerData> responseList = new ArrayList<>();			
			
			
			data = mapperService.convertToData(update, MasWebsiteBannerData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("website-banner/{bannerId}")
	public ResponseEntity<Map<String, Object>> getWebsiteBanner(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "bannerId", required = true) Long bannerId) {
		try {
			
			MasBanner masWebsiteBanner = masWebsiteBannerRepository.findById(bannerId).orElse(null);
			if (null == masWebsiteBanner) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			MasWebsiteBannerData data = mapperService.convertToData(masWebsiteBanner, MasWebsiteBannerData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@DeleteMapping("website-banner/delete/{bannerId}")
	public ResponseEntity<Map<String, Object>> deleteWebsiteBanner(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "bannerId", required = true) Long bannerId) {
		try {
			
			MasBanner masWebsiteBanner = masWebsiteBannerRepository.findById(bannerId).orElse(null);
			if (null == masWebsiteBanner) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			masWebsiteBannerRepository.delete(masWebsiteBanner);
			
			return new ResponseEntity<>(CommonUtils.response(null, MSG_DELETE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("department/pull")
	public ResponseEntity<Map<String, Object>> getDepartmentPull(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			AutUser userAction = utilityService.getActionUser(request);
			
			SysSyncProgress progress = sysSyncProgressRepository.findByTableName(SyncTable.MAS_DEPARTMENT.getValue());
			if (null == progress) {
				SysSyncProgress update = SysSyncProgress
						.builder()
						.tableName(SyncTable.MAS_DEPARTMENT.getValue())
						.progress(new BigDecimal(100).setScale(2, RoundingMode.HALF_EVEN))
						.executeRow(0l)
						.activeFlag(true)
						.createBy(userAction)
						.createDate(new Date())
						.build();
				sysSyncProgressRepository.save(update);
				progress = update;
			}
			
			SysSyncProgress update = progress.toBuilder()
					.executeRow(0l)
					.progress(new BigDecimal(BigInteger.ZERO))
					.updateBy(userAction)
					.updateDate(new Date())
					.build();
			
			sysSyncProgressRepository.save(update);
			
			jmsSender.sendMessage(Q_DUMP_MASTER_DEPARTMENT, "");
			
			return new ResponseEntity<>(CommonUtils.response(MSG_SAVE_SUCCESS, MSG_SAVE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("department/check")
	public ResponseEntity<Map<String, Object>> getDepartmentCheck(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			SysSyncProgress progress = sysSyncProgressRepository.findByTableName(SyncTable.MAS_DEPARTMENT.getValue());
			
			if (null == progress) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			SysProgressData data = mapperService.convertToData(progress, SysProgressData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SAVE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
    
}
