package com.arg.swu.digitalsignatures.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arg.swu.digitalsignatures.model.DgtCertificate;
import com.arg.swu.digitalsignatures.model.DgtSigner;
import com.arg.swu.digitalsignatures.model.DgtTenantProvider;
import com.arg.swu.digitalsignatures.repositories.DgtCertificateRepo;
import com.arg.swu.digitalsignatures.repositories.DgtSignerRepo;
import com.arg.swu.digitalsignatures.repositories.DgtTenantProviderRepo;
import com.arg.swu.digitalsignatures.util.KeystoreUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JavaKeyStoreGenerate 
{
    @Autowired
    private DgtTenantProviderRepo tn;
    @Autowired
    private DgtCertificateRepo certRepo;
    @Autowired
    private DgtSignerRepo signerRepo;
    
    public void registerCertificate() {
        
        try {
            KeystoreUtils ku = KeystoreUtils.builder().build();
            String valueBase64 = ku.loadFromResource("/ssl/swufull/keystore/swu.ac.co.th.jks").generateToBase64();
            String storePassBase64 = KeystoreUtils.builder().rawdata("Dbase*@i012345".getBytes()).build().generateToBase64();
            
            log.info(valueBase64);
            DgtTenantProvider pv = DgtTenantProvider.builder()
                                    .companyCode("SWU")
                                    .activeFlag(Boolean.FALSE)
                                    .nameLo("มหาวิทยาลัยศรีนครินทรวิโรฒ")
                                    .nameEn("Srinakharinwirot University")
                                    .typeName("EDU")
                                    .address1("114 Sukhumvit 23, Bangkok 10110, Thailand. Tel +66 2 649 5000, Fax +66 2 258 4007 e-mail : contact@g.swu.ac.th")
                                    .registrationDate(new Date())
                                    
                                    .build();
                                
            tn.save(pv);

            DgtCertificate cert = DgtCertificate.builder()
                                    .activeFlag(Boolean.TRUE)
                                    .issueDate(new Date())
                                    .expiryDate(null)
                                    .jksData(valueBase64)
                                    .jksPrivateKeyPass(storePassBase64)
                                    .jksStorePass(storePassBase64)
                                
                                    .tentantProvider(pv)
                                    .build();
            certRepo.save(cert);

            DgtSigner signer = DgtSigner.builder()
                                        .active(Boolean.TRUE)
                                        .certificate(cert)
                                        .positionX(360)
                                        .positionY(360)
                                        .width(100)
                                        .height(100)
                                        .build();

            signerRepo.save(signer);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
