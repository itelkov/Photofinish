package ru.selket.photofinish.api.photo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhotoRequestRepository extends CrudRepository<PhotoRequest, String> {
    PhotoRequest findByUserIdAndRaceIdAndMemberId(String userId, String raceId, String memberId);
    List<PhotoRequest> findByStatus(PhotoRequestStatus status);
}
