package br.com.jpb.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.jpb.model.Role;
import br.com.jpb.model.User;
import br.com.jpb.service.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Profile({
		"development",
		"test"
})
public class DbDataInitializer implements CommandLineRunner {

	public static final String DEFAULT_ADMIN_LOGIN = "jpbassinello@gmail.com";
	public static final String DEFAULT_PASSWORD = "123";

	private final UserService userService;

	private void createUser(final String name, final String email, final Role mainRole) {
		User user = userService.findByEmail(email);
		if (user != null) {
			return;
		}

		userService.register(name, email, DEFAULT_PASSWORD, mainRole);
	}

	@Override
	public void run(String... strings) {
		createUser("ADMIN", DEFAULT_ADMIN_LOGIN, Role.ADMIN);
	}
}
