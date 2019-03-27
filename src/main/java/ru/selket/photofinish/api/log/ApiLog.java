package ru.selket.photofinish.api.log;


import feign.Feign;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import ru.selket.photofinish.Application;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(timeToLive = 14 * 24 * 3600 * 1000)
public class ApiLog implements Serializable {

    @Id
    private String id;
    private Integer status;
    private String path;
    private String request;
    private String response;
    private LocalDateTime requestDateTime;
}
