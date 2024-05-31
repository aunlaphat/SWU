package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.MasGeneralSkill;
import java.util.List;


/**
 * 
 * @author sitthichaim
 *
 */
public interface MasGeneralSkillRepository extends JpaRepository<MasGeneralSkill, Long> {
	

	@Query(nativeQuery = true, value = """
			select count(*)
			from mas_general_skill
			where general_skill_code = :genealSkillCode
			""")
	public int countGeneralSkillCode(String genealSkillCode);
	
	@Query(nativeQuery = true, value = """
			select count(*)
			from mas_general_skill
			where general_skill_code = :genealSkillCode
			and general_skill_id != :generalSkillId
			""")
	public int countEditGeneralSkillCode(String genealSkillCode, Long generalSkillId);

}
