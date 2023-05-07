package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.service.ShelterService;

import kotlin.jvm.Throws;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShelterControllerTest {

    @Mock
    ShelterService service;
    private MockMvc mockMvc;
    @Spy
    @InjectMocks
    private ShelterController controller;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    void uploadDogShelterCorrectFile() throws Exception {

        FileInputStream fis = new FileInputStream("src/main/resources/dogShelter.json");
        MockMultipartFile file = new MockMultipartFile("file", "file.json", MediaType.MULTIPART_FORM_DATA_VALUE, fis);

        mockMvc
                = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("http://localhost:8080/files/uploadDogShelter").file(file))
                .andExpect(status().isOk());}

    @Test
    void uploadCatShelterCorrectFile() throws Exception {
        FileInputStream fis = new FileInputStream("src/main/resources/catShelter.json");
        MockMultipartFile file = new MockMultipartFile("file", "file.json", MediaType.MULTIPART_FORM_DATA_VALUE, fis);

        mockMvc
                = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("http://localhost:8080/files/uploadCatShelter").file(file))
                .andExpect(status().isOk());
    }

    @Test
    void downloadDogShelter() throws Exception {
        Mockito.when(service.getDataFile(any())).thenReturn(new File("src/main/resources/dogShelter.json"));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/files/downloadDogShelter").contentType(MediaType.APPLICATION_OCTET_STREAM)).andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
        Assertions.assertEquals("application/json", result.getResponse().getContentType());
    }

    @Test
    void downloadCatShelter() throws Exception {
        Mockito.when(service.getDataFile(any())).thenReturn(new File("src/main/resources/catShelter.json"));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/files/downloadCatShelter").contentType(MediaType.APPLICATION_OCTET_STREAM)).andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        Assertions.assertEquals(200, result.getResponse().getStatus());
        Assertions.assertEquals("application/json", result.getResponse().getContentType());
    }

}