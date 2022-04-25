package com.api.testBot.bot.service;


import com.api.testBot.bot.BotState;
import com.api.testBot.bot.EventState;
import com.api.testBot.parser.ParserRasp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
public class Handler {

    private final BotState botState;
    private final EventState eventState;
    private final ParserRasp parserRasp;
    private final KeyboardBuilder keyboardBuilder;

    @Autowired
    public Handler(BotState botState, EventState eventState, ParserRasp parserRasp, KeyboardBuilder keyboardBuilder) {
        this.botState = botState;
        this.eventState = eventState;
        this.parserRasp = parserRasp;
        this.keyboardBuilder = keyboardBuilder;
    }


    public BotApiMethod<?> hand(Long chatId, Update update, State state){
        String event = eventState.getEventState().get(chatId);

        switch (update.getMessage().getText()){
            case ("Назад к выбору института"):
                botState.saveBotState(chatId, State.DEFAULT);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Выберите институт");
                sendMessage.setChatId(chatId.toString());
                if(event != null) {
                    sendMessage.setReplyMarkup(keyboardBuilder.getKeyboard(chatId));
                }else {
                    sendMessage.setReplyMarkup(keyboardBuilder.createMainKeyboard());
                }
                return sendMessage;
        }
        switch (state){
            case CHOSE_FACULT:
                BotApiMethod botApiMethod = getGroups(update.getMessage().getText(), chatId);
                if(botApiMethod != null) {
                    botState.saveBotState(chatId, State.FIND);
                    return botApiMethod;
                }else {
                    return SendMessage.builder().text("Такого факультета нет(").chatId(chatId.toString()).build();
                }

            case FIND:
                eventState.saveEvent(chatId, update.getMessage().getText());
                botState.saveBotState(chatId, State.CHOISE);
                return keyboardBuilder.getGroupInlineMessage(chatId);
        }
        return null;

    }

    public BotApiMethod<?> getGroups(String name, Long chatId){
        List<String> groups = parserRasp.getGroupsByFacultret(name);
        if(groups.size() == 0){return null;}
        return keyboardBuilder.buildKeyboard(groups, chatId);
    }

    public BotApiMethod<?> getFacultets(String name, Long chatId) {
        List<String> f = new ArrayList<>(parserRasp.findFacultetsByInst(name));
        return keyboardBuilder.buildKeyboard(f, chatId);

    }
}
