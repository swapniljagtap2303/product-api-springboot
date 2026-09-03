package com.zestindia.productapi.repository;

import com.zestindia.productapi.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String t);
}
