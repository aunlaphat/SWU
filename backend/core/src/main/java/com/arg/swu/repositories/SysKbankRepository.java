package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.SysKbank;

/**
 * 
 * @author sitthichaim
 *
 */
public interface SysKbankRepository extends JpaRepository<SysKbank, Long> {

	@Query("select sk from SysKbank sk WHERE sk.activeFlag = true")
	public SysKbank findActive();
	
}
