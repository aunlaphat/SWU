package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.NewsInfo;
import com.arg.swu.models.NotiInfo;
import java.util.List;
import java.util.Optional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface NotiInfoRepository extends JpaRepository<NotiInfo, Long> {
    
    @Query("select nif from NotiInfo nif where notiTopic = :notiTopic and activeFlag = :activeFlag")
    Optional<NotiInfo> findByNotiTopicAndActiveFlag(Long notiTopic, Boolean activeFlag);

}
