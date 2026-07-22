package com.transitops.exception;

import lombok.Getter;

@Getter
public class BusinessRuleException extends RuntimeException {

	private final String field;

	public BusinessRuleException(String field, String message) {
		super(message);
		this.field = field;
	}
}
