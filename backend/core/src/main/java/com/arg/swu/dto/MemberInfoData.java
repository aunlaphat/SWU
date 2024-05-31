package com.arg.swu.dto;

import java.io.Serializable;
import java.util.Date;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class MemberInfoData extends PageableCommon implements Serializable {

	private static final long serialVersionUID = 3666344936019544800L;
	
	@Schema(description = "PK")
	private Long memberId;
	
	@Schema(description = "รูปผู้ลงทะเบียนเรียน")
	private String filename;
	
	@Schema(description = "Prefix")
	private String prefix;
	
	@Schema(description = "Module")
	private String module;
	
	@Schema(description = "อีเมล")
	private String memberEmail;
	
	@Schema(description = "รูปแบบบัญชีที่ใช้สมัคร")
	private Long memberChannel;
	
	@Schema(description = "gmail_token")
	private String gmailToken;
	
	@Schema(description = "facebook_token")
	private String facebookToken;
	
	@Schema(description = "ประเภทสัญชาติผู้สมัคร")
	private Long memberCountryType;
	
	@Schema(description = "สัญชาติ")
	private Long countryId;
	
	@Schema(description = "เลขประจำตัวประชาชน/หมายเลขพาสปอร์ต")
	private String memberCardno;
	
	@Schema(description = "คำนำหน้า")
	private Long prefixnameId;
	
	@Schema(description = "ชื่อ (ไทย)")
	private String memberFirstnameTh;
	
	@Schema(description = "นามสกุล (ไทย)")
	private String memberLastnameTh;
	
	@Schema(description = "ชื่อ (อังกฤษ)")
	private String memberFirstnameEn;
	
	@Schema(description = "นามสกุล (อังกฤษ)")
	private String memberLastnameEn;
	
	@Schema(description = "เพศ")
	private Long memberGender;
	
	@Schema(description = "วัน/เดือน/ปีเกิด")
	private Date memberBirthdate;
	
	@Schema(description = "โทรศัพท์")
	private String memberPhoneno;
	
	@Schema(description = "วุฒิการศึกษาสูงสุด (ที่ได้รับ)")
	private Long maxEducation;
	
	@Schema(description = "สถานะการศึกษา/การทำงานปัจจุบัน")
	private Long currentJob;
	
	@Schema(description = "สถานะการสมัครรับข่าวสาร")
	private Boolean receivedNews;
	
	@Schema(description = "อ้างอิงรหัส moodle user id (PK)")
	private Long moodleUserId;
	
	@Schema(description = "รูปแบบบัญชีที่ใช้สมัคร (ไทย)")
	private String channelTh;
	
	@Schema(description = "รูปแบบบัญชีที่ใช้สมัคร (อังกฤษ)")
	private String channelEn;
	
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	@Schema(description = "Custom ที่อยู่")
	private MemberAddressData memberAdderss;
	
    private String memberNo;
    
    private Date lastLoginDatetime;
    private Date lastLogin;
    
    
}
