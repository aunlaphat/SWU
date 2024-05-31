package com.arg.swu.digitalsignatures.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ForeignKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DgtSingingConsole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
	private Long id;

    private Date issueTrustDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TENTANT_ID", referencedColumnName = "TENANT_ID", nullable = true
    , foreignKey=@ForeignKey(name="fk_signingcole_td"))  
    private DgtTenantProvider tentant;

    private String status;
    private String message;
    private float fileSize;    
}
