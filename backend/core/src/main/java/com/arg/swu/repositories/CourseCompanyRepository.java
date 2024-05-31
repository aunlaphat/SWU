package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CourseCompany;

import jakarta.transaction.Transactional;
import java.util.List;


/**
 * 
 * @author sitthichaim
 *
 */
public interface CourseCompanyRepository extends JpaRepository<CourseCompany, Long> {
	
	@Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from course_company
    		where course_id = :courseId
    		""")
    public void deleteCourseCompanyByCourseId(Long courseId);

	public List<CourseCompany> findByCourseId(Long courseId);
	
}
