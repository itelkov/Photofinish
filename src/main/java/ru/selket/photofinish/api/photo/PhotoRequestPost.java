package ru.selket.photofinish.api.photo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoRequestPost implements Serializable {

    @JsonProperty("race_id")
    private String raceId;
    @JsonProperty("number")
    private String memberId;
}
