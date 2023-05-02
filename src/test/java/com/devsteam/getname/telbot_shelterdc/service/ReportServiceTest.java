package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.dto.PetDTO;
import com.devsteam.getname.telbot_shelterdc.dto.PetOwnerDTO;
import com.devsteam.getname.telbot_shelterdc.dto.ReportDTO;
import com.devsteam.getname.telbot_shelterdc.exception.NoSuchEntityException;
import com.devsteam.getname.telbot_shelterdc.exception.ReportListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import com.devsteam.getname.telbot_shelterdc.repository.ReportRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.devsteam.getname.telbot_shelterdc.model.Kind.CAT;
import static com.devsteam.getname.telbot_shelterdc.model.Kind.DOG;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReportServiceTest {

    @Autowired
    ReportService reportService;
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    PetOwnerService petOwnerService;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    PetService petService;
    @Autowired
    PetRepository petRepository;

    @BeforeEach
    public void setUp(){
        PetDTO pet1 = new PetDTO(0, 2020, "Chad", "mixed", "feisty", Color.SPOTTED, Status.FREE, 0, CAT);
        petService.addPet(pet1);
        PetOwnerDTO owner1 = new PetOwnerDTO(0, 405441405, "fullName", "phone", "address", StatusOwner.PROBATION, LocalDate.now(), 1);
        petOwnerService.creatPetOwner(owner1);
    }
    @AfterEach
    public void clearOwnerDB(){
        ownerRepository.deleteAll();
    }

    @Test
    public void addingReportWithCorrectParametersCreatesReportAndAddsItToDB(){
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId,"mealsAndStuff", "photo");
        assertTrue(reportService.getReportsByChatId(chatId).contains(reportService.getReportByReportId(reportDTO.id())));
    }

    @Test
    public void addingReportWithNonExistentChatIdThrowsNoSuchEntityException(){
        Exception exception = assertThrows(NoSuchEntityException.class, () -> reportService
                .addReport(0, "string", "string"));
        String expectedMessage = "No owner with such chat ID";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage,actualMessage);
    }

    @Test
    public void addingReportWithEmptyMealsWellBeingAndAdaptationBehaviourChangesThrowsIllegalArgumentException(){
        assertThrows(IllegalArgumentException.class, () -> reportService
                .addReport(405441405, "", "string"));
    }

    @Test
    public void addingReportWithBlankMealsWellBeingAndAdaptationBehaviourChangesThrowsIllegalArgumentException(){
        assertThrows(IllegalArgumentException.class, () -> reportService
                .addReport(405441405, " ", "string"));
    }
    @Test
    public void addingReportWithEmptyPhotoThrowsIllegalArgumentException(){
        assertThrows(IllegalArgumentException.class, () -> reportService
                .addReport(405441405, "1", ""));
    }

    @Test
    public void addingReportWithBlankPhotoThrowsIllegalArgumentException(){
        assertThrows(IllegalArgumentException.class, () -> reportService
                .addReport(405441405, "1", " "));
    }

    @Test
    public void gettingReportByExistingReportIdReturnsCorrectReportDTO(){
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId,"mealsAndStuff", "photo");
        assertEquals(reportService.getReportByReportId(reportDTO.id()), reportDTO);
    }

    @Test
    public void gettingReportByNonExistingReportIdThrowsNoSuchEntityException(){
        assertThrows(NoSuchEntityException.class, () -> reportService.getReportByReportId(0));
    }

    @Test
    public void gettingReportsByExistingPetIdReturnsCorrectReportDTOList(){
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId,"mealsAndStuff", "photo");
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        assertEquals(reportDTOS, reportService.getReportsByPetId(reportDTO.petId()));
    }

    @Test
    public void gettingReportsByNonExistingPetIdThrowsNoSuchEntityException(){
        assertThrows(NoSuchEntityException.class, () -> reportService.getReportsByPetId(0));
    }

    @Test
    public void gettingReportsByExistingDateAndKindReturnsCorrectReportDTOList(){
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId,"mealsAndStuff", "photo");
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        Kind kind = petService.getPet(reportDTO.petId()).kind();
        LocalDate date = reportDTO.reportDate();
        assertEquals(reportDTOS, reportService.getReportsByDate(date, kind));
    }

    @Test
    public void gettingReportsByNonExistingInDBDateAndKindThrowsNoSuchEntityException(){
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId,"mealsAndStuff", "photo");
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        Kind kind = petService.getPet(reportDTO.petId()).kind();
        LocalDate date = reportDTO.reportDate();
        assertThrows(NoSuchEntityException.class, () -> reportService.getReportsByDate(date.plusDays(1), kind));
    }

    @Test
    public void gettingReportsByNonExistingInDBKindAndKindThrowsNoSuchEntityException(){
        PetDTO pet2 = new PetDTO(0, 2021, "Lara", "Labrador", "kind", Color.WHITE, Status.FREE, 0, DOG);
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId,"mealsAndStuff", "photo");
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        Kind kind = petService.getPet(reportDTO.petId()).kind();
        LocalDate date = reportDTO.reportDate();
        assertThrows(NoSuchEntityException.class, () -> reportService.getReportsByDate(date, pet2.kind()));
    }






}
