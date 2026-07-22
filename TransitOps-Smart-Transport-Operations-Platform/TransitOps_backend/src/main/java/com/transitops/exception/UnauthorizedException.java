package com.transitops.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {

	public UnauthorizedException(String message) {
		super(message);
	}
}
