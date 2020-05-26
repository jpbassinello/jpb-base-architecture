package br.com.jpb.service;

import static java.util.Collections.singletonList;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.openidconnect.IdToken.Payload;

import br.com.jpb.components.Facebook;
import br.com.jpb.components.Google;
import br.com.jpb.exception.EntityNotFoundException;
import br.com.jpb.exception.UserAlreadyExistsException;
import br.com.jpb.model.AuthWithProviderVo;
import br.com.jpb.model.IdentityProvider;
import br.com.jpb.model.RegisterVo;
import br.com.jpb.model.Role;
import br.com.jpb.model.User;
import br.com.jpb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService implements Serializable {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final Facebook facebook;
	private final Google google;
	private final ObjectMapper objectMapper;

	@Value("${security.oauth2.resource.jwt.key-pair.alias}")
	private String oauth2Alias;
	@Value("${security.oauth2.resource.jwt.key-pair.store-password}")
	private String oauth2Password;
	@Value("${app.url}")
	private String appUrl;

	public User findById(final long id) {
		return userRepository
				.findById(id)
				.orElse(null);
	}

	public User findByEmailAndActiveIsTrue(final String email) {
		return userRepository.findByEmailAndActiveIsTrue(email);
	}

	public User findByEmail(final String email) {
		return userRepository.findByEmail(email);
	}

	@Transactional
	public User register(String name, String email, String password, Role mainRole) {
		return userRepository.save(new User(name, email, passwordEncoder.encode(password), mainRole));
	}

	@Transactional
	public User update(User user) {
		return userRepository.save(user);
	}

	@Transactional
	public User register(RegisterVo vo) {
		User alreadyExist = findByEmail(vo.getEmail());

		if (alreadyExist != null && !vo.isAuthWithProvider()) {
			throw new UserAlreadyExistsException(vo.getEmail());
		}

		User newUser = new User(vo.getName(), vo.getEmail(), passwordEncoder.encode(vo.getPassword()));
		return userRepository.save(newUser);
	}

	@Transactional
	public User authWithProvider(AuthWithProviderVo vo) {

		// GOOGLE, FACEBOOK
		IdentityProvider provider = vo.getProvider();

		checkUserWithProviderAccessToken(provider, vo.getAccessToken(), vo.getProviderUserId());

		User user = findByEmail(vo.getEmail().toLowerCase());
		String tempPassword = "JPB!" + System.currentTimeMillis();
		boolean isNew = user == null;
		if (isNew) {
			RegisterVo registerVO = RegisterVo.builder()
					.email(vo.getEmail())
					.name(vo.getName())
					.password(tempPassword)
					.passwordConfirm(tempPassword)
					.provider(provider)
					.build();

			user = register(registerVO);
		} else {
			user.setName(vo.getName());
			user.setPassword(passwordEncoder.encode(tempPassword));
		}

		user.getIdentityProviders().put(provider, vo.getProviderUserId());
		User saved = userRepository.save(user);
		if (!isNew && user.isActive()) {
			// this will be used to trigger an internalLogin
			saved.setTransientTempPassword(tempPassword);
		}

		return saved;
	}

	private void checkUserWithProviderAccessToken(IdentityProvider provider, String providerAccessToken, String providerUserId) {
		switch (provider) {
			case FACEBOOK:
				com.restfb.types.User me = facebook.fetchLoggedUser(providerAccessToken);
				if (me == null || !providerUserId.equals(me.getId())) {
					throw new EntityNotFoundException("Could not check and find Facebook User with the provided token");
				}
				break;
			case GOOGLE:
				Payload payload = google.fetchLoggedUser(providerAccessToken);
				if (payload == null || !providerUserId.equals(payload.getSubject())) {
					throw new EntityNotFoundException("Could not check and find Google User with the provided token");
				}
				break;
			default:
				throw new EntityNotFoundException("Could not authenticate with provider " + provider.name());
		}
	}

	/**
	 * Post to /oauth/token at this server to return the accessToken to client
	 *
	 * @param email    username
	 * @param password password
	 */
	public Map<String, String> internalLogin(String email, String password) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth(oauth2Alias, oauth2Password);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.put("username", singletonList(email));
		map.put("password", singletonList(password));
		map.put("grant_type", singletonList("password"));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		RestTemplate restTemplate = new RestTemplate();
		String loginUrl = appUrl + "/oauth/token";
		try {
			ResponseEntity<String> response
					= restTemplate.postForEntity(loginUrl, request, String.class);
			return objectMapper.readValue(response.getBody(),
					new TypeReference<>() {
					});
		} catch (Exception e) {
			log.error("Unexpected exception while trying to do an internalLoging of user {}", email, e);
			throw new IllegalStateException(e);
		}
	}

	@Transactional
	public void addRole(final User user, final Role role) {
		user.addRole(role);
		update(user);
	}

	@Transactional
	public void removeRole(final User user, final Role role) {
		user.removeRole(role);
		update(user);
	}
}
