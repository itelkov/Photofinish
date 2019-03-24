package ru.selket.photofinish.bot.telegram.handler;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.selket.photofinish.bot.telegram.TelegramBot;
import ru.selket.photofinish.bot.telegram.command.Command;
import ru.selket.photofinish.bot.telegram.command.CommandCode;
import ru.selket.photofinish.bot.telegram.command.ICommandHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Log
@Component
public class TelegramBotCommandHandlerGreeting extends TelegramBotHandlerBase implements ICommandHandler {

    public void handle(Update update, Command command){
        SendMessage sendMessage = new SendMessage();
        String text = command == null ? "Добро пожаловать, "
                + telegramBot.getUser(update).getUserName()
                + "!"
                : " - ";
        sendMessage.setText(text);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        inlineKeyboardButtons.add(getInlineKeyboardButton("Полный список соревнований", CommandCode.RACE_LIST, null));
        keyboard.add(inlineKeyboardButtons);
        inlineKeyboardButtons = new ArrayList<>();
        inlineKeyboardButtons.add(getInlineKeyboardButton("Поиск по названию", CommandCode.RACE_FIND_BY_QUERY, null));
        keyboard.add(inlineKeyboardButtons);
        /*inlineKeyboardButtons = new ArrayList<>();
        inlineKeyboardButtons.add(getInlineKeyboardButton("Поиск по дате соревнования", RequestCommand.RACE_FIND_BY_YEAR, null));
        keyboard.add(inlineKeyboardButtons);
        inlineKeyboardButtons = new ArrayList<>();
        inlineKeyboardButtons.add(getInlineKeyboardButton("Поиск по городу", RequestCommand.RACE_FIND_BY_LOC, null));
        keyboard.add(inlineKeyboardButtons);*/
        sendMessage.setReplyMarkup(new InlineKeyboardMarkup().setKeyboard(keyboard));
        Long chatId = getChatId(update);
        sendMessage.setChatId(chatId);
        telegramBot.sendApiMessage(sendMessage);
    }

}
