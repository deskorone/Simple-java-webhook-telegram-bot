package com.api.testBot.bot;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class TgConf {

    @Value("${telegram.webhookpath}")
    private String webHookPath;

    @Value("${telegram.token")
    private String token;

    @Value("${telegram.username}")
    private String username;


}
