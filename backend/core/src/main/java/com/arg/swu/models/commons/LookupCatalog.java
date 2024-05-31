package com.arg.swu.models.commons;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="lookup_catalog")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="parentId", 
  discriminatorType = DiscriminatorType.INTEGER)
public class LookupCatalog extends Footprint {

    private static final long serialVersionUID = 1l;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catalogId;
    // @Column(name="parent_id")
    // private Long parentIdOn;
    private String key;
    private String nameLo;
    private String nameEn;
}
