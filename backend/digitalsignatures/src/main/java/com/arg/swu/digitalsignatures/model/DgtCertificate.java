package com.arg.swu.digitalsignatures.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ForeignKey;
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
public class DgtCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
	private Long id;

     
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TENTANT_ID", referencedColumnName = "TENANT_ID", nullable = true
    , foreignKey=@ForeignKey(name="fk_dgtcert_tn"))  
    private DgtTenantProvider tentantProvider;

    @Column(name="JKS_DATA" , columnDefinition = "TEXT")    
    private String jksData;

    @Column(name="JKS_STORE_PASS" , columnDefinition = "TEXT")
    private String jksStorePass;

    @Column(name="JKS_PRIVATEKEY_PASS" , columnDefinition = "TEXT")
    private String jksPrivateKeyPass;

    @Column(name="ACTIVE_FLAG")
    private Boolean activeFlag;
    @Column(name="ISSUE_DATE")
    private Date issueDate;
    @Column(name="EXPIRE_DATE")
    private Date expiryDate;

    private Date createdDate;
    private Date updatedDate;
}
