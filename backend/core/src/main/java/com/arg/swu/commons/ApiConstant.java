package com.arg.swu.commons;

import java.util.Locale;

/**
 * 
 * @author sitthichaim
 *
 */
public interface ApiConstant {

    public static final Locale LOCALE_THAI = new Locale("th", "TH");
    public static final Locale LOCALE_ENG = new Locale("en", "US");
	public static final String formatDateDefault = "dd/MM/yyyy HH:mm";
    
	public static final String STATUS = "status";
	public static final String MESSAGE = "message";
	public static final String DATA = "data";
	public static final String SUCCESS = "success";
	public static final String EXCEPTION = "exception";
	public static final String ENTRIES = "entries";
	public static final String QUERY = "query";
	public static final String PARAMS = "params";

	public static final String DEFAULT_LANG = "th";
	public static final String DEFAULT_DROPDOWN_LIMIT = " offset 0 limit 15 ";
	public static final String TH = "th";
	public static final String EN = "en";
	
	public static final String CLASSPATH = "language/messages";

	public static final String WHERE = " where 1 = 1 ";
	public static final String LIMIT = " offset ? limit ? ";
	public static final String TOTAL_RECORDS = "totalRecords";
	
	public static final String MSG_SEARCH_SUCCESS = "ค้นหาข้อมูลสำเร็จ";
	public static final String MSG_CREATE_SUCCESS = "สร้างข้อมูลสำเร็จ";
	public static final String MSG_EDIT_SUCCESS = "แก้ไขข้อมูลสำเร็จ";
	public static final String MSG_SAVE_SUCCESS = "บันทึกข้อมูลสำเร็จ";
	public static final String MSG_DELETE_SUCCESS = "ลบข้อมูลสำเร็จ";
	public static final String MSG_UPLOAD_SUCCESS = "อัพโหลดข้อมูลสำเร็จ";
	public static final String MSG_EDIT_DATA_NOTFOUND = "ไม่พบข้อมูลที่แก้ไข";
	public static final String MSG_DATA_NOTFOUND = "ไม่พบข้อมูล";
	public static final String MSG_LOGIN_SUCCESS = "Login success";
	public static final String MSG_INVALID_USER = "Invalid user";
	public static final String MSG_RESET_PASSWORD_NOTFOUND = "อีเมลไม่ถูกต้อง กรุณาตรวจอีกครั้ง";
	public static final String MSG_RESET_PASSWORD_GOOGLE = "อีเมลนี้ได้ผูกไว้กับบัญชี Google กรุณาเข้าสู่ระบบด้วยบัญชี Google";
	public static final String MSG_RESET_PASSWORD_FACEBOOK = "อีเมลนี้ได้ผูกไว้กับบัญชี Facebook กรุณาเข้าสู่ระบบด้วยบัญชี Facebook";
	public static final String MSG_RESET_PASSWORD_SUCCESS = "สร้างรหัสผ่านใหม่สำเร็จ";
	
	public enum RunningNumber {
			OCCUPATION("OC"),
			COURSE_TYPE("CT"),
			BUASRI_ID("cbs"),
			GENERAL_SKILL("S"),
			RECEIPT_NO("");
			
			private String value;

			private RunningNumber(String value) {
				this.value = value;
			}

			public String getValue() {
				return this.value;
			}
	};
	
	public static final String MODE_SEARCH = "search";
	public static final String MODE_EXPORT_EXCEL_BASE64 = "excelbase64";
	public static final String MODE_EXPORT_CVS = "csv";
	public static final String MODE_PDF = "pdf";
	public static final String MODE_TXT = "txt";
	public static final String MODE_DOCX = "docx";
	public static final String MODE_COPY = "copy";
	
	public static final String SLASH = "/";

	public static final String CURRENCY_THB = "THB";
	public static final String SOURCE_TYPE_QR = "qr";
	public static final String SOURCE_TYPE_CARD = "card";
	public static final String TOKEN = "token";

	public static final Long EMAIL_TEMPLATE_30036001 = 30036001L;
	public static final Long EMAIL_TEMPLATE_30036002 = 30036002L;
	public static final Long EMAIL_TEMPLATE_30036003 = 30036003L;
	public static final Long EMAIL_TEMPLATE_30036004 = 30036004L;
	public static final Long EMAIL_TEMPLATE_30036005 = 30036005L;
	public static final Long EMAIL_TEMPLATE_30036006 = 30036006L;
	public static final Long EMAIL_TEMPLATE_30036007 = 30036007L;
	public static final Long EMAIL_TEMPLATE_30036008 = 30036008L;
	public static final Long EMAIL_TEMPLATE_30036009 = 30036009L;
	public static final Long EMAIL_TEMPLATE_30036010 = 30036010L;
	
	public enum PathModule {
			BANNER,
			COURSE,
			COURSEPUBLIC,
			MASTER_DOCUMENT,
			MASTER_FINANCIAL,
			MEMBER,
			PERSONAL,
			RECEIPT,
			SYSTEM,
            NEWS
	};

	public static final String PATH_BANNER = "banner";
	public static final String PATH_COURSE = "course";
	public static final String PATH_COURSEPUBLIC = "coursepublic";
	public static final String PATH_MASTER_DOCUMENT = "master-document";
	public static final String PATH_MASTER_FINANCIAL = "master-financial";
	public static final String PATH_MEMBER = "member";
	public static final String PATH_PERSONAL = "personal";
	public static final String PATH_RECEIPT = "receipt";
	public static final String PATH_SYSTEM = "system";
	public static final String PATH_NEWS = "news";

	public static final String Q_IMPORT_PAYMENT = "importpayment";
	public static final String Q_CONFIRM_PAYMENT = "confirmpayment";
	public static final String Q_RECONCILE_PAYMENT = "reconcilepayment";
	public static final String Q_SENDMAIL = "sendmail";
	public static final String Q_RECEIPT_PRINT = "receiptreport";
	public static final String Q_SIGN_FILE = "signfile";
	public static final String Q_PRINTING = "printingscheduled";
	public static final String Q_DUMP_MASTER_PERSONAL = "dumpmasterpersonal";
	public static final String Q_DUMP_MASTER_DEPARTMENT = "dumpmasterdepartment";
	
	public enum ImportStatus {
		PASS("pass"),
		FAIL("fail"),
		MISS("miss");
		
		private String value;

		private ImportStatus(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public static final String TRANSACTION_STATE_AUTHORIZED = "Authorized";
	public static final String TRANSACTION_STATE_PRE_AUTHORIZED = "Pre-Authorized";
	public static final String REDIRECT = "redirect:";
	
	public enum SyncTable {
		MAS_PERSONAL("mas_personal"),
		MAS_DEPARTMENT("mas_department");
		
		private String value;
		
		private SyncTable(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}
	
}
