package com.arg.swu.services;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * @author sitthichaim
 *
 */
@Service
public class JWTService implements Serializable {

	private static final long serialVersionUID = 3356073726692980276L;

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expire.minute}")
	private long jwtExpireMinute;

	public String generateToken(Long userId, String username) {
		
		long timestamp = System.currentTimeMillis() / 1000 ;
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", userId);
		claims.put("name", username);
		claims.put("iat", timestamp);
		claims.put("exp", timestamp + (jwtExpireMinute * 60 * 60 * 1000));
		return doGenerateToken(claims);
	}

	private String doGenerateToken(Map<String, Object> claims) {
		Map<String, Object> header = new HashMap<>();
		header.put("typ", "JWT");
		return Jwts.builder().setClaims(claims)
				.setHeader(header)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpireMinute * 60 * 60 * 1000))
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}
}
