package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CourseAttach;

import jakarta.transaction.Transactional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface CourseAttachRepository extends JpaRepository<CourseAttach, Long> {
	
	@Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from course_attach
    		where course_id = :courseId
    		""")
    public void deleteCourseAttachByCourseId(Long courseId);

	public List<CourseAttach> findByCourseId(Long courseId);
	
}
