package com.arg.swu.digitalsignatures.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class DgtSigner 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
	private Long id; 

    private String code;
    private String name;
    private Integer type; //1. content 2. Image GIF 3. emtry

    private String content;
    private String imageBase64;
    private String fileName;
    private Boolean active;
    private Date createdDate;
    private Date updatedDate;

    private float positionX;
    private float positionY;
    private float width;
    private float height;
    private String fontName;
    private String fontSize;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TENTANT_ID", referencedColumnName = "TENANT_ID", nullable = true
    , foreignKey=@ForeignKey(name="fk_signer_td"))  
    private DgtTenantProvider tentantProvider;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CERTIFICATE_ID", referencedColumnName = "ID", nullable = true
    , foreignKey=@ForeignKey(name="fk_signer_cert"))  
    private DgtCertificate certificate;

}
