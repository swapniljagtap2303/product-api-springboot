package com.zestindia.productapi.repository;

import com.zestindia.productapi.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface UserRepository extends JpaRepository<AppUser, Long> {
	Optional<AppUser> findByEmail(String e);

	boolean existsByEmail(String e);
}
