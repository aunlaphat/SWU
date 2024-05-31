package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arg.swu.models.MemberConsent;

public interface MemberConsentRepository extends JpaRepository<MemberConsent, Long> {

}
