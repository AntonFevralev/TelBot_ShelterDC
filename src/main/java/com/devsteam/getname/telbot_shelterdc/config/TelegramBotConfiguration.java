package com.devsteam.getname.telbot_shelterdc.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

    @Configuration
    //конфигурация бота
    public class TelegramBotConfiguration {
        @Bean
        public TelegramBot tgBot(@Value("${telegram.bot.token}") String token){
            return  new TelegramBot(token);
        }

    }
