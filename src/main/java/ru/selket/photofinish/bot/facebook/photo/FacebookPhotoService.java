package ru.selket.photofinish.bot.facebook.photo;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.selket.photofinish.api.photo.*;


@Log
@Component
public class FacebookPhotoService implements PhotoNotifier {

    @Autowired
    private PhotoRequestService photoRequestService;
    @Autowired
    private FacebookPhotoImageNoticeService facebookPhotoImageNoticeService;

    public void addNotice(PhotoRequest photoRequest, PhotoImage photoImage) {
        facebookPhotoImageNoticeService.set(photoRequest, photoImage);
    }

    public void removeNotice(PhotoRequest photoRequest, PhotoImage photoImage) {
        facebookPhotoImageNoticeService.delete(photoRequest);
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 25 * 1000)
    public void check() {
        log.info("Facebook image addNotice");

    }
}
