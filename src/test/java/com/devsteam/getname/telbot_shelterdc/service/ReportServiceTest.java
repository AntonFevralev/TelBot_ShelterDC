package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.exception.NoSuchEntityException;
import com.devsteam.getname.telbot_shelterdc.exception.PetIsNotAssignedException;
import com.devsteam.getname.telbot_shelterdc.model.Color;
import com.devsteam.getname.telbot_shelterdc.model.Pet;
import com.devsteam.getname.telbot_shelterdc.model.Status;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import com.devsteam.getname.telbot_shelterdc.repository.ReportRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    OwnerRepository ownerRepository;
    @Autowired
    PetRepository petRepository;

//    Pet pet1 = new Pet(2020, "Chad", "mixed", "feisty", Color.SPOTTED, Status.FREE, CAT);
//    Pet pet2 = new Pet(2023, "Inky", "black", "not very friendly", Color.BLACK, Status.FREE, CAT);

    @Test
    @Order(2)
    public void addingReportWithNonExistentChatIdThrowsNoSuchEntityException(){
        Exception exception = assertThrows(NoSuchEntityException.class, () -> reportService
                .addReport(0, "string", "string"));
        String expectedMessage = "No owner with such chat ID";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage,actualMessage);
    }

    @Test
    @Order(1)
    public void addingReportWithPetlessOwnerThrowsPetIsNotAssignedException(){
        Exception exception = assertThrows(PetIsNotAssignedException.class, () -> reportService
                .addReport(405441405, "string", "string"));
        String expectedMessage = "this owner doesn't have assigned pet yet";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage,actualMessage);
    }



}
