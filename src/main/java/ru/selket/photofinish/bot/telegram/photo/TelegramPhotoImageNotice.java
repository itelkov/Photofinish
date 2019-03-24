package ru.selket.photofinish.bot.telegram.photo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import ru.selket.photofinish.api.photo.PhotoRequestStatus;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "${spring.redis.repo.telegram.photo.image.addNotice}", timeToLive = 7 * 24 * 3600 * 1000)
public class TelegramPhotoImageNotice implements Serializable {

    @Id
    private String id;
    @Indexed
    private String imageId;
    @Indexed
    private String requestId;
    private LocalDateTime requestDateTime;
}
