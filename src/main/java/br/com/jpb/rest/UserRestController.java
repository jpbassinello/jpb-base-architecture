package br.com.jpb.rest;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jpb.model.RegisterVo;
import br.com.jpb.model.Role;
import br.com.jpb.model.User;
import br.com.jpb.model.UserVo;
import br.com.jpb.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserRestController extends BaseRestController {

	private final UserService userService;

	@GetMapping("/me")
	public ResponseEntity<?> me(Principal principal) {
		User me = userService.findByEmailAndActiveIsTrue(principal.getName());
		if (me == null) {
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.build();
		}

		return ResponseEntity.ok(new UserVo(me));
	}

	@PostMapping("/public/register")
	@ApiOperation(httpMethod = "POST", value = "User's registration")
	public ResponseEntity<?> register(@Valid @RequestBody final RegisterVo registerVO) {
		User registered = userService.register(registerVO);
		return ResponseEntity.ok(registered);
	}

	//	@PostMapping("/public/auth/provider")
	//	@ApiOperation(httpMethod = "POST", value = "User's authentication with a provider (GOOGLE, FACEBOOK, etc..)")
	//	public ResponseEntity<?> authWithProvider(@Valid @RequestBody final AuthWithProviderVO authWithProviderVO) {
	//		User registered = userService.authWithProvider(authWithProviderVO);
	//		if (registered.getTransientTempPassword() != null) {
	//			// it means that user is already registered and active
	//			return ResponseEntity.ok(userService.internalLogin(registered.getEmail(), registered.getTransientTempPassword()));
	//		}
	//		return ResponseEntity.ok(UserVO.from(registered));
	//	}

	@PutMapping("/me/roles")
	@ApiOperation(httpMethod = "POST", value = "Add new role to the logged user")
	public ResponseEntity<?> addRole(@RequestParam(name = "role") final Role role) {
		userService.addRole(loggedUser(), role);
		return ResponseEntity
				.ok(DefaultResponse.success());
	}

	@DeleteMapping("/me/roles")
	@ApiOperation(httpMethod = "POST", value = "Remove logged user role")
	public ResponseEntity<?> removeRole(@RequestParam(name = "role") final Role role) {
		userService.removeRole(loggedUser(), role);
		return ResponseEntity
				.ok(DefaultResponse.success());
	}
}
