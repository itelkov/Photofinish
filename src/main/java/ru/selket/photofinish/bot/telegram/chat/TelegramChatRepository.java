package ru.selket.photofinish.bot.telegram.chat;

import org.springframework.data.repository.CrudRepository;


public interface TelegramChatRepository extends CrudRepository<TelegramChat, String> {
    TelegramChat findByUserId(String userId);
}
