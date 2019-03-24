package ru.selket.photofinish.bot.facebook.photo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.selket.photofinish.api.photo.PhotoImage;
import ru.selket.photofinish.api.photo.PhotoRequest;
import ru.selket.photofinish.bot.telegram.photo.TelegramPhotoImageNotice;

import java.time.LocalDateTime;


@Component
public class FacebookPhotoImageNoticeService {


    @Autowired
    private FacebookPhotoImageNoticeRepository facebookPhotoImageNoticeRepository;


    public FacebookPhotoImageNotice get(String chatId) {
        return facebookPhotoImageNoticeRepository.findById(chatId).orElse(null);
    }

    public void set(FacebookPhotoImageNotice facebookPhotoImageNotice) {
        facebookPhotoImageNoticeRepository.save(facebookPhotoImageNotice);
    }

    public void set(PhotoRequest photoRequest, PhotoImage photoImage) {
        FacebookPhotoImageNotice facebookPhotoImageNotice = new FacebookPhotoImageNotice();

        if(photoImage != null){
            facebookPhotoImageNotice.setImageId(photoImage.getId());
        }

        facebookPhotoImageNotice.setRequestDateTime(LocalDateTime.now());
        facebookPhotoImageNoticeRepository.save(facebookPhotoImageNotice);
    }

    public void delete(FacebookPhotoImageNotice facebookPhotoImageNotice) {
        facebookPhotoImageNoticeRepository.delete(facebookPhotoImageNotice);
    }

    public void delete(PhotoRequest photoRequest) {
        facebookPhotoImageNoticeRepository.deleteByRequestId(photoRequest.getId());
    }
}
