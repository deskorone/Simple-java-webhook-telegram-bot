package com.api.testBot.bot;

import com.api.testBot.bot.service.CallbackHandler;
import com.api.testBot.bot.service.MessageHandler;
import com.api.testBot.bot.service.State;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;


import java.io.IOException;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bot extends SpringWebhookBot {
    String botPath;
    String botUsername;
    String botToken;
    State state = State.DEFAULT;


    @Autowired
    final MessageHandler messageHandler;

    @Autowired
    final CallbackHandler callbackHandler;



    @Autowired
    public Bot(SetWebhook setWebhook, MessageHandler message, CallbackHandler callbackHandler) {
        super(setWebhook);
        this.messageHandler = message;
        this.callbackHandler = callbackHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                if(update.getMessage().getText() != null) {
                    return messageHandler.handleMessage(update);
                }
                return null;
            }else {
                return callbackHandler.handCallBack(update);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private BotApiMethod<?> handleUpdate(Update update) throws IOException {
        return SendMessage.builder().text("Выберите инфтитут").chatId(update.getMessage().getChatId().toString()).build();
    }
}