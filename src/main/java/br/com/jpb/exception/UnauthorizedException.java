package br.com.jpb.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.jpb.components.Messages;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UnauthorizedException extends BaseException {
	public UnauthorizedException() {
		this(Messages.getMessage("user.access.denied"));
	}

	public UnauthorizedException(String msg) {
		super(HttpStatus.FORBIDDEN, Messages.getMessage("user.access.denied"));
	}
}
