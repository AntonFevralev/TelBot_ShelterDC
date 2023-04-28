package com.devsteam.getname.telbot_shelterdc.listener;

import com.devsteam.getname.telbot_shelterdc.listener.TelegramBotUpdatesListener;
import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import com.devsteam.getname.telbot_shelterdc.repository.CatOwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.CatReportRepository;
import com.devsteam.getname.telbot_shelterdc.repository.CatRepository;
import com.devsteam.getname.telbot_shelterdc.repository.ShelterRepository;
import com.devsteam.getname.telbot_shelterdc.service.CatReportService;
import com.devsteam.getname.telbot_shelterdc.service.ShelterService;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;


@ExtendWith(MockitoExtension.class)
@Sql(value = {"/schema.sql", "/data.sql"})
public class TelegramBotUpdatesListenerTest {
    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;
    @Mock
    private ShelterService service;

    @Test
    public void handleStartTest() throws URISyntaxException, IOException {
        String json = Files.readString(
                Paths.get(TelegramBotUpdatesListenerTest.class.getResource("text_update.json").toURI()));
        Update update = getUpdate(json, "/start");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
      /*  Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(
                "   Привет! Данный бот предоставляет информацию о двух приютах. Кошачий приют title и собачий приют title. Выберите один");*/
        /*Assertions.assertThat(actual.getParameters().get("parse_mode"))
                .isEqualTo(ParseMode.Markdown.name());*/
    }


    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }
}