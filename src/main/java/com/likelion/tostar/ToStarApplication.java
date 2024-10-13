package com.likelion.tostar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Entity Listener available
public class ToStarApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToStarApplication.class, args);
	}

}
