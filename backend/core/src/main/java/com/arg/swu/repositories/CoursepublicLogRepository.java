package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.CoursepublicLog;

import jakarta.transaction.Transactional;
import java.util.List;

/**
 * 
 * @author sitthichaim
 *
 */
public interface CoursepublicLogRepository extends JpaRepository<CoursepublicLog, Long> {

	@Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    		delete from coursepublic_log
    		where coursepublic_id = :coursepublicId
    		""")
    public void deleteCoursepublicLogByCoursepublicId(Long coursepublicId);
	
    public List<CoursepublicLog> findByCoursepublicIdAndCoursepublicStatus(Long coursepublicId,Long coursepublicStatus);
    
    public List<CoursepublicLog> findByCoursepublicId(Long coursepublicId);
    
}
