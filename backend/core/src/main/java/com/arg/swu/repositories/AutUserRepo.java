package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.AutUser;

/**
 * 
 * @author sitthichaim
 *
 */
public interface AutUserRepo extends JpaRepository<AutUser, Long>{

	public AutUser findByUsername(String username);
        public AutUser findByUserId(Long userid);
	@Query(nativeQuery = true, value = """
			select count(*)
			from aut_user
			where username = :username
			""")
	public int countUsername(String username);

	@Query(nativeQuery = true, value = """
			select count(*)
			from aut_user
			where username = :username
			and user_id != :userId
			""")
	public int countEditUsername(String username, Long userId);

	@Query(nativeQuery = true, value = """
			select count(*)
			from aut_user
			where ref_user_id = :refUserId
			""")
	public int countRefUserId(Long refUserId);
	
}
