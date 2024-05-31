package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.AutUserRole;
import com.arg.swu.models.AutUserRolePK;

import jakarta.transaction.Transactional;

/**
 * 
 * @author sitthichaim
 *
 */
public interface AutUserRoleRepository extends JpaRepository<AutUserRole, AutUserRolePK> {
	
    @Query("SELECT aur FROM AutUserRole aur WHERE aur.id.userId = :userId")
    public List<AutUserRole> findByUserId(Long userId);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM AutUserRole aur WHERE aur.id.userId = :userId")
    public void deleteByUserId(Long userId);

}
