package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.dto.PetDTO;
import com.devsteam.getname.telbot_shelterdc.dto.PetOwnerDTO;
import com.devsteam.getname.telbot_shelterdc.exception.NoSuchEntityException;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import com.devsteam.getname.telbot_shelterdc.repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.devsteam.getname.telbot_shelterdc.model.Kind.CAT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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


    public void setUp(){
        PetDTO pet1 = new PetDTO(0, 2020, "Chad", "mixed", "feisty", Color.SPOTTED, Status.FREE, 0, CAT);
        PetDTO pet2 = new PetDTO(0, 2023, "Inky", "black", "not very friendly", Color.BLACK, Status.FREE, 0, CAT);
        petService.addPet(pet1);
        petService.addPet(pet2);
        PetOwnerDTO owner1 = new PetOwnerDTO(0,0, "fullName", "phone", "address", StatusOwner.PROBATION, LocalDate.now(), 1);
        PetOwnerDTO owner2 = new PetOwnerDTO(0, 405441405, "fullName", "phone", "address", StatusOwner.PROBATION, LocalDate.now(), 2);
        petOwnerService.creatPetOwner(owner1);
        petOwnerService.creatPetOwner(owner2);
    }
    public void clearOwnerDB(){
        ownerRepository.deleteAll();
    }

    @Test
    public void addingReportWithNonExistentChatIdThrowsNoSuchEntityException(){
        Exception exception = assertThrows(NoSuchEntityException.class, () -> reportService
                .addReport(0, "string", "string"));
        String expectedMessage = "No owner with such chat ID";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage,actualMessage);
        clearOwnerDB();
    }

//    @Test
//    public void addingReportWithPetlessOwnerThrowsPetIsNotAssignedException(){
//        Exception exception = assertThrows(PetIsNotAssignedException.class, () -> reportService
//                .addReport(405441405L, "string", "string"));
//        String expectedMessage = "this owner doesn't have assigned pet yet";
//        String actualMessage = exception.getMessage();
//        assertEquals(expectedMessage,actualMessage);
//    }
    @Test
    public void addingReportWithEmptyMealsWellBeingAndAdaptationBehaviourChangesThrowsIllegalArgumentException(){
        setUp();
        assertThrows(IllegalArgumentException.class, () -> reportService
                .addReport(405441405, "", "string"));
        clearOwnerDB();

    }
    @Test
    public void addingReportWithBlankMealsWellBeingAndAdaptationBehaviourChangesThrowsIllegalArgumentException(){
        setUp();
        assertThrows(IllegalArgumentException.class, () -> reportService
                .addReport(405441405, " ", "string"));
        clearOwnerDB();
    }


}
