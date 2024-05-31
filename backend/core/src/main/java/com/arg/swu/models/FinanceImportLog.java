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

import org.hibernate.annotations.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author sutthiyapakc
 */
@Data
@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FinanceImportLog extends Footprint implements Serializable {

	private static final long serialVersionUID = 4013581915638372484L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long impId;
	
    private String fileReferenceCode;
    private String impFileName;
    private String impFileLink;
    private Long impFileSize;
    private Long impFileRow;
    private Long passRow;
    private Long failRow;
    private Long missRow;
    private String messageError;
    private BigDecimal impFileMoney;
    private Long coursepublicId;
    @Comment("prcessing, imported, generated, success")
    private String status;
    @Comment("Output file zip cert, receipt")
    private String resultFileZip;
    
}
