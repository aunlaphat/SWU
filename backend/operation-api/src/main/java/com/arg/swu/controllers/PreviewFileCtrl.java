package com.arg.swu.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;

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
@RequestMapping("preview")
@RequiredArgsConstructor
public class PreviewFileCtrl implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(PreviewFileCtrl.class);
	
	private final FileStorageService fileStorageService;
	
	@PostMapping("file")
	public ResponseEntity<Map<String, Object>> postFile(HttpServletRequest request, HttpServletResponse response,
			@RequestBody UploadFileData data) {
		try {
			
			if (null == data) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			if ("..".contains(data.getFilename()) || "..".contains(data.getPrefix())) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			if (!fileStorageService.checkLinkFileIsExistsByModel(data)) {
				return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			UploadFileData uploadFileData = fileStorageService.getUploadFileData(data);
			return new ResponseEntity<>(CommonUtils.response(uploadFileData, MSG_UPLOAD_SUCCESS, null), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("=================[ postFile ]===============");
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("path")
	public ResponseEntity<ByteArrayResource> getFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name = "fullpathname", required = true) String fullpathname) {
		try {
			
			// UploadFileData data = UploadFileData.builder().fullpath(fullpathname).build();
			LOG.info("Get File "+ fullpathname);
                    
			if (null == fullpathname) {
				return null;
				// return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			if ("..".contains(fullpathname)) {
				return null;
				// return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			if (!fileStorageService.checkLinkFileIsExists(fullpathname)) {
				return null;
				// return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			byte[] vals = fileStorageService.getContentfilePublic(fullpathname);
			
			if (null == vals) return null;
			
			// String extensoin = (null == FilenameUtils.getExtension(fullpathname) ? "txt" : FilenameUtils.getExtension(fullpathname));
			
            // return ResponseEntity.ok()
            //         .contentType(MediaType.parseMediaType("image/"+ extensoin))
            //         .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fullpathname + "\"")
            //         .body(new ByteArrayResource(vals));
			return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(getContentType(fullpathname)))
				.header(getContentDisposition(fullpathname))
				.body(new ByteArrayResource(vals));

		} catch (Exception e) {
			LOG.error("=================[ getFile ]===============");
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	public static String getContentDisposition(String fileName) throws UnsupportedEncodingException {
        String encodedFileName = URLEncoder.encode(fileName,"UTF-8");
        return "attachment;filename*=UTF-8''"+encodedFileName+";";
    }

	public static String getContentType(String fileName) throws IOException {
        Path path = new File(fileName).toPath();
        return Files.probeContentType(path);
    }

}
