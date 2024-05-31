package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CoursepublicMedia;

import jakarta.transaction.Transactional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface CoursepublicMediaRepository extends JpaRepository<CoursepublicMedia, Long> {
	
	@Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from coursepublic_media
    		where coursepublic_id = :coursepublicId
    		""")
    public void deleteCoursepublicMediaByCoursepublicId(Long coursepublicId);

}
