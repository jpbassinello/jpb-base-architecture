package br.com.jpb.exception;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

	private final HttpStatus status;
	private final List<String> errors;

	public BaseException(final HttpStatus status) {
		this(status, Collections.emptyList());
	}

	public BaseException(final HttpStatus status, final String error) {
		this(status, Collections.singletonList(error));
	}

	public BaseException(final HttpStatus status, final List<String> errors) {
		super(String.join(". ", errors));
		this.status = status;
		this.errors = errors;
	}
}
