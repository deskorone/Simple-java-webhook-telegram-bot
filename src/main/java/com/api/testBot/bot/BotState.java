package com.api.testBot.bot;

import com.api.testBot.bot.service.State;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotState {

        private Map<Long, State> botStateMap = new HashMap<>();

        public void saveBotState(Long userId, State state) {
            botStateMap.put(userId, state);
        }

    public Map<Long, State> getBotStateMap() {
        return botStateMap;
    }
}
