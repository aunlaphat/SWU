package com.arg.swu.repositories;

import com.arg.swu.dto.MasSharePercentData;
import org.springframework.data.jpa.repository.JpaRepository;

import com.arg.swu.models.MasSharePercent;
import java.util.List;

/**
 * 
 * @author sitthichaim
 *
 */
public interface MasSharePercentRepository extends JpaRepository<MasSharePercent, Long> {
    public List<MasSharePercent> findByDepId(Long depId);
}
