package br.com.jpb.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends BaseException {

	public EntityNotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}

}
