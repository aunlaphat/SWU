package com.arg.swu.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import com.arg.swu.dto.CoursepublicMainData;
import com.arg.swu.dto.FinanceImportDetailData;
import com.arg.swu.dto.FinanceImportLogData;
import com.arg.swu.dto.FinancePaymentData;
import com.arg.swu.dto.FinanceReceiptConfigData;
import com.arg.swu.dto.TmpFinanceImportDetailData;
import com.arg.swu.dto.TmpFinanceImportLogData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.CoursepublicMain;
import com.arg.swu.models.FinanceImportDetail;
import com.arg.swu.models.FinanceImportLog;
import com.arg.swu.models.FinancePayment;
import com.arg.swu.models.FinanceReceiptConfig;
import com.arg.swu.models.MemberCourse;
import com.arg.swu.models.TmpFinanceImportDetail;
import com.arg.swu.models.TmpFinanceImportLog;
import com.arg.swu.repositories.CoursepublicMainRepository;
import com.arg.swu.repositories.FinanceImportDetailRepository;
import com.arg.swu.repositories.FinanceImportLogRepository;
import com.arg.swu.repositories.FinancePaymentRepository;
import com.arg.swu.repositories.FinanceReceiptConfigRepository;
import com.arg.swu.repositories.MemberCourseRepository;
import com.arg.swu.repositories.TmpFinanceImportDetailRepository;
import com.arg.swu.repositories.TmpFinanceImportLogRepository;
import com.arg.swu.services.EntityMapperService;
import com.arg.swu.services.FinancialManagementService;
import com.arg.swu.services.JmsSender;
import com.arg.swu.services.UtilityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@RestController
@RequestMapping("financial-management")
@RequiredArgsConstructor
public class FinancialManagementCtrl implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(FinancialManagementCtrl.class);
	
	private final EntityMapperService mapperService;
	private final JmsSender jmsSender;
	private final UtilityService utilityService;
    
	private final FinancialManagementService financialManagementService;

	private final CoursepublicMainRepository coursepublicMainRepository;
	private final FinancePaymentRepository financePaymentRepository;
	private final FinanceImportLogRepository financeImportLogRepository;
	private final FinanceImportDetailRepository financeImportDetailRepository;
	private final FinanceReceiptConfigRepository financeReceiptConfigRepository;
	private final TmpFinanceImportDetailRepository tmpFinanceImportDetailRepository;
	private final TmpFinanceImportLogRepository tmpFinanceImportLogRepository;
	private final MemberCourseRepository memberCourseRepository;
	
	@PostMapping("find-finance-payment")
	public ResponseEntity<Map<String, Object>> findFinancePayment(HttpServletRequest request, HttpServletResponse response,
			@RequestBody FinancePaymentData data ) {
		
		try {
			
			Map<String, Object> result = financialManagementService.findPaymentListByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("finance-payment")
	public ResponseEntity<Map<String, Object>> postFinancePayment (HttpServletRequest request, HttpServletResponse response,
			@RequestBody FinancePaymentData data) {
		try {
			AutUser userAction = utilityService.getActionUser(request);
			
			FinancePayment financePayment = mapperService.convertDataToEntity(data, FinancePayment.class, userAction);
			financePaymentRepository.save(financePayment);
			
			data = mapperService.convertToEntity(financePayment, FinancePaymentData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("finace-payment/{paymentId}")
	public ResponseEntity<Map<String, Object>> putFinancePayment(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "paymentId", required = true) Long paymentId,
			@RequestBody FinancePaymentData data) {
		try {
			FinancePayment financePayment = financePaymentRepository.findById(paymentId).orElse(null);
			if (null == financePayment) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			FinancePayment update = mapperService.convertDataToEntity4Update(data, FinancePayment.class, financePayment, userAction);
			update.setPaymentId(paymentId);
			
			financePaymentRepository.save(update);
			
			data = mapperService.convertToData(update, FinancePaymentData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("finace-payment/member-receipt-view-flag/{paymentId}")
	public ResponseEntity<Map<String, Object>> putFinancePaymentMemberReceiptViewFlag(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "paymentId", required = true) Long paymentId,
			@RequestBody FinancePaymentData data) {
		try {
			FinancePayment financePayment = financePaymentRepository.findById(paymentId).orElse(null);
			if (null == financePayment) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			FinancePayment update = financePayment.toBuilder().memberReceiptViewFlag(true).updateBy(userAction).updateDate(new Date()).build();
			financePaymentRepository.save(update);
			
			data = mapperService.convertToData(update, FinancePaymentData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("finance-payment/{paymentId}")
	public ResponseEntity<Map<String, Object>> getFinancePayment(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "paymentId", required = true) Long paymentId) {
		try {
			FinancePayment financePayment = financePaymentRepository.findById(paymentId).orElse(null);
			if(null == financePayment) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			FinancePaymentData data = mapperService.convertToData(financePayment, FinancePaymentData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-finance-import-log")
	public ResponseEntity<Map<String, Object>> findFinanceImportLog(HttpServletRequest request, HttpServletResponse response,
			@RequestBody FinanceImportLogData data ) {
	
		try {
			Map<String, Object> result = financialManagementService.findPaymentImportListByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("finance-import-log")
	public ResponseEntity<Map<String, Object>> postFinanceImportLog (HttpServletRequest request, HttpServletResponse response,
			@RequestBody FinanceImportLogData data) {
		try {
			AutUser userAction = utilityService.getActionUser(request);
			
			FinanceImportLog financeImportLog = mapperService.convertDataToEntity(data, FinanceImportLog.class, userAction);
			financeImportLogRepository.save(financeImportLog);
			
			data = mapperService.convertToEntity(financeImportLog, FinanceImportLogData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("finace-import-log/{impId}")
	public ResponseEntity<Map<String, Object>> putFinanceImportLog(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "impId", required = true) Long impId,
			@RequestBody FinanceImportLogData data) {
		try {
			FinanceImportLog financeImportLog = financeImportLogRepository.findById(impId).orElse(null);
			if (null == financeImportLog) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			FinanceImportLog update = mapperService.convertDataToEntity4Update(data, FinanceImportLog.class, financeImportLog, userAction);
			update.setImpId(impId);
			
			financeImportLogRepository.save(update);
			
			data = mapperService.convertToData(update, FinanceImportLogData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("finance-import-log/{impId}")
	public ResponseEntity<Map<String, Object>> getFinanceImportLog(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "impId", required = true) Long impId) {
		try {
			FinanceImportLog financeImportLog = financeImportLogRepository.findById(impId).orElse(null);
			if(null == financeImportLog) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			FinanceImportLogData data = mapperService.convertToData(financeImportLog, FinanceImportLogData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-finance-import-detail")
	public ResponseEntity<Map<String, Object>> findFinanceImportDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestBody FinanceImportDetailData data ) {
	
		try {
			Map<String, Object> result = financialManagementService.findPaymentImportDetailListByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("finance-import-detail")
	public ResponseEntity<Map<String, Object>> postFinanceImportDetail (HttpServletRequest request, HttpServletResponse response,
			@RequestBody FinanceImportDetailData data) {
		try {
			AutUser userAction = utilityService.getActionUser(request);
			
			FinanceImportDetail financeImportDetail = mapperService.convertDataToEntity(data, FinanceImportDetail.class, userAction);
			financeImportDetailRepository.save(financeImportDetail);
			
			data = mapperService.convertToEntity(financeImportDetail, FinanceImportDetailData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("finance-import-detail/{detailId}")
	public ResponseEntity<Map<String, Object>> putFinanceImportDetail(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "detailId", required = true) Long detailId,
			@RequestBody FinanceImportDetailData data) {
		try {
			FinanceImportDetail financeImportDetail = financeImportDetailRepository.findById(detailId).orElse(null);
			if (null == financeImportDetail) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			FinanceImportDetail update = mapperService.convertDataToEntity4Update(data, FinanceImportDetail.class, financeImportDetail, userAction);
			update.setImpId(detailId);
			
			financeImportDetailRepository.save(update);
			
			data = mapperService.convertToData(update, FinanceImportDetailData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("finance-import-detail/{detailId}")
	public ResponseEntity<Map<String, Object>> getFinanceImportDetail(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "detailId", required = true) Long detailId) {
		try {
			FinanceImportDetail financeImportDetail = financeImportDetailRepository.findById(detailId).orElse(null);
			if(null == financeImportDetail) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			FinanceImportDetailData data = mapperService.convertToData(financeImportDetail, FinanceImportDetailData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-finance-receipt-config")
	public ResponseEntity<Map<String, Object>> findFinanceReceiptConfig(HttpServletRequest request, HttpServletResponse response,
			@RequestBody FinanceReceiptConfigData data ) {
	
		try {
			Map<String, Object> result = financialManagementService.findFinanceReceiptConfigByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("finance-receipt-config")
	public ResponseEntity<Map<String, Object>> postFinanceReceiptConfig (HttpServletRequest request, HttpServletResponse response,
			@RequestBody FinanceReceiptConfigData data) {
		try {
			AutUser userAction = utilityService.getActionUser(request);
			
			FinanceReceiptConfig financeReceiptConfig = mapperService.convertDataToEntity(data, FinanceReceiptConfig.class, userAction);
			financeReceiptConfigRepository.save(financeReceiptConfig);
			
			data = mapperService.convertToEntity(financeReceiptConfig, FinanceReceiptConfigData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("finance-receipt-config/{receiptConfigId}")
	public ResponseEntity<Map<String, Object>> putFinanceReceiptConfig(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "receiptConfigId", required = true) Long receiptConfigId,
			@RequestBody FinanceReceiptConfigData data) {
		try {
			FinanceReceiptConfig financeReceiptConfig = financeReceiptConfigRepository.findById(receiptConfigId).orElse(null);
			if (null == financeReceiptConfig) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			FinanceReceiptConfig update = mapperService.convertDataToEntity4Update(data, FinanceReceiptConfig.class, financeReceiptConfig, userAction);
			update.setReceiptConfigId(receiptConfigId);
			
			financeReceiptConfigRepository.save(update);
			
			data = mapperService.convertToData(update, FinanceReceiptConfigData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("finance-receipt-config")
	public ResponseEntity<Map<String, Object>> getFinanceReceiptConfig(HttpServletRequest request, HttpServletResponse response) {
		try {
			FinanceReceiptConfig financeReceiptConfig = financeReceiptConfigRepository.findActive();
			if(null == financeReceiptConfig) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			FinanceReceiptConfigData data = mapperService.convertToData(financeReceiptConfig, FinanceReceiptConfigData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("find-tmp-finance-import-detail")
	public ResponseEntity<Map<String, Object>> findTmpFinanceImportDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestBody TmpFinanceImportDetailData data ) {
	
		try {
			Map<String, Object> result = financialManagementService.findTmpFinanceImportDetailByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("tmp-finance-import-detail")
	public ResponseEntity<Map<String, Object>> postTmpFinanceImportDetail (HttpServletRequest request, HttpServletResponse response,
			@RequestBody TmpFinanceImportDetailData data) {
		try {
			AutUser userAction = utilityService.getActionUser(request);
			
			TmpFinanceImportDetail tmpFinanceImportDetail = mapperService.convertDataToEntity(data, TmpFinanceImportDetail.class, userAction);
			tmpFinanceImportDetailRepository.save(tmpFinanceImportDetail);
			
			data = mapperService.convertToEntity(tmpFinanceImportDetail, TmpFinanceImportDetailData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("tmp-finance-import-detail/{detailId}")
	public ResponseEntity<Map<String, Object>> putTmpFinanceImportDetail(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "detailId", required = true) Long detailId,
			@RequestBody TmpFinanceImportDetailData data) {
		try {
			TmpFinanceImportDetail tmpFinanceImportDetail = tmpFinanceImportDetailRepository.findById(detailId).orElse(null);
			if (null == tmpFinanceImportDetail) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			TmpFinanceImportDetail update = mapperService.convertDataToEntity4Update(data, TmpFinanceImportDetail.class, tmpFinanceImportDetail, userAction);
			update.setDetailId(detailId);
			
			tmpFinanceImportDetailRepository.save(update);
			
			data = mapperService.convertToData(update, TmpFinanceImportDetailData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("tmp-finance-import-detail/{detailId}")
	public ResponseEntity<Map<String, Object>> getTmpFinanceImportDetail(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "detailId", required = true) Long detailId) {
		try {
			TmpFinanceImportDetail tmpFinanceImportDetail = tmpFinanceImportDetailRepository.findById(detailId).orElse(null);
			if(null == tmpFinanceImportDetail) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			TmpFinanceImportDetailData data = mapperService.convertToData(tmpFinanceImportDetail, TmpFinanceImportDetailData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("find-tmp-finance-import-log")
	public ResponseEntity<Map<String, Object>> findTmpFinanceImportLog(HttpServletRequest request, HttpServletResponse response,
			@RequestBody TmpFinanceImportLogData data ) {
	
		try {
			Map<String, Object> result = financialManagementService.findTmpFinanceImportLogByCondition(data);
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping("tmp-finance-import-log")
	public ResponseEntity<Map<String, Object>> postTmpFinanceImportLog (HttpServletRequest request, HttpServletResponse response,
			@RequestBody TmpFinanceImportLogData data) {
		try {
			AutUser userAction = utilityService.getActionUser(request);
			
			TmpFinanceImportLog tmpFinanceImportLog = mapperService.convertDataToEntity(data, TmpFinanceImportLog.class, userAction);
			
			if (null == tmpFinanceImportLog) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			if (null == tmpFinanceImportLog.getCoursepublicId()) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), "ไม่พบรอบคอร์ส"), HttpStatus.OK);
			}
			
			tmpFinanceImportLog.setUuid(UUID.randomUUID().toString());
			tmpFinanceImportLogRepository.save(tmpFinanceImportLog);
			
			data = mapperService.convertToEntity(tmpFinanceImportLog, TmpFinanceImportLogData.class);
			
			jmsSender.sendMessage(Q_IMPORT_PAYMENT, data.getTmpImpId());
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PutMapping("tmp-finance-import-log/{tmpImpId}")
	public ResponseEntity<Map<String, Object>> putTmpFinanceImportLog(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "tmpImpId", required = true) Long tmpImpId,
			@RequestBody TmpFinanceImportLogData data) {
		try {
			TmpFinanceImportLog tmpFinanceImportLog = tmpFinanceImportLogRepository.findById(tmpImpId).orElse(null);
			if (null == tmpFinanceImportLog) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			AutUser userAction = utilityService.getActionUser(request);
			
			TmpFinanceImportLog update = mapperService.convertDataToEntity4Update(data, TmpFinanceImportLog.class, tmpFinanceImportLog, userAction);
			update.setTmpImpId(tmpImpId);
			
			tmpFinanceImportLogRepository.save(update);
			
			data = mapperService.convertToData(update, TmpFinanceImportLogData.class);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@GetMapping("tmp-finance-import-log/{tmpImpId}")
	public ResponseEntity<Map<String, Object>> getTmpFinanceImportLog(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "tmpImpId", required = true) Long tmpImpId) {
		try {
			TmpFinanceImportLog tmpFinanceImportLog = tmpFinanceImportLogRepository.findById(tmpImpId).orElse(null);
			if(null == tmpFinanceImportLog) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			TmpFinanceImportLogData data = mapperService.convertToData(tmpFinanceImportLog, TmpFinanceImportLogData.class);
			
			CoursepublicMain cm = coursepublicMainRepository.findById(data.getCoursepublicId()).orElse(null);
			if (null != cm) {
				data.setCoursepublicMainData(mapperService.convertToData(cm, CoursepublicMainData.class));
			}
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_SEARCH_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	

	@PutMapping("tmp-finance-import-log/commit/{tmpImpId}")
	public ResponseEntity<Map<String, Object>> putTmpFinanceImportLogCommit(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "tmpImpId", required = true) Long tmpImpId,
			@RequestBody TmpFinanceImportLogData data) {
		try {
			TmpFinanceImportLog tmpFinanceImportLog = tmpFinanceImportLogRepository.findById(tmpImpId).orElse(null);
			if (null == tmpFinanceImportLog) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			jmsSender.sendMessage(Q_CONFIRM_PAYMENT, tmpImpId);
			
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
    @PutMapping("finace-payment/void/{paymentId}")
    public ResponseEntity<Map<String, Object>> putFinancePaymentVoid(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(name = "paymentId", required = true) Long paymentId,
			@RequestBody FinancePaymentData data) {
		try {
			
			FinancePayment financePaymentData = financePaymentRepository.findById(paymentId).orElse(null);
			if (null == financePaymentData) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_EDIT_DATA_NOTFOUND), HttpStatus.OK);
			}
			AutUser userAction = utilityService.getActionUser(request);
			financePaymentData.setPaymentId(paymentId);
			financePaymentData.setPaymentStatus(data.getPaymentStatus());
                        financePaymentData.setUpdateBy(userAction);
                        financePaymentData.setUpdateDate(new Date());
                        
			if(data.getStudyStatus()!=null){
                MemberCourse memberCourseData = memberCourseRepository.findById(financePaymentData.getMemberCourseId()).orElse(null);
                memberCourseData.setUpdateBy(userAction);
                memberCourseData.setUpdateDate(new Date());
                memberCourseData.setStudyStatus(data.getStudyStatus());
                memberCourseRepository.save(memberCourseData);
            }
			
            financePaymentRepository.save(financePaymentData);
                        
			data = mapperService.convertToData(financePaymentData, FinancePaymentData.class);
                        
			return new ResponseEntity<>(CommonUtils.response(data, MSG_EDIT_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

}
