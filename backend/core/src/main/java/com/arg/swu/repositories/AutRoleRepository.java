package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.AutRole;

/**
 * 
 * @author sitthichaim
 *
 */
public interface AutRoleRepository extends JpaRepository<AutRole, Long> {

	@Query(nativeQuery = true, value = """
			select count(*)
			from aut_role
			where name = :name
			""")
	public int countRoleName(String name);

	@Query(nativeQuery = true, value = """
			select count(*)
			from aut_role
			where name = :name
			and role_id != :roleId
			""")
	public int countEditRoleName(String name, Long roleId);
}
