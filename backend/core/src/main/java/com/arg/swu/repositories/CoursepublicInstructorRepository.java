package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CoursepublicInstructor;

import jakarta.transaction.Transactional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface CoursepublicInstructorRepository extends JpaRepository<CoursepublicInstructor, Long> {
	
	@Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from coursepublic_instructor
    		where coursepublic_id = :coursepublicId
    		""")
    public void deleteCoursepublicInstructorByCoursepublicId(Long coursepublicId);

    @Query(nativeQuery = true, value = """
    		select count(*) 
    		from coursepublic_instructor ci 
    		where ci.instructor_main = true
    		and ci.coursepublic_id = :coursepublicId
    		""")
	public int countCoursepublicInstructorMain(Long coursepublicId);
	
    @Query(nativeQuery = true, value = """
    		select count(*) 
    		from coursepublic_instructor ci 
    		where ci.instructor_main = true
    		and ci.coursepublic_id = :coursepublicId
    		and ci.coursepublic_instructor_id != :coursepublicInstructorId
    		""")
	public int countEditCoursepublicInstructorMain(Long coursepublicId, Long coursepublicInstructorId);

	public List<CoursepublicInstructor> findByCoursepublicId(Long coursepublicId);
    
}
