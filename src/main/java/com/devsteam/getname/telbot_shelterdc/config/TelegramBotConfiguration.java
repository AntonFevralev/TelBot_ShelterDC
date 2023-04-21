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
    //конфигурация бота
    public class TelegramBotConfiguration {
        @Bean
        public TelegramBot tgBot(@Value("6217544357:AAElZG0Z-HzoYXe5V-vApdMNpLDtRKZ_0BI") String token){
            return  new TelegramBot(token);
        }

    }
