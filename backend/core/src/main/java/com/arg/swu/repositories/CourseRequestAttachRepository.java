package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CourseRequestAttach;

import jakarta.transaction.Transactional;
import java.util.List;


/**
 * 
 * @author sitthichaim
 *
 */
public interface CourseRequestAttachRepository extends JpaRepository<CourseRequestAttach, Long> {
	
	@Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from course_request_attach
    		where course_id = :courseId
    		""")
    public void deleteCourseRequestAttachByCourseId(Long courseId);
	
	public List<CourseRequestAttach> findByCourseId(Long courseId);

}
