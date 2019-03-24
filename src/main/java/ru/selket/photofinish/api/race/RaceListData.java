package ru.selket.photofinish.api.race;


import lombok.Data;

import java.util.List;

@Data
public class RaceListData {

    private List<Race> data;
    private RaceDataLinks links;
    private RaceDataMeta meta;
}
