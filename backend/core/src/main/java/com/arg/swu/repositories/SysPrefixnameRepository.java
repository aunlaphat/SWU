package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.SysPrefixname;


/**
 * 
 * @author sitthichaim
 *
 */
public interface SysPrefixnameRepository extends JpaRepository<SysPrefixname, Long> {

	@Query(nativeQuery = true, value = """
			select *
			from sys_prefixname
			where prefixname_name_th = :prefixnameNameTh
			order by prefixname_id asc
			limit 1
			""")
	public SysPrefixname findByPrefixnameNameTh(String prefixnameNameTh);
}
