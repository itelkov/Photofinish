package ru.selket.photofinish.api.race;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import ru.selket.photofinish.config.MySqlJsonDateDeserializer;

import java.time.LocalDateTime;

@Data
public class Race {

    private long id;

    @JsonProperty("geo_id")
    private long geoId;

    private String title;

    @JsonProperty("date")
    @JsonDeserialize(using = MySqlJsonDateDeserializer.class)
    private LocalDateTime dateTime;

}
