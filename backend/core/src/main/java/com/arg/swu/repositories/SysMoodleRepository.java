package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.SysMoodle;

/**
 * 
 * @author sitthichaim
 *
 */
public interface SysMoodleRepository extends JpaRepository<SysMoodle, Long> {

	@Query("select sm from SysMoodle sm where sm.activeFlag = true ")
	public SysMoodle findActive();
}
