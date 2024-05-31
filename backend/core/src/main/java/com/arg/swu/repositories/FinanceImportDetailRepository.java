package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.FinanceImportDetail;
import com.arg.swu.models.FinancePayment;

/**
 * 
 * @author sutthiyapakc
 *
 */
public interface FinanceImportDetailRepository extends JpaRepository<FinanceImportDetail, Long> {
    public List<FinanceImportDetail> findByImpId(Long importId);

    @Query("select finImpDetail from FinanceImportDetail finImpDetail where finImpDetail.impId = :importId and (finImpDetail.gentPdfFlag is null or finImpDetail.gentPdfFlag = false) order by finImpDetail.detailId")
    public List<FinanceImportDetail> findByImpIdForGen(Long importId);
}