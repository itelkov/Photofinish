package ru.selket.photofinish.bot.telegram.photo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.selket.photofinish.api.photo.PhotoImage;
import ru.selket.photofinish.api.photo.PhotoRequest;

import java.time.LocalDateTime;
import java.util.List;


@Component
public class TelegramPhotoImageNoticeService {


    @Autowired
    private TelegramPhotoImageNoticeRepository telegramPhotoImageNoticeRepository;


    public TelegramPhotoImageNotice get(String chatId) {
        return telegramPhotoImageNoticeRepository.findById(chatId).orElse(null);
    }

    public Iterable<TelegramPhotoImageNotice> get() {
        return telegramPhotoImageNoticeRepository.findAll();
    }

    public void set(TelegramPhotoImageNotice telegramPhotoImageNotice) {
        telegramPhotoImageNoticeRepository.save(telegramPhotoImageNotice);
    }

    public void set(PhotoRequest photoRequest, PhotoImage photoImage) {
        TelegramPhotoImageNotice telegramPhotoImageNotice = new TelegramPhotoImageNotice();

        if(photoImage != null){
            telegramPhotoImageNotice.setImageId(photoImage.getId());
        }
        else {
            telegramPhotoImageNotice.setImageId(null);
        }

        telegramPhotoImageNotice.setRequestId(photoRequest.getId());
        telegramPhotoImageNotice.setRequestDateTime(LocalDateTime.now());
        telegramPhotoImageNoticeRepository.save(telegramPhotoImageNotice);
    }

    public void delete(TelegramPhotoImageNotice telegramPhotoImageNotice) {
        telegramPhotoImageNoticeRepository.delete(telegramPhotoImageNotice);
    }

    public void delete(PhotoRequest photoRequest) {
        telegramPhotoImageNoticeRepository.deleteByRequestId(photoRequest.getId());
    }

    public void delete(String id) {
        telegramPhotoImageNoticeRepository.deleteById(id);
    }
}
