package com.zerek.featherchatmonitor;

import com.zerek.featherchatmonitor.listeners.AsyncChatListener;
import com.zerek.featherchatmonitor.managers.DistinguishManager;
import com.zerek.featherchatmonitor.managers.SpamManager;
import com.zerek.featherchatmonitor.tasks.SpamCheckTask;
import org.bukkit.plugin.java.JavaPlugin;
;

public final class FeatherChatMonitor extends JavaPlugin {

    private SpamManager spamManager;

    private DistinguishManager distinguishManager;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents(new AsyncChatListener(this),this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new SpamCheckTask(this),20L, 20L);

        this.spamManager = new SpamManager(this);

        this.distinguishManager = new DistinguishManager(this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public SpamManager getSpamManager() {
        return spamManager;
    }

    public DistinguishManager getDistinguishManager() {
        return distinguishManager;
    }
}
