package br.com.jpb.components;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Google {

	private static final HttpTransport transport = new NetHttpTransport();
	private static final JsonFactory jsonFactory = new JacksonFactory();

	@Value("${google.appId}")
	private String googleAppId;

	public Payload fetchLoggedUser(String accessToken) {
		try {
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
					.setAudience(Collections.singletonList(googleAppId))
					.build();

			GoogleIdToken idToken = verifier.verify(accessToken);

			return Optional.ofNullable(idToken).map(GoogleIdToken::getPayload).orElse(null);
		} catch (Exception e) {
			log.error("Error while fetching user / checking google access token", e);
			return null;
		}
	}
}
