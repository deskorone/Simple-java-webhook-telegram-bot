package com.api.testBot.controllers;

import com.api.testBot.bot.Bot;
import com.api.testBot.parser.ParserRasp;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@AllArgsConstructor
public class WebHookController {
    private final Bot writeReadBot;
    ParserRasp parserRasp;

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return writeReadBot.onWebhookUpdateReceived(update);
    }

}
