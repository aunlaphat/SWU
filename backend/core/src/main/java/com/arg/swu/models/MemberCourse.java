/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.swu.models;

import com.arg.swu.models.commons.Footprint;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author kumpeep
 */
@Data
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MemberCourse extends Footprint implements Serializable {
	
    private static final long serialVersionUID = -4673557792395036448L;
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberCourseId;
    private Long coursepublicId;
    private Long memberId;
    private Date registerDate;
    private Long paymentStatus;
    private Long studyStatus;
    private BigDecimal resultScore;
    private String resultGrade;
    private Boolean passStatus;
    private Long courseId;
    private Long detailImpId;
    private String certificateCaPathTh;
    private String certificateCaPathEn;
    private String certificateNonCaPathTh;
    private String certificateNonCaPathEn;
    private String certificateVerifyTh;
    private String certificateVerifyEn;
    
    private String certificateNo;
    private Date passDate; 
    private Date genPdfDate;
    private Boolean genPdfFlag;
    
}
