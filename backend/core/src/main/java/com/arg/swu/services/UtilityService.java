package com.arg.swu.services;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.configs.JwtTokenUtil;
import com.arg.swu.models.AutUser;
import com.arg.swu.repositories.AutUserRepo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
@RequiredArgsConstructor
public class UtilityService implements ApiConstant {
	
	private static final Logger LOG = LogManager.getLogger(UtilityService.class);
	
	private final JwtTokenUtil jwtTokenUtil;
	private final AutUserRepo userRepository;
	
	@Value("${jwt.secret}")
    private String secretKey;
	
	@Value("${secret.key.pass}")
    private String secretKeyPass;
	
	@Value("${security.default.password}")
	private String defaultPassword;
	
	private static final String ALGORITHM = "AES";
	
	private static final String AES_GCM_NOPADDING = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
	
    public String encryptAES256(String plainText) throws Exception {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

        // Generate a random IV
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        // Prepare GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

        Cipher cipher = Cipher.getInstance(AES_GCM_NOPADDING);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);

        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // Concatenate IV and cipher text
        byte[] encryptedBytes = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, encryptedBytes, 0, iv.length);
        System.arraycopy(cipherText, 0, encryptedBytes, iv.length, cipherText.length);

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    public String decryptAES256(String cipherText) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(cipherText);
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

        // Extract IV from the encrypted data
        byte[] iv = new byte[GCM_IV_LENGTH];
        System.arraycopy(encryptedBytes, 0, iv, 0, GCM_IV_LENGTH);

        // Prepare GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);

        Cipher cipher = Cipher.getInstance(AES_GCM_NOPADDING);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes, GCM_IV_LENGTH, encryptedBytes.length - GCM_IV_LENGTH);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
    
    public boolean checkPassword(String plainText, String encryptPassword) throws Exception {
    	return decryptAES256(encryptPassword).equals(plainText);
    }

    
    public boolean isLangLocal(HttpServletRequest request) {
    	return jwtTokenUtil.getLangFromHeader(request).equals(TH);
    }

    public AutUser getActionUser(HttpServletRequest request) {
    	Long userId = jwtTokenUtil.getUserIdFormToken(request);
    	return userRepository.findById(userId).orElse(null);
    }
    
    public AutUser getActionUser(HttpServletRequest request, boolean mockup) {
    	return userRepository.findById(1l).orElse(null);
    }
    
    public String generateDefaultPassword(String username) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(username);
    	sb.append(defaultPassword);
    	return sb.toString();
    }
    
}
