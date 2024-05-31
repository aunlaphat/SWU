package com.arg.swu.models;

import java.io.Serializable;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class MasPersonal  extends Footprint implements Serializable {
	
	private static final long serialVersionUID = 3914277319735216925L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalId;

    private Long personalType;
    private String personalLine;
    private String buasriId;
    private String statusId;
    private String statusNameTh;
    private String statusNameEn;
    private String email;
    private String prefixShortTh;
    private String prefixTh;
    private String prefixShortEn;
    private String prefixEn;
    private String firstnameTh;
    private String middlenameTh;
    private String lastnameTh;
    private String firstnameEn;
    private String middlenameEn;
    private String lastnameEn;
    private String positionTh;
    private String positionEn;
    private String depCodeLevel1;
    private Long depIdLevel1;
    private String depCodeLevel2;
    private Long depIdLevel2;
    private String personalPhotoPath;
    private Long module;
    private String prefix;

    private Long moodleUserId;
    private String moodlePassword;
    
}
