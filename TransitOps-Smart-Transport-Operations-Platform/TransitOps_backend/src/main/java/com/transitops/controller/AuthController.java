package com.transitops.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.transitops.dto.SessionResponse;
import com.transitops.dto.SigninRequest;
import com.transitops.dto.SigninResponse;
import com.transitops.dto.SignupRequest;
import com.transitops.dto.SignupResponse;
import com.transitops.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@GetMapping("/me")
	public SessionResponse me() {
		return authService.getSession();
	}

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public SignupResponse signup(@Valid @RequestBody SignupRequest request) {
		return authService.signup(request);
	}

	@PostMapping("/signin")
	public SigninResponse signin(@Valid @RequestBody SigninRequest request) {
		return authService.signin(request);
	}

	@PostMapping("/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void logout(@RequestHeader("Authorization") String authorization) {
		authService.logout(authorization);
	}
}
