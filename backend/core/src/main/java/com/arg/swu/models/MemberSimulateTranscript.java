package com.arg.swu.models;

import java.io.Serializable;
import java.math.BigDecimal;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MemberSimulateTranscript extends Footprint implements Serializable {
	
	private static final long serialVersionUID = -7662788234137848866L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long simulateTranscriptId;
	
	private Long memberId;
    private Long courseId;
    private String courseCode;    
    private String courseNameTh;
    private String courseNameEn;
    private BigDecimal creditAmount;
    private String creditType;
    private String resultGrade;
    private Boolean passStatus;
    
}
