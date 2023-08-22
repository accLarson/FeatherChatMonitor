package dev.zerek.featherchatmonitor.listeners;

import com.destroystokyo.paper.event.player.PlayerSetSpawnEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerSetSpawnListener implements Listener {

    @EventHandler
    public void onPlayerSetSpawn(PlayerSetSpawnEvent event) {
        event.setNotification(Component.text("Beds do not set home on feather64. Use /sethome and /home."));
    }
}
