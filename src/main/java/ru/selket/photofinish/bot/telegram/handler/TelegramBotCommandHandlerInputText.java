package ru.selket.photofinish.bot.telegram.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.selket.photofinish.bot.telegram.command.Command;
import ru.selket.photofinish.bot.telegram.command.ICommandHandler;

import java.util.stream.Stream;

public class TelegramBotCommandHandlerInputText extends TelegramBotHandlerBase implements ICommandHandler {

    public void handle(Update update, Command command){
    }
}
