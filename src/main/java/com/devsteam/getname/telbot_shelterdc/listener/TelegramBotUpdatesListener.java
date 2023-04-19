package com.devsteam.getname.telbot_shelterdc.listener;

import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import com.devsteam.getname.telbot_shelterdc.repository.ShelterRepository;
import com.devsteam.getname.telbot_shelterdc.service.ShelterService;
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
import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    /**
     * объект приюта собак, поля которого заполняются из БД для работы бота
     */
    public static Shelter dogsShelter;
    /**
     * объект приюта кошек, поля которого заполняются из БД для работы бота
     */
    public static Shelter catsShelter;
    private final TelegramBot telegramBot;

    private final ShelterService service;


    public TelegramBotUpdatesListener(TelegramBot telegramBot, ShelterService service) {
        this.telegramBot = telegramBot;
        this.service = service;

    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
        this.dogsShelter = service.getByID(1);
        this.catsShelter = service.getByID(2);
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
            if (update.message() != null){
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
                if (message.contact() != null && message.replyToMessage().text().contains("Нажмите на кнопку оставить контакты для приюта собак")) {
                    sendContact(message, chatId);
                }
            }
        });
    } catch(
    Exception e)

    {
    }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
}

    /**
     * стартовое сообщение-приветствие и начальное меню
     *
     * @param chatId идентификатор чата
     */
    public void startMessage(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "   Привет! Данный бот предоставляет информацию о двух приютах. Кошачий приют \"" + TelegramBotUpdatesListener.catsShelter.getTitle() + "\"" +
                " и собачий приют \"" + TelegramBotUpdatesListener.dogsShelter.getTitle() + "\". Выберите один");
        sendMessage.parseMode(ParseMode.HTML);
        InlineKeyboardButton button1 = new InlineKeyboardButton("Кошки");
        button1.callbackData("Cats");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Собаки");
        button2.callbackData("Dogs");
        Keyboard keyboard = new InlineKeyboardMarkup(button1, button2);
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
        InlineKeyboardButton button1 = new InlineKeyboardButton("Кошки");
        button1.callbackData("Cats");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Собаки");
        button2.callbackData("Dogs");
        Keyboard keyboard = new InlineKeyboardMarkup(button1, button2);
        sendMessage.replyMarkup(keyboard);
        telegramBot.execute(sendMessage);
    }

    /**
     * главное меню приюта собак
     *
     * @param chatId идентификатор чата
     */
    public void dogsShelter(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Вы выбрали приют " + TelegramBotUpdatesListener.dogsShelter.getTitle());
        InlineKeyboardButton button1 = new InlineKeyboardButton("Основная информация");
        button1.callbackData("InfoDogs");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Как взять питомца?");
        button2.callbackData("HowToTakeDog");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Назад");
        button3.callbackData("BackFromDogsShelter");
        InlineKeyboardButton button4 = new InlineKeyboardButton("Главное меню");
        button4.callbackData("MainMenu");
        Keyboard keyboard = new InlineKeyboardMarkup().addRow(button1).addRow(button2).addRow(button3).addRow(button4).addRow(
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
        SendMessage sendMessage = new SendMessage(chatId, "Приют " + TelegramBotUpdatesListener.dogsShelter.getTitle());
        InlineKeyboardButton button1 = new InlineKeyboardButton("Информация о приюте");
        button1.callbackData("InfoDogsShelter");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Расписание, схема проезда, адрес");
        button2.callbackData("ScheduleDogs");
        InlineKeyboardButton button3 = new InlineKeyboardButton("Оформить пропуск");
        button3.callbackData("DogsShelterSecurity");
        InlineKeyboardButton button4 = new InlineKeyboardButton("Техника безопасности в приюте");
        button4.callbackData("SafetyRecommendationsDogsShelter");
        InlineKeyboardButton button5 = new InlineKeyboardButton("Оставить контакты");
        button5.callbackData("DogsShelterContact");
        InlineKeyboardButton button6 = new InlineKeyboardButton("Назад");
        button6.callbackData("BackFromDogsInfo");
        InlineKeyboardButton button7 = new InlineKeyboardButton("Главное меню");
        button7.callbackData("MainMenu");
        Keyboard keyboard = new InlineKeyboardMarkup().addRow(button1).addRow(button2).addRow(button3).addRow(button4).addRow(button5).addRow(button6).addRow(button7).addRow(
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
        KeyboardButton keyboardButton = new KeyboardButton("Оставить контакты").requestContact(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButton).oneTimeKeyboard(true);
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
     * @param message входящее сообщение
     * @param chatId идентификатор чата
     */
    public void sendContact(Message message, long chatId){
        Contact contact = message.contact();
        SendContact sendContact = new SendContact(dogsShelter.getChatId(), contact.phoneNumber(), contact.firstName());
        //отправляем контакт волонтеру приюта собак
        telegramBot.execute(sendContact);
    }
}

