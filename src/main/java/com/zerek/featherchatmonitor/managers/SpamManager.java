package com.zerek.featherchatmonitor.managers;

import com.zerek.featherchatmonitor.FeatherChatMonitor;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class SpamManager {

    private final int spamMessageCount,spamWarningCount;


    private final Map<Instant, String> messages = new HashMap<>();

    private final Map<Instant, String> warnings = new HashMap<>();


    public SpamManager(FeatherChatMonitor plugin) {

        this.spamMessageCount = plugin.getConfig().getInt("spam-limit.messages.message-count");

        this.spamWarningCount = plugin.getConfig().getInt("spam-limit.warnings.message-count");
    }

    public Map<Instant, String> getMessages() {

        return messages;
    }

    public void addMessage(String playerName){

        messages.put(Instant.now(),playerName);
    }

    public void removeMessage(Instant instant){

        messages.remove(instant);
    }

    public boolean isSpam(String playerName){

        return Collections.frequency(messages.values(),playerName) >= spamMessageCount;
    }

    public Map<Instant, String> getWarnings() {

        return warnings;
    }

    public void addWarning(String playerName){

        warnings.put(Instant.now(),playerName);
    }

    public void removeWarning(Instant instant){

        warnings.remove(instant);
    }

    public boolean needsKick(String playerName){

        return Collections.frequency(warnings.values(),playerName) >= spamWarningCount;
    }
}
