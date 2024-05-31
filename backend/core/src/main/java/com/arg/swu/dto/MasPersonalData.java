package com.arg.swu.dto;

import java.io.Serializable;

import com.arg.swu.models.commons.PageableCommon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 
 * @author sitthichaim
 *
 */
@Data
public class MasPersonalData extends PageableCommon implements Serializable {
	
	private static final long serialVersionUID = 912051669138142479L;

    @Schema(description = "PK")
    private Long personalId;

    @Schema(description = "ประเภทบุคลากร LOOKUP (30020000) ")
    private Long personalType;

    @Schema(description = "ประเภทปฏิบัติการ")
    private String personalLine;

    @Schema(description = "Buasri ID")
    private String buasriId;

    @Schema(description = "รหัสสถานะ")
    private String statusId;

    @Schema(description = "สถานภาพ (ไทย)")
    private String statusNameTh;

    @Schema(description = "สถานภาพ (อังกฤษ)")
    private String statusNameEn;

    @Schema(description = "อีเมล")
    private String email;

    @Schema(description = "ชื่อย่อคำนำหน้า (ไทย)")
    private String prefixShortTh;

    @Schema(description = "คำนำหน้า (ไทย)")
    private String prefixTh;

    @Schema(description = "ชื่อย่อคำนำหน้า (อังกฤษ)")
    private String prefixShortEn;

    @Schema(description = "คำนำหน้า (อังกฤษ)")
    private String prefixEn;

    @Schema(description = "ชื่อ (ไทย)")
    private String firstnameTh;

    @Schema(description = "ชื่อกลาง (ไทย)")
    private String middlenameTh;

    @Schema(description = "นามสกุล (ไทย)")
    private String lastnameTh;

    @Schema(description = "ชื่อ (อังกฤษ)")
    private String firstnameEn;

    @Schema(description = "ชื่อกลาง (อังกฤษ)")
    private String middlenameEn;

    @Schema(description = "นามสกุล (อังกฤษ)")
    private String lastnameEn;

    @Schema(description = "ตำแหน่ง (ไทย)")
    private String positionTh;

    @Schema(description = "ตำแหน่ง (อังกฤษ)")
    private String positionEn;

    @Schema(description = "รหัสส่วนงาน/คณะ")
    private String depCodeLevel1;

    @Schema(description = "filename")
    private String personalPhotoPath;

    @Schema(description = "fk ส่วนงาน/คณะ")
    private Long depIdLevel1;

    @Schema(description = "fk หน่วยงานย่อย")
    private Long depCodeLevel2;

	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;
	
    @Schema(description = "custom ใช้สำหรับค้นหา ชื่อ-สกุล")
    private String fullname;
    
//    @Schema(description = "custom ใช้สำหรับค้นหา หน่วยงาน")
//    private Long depId;

    @Schema(description = "custom ชื่อส่วนงาน/คณะ (ไทย)")
    private String depNameLv1Th;
    
    @Schema(description = "custom ชื่อส่วนงาน/คณะ (อังกฤษ)")
    private String depNameLv1En;
    
    @Schema(description = "custom ชื่อหน่วยงานย่อย (ไทย)")
    private String depNameLv2Th;
    
    @Schema(description = "custom ชื่อหน่วยงานย่อย (อังกฤษ)")
    private String depNameLv2En;
    
    @Schema(description = "custom ชื่อประเภทบุคลากร (ไทย)")
    private String personalTypeTh;
    
    @Schema(description = "custom ชื่อประเภทบุคลากร (อังกฤษ)")
    private String personalTypeEn;
    //criteria for query only (Do not add to table)
    @Schema(description = "coursepublic_status")
    private Long coursepublicStatus;
    @Schema(description = "public_name_th")
    private String publicNameTh;
    @Schema(description = "public_name_en")
    private String publicNameEn;
    private Long module;
    private String prefix;

    @Schema(description = "custom ใช้สำหรับค้นหา ชื่อ-สกุล (ภาษาไทย)")
    private String fullNameTh;

    @Schema(description = "custom ใช้สำหรับค้นหา ชื่อ-สกุล (ภาษาอังกฤษ)")
    private String fullNameEn;
    
    private Long moodleUserId;
    private String moodlePassword;

}
