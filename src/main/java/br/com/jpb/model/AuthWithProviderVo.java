package br.com.jpb.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Setter
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthWithProviderVo {

	@NotBlank(message = "{user.name.notEmpty}")
	@Size(max = 255, message = "{user.name.maxSize}")
	String name;
	@NotBlank(message = "{user.email.notEmpty}")
	@Size(max = 150, message = "{user.email.maxSize}")
	@Email(message = "{email.invalid}")
	String email;

	@NotBlank
	String providerUserId;
	@NotBlank
	String accessToken;

	@NotNull
	private IdentityProvider provider;
}
