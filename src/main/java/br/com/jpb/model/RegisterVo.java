package br.com.jpb.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.google.common.base.Strings;

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
public class RegisterVo {

	@NotBlank(message = "{user.name.notEmpty}")
	@Size(max = 255, message = "{user.name.maxSize}")
	String name;
	@NotBlank(message = "{user.email.notEmpty}")
	@Size(max = 150, message = "{user.email.maxSize}")
	@Email(message = "{email.invalid}")
	String email;
	@NotBlank(message = "{user.password.notEmpty}")
	@Size(min = 8, message = "{user.password.size.min}")
	String password;
	String passwordConfirm;

	IdentityProvider provider;

	@AssertTrue(message = "{user.password.match}")
	protected boolean isPasswordConfirmed() {
		return Strings
				.nullToEmpty(password)
				.equals(passwordConfirm);
	}

	public boolean isAuthWithProvider() {
		return provider != null;
	}
}
