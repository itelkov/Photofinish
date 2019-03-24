package ru.selket.photofinish.bot.telegram.photo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface TelegramPhotoImageNoticeRepository extends CrudRepository<TelegramPhotoImageNotice, String> {
    public void deleteByRequestId(String requestId);
}
