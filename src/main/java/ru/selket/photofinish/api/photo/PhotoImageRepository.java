package ru.selket.photofinish.api.photo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhotoImageRepository extends CrudRepository<PhotoImage, String> {
    List<PhotoImage> findByRequestId(String requestId);
}
