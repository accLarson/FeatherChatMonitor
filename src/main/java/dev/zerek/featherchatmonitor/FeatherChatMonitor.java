package dev.zerek.featherchatmonitor;

import dev.zerek.featherchatmonitor.listeners.AsyncChatListener;
import dev.zerek.featherchatmonitor.listeners.PlayerSetSpawnListener;
import dev.zerek.featherchatmonitor.managers.DistinguishManager;
import dev.zerek.featherchatmonitor.managers.SpamManager;
import dev.zerek.featherchatmonitor.tasks.SpamCheckTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class FeatherChatMonitor extends JavaPlugin {

    private SpamManager spamManager;

    private DistinguishManager distinguishManager;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents(new AsyncChatListener(this),this);
        this.getServer().getPluginManager().registerEvents(new PlayerSetSpawnListener(),this);

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
