package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arg.swu.models.MasFeeConfig;

public interface MasFeeConfigRepository extends JpaRepository<MasFeeConfig, Long> {

    public List<MasFeeConfig> findByActiveFlagTrueOrderByFeeOrder();
    
}
