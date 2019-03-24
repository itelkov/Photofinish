package ru.selket.photofinish.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import ru.selket.photofinish.api.photo.PhotoImageRepository;
import ru.selket.photofinish.api.photo.PhotoRequestRepository;
import ru.selket.photofinish.bot.facebook.photo.FacebookPhotoImageNoticeRepository;
import ru.selket.photofinish.bot.telegram.photo.TelegramPhotoImageNoticeRepository;

import javax.annotation.PostConstruct;


@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> rest = new RedisTemplate<byte[], byte[]>();
        rest.setConnectionFactory(redisConnectionFactory());
        return rest;
    }

}
