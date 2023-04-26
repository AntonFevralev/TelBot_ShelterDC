package com.devsteam.getname.telbot_shelterdc;

import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import com.devsteam.getname.telbot_shelterdc.repository.ShelterRepository;
import com.devsteam.getname.telbot_shelterdc.service.ShelterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootApplication
public class TelBotShelterDcApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelBotShelterDcApplication.class, args);
	}

}
