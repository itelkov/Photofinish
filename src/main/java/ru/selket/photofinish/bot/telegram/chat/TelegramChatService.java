package ru.selket.photofinish.bot.telegram.chat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TelegramChatService {

    @Autowired
    private TelegramChatRepository telegramChatRepository;

    public TelegramChat get(String chatId) {
        return telegramChatRepository.findById(chatId).orElse(null);
    }

    public TelegramChat getByUserId(String userId) {
        return telegramChatRepository.findByUserId(userId);
    }

    public void set(TelegramChat telegramChat) {
        telegramChatRepository.save(telegramChat);
    }

    public void delete(TelegramChat telegramChat) {
        telegramChatRepository.delete(telegramChat);
    }
}
