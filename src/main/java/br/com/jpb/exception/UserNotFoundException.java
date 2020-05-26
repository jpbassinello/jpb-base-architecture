package br.com.jpb.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.jpb.components.Messages;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends EntityNotFoundException {

	public UserNotFoundException() {
		super(Messages.getMessage("user.not.found"));
	}

}
