package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDate;

import static com.devsteam.getname.telbot_shelterdc.model.Kind.CAT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NotificationTest {

    @Autowired
    PetOwnerService petOwnerService;

    @Autowired
    PetRepository petRepository;

    @Autowired
    OwnerRepository ownerRepository;


    @SpyBean
    TelegramBot telegramBot;

    private PetOwner catOwner;
    private Pet cat;

    @BeforeEach
    public void setUp() {
        cat = new Pet();
        cat.setName("Muska");
        cat.setStatus(Status.FREE);
        cat.setDescription("Good");
        cat.setKind(CAT);
        cat.setBreed("siam");
        cat.setColor(Color.BLACK_AND_WHITE);
        petRepository.save(cat);
        catOwner = new PetOwner(306336303L, "FIO", "900","address", StatusOwner.PROBATION, LocalDate.now(), cat);
        cat.setPetOwner(catOwner);
        ownerRepository.save(catOwner);
    }

    @Test
    public void ifPetOwnerStatusChangesToSEARCHbyIdCONotifyPO(){
        petOwnerService.changeStatusOwnerByIdCO(catOwner.getIdCO(), StatusOwner.SEARCH);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(catOwner.getChatId());
        assertThat(actual.getParameters().get("text")).isEqualTo("Постараемся подобрать Вам питомца");
    }

    @Test
    public void ifPetOwnerStatusChangesToPROBATIONbyIdCONotifyPO(){
        petOwnerService.changeStatusOwnerByIdCO(catOwner.getIdCO(), StatusOwner.PROBATION);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(catOwner.getChatId());
        assertThat(actual.getParameters().get("text")).isEqualTo("Вам будет выдан питомец и назначен " +
                "испытательный срок до " + catOwner.getFinishProba() + " В течение испытательного срока Вы должны" +
                " присылать ежедневный отчет о состоянии питомца");
    }

    @Test
    public void ifPetOwnerStatusChangesToOWNERbyIdCONotifyPO(){
        petOwnerService.changeStatusOwnerByIdCO(catOwner.getIdCO(), StatusOwner.OWNER);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(catOwner.getChatId());
        assertThat(actual.getParameters().get("text")).isEqualTo("Вы прошли испытательный срок, поздравляем!");
    }

    @Test
    public void ifPetOwnerStatusChangesToREJECTIONbyIdCONotifyPO(){
        petOwnerService.changeStatusOwnerByIdCO(catOwner.getIdCO(), StatusOwner.REJECTION);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(catOwner.getChatId());
        assertThat(actual.getParameters().get("text")).isEqualTo("Вам отказано в усыновлении, свяжитесь с волонтером");
    }


    @Test
    public void ifPetOwnerStatusChangesToBLACKLISTEDbyIdCONotifyPO(){
        petOwnerService.changeStatusOwnerByIdCO(catOwner.getIdCO(), StatusOwner.BLACKLISTED);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(catOwner.getChatId());
        assertThat(actual.getParameters().get("text")).isEqualTo("Вы внесены в черный список нашего приюта");
    }

    @Test
    public void RemindIfFinishProbaDateChanged(){
         petOwnerService.updateFinishProba(catOwner.getIdCO(), 10);
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getParameters().get("chat_id")).isEqualTo(catOwner.getChatId());
        assertThat(actual.getParameters().get("text")).isEqualTo("Вам продлен испытательный срок до "
                + catOwner.getFinishProba().plusDays(10));
    }
}
