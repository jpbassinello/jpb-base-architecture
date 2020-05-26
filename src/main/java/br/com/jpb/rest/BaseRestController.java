package br.com.jpb.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.jpb.model.User;
import br.com.jpb.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseRestController {

	@Autowired
	protected UserRepository userRepository;

	protected User loggedUser() {
		String email = ((String) (SecurityContextHolder
				.getContext()
				.getAuthentication()).getPrincipal());
		return userRepository.findByEmailAndActiveIsTrue(email);
	}
}
