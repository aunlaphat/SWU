package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CourseInstructor;

import jakarta.transaction.Transactional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface CourseInstructorRepository extends JpaRepository<CourseInstructor, Long> {
	 
	@Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from course_instructor
    		where course_id = :courseId
    		""")
    public void deleteCourseInstructorByCourseId(Long courseId);
	
    @Query(nativeQuery = true, value = """
    		select count(*) 
    		from course_instructor ci 
    		where ci.instructor_main = true
    		and ci.course_id = :courseId
    		""")
	public int countCourseInstructorMain(Long courseId);
	
    @Query(nativeQuery = true, value = """
    		select count(*) 
    		from course_instructor ci 
    		where ci.instructor_main = true
    		and ci.course_id = :courseId
    		and ci.course_instructor_id != :courseInstructorId
    		""")
	public int countEditCourseInstructorMain(Long courseId, Long courseInstructorId);

	public List<CourseInstructor> findByCourseId(Long courseId);
	
}
