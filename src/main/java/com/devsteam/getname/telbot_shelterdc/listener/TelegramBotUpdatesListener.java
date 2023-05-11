package com.devsteam.getname.telbot_shelterdc.listener;

import com.devsteam.getname.telbot_shelterdc.model.*;

import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.ReportRepository;

import com.devsteam.getname.telbot_shelterdc.service.ReportService;

import com.google.gson.Gson;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendContact;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.Files.readString;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    /**
     * объект приюта собак, поля которого заполняются из БД для работы бота
     */
    private final Shelter dogsShelter;
    /**
     * объект приюта кошек, поля которого заполняются из БД для работы бота
     */
    private final Shelter catsShelter;
    private final TelegramBot telegramBot;

    private final ReportService reportService;


    private final OwnerRepository ownerRepository;

    private final Map<Long, String> waitingForContact = new HashMap<>();

    public TelegramBotUpdatesListener(TelegramBot telegramBot, ReportService reportService, ReportRepository reportRepository, OwnerRepository ownerRepository) throws IOException {
        this.ownerRepository = ownerRepository;

        this.dogsShelter = new Gson().fromJson(readString(Path.of("src/main/resources/", "dogShelter.json")), Shelter.class);

        this.catsShelter = new Gson().fromJson(readString(Path.of("src/main/resources/", "catShelter.json")), Shelter.class);

        this.telegramBot = telegramBot;
        this.reportService = reportService;
    }

    /**
     * ининициализирует бота
     */
    @PostConstruct
    public void init() {

        telegramBot.setUpdatesListener(this);

    }

    /**
     * основной метод по работе с обновлениями чата
     *
     * @param updates входящие обновления бота
     * @return все обработанные обновления
     */
    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Handles update: {}", update);
                //если была нажата кнопка
                if (update.callbackQuery() != null) {
                    callBackQueryHandler(update);
                }//если сообщение не пустое
                if (update.message() != null) {
                    Message message = update.message();
                    String text = message.text();
                    long chatId = message.chat().id();
                    //если тест сообщения старт
                    if ("/start".equals(text)) {
                        startMessage(chatId);
                        //если к сообщению прикреплен контакт и сообщение является ответом на сообщение, содержащее определенный текст
                    } else if ("/id".equals(text)) {
                        sendChatId(chatId);
                    }

                    else if (message.caption() != null&&message.caption().startsWith("/report")&& (message.photo() != null||message.document().mimeType().equals("image/jpeg"))) {
                        byte[] photoAsByteArray = reportService.processPhoto(message);
                        if (photoAsByteArray == null){
                            throw new RuntimeException("no photo from tg downloaded");
                        }
                        reportService.addReport(chatId, message.caption().substring(7), "photo", photoAsByteArray);
                        telegramBot.execute(new SendMessage(chatId, "добавляем отчёт"));
                    }
                    if (message.contact() != null&&waitingForContact.get(chatId).equals("Dog")) {
                        sendContact(message, chatId, dogsShelter);
                    } else if (message.contact() != null&&waitingForContact.get(chatId).equals("Cat")){
                        sendContact(message, chatId, catsShelter);
                    }
                }
            });
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * стартовое сообщение-приветствие и начальное меню
     *
     * @param chatId идентификатор чата
     */
    public void startMessage(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "   Привет! Данный бот предоставляет информацию о двух приютах. Кошачий приют \"" +
                this.catsShelter.getTitle()+"\"" + " и собачий приют \"" + this.dogsShelter.getTitle() + "\". Выберите один");
        sendMessage.parseMode(ParseMode.Markdown);
        InlineKeyboardButton cats = new InlineKeyboardButton("Кошки");
        cats.callbackData("Cats");
        InlineKeyboardButton dogs = new InlineKeyboardButton("Собаки");
        dogs.callbackData("Dogs");
        Keyboard keyboard = new InlineKeyboardMarkup(cats, dogs);
        sendMessage.replyMarkup(keyboard);
        telegramBot.execute(sendMessage);
    }
    /**
     * Ветвление кода по кнопкам, работает с callbackQuery.data()
     *
     * @param update
     */
    public void callBackQueryHandler(Update update) {
        Long chatId = update.callbackQuery().message().chat().id();
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        switch (data) {
            case "MainMenu" -> startMessageWithoutGreeting(chatId);
            case "Dogs" -> shelter(chatId, "InfoDogs", "HowToTakeDog", "MainMenu", dogsShelter);
            case "Cats" -> shelter(chatId, "InfoCats","HowToTakeCat",  "MainMenu", catsShelter);
            case "InfoDogs" -> shelterInfo(chatId, "InfoDogsShelter", "ScheduleDogs",  "DogsShelterSecurity",
                    "SafetyRecommendationsDogsShelter","DogsShelterContact", "Dogs", dogsShelter);
            case "InfoCats" -> shelterInfo(chatId, "InfoCatsShelter", "ScheduleCats",  "CatsShelterSecurity",
                    "SafetyRecommendationsCatsShelter","CatsShelterContact", "Cats", catsShelter);
            /* case "HowToTakeDog"-> howToTakeDog(chatId);*/
            case "InfoDogsShelter" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId, dogsShelter.getInfo(), "InfoDogs");
            case "InfoCatsShelter" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId, catsShelter.getInfo(), "InfoCats");
            case "ScheduleDogs" ->
                    sendMessageWithMainMenuButtonFromInfoMenu(chatId, dogsShelter.getAddress() + "\n\n" + dogsShelter.getSchedule() + "\n\n " + "<a href=\"" + dogsShelter.getMapLink() + "\">Ссылка на Google Maps</a>", "InfoDogs");
            case "ScheduleCats" ->
                    sendMessageWithMainMenuButtonFromInfoMenu(chatId, catsShelter.getAddress() + "\n\n" + catsShelter.getSchedule() + "\n\n " + "<a href=\"" + catsShelter.getMapLink() + "\">Ссылка на Google Maps</a>", "InfoDogs");
            case "DogsShelterSecurity" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId, dogsShelter.getSecurity(), "InfoDogs");
            case "CatsShelterSecurity" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId, catsShelter.getSecurity(), "InfoCats");
            case "SafetyRecommendationsDogsShelter" ->
                    sendMessageWithMainMenuButtonFromInfoMenu(chatId, dogsShelter.getSafetyPrecautions(), "InfoDogs");
            case "SafetyRecommendationsCatsShelter" ->
            sendMessageWithMainMenuButtonFromInfoMenu(chatId, catsShelter.getSafetyPrecautions(), "InfoCats");
            case "DogsShelterContact" ->{ shelterContact(chatId);
                                            waitingForContact.put(chatId, "Dog");}
            case "CatsShelterContact" -> {shelterContact(chatId);
            waitingForContact.put(chatId, "Cat");}
            case "SendReport" -> initiateReportDialog(chatId);
            case "HowToTakeDog"->howToTakeDog(chatId);
            case "HowToTakeCat"->howToTakeCat(chatId);
            case "DogMeetAndGreetRules" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    dogsShelter.getMeetAndGreatRules(), "HowToTakeDog");
            case "DogDocList" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    dogsShelter.getDocList(), "HowToTakeDog");
            case "DogTransportingRecommendations" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    dogsShelter.getTransportingRules(), "HowToTakeDog");
            case "DogRecommendations" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    dogsShelter.getRecommendations(), "HowToTakeDog");
            case "DogRecommendationsAdult" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    dogsShelter.getRecommendationsAdult(), "HowToTakeDog");
            case "DogRecommendationsDisabled" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    dogsShelter.getRecommendationsDisabled(), "HowToTakeDog");
            case "cynologistAdvice" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    dogsShelter.getCynologistAdvice(), "HowToTakeDog");
            case "cynologists" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    dogsShelter.getRecommendedCynologists(), "HowToTakeDog");
            case "DogRejectionList" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    dogsShelter.getRejectReasonsList(), "HowToTakeDog");
            case "CatMeetAndGreetRules" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    catsShelter.getMeetAndGreatRules(), "HowToTakeCat");
            case "CatRecommendations" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    catsShelter.getRecommendations(), "HowToTakeCat");
            case "CatDocList" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    catsShelter.getDocList(), "HowToTakeCat");
            case "CatTransportingRecommendations" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    catsShelter.getTransportingRules(), "HowToTakeCat");
            case "CatRecommendationsAdult" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    catsShelter.getRecommendationsAdult(), "HowToTakeCat");
            case "CatRecommendationsDisabled" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    catsShelter.getRecommendationsDisabled(), "HowToTakeCat");
            case "CatRejectionList" -> sendMessageWithMainMenuButtonFromInfoMenu(chatId,
                    catsShelter.getRejectReasonsList(), "HowToTakeCat");

        }

    }

    private void howToTakeDog(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Здравствуйте! Здесь вы можете узнать о том, как взять " +
                "кошку из нашего приюта"
    );
        InlineKeyboardButton meetAndGreet = new InlineKeyboardButton("Правила знакомства с животным");
        meetAndGreet.callbackData("DogMeetAndGreetRules");
        InlineKeyboardButton docList = new InlineKeyboardButton("Список документов для усыновления");
        docList.callbackData("DogDocList");
        InlineKeyboardButton transportingRecommendations = new InlineKeyboardButton("Рекомендации по транспортировке");
        transportingRecommendations.callbackData("DogTransportingRecommendations");
        InlineKeyboardButton recommendations = new InlineKeyboardButton("Обустройство дома для щенка");
        recommendations.callbackData("DogRecommendations");
        InlineKeyboardButton recommendationsAdult = new InlineKeyboardButton("Обустройство дома для взрослой собаки");
        recommendationsAdult.callbackData("DogRecommendationsAdult");
        InlineKeyboardButton recommendationsDisabled = new InlineKeyboardButton("Обустройство дома для собаки-инвалида");
        recommendationsDisabled.callbackData("DogRecommendationsDisabled");
        InlineKeyboardButton cynologistAdvice = new InlineKeyboardButton("Первичные рекомендации кинолога");
        cynologistAdvice.callbackData("cynologistAdvice");
        InlineKeyboardButton recommendedCynologist = new InlineKeyboardButton("Рекомендуем кинологов");
        recommendedCynologist.callbackData("cynologists");
        InlineKeyboardButton rejectionList = new InlineKeyboardButton("Причины отказа в усыновлении");
        rejectionList.callbackData("DogRejectionList");
        InlineKeyboardButton sendContacts = new InlineKeyboardButton("Оставить контакты");
        sendContacts.callbackData("DogsShelterContact");
        InlineKeyboardButton back = new InlineKeyboardButton("Назад");
        back.callbackData("Dogs");
        InlineKeyboardButton mainMenu = new InlineKeyboardButton("Главное меню");
        mainMenu.callbackData("MainMenu");
        Keyboard keyboard = new InlineKeyboardMarkup().addRow(meetAndGreet).addRow(docList).addRow(transportingRecommendations)
                .addRow(recommendations).addRow(recommendationsAdult).addRow(recommendationsDisabled).addRow(cynologistAdvice)
                .addRow(recommendedCynologist).addRow(rejectionList).addRow(sendContacts).addRow(back).addRow(mainMenu)
                .addRow( new InlineKeyboardButton("Позвать волонтера").url("https://t.me/fevralevanton"));
        sendMessage.replyMarkup(keyboard);
        telegramBot.execute(sendMessage);
    }
    private void howToTakeCat(Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Здравствуйте! Здесь вы можете узнать о том, как взять " +
                "собаку из нашего приюта"
        );
        InlineKeyboardButton meetAndGreet = new InlineKeyboardButton("Правила знакомства с животным");
        meetAndGreet.callbackData("CatMeetAndGreetRules");
        InlineKeyboardButton docList = new InlineKeyboardButton("Список документов для усыновления");
        docList.callbackData("CatDocList");
        InlineKeyboardButton transportingRecommendations = new InlineKeyboardButton("Рекомендации по транспортировке");
        transportingRecommendations.callbackData("CatTransportingRecommendations");
        InlineKeyboardButton recommendations = new InlineKeyboardButton("Обустройство дома для котенка");
        recommendations.callbackData("CatRecommendations");
        InlineKeyboardButton recommendationsAdult = new InlineKeyboardButton("Обустройство дома для взрослой кошки");
        recommendationsAdult.callbackData("CatRecommendationsAdult");
        InlineKeyboardButton recommendationsDisabled = new InlineKeyboardButton("Обустройство дома для кошки-инвалида");
        recommendationsDisabled.callbackData("CatRecommendationsDisabled");
        InlineKeyboardButton rejectionList = new InlineKeyboardButton("Причины отказа в усыновлении");
        rejectionList.callbackData("CatRejectionList");
        InlineKeyboardButton sendContacts = new InlineKeyboardButton("Оставить контакты");
        sendContacts.callbackData("CatsShelterContact");
        InlineKeyboardButton back = new InlineKeyboardButton("Назад");
        back.callbackData("Cats");
        InlineKeyboardButton mainMenu = new InlineKeyboardButton("Главное меню");
        mainMenu.callbackData("MainMenu");
        Keyboard keyboard = new InlineKeyboardMarkup().addRow(meetAndGreet).addRow(docList).addRow(transportingRecommendations)
                .addRow(recommendations).addRow(recommendationsAdult).addRow(recommendationsDisabled).
                addRow(rejectionList).addRow(sendContacts).addRow(back).addRow(mainMenu)
                .addRow( new InlineKeyboardButton("Позвать волонтера").url("https://t.me/fevralevanton"));
        sendMessage.replyMarkup(keyboard);
        telegramBot.execute(sendMessage);
    }

    /**
     * начальное меню без приветствия
     *
     * @param chatId идентификатор чата
     */
    public void startMessageWithoutGreeting(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Выберите приют");
        InlineKeyboardButton cats = new InlineKeyboardButton("Кошки");
        cats.callbackData("Cats");
        InlineKeyboardButton dogs = new InlineKeyboardButton("Собаки");
        dogs.callbackData("Dogs");
        Keyboard keyboard = new InlineKeyboardMarkup(cats, dogs);
        sendMessage.replyMarkup(keyboard);
        telegramBot.execute(sendMessage);
    }

    /**
     * главное меню приюта собак
     *
     * @param chatId идентификатор чата
     */
    public void shelter(long chatId, String mainInfoData, String howToTakeData, String backData, Shelter shelter) {
        SendMessage sendMessage = new SendMessage(chatId, "Вы выбрали приют " + shelter.getTitle());
        InlineKeyboardButton mainInfo = new InlineKeyboardButton("Основная информация");
        mainInfo.callbackData(mainInfoData);
        InlineKeyboardButton howToTake = new InlineKeyboardButton("Как взять питомца?");
        howToTake.callbackData(howToTakeData);
        InlineKeyboardButton back = new InlineKeyboardButton("Назад");
        back.callbackData(backData);
        InlineKeyboardButton sendReport = new InlineKeyboardButton("Отправить отчет о питомце");
        sendReport.callbackData("SendReport");
        InlineKeyboardButton mainMenu = new InlineKeyboardButton("Главное меню");
        mainMenu.callbackData("MainMenu");
        Keyboard keyboard = new InlineKeyboardMarkup().addRow(mainInfo).addRow(howToTake).addRow(back).addRow(mainMenu).addRow(
                new InlineKeyboardButton("Позвать волонтера").url("https://t.me/fevralevanton"));
        sendMessage.replyMarkup(keyboard);
        telegramBot.execute(sendMessage);
    }
    /**
     * меню Основная информация приюта собак
     *
     * @param chatId идентификатор чата
     */
    public void shelterInfo(long chatId, String infoShelterData, String scheduleData,
                            String securityData, String safetyRecommendationsData, String contactData, String backData, Shelter shelter) {
        SendMessage sendMessage = new SendMessage(chatId, "Приют " + shelter.getTitle());
        InlineKeyboardButton infoShelter = new InlineKeyboardButton("Информация" +
                " о приюте");
        infoShelter.callbackData(infoShelterData);
        InlineKeyboardButton scheduleShelter = new InlineKeyboardButton("Расписание, схема проезда, адрес");
        scheduleShelter.callbackData(scheduleData);
        InlineKeyboardButton shelterSecurity = new InlineKeyboardButton("Оформить пропуск");
        shelterSecurity.callbackData(securityData);
        InlineKeyboardButton safetyRecommendations = new InlineKeyboardButton("Техника безопасности в приюте");
        safetyRecommendations.callbackData(safetyRecommendationsData);
        InlineKeyboardButton shelterContact = new InlineKeyboardButton("Оставить контакты");
        shelterContact.callbackData(contactData);
        InlineKeyboardButton back = new InlineKeyboardButton("Назад");
        back.callbackData(backData);
        InlineKeyboardButton mainMenu = new InlineKeyboardButton("Главное меню");
        mainMenu.callbackData("MainMenu");
        Keyboard keyboard = new InlineKeyboardMarkup().addRow(infoShelter).addRow(scheduleShelter)
                .addRow(safetyRecommendations).addRow(shelterContact).addRow(back).addRow(mainMenu).addRow(
                        new InlineKeyboardButton("Позвать волонтера").url("https://t.me/fevralevanton"));
        sendMessage.replyMarkup(keyboard);
        telegramBot.execute(sendMessage);
    }



    /**
     * отправка сообщения с кнопнкой главное меню и кнопкой назад из меню Основная информация приюта собак
     *
     * @param chatId  идентификатор чата
     * @param message сообщение
     */
    public void sendMessageWithMainMenuButtonFromInfoMenu(long chatId, String message, String backData) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.parseMode(ParseMode.HTML);
        InlineKeyboardButton mainMenu = new InlineKeyboardButton("Главное меню");
        mainMenu.callbackData("MainMenu");
        InlineKeyboardButton back = new InlineKeyboardButton("Назад");
        back.callbackData(backData);
        Keyboard keyboard = new InlineKeyboardMarkup().addRow(mainMenu).addRow(back);
        sendMessage.replyMarkup(keyboard);
        telegramBot.execute(sendMessage);
    }

    /**
     * сообщение с запросом контактов для приюта собак
     *
     * @param chatId идентификатор чата
     */
    public void shelterContact(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Нажмите на кнопку оставить контакты для приюта");
        KeyboardButton sendContacts = new KeyboardButton("Оставить контакты").requestContact(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(sendContacts).oneTimeKeyboard(true);
        sendMessage.replyMarkup(replyKeyboardMarkup);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
    }
    /**
     * отправляет в чат идентификатор чата
     *
     * @param chatId идентификатор чата
     */
    public void sendChatId(long chatId) {
        SendMessage message = new SendMessage(chatId, "Ваш идентификатор чата " + chatId);
        telegramBot.execute(message);
    }



    /**
     * Отправляет контакты пользователя волонтеру
     *
     * @param message входящее сообщение
     * @param chatId  идентификатор чата
     */
    public void sendContact(Message message, long chatId, Shelter shelter) {
        Contact contact = message.contact();
        SendContact sendContact = new SendContact(shelter.getChatId(), contact.phoneNumber(), contact.firstName());
        SendMessage sendMessage = new SendMessage(shelter.getChatId(), "Идентификатор чата клиента " + chatId);
        //отправляем контакт волонтеру приюта собак
        telegramBot.execute(sendContact);
        telegramBot.execute(sendMessage);
    }

    /**
     * Отправляет пользователю сообщение о формате отчёта
     *
     * @param chatId идентификатор чата
     */
    public void initiateReportDialog(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, """
                Пожалуйста, заполните отчёт по следующим пунктам:
                1) id животного.
                2) Рацион животного.
                3) Общее самочувствие и привыкание к новому месту.
                4) Изменение в поведении: отказ от старых привычек, приобретение новых.
                Также, не забудьте прикрепить к сообщению фото животного.
                """);
        telegramBot.execute(sendMessage);
    }




}

