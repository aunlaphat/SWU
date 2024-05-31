package com.arg.swu.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author sitthichaim
 *
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AutUserData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 1140833922674488657L;
	
	@Schema(description = "PK")
	private Long userId;
	
	@Schema(description = "FileName")
	private String filename;
	
	@Schema(description = "Prefix")
	private String prefix;
	
	@Schema(description = "Module")
	private Long module;
	
	@Schema(description = "ชื่อผู้ใช้งาน")
	private String username;
	
	@Schema(description = "ประเภทข้อมูลอ้างอิงผู้ใช้งาน 30032000 ")
	private Long refUserType;
	
	@Schema(description = "รหัสอ้างอิง ID ผู้ใช้งาน")
	private Long refUserId;
	
	@Schema(description = "ชื่อ (ไทย)")
	private String firstnameTh;
	
	@Schema(description = "นามสกุล (ไทย)")
	private String lastnameTh;
	
	@Schema(description = "ชื่อ (อังกฤษ)")
	private String firstnameEn;
	
	@Schema(description = "นามสกุล (อังกฤษ)")
	private String lastnameEn;
	
//	@Schema(description = "คณะ/หน่วยงาน (mas_department)")
//	private Long depId;
	
	@Schema(description = "อีเมล")
	private String email;
	
	@Schema(description = "คณะ (mas_department)")
    private Long depIdLevel1;
	
	@Schema(description = "ภาควิชา (mas_department)")
    private Long depIdLevel2;
	
	@Schema(description = "เบอร์โทรศัพท์")
	private String telephone;
	
	@Schema(description = "รูปภาพลายเซ็นต์")
	private String userSignaturePath;
	
	@Schema(description = "รหัสผ่าน")
	private String password;
	
	@Schema(description = "Login ผ่าน Lifelong")
	private Boolean isLocal;
	
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
	@Schema(description = "กำหนดวันที่ให้ดำเนินการ")
	private Boolean activePeriodStatus;
	
	@Schema(description = "วันที่ให้ดำเนินการเริ่มต้น")
	private Date activePeriodStart;
	
	@Schema(description = "วันที่ให้ดำเนินการสิ้นสุด")
	private Date activePeriodEnd;
	
	@Schema(description = "ระดับการเข้าถึง")
	private Long accessLevel;
	
	@Schema(description = "วันที่เข้าสู่ระบบล่าสุด")
	private Date lastLoginDatetime;
	
	@Schema(description = "วันที่ออกจากระบบล่าสุด")
	private Date lastLogoutDatetime;
	
	@Schema(description = "วันที่เปลี่ยนรหัสผ่านล่าสุด")
	private Date lastChangepasswdDatetime;
	
	@Schema(description = "custom รหัสคณะ/หน่วยงาน")
	private String depCode;
	
	@Schema(description = "custom ชื่อเต็มคณะ/หน่วยงาน (ภาษาไทย)")
	private String depNameTh;
	
	@Schema(description = "custom ชื่อเต็มคณะ/หน่วยงาน (ภาษาอังกฤษ)")
	private String depNameEn;
	
	@Schema(description = "custom ชื่อย่อคณะ/หน่วยงาน (ภาษาไทย)")
	private String depNameShortTh;
	
	@Schema(description = "custom ชื่อย่อคณะ/หน่วยงาน (ภาษาอังกฤษ)")
	private String depNameShortEn;
	
	@Schema(description = "custom ตัวย่อคณะ/หน่วยงาน (ภาษาไทย)")
	private String depNameAbbrTh;
	
	@Schema(description = "custom ตัวย่อคณะ/หน่วยงาน (ภาษาอังกฤษ)")
	private String depNameAbbrEn;

	@Schema(description = "custom pkId สิทธิ์")
	private Long roleId;

	@Schema(description = "custom pkIds สิทธิ์")
	private List<Long> roleIds;
	
	@Schema(description = "custom สิทธิ์")
	private List<AutRoleData> roles;
	
	@Schema(description = "Personal Id")
    private Long personalId;

    @Schema(description = "count")
    private Long count;
}
