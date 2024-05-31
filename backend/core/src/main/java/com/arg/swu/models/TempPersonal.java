package com.arg.swu.models;

import java.io.Serializable;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class TempPersonal extends Footprint implements Serializable {
	
	private static final long serialVersionUID = 3626738325145262959L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "temp_personal_id_seq", allocationSize = 1)
	private Long id;
	
    private String staffType;
    private String staffLine;
    private String buasriId;
    private String statusId;
    private String statusTh;
    private String statusEn;
    private String gafeMail;
    private String officeMail;
    private String pnameIntSnameTh;
    private String pnameIntLnameTh;
    private String pnameIntSnameEn;
    private String pnameIntLnameEn;
    private String personFnameTh;
    private String personMnameTh;
    private String personLnameTh;
    private String personFnameEn;
    private String personMnameEn;
    private String personLnameEn;
    private String positionTh;
    private String positionEn;
    private String facultyId;
    private String facultyLnameTh;
    private String facultyLnameEn;
    private String facultySnameTh;
    private String facultySnameEn;
    private String facultyAbbvTh;
    private String facultyAbbvEn;
    private String deptId;
    private String deptLnameTh;
    private String deptLnameEn;
    private String deptSnameTh;
    private String deptSnameEn;
    private String deptAbbvTh;
    private String deptAbbvEn;

}
