package br.com.jpb.config;

import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig {

	@Value("${security.oauth2.resource.jwt.key-pair.store-password}")
	private String keyStorePass;

	@Value("${security.oauth2.resource.jwt.key-pair.alias}")
	private String keyPairAlias;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

		KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore-jwt.jks"),
				keyStorePass.toCharArray()).getKeyPair(keyPairAlias);
		converter.setKeyPair(keyPair);
		return converter;
	}

}
