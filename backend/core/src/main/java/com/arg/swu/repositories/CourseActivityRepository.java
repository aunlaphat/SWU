package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CourseActivity;

import jakarta.transaction.Transactional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface CourseActivityRepository extends JpaRepository<CourseActivity, Long> {
	
	@Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from course_activity
    		where course_id = :courseId
    		""")
    public void deleteCourseActivityByCourseId(Long courseId);

	public List<CourseActivity> findByCourseId(Long courseId);
	
}