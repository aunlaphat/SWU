package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arg.swu.models.MemberInfo;

import jakarta.transaction.Transactional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {

	@Query(nativeQuery = true, value = """
			select *
			from member_info
			where member_cardno = :memberCardno
			order by member_id desc
			limit 1
			""")
	public MemberInfo findOneByMemberCardno(String memberCardno);

	@Query(nativeQuery = true, value = """
			select count(*)
			from member_info
			where member_cardno = :memberCardno
			""")
	public int countMemberCardNo(String memberCardno);
    
	@Transactional
	@Modifying
	@Query("update MemberInfo minfo set minfo.virtualTranscriptPdfPathTh = :virtualTranscriptPdfPathTh , \n"
			+ "minfo.virtualTranscriptPdfPathEn = :virtualTranscriptPdfPathEn , \n"
			+ "minfo.learningOutcomesPdfPathTh = :learningOutcomesPdfPathTh, \n"
			+ "minfo.learningOutcomesPdfPathEn = :learningOutcomesPdfPathEn, \n"
			+ "minfo.experienceTranscriptPdfPathTh = :experienceTranscriptPdfPathTh, \n"
			+ "minfo.experienceTranscriptPdfPathEn = :experienceTranscriptPdfPathEn, \n"
			+ "minfo.virtualTranscriptVerifyTh = :virtualTranscriptVerifyTh, \n"
			+ "minfo.virtualTranscriptVerifyEn = :virtualTranscriptVerifyEn, \n"
			+ "minfo.learningOutcomesVerifyTh = :learningOutcomesVerifyTh, \n"
			+ "minfo.learningOutcomesVerifyEn = :learningOutcomesVerifyEn, \n"
			+ "minfo.experienceTranscriptVerifyTh = :experienceTranscriptVerifyTh, \n"
			+ "minfo.experienceTranscriptVerifyEn = :experienceTranscriptVerifyEn where minfo.memberId = :memberId")
	public void updateFileById(
		@Param("memberId")  Long memberId
		,@Param("virtualTranscriptPdfPathTh") String virtualTranscriptPdfPathTh
		,@Param("virtualTranscriptPdfPathEn") String virtualTranscriptPdfPathEn
		,@Param("learningOutcomesPdfPathTh") String learningOutcomesPdfPathTh
		,@Param("learningOutcomesPdfPathEn") String learningOutcomesPdfPathEn
		,@Param("experienceTranscriptPdfPathTh") String experienceTranscriptPdfPathTh
		,@Param("experienceTranscriptPdfPathEn") String experienceTranscriptPdfPathEn
		,@Param("virtualTranscriptVerifyTh") String virtualTranscriptVerifyTh
		,@Param("virtualTranscriptVerifyEn") String virtualTranscriptVerifyEn
		,@Param("learningOutcomesVerifyTh") String learningOutcomesVerifyTh
		,@Param("learningOutcomesVerifyEn") String learningOutcomesVerifyEn
		,@Param("experienceTranscriptVerifyTh") String experienceTranscriptVerifyTh
		,@Param("experienceTranscriptVerifyEn") String experienceTranscriptVerifyEn
	);
}
