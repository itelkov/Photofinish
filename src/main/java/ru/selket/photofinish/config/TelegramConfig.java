package ru.selket.photofinish.config;

import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Log
@Configuration
public class TelegramConfig {

    private static final String LOG_TAG = "BOT";

    @Getter
    @Value("${telegram.bots.engym.name}")
    private String telegramEngymName;

    @Getter
    @Value("${telegram.bots.engym.token}")
    private String telegramEngymToken;

    @Getter
    @Value("${telegram.bots.entip.name}")
    private String telegramEntipName;

    @Getter
    @Value("${telegram.bots.entip.token}")
    private String telegramEntipToken;

    @Getter
    @Value("${telegram.bots.photo-finish.name}")
    private String telegramPhotoFinishName;

    @Getter
    @Value("${telegram.bots.photo-finish.token}")
    private String telegramPhotoFinishToken;


    @Value("${telegram.proxy.host}")
    private String proxyHost;
    @Value("${telegram.proxy.port}")
    private int proxyPort;
    @Value("${telegram.proxy.user}")
    private String proxyUser;
    @Value("${telegram.proxy.password}")
    private String proxyPassword;

}
