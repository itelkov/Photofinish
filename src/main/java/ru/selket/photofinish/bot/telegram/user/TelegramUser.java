package ru.selket.photofinish.bot.telegram.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash
public class TelegramUser implements Serializable {

    @Id
    private String id; // telegram bot api user id
    @Indexed
    private String userId;
    private String lang;
    private String chatId;
}
