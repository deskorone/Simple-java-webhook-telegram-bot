package com.api.testBot.bot.service;


import com.api.testBot.bot.BotState;
import com.api.testBot.bot.EventState;
import com.api.testBot.parser.ParserRasp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
public class CallbackHandler {

    private final BotState botState;
    private final ParserRasp parserRasp;
    private final EventState eventState;
    private final KeyboardBuilder keyboardBuilder;


    @Autowired
    public CallbackHandler(BotState botState, ParserRasp parserRasp, EventState eventState, KeyboardBuilder keyboardBuilder) {
        this.botState = botState;
        this.parserRasp = parserRasp;
        this.keyboardBuilder = keyboardBuilder;
        this.eventState = eventState;
    }


    public BotApiMethod<?> handCallBack(Update update){
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        State state = botState.getBotStateMap().get(chatId);
        String group = eventState.getEventState().get(chatId);
        if(state == State.CHOISE && group != null) {
            SendMessage sendMessage = new SendMessage();
            switch (update.getCallbackQuery().getData()) {
                case ("group2day"):
                    sendMessage.setReplyMarkup(keyboardBuilder.getKeyboard(chatId));
                    sendMessage.setChatId(chatId.toString());
                    sendMessage.setParseMode(ParseMode.HTML);
                    String str = getRasp(group, true);
                    if(str != null){
                        sendMessage.setText(str);
                    }else {
                        sendMessage.setText(String.format("Расписание группы %s не найдено (", group));
                    }
                    botState.saveBotState(chatId, State.DEFAULT);
                    return sendMessage;
                case ("group7day"):
                    sendMessage.setReplyMarkup(keyboardBuilder.getKeyboard(chatId));
                    sendMessage.setChatId(chatId.toString());
                    sendMessage.setParseMode(ParseMode.HTML);
                    String strSeven = getRasp(group, false);
                    if(strSeven != null){
                        sendMessage.setText(strSeven);
                    }else {
                        sendMessage.setText(String.format("Расписание группы %s не найдено(", group));
                    }
                    botState.saveBotState(chatId, State.DEFAULT);
                    return sendMessage;
                default:
                    return SendMessage.builder().text("Произошла ошибка").chatId(chatId.toString()).build();
            }
        }else {
            botState.saveBotState(chatId, State.DEFAULT);
            return SendMessage.builder().text("Кнопка не работает(").chatId(chatId.toString()).replyMarkup(keyboardBuilder.createMainKeyboard()).build();
        }
    }


    public String getRasp(String name, boolean isTwo){
        StringBuilder stringBuilder = new StringBuilder();
        List<String> list = parserRasp.findByGroup(name, isTwo);
        if(list != null){
            list.forEach(stringBuilder::append);
        }else {
            return null;
        }
        return stringBuilder.toString();
    }


}
