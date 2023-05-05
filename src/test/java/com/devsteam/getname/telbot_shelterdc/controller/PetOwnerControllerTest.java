package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.dto.PetDTO;
import com.devsteam.getname.telbot_shelterdc.dto.PetOwnerDTO;
import com.devsteam.getname.telbot_shelterdc.model.Color;
import com.devsteam.getname.telbot_shelterdc.model.Status;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.service.PetOwnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.devsteam.getname.telbot_shelterdc.model.Kind.CAT;
import static com.devsteam.getname.telbot_shelterdc.model.StatusOwner.PROBATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PetOwnerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    PetOwnerService petOwnerService;

    @Test
    void givenNoPetOwnersInDatabase_whenGetOwners_thenOwnersNotFound() throws Exception {
        mockMvc.perform(get("http://localhost:8080/petowner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenNoPetsInDatabase_whenPetAddedItIsAddedCorrectly() throws Exception {
        PetOwnerDTO test = new PetOwnerDTO(0L, 112L, "fullName", "phone",
                "address", PROBATION, LocalDate.now(), 3L);

        mockMvc.perform(post("http://localhost:8080/petowner")
                        .content(objectMapper.writeValueAsString(test))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.idCO").isNotEmpty())
                .andExpect(jsonPath("$.idCO").isNumber())
                .andExpect(jsonPath("$.idCO").value(1L))
                .andExpect(jsonPath("$.chatId").value(112L))
                .andExpect(jsonPath("$.fullName").value("fullName"))
                .andExpect(jsonPath("$.phone").value("phone"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.statusOwner").value("PROBATION"))
                .andExpect(jsonPath("$.start").value(LocalDate.now()))
                .andExpect(jsonPath("$.petId").value(3L));
    }

    @Test
    void givenPetsInDatabase_thenItIsFoundById() throws Exception {
        PetOwnerDTO test = new PetOwnerDTO(0L, 112L, "fullName", "phone",
                "address", PROBATION, LocalDate.now(), 3L);
        mockMvc.perform(post("http://localhost:8080/petowner")
                        .content(objectMapper.writeValueAsString(test))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.idCO").isNotEmpty())
                .andExpect(jsonPath("$.idCO").isNumber())
                .andExpect(jsonPath("$.chatId").value(112L))
                .andExpect(jsonPath("$.fullName").value("fullName"))
                .andExpect(jsonPath("$.phone").value("phone"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.statusOwner").value("PROBATION"))
                .andExpect(jsonPath("$.start").value(LocalDate.now()))
                .andExpect(jsonPath("$.petId").value(3L));

        mockMvc.perform(get("http://localhost:8080/petowner/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.idCO").isNotEmpty())
                .andExpect(jsonPath("$.idCO").isNumber())
                .andExpect(jsonPath("$.chatId").value(112L))
                .andExpect(jsonPath("$.fullName").value("fullName"))
                .andExpect(jsonPath("$.phone").value("phone"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.statusOwner").value("PROBATION"))
                .andExpect(jsonPath("$.start").value(LocalDate.now()))
                .andExpect(jsonPath("$.petId").value(3L));
    }

    //-----------------------------------------------------------------------------------
    @Test
    void givenPetOwnersInDatabase_thenItIsDeletedById() throws Exception {
        PetOwnerDTO test = new PetOwnerDTO(0L, 112L, "fullName", "phone",
                "address", PROBATION, LocalDate.now(), 3L);
        mockMvc.perform(post("http://localhost:8080/petowner")
                        .content(objectMapper.writeValueAsString(test))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.idCO").isNotEmpty())
                .andExpect(jsonPath("$.idCO").isNumber())
                .andExpect(jsonPath("$.chatId").value(112L))
                .andExpect(jsonPath("$.fullName").value("fullName"))
                .andExpect(jsonPath("$.phone").value("phone"))
                .andExpect(jsonPath("$.address").value("address"))
                .andExpect(jsonPath("$.statusOwner").value("PROBATION"))
                .andExpect(jsonPath("$.start").value(LocalDate.now()))
                .andExpect(jsonPath("$.petId").value(3L));

        long idCO = test.idCO();

        mockMvc.perform(delete("/petowner/{idCO}", idCO))
                .andExpect(status().isOk());

        mockMvc.perform(get("/petowner"))  // почему без id ?
                .andExpect(status().isNotFound());

    }
}
    //-----------------------------------------------------------------------------------
//    @Test
//    void givenThereIsOnePetCreated_whenPetIsEdited_thenItIsChangedInDatabase() throws Exception {
//        PetDTO testPet = new PetDTO(1L, 2019, "Pusheen", "tabby",
//                "very friendly", Color.BLACK_AND_WHITE, Status.FREE, 0, CAT);
//        mockMvc.perform(post("http://localhost:8080/pets/addPet")
//                        .content(objectMapper.writeValueAsString(testPet))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.id").isNotEmpty())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.name").value("Pusheen"));
//
//        long id = testPet.id();
//
//        PetDTO updatedPet = new PetDTO(1L, 2019, "Marusya", "tabby",
//                "very friendly", Color.BLACK_AND_WHITE, Status.FREE, 0, CAT);
//        mockMvc.perform(put("/pets/{id}", id)
//                        .content(objectMapper.writeValueAsString(updatedPet))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.id").isNotEmpty())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.name").value("Marusya"));
//
//        mockMvc.perform(get("http://localhost:8080/pets/1"))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.id").isNotEmpty())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.name").value("Marusya"));
//    }



