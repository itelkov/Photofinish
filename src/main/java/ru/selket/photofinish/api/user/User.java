package ru.selket.photofinish.api.user;


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
@RedisHash("${spring.redis.repo.api.user}")
public class User implements Serializable {

    @Id
    private String id;
    @Indexed
    private String token;
    @Indexed
    private String email;
    private String name;
}
