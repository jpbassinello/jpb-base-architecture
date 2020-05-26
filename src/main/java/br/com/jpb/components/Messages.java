package br.com.jpb.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Messages {

	private static MessageSource messageSource;

	@Autowired
	Messages(MessageSource messageSource) {
		Messages.messageSource = messageSource;
	}

	public static String getMessage(String bundleKey, Object... args) {
		return messageSource == null ? bundleKey : messageSource.getMessage(bundleKey, args,
				LocaleContextHolder.getLocale());
	}

}
