package br.com.jpb.config;

import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.jpb.Constants;

@SpringBootApplication
public class Application {

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(Constants.DEFAULT_TIME_ZONE_ID));
		Locale.setDefault(Locale.forLanguageTag(Constants.DEFAULT_LANGUAGE_TAG));
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
