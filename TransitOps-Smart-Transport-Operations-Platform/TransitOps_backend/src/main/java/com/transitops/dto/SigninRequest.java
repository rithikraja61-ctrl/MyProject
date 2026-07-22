package com.transitops.dto;

import com.transitops.entity.RoleName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SigninRequest {

	@NotBlank(message = "Email is required")
	@Email(message = "Email must be a valid address")
	private String email;

	@NotBlank(message = "Password is required")
	private String password;

	@NotNull(message = "Role is required")
	private RoleName role;
}
