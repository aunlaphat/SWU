package com.arg.swu.digitalsignatures.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.text.MessageFormat;
import java.util.Date;

import org.apache.tomcat.util.codec.binary.Base64;

import com.arg.swu.digitalsignatures.model.handler.DgtCertificationHandler;
import com.arg.swu.digitalsignatures.model.handler.DgtStatusFailHandler;
import com.arg.swu.digitalsignatures.model.handler.DgtStatusProcessHandler;
import com.arg.swu.digitalsignatures.model.handler.DgtStatusSuccessHandler;
import com.arg.swu.digitalsignatures.model.receive.DgtKeyStoreUnwrap;
import com.arg.swu.digitalsignatures.model.receive.RequestOrderSign;
import com.arg.swu.digitalsignatures.util.DgtSignStatusMsg;
// import com.itextpdf.kernel.geom.Rectangle;
// import com.itextpdf.signatures.DigestAlgorithms;
// import com.itextpdf.signatures.PdfSigner;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class DigitalsignatureProcess<C>{

    private C s;
    private Long consoleId;
    private DgtKeyStoreUnwrap keyStore;
    private Certificate[] chain;
    // private Rectangle ractangle;
    private PrivateKey pk;
    private String providerName; //BouncyCastleProvider
    private char[] pin;

    private DgtCertificationHandler dgtCertHandler;
    private DgtStatusFailHandler dgtStatusFailHandler;
    private DgtStatusProcessHandler dgtStatusProcessHandler;
    private DgtStatusSuccessHandler dgtStatusSuccessHandler;

    private byte[] fileSigned;
    private byte[] filePdfSrc;

    private String keepFile = "/Users/niwatroongroj/DocMy/Project/share/test_ca.pdf";

    public DigitalsignatureProcess sign(RequestOrderSign order) throws Exception 
    {
        //update status process
        if(this.dgtStatusProcessHandler != null)
        {
            this.consoleId = this.dgtStatusProcessHandler.onStatus(DgtSignStatusMsg.PROCESS
                , MessageFormat.format("Digital Signature doing for order tennant id {0} Process {1}", order.getTennantId(), new Date()));
        }

        log.info("###### Certificate doning ######"+ (this.dgtCertHandler != null));
        //Get certificate
        if(this.dgtCertHandler != null)
        {
            keyStore = this.dgtCertHandler.jks();
            this.chain = keyStore.getChain();
            this.pk = keyStore.getPrivateKey();
            this.pin = keyStore.getPin();
        }

        //Convert byte[] to file
        Date data = new Date();
        String fileNameOutput = MessageFormat.format("/{0}-{1}.pdf", data.getTime(), Math.random());    
        File inputFile = new File(System.getProperty("java.io.tmpdir")+fileNameOutput);
        log.info("========= OUT FILE NAME ========="+inputFile.getAbsolutePath());
         
        try (OutputStream stream = new FileOutputStream(inputFile)) {
            stream.write(this.filePdfSrc);
            stream.close();
        }

             
        
        //Sign
        Digitalsignature ds = new Digitalsignature();
        try {
            // ds.signApache(inputFile, null, pin);
            this.fileSigned = ds.signApache(                        
                        inputFile
                        , this.keyStore.getKeyStore()
                        ,this.pin);
            
            

        } catch ( IOException e) {
            log.error(e.getMessage(),e);
            //update status success
            if(this.dgtStatusFailHandler != null)
            {
                this.dgtStatusFailHandler.onStatus(this.consoleId ,DgtSignStatusMsg.FAIL
                    , MessageFormat.format("Digital Signature doing for order tennant id {0} Fail {1}", order.getTennantId(), new Date()));
            }
        } 
        finally 
        {
            // or
			if(Files.deleteIfExists(Path.of(inputFile.getPath()))){
				// success
			}else{
				// file does not exist
			}
        }

        //update status success
        if(this.dgtStatusSuccessHandler != null)
        {
            this.dgtStatusSuccessHandler.onStatus(this.consoleId, DgtSignStatusMsg.SUCCESS
                , MessageFormat.format("Digital Signature doing for order tennant id {0} Success {1}", order.getTennantId(), new Date()));
        }

        return this;
    }

    public byte [] toByteArray()
    {
        return this.fileSigned;
    }

    public String toBase64()
    {
        return Base64.encodeBase64String(this.fileSigned);
    }


}
