package br.com.jpb.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationException extends BaseException {

	public ValidationException(List<String> errors) {
		super(HttpStatus.BAD_REQUEST, errors);
	}
}
