package com.arg.swu.provider;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.arg.swu.dto.UploadFileData;
import com.arg.swu.models.FinancePayment;
import com.arg.swu.provider.handler.SignProvider;
import com.arg.swu.repositories.FinancePaymentRepository;
import com.arg.swu.services.FileStorageService;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
public class FinancePaymentCopyProvider extends SignProvider{

    @Autowired
    private FileStorageService fss;
    @Autowired 
    private FinancePaymentRepository financePaymentRepo;

    @Value("${default.path}")
	private String defaultPath;

    private Long financePaymentId;
    private FinancePayment data;

    private String fileNameScr;
    private String fileNameDes;
    private String pathModuleSrc;

    @Override
    public void builderData() {
        this.data = financePaymentRepo.findById(financePaymentId).orElse(null);		 
		this.fileNameScr = data.getReceiptCopyNonCaPath();
        if(moduleFile == null)
        {
            Assert.notNull(moduleFile, "Request module file id [moduleFile]");
        }
    }

    @Override
    public String srcFileBase64() throws Exception {

        String fileSrc = this.defaultPath+this.fileNameScr;
        log.info("### FILE SRC "+ fileSrc);
        InputStream fileInputStream = new FileInputStream(this.defaultPath+this.fileNameScr);
		
		return Base64.getEncoder().encodeToString(fileInputStream.readAllBytes());
    }

    @Override
    public void updateFileResult(byte[] file) throws Exception {
        
        String fileName = this.fileNameScr.substring(this.fileNameScr.lastIndexOf("/")+1);

        String [] fileExt = fileName.split("\\.");
 
        this.fileNameDes = fileExt[0]+"-signed."+fileExt[1];
        log.info(" FILE NAME fileNameDes #### "+ fileNameDes);
        UploadFileData rest;
       
            rest = fss.saveByteArrayToStorage(file, this.fileNameDes, this.moduleFile);
            data.setReceiptCopyCaPath(rest.getFullpath());  
        
        financePaymentRepo.save(data);
    }
}
