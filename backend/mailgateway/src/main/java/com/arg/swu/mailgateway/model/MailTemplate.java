package com.arg.swu.mailgateway.model;


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
public class MailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
	private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TENTANT_ID", referencedColumnName = "TENANT_ID", nullable = true
    , foreignKey=@ForeignKey(name="fk_mailtemplate_td"))  
    private MailTenantProvider tentant;

    private Long templateTypeId; //Fix by lookup
    private Date createdDate;
    private Date updatedDate;

    private String typeName;
    private String subject;
    private String templateFileName;

    
}
