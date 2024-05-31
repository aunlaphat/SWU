package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.SysSyncData;

/**
 * 
 * @author sitthichaim
 *
 */
public interface SysSyncDataRepository extends JpaRepository<SysSyncData, Long> {

	@Query("SELECT ssd FROM SysSyncData ssd WHERE ssd.tableName = :tableName")
	public SysSyncData findByTableName(String tableName);
	
}
