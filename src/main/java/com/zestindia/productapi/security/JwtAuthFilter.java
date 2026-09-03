package com.zestindia.productapi.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.*;
import java.util.*;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	final JwtService jwt;

	public JwtAuthFilter(JwtService j) {
		jwt = j;
	}

	protected void doFilterInternal(HttpServletRequest q, HttpServletResponse r, FilterChain c)
			throws ServletException, IOException {
		String h = q.getHeader("Authorization");
		if (h != null && h.startsWith("Bearer ")) {
			String t = h.substring(7);
			if (jwt.valid(t)) {
				var cl = jwt.claims(t);
				var a = new UsernamePasswordAuthenticationToken(cl.getSubject(), null,
						List.of(new SimpleGrantedAuthority("ROLE_" + cl.get("role", String.class))));
				SecurityContextHolder.getContext().setAuthentication(a);
			}
		}
		c.doFilter(q, r);
	}
}
