package com.stackly.authenticationService.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final String SECRET_KEY =
			"ThisIsASampleSecretKeyForJwtTokenGeneration123456789";
	
	public String generateToken(String username, String role) {
		
		SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
		
		return Jwts.builder()
				.subject(username)
				.claim("role", role)
				.issuedAt(new Date())
				.signWith(key)
				.compact();
	}

}
