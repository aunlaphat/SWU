package com.arg.swu.digitalsignatures.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DgtTenantProvider {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TENANT_ID")
	private Long tenantId;


    @Column(name="CODE")
    private String companyCode;
    @Column(name="TYPE_NAME")
    private String typeName;
    @Column(name="NAME_EN")
    private String nameEn;
    @Column(name="NAME_LO")
    private String nameLo;
    @Column(name="IDENTITY_ID")
    private String identityId;
    @Column(name="ADDRESS1")
    private String address1;
    @Column(name="ADDRESS2")
    private String address2;

    @Column(name="REGISTRATION_DATE")
    private Date registrationDate;

    @Column(name="ACTIVE_FLAG")
    private Boolean activeFlag;


}
