package ru.selket.photofinish.api.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    User findByTelegramUserId(String telegramUserId);
    User findByFaceBookUserId(String faceBookUserId);
}
