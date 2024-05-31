package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CourseMain;

import jakarta.transaction.Transactional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface CourseMainRepository extends JpaRepository<CourseMain, Long> {
	
	@Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from course_main
    		where course_id = :courseId
    		""")
    public void deleteCourseMainByCourseId(Long courseId);

	@Query(nativeQuery = true, value = """
			select count (*) + 1 
			from course_main cm 
			where course_id = :courseId
			""")
	public int countVersion(Long courseId);
	
}
