package com.transitops.dto;

import java.util.List;

import com.transitops.entity.RoleName;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SessionResponse {

	Long id;
	String username;
	String email;
	RoleName role;
	List<String> scopes;
}
