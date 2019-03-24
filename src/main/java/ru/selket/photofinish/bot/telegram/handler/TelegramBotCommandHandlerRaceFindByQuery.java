package ru.selket.photofinish.bot.telegram.handler;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.selket.photofinish.api.ApiClient;
import ru.selket.photofinish.api.geo.GeoService;
import ru.selket.photofinish.api.race.Race;
import ru.selket.photofinish.api.race.RaceListData;
import ru.selket.photofinish.bot.telegram.command.Command;
import ru.selket.photofinish.bot.telegram.command.CommandCode;
import ru.selket.photofinish.bot.telegram.command.ICommandHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@Log
@Component
public class TelegramBotCommandHandlerRaceFindByQuery extends TelegramBotHandlerBase implements ICommandHandler {

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private GeoService getService;

    public void handle(Update update, Command command){
        SendMessage sendMessage = new SendMessage();
        String text = "Введите часть названия";
        sendMessage.setText(text);
        sendMessage.setChatId(getChatId(update));
        telegramBot.sendApiMessage(sendMessage);
    }

    public void handleText(Update update, Command command){
        RaceListData raceListData = apiClient.findRaceList(update.getMessage().getText());

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        SendMessage sendMessage = new SendMessage();

        if (raceListData.getData().isEmpty()){
            String text = "Ничего не найдено по запросу \""+update.getMessage().getText()+"\"";
            sendMessage.setText(text);
            inlineKeyboardButtons = new ArrayList<>();
            inlineKeyboardButtons.add(getInlineKeyboardButton("В начало", CommandCode.GREETING, null));
            keyboard.add(inlineKeyboardButtons);
        }
        else{
            sendMessage.setText(" - ");
            for(Race race : raceListData.getData()){
                inlineKeyboardButtons.add(getInlineKeyboardButton(race.getTitle(), CommandCode.RACE, "" + race.getId()));
                keyboard.add(inlineKeyboardButtons);
                inlineKeyboardButtons = new ArrayList<>();
            }
        }

        sendMessage.setReplyMarkup(new InlineKeyboardMarkup().setKeyboard(keyboard));
        sendMessage.setChatId(getChatId(update));
        telegramBot.sendApiMessage(sendMessage);
    }
}
