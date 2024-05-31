package com.arg.swu.controllers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.dto.FinancePaymentData;
import com.arg.swu.dto.MemberCourseData;
import com.arg.swu.dto.ReportCanceledCourseData;
import com.arg.swu.dto.ReportCourseEnrollmentData;
import com.arg.swu.dto.ReportCoursePaymentData;
import com.arg.swu.dto.ReportDepartmentIncomeData;
import com.arg.swu.dto.ReportEnrollmentAndPaymentData;
import com.arg.swu.dto.ReportEnrollmentListData;
import com.arg.swu.dto.ReportOfferedCourseData;
import com.arg.swu.exportor.ExportDataProcess;
import com.arg.swu.exportor.provider.CanceledCourseExportProvider;
import com.arg.swu.exportor.provider.CourseEnrollmentExportProvider;
import com.arg.swu.exportor.provider.CoursePaymentExportProvider;
import com.arg.swu.exportor.provider.DepartmentIncomeExportProvider;
import com.arg.swu.exportor.provider.EnrollmentAndPaymentExportProvider;
import com.arg.swu.exportor.provider.EnrollmentListExportProvider;
import com.arg.swu.exportor.provider.OfferedCourseExportProvider;
import com.arg.swu.exportor.provider.PaymentDataExportProvider;
import com.arg.swu.exportor.provider.TeacherCourseGradeExportProvider;
import com.arg.swu.services.ReportService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@RestController
@RequestMapping("report")
@RequiredArgsConstructor
public class ReportCtrl implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(ReportCtrl.class);
	
	private final ReportService reportService;
	
    private final JdbcTemplate jdbcTemplate;
    
    
    public static String getContentType(String fileName) throws IOException {
        Path path = new File(fileName).toPath();
        return Files.probeContentType(path);
    }
	
	public static String getContentDisposition(String fileName) throws UnsupportedEncodingException {
        String encodedFileName = URLEncoder.encode(fileName,"UTF-8");
        return "attachment;filename*=UTF-8''"+encodedFileName+";";
    }
	
	
// report 1
	@PostMapping("find-offered-course-report")
	public ResponseEntity<Map<String, Object>> findOfferedCourseReport (HttpServletRequest request, HttpServletResponse response, 
			@RequestBody ReportOfferedCourseData data ) {
		try {
			
			Map<String, Object> result = reportService.findOfferedCourseReport(data);
			
			if(MODE_EXPORT_EXCEL_BASE64.equals(data.getMode()))
			{
				ExportDataProcess export = new ExportDataProcess();

				export.setJdbcTemplate(jdbcTemplate);
				export.setLimitRow(200000);
				export.setTransactionId((new Date()).getTime());
				export.buildData(new OfferedCourseExportProvider(jdbcTemplate, (String) result.get(QUERY), (Object[]) result.get(PARAMS) ));
			
				export.export();
				
				byte [] exportFile = export.getOutputRaw();

				/* For return file  */
				// PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(exportFile)
				// .fileName("OfferedCourseReport.xlsx")
				// .build();
				

				// Object obj = ResponseEntity.ok()
				// 		.contentType(MediaType.parseMediaType(getContentType(reponse.getFileName())))
				// 		.header(getContentDisposition(reponse.getFileName()))
				// 		.body(new ByteArrayResource(reponse.getDatas()));

			 	result.put(ENTRIES, Base64.getEncoder().encodeToString(exportFile));
			}
			
			Map<String ,Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));

			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
// report 2
	@PostMapping("find-canceled-course-report")
	public ResponseEntity<Map<String, Object>> findCanceledCourseReport (HttpServletRequest request, HttpServletResponse response,
			@RequestBody ReportCanceledCourseData data) {
		try {
			
			Map<String, Object> result = reportService.findCanceledCourseReport(data);
			
			if(MODE_EXPORT_EXCEL_BASE64.equals(data.getMode()))
			{
				ExportDataProcess export = new ExportDataProcess();

				export.setJdbcTemplate(jdbcTemplate);
				export.setLimitRow(200000);
				export.setTransactionId((new Date()).getTime());
				export.buildData(new CanceledCourseExportProvider(jdbcTemplate, (String) result.get(QUERY), (Object[]) result.get(PARAMS) ));
			
				export.export();
				
				byte [] exportFile = export.getOutputRaw();

				/* For return file  */
				// PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(exportFile)
				// .fileName("OfferedCourseReport.xlsx")
				// .build();
				

				// Object obj = ResponseEntity.ok()
				// 		.contentType(MediaType.parseMediaType(getContentType(reponse.getFileName())))
				// 		.header(getContentDisposition(reponse.getFileName()))
				// 		.body(new ByteArrayResource(reponse.getDatas()));

			 	result.put(ENTRIES, Base64.getEncoder().encodeToString(exportFile));
			}
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
// report 3	
	@PostMapping("find-enrollment-list-report")
	public ResponseEntity<Map<String, Object>> findEnrollmentListReport (HttpServletRequest request, HttpServletResponse response,
			@RequestBody ReportEnrollmentListData data) {
		try {
			
			Map<String, Object> result = reportService.findEnrollmentListReport(data);
			
			if(MODE_EXPORT_EXCEL_BASE64.equals(data.getMode()))
			{
				ExportDataProcess export = new ExportDataProcess();

				export.setJdbcTemplate(jdbcTemplate);
				export.setLimitRow(200000);
				export.setTransactionId((new Date()).getTime());
				export.buildData(new EnrollmentListExportProvider(jdbcTemplate, (String) result.get(QUERY), (Object[]) result.get(PARAMS) ));
			
				export.export();
				
				byte [] exportFile = export.getOutputRaw();

				/* For return file  */
				// PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(exportFile)
				// .fileName("OfferedCourseReport.xlsx")
				// .build();
				

				// Object obj = ResponseEntity.ok()
				// 		.contentType(MediaType.parseMediaType(getContentType(reponse.getFileName())))
				// 		.header(getContentDisposition(reponse.getFileName()))
				// 		.body(new ByteArrayResource(reponse.getDatas()));

			 	result.put(ENTRIES, Base64.getEncoder().encodeToString(exportFile));
			}
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
// report 4
	@PostMapping("find-course-enrollment-report")
	public ResponseEntity<Map<String, Object>> findCourseEnrollmentReport (HttpServletRequest request, HttpServletResponse response,
			@RequestBody ReportCourseEnrollmentData data) {
		try {
			
			Map<String, Object> result = reportService.findCourseEnrollmentReport(data);
			
			if(MODE_EXPORT_EXCEL_BASE64.equals(data.getMode()))
			{
				ExportDataProcess export = new ExportDataProcess();

				export.setJdbcTemplate(jdbcTemplate);
				export.setLimitRow(200000);
				export.setTransactionId((new Date()).getTime());
				export.buildData(new CourseEnrollmentExportProvider(jdbcTemplate, (String) result.get(QUERY), (Object[]) result.get(PARAMS) ));
			
				export.export();
				
				byte [] exportFile = export.getOutputRaw();

				/* For return file  */
				// PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(exportFile)
				// .fileName("OfferedCourseReport.xlsx")
				// .build();
				

				// Object obj = ResponseEntity.ok()
				// 		.contentType(MediaType.parseMediaType(getContentType(reponse.getFileName())))
				// 		.header(getContentDisposition(reponse.getFileName()))
				// 		.body(new ByteArrayResource(reponse.getDatas()));

			 	result.put(ENTRIES, Base64.getEncoder().encodeToString(exportFile));
			}
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
// report 5 
	@PostMapping("find-course-payment-report")
	public ResponseEntity<Map<String, Object>> findCoursePaymentReport (HttpServletRequest request, HttpServletResponse response,
			@RequestBody ReportCoursePaymentData data) {
		try {
			
			Map<String, Object> result = reportService.findCoursePaymentReport(data);
			
			if(MODE_EXPORT_EXCEL_BASE64.equals(data.getMode()))
			{
				ExportDataProcess export = new ExportDataProcess();

				export.setJdbcTemplate(jdbcTemplate);
				export.setLimitRow(200000);
				export.setTransactionId((new Date()).getTime());
				export.buildData(new CoursePaymentExportProvider(jdbcTemplate, (String) result.get(QUERY), (Object[]) result.get(PARAMS) ));
			
				export.export();
				
				byte [] exportFile = export.getOutputRaw();

				/* For return file  */
				// PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(exportFile)
				// .fileName("OfferedCourseReport.xlsx")
				// .build();
				

				// Object obj = ResponseEntity.ok()
				// 		.contentType(MediaType.parseMediaType(getContentType(reponse.getFileName())))
				// 		.header(getContentDisposition(reponse.getFileName()))
				// 		.body(new ByteArrayResource(reponse.getDatas()));

			 	result.put(ENTRIES, Base64.getEncoder().encodeToString(exportFile));
			}
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
// report 6
	@PostMapping("find-enrollment-and-payment-report")
	public ResponseEntity<Map<String, Object>> findEnrollmentAndPaymentReport (HttpServletRequest request, HttpServletResponse response, 
			@RequestBody ReportEnrollmentAndPaymentData data) {
		try {
			
			Map<String, Object> result = reportService.findEnrollmentAndPaymentReport(data);
			
			if(MODE_EXPORT_EXCEL_BASE64.equals(data.getMode()))
			{
				ExportDataProcess export = new ExportDataProcess();

				export.setJdbcTemplate(jdbcTemplate);
				export.setLimitRow(200000);
				export.setTransactionId((new Date()).getTime());
				export.buildData(new EnrollmentAndPaymentExportProvider(jdbcTemplate, (String) result.get(QUERY), (Object[]) result.get(PARAMS) ));
			
				export.export();
				
				byte [] exportFile = export.getOutputRaw();

				/* For return file  */
				// PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(exportFile)
				// .fileName("OfferedCourseReport.xlsx")
				// .build();
				

				// Object obj = ResponseEntity.ok()
				// 		.contentType(MediaType.parseMediaType(getContentType(reponse.getFileName())))
				// 		.header(getContentDisposition(reponse.getFileName()))
				// 		.body(new ByteArrayResource(reponse.getDatas()));

			 	result.put(ENTRIES, Base64.getEncoder().encodeToString(exportFile));
			}
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	
// report 7
	@PostMapping("find-department-income-report")
	public ResponseEntity<Map<String, Object>> findDepartmentIncomeReport (HttpServletRequest request, HttpServletResponse response, 
			@RequestBody ReportDepartmentIncomeData data) {
		try {
			Map<String, Object> result = reportService.findDepartmentIncomeReport(data);
			
			if(MODE_EXPORT_EXCEL_BASE64.equals(data.getMode()))
			{
				ExportDataProcess export = new ExportDataProcess();

				export.setJdbcTemplate(jdbcTemplate);
				export.setLimitRow(200000);
				export.setTransactionId((new Date()).getTime());
				export.buildData(new DepartmentIncomeExportProvider(jdbcTemplate, (String) result.get(QUERY), (Object[]) result.get(PARAMS) ));
			
				export.export();
				
				byte [] exportFile = export.getOutputRaw();

				/* For return file  */
				// PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(exportFile)
				// .fileName("OfferedCourseReport.xlsx")
				// .build();
				

				// Object obj = ResponseEntity.ok()
				// 		.contentType(MediaType.parseMediaType(getContentType(reponse.getFileName())))
				// 		.header(getContentDisposition(reponse.getFileName()))
				// 		.body(new ByteArrayResource(reponse.getDatas()));

			 	result.put(ENTRIES, Base64.getEncoder().encodeToString(exportFile));
			}
			
			Map<String, Object> addOn = new HashMap<>();
			addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
			
			return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
		}
	}
	//Report Grade
	@PostMapping("find-course-grade-result-export")
    public ResponseEntity<Map<String, Object>> findGradeExportList(HttpServletRequest request, HttpServletResponse response,@RequestBody MemberCourseData memberCourseData) {
	try {
		Map<String, Object> result = reportService.findGradeExportist(memberCourseData);
		if(MODE_EXPORT_EXCEL_BASE64.equals(memberCourseData.getMode()))
		{
			ExportDataProcess export = new ExportDataProcess();

			export.setJdbcTemplate(jdbcTemplate);
			export.setLimitRow(200000);
			export.setTransactionId((new Date()).getTime());
			export.buildData(new TeacherCourseGradeExportProvider(jdbcTemplate, (String) result.get(QUERY), (Object[]) result.get(PARAMS) ));
		
			export.export();
			
			byte [] exportFile = export.getOutputRaw();

			/* For return file  */
			// PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(exportFile)
			// .fileName("OfferedCourseReport.xlsx")
			// .build();
			

			// Object obj = ResponseEntity.ok()
			// 		.contentType(MediaType.parseMediaType(getContentType(reponse.getFileName())))
			// 		.header(getContentDisposition(reponse.getFileName()))
			// 		.body(new ByteArrayResource(reponse.getDatas()));

		 	result.put(ENTRIES, Base64.getEncoder().encodeToString(exportFile));
		}
                    Map<String, Object> addOn = new HashMap<>();
                    addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
		return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
	} catch (Exception e) {
		LOG.error(e.getMessage(), e);
		return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
	}
}

    @PostMapping("find-payment-data-export-list")
    public ResponseEntity<Map<String, Object>> findPaymentDataExportList(HttpServletRequest request, HttpServletResponse response,@RequestBody FinancePaymentData criteria) {
	try {
		Map<String, Object> result = reportService.findPaymentDataExportList(criteria);
		if(MODE_EXPORT_EXCEL_BASE64.equals(criteria.getMode()))
		{
			ExportDataProcess export = new ExportDataProcess();

			export.setJdbcTemplate(jdbcTemplate);
			export.setLimitRow(200000);
			export.setTransactionId((new Date()).getTime());
			export.buildData(new PaymentDataExportProvider(jdbcTemplate, (String) result.get(QUERY), (Object[]) result.get(PARAMS) ));
		
			export.export();
			
			byte [] exportFile = export.getOutputRaw();

			/* For return file  */
			// PdfFileDownloadReponse reponse = PdfFileDownloadReponse.builder().datas(exportFile)
			// .fileName("OfferedCourseReport.xlsx")
			// .build();
			

			// Object obj = ResponseEntity.ok()
			// 		.contentType(MediaType.parseMediaType(getContentType(reponse.getFileName())))
			// 		.header(getContentDisposition(reponse.getFileName()))
			// 		.body(new ByteArrayResource(reponse.getDatas()));

		 	result.put(ENTRIES, Base64.getEncoder().encodeToString(exportFile));
		}
                    Map<String, Object> addOn = new HashMap<>();
                    addOn.put(TOTAL_RECORDS, result.get(TOTAL_RECORDS));
		return new ResponseEntity<>(CommonUtils.response(result.get(ENTRIES), MSG_SEARCH_SUCCESS, addOn), HttpStatus.OK);
	} catch (Exception e) {
		LOG.error(e.getMessage(), e);
		return new ResponseEntity<>(CommonUtils.responseError(e.getMessage()), HttpStatus.OK);
	}
    }
}
