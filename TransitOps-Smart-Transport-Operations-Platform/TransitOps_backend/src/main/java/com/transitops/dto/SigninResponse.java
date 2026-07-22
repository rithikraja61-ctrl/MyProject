package com.transitops.dto;

import java.util.List;

import com.transitops.entity.RoleName;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SigninResponse {

	private String accessToken;
	private String tokenType;
	private long expiresIn;
	private Long id;
	private String username;
	private String email;
	private RoleName role;
	private List<String> scopes;
}
