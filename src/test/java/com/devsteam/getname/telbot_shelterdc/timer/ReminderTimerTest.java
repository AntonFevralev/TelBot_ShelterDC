package com.devsteam.getname.telbot_shelterdc.timer;

import com.devsteam.getname.telbot_shelterdc.config.TimeMachine;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.mockito.internal.util.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReminderTimerTest {
    // Some fixed date to make your tests

    @Autowired
    private  PetRepository petRepository;
    @Autowired
    private OwnerRepository ownerRepository;


    // mock your tested class
    @SpyBean
    private ReminderTimer reminderTimer;

    @SpyBean
    TelegramBot telegramBot;

    private PetOwner owner;
    private Pet pet;

    @BeforeEach
    public void setUp() {
        pet = new Pet();
        pet.setName("Muska");
        pet.setStatus(Status.FREE);
        pet.setDescription("Good");
        pet.setKind(Kind.CAT);
        pet.setBreed("siam");
        pet.setColor(Color.BLACK_AND_WHITE);
        petRepository.save(pet);
        owner = new PetOwner(306336303L, "FIO", "900","address", StatusOwner.PROBATION);
        pet.setPetOwner(owner);
        owner.setPet(pet);
        ownerRepository.save(owner);
}
    @Test
    public void remindIfThereIsNoReportTodayAndOwnerStatusProbation(){
        TimeMachine.useFixedClockAt(LocalTime.of(21, 15, 0));
        reminderTimer.remind();
        ArgumentCaptor <SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(owner.getChatId());
        assertThat(actual.getParameters().get("text")).isEqualTo("Отправьте, пожалуйста, отчет о животном!");
    }

    @Test
    public void remindIfTwoDaysDogOwnerDidntSendReport(){
        Pet pet1 = new Pet();
        pet1.setName("Muhtar");
        pet1.setStatus(Status.FREE);
        pet1.setDescription("Good");
        pet1.setKind(Kind.DOG);
        pet1.setBreed("ovcharka");
        pet1.setColor(Color.BLACK_AND_WHITE);
        petRepository.save(pet1);
        PetOwner owner1 = new PetOwner(306336303L, "FIO", "900","address", StatusOwner.PROBATION);
        pet1.setPetOwner(owner1);
        owner1.setPet(pet1);
        ownerRepository.save(owner1);
        TimeMachine.useFixedClockAt(LocalTime.of(21, 20, 0));
        reminderTimer.remindIfTwoDaysDogOwnerDidntSendReport();
        ArgumentCaptor <SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(306336303L);
        assertThat(actual.getParameters().get("text")).isEqualTo("Владелец с id " + owner.getChatId() + " не отправлял отчет 2 дня");
    }

    @Test
    public void remindIfTwoDaysCatOwnerDidntSendReport(){
        TimeMachine.useFixedClockAt(LocalTime.of(21, 17, 0));
        reminderTimer.remindIfTwoDaysCatOwnerDidntSendReport();
        ArgumentCaptor <SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(306336303L);
        assertThat(actual.getParameters().get("text")).isEqualTo("Владелец с id " + owner.getChatId() + " не отправлял отчет 2 дня");
        }
}