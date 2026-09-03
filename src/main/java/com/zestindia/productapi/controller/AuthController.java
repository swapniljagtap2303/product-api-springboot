package com.zestindia.productapi.controller;

import com.zestindia.productapi.dto.DTOs.*;
import com.zestindia.productapi.entity.*;
import com.zestindia.productapi.repository.*;
import com.zestindia.productapi.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	final UserRepository u;
	final RefreshTokenRepository rt;
	final PasswordEncoder enc;
	final JwtService jwt;

	public AuthController(UserRepository a, RefreshTokenRepository b, PasswordEncoder c, JwtService d) {
		u = a;
		rt = b;
		enc = c;
		jwt = d;
	}

	@PostMapping("/register")
	ResponseEntity<String> reg(@Valid @RequestBody AuthRequest r) {
		if (u.existsByEmail(r.email()))
			return ResponseEntity.status(409).body("Email already registered");
		u.save(new AppUser(r.email(), enc.encode(r.password()), "USER"));
		return ResponseEntity.status(201).body("User registered successfully");
	}

	@PostMapping("/login")
	AuthResponse login(@Valid @RequestBody AuthRequest r) {
		AppUser x = u.findByEmail(r.email()).orElseThrow(() -> new RuntimeException("Invalid credentials"));
		if (!enc.matches(r.password(), x.getPassword()))
			throw new RuntimeException("Invalid credentials");
		String t = UUID.randomUUID().toString();
		rt.save(new RefreshToken(t, x, Instant.now().plusSeconds(604800)));
		return new AuthResponse(jwt.generate(x.getEmail(), x.getRole()), t, "Bearer");
	}

	@PostMapping("/refresh")
	AuthResponse refresh(@RequestParam String refreshToken) {
		RefreshToken old = rt.findByToken(refreshToken)
				.orElseThrow(() -> new RuntimeException("Invalid refresh token"));
		if (old.isRevoked() || old.getExpiry().isBefore(Instant.now()))
			throw new RuntimeException("Refresh token expired or revoked");
		old.setRevoked(true);
		rt.save(old);
		String n = UUID.randomUUID().toString();
		rt.save(new RefreshToken(n, old.getUser(), Instant.now().plusSeconds(604800)));
		return new AuthResponse(jwt.generate(old.getUser().getEmail(), old.getUser().getRole()), n, "Bearer");
	}
}
