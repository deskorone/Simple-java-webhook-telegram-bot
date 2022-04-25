package com.api.testBot.bot.service;


import com.api.testBot.bot.BotState;
import com.api.testBot.bot.EventState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Service
public class MessageHandler {

    private final KeyboardBuilder keyboardBuilder;
    private final BotState botState;
    private final Handler handler;
    private final EventState eventState;

    @Autowired
    public MessageHandler(KeyboardBuilder keyboardBuilder, BotState botState, Handler handler, EventState eventState) {
        this.keyboardBuilder = keyboardBuilder;
        this.botState = botState;
        this.handler = handler;
        this.eventState = eventState;
    }

    public BotApiMethod<?> handleMessage(Update update) {
        Long chatId = update.getMessage().getChatId();
        State userState = botState.getBotStateMap().get(chatId);
        String event = eventState.getEventState().get(chatId);
        System.out.println(userState + "     " + chatId);
        if (update.getMessage().getText().equals("/get_rasp")) {
            botState.saveBotState(chatId, State.DEFAULT);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Выберите институт");
            sendMessage.setChatId(chatId.toString());
            if (event != null) {
                sendMessage.setReplyMarkup(keyboardBuilder.getKeyboard(chatId));
            } else {
                sendMessage.setReplyMarkup(keyboardBuilder.createMainKeyboard());
            }
            return sendMessage;
        }
        if (userState == State.DEFAULT || userState == null) {
            switch (update.getMessage().getText().trim()) {
                case ("/start"):
                    botState.saveBotState(chatId, State.DEFAULT);
                    return getKeyboard(chatId);

                case ("Ранее выбранная группа"):
                    if(event != null) {
                        botState.saveBotState(chatId, State.CHOISE);
                        return keyboardBuilder.getGroupInlineMessage(chatId);
                    }else {
                        return SendMessage.builder().text("Ранее выбранная группа не была найдена").chatId(chatId.toString()).build();
                    }
                case ("Институт электронной техники и приборостроения"):
                case ("Институт прикладных информационных технологий и коммуникаций"):
                case ("Институт энергетики"):
                case ("Институт социального и производственного менеджмента"):
                case ("Институт машиностроения, материаловедения и транспорта"):
                case ("Физико-технический институт"):
                case ("Институт урбанистики, архитектуры и строительства"):
                case ("Социально-экономический институт"):
                    botState.saveBotState(chatId, State.CHOSE_FACULT);
                    return handler.getFacultets(update.getMessage().getText(), chatId);
                default:
                    return SendMessage.builder()
                            .chatId(update.getMessage().getChatId().toString())
                            .text("Воспользуйтесь клавиатурой или командой!")
                            .build();
            }
        }else {
            return handler.hand(chatId, update, userState);
        }
    }

    public BotApiMethod<?> getKeyboard(Long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardBuilder.createMainKeyboard();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Выберите инстиут");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setChatId(chatId.toString());
        return sendMessage;
    }




}
