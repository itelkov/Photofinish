package ru.selket.photofinish.api.race;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RaceDataMeta {

    private String path;

    @JsonProperty("current_page")
    private int currentPage;

    @JsonProperty("last_page")
    private int lastPage;

    @JsonProperty("per_page")
    private int perPage;

    private int from;
    private int to;
    private int total;
}
