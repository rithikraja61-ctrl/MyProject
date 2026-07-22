package com.transitops.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transitops.entity.RevokedToken;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {

	boolean existsByJti(String jti);

	void deleteByExpiresAtBefore(Instant instant);
}
