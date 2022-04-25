package com.api.testBot.config;


import com.api.testBot.bot.Bot;
import com.api.testBot.bot.BotState;
import com.api.testBot.bot.TgConf;
import com.api.testBot.bot.service.CallbackHandler;
import com.api.testBot.bot.service.MessageHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;


@Configuration
@AllArgsConstructor
public class Config {

    private final TgConf tgConf;

    private final MessageHandler messageHandler;

    private final CallbackHandler callbackHandler;

    @Bean
    public SetWebhook setWebhook(){
        return SetWebhook.builder().url(tgConf.getWebHookPath()).build();
    }

    @Bean
    public Bot bot(SetWebhook setWebhook){
        Bot bot = new Bot(setWebhook, messageHandler, callbackHandler);
        bot.setBotPath(tgConf.getWebHookPath());
        bot.setBotToken(tgConf.getToken());
        bot.setBotUsername(tgConf.getUsername());
        return bot;
    }


}
