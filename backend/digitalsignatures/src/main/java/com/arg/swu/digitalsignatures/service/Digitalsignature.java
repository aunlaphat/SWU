package com.arg.swu.digitalsignatures.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.text.MessageFormat;

import org.apache.pdfbox.io.IOUtils;
import org.springframework.stereotype.Component;

// import com.itextpdf.forms.form.element.SignatureFieldAppearance;
// import com.itextpdf.io.image.ImageData;
// import com.itextpdf.io.image.ImageDataFactory;
// import com.itextpdf.kernel.font.PdfFont;
// import com.itextpdf.kernel.font.PdfFontFactory;
// import com.itextpdf.kernel.geom.Rectangle;
// import com.itextpdf.kernel.pdf.PdfReader;
// import com.itextpdf.kernel.pdf.StampingProperties;
// import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
// import com.itextpdf.layout.properties.BackgroundImage;
// import com.itextpdf.layout.properties.BackgroundPosition;
// import com.itextpdf.layout.properties.BackgroundRepeat;
// import com.itextpdf.layout.properties.BackgroundSize;
// import com.itextpdf.layout.properties.UnitValue;
// import com.itextpdf.signatures.BouncyCastleDigest;
// import com.itextpdf.signatures.IExternalDigest;
// import com.itextpdf.signatures.IExternalSignature;
// import com.itextpdf.signatures.PdfSigner;
// import com.itextpdf.signatures.PrivateKeySignature;

import lombok.extern.slf4j.Slf4j;
import java.util.Date;

@Component
@Slf4j
public class Digitalsignature {

//     private PdfFont fontSign;
    public void loadKey() throws Exception
    {
 
            KeyStore keyStore = KeyStore.getInstance("JKS");
            
            keyStore.load(getClass().getResourceAsStream("/sender_keystore.jks"), "changeit".toCharArray());
            PrivateKey privateKey = 
             (PrivateKey) keyStore.getKey("senderKeyPair", "changeit".toCharArray());

            log.info(privateKey.getAlgorithm());
        
    }

    public byte[] signApache(File src, KeyStore keystore, char[] pin)
            throws GeneralSecurityException, IOException {

        Date data = new Date();
        String fileNameOutput = MessageFormat.format("/{0}-{1}.pdf", data.getTime(), Math.random());        
        File outFile = new File(System.getProperty("java.io.tmpdir")+fileNameOutput);
        log.info("###### FILE TIME OUTPUT ######");
        log.info(src.getAbsolutePath());
        log.info(outFile.getAbsolutePath());
        // sign PDF
        ApacheCreateSignature signing = new ApacheCreateSignature(keystore, pin);
        signing.setExternalSigning(true);

        // File inFile = new File(getClass().getResource(C2_01_SignHelloWorld.SRC).getFile());
        // String name = inFile.getName();
        // String substring = name.substring(0, name.lastIndexOf('.'));

        // File outFile = new File(inFile.getParent(), substring + "_xyzsigned.pdf");
         
        signing.signDetached(src, outFile);

        
        FileInputStream file = new FileInputStream(outFile);
        byte[] bytes = IOUtils.toByteArray(file); 
        
        // or
        if(Files.deleteIfExists(Path.of(outFile.getPath()))){
                // success
        }else{
                // file does not exist
        }
        return bytes;
    }

//     public Digitalsignature sign(InputStream src, String name, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm,
//             String provider, PdfSigner.CryptoStandard signatureType, String reason, String location)
//             throws GeneralSecurityException, IOException {
//         PdfReader reader = new PdfReader(src);
//         reader.setUnethicalReading(true);

//         PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest), new StampingProperties());
       
//         // Create the signature appearance
//         Rectangle rect = new Rectangle(360, 670, 300, 120);
       
         


//         //Image singer
//         ImageData singerImage = ImageDataFactory.create(getClass().getResourceAsStream("/signers/signer.gif").readAllBytes());
       
//         // Create the signature appearance
//         signer
//                 .setReason(reason)
//                 .setLocation(location)
//                 .setPageRect(rect)
//                 .setPageNumber(1)
//                 .setFieldName("sig");
//                 signer.getSignatureField().setReuseAppearance(false);        

//         log.info(" FIELND NAME "+ signer.getFieldName());
//         //Get Field name
//         SignatureFieldAppearance apf = new SignatureFieldAppearance(signer.getFieldName());               

//         // apf.setContent("เอกสารฉบับนี้ได้รับการรับรองทางอิเล็กทรอนิก");
//         apf.setContent(singerImage);        
//         apf.setFontSize(8);
//         apf.setFont(fontSign);

        /* Background img */
        /* 
        BackgroundSize size = new BackgroundSize();
        size.setBackgroundSizeToValues(UnitValue.createPointValue(singerImage.getWidth()),
                UnitValue.createPointValue(singerImage.getHeight()));

        BackgroundPosition backgroundPosition = new BackgroundPosition();

        backgroundPosition.setPositionX(BackgroundPosition.PositionX.CENTER)
                .setPositionY(BackgroundPosition.PositionY.CENTER);
        
        apf.setBackgroundImage(new BackgroundImage.Builder()
                                .setBackgroundRepeat(new BackgroundRepeat(BackgroundRepeat.BackgroundRepeatValue.NO_REPEAT))
                                .setBackgroundPosition(backgroundPosition)
                                .setBackgroundSize(size)
                                .setImage(new PdfImageXObject(singerImage)).build());
        */

        //Append
//         signer.setSignatureAppearance(apf);
    

//         IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
//         IExternalDigest digest = new BouncyCastleDigest();
 
//         // Sign the document using the detached mode, CMS or CAdES equivalent.
//         signer.signDetached(digest, pks, chain, null, null, null, 0, signatureType);

//         return this;
//     }
    
//     public Digitalsignature signWording(InputStream src, String name, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm,
//             String provider, PdfSigner.CryptoStandard signatureType, String reason, String location)
//             throws GeneralSecurityException, IOException {
//         PdfReader reader = new PdfReader(src);
//         reader.setUnethicalReading(true);

//         PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest), new StampingProperties());
       
//         // Create the signature appearance
//         Rectangle rect = new Rectangle(360, 470, 400, 100);
       
         


        //Image singer
        // ImageData singerImage = ImageDataFactory.create(getClass().getResourceAsStream("/signers/signer.gif").readAllBytes());
       
        // Create the signature appearance
        // signer
        //         .setReason(reason)
        //         .setLocation(location)
        //         .setPageRect(rect)
        //         .setPageNumber(1)
        //         .setFieldName("sig");
        //         signer.getSignatureField().setReuseAppearance(false);        

        // log.info(" FIELND NAME "+ signer.getFieldName());
        // //Get Field name
        // SignatureFieldAppearance apf = new SignatureFieldAppearance(signer.getFieldName());               

        // apf.setContent("เอกสารฉบับนี้ได้รับการรับรองทางอิเล็กทรอนิก");
        // // apf.setContent(singerImage);        
        // apf.setFontSize(14);
        // apf.setFont(fontSign);

        /* Background img */
        /* 
        BackgroundSize size = new BackgroundSize();
        size.setBackgroundSizeToValues(UnitValue.createPointValue(singerImage.getWidth()),
                UnitValue.createPointValue(singerImage.getHeight()));

        BackgroundPosition backgroundPosition = new BackgroundPosition();

        backgroundPosition.setPositionX(BackgroundPosition.PositionX.CENTER)
                .setPositionY(BackgroundPosition.PositionY.CENTER);
        
        apf.setBackgroundImage(new BackgroundImage.Builder()
                                .setBackgroundRepeat(new BackgroundRepeat(BackgroundRepeat.BackgroundRepeatValue.NO_REPEAT))
                                .setBackgroundPosition(backgroundPosition)
                                .setBackgroundSize(size)
                                .setImage(new PdfImageXObject(singerImage)).build());
        */

        //Append
        // signer.setSignatureAppearance(apf);
    

        // IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
        // IExternalDigest digest = new BouncyCastleDigest();
 
        // // Sign the document using the detached mode, CMS or CAdES equivalent.
        // signer.signDetached(digest, pks, chain, null, null, null, 0, signatureType);

//         return this;
//     }

//     public ByteArrayOutputStream signWordingSwu(InputStream src, String name, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm,
//             String provider, PdfSigner.CryptoStandard signatureType, String reason, String location)
//             throws GeneralSecurityException, IOException {
//         PdfReader reader = new PdfReader(src);
//         reader.setUnethicalReading(true);

//         ByteArrayOutputStream keepFileOutput = null;
//         if(dest != null)
//         {
//                 keepFileOutput = new ByteArrayOutputStream();
//         }
//         else //to temp
//         {
//                 // Date data = new Date();
//                 // String fileNameOutput = MessageFormat.format("/{0}-{1}.pdf", data.getTime(), Math.random());
//                 // keepFileOutput = new FileOutputStream(System.getProperty("java.io.tmpdir")+fileNameOutput);
//                 keepFileOutput = new ByteArrayOutputStream();
//         }
         

//         PdfSigner signer = new PdfSigner(reader, keepFileOutput, new StampingProperties());
       
//         // Create the signature appearance
//         Rectangle rect = new Rectangle(470, 300, 300, 400);
       
         


//         //Image singer
//         ImageData singerImage = ImageDataFactory.create(getClass().getResourceAsStream("/signers/signer.gif").readAllBytes());
       
//         // Create the signature appearance
//         signer
//                 .setReason(reason)
//                 .setLocation(location)
//                 .setPageRect(rect)
//                 .setPageNumber(1)
//                 .setFieldName("sig");
//                 signer.getSignatureField().setReuseAppearance(false);        

//         log.info(" FIELND NAME "+ signer.getFieldName());
//         //Get Field name
//         SignatureFieldAppearance apf = new SignatureFieldAppearance(signer.getFieldName());               

//         apf.setContent("เอกสารฉบับนี้ได้รับรองลายเซ็นอิเล็กทรอนิกส์");
//         // apf.setContent(singerImage);        
//         apf.setFontSize(10);
//         apf.setFont(fontSign);

//         //Append
//         signer.setSignatureAppearance(apf);
    

//         IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
//         IExternalDigest digest = new BouncyCastleDigest();
 
//         // Sign the document using the detached mode, CMS or CAdES equivalent.
//         signer.signDetached(digest, pks, chain, null, null, null, 0, signatureType);
        
//         return keepFileOutput;
//     }



//     public Digitalsignature signCanvas(InputStream src, String name, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm,
//             String provider, PdfSigner.CryptoStandard subfilter, String reason, String location) throws GeneralSecurityException, IOException {
//         PdfReader reader = new PdfReader(src);
//         reader.setUnethicalReading(true);

//         PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest), new StampingProperties());

//         signer            
//             .setReason(reason)
//             .setLocation(location)
//             .setFieldName(name);
            

//         // Set a custom text and a background image
//         ImageData imageData = ImageDataFactory.create(getClass().getResourceAsStream("/signers/signer.png").readAllBytes());
//         SignatureFieldAppearance appearance = new SignatureFieldAppearance(signer.getFieldName());
//         appearance.setContent("This document was signed by Bruno Specimen");
//         appearance.setFont(fontSign);
//         BackgroundSize size = new BackgroundSize();
//         size.setBackgroundSizeToValues(UnitValue.createPointValue(imageData.getWidth()),
//                 UnitValue.createPointValue(imageData.getHeight()));

//         BackgroundPosition backgroundPosition = new BackgroundPosition();

//         backgroundPosition.setPositionX(BackgroundPosition.PositionX.CENTER)
//                 .setPositionY(BackgroundPosition.PositionY.CENTER);

//         appearance.setBackgroundImage(new BackgroundImage.Builder()
//                 .setImage(new PdfImageXObject(imageData))
//                 .setBackgroundRepeat(new BackgroundRepeat(BackgroundRepeat.BackgroundRepeatValue.NO_REPEAT))
//                 .setBackgroundPosition(backgroundPosition)
//                 .setBackgroundSize(size).build());

//         signer.setSignatureAppearance(appearance);

//         PrivateKeySignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
//         IExternalDigest digest = new BouncyCastleDigest();
//         signer.signDetached(digest, pks, chain, null, null, null, 0, subfilter);

//         return this;
//     }


//     public Digitalsignature font() throws IOException
//     {
//         fontSign = PdfFontFactory.createFont(getClass().getResourceAsStream("/fonts/Sarabun-Light.ttf")
//                 .readAllBytes(), "cp874");
//         return this;
//     }

}
