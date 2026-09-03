package com.zestindia.productapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.crypto.SecretKey;

@Service
public class JwtService {
	final SecretKey key;
	final long exp;

	public JwtService(@Value("${app.jwt.secret}") String s, @Value("${app.jwt.expiration-ms}") long e) {
		key = Keys.hmacShaKeyFor(s.getBytes(StandardCharsets.UTF_8));
		exp = e;
	}

	public String generate(String email, String role) {
		Date n = new Date();
		return Jwts.builder().subject(email).claim("role", role).issuedAt(n).expiration(new Date(n.getTime() + exp))
				.signWith(key).compact();
	}

	public Claims claims(String t) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(t).getPayload();
	}

	public boolean valid(String t) {
		try {
			claims(t);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
