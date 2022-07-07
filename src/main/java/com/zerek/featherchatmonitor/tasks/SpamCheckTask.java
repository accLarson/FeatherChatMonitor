package com.zerek.featherchatmonitor.tasks;

import com.zerek.featherchatmonitor.FeatherChatMonitor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class SpamCheckTask implements Runnable{

    private final FeatherChatMonitor plugin;
    Duration spamLimit;

    public SpamCheckTask(FeatherChatMonitor plugin) {
        this.plugin = plugin;
        spamLimit = Duration.ofSeconds(this.plugin.getConfig().getInt("spam-limit.messages.second-count"));
    }

    @Override
    public void run() {
        Set<Instant> expiredMessages = new HashSet<>();
        Set<Instant> expiredWarnings = new HashSet<>();
        plugin.getSpamManager().getMessages().keySet().forEach(instant -> {
            if (Duration.between(instant, Instant.now()).compareTo(spamLimit) > 0) expiredMessages.add(instant);
        });
        plugin.getSpamManager().getWarnings().keySet().forEach(instant -> {
            if (Duration.between(instant, Instant.now()).compareTo(spamLimit) > 0) expiredWarnings.add(instant);
        });
        expiredMessages.forEach(instant -> plugin.getSpamManager().removeMessage(instant));
        expiredWarnings.forEach(instant -> plugin.getSpamManager().removeWarning(instant));
    }
}
