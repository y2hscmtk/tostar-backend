package com.choi76.base_code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Entity Listener available
public class BaseCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseCodeApplication.class, args);
	}

}
