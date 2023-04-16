package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.listener.TelegramBotUpdatesListener;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
//В данном классе описан функционал бота по отправке сообщений и их меню
@Service
public class TelegramBotService {

    private final TelegramBot telegramBot;

    public TelegramBotService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
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
        Keyboard keyboard = new InlineKeyboardMarkup().addRow(button1).addRow(button2).addRow(button3).addRow(
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
        Keyboard keyboard = new InlineKeyboardMarkup().addRow(button1).addRow(button2).addRow(button3).addRow(button4).addRow(button5).addRow(button6).addRow(
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
        SendMessage sendMessage = new SendMessage(chatId, "Нажмите на кнопку оставить контакты");
        KeyboardButton keyboardButton =new KeyboardButton("Оставить контакты").requestContact(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButton);
        sendMessage.replyMarkup(replyKeyboardMarkup);
        telegramBot.execute(sendMessage);
}}
