package ru.selket.photofinish.bot.telegram.command;


import com.fasterxml.jackson.annotation.JsonFormat;


@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum CommandCode {
    GREETING,
    RACE,
    RACE_LIST,
    RACE_LIST_NEXT,
    RACE_LIST_PREV,
    RACE_FIND_BY_YEAR,
    RACE_FIND_BY_QUERY,
    RACE_FIND_BY_DATE,
    RACE_FIND_BY_LOC,
    REQUEST
}
