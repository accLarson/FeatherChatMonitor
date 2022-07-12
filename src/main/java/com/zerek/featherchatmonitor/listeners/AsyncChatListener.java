package com.zerek.featherchatmonitor.listeners;

import com.zerek.featherchatmonitor.FeatherChatMonitor;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.Locale;

public class AsyncChatListener implements Listener {

    private final FeatherChatMonitor plugin;
    private final Sound sound;
    private final float volume, pitch;
    private final String spamMessage, kickMessage;


    public AsyncChatListener(FeatherChatMonitor plugin) {
        this.plugin = plugin;
        sound = Sound.valueOf(plugin.getConfig().getString("ping.sound"));
        volume = (float) plugin.getConfig().getDouble("ping.volume");
        pitch = (float) plugin.getConfig().getDouble("ping.pitch");
        spamMessage = plugin.getConfig().getString("spam-limit.warnings.warning");
        kickMessage = plugin.getConfig().getString("spam-limit.warnings.kick-message");
    }

    @EventHandler
    public void onAsyncChatEvent(AsyncChatEvent event){
        if (!event.getPlayer().hasPermission("feather.spam.bypass") && plugin.getSpamManager().needsKick(event.getPlayer().getName())){
            plugin.getServer().getScheduler().runTask(plugin, () -> event.getPlayer().kick(MiniMessage.miniMessage().deserialize(kickMessage)));
        }
        if (!event.getPlayer().hasPermission("feather.spam.bypass") && plugin.getSpamManager().isSpam(event.getPlayer().getName())) {
            event.setCancelled(true);
            plugin.getSpamManager().addWarning(event.getPlayer().getName());
            event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize(spamMessage));
            plugin.getLogger().info("Spam detected from: " + event.getPlayer().getName() + "| Message suppressed: " + PlainTextComponentSerializer.plainText().serialize(event.message()));
        }
        else {
            plugin.getSpamManager().addMessage(event.getPlayer().getName());
            String message = PlainTextComponentSerializer.plainText().serialize(event.message());
            message = message.replace(".","");
            message = message.replace(",","");
            message = message.replace("\"","");
            message = message.replace("!","");
            message = message.replace("?","");
            message = message.replace("(","");
            message = message.replace(")","");
            message = message.toLowerCase();
            String finalMessage = message;
            plugin.getServer().getOnlinePlayers().forEach(player -> Arrays.stream(finalMessage.split(" ")).filter(s -> s.equals(player.getName().toLowerCase())).forEach(s -> player.playSound(player.getLocation(), sound, volume, pitch)));
        }

    }
}
