package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.MasBankBranch;

/**
 * 
 * @author sitthichaim
 *
 */
public interface MasBankBranchRepository extends JpaRepository<MasBankBranch, Long> {
	
	@Query(nativeQuery = true, value = """
			select count(*)
			from mas_bank_branch
			where bank_branch_code = :bankBranchCode
			and bank_id = :bankId
			""")
	public int countBankBranchCode(String bankBranchCode, Long bankId);
	
	@Query(nativeQuery = true, value = """
			select count(*)
			from mas_bank_branch
			where bank_branch_code = :bankBranchCode
			and bank_id = :bankId
			and bank_branch_id != :bankBranchId
			""")
	public int countEditBankBranchCode(String bankBranchCode, Long bankId, Long bankBranchId);
	
}
