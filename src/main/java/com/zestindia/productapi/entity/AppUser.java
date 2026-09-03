package com.zestindia.productapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class AppUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(nullable = false)
	String email;
	@Column(nullable = false)
	String password;
	@Column(nullable = false)
	String role;

	public AppUser() {
	}

	public AppUser(String e, String p, String r) {
		email = e;
		password = p;
		role = r;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}
}
