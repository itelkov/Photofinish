package ru.selket.photofinish.api.geo;


import lombok.Data;
import ru.selket.photofinish.api.race.RaceDataLinks;

import java.util.List;

@Data
public class GeoData {

    private List<Geo> data;
    private List<?> links;
}
