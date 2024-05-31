package com.arg.swu.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class ApplyCourseData implements Serializable {

	private static final long serialVersionUID = 2542333836008434811L;

    private Long memberId;
    private Long coursePublicId;
    private Long prefixnameId;
    private String memberFirstNameTh;
    private String memberLastNameTh;
    private String memberFirstNameEn;
    private String memberLastNameEn;
    private String memberEmail;
    private String memberCardno;
    private Date memberBirthdate;
    private String memberPhoneno;
    private Long currentJob;
    private Long maxEducation;
    private Long memberCountryType;
    private Long countryId;
    private Long memberGender;
    private MemberAddressData memberAddress;
    private Long consentId;
    private String consentVersion;
    private String consentDisclose;
    private String ipAddress;
    private String userAgent;
    private String couponCode;

}
