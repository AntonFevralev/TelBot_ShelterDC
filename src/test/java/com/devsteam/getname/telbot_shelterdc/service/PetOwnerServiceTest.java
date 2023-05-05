package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.dto.PetDTO;
import com.devsteam.getname.telbot_shelterdc.dto.PetOwnerDTO;
import com.devsteam.getname.telbot_shelterdc.exception.PetIsNotFreeException;
import com.devsteam.getname.telbot_shelterdc.exception.WrongPetException;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.devsteam.getname.telbot_shelterdc.model.Color.RED;
import static com.devsteam.getname.telbot_shelterdc.model.Kind.CAT;
import static com.devsteam.getname.telbot_shelterdc.model.Status.BUSY;
import static com.devsteam.getname.telbot_shelterdc.model.Status.FREE;
import static com.devsteam.getname.telbot_shelterdc.model.StatusOwner.PROBATION;
import static com.devsteam.getname.telbot_shelterdc.model.StatusOwner.SEARCH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PetOwnerServiceTest {
    @Autowired
    PetOwnerService service;

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    PetService petService;

    @Autowired
    PetRepository petRepository;


        Pet petTest = new Pet(1L, 2017, "Cat", "tabby", "very friendly", RED, FREE, CAT);
        PetOwner petOwnerTest = new PetOwner(1L, "fullname", "phone", "address",
                PROBATION, LocalDate.now(), petTest);
        PetOwnerDTO petOwnerDTOtest = new PetOwnerDTO(0L, 1L, "fullname", "phone",
                "address", PROBATION, LocalDate.now(), 1L);


    @Test
    public void addingOwnerNullChatIdThrowsException() {
        PetOwnerDTO petOwnerDTO = new PetOwnerDTO(0L,0L, "fullname", "phone",
                "address",PROBATION,LocalDate.now(),1L );
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.creatPetOwner(petOwnerDTO));
        String expectedMessage = "Данные заполнены не корректно.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void addingOwnerWithoutFullNameThrowsException() {
        PetOwnerDTO petOwnerDTO = new PetOwnerDTO(0L,1L, null, "phone",
                "address",PROBATION,LocalDate.now(),1L );
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.creatPetOwner(petOwnerDTO));
        String expectedMessage = "Данные заполнены не корректно.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void addingOwnerWithoutPhoneThrowsException() {
        PetOwnerDTO petOwnerDTO = new PetOwnerDTO(0L,1L, "fullname", null,
                "address",PROBATION,LocalDate.now(),1L );
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.creatPetOwner(petOwnerDTO));
        String expectedMessage = "Данные заполнены не корректно.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void addingOwnerWithoutAddressThrowsException() {
        PetOwnerDTO petOwnerDTO = new PetOwnerDTO(0L,1L, "fullname", "phone",
                null,PROBATION,LocalDate.now(),1L );
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.creatPetOwner(petOwnerDTO));
        String expectedMessage = "Данные заполнены не корректно.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void addingOwnerIfPetIsNotFreeThrowsException() {
        PetDTO petDTO = new PetDTO(1L,2017, "Cat","tabby","frendly",RED,BUSY,0L,CAT);
        PetOwnerDTO petOwnerDTO = new PetOwnerDTO(0L,1L, "fullname", "phone",
                "address",PROBATION,LocalDate.now(),petDTO.id() );
        Exception exception = assertThrows(PetIsNotFreeException.class,
                () -> service.creatPetOwner(petOwnerDTO));
        String expectedMessage = "Животное занято другим человеком.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void checkIfPetOwnerIsAddedToRepository() {
        PetOwnerDTO petOwnerDTOtest = new PetOwnerDTO(1L, 1L, "fullname", "phone",
                "address", PROBATION, LocalDate.now(), 3L);
        long testPetOwnerId = service.creatPetOwner(petOwnerDTOtest).idCO(); // животное занято!?
        PetOwnerDTO expectedPetOwnerDTO = new PetOwnerDTO(1L,1L, "fullname", "phone",
                "address",PROBATION,LocalDate.now(),3L );
        assertEquals(expectedPetOwnerDTO, service.getPetOwner(testPetOwnerId));
    }

    @Test
    public void checkIfPetOwnerStatusIsUpdated() {
        PetOwner updatedPetOwner = new PetOwner(1L,1L, "fullname", "phone", "address",
                PROBATION);
        ownerRepository.save(updatedPetOwner);
        long testIdCO = updatedPetOwner.getIdCO();
        PetOwner actualOwner = service.changeStatusOwnerByIdCO(testIdCO,SEARCH);
        StatusOwner actual = actualOwner.getStatusOwner();
        assertEquals(SEARCH, actual);
    }

    @Test
    public void checkIfPetOwnerIsDeletedCorrectly() {
        PetOwnerDTO petOwnerDTOtest = new PetOwnerDTO(0L, 1L, "fullname", "phone",
                "address", PROBATION,LocalDate.now(),1L);
        long idCO = service.creatPetOwner(petOwnerDTOtest).idCO();
        service.deletePetOwnerByIdCO(idCO);
        Assertions.assertFalse(service.getAllPetOwners().contains(petOwnerDTOtest));
    }


}

