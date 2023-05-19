package com.devsteam.getname.telbot_shelterdc.listener;


import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import com.devsteam.getname.telbot_shelterdc.service.ReportService;
import com.google.gson.Gson;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static java.nio.file.Files.readString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {


    @MockBean
    private TelegramBot telegramBot;
    @MockBean
    private ReportService reportService;

    @Autowired
    private TelegramBotUpdatesListener telegramBotUpdatesListener;

    /**
     * объект приюта собак, поля которого заполняются из БД для работы бота
     */
    private Shelter dogsShelter;
    /**
     * объект приюта кошек, поля которого заполняются из БД для работы бота
     */
    private Shelter catsShelter;

    @BeforeEach
    void setUp() throws IOException {
        try (InputStream in = Files.newInputStream(Path.of("src/main/resources/dogShelter.json").toAbsolutePath());
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            this.dogsShelter = new Gson().fromJson(reader, Shelter.class);
        }
        try (InputStream in = Files.newInputStream(Path.of("src/main/resources/catShelter.json").toAbsolutePath());
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            this.catsShelter = new Gson().fromJson(reader, Shelter.class);
        }
    }

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
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(
                "   Привет! Данный бот предоставляет информацию о двух приютах. Кошачий приют \"" + this.catsShelter.getTitle()+"\"" + " и собачий приют \"" + this.dogsShelter.getTitle() + "\". Выберите один");
        Assertions.assertThat(actual.getParameters().get("parse_mode"))
                .isEqualTo(ParseMode.Markdown.name());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Dogs? Вы выбрали приют Лучший друг",
            "Cats? Вы выбрали приют Пушистик",
            "HowToTakeDog? Здравствуйте! Здесь вы можете узнать о том, как взять собаку из нашего приюта",
            "HowToTakeCat? Здравствуйте! Здесь вы можете узнать о том, как взять кошку из нашего приюта",
            "InfoDogsShelter? Приют был основан в 1999 году, на данный момент в нем находится более 10 собак разных пород. За животными ухаживают волонтеры",
            "DogsShelterSecurity? +79999999999 Федор",
            "CatsShelterSecurity? +79999999998 Иван",
            "MainMenu? Выберите приют",
            "InfoDogs? Приют Лучший друг",
            "InfoCats? Приют Пушистик",
            "DogsShelterContact? Нажмите на кнопку оставить контакты для приюта",
            "CatsShelterContact? Нажмите на кнопку оставить контакты для приюта"
    }, delimiter = '?'
    )
    public void handleButtonParameterized(String callBackData, String menuMessage) throws URISyntaxException, IOException {
        handleButtonCallBackData(callBackData, menuMessage);
    }

    @Test
    public void handleButtonInfoDogsShelter() throws URISyntaxException, IOException {
        handleButtonCallBackData("InfoDogsShelter", dogsShelter.getInfo());
    }

    @Test
    public void handleButtonScheduleDogs() throws URISyntaxException, IOException {
        handleButtonCallBackData("ScheduleDogs", dogsShelter.getAddress() + "\n\n" + dogsShelter.getSchedule() + "\n\n " + "<a href=\"" + dogsShelter.getMapLink() + "\">Ссылка на Google Maps</a>");
    }
    @Test
    public void handleButtonScheduleCats() throws URISyntaxException, IOException {
        handleButtonCallBackData("ScheduleCats", catsShelter.getAddress() + "\n\n" + catsShelter.getSchedule() + "\n\n " + "<a href=\"" + catsShelter.getMapLink() + "\">Ссылка на Google Maps</a>");
    }

    @Test
    public void handleButtonSafetyRecommendationsDogsShelter() throws URISyntaxException, IOException {
        handleButtonCallBackData("SafetyRecommendationsDogsShelter", dogsShelter.getSafetyPrecautions());
    }
    @Test
    public void handleButtonSafetyRecommendationsCatsShelter() throws URISyntaxException, IOException {
        handleButtonCallBackData("SafetyRecommendationsCatsShelter", catsShelter.getSafetyPrecautions());
    }
    @Test
    public void handleButtonDogDocList() throws URISyntaxException, IOException {
        handleButtonCallBackData("DogDocList", dogsShelter.getDocList());
    }    @Test
    public void handleButtonDogMeetAndGreetRules() throws URISyntaxException, IOException {
        handleButtonCallBackData("DogMeetAndGreetRules", dogsShelter.getMeetAndGreatRules());
    }    @Test
    public void handleButtonDogTransportingRecommendations() throws URISyntaxException, IOException {
        handleButtonCallBackData("DogTransportingRecommendations", dogsShelter.getTransportingRules());
    }    @Test
    public void handleButtonDogRecommendations() throws URISyntaxException, IOException {
        handleButtonCallBackData("DogRecommendations", dogsShelter.getRecommendations());
    }    @Test
    public void handleButtonDogRecommendationsAdult() throws URISyntaxException, IOException {
        handleButtonCallBackData("DogRecommendationsAdult", dogsShelter.getRecommendationsAdult());
    }
    @Test
    public void handleButtonDogRecommendationsDisabled() throws URISyntaxException, IOException {
        handleButtonCallBackData("DogRecommendationsDisabled", dogsShelter.getRecommendationsDisabled());
    } @Test
    public void handleButtonCynologistAdvice() throws URISyntaxException, IOException {
        handleButtonCallBackData("cynologistAdvice", dogsShelter.getCynologistAdvice());
    } @Test
    public void handleButtonCynologists() throws URISyntaxException, IOException {
        handleButtonCallBackData("cynologists", dogsShelter.getRecommendedCynologists());
    }
    @Test
    public void handleButtonDogRejectionList() throws URISyntaxException, IOException {
        handleButtonCallBackData("DogRejectionList", dogsShelter.getRejectReasonsList());
    }
    @Test
    public void handleButtonCatMeetAndGreetRules() throws URISyntaxException, IOException {
        handleButtonCallBackData("CatMeetAndGreetRules", catsShelter.getMeetAndGreatRules());
    }

    @Test
    public void handleButtonCatDocList() throws URISyntaxException, IOException {
        handleButtonCallBackData("CatDocList", catsShelter.getDocList());
    }
    @Test
    public void handleButtonCatTransportingRecommendations() throws URISyntaxException, IOException {
        handleButtonCallBackData("CatTransportingRecommendations", catsShelter.getTransportingRules());
    }
    @Test
    public void handleButtonCatRecommendations() throws URISyntaxException, IOException {
        handleButtonCallBackData("CatRecommendations", catsShelter.getRecommendations());
    }

    @Test
    public void handleButtonCatRecommendationsAdult() throws URISyntaxException, IOException {
        handleButtonCallBackData("CatRecommendationsAdult", catsShelter.getRecommendationsAdult());
    }
    @Test
    public void handleButtonCatRecommendationsDisabled() throws URISyntaxException, IOException {
        handleButtonCallBackData("CatRecommendationsDisabled", catsShelter.getRecommendationsDisabled());
    }
    @Test
    public void handleButtonCatRejectionList() throws URISyntaxException, IOException {
        handleButtonCallBackData("CatRejectionList", catsShelter.getRejectReasonsList());
    }
    @Test
    public void handleButtonSendReport() throws URISyntaxException, IOException {
        String json = Files.readString(
                Paths.get(TelegramBotUpdatesListenerTest.class.getResource("callback_data_update.json").toURI()));
        Update update = getUpdate(json, "SendReport");
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(
                """
                Пожалуйста, заполните отчёт по следующим пунктам:
                1) Рацион животного.
                2) Общее самочувствие и привыкание к новому месту.
                3) Изменение в поведении: отказ от старых привычек, приобретение новых.
                Также, не забудьте прикрепить к сообщению фото животного.
                """);
    }



    private Update getUpdate(String json, String replaced) {
        return BotUtils.fromJson(json.replace("%command%", replaced), Update.class);
    }

    @Test
    public void handleValidReport() throws URISyntaxException, IOException {
        String json = Files.readString(
                Paths.get(TelegramBotUpdatesListenerTest.class.getResource("photo_update.json").toURI()));
        Update update = getUpdate(json, "test");
        when(reportService.processAttachment(any())).thenReturn("sss".getBytes());
        telegramBotUpdatesListener.waitingForReport.put(306336303L, true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> sendMessageArgumentCaptor = ArgumentCaptor.forClass(
                SendMessage.class);
        Mockito.verify(telegramBot).execute(sendMessageArgumentCaptor.capture());
        SendMessage actualSendMessage = sendMessageArgumentCaptor.getValue();

        ArgumentCaptor<String> descriptionArgumentCaptor = ArgumentCaptor.forClass(
                String.class);
        ArgumentCaptor<byte[]> photoArgumentCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(reportService).addReport(
                longArgumentCaptor.capture(),
                descriptionArgumentCaptor.capture(),
                photoArgumentCaptor.capture()
        );
        String actualDescription = descriptionArgumentCaptor.getValue();
        byte[] actualPhoto = photoArgumentCaptor.getValue();
        Long actualLong = longArgumentCaptor.getValue();

        Assertions.assertThat(actualDescription)
                .isEqualTo("test");
        Assertions.assertThat(actualPhoto).isNotEmpty();
        Assertions.assertThat(actualLong).isEqualTo(306336303L);
        Assertions.assertThat(actualSendMessage.getParameters().get("text")).isEqualTo(
                "добавляем отчёт");
    }
    @Test
    public void handleReportWithoutPhoto() throws URISyntaxException, IOException {
        String json = Files.readString(
                Paths.get(TelegramBotUpdatesListenerTest.class.getResource("text_update.json").toURI()));
        Update update = getUpdate(json, "test");
        telegramBotUpdatesListener.waitingForReport.put(123L, true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> sendMessageArgumentCaptor = ArgumentCaptor.forClass(
                SendMessage.class);
        Mockito.verify(telegramBot).execute(sendMessageArgumentCaptor.capture());
        SendMessage actualSendMessage = sendMessageArgumentCaptor.getValue();
        Assertions.assertThat(actualSendMessage.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actualSendMessage.getParameters().get("text")).isEqualTo(
                "Вы не приложили фото к отчету");
    }

    @Test
    public void handleReportWithoutText() throws URISyntaxException, IOException {
        String json = Files.readString(
                Paths.get(TelegramBotUpdatesListenerTest.class.getResource("empty_caption_update.json").toURI()));
        Update update = getUpdate(json, "");
        telegramBotUpdatesListener.waitingForReport.put(123L, true);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> sendMessageArgumentCaptor = ArgumentCaptor.forClass(
                SendMessage.class);
        Mockito.verify(telegramBot).execute(sendMessageArgumentCaptor.capture());
        SendMessage actualSendMessage = sendMessageArgumentCaptor.getValue();

        Assertions.assertThat(actualSendMessage.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actualSendMessage.getParameters().get("text")).isEqualTo(
                "Вы не приложили отчет");
    }

    public void handleButtonCallBackData(String callBackData, String menuMessage) throws URISyntaxException, IOException {
        String json = Files.readString(
                Paths.get(TelegramBotUpdatesListenerTest.class.getResource("callback_data_update.json").toURI()));
        Update update = getUpdate(json, callBackData);
        telegramBotUpdatesListener.process(Collections.singletonList(update));

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();

        Assertions.assertThat(actual.getParameters().get("chat_id")).isEqualTo(123L);
        Assertions.assertThat(actual.getParameters().get("text")).isEqualTo(
                menuMessage);
        Assertions.assertThat(actual.getParameters().get("reply_markup")).isNotNull();




    }
}