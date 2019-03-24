package ru.selket.photofinish.api.log;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ApiLogService {

    @Autowired
    private ApiLogRepository apiLogRepository;

    public ApiLog get(String id) {
        return apiLogRepository.findById(id).orElse(null);
    }

    public void set(ApiLog apiLog) {
        apiLogRepository.save(apiLog);
    }

    public void delete(ApiLog apiLog) {
        apiLogRepository.delete(apiLog);
    }
}
