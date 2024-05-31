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
import lombok.Data;

/**
 *
 * @author kumpeep
 */
@Data
@Entity
public class MasDepartment extends Footprint implements Serializable {

    private static final long serialVersionUID = 3139584132074800412L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long depId;
    private Long depParentId;
    private Long depType;
    private String depCode;
    private String depNameTh;
    private String depNameEn;
    private String depNameShortTh;
    private String depNameShortEn;
    private String depNameAbbrTh;
    private String depNameAbbrEn;
    private Boolean educationStatus;
}
