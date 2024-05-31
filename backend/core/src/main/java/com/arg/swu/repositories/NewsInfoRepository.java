package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arg.swu.models.NewsInfo;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

/**
 * 
 * @author sitthichaim
 *
 */
public interface NewsInfoRepository extends JpaRepository<NewsInfo, Long> {
    
    @Query(nativeQuery = true, value = """
	select * from news_info where news_hilight =:newsHilight""")
	public List<NewsInfo> findBynewsHilight(Long newsHilight);
    
}
