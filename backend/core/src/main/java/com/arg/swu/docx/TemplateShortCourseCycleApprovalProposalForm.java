package com.arg.swu.docx;

import java.util.HashMap;

import com.arg.swu.commons.BahtTextUtil;
import com.arg.swu.commons.CommonUtils;
import com.arg.swu.commons.ConvertFormatUtil;
import com.arg.swu.dto.DocxShortCourseCycleApprovalProposalFormData;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author sitthichaim
 *
 */
public class TemplateShortCourseCycleApprovalProposalForm extends DocxGenerator {
	
	private static final String COURSE_NAME_TH = "courseNameTh";
	private static final String COURSE_NAME_EN = "courseNameEn";
	private static final String PUBLIC_NAME_TH = "publicNameTh";
	private static final String PUBLIC_NAME_EN = "publicNameEn";
	private static final String COURSE_DESC_TH = "courseDescTh";
	private static final String COURSE_DESC_EN = "courseDescEn";
	private static final String MAX_ENROLL = "maxEnroll";
	private static final String COURSE_REGIS = "courseRegis";
	private static final String COURSE_CLASS = "courseClass";
	private static final String COURSE_FORMAT = "courseFormat";
	private static final String COURSE_FORMAT_DESC_TH = "courseFormatDescTh";
	private static final String COURSE_FORMAT_DESC_EN = "courseFormatDescEn";
	private static final String COURSE_TIME_TH = "courseTimeTh";
	private static final String COURSE_TIME_EN = "courseTimeEn";
	private static final String FEE_STATUS = "feeStatus";
	private static final String FEE_AMOUNT = "feeAmount";
	private static final String FEE_AMOUNT_TEXT = "feeAmountText";
	// private static final String PROMOTION_AMOUNT = "promotionAmount";
	private static final String EMAIL = "email";
	private static final String INSTRUCTOR_NAME = "instructorName";
	
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
	public TemplateShortCourseCycleApprovalProposalForm(DocxGeneratorBuilder builder, TemplateShortCourseCycleApprovalProposalFormBuilder selfBuilder) {
		super(builder);
		params = selfBuilder.params;
	}
	
	@Getter
	@Setter
	public static class TemplateShortCourseCycleApprovalProposalFormBuilder extends DocxGeneratorBuilder {
		
		private DocxShortCourseCycleApprovalProposalFormData model;
		private String lang;
		
		private HashMap<String, String> params = new HashMap<>();
		
		public TemplateShortCourseCycleApprovalProposalFormBuilder(String fileName, DocxShortCourseCycleApprovalProposalFormData dataModel, String lang) {
			super(fileName);
			this.model = dataModel;
			this.lang = lang;
		}
		
		public TemplateShortCourseCycleApprovalProposalFormBuilder generateParam() {
			if (null != this.model) {
				
				this.params.put(COURSE_NAME_TH, this.model.getCourseNameTh());
				this.params.put(COURSE_NAME_EN, this.model.getCourseNameEn());
				this.params.put(PUBLIC_NAME_TH, this.model.getPublicNameTh());
				this.params.put(PUBLIC_NAME_EN, this.model.getPublicNameEn());
				this.params.put(COURSE_DESC_TH, this.model.getCourseDescTh());
				this.params.put(COURSE_DESC_EN, this.model.getCourseDescEn());
				this.params.put(MAX_ENROLL, ConvertFormatUtil.convertFormat(this.model.getMaxEnroll(), ConvertFormatUtil.DEFAULT_DECIMAL_0DIGIT_FORMAT));
				this.params.put(COURSE_FORMAT_DESC_TH, this.model.getCourseFormatDescTh());
				this.params.put(COURSE_FORMAT_DESC_EN, this.model.getCourseFormatDescEn());
				this.params.put(COURSE_TIME_TH, this.model.getCourseTimeTh());
				this.params.put(COURSE_TIME_EN, this.model.getCourseTimeEn());

				this.params.put(FEE_AMOUNT, ConvertFormatUtil.convertFormat(this.model.getFeeAmount(), ConvertFormatUtil.DEFAULT_DECIMAL_2DIGIT_FORMAT));
				this.params.put(FEE_AMOUNT_TEXT, BahtTextUtil.getBathText(this.model.getFeeAmount()));
				
				// this.params.put(PROMOTION_AMOUNT, ConvertFormatUtil.convertFormat(this.model.getPromotionAmount(), ConvertFormatUtil.DEFAULT_DECIMAL_2DIGIT_FORMAT));
				this.params.put(EMAIL, this.model.getEmail());
				

				if (TH.equals(this.lang)) {
					
					this.params.put(COURSE_FORMAT, this.model.getCourseFormatTh());
					this.params.put(FEE_STATUS, this.model.getFeeStatusTh());
					this.params.put(INSTRUCTOR_NAME, this.model.getInstructorNameTh());
					
					if (null != this.model.getCourseRegisStart() && null != this.model.getCourseRegisEnd()) {
						StringBuilder courseRegis = new StringBuilder();
						courseRegis.append(CommonUtils.formatDate(this.model.getCourseRegisStart(), DATE_FORMAT, LOCALE_THAI));
						courseRegis.append(" - ");
						courseRegis.append(CommonUtils.formatDate(this.model.getCourseRegisEnd(), DATE_FORMAT, LOCALE_THAI));
						this.params.put(COURSE_REGIS, courseRegis.toString());
					} else if (null != this.model.getCourseRegisStart()) {
						this.params.put(COURSE_REGIS, CommonUtils.formatDate(this.model.getCourseRegisStart(), DATE_FORMAT, LOCALE_THAI));
					}

					if (null != this.model.getCourseClassStart() && null != this.model.getCourseClassEnd()) {
						StringBuilder courseClass = new StringBuilder();
						courseClass.append(CommonUtils.formatDate(this.model.getCourseClassStart(), DATE_FORMAT, LOCALE_THAI));
						courseClass.append(" - ");
						courseClass.append(CommonUtils.formatDate(this.model.getCourseClassEnd(), DATE_FORMAT, LOCALE_THAI));
						this.params.put(COURSE_CLASS, courseClass.toString());
					} else if (null != this.model.getCourseClassStart()) {
						this.params.put(COURSE_CLASS, CommonUtils.formatDate(this.model.getCourseClassStart(), DATE_FORMAT, LOCALE_THAI));
					}
				} else {
					
					this.params.put(COURSE_FORMAT, this.model.getCourseFormatEn());
					this.params.put(FEE_STATUS, this.model.getFeeStatusEn());
					this.params.put(INSTRUCTOR_NAME, this.model.getInstructorNameEn());
					
					if (null != this.model.getCourseRegisStart() && null != this.model.getCourseRegisEnd()) {
						StringBuilder courseRegis = new StringBuilder();
						courseRegis.append(CommonUtils.formatDate(this.model.getCourseRegisStart(), DATE_FORMAT, LOCALE_ENG));
						courseRegis.append(" - ");
						courseRegis.append(CommonUtils.formatDate(this.model.getCourseRegisEnd(), DATE_FORMAT, LOCALE_ENG));
						this.params.put(COURSE_REGIS, courseRegis.toString());
					} else if (null != this.model.getCourseRegisStart()) {
						this.params.put(COURSE_REGIS, CommonUtils.formatDate(this.model.getCourseRegisStart(), DATE_FORMAT, LOCALE_ENG));
					}

					if (null != this.model.getCourseClassStart() && null != this.model.getCourseClassEnd()) {
						StringBuilder courseClass = new StringBuilder();
						courseClass.append(CommonUtils.formatDate(this.model.getCourseClassStart(), DATE_FORMAT, LOCALE_ENG));
						courseClass.append(" - ");
						courseClass.append(CommonUtils.formatDate(this.model.getCourseClassEnd(), DATE_FORMAT, LOCALE_ENG));
						this.params.put(COURSE_CLASS, courseClass.toString());
					} else if (null != this.model.getCourseClassStart()) {
						this.params.put(COURSE_CLASS, CommonUtils.formatDate(this.model.getCourseClassStart(), DATE_FORMAT, LOCALE_ENG));
					}
				}
			}
			return this;
		}
		
		public TemplateShortCourseCycleApprovalProposalFormBuilder addParam(String key, String value) {
			this.params.put(key, value);
			return this;
		}
		
		public TemplateShortCourseCycleApprovalProposalForm build() {
			return (new TemplateShortCourseCycleApprovalProposalForm(new DocxGeneratorBuilder(getFileTemplate()), this));
		}
		
	}
	
}
