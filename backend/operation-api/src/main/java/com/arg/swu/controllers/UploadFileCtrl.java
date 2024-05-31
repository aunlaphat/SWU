package com.arg.swu.controllers;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.UploadFileData;
import com.arg.swu.services.FileStorageService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@RestController
@RequestMapping("upload")
@RequiredArgsConstructor
public class UploadFileCtrl implements ApiConstant {

	private static final Logger LOG = LogManager.getLogger(UploadFileCtrl.class);
	
	private final FileStorageService fileStorageService;
	
	@PostMapping(value = "/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> postBanner(HttpServletRequest request, HttpServletResponse response,
			@RequestPart("file") MultipartFile file) {
		try {
			
			if (null == file) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			UploadFileData uploadFileData = fileStorageService.saveFileToStorage(file, 1);
			return new ResponseEntity<>(CommonUtils.response(uploadFileData, MSG_UPLOAD_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("=================[ postBanner ]===============");
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
	@PostMapping(value = "/course", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> postCourse(HttpServletRequest request, HttpServletResponse response,
			@RequestPart("file") MultipartFile file) {
		try {
			
			if (null == file) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			UploadFileData uploadFileData = fileStorageService.saveFileToStorage(file, 2);
			return new ResponseEntity<>(CommonUtils.response(uploadFileData, MSG_UPLOAD_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("=================[ postCourse ]===============");
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping(value = "/coursepublic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> postCoursepublic(HttpServletRequest request, HttpServletResponse response,
			@RequestPart("file") MultipartFile file) {
		try {
			
			if (null == file) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			UploadFileData uploadFileData = fileStorageService.saveFileToStorage(file, 3);
			return new ResponseEntity<>(CommonUtils.response(uploadFileData, MSG_UPLOAD_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("=================[ postCoursepublic ]===============");
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping(value = "/master-document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> postMasterDocument(HttpServletRequest request, HttpServletResponse response,
			@RequestPart("file") MultipartFile file) {
		try {
			
			if (null == file) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			UploadFileData uploadFileData = fileStorageService.saveFileToStorage(file, 4);
			return new ResponseEntity<>(CommonUtils.response(uploadFileData, MSG_UPLOAD_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("=================[ postMasterDocument ]===============");
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping(value = "/master-financial", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> postMasterFinancial(HttpServletRequest request, HttpServletResponse response,
			@RequestPart("file") MultipartFile file) {
		try {
			
			if (null == file) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			UploadFileData uploadFileData = fileStorageService.saveFileToStorage(file, 5);
			return new ResponseEntity<>(CommonUtils.response(uploadFileData, MSG_UPLOAD_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("=================[ postMasterFinancial ]===============");
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping(value = "/member", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> postMember(HttpServletRequest request, HttpServletResponse response,
			@RequestPart("file") MultipartFile file) {
		try {
			
			if (null == file) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			UploadFileData uploadFileData = fileStorageService.saveFileToStorage(file, 6);
			return new ResponseEntity<>(CommonUtils.response(uploadFileData, MSG_UPLOAD_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("=================[ postMember ]===============");
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping(value = "/personal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> postPersonal(HttpServletRequest request, HttpServletResponse response,
			@RequestPart("file") MultipartFile file) {
		try {
			
			if (null == file) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			UploadFileData uploadFileData = fileStorageService.saveFileToStorage(file, 7);
			return new ResponseEntity<>(CommonUtils.response(uploadFileData, MSG_UPLOAD_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("=================[ postPersonal ]===============");
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping(value = "/receipt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> postReceipt(HttpServletRequest request, HttpServletResponse response,
			@RequestPart("file") MultipartFile file) {
		try {
			
			if (null == file) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			UploadFileData uploadFileData = fileStorageService.saveFileToStorage(file, 8);
			return new ResponseEntity<>(CommonUtils.response(uploadFileData, MSG_UPLOAD_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("=================[ postReceipt ]===============");
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping(value = "/system", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> postSystem(HttpServletRequest request, HttpServletResponse response,
			@RequestPart("file") MultipartFile file) {
		try {
			
			if (null == file) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			UploadFileData uploadFileData = fileStorageService.saveFileToStorage(file, 9);
			return new ResponseEntity<>(CommonUtils.response(uploadFileData, MSG_UPLOAD_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("=================[ postSystem ]===============");
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
        
        @PostMapping(value = "/news", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, Object>> postNews(HttpServletRequest request, HttpServletResponse response,
			@RequestPart("file") MultipartFile file) {
		try {
			
			if (null == file) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			UploadFileData uploadFileData = fileStorageService.saveFileToStorage(file, 10);
			return new ResponseEntity<>(CommonUtils.response(uploadFileData, MSG_UPLOAD_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("=================[ postSystem ]===============");
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
}
