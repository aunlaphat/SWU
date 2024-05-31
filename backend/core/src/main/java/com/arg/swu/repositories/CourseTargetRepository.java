package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CourseTarget;

import jakarta.transaction.Transactional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface CourseTargetRepository extends JpaRepository<CourseTarget, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from course_target
    		where course_id = :courseId
    		""")
    public void deleteCourseTargetWhereCourseId(Long courseId);
    
    public List<CourseTarget> findByCourseId(Long courseId);
    
}
