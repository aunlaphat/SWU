package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arg.swu.models.TmpFinanceImportDetail;
import java.util.List;


/**
 * 
 * @author sitthichaim
 *
 */
public interface TmpFinanceImportDetailRepository extends JpaRepository<TmpFinanceImportDetail, Long> {

	public List<TmpFinanceImportDetail> findByTmpImpId(Long tmpImpId);
	
}