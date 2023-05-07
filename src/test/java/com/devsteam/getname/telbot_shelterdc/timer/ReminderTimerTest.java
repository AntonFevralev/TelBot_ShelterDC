package com.devsteam.getname.telbot_shelterdc.timer;

import com.devsteam.getname.telbot_shelterdc.config.TimeMachine;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import com.devsteam.getname.telbot_shelterdc.repository.ReportRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;

import java.time.*;

import static org.assertj.core.api.Assertions.*;
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

    @Autowired
    private ReportRepository reportRepository;

    // mock your tested class
    @SpyBean
    private ReminderTimer reminderTimer;

    @SpyBean
    TelegramBot telegramBot;

    private PetOwner catOwner;
    private PetOwner dogOwner;
    private Pet cat;

    private Pet dog;

    @BeforeEach
    public void setUp() {
        cat = new Pet();
        cat.setName("Muska");
        cat.setStatus(Status.FREE);
        cat.setDescription("Good");
        cat.setKind(Kind.CAT);
        cat.setBreed("siam");
        cat.setColor(Color.BLACK_AND_WHITE);
        petRepository.save(cat);
        catOwner = new PetOwner(306336303L, "FIO", "900","address", StatusOwner.PROBATION);
        cat.setPetOwner(catOwner);
        catOwner.setPet(cat);
        ownerRepository.save(catOwner);

        dog = new Pet();
        dog.setName("Muhtar");
        dog.setStatus(Status.FREE);
        dog.setDescription("Good");
        dog.setKind(Kind.DOG);
        dog.setBreed("ovcharka");
        dog.setColor(Color.BLACK_AND_WHITE);
        petRepository.save(dog);
        dogOwner = new PetOwner(306336303L, "FIO", "900","address", StatusOwner.PROBATION);
        dog.setPetOwner(dogOwner);
        dogOwner.setPet(dog);
        ownerRepository.save(dogOwner);
}
    @Test
    public void remindIfThereIsNoReportTodayAndOwnerStatusProbation(){
        dogOwner.setPet(null);
        dog.setPetOwner(null);
        ownerRepository.delete(dogOwner);
        petRepository.delete(dog);
        TimeMachine.useFixedClockAt(LocalTime.of(21, 15, 0));
        reminderTimer.remind();
        ArgumentCaptor <SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(catOwner.getChatId());
        assertThat(actual.getParameters().get("text")).isEqualTo("Отправьте, пожалуйста, отчет о животном!");
    }

    @Test
    public void NoRemindIfThereIsTodayReport(){
        Report report = new Report();
        report.setReportIsComplete(true);
        report.setReportIsInspected(true);
        report.setReportDate(LocalDate.now());
        report.setReportTime(LocalTime.now());
        report.setPhoto("photo");
        report.setMealsWellBeingAndAdaptationBehaviorChanges("meal");
        report.setPet(cat);
        report.setPetOwner(catOwner);
        reportRepository.save(report);

        TimeMachine.useFixedClockAt(LocalTime.of(21, 15, 0));
        reminderTimer.remind();
        ArgumentCaptor <SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(0)).execute(argumentCaptor.capture());
    }


    @Test
    public void remindIfTwoDaysDogOwnerDidntSendReport(){

        TimeMachine.useFixedClockAt(LocalTime.of(21, 20, 0));
        reminderTimer.remindIfTwoDaysDogOwnerDidntSendReport();
        ArgumentCaptor <SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(306336303L);
        assertThat(actual.getParameters().get("text")).isEqualTo("Владелец с id " + catOwner.getChatId() + " не отправлял отчет 2 дня");
    }

    @Test
    public void remindIfTwoDaysCatOwnerDidntSendReport(){
        TimeMachine.useFixedClockAt(LocalTime.of(21, 17, 0));
        reminderTimer.remindIfTwoDaysCatOwnerDidntSendReport();
        ArgumentCaptor <SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(306336303L);
        assertThat(actual.getParameters().get("text")).isEqualTo("Владелец с id " + catOwner.getChatId() + " не отправлял отчет 2 дня");
        }

    @Test
    public void NoVolunteerRemindIfTOneDaysDogOwnerDidntSendReport(){
        Report report = new Report();
        report.setReportIsComplete(true);
        report.setReportIsInspected(true);
        report.setReportDate(LocalDate.now().minusDays(1));
        report.setReportTime(LocalTime.now());
        report.setPhoto("photo");
        report.setMealsWellBeingAndAdaptationBehaviorChanges("meal");
        report.setPet(cat);
        report.setPetOwner(catOwner);
        reportRepository.save(report);
        TimeMachine.useFixedClockAt(LocalTime.of(21, 20, 0));
        reminderTimer.remind();
        ArgumentCaptor <SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(0)).execute(argumentCaptor.capture());
    }


}