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
import java.util.List;

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

                    else if (message.caption().contains("/report") && message.caption() != null && (message.photo() != null||message.document().mimeType().equals("image/jpeg"))) {
                        reportService.addReport(chatId, message.caption().substring(7), "photo");
                        telegramBot.execute(new SendMessage(chatId, "добавляем отчёт"));
                        //initiateReportDialog(chatId);
                    }
                    if (message.contact() != null) {
                        sendContact(message, chatId);
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
                this.catsShelter.getTitle() + " и собачий приют \"" + this.dogsShelter.getTitle() + "\". Выберите один");
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
    public void dogsShelter(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Вы выбрали приют " + this.dogsShelter.getTitle());
        InlineKeyboardButton mainInfoDogs = new InlineKeyboardButton("Основная информация");
        mainInfoDogs.callbackData("InfoDogs");
        InlineKeyboardButton howToTakeDog = new InlineKeyboardButton("Как взять питомца?");
        howToTakeDog.callbackData("HowToTakeDog");
        InlineKeyboardButton back = new InlineKeyboardButton("Назад");
        back.callbackData("BackFromDogsShelter");
        InlineKeyboardButton mainMenu = new InlineKeyboardButton("Главное меню");
        mainMenu.callbackData("MainMenu");
        Keyboard keyboard = new InlineKeyboardMarkup().addRow(mainInfoDogs).addRow(howToTakeDog).addRow(back).addRow(mainMenu).addRow(
                new InlineKeyboardButton("Позвать волонтера").url("https://t.me/fevralevanton"));
        sendMessage.replyMarkup(keyboard);
        telegramBot.execute(sendMessage);
    }

    /**
     * меню Основная информация приюта собак
     *
     * @param chatId идентификатор чата
     */
    public void infoDogs(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Приют " + this.dogsShelter.getTitle());
        InlineKeyboardButton infoDogsShelter = new InlineKeyboardButton("Информация о приюте");
        infoDogsShelter.callbackData("InfoDogsShelter");
        InlineKeyboardButton scheduleDogsShelter = new InlineKeyboardButton("Расписание, схема проезда, адрес");
        scheduleDogsShelter.callbackData("ScheduleDogs");
        InlineKeyboardButton dogsShelterSecurity = new InlineKeyboardButton("Оформить пропуск");
        dogsShelterSecurity.callbackData("DogsShelterSecurity");
        InlineKeyboardButton safetyRecommendationsDogs = new InlineKeyboardButton("Техника безопасности в приюте");
        safetyRecommendationsDogs.callbackData("SafetyRecommendationsDogsShelter");
        InlineKeyboardButton dogsShelterContact = new InlineKeyboardButton("Оставить контакты");
        dogsShelterContact.callbackData("DogsShelterContact");
        InlineKeyboardButton back = new InlineKeyboardButton("Назад");
        back.callbackData("BackFromDogsInfo");
        InlineKeyboardButton mainMenu = new InlineKeyboardButton("Главное меню");
        mainMenu.callbackData("MainMenu");
        Keyboard keyboard = new InlineKeyboardMarkup().addRow(infoDogsShelter).addRow(scheduleDogsShelter)
                .addRow(safetyRecommendationsDogs).addRow(dogsShelterContact).addRow(back).addRow(mainMenu).addRow(
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
    public void sendMessageWithMainMenuButtonFromDogsInfo(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.parseMode(ParseMode.HTML);
        InlineKeyboardButton mainMenu = new InlineKeyboardButton("Главное меню");
        mainMenu.callbackData("MainMenu");
        InlineKeyboardButton back = new InlineKeyboardButton("Назад");
        back.callbackData("InfoDogs");
        Keyboard keyboard = new InlineKeyboardMarkup().addRow(mainMenu).addRow(back);
        sendMessage.replyMarkup(keyboard);
        telegramBot.execute(sendMessage);
    }

    /**
     * сообщение с запросом контактов для приюта собак
     *
     * @param chatId идентификатор чата
     */
    public void dogsShelterContact(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Нажмите на кнопку оставить контакты для приюта собак");
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
     * Ветвление кода по кнопкам, работает с callbackQuery.data()
     *
     * @param update
     */
    public void callBackQueryHandler(Update update) {
        Long chatId = update.callbackQuery().message().chat().id();
        CallbackQuery callbackQuery = update.callbackQuery();
        String data = callbackQuery.data();
        switch (data) {
            case "MainMenu", "BackFromDogsShelter" -> startMessageWithoutGreeting(chatId);
            case "Dogs", "BackFromDogsInfo" -> dogsShelter(chatId);
            case "InfoDogs" -> infoDogs(chatId);
            /* case "HowToTakeDog"-> howToTakeDog(chatId);*/
            case "InfoDogsShelter" -> sendMessageWithMainMenuButtonFromDogsInfo(chatId, dogsShelter.getInfo());
            case "ScheduleDogs" ->
                    sendMessageWithMainMenuButtonFromDogsInfo(chatId, dogsShelter.getAddress() + "\n\n" + dogsShelter.getSchedule() + "\n\n " + "<a href=\"" + dogsShelter.getMapLink() + "\">Ссылка на Google Maps</a>");
            case "DogsShelterSecurity" -> sendMessageWithMainMenuButtonFromDogsInfo(chatId, dogsShelter.getSecurity());
            case "SafetyRecommendationsDogsShelter" ->
                    sendMessageWithMainMenuButtonFromDogsInfo(chatId, dogsShelter.getSafetyPrecautions());
            case "DogsShelterContact" -> dogsShelterContact(chatId);
        }

    }

    /**
     * Отправляет контакты пользователя волонтеру
     *
     * @param message входящее сообщение
     * @param chatId  идентификатор чата
     */
    public void sendContact(Message message, long chatId) {
        Contact contact = message.contact();
        SendContact sendContact = new SendContact(dogsShelter.getChatId(), contact.phoneNumber(), contact.firstName());
        SendMessage sendMessage = new SendMessage(dogsShelter.getChatId(), "Идентификатор чата клиента " + chatId);
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

