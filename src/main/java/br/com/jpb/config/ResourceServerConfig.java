package br.com.jpb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.requestMatchers()
				.antMatchers("/api/**/public/**")
				.and()
				.authorizeRequests()
				.antMatchers("/api/**/public/**")
				.permitAll()
				.and()
				.csrf()
				.disable();
		http
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.requestMatchers()
				.antMatchers("/api/**")
				.and()
				.authorizeRequests()
				.antMatchers("/api/**")
				.authenticated()
				.and()
				.csrf()
				.disable();
	}
}
