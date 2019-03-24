package ru.selket.photofinish.bot.telegram.photo;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.selket.photofinish.api.ApiClient;
import ru.selket.photofinish.api.geo.GeoService;
import ru.selket.photofinish.api.photo.*;
import ru.selket.photofinish.api.race.RaceData;
import ru.selket.photofinish.api.user.UserService;
import ru.selket.photofinish.bot.telegram.TelegramBot;
import ru.selket.photofinish.bot.telegram.chat.TelegramChatService;
import ru.selket.photofinish.bot.telegram.user.TelegramUser;
import ru.selket.photofinish.bot.telegram.user.TelegramUserService;

import java.time.format.DateTimeFormatter;

@Log
@Component
public class TelegramPhotoService implements PhotoNotifier {

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private ApiClient apiClient;
    @Autowired
    private GeoService geoService;
    @Autowired
    private PhotoRequestService photoRequestService;
    @Autowired
    private UserService userService;
    @Autowired
    private TelegramChatService telegramChatService;
    @Autowired
    private TelegramUserService telegramUserService;
    @Autowired
    private TelegramPhotoImageNoticeService telegramPhotoImageNoticeService;

    public void addNotice(PhotoRequest photoRequest, PhotoImage photoImage) {
        telegramPhotoImageNoticeService.set(photoRequest, photoImage);
    }

    public void removeNotice(PhotoRequest photoRequest, PhotoImage photoImage) {
        telegramPhotoImageNoticeService.delete(photoRequest);
    }

    @Scheduled(fixedDelay = 4 * 60 * 1000, initialDelay = 20 * 1000)
    public void send(){
        log.info("Telegram image addNotice");
        telegramPhotoImageNoticeService.get().forEach(telegramPhotoImageNotice -> {
            log.info("telegramPhotoImageNotice: " + telegramPhotoImageNotice);

            PhotoRequest photoRequest = photoRequestService.get(telegramPhotoImageNotice.getRequestId());
            log.info("photoRequest: " + photoRequest);

            if (photoRequest != null) {
                TelegramUser telegramUser = telegramUserService.getByUserId(photoRequest.getUserId());
                log.info("telegramUser: " + telegramUser);

                if (telegramUser != null) {
                    Long raceId = Long.parseLong(photoRequest.getRaceId());
                    RaceData raceData = apiClient.getRace(raceId);

                    if (telegramPhotoImageNotice.getImageId() != null && !telegramPhotoImageNotice.getImageId().isEmpty()){
                        PhotoImage photoImage = photoRequestService.getImage(telegramPhotoImageNotice.getImageId());
                        log.info("photoImage: " + photoImage);

                        if (photoImage != null){
                            String text = "Фотография участника №" + photoRequest.getMemberId()
                                    + " с соревнования \"" + raceData.getData().getTitle()
                                    + " (" +  geoService.getGeoName(raceData.getData().getGeoId())
                                    + ", " + raceData.getData().getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                    + ")\"\n"
                                    + "Купить фотографию: " + photoImage.getPageUrl();

                            try {
                                SendPhoto sendPhoto = new SendPhoto();
                                sendPhoto.setPhoto(photoImage.getUrl());
                                sendPhoto.setChatId(telegramUser.getChatId());
                                sendPhoto.setCaption(text);
                                telegramBot.execute(sendPhoto);
                            } catch (TelegramApiException e) {
                                log.info(e.toString());
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        String text = "Не найдено фотографий участника №" + photoRequest.getMemberId()
                                + " с соревнования \"" + raceData.getData().getTitle() + "\""
                                + ", " +  geoService.getGeoName(raceData.getData().getGeoId())
                                + ", " + raceData.getData().getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                + "";

                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText(text);
                        sendMessage.setChatId(telegramUser.getChatId());
                        telegramBot.sendApiMessage(sendMessage);
                    }

                    telegramPhotoImageNoticeService.delete(telegramPhotoImageNotice);
                }
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.warning(e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
