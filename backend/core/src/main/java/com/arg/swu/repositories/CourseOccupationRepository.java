package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CourseOccupation;

import jakarta.transaction.Transactional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface CourseOccupationRepository extends JpaRepository<CourseOccupation, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from course_occupation
    		where course_id = :courseId
    		""")
    public void deleteCourseOccupationWhereCourseId(Long courseId);
    
    public List<CourseOccupation> findByCourseId(Long courseId);
	
}
