package ru.selket.photofinish.bot.telegram;

import lombok.Getter;
import lombok.Setter;

public class BotException extends RuntimeException {

    @Getter @Setter
    protected String code;

    @Getter @Setter
    protected String message;

    @Getter @Setter
    protected String debug;

    public BotException(String message) {
        super(message);
    }

    public BotException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BotException(String code, String message, String debug) {
        super(message);
        this.code = code;
        this.message = message;
        this.debug = debug;
    }

    public BotException(String code, Throwable e, String debug) {
        super(e.getMessage());
        this.code = code;
        this.message = e.getMessage();
        this.debug = debug;
    }
}