package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.dto.PetDTO;
import com.devsteam.getname.telbot_shelterdc.model.Color;
import com.devsteam.getname.telbot_shelterdc.model.Status;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import com.devsteam.getname.telbot_shelterdc.service.PetService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.devsteam.getname.telbot_shelterdc.model.Kind.CAT;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PetControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PetRepository petRepository;

    @MockBean
    PetService petService;

    @Test
    void givenNoPetsInDatabase_whenGetPets_thenPetsNotFound() throws Exception {
        mockMvc.perform(get("/getAllPets"))
                .andExpect(status().isNotFound());
    }
    @Test
    void givenNoPetsInDatabase_whenPetAdded_thenItExistsInList() throws Exception {
        PetDTO testPet = new PetDTO(1L, 2019, "Pusheen", "tabby",
                "very friendly", Color.BLACK_AND_WHITE, Status.FREE, 0, CAT);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Pusheen", testPet);
        mockMvc.perform(post("http://localhost:8080/addPet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Pusheen"));
    }
//    @Test
//    void givenPetsInDatabase_thenItExistsInList() throws Exception {
//        PetDTO testPet = new PetDTO(1L, 2019, "Pusheen", "tabby",
//                "very friendly", Color.BLACK_AND_WHITE, Status.FREE, 0, CAT);
//        mockMvc.perform(post("/addPet", testPet)
//                .accept(MediaType.APPLICATION_JSON));
//        List<PetDTO> allPets = Arrays.asList(testPet);
//        when(petService.getAllPets(CAT)).thenReturn(allPets);
//
//        mockMvc.perform(get("/getAllPets")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].name", is(testPet.name())));
//    }
    }


