package ru.selket.photofinish.bot.telegram.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import ru.selket.photofinish.bot.telegram.command.Command;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "${spring.redis.repo.telegram.chat}", timeToLive = 3600000L)
public class TelegramChat implements Serializable {

    @Id
    private String id;
    @Indexed
    private String userId;
    private String messageId;
    private Command command;
}
