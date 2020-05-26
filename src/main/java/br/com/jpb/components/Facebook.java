package br.com.jpb.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.types.User;

@Component
public class Facebook {

	@Value("${facebook.appId}")
	private String facebookAppId;

	private FacebookClient defaultClient(String accessToken) {
		return new DefaultFacebookClient(accessToken, Version.LATEST);
	}

	public User fetchLoggedUser(String accessToken) {
		return defaultClient(accessToken).fetchObject("me", User.class);
	}
}
