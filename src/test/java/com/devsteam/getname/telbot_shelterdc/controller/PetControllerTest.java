package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.dto.PetDTO;
import com.devsteam.getname.telbot_shelterdc.model.Color;
import com.devsteam.getname.telbot_shelterdc.model.Status;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import com.devsteam.getname.telbot_shelterdc.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;


import static com.devsteam.getname.telbot_shelterdc.model.Gender.MALE;
import static com.devsteam.getname.telbot_shelterdc.model.Kind.CAT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PetControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PetRepository petRepository;

    @Autowired
    PetService petService;

    @Test
    void givenNoPetsInDatabase_whenGetPets_thenPetsNotFound() throws Exception {
        mockMvc.perform(get("http://localhost:8080/pets"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNoPetsInDatabase_whenPetAddedItIsAddedCorrectly() throws Exception {
        PetDTO testPet = new PetDTO(1L, 2019, "Pusheen", "tabby",
                "very friendly", Color.BLACK_AND_WHITE, Status.FREE, 0, CAT, MALE);

        mockMvc.perform(post("http://localhost:8080/pets/addPet")
                        .content(objectMapper.writeValueAsString(testPet))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pusheen"))
                .andExpect(jsonPath("$.birthYear").value(2019))
                .andExpect(jsonPath("$.breed").value("tabby"))
                .andExpect(jsonPath("$.description").value("very friendly"))
                .andExpect(jsonPath("$.color").value("BLACK_AND_WHITE"))
                .andExpect(jsonPath("$.status").value("FREE"))
                .andExpect(jsonPath("$.ownerId").value(0))
                .andExpect(jsonPath("$.kind").value("CAT"));
    }

    @Test
    void givenPetsInDatabase_thenItIsFoundById() throws Exception {
        PetDTO testPet = new PetDTO(1L, 2019, "Pusheen", "tabby",
                "very friendly", Color.BLACK_AND_WHITE, Status.FREE, 0, CAT, MALE);
        mockMvc.perform(post("http://localhost:8080/pets/addPet")
                        .content(objectMapper.writeValueAsString(testPet))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Pusheen"));

        mockMvc.perform(get("http://localhost:8080/pets/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Pusheen"));
    }
    @Test
    void givenPetsInDatabase_thenItIsDeletedByIdCorrectly() throws Exception {
        PetDTO testPet = new PetDTO(1L, 2019, "Pusheen", "tabby",
                "very friendly", Color.BLACK_AND_WHITE, Status.FREE, 0, CAT, MALE);
        mockMvc.perform(post("http://localhost:8080/pets/addPet")
                        .content(objectMapper.writeValueAsString(testPet))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Pusheen"));

        long id = testPet.id();

        mockMvc.perform(delete("/pets/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/pets"))
                .andExpect(status().isNotFound());
    }
    @Test
    void givenThereIsOnePetCreated_whenPetIsEdited_thenItIsChangedInDatabase() throws Exception {
        PetDTO testPet = new PetDTO(1L, 2019, "Pusheen", "tabby",
                "very friendly", Color.BLACK_AND_WHITE, Status.FREE, 0, CAT, MALE);
        mockMvc.perform(post("http://localhost:8080/pets/addPet")
                        .content(objectMapper.writeValueAsString(testPet))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Pusheen"));

        long id = testPet.id();

        PetDTO updatedPet = new PetDTO(1L, 2019, "Marusya", "tabby",
                "very friendly", Color.BLACK_AND_WHITE, Status.FREE, 0, CAT, MALE);
        mockMvc.perform(put("/pets/{id}", id)
                        .content(objectMapper.writeValueAsString(updatedPet))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Marusya"));

        mockMvc.perform(get("http://localhost:8080/pets/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Marusya"));
    }

}

