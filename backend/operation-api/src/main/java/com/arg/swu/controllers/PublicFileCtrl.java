package com.arg.swu.controllers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.commons.ApiConstant;
import static com.arg.swu.commons.ApiConstant.ENTRIES;
import static com.arg.swu.commons.ApiConstant.MSG_SEARCH_SUCCESS;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.CoursepublicMainData;
import com.arg.swu.dto.MemberCourseData;
import com.arg.swu.dto.UploadFileData;
import com.arg.swu.models.AutUser;
import com.arg.swu.models.commons.PdfFileDownloadReponse;
import com.arg.swu.report.AdminReport;
import com.arg.swu.report.JasperReportType;
import com.arg.swu.services.FileStorageService;
import com.arg.swu.services.PrintReceiptService;
import com.arg.swu.services.UtilityService;

import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;

/**
 * 
 * @author sitthichaim
 *
 */
@RestController
@RequestMapping("publicfile")
@RequiredArgsConstructor
@Slf4j
public class PublicFileCtrl implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(PublicFileCtrl.class);
	
	private final FileStorageService fileStorageService;
	
        private final UtilityService utilityService;
        
        private final PrintReceiptService printReceiptService;
        
	private final JdbcTemplate jdbcTemplate;
	
	@GetMapping("testreport")
	public ResponseEntity<ByteArrayResource> testreport(HttpServletRequest request, HttpServletResponse response)
	{
		
		try {
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
			BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
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
			adminReport.getParams().put("backgroundBorder", backgroundBorder);
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
	
	
	
	@GetMapping("virtualtranscript")
	public ResponseEntity<ByteArrayResource> virtualtranscript(HttpServletRequest request, HttpServletResponse response)
		{
		try {
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
			BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
			BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signature.png"));
			
			AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
			.reportName("/com/arg/swu/jrxml/virtualTranscript.jrxml")
			.reportType(JasperReportType.PDF)
			.subReport(new HashMap<>())
			.params(new HashMap<>())
			.build();
			
			// adminReport.addSubReport("reportCourse","/com/arg/swu/jrxml/course.jrxml");
			// adminReport.addSubReport("reportSkill","/com/arg/swu/jrxml/skill.jrxml");
		
			adminReport.getParams().put("lang", "th");
			adminReport.getParams().put("logo", image);
			adminReport.getParams().put("background", background);
			adminReport.getParams().put("backgroundBorder", backgroundBorder);
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
	
	@GetMapping("experiencetranscript")
	public ResponseEntity<ByteArrayResource> experiencetranscript(HttpServletRequest request, HttpServletResponse response)
		{
		try {
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
			BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
			BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signature.png"));
			
			AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
			.reportName("/com/arg/swu/jrxml/experienceTranscript.jrxml")
			.reportType(JasperReportType.PDF)
			.subReport(new HashMap<>())
			.params(new HashMap<>())
			.build();
			
			// adminReport.addSubReport("reportCourse","/com/arg/swu/jrxml/course.jrxml");
			// adminReport.addSubReport("reportSkill","/com/arg/swu/jrxml/skill.jrxml");
		
			adminReport.getParams().put("lang", "th");
			adminReport.getParams().put("logo", image);
			adminReport.getParams().put("background", background);
			adminReport.getParams().put("backgroundBorder", backgroundBorder);
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
	
	@GetMapping("learningOutcome")
	public ResponseEntity<ByteArrayResource> learningOutcome(HttpServletRequest request, HttpServletResponse response)
		{
		try {
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
			BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
			BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signature.png"));
			
			AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
			.reportName("/com/arg/swu/jrxml/learningOutcomes.jrxml")
			.reportType(JasperReportType.PDF)
			.subReport(new HashMap<>())
			.params(new HashMap<>())
			.build();
			
			// adminReport.addSubReport("reportCourse","/com/arg/swu/jrxml/course.jrxml");
			// adminReport.addSubReport("reportSkill","/com/arg/swu/jrxml/skill.jrxml");
		
			adminReport.getParams().put("lang", "th");
			adminReport.getParams().put("logo", image);
			adminReport.getParams().put("background", background);
			adminReport.getParams().put("backgroundBorder", backgroundBorder);
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
	
	@GetMapping("simulatevirtualtranscript/course/member/{memberId}")
	public ResponseEntity<ByteArrayResource> simulatevirtualtranscript(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "memberId", required = true) Long memberId
			)
		{
		try {
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/SWU_Logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
			BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
			BufferedImage holderbg = ImageIO.read(getClass().getResource("/com/arg/swu/image/holder.png"));
			
			HashMap params = new HashMap<>();
			params.put("memberId", memberId);
			
			AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
			.reportName("/com/arg/swu/jrxml/simVirtualTranscript.jrxml")
			.reportType(JasperReportType.PDF)
			.subReport(new HashMap<>())
			.params(params)
			.build();
			
			// adminReport.addSubReport("reportCourse","/com/arg/swu/jrxml/course.jrxml");
			// adminReport.addSubReport("reportSkill","/com/arg/swu/jrxml/skill.jrxml");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",new java.util.Locale("th", "TH"));
			adminReport.getParams().put("lang", "th");
			adminReport.getParams().put("logo", image);
			adminReport.getParams().put("background", background);
			adminReport.getParams().put("backgroundBorder", backgroundBorder);
			adminReport.getParams().put("holderbg", holderbg);
			adminReport.getParams().put("dateFormat",dateFormat.format(new Date()));

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
	
	@GetMapping("simulateexperiencetranscript/course/member/{memberId}")
	public ResponseEntity<ByteArrayResource> simulateexperiencetranscript(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "memberId", required = true) Long memberId
			)
		{
		try {
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/SWU_Logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
			BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
			BufferedImage holderbg = ImageIO.read(getClass().getResource("/com/arg/swu/image/holder.png"));
			
			HashMap params = new HashMap<>();
			params.put("memberId", memberId);
			
			AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
			.reportName("/com/arg/swu/jrxml/simExperienceTranscript.jrxml")
			.reportType(JasperReportType.PDF)
			.subReport(new HashMap<>())
			.params(params)
			.build();
			
			// adminReport.addSubReport("reportCourse","/com/arg/swu/jrxml/course.jrxml");
			// adminReport.addSubReport("reportSkill","/com/arg/swu/jrxml/skill.jrxml");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",new java.util.Locale("th", "TH"));
			adminReport.getParams().put("lang", "th");
			adminReport.getParams().put("logo", image);
			adminReport.getParams().put("background", background);
			adminReport.getParams().put("backgroundBorder", backgroundBorder);
			adminReport.getParams().put("holderbg", holderbg);
			adminReport.getParams().put("dateFormat",dateFormat.format(new Date()));

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
	
	@GetMapping("virtualtranscript/memberId/{memberId}")
	public ResponseEntity<ByteArrayResource> virtualtranscriptTest(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "memberId", required = true) Long memberId)
		{
		try {
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
			BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
			BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signature.png"));
			
			HashMap params = new HashMap<>();
			params.put("memberId", memberId);
			
			StringBuilder sb = new StringBuilder();
            List<Object> param = new ArrayList<>();
			sb.append(" select cm.course_code , cm.course_name_th  ,cm.course_name_en , cpm.course_id, mi.member_id, mi.member_no , mi.credit_bank,(SELECT string_agg(concat(ss.subject_code_th, ' ', ss.subject_name_th), ', ') \n"+
				    " FROM course_matching cm2 \n"+
				    " join swu_subject ss on cm2.subject_swu_id = ss.subject_swu_id and cm2.curriculum_swu_id = ss.curriculum_swu_id \n"+
				    " WHERE cm2.course_id = cpm.course_id) as subject_code_th, \n"+
				    " (SELECT string_agg(concat(ss.subject_code_en, ' ', ss.subject_name_en), ', ') \n"+
				    " FROM course_matching cm2 \n"+
				    " join swu_subject ss on cm2.subject_swu_id = ss.subject_swu_id and cm2.curriculum_swu_id = ss.curriculum_swu_id \n"+
				    " WHERE cm2.course_id = cpm.course_id) as subject_code_en, \n"+
				    " cpm.course_class_start, mc.pass_date, cm.credit_amount , mc.result_grade, mc.member_course_id, sum(cm.credit_amount) as total_credit, mi.member_firstname_th, \n"+
				    " mi.member_lastname_th, mi.member_firstname_en, mi.member_lastname_en, mi.member_email \n"+
				" from coursepublic_main cpm \n"+
				" join course_main cm on cpm.course_id = cm.course_id \n"+
				" join member_course mc on mc.coursepublic_id = cpm.coursepublic_id \n"+
				" join member_info mi on mc.member_id = mi.member_id \n"+
				" where mc.member_id = ? and mc.pass_status=true and exists(select * from course_matching cm3 where cm3.course_id=cm.course_id)\n"+
				" group by cm.course_code , cm.course_name_th  ,cm.course_name_en , cpm.course_id, subject_code_th, subject_code_en, cpm.course_class_start, mc.pass_date , cm.credit_amount , \n"+
				" mc.result_grade, mc.member_course_id, mi.member_firstname_th, mi.member_lastname_th, mi.member_firstname_en, mi.member_lastname_en, mi.member_email, mi.member_id, mi.credit_bank");
			 param.add(memberId);
			
			 List<CoursepublicMainData> coursepublicMainData = jdbcTemplate.query(sb.toString(),BeanPropertyRowMapper.newInstance(CoursepublicMainData.class), param.toArray());
	         List<MemberCourseData> memberCourseData = jdbcTemplate.query(sb.toString(),BeanPropertyRowMapper.newInstance(MemberCourseData.class), param.toArray());
	            
			AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
			.reportName("/com/arg/swu/jrxml/virtualTranscript.jrxml")
			.reportType(JasperReportType.PDF)
			.subReport(new HashMap<>())
			.params(params)
			.build();
			
			// adminReport.addSubReport("reportCourse","/com/arg/swu/jrxml/course.jrxml");
			// adminReport.addSubReport("reportSkill","/com/arg/swu/jrxml/skill.jrxml");
	           
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",new java.util.Locale("th", "TH"));
			adminReport.getParams().put("lang", "th");
			adminReport.getParams().put("logo", image);
			adminReport.getParams().put("background", background);
			adminReport.getParams().put("backgroundBorder", backgroundBorder);
			adminReport.getParams().put("signatureImg", signatureImg);
			adminReport.getParams().put("dateFormat", dateFormat.format(new Date()));
			adminReport.getParams().put("startDate",dateFormat.format(coursepublicMainData.get(0).getCourseClassStart()));
			adminReport.getParams().put("passDate",dateFormat.format(memberCourseData.get(0).getPassDate()));
			
			

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
	
	@GetMapping("certificate/memberCourseId/{memberCourseId}")
	public ResponseEntity<ByteArrayResource> certificate(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(name = "memberCourseId", required = true) Long memberCourseId
			)
		{
		try {
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/SWU_Logo.png"));
			BufferedImage bgcert = ImageIO.read(getClass().getResource("/com/arg/swu/image/certificate-bg.jpg"));
			
			HashMap params = new HashMap<>();
			params.put("memberCourseId", memberCourseId);
			
			AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
			.reportName("/com/arg/swu/jrxml/verifycert.jrxml")
			.reportType(JasperReportType.PDF)
			.subReport(new HashMap<>())
			.params(params)
			.build();
			
			// adminReport.addSubReport("reportCourse","/com/arg/swu/jrxml/course.jrxml");
			// adminReport.addSubReport("reportSkill","/com/arg/swu/jrxml/skill.jrxml");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy",new java.util.Locale("th", "TH"));
			adminReport.getParams().put("lang", "th");
			adminReport.getParams().put("logo", image);
			adminReport.getParams().put("bgcert", bgcert);
			adminReport.getParams().put("formatDate",dateFormat.format(new Date()));

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

	
        
    @GetMapping("updateregis/{paymentId}")
	public ResponseEntity<Map<String, Object>> updateRegisPayment(HttpServletRequest request, HttpServletResponse response,@PathVariable(name = "paymentId", required = true) Long paymentId)
        {
            try {
                LOG.info(">>>>updateRegisPayment<<<<");
                AutUser userAction = utilityService.getActionUser(request);
                Map<String, Object> result = printReceiptService.updateRegisPayment(userAction, paymentId);
                return new ResponseEntity<>(CommonUtils.response(null, MSG_CREATE_SUCCESS, null), HttpStatus.OK);
            } catch (Exception e) {
		LOG.error("=================[ getFile ]===============");
		LOG.error(e.getMessage(), e);
		return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
            }
        }

}
