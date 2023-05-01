package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.dto.PetDTO;
import com.devsteam.getname.telbot_shelterdc.exception.WrongPetException;
import com.devsteam.getname.telbot_shelterdc.model.Color;
import com.devsteam.getname.telbot_shelterdc.model.Kind;
import com.devsteam.getname.telbot_shelterdc.model.Pet;
import com.devsteam.getname.telbot_shelterdc.model.Status;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static com.devsteam.getname.telbot_shelterdc.model.Kind.CAT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PetServiceTests {

    @Autowired
    PetService petService;

    @Autowired
    PetRepository petRepository;

    Pet testPet = new Pet(2019, "Pusheen", "tabby", "very friendly", Color.BLACK_AND_WHITE, Status.FREE, CAT);
    Pet testPet2 = new Pet(2010, "Argo", "scottish", "not very friendly", Color.GREY, Status.FREE, CAT);


    @Test
    public void addingCatWithoutNameThrowsException() {
        Pet testPet = new Pet(2017, null, "tabby", "very friendly", Color.RED, Status.FREE, CAT);
        Exception exception = assertThrows(WrongPetException.class, () -> petService.addPet(PetDTO.petToDTO(testPet)));
        String expectedMessage = "Необходимо заполнить следующие поля: имя животного, порода, описание.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void addingCatWithoutBreedThrowsException() {
        Pet testPet = new Pet(2017, "Pusheen", null, "very friendly", Color.RED, Status.FREE, CAT);
        Exception exception = assertThrows(WrongPetException.class, () -> petService.addPet(PetDTO.petToDTO(testPet)));
        String expectedMessage = "Необходимо заполнить следующие поля: имя животного, порода, описание.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void addingCatWithoutDescriptionThrowsException() {
        Pet testPet = new Pet(2017, "Pusheen", "tabby", null, Color.BLACK_AND_WHITE, Status.FREE, CAT);
        Exception exception = assertThrows(WrongPetException.class, () -> petService.addPet(PetDTO.petToDTO(testPet)));
        String expectedMessage = "Необходимо заполнить следующие поля: имя животного, порода, описание.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void addingCatWithTooSmallBirthYearThrowsException() {
        Pet testPet = new Pet(-2017, "Pusheen", "tabby", "very friendly", Color.BLACK_AND_WHITE, Status.FREE, CAT);
        Exception exception = assertThrows(WrongPetException.class, () -> petService.addPet(PetDTO.petToDTO(testPet)));
        String expectedMessage = "Год рождения животного не может быть меньше 2000 и больше текущего!";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void addingCatWithTooBigBirthYearThrowsException() {
        Pet testPet = new Pet(3125, "Pusheen", "tabby", "very friendly", Color.BLACK_AND_WHITE, Status.FREE, CAT);
        Exception exception = assertThrows(WrongPetException.class, () -> petService.addPet(PetDTO.petToDTO(testPet)));
        String expectedMessage = "Год рождения животного не может быть меньше 2000 и больше текущего!";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void checkIfPetIsAddedToRepository() {
        long idTestPet = petService.addPet(PetDTO.petToDTO(testPet)).id();
        PetDTO expectedPet = new PetDTO(1L, 2019, "Pusheen", "tabby",
                "very friendly", Color.BLACK_AND_WHITE, Status.FREE, 0, CAT);
        assertEquals(expectedPet, petService.getPet(idTestPet));
    }

    @Test
    public void checkIfPetIsFoundById() {
        long idTestPet = petService.addPet(PetDTO.petToDTO(testPet)).id();
        PetDTO expectedPet = new PetDTO(1L, 2019, "Pusheen", "tabby",
                "very friendly", Color.BLACK_AND_WHITE, Status.FREE, 0, CAT);
        assertEquals(expectedPet, petService.getPet(idTestPet));
    }

    @Test
    public void checkIfAllPetsAreReceivedCorrectly() {
        long idTestPet1 = petService.addPet(PetDTO.petToDTO(testPet)).id();
        long idTestPet2 = petService.addPet(PetDTO.petToDTO(testPet2)).id();
        PetDTO expectedPet1 = new PetDTO(1L, 2019, "Pusheen", "tabby",
                "very friendly", Color.BLACK_AND_WHITE, Status.FREE, 0, CAT);
        PetDTO expectedPet2 = new PetDTO(2L, 2010, "Argo", "scottish",
                "not very friendly", Color.GREY, Status.FREE, 0, CAT);
        assertEquals(expectedPet1, petService.getPet(idTestPet1));
        assertEquals(expectedPet2, petService.getPet(idTestPet2));
    }

    @Test
    public void checkIfPetDescriptionIsUpdatedCorrectly() {
        petService.addPet(PetDTO.petToDTO(testPet));
        petService.addPet(PetDTO.petToDTO(testPet2));
        PetDTO updatedPet = new PetDTO(2L, 2010, "Argo", "scottish",
                "now is very friendly too", Color.GREY, Status.FREE, 0, CAT);
        petService.updatePet(updatedPet);
        String expected = "now is very friendly too";
        String actual = petService.getPet(2L).description();
        assertEquals(expected, actual);
    }

    @Test
    public void checkIfPetNameIsUpdatedCorrectly() {
        petService.addPet(PetDTO.petToDTO(testPet));
        petService.addPet(PetDTO.petToDTO(testPet2));
        PetDTO updatedPet = new PetDTO(2L, 2010, "Pushok", "scottish", "not very friendly", Color.GREY, Status.FREE, 0, CAT);
        petService.updatePet(updatedPet);
        String expected = "Pushok";
        String actual = petService.getPet(2L).name();
        assertEquals(expected, actual);
    }

    @Test
    public void checkIfPetBirthYearIsUpdatedCorrectly() {
        petService.addPet(PetDTO.petToDTO(testPet));
        petService.addPet(PetDTO.petToDTO(testPet2));
        PetDTO updatedPet = new PetDTO(2L, 2011, "Argo", "scottish", "not very friendly", Color.GREY, Status.FREE, 0, CAT);
        petService.updatePet(updatedPet);
        int expected = 2011;
        int actual = petService.getPet(2L).birthYear();
        assertEquals(expected, actual);
    }

    @Test
    public void checkIfPetColorIsUpdatedCorrectly() {
        petService.addPet(PetDTO.petToDTO(testPet));
        petService.addPet(PetDTO.petToDTO(testPet2));
        PetDTO updatedPet = new PetDTO(2L, 2011, "Argo", "scottish", "not very friendly", Color.WHITE, Status.FREE, 0, CAT);
        petService.updatePet(updatedPet);
        Color expected = Color.WHITE;
        Color actual = petService.getPet(2L).color();
        assertEquals(expected, actual);
    }

    @Test
    public void checkIfPetBreedIsUpdatedCorrectly() {
        petService.addPet(PetDTO.petToDTO(testPet));
        petService.addPet(PetDTO.petToDTO(testPet2));
        PetDTO updatedPet = new PetDTO(2L, 2010, "Argo", "tabby", "not very friendly", Color.GREY, Status.FREE, 0, CAT);
        petService.updatePet(updatedPet);
        String expected = "tabby";
        String actual = petService.getPet(2L).breed();
        assertEquals(expected, actual);
    }

    @Test
    public void checkIfPetStatusIsUpdatedCorrectly() {
        petService.addPet(PetDTO.petToDTO(testPet));
        petService.addPet(PetDTO.petToDTO(testPet2));
        PetDTO updatedPet = new PetDTO(2L, 2010, "Argo", "scottish", "not very friendly", Color.GREY, Status.ADOPTED, 0, CAT);
        petService.updatePet(updatedPet);
        Status expected = Status.ADOPTED;
        Status actual = petService.getPet(2L).status();
        assertEquals(expected, actual);
    }

    @Test
    public void checkIfPetIsDeletedCorrectly() {
        PetDTO testPet3 = new PetDTO(781945L, 2014, "Marusya", "sphynx",
                "calm and independent", Color.GREY, Status.FREE, 0, CAT);
        long id = petService.addPet(testPet3).id();
        petService.removePet(id);
        Assertions.assertFalse(petService.getAllPets(CAT).contains(testPet3));
    }
}