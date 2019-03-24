package ru.selket.photofinish.bot.telegram.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.selket.photofinish.bot.telegram.TelegramBot;
import ru.selket.photofinish.bot.telegram.chat.TelegramChat;
import ru.selket.photofinish.bot.telegram.chat.TelegramChatService;
import ru.selket.photofinish.bot.telegram.command.Command;
import ru.selket.photofinish.bot.telegram.command.CommandCode;
import ru.selket.photofinish.bot.telegram.command.CommandSerializer;
import ru.selket.photofinish.bot.telegram.user.TelegramUserService;

abstract class TelegramBotHandlerBase {

    @Autowired
    protected TelegramBot telegramBot;
    @Autowired
    protected TelegramUserService telegramUserService;
    @Autowired
    protected TelegramChatService telegramChatService;

    InlineKeyboardButton getInlineKeyboardButton(String title, CommandCode code, String param){
        Command command = new Command();
        command.setCode(code);
        command.setParam(param);
        return new InlineKeyboardButton()
                .setText(title)
                .setCallbackData(CommandSerializer.serialize(command));
    }

    protected Long getChatId(Update update){
        Chat chat = telegramBot.getChat(update);

        if (chat != null){
            return chat.getId();
        }

        User user = telegramBot.getUser(update);

        if (user != null){
            TelegramChat telegramChat = telegramChatService.getByUserId(String.valueOf(user.getId()));

            if (telegramChat != null){
                return Long.getLong(telegramChat.getId());
            }
        }

        return null;
    }

    public void handleText(Update update, Command command){

    }
}
