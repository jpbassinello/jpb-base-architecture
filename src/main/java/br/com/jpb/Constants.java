package br.com.jpb;

import java.time.format.DateTimeFormatter;

public interface Constants {

	DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/YYYY");
	DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm");
	String DEFAULT_TIME_ZONE_ID = "America/Sao_Paulo";
	String DEFAULT_LANGUAGE_TAG = "pt-BR";

}
