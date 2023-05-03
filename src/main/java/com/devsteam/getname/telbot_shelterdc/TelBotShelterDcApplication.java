package com.devsteam.getname.telbot_shelterdc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;

@SpringBootApplication
@EnableScheduling
public class TelBotShelterDcApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelBotShelterDcApplication.class, args);
	}

}
