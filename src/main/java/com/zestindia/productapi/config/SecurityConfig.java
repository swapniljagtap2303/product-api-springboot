package com.zestindia.productapi.config;

import com.zestindia.productapi.security.JwtAuthFilter;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;
import java.util.*;

@Configuration
public class SecurityConfig {
	final JwtAuthFilter f;

	public SecurityConfig(JwtAuthFilter x) {
		f = x;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain security(HttpSecurity h) throws Exception {
		return h.csrf(c -> c.disable()).cors(c -> c.configurationSource(cors()))
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(a -> a
						.requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**")
						.permitAll().requestMatchers("/api/v1/products/**").hasAnyRole("USER", "ADMIN").anyRequest()
						.authenticated())
				.addFilterBefore(f, UsernamePasswordAuthenticationFilter.class)
				.requiresChannel(x -> x.requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null).requiresSecure())
				.build();
	}

	CorsConfigurationSource cors() {
		CorsConfiguration c = new CorsConfiguration();
		c.setAllowedOrigins(List.of("*"));
		c.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		c.setAllowedHeaders(List.of("*"));
		UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
		s.registerCorsConfiguration("/**", c);
		return s;
	}
}
