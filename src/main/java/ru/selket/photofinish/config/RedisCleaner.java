package ru.selket.photofinish.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.selket.photofinish.api.photo.PhotoImageRepository;
import ru.selket.photofinish.api.photo.PhotoRequestRepository;
import ru.selket.photofinish.bot.facebook.photo.FacebookPhotoImageNoticeRepository;
import ru.selket.photofinish.bot.telegram.photo.TelegramPhotoImageNoticeRepository;

import javax.annotation.PostConstruct;

@Component
public class RedisCleaner {

    @Autowired
    private PhotoRequestRepository photoRequestRepository;
    @Autowired
    private PhotoImageRepository photoImageRepository;
    @Autowired
    private TelegramPhotoImageNoticeRepository telegramPhotoImageNoticeRepository;
    @Autowired
    private FacebookPhotoImageNoticeRepository facebookPhotoImageNoticeRepository;


    @PostConstruct
    private void clear(){
        /*telegramPhotoImageNoticeRepository.deleteAll();
        facebookPhotoImageNoticeRepository.deleteAll();
        photoImageRepository.deleteAll();
        photoRequestRepository.deleteAll();*/
    }
}
