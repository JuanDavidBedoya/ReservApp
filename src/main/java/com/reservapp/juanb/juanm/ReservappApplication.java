package com.reservapp.juanb.juanm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReservappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservappApplication.class, args);
	}

}
