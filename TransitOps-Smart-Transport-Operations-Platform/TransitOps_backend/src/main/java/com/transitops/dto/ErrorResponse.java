package com.transitops.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

	private int status;
	private String error;
	private String message;
	private Map<String, String> errors;
}
