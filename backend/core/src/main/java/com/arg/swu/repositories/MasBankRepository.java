package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arg.swu.models.MasBank;

/**
 * 
 * @author sitthichaim
 *
 */
public interface MasBankRepository extends JpaRepository<MasBank, Long> {
	
	@Query(nativeQuery = true, value = """
			select count(*)
			from mas_bank
			where bank_code = :bankCode
			""")
	public int countBankCode(String bankCode);
	
	@Query(nativeQuery = true, value = """
			select count(*)
			from mas_bank
			where bank_code = :bankCode
			and bank_id != :bankId
			""")
	public int countEditBankCode(String bankCode, Long bankId);
	
}
