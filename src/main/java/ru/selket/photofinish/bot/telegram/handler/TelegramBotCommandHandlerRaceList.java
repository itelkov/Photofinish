package ru.selket.photofinish.bot.telegram.handler;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.selket.photofinish.api.ApiClient;
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
public class TelegramBotCommandHandlerRaceList extends TelegramBotHandlerBase implements ICommandHandler {

    @Autowired
    private ApiClient apiClient;

    private int racePerPage = 20;

    public void handle(Update update, Command command){
        String pageParam = command.getParam();
        log.info("pageParam: " + pageParam);
        Integer pageParamInt = pageParam == null || pageParam.isEmpty() ? 0 : Integer.parseInt(pageParam);
        int page = pageParamInt == 0 ? 1 : pageParamInt;
        RaceListData raceListData = apiClient.getRaceList(page);
        log.info("raceData: " + raceListData);
log.info("raceData.getMeta().getCurrentPage(): " + raceListData.getMeta().getCurrentPage());
        log.info("raceData.getMeta().getLastPage(): " + raceListData.getMeta().getLastPage());
        SendMessage sendMessage = new SendMessage();
        String text = "Полный список - страница " + raceListData.getMeta().getCurrentPage();
        sendMessage.setText(text);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtons;

        for(Race race : raceListData.getData()){
            inlineKeyboardButtons = new ArrayList<>();
            inlineKeyboardButtons.add(getInlineKeyboardButton(race.getTitle(), CommandCode.RACE, "" + race.getId()));
            keyboard.add(inlineKeyboardButtons);
        }

        inlineKeyboardButtons = new ArrayList<>();

        if (raceListData.getMeta().getCurrentPage() > 1){
            inlineKeyboardButtons.add(getInlineKeyboardButton("Назад", CommandCode.RACE_LIST, "" + (raceListData.getMeta().getCurrentPage() - 1)));
        }
        else {
            inlineKeyboardButtons.add(getInlineKeyboardButton("В начало", CommandCode.GREETING, ""));
        }

        if (raceListData.getMeta().getCurrentPage() < raceListData.getMeta().getLastPage()){
            inlineKeyboardButtons.add(getInlineKeyboardButton("Вперед", CommandCode.RACE_LIST, "" + (raceListData.getMeta().getCurrentPage() + 1)));
        }

        if (!inlineKeyboardButtons.isEmpty()){
            keyboard.add(inlineKeyboardButtons);
        }

        sendMessage.setReplyMarkup(new InlineKeyboardMarkup().setKeyboard(keyboard));
        sendMessage.setChatId(getChatId(update));
        telegramBot.sendApiMessage(sendMessage);
    }

}
