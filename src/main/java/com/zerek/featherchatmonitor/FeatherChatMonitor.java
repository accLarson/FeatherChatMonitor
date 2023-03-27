package com.zerek.featherchatmonitor;

import com.zerek.featherchatmonitor.listeners.AsyncChatListener;
import com.zerek.featherchatmonitor.managers.SpamManager;
import com.zerek.featherchatmonitor.tasks.SpamCheckTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class FeatherChatMonitor extends JavaPlugin {

    private SpamManager spamManager;

    private final Map<String,String> distinguishColors = new HashMap<>();

    private String distinguishTag = "";

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents(new AsyncChatListener(this),this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, new SpamCheckTask(this),20L, 20L);

        this.spamManager = new SpamManager(this);

        getConfig().getConfigurationSection("distinguish.colors").getKeys(false).forEach(role -> distinguishColors.put(role,getConfig().getString("distinguish.colors." + role)));

        distinguishTag = getConfig().getString("distinguish.tag");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public SpamManager getSpamManager() {
        return spamManager;
    }

    public Map<String, String> getDistinguishColors() {
        return distinguishColors;
    }

    public String getDistinguishTag() {
        return distinguishTag;
    }
}
