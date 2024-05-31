package com.arg.swu.models.coursemgt;

import java.io.Serializable;

import com.arg.swu.models.commons.Footprint;
import com.arg.swu.models.commons.LookupCatalog;
import com.arg.swu.models.lookup.CourseTargetGroup;
import com.arg.swu.models.lookup.IndustryGroupLookup;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Data
// @Table(name="xxxxxd")
public class CourseMgt extends Footprint implements Serializable { 

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Schema(description = "อ้างอิงหลักสูตร")
    private String courseRefNo;

    @Schema(description = "รหัสหลักสูตร")
    private String courseNo;

    @Schema(description = "ชื่อหลักสูตร (ภาษาไทย)", requiredMode = RequiredMode.REQUIRED, maxLength = 255, pattern = "dd/MM/yyyy")
    private String thCourseName;

    @Schema(description = "ชื่อหลักสูตร (ภาษาอังกฤษ")
    private String enCourseName;

    @Schema(description = "คำอธิบายหลักสูตร")
    private String courseDesc;

    @Schema(description = "กลุ่มอุตสาหกรรม กลุ่มพัฒนาคน")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "industryGroupId", referencedColumnName = "catalogId", nullable = true
    , foreignKey=@ForeignKey(name="course_fk_lookup_ind"))    
    private IndustryGroupLookup industryGroup;

    @Schema(description = "กลุ่มเป้าหมาย")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "targetGroup", referencedColumnName = "catalogId", nullable = true
    , foreignKey=@ForeignKey(name="course_fk_lookup_target"))
    private CourseTargetGroup targetGroup;
}
