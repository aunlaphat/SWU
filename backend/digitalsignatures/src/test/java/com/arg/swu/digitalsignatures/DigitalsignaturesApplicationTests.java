package com.arg.swu.digitalsignatures;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.pdfbox.io.IOUtils;
import org.aspectj.lang.annotation.After;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.xmlunit.builder.Input;

import com.arg.swu.digitalsignatures.controller.DigitalsignatureController;
import com.arg.swu.digitalsignatures.model.DgtCertificate;
import com.arg.swu.digitalsignatures.model.DgtTenantProvider;
import com.arg.swu.digitalsignatures.model.receive.RequestOrderSign;
import com.arg.swu.digitalsignatures.repositories.DgtCertificateRepo;
import com.arg.swu.digitalsignatures.service.ApacheCreateSignature;
import com.arg.swu.digitalsignatures.service.C2_01_SignHelloWorld;
import com.arg.swu.digitalsignatures.service.Digitalsignature;
import com.arg.swu.digitalsignatures.service.JavaKeyStoreGenerate;
import com.fasterxml.jackson.databind.ObjectMapper;
// import com.itextpdf.signatures.DigestAlgorithms;
// import com.itextpdf.signatures.PdfSigner;
// import com.itextpdf.signatures.SignatureUtil;
// import com.itextpdf.io.exceptions.IOException;
// import com.itextpdf.kernel.pdf.PdfReader;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Date;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class DigitalsignaturesApplicationTests {

	@Autowired
	Digitalsignature ds;

        @Autowired
        DgtCertificateRepo certRepo;

        @Autowired
        JavaKeyStoreGenerate jksg;

        @Autowired
	private MockMvc mockMvc;


	// @Test
	// void contextLoads() {
	// }
        // @Test
        void testformatstring()
        {
                log.info("########### TEST FORMAT ###########");
                log.info(String.valueOf(Math.random()));
                Date date = new Date();
                log.info(String.valueOf(date.getTime()));
        }

        // @Test
        public void infoTest()
        throws Exception {
                ResultActions result = mockMvc.perform(get("/info"));

                // Assert
                result.andExpect(status().isOk());
        }

        // @Test
        public void sign() throws Exception {
                String srcPdf = Base64.getEncoder().encodeToString(getClass().getResourceAsStream("/pdfs/bill_bf_sign.pdf").readAllBytes());
                ResultActions result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/digitaisingature")                                
                                .content(asJsonString(RequestOrderSign.builder().tennantId(5L).rawBase64Pdf(srcPdf).build()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                
                        );

                // Assert
                result
                .andDo(print())
                .andExpect(status().isOk());


                // log.info("Enc "+ Base64.getEncoder().encodeToString("P@ssw0rd12345".getBytes()));
                
                // String decodedString = new String(Base64.getDecoder().decode("UEBzc3cwcmQxMjM0NQ=="));
                // log.info("Desc " +decodedString);


                // DgtCertificate cert =  certRepo.findByTentantProviderAndActiveFlag(3L, true);
                // InputStream jks = new ByteArrayInputStream(Base64.getDecoder().decode(cert.getJksData()));

        }

        public static String asJsonString(final Object obj) {
                try {
                        return new ObjectMapper().writeValueAsString(obj);
                } catch (Exception e) {
                        throw new RuntimeException(e);
                }
        }

        /* FOR GENERATE JKS TO DB */
//        @Test
        void loadFileJks()
        {
                jksg.registerCertificate();
        }

	// @Test
	// void testDigitalsignature() throws Exception
	// {
	// 	File file = new File(C2_01_SignHelloWorld.DEST);
        //         file.mkdirs();
        
        //         BouncyCastleProvider provider = new BouncyCastleProvider();
        //         Security.addProvider(provider);
        //         KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        //         // ks.load(getClass().getResourceAsStream("/sender_keystore.jks"), C2_01_SignHelloWorld.PASSWORD);
        //         ks.load(getClass().getResourceAsStream("/ssl/swu/keystore/swu.ac.co.th-only.jks"), "Dbase*@i012345".toCharArray());
        //         String alias = ks.aliases().nextElement();
        //         PrivateKey pk = (PrivateKey) ks.getKey(alias, "Dbase*@i012345".toCharArray());
        //         // PrivateKey pk = (PrivateKey) ks.getKey(alias, C2_01_SignHelloWorld.PASSWORD);
        //         Certificate[] chain = ks.getCertificateChain(alias);
                        
        //                 log.info("######## algorithms #######" );
        //                 log.info(pk.getAlgorithm());
        //                 log.info("######## algorithms #######");
        //                 String signatureName = "sign";
        //                 String signatureReason = "Custom appearance example";
        //                 String location = "Ghent";

        //                 PdfReader reader = new PdfReader(getClass().getResourceAsStream("/pdfs/bill_bf_sign.pdf"));
        //                 reader.setUnethicalReading(true);
        //                 log.info(String.valueOf(reader.getFileLength()));
        //                 // log.info(String.valueOf(reader.isOpenedWithFullPermission()));

        //                 ds.font()
        //                 .signWordingSwu(
        //                         getClass().getResourceAsStream(C2_01_SignHelloWorld.SRC), 
        //                         signatureName, C2_01_SignHelloWorld.DEST + C2_01_SignHelloWorld.RESULT_FILES[0], chain, pk,
        //                         DigestAlgorithms.SHA256, provider.getName(), PdfSigner.CryptoStandard.CMS,
        //                         signatureReason, location);

	// }

        // @Test
        void testFile() throws IOException
        {
                File outFile = new File("/Users/niwatroongroj/DocMy/Project/share/20240401004428-970.pdf");
                FileInputStream file = new FileInputStream(outFile);
                byte[] bytes = IOUtils.toByteArray(file); 
                System.out.println(bytes);
        }
        // @Test
        void testApacheDigitalsignature() throws Exception
        {
                 
        
                BouncyCastleProvider provider = new BouncyCastleProvider();
                Security.addProvider(provider);
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                // ks.load(getClass().getResourceAsStream("/sender_keystore.jks"), C2_01_SignHelloWorld.PASSWORD);
                keystore.load(getClass().getResourceAsStream("/ssl/by_vallop/jks/swucert2023.jks"), "Dbase*@i012345".toCharArray());
                String alias = keystore.aliases().nextElement();
                PrivateKey pk = (PrivateKey) keystore.getKey(alias, "Dbase*@i012345".toCharArray());
                // PrivateKey pk = (PrivateKey) ks.getKey(alias, C2_01_SignHelloWorld.PASSWORD);
                Certificate[] chain = keystore.getCertificateChain(alias);

                log.info("######## algorithms #######" );
                log.info(pk.getAlgorithm());
                log.info("######## algorithms #######");
                String signatureName = "sign";
                String signatureReason = "Custom appearance example";
                String location = "Ghent";


                // sign PDF
                ApacheCreateSignature signing = new ApacheCreateSignature(keystore, "Dbase*@i012345".toCharArray());
                signing.setExternalSigning(true);

                File inFile = new File(getClass().getResource(C2_01_SignHelloWorld.SRC).getFile());
                String name = inFile.getName();
                String substring = name.substring(0, name.lastIndexOf('.'));

                File outFile = new File(inFile.getParent(), substring + "_xyzsigned.pdf");
                
                log.info("...... "+ inFile.getParent());
                signing.signDetached(inFile, outFile);
        }

        // @Test
	// void testJksSwu2023Digitalsignature() throws Exception
	// {
	// 	File file = new File(C2_01_SignHelloWorld.DEST);
        //         file.mkdirs();
        
        //         BouncyCastleProvider provider = new BouncyCastleProvider();
        //         Security.addProvider(provider);
        //         KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        //         // ks.load(getClass().getResourceAsStream("/sender_keystore.jks"), C2_01_SignHelloWorld.PASSWORD);
        //         ks.load(getClass().getResourceAsStream("/ssl/by_vallop/jks/swucert2023.jks"), "Dbase*@i012345".toCharArray());
        //         String alias = ks.aliases().nextElement();
        //         PrivateKey pk = (PrivateKey) ks.getKey(alias, "Dbase*@i012345".toCharArray());
        //         // PrivateKey pk = (PrivateKey) ks.getKey(alias, C2_01_SignHelloWorld.PASSWORD);
        //         Certificate[] chain = ks.getCertificateChain(alias);

        //                 log.info("######## algorithms #######" );
        //                 log.info(pk.getAlgorithm());
        //                 log.info("######## algorithms #######");
        //                 String signatureName = "sign";
        //                 String signatureReason = "Custom appearance example";
        //                 String location = "Ghent";

        //                 PdfReader reader = new PdfReader(getClass().getResourceAsStream("/pdfs/bill_bf_sign.pdf"));
        //                 reader.setUnethicalReading(true);
        //                 log.info(String.valueOf(reader.getFileLength()));
        //                 // log.info(String.valueOf(reader.isOpenedWithFullPermission()));

        //                 ds.font()
        //                 .signWordingSwu(
        //                         getClass().getResourceAsStream(C2_01_SignHelloWorld.SRC), 
        //                         signatureName, C2_01_SignHelloWorld.DEST + C2_01_SignHelloWorld.RESULT_FILES[0], chain, pk,
        //                         DigestAlgorithms.SHA256, provider.getName(), PdfSigner.CryptoStandard.CMS,
        //                         signatureReason, location);

	// }

        // @Test
        // void testcert() throws Exception
        // {
        //         // CertificateFactory factory = CertificateFactory.getInstance("X.509");
        //         InputStream is = getClass().getResourceAsStream("/ssl/swu/swu.ac.co.th.crt");
        //         // You could get a resource as a stream instead.

        //         CertificateFactory cf = CertificateFactory.getInstance("X.509");
        //         X509Certificate caCert = (X509Certificate)cf.generateCertificate(is);

        //         TrustManagerFactory tmf = TrustManagerFactory
        //         .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        //         KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        //         ks.load(null); // You don't need the KeyStore instance to come from a file.
        //         ks.setCertificateEntry("caCert", caCert);

        //         tmf.init(ks);

        //         SSLContext sslContext = SSLContext.getInstance("TLS");
        //         sslContext.init(null, tmf.getTrustManagers(), null);

        //         /* Crt and private key */
        //         Certificate[] chain = new Certificate[1];
        //         chain[0] = ks.getCertificate("caCert");
        //         log.info("#### CERT ####");
        //         // InputStream privateKeyFile = getClass().getResourceAsStream("/ssl/swu/pkcs8_key.pem");
        //         PrivateKey rsaKey = readPKCS8PrivateKey("/ssl/swu/pkcs8_key.der");

        //         log.info("#### private key Algorithm #####");                
        //         log.info(rsaKey.getAlgorithm());
        //         log.info("#### private key Algorithm #####");

        //         /* end Crt and private key */

        //         String signatureName = "sign";
        //                 String signatureReason = "Custom appearance example";
        //                 String location = "Ghent";

        //         BouncyCastleProvider provider = new BouncyCastleProvider();
        //                 Security.addProvider(provider);

        //         ds.font()
        //              .signWording(
        //                 getClass().getResourceAsStream(C2_01_SignHelloWorld.SRC), 
        //                 signatureName, "/Users/niwatroongroj/DocMy/Project/share/test_ca.pdf", chain, rsaKey,
        //                 DigestAlgorithms.SHA256, provider.getName(), PdfSigner.CryptoStandard.CMS,
        //                 signatureReason, location);
        // }

        // @Test
        // void testcertvallop() throws Exception
        // {
        //         // CertificateFactory factory = CertificateFactory.getInstance("X.509");
        //         InputStream is = getClass().getResourceAsStream("/ssl/by_vallop/swucert2023.crt");
        //         // You could get a resource as a stream instead.

        //         CertificateFactory cf = CertificateFactory.getInstance("X.509");
        //         X509Certificate caCert = (X509Certificate)cf.generateCertificate(is);

        //         TrustManagerFactory tmf = TrustManagerFactory
        //         .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        //         KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        //         ks.load(null); // You don't need the KeyStore instance to come from a file.
        //         ks.setCertificateEntry("caCert", caCert);

        //         tmf.init(ks);

        //         SSLContext sslContext = SSLContext.getInstance("TLS");
        //         sslContext.init(null, tmf.getTrustManagers(), null);

        //         /* Crt and private key */
        //         Certificate[] chain = new Certificate[1];
        //         chain[0] = ks.getCertificate("caCert");
        //         log.info("#### CERT ####");
        //         // InputStream privateKeyFile = getClass().getResourceAsStream("/ssl/swu/pkcs8_key.pem");
        //         PrivateKey rsaKey = readPKCS8PrivateKey("/ssl/by_vallop/swucert.key.key");

        //         log.info("#### private key Algorithm #####");                
        //         log.info(rsaKey.getAlgorithm());
        //         log.info("#### private key Algorithm #####");

        //         /* end Crt and private key */

        //         String signatureName = "sign";
        //                 String signatureReason = "Custom appearance example";
        //                 String location = "Ghent";

        //         BouncyCastleProvider provider = new BouncyCastleProvider();
        //                 Security.addProvider(provider);

        //         ds.font()
        //              .signWording(
        //                 getClass().getResourceAsStream(C2_01_SignHelloWorld.SRC), 
        //                 signatureName, "/Users/niwatroongroj/DocMy/Project/share/test_ca_by_vallop.pdf", chain, rsaKey,
        //                 DigestAlgorithms.SHA256, provider.getName(), PdfSigner.CryptoStandard.CMS,
        //                 signatureReason, location);
        // }

        public PrivateKey readPKCS8PrivateKey(String file) throws Exception {
                // KeyFactory factory = KeyFactory.getInstance("RSA");
                
                // try (Reader keyReader = file;
                // PemReader pemReader = new PemReader(keyReader)) {

                //         // log.info(" PEM "+ pemReader.readPemObject());
                //         PemObject pemObject = pemReader.readPemObject();
                        
                //         byte[] content = pemObject.getContent();
                //         log.info(" Content "+ content);
                //         PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
                //         return (RSAPrivateKey) factory.generatePrivate(privKeySpec);

                
                // }
                
                // String privateKeyContent = new String(Files.readAllBytes(Paths.get(getClass().getResource("/ssl/swu/privatekey.key").toURI())));
                // privateKeyContent = privateKeyContent.replaceAll("\\n", "")
                // .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                // .replace("-----END RSA PRIVATE KEY-----", "");
                // log.info(privateKeyContent);        

                
                KeyFactory kf = KeyFactory.getInstance("RSA");
                
                // System.out.println(Base64.getDecoder().decode(privateKeyContent));

                PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Files.readAllBytes(Paths.get(getClass().getResource(file).toURI())));

                // KeyPair pair = KeyPair
                // RSAPrivateKey pv = RSAUtils
                log.info("######## PKCS8EncodedKeySpec #########");
                log.info(keySpecPKCS8.getFormat());
                log.info(keySpecPKCS8.getAlgorithm());
                // System.out.println(keySpecPKCS8.getEncoded());

                PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);
                return privKey;

        }
}
