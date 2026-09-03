package com.zestindia.productapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.time.LocalDateTime;

record ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
}

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	ResponseEntity<ErrorResponse> nf(ResourceNotFoundException e, HttpServletRequest r) {
		return x(404, "Not Found", e.getMessage(), r);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ErrorResponse> val(MethodArgumentNotValidException e, HttpServletRequest r) {
		return x(400, "Validation Failed", e.getBindingResult().getFieldErrors().get(0).getDefaultMessage(), r);
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorResponse> all(Exception e, HttpServletRequest r) {
		return x(500, "Internal Server Error", "Unexpected error", r);
	}

	ResponseEntity<ErrorResponse> x(int s, String e, String m, HttpServletRequest r) {
		return ResponseEntity.status(s).body(new ErrorResponse(LocalDateTime.now(), s, e, m, r.getRequestURI()));
	}
}
