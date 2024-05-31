package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CourseLog;

import jakarta.transaction.Transactional;
import java.util.List;


/**
 * 
 * @author sitthichaim
 *
 */
public interface CourseLogRepository extends JpaRepository<CourseLog, Long> {
	
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from course_log
    		where course_id = :courseId
    		""")
    public void deleteCourseLogByCourseId(Long courseId);
    
    public List<CourseLog> findByCourseId(Long courseId);

}
