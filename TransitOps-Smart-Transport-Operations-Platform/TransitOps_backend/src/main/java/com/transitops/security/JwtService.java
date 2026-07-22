package com.transitops.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.transitops.config.JwtProperties;
import com.transitops.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

	private final JwtProperties jwtProperties;
	private SecretKey secretKey;

	@PostConstruct
	void init() {
		if (jwtProperties.getSecret() == null || jwtProperties.getSecret().isBlank()) {
			throw new IllegalStateException("JWT_SECRET must be set");
		}
		secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(User user) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtProperties.getExpirationMs());
		String scope = String.join(" ", user.getRole().getScopes());

		return Jwts.builder()
			.id(UUID.randomUUID().toString())
			.subject(String.valueOf(user.getId()))
			.claim("email", user.getEmail())
			.claim("role", user.getRole().getName().name())
			.claim("scope", scope)
			.issuedAt(now)
			.expiration(expiry)
			.signWith(secretKey)
			.compact();
	}

	public Claims parseToken(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	public long getExpirationSeconds() {
		return jwtProperties.getExpirationMs() / 1000L;
	}

	public static String getJti(Claims claims) {
		return claims.getId();
	}

	public static Instant getExpiresAt(Claims claims) {
		return claims.getExpiration().toInstant();
	}

	public static List<String> scopesFromClaims(Claims claims) {
		String scope = claims.get("scope", String.class);
		if (scope == null || scope.isBlank()) {
			return List.of();
		}
		return List.of(scope.split(" "));
	}
}
