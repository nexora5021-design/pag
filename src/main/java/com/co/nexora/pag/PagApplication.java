package com.co.nexora.pag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PagApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagApplication.class, args);
	}

}
