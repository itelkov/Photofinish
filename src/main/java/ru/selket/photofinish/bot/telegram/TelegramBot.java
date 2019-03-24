package ru.selket.photofinish.bot.telegram;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.selket.photofinish.api.user.UserService;
import ru.selket.photofinish.bot.telegram.chat.TelegramChat;
import ru.selket.photofinish.bot.telegram.chat.TelegramChatService;
import ru.selket.photofinish.bot.telegram.command.Command;
import ru.selket.photofinish.bot.telegram.command.CommandCode;
import ru.selket.photofinish.bot.telegram.command.CommandSerializer;
import ru.selket.photofinish.bot.telegram.command.ICommandHandler;
import ru.selket.photofinish.bot.telegram.user.TelegramUser;
import ru.selket.photofinish.bot.telegram.user.TelegramUserService;
import ru.selket.photofinish.config.TelegramConfig;

import java.util.*;


@Log
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String handlerPrefix = "ru.selket.photofinish.bot.telegram.handler.TelegramBotCommandHandler";

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserService apiUserService;
    @Autowired
    private TelegramUserService userService;
    @Autowired
    private TelegramChatService chatService;

    @Override
    public String getBotUsername() {
        return context.getBean(TelegramConfig.class).getTelegramPhotoFinishName();
    }

    @Override
    public String getBotToken() {
        return context.getBean(TelegramConfig.class).getTelegramPhotoFinishToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.fine("--------------- Update " + update.getUpdateId() + " --------------");

        try {
            handle(update);

            /*if (update.hasMessage()){
                deletePreviousUpdate(update);
            }*/

            //sendApiMessages(messages);

            /*if (update.hasMessage()){
                saveUpdate(update);
            }*/
        } catch (RuntimeException th1) {
            th1.printStackTrace();
            //SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
            //sendMessage.setText(th1.getMessage());
            /*try {
                sendApiMethod(sendMessage);
            } catch (TelegramApiException tae) {
                tae.printStackTrace();
            } catch (Throwable th2) {
                th2.printStackTrace();
            }*/
        }
        finally {
            log.fine("--------------- End update " + update.getUpdateId() + " --------------");
        }
    }

    private void deleteApiMessage(DeleteMessage deleteMessage){
            try {
                if (deleteMessage != null){
                    log.fine("Message out: " + deleteMessage.getMethod());
                    sendApiMethod(deleteMessage);
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
    }

    private void handle(Update update){
        TelegramChat telegramChat = null;
        TelegramUser telegramUser = null;
        User user = getUser(update);
        Chat chat = getChat(update);

        if (user == null){
            return;
        }

        if (chat != null){
            telegramChat = chatService.get(String.valueOf(chat.getId()));

            if (telegramChat == null){
                telegramChat = new TelegramChat();
                telegramChat.setId(String.valueOf(chat.getId()));
                telegramChat.setUserId(String.valueOf(user.getId()));
                telegramChat.setCommand(null);
                chatService.set(telegramChat);
            }
        }
        else {
            telegramChat = chatService.getByUserId(String.valueOf(user.getId()));
        }

        telegramUser = userService.get(String.valueOf(user.getId()));

        if (telegramUser == null){
            ru.selket.photofinish.api.user.User apiUser = new ru.selket.photofinish.api.user.User();
            apiUser.setName(user.getUserName());
            apiUserService.set(apiUser);
            log.info("apiUser: " + apiUser);

            telegramUser = new TelegramUser();
            telegramUser.setId(String.valueOf(user.getId()));
            telegramUser.setUserId(apiUser.getId());
            telegramUser.setChatId(telegramChat != null ? telegramChat.getId() : null);
            telegramUser.setLang(user.getLanguageCode());
        }

        log.info("telegramUser: " + telegramUser);
        log.info("telegramChat: " + telegramChat);

        if (update.hasInlineQuery()){
            handleInlineQuery(update, null);
        }
        else if (update.hasChosenInlineQuery()){
            handleChosenInlineQuery(update, null);
        }
        else if (update.hasCallbackQuery()){
            handleCallbackQuery(update);
            telegramChat.setMessageId(String.valueOf(update.getCallbackQuery().getMessage().getMessageId()));
            telegramChat.setCommand(getCallbackQueryRequest(update));
            chatService.set(telegramChat);
        }
        else {
            if (update.hasMessage()){
                Command command = telegramChat.getCommand();

                if (command != null){
                    telegramChat.setCommand(null);
                    chatService.set(telegramChat);
                }

                if (update.getMessage().hasText() && command != null){
                    handleText(update, command);
                    //telegramRequest.getRequest().setParam(telegramRequest.getRequest().getParam() + ":" + update.getMessage().getText());
                    //chatService.delete(telegramRequest);
                }
                else {
                    sendGreetingMessages(update);
                }
            }
        }
    }

    private void handleInlineQuery(Update update, Command command){
        // if it's the first message - return greeting message
        if (command == null){
            sendGreetingMessages(update);
        }
    }

    private void handleChosenInlineQuery(Update update, Command command){
        sendGreetingMessages(update);
    }

    private void handleCallbackQuery(Update update){
        Command command = getCallbackQueryRequest(update);
        getHandler(command.getCode()).handle(update, command);
    }

    private Command getCallbackQueryRequest(Update update){
        return CommandSerializer.deserialize(update.getCallbackQuery().getData());
    }

    private void handleText(Update update, Command command){
        if (command == null){
            sendGreetingMessages(update);
        }
        else {
            getHandler(command.getCode()).handleText(update, command);
        }
    }

    private ICommandHandler getHandler(CommandCode command){
        try {
            String className = handlerPrefix + capitalize(underscoreToCamel(command.name()));
            log.fine("Handler: " + className + " for command: " + command.name());
            return (ICommandHandler)context.getBean(Class.forName(className));
        } catch (ClassNotFoundException e) {
            log.info(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public void sendGreetingMessages(Update update){
        getHandler(CommandCode.GREETING).handle(update,null);
    }

    public User getUser(Update update){
        User user = null;

        if (update.hasInlineQuery()){
            user = update.getInlineQuery().getFrom();
            log.info("hasInlineQuery: " + user);
        }
        else if (update.hasChosenInlineQuery()){
            user = update.getChosenInlineQuery().getFrom();
            log.info("hasChosenInlineQuery: " + user);
        }
        else if (update.hasCallbackQuery()){
            user = update.getCallbackQuery().getFrom();
            log.info("hasCallbackQuery: " + user);
        }
        else if (update.hasMessage()){
            user = update.getMessage().getFrom();
            log.info("hasMessage: " + user);
        }

        return user;
    }

    public Chat getChat(Update update){
        Chat chat = null;

        if (update.hasCallbackQuery()){
            chat = update.getCallbackQuery().getMessage().getChat();
        }
        else if (update.hasMessage()){
            chat = update.getMessage().getChat();
        }

        return chat;
    }

    public void sendApiMessages(List<BotApiMethod<Message>> sendMessages){
        sendMessages.forEach(this::sendApiMessage);
    }

    public void sendApiMessage(BotApiMethod<Message> message){
        try {
            if (message != null){
                log.fine("Message out: " + message.getMethod());
                sendApiMethod(message);
            }
        } catch (TelegramApiException e) {
            log.info(e.toString());
            e.printStackTrace();
        }
    }

    /*private static synchronized void saveUpdate(Update update){
        lastUpdates.put(update.getMessage().getChatId(), update);
    }

    private void deletePreviousUpdate(Update update){
            DeleteMessage deleteMessage = null;

            if (lastUpdates.containsKey(update.getMessage().getChatId())){
                Update previousUpdate = lastUpdates.get(update.getMessage().getChatId());
                deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(previousUpdate.getMessage().getChatId());
                deleteMessage.setMessageId(previousUpdate.getMessage().getMessageId());
                deleteApiMessage(deleteMessage);
                lastUpdates.remove(previousUpdate.getMessage().getChatId());
            }
    }*/

    private String underscoreToCamel(String underscore) {
        StringBuilder result = new StringBuilder();
        String[] val = underscore.split("_");

        for (int i = 0; i < val.length; i++) {
            String lower = val[i].toLowerCase();
            if (i > 0) {
                char c = Character.toUpperCase(lower.charAt(0));
                result.append(c);
                result.append(lower.substring(1));
            } else {
                result.append(lower);
            }
        }

        return result.toString();
    }

    private String capitalize(String str){
        if(str.length() == 1)
            return str.toUpperCase();

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
