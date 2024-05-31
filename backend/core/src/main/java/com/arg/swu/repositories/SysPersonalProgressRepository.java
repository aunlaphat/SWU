package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.SysPersonalProgress;

/**
 * 
 * @author sitthichaim
 *
 */
public interface SysPersonalProgressRepository extends JpaRepository<SysPersonalProgress, Long> {

	@Query("select spp from SysPersonalProgress spp WHERE spp.activeFlag = true")
	public SysPersonalProgress findActive();
	
}
