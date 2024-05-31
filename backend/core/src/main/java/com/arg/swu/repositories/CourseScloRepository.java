package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CourseSclo;

import jakarta.transaction.Transactional;
import java.util.List;


/**
 * 
 * @author sitthichaim
 *
 */
public interface CourseScloRepository extends JpaRepository<CourseSclo, Long> {

	@Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from course_sclo
    		where course_id = :courseId
    		""")
    public void deleteCourseScloByCourseId(Long courseId);
	
	public List<CourseSclo> findByCourseId(Long courseId);
	
}