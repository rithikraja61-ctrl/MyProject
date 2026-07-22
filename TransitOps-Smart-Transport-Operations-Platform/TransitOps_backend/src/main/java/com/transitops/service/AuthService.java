package com.transitops.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transitops.dto.SessionResponse;
import com.transitops.dto.SigninRequest;
import com.transitops.dto.SigninResponse;
import com.transitops.dto.SignupRequest;
import com.transitops.dto.SignupResponse;
import com.transitops.entity.Role;
import com.transitops.entity.User;
import com.transitops.exception.DuplicateResourceException;
import com.transitops.exception.ResourceNotFoundException;
import com.transitops.exception.UnauthorizedException;
import com.transitops.repository.RoleRepository;
import com.transitops.repository.UserRepository;
import com.transitops.security.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final TokenBlacklistService tokenBlacklistService;

	@Transactional
	public SignupResponse signup(SignupRequest request) {
		String email = request.getEmail().trim().toLowerCase();
		String username = request.getUsername().trim().toLowerCase();

		if (userRepository.existsByEmail(email)) {
			throw new DuplicateResourceException("email", "Email already registered");
		}
		if (userRepository.existsByUsername(username)) {
			throw new DuplicateResourceException("username", "Username already taken");
		}

		Role role = roleRepository.findByName(request.getRole())
			.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.getRole()));

		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
		user.setRole(role);

		User saved = userRepository.save(user);

		return SignupResponse.builder()
			.id(saved.getId())
			.username(saved.getUsername())
			.email(saved.getEmail())
			.role(saved.getRole().getName())
			.scopes(saved.getRole().getScopes())
			.build();
	}

	@Transactional(readOnly = true)
	public SigninResponse signin(SigninRequest request) {
		String email = request.getEmail().trim().toLowerCase();

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
			throw new UnauthorizedException("Invalid credentials");
		}

		if (!user.getRole().getName().equals(request.getRole())) {
			throw new UnauthorizedException("Invalid credentials");
		}

		return SigninResponse.builder()
			.accessToken(jwtService.generateToken(user))
			.tokenType("Bearer")
			.expiresIn(jwtService.getExpirationSeconds())
			.id(user.getId())
			.username(user.getUsername())
			.email(user.getEmail())
			.role(user.getRole().getName())
			.scopes(user.getRole().getScopes())
			.build();
	}

	@Transactional(readOnly = true)
	public SessionResponse getSession() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getPrincipal() == null) {
			throw new UnauthorizedException("Authentication required");
		}

		String email = authentication.getPrincipal().toString();
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UnauthorizedException("Authentication required"));

		return SessionResponse.builder()
			.id(user.getId())
			.username(user.getUsername())
			.email(user.getEmail())
			.role(user.getRole().getName())
			.scopes(user.getRole().getScopes())
			.build();
	}

	@Transactional
	public void logout(String authorization) {
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			throw new UnauthorizedException("Authentication required");
		}

		String token = authorization.substring(7);
		Claims claims;
		try {
			claims = jwtService.parseToken(token);
		} catch (ExpiredJwtException ex) {
			throw new UnauthorizedException("Authentication required");
		} catch (JwtException ex) {
			throw new UnauthorizedException("Authentication required");
		}

		String jti = JwtService.getJti(claims);
		if (jti == null || jti.isBlank()) {
			throw new UnauthorizedException("Authentication required");
		}

		tokenBlacklistService.revoke(jti, JwtService.getExpiresAt(claims));
	}
}
