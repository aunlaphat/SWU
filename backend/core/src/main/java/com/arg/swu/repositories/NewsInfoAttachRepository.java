package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.NewsInfoAttach;


/**
 * 
 * @author sitthichaim
 *
 */
public interface NewsInfoAttachRepository extends JpaRepository<NewsInfoAttach, Long> {

	@Query("""
			SELECT nia FROM NewsInfoAttach nia WHERE nia.newsId = :newsId
			""")
	public List<NewsInfoAttach> findByNewsId(Long newsId);
	
        public void deleteByFileNameTh(String fileNameTh);
        
        public void deleteByNewsId(Long newsId);
}
