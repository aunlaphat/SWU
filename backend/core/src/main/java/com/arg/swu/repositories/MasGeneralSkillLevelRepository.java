/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.swu.repositories;

import com.arg.swu.models.MasGeneralSkillLevel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kumpeep
 */
public interface MasGeneralSkillLevelRepository extends JpaRepository<MasGeneralSkillLevel, Long> {

    public List<MasGeneralSkillLevel> findByGeneralSkillIdAndLevelNo(Long generalSkillId,Long levelNo);
    
	public List<MasGeneralSkillLevel> findByGeneralSkillId(Long generalSkillId);

}
