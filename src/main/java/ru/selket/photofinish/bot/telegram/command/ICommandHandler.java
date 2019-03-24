package ru.selket.photofinish.bot.telegram.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.selket.photofinish.bot.telegram.command.Command;

import java.util.stream.Stream;

public interface ICommandHandler {
    void handle(Update update, Command command);
    void handleText(Update update, Command command);
}
