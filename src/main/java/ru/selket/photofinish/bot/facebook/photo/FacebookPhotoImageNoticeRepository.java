package ru.selket.photofinish.bot.facebook.photo;

import org.springframework.data.repository.CrudRepository;


public interface FacebookPhotoImageNoticeRepository extends CrudRepository<FacebookPhotoImageNotice, String> {
    public void deleteByRequestId(String requestId);
}
