package ru.selket.photofinish.api.photo;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.selket.photofinish.api.ApiClient;
import ru.selket.photofinish.api.user.User;
import ru.selket.photofinish.bot.facebook.photo.FacebookPhotoService;
import ru.selket.photofinish.bot.telegram.photo.TelegramPhotoService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Log
@Component
public class PhotoRequestService {

    @Autowired
    private PhotoRequestRepository photoRequestRepository;
    @Autowired
    private PhotoImageRepository photoImageRepository;
    @Autowired
    private ApiClient apiClient;
    private List<PhotoNotifier> photoNotifiers;

    @Autowired
    private TelegramPhotoService telegramPhotoService;
    @Autowired
    private FacebookPhotoService facebookPhotoService;

    @PostConstruct
    public void init(){
        photoNotifiers = new ArrayList<>();
        photoNotifiers.add(telegramPhotoService);
        photoNotifiers.add(facebookPhotoService);
    }

    public PhotoRequest get(String id) {
        return photoRequestRepository.findById(id).orElse(null);
    }

    public PhotoRequest get(String userId, String raceId, String memberId) {
        return photoRequestRepository.findByUserIdAndRaceIdAndMemberId(userId, raceId, memberId);
    }

    public PhotoRequest get(PhotoImage photoImage){
        photoImage = photoImageRepository.findById(photoImage.getId()).orElse(null);

        if (photoImage != null){
            return get(photoImage.getRequestId());
        }

        return null;
    }

    public void set(PhotoRequest photoRequest) {
        photoRequestRepository.save(photoRequest);
    }

    public void delete(PhotoRequest photoRequest) {
        photoRequestRepository.delete(photoRequest);
    }

    public PhotoRequest add(User user, String raceId, String memberId){
        PhotoRequest photoRequest = null;
        PhotoRequestPost photoRequestPost = new PhotoRequestPost();
        photoRequestPost.setRaceId(raceId);
        photoRequestPost.setMemberId(memberId);
        PhotoRequestResponse requestResponse = apiClient.setRequest(photoRequestPost);

        if (requestResponse != null && requestResponse.getId() != null){
            photoRequest = new PhotoRequest();
            photoRequest.setRequestId(String.valueOf(requestResponse.getId()));
            photoRequest.setUserId(user.getId());
            photoRequest.setRaceId(raceId);
            photoRequest.setMemberId(memberId);
            photoRequest.setStatus(PhotoRequestStatus.PROCESSING);
            photoRequest.setRequestDateTime(LocalDateTime.now());
            set(photoRequest);
        }

        return photoRequest;
    }

    @Scheduled(fixedDelay = 3 * 60 * 1000, initialDelay = 60 * 1000)
    public void check() {
        log.info("Photo image check");
        getPhotoRequests(PhotoRequestStatus.PROCESSING).forEach(photoRequest -> {
            log.info("photoRequest: " + photoRequest);
            PhotoRequestResponse requestResponse = apiClient.getRequest(Long.valueOf(photoRequest.getRequestId()));
            log.info("apiResponse: " + photoRequest);

            if (requestResponse != null && requestResponse.getStatus().equals(PhotoRequestStatus.READY)){
                if (requestResponse.getImages() != null && !requestResponse.getImages().isEmpty()){
                    requestResponse.getImages().forEach(photoRequestImage -> {
                        PhotoImage photoImage = new PhotoImage();
                        photoImage.setRequestId(photoRequest.getRequestId());
                        photoImage.setUrl(photoRequestImage.getUrl());
                        photoImage.setPageUrl(photoRequestImage.getPageUrl());
                        photoImageRepository.save(photoImage);

                        // addNotice api clients
                        photoNotifiers.forEach(photoNotifier -> photoNotifier.addNotice(photoRequest, photoImage));
                    });
                }
                else {
                    photoNotifiers.forEach(photoNotifier -> photoNotifier.addNotice(photoRequest, null));
                }

                photoRequest.setRequestDateTime(LocalDateTime.now());
                photoRequest.setStatus(PhotoRequestStatus.READY);
                photoRequestRepository.save(photoRequest);
            }
        });
    }

    public List<PhotoRequest> getPhotoRequests(PhotoRequestStatus status){
        return photoRequestRepository.findByStatus(status);
    }

    public void rollback(PhotoRequest photoRequest){
        deleteImages(photoRequest);
        photoRequest.setStatus(PhotoRequestStatus.PROCESSING);
        photoRequest.setRequestDateTime(LocalDateTime.now());
        set(photoRequest);
        deleteImages(photoRequest);
        photoNotifiers.forEach(photoNotifier -> photoNotifier.removeNotice(photoRequest, null));
    }

    public List<PhotoImage> getImages(PhotoRequest photoRequest){
        return photoImageRepository.findByRequestId(photoRequest.getId());
    }

    public PhotoImage getImage(String id){
        return photoImageRepository.findById(id).orElse(null);
    }

    private void deleteImages(PhotoRequest photoRequest){
        getImages(photoRequest).forEach(photoImage -> photoImageRepository.delete(photoImage));
    }
}
