package ru.selket.photofinish.api.photo;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class PhotoImage implements Serializable {

    @Id
    private String id;
    @Indexed
    private String requestId;
    private String url;
    private String pageUrl;
}
