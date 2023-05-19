package com.devsteam.getname.telbot_shelterdc.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * конфигурация телеграм бота
 */
    @Configuration
    public class TelegramBotConfiguration {
        @Bean
        public TelegramBot telegramBot(@Value("${telegram.bot.token}") String token){
            return  new TelegramBot(token);
        }

    }
