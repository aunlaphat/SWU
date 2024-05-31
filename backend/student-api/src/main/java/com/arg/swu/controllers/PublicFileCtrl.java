package com.arg.swu.controllers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.dto.UploadFileData;
import com.arg.swu.models.commons.PdfFileDownloadReponse;
import com.arg.swu.report.AdminReport;
import com.arg.swu.report.JasperReportType;
import com.arg.swu.services.FileStorageService;

import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@RestController
@RequestMapping("publicfile")
@RequiredArgsConstructor
public class PublicFileCtrl implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(PublicFileCtrl.class);
	
	private final FileStorageService fileStorageService;
	
	private final JdbcTemplate jdbcTemplate;
	
	@GetMapping("testreport")
	public ResponseEntity<ByteArrayResource> testreport(HttpServletRequest request, HttpServletResponse response)
	{
		
		try {
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));

			BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signature.png"));
			
			AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
			.reportName("/com/arg/swu/jrxml/example.jrxml")
			.reportType(JasperReportType.PDF)
			.subReport(new HashMap<>())
			.params(new HashMap<>())
			.build();
			// adminReport.addSubReport("reportCourse","/com/arg/swu/jrxml/course.jrxml");
			// adminReport.addSubReport("reportSkill","/com/arg/swu/jrxml/skill.jrxml");
			adminReport.getParams().put("lang", "th");
			
			adminReport.getParams().put("logo", image);
			adminReport.getParams().put("background", background);
			adminReport.getParams().put("signatureImg", signatureImg);

			byte[] result = adminReport.execDatasource();

			PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(result)
			.fileName("example.pdf")
			.build();
			

			return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(getContentType(reponse.getFileName())))
                    .header(getContentDisposition(reponse.getFileName()))
                    .body(new ByteArrayResource(reponse.getDatas()));


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
		
	}

	public Object responseFile(HttpServletResponse response, PdfFileDownloadReponse fileDownloadInfo) throws IOException {
     
        if(fileDownloadInfo.getDatas() != null) {
            response.setContentType(getContentType(fileDownloadInfo.getFileName()));
            response.setHeader("Content-Disposition", getContentDisposition(fileDownloadInfo.getFileName()));

            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.write(fileDownloadInfo.getDatas());
                outputStream.flush();
            }
        }
        return null;
    }

	public static String getContentType(String fileName) throws IOException {
        Path path = new File(fileName).toPath();
        return Files.probeContentType(path);
    }

	 public static String getContentDisposition(String fileName) throws UnsupportedEncodingException {
        String encodedFileName = URLEncoder.encode(fileName,"UTF-8");
        return "attachment;filename*=UTF-8''"+encodedFileName+";";
    }

	@GetMapping("/{prefix}/{module}/{filename}")
	public ResponseEntity<ByteArrayResource> getFile(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "prefix", required = true) String prefix,
			@PathVariable(name = "module", required = true) Long module,
			@PathVariable(name = "filename", required = true) String filename) {
		try {
			
			UploadFileData data = UploadFileData.builder().prefix(prefix).module(module).filename(filename).build();
                    
			if (null == data) {
				return null;
				// return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			if ("..".contains(data.getFilename()) || "..".contains(data.getPrefix())) {
				return null;
				// return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			if (!fileStorageService.checkLinkFileIsExistsByModel(data)) {
				return null;
				// return new ResponseEntity<>(CommonUtils.responseByStatus(HttpStatus.NO_CONTENT.value(), MSG_DATA_NOTFOUND), HttpStatus.OK);
			}
			
			byte[] vals = fileStorageService.getContentfilePublic(data);
			
			if (null == vals) return null;
			
			String extensoin = (null == FilenameUtils.getExtension(data.getFilename()) ? "txt" : FilenameUtils.getExtension(data.getFilename()));
			
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/"+ extensoin))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + data.getFilename() + "\"")
                    .body(new ByteArrayResource(vals));
            
		} catch (Exception e) {
			LOG.error("=================[ getFile ]===============");
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

}
