package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.SysSyncProgress;

/**
 * 
 * @author sitthichaim
 *
 */
public interface SysSyncProgressRepository extends JpaRepository<SysSyncProgress, Long> {

	@Query("SELECT ssp FROM SysSyncProgress ssp WHERE ssp.tableName = :tableName")
	public SysSyncProgress findByTableName(String tableName);
	
}
