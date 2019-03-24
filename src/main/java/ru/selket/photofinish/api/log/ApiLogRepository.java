package ru.selket.photofinish.api.log;

import org.springframework.data.repository.CrudRepository;
import ru.selket.photofinish.api.user.User;

public interface ApiLogRepository extends CrudRepository<ApiLog, String> {
}
