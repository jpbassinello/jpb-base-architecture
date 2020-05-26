package br.com.jpb.exception;

import static java.util.Collections.singletonList;

import br.com.jpb.components.Messages;

public final class UserAlreadyExistsException extends ValidationException {

	public UserAlreadyExistsException(String email) {
		super(singletonList(
				Messages.getMessage("user.already.exists", email)));
	}
}
