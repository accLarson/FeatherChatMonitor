package dev.zerek.featherchatmonitor.listeners;

import com.earth2me.essentials.Essentials;
import dev.zerek.featherchatmonitor.FeatherChatMonitor;
import net.ess3.api.events.PrivateMessageSentEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PrivateMessageSentListener implements Listener {

    private final FeatherChatMonitor plugin;
    private final Essentials ess;
    private final Sound sound;
    private final float volume;
    private final float pitch;

    public PrivateMessageSentListener(FeatherChatMonitor plugin) {
        this.plugin = plugin;
        ess = (Essentials) plugin.getServer().getPluginManager().getPlugin("Essentials");

        sound = Sound.valueOf(plugin.getConfig().getString("ping.sound"));
        volume = (float) plugin.getConfig().getDouble("ping.volume");
        pitch = (float) plugin.getConfig().getDouble("ping.pitch");

    }

    @EventHandler
    public void onPrivateMessageSent(PrivateMessageSentEvent event) {
        Player recipient = Bukkit.getPlayer(event.getRecipient().getUUID());
        Player sender = Bukkit.getPlayer(event.getSender().getUUID());

        if (recipient == null || !recipient.isOnline()) return;

        if (!ess.getUser(recipient).isIgnoredPlayer(ess.getUser(sender))) recipient.playSound(recipient.getLocation(), sound, volume, pitch);
    }
}
