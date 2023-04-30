package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.dto.PetDTO;
import com.devsteam.getname.telbot_shelterdc.exception.WrongPetException;
import com.devsteam.getname.telbot_shelterdc.model.Color;
import com.devsteam.getname.telbot_shelterdc.model.Kind;
import com.devsteam.getname.telbot_shelterdc.model.Pet;
import com.devsteam.getname.telbot_shelterdc.model.Status;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PetServiceTests {

    @Autowired
    PetService petService;

    @Autowired
    PetRepository petRepository;


    @Test
    public void addingCatWithoutNameThrowsException() {
        Pet testPet = new Pet(2017, null, "tabby", "very friendly", Color.RED, Status.FREE, Kind.CAT);
        Exception exception = assertThrows(WrongPetException.class, () -> petService.addPet(PetDTO.petToDTO(testPet)));
        String expectedMessage = "Необходимо заполнить следующие поля: имя животного, порода, описание.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void addingCatWithoutBreedThrowsException() {
        Pet testPet = new Pet(2017, "Pusheen", null, "very friendly", Color.RED, Status.FREE, Kind.CAT);
        Exception exception = assertThrows(WrongPetException.class, () -> petService.addPet(PetDTO.petToDTO(testPet)));
        String expectedMessage = "Необходимо заполнить следующие поля: имя животного, порода, описание.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void addingCatWithoutDescriptionThrowsException() {
        Pet testPet = new Pet(2017, "Pusheen", "tabby", null, Color.BLACK_AND_WHITE, Status.FREE, Kind.CAT);
        Exception exception = assertThrows(WrongPetException.class, () -> petService.addPet(PetDTO.petToDTO(testPet)));
        String expectedMessage = "Необходимо заполнить следующие поля: имя животного, порода, описание.";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void addingCatWithTooSmallBirthYearThrowsException() {
        Pet testPet = new Pet(-2017, "Pusheen", "tabby", "very friendly", Color.BLACK_AND_WHITE, Status.FREE, Kind.CAT);
        Exception exception = assertThrows(WrongPetException.class, () -> petService.addPet(PetDTO.petToDTO(testPet)));
        String expectedMessage = "Год рождения животного не может быть меньше 2000 и больше текущего!";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void addingCatWithTooBigBirthYearThrowsException() {
        Pet testPet = new Pet(3125, "Pusheen", "tabby", "very friendly", Color.BLACK_AND_WHITE, Status.FREE, Kind.CAT);
        Exception exception = assertThrows(WrongPetException.class, () -> petService.addPet(PetDTO.petToDTO(testPet)));
        String expectedMessage = "Год рождения животного не может быть меньше 2000 и больше текущего!";
        String actualMessage = exception.getMessage();
        assertThat(actualMessage.equals(expectedMessage));
    }

    @Test
    public void checkIfCatIsAddedToRepository() {
        Pet testPet = new Pet(2019, "Pusheen", "tabby", "very friendly", Color.BLACK_AND_WHITE, Status.FREE, Kind.CAT);
        petService.addPet(PetDTO.petToDTO(testPet));
        assertEquals(PetDTO.petToDTO(testPet), petService.getPet(1L));
    }

    @Test
    public void checkIfPetIsFoundById() {
        Pet testPet = new Pet(2019, "Pusheen", "tabby", "very friendly", Color.BLACK_AND_WHITE, Status.FREE, Kind.CAT);
        petService.addPet(PetDTO.petToDTO(testPet));
        assertEquals(PetDTO.petToDTO(testPet), petService.getPet(1L));
    }

    @Test
    public void checkIfAllPetsAreReceivedCorrectly() {
        Pet testPet = new Pet(2019, "Pusheen", "tabby", "very friendly", Color.BLACK_AND_WHITE, Status.FREE, Kind.CAT);
        Pet testPet2 = new Pet(2010, "Argo", "scottish", "not very friendly", Color.GREY, Status.FREE, Kind.CAT);
        petService.addPet(PetDTO.petToDTO(testPet));
        petService.addPet(PetDTO.petToDTO(testPet2));
        List<PetDTO> addedPets = List.of(PetDTO.petToDTO(testPet), PetDTO.petToDTO(testPet2));
        assertEquals(PetDTO.petToDTO(testPet), petService.getPet(1L));
        assertEquals(PetDTO.petToDTO(testPet2), petService.getPet(2L));
    }

    @Test
    public void checkIfPetDescriptionIsUpdatedCorrectly() {
        Pet testPet = new Pet(2019, "Pusheen", "tabby", "very friendly", Color.BLACK_AND_WHITE, Status.FREE, Kind.CAT);
        Pet testPet2 = new Pet(2010, "Argo", "scottish", "not very friendly", Color.GREY, Status.FREE, Kind.CAT);
        petService.addPet(PetDTO.petToDTO(testPet));
        petService.addPet(PetDTO.petToDTO(testPet2));
        PetDTO updatedPet = new PetDTO(2L, 2010, "Argo", "scottish", "now is very friendly too", Color.GREY, Status.FREE, 0, Kind.CAT);
        petService.updatePet(updatedPet);
        String expected = "now is very friendly too";
        String actual = petService.getPet(2L).description();
        assertEquals(expected, actual);
    }

    @Test
    public void checkIfPetNameIsUpdatedCorrectly() {
        Pet testPet = new Pet(2019, "Pusheen", "tabby", "very friendly", Color.BLACK_AND_WHITE, Status.FREE, Kind.CAT);
        Pet testPet2 = new Pet(2010, "Argo", "scottish", "not very friendly", Color.GREY, Status.FREE, Kind.CAT);
        petService.addPet(PetDTO.petToDTO(testPet));
        petService.addPet(PetDTO.petToDTO(testPet2));
        PetDTO updatedPet = new PetDTO(2L, 2010, "Pushok", "scottish", "not very friendly", Color.GREY, Status.FREE, 0, Kind.CAT);
        petService.updatePet(updatedPet);
        String expected = "Pushok";
        String actual = petService.getPet(2L).name();
        assertEquals(expected, actual);
    }

    @Test
    public void checkIfPetBirthYearIsUpdatedCorrectly() {
        Pet testPet = new Pet(2019, "Pusheen", "tabby", "very friendly", Color.BLACK_AND_WHITE, Status.FREE, Kind.CAT);
        Pet testPet2 = new Pet(2010, "Argo", "scottish", "not very friendly", Color.GREY, Status.FREE, Kind.CAT);
        petService.addPet(PetDTO.petToDTO(testPet));
        petService.addPet(PetDTO.petToDTO(testPet2));
        PetDTO updatedPet = new PetDTO(2L, 2011, "Argo", "scottish", "not very friendly", Color.GREY, Status.FREE, 0, Kind.CAT);
        petService.updatePet(updatedPet);
        int expected = 2011;
        int actual = petService.getPet(2L).birthYear();
        assertEquals(expected, actual);
    }
    @AfterEach
    void clearDatabase() {
        petRepository.deleteAll();
    }
}