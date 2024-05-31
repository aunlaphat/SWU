/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.swu.dto;

import com.arg.swu.models.commons.PageableCommon;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author kumpeep
 */
@Data
public class MasDepartmentData extends PageableCommon implements Serializable {

    private static final long serialVersionUID = 2170830102360053261L;
    
    @Schema(description = "PK auto run (dep_id) ")
    private Long depId;
    @Schema(description = "fk (dep_parent_id)")
    private Long depParentId;
    @Schema(description = "30009001  คณะ, 30009002  ภาค, 30009003  วิทยาลัย")
    private Long depType;
    @Schema(description = "รหัสคณะ/หน่วยงาน(ห้ามซ้ำ)")
    private String depCode;
    @Schema(description = "ชื่อเต็มคณะ/หน่วยงาน (ภาษาไทย)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String depNameTh;
    @Schema(description = "ชื่อเต็มคณะ/หน่วยงาน (ภาษาอังกฤษ)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String depNameEn;
    @Schema(description = "ชื่อย่อคณะ/หน่วยงาน (ภาษาไทย)")
    private String depNameShortTh;
    @Schema(description = "ชื่อย่อคณะ/หน่วยงาน (ภาษาอังกฤษ)")
    private String depNameShortEn;
    @Schema(description = "ตัวย่อคณะ/หน่วยงาน (ภาษาไทย)")
    private String depNameAbbrTh;
    @Schema(description = "ตัวย่อคณะ/หน่วยงาน (ภาษาอังกฤษ)")
    private String depNameAbbrEn;
    @Schema(description = "สถานะจัดการเรียนการสอน")
    private Boolean educationStatus;
    
	@Schema(description = "สถานะการใช้งาน")
	private Boolean activeFlag;

}
