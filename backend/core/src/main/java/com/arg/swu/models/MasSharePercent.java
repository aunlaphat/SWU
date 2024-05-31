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
import lombok.Data;

/**
 *
 * @author kumpeep
 */
@Data
@Entity
public class MasSharePercent extends Footprint implements Serializable {
    
	private static final long serialVersionUID = 3139584132073800412L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sharePercentId;
    private Long depId;
    private BigDecimal costShareDepPercent;
    private BigDecimal costShareGlobalPercent;
    private BigDecimal costShareCenterPercent;
}
