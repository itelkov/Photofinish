package ru.selket.photofinish.api.photo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoRequestResponse implements Serializable {

    private Long id;
    private PhotoRequestStatus status;
    @JsonProperty("created_at")
    private String created;
    @JsonProperty("updated_at")
    private String updated;
    private List<PhotoRequestImage> images;
}
