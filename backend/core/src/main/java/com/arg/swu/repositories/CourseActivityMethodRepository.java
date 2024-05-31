package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CourseActivityMethod;

import jakarta.transaction.Transactional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface CourseActivityMethodRepository extends JpaRepository<CourseActivityMethod, Long> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from course_activity_method
    		where course_id = :courseId
    		""")
    public void deleteCourseActivityMethodByCourseId(Long courseId);
    
    
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from course_activity_method
    		where course_activity_id = :courseActivityId
    		""")
    public void deleteCourseActivityMethodByCourseActivityId(Long courseActivityId);
    
    public List<CourseActivityMethod> findByCourseActivityId(Long courseId);
    
    public List<CourseActivityMethod> findByCourseId(Long courseId);
    
}
