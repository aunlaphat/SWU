package com.arg.swu.configs;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.arg.swu.commons.ApiConstant;
import com.arg.swu.models.AutUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * @author sitthichaim
 *
 */
@Component
public class JwtTokenUtil implements Serializable, ApiConstant {

	private static final Logger LOG = LogManager.getLogger(JwtTokenUtil.class);

	private static final long serialVersionUID = 4920283549102498659L;
	
	@Value("${jwt.secret}")
	private String secret;

    public Long getUserIdFormToken(HttpServletRequest request) {
    	String token = request.getHeader("Authorization").substring(7);
    	final Claims claims = getAllClaimsFromToken(token);
    	return claims.get("id", Long.class);
    }
    
	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
    public String getLangFromHeader(HttpServletRequest request) {
    	String lang = request.getHeader("lang");
    	return StringUtils.isBlank(lang) ? DEFAULT_LANG : lang;
    }
	
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	public String generateToken(AutUser user) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, StringUtils.isBlank(user.getEmail()) ? "default" : user.getEmail());
	}
	
	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 5l * 60 * 60 * 1000))
				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	public Boolean validateToken(String token, AutUser autUser) {
		if (null == autUser) return false;
		final Long userId = getUserIdFromToken(token);
		return (userId.equals(autUser.getUserId()) && !isTokenExpired(token));
	}
	
	public void authenticate(String user, String passwordEnc, AutUser autUser) throws Exception{
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(passwordEnc);
        LOG.info("Hash end .. {}", hashedPassword);
        
        if(!passwordEncoder.matches(passwordEnc, autUser.getPassword()))
        {
            throw new Exception("Authen Fail");
        }
    }
	
	public Long getUserIdFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.get("id", Long.class);
	}

	public String getUsernameFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.get("name", String.class);
	}

	

    
}
