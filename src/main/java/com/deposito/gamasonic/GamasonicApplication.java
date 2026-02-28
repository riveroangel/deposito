package com.deposito.gamasonic;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.TimeZone;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})


public class GamasonicApplication {
	@PostConstruct
	public void init() {
		// Ajusta esto a tu zona horaria
		TimeZone.setDefault(TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
	}

	public static void main(String[] args) {
		SpringApplication.run(GamasonicApplication.class, args);
	}
}