package com.arg.swu.digitalsignatures.service;


// import com.itextpdf.forms.form.element.SignatureFieldAppearance;
// import com.itextpdf.io.font.PdfEncodings;
// import com.itextpdf.io.font.constants.StandardFonts;
// import com.itextpdf.io.image.ImageData;
// import com.itextpdf.io.image.ImageDataFactory;
// import com.itextpdf.kernel.font.PdfFontFactory;
// import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
// import com.itextpdf.kernel.geom.Rectangle;
// import com.itextpdf.kernel.pdf.PdfReader;
// import com.itextpdf.kernel.pdf.StampingProperties;
// import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
// import com.itextpdf.layout.element.Div;
// import com.itextpdf.layout.element.Paragraph;
// import com.itextpdf.layout.element.Text;
// import com.itextpdf.layout.properties.BackgroundImage;
// import com.itextpdf.layout.properties.BackgroundPosition;
// import com.itextpdf.layout.properties.BackgroundRepeat;
// import com.itextpdf.layout.properties.BackgroundSize;
// import com.itextpdf.layout.properties.BaseDirection;
// import com.itextpdf.layout.properties.TextAlignment;
// import com.itextpdf.layout.properties.UnitValue;
// import com.itextpdf.signatures.BouncyCastleDigest;
// import com.itextpdf.signatures.DigestAlgorithms;
// import com.itextpdf.signatures.IExternalDigest;
// import com.itextpdf.signatures.IExternalSignature;
// import com.itextpdf.signatures.PdfSignatureAppearance;
// import com.itextpdf.signatures.PdfSigner;
// import com.itextpdf.signatures.PrivateKeySignature;
 
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
 
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class C2_01_SignHelloWorld {
    public static final String DEST = "./target/signatures/chapter02/";
 
    public static final String KEYSTORE = "/sender_keystore.jks";
    public static final String SRC = "/pdfs/bill_bf_sign.pdf";
    public static final String IMG = "./src/test/resources/img/1t3xt.gif";
 
    public static final char[] PASSWORD = "changeit".toCharArray();
 
 
    public static final String[] RESULT_FILES = new String[] {
            "signature_appearance1.pdf",
            "signature_appearance2.pdf",
            "signature_appearance3.pdf",
            "signature_appearance4.pdf"
    };

//     public void sign1(String src, String name, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm,
//             String provider, PdfSigner.CryptoStandard subfilter, String reason, String location)
//             throws GeneralSecurityException, IOException {
//         PdfReader reader = new PdfReader(src);
//         PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest), new StampingProperties());

//         // Create the signature appearance
//         signer
//             .setReason(reason)
//             .setLocation(location);

//         // This name corresponds to the name of the field that already exists in the document.
//         signer.setFieldName(name);

//         // Set the custom text and a custom font
//         SignatureFieldAppearance appearance = new SignatureFieldAppearance(signer.getFieldName());
//         appearance.setContent("This document was signed by Bruno Specimen");
//         appearance.setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN));
//         signer.setSignatureAppearance(appearance);

//         IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
//         IExternalDigest digest = new BouncyCastleDigest();

//         // Sign the document using the detached mode, CMS or CAdES equivalent.
//         signer.signDetached(digest, pks, chain, null, null, null, 0, subfilter);
//     }

//     public void sign2(String src, String name, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm,
//             String provider, PdfSigner.CryptoStandard subfilter, String reason, String location)
//             throws GeneralSecurityException, IOException {
//         PdfReader reader = new PdfReader(src);
//         PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest), new StampingProperties());

//         signer
//             .setReason(reason)
//             .setLocation(location);
//         signer.setFieldName(name);

//         // Creating the appearance for layer 2
//         // Set custom text, custom font, and right-to-left writing.
//         // Characters: لورانس العرب
//         Text text = new Text("\u0644\u0648\u0631\u0627\u0646\u0633 \u0627\u0644\u0639\u0631\u0628");
//         text.setFont(PdfFontFactory.createFont("./src/test/resources/font/NotoNaskhArabic-Regular.ttf",
//                 PdfEncodings.IDENTITY_H, EmbeddingStrategy.PREFER_EMBEDDED));
//         text.setBaseDirection(BaseDirection.RIGHT_TO_LEFT);
//         SignatureFieldAppearance appearance = new SignatureFieldAppearance(signer.getFieldName());
//         appearance.setContent(new Div().add(new Paragraph(text).setTextAlignment(TextAlignment.RIGHT)));
//         signer.setSignatureAppearance(appearance);

//         PrivateKeySignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
//         IExternalDigest digest = new BouncyCastleDigest();
//         signer.signDetached(digest, pks, chain, null, null, null, 0, subfilter);
//     }

//     public void sign3(String src, String name, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm,
//             String provider, PdfSigner.CryptoStandard subfilter, String reason, String location)
//             throws GeneralSecurityException, IOException {
//         PdfReader reader = new PdfReader(src);
//         PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest), new StampingProperties());

//         signer
//             .setReason(reason)
//             .setLocation(location)
//             .setFieldName(name);

//         // Set a custom text and a background image
//         ImageData imageData = ImageDataFactory.create(IMG);
//         SignatureFieldAppearance appearance = new SignatureFieldAppearance(signer.getFieldName());
//         appearance.setContent("This document was signed by Bruno Specimen");
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
//     }

//     public void sign4(String src, String name, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm,
//             String provider, PdfSigner.CryptoStandard subfilter, String reason, String location)
//             throws GeneralSecurityException, IOException {
//         PdfReader reader = new PdfReader(src);
//         PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest), new StampingProperties());

//         signer
//             .setReason(reason)
//             .setLocation(location);
//         signer.setFieldName(name);

//         // Set a custom text and a scaled background image
//         SignatureFieldAppearance appearance = new SignatureFieldAppearance(signer.getFieldName());
//         appearance.setContent("This document was signed by Bruno Specimen");
//         BackgroundSize backgroundSize = new BackgroundSize();
//         backgroundSize.setBackgroundSizeToContain();
//         BackgroundPosition backgroundPosition = new BackgroundPosition();
//         backgroundPosition.setPositionX(BackgroundPosition.PositionX.CENTER)
//                 .setPositionY(BackgroundPosition.PositionY.CENTER);
//         appearance.setBackgroundImage(new BackgroundImage.Builder()
//                 .setImage(new PdfImageXObject(ImageDataFactory.create(IMG)))
//                 .setBackgroundRepeat(new BackgroundRepeat(BackgroundRepeat.BackgroundRepeatValue.NO_REPEAT))
//                 .setBackgroundPosition(backgroundPosition).setBackgroundSize(backgroundSize).build());
//         signer.setSignatureAppearance(appearance);

//         PrivateKeySignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
//         IExternalDigest digest = new BouncyCastleDigest();
//         signer.signDetached(digest, pks, chain, null, null, null, 0, subfilter);
//     }

//     public static void main(String[] args) throws IOException, GeneralSecurityException {
//         File file = new File(DEST);
//         file.mkdirs();

//         BouncyCastleProvider provider = new BouncyCastleProvider();
//         Security.addProvider(provider);
//         KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//         ks.load(new FileInputStream(KEYSTORE), PASSWORD);
//         String alias = ks.aliases().nextElement();
//         PrivateKey pk = (PrivateKey) ks.getKey(alias, PASSWORD);
//         Certificate[] chain = ks.getCertificateChain(alias);

//         C2_01_SignHelloWorld app = new C2_01_SignHelloWorld();
//         String signatureName = "Signature1";
//         String signatureReason = "Custom appearance example";
//         String location = "Ghent";
//         app.sign1(SRC, signatureName, DEST + RESULT_FILES[0], chain, pk,
//                 DigestAlgorithms.SHA256, provider.getName(), PdfSigner.CryptoStandard.CMS,
//                 signatureReason, location);

//         app.sign2(SRC, signatureName, DEST + RESULT_FILES[1], chain, pk,
//                 DigestAlgorithms.SHA256, provider.getName(), PdfSigner.CryptoStandard.CMS,
//                 signatureReason, location);

//         app.sign3(SRC, signatureName, DEST + RESULT_FILES[2], chain, pk,
//                 DigestAlgorithms.SHA256, provider.getName(), PdfSigner.CryptoStandard.CMS,
//                 signatureReason, location);

//         app.sign4(SRC, signatureName, DEST + RESULT_FILES[3], chain, pk,
//                 DigestAlgorithms.SHA256, provider.getName(), PdfSigner.CryptoStandard.CMS,
//                 signatureReason, location);
//     }
}
