package br.com.jpb.rest;

import br.com.jpb.components.Messages;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class DefaultResponse {

	private final String message;

	public static DefaultResponse success() {
		return new DefaultResponse(Messages.getMessage("success"));
	}

}
