package com.examen.badwallet_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BusinessException extends ResponseStatusException {

	public BusinessException(String message) {
		super(HttpStatus.CONFLICT, message);
	}
}
