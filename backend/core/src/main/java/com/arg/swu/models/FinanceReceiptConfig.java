package com.arg.swu.models;

import java.io.Serializable;

import com.arg.swu.models.commons.Footprint;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * 
 * @author sitthichaim
 *
 */
@Data
@Entity
public class FinanceReceiptConfig extends Footprint implements Serializable {

    private static final long serialVersionUID = 7389843873487674253L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptConfigId;

    private String logoPath;
    private String depNameTh;
    private String depNameEn;
    private String depAddressTh;
    private String depAddressEn;
    private String depTaxId;
    private String receiptPrefix;
    private String receiptNoteTh;
    private String receiptNoteEn;
    private String receiptRemark;
    private String staffSignaturePath;
    private String staffName;
    private String staffPosition;
    private String approvedSignaturePath;
    private String approvedName;
    private String approvedPosition;

}
