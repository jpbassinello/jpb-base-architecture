package br.com.jpb.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;

@Configuration
@Profile("test")
public class TestDatabaseConfig {

	@Bean
	MariaDB4jSpringService mariaDB4jSpringService() {
		final MariaDB4jSpringService mariaDB4jSpringService = new MariaDB4jSpringService();
		mariaDB4jSpringService.getConfiguration().addArg("--lower-case-table-names=1");
		return mariaDB4jSpringService;
	}

	@Bean
	DataSource dataSource(MariaDB4jSpringService mariaDB4jSpringService,
						  @Value("${mariaDB4j.databaseName}") String databaseName,
						  @Value("${spring.datasource.username}") String datasourceUsername,
						  @Value("${spring.datasource.password}") String datasourcePassword,
						  @Value("${spring.datasource.driver-class-name}") String datasourceDriver) throws ManagedProcessException {
		//Create our database with default root user and no password
		mariaDB4jSpringService().getDB().createDB(databaseName);

		DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();

		return DataSourceBuilder
				.create()
				.username(datasourceUsername)
				.password(datasourcePassword)
				.url(config.getURL(databaseName))
				.driverClassName(datasourceDriver)
				.build();
	}
}
