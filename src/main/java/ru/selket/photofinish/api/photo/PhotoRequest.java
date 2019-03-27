package ru.selket.photofinish.api.photo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash
public class PhotoRequest implements Serializable {

    @Id
    private String id;
    @Indexed
    private String requestId;
    @Indexed
    private String userId;
    @Indexed
    private String raceId;
    @Indexed
    private String memberId;
    @Indexed
    private PhotoRequestStatus status;
    private LocalDateTime requestDateTime;
}
