package ru.selket.photofinish.bot.telegram.user;

import org.springframework.data.repository.CrudRepository;
import ru.selket.photofinish.bot.telegram.chat.TelegramChat;

public interface TelegramUserRepository extends CrudRepository<TelegramUser, String> {
    TelegramUser findByUserId(String userId);
}
