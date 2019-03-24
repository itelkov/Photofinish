package ru.selket.photofinish.bot.telegram.handler;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.selket.photofinish.api.ApiClient;
import ru.selket.photofinish.api.geo.GeoService;
import ru.selket.photofinish.api.photo.*;
import ru.selket.photofinish.api.race.RaceData;
import ru.selket.photofinish.api.user.User;
import ru.selket.photofinish.api.user.UserService;
import ru.selket.photofinish.bot.telegram.command.Command;
import ru.selket.photofinish.bot.telegram.command.ICommandHandler;
import ru.selket.photofinish.bot.telegram.user.TelegramUser;
import ru.selket.photofinish.bot.telegram.user.TelegramUserService;

import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;


@Log
@Component
public class TelegramBotCommandHandlerRace extends TelegramBotHandlerBase implements ICommandHandler {

    @Autowired
    private ApiClient apiClient;
    @Autowired
    private GeoService geoService;
    @Autowired
    private UserService userService;
    @Autowired
    private TelegramUserService telegramUserService;
    @Autowired
    private PhotoRequestService photoRequestService;

    @Autowired
    private TelegramBotCommandHandlerGreeting greeting;

    public void handle(Update update, Command command){
        String raceParam = command.getParam();
        Long raceParamInt = raceParam == null || raceParam.isEmpty() ? 0 : Long.parseLong(raceParam);
        long raceId = raceParamInt == 0 ? 1 : raceParamInt;
        RaceData raceData = apiClient.getRace(raceId);

        SendMessage sendMessage = new SendMessage();
        String text = raceData.getData().getTitle() + ", "
                + geoService.getGeoName(raceData.getData().getGeoId()) + ", "
                + raceData.getData().getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\n" +
                "\n" +
                "Чтобы подписаться на получение фотографий участника с этого соревнования, " +
                "введите номер данного участника.";

        sendMessage.setText(text);
        sendMessage.setChatId(getChatId(update));
        telegramBot.sendApiMessage(sendMessage);
    }

    public void handleText(Update update, Command command){
        PhotoRequest _photoRequest = new PhotoRequest();
        _photoRequest.setId("e8b97943-96af-40dc-a7fb-6c42a075d6d1");
        photoRequestService.delete(_photoRequest);

        User user;
        String raceId = command.getParam();
        String memberId = update.getMessage().getText().trim();
        TelegramUser telegramUser = telegramUserService.get(String.valueOf(update.getMessage().getFrom().getId()));

        if (telegramUser == null){
            user = new User();
            user.setName(update.getMessage().getFrom().getUserName());
            userService.set(user);

            telegramUser = new TelegramUser();
            telegramUser.setChatId(String.valueOf(update.getMessage().getChatId()));
            telegramUser.setLang(update.getMessage().getFrom().getLanguageCode());
            telegramUser.setUserId(user.getId());
            telegramUserService.set(telegramUser);
        }
        else {
            user = userService.get(telegramUser.getUserId());
        }

        if (!isNumeric(memberId)){
            greeting.handle(update, command);
        }
        else {
            PhotoRequest photoRequest = photoRequestService.get(user.getId(), raceId, memberId);
            String text;

            if (photoRequest != null){
                photoRequestService.rollback(photoRequest);
                text = "Вы снова подписались на фотографии участника под номером " + memberId + ".";
            }
            else {
                photoRequest = photoRequestService.add(user, raceId, memberId);

                if (photoRequest != null && photoRequest.getId() != null){
                    text = "Вы подписались на фотографии участника под номером " + memberId + ".";
                }
                else {
                    text = "Ошибка! Повторите попытку позже.";
                }
            }

            log.info("photoRequest: " + photoRequest);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(text);
            sendMessage.setChatId(getChatId(update));
            telegramBot.sendApiMessage(sendMessage);
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
