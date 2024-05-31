package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.MasBankAccount;

/**
 * 
 * @author sitthichaim
 *
 */
public interface MasBankAccountRepository extends JpaRepository<MasBankAccount, Long> {

	@Query("select mba from MasBankAccount mba WHERE mba.activeFlag = true")
	public MasBankAccount findActive();
	
}
