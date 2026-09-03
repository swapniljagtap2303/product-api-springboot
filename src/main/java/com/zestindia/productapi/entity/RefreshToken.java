package com.zestindia.productapi.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refresh_token", indexes = @Index(name = "idx_refresh_token", columnList = "token", unique = true))
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(nullable = false, unique = true, length = 500)
	String token;
	@ManyToOne(optional = false)
	AppUser user;
	@Column(nullable = false)
	Instant expiry;
	@Column(nullable = false)
	boolean revoked;

	public RefreshToken() {
	}

	public RefreshToken(String t, AppUser u, Instant e) {
		token = t;
		user = u;
		expiry = e;
	}

	public String getToken() {
		return token;
	}

	public AppUser getUser() {
		return user;
	}

	public Instant getExpiry() {
		return expiry;
	}

	public boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(boolean x) {
		revoked = x;
	}
}
