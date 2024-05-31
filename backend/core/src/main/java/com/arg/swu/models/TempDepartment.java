package com.arg.swu.models;

import java.io.Serializable;
import java.util.Date;

import com.arg.swu.models.commons.FootprintInterfaces;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = { "activeFlag" })
public class TempDepartment implements Serializable, FootprintInterfaces {
	
	private static final long serialVersionUID = 7859168208976125195L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "temp_department_id_seq", allocationSize = 1)
	private Long id;
	
    private String deptId;
    private String deptLnameTh;
    private String deptLnameEn;
    private String deptSnameTh;
    private String deptSnameEn;
    private String deptAbbvTh;
    private String deptAbbvEn;
    private String facultyId;
    private String facultyLnameTh;
    private String facultyLnameEn;
    private String facultySnameTh;
    private String facultySnameEn;
    private String facultyAbbvTh;
    private String facultyAbbvEn;
    private String studyFlag; 

    private Boolean activeFlag;

	@Schema(hidden = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "createById", referencedColumnName = "userId", nullable = true)
	private AutUser createBy;
	
	@Schema(hidden = true)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "updateById", referencedColumnName = "userId", nullable = true)
	private AutUser updateBy;
	
    @Schema(hidden = true)
	private Date createDate;
    
	@Schema(hidden = true)
	private Date updateDate;
	
    
}
