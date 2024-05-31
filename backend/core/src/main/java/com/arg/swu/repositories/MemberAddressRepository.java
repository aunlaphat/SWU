package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arg.swu.models.MemberAddress;

/**
 * 
 * @author sitthichaim
 *
 */
public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {

	public MemberAddress findByMemberId(Long memberId);
	
}
