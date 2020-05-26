package br.com.jpb.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

import br.com.jpb.TestUtil;
import br.com.jpb.model.RegisterVo;
import br.com.jpb.model.Role;
import br.com.jpb.model.UserVo;
import io.restassured.authentication.OAuthSignature;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRestControllerTest extends BaseRestControllerTest {

	@Test
	public void invalid_register() {
		registerPost_expectBadRequest(RegisterVo
				.builder()
				.build(), 4);

		registerPost_expectBadRequest(RegisterVo
				.builder()
				.email(TestUtil.BIG_INPUT)
				.name(TestUtil.BIG_INPUT)
				.password(TestUtil.BIG_INPUT)
				.passwordConfirm(TestUtil.BIG_INPUT)
				.build(), 3);

		registerPost_expectBadRequest(RegisterVo
				.builder()
				.email("email@email.com")
				.name("John Doe")
				.password("Password1")
				.passwordConfirm("Password2")
				.build(), 1);

		registerPost_expectBadRequest(RegisterVo
				.builder()
				.email("jpbassinello@gmail.com") // email already exists
				.name("John Doe")
				.password("Password#")
				.passwordConfirm("Password#")
				.build(), 1);
	}

	private void registerPost_expectBadRequest(RegisterVo registerVO, int expectedErrors) {
		List<String> errors = register(registerVO)
				.then()
				.statusCode(is(HttpStatus.SC_BAD_REQUEST))
				.extract()
				.jsonPath()
				.getList("", String.class);
		log.info("Expected register POST errrors: " + errors.toString());
		assertThat(errors.size(), is(expectedErrors));
	}

	@Test
	public void valid_register() {
		register("email@email.com");
	}

	private void register(String email) {
		register(RegisterVo
				.builder()
				.email(email)
				.name("John Doe")
				.password("Password#")
				.passwordConfirm("Password#")
				.build())
				.then()
				.statusCode(is(HttpStatus.SC_OK));
	}

	private Response register(RegisterVo registerVO) {
		return given()
				.body(registerVO)
				.when()
				.contentType("application/json")
				.post("/api/users/public/register");
	}

	@Test
	public void add_remove_Roles() {
		String email = "email1@email.com";
		register(email);
		String accessToken = getAccessToken(email, "Password#");

		Assert.assertFalse(me(accessToken)
				.getRoles()
				.contains(Role.ADMIN));

		this.addRole(Role.ADMIN, accessToken);
		Assert.assertTrue(me(accessToken)
				.getRoles()
				.contains(Role.ADMIN));

		this.removeRole(Role.ADMIN, accessToken);
		Assert.assertFalse(me(accessToken)
				.getRoles()
				.contains(Role.ADMIN));
	}

	private UserVo me(String accessToken) {
		return given()
				.when()
				.contentType("application/json")
				.auth()
				.oauth2(accessToken, OAuthSignature.HEADER)
				.get("/api/users/me")
				.then()
				.statusCode(is(HttpStatus.SC_OK))
				.extract()
				.as(UserVo.class);
	}

	private void addRole(Role role, String accessToken) {
		given()
				.body(role)
				.when()
				.contentType("application/json")
				.auth()
				.oauth2(accessToken, OAuthSignature.HEADER)
				.put("/api/users/me/roles?role=" + role.name())
				.then()
				.statusCode(is(HttpStatus.SC_OK));
	}

	private void removeRole(Role role, String accessToken) {
		given()
				.body(role)
				.when()
				.contentType("application/json")
				.auth()
				.oauth2(accessToken, OAuthSignature.HEADER)
				.delete("/api/users/me/roles?role=" + role.name())
				.then()
				.statusCode(is(HttpStatus.SC_OK));
	}
}
