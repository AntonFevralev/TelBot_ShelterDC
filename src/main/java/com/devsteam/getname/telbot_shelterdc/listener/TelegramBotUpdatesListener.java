package com.devsteam.getname.telbot_shelterdc.listener;

import com.devsteam.getname.telbot_shelterdc.model.Shelter;
import com.devsteam.getname.telbot_shelterdc.service.ShelterService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendContact;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    public static Shelter dogsShelter;
    public static Shelter catsShelter;
    private final TelegramBot telegramBot;


    private final ShelterService service;


    public TelegramBotUpdatesListener(TelegramBot telegramBot, ShelterService service ) {
        this.telegramBot = telegramBot;
        this.service = service;
        this.dogsShelter = service.getByID(1);
        this.catsShelter = service.getByID(2);
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);

    }
    //основное метод по работе с обновлениями чата
    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Handles update: {}", update);
                if (update.callbackQuery() != null) {
                    Long chatId = update.callbackQuery().message().chat().id();
                    CallbackQuery callbackQuery = update.callbackQuery();
                    String data = callbackQuery.data();
                    switch (data) {
                        case "MainMenu", "BackFromDogsShelter" -> startMessageWithoutGreeting(chatId);
                        case "Dogs", "BackFromDogsInfo" -> dogsShelter(chatId);
                        case "InfoDogs" -> infoDogs(chatId);
                       /* case "HowToTakeDog"-> howToTakeDog(chatId);*/
                        case "InfoDogsShelter"-> sendMessageWithMainMenuButtonFromDogsInfo(chatId, dogsShelter.getInfo());
                        case "ScheduleDogs"-> sendMessageWithMainMenuButtonFromDogsInfo(chatId, dogsShelter.getAddress() +"\n\n"+dogsShelter.getSchedule() +"\n\n "+"<a href=\""+dogsShelter.getMapLink()+"\">Ссылка на Google Maps</a>");
                        case "DogsShelterSecurity" -> sendMessageWithMainMenuButtonFromDogsInfo(chatId, dogsShelter.getSecurity());
                        case "SafetyRecommendationsDogsShelter"->sendMessageWithMainMenuButtonFromDogsInfo(chatId, dogsShelter.getSafetyPrecautions());
                        case "DogsShelterContact"-> dogsShelterContact(chatId);
                    }
                }
                if (update.message() != null) {
                    Message message = update.message();
                    String text = message.text();
                    long chatId = message.chat().id();
                    if ("/start".equals(text)) {
                        startMessage(chatId);
                    }if(message.contact()!=null&&message.replyToMessage().text().contains("Нажмите на кнопку оставить контакты для приюта собак")){
                        Contact contact = message.contact();
                        SendContact sendContact = new SendContact(dogsShelter.getChatId(), contact.phoneNumber(), contact.firstName());
                        telegramBot.execute(sendContact);
                    }
                }
            });
        } catch (
                Exception e) {
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
    //стартовое сообщение-приветствие и начальное меню
    public void startMessage(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "   Привет! Данный бот предоставляет информацию о двух приютах. Кошачий приют \""+TelegramBotUpdatesListener.catsShelter.getTitle()+"\"" +
                " и собачий приют \""+ TelegramBotUpdatesListener.dogsShelter.getTitle() +"\". Выберите один");
        sendMessage.parseMode(ParseMode.HTML);
        InlineKeyboardButton button1 = new InlineKeyboardButton("Кошки");
        button1.callbackData("Cats");
        InlineKeyboardButton button2 = new InlineKeyboardButton("Собаки");
        button2.callbackData("Dogs");
        Keyboard keyboard = new InlineKeyboardMarkup(button1, button2);
        sendMessage.replyMarkup(keyboard);
        telegramBot.execute(sendMessage);
    }
    //начальное меню без приветствия
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
    //главное меню приюта собак
    public void dogsShelter(long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, "Вы выбрали приют "+TelegramBotUpdatesListener.dogsShelter.getTitle());
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
    //меню Основная информация приюта собак
    public void infoDogs(long chatId){
        SendMessage sendMessage = new SendMessage(chatId, "Приют "+TelegramBotUpdatesListener.dogsShelter.getTitle());
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
    //отправка сообщения с кнопнкой главное меню и кнопкой назад из меню Основная информация приюта соба
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

    //сообщение с запросом контактов для приюта собак
    public void dogsShelterContact(long chatId){
        SendMessage sendMessage = new SendMessage(chatId, "Нажмите на кнопку оставить контакты для приюта собак");
        KeyboardButton keyboardButton =new KeyboardButton("Оставить контакты").requestContact(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButton).oneTimeKeyboard(true);
        sendMessage.replyMarkup(replyKeyboardMarkup);
        SendResponse sendResponse = telegramBot.execute(sendMessage);



    }

}

