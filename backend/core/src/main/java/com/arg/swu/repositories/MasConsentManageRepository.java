package com.arg.swu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arg.swu.models.MasConsentManage;

public interface MasConsentManageRepository extends JpaRepository<MasConsentManage, Long> {

    public MasConsentManage findByFormTypeAndConsentStatus(String formType, Boolean consentStatus);

}
