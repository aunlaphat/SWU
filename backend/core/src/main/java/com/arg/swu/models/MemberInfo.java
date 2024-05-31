package com.arg.swu.models;

import java.io.Serializable;
import java.util.Date;

import com.arg.swu.models.commons.Footprint;

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
public class MemberInfo extends Footprint implements Serializable {
	
	private static final long serialVersionUID = -3816589389391092543L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;
	
	private String memberEmail;
	private Long memberChannel;
	private String gmailToken;
	private String facebookToken;
	private Long memberCountryType;
	private Long countryId;
	private String memberCardno;
	private Long prefixnameId;
	private String memberFirstnameTh;
	private String memberLastnameTh;
	private String memberFirstnameEn;
	private String memberLastnameEn;
	private Long memberGender;
	private Date memberBirthdate;
	private String memberPhoneno;
	private Long maxEducation;
	private Long currentJob;
	private Boolean receivedNews;
	private Long moodleUserId;
	private String moodlePassword;
	private String filename;
	private String prefix;
	private Long module;
	private String virtualTranscriptPdfPath;
	private String virtualTranscriptVerify;
	private String learningOutcomesPdfPath;
	private String learningOutcomesVerify;
	private String experienceTranscriptPdfPath;
	private String experienceTranscriptVerify;

	private Long degreeCourseTaken;
	private Long nonDegreeCourseTaken;
	private Long finishCourseTaken;
	private Long creditBank;
	private String lastRegisterConsentVersion;
	private Long lastJobGoal;
	private String forgetPasswordToken;
	private String buasriId;
	private Date buasriIdDate;
	private Date buasriIdExpireDate;
	private Date buasriIdCreateDate;
	private Boolean buasriActiveStatus;

	private String memberNo;
	private Date lastLoginDatetime;
	
	private String virtualTranscriptPdfPathTh;
	private String virtualTranscriptPdfPathEn;
	private String learningOutcomesPdfPathTh;
	private String learningOutcomesPdfPathEn;
	private String experienceTranscriptPdfPathTh;
	private String experienceTranscriptPdfPathEn;
	private String virtualTranscriptVerifyTh;
	private String virtualTranscriptVerifyEn;
	private String learningOutcomesVerifyTh;
	private String learningOutcomesVerifyEn;
	private String experienceTranscriptVerifyTh;
	private String experienceTranscriptVerifyEn;
    
	
}
