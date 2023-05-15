package com.devsteam.getname.telbot_shelterdc.controller;

import com.devsteam.getname.telbot_shelterdc.dto.PetDTO;
import com.devsteam.getname.telbot_shelterdc.dto.ReportDTO;
import com.devsteam.getname.telbot_shelterdc.model.Color;
import com.devsteam.getname.telbot_shelterdc.model.Status;
import com.devsteam.getname.telbot_shelterdc.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static com.devsteam.getname.telbot_shelterdc.model.Kind.CAT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReportControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ReportService reportService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void givenNoReportInDatabase_whenGetReports_thenNoReportsAreFound() throws Exception {
        mockMvc.perform(get("http://localhost:8080/pets/report?kind=CAT"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void givenReportIsInDatabase_thenItIsFoundById() throws Exception {
        mockMvc.perform(post("http://localhost:8080/pets/report")
                        .content(objectMapper.writeValueAsString(reportService.addReport(405441405, "string", new byte[0])))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.chatId").isNotEmpty())
                .andExpect(jsonPath("$.chatId").isNumber())
                .andExpect(jsonPath("$.chatId").value(405441405))
                .andExpect(jsonPath("$.mealsWellBeingAndAdaptationBehaviorChanges").value("string"))
                .andExpect(jsonPath("$.photo").value("string"));

        mockMvc.perform(get("http://localhost:8080/pets/report/reportId?reportId=1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.chatId").value(405441405));
    }
}
