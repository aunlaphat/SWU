package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.FinanceImportLog;

/**
 * 
 * @author sutthiyapakc
 *
 */
public interface FinanceImportLogRepository extends JpaRepository<FinanceImportLog, Long> {

	public FinanceImportLog findByCoursepublicId(Long coursepublicId);

	 
	public List<FinanceImportLog> findByStatus(String status);

	
}