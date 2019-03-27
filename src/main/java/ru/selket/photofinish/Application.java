package ru.selket.photofinish;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.selket.photofinish.bot.telegram.TelegramBot;


@SpringBootApplication
@EnableScheduling
public class Application {

    public static ApplicationContext applicationContext;

    @Value("${telegram.proxy.host}")
    private String proxyHost;
    @Value("${telegram.proxy.port}")
    private int proxyPort;
    @Value("${telegram.proxy.user}")
    private String proxyUser;
    @Value("${telegram.proxy.password}")
    private String proxyPassword;

    public static void main(String[] args){
        ApiContextInitializer.init();

        SpringApplication app = new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setAdditionalProfiles(Environment.getProfile());
        app.setWebApplicationType(WebApplicationType.NONE);
        ConfigurableApplicationContext ctx = app.run(args);
        applicationContext = ctx.getParent();
        initTelegramBot(ctx);
    }

    private static void initTelegramBot(ApplicationContext applicationContext){
        // telegram bot logger
        /*BotLogger.setLevel(Level.ALL);
        BotLogger.registerLogger(new ConsoleHandler());
        try {
            BotLogger.registerLogger(new BotsFileHandler());
        } catch (IOException e) {
            BotLogger.severe(LOG_TAG, e);
        }*/

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            TelegramBot telegramBot = applicationContext.getBean(TelegramBot.class);
            botsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            //BotLogger.error(LOG_TAG, e);
        }
    }

}