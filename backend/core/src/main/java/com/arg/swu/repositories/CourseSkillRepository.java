package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CourseSkill;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;


/**
 * 
 * @author sitthichaim
 *
 */
public interface CourseSkillRepository extends JpaRepository<CourseSkill, Long> {
	
	@Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from course_skill
    		where course_id = :courseId
    		""")
    public void deleteCourseSkillByCourseId(Long courseId);

	@Query(nativeQuery = true, value = """
			select count(*)
			from course_skill cs
			where 1 = 1 and cs.active_flag = true
			and course_id = :courseId
			""")
	public int countCourseSkillByCourseId(Long courseId);
	
	@Query(nativeQuery = true, value = """
			select count(*)
			from course_skill cs 
			where cs.course_id = :courseId
			and cs.general_skill_id = :generalSkillId
			""")
	public int countGeneralSkill(Long courseId, Long generalSkillId);
	
	@Query(nativeQuery = true, value = """
			select count(*)
			from course_skill cs 
			where cs.course_id = :courseId
			and cs.general_skill_id = :generalSkillId
			and cs.course_skill_id != :courseSkillId
			""")
	public int countEditGeneralSkill(Long courseId, Long generalSkillId, Long courseSkillId);
	
	public List<CourseSkill> findByCourseId(Long courseId);
	
	@Query(nativeQuery = true, value = """
			select coalesce(sum(cs.skill_weight), 0)
			from course_skill cs
			where 1 = 1
			and cs.course_id = :courseId
			""")
	public BigDecimal sumSkillWeight(Long courseId);
	
	@Query(nativeQuery = true, value = """
			select coalesce(sum(cs.skill_weight), 0)
			from course_skill cs
			where 1 = 1
			and cs.course_id = :courseId
			and cs.course_skill_id != :courseSkillId
			""")
	public BigDecimal sumEditSkillWeight(Long courseId, Long courseSkillId);
	
}
