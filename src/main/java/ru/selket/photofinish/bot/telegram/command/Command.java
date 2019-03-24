package ru.selket.photofinish.bot.telegram.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


public class Command {

    @Getter
    @Setter
    @JsonProperty("c")
    private CommandCode code;

    @Getter
    @Setter
    @JsonProperty("p")
    private String param;
}
