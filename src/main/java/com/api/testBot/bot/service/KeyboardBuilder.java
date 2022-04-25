package com.api.testBot.bot.service;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardBuilder {




    public ReplyKeyboardMarkup createMainKeyboard() {

        KeyboardRow keyboardButtons1 = new KeyboardRow();
        keyboardButtons1.add(new KeyboardButton("Институт машиностроения, материаловедения и транспорта"));
        keyboardButtons1.add(new KeyboardButton("Институт прикладных информационных технологий и коммуникаций"));

        KeyboardRow keyboardButtons2 = new KeyboardRow();
        keyboardButtons2.add(new KeyboardButton("Институт электронной техники и приборостроения"));
        keyboardButtons2.add(new KeyboardButton("Институт энергетики"));

        KeyboardRow keyboardButtons3 = new KeyboardRow();
        keyboardButtons3.add(new KeyboardButton("Институт социального и производственного менеджмента"));
        keyboardButtons3.add(new KeyboardButton("Физико-технический институт"));

        KeyboardRow keyboardButtons4 = new KeyboardRow();
        keyboardButtons4.add(new KeyboardButton("Социально-экономический институт"));
        keyboardButtons4.add(new KeyboardButton("Институт урбанистики, архитектуры и строительства"));

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardButtons1);
        keyboardRows.add(keyboardButtons2);
        keyboardRows.add(keyboardButtons3);
        keyboardRows.add(keyboardButtons4);

        return ReplyKeyboardMarkup.builder().keyboard(keyboardRows).oneTimeKeyboard(false).resizeKeyboard(true).selective(true).build();

    }

    public ReplyKeyboardMarkup getKeyboard(Long chatId) {

        //проверку на нул сделать надо
        KeyboardRow keyboardButtons0 = new KeyboardRow();
        keyboardButtons0.add(new KeyboardButton("Ранее выбранная группа"));

        KeyboardRow keyboardButtons1 = new KeyboardRow();
        keyboardButtons1.add(new KeyboardButton("Институт машиностроения, материаловедения и транспорта"));
        keyboardButtons1.add(new KeyboardButton("Институт прикладных информационных технологий и коммуникаций"));

        KeyboardRow keyboardButtons2 = new KeyboardRow();
        keyboardButtons2.add(new KeyboardButton("Институт электронной техники и приборостроения"));
        keyboardButtons2.add(new KeyboardButton("Институт энергетики"));

        KeyboardRow keyboardButtons3 = new KeyboardRow();
        keyboardButtons3.add(new KeyboardButton("Институт социального и производственного менеджмента"));
        keyboardButtons3.add(new KeyboardButton("Физико-технический институт"));

        KeyboardRow keyboardButtons4 = new KeyboardRow();
        keyboardButtons4.add(new KeyboardButton("Институт урбанистики, архитектуры и строительства"));
        keyboardButtons4.add(new KeyboardButton("Социально-экономический институт"));

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        keyboardRows.add(keyboardButtons0);
        keyboardRows.add(keyboardButtons1);
        keyboardRows.add(keyboardButtons2);
        keyboardRows.add(keyboardButtons3);
        keyboardRows.add(keyboardButtons4);


        return ReplyKeyboardMarkup.builder().keyboard(keyboardRows).oneTimeKeyboard(false).resizeKeyboard(true).selective(true).build();
    }


    public BotApiMethod<?> getGroupInlineMessage(Long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> inlineKeyboardButtonsRow = new ArrayList<>();
        inlineKeyboardButtonsRow.add(InlineKeyboardButton.builder().text("На 2 дня").callbackData("group2day").build());
        inlineKeyboardButtonsRow.add(InlineKeyboardButton.builder().text("На 7 дней").callbackData("group7day").build());
        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        list.add(inlineKeyboardButtonsRow);
        inlineKeyboardMarkup.setKeyboard(list);
        return SendMessage.builder().text("Выберите колличество дней").replyMarkup(inlineKeyboardMarkup).chatId(chatId.toString()).build();

    }

    public BotApiMethod<?> buildKeyboard(List<String> f, Long chatId) {
        List<KeyboardRow> list = new ArrayList<>();
        for (int i = 0; i < f.size() - 1; i++) {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(KeyboardButton.builder().text(f.get(i)).build());
            keyboardRow.add(KeyboardButton.builder().text(f.get(i + 1)).build());
            list.add(keyboardRow);
            i=i+1;
        }
        KeyboardRow keyboardRow = new KeyboardRow();
        if(f.size() % 2 != 0) {
            keyboardRow.add(KeyboardButton.builder().text(f.get(f.size()-1)).build());
        }
        keyboardRow.add(KeyboardButton.builder().text("Назад к выбору института").build());
        list.add(keyboardRow);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setReplyMarkup(ReplyKeyboardMarkup.builder().keyboard(list).oneTimeKeyboard(true).resizeKeyboard(true).selective(true).build());
        sendMessage.setText("Воспользуйтесь клавиатурой");
        return sendMessage;

    }

}

