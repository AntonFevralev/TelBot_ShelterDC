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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ShelterServiceTest {

    @Autowired
    private ShelterService shelterService;

    Shelter expectedShelter;

    Shelter expectedShelter2;


    @BeforeEach
    void setUp() {
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
        shelterService.save(expectedShelter);
    }


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
    void getByIDNotExistEntityThrowEntityNotFound() {
        Assertions.assertThrows(NoSuchEntityException.class, () -> {
            shelterService.getByID(3);
        });
    }

    @Test
    void editShelterContactsWithValidData() {

        int id = 1;
        String address = "new address";
        String schedule = "new schedule";
        String security = "new security";
        String title = "new title";
        String mapLink = "new MapLink";
        Shelter actualShelter = shelterService.editShelterContacts(1, address, schedule, security, title, mapLink);

        Assertions.assertEquals(address, actualShelter.getAddress());
        Assertions.assertEquals(schedule, actualShelter.getSchedule());
        Assertions.assertEquals(security, actualShelter.getSecurity());
        Assertions.assertEquals(title, actualShelter.getTitle());
        Assertions.assertEquals(mapLink, actualShelter.getMapLink());


    }

    @Test
    void editShelterContactsWithInvalidAddress() {

        String address = null;
        String schedule = "new schedule";
        String security = "new security";
        String title = "new title";
        String mapLink = "new MapLink";
        Shelter actualShelter = shelterService.editShelterContacts(1, address, schedule, security, title, mapLink);

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
        assertNotEquals("", actualShelter.getSafetyPrecautions());
    }

    @Test
    void editTransportingRulesWithValidString() {
        Shelter actualShelter = shelterService.editTransportingRules(1, "TransRules");
        assertEquals("TransRules", actualShelter.getTransportingRules());
    }


    @Test
    void editTransportingRulesWithEmptyString() {
        Shelter actualShelter = shelterService.editTransportingRules(1, "");
        assertNotEquals("", actualShelter.getTransportingRules());
    }

    @Test
    void editTransportingRulesWithNull() {
        Shelter actualShelter = shelterService.editTransportingRules(1, null);
        assertNotNull(actualShelter.getTransportingRules());
    }

    @Test
    void editRecommendationsWithValidString() {
        Shelter actualShelter = shelterService.editRecommendations(1, "Recommens");
        assertEquals("Recommens", actualShelter.getRecommendations());
    }

    @Test
    void editRecommendationsWithEmptyString() {
        Shelter actualShelter = shelterService.editRecommendations(1, "");
        assertNotEquals("", actualShelter.getRecommendations());
    }

    @Test
    void editRecommendationsWithNull() {
        Shelter actualShelter = shelterService.editRecommendations(1, null);
        assertNotNull(actualShelter.getRecommendations());
    }

    @Test
    void editMeetAndGreetRulesWithValidString() {
        Shelter actualShelter = shelterService.editMeetAndGreetRules(1, "GreetAndMeet");
        assertEquals("GreetAndMeet", actualShelter.getMeetAndGreatRules());
    }

    @Test
    void editMeetAndGreetRulesWithEmptyString() {
        Shelter actualShelter = shelterService.editMeetAndGreetRules(1, "");
        assertNotEquals("", actualShelter.getMeetAndGreatRules());
    }

    @Test
    void editMeetAndGreetRulesWithEmptyNull() {
        Shelter actualShelter = shelterService.editMeetAndGreetRules(1, null);
        assertNotNull(actualShelter.getMeetAndGreatRules());
    }

    @Test
    void editCynologistsAdviceWithValidString() {
        Shelter actualShelter = shelterService.editCynologistsAdvice("advice");
        assertEquals("advice", actualShelter.getCynologistAdvice());
    }

    @Test
    void editCynologistsAdviceWithEmptyString() {
        Shelter actualShelter = shelterService.editCynologistsAdvice("");
        assertNotEquals("", actualShelter.getCynologistAdvice());
    }

    @Test
    void editCynologistsAdviceWithNull() {
        Shelter actualShelter = shelterService.editCynologistsAdvice(null);
        assertNotNull(actualShelter.getCynologistAdvice());
    }

    @Test
    void editDocListWithValidString() {
        Shelter actualShelter = shelterService.editDocList(1, "doclist");
        assertEquals("doclist", actualShelter.getDocList());
    }

    @Test
    void editDocListWithEmptyString() {
        Shelter actualShelter = shelterService.editDocList(1, "");
        assertNotEquals("", actualShelter.getDocList());
    }

    @Test
    void editDocListWithNull() {
        Shelter actualShelter = shelterService.editDocList(1, null);
        assertNotNull(actualShelter.getDocList());
    }


    @Test
    void editRejectReasonListWithValidString() {
        Shelter actualShelter = shelterService.editRejectReasonList(1, "rejectlist");
        assertEquals("rejectlist", actualShelter.getRejectReasonsList());
    }

    @Test
    void editRejectReasonListWithEmptyString() {
        Shelter actualShelter = shelterService.editRejectReasonList(1, "");
        assertNotEquals("", actualShelter.getRejectReasonsList());
    }

    @Test
    void editRejectReasonListWithNull() {
        Shelter actualShelter = shelterService.editRejectReasonList(1, null);
        assertNotNull(actualShelter.getRejectReasonsList());
    }

    @Test
    void editCynologistListWithValidString() {
        Shelter actualShelter = shelterService.editCynologistList("cynololist");
        assertEquals("cynololist", actualShelter.getRecommendedCynologists());
    }

    @Test
    void editCynologistListWithEmptyString() {
        Shelter actualShelter = shelterService.editCynologistList("");
        assertNotEquals("", actualShelter.getRecommendedCynologists());
    }

    @Test
    void editCynologistListWithNull() {
        Shelter actualShelter = shelterService.editCynologistList(null);
        assertNotNull(actualShelter.getRecommendedCynologists());
    }

    @Test
    void editDescriptionWithValidString() {
        Shelter actualShelter = shelterService.editDescription(1, "description");
        assertEquals("description", actualShelter.getInfo());
    }

    @Test
    void editDescriptionWithEmptyString() {
        Shelter actualShelter = shelterService.editDescription(1, "");
        assertNotEquals("", actualShelter.getInfo());
    }

    @Test
    void editDescriptionWithNull() {
        Shelter actualShelter = shelterService.editDescription(1, null);
        assertNotNull(actualShelter.getInfo());
    }

    @Test
    void editRecommendationsAdultWithValidString() {
        Shelter actualShelter = shelterService.editRecommendationsAdult(1, "recommendationsAdult");
        assertEquals("recommendationsAdult", actualShelter.getRecommendationsAdult());
    }

    @Test
    void editRecommendationsAdultWithEmptyString() {
        Shelter actualShelter = shelterService.editRecommendationsAdult(1, "");
        assertNotEquals("", actualShelter.getRecommendationsAdult());
    }
    @Test
    void editRecommendationsAdultWithNull() {
        Shelter actualShelter = shelterService.editRecommendationsAdult(1, null);
        assertNotNull(actualShelter.getRecommendationsAdult());
    }

    @Test
    void editRecommendationsDisabledWithValidString() {
        Shelter actualShelter = shelterService.editRecommendationsDisabled(1, "recommendationsDisabled");
        assertEquals("recommendationsDisabled", actualShelter.getRecommendationsDisabled());
    }
    @Test
    void editRecommendationsDisabledWithEmptyString() {
        Shelter actualShelter = shelterService.editRecommendationsDisabled(1, "");
        assertNotEquals("", actualShelter.getRecommendationsDisabled());
    }

    @Test
    void editRecommendationsDisabledWithNull() {
        Shelter actualShelter = shelterService.editRecommendationsDisabled(1, null);
        assertNotNull(actualShelter.getRecommendationsDisabled());
    }
}