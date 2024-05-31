package com.arg.swu.models;

import com.arg.swu.models.commons.Footprint;
import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutUser extends Footprint implements Serializable {

	private static final long serialVersionUID = 1495018576369922672L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	private String filename;
	private String prefix;
	private Long module;
	private String username;
	private Long refUserType;
	private Long refUserId;
	private String firstnameTh;
	private String lastnameTh;
	private String firstnameEn;
	private String lastnameEn;
	// private Long depId;
	// private String email;
    
    private String email;
    
    private Long depIdLevel1;
    private Long depIdLevel2;
    
	private String telephone;
	private String userSignaturePath;
	private String password;
	private Boolean isLocal;
	private Boolean activeFlag;
	private Boolean activePeriodStatus;
	private Date activePeriodStart;
	private Date activePeriodEnd;
	private Long accessLevel;
	private Date lastLoginDatetime;
	private Date lastLogoutDatetime;
	private Date lastChangepasswdDatetime;

	@Column(insertable=false, updatable=false)
	private Long createById;
	private Date createDate;

	@Column(insertable=false, updatable=false)
	private Long updateById;
	private Date updateDate;

	public AutUser(Long userId) {
		this.userId = userId;
	}

	public AutUser(Long userId, Long createById, Date createDate) {
		this.userId = userId;
		setCreateById(createById);
		setCreateDate(createDate);
	}
	
}
