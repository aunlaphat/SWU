package com.arg.swu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arg.swu.models.MasConsentDisclose;

public interface MasConsentDiscloseRepository extends JpaRepository<MasConsentDisclose, Long> {

    public List<MasConsentDisclose> findAll();
    public List<MasConsentDisclose> findByConsentId(Long consentId);

}
