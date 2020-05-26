package br.com.jpb.rest;

import static io.restassured.RestAssured.given;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.jpb.config.Application;
import br.com.jpb.config.DbDataInitializer;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = Application.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
public abstract class BaseRestControllerTest {

	@LocalServerPort
	private int port;

	@Before
	public void init() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	public String getAdminAccessToken() {
		return getAccessToken(DbDataInitializer.DEFAULT_ADMIN_LOGIN, DbDataInitializer.DEFAULT_PASSWORD);
	}

	protected String getAccessToken(String user, String password) {
		String oauthToken = oauthTokenPost(user, password)
				.asString();

		return new JsonPath(oauthToken).getString("access_token");
	}

	private Response oauthTokenPost(String user, String password) {
		return given()
				.param("grant_type", "password")
				.param("username", user)
				.param("password", password)
				.auth()
				.basic("jpb-springboot-arqref-key", "psw.-jpb@1298!")
				.post("/oauth/token");
	}

}
