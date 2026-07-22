package com.transitops.service;

import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transitops.entity.RevokedToken;
import com.transitops.repository.RevokedTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

	private final RevokedTokenRepository revokedTokenRepository;

	public boolean isRevoked(String jti) {
		return revokedTokenRepository.existsByJti(jti);
	}

	@Transactional
	public void revoke(String jti, Instant expiresAt) {
		if (revokedTokenRepository.existsByJti(jti)) {
			return;
		}
		try {
			revokedTokenRepository.save(new RevokedToken(jti, expiresAt));
		} catch (DataIntegrityViolationException ex) {
			// concurrent logout — idempotent
		}
	}
}
