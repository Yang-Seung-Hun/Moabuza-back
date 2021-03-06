package com.project.moabuja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MoabujaApplication {
	public static void main(String[] args) {
		SpringApplication.run(MoabujaApplication.class, args);
	}
}
