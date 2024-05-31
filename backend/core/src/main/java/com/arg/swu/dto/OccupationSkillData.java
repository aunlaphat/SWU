package com.arg.swu.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OccupationSkillData implements Serializable {

	private static final long serialVersionUID = 5407661053840783451L;

    @Schema(description = "")
    private Long memberId;

    @Schema(description = "รหัส occupation Id")
    private Long occupationId;

    @Schema(description = "รหัสอาชีพ")
    private String occupationCode;

    @Schema(description = "ชื่ออาชีพ (ภาษาไทย)")
    private String occupationNameTh;

    @Schema(description = "ชื่ออาชีพ (ภาษาอังกฤษ)")
    private String occupationNameEn;

    @Schema(description = "รหัส generalSkill Id")
    private Long generalSkillId;

    @Schema(description = "รหัสทักษะทั่วไป")
    private String generalSkillCode;

    @Schema(description = "ชื่อทักษะทั่วไป (ไทย)")
    private String generalSkillNameTh;

    @Schema(description = "ชื่อทักษะทั่วไป (อังกฤษ)")
    private String generalSkillNameEn;

    @Schema(description = "ระดับความยาก")
    private String occSkillLevel;

    @Schema(description = "ระดับคะแนน")
    private String levelScore;

    @Schema(description = "ชื่อระดับภาษาไทย")
    private String levelNameTh;

    @Schema(description = "ชื่อระดับภาษาอังกฤษ")
    private String levelNameEn;

    @Schema(description = "getLevelId")
    private Long getLevelId;

    @Schema(description = "getLevelNameTh")
    private String getLevelNameTh;

    @Schema(description = "getLevelNameEn")
    private String getLevelNameEn;

    @Schema(description = "compareResult")
    private String compareResult;

}
