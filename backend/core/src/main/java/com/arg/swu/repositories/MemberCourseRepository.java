package com.arg.swu.repositories;

import com.arg.swu.models.FinancePayment;
import com.arg.swu.models.MemberCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import java.util.List;


/**
 * 
 * @author sitthichaim
 *
 */
public interface MemberCourseRepository extends JpaRepository<MemberCourse, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE MemberCourse mc SET mc.paymentStatus = :paymentStatus, mc.studyStatus = :studyStatus WHERE mc.memberCourseId = :memberCourseId")
    public void updateMemberCourse(Long paymentStatus, Long studyStatus, Long memberCourseId);
    
    @Query(nativeQuery = true, value = """
    		select *
    		from member_course mc
    		where mc.study_status = :studyStatus
    		and mc.gen_pdf_flag is null or mc.gen_pdf_flag = false
    		""")
    public List<MemberCourse> findByStudyStatusAndGenPdfFlag(Long studyStatus);

    @Query(nativeQuery = true, value = """
    		select *
    		from member_course mc
    		where mc.study_status = :studyStatusId
            and mc.coursepublic_id = :coursepublicId
    		and mc.gen_pdf_flag is null or mc.gen_pdf_flag = false
    		""")
    public List<MemberCourse> findByCoursepublicId(Long studyStatusId, Long coursepublicId);
    
    public List<MemberCourse> findByCoursepublicId(Long coursepublicId);

    public MemberCourse findByDetailImpId(Long id);
}
