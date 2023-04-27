package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.TelBotShelterDcApplication;
import com.devsteam.getname.telbot_shelterdc.exception.NoSuchEntityException;
import com.devsteam.getname.telbot_shelterdc.listener.TelegramBotUpdatesListener;
import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import com.devsteam.getname.telbot_shelterdc.repository.ShelterRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class ShelterServiceTest {

    @Autowired
    private  ShelterService shelterService;
    @Autowired
    private ShelterRepository shelterRepository;

    Shelter expectedShelter;

    Shelter expectedShelter2;


    @BeforeEach
     void setUp(){
        expectedShelter = new Shelter(1, "title", "schedule", "address", "maplink",
                "security", "info", "safetyPrecautions", "meetAndGreetRules",
                "docList", "transRules", "recommendatios",
                "recommendationsAdults", "recommendationsDisabled",
                "cyAdvices", "recomCy", "rejectList", 123);
         shelterService.save(expectedShelter);
    expectedShelter2 = new Shelter(2, "title", "schedule", "address", "maplink",
                                          "security", "info", "safetyPrecautions", "meetAndGreetRules",
                                          "docList", "transRules", "recommendatios",
                                          "recommendationsAdults", "recommendationsDisabled",
                                          "cyAdvices", "recomCy", "rejectList", 321);
         shelterService.save(expectedShelter);}


    @Test
    void getByIDWithValidDataReturnValidEntity() {
        Shelter actualShelter = shelterService.getByID(1);
        Assertions.assertEquals(expectedShelter.getInfo(), actualShelter.getInfo());
        Assertions.assertEquals(expectedShelter.getChatId(), actualShelter.getChatId());
        Assertions.assertEquals(expectedShelter.getDocList(), actualShelter.getDocList());
        Assertions.assertEquals(expectedShelter.getRecommendations(), actualShelter.getRecommendations());
        Assertions.assertEquals(expectedShelter.getSafetyPrecautions(), actualShelter.getSafetyPrecautions());
        Assertions.assertEquals(expectedShelter.getCynologistAdvice(), actualShelter.getCynologistAdvice());
        Assertions.assertEquals(expectedShelter.getMeetAndGreatRules(), actualShelter.getMeetAndGreatRules());
        Assertions.assertEquals(expectedShelter.getRecommendedCynologists(), actualShelter.getRecommendedCynologists());
        Assertions.assertEquals(expectedShelter.getRecommendationsAdult(), actualShelter.getRecommendationsAdult());
        Assertions.assertEquals(expectedShelter.getRecommendationsDisabled(), actualShelter.getRecommendationsDisabled());
        Assertions.assertEquals(expectedShelter.getTitle(), actualShelter.getTitle());
        Assertions.assertEquals(expectedShelter.getAddress(), actualShelter.getAddress());
        Assertions.assertEquals(expectedShelter.getSchedule(), actualShelter.getSchedule());
        Assertions.assertEquals(expectedShelter.getSecurity(), actualShelter.getSecurity());
        Assertions.assertEquals(expectedShelter.getTransportingRules(), actualShelter.getTransportingRules());
        Assertions.assertEquals(expectedShelter.getMapLink(), actualShelter.getMapLink());
        Assertions.assertEquals(expectedShelter.getRejectReasonsList(), actualShelter.getRejectReasonsList());

    }

    @Test
    void getByIDNotExistEntityThrowEntityNotFound(){
        Assertions.assertThrows(NoSuchEntityException.class, ()->{shelterService.getByID(3);});
    }

    @Test
    void editShelterContactsWithValidData() {

        int id=1;
        String address="new address";
        String schedule = "new schedule";
        String security = "new security";
        String title = "new title";
        String mapLink = "new MapLink";
        Shelter actualShelter = shelterService.editShelterContacts(1,address,schedule,security,title, mapLink);

        Assertions.assertEquals(address, actualShelter.getAddress());
        Assertions.assertEquals(schedule, actualShelter.getSchedule());
        Assertions.assertEquals(security, actualShelter.getSecurity());
        Assertions.assertEquals(title, actualShelter.getTitle());
        Assertions.assertEquals(mapLink, actualShelter.getMapLink());


    }
    @Test
    void editShelterContactsWithInvalidAddress() {

        String address=null;
        String schedule = "new schedule";
        String security = "new security";
        String title = "new title";
        String mapLink = "new MapLink";
        Shelter actualShelter = shelterService.editShelterContacts(1,address,schedule,security,title, mapLink);

        Assertions.assertNotNull(actualShelter.getAddress());
        Assertions.assertEquals(schedule, actualShelter.getSchedule());
        Assertions.assertEquals(security, actualShelter.getSecurity());
        Assertions.assertEquals(title, actualShelter.getTitle());
        Assertions.assertEquals(mapLink, actualShelter.getMapLink());
    }

    @Test
    void editSafetyRulesWithValidString() {
        Shelter actualShelter = shelterService.editSafetyRules(1, "New Safety Rules");
        assertEquals("New Safety Rules", actualShelter.getSafetyPrecautions());
    }

    @Test
    void editSafetyRulesWithNull() {
        Shelter actualShelter = shelterService.editSafetyRules(1, null);
        assertNotNull(actualShelter.getSafetyPrecautions());
    }
    @Test
    void editSafetyRulesWithEmptyString() {
        Shelter actualShelter = shelterService.editSafetyRules(1, "");
        assertNotNull(actualShelter.getSafetyPrecautions());
    }


    @Test
    void editTransportingRules() {
    }

    @Test
    void editRecommendations() {
    }

    @Test
    void editMeetAndGreetRules() {
    }

    @Test
    void editCynologistsAdvice() {
    }

    @Test
    void editDocList() {
    }

    @Test
    void editRejectReasonList() {
    }

    @Test
    void editCynologistList() {
    }

    @Test
    void editDescription() {
    }

    @Test
    void getShelterInfo() {
    }

    @Test
    void editRecommendationsAdult() {
    }

    @Test
    void editRecommendationsDisabled() {
    }
}