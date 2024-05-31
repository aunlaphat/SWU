package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CoursepublicAttach;

import jakarta.transaction.Transactional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface CoursepublicAttachRepository extends JpaRepository<CoursepublicAttach, Long> {

	@Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from coursepublic_attach
    		where coursepublic_id = :coursepublicId
    		""")
    public void deleteCoursepublicAttachByCoursepublicId(Long coursepublicId);
	
}
