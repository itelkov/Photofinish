package ru.selket.photofinish.bot.telegram.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.selket.photofinish.bot.telegram.chat.TelegramChat;


@Component
public class TelegramUserService {

    @Autowired
    private TelegramUserRepository userRepository;

    public TelegramUser get(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public TelegramUser getByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public void set(TelegramUser user) {
        userRepository.save(user);
    }

    public void delete(TelegramUser user) {
        userRepository.delete(user);
    }
}
