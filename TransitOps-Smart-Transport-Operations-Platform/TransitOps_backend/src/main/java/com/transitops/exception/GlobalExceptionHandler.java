package com.transitops.exception;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.transitops.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		return ResponseEntity.badRequest().body(validationError(toFieldErrors(ex.getBindingResult().getFieldErrors())));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		String message = "Malformed JSON request body";
		if (ex.getCause() instanceof InvalidFormatException invalidFormat
			&& invalidFormat.getTargetType() != null
			&& invalidFormat.getTargetType().isEnum()) {
			message = "Invalid value for field: " + invalidFormat.getPathReference()
				+ ". Allowed values: "
				+ String.join(", ", Arrays.stream(invalidFormat.getTargetType().getEnumConstants()).map(Object::toString).toList());
		}
		return ResponseEntity.badRequest().body(singleError(HttpStatus.BAD_REQUEST, message));
	}

	@ExceptionHandler({
		MethodArgumentTypeMismatchException.class,
		MissingServletRequestParameterException.class
	})
	public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
		return ResponseEntity.badRequest().body(singleError(HttpStatus.BAD_REQUEST, ex.getMessage()));
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(singleError(HttpStatus.UNAUTHORIZED, ex.getMessage()));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(singleError(HttpStatus.UNAUTHORIZED, "Authentication required"));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
			.body(singleError(HttpStatus.FORBIDDEN, "Access denied"));
	}

	@ExceptionHandler({ NoResourceFoundException.class, ResourceNotFoundException.class })
	public ResponseEntity<ErrorResponse> handleNotFound(Exception ex) {
		String message = ex instanceof ResourceNotFoundException ? ex.getMessage() : "Resource not found";
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(singleError(HttpStatus.NOT_FOUND, message));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
			.body(singleError(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage()));
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
		return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
			.body(singleError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage()));
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.builder()
			.status(HttpStatus.CONFLICT.value())
			.error(HttpStatus.CONFLICT.getReasonPhrase())
			.message("Validation failed")
			.errors(Map.of(ex.getField(), ex.getMessage()))
			.build());
	}

	@ExceptionHandler(BusinessRuleException.class)
	public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException ex) {
		return ResponseEntity.badRequest().body(ErrorResponse.builder()
			.status(HttpStatus.BAD_REQUEST.value())
			.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
			.message("Validation failed")
			.errors(Map.of(ex.getField(), ex.getMessage()))
			.build());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(singleError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"));
	}

	private static ErrorResponse validationError(Map<String, String> errors) {
		return ErrorResponse.builder()
			.status(HttpStatus.BAD_REQUEST.value())
			.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
			.message("Validation failed")
			.errors(errors)
			.build();
	}

	private static Map<String, String> toFieldErrors(java.util.List<FieldError> fieldErrors) {
		return fieldErrors.stream()
			.collect(Collectors.toMap(
				FieldError::getField,
				error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
				(first, second) -> first,
				LinkedHashMap::new
			));
	}

	private static ErrorResponse singleError(HttpStatus status, String message) {
		return ErrorResponse.builder()
			.status(status.value())
			.error(status.getReasonPhrase())
			.message(message)
			.build();
	}
}
