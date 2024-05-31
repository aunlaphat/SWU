package com.arg.swu.services;

import java.awt.image.BufferedImage;
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

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.arg.swu.dto.CoursepublicMainData;
import com.arg.swu.dto.MemberCourseData;
import com.arg.swu.dto.UploadFileData;
import com.arg.swu.models.commons.PdfFileDownloadReponse;
import com.arg.swu.report.AdminReport;
import com.arg.swu.report.JasperReportType;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class VirtualTranscriptReportService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private FileStorageService fileStorageService;
	
	public byte[] generateExperienceTh(Long memberId, String qrcode) throws Exception
	{
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/SWU_Logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
			BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
			BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signature.png"));
			
			HashMap params = new HashMap<>();
			params.put("memberId", memberId);
			params.put("qrcode", qrcode);
			
			StringBuilder sb = new StringBuilder();
            List<Object> paramMc = new ArrayList<>();
            sb.append(" select row_number() over() rn,mc.member_id , cm.course_code ,cm.credit_amount, cm.course_name_th  ,cm.course_name_en , ");
            sb.append(" cpm.course_id,(SELECT string_agg(concat( ss.subject_code_th,'-',ss.subject_set), ', ') ");
            sb.append(" FROM course_matching cm2 ");
            sb.append(" join swu_subject ss on cm2.subject_swu_id = ss.subject_swu_id and cm2.curriculum_swu_id = ss.curriculum_swu_id and mc.pass_status=true");
            sb.append(" WHERE cm2.course_id = cpm.course_id) as subject_code_th, ");
            sb.append(" (SELECT string_agg(concat(ss.subject_code_en,'-',ss.subject_set), ', ' ) ");
            sb.append(" FROM course_matching cm2 ");
            sb.append(" join swu_subject ss on cm2.subject_swu_id = ss.subject_swu_id and cm2.curriculum_swu_id = ss.curriculum_swu_id and mc.pass_status=true ");
            sb.append(" WHERE cm2.course_id = cpm.course_id) as subject_code_en, ");
            sb.append(" mc.pass_date , cm.course_total_h ");
            sb.append(" from coursepublic_main cpm ");
            sb.append(" join course_main cm on cpm.course_id = cm.course_id ");
            sb.append(" join member_course mc on mc.coursepublic_id = cpm.coursepublic_id ");
            sb.append(" join finance_payment fp on fp.member_course_id = mc.member_course_id ");
            sb.append(" where fp.payment_status='30033002' and mc.member_id = ? ");
			paramMc.add(memberId);
			
			List<MemberCourseData> memberCourseData = jdbcTemplate.query(sb.toString(),BeanPropertyRowMapper.newInstance(MemberCourseData.class), paramMc.toArray());  
			            
			AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
			.reportName("/com/arg/swu/jrxml/experienceTranscript.jrxml")
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
			adminReport.getParams().put("formatDate", dateFormat.format(new Date()));
			if (!memberCourseData.isEmpty()) {
				adminReport.getParams().put("passDate",dateFormat.format(memberCourseData.get(0).getPassDate()));
			}
			
			byte[] result = adminReport.execDatasource();

//			PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(result)
//			.fileName("example.pdf")
//			.build();
			
			return result;
	}
	
	public byte[] generateExperienceEn(Long memberId, String qrcode) throws Exception
	{
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/SWU_Logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
			BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
			BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signature.png"));
			
			HashMap params = new HashMap<>();
			params.put("memberId", memberId);
			params.put("qrcode", qrcode);
			
			StringBuilder sb = new StringBuilder();
            List<Object> paramMc = new ArrayList<>();
            sb.append(" select row_number() over() rn,mc.member_id , cm.course_code ,cm.credit_amount, cm.course_name_th  ,cm.course_name_en , ");
            sb.append(" cpm.course_id,(SELECT string_agg(concat( ss.subject_code_th,'-',ss.subject_set), ', ') ");
            sb.append(" FROM course_matching cm2 ");
            sb.append(" join swu_subject ss on cm2.subject_swu_id = ss.subject_swu_id and cm2.curriculum_swu_id = ss.curriculum_swu_id and mc.pass_status=true");
            sb.append(" WHERE cm2.course_id = cpm.course_id) as subject_code_th, ");
            sb.append(" (SELECT string_agg(concat(ss.subject_code_en,'-',ss.subject_set), ', ' ) ");
            sb.append(" FROM course_matching cm2 ");
            sb.append(" join swu_subject ss on cm2.subject_swu_id = ss.subject_swu_id and cm2.curriculum_swu_id = ss.curriculum_swu_id and mc.pass_status=true ");
            sb.append(" WHERE cm2.course_id = cpm.course_id) as subject_code_en, ");
            sb.append(" mc.pass_date , cm.course_total_h ");
            sb.append(" from coursepublic_main cpm ");
            sb.append(" join course_main cm on cpm.course_id = cm.course_id ");
            sb.append(" join member_course mc on mc.coursepublic_id = cpm.coursepublic_id ");
            sb.append(" join finance_payment fp on fp.member_course_id = mc.member_course_id ");
            sb.append(" where fp.payment_status='30033002' and mc.member_id = ? ");
			paramMc.add(memberId);
			
			List<MemberCourseData> memberCourseData = jdbcTemplate.query(sb.toString(),BeanPropertyRowMapper.newInstance(MemberCourseData.class), paramMc.toArray());
			            
			AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
			.reportName("/com/arg/swu/jrxml/experienceTranscript.jrxml")
			.reportType(JasperReportType.PDF)
			.subReport(new HashMap<>())
			.params(params)
			.build();
			
			// adminReport.addSubReport("reportCourse","/com/arg/swu/jrxml/course.jrxml");
			// adminReport.addSubReport("reportSkill","/com/arg/swu/jrxml/skill.jrxml");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",new java.util.Locale("en", "EN"));
			adminReport.getParams().put("lang", "en");
			adminReport.getParams().put("logo", image);
			adminReport.getParams().put("background", background);
			adminReport.getParams().put("backgroundBorder", backgroundBorder);
			adminReport.getParams().put("signatureImg", signatureImg);
			adminReport.getParams().put("formatDate", dateFormat.format(new Date()));
			if (!memberCourseData.isEmpty()) {
				adminReport.getParams().put("passDate",dateFormat.format(memberCourseData.get(0).getPassDate()));
			}
			
			byte[] result = adminReport.execDatasource();

//			PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(result)
//			.fileName("example.pdf")
//			.build();
			
			return result;
	}
	
	public byte[] generateLearningTh(Long memberId, String qrcode) throws Exception
	{
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/SWU_Logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
			BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
			BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signature.png"));
			
			HashMap params = new HashMap<>();
			params.put("memberId", memberId);
			params.put("qrcode", qrcode);
			
			AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
			.reportName("/com/arg/swu/jrxml/learningOutcomes.jrxml")
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
			
			byte[] result = adminReport.execDatasource();

//			PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(result)
//			.fileName("example.pdf")
//			.build();
			
			return result;
	}
	
	public byte[] generateLearningEn(Long memberId, String qrcode) throws Exception
	{
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/SWU_Logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
			BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
			BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signature.png"));
			
			HashMap params = new HashMap<>();
			params.put("memberId", memberId);
			params.put("qrcode", qrcode);
			
			AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
			.reportName("/com/arg/swu/jrxml/learningOutcomes.jrxml")
			.reportType(JasperReportType.PDF)
			.subReport(new HashMap<>())
			.params(params)
			.build();
			
			// adminReport.addSubReport("reportCourse","/com/arg/swu/jrxml/course.jrxml");
			// adminReport.addSubReport("reportSkill","/com/arg/swu/jrxml/skill.jrxml");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",new java.util.Locale("en", "En"));
			adminReport.getParams().put("lang", "en");
			adminReport.getParams().put("logo", image);
			adminReport.getParams().put("background", background);
			adminReport.getParams().put("backgroundBorder", backgroundBorder);
			adminReport.getParams().put("signatureImg", signatureImg);
			adminReport.getParams().put("dateFormat", dateFormat.format(new Date()));
			
			byte[] result = adminReport.execDatasource();

//			PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(result)
//			.fileName("example.pdf")
//			.build();
			
			return result;
	}
	
	public byte[] generateVirtualTh(Long memberId, String qrcode) throws Exception
	{
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/SWU_Logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
			BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
			BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signature.png"));
			
			HashMap params = new HashMap<>();
			params.put("memberId", memberId);
			params.put("qrcode", qrcode);
			
			StringBuilder sb = new StringBuilder();
            List<Object> param = new ArrayList<>();
			sb.append(" select cm.course_code , cm.course_name_th  ,cm.course_name_en , cpm.course_id, mi.member_id, mi.member_no , mi.credit_bank,(SELECT string_agg(concat(ss.subject_code_th, ' ', ss.subject_name_th), ', ') ");
			sb.append(" FROM course_matching cm2 ");
			sb.append(" join swu_subject ss on cm2.subject_swu_id = ss.subject_swu_id and cm2.curriculum_swu_id = ss.curriculum_swu_id ");
			sb.append(" WHERE cm2.course_id = cpm.course_id) as subject_code_th, ");
			sb.append(" (SELECT string_agg(concat(ss.subject_code_en, ' ', ss.subject_name_en), ', ') ");
			sb.append(" FROM course_matching cm2 ");
			sb.append(" join swu_subject ss on cm2.subject_swu_id = ss.subject_swu_id and cm2.curriculum_swu_id = ss.curriculum_swu_id ");
			sb.append(" WHERE cm2.course_id = cpm.course_id) as subject_code_en, ");
			sb.append(" cpm.course_class_start, mc.pass_date, cm.credit_amount , mc.result_grade, mc.member_course_id, sum(cm.credit_amount) as total_credit, mi.member_firstname_th, ");
			sb.append(" mi.member_lastname_th, mi.member_firstname_en, mi.member_lastname_en, mi.member_email ");
			sb.append(" from coursepublic_main cpm ");
			sb.append(" join course_main cm on cpm.course_id = cm.course_id ");
			sb.append(" join member_course mc on mc.coursepublic_id = cpm.coursepublic_id ");
			sb.append(" join member_info mi on mc.member_id = mi.member_id ");
			sb.append(" where mc.member_id = ? and mc.pass_status=true and exists(select * from course_matching cm3 where cm3.course_id=cm.course_id)");
			sb.append(" group by cm.course_code , cm.course_name_th  ,cm.course_name_en , cpm.course_id, subject_code_th, subject_code_en, cpm.course_class_start, mc.pass_date , cm.credit_amount , ");
			sb.append(" mc.result_grade, mc.member_course_id, mi.member_firstname_th, mi.member_lastname_th, mi.member_firstname_en, mi.member_lastname_en, mi.member_email, mi.member_id, mi.credit_bank ");
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
			if (!coursepublicMainData.isEmpty()) {
				adminReport.getParams().put("startDate",dateFormat.format(coursepublicMainData.get(0).getCourseClassStart()));
			}
			if (!memberCourseData.isEmpty()) {
				adminReport.getParams().put("passDate",dateFormat.format(memberCourseData.get(0).getPassDate()));
			}
			
	        
	        
			byte[] result = adminReport.execDatasource();

//			PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(result)
//			.fileName("example.pdf")
//			.build();
			
			return result;
	}
	
	public byte[] generateVirtualEn(Long memberId, String qrcode) throws Exception
	{
		
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/SWU_Logo.png"));
			BufferedImage background = ImageIO.read(getClass().getResource("/com/arg/swu/image/logo-30.png"));
			BufferedImage backgroundBorder = ImageIO.read(getClass().getResource("/com/arg/swu/image/backgroundborder.png"));
			BufferedImage signatureImg = ImageIO.read(getClass().getResource("/com/arg/swu/image/signature.png"));
			
			HashMap params = new HashMap<>();
			params.put("memberId", memberId);
			params.put("qrcode", qrcode);
			
			StringBuilder sb = new StringBuilder();
            List<Object> param = new ArrayList<>();
			sb.append(" select cm.course_code , cm.course_name_th  ,cm.course_name_en , cpm.course_id, mi.member_id, mi.member_no , mi.credit_bank,(SELECT string_agg(concat(ss.subject_code_th, ' ', ss.subject_name_th), ', ') ");
			sb.append(" FROM course_matching cm2 ");
			sb.append(" join swu_subject ss on cm2.subject_swu_id = ss.subject_swu_id and cm2.curriculum_swu_id = ss.curriculum_swu_id ");
			sb.append(" WHERE cm2.course_id = cpm.course_id) as subject_code_th, ");
			sb.append(" (SELECT string_agg(concat(ss.subject_code_en, ' ', ss.subject_name_en), ', ') ");
			sb.append(" FROM course_matching cm2 ");
			sb.append(" join swu_subject ss on cm2.subject_swu_id = ss.subject_swu_id and cm2.curriculum_swu_id = ss.curriculum_swu_id ");
			sb.append(" WHERE cm2.course_id = cpm.course_id) as subject_code_en, ");
			sb.append(" cpm.course_class_start, mc.pass_date, cm.credit_amount , mc.result_grade, mc.member_course_id, sum(cm.credit_amount) as total_credit, mi.member_firstname_th, ");
			sb.append(" mi.member_lastname_th, mi.member_firstname_en, mi.member_lastname_en, mi.member_email ");
			sb.append(" from coursepublic_main cpm ");
			sb.append(" join course_main cm on cpm.course_id = cm.course_id ");
			sb.append(" join member_course mc on mc.coursepublic_id = cpm.coursepublic_id ");
			sb.append(" join member_info mi on mc.member_id = mi.member_id ");
			sb.append(" where mc.member_id = ? and mc.pass_status=true and exists(select * from course_matching cm3 where cm3.course_id=cm.course_id) ");
			sb.append(" group by cm.course_code , cm.course_name_th  ,cm.course_name_en , cpm.course_id, subject_code_th, subject_code_en, cpm.course_class_start, mc.pass_date , cm.credit_amount , ");
			sb.append(" mc.result_grade, mc.member_course_id, mi.member_firstname_th, mi.member_lastname_th, mi.member_firstname_en, mi.member_lastname_en, mi.member_email, mi.member_id, mi.credit_bank ");
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
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",new java.util.Locale("en", "EN"));
			adminReport.getParams().put("lang", "en");
			adminReport.getParams().put("logo", image);
			adminReport.getParams().put("background", background);
			adminReport.getParams().put("backgroundBorder", backgroundBorder);
			adminReport.getParams().put("signatureImg", signatureImg);
			adminReport.getParams().put("dateFormat", dateFormat.format(new Date()));
			if (!coursepublicMainData.isEmpty()) {
				adminReport.getParams().put("startDate",dateFormat.format(coursepublicMainData.get(0).getCourseClassStart()));
			}
			if (!memberCourseData.isEmpty()) {
				adminReport.getParams().put("passDate",dateFormat.format(memberCourseData.get(0).getPassDate()));
			}
			
			byte[] result = adminReport.execDatasource();

//			PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(result)
//			.fileName("example.pdf")
//			.build();
			
			return result;
	}
	
	public byte[] generateCertTh(Long memberCourseId, String qrcode) throws Exception
	{
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/SWU_Logo.png"));
			BufferedImage bgcert = ImageIO.read(getClass().getResource("/com/arg/swu/image/certificate-bg.jpg"));
			
			HashMap params = new HashMap<>();
			params.put("memberCourseId", memberCourseId);
			params.put("qrcode", qrcode);
			
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

//			PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(result)
//			.fileName("example.pdf")
//			.build();
			
			return result;
	}
	
	public byte[] generateCertEn(Long memberCourseId, String qrcode) throws Exception
	{
			
			BufferedImage image = ImageIO.read(getClass().getResource("/com/arg/swu/image/SWU_Logo.png"));
			BufferedImage bgcert = ImageIO.read(getClass().getResource("/com/arg/swu/image/certificate-bg.jpg"));
			
			HashMap params = new HashMap<>();
			params.put("memberCourseId", memberCourseId);
			params.put("qrcode", qrcode);
			
			AdminReport adminReport = AdminReport.builder().conn(jdbcTemplate.getDataSource().getConnection())
			.reportName("/com/arg/swu/jrxml/verifycert.jrxml")
			.reportType(JasperReportType.PDF)
			.subReport(new HashMap<>())
			.params(params)
			.build();
			
			// adminReport.addSubReport("reportCourse","/com/arg/swu/jrxml/course.jrxml");
			// adminReport.addSubReport("reportSkill","/com/arg/swu/jrxml/skill.jrxml");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy",new java.util.Locale("en", "EN"));
			adminReport.getParams().put("lang", "en");
			adminReport.getParams().put("logo", image);
			adminReport.getParams().put("bgcert", bgcert);
			adminReport.getParams().put("formatDate",dateFormat.format(new Date()));
			
			byte[] result = adminReport.execDatasource();

//			PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(result)
//			.fileName("example.pdf")
//			.build();
			
			return result;
			
			 
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
			return null;
		}
	}
	
}
