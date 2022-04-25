package com.api.testBot.bot;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@Data
public class EventState {

    private final Map<Long, String> eventState = new HashMap<>();

    public void saveEvent(Long chatId, String group){
        eventState.put(chatId, group);
    }

}
