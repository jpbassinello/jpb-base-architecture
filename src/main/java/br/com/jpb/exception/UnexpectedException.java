package br.com.jpb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class UnexpectedException extends BaseException {

	public UnexpectedException() {
		super(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public UnexpectedException(final String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}

}
